/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * The UserNotAuthorizedException is thrown by the OMAG Service when a userId passed on a request is not
 * authorized to perform the requested action.
 */
public class UserNotAuthorizedException extends org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException
{
    private static final long    serialVersionUID = 1L;


    /**
     * This is the typical constructor used for creating an UserNotAuthorizedException.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param userId offending userId
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      String                     userId)
    {
        super(messageDefinition, className, actionDescription, userId);
    }


    /**
     * This is the typical constructor used for creating an UserNotAuthorizedException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param userId offending userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      String                     userId,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, userId, relatedProperties);
    }


    /**
     * This is the constructor used for creating an UserNotAuthorizedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param userId offending userId
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError,
                                      String                     userId)
    {
        super(messageDefinition, className, actionDescription, caughtError, userId);
    }


    /**
     * This is the constructor used for creating an UserNotAuthorizedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param userId offending userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String                     className,
                                      String                     actionDescription,
                                      Throwable                  caughtError,
                                      String                     userId,
                                      Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, userId, relatedProperties);
    }


    /**
     * This is the constructor used when receiving an exception from a remote server.  The values are
     * stored directly in the response object and are passed explicitly to the new exception.
     * Notice that the technical aspects of the exception - such as class name creating the exception
     * are local values so that the implementation of the server is not exposed.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param errorMessageId unique identifier for the message
     * @param errorMessageParameters parameters that were inserted in the message
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtErrorClassName   previous error causing this exception
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public UserNotAuthorizedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              errorMessageId,
                                      String[]            errorMessageParameters,
                                      String              systemAction,
                                      String              userAction,
                                      String              caughtErrorClassName,
                                      String              userId,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode,
              className,
              actionDescription,
              errorMessage,
              errorMessageId,
              errorMessageParameters,
              systemAction,
              userAction,
              caughtErrorClassName,
              userId,
              relatedProperties);
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
     */
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
                ", errorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }
}
