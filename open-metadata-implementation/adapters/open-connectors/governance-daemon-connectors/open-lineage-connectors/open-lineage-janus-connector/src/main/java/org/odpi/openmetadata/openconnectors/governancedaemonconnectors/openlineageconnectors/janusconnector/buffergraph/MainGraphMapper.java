
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphMapper {

    private static final Logger log = LoggerFactory.getLogger(MainGraphMapper.class);

    private JanusGraph bufferGraph;
    private JanusGraph mainGraph;
    private GraphTraversalSource bufferG;
    private GraphTraversalSource mainG;

    public MainGraphMapper(){}

    public MainGraphMapper(JanusGraph bufferGraph,JanusGraph mainGraph){
        this.bufferGraph = bufferGraph;
        this.mainGraph = mainGraph;
        this.bufferG = bufferGraph.traversal();
        this.mainG = bufferGraph.traversal();

    }

    /**
     * Checks bufferGraph about the columns related to the process exist
     *
     * @param columnInGuid  - unique id for starting column
     * @param columnOutGuid - unique id for end column
     */
    public void checkBufferGraph(String columnInGuid,String columnOutGuid,Vertex process){

        try {
            GraphTraversalSource bufferG = bufferGraph.traversal();

            Vertex columnInVertex = bufferG.V().has(PROPERTY_KEY_ENTITY_GUID, columnInGuid).next();
            Vertex columnOutVertex = bufferG.V().has(PROPERTY_KEY_ENTITY_GUID, columnOutGuid).next();
            bufferGraph.tx().commit();

            checkMainGraph(columnInVertex,columnOutVertex,process);

        }catch (Exception e){
            log.error("Something went wrong during the Janus transaction {}",e.getMessage());
            //TODO throw  exception
            bufferGraph.tx().rollback();
        }
    }

    /**
     * Add columns that are related to a process to the mainGraph.
     *
     * @param columnInVertex  - Vertex for starting column
     * @param columnOutVertex - Vertex for end column
     * @param process         - Vertex for process
     */
    private void checkMainGraph(Vertex columnInVertex,Vertex columnOutVertex,Vertex process){
        GraphTraversalSource mainG = mainGraph.traversal();
        GraphTraversalSource bufferG = bufferGraph.traversal();

        Iterator<Vertex> columnIn = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID, columnInVertex.property(PROPERTY_KEY_ENTITY_GUID).value());
        Iterator<Vertex> columnOut = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID, columnOutVertex.property(PROPERTY_KEY_ENTITY_GUID).value());

        Vertex newColumnIn = null;
        Vertex newColumnOut = null;

        if (!columnIn.hasNext()) {
            newColumnIn = checkAssetVertex(mainG, bufferG, columnInVertex);
        }

        if (!columnOut.hasNext()) {
            newColumnOut = checkAssetVertex(mainG, bufferG, columnOutVertex);
        }
        mainG.tx().commit();
        bufferG.tx().commit();

        if (newColumnIn == null) {
            newColumnIn = columnIn.next();
        }

        if (newColumnOut == null) {
            newColumnOut = columnOut.next();
        }

        addProcess(newColumnIn, newColumnOut, process);
        }

    private Vertex checkAssetVertex(GraphTraversalSource mainG,GraphTraversalSource bufferG,Vertex originalVertex){
        Vertex newColumn = mainG.addV(NODE_LABEL_COLUMN)
                                .property(PROPERTY_KEY_ENTITY_NODE_ID,
                                          originalVertex.property(PROPERTY_KEY_ENTITY_GUID).value())
                                .next();

        copyVertexProperties(originalVertex, newColumn);
        addExtraProperties(mainG,bufferG,originalVertex,newColumn);

        return newColumn;
    }

    /**
     * Copies all the properties from an existing Vertex to a new one.
     *
     * @param originalVertex  - Vertex to copy
     * @param newVertex - Vertex to add the copied properties
     */
    private void copyVertexProperties(Vertex originalVertex, Vertex newVertex) {
        final Iterator<VertexProperty<Object>> iterator = originalVertex.properties();
        while (iterator.hasNext()) {
            VertexProperty oldVertexProperty = iterator.next();
            newVertex.property(oldVertexProperty.key(), oldVertexProperty.value());
        }
    }

    /**
     * Add new properties to the asset
     *
     * @param originalVertex  - Vertex to find the properties
     * @param newVertex - Vertex to add the copied properties
     * */
    private void addExtraProperties(GraphTraversalSource mainG,GraphTraversalSource bufferG,Vertex originalVertex,Vertex newVertex){
        //TODO make this generic
        Iterator<Vertex> tableAsset = bufferG.V(originalVertex.id()).
                                                emit().
                                                repeat(bothE().
                                                        otherV().
                                                        simplePath()).
                                                times(2).
                                                or(hasLabel(RELATIONAL_TABLE),hasLabel(DATA_FILE));

        Iterator<Vertex> databaseSchemaType = bufferG.V(originalVertex.id()).emit().repeat(bothE().inV().simplePath()).times(1).
                or(hasLabel(RELATIONAL_DB_SCHEMA_TYPE),hasLabel(TABULAR_SCHEMA_TYPE));

        Iterator<Vertex> databaseSchema = bufferG.V(originalVertex.id()).emit().repeat(bothE().inV().simplePath()).times(3).
                or(hasLabel(DEPLOYED_DB_SCHEMA_TYPE),hasLabel(FILE_FOLDER));

        Iterator<Vertex> database = bufferG.V(originalVertex.id()).emit().repeat(bothE().inV().simplePath()).times(4).
                or(hasLabel(DATABASE),hasLabel(FILE_FOLDER));

        //TODO fix the query to find a Connection of a TabularColumn when we have test data for that
        Iterator<Vertex> connection = bufferG.V(originalVertex.id()).emit().repeat(bothE().inV().simplePath()).times(5).hasLabel(CONNECTION);

        if(tableAsset.hasNext()){
            newVertex.property(PROPERTY_KEY_TABLE_DISPLAY_NAME,tableAsset.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value());
        }

        if(databaseSchemaType.hasNext()){
            newVertex.property(PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME,databaseSchemaType.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value());
        }

        if(databaseSchema.hasNext()){
            newVertex.property(PROPERTY_KEY_SCHEMA_DISPLAY_NAME,databaseSchema.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value());
        }

        if(database.hasNext()){
            newVertex.property(PROPERTY_KEY_DATABASE_DISPLAY_NAME,database.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value());
        }

        getGlossaryTerm(mainG,bufferG,newVertex);

    }

    /**
     * Add glossary term related to the asset
     *
     * @param asset  - Asset to find glossary term
     * */
    private void getGlossaryTerm(GraphTraversalSource mainG,GraphTraversalSource bufferG,Vertex asset){

        Iterator<Vertex> glossaryTermBuffer = bufferG.V()
                                                     .has(PROPERTY_KEY_ENTITY_GUID,asset.property(PROPERTY_KEY_ENTITY_GUID).value().toString())
                                                     .bothE(SEMANTIC_ASSIGNMENT)
                                                     .outV();

        if(glossaryTermBuffer.hasNext()) {
            Vertex glossaryBuffer = glossaryTermBuffer.next();
            String guidGlossary = glossaryBuffer.property(PROPERTY_KEY_ENTITY_GUID).value().toString();

           Vertex glossaryMain =   mainG.V().has(PROPERTY_KEY_ENTITY_GUID, guidGlossary).
                    fold().
                    coalesce(unfold(),
                            addV(NODE_LABEL_GLOSSARYTERM)).property(PROPERTY_KEY_ENTITY_NODE_ID,guidGlossary).next();
            mainG.V(asset.id()).as("v").
                        V(glossaryMain.id()).
                        coalesce(__.outE(EDGE_LABEL_SEMANTIC).where(inV().as("v")),
                                addE(EDGE_LABEL_SEMANTIC).from("v")).next();

            copyVertexProperties(glossaryBuffer,glossaryMain);
        }
    }

    /**
     * Add the Process and the relationships to the Process to the graph
     *
     * @param columnInVertex  - Initial column tha is processed
     * @param columnOutVertex - Column coming out of the process
     * @param process         - Process to add relationships
     *
     * */
    private void addProcess(Vertex columnInVertex,Vertex columnOutVertex,Vertex process){

        GraphTraversalSource mainG = mainGraph.traversal();

        final String processGuid = process.value(PROPERTY_KEY_ENTITY_GUID);
        final String processName = process.value(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);

        if(mainG.V(columnInVertex.id()).bothE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).otherV().has(PROPERTY_KEY_ENTITY_GUID,processGuid).hasNext()){
            return;
        }

        if(columnInVertex != null && columnOutVertex != null) {
            Vertex subProcess = mainG.addV(NODE_LABEL_SUB_PROCESS)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, UUID.randomUUID().toString())
                    .property(PROPERTY_KEY_ENTITY_GUID, processGuid)
                    .property(PROPERTY_KEY_DISPLAY_NAME, processName)
                    .next();

            columnInVertex.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS, subProcess);
            subProcess.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS, columnOutVertex);

            Iterator<Vertex> processTopLevel = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID,process.property(PROPERTY_KEY_ENTITY_GUID).value());
            if(processTopLevel.hasNext()){
                Vertex mainProcess  = processTopLevel.next();
                subProcess.addEdge(EDGE_LABEL_INCLUDED_IN,mainProcess);
                mainG.tx().commit();

                addTableNode(columnInVertex,columnOutVertex,mainProcess);

            }
            else {
                Vertex mainProcess = mainG.addV(NODE_LABEL_PROCESS).next();
                mainProcess.property(PROPERTY_KEY_ENTITY_NODE_ID, processGuid);
                mainProcess.property(PROPERTY_KEY_ENTITY_GUID, processGuid);
                mainProcess.property(PROPERTY_KEY_DISPLAY_NAME, processName);
                subProcess.addEdge(EDGE_LABEL_INCLUDED_IN,mainProcess);

                mainG.tx().commit();

                addTableNode(columnInVertex,columnOutVertex,mainProcess);
            }
        }else{
            log.debug("Process failed");
            mainG.tx().rollback();
        }
    }

    private void addTableNode(Vertex columnInVertex, Vertex columnOutVertex, Vertex process){
        GraphTraversalSource bufferG = bufferGraph.traversal();
        GraphTraversalSource mainG = mainGraph.traversal();


        Vertex tableIn = getTable(bufferG,mainG,columnInVertex);
        Vertex tableOut = getTable(bufferG,mainG,columnOutVertex);

        if (tableIn == null || tableOut == null){
            bufferG.tx().rollback();
            mainG.tx().rollback();
            return;
        }

        addTableRelationships(bufferG,mainG,tableIn,process,columnInVertex);
        addTableRelationships(bufferG,mainG,tableOut,process,columnOutVertex);

        addColumns(bufferG,mainG,tableOut);

        bufferG.tx().commit();
        mainG.tx().commit();
    }

    /**
     * Add all the columns related to a table. This is needed  due to lack of Lineage Mappings
     * between input schema element and it's output.
     *
     * @param bufferG  - Traversal source for buffer Graph
     * @param mainG - Traversal source for main Graph
     * @param tableOut - table to add the columns
     * */
    private void addColumns(GraphTraversalSource bufferG, GraphTraversalSource mainG, Vertex tableOut) {
        List<Vertex> columns =  bufferG.V().
                                has(PROPERTY_KEY_ENTITY_GUID,tableOut.property(PROPERTY_KEY_ENTITY_GUID).value()).
                                inE(NESTED_SCHEMA_ATTRIBUTE).
                                otherV().toList();

        List<String> guidList = columns.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());
        for(String guid: guidList) {

            Iterator<Vertex> columnToAdd = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID, guid);
            if (!columnToAdd.hasNext()) {
                Vertex newColumn = mainG.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, guid).next();
                Vertex originalVertex = bufferG.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
                copyVertexProperties(originalVertex, newColumn);
                addExtraProperties(mainG, bufferG, originalVertex, newColumn);

                Iterator<Vertex> columnVertex = mainG.V(newColumn.id()).bothE(EDGE_LABEL_INCLUDED_IN).otherV()
                        .has(PROPERTY_KEY_ENTITY_NODE_ID, tableOut.property(PROPERTY_KEY_ENTITY_GUID).value());
                if (!columnVertex.hasNext()) {
                    newColumn.addEdge(EDGE_LABEL_INCLUDED_IN, tableOut);
                }
            }
        }
    }

    private Vertex getTable(GraphTraversalSource bufferG,GraphTraversalSource mainG,Vertex asset){
        Iterator<Vertex> table = bufferG.V().has(PROPERTY_KEY_ENTITY_GUID,asset.property(PROPERTY_KEY_ENTITY_GUID).value())
                .emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(RELATIONAL_TABLE),hasLabel(DATA_FILE));

        if (!table.hasNext()){
            return null;
        }

        Vertex tableBuffer = table.next();
        Iterator<Vertex> tableVertex = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID,tableBuffer.property(PROPERTY_KEY_ENTITY_GUID).value());
        if (!tableVertex.hasNext()) {
            Vertex newTable = mainG.addV(NODE_LABEL_TABLE)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID,
                            tableBuffer.property(PROPERTY_KEY_ENTITY_GUID).value())
                    .next();
            copyVertexProperties(tableBuffer, newTable);
            return newTable;
        }

        return tableVertex.next();
    }

    private void addTableRelationships(GraphTraversalSource bufferG,GraphTraversalSource mainG,Vertex table,Vertex process,Vertex column){

        getGlossaryTerm(mainG,bufferG,table);
        Iterator<Vertex> tableVertex = mainG.V(table.id()).outE(EDGE_LABEL_DATAFLOW_WITH_PROCESS).otherV();
        if(!tableVertex.hasNext()){
            table.addEdge(EDGE_LABEL_DATAFLOW_WITH_PROCESS,process);
        }

        Iterator<Vertex> columnVertex = mainG.V(column.id()).outE(EDGE_LABEL_INCLUDED_IN).inV().has(PROPERTY_KEY_ENTITY_GUID, table.property(PROPERTY_KEY_ENTITY_GUID).value());
        if(!columnVertex.hasNext()) {
            column.addEdge(EDGE_LABEL_INCLUDED_IN, table);
        }
    }

    public void updateVertex(Vertex vertex, Map<String, Object> properties){
//        String type = properties.get(PROPERTY_KEY_DISPLAY_NAME).toString();
//        //TODO add types for TabularColumns
//        List<String> typesList = new ArrayList<>(Arrays.asList(RELATIONAL_DB_SCHEMA_TYPE,DEPLOYED_DB_SCHEMA_TYPE,DATABASE,CONNECTION));
//        if(typesList.contains(type)){
//
//        }
        Iterator<Vertex> vertexMainIt = mainG.V().has(PROPERTY_KEY_ENTITY_NODE_ID,vertex.property(PROPERTY_KEY_ENTITY_GUID).value());
        if (!vertexMainIt.hasNext()){
            log.debug("Vertex with guid {} does not exist in the mainGraph",vertex.property(PROPERTY_KEY_ENTITY_GUID).value());
            return;
        }

        mainG.inject(properties)
                .unfold()
                .as("properties")
                .V(vertexMainIt.next().id())
                .as("v")
                .sideEffect(__.select("properties")
                        .unfold()
                        .as("kv")
                        .select("v")
                        .property(__.select("kv").by(Column.keys), __.select("kv").by(Column.values))) ;
        mainG.tx().commit();
    }

}


