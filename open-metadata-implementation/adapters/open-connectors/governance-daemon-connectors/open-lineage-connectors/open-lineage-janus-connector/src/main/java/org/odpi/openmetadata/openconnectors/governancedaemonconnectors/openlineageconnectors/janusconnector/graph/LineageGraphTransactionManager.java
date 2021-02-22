/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class responsible with transaction commits for operations done on the graph. Wraps incoming functional
 * interfaces in a try/catch block, calls their specific method and commits the transaction.
 */
public class LineageGraphTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphTransactionManager.class);

    /**
     * Commits a transaction after calling method apply on incoming function
     *
     * @param factory graph factory
     * @param g graph traversal source
     * @param function graph operation
     * @param entity function argument
     * @param errorMessage message logged at ERROR level, in case of exceptions
     *
     * @return function output
     */
    public static <T, R> R commit(GraphFactory factory, GraphTraversalSource g,
                                  Function<T, R> function, T entity, String errorMessage){

        R object = null;
        try{
            object = function.apply(entity);

            if (factory.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error(errorMessage, e);
            if (factory.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
        return object;
    }

    /**
     * Commits a transaction after calling method get on incoming supplier
     *
     * @param factory graph factory
     * @param g graph traversal source
     * @param supplier graph operation
     * @param errorMessage message logged at ERROR level, in case of exceptions
     *
     * @return supplier output
     */
    public static <R> R commit(GraphFactory factory, GraphTraversalSource g,
                               Supplier<R> supplier, String errorMessage){

        R object = null;
        try{
            object = supplier.get();

            if (factory.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error(errorMessage, e);
            if (factory.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
        return object;
    }

    /**
     * Commits a transaction after calling method accept on incoming consumer
     *
     * @param factory graph factory
     * @param g graph traversal source
     * @param consumer graph operation
     * @param arg1 consumer argument
     * @param errorMessage message logged at ERROR level, in case of exceptions
     */
    public static <T> void commit(GraphFactory factory, GraphTraversalSource g,
                                  Consumer<T> consumer, T arg1, String errorMessage){

        try{
            consumer.accept(arg1);

            if (factory.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error(errorMessage, e);
            if (factory.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
    }

    /**
     * Commits a transaction after calling method accept on incoming biConsumer
     *
     * @param factory graph factory
     * @param g graph traversal source
     * @param biConsumer graph operation
     * @param arg1 biConsumer argument
     * @param arg2 biConsumer argument
     * @param errorMessage message logged at ERROR level, in case of exceptions
     */
    public static <T, U> void commit(GraphFactory factory, GraphTraversalSource g,
                                     BiConsumer<T, U> biConsumer, T arg1, U arg2, String errorMessage){

        try{
            biConsumer.accept(arg1, arg2);

            if (factory.isSupportingTransactions()) {
                g.tx().commit();
            }
        }catch (Exception e){
            log.error(errorMessage, e);
            if (factory.isSupportingTransactions()) {
                g.tx().rollback();
            }
        }
    }

}
