/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;


public class OpenMetadataPlatformSecurityVerifier
{
    private static Connection                            platformSecurityConnection = null;
    private static OpenMetadataPlatformSecurityConnector platformSecurityConnector  = null;

    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public static synchronized void setPlatformSecurityConnection(String       userId,
                                                                  Connection   connection) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "setPlatformSecurityConnection";

        /*
         * Validate that someone has authority to override the connector.
         */
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserAsOperatorForPlatform(userId);
        }


        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       newConnector    = connectorBroker.getConnector(connection);

            platformSecurityConnector = (OpenMetadataPlatformSecurityConnector)newConnector;
            platformSecurityConnection = connection;
        }
        catch (Throwable error)
        {
            /*
             * The assumption is that any exceptions creating the new connector are down to a bad connection
             */
            OpenMetadataSecurityErrorCode errorCode    = OpenMetadataSecurityErrorCode.BAD_PLATFORM_SECURITY_CONNECTION;
            String                        errorMessage = errorCode.getErrorMessageId()
                                                       + errorCode.getFormattedErrorMessage(error.getMessage(), connection.toString());

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                OpenMetadataPlatformSecurityVerifier.class.getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error,
                                                "connection");
        }
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public static synchronized Connection getPlatformSecurityConnection(String       userId) throws UserNotAuthorizedException
    {
        /*
         * Validate that someone has authority to retrieve the connector.
         */
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserAsOperatorForPlatform(userId);
        }

        return platformSecurityConnection;
    }


    /**
     * Clear the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public static synchronized void clearPlatformSecurityConnection(String   userId) throws UserNotAuthorizedException
    {
        /*
         * Validate that someone has authority to override the connector.
         */
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserAsOperatorForPlatform(userId);
        }

        platformSecurityConnection = null;
        platformSecurityConnector  = null;
    }

    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public static synchronized void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserForNewServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public static synchronized void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserAsOperatorForPlatform(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    public static synchronized void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        if (platformSecurityConnector != null)
        {
            platformSecurityConnector.validateUserAsInvestigatorForPlatform(userId);
        }
    }


    /**
     * Return the Open Metadata Server Security Connector for the connection.
     *
     * @param serverName name of the server
     * @param connection connection from the configuration document
     * @return connector or null
     * @throws InvalidParameterException connection did not create a connector
     */
    public  static OpenMetadataServerSecurityConnector getServerSecurityConnector(String     serverName,
                                                                                  Connection connection) throws InvalidParameterException
    {
        final String methodName = "getServerSecurityConnector";

        OpenMetadataServerSecurityConnector serverSecurityConnector = null;

        if (connection != null)
        {
            try
            {
                ConnectorBroker connectorBroker = new ConnectorBroker();
                Connector       connector = connectorBroker.getConnector(connection);

                serverSecurityConnector = (OpenMetadataServerSecurityConnector)connector;
            }
            catch (Throwable error)
            {
                /*
                 * The assumption is that any exceptions creating the new connector are down to a bad connection
                 */
                OpenMetadataSecurityErrorCode errorCode    = OpenMetadataSecurityErrorCode.BAD_SERVER_SECURITY_CONNECTION;
                String                        errorMessage = errorCode.getErrorMessageId()
                                                           + errorCode.getFormattedErrorMessage(serverName,
                                                                                                error.getMessage(),
                                                                                                connection.toString());

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    OpenMetadataPlatformSecurityVerifier.class.getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction(),
                                                    error,
                                                    "connection");
            }
        }

        return serverSecurityConnector;
    }
}
