/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.rest.CommunityProfileOMASAPIResponse;

import java.util.Map;

/**
 * RESTExceptionHandler detects exceptions encoded a REST response and throws the appropriate Java exception.
 */
class RESTExceptionHandler
{
    /**
     * Default constructor
     */
    RESTExceptionHandler()
    {
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    void detectAndThrowInvalidParameterException(String                          methodName,
                                                 CommunityProfileOMASAPIResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String paramName = null;

            Map<String, Object> exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get("parameterName");

                if (nameObject != null)
                {
                    paramName = (String)nameObject;
                }
            }
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                paramName);
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws PropertyServerException encoded exception from the server
     */
    void detectAndThrowPropertyServerException(String                          methodName,
                                               CommunityProfileOMASAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyServerException(restResult.getRelatedHTTPCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              restResult.getExceptionErrorMessage(),
                                              restResult.getExceptionSystemAction(),
                                              restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an NoProfileForUserException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws NoProfileForUserException encoded exception from the server
     */
    void detectAndThrowNoProfileForUserException(String                          methodName,
                                                 CommunityProfileOMASAPIResponse restResult) throws NoProfileForUserException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new NoProfileForUserException(restResult.getRelatedHTTPCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                restResult.getExceptionErrorMessage(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                userId);
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    void detectAndThrowUserNotAuthorizedException(String                          methodName,
                                                  CommunityProfileOMASAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  userIdObject = exceptionProperties.get("userId");

                if (userIdObject != null)
                {
                    userId = (String)userIdObject;
                }
            }

            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 userId);
        }
    }
}
