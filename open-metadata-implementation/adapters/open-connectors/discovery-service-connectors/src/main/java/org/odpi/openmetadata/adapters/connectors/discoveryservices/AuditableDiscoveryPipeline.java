/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryPipeline;


/**
 * AuditableDiscoveryPipeline is a base class for discovery pipelines that wish to use the audit log.
 */
public abstract class AuditableDiscoveryPipeline extends DiscoveryPipeline implements AuditLoggingComponent
{
    protected AuditLog  auditLog = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }
}
