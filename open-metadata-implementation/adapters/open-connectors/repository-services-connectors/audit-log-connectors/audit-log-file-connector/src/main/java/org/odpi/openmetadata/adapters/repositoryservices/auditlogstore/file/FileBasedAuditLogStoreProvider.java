/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file;

import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * FileBasedRegistryStoreProvider is the OCF connector provider for the file based cohort registry store.
 */
public class FileBasedAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public FileBasedAuditLogStoreProvider()
    {
        Class    connectorClass = FileBasedAuditLogStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
