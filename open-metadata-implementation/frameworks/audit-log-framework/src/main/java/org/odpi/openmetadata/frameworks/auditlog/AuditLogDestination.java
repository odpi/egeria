/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import java.util.Map;

/**
 * AuditLogDestination provides the support to push audit log records to the desired locations
 * for either storage or processing.
 */
public abstract class AuditLogDestination
{
    protected Map<String, String>  originatorProperties = null;


    /**
     * Default constructor for subclasses to give them an opportunity to build the
     * originator properties.
     */
    protected AuditLogDestination()
    {
    }


    /**
     * The normal constructor takes the description of the process/server that is using the
     * audit log.  It is added to every log record.
     *
     * @param originatorProperties map of name-value pairs
     */
    public AuditLogDestination(Map<String, String> originatorProperties)
    {
        this.originatorProperties = originatorProperties;
    }


    /**
     * Return the map of properties used to describe the originator process/server.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getOriginatorProperties()
    {
        return originatorProperties;
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the
     * open metadata services.
     *
     * @param logRecord the log record
     */
    public abstract void addLogRecord(AuditLogRecord  logRecord);
}
