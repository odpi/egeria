/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

/**
 * AuditLoggingComponent is an interface that components can implement to indicate that they can make use of an audit log.
 */
public interface AuditLoggingComponent
{
    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    void setAuditLog(AuditLog auditLog);
}
