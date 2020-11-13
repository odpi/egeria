/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The OMRSRESTRepositoryConnectorProvider is the connector provider for the OMRSRESTRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The OMRSRESTRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class OMRSRESTRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase
{
    static final String  connectorTypeGUID = "75ea56d1-656c-43fb-bc0c-9d35c5553b9e";
    static final String  connectorTypeName = "OMRS REST API Repository Connector";
    static final String  connectorTypeDescription = "OMRS Repository Connector that calls the repository services REST API of a remote server.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public OMRSRESTRepositoryConnectorProvider()
    {
        Class<OMRSRESTRepositoryConnector> connectorClass = OMRSRESTRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
        super.setConnectorComponentDescription(OMRSAuditingComponent.REMOTE_REPOSITORY_CONNECTOR);
    }
}