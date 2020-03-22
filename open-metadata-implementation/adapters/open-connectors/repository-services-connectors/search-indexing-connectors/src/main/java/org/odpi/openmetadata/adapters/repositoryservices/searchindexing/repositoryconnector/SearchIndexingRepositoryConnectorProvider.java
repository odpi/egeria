package org.odpi.openmetadata.adapters.repositoryservices.searchindexing.repositoryconnector;

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

public class SearchIndexingRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase {

    static final String connectorTypeGUID = "68d4d5fa-fbe2-fbed-ed07-9924e0fda146";
    static final String connectorTypeName = "Search Indexing REST API Repository Connector";
    static final String connectorTypeDescription = "Search Indexing Repository Connector that calls the search REST API of a search indexing server.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public SearchIndexingRepositoryConnectorProvider() {
        Class<SearchIndexingRepositoryConnector> connectorClass = SearchIndexingRepositoryConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}