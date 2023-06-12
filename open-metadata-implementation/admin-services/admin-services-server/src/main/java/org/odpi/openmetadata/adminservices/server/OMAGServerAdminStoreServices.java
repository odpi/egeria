/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStore;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * OMAGServerAdminStoreServices provides the capability to store and retrieve configuration documents.
 *
 * A configuration document provides the configuration information for a server.  By default, a
 * server's configuration document is stored in its own file.  However, it is possible to override
 * the default location using setConfigurationStoreConnection.  This override affects all
 * server instances in this process.
 */
public class OMAGServerAdminStoreServices
{
    private static Connection  configurationStoreConnection = null;

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminStoreServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private final OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();
    private final OMAGServerErrorHandler     errorHandler     = new OMAGServerErrorHandler();

    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    public synchronized VoidResponse setConfigurationStoreConnection(String       userId,
                                                                     Connection   connection)
    {
        final String methodName = "setConfigurationStoreConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            errorHandler.validatePlatformConnection(connection, methodName);

            configurationStoreConnection = connection;
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized ConnectionResponse getConfigurationStoreConnection(String       userId)
    {
        final String methodName = "getConfigurationStoreConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            response.setConnection(configurationStoreConnection);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the connection object for the configuration store.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized VoidResponse clearConfigurationStoreConnection(String   userId)
    {
        final String methodName = "clearConfigurationStoreConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            configurationStoreConnection = null;
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the connection for the configuration document store.  If a connection has been provided by an
     * external party then return that - otherwise extract the file connector for the server.
     *
     * @param serverName  name of the server
     * @return Connection object
     */
    private synchronized Connection getConnection(String serverName)
    {
        if (configurationStoreConnection == null)
        {
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            return connectorConfigurationFactory.getServerConfigConnection(serverName);
        }
        else
        {
            return configurationStoreConnection;
        }
    }

    /**
     * Retrieve the connection for the configuration document store.  If a connection has been provided by an
     * external party then return that - otherwise extract the file connector for the server.
     *
     * @return Connection object
     */
    private synchronized Connection getConnectionForRetrieveAll()
    {
        if (configurationStoreConnection == null)
        {
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            return connectorConfigurationFactory.getServerConfigConnectionForRetrieveAll();
        }
        else
        {
            return configurationStoreConnection;
        }
    }



    /**
     * Retrieve the connection to the config file.
     *
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @return configuration connector file
     * @throws OMAGInvalidParameterException the connector could not be created from the supplied config.
     */
    private OMAGServerConfigStore getServerConfigStore(String   serverName,
                                                       String   methodName) throws OMAGInvalidParameterException
    {
        Connection   connection = this.getConnection(serverName);

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMAGServerConfigStore serverConfigStore = (OMAGServerConfigStore) connector;

            serverConfigStore.setServerName(serverName);

            connector.start();

            return serverConfigStore;
        }
        catch (Exception   error)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.BAD_CONFIG_FILE.getMessageDefinition(serverName,
                                                                                                            methodName,
                                                                                                            error.getClass().getName(),
                                                                                                            error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
        }
    }
    /**
     * Retrieve the connection to the config files.
     *
     * @return configuration connector file
     * @throws OMAGConfigurationErrorException A connection error occurred while attempting to access the server config store.
     */
    private OMAGServerConfigStoreRetrieveAll getServerConfigStoreForRetrieveAll() throws OMAGConfigurationErrorException
    {
        final String methodName = "getServerConfigStoreForRetrieveAll";

        Connection   connection = this.getConnectionForRetrieveAll();

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMAGServerConfigStore serverConfigStore = (OMAGServerConfigStore) connector;

            connector.start();
            return getOMAGServerConfigStoreRetrieveAll(serverConfigStore, methodName);
        }
        catch (Exception   error)
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNABLE_TO_OBTAIN_SERVER_CONFIG_STORE.getMessageDefinition(methodName,
                                                                                                            error.getClass().getName(),
                                                                                                            error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
        }
    }

    /**
     * Get the OMAG Server Config store for retrieving all the server configurations associated with this platform.
     *
     * @param serverConfigStore the server config store- note this may not support this operations
     * @param methodName current operation
     * @return the store to use for the retrieve all server configurations
     * @throws OMAGConfigurationErrorException the store does not support the retrieve all configured servers for this platform operation
     */
    OMAGServerConfigStoreRetrieveAll getOMAGServerConfigStoreRetrieveAll(OMAGServerConfigStore serverConfigStore, String  methodName) throws OMAGConfigurationErrorException
    {
        OMAGServerConfigStoreRetrieveAll omagServerConfigStoreRetrieveAll;

        if (serverConfigStore instanceof  OMAGServerConfigStoreRetrieveAll)
        {
            omagServerConfigStoreRetrieveAll = (OMAGServerConfigStoreRetrieveAll) serverConfigStore;
        }
        else
        {
            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.RETRIEVE_ALL_CONFIGS_NOT_SUPPORTED.getMessageDefinition(),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return omagServerConfigStoreRetrieveAll;
    }


    /**
     * Retrieve any saved configuration for this server.
     *
     * @param userId calling user
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @return  configuration properties
     * @throws OMAGInvalidParameterException problem with the configuration file
     * @throws OMAGNotAuthorizedException user not authorized to make these changes
     */
    OMAGServerConfig getServerConfig(String   userId,
                                     String   serverName,
                                     String   methodName) throws OMAGInvalidParameterException,
                                                                 OMAGNotAuthorizedException
    {
        return getServerConfig(userId, serverName, true, methodName);
    }


    /**
     * Retrieve any saved configuration for this server.
     *
     * @param userId calling user
     * @param serverName  name of the server
     * @param adminCall flag to indicate whether the
     * @param methodName  method requesting the server details
     * @return  configuration properties
     * @throws OMAGInvalidParameterException problem with the configuration file
     * @throws OMAGNotAuthorizedException user not authorized to make these changes
     */
    public OMAGServerConfig getServerConfig(String userId,
                                            String serverName,
                                            boolean adminCall,
                                            String methodName) throws OMAGInvalidParameterException,
                                                                 OMAGNotAuthorizedException
    {
        OMAGServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);
        OMAGServerConfig        serverConfig      = null;

        if (serverConfigStore != null)
        {
            serverConfig = serverConfigStore.retrieveServerConfig();
        }

        if (serverConfig == null)
        {
            try
            {
                OpenMetadataPlatformSecurityVerifier.validateUserForNewServer(userId);
            }
            catch (UserNotAuthorizedException error)
            {
                throw new OMAGNotAuthorizedException(error.getReportedErrorMessage(), error);
            }

            serverConfig = new OMAGServerConfig();
            serverConfig.setVersionId(OMAGServerConfig.VERSION_TWO);
            serverConfig.setLocalServerType(OMAGServerConfig.defaultLocalServerType);
        }
        else
        {
            String  versionId           = serverConfig.getVersionId();
            boolean isCompatibleVersion = false;

            if (versionId == null)
            {
                versionId = OMAGServerConfig.VERSION_ONE;
            }

            for (String compatibleVersion : OMAGServerConfig.COMPATIBLE_VERSIONS)
            {
                if (compatibleVersion.equals(versionId))
                {
                    isCompatibleVersion = true;
                    break;
                }
            }

            if (!isCompatibleVersion)
            {
                throw new OMAGInvalidParameterException(OMAGAdminErrorCode.INCOMPATIBLE_CONFIG_FILE.getMessageDefinition(serverName,
                                                                                                                         versionId,
                                                                                                                         OMAGServerConfig.COMPATIBLE_VERSIONS.toString()),
                                                        this.getClass().getName(),
                                                        methodName);
            }

            validateConfigServerName(serverName, serverConfig.getLocalServerName(), methodName);

            try
            {
                OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

                securityVerifier.registerSecurityValidator(serverConfig.getLocalServerUserId(),
                                                           serverName,
                                                           null,
                                                           serverConfig.getServerSecurityConnection());

                if (adminCall)
                {
                    securityVerifier.validateUserAsServerAdmin(userId);
                }
                else
                {
                    securityVerifier.validateUserAsServerOperator(userId);
                }
            }
            catch (InvalidParameterException error)
            {
                throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
            }
            catch (UserNotAuthorizedException error)
            {
                throw new OMAGNotAuthorizedException(error.getReportedErrorMessage(), error);
            }
        }

        serverConfig.setLocalServerName(serverName);

        /*
         * Refresh the definition of any access service to match the current platform implementation.
         */
        if (serverConfig.getAccessServicesConfig() != null)
        {
            for (AccessServiceConfig accessServiceConfig : serverConfig.getAccessServicesConfig())
            {
                if (accessServiceConfig != null)
                {
                    AccessServiceDescription description = AccessServiceDescription.getAccessServiceDefinition(accessServiceConfig.getAccessServiceId());

                    if (description != null)
                    {
                        accessServiceConfig.setAccessServiceName(description.getAccessServiceName());
                        accessServiceConfig.setAccessServiceDevelopmentStatus(description.getAccessServiceDevelopmentStatus());
                        accessServiceConfig.setAccessServiceFullName(description.getAccessServiceFullName());
                        accessServiceConfig.setAccessServiceDescription(description.getAccessServiceDescription());
                        accessServiceConfig.setAccessServiceURLMarker(description.getAccessServiceURLMarker());
                        accessServiceConfig.setAccessServiceWiki(description.getAccessServiceWiki());
                    }
                }
            }
        }

        /*
         * Refresh the definition of any engine service to match the current platform implementation.
         */
        if (serverConfig.getEngineHostServicesConfig() != null)
        {
            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig.getEngineServiceConfigs() != null)
            {
                for (EngineServiceConfig engineServiceConfig : engineHostServicesConfig.getEngineServiceConfigs())
                {
                    if (engineServiceConfig != null)
                    {
                        EngineServiceDescription description = EngineServiceDescription.getEngineServiceDefinition(engineServiceConfig.getEngineServiceId());

                        if (description != null)
                        {
                            engineServiceConfig.setEngineServiceName(description.getEngineServiceName());
                            engineServiceConfig.setEngineServiceDevelopmentStatus(description.getEngineServiceDevelopmentStatus());
                            engineServiceConfig.setEngineServiceFullName(description.getEngineServiceFullName());
                            engineServiceConfig.setEngineServiceDescription(description.getEngineServiceDescription());
                            engineServiceConfig.setEngineServiceURLMarker(description.getEngineServiceURLMarker());
                            engineServiceConfig.setEngineServiceWiki(description.getEngineServiceWiki());
                        }
                    }
                }
            }
        }

        /*
         * Refresh the definition of any integration service to match the current platform implementation.
         */
        if (serverConfig.getIntegrationServicesConfig() != null)
        {
            for (IntegrationServiceConfig integrationServiceConfig : serverConfig.getIntegrationServicesConfig())
            {
                if (integrationServiceConfig != null)
                {
                    IntegrationServiceDescription description = IntegrationServiceDescription.getIntegrationServiceDefinition(integrationServiceConfig.getIntegrationServiceId());

                    if (description != null)
                    {
                        integrationServiceConfig.setIntegrationServiceName(description.getIntegrationServiceName());
                        integrationServiceConfig.setIntegrationServiceDevelopmentStatus(description.getIntegrationServiceDevelopmentStatus());
                        integrationServiceConfig.setIntegrationServiceFullName(description.getIntegrationServiceFullName());
                        integrationServiceConfig.setIntegrationServiceDescription(description.getIntegrationServiceDescription());
                        integrationServiceConfig.setIntegrationServiceURLMarker(description.getIntegrationServiceURLMarker());
                        integrationServiceConfig.setIntegrationServiceWiki(description.getIntegrationServiceWiki());
                    }
                }
            }
        }

        /*
         * Refresh the definition of any view service to match the current platform implementation.
         */
        if (serverConfig.getViewServicesConfig() != null)
        {
            for (ViewServiceConfig viewServiceConfig : serverConfig.getViewServicesConfig())
            {
                if (viewServiceConfig != null)
                {
                    ViewServiceDescription description = ViewServiceDescription.getViewServiceDefinition(viewServiceConfig.getViewServiceId());

                    if (description != null)
                    {
                        viewServiceConfig.setViewServiceName(description.getViewServiceName());
                        viewServiceConfig.setViewServiceDevelopmentStatus(description.getViewServiceDevelopmentStatus());
                        viewServiceConfig.setViewServiceFullName(description.getViewServiceFullName());
                        viewServiceConfig.setViewServiceDescription(description.getViewServiceDescription());
                        viewServiceConfig.setViewServiceURLMarker(description.getViewServiceURLMarker());
                        viewServiceConfig.setViewServiceWiki(description.getViewServiceWiki());
                    }
                }
            }
        }
        return serverConfig;

    }


    /**
     * Save the server's config ...
     *
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @param serverConfig  properties to save
     * @throws OMAGInvalidParameterException problem with the config file
     */
    public void saveServerConfig(String serverName,
                                 String methodName,
                                 OMAGServerConfig serverConfig) throws OMAGInvalidParameterException
    {
        OMAGServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);

        if (serverConfigStore != null)
        {
            if (serverConfig != null)
            {
                validateConfigServerName(serverName, serverConfig.getLocalServerName(), methodName);
                serverConfigStore.saveServerConfig(serverConfig);
            }
            else
            {
                /*
                 * If the server config is null we delete the file rather than have an empty file hanging around.
                 */
                serverConfigStore.removeServerConfig();
            }
        }
    }

    /**
     * Retrieve all the saved OMAG Server configurations for this platform.  If the calling user is not authorized to access a particular
     * server's configuration it is removed from the list.
     *
     * @param userId calling user
     * @return  a set of OMAG Server configurations
     * @throws OMAGConfigurationErrorException the OMAG Server configuration connector defined in configuration does not support retrieve all servers call.
     */
    Set<OMAGServerConfig> retrieveAllServerConfigs(String   userId) throws OMAGConfigurationErrorException
    {
        final String methodName = "retrieveAllServerConfigs";

        OMAGServerConfigStoreRetrieveAll serverConfigStore = this.getServerConfigStoreForRetrieveAll();

        Set<OMAGServerConfig> configuredServers = serverConfigStore.retrieveAllServerConfigs();

        /*
         * This process ensures the userId is able to access each server.
         */
        if (configuredServers != null)
        {
            Set<OMAGServerConfig> validatedServers = new HashSet<>();

            for (OMAGServerConfig serverConfig : configuredServers)
            {
                if (serverConfig != null)
                {
                    try
                    {
                        OMAGServerConfig validatedConfig = this.getServerConfig(userId, serverConfig.getLocalServerName(), false, methodName);

                        if (validatedConfig != null)
                        {
                            validatedServers.add(validatedConfig);
                        }
                    }
                    catch (Exception error)
                    {
                        // skip server
                    }
                }
            }

            return validatedServers;
        }

        return null;
    }



    /**
     * If there is a mismatch in the server name inside the configuration document and the
     * requested server name it means there is an error in either the implementation or
     * configuration of the configuration document store.
     *
     * @param serverName  serverName passed on a request
     * @param configServerName serverName passed in config (should match request name)
     * @param methodName  method being called
     * @throws OMAGInvalidParameterException incompatible server names
     */
    private void validateConfigServerName(String serverName,
                                          String configServerName,
                                          String methodName) throws OMAGInvalidParameterException
    {
        if (! serverName.equals(configServerName))
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.INCOMPATIBLE_SERVER_NAMES.getMessageDefinition(serverName,
                                                                                                                      configServerName),
                                                    this.getClass().getName(),
                                                    methodName);

        }
    }
}
