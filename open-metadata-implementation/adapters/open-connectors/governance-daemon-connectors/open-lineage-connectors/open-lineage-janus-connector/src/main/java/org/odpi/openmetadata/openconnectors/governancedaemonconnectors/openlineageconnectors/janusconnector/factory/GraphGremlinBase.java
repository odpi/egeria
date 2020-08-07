/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class GraphGremlinBase {

    private static final Logger log = LoggerFactory.getLogger(GraphGremlinBase.class);

    protected Graph graph;
    protected GraphTraversalSource g;
    protected boolean supportingTransactions;
    protected Map<String,Object> properties;


    /**
     * Constructs a graph using the given properties.
     * @param connectionProperties the properties coming from the request
     */
    public GraphGremlinBase(ConnectionProperties connectionProperties){
        this.properties = connectionProperties.getConfigurationProperties();
    }


    /**
     * Opens the graph instance. If the graph instance does not exist, a new
     * graph instance will be initialized.
     */
    public GraphTraversalSource openGraph() {
        log.debug("Trying to open the graph");
        try
        {
            graph = GraphFactory.open(properties);
            g = graph.traversal();
            return g;
        }catch (Exception e){
            log.debug("exception: ",e);
            //TODO add proper exception
            throw new RuntimeException();
        }

    }


    /**
     * Closes the graph instance.
     */
    public void closeGraph() throws Exception {
        log.debug("closing graph");
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
    public void dropGraph() throws Exception {
    }

    /**
     * Creates the graph schema. The default implementation does nothing.
     */
    protected void createSchema() {
    }


    /**
     * Makes an update to the existing graph structure. Does not create any
     * new vertices or edges.
     */
    public void updateElements() {}

    /**
     * Returns transaction support property of the specific graph instance.
     */
    public boolean isSupportingTransactions() {
        return supportingTransactions;
    }

}
