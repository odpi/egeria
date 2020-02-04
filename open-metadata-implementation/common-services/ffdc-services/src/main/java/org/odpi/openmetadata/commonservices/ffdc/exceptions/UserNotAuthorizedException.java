/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import java.util.Map;

/**
 * The UserNotAuthorizedException is thrown by the OMAG Service when a userId passed on a request is not
 * authorized to perform the requested action.
 */
public class UserNotAuthorizedException extends org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param userId offending userId
     */
    public UserNotAuthorizedException(int    httpCode,
                                      String className,
                                      String actionDescription,
                                      String errorMessage,
                                      String systemAction,
                                      String userAction,
                                      String userId)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, userId);
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param userId offending userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              systemAction,
                                      String              userAction,
                                      String              userId,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, userId, relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param userId calling user
     */
    public UserNotAuthorizedException(int       httpCode,
                                      String    className,
                                      String    actionDescription,
                                      String    errorMessage,
                                      String    systemAction,
                                      String    userAction,
                                      Throwable caughtError,
                                      String    userId)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, userId);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param userId calling user
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(int                  httpCode,
                                      String               className,
                                      String               actionDescription,
                                      String               errorMessage,
                                      String               systemAction,
                                      String               userAction,
                                      Throwable            caughtError,
                                      String               userId,
                                      Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, userId, relatedProperties);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public UserNotAuthorizedException(org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "UserNotAuthorizedException{" +
                "userId='" + getUserId() + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }
}
