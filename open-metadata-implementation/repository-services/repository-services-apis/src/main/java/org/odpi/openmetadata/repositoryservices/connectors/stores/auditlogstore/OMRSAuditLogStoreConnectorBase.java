/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

/**
 * OMRSAuditLogStoreConnectorBase is the base class for connectors that support the OMRSAuditLog
 */
public abstract class OMRSAuditLogStoreConnectorBase extends ConnectorBase implements OMRSAuditLogStore
{
    /**
     * Default constructor
     */
    public OMRSAuditLogStoreConnectorBase()
    {
    }
}
