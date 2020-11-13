/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;
import java.util.Objects;

/**
 * The UserNotAuthorizedException is thrown by an OMRS Connector when the supplied UserId is not permitted to
 * perform a specific operation on the metadata collection.
 */
public class UserNotAuthorizedException extends OMRSCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private String  userId = null;

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
        super(messageDefinition, className, actionDescription);

        this.userId = userId;
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
        super(messageDefinition, className, actionDescription, relatedProperties);

        this.userId = userId;
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
        super(messageDefinition, className, actionDescription, caughtError);

        this.userId = userId;
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
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);

        this.userId = userId;
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
              relatedProperties);

        this.userId = userId;
    }


    /**
     * This is the typical constructor used for creating a UserNotAuthorizedException.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    @Deprecated
    public UserNotAuthorizedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a UserNotAuthorizedException that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  the error that resulted in this exception.
     */
    @Deprecated
    public UserNotAuthorizedException(int       httpCode,
                                      String    className,
                                      String    actionDescription,
                                      String    errorMessage,
                                      String    systemAction,
                                      String    userAction,
                                      Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
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
     * @param userId failing userId
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
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);

        this.userId = userId;
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
     * @param userId failing userId
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
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);

        this.userId = userId;
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
     * @param userId failing userId
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
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);

        this.userId = userId;
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
     * @param userId failing userId
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    @Deprecated
    public UserNotAuthorizedException(int                 httpCode,
                                      String              className,
                                      String              actionDescription,
                                      String              errorMessage,
                                      String              systemAction,
                                      String              userAction,
                                      Throwable           caughtError,
                                      String              userId,
                                      Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);

        this.userId = userId;

    }


    /**
     * Constructor to translate an OCF exception into an Open Metadata Repository Services (OMRS) exception
     *
     * @param caughtError OCF exception
     */

    public UserNotAuthorizedException(org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  caughtError)
    {
        super(caughtError.getReportedErrorMessage(), caughtError);

        this.userId = caughtError.getUserId();
    }


    /**
     * Return the userId passed on the request.
     *
     * @return string user id
     */
    public String getUserId()
    {
        return userId;
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
                "userId='" + userId + '\'' +
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


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UserNotAuthorizedException that = (UserNotAuthorizedException) objectToCompare;
        return Objects.equals(getUserId(), that.getUserId());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserId());
    }
}
