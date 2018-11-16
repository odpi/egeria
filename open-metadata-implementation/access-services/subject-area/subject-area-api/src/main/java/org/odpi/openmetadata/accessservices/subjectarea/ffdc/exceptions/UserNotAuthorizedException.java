/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


/**
 * The UserNotAuthorizedException is thrown by the Subject Area OMAS when a userId passed on a request is not
 * authorized to perform the requested action.
 */
public class UserNotAuthorizedException extends SubjectAreaCheckedExceptionBase
{
    private String userId=null;
    /**
     * This is the typical constructor used for creating a UserNotAuthorizedException.
     *
     * @param httpCode http response code to use if this exception flows over a rest call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param userId  user identifier
     */
    public UserNotAuthorizedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction,String userId)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
        this.userId=userId;
    }


    /**
     * This is the constructor used for creating a UserNotAuthorizedException that resulted from a previous error.
     *
     * @param httpCode http response code to use if this exception flows over a rest call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError the error that resulted in this exception.
     * @param userId  user identifier
     * */
    public UserNotAuthorizedException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, String userId,Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }
}
