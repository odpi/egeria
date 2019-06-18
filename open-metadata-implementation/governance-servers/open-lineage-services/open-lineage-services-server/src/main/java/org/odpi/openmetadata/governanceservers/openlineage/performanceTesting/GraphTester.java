/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.performanceTesting;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphFactory.open;

@Component
public class GraphTester {


    private static final Logger log = LoggerFactory.getLogger(GraphTester.class);
    private JanusGraph janusGraph;

    private int numberGlossaryTerms;
    private int numberTables;
    private int processesPerFlow;
    private int tablesPerFlow;
    private int columnsPerTable;
    private int numberProcesses;
    private int numberFlows;

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
        this.numberGlossaryTerms = 0;
        this.processesPerFlow = 3;
        this.numberFlows = 3;
        this.columnsPerTable = 3;

        this.tablesPerFlow = processesPerFlow + 1;
        this.numberProcesses = numberFlows * processesPerFlow;
        this.numberTables =  numberFlows * tablesPerFlow;

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
        List<Vertex> columnNodes;
        List<Vertex> glossaryNodes = new ArrayList<>();
        List<List<Vertex>> tableNodes = new ArrayList<>();

        GraphTraversalSource g = janusGraph.traversal();

        for (int i = 0; i < numberGlossaryTerms; i++)
            glossaryNodes.add(g.addV("Glossary term").next());

        List<Vertex> processNodes = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++)
            processNodes.add(g.addV("Process").next());

        for (int j = 0; j < numberTables; j++) {
            Vertex tableVertex = g.addV("Table").next();
            tableVertex.property("qualifiedName", "Qualified Name " + j);
            columnNodes = new ArrayList<>();
            for (int i = 0; i < columnsPerTable; i++) {
                Vertex columnVertex = g.addV("Column").next();
                columnVertex.property("qualifiedName", "Qualified Name " + i);
                tableVertex.addEdge("Included in", columnVertex);
                if (numberGlossaryTerms != 0) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, numberGlossaryTerms);
                    Vertex glossaryNode = glossaryNodes.get(randomNum);
                    glossaryNode.addEdge("Semantic relationship", columnVertex);
                }
                columnNodes.add(columnVertex);
            }
            tableNodes.add(columnNodes);
        }

        for (int k = 0; k < numberFlows; k++) {
            for (int j = 0; j < tablesPerFlow; j++) {
                for (int i = 0; i < columnsPerTable; i++) {
                    if (j + 1 < tablesPerFlow)
                        tableNodes.get(k * tablesPerFlow + j).get(i).addEdge("ETL", processNodes.get(k * processesPerFlow + j));
                    if (j + 1 < tablesPerFlow)
                        processNodes.get(k * processesPerFlow + j).addEdge("ETL", tableNodes.get(k * tablesPerFlow + j + 1).get(i));
                }
            }
        }
        g.tx().commit();
    }

        public void exportGraph () {

            try {
                janusGraph.io(IoCore.graphml()).writeGraph("testGraph.graphml");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }


    }