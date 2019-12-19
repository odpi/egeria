/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.ffdc.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.viewservices.subjectarea.ffdc.SubjectAreaViewErrorCode;


/**
 * ErrorHandler provides common validation routines for the other handler classes
 */
public class ErrorHandler
{
     static String className = ErrorHandler.class.getName();

    /**
     * return an exception response if the supplied userId is not authorized to perform a request
     *
     * @param methodName name of the method making the call.
     * @param serverName name of this server name of this server
     * @param serviceName name of this access service
     * @return UserNotAuthorizedException response the userId is unauthorised for the request
     */
    public static  SubjectAreaOMASAPIResponse handleNullUser(
                                                             String methodName,
                                                             String serverName,
                                                             String serviceName)
    {
        SubjectAreaViewErrorCode errorCode = SubjectAreaViewErrorCode.NULL_USER_ID;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(
                                                     methodName,
                                                     serviceName,
                                                     serverName);
        // We are using the access server exception, rather than creating a new view version of this Exception
        UserNotAuthorizedException oe =new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                             className,
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction(),
                                            null);
        return OMASExceptionToResponse.convertUserNotAuthorizedException(oe);

    }


}
