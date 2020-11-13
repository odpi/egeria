/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The UserNotAuthorizedException is thrown by the Subject Area OMAS when a userId passed on a request is not
 * authorized to perform the requested action.
 */
public class UserNotAuthorizedException extends SubjectAreaCheckedException {
    private String userId = null;

    /**
     * This is the typical constructor used for creating an UserNotAuthorizedException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param userId            userId associated with this Exception
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String className,
                                      String actionDescription,
                                      String userId) {
        super(messageDefinition, className, actionDescription);
        this.userId = userId;
    }


    /**
     * This is the constructor used for creating an UserNotAuthorizedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     * @param userId            userId associated with this Exception
     */
    public UserNotAuthorizedException(ExceptionMessageDefinition messageDefinition,
                                      String className,
                                      String actionDescription,
                                      Throwable caughtError,
                                      String userId) {
        super(messageDefinition, className, actionDescription, caughtError);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
