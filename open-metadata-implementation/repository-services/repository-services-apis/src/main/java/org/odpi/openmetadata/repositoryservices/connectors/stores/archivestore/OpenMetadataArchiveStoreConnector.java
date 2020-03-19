/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
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
    public void setAuditLog(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }
}
