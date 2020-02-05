/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.OMAGServerExceptionHandler;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.userinterface.adminservices.rest.ConnectionResponse;

import org.odpi.openmetadata.userinterface.adminservices.store.UIServerConfigStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UIServerAdminStoreServices provides the capability to configStore and retrieve configuration documents.
 *
 * A configuration document provides the configuration information for a server.  By default, a
 * server's configuration document is stored in its own file.  However, it is possible to override
 * the default location using setConfigurationStoreConnection.  This override affects all
 * server instances in this process.
 */
public class UIServerAdminStoreServices
{
    private static Connection  configurationStoreConnection = null;

    private static final Logger log = LoggerFactory.getLogger(UIServerAdminStoreServices.class);

    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();
    private UIServerErrorHandler errorHandler = new UIServerErrorHandler();

    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real configStore.
     * @return void response
     */
    public synchronized VoidResponse setConfigurationStoreConnection(String       userId,
                                                                     Connection   connection)
    {
        final String methodName = "setConfigurationStoreConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            errorHandler.validateConnection(connection, methodName);

            configurationStoreConnection = connection;
        }
        catch (InvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the connection object for the configuration configStore.  Null is returned if the server should
     * use the default configStore.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized ConnectionResponse getConfigurationStoreConnection(String       userId)
    {
        final String methodName = "getConfigurationStoreConnection";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            response.setConnection(configurationStoreConnection);
        }
        catch (Throwable   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Clear the connection object for the configuration configStore.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized VoidResponse clearConfigurationStoreConnection(String   userId)
    {
        final String methodName = "clearConfigurationStoreConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);

            configurationStoreConnection = null;
        }
        catch (Throwable   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

    /**
     * Retrieve the connection for the configuration document configStore.  If a connection has been provided by an
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

            return connectorConfigurationFactory.getUIServerConfigConnection(serverName);
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
     * @throws OMAGNotAuthorizedException the connector could not be created from the supplied config.
     */
    private UIServerConfigStore getServerConfigStore(String   serverName,
                                                     String   methodName) throws OMAGNotAuthorizedException
    {
        Connection   connection = this.getConnection(serverName);

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            return (UIServerConfigStore) connector;
        }
        catch (Throwable   error)
        {
            UIAdminErrorCode errorCode = UIAdminErrorCode.BAD_CONFIG_FILE;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(serverName, methodName, error.getMessage());

            throw new OMAGNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    error);
        }
    }


    /**
     * Retrieve any saved configuration for this server.
     *
     * @param userId calling user
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @return  configuration properties
     * @throws OMAGNotAuthorizedException problem with the configuration file
     */
    public UIServerConfig getServerConfig(String   userId,
                                       String   serverName,
                                       String   methodName) throws OMAGNotAuthorizedException, OMAGInvalidParameterException {
        UIServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);
        UIServerConfig        serverConfig      = null;

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
                throw new OMAGNotAuthorizedException(error);
            }

            serverConfig = new UIServerConfig();
            serverConfig.setVersionId(UIServerConfig.VERSION_ONE);
        }
        else
        {
            try
            {
                OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

                securityVerifier.registerSecurityValidator(serverConfig.getLocalServerUserId(),
                                                                                        serverName,
                                                                                        null,
                                                                                        serverConfig.getServerSecurityConnection());

                securityVerifier.validateUserAsServerAdmin(userId);
            }
            catch (InvalidParameterException error)
            {
                throw new OMAGInvalidParameterException(error);
            } catch (UserNotAuthorizedException error) {
                throw new OMAGNotAuthorizedException(error);
            }
        }

        serverConfig.setLocalServerName(serverName);

        return serverConfig;

    }


    /**
     * Save the server's config ...
     *
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @param serverConfig  properties to save
     * @throws OMAGNotAuthorizedException problem with the config file
     */
    public void saveServerConfig(String            serverName,
                          String            methodName,
                          UIServerConfig  serverConfig) throws OMAGNotAuthorizedException
    {
        UIServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);

        if (serverConfigStore != null)
        {
            if (serverConfig != null)
            {
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
}
