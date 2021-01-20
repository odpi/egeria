/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The ReadOnlyOMRSRepositoryConnectorProvider is the connector provider for the ReadOnlyOMRSRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The ReadOnlyOMRSRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class ReadOnlyOMRSRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    static final String  connectorTypeGUID = "72aa9091-757f-4bcd-b937-426160be8bc2";
    static final String  connectorTypeName = "OMRS Read Only Repository Connector";
    static final String  connectorTypeDescription = "OMRS Repository Connector that uses an archive for content and stores nothing else.";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public ReadOnlyOMRSRepositoryConnectorProvider()
    {
        Class<?> connectorClass = ReadOnlyOMRSRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
        super.setConnectorComponentDescription(OMRSAuditingComponent.READ_ONLY_REPOSITORY_CONNECTOR);
    }
}