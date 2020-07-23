/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The PropertyServerException is thrown by the Subject Area OMAS when there isa property server error
 * value.
 */
public class PropertyServerException extends SubjectAreaCheckedException {
    /**
     * This is the typical constructor used for creating a PropertyServerException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String className,
                                   String actionDescription) {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the constructor used for creating an PropertyServerException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String className,
                                   String actionDescription,
                                   Throwable caughtError) {
        super(messageDefinition, className, actionDescription, caughtError);
    }
}