/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GraphConstructor {


    private static final Logger log = LoggerFactory.getLogger(GraphConstructor.class);
    private Graph graph;
    private GraphTraversalSource g;

    public GraphConstructor() {
        this.graph = TinkerGraph.open();
        this.g = graph.traversal();
    }

    public void addNewEntity(OMRSInstanceEvent omrsInstanceEvent) {
        InstancePropertyValue instancePropertyValue = omrsInstanceEvent.getEntity().getProperties().getInstanceProperties().get("qualifiedName");
        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) instancePropertyValue;
        String qualifiedName = primitivePropertyValue.getPrimitiveValue().toString();

        String GUID = omrsInstanceEvent.getEntity().getGUID();
        String typeDefName = omrsInstanceEvent.getEntity().getType().getTypeDefName();

        Vertex v1 = g.addV(GUID).next();
        v1.property("qualifiedName", qualifiedName);
        v1.property("typeDefName", typeDefName);
    }

    public void addNewRelationship(OMRSInstanceEvent omrsInstanceEvent) {
        EntityProxy proxy1 = omrsInstanceEvent.getRelationship().getEntityOneProxy();
        EntityProxy proxy2 = omrsInstanceEvent.getRelationship().getEntityTwoProxy();

        String GUID1 = proxy1.getGUID();
        String GUID2 = proxy2.getGUID();

        Vertex v1 = g.V().hasLabel(GUID1).next();
        Vertex v2 = g.V().hasLabel(GUID2).next();

        v1.addEdge("Semantic Relationship", v2);
    }

    public void exportGraph() {
        File file = new File("lineageGraph.graphml");

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file, true);
            try {
                GraphMLWriter.build().create().writeGraph(fos, graph);
                log.info("Graph saved to lineageGraph.graphml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}