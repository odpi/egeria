/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStore;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * OMAGServerAdminStoreServices provides the capability to store and retrieve configuration documents.
 * A configuration document provides the configuration information for a server.  By default, a
 * server's configuration document is stored in its own file.  However, it is possible to override
 * the default location using setConfigurationStoreConnection.  This override affects all
 * server instances in this process.
 */
public class OMAGServerAdminStoreServices
{
    private static Connection          configurationStoreConnection = null;
    private static OMAGServerConfig    defaultServerConfig          = new OMAGServerConfig();
    private static Map<String, String> placeHolderVariables         = null;

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminStoreServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private final OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();
    private final OMAGServerErrorHandler     errorHandler     = new OMAGServerErrorHandler();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    /**
     * Override the default server configuration document.
     *
     * @param userId              calling user.
     * @param defaultServerConfig values to include in every new configured server.
     * @return void response
     */
    public synchronized VoidResponse setDefaultOMAGServerConfig(String userId,
                                                                OMAGServerConfig defaultServerConfig)
    {
        final String methodName    = "setDefaultOMAGServerConfig";
        final String parameterName = "defaultServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            errorHandler.validatePropertyNotNull(defaultServerConfig, parameterName, null, methodName);

            OMAGServerAdminStoreServices.defaultServerConfig = defaultServerConfig;
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the default server configuration document.
     *
     * @param userId calling user
     * @return OMAGServerConfig response
     */
    public synchronized OMAGServerConfigResponse getDefaultOMAGServerConfig(String userId)
    {
        final String methodName = "getDefaultOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            response.setOMAGServerConfig(defaultServerConfig);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the default configuration document.
     *
     * @param userId calling user
     * @return void response
     */
    public synchronized VoidResponse clearDefaultOMAGServerConfig(String userId)
    {
        final String methodName = "clearDefaultServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            defaultServerConfig = new OMAGServerConfig();
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the default server configuration document.
     *
     * @return default server config
     */
    private synchronized OMAGServerConfig getDefaultServerConfig()
    {
        return defaultServerConfig;
    }


    /**
     * Set up the placeholder variables that will be used on each server start up.
     *
     * @param userId               calling user.
     * @param placeholderVariables map of variable name to value.
     * @return void response
     */
    public synchronized VoidResponse setPlaceholderVariables(String userId,
                                                             Map<String, String> placeholderVariables)
    {
        final String methodName    = "setPlaceholderVariables";
        final String parameterName = "placeholderVariables";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            errorHandler.validatePropertyNotNull(placeholderVariables, parameterName, null, methodName);

            OMAGServerAdminStoreServices.placeHolderVariables = placeholderVariables;
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the placeholder variables that will be used on each server start up.
     *
     * @param userId calling user
     * @return string map response
     */
    public synchronized StringMapResponse getPlaceholderVariables(String userId)
    {
        final String methodName = "getPlaceholderVariables";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        StringMapResponse response = new StringMapResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            response.setStringMap(placeHolderVariables);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the placeholder variables.
     *
     * @param userId calling user
     * @return void response
     */
    public synchronized VoidResponse clearPlaceholderVariables(String userId)
    {
        final String methodName = "clearPlaceholderVariables";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            placeHolderVariables = null;
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the placeholder variables.
     *
     * @return string map or null
     */
    private synchronized Map<String, String> getPlaceholderVariables()
    {
        return placeHolderVariables;
    }


    /**
     * Override the default location of the configuration documents.
     *
     * @param userId     calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    public synchronized VoidResponse setConfigurationStoreConnection(String userId,
                                                                     Connection connection)
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
        catch (Exception error)
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
    public synchronized ConnectionResponse getConfigurationStoreConnection(String userId)
    {
        final String methodName = "getConfigurationStoreConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            response.setConnection(configurationStoreConnection);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
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
    public synchronized VoidResponse clearConfigurationStoreConnection(String userId)
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
        catch (Exception error)
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
     * @param serverName name of the server
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
     * @param serverName name of the server
     * @param methodName method requesting the server details
     * @return configuration connector file
     * @throws OMAGInvalidParameterException the connector could not be created from the supplied config.
     */
    private OMAGServerConfigStore getServerConfigStore(String serverName,
                                                       String methodName) throws OMAGInvalidParameterException
    {
        Connection connection = this.getConnection(serverName);

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMAGServerConfigStore serverConfigStore = (OMAGServerConfigStore) connector;

            serverConfigStore.setServerName(serverName);

            connector.start();

            return serverConfigStore;
        }
        catch (Exception error)
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

        Connection connection = this.getConnectionForRetrieveAll();

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMAGServerConfigStore serverConfigStore = (OMAGServerConfigStore) connector;

            connector.start();
            return getOMAGServerConfigStoreRetrieveAll(serverConfigStore, methodName);
        }
        catch (Exception error)
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
     * @param serverConfigStore the server config store - note the configured config store may not support this operation
     * @param methodName        current operation
     * @return the store to use for the retrieve all server configurations
     * @throws OMAGConfigurationErrorException the store does not support the retrieve all configured servers for this platform operation
     */
    OMAGServerConfigStoreRetrieveAll getOMAGServerConfigStoreRetrieveAll(OMAGServerConfigStore serverConfigStore, String methodName) throws OMAGConfigurationErrorException
    {
        OMAGServerConfigStoreRetrieveAll omagServerConfigStoreRetrieveAll;

        if (serverConfigStore instanceof OMAGServerConfigStoreRetrieveAll)
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
     * @param userId     calling user
     * @param serverName name of the server
     * @param methodName method requesting the server details
     * @return configuration properties
     * @throws OMAGInvalidParameterException problem with the configuration file
     * @throws OMAGNotAuthorizedException    user not authorized to make these changes
     * @throws OMAGConfigurationErrorException  problem working with configuration document
     */
    OMAGServerConfig getServerConfig(String userId,
                                     String serverName,
                                     String methodName) throws OMAGInvalidParameterException,
                                                               OMAGNotAuthorizedException,
                                                               OMAGConfigurationErrorException
    {
        return getServerConfig(userId, serverName, true, methodName);
    }


    /**
     * Retrieve any saved configuration for this server.
     *
     * @param userId     calling user
     * @param serverName name of the server
     * @param adminCall  flag to indicate whether the call is to change or just read the configuration
     * @param methodName method requesting the server details
     * @return configuration properties
     * @throws OMAGInvalidParameterException problem with the configuration file
     * @throws OMAGNotAuthorizedException    user not authorized to make these changes
     * @throws OMAGConfigurationErrorException problem with the configuration document
     */
    public OMAGServerConfig getServerConfig(String  userId,
                                            String  serverName,
                                            boolean adminCall,
                                            String  methodName) throws OMAGInvalidParameterException,
                                                                       OMAGNotAuthorizedException,
                                                                       OMAGConfigurationErrorException
    {
        OMAGServerConfigStore serverConfigStore = getServerConfigStore(serverName, methodName);
        OMAGServerConfig      serverConfig      = null;

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

            serverConfig = new OMAGServerConfig(this.getDefaultServerConfig());
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

            OMAGServerConfig resolvedServerConfig = resolvePlaceholdersInConfig(serverConfig);

            try
            {
                OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

                securityVerifier.registerSecurityValidator(resolvedServerConfig.getLocalServerUserId(),
                                                           serverName,
                                                           null,
                                                           resolvedServerConfig.getServerSecurityConnection());

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

        return serverConfig;
    }


    /**
     * Retrieve any saved configuration for this server.
     *
     * @param userId     calling user
     * @param serverName name of the server
     * @param methodName method requesting the server details
     * @return configuration properties
     * @throws OMAGInvalidParameterException   problem with the configuration file
     * @throws OMAGNotAuthorizedException      user not authorized to make these changes
     * @throws OMAGConfigurationErrorException unable to parse the OMAGServerConfig
     */
    public OMAGServerConfig getServerConfigForStartUp(String userId,
                                                      String serverName,
                                                      String methodName) throws OMAGInvalidParameterException,
                                                                                OMAGNotAuthorizedException,
                                                                                OMAGConfigurationErrorException
    {
        OMAGServerConfigStore serverConfigStore = getServerConfigStore(serverName, methodName);
        OMAGServerConfig      serverConfig      = null;

        if (serverConfigStore != null)
        {
            serverConfig = serverConfigStore.retrieveServerConfig();
        }

        if (serverConfig == null)
        {
            throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NO_CONFIG_DOCUMENT.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName);
        }

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


        /*
         * Replace placeholder variables with real values if required.
         */
        serverConfig = resolvePlaceholdersInConfig(serverConfig);

        /*
         * Double check that the server config matches the requested server name
         */
        validateConfigServerName(serverName, serverConfig.getLocalServerName(), methodName);

        /*
         * Check that the calling user has access to the server config.
         */
        try
        {
            OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

            securityVerifier.registerSecurityValidator(serverConfig.getLocalServerUserId(),
                                                       serverName,
                                                       null,
                                                       serverConfig.getServerSecurityConnection());

            securityVerifier.validateUserAsServerOperator(userId);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }
        catch (UserNotAuthorizedException error)
        {
            throw new OMAGNotAuthorizedException(error.getReportedErrorMessage(), error);
        }

        serverConfig.setLocalServerName(serverName);

        /*
         * The default configurations used in the docker containers do not specify the local serverId UUIDs.
         * They are set up the first time the server starts so that they are unique for each container instance
         * (in case the containers are connected).
         */
        if (serverConfig.getLocalServerId() == null)
        {
            serverConfig.setLocalServerId(UUID.randomUUID().toString());
            serverConfigStore.saveServerConfig(serverConfig);
        }

        return serverConfig;
    }


    /**
     * Replace each of the placeholders in the config with supplied placeholder values.
     *
     * @param retrievedServerConfig configuration document from the store
     * @return resolved server configuration document
     * @throws OMAGConfigurationErrorException problem working with the configuration doc
     */
    private OMAGServerConfig resolvePlaceholdersInConfig(OMAGServerConfig retrievedServerConfig) throws OMAGConfigurationErrorException
    {
        final String methodName = "resolvePlaceholdersInConfig";

        Map<String, String> currentPlaceholderValues = this.getPlaceholderVariables();

        if ((currentPlaceholderValues != null) && (! currentPlaceholderValues.isEmpty()))
        {
            try
            {
                String omagServerConfigString = OBJECT_MAPPER.writeValueAsString(retrievedServerConfig);

                for (String variableName : currentPlaceholderValues.keySet())
                {
                    String   regExMatchString = Pattern.quote("~{" + variableName + "}~");
                    String[] configBits       = omagServerConfigString.split(regExMatchString);

                    if (configBits.length > 1)
                    {
                        StringBuilder newConfigString = new StringBuilder();
                        boolean       firstPart       = true;

                        for (String configBit : configBits)
                        {
                            if (!firstPart)
                            {
                                newConfigString.append(currentPlaceholderValues.get(variableName));
                            }

                            firstPart = false;

                            if (configBit != null)
                            {
                                newConfigString.append(configBit);
                            }
                        }

                        omagServerConfigString = newConfigString.toString();
                    }
                }

                return OBJECT_MAPPER.readValue(omagServerConfigString, OMAGServerConfig.class);
            }
            catch (JsonProcessingException parsingError)
            {
                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.CONFIG_DOCUMENT_PARSE_ERROR.getMessageDefinition(retrievedServerConfig.getLocalServerName(),
                                                                                                                              parsingError.getClass().getName(),
                                                                                                                              parsingError.getMessage()),
                                                          this.getClass().getName(),
                                                          methodName);
            }
        }

        return retrievedServerConfig;
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
