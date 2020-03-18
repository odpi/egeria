/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

/**
 * AuditLogMessageSet is the interface implemented by an enum that contains the collection of message definitions for a component.
 */
public interface AuditLogMessageSet
{
    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    AuditLogMessageDefinition getMessageDefinition();


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    AuditLogMessageDefinition getMessageDefinition(String ...params);
}
