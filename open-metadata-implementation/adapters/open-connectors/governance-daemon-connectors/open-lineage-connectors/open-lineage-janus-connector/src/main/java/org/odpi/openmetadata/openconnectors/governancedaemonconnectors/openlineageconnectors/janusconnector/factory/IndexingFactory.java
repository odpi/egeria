/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.immutableCorePropertyTypes;

public class IndexingFactory {

    private static final Logger log = LoggerFactory.getLogger(IndexingFactory.class);
    private JanusGraph graph;
    /**
     * Creates the indexes for the properties of vertices.
     *
     * @param propertyName  - property name
     * @param propertyKeyName - the property name that is stored in the graph
     * @param unique - if the propery name is unique or not
     * @param graph - graph instance to create the indexes
     * @param type - type
     */
    protected void createCompositeIndexForProperty(String propertyName,
                                                   String propertyKeyName,
                                                   boolean unique,
                                                   JanusGraph graph,
                                                   Class type) {
        String indexName = "vertexIndexComposite" + propertyKeyName;
        log.info("INDEX to be created {}", indexName);
        this.graph = graph;
        checkIndex(indexName,propertyName,propertyKeyName,unique,type);
    }

    private void checkIndex(String indexName,
                            String propertyName,
                            String propertyKeyName,
                            boolean unique,
                            Class type){

        JanusGraphManagement management = graph.openManagement();

        JanusGraphIndex existingIndex = management.getGraphIndex(indexName);
        if (existingIndex != null) {
            log.info("{} index already exists", indexName);
            management.rollback();
            return;
        }

        createIndex(management,indexName,propertyName,propertyKeyName,unique,type);

    }

    private void createIndex(JanusGraphManagement management,
                             String indexName,
                             String propertyName,
                             String propertyKeyName,
                             boolean unique,
                             Class type) {

        String className = immutableCorePropertyTypes.get(propertyName);

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("class not found for property {}", propertyName);
            log.error("NO INDEX created for property {}", propertyName);
            return;
        }

        PropertyKey propertyKey;
        boolean oldKey = false;
        PropertyKey existingPropertyKey = management.getPropertyKey(propertyKeyName);
        if (existingPropertyKey != null) {
            log.debug("property key already exists for property {}", propertyKeyName);
            propertyKey = existingPropertyKey;
            oldKey = true;
        } else {
            log.debug("make property key for property {}", propertyKeyName);
            propertyKey = management.makePropertyKey(propertyKeyName).dataType(clazz).make();
        }

        buildIndex(management,indexName,propertyKey,unique,oldKey,type);

    }

    private void buildIndex(JanusGraphManagement management,String indexName,PropertyKey propertyKey,boolean unique,boolean oldKey,Class type){

        if(type.equals(Vertex.class)){
            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, type).addKey(propertyKey);
            if (unique) {
                indexBuilder.unique();
            }
            JanusGraphIndex index = indexBuilder.buildCompositeIndex();
            if (unique) {
                management.setConsistency(index, ConsistencyModifier.LOCK);
            }
            management.commit();
        }else if (type.equals(Edge.class)){
            JanusGraphManagement.IndexBuilder indexBuilder = management.buildIndex(indexName, Edge.class).addKey(propertyKey);
            indexBuilder.buildCompositeIndex();
            management.commit();
        }
        else {

            management.rollback();
            return;
        }

        enableIndex(management,indexName,oldKey);
    }

    private void enableIndex(JanusGraphManagement management,String indexName,boolean oldKey){
        final String methodName = "enableIndex";

        try {
            if (oldKey) {
                // If we are reusing a key created in an earlier management transaction - e.g. "guid" - we need to reindex
                // Block until the SchemaStatus transitions from INSTALLED to REGISTERED
                ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.REGISTERED).call();
                management = graph.openManagement();
                JanusGraphIndex index = management.getGraphIndex(indexName);
                management.updateIndex(index, SchemaAction.REINDEX);
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

}
