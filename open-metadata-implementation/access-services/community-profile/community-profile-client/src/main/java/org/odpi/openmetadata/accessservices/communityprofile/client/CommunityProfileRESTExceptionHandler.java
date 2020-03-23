/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * RESTExceptionHandler detects exceptions encoded a REST response and throws the appropriate Java exception.
 */
class CommunityProfileRESTExceptionHandler extends org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler
{
    /**
     * Default constructor
     */
    CommunityProfileRESTExceptionHandler()
    {
    }


    /**
     * Throw an NoProfileForUserException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called.
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws NoProfileForUserException encoded exception from the server
     */
    void detectAndThrowNoProfileForUserException(String           methodName,
                                                 FFDCResponseBase restResult) throws NoProfileForUserException
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
                                                restResult.getExceptionErrorMessageId(),
                                                restResult.getExceptionErrorMessageParameters(),
                                                restResult.getExceptionSystemAction(),
                                                restResult.getExceptionUserAction(),
                                                restResult.getExceptionCausedBy(),
                                                userId,
                                                restResult.getExceptionProperties());
        }
    }
}
