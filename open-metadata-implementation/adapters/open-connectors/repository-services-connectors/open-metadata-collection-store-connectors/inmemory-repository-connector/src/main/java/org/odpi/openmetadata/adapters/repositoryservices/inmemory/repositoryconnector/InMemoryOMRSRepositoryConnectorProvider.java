/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The InMemoryOMRSRepositoryConnectorProvider is the connector provider for the InMemoryOMRSRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The InMemoryOMRSRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class InMemoryOMRSRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    static final String  connectorTypeGUID = "65cc9091-757f-4bcd-b937-426160be8bc2";
    static final String  connectorTypeName = "OMRS In Memory Repository Connector";
    static final String  connectorTypeDescription = "OMRS Repository Connector that uses an in-memory store.";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public InMemoryOMRSRepositoryConnectorProvider()
    {
        Class<?>    connectorClass = InMemoryOMRSRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
        super.setConnectorComponentDescription(OMRSAuditingComponent.INMEM_REPOSITORY_CONNECTOR);
    }
}