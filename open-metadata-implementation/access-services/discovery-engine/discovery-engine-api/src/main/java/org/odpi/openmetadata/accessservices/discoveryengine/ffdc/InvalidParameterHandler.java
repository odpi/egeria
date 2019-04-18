/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.ffdc;


import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;

import java.util.List;

/**
 * InvalidParameterHandler is a common error handler.  It provides validation for incoming parameters.
 */
public class InvalidParameterHandler
{
    /**
     * Default constructor
     */
    public InvalidParameterHandler()
    {
    }


    /**
     * Throw an exception if a server URL has not been supplied on the constructor.
     *
     * @param omasServerURL url of the server
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException the server URL is not set
     */
    public void validateOMASServerURL(String omasServerURL,
                                      String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    public void validateUserId(String userId,
                               String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "userId");
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param guid           unique identifier to validate
     * @param guidParameter  name of the parameter that passed the guid.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validateGUID(String guid,
                             String guidParameter,
                             String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(guidParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param name           unique name to validate
     * @param nameParameter  name of the parameter that passed the name.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validateName(String name,
                             String nameParameter,
                             String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_NAME;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(nameParameter,
                                                                                     methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }


    /**
     * Throw an exception if the supplied paging values don't make sense.
     *
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validatePaging(int    startFrom,
                               int    pageSize,
                               String methodName) throws InvalidParameterException
    {
        final  String   startFromParameterName = "startFrom";
        final  String   pageSizeParameterName = "pageSize";

        if (startFrom < 0)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NEGATIVE_START_FROM;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(startFromParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                startFromParameterName);
        }


        if (pageSize < 1)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.EMPTY_PAGE_SIZE;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(pageSizeParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                pageSizeParameterName);
        }
    }


    /**
     * Throw an exception if the supplied connection is null
     *
     * @param connection  object to validate
     * @param parameterName  name of the parameter that passed the connection.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    public void validateConnection(Connection connection,
                                   String     parameterName,
                                   String     methodName) throws InvalidParameterException
    {
        if (connection == null)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_CONNECTION_PARAMETER;
            String                   errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied array is null or empty
     *
     * @param stringArray  object to validate
     * @param parameterName  name of the parameter that passed the array.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    public void validateStringArray(List<String> stringArray,
                                    String        parameterName,
                                    String        methodName) throws InvalidParameterException
    {
        if ((stringArray == null) || (stringArray.isEmpty()))
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NULL_ARRAY_PARAMETER;
            String                   errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }
    }
}
