/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The StatusNotSupportedException is thrown by the Subject Area OMAS when a status is not supported
 * value.
 */
public class StatusNotSupportedException extends SubjectAreaCheckedException {
    /**
     * This is the typical constructor used for creating a StatusNotSupportedException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public StatusNotSupportedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription) {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating a StatusNotSupportedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     */
    public StatusNotSupportedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       Throwable caughtError) {
        super(messageDefinition, className, actionDescription, caughtError);
    }
}