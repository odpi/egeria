/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

/**
 * ExceptionMessageSet is the interface implemented by an enum that contains the collection of message definitions for a component's exceptions.
 */
public interface ExceptionMessageSet
{
    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    ExceptionMessageDefinition getMessageDefinition();


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    ExceptionMessageDefinition getMessageDefinition(String... params);
}
