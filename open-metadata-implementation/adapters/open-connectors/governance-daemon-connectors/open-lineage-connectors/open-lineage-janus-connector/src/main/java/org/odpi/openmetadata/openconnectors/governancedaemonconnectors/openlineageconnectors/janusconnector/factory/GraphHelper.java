/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.util.function.TriConsumer;
import org.apache.tinkerpop.gremlin.util.function.TriFunction;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.GraphDetails;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphHelper {

    private AuditLog auditLog;
    private static final Logger log = LoggerFactory.getLogger(GraphHelper.class);
    private GraphFactory graphFactory;
    private GraphDetails graphDetails;

    /**
     * Set the config for Janus Graph.
     *
     * @param providerClass           - Provider Class name to be used
     * @param configurationProperties - The configuration properties for janusGraph
     * @param auditLog                - Used for logging errors
     * @throws JanusConnectorException if init fails
     * @throws OpenLineageException    if init fails
     */


    public void openGraph(String providerClass, Map<String, Object> configurationProperties, AuditLog auditLog) throws JanusConnectorException, OpenLineageException {
        graphFactory = new GraphFactory();
        this.auditLog = auditLog;
        graphDetails = graphFactory.openGraph(providerClass, configurationProperties);
    }

    public GraphTraversalSource getGraphTraversalSource() {
        // TODO will this clone thing help?
        return graphDetails.getGraphTraversalSource();
    }

    public boolean isSupportingTransactions() {
        return graphDetails.isSupportingTransactions();
    }

    public void closeGraph() {
        graphFactory.closeGraph();
    }

    public <U> void commit(BiConsumer<GraphTraversalSource, U> consumer, U argument, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        consumer.accept(g, argument);
        try {
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
    }

    public <U> U getResult(Function<GraphTraversalSource, U> function, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = function.apply(g);
        try {
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
        return result;
    }

    public <U, V> U getResult(BiFunction<GraphTraversalSource, V, U> function, V argument, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = function.apply(g, argument);
        try {
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
        return result;
    }

    public <V, U> void commit(TriConsumer<GraphTraversalSource, V, U> consumer, V argument1, U argument2, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        consumer.accept(g, argument1, argument2);
        try {
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
    }

    public <U, V, T> U getResult(TriFunction<GraphTraversalSource, V, T, U> function, V argument1, T argument2, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = function.apply(g, argument1, argument2);
        try {
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
        return result;
    }
}
