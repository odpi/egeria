/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.util.function.TriConsumer;
import org.apache.tinkerpop.gremlin.util.function.TriFunction;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.factory.GraphFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.GraphDetails;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.ffdc.JanusConnectorException;
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
     * Open the graph with the provided configuration
     *
     * @param providerClass           - Provider Class name to be used
     * @param configurationProperties - The configuration properties for janusGraph
     * @param auditLog                - Used for logging errors
     * @throws JanusConnectorException if init fails
     * @throws LineageWarehouseException    if init fails
     */
    public void openGraph(String providerClass, Map<String, Object> configurationProperties, AuditLog auditLog) throws JanusConnectorException, LineageWarehouseException {
        graphFactory = new GraphFactory();
        this.auditLog = auditLog;
        graphDetails = graphFactory.openGraph(providerClass, configurationProperties);
    }

    /**
     * Returns a traversal that is used to query the graph
     *
     * @return traversal for the janusgraph
     */
    public GraphTraversalSource getGraphTraversalSource() {
        return graphDetails.getGraphTraversalSource();
    }


    /**
     * return whether the graph is supporting transactions
     */
    public boolean isSupportingTransactions() {
        return graphDetails.isSupportingTransactions();
    }


    /**
     * Close the connection to the graph
     */
    public void closeGraph() {
        graphFactory.closeGraph();
    }

    /**
     * Helper method that gets a traversal and executes the code that queries the graph
     *
     * @param consumer     must accept a GraphTraversalSource as the first parameter and has a second parameter
     * @param argument     the second argument of the consumer
     * @param errorHandler function that is called when an error occurs while executing the consumer
     * @param <U>          type of the second parameter of the consumer
     */
    public <U> void commit(BiConsumer<GraphTraversalSource, U> consumer, U argument, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        try {
            consumer.accept(g, argument);
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
    }


    /**
     * Helper method that gets a traversal and executes the code that queries the graph
     *
     * @param consumer     must accept a GraphTraversalSource as the first parameter and has two more parameter
     * @param argument1    the second argument of the consumer
     * @param argument2    the third argument of the consumer
     * @param errorHandler function that is called when an error occurs while executing the consumer
     * @param <U>          type of the second parameter of the consumer
     * @param <V>          type of the third parameter of the consumer
     */
    public <V, U> void commit(TriConsumer<GraphTraversalSource, V, U> consumer, V argument1, U argument2, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        try {
            consumer.accept(g, argument1, argument2);
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
    }


    /**
     * Helper method that gets a traversal and executes the code that queries the graph
     *
     * @param function     must accept a GraphTraversalSource as the parameter
     * @param errorHandler consumer that is called when an error occurs while executing the function
     * @param <U>          type of the returned result
     * @return the result of the function
     */
    public <U> U getResult(Function<GraphTraversalSource, U> function, Consumer<Exception> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = null;
        try {
            result = function.apply(g);
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e);
        }
        return result;
    }

    /**
     * Helper method that gets a traversal and executes the code that queries the graph
     *
     * @param function     must accept a GraphTraversalSource as the first parameter and has a second parameter
     * @param argument     the argument of the function
     * @param errorHandler consumer that is called when an error occurs while executing the function
     * @param <V>          the type of the parameter of the function
     * @param <U>          type of the returned result
     * @return the result of the function
     */
    public <U, V> U getResult(BiFunction<GraphTraversalSource, V, U> function, V argument, BiConsumer<Exception, V> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = null;
        try {
            result = function.apply(g, argument);
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e, argument);
        }
        return result;
    }

    /**
     * Helper method that gets a traversal and executes the code that queries the graph
     *
     * @param function     must accept a GraphTraversalSource as the first parameter and has two more parameters
     * @param argument1    the first argument of the function
     * @param argument2    the second argument of the function
     * @param errorHandler consumer that is called when an error occurs while executing the function
     * @param <V>          the type of the second parameter of the function
     * @param <T>          the type of the third parameter of the function
     * @param <U>          type of the returned result
     * @return the result of the function
     */
    public <U, V, T> U getResult(TriFunction<GraphTraversalSource, V, T, U> function, V argument1, T argument2, TriConsumer<Exception, V, T> errorHandler) {
        GraphTraversalSource g = this.getGraphTraversalSource();
        U result = null;
        try {
            result = function.apply(g, argument1, argument2);
            if (isSupportingTransactions()) {
                g.tx().commit();
            }
        } catch (Exception e) {
            g.tx().rollback();
            errorHandler.accept(e, argument1, argument2);
        }
        return result;
    }
}
