/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.client;

import org.odpi.openmetadata.conformance.rest.ConformanceServicesAPIResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

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
    void detectAndThrowInvalidParameterException(String                        methodName,
                                                 ConformanceServicesAPIResponse restResult) throws InvalidParameterException
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
            InvalidParameterException restException = new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                                                    this.getClass().getName(),
                                                                                    methodName,
                                                                                    restResult.getExceptionErrorMessage(),
                                                                                    restResult.getExceptionSystemAction(),
                                                                                    restResult.getExceptionUserAction(),
                                                                                    paramName);

            restException.setReportedURL(restResult.getExceptionURL());

            throw restException;
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
    void detectAndThrowPropertyServerException(String                         methodName,
                                               ConformanceServicesAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            PropertyServerException restException = new PropertyServerException(restResult.getRelatedHTTPCode(),
                                                                                this.getClass().getName(),
                                                                                methodName,
                                                                                restResult.getExceptionErrorMessage(),
                                                                                null,
                                                                                null,
                                                                                restResult.getExceptionSystemAction(),
                                                                                restResult.getExceptionUserAction(),
                                                                                null,
                                                                                null);

            restException.setReportedURL(restResult.getExceptionURL());

            throw restException;
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
    void detectAndThrowUserNotAuthorizedException(String                         methodName,
                                                  ConformanceServicesAPIResponse restResult) throws UserNotAuthorizedException
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

            UserNotAuthorizedException restException = new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                                                      this.getClass().getName(),
                                                                                      methodName,
                                                                                      restResult.getExceptionErrorMessage(),
                                                                                      restResult.getExceptionSystemAction(),
                                                                                      restResult.getExceptionUserAction(),
                                                                                      userId);

            restException.setReportedURL(restResult.getExceptionURL());

            throw restException;
        }
    }
}
