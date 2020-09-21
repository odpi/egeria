/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.handlers;



import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
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
import org.odpi.openmetadata.viewservices.dino.api.properties.DinoServerInstance;
import org.odpi.openmetadata.viewservices.dino.api.properties.PlatformOverview;
import org.odpi.openmetadata.viewservices.dino.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServerCohortDetails;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServerOverview;
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
public class DinoViewHandler
{
    private static final Logger log = LoggerFactory.getLogger(DinoViewHandler.class);

    /* TODO!! viewServiceOptions should have been validated in the Admin layer.
     * The viewServiceOptions contains a list of resource endpoints that the
     * view service can connect to. It is formatted like this:
     * "resourceEndpoints" : [
                 {
                     resourceCategory   : "Platform",
                     resourceName       : "Platform2",
                     resourceRootURL    : "https://localhost:9443"
                 },
                 {
                     resourceCategory   : "Platform",
                     resourceName       : "Platform1",
                     resourceRootURL    : "https://localhost:8082"
                 },
                 {
                     resourceCategory   : "Server",
                     resourceName       : "Metadata_Server1",
                     resourceRootURL    : "https://localhost:8082"
                 },
                 {
                     resourceCategory   : "Server",
                     resourceName       : "Metadata_Server2",
                     resourceRootURL    : "https://localhost:9443"
                 }
             ]
     */
    private Map<String, ResourceEndpoint>  configuredPlatforms = null;  // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint>  configuredServerInstances   = null;  // map is keyed using serverName+platformRootURL so each instance is unique
    //private Map<String, ResourceEndpoint>  discoveredServers   = null;  // map is keyed using serverName+platformRootURL so each instance is unique


    /**
     * Constructor for the DinoViewHandler
     * ~Store the configured endpoints
     */
    public DinoViewHandler(List<ResourceEndpointConfig>  resourceEndpoints) {


        /*
         * Populate map of resources with their endpoints....
         */

        // TODO - need to add validation rules to this - ensure uniqueness etc...

        if (resourceEndpoints != null && !resourceEndpoints.isEmpty()) {
            configuredPlatforms         = new HashMap<>();
            configuredServerInstances   = new HashMap<>();
            //discoveredServers   = new HashMap<>();

            resourceEndpoints.forEach(res -> {

                String resCategory   = res.getResourceCategory();
                ResourceEndpoint rep = new ResourceEndpoint(res);

                String resName = null;

                switch (resCategory) {
                    case "Platform":
                        resName = res.getPlatformName();
                        configuredPlatforms.put(resName, rep);
                        break;

                    case "Server":
                        resName = res.getServerInstanceName();
                        configuredServerInstances.put(resName, rep);
                        break;

                    default:
                        // LOG ERROR!! TODO
                        break;

                }
            });
        }
    }

    /**
     * getResourceEndpoints - returns a list of the configured resource endpoints. Does not include discovered resource endpoints.
     *
     * @param userId  userId under which the request is performed
     * @param methodName The name of the method being invoked
     * This method will return the resource endpoints that have been configured for the view service
     *
     */
    public Map<String, List<ResourceEndpoint>> getResourceEndpoints(String userId, String methodName)

    {

        List<ResourceEndpoint> platformList = new ArrayList<>();
        List<ResourceEndpoint> serverList   = new ArrayList<>();

        platformList.addAll(configuredPlatforms.values());
        serverList.addAll(configuredServerInstances.values());

        Map<String, List<ResourceEndpoint>> returnMap = new HashMap<>();
        returnMap.put("platformList",platformList);
        returnMap.put("serverList",serverList);
        return returnMap;
    }



    /**
     * resolvePlatformRootURL
     *
     * This method will look up the configured root URL for the named platform.
     *
     * @param platformName
     * @return resolved platform URL Root
     * @throws InvalidParameterException
     */
    private String resolvePlatformRootURL(String platformName, String methodName) throws InvalidParameterException

    {
        String platformRootURL = null;

        if (platformName != null) {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null) {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null) {
            // TODO - this error code is not entirely accurate - the platformName may not have been null but it is not in the configured map
            throw new InvalidParameterException(OMAGCommonErrorCode.VIEW_SERVICE_NULL_PLATFORM_NAME.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                "platformName");
        }

        return platformRootURL;
    }





    /**
     * getPlatformServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param platformName
     * @param platformRootURL
     * @throws InvalidParameterException
     */
    private PlatformServicesClient getPlatformServicesClient(String platformName,
                                                             String platformRootURL) throws InvalidParameterException

    {

        PlatformServicesClient client = new PlatformServicesClient(platformName, platformRootURL);

        return client;
    }

    /**
     * getMetadataServerConfigurationClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param serverName
     * @param serverRootURL
     * @throws OMAGInvalidParameterException
     */
    private OMAGServerConfigurationClient getOMAGServerConfigurationClient(String userId,
                                                                           String serverName,
                                                                           String serverRootURL) throws OMAGInvalidParameterException

    {

        OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(userId, serverName, serverRootURL);

        return client;
    }

    /**
     * getMetadataHighwayServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Platform Services interface.
     *
     * @param serverName
     * @param platformRootURL
     * @throws InvalidParameterException
     */
    private MetadataHighwayServicesClient getMetadataHighwayServicesClient(String userId,
                                                                           String serverName,
                                                                           String platformRootURL) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException

    {

        String serverRootURL = platformRootURL + "/servers/" + serverName;

        MetadataHighwayServicesClient client = new MetadataHighwayServicesClient(serverName, serverRootURL);

        return client;
    }




    /**
     * getAuditLogServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * Repository Services Audit Log Services interface.
     *
     * @param serverName
     * @param platformRootURL
     * @throws InvalidParameterException
     */
    private AuditLogServicesClient getAuditLogServicesClient(String userId,
                                                             String serverName,
                                                             String platformRootURL) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException

    {

        String serverRootURL = platformRootURL + "/servers/" + serverName;

        AuditLogServicesClient client = new AuditLogServicesClient(serverName, serverRootURL);

        return client;
    }



    /*
     * Retrieve the platform overview
     * @param userId  userId under which the request is performed
     * @param platformName The name of the platform to interrogate
     * @param methodName The name of the method being invoked
     * @return the platform overview
     *
     * Exceptions returned by the server
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public PlatformOverview platformGetOverview(String    userId,
                                                String    platformName,
                                                String    methodName)     throws  InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            /*
             * Construct an overview with the configured aspects
             */
            ResourceEndpoint platformConfig = configuredPlatforms.get(platformName);
            PlatformOverview platformOverview = new PlatformOverview();
            platformOverview.setPlatformName(platformConfig.getPlatformName());
            platformOverview.setDescription(platformConfig.getResourceDescription());
            platformOverview.setPlatformRootURL(platformConfig.getResourceRootURL());

            // Fetch the platformOrigin
            String platformOrigin = platformServicesClient.getPlatformOrigin(userId);
            platformOverview.setPlatformOrigin(platformOrigin);

            // Fetch the various types of registered services
            // An RegisteredOMAGService contains serviceName, serviceURLMarker, serviceDescription, serviceWiki
            List<RegisteredOMAGService> accessServiceList = platformServicesClient.getAccessServices(userId);
            platformOverview.setAccessServices(accessServiceList);
            List<RegisteredOMAGService> commonServiceList = platformServicesClient.getCommonServices(userId);
            platformOverview.setCommonServices(commonServiceList);
            List<RegisteredOMAGService> governanceServiceList = platformServicesClient.getGovernanceServices(userId);
            platformOverview.setGovernanceServices(governanceServiceList);
            List<RegisteredOMAGService> viewServiceList = platformServicesClient.getViewServices(userId);
            platformOverview.setViewServices(viewServiceList);

            return platformOverview;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public String platformGetOrigin(String    userId,
                                    String    platformName,
                                    String    methodName)     throws  InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            String string = platformServicesClient.getPlatformOrigin(userId);

            return string;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<DinoServerInstance> platformGetActiveServerList(String    userId,
                                                                String    platformName,
                                                                String    methodName)     throws  InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            /* Retrieve the server names */
            List<String> serverNames = platformServicesClient.getActiveServers(userId);

            /*
             * Construct the return list indicating that all servers it contains are active
             */
            List<DinoServerInstance> serverList = new ArrayList();
            serverNames.forEach(serverName -> {
                DinoServerInstance dinoServerInstance = new DinoServerInstance();
                // Try to locate the serverName and plaformRootURL in the configured serverInstances. If found include the serverInstanceName,
                // else ensure it is set to null.
                String configuredInstanceName = null;

                Iterator<ResourceEndpoint> configuredServerInstances = this.configuredServerInstances.values().iterator();
                while (configuredServerInstances.hasNext()) {
                    ResourceEndpoint csire = configuredServerInstances.next();
                    if (csire.getServerName().equals(serverName)
                        //&&
                        //csire.getResourceRootURL().equals(serverName)   ){   // TODO - rename as platformRootURL!!
                        && csire.getPlatformName().equals(platformName) ){
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
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<DinoServerInstance> platformGetKnownServerList(String    userId,
                                                               String    platformName,
                                                               String    methodName)     throws  InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {



        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<String> serverNames = platformServicesClient.getKnownServers(userId);


            /*
             * Construct the return list indicating which servers are active
             */
            List<DinoServerInstance> serverList = new ArrayList();

            /* Retrieve a list of names of the active servers */
            List<String> activeServerNames = platformServicesClient.getActiveServers(userId);
            Map<String, DinoServerInstance> serverMap = new HashMap<>();
            serverNames.forEach(serverName -> {
                DinoServerInstance dinoServerInstance = new DinoServerInstance();
                dinoServerInstance.setServerName(serverName);
                dinoServerInstance.setPlatformName(platformName);
                if (activeServerNames.contains(serverName)) {
                    dinoServerInstance.setIsActive(true);
                }
                else {
                    dinoServerInstance.setIsActive(false);
                }
                serverList.add(dinoServerInstance);
            });

            return serverList;



        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<RegisteredOMAGService> platformGetAccessServiceList(String    userId,
                                                                    String    platformName,
                                                                    String    methodName)     throws  InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<RegisteredOMAGService> serviceList = new ArrayList<>();

            serviceList = platformServicesClient.getAccessServices(userId);

            return serviceList;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<RegisteredOMAGService> platformGetViewServiceList(String    userId,
                                                                  String    platformName,
                                                                  String    methodName)     throws  InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<RegisteredOMAGService> serviceList = new ArrayList<>();

            serviceList = platformServicesClient.getViewServices(userId);

            return serviceList;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<RegisteredOMAGService> platformGetGovernanceServiceList(String    userId,
                                                                        String    platformName,
                                                                        String    methodName)     throws  InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<RegisteredOMAGService> serviceList = new ArrayList<>();

            serviceList = platformServicesClient.getGovernanceServices(userId);

            return serviceList;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public List<RegisteredOMAGService> platformGetCommonServiceList(String    userId,
                                                                    String    platformName,
                                                                    String    methodName)     throws  InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            List<RegisteredOMAGService> serviceList = new ArrayList<>();

            serviceList = platformServicesClient.getCommonServices(userId);

            return serviceList;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public ServerOverview serverGetOverview(String    userId,
                                            String    serverName,
                                            String    platformName,
                                            String    serverInstanceName,
                                            String    description,
                                            String    methodName)     throws  InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException,
                                                                                  RepositoryErrorException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client with platformName
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            /*
             * Construct an overview with the configured aspects
             */
            ServerOverview serverOverview = null;
            ResourceEndpoint serverEndpoint = null;

            serverOverview = new ServerOverview();
            serverOverview.setServerName(serverName);
            serverOverview.setPlatformRootURL(platformRootURL);
            if (serverInstanceName != null)
                serverOverview.setServerInstanceName(serverInstanceName);
            if (description != null)
                serverOverview.setDescription(description);

            // Fetch the platformOrigin
            String serverOrigin = platformServicesClient.getPlatformOrigin(userId);
            serverOverview.setServerOrigin(serverOrigin);

            // Fetch the server classification
            try {
                ServerTypeClassificationSummary serverClassification = serverGetTypeClassification(userId, serverName, platformName, methodName);
                serverOverview.setServerClassification(serverClassification);
            }
            catch (OMAGInvalidParameterException exc) {

                // Wrap the OMAG exception
                throw new InvalidParameterException("Could not retrieve server type classification due to an invalid value for serverName parameter",
                                                        exc,
                                                        serverName);
            }
            catch (OMAGNotAuthorizedException exc) {
                    // Wrap the OMAG exception
                    // TODO !!!!!  This is temporary to get a build working - should be throwing UserNotAuthorizedException
                    throw new InvalidParameterException("Could not retrieve server type classification as user is not authorized to perform this operation",
                                                        exc,
                                                        serverName);
            }
            catch (OMAGConfigurationErrorException exc) {
                    // Wrap the OMAG exception
                    throw new PropertyServerException(exc);
            }

            // Fetch the server status (including history)
            ServerStatus serverStatus = platformServicesClient.getServerStatus(userId, serverName);
            serverOverview.setServerStatus(serverStatus);

            /*
             * Get the active services running on the server....
             */
            List<String> serverList = platformServicesClient.getActiveServices(userId, serverName);
            serverOverview.setServerServicesList(serverList);

            /*
             * Fechez la vache
             */
            Map<String, ServerCohortDetails> cohortDetails = serverGetCohortDetails(userId, serverName, platformName, methodName);
            serverOverview.setCohortDetails(cohortDetails);

            // Fetch the other overview information desirable for a ServerOverview
            // TODO...

            return serverOverview;

        }
        catch ( UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public String serverGetOrigin(String    userId,
                                  String    serverName,
                                  String    platformName,
                                  String    methodName)     throws  InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use platform services client
             */
            PlatformServicesClient platformServicesClient = this.getPlatformServicesClient(platformName, platformRootURL);

            String string = platformServicesClient.getPlatformOrigin(userId);

            return string;

        }
        catch (UserNotAuthorizedException |
                PropertyServerException    |
                InvalidParameterException  e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public ServerTypeClassificationSummary serverGetTypeClassification(String    userId,
                                                                       String    serverName,
                                                                       String    platformName,
                                                                       String    methodName)     throws  InvalidParameterException,
                                                                                                         OMAGInvalidParameterException,
                                                                                                         OMAGNotAuthorizedException,
                                                                                                         OMAGConfigurationErrorException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classfiication method is in the abstract superclass.
             */
            OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);

            ServerTypeClassificationSummary summary = adminServicesClient.getServerClassification();  // TODO - check no use of userId??
            return summary;

        }
        catch (OMAGInvalidParameterException | OMAGNotAuthorizedException | OMAGConfigurationErrorException e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public OMAGServerConfig serverGetStoredConfiguration(String    userId,
                                                   String    serverName,
                                                   String    platformName,
                                                   String    methodName)     throws  InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            // Nest try..catch blocks in order to wrap OMAG exceptions and throw the corresponding OCF exceptions
            try {

                /*
                 *  Use admin services client - need to speculatively choose one of the concrete admin clients, since type classfiication method is in the abstract superclass.
                 */
                OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);


                OMAGServerConfig config = adminServicesClient.getOMAGServerConfig();  // TODO - check no use of userId??
                return config;

            }
            catch (OMAGInvalidParameterException exc) {

                // Wrap the OMAG exception
                throw new InvalidParameterException("Could not retrieve server type classification due to an invalid value for serverName parameter",
                                                    exc,
                                                    serverName);
            }
            catch (OMAGNotAuthorizedException exc) {

                // Wrap the OMAG exception
                throw new UserNotAuthorizedException(exc,
                                                    userId);
            }
            catch (OMAGConfigurationErrorException exc) {

                // Wrap the OMAG exception
                throw new PropertyServerException(exc);
            }
        }
        catch ( UserNotAuthorizedException |
                PropertyServerException |
                InvalidParameterException e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public OMAGServerConfig serverGetInstanceConfiguration(String    userId,
                                                   String    serverName,
                                                   String    platformName,
                                                   String    methodName)     throws  InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            // Nest try..catch blocks in order to wrap OMAG exceptions and throw the corresponding OCF exceptions
            try {

                /*
                 *  Use admin services client.
                 */
                OMAGServerConfigurationClient adminServicesClient = this.getOMAGServerConfigurationClient(userId, serverName, platformRootURL);


                OMAGServerConfig config = adminServicesClient.getOMAGServerInstanceConfig();  // TODO - check no use of userId??
                return config;

            }
            catch (OMAGInvalidParameterException exc) {

                // Wrap the OMAG exception
                throw new InvalidParameterException("Could not retrieve server type classification due to an invalid value for serverName parameter",
                                                    exc,
                                                    serverName);
            }
            catch (OMAGNotAuthorizedException exc) {

                // Wrap the OMAG exception
                throw new UserNotAuthorizedException(exc,
                                                     userId);
            }
            catch (OMAGConfigurationErrorException exc) {

                // Wrap the OMAG exception
                throw new PropertyServerException(exc);
            }
        }
        catch ( UserNotAuthorizedException |
                PropertyServerException |
                InvalidParameterException e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     *
     */
    public Map<String, ServerCohortDetails> serverGetCohortDetails(String    userId,
                                                                   String    serverName,
                                                                   String    platformName,
                                                                   String    methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException

    {

        Map<String, ServerCohortDetails> returnMap = new HashMap<>();

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            // Nest try..catch blocks in order to wrap OMAG exceptions and throw the corresponding OCF exceptions
            try {

                /*
                 *  Use metadata highway services client.
                 */
                MetadataHighwayServicesClient metadataHighwayServicesClient = this.getMetadataHighwayServicesClient(userId, serverName, platformRootURL);

                List<CohortDescription> cohortDescriptions = metadataHighwayServicesClient.getCohortDescriptions(userId);

                // For each cohort construct and populate a ServerCohortDetails object and add it to the map
                for (CohortDescription cohortDescription : cohortDescriptions) {
                    String cohortName = cohortDescription.getCohortName();
                    ServerCohortDetails serverCohortDetails = new ServerCohortDetails();
                    serverCohortDetails.setCohortDescription(cohortDescription);

                    // Get the local registration and add that to the SCD
                    MemberRegistration localRegistration = metadataHighwayServicesClient.getLocalRegistration(serverName, userId, cohortName);
                    serverCohortDetails.setLocalRegistration(localRegistration);

                    // Get the remote registrations and add them to the SCD
                    List<MemberRegistration> remoteRegistrations = metadataHighwayServicesClient.getRemoteRegistrations(serverName, userId, cohortName);
                    serverCohortDetails.setRemoteRegistrations(remoteRegistrations);

                    returnMap.put(cohortName, serverCohortDetails);

                }
                return returnMap;

            }
            catch (RepositoryErrorException exc) {
                /* If the server is not in a cohort, it may not have metadata highway services enabled. In this
                 * case just return an empty result
                 */
                return null;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException exc) {

                // Wrap the OMAG exception
                throw new InvalidParameterException("Could not retrieve cohort descriptions due to an invalid value for serverName parameter",
                                                    exc,
                                                    serverName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException exc) {

                // Wrap the OMAG exception
                throw new UserNotAuthorizedException(exc,
                                                     userId);
            }

        } catch (UserNotAuthorizedException |
                InvalidParameterException e) {
            throw e;
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
     * @throws InvalidParameterException   one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException  user is not authorized to perform the requested operation
     * @throws PropertyServerException     there is a problem reported in the open metadata server(s)
     *
     */
    public OMRSAuditLogReport serverGetAuditLog(String    userId,
                                    String    serverName,
                                    String    platformName,
                                    String    methodName) throws
                                                              UserNotAuthorizedException,
                                                              InvalidParameterException

    {

        String platformRootURL = resolvePlatformRootURL(platformName, methodName);

        try {

            /*
             *  Use repository services audit log services client.
             */
            AuditLogServicesClient auditLogServicesClient = this.getAuditLogServicesClient(userId, serverName, platformRootURL);

            OMRSAuditLogReport report = auditLogServicesClient.getAuditLog(userId);
            return report;

        }
        catch (RepositoryErrorException exc) {
            /* If the server is not in a cohort, it may not have metadata highway services enabled. In this
             * case just return an empty result
             */
            return null;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException exc) {

            // Wrap the OMAG exception
            throw new InvalidParameterException("Could not retrieve cohort descriptions due to an invalid value for serverName parameter",
                                                exc,
                                                serverName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException exc) {

            // Wrap the OMAG exception
            throw new UserNotAuthorizedException(exc,
                                                 userId);
        }

    }

}
