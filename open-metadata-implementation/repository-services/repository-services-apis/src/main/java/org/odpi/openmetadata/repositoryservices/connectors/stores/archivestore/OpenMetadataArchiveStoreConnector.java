/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;


/**
 * OpenMetadataArchiveStoreConnector is the base class for connectors that support the OpenMetadataArchiveStore
 */
public abstract class OpenMetadataArchiveStoreConnector extends ConnectorBase implements OpenMetadataArchiveStore,
                                                                                         AuditLoggingComponent
{
    protected AuditLog auditLog = null;


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }
}
