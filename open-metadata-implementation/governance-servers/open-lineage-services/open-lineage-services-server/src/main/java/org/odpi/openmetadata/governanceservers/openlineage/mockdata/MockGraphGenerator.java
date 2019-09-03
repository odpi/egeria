/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.mockdata;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mockGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

/**
 * The Open Lineage Services MockGraphGenerator is meant for creating huge lineage data sets, either for performance
 * testing or demoing lineage with realistic data sizes. The user can specify how many tables there will be, columns per
 * table, number of ETL processes, and the number of glossary terms.
 */
public class MockGraphGenerator {

    private static final Logger log = LoggerFactory.getLogger(MockGraphGenerator.class);

    private int numberGlossaryTerms;
    private int numberTables;
    private int processesPerFlow;
    private int tablesPerFlow;
    private int columnsPerTable;
    private int numberProcesses;
    private int numberFlows;
    private boolean hasCycles;


    public MockGraphGenerator() {
        setProperties();
    }

    /**
     * The parameters for the graph that is to be generated are hardcoded for now.
     * A "flow" constitutes of a path between columns of different tables, connected together by process nodes.
     * I.E. columnnode1 -> processnode1 -> columnnode2 -> processnode2 -> columnnode3.
     * The length of this path is specified by the number of processes within the flow.
     */
    private void setProperties() {
        this.numberGlossaryTerms = 20;
        this.numberFlows = 1;
        this.processesPerFlow = 5;
        this.columnsPerTable = 5;
        this.hasCycles = false;  //Only works correctly when numberFlows=1 !

        this.tablesPerFlow = processesPerFlow + 1;
        this.numberTables = numberFlows * tablesPerFlow;
        if (hasCycles)
            this.numberProcesses = numberFlows * processesPerFlow + 1;
        else
            this.numberProcesses = numberFlows * processesPerFlow;

    }

    public void generate() {
        try {
            generateVerbose();
            log.info("Generated mock graph");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate the graph based on the parameters specified in setProperties().
     */
    private void generateVerbose() {
        List<Vertex> columnNodesPerTable;
        List<Vertex> glossaryNodes = new ArrayList<>();
        List<Vertex> tableNodes = new ArrayList<>();
        List<List<Vertex>> columnNodes = new ArrayList<>();

        GraphTraversalSource g = mockGraph.traversal();

        //Create all Glossary Term nodes
        for (int i = 0; i < numberGlossaryTerms; i++) {
            Vertex glossaryVertex = g.addV(NODE_LABEL_GLOSSARYTERM).next();
            addGlossaryTermProperties(i, glossaryVertex);
            glossaryNodes.add(glossaryVertex);
        }

        //Create all Process nodes
        List<Vertex> processNodes = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++) {
            Vertex processVertex = g.addV(NODE_LABEL_SUB_PROCESS).next();
            addProcessProperties(i, processVertex);
            processNodes.add(processVertex);
        }

        //Create all Table nodes and a Host node for each table.
        for (int j = 0; j < numberTables; j++) {
            Vertex tableVertex = g.addV(NODE_LABEL_TABLE).next();
            addTableProperties(j, tableVertex);
            tableNodes.add(tableVertex);

            //Create all Column nodes.
            columnNodesPerTable = new ArrayList<>();
            for (int i = 0; i < columnsPerTable; i++) {
                Vertex columnVertex = g.addV(NODE_LABEL_COLUMN).next();
                addColumnProperties(j, i, columnVertex);
                addEdge(EDGE_LABEL_INCLUDED_IN, columnVertex, tableVertex);

                //Randomly connect Column nodes with Glossary Term nodes.
                if (numberGlossaryTerms != 0) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, numberGlossaryTerms);
                    Vertex glossaryNode = glossaryNodes.get(randomNum);
                    addEdge(EDGE_LABEL_SEMANTIC, columnVertex, glossaryNode);
                }
                columnNodesPerTable.add(columnVertex);
            }
            columnNodes.add(columnNodesPerTable);
        }

        //Create the lineage flows by connecting columns to processes and connecting processes to the columns of the
        // next table.

        //For each flow
        for (int flowIndex = 0; flowIndex < numberFlows; flowIndex++) {

            //For each table in a flow
            for (int tableIndex = 0; tableIndex < tablesPerFlow - 1; tableIndex++) {

                final Vertex process = processNodes.get(flowIndex * processesPerFlow + tableIndex);

                final Vertex table1 = tableNodes.get(flowIndex * tablesPerFlow + tableIndex);
                final Vertex table2 = tableNodes.get(flowIndex * tablesPerFlow + tableIndex + 1);

                final List<Vertex> columnsOfTable1 = columnNodes.get(flowIndex * tablesPerFlow + tableIndex);
                final List<Vertex> columnsOfTable2 = columnNodes.get(flowIndex * tablesPerFlow + tableIndex + 1);

                addEdge(EDGE_LABEL_TABLE_AND_PROCESS, table1, process);
                addEdge(EDGE_LABEL_TABLE_AND_PROCESS, process, table2);

                //For each column in a table
                for (int columnIndex = 0; columnIndex < columnsPerTable; columnIndex++) {

                    final Vertex column1 = columnsOfTable1.get(columnIndex);
                    final Vertex column2 = columnsOfTable2.get(columnIndex);

                    addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, column1, process);
                    addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, process, column2);
                }
            }
            if (hasCycles) {
                final Vertex process = processNodes.get(flowIndex * processesPerFlow + tablesPerFlow -1);

                final Vertex table1 = tableNodes.get(flowIndex * tablesPerFlow + tablesPerFlow -1);
                final Vertex table2 = tableNodes.get(flowIndex * tablesPerFlow + 0);

                final List<Vertex> columnsOfTable1 = columnNodes.get(flowIndex * tablesPerFlow + tablesPerFlow -1) ;
                final List<Vertex> columnsOfTable2 = columnNodes.get(flowIndex * 0);

                addEdge(EDGE_LABEL_TABLE_AND_PROCESS, table1, process);
                addEdge(EDGE_LABEL_TABLE_AND_PROCESS, process, table2);

                //For each column in a table
                for (int columnIndex = 0; columnIndex < columnsPerTable; columnIndex++) {

                    final Vertex column1 = columnsOfTable1.get(columnIndex);
                    final Vertex column2 = columnsOfTable2.get(columnIndex);

                    addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, column1, process);
                    addEdge(EDGE_LABEL_COLUMN_AND_PROCESS, process, column2);
                }
            }
        }
        g.tx().commit();
    }

    private void addGlossaryTermProperties(int i, Vertex glossaryVertex) {
        glossaryVertex.property(PROPERTY_KEY_ENTITY_GUID, "g" + i);
        glossaryVertex.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, "qualified.name.g" + i);
        glossaryVertex.property(PROPERTY_KEY_DISPLAY_NAME, "g" + i);
        glossaryVertex.property(PROPERTY_KEY_GLOSSARY, "glossary");
    }

    private void addProcessProperties(int i, Vertex processVertex) {
        processVertex.property(PROPERTY_KEY_ENTITY_GUID, "p" + i);
        processVertex.property(PROPERTY_KEY_DISPLAY_NAME, "p" + i);
        processVertex.property(PROPERTY_KEY_CREATE_TIME, "createTime");
        processVertex.property(PROPERTY_KEY_UPDATE_TIME, "updateTime");
        processVertex.property(PROPERTY_KEY_FORMULA, "formula");
        processVertex.property(PROPERTY_KEY_PROCESS_DESCRIPTION_URI, "processDescriptionURI");
        processVertex.property(PROPERTY_KEY_VERSION, "version");
        processVertex.property(PROPERTY_KEY_PROCESS_TYPE, "processType");
    }

    private void addTableProperties(int j, Vertex tableVertex) {
        tableVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j);
        tableVertex.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, "qualified.name.t" + j);
        tableVertex.property(PROPERTY_KEY_DISPLAY_NAME, "g" + j);
        tableVertex.property(PROPERTY_KEY_GLOSSARY_TERM, "glossary term");
        tableVertex.property(PROPERTY_KEY_DATABASE_DISPLAY_NAME, "database.displayName");
        tableVertex.property(PROPERTY_KEY_HOST_DISPLAY_NAME, "host.displayName");
        tableVertex.property(PROPERTY_KEY_SCHEMA_DISPLAY_NAME, "schema.displayName");
    }

    private void addColumnProperties(int j, int i, Vertex columnVertex) {
        columnVertex.property(PROPERTY_KEY_ENTITY_GUID, "t" + j + "c" + i);
        columnVertex.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, "qualified.name.t" + j + "c" + i);
        columnVertex.property(PROPERTY_KEY_DISPLAY_NAME, "t" + j + "c" + i);
        columnVertex.property(PROPERTY_KEY_GLOSSARY_TERM, "glossary term");
        columnVertex.property(PROPERTY_KEY_DATABASE_DISPLAY_NAME, "database.displayName");
        columnVertex.property(PROPERTY_KEY_HOST_DISPLAY_NAME, "host.displayName");
        columnVertex.property(PROPERTY_KEY_SCHEMA_DISPLAY_NAME, "schema.displayName");
        columnVertex.property(PROPERTY_KEY_TABLE_DISPLAY_NAME, "table.displayName");
    }

    private void addEdge(String edgeLabel, Vertex fromVertex, Vertex toVertex) {
        Edge edge = fromVertex.addEdge(edgeLabel, toVertex);
        String uuid = UUID.randomUUID().toString();
        edge.property(PROPERTY_KEY_ENTITY_GUID, uuid);
    }

}