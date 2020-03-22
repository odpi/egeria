package org.odpi.openmetadata.repositoryservices.connectors.stores.searchindexingstore;

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperConnector;

public class OMRSSearchIndexingConnectorProvider {

    private Connection searchIndexingRepositoryConnection;
    private OMRSRepositoryEventMapperConnector realEventMapper;

    /**
     * Constructor used by OMRSOperationalServices during server start-up. It
     * provides the configuration information about the local server that is used to set up the
     * local repository connector.
     *
     * @param searchIndexingRepositoryConnection connection object for creating a remote connector to this search indexing repository.
     * @param realEventMapper                    optional event mapper for local repository
     */
    public OMRSSearchIndexingConnectorProvider(Connection searchIndexingRepositoryConnection,
                                               OMRSRepositoryEventMapperConnector realEventMapper) {
        this.searchIndexingRepositoryConnection = searchIndexingRepositoryConnection;
        this.realEventMapper = realEventMapper;
    }

    /**
     * Creates a new instance of a connector based on the information in the supplied connection.
     *
     * @param realConnection connection that should have all of the properties needed by the Connector Provider
     *                       to create a connector instance.
     * @return Connector instance of the LocalOMRSRepositoryConnector wrapping the real local connector.
      */
    public synchronized Connector getConnector(Connection realConnection) {

        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector connector = connectorBroker.getConnector(realConnection);
            OMRSSearchIndexingConnector searchIndexingConnector = (OMRSSearchIndexingConnector) connector;

            return searchIndexingConnector;
        } catch (ConnectionCheckedException | ConnectorCheckedException e) {
            //log the exception
            //TODO: DANIELA
        }

        return null;
    }

}
