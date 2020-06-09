/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * The EntityNotDeletedException is thrown by the Subject Area OMAS when an entity is not deleted
 * value.
 */

public class EntityNotDeletedException extends GuidOrientatedException {

    /**
     * This is the typical constructor used for creating an EntityNotDeletedException
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param guid              the guid of the entity that was not deleted
     */
    public EntityNotDeletedException(ExceptionMessageDefinition messageDefinition,
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
     * @param guid              the guid of the entity that was not deleted
     */
    public EntityNotDeletedException(ExceptionMessageDefinition messageDefinition,
                                     String className,
                                     String actionDescription,
                                     Throwable caughtError,
                                     String guid) {
        super(messageDefinition, className, actionDescription, caughtError, guid);
    }

}


