/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.cassandra;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.*;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.corePropertyTypes;

public class IndexingFactory {

    private static final Logger log = LoggerFactory.getLogger(IndexingFactory.class);

    /**
     * Creates the indexes for the properties of vertices.
     *
     * @param propertyName  - property name
     * @param propertyKeyName - the property name that is stored in the graph
     * @param unique - if the propery name is unique or not
     * @param graph - graph instance to create the indexes
     */
    protected static void createCompositeIndexForVertexProperty(String propertyName, String propertyKeyName, boolean unique, JanusGraph graph)
    {

        final String methodName = "createCompositeIndexForVertexProperty";

        String className = corePropertyTypes.get(propertyName);

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("{} class not found for property {}", methodName, propertyName);
            log.error("{} NO INDEX CREATED for property {}", methodName, propertyName);
            return;
        }

        String indexName = "vertexIndexComposite" + propertyKeyName;
        log.info("INDEX CREATE {}", indexName);

        JanusGraphManagement management = graph.openManagement();

        try {

            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists", methodName, indexName);
                management.rollback();
                return;
            }

            log.info("{} index create {} for vertex property {}", methodName, indexName, propertyKeyName);

            // Check whether property key exists (e.g. edge also has a "guid" property) and if not create it...
            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propertyKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists for property {}", methodName, propertyKeyName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propertyKeyName);
                propertyKey = management.makePropertyKey(propertyKeyName).dataType(clazz).make();
            }


            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Vertex.class).addKey(propertyKey);
            if (unique) {
                indexBuilder.unique();
            }
            JanusGraphIndex index = indexBuilder.buildCompositeIndex();
            if (unique) {
                management.setConsistency(index, ConsistencyModifier.LOCK);
            }
            management.commit();

            if (oldKey) {

                // If we are reusing a key created in an earlier management transaction - e.g. "guid" - we need to reindex
                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();
                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();

            }

            // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute) because the property key may be shared
            // (e.g. vertex "guid" vs edge "guid" but we know the index is for vertices and there are no property key name clashes within vertices.
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
        } catch (Exception e) {
            log.error("{} caught interrupted exception from awaitGraphIndexStatus ENABLED {}", methodName, e);
            management.rollback();
        }

    }

    /**
     * Creates the indexes for the properties of edges.
     *
     * @param propertyName  - property name
     * @param propertyKeyName - the property name that is stored in the graph
     * @param graph - graph instance to create the indexes
     */
    protected static void createCompositeIndexForEdgeProperty(String propertyName, String propertyKeyName,JanusGraph graph) {

        final String methodName = "createCompositeIndexForEdgeProperty";

        String className = corePropertyTypes.get(propertyName);

        Class clazz;
        try {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            log.error("{} class not found for property {}", methodName, propertyName);
            log.error("{} NO INDEX CREATED for property {}", methodName, propertyName);
            return;
        }

        String indexName = "edgeIndexComposite" + propertyKeyName;
        log.info("INDEX CREATE {}", indexName);

        JanusGraphManagement management = graph.openManagement();

        try {

            // Check if index exists
            JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
            if (existingIndex != null) {
                log.info("{} index {} already exists for property {}", methodName, indexName, propertyKeyName);
                management.rollback();
                return;
            } else {
                // index does not already exist - create
                log.info("{} INDEX CREATE {} for vertex property {}", methodName, indexName, propertyKeyName);
            }


            // Check whether property key exists (e.g. edge also has a "guid" property) and if not create it...
            PropertyKey propertyKey;
            boolean oldKey = false;
            PropertyKey existingPropertyKey = management.getPropertyKey(propertyKeyName);
            if (existingPropertyKey != null) {
                log.debug("{} property key already exists for property {}", methodName, propertyKeyName);
                propertyKey = existingPropertyKey;
                oldKey = true;
            } else {
                log.debug("{} make property key for property {}", methodName, propertyKeyName);
                propertyKey = management.makePropertyKey(propertyKeyName).dataType(clazz).make();
            }

            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Edge.class).addKey(propertyKey);
            JanusGraphIndex index = indexBuilder.buildCompositeIndex();
            management.commit();

            // If we are reusing a key creating in an earlier management transaction - e.g. "guid" - we need to reindex

            if (oldKey) {

                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();

                management = graph.openManagement();
                index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);  // no need to get the future - await ENABLED below...
                management.commit();
            }

            // Enable the index - set a relatively short timeout (10 s vs the default of 1 minute) because the property key may be shared
            // (e.g. vertex "guid" vs edge "guid" but we know the index is for edges and there are no property key name clashes within edges.
            log.debug("{} awaitGraphIndexStatus ENABLED for {}", methodName, indexName);
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.ENABLED).timeout(10, ChronoUnit.SECONDS).call();
        }
        catch (Exception e) {
            log.error("{} caught interrupted exception from awaitGraphIndexStatus ENABLED {}", methodName, e);
            management.rollback();
        }

    }
}
