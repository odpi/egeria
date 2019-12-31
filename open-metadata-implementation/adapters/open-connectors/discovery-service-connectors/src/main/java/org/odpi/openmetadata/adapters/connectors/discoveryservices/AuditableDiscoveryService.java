/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;

/**
 * AuditableDiscoveryService is a base class for discovery services that wish to use the audit log.
 */
public abstract class AuditableDiscoveryService extends DiscoveryService implements AuditableConnector
{
    protected OMRSAuditLog  auditLog = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(OMRSAuditLog auditLog)
    {
        this.auditLog = auditLog;
    }
}
