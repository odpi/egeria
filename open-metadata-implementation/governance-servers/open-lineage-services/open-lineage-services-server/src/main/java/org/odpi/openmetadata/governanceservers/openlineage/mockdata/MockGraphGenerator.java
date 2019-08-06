/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.mockdata;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mockGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

public class MockGraphGenerator {

    private static final Logger log = LoggerFactory.getLogger(MockGraphGenerator.class);

    private int numberGlossaryTerms;
    private int numberTables;
    private int processesPerFlow;
    private int tablesPerFlow;
    private int columnsPerTable;
    private int numberProcesses;
    private int numberFlows;

    private List<String> nodes = new ArrayList<>();
    private List<String> properties = new ArrayList<>();

    public MockGraphGenerator() {
        setProperties();
    }

    private void setProperties() {

        this.numberGlossaryTerms = 20;
        this.numberFlows = 3;
        this.processesPerFlow = 4;
        this.columnsPerTable = 4;

        this.tablesPerFlow = processesPerFlow + 1;
        this.numberTables = numberFlows * tablesPerFlow;
        this.numberProcesses = numberFlows * processesPerFlow;

        nodes.add("table");
        nodes.add("column");
        nodes.add("glossaryTerm");
        nodes.add("process");

        properties.add("port");
        properties.add("qualifiedName");
        properties.add("guid");
    }

    public void generate() {
        try {
            if (nodes.contains("table") && nodes.contains("column") && nodes.contains("glossaryTerm")) {
                generateVerbose();
            }
            log.info("Generated mock graph");
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void generateVerbose() {
        List<Vertex> columnNodes;
        List<Vertex> glossaryNodes = new ArrayList<>();
        List<List<Vertex>> tableNodes = new ArrayList<>();

        GraphTraversalSource g = mockGraph.traversal();

        for (int i = 0; i < numberGlossaryTerms; i++) {
            Vertex glossaryVertex = g.addV(NODE_LABEL_GLOSSARYTERM).next();
            glossaryVertex.property(PROPERTY_KEY_ENTITY_GUID, "g" + i);
            glossaryNodes.add(glossaryVertex);
        }
        List<Vertex> processNodes = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++) {
            Vertex processVertex = g.addV(NODE_LABEL_PROCESS).next();
            processVertex.property(PROPERTY_KEY_ENTITY_GUID, "p" + i);
            processNodes.add(processVertex);
        }
        for (int j = 0; j < numberTables; j++) {
            Vertex tableVertex = g.addV(NODE_LABEL_TABLE).next();
            tableVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j);
            columnNodes = new ArrayList<>();
            for (int i = 0; i < columnsPerTable; i++) {
                Vertex columnVertex = g.addV(NODE_LABEL_COLUMN).next();
                columnVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j + "c" + i);
                columnVertex.addEdge(EDGE_LABEL_COLUMN_TO_TABLE, tableVertex);
                if (numberGlossaryTerms != 0) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, numberGlossaryTerms);
                    Vertex glossaryNode = glossaryNodes.get(randomNum);
                    columnVertex.addEdge(EDGE_LABEL_ENTITY_TO_GLOSSARYTERM, glossaryNode);
                }
                columnNodes.add(columnVertex);
            }
            tableNodes.add(columnNodes);
        }

        for (int k = 0; k < numberFlows; k++) {
            for (int j = 0; j < tablesPerFlow; j++) {
                for (int i = 0; i < columnsPerTable; i++) {
                    if (j + 1 < tablesPerFlow)
                        tableNodes.get(k * tablesPerFlow + j).get(i).addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, processNodes.get(k * processesPerFlow + j));
                    if (j + 1 < tablesPerFlow)
                        processNodes.get(k * processesPerFlow + j).addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, tableNodes.get(k * tablesPerFlow + j + 1).get(i));
                }
            }
        }
        g.tx().commit();
    }

}