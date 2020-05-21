/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The UnrecognizedGUIDException is thrown by the Subject Area OMAS when the unique identifier (guid)
 * used to request an object is either unrecognized, or is the identifier for a different type of object.
 */
public class UnrecognizedGUIDException extends GuidOrientatedException {

    /**
     * This is the typical constructor used for creating an UnrecognizedGUIDException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param guid              unrecognised guid
     */
    public UnrecognizedGUIDException(ExceptionMessageDefinition messageDefinition,
                                     String className,
                                     String actionDescription,
                                     String guid) {
        super(messageDefinition, className, actionDescription, guid);
    }


    /**
     * This is the constructor used for creating an UnrecognizedGUIDException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError       previous error causing this exception
     * @param guid              unrecognised guid
     */
    public UnrecognizedGUIDException(ExceptionMessageDefinition messageDefinition,
                                     String className,
                                     String actionDescription,
                                     Throwable caughtError,
                                     String guid) {
        super(messageDefinition, className, actionDescription, caughtError, guid);
    }

}

