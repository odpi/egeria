package org.odpi.openmetadata.adapters.repositoryservices.searchindexing.repositoryconnector;

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.searchindexingstore.OMRSSearchIndexingConnector;

public class SearchIndexingRepositoryConnector extends OMRSSearchIndexingConnector {

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public SearchIndexingRepositoryConnector() {
        System.out.println("this is the search indexing connector: " +  this.getConnectorInstanceId());
    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem disconnecting the connector.
     */
    public void disconnect() throws ConnectorCheckedException {
    }
}