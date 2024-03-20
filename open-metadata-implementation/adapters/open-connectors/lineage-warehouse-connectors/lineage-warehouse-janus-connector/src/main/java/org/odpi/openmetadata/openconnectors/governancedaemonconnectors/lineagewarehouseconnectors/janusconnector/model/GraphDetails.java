/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public final class GraphDetails {
    private final GraphTraversalSource g;
    private final boolean isSupportingTransactions;
    private final boolean isRemote;

    public GraphDetails(GraphTraversalSource g, boolean isSupportingTransactions, boolean isRemote) {
        this.g = g;
        this.isSupportingTransactions = isSupportingTransactions;
        this.isRemote = isRemote;
    }

    public GraphTraversalSource getGraphTraversalSource() {
        return g;
    }

    public boolean isSupportingTransactions() {
        return isSupportingTransactions;
    }

    public boolean isRemote() {
        return isRemote;
    }
}
