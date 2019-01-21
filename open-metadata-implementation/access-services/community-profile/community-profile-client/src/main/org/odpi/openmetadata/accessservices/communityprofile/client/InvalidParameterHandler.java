/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;

/**
 * InvalidParameterHandler is the client-side error handler.  It provides validation for incoming parameters.
 */
class InvalidParameterHandler
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
    void validateOMASServerURL(String omasServerURL,
                               String methodName) throws PropertyServerException
    {
        if (omasServerURL == null)
        {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

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
    void validateUserId(String userId,
                        String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

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
    void validateGUID(String guid,
                      String guidParameter,
                      String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_GUID;
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
    void validateName(String name,
                      String nameParameter,
                      String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.NULL_NAME;
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
}
