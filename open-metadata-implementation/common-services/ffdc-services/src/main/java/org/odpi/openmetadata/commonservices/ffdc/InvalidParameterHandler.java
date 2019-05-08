/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;

/**
 * InvalidParameterHandler is a common error handler.  It provides validation for incoming parameters.
 */
public class InvalidParameterHandler
{
    private  int   maxPagingSize = 500;

    /**
     * Default constructor
     */
    public InvalidParameterHandler()
    {
    }


    /**
     * Override the default maximum paging size.
     *
     * @param maxPagingSize new value
     */
    public void setMaxPagingSize(int maxPagingSize)
    {
        this.maxPagingSize = maxPagingSize;
    }


    /**
     * Throw an exception if a server URL or  has not been supplied.  It is typically
     * used in OMAG Clients or OMAG Servers that call other OMAG Servers.
     *
     * @param omagServerPlatformURL url of the server
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the server URL or server name are not set
     */
    public void validateOMAGServerPlatformURL(String omagServerPlatformURL,
                                              String serverName,
                                              String methodName) throws InvalidParameterException
    {
        if (omagServerPlatformURL == null)
        {
            final String parameterName = "omagServerPlatformURL";

            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.SERVER_URL_NOT_SPECIFIED;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                parameterName);
        }

        if (serverName == null)
        {
            final String parameterName = "serverName";

            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.SERVER_NAME_NOT_SPECIFIED;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

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
            final String parameterName = "userId";

            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_USER_ID;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

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
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_GUID;
            String              errorMessage = errorCode.getErrorMessageId()
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
     * @throws InvalidParameterException the name is null
     */
    public void validateName(String name,
                             String nameParameter,
                             String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_NAME;
            String              errorMessage = errorCode.getErrorMessageId()
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
     * Throw an exception if the supplied enum is null
     *
     * @param enumValue  enum value to validate
     * @param parameterName  name of the parameter that passed the enum.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the enum is null
     */
    public void validateEnum(Object enumValue,
                             String parameterName,
                             String methodName) throws InvalidParameterException
    {
        if (enumValue == null)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_ENUM;
            String              errorMessage = errorCode.getErrorMessageId()
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
     * Throw an exception if the supplied text field is null
     *
     * @param text  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    public void validateText(String text,
                             String parameterName,
                             String methodName) throws InvalidParameterException
    {
        if (text == null)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_TEXT;
            String              errorMessage = errorCode.getErrorMessageId()
                                             + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "text");
        }
    }


    /**
     * Throw an exception if the supplied paging values don't make sense.
     *
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the paging options are incorrect
     */
    public void validatePaging(int    startFrom,
                               int    pageSize,
                               String methodName) throws InvalidParameterException
    {
        final  String   startFromParameterName = "startFrom";
        final  String   pageSizeParameterName  = "pageSize";

        if (startFrom < 0)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NEGATIVE_START_FROM;
            String              errorMessage = errorCode.getErrorMessageId()
                                             + errorCode.getFormattedErrorMessage(Integer.toString(startFrom), startFromParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                startFromParameterName);
        }


        if (pageSize < 0)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NEGATIVE_PAGE_SIZE;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(Integer.toString(pageSize), pageSizeParameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                pageSizeParameterName);
        }


        if (pageSize > maxPagingSize)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.MAX_PAGE_SIZE;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(Integer.toString(pageSize), Integer.toString(maxPagingSize), methodName, Integer.toString(maxPagingSize));

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
     * Throw an exception if the supplied array is null or empty
     *
     * @param stringArray  object to validate
     * @param parameterName  name of the parameter that passed the array.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the array is null or empty
     */
    public void validateStringArray(List<String>  stringArray,
                                    String        parameterName,
                                    String        methodName) throws InvalidParameterException
    {
        if ((stringArray == null) || (stringArray.isEmpty()))
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_ARRAY_PARAMETER;
            String              errorMessage = errorCode.getErrorMessageId()
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
     * Throw an exception if the supplied connection is null
     *
     * @param connection  object to validate
     * @param parameterName  name of the parameter that passed the connection.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the connection is null
     */
    public void validateConnection(Connection connection,
                                   String     parameterName,
                                   String     methodName) throws InvalidParameterException
    {
        if (connection == null)
        {
            OMAGCommonErrorCode errorCode    = OMAGCommonErrorCode.NULL_CONNECTION_PARAMETER;
            String              errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(parameterName, methodName);

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
