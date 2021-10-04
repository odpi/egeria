/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.GremlinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class GraphGremlinBase {

    private static final Logger log = LoggerFactory.getLogger(GraphGremlinBase.class);
    private static final String OPEN_GRAPH_INSTANCE_EXCEPTION = "An exception occurred while trying to open an existing graph instance or initializing a new one: ";
    private static final String CLOSING_GRAPH = "Closing graph";

    protected Graph graph;
    protected GraphTraversalSource g;
    protected boolean supportingTransactions;
    protected Map<String,Object> properties;

    /**
     * Constructs a graph using the given properties.
     * @param connectionProperties the properties coming from the request
     */
    public GraphGremlinBase(ConnectionProperties connectionProperties) {
        this.properties = connectionProperties.getConfigurationProperties();
    }

    /**
     * Opens the graph instance. If the graph instance does not exist, a new
     * graph instance will be initialized.
     */
    public GraphTraversalSource openGraph() throws GremlinException {
        log.debug("Trying to open the graph");
        try {
            graph = GraphFactory.open(properties);
            g = graph.traversal();
            return g;
        } catch (Exception e) {
            log.debug(OPEN_GRAPH_INSTANCE_EXCEPTION, e);
            throw new GremlinException();
        }

    }

    /**
     * Closes the graph instance.
     */
    public void closeGraph() throws Exception {
        log.debug(CLOSING_GRAPH);
        try {
            if (g != null) {
                g.close();
            }
            if (graph != null) {
                graph.close();
            }
        } finally {
            g = null;
            graph = null;
        }
    }

    /**
     * Drops the graph instance. The default implementation does nothing.
     */
    public void dropGraph() throws UnsupportedOperationException {
    }

    /**
     * Creates the graph schema. The default implementation does nothing.
     */
    protected void createSchema() throws UnsupportedOperationException {
    }

    /**
     * Makes an update to the existing graph structure. Does not create any
     * new vertices or edges.
     */
    public void updateElements() throws UnsupportedOperationException{}

    /**
     * Returns transaction support property of the specific graph instance.
     */
    public boolean isSupportingTransactions() {
        return supportingTransactions;
    }

}
