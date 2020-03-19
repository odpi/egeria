/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.auditable;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * AuditableConnector provides a method for passing an audit log to a connector.  Where it is used, it should be replaced
 * with AuditLoggingComponent from the Audit Log Framework (ALF).
 */
@Deprecated
public interface AuditableConnector
{
    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Deprecated
    void setAuditLog(OMRSAuditLog auditLog);
}
