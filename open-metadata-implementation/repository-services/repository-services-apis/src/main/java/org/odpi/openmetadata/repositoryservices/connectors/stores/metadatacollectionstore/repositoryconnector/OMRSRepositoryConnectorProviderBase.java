/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

/**
 * The OMRSRepositoryConnectorProviderBase provides a base class for the connector provider supporting OMRS Connectors.
 * It adds no function but provides a placeholder for additional function if needed for the creation of
 * any OMRS Repository connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * OMRSRepositoryConnectorProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OMRSRepositoryConnectorProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OMRSRepositoryConnectorProviderBase()
    {
        super.setConnectorComponentDescription(OMRSAuditingComponent.REPOSITORY_CONNECTOR);
    }
}