/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The UnexpectedResponseException is thrown by the Subject Area OMAS when an unexpected error occurs
 */
public class UnexpectedResponseException extends SubjectAreaCheckedException {
    String unexpectedCategoryName;

    /**
     * This is the typical constructor used for creating a UnexpectedResponseException
     *
     * @param messageDefinition      content of the message
     * @param className              name of class reporting error
     * @param actionDescription      description of function it was performing when error detected
     * @param unexpectedCategoryName unexpected category name
     */
    public UnexpectedResponseException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       String unexpectedCategoryName) {
        super(messageDefinition, className, actionDescription);
        this.unexpectedCategoryName = unexpectedCategoryName;
    }


    /**
     * This is the constructor used for creating an UnexpectedResponseException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     */
    public UnexpectedResponseException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       Throwable caughtError) {
        super(messageDefinition, className, actionDescription, caughtError);
    }

    public String getUnexpectedCategoryName() {
        return unexpectedCategoryName;
    }
}