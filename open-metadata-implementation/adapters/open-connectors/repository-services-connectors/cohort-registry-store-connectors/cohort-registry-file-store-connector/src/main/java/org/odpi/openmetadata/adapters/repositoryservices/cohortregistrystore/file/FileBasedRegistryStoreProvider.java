/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStoreProviderBase;

/**
 * FileBasedRegistryStoreProvider is the OCF connector provider for the file based cohort registry store.
 */
public class FileBasedRegistryStoreProvider extends OMRSCohortRegistryStoreProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public FileBasedRegistryStoreProvider()
    {
        Class    connectorClass = FileBasedRegistryStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
