/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * SubjectAreaCheckedException provides a checked exception for reporting errors found when using
 * the Subject Area OMAS services.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * However, there may be the odd bug that surfaces here.
 * The SubjectAreaErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
public class SubjectAreaCheckedException extends OMAGCheckedExceptionBase {

    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an SubjectAreaCheckedException.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public SubjectAreaCheckedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription) {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an SubjectAreaCheckedException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param relatedProperties arbitrary properties that may help with diagnosing the problem.
     */
    public SubjectAreaCheckedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       Map<String, Object> relatedProperties) {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an SubjectAreaCheckedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     */
    public SubjectAreaCheckedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       Exception caughtError) {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an SubjectAreaCheckedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     * @param relatedProperties arbitrary properties that may help with diagnosing the problem.
     */
    public SubjectAreaCheckedException(ExceptionMessageDefinition messageDefinition,
                                       String className,
                                       String actionDescription,
                                       Exception caughtError,
                                       Map<String, Object> relatedProperties) {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }

}
