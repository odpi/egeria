/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.handlers;

import org.odpi.openmetadata.adminservices.client.*;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorExceptionHandler;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorViewErrorCode;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorViewServiceException;
import org.odpi.openmetadata.viewservices.serverauthor.api.properties.Platform;
import org.odpi.openmetadata.viewservices.serverauthor.api.properties.PlatformStatus;
import org.odpi.openmetadata.viewservices.serverauthor.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.serverauthor.api.properties.StoredServer;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReportSeverity;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


import java.util.*;


/**
 * The ServerAuthorViewHandler is initialised with the server the call should be sent to.
 * The handler exposes methods for functionality for the Server Author view allowing servers to authored and deployed
 * onto a platform.
 *
 * The handler implements server authoring capabilies, OMAG server configurations are accumulated on the
 * platform that is identified in the Server author View configuration. While authoring servers, the caller is
 * not aware of where the server configurations are being accumulated.
 *
 * In addition to server authoring capabilities, this handler allows the known platforms, as defined in the view
 * configuration to be queried to see what servers are configured on them. The call can then deploy a server that has
 * been configured onto a particular platform.
 *
 */
public class ServerAuthorViewHandler {

    private static final String className = ServerAuthorViewHandler.class.getName();


    /*
    * viewServiceOptions should have been validated in the Admin layer.
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
                }
            ]
    */
    private Map<String, ResourceEndpoint> configuredPlatforms = null;  // map is keyed using platformRootURL
    private Map<String, ResourceEndpoint> configuredServerInstances = null;  // map is keyed using serverName+platformRootURL so each instance is unique
    private String userId = null;
    private String platformURL = null;

    /**
     * Default constructor for ServerAuthorViewHandler
     */
    public ServerAuthorViewHandler() {

    }


    /**
     * ServerAuthorViewHandler constructor with configured resourceEndpoints
     *
     * @param localServerUserId admin userid
     * @param metadataServerURL url of the platform to issue admin calls to store configuration
     * @param resourceEndpoints list of platforms
     */
    public ServerAuthorViewHandler(String localServerUserId, String metadataServerURL, List<ResourceEndpointConfig> resourceEndpoints) {

        this.userId = localServerUserId;
        this.platformURL = metadataServerURL;
        /*
         * Populate map of resources with their endpoints....
         */

        // TODO - It would be desirable to add validation rules to ensure uniqueness etc.

        configuredPlatforms = new HashMap<>();
        configuredServerInstances = new HashMap<>();

        if (resourceEndpoints != null && !resourceEndpoints.isEmpty()) {

            resourceEndpoints.forEach(res -> {

                String resCategory = res.getResourceCategory();
                ResourceEndpoint rep = new ResourceEndpoint(res);

                String resName = null;

                switch (resCategory) {
                    case "Platform":
                        resName = res.getPlatformName();
                        configuredPlatforms.put(resName, rep);
                        break;
                    // At this time there is no need to pick up servers from the config for this view service.
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
     * @return the map of resource endpoints
     */
    public Map<String, List<ResourceEndpoint>> getResourceEndpoints() {

        Map<String, List<ResourceEndpoint>> returnMap = new HashMap<>();

        List<ResourceEndpoint> platformList = null;
        List<ResourceEndpoint> serverList = null;

        if (!configuredPlatforms.isEmpty()) {
            platformList = new ArrayList<>();
            platformList.addAll(configuredPlatforms.values());
        }

        if (!configuredServerInstances.isEmpty()) {
            serverList = new ArrayList<>();
            serverList.addAll(configuredServerInstances.values());
        }

        returnMap.put("platformList", platformList);

        return returnMap;
    }

    /**
     * Get the server configurations associated with the platforms that this view service knows about as well as the
     * services it supports.
     *
     * @param userId     userId under which the request is performed
     * @param methodName The name of the method being invoked
     * @param auditLog  auditLog
     * @return the known platforms, which if active will contain their associated omag server configurations
     * @throws ServerAuthorViewServiceException the server author view has detected an error
     */
    public Set<Platform> getKnownPlatforms(String userId, String methodName, AuditLog auditLog) throws ServerAuthorViewServiceException {
        Set<Platform> knownPlatforms = new HashSet<>();
        try {
            for (String platformName : configuredPlatforms.keySet()) {
                ResourceEndpoint resourceEndpoint = configuredPlatforms.get(platformName);
                Platform platform = new Platform(resourceEndpoint.getPlatformName(), resourceEndpoint.getResourceDescription());
                platform.setPlatformStatus(PlatformStatus.UNKNOWN);
                // need to use the other constructor to pass the user and password from the configuration
                Set<OMAGServerConfig> omagServerConfigSet = null;
                try {
                    String platFormEndpoint = resourceEndpoint.getResourceRootURL();
                    OMAGServerPlatformConfigurationClient omagServerPlatformConfigurationClient = new OMAGServerPlatformConfigurationClient(userId, platFormEndpoint);
                    omagServerConfigSet =  omagServerPlatformConfigurationClient.getAllServerConfigurations();
                    platform.setPlatformStatus(PlatformStatus.ACTIVE);

                    // populate the platform with summaries of each server.
                    for (OMAGServerConfig omagServerConfig : omagServerConfigSet) {
                        String serverName = omagServerConfig.getLocalServerName();
                        StoredServer storedServer = new StoredServer();
                        storedServer.setStoredServerName(serverName);
                        storedServer.setServerType(omagServerConfig.getLocalServerType());
                        storedServer.setStoredServerDescription(omagServerConfig.getLocalServerDescription());
                        platform.addStoredServer(storedServer);
                    }
                    PlatformServicesClient platformServicesClient = new PlatformServicesClient(platformName, platFormEndpoint);

                    List<RegisteredOMAGService> accessServiceList = platformServicesClient.getAccessServices(userId);
                    platform.setAccessServices(accessServiceList);

                    List<RegisteredOMAGService> engineServicesList = platformServicesClient.getEngineServices(userId);
                    platform.setEngineServices(engineServicesList);

                    List<RegisteredOMAGService> integrationServiceList = platformServicesClient.getIntegrationServices(userId);
                    platform.setIntegrationServices(integrationServiceList);

                    List<RegisteredOMAGService> viewServiceList = platformServicesClient.getViewServices(userId);
                    platform.setViewServices(viewServiceList);

                } catch (OMAGConfigurationErrorException e) {
                    // if we have a configuration error, this is likely because we could not contact the platform using the platform root URL
                    // configured in this view service.
                    platform.setPlatformStatus(PlatformStatus.NOT_CONTACTABLE);
                } catch (PropertyServerException e) {
                    // create a new platform object in case it is partially filled in.
                    platform = new Platform();
                    platform.setPlatformStatus(PlatformStatus.NOT_CONTACTABLE);
                } catch (InvalidParameterException error) {
                    throw ServerAuthorExceptionHandler.mapOCFInvalidParameterException(className, methodName, error);
                } catch (UserNotAuthorizedException error) {
                    throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
                }
                knownPlatforms.add(platform);
            }
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        }

        return knownPlatforms;
    }


    /**
     * resolvePlatformRootURL
     * <p>
     * This method will look up the configured root URL for the named platform.
     *
     * @param platformName
     * @return resolved platform URL Root
     * @throws OMAGInvalidParameterException in valid platform name parameter
     */
    private String resolvePlatformRootURL(String platformName, String methodName) throws ServerAuthorViewServiceException {
        String platformRootURL = null;

        if (platformName != null) {
            ResourceEndpoint resource = configuredPlatforms.get(platformName);
            if (resource != null) {
                platformRootURL = resource.getResourceRootURL();
            }
        }
        if (platformName == null || platformRootURL == null) {
            throw new ServerAuthorViewServiceException(ServerAuthorViewErrorCode.INVALID_PARAMETER.getMessageDefinition(methodName, "platformName"),
                                                       className,
                                                       methodName);
        }

        return platformRootURL;
    }

    /**
     * Set the local repository to be in memory for the named server
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server being configured
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setInMemLocalRepository(String className, String methodName, String serverToBeConfiguredName
                                       ) throws ServerAuthorViewServiceException {

        try {
            MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                                    serverToBeConfiguredName,
                                                                                                                    this.platformURL
            );
            configurationClient.setInMemLocalRepository();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Set the local repository to be graph for the named server
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server being configured
     * @param storageProperties        storage properties
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setGraphLocalRepository(String className, String methodName, String serverToBeConfiguredName, Map<String, Object> storageProperties) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                                    serverToBeConfiguredName,
                                                                                                                    this.platformURL
            );
            configurationClient.setGraphLocalRepository(storageProperties);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Set the local repository to be read only for the named server
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server being configured
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setReadOnlyLocalRepository(String className, String methodName, String serverToBeConfiguredName) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                                    serverToBeConfiguredName,
                                                                                                                    this.platformURL
            );
            configurationClient.setReadOnlyLocalRepository();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connection  connection to the OMRS repository connector.
     * @throws ServerAuthorViewServiceException a server author exception
     */
    public void setPluginRepositoryConnection(String className, String methodName, String serverToBeConfiguredName, Connection connection) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                                    serverToBeConfiguredName,
                                                                                                                    this.platformURL
            );
            configurationClient.setPluginRepositoryConnection(connection);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Get the stored configuration for the named server
     *
     * @param className               class Name for diagnostic purposes
     * @param methodName              current operation
     * @param serverToBeRetrievedName name of the server whose configuration is to be retrieved
     * @return the omag server configuration
     * @throws ServerAuthorViewServiceException server author exception
     */
    public OMAGServerConfig getStoredConfiguration(String className, String methodName, String serverToBeRetrievedName) throws ServerAuthorViewServiceException {
        OMAGServerConfig config = null;


        try {
            OMAGServerConfigurationClient adminServicesClient = new OMAGServerConfigurationClient(this.userId,
                                                                                                  serverToBeRetrievedName,
                                                                                                  this.platformURL);
            config = adminServicesClient.getOMAGServerConfig();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
        return config;
    }

    /**
     * Deploy an OMAG Server configuration onto a target platform.
     *
     * @param className               class Name for diagnostic purposes
     * @param methodName              current operation
     * @param destinationPlatformName platform onto which the server is to be deployed
     * @param serverToBeDeployedName  name of the server to deploy
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void deployOMAGServerConfig(String className, String methodName, String destinationPlatformName, String serverToBeDeployedName) throws ServerAuthorViewServiceException {
        try {
            String destinationPlatformRootURL = resolvePlatformRootURL(destinationPlatformName, methodName);
            OMAGServerConfigurationClient adminServicesClient = new OMAGServerConfigurationClient(this.userId,
                                                                                                  serverToBeDeployedName,
                                                                                                  this.platformURL);
            adminServicesClient.deployOMAGServerConfig(destinationPlatformRootURL);

        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Set an OMAG Server's configuration
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to be configured
     * @param omagServerConfig         the server configuration we are setting
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setOMAGServerConfig(String className, String methodName, String serverToBeConfiguredName, OMAGServerConfig omagServerConfig) throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient adminServicesClient = new OMAGServerConfigurationClient(this.userId,
                                                                                                  serverToBeConfiguredName,
                                                                                                  this.platformURL);
            adminServicesClient.setOMAGServerConfig(omagServerConfig);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Configure an access service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the access service to configure
     * @param accessServiceOptions     access service options
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void configureAccessService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker, Map<String, Object> accessServiceOptions) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessPointConfigurationClient client = new MetadataAccessPointConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            if (accessServiceOptions == null) {
                accessServiceOptions = new HashMap<>();
            }
            client.configureAccessService(serviceURLMarker, accessServiceOptions);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Disable an access service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the access service to configure
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void disableAccessService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessPointConfigurationClient client = new MetadataAccessPointConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            client.disableAccessService(serviceURLMarker);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Configure all access services
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param accessServiceOptions     access service options to apply to all access services
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void configureAllAccessServices(String className, String methodName, String serverToBeConfiguredName, Map<String, Object> accessServiceOptions) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessPointConfigurationClient client = new MetadataAccessPointConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            if (accessServiceOptions == null) {
                accessServiceOptions = new HashMap<>();
            }
            client.configureAllAccessServices(accessServiceOptions);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Configure an view service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the view service to configure
     * @param viewServiceOptions     view service options
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void configureViewService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker, Map<String, Object> viewServiceOptions) throws ServerAuthorViewServiceException {
        try {
            ViewServerConfigurationClient client = new ViewServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            if (viewServiceOptions == null) {
                viewServiceOptions = new HashMap<>();
            }
            client.configureViewService(serviceURLMarker, viewServiceOptions);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Disable an view service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the view service to configure
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void disableViewService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker) throws ServerAuthorViewServiceException {
        try {
            ViewServerConfigurationClient client = new ViewServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.disableViewService(serviceURLMarker);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Configure an engine service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the engine service to configure
     * @param engineServiceOptions     engine service options
     * @param engines                  engines
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void configureEngineService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker, Map<String, Object> engineServiceOptions, List<EngineConfig> engines) throws ServerAuthorViewServiceException {
        try {
            EngineHostConfigurationClient client = new EngineHostConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.configureEngineService(serviceURLMarker, engineServiceOptions, engines);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Disable an view service
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param serviceURLMarker         identifier of the view service to configure
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void disableEngineService(String className, String methodName, String serverToBeConfiguredName, String serviceURLMarker) throws ServerAuthorViewServiceException {
        try {
            ViewServerConfigurationClient client = new ViewServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.disableViewService(serviceURLMarker);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }


    /**
     * set the Enterprise Access config
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param enterpriseAccessConfig   enterprise access config
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setEnterpriseAccessConfig(String className, String methodName, String serverToBeConfiguredName, EnterpriseAccessConfig enterpriseAccessConfig) throws ServerAuthorViewServiceException {
        try {
            MetadataAccessPointConfigurationClient client = new MetadataAccessPointConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            client.setEnterpriseAccessConfig(enterpriseAccessConfig);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * set the Event bus
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param connectorProvider        connector provider name
     * @param topicURLRoot             topic URL root
     * @param configurationProperties  configuration properties
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setEventBus(String className, String methodName, String serverToBeConfiguredName, String connectorProvider, String topicURLRoot, Map<String, Object> configurationProperties)
    throws ServerAuthorViewServiceException {
        try {

            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.setEventBus(connectorProvider, topicURLRoot, configurationProperties);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * set the default audit log
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void setDefaultAuditLog(String className, String methodName, String serverToBeConfiguredName) throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.setDefaultAuditLog();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * add a console audit log
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param supportedSeverities      a list of support severities
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void addConsoleAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, List<String> supportedSeverities)
    throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.addConsoleAuditLogDestination(supportedSeverities);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * add a SLF4J audit log
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param supportedSeverities      a list of support severities
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void addSLF4JAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, List<String> supportedSeverities)
    throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.addSLF4JAuditLogDestination(supportedSeverities);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * add a File audit log destination
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param supportedSeverities      a list of support severities
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void addFileAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, List<String> supportedSeverities)
    throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.addFileAuditLogDestination(supportedSeverities);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * add a Event Topic audit log destination
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param supportedSeverities      a list of support severities
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void addEventTopicAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, List<String> supportedSeverities)
    throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.addEventTopicAuditLogDestination(supportedSeverities);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * add an audit log destination specified by a connection
     *
     * @param className                class Name for diagnostic purposes
     * @param methodName               current operation
     * @param serverToBeConfiguredName name of the server to being configured
     * @param connection               connection to use for the audit log destination
     * @throws ServerAuthorViewServiceException server author exception
     */
    public void addAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, Connection connection)
    throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.addAuditLogDestination(connection);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
    /**
     * Update an audit log destination that is identified with the supplied destination name with
     * the supplied connection object.
     *
     * @param className            class name used for diagnostics
     * @param methodName           the current operation
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be updated
     * @param auditLogDestination connection object that defines the audit log destination
     * @throws ServerAuthorViewServiceException a server author exception
     */
    public void updateAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, String auditLogDestinationName, Connection auditLogDestination)
        throws ServerAuthorViewServiceException {
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.updateAuditLogDestination(auditLogDestinationName, auditLogDestination);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
    /**
     * Delete an audit log destination that is identified with the supplied destination name
     *
     * @param className            class name used for diagnostics
     * @param methodName           the current operation
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be deleted
     * @throws ServerAuthorViewServiceException a server author exception
     */
    public void deleteAuditLogDestination(String className, String methodName, String serverToBeConfiguredName, String auditLogDestinationName) throws ServerAuthorViewServiceException{
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.clearAuditLogDestination(auditLogDestinationName);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }

    /**
     * Get the active configuration of the named server
     *
     * @param className            class name used for diagnostics
     * @param methodName           the current operation
     * @param serverToRetrieveName the server to retrieve name
     * @return the activate configuration or
     * @throws ServerAuthorViewServiceException a server author exception
     */
    public OMAGServerConfig getActiveConfiguration(String className, String methodName, String serverToRetrieveName)
    throws ServerAuthorViewServiceException {
        OMAGServerConfig config = null;
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToRetrieveName,
                                                                                     this.platformURL);
            config = client.getOMAGServerInstanceConfig();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
        return config;
    }

    /**
     * Activate the stored configuration for the named server on the named destination platform
     *
     * @param className               class name for diagnostics
     * @param methodName              action being performed
     * @param destinationPlatformName name of the platform name on which the configuration should be activated
     * @param serverToBeActivatedName server name of the configuration to activate
     */
    public void activateWithStoredConfig(String className, String methodName, String
            destinationPlatformName, String serverToBeActivatedName) {
        // TODO not yet implemented

    }

    /**
     * Deactivate the stored configuration for the named server on the named destination platform permanently. The configuration associated with this
     * server is deleted, so it cannot be activated again.
     * <p>
     * To activate this server again on this platform, a new configuration must be created, which can then be activated.
     *
     * @param className                 class name for diagnostics
     * @param methodName                action being performed
     * @param destinationPlatformName   name of the platform name on which the configuration should be activated
     * @param serverToBeDeactivatedName server name of the configuration to deactivate
     */
    public void deactivateServerPermanently(String className, String methodName, String
            destinationPlatformName, String serverToBeDeactivatedName) {
        // TODO not yet implemented
    }

    /**
     * Deactivate the stored configuration for the named server on the named destination platform temporarily. The configuration
     * still exists after this operation, so it can be activated again
     *
     * @param className                 class name for diagnostics
     * @param methodName                action being performed
     * @param destinationPlatformName   name of the platform name on which the configuration should be activated
     * @param serverToBeDeactivatedName server name of the configuration to deactivate
     */
    public void deactivateServerTemporarily(String className, String methodName, String destinationPlatformName, String serverToBeDeactivatedName) {
        // TODO not yet implemented
    }

    /**
     * Get the supported severities for the audit log
     *
     * @return the list of the supported severities for  the audit log
     */
    public List<OMRSAuditLogReportSeverity> getSupportedAuditLogSeverities() {
       return OMRSAuditLogRecordSeverity.getSeverityList();
    }
    /**
     * Clear the audit log destinations associated with the server being configured
     *
     * @param serverToBeConfiguredName name of the server to be configured.
     * @throws ServerAuthorViewServiceException a server author exception
     *
     */
    public void clearAuditLogDestinations(String serverToBeConfiguredName)  throws ServerAuthorViewServiceException {
        final String methodName = "clearAuditLogDestinations";
        try {
            OMAGServerConfigurationClient client = new OMAGServerConfigurationClient(this.userId,
                                                                                     serverToBeConfiguredName,
                                                                                     this.platformURL);
            client.clearAuditLogDestinations();
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGConfigurationErrorException error) {
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS).
     *
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param serverToBeConfiguredName  server being configured
     * @param cohortName  name of the cohort.
     * @throws ServerAuthorViewServiceException error occurred during the registration of the cohort
     */
    public void addCohortRegistration(String serverToBeConfiguredName,
                                      String cohortName
                                    ) throws ServerAuthorViewServiceException {
        final String methodName = "addCohortRegistration";
        try {
            MetadataAccessStoreConfigurationClient client = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            client.addCohortRegistration(cohortName,null);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGConfigurationErrorException error) {
            // if we have a configuration error, this is likely because we could not contact the platform using the platform root URL
            // configured in this view service.
           throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param serverToBeConfiguredName  server being configured
     * @param cohortName  name of the cohort.
     * @throws ServerAuthorViewServiceException error occurred during the unregistration of the cohort
     */
    public void removeCohortRegistration(String serverToBeConfiguredName, String cohortName ) throws ServerAuthorViewServiceException {
        final String methodName = "removeCohortRegistration";
        try {
            MetadataAccessStoreConfigurationClient client = new MetadataAccessStoreConfigurationClient(this.userId,
                                                                                                       serverToBeConfiguredName,
                                                                                                       this.platformURL);
            client.clearCohortRegistration(cohortName);
        } catch (OMAGInvalidParameterException error) {
            throw ServerAuthorExceptionHandler.mapOMAGInvalidParameterException(className, methodName, error);
        } catch (OMAGNotAuthorizedException error) {
            throw ServerAuthorExceptionHandler.mapToUserNotAuthorizedException(className, methodName);
        } catch (OMAGConfigurationErrorException error) {
            // if we have a configuration error, this is likely because we could not contact the platform using the platform root URL
            // configured in this view service.
            throw ServerAuthorExceptionHandler.mapOMAGConfigurationErrorException(className, methodName, error);
        }
    }
}

