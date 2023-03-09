/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceServiceProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceService;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceServiceProperties;
import org.odpi.openmetadata.adminservices.client.EngineHostConfigurationClient;
import org.odpi.openmetadata.adminservices.client.IntegrationDaemonConfigurationClient;
import org.odpi.openmetadata.adminservices.client.MetadataAccessPointConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.client.ViewServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.platformservices.properties.ServerStatus;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;
import org.odpi.openmetadata.repositoryservices.clients.AuditLogServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataHighwayServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoExceptionHandler;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoViewErrorCode;
import org.odpi.openmetadata.viewservices.dino.api.ffdc.DinoViewServiceException;
import org.odpi.openmetadata.viewservices.dino.api.properties.DinoServerInstance;
import org.odpi.openmetadata.viewservices.dino.api.properties.EngineDetails;
import org.odpi.openmetadata.viewservices.dino.api.properties.PlatformOverview;
import org.odpi.openmetadata.viewservices.dino.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServerCohortDetails;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServerOverview;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServiceDetails;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServicePropertiesAndRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The DinoViewHandler is initialised with the configured resource endpoints.
 * The handler exposes methods for functionality for the dino view
 */
public class DinoViewHandler {
    private static final Logger log = LoggerFactory.getLogger(DinoViewHandler.class);

    /*
     * viewServiceOptions should have been validated in the Admin layer.
     * The viewServiceOptions contains a list of resource endpoints that the
     * view service can connect to. It is formatted like this:
     *
     *  "resourceEndpoints" : [
     *            {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Platform",
     *               "platformName"       : "Platform1",
     *               "platformRootURL"    : "https://localhost:8082",
     *               "description"        : "Egeria deployment on local development server"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Platform",
     *               "platformName"       : "Platform2",
     *               "platformRootURL"    : "https://localhost:9443",
     *               "description"        : "Egeria deployment on local test server"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Server",
     *               "serverName"         : "Metadata_Server",
     *               "serverInstanceName" : "Metadata Server 1",
     *               "platformName"       : "Platform1",
     *               "description"        : "Metadata server for development testing"
     *           },
     *           {
     *               "class"              : "ResourceEndpointConfig",
     *               "resourceCategory"   : "Server",
     *               "serverName"         : "Metadata_Server2",
     *               "serverInstanceName" : "Metadata Server 2",
     *               "platformName"       : "Platform2",
     *               "description"        : "Metadata server used as home for test artefacts"
     *           }
     *       ]
     */
    private Map<String, ResourceEndpoint> configuredPlatforms = null;  // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint> configuredServerInstances = null;  // map is keyed using serverName+platformRootURL so each instance is unique

    /**
     * Default constructor for DinoViewHandler
     */
    public DinoViewHandler()
    {

    }

    /**
     * Constructor for DinoViewHandler with configured resourceEndpoints
     *
     * @param resourceEndpoints - list of resource endpoint configuration objects for this view service
     */
    public DinoViewHandler(List<ResourceEndpointConfig> resourceEndpoints)
    {

        /*
         * Populate map of resources with their endpoints....
         */

        // TODO - It would be desirable to add validation rules to ensure uniqueness etc.

        configuredPlatforms = new HashMap<>();
        configuredServerInstances = new HashMap<>();

        if (resourceEndpoints != null && !resourceEndpoints.isEmpty())
        {

            resourceEndpoints.forEach(res -> {

                String resCategory = res.getResourceCategory();
                ResourceEndpoint rep = new ResourceEndpoint(res);

                String resName;

                switch (resCategory)
                {
                    case "Platform":
                        resName = res.getPlatformName();
                        configuredPlatforms.put(resName, rep);
                        break;

                    case "Server":
                        resName = res.getServerInstanceName();
                        configuredServerInstances.put(resName, rep);
                        break;

                    default:
                        // Unsupported category is ignored
                        break;

                }
            });
        }
    }

    /**
     * getResourceEndpoints - returns a list of the configured resource endpoints. Does not include discovered resource endpoints.
     *
     * @param userId     userId under which the request is performed
     * @param methodName The name of the method being invoked
     * @return The resource endpoints that have been configured for the view service
     */
    public Map<String, List<ResourceEndpoint>> getResourceEndpoints(String userId,
                                                                    String methodName)

    {
        Map<String, List<ResourceEndpoint>> returnMap = new HashMap<>();

        List<ResourceEndpoint> platformList = null;
        List<ResourceEndpoint> serverList = null;

        if (!configuredPlatforms.isEmpty())
        {
            platformList = new ArrayList<>();
            platformList.addAll(configuredPlatforms.values());
        }

        if (!configuredServerInstances.isEmpty())
        {
            serverList = new ArrayList<>();
            serverList.addAll(configuredServerInstances.values());
        }

        returnMap.put("platformList", platformList);
        returnMap.put("serverList", serverList);

        return returnMap;
    }


    /**
     * resolvePlatformRootURL
     * <p>
     * This method will look up the configured root URL for the named platform.
     *
     * @param platformName - the name of the platform to resolve
     * @param methodName   - the name of the calling method
     * @return resolved platform URL Root
     * <p>
     * Exceptions
     * @throws DinoViewServiceException an error was detected and details are reported in the exception
     */
    private String resolvePlatformRootURL(String platformName,
                                          String methodName)
    throws DinoViewServiceException

    {
        String platformRootURL = null;

        if (platformName != null)
        {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null)
            {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null)
        {
            throw new DinoViewServiceException(DinoViewErrorCode.VIEW_SERVICE_NULL_PLATFORM_NAME.getMessageDefinition(),
                                               this.getClass().getName(),
                                               methodName);
        }

        return platformRootURL;
    }


    /**
     * getPlatformServicesClient
     * <p>
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param platformName    - name of the platform to connect to
     * @param platformRootURL - the root URL to connect to the platform
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private PlatformServicesClient getPlatformServicesClient(String platformName,
                                                             String platformRootURL)
    throws DinoViewServiceException

    {
        String methodName = "getOMAGServerConfigurationClient";

        try
        {
            return new PlatformServicesClient(platformName, platformRootURL);
        }
        catch (InvalidParameterException e)

        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }

    /**
     * getOMAGServerConfigurationClient
     * <p>
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param userId        - name of the user performing the operation
     * @param serverName    - name of the server to connect to
     * @param serverRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private OMAGServerConfigurationClient getOMAGServerConfigurationClient(String userId,
                                                                           String serverName,
                                                                           String serverRootURL)

    throws DinoViewServiceException

    {
        String methodName = "getOMAGServerConfigurationClient";

        try
        {

            return new OMAGServerConfigurationClient(userId, serverName, serverRootURL);

        }
        catch (OMAGInvalidParameterException e)

        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }


    }

    /**
     * getMetadataHighwayServicesClient
     * <p>
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param userId          - name of the user performing the operation
     * @param serverName      - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private MetadataHighwayServicesClient getMetadataHighwayServicesClient(String userId,
                                                                           String serverName,
                                                                           String platformRootURL)

    throws DinoViewServiceException

    {
        String methodName = "getMetadataHighwayServicesClient";

        try
        {
            String serverRootURL = platformRootURL + "/servers/" + serverName;

            return new MetadataHighwayServicesClient(serverName, serverRootURL);

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e)

        {
            throw DinoExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);
        }

    }




    /**
     * getAuditLogServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private AuditLogServicesClient getAuditLogServicesClient(String userId,
                                                             String serverName,
                                                             String platformRootURL)

    throws DinoViewServiceException



    {

        String methodName = "getAuditLogServicesClient";

        try
        {

            String serverRootURL = platformRootURL + "/servers/" + serverName;

            return new AuditLogServicesClient(serverName, serverRootURL);


        }
        catch(org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }



    /**
     * getIntegrationDaemonConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private IntegrationDaemonConfigurationClient getIntegrationDaemonConfigurationClient(String userId,
                                                                                         String serverName,
                                                                                         String platformRootURL)

    throws DinoViewServiceException



    {

        String methodName = "getIntegrationDaemonConfigurationClient";

        try
        {

            return new IntegrationDaemonConfigurationClient(userId, serverName, platformRootURL);

        }
        catch(OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }

    /**
     * getEngineHostConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private EngineHostConfigurationClient getEngineHostConfigurationClient(String userId,
                                                                           String serverName,
                                                                           String platformRootURL)

    throws DinoViewServiceException

    {

        String methodName = "getEngineHostConfigurationClient";

        try
        {

            return new EngineHostConfigurationClient(userId, serverName, platformRootURL);

        }
        catch(OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }


    /**
     * getMetadataAccessPointConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private MetadataAccessPointConfigurationClient getMetadataAccessPointConfigurationClient(String userId,
                                                                                             String serverName,
                                                                                             String platformRootURL)

    throws DinoViewServiceException

    {

        String methodName = "getMetadataAccessPointConfigurationClient";

        try
        {

            return new MetadataAccessPointConfigurationClient(userId, serverName, platformRootURL);

        }
        catch(OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }

    /**
     * getViewServerConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private ViewServerConfigurationClient getViewServerConfigurationClient(String userId,
                                                                           String serverName,
                                                                           String platformRootURL)

    throws DinoViewServiceException

    {

        String methodName = "getViewServerConfigurationClient";

        try
        {

            return new ViewServerConfigurationClient(userId, serverName, platformRootURL);

        }
        catch(OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }


    /**
     * getGovernanceEngineConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param userId - name of the user performing the operation
     * @param serverName - name of the server to connect to
     * @param platformRootURL - the root URL to connect to the server
     * @throws DinoViewServiceException - an invalid parameter was detected and reported
     */
    private GovernanceEngineConfigurationClient getGovernanceEngineConfigurationClient(String userId,
                                                                                       String serverName,
                                                                                       String platformRootURL)

    throws DinoViewServiceException

    {

        String methodName = "getGovernanceEngineConfigurationClient";

        try
        {

            return new GovernanceEngineConfigurationClient(serverName, platformRootURL );

        }
        catch(InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapInvalidParameterException(this.getClass().getName(), methodName, e);
        }
    }

    /*
     * Retrieve the platform overview
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform overview
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public PlatformOverview platformGetOverview(String    userId,
                                                String    platformName,
                                                String    methodName)
    throws
        DinoViewServiceException

    {

        PlatformServicesClient platformServicesClient;

        /*
         * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
         */
        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        /*
         *  Use platform services client. Internal method will only throw a DinoViewServiceException
         */
        platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);


        try
        {

            /*
             * Construct an overview with the configured aspects
             */
            ResourceEndpoint platformConfig = configuredPlatforms.get(platformName);
            PlatformOverview platformOverview = new PlatformOverview();
            platformOverview.setPlatformName(platformConfig.getPlatformName());
            platformOverview.setDescription(platformConfig.getResourceDescription());
            platformOverview.setPlatformRootURL(platformConfig.getResourceRootURL());

            // All the following calls to the platformServicesClient can throw a number of OCF exceptions

            // Fetch the platformOrigin
            String platformOrigin = platformServicesClient.getPlatformOrigin(userId);
            platformOverview.setPlatformOrigin(platformOrigin);

            // Fetch the various types of registered services
            // A RegisteredOMAGService contains serviceName, serviceURLMarker, serviceDescription, serviceWiki
            List<RegisteredOMAGService> accessServiceList = platformServicesClient.getAccessServices(userId);
            platformOverview.setAccessServices(accessServiceList);

            List<RegisteredOMAGService> commonServiceList = platformServicesClient.getCommonServices(userId);
            platformOverview.setCommonServices(commonServiceList);

            List<RegisteredOMAGService> governanceServiceList = platformServicesClient.getGovernanceServices(userId);
            platformOverview.setGovernanceServices(governanceServiceList);

            List<RegisteredOMAGService> viewServiceList = platformServicesClient.getViewServices(userId);
            platformOverview.setViewServices(viewServiceList);

            List<RegisteredOMAGService> integrationServiceList = platformServicesClient.getIntegrationServices(userId);
            platformOverview.setIntegrationServices(integrationServiceList);

            List<RegisteredOMAGService> engineServiceList = platformServicesClient.getEngineServices(userId);
            platformOverview.setEngineServices(engineServiceList);
            return platformOverview;

        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }



    /*
     * Retrieve the platform origin
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform origin
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public String platformGetOrigin(String    userId,
                                    String    platformName,
                                    String    methodName)
    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getPlatformOrigin(userId);


        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }


    /*
     * Retrieve the active server list from the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return response containing the DinoServerListResponse object.
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<DinoServerInstance> platformGetActiveServerList(String    userId,
                                                                String    platformName,
                                                                String    methodName)
    throws
    DinoViewServiceException

    {

        try
        {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            /* Retrieve the server names */
            List<String> serverNames = platformServicesClient.getActiveServers(userId);


            if (serverNames != null)
            {
                /*
                 * Construct the return list indicating that all servers it contains are active
                 */
                List<DinoServerInstance> serverList = new ArrayList<>();

                serverNames.forEach(serverName -> {
                    DinoServerInstance dinoServerInstance = new DinoServerInstance();
                    // Try to locate the serverName and platformRootURL in the configured serverInstances. If found include the serverInstanceName,
                    // else ensure it is set to null.
                    String configuredInstanceName = null;

                    Iterator<ResourceEndpoint> configuredServerInstances = this.configuredServerInstances.values().iterator();
                    while (configuredServerInstances.hasNext())
                    {
                        ResourceEndpoint csire = configuredServerInstances.next();
                        if (csire.getServerName().equals(serverName)
                                && csire.getPlatformName().equals(platformName))
                        {
                            // This is our configuration entry...
                            configuredInstanceName = csire.getServerInstanceName();
                        }
                    }
                    dinoServerInstance.setServerInstanceName(configuredInstanceName);
                    dinoServerInstance.setIsActive(true);
                    dinoServerInstance.setServerName(serverName);
                    dinoServerInstance.setPlatformName(platformName);
                    serverList.add(dinoServerInstance);
                });

                return serverList;
            }
            else
            {
                return null;
            }

        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }


    /*
     * Retrieve the known server list from the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the list of server names
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<DinoServerInstance> platformGetKnownServerList(String    userId,
                                                               String    platformName,
                                                               String    methodName)
    throws
    DinoViewServiceException

    {

        try
        {
            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<String> serverNames = platformServicesClient.getKnownServers(userId);

            if (serverNames != null)
            {
                /*
                 * Construct the return list indicating which servers are active
                 */
                List<DinoServerInstance> serverList = new ArrayList<>();

                /* Retrieve a list of names of the active servers */
                List<String> activeServerNames = platformServicesClient.getActiveServers(userId);
                serverNames.forEach(serverName -> {
                    DinoServerInstance dinoServerInstance = new DinoServerInstance();
                    dinoServerInstance.setServerName(serverName);
                    dinoServerInstance.setPlatformName(platformName);
                    if (activeServerNames != null && activeServerNames.contains(serverName))
                    {
                        dinoServerInstance.setIsActive(true);
                    }
                    else
                    {
                        dinoServerInstance.setIsActive(false);
                    }
                    serverList.add(dinoServerInstance);
                });

                return serverList;

            }
            else
            {
                return null;
            }

        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }

    /*
     * Retrieve the access services for the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the list of services
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService> platformGetAccessServiceList(String    userId,
                                                                    String    platformName,
                                                                    String    methodName)

    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getAccessServices(userId);


        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }


    /*
     * Retrieve the view services for the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the list of services
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService> platformGetViewServiceList(String    userId,
                                                                  String    platformName,
                                                                  String    methodName)
    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getViewServices(userId);

        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }

    /*
     * Retrieve the governance services for the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the list of services
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService> platformGetGovernanceServiceList(String    userId,
                                                                        String    platformName,
                                                                        String    methodName)
    throws
    DinoViewServiceException

    {

        try {
            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getGovernanceServices(userId);


        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }

    /*
     * Retrieve the common services for the platform
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the list of services
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService> platformGetCommonServiceList(String    userId,
                                                                    String    platformName,
                                                                    String    methodName)
    throws
    DinoViewServiceException

    {

        try
        {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getCommonServices(userId);


        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }



    /*
     * Retrieve the server overview
     * @param userId  userId under which the request is performed
     * @param platformName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the server overview
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServerOverview serverGetOverview(String    userId,
                                            String    serverName,
                                            String    platformName,
                                            String    serverInstanceName,
                                            String    description,
                                            String    methodName)

    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */

            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            /*
             * Construct an overview with the configured aspects
             */

            ServerOverview serverOverview = new ServerOverview();
            serverOverview.setServerName(serverName);
            serverOverview.setPlatformRootURL(platformRootURL);
            if (serverInstanceName != null)
                serverOverview.setServerInstanceName(serverInstanceName);
            if (description != null)
                serverOverview.setDescription(description);

            // Fetch the platformOrigin
            String serverOrigin = platformServicesClient.getPlatformOrigin(userId);
            serverOverview.setServerOrigin(serverOrigin);

            // Fetch the server classification - internal method will only throw DinoViewServiceException

            ServerTypeClassificationSummary serverClassification = serverGetTypeClassification(userId, serverName, platformName, methodName);
            serverOverview.setServerClassification(serverClassification);


            // Fetch the various aspects of server status (including history) - can throw OCF exceptions
            ServerStatus serverStatus = platformServicesClient.getServerStatus(userId, serverName);
            serverOverview.setServerStatus(serverStatus);

            ///*
            // * Get the active services running on the server....
            // */
            //List<String> serverList = platformServicesClient.getActiveServices(userId, serverName);
            //serverOverview.setServerServicesList(serverList);

            /*
             * Get the integration services running on the server....
             */
            List<RegisteredOMAGService> integrationServices = this.serverGetIntegrationServices(userId, serverName, platformName, methodName);
            serverOverview.setIntegrationServices(integrationServices);

            /*
             * Get the engine services running on the server....
             */
            List<RegisteredOMAGService> engineServices = this.serverGetEngineServices(userId, serverName, platformName, methodName);
            serverOverview.setEngineServices(engineServices);

            /*
             * Get the access services running on the server....
             */
            List<RegisteredOMAGService> accessServices = this.serverGetAccessServices(userId, serverName, platformName, methodName);
            serverOverview.setAccessServices(accessServices);

            /*
             * Get the view services running on the server....
             */
            List<RegisteredOMAGService> viewServices = this.serverGetViewServices(userId, serverName, platformName, methodName);
            serverOverview.setViewServices(viewServices);

            /*
             * Fechez la vache
             *
             * This is an internal method that will already have mapped any exceptions to DinoViewServiceException
             */
            Map<String, ServerCohortDetails> cohortDetails = serverGetCohortDetails(userId, serverName, platformName, methodName);
            serverOverview.setCohortDetails(cohortDetails);

            return serverOverview;

        }

        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }


    /*
     * Retrieve the server origin
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform origin
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public String serverGetOrigin(String    userId,
                                  String    serverName,
                                  String    platformName,
                                  String    methodName)

    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use platform services client
             *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            return platformServicesClient.getPlatformOrigin(userId);

        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOCFInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOCFUserNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }

    }

    /*
     * Retrieve the server type classification
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServerTypeClassificationSummary serverGetTypeClassification(String    userId,
                                                                       String    serverName,
                                                                       String    platformName,
                                                                       String    methodName)

    throws
    DinoViewServiceException

    {

        try {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classfiication method is in the abstract superclass.
             *
             *  *  Can throw a number of OCF exceptions - catch and map them to DinoViewServiceException
             */
            OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);

            return adminServicesClient.getServerClassification();

        }
        catch (OMAGInvalidParameterException e) {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e) {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e) {
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }

    /*
     * Retrieve the server's stored configuration
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform origin
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public OMAGServerConfig serverGetStoredConfiguration(String    userId,
                                                         String    serverName,
                                                         String    platformName,
                                                         String    methodName)

    throws
    DinoViewServiceException

    {

        try
        {

            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classfiication method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */

            OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get the configuration - can throw OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            return adminServicesClient.getOMAGServerConfig();

        }

        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }


    }

    /*
     * Retrieve the server's running instance configuration
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform origin
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public OMAGServerConfig serverGetInstanceConfiguration(String    userId,
                                                   String    serverName,
                                                   String    platformName,
                                                   String    methodName)

    throws
    DinoViewServiceException

    {

        try
        {
            /*
             * Resolve the platformURL - can throw a DinoViewServiceException - no need to catch
             */
            String platformRootURL = resolvePlatformRootURL(platformName, methodName);


            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classfiication method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get the configuration - can throw OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            return adminServicesClient.getOMAGServerInstanceConfig();

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }




    /**
     * Retrieve the server's cohort descriptions and the local and remote registrations for each cohort
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform the server can be reached at
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException   an error was detected and reported
     */
    public Map<String, ServerCohortDetails> serverGetCohortDetails(String    userId,
                                                                   String    serverName,
                                                                   String    platformName,
                                                                   String    methodName)

    throws
    DinoViewServiceException

    {

        Map<String, ServerCohortDetails> returnMap = new HashMap<>();

        try
        {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use metadata highway services client - this is an internal function and will only throw a DinoViewServiceException
             */
            MetadataHighwayServicesClient metadataHighwayServicesClient = this.getMetadataHighwayServicesClient(userId, serverName, platformRootURL);

            /*
             * metadataHighwayServicesClient can throw OMRS exceptions for InvalidParameter, UserNotAuthorized or RepositoryError
             */

            List<CohortDescription> cohortDescriptions = metadataHighwayServicesClient.getCohortDescriptions(userId);

            // For each cohort construct and populate a ServerCohortDetails object and add it to the map
            for (CohortDescription cohortDescription : cohortDescriptions)
            {
                String cohortName = cohortDescription.getCohortName();
                ServerCohortDetails serverCohortDetails = new ServerCohortDetails();
                serverCohortDetails.setCohortDescription(cohortDescription);

                // Get the local registration and add that to the SCD
                MemberRegistration localRegistration = metadataHighwayServicesClient.getLocalRegistration(userId, cohortName);
                serverCohortDetails.setLocalRegistration(localRegistration);

                // Get the remote registrations and add them to the SCD
                List<MemberRegistration> remoteRegistrations = metadataHighwayServicesClient.getRemoteRegistrations(userId, cohortName);
                serverCohortDetails.setRemoteRegistrations(remoteRegistrations);

                returnMap.put(cohortName, serverCohortDetails);

            }
            return returnMap;

        }

        catch (RepositoryErrorException e)
        {
            /* If the server is not in a cohort, it may not have metadata highway services enabled. In this
             * case just return an empty result
             */
            return null;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(), methodName, e);
        }

    }


    /*
     * Retrieve the server audit log
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public OMRSAuditLogReport serverGetAuditLog(String    userId,
                                    String    serverName,
                                    String    platformName,
                                    String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use repository services audit log services client.
             *  Internal method will only throw DinoViewServiceException
             */
            AuditLogServicesClient auditLogServicesClient = this.getAuditLogServicesClient(userId, serverName, platformRootURL);

            return auditLogServicesClient.getAuditLogReport(userId);

        }
        catch (RepositoryErrorException exc) {
            /* If the server is not in a cohort, it may not have metadata highway services enabled. In this
             * case just return an empty result
             */
            return null;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            throw DinoExceptionHandler.mapOMRSInvalidParameterException(this.getClass().getName(), methodName, e);

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            throw DinoExceptionHandler.mapOMRSUserNotAuthorizedException(this.getClass().getName(), methodName, e);

        }

    }


    /*
     * Retrieve a list of integration services for a specified service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService>  serverGetIntegrationServices(String    userId,
                                                  String    serverName,
                                                  String    platformName,
                                                  String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            IntegrationDaemonConfigurationClient integrationDaemonConfigurationClient =
                    this.getIntegrationDaemonConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get a list of the integration services that are configured on the server
             *
             */

            List<RegisteredOMAGService> serviceList = integrationDaemonConfigurationClient.getConfiguredIntegrationServices();

            return serviceList;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }



    /*
     * Retrieve a list of engine services for a specified service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService>  serverGetEngineServices(String    userId,
                                                                String    serverName,
                                                                String    platformName,
                                                                String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            EngineHostConfigurationClient engineHostConfigurationClient =
                    this.getEngineHostConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get a list of the integration services that are configured on the server
             *
             */

            List<RegisteredOMAGService> serviceList = engineHostConfigurationClient.getConfiguredEngineServices();

            return serviceList;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }

    /*
     * Retrieve a list of access services for a specified service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService>  serverGetAccessServices(String    userId,
                                                                String    serverName,
                                                                String    platformName,
                                                                String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            MetadataAccessPointConfigurationClient metadataAccessPointConfigurationClient =
                    this.getMetadataAccessPointConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get a list of the integration services that are configured on the server
             *
             */

            List<RegisteredOMAGService> serviceList = metadataAccessPointConfigurationClient.getConfiguredAccessServices();

            return serviceList;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }


    /*
     * Retrieve a list of view services for a specified service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public List<RegisteredOMAGService>  serverGetViewServices(String    userId,
                                                              String    serverName,
                                                              String    platformName,
                                                              String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            ViewServerConfigurationClient viewServerConfigurationClient =
                    this.getViewServerConfigurationClient(userId, serverName, platformRootURL);

            /*
             * Get a list of the integration services that are configured on the server
             *
             */

            List<RegisteredOMAGService> serviceList = viewServerConfigurationClient.getConfiguredViewServices();

            return serviceList;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }




    /*
     * Retrieve the service details for a specified integration service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param serviceName The name of the service to be retrieved
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServiceDetails serverGetIntegrationServiceDetails(String    userId,
                                                             String    serverName,
                                                             String    platformName,
                                                             String    serviceURLMarker,
                                                             String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            IntegrationDaemonConfigurationClient integrationDaemonConfigurationClient =
                    this.getIntegrationDaemonConfigurationClient(userId, serverName, platformRootURL);


            /*
             * Get the configuration of the integration service that has been requested. This can throw
             * OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            IntegrationServiceConfig serviceConfig = integrationDaemonConfigurationClient.getIntegrationServiceConfiguration(serviceURLMarker);

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceCat(ServiceDetails.ServiceCat.IntegrationService);
            serviceDetails.setIntegrationServiceConfig(serviceConfig);

            return serviceDetails;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }


    /*
     * Retrieve the service details for a specified engine service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param serviceName The name of the service to be retrieved
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServiceDetails serverGetEngineServiceDetails(String    userId,
                                                        String    serverName,
                                                        String    platformName,
                                                        String    serviceURLMarker,
                                                        String    methodName)

    throws
    DinoViewServiceException

    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            EngineHostConfigurationClient engineHostConfigurationClient =
                    this.getEngineHostConfigurationClient(userId, serverName, platformRootURL);
            
            /*
             * Get the configuration of the integration service that has been requested. This can throw
             * OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            EngineServiceConfig serviceConfig = engineHostConfigurationClient.getEngineServiceConfiguration(serviceURLMarker);

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceCat(ServiceDetails.ServiceCat.EngineService);
            serviceDetails.setEngineServiceConfig(serviceConfig);

            return serviceDetails;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }

    /*
     * Retrieve the service details for a specified access service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param serviceName The name of the service to be retrieved
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServiceDetails serverGetAccessServiceDetails(String    userId,
                                                        String    serverName,
                                                        String    platformName,
                                                        String    serviceFullName,
                                                        String    serviceURLMarker,
                                                        String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            MetadataAccessPointConfigurationClient metadataAccessPointConfigurationClient =
                    this.getMetadataAccessPointConfigurationClient(userId, serverName, platformRootURL);


            /*
             * Get the configuration of the integration service that has been requested. This can throw
             * OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            List<AccessServiceConfig> accessServices = metadataAccessPointConfigurationClient.getAccessServicesConfiguration();
            AccessServiceConfig serviceConfig = null;
            for (int i=0; i<accessServices.size(); i++) {
                AccessServiceConfig svc = accessServices.get(i);
                if (  serviceFullName.equals(svc.getAccessServiceFullName()) ||
                      serviceURLMarker.equals(svc.getAccessServiceURLMarker()))
                    serviceConfig = svc;
            }
            if (serviceConfig == null) {
                throw new DinoViewServiceException(
                        DinoViewErrorCode.COULD_NOT_RETRIEVE_SERVER_CONFIGURATION.getMessageDefinition(
                                methodName,
                                serverName),
                        this.getClass().getName(),
                        methodName);
            }

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceCat(ServiceDetails.ServiceCat.AccessService);
            serviceDetails.setAccessServiceConfig(serviceConfig);

            return serviceDetails;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }


    /*
     * Retrieve the service details for a specified view service
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param serviceName The name of the service to be retrieved
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public ServiceDetails serverGetViewServiceDetails(String    userId,
                                                      String    serverName,
                                                      String    platformName,
                                                      String    serviceURLMarker,
                                                      String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */
            ViewServerConfigurationClient viewServerConfigurationClient =
                    this.getViewServerConfigurationClient(userId, serverName, platformRootURL);


            /*
             * Get the configuration of the integration service that has been requested. This can throw
             * OMAGNotAuthorizedException, OMAGInvalidParameterException, OMAGConfigurationErrorException
             *
             */
            List<ViewServiceConfig> viewServices = viewServerConfigurationClient.getViewServicesConfiguration();
            ViewServiceConfig serviceConfig = null;
            for (int i=0; i<viewServices.size(); i++) {
                ViewServiceConfig svc = viewServices.get(i);
                if (serviceURLMarker.equals(svc.getViewServiceURLMarker()))
                    serviceConfig = svc;
            }
            if (serviceConfig == null) {
                throw new DinoViewServiceException(
                        DinoViewErrorCode.COULD_NOT_RETRIEVE_SERVER_CONFIGURATION.getMessageDefinition(
                                methodName,
                                serverName),
                        this.getClass().getName(),
                        methodName);
            }

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceCat(ServiceDetails.ServiceCat.ViewService);
            serviceDetails.setViewServiceConfig(serviceConfig);

            return serviceDetails;

        }
        catch (OMAGInvalidParameterException e)
        {
            throw DinoExceptionHandler.mapOMAGInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (OMAGNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapOMAGNotAuthorizedException(this.getClass().getName(), methodName, userId, e);
        }
        catch (OMAGConfigurationErrorException e)
        {
            /*
             * You may get this exception if the server is not running - and has been asked for its instance configuration
             * In this case you will get an exception in which the 'cause' has a reportedErrorMessageId of OMAG-MULTI-TENANT-404-001.
             * In this specific case ONLY, tolerate the error and pass back a null in the response for activeConfig. For any other error codes
             * report the exception.
             */

            if (e.getCause() != null && e.getCause() instanceof OCFCheckedExceptionBase)
            {
                OCFCheckedExceptionBase cause = (OCFCheckedExceptionBase) (e.getCause());
                if (cause.getReportedErrorMessageId().equals("OMAG-MULTI-TENANT-404-001"))
                {
                    /* In this specific circumstance, tolerate the exception... */
                    return null;
                }
            }
            /* If the OMAGConfigurationErrorException was for a different reason, do not tolerate.... */
            throw DinoExceptionHandler.mapOMAGConfigurationErrorException(this.getClass().getName(), methodName, serverName, e);
        }

    }





    /*
     * Retrieve the engine details for a specified engine
     * @param userId  userId under which the request is performed
     * @param serverName The name of the server to interrogate
     * @param platformName The name of the platform hosting the server
     * @param serviceName The name of the service to be retrieved
     * @param methodName The name of the method being invoked
     * @return the server type as a String
     *
     * Exceptions returned by the server
     * @throws DinoViewServiceException    an error was detected and reported
     *
     */
    public EngineDetails serverGetEngineDetails(String    userId,
                                                String    serverName,
                                                String    platformName,
                                                String    engineQualifiedName,
                                                String    methodName)

    throws
    DinoViewServiceException


    {

        try {

            String platformRootURL = resolvePlatformRootURL(platformName, methodName);

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classification method is in the abstract superclass.
             *
             * Can throw OMAGInvalidParameterException
             */

            GovernanceEngineConfigurationClient gecc = this.getGovernanceEngineConfigurationClient(userId, serverName, platformRootURL);

            GovernanceEngineElement governanceEngineElement = gecc.getGovernanceEngineByName(userId, engineQualifiedName);

            if (governanceEngineElement == null) {
                /*
                 * Could not retrieve the engine - throw an exception
                 */
                throw new DinoViewServiceException(
                        DinoViewErrorCode.COULD_NOT_RETRIEVE_GOVERNANCE_ENGINE.getMessageDefinition(
                                methodName,
                                engineQualifiedName),
                        this.getClass().getName(),
                        methodName);
            }

            /*
             * Fill out the EngineDetails to be returned to the caller.
             * Includes:
             *   Engine:
             *      engineDisplayName     ==> GovernanceEngineElement.properties.displayName
             *      engineQualifiedName   ==> GovernanceEngineElement.properties.qualifiedName
             *      engineDescription     ==> GovernanceEngineElement.properties.description
             *      engineTypeDescription ==> GovernanceEngineElement.properties.typeDescription
             *      engineVersion         ==> GovernanceEngineElement.properties.version
             *      engineGUID      ==> GovernanceEngineElement.elementHeader.guid
             * Services, for each Service:
             *      for each requestType:
             *          requestType (string) : { parameter : value , parameter : value, etc. }
             *                       => RegisteredGovernanceService.requestTypes
             */

            EngineDetails engineDetails = new EngineDetails();
            engineDetails.setEngineDisplayName(governanceEngineElement.getProperties().getDisplayName());
            engineDetails.setEngineQualifiedName(governanceEngineElement.getProperties().getQualifiedName());
            engineDetails.setEngineDescription(governanceEngineElement.getProperties().getDescription());
            engineDetails.setEngineTypeDescription(governanceEngineElement.getProperties().getTypeDescription());
            engineDetails.setEngineVersion(governanceEngineElement.getProperties().getVersion());
            engineDetails.setEngineGUID(governanceEngineElement.getElementHeader().getGUID());

            String governanceEngineGUID = governanceEngineElement.getElementHeader().getGUID();

            /*
             * The following call will return a list of governance service GUIDs.
             * For each of the GUIDs, call getGovernanceServiceByGUID to get the governance service properties (including
             * qualifiedName, description, zones etc).
             * Also call the getRegisteredGovernanceService method to get the requestTtypes and requestParameters
             * with which the service was registered with the engine.
             */

            List<RegisteredGovernanceServiceElement> governanceServices = gecc.getRegisteredGovernanceServices(userId, governanceEngineGUID, 0, 100);

            if (governanceServices != null && governanceServices.size()>0) {

                Map<String, ServicePropertiesAndRequests> serviceMap = new HashMap<>();

                for (RegisteredGovernanceServiceElement governanceService : governanceServices) {

                    GovernanceServiceProperties gsp = governanceService.getProperties();

                    String governanceServiceQualifiedName = gsp.getQualifiedName();

                    Map<String, RegisteredGovernanceServiceProperties> requestTypes = governanceService.getProperties().getRequestTypes();

                    if (requestTypes != null) {

                        Map<String, Map<String, String>> requestTypeMap = new HashMap<>();

                        for (String requestType : requestTypes.keySet()) {

                            RegisteredGovernanceServiceProperties registeredGovernanceServiceProperties = requestTypes.get(requestType);

                            if (registeredGovernanceServiceProperties != null) {

                                if (registeredGovernanceServiceProperties.getServiceRequestType() == null) {

                                    requestTypeMap.put(requestType, registeredGovernanceServiceProperties.getRequestParameters());
                                }
                                else {

                                    requestTypeMap.put(requestType + " -> " + registeredGovernanceServiceProperties.getServiceRequestType(), registeredGovernanceServiceProperties.getRequestParameters());
                                }
                            }
                        }

                        ServicePropertiesAndRequests spar = new ServicePropertiesAndRequests(gsp, requestTypeMap);

                        serviceMap.put(governanceServiceQualifiedName, spar);
                    }

                }
                engineDetails.setServiceMap(serviceMap);
            }

            return engineDetails;

        }
        catch (InvalidParameterException e)
        {
            throw DinoExceptionHandler.mapInvalidParameterException(this.getClass().getName(), methodName, e);
        }
        catch (UserNotAuthorizedException e)
        {
            throw DinoExceptionHandler.mapUserNotAuthorizedException(this.getClass().getName(), methodName, e);
        }
        catch (PropertyServerException e)
        {
            throw DinoExceptionHandler.mapOCFPropertyServerException(this.getClass().getName(), methodName, platformName, e);
        }
    }
}
