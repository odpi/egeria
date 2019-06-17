/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.performanceTesting;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphFactory.open;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_NAME_QUALIFIED_NAME;

@Component
public class GraphTester {


    private static final Logger log = LoggerFactory.getLogger(GraphTester.class);
    private JanusGraph janusGraph;

    private int numberGlossaryTerms;
    private int numberTables;
    private int columnsPerTable;
    private int numberProcesses;
    private int columnsPerProces;
    private int processesPerFlow;

    private List<String> nodes = new ArrayList<>();
    private List<String> properties = new ArrayList<>();


    public GraphTester() {
        setProperties();

        try {
            janusGraph = open();
        } catch (RepositoryErrorException e) {
            log.error("{} Could not open graph database", "GraphBuilder constructor");
        }
    }

    private void setProperties() {
        this.numberGlossaryTerms = 5;
        this.numberTables = 3;
        this.columnsPerTable = 4;
        this.numberProcesses = 2;
        this.columnsPerProces = 1;
        this.processesPerFlow = 1;


        nodes.add("table");
        nodes.add("column");
        nodes.add("glossaryTerm");
        nodes.add("process");

        properties.add("port");
        properties.add("qualifiedName");
    }

    public void generate() {
        if (nodes.contains("table") && nodes.contains("column") && nodes.contains("glossaryTerm")) {
            generateVerbose();
        }
    }

    private void generateVerbose() {
        List<Vertex> glossaryNodes = new ArrayList<>();
        List<Vertex> columnNodes = new ArrayList<>();
        List<List<Vertex>> tableNodes = new ArrayList<>();

        GraphTraversalSource g = janusGraph.traversal();

        for (int i = 0; i < numberGlossaryTerms; i++)
            glossaryNodes.add(g.addV("Glossary term").next());

        List<Vertex> processNodes = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++)
            processNodes.add(g.addV("Process").next());

        for (int j = 0; j < numberTables; j++) {
            Vertex tableVertex = g.addV("table").next();
            tableVertex.property("qualifiedName", "Qualified Name " + j);
            columnNodes = new ArrayList<>();
            for (int i = 0; i < columnsPerTable; i++) {
                Vertex columnVertex = g.addV("column").next();
                columnVertex.property("qualifiedName", "Qualified Name " + i);
                tableVertex.addEdge("Included in", columnVertex);
                int randomNum = ThreadLocalRandom.current().nextInt(0, numberGlossaryTerms);
                Vertex glossaryNode = glossaryNodes.get(randomNum);
                glossaryNode.addEdge("Semantic relationship", columnVertex);
                columnNodes.add(columnVertex);
            }
            tableNodes.add(columnNodes);
        }

        for (int j = 0; j < numberTables; j++) {
            for (int i = 0; i < columnsPerTable; i++) {
                if (j < numberTables -1)
                    tableNodes.get(j).get(i).addEdge("ETL", processNodes.get(j));
                if (j + 1 < numberTables)
                    processNodes.get(j).addEdge("ETL", tableNodes.get(j + 1).get(i));
            }
        }
        g.tx().commit();
    }

    public void exportGraph() {

        try {
            janusGraph.io(IoCore.graphml()).writeGraph("testGraph.graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}