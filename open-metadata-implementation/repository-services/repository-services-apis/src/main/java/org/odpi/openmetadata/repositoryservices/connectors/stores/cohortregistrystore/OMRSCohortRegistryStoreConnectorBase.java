/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;

/**
 * OMRSCohortRegistryStoreConnectorBase provides the base class for a cohort registry store.  It defines the
 * specific interface for this type of connector.
 */
public abstract class OMRSCohortRegistryStoreConnectorBase extends ConnectorBase implements OMRSCohortRegistryStore,
                                                                                            AuditableConnector
{
    protected OMRSAuditLog auditLog = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(OMRSAuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }
}
