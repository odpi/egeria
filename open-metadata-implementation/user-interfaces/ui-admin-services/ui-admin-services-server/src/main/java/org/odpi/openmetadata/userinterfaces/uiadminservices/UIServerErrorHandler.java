/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.uiadminservices;

import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UINotAuthorizedException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIInvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIConfigurationErrorException;

/**
 * UIServerErrorHandler provides common error handling routines for the ui admin services
 */
class UIServerErrorHandler
{
    /**
     * Default constructor
     */
    public UIServerErrorHandler()
    {
    }


    /**
     * Validate that the user id is not null.
     *
     * @param userId  user name passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws UINotAuthorizedException no userId provided
     */
    void validateUserId(String userId,
                        String serverName,
                        String methodName) throws UINotAuthorizedException
    {
        if (userId == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_USER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new UINotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @throws UIInvalidParameterException null server name
     */
    void validateServerName(String serverName,
                            String methodName) throws UIInvalidParameterException
    {
        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if (serverName == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_LOCAL_SERVER_NAME;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new UIInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param configServerName serverName passed in config (should match request name)
     * @param methodName  method being called
     * @throws UIConfigurationErrorException incompatible server names
     */
    void validateConfigServerName(String serverName,
                                  String configServerName,
                                  String methodName) throws UIConfigurationErrorException
    {
        if (! serverName.equals(configServerName))
        {
            UIAdminErrorCode errorCode = UIAdminErrorCode.INCOMPATIBLE_SERVER_NAMES;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,
                                                                                                            configServerName);

            throw new UIConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());

        }
    }

    /**
     * Validate that the connection is not null.
     *
     * @param connection  connection passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws UIInvalidParameterException the connection is null
     */
    void validateConnection(Connection connection,
                            String     serverName,
                            String     methodName) throws UIInvalidParameterException
    {
        if (connection == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new UIInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Validate that the connection is not null.
     *
     * @param connection  connection passed on the request
     * @param methodName  method called
     * @throws UIInvalidParameterException the connection is null
     */
    void validateConnection(Connection connection,
                            String     methodName) throws UIInvalidParameterException
    {
        if (connection == null)
        {
            UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_PLATFORM_CONNECTION;
            String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new UIInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }
}
