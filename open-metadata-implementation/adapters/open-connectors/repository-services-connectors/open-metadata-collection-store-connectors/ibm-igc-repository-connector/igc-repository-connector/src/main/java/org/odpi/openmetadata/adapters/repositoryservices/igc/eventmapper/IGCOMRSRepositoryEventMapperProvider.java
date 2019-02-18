/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The IGCOMRSRepositoryEventMapperProvider is the connector provider for the IGCOMRSRepositoryEventMapperProvider.
 * It extends OMRSRepositoryEventMapperProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The IGCOMRSRepositoryEventMapperProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class IGCOMRSRepositoryEventMapperProvider extends OMRSRepositoryConnectorProviderBase {

    static final String  connectorTypeGUID = "46643e1b-0625-4b7a-96db-4a2153f0ae88";
    static final String  connectorTypeName = "OMRS IGC Event Mapper Connector";
    static final String  connectorTypeDescription = "OMRS IGC Event Mapper Connector that processes events from the IBM InfoSphere Information Governance Catalog repository store.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public IGCOMRSRepositoryEventMapperProvider() {
        Class connectorClass = IGCOMRSRepositoryEventMapper.class;
        super.setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        super.setConnectorTypeProperties(connectorType);
    }

}
