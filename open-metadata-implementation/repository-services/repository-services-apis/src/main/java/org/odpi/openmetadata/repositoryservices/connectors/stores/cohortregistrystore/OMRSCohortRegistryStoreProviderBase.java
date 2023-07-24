/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

/**
 * The OMRSCohortRegistryStoreProviderBase provides a base class for the connector provider supporting OMRS
 * cohort registry stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of OMRSCohortRegistryStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the registry store connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OMRSCohortRegistryStoreProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OMRSCohortRegistryStoreProviderBase()
    {
        super.setConnectorComponentDescription(OMRSAuditingComponent.REGISTRY_STORE);
    }
}

