/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The OMRSCohortRegistryStoreProviderBase provides a base class for the connector provider supporting OMRS
 * cluster registry stores.  It extends ConnectorProviderBase which does the creation of connector instances.
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
        /*
         * Nothing to do
         */
    }
}

