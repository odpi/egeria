/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.OMAGErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStore;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

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


    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     */
    public synchronized VoidResponse setConfigurationStoreConnection(String       userId,
                                                                     Connection   connection)
    {
        configurationStoreConnection = connection;
        return new VoidResponse();
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
        ConnectionResponse  response = new ConnectionResponse();

        response.setConnection(configurationStoreConnection);

        return response;
    }


    /**
     * Clear the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized VoidResponse clearConfigurationStoreConnection(String   userId)
    {
        configurationStoreConnection = null;
        return new VoidResponse();
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

            return (OMAGServerConfigStore) connector;
        }
        catch (Throwable   error)
        {
            OMAGErrorCode errorCode = OMAGErrorCode.BAD_CONFIG_FILE;
            String        errorMessage = errorCode.getErrorMessageId()
                                       + errorCode.getFormattedErrorMessage(serverName, methodName, error.getMessage());

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
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
     * @param serverName  name of the server
     * @param methodName  method requesting the server details
     * @return  configuration properties
     * @throws OMAGInvalidParameterException problem with the configuration file
     */
    OMAGServerConfig getServerConfig(String   serverName,
                                     String   methodName) throws OMAGInvalidParameterException
    {
        OMAGServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);
        OMAGServerConfig        serverConfig      = null;


        if (serverConfigStore != null)
        {
            serverConfig = serverConfigStore.retrieveServerConfig();
        }

        if (serverConfig == null)
        {
            serverConfig = new OMAGServerConfig();
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
     * @throws OMAGInvalidParameterException problem with the config file
     */
    void saveServerConfig(String            serverName,
                          String            methodName,
                          OMAGServerConfig  serverConfig) throws OMAGInvalidParameterException
    {
        OMAGServerConfigStore   serverConfigStore = getServerConfigStore(serverName, methodName);

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
