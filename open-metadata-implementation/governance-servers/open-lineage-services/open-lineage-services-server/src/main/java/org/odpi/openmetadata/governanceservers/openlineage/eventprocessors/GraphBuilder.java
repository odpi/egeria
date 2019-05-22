/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetElement;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Connection;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Term;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetContext;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.AssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;

public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);
    private Graph graph;
    private GraphTraversalSource g;
    private Map<Integer,String> relationships = new HashMap<>();

    public GraphBuilder() {
        this.graph = TinkerGraph.open();
        this.g = graph.traversal();

        this.relationships.put(0,"AttributeForSchema");
        this.relationships.put(1,"SchemaAttributeType");
        this.relationships.put(2,"AttributeForSchema");
        this.relationships.put(3,"AssetSchemaType");
        this.relationships.put(4,"DataContentForDataSet");
    }


    public void addAsset(AssetContext event) {
        Term term = event.getAssets().get(0);
        AssetElement database = event.getAssets().get(0).getElements().get(0);
        Connection connection = database.getConnections().get(0);

        //TODO Open Metadata format should not be used in graph, e.g. column/table types should be properties instead. TBD.
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(addVertex(term));

        createNodes(term,database,vertices);

    }

    /**
     * Connection is part of the event json
     * @param connection
     */
    private void addConnection(Connection connection) {
        String GUID = connection.getGuid();
        String qualifiedName = connection.getQualifiedName();

        Vertex v1 = g.addV(GUID).next();
        v1.property(QUALIFIED_NAME, qualifiedName);
    }

    private Vertex addVertex(Element assetElement) {
        String GUID = assetElement.getGuid();
        String type = assetElement.getType();
        String qualifiedName = assetElement.getQualifiedName();

        Vertex v1 = g.addV(GUID).next();
        v1.property(QUALIFIED_NAME, qualifiedName);
        v1.property(TYPE, type);
        return v1;
    }

    private void addEdge(String relationship, Vertex v1, Vertex v2) {
        v1.addEdge(relationship, v2);
    }


    public void exportGraph() {
        File file = new File(GRAPHML);

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

    private void createNodes(Term term, AssetElement database,  List<Vertex> vertices){
        List<Element> elements ;
        switch (term.getType()){
            case "RelationalColumn":
                elements = createElements(AssetType.RELATIONAL_COLUMN.getElementesContained(),database);
                createGraph(AssetType.RELATIONAL_COLUMN.getIndexForRelationships(),elements,vertices);
                break;
            case "RelationalTable":
                 elements = createElements(AssetType.RELATIONAL_COLUMN.getElementesContained(),database);
                 createGraph(AssetType.RELATIONAL_TABLE.getIndexForRelationships(),elements,vertices);
                break;

        }

    }

    private List<Element> createElements(int assetTypeCode, AssetElement database){
        List<Element> elements = new ArrayList<>();

        for(int i = 0; i < assetTypeCode ; i++){
            Element element = database.getContext().get(i);
            elements.add(element);
        }
        elements.add(database);
        return elements;
    }

    private void createGraph(int index, List<Element> elements,  List<Vertex>  vertices){

        //add nodes
        for(int i = 0; i < elements.size(); i++){
            vertices.add(addVertex(elements.get(i)));
        }

        //add edges
        for(int i = index ; i < vertices.size()-1; i++){
            addEdge(relationships.get(i)
                    ,vertices.get(i+1),vertices.get(i));

        }
    }

}