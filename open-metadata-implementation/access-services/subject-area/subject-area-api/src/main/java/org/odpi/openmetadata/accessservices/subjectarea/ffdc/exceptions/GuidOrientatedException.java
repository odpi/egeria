/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The GuidOrientatedException is thrown by the Subject Area OMAS when an error occurs that has an associated guid.
 */
public class GuidOrientatedException extends SubjectAreaCheckedException {
    private final String guid;

    /**
     * This is the typical constructor used for creating a GuidOrientatedException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param guid              guid associated with this Exception
     */
    public GuidOrientatedException(ExceptionMessageDefinition messageDefinition,
                                   String className,
                                   String actionDescription,
                                   String guid) {
        super(messageDefinition, className, actionDescription);
        this.guid = guid;
    }


    /**
     * This is the constructor used for creating an GuidOrientatedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     * @param guid              guid associated with this Exception
     */
    public GuidOrientatedException(ExceptionMessageDefinition messageDefinition,
                                   String className,
                                   String actionDescription,
                                   Throwable caughtError,
                                   String guid) {
        super(messageDefinition, className, actionDescription, caughtError);
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

}