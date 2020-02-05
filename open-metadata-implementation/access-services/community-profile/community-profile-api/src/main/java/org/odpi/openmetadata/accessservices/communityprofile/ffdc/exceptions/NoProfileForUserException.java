/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions;


import java.util.Objects;

/**
 * The NoProfileForUserException is thrown by the OMAS when a userId passed on a request does not have an associated
 * actor profile.
 */
public class NoProfileForUserException extends CommunityProfileCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    private String  userId;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param userId calling user
     */
    public NoProfileForUserException(int    httpCode,
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
    public NoProfileForUserException(int       httpCode,
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
        return "NoProfileForUserException{" +
                "userId='" + userId + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
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
        if (!(objectToCompare instanceof NoProfileForUserException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        NoProfileForUserException that = (NoProfileForUserException) objectToCompare;
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
