/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_QUALIFIED_NAME;

/**
 * Based on provided information, the graph will be interrogated on assets of type Process, DataFile, RelationalColumn,
 * TabularColumn and RelationalColumn, and the graph structure around them will be validated. Since asserts are used,
 * VM option -ea is necessary. As for necessary program arguments, only the path to a json file of the following structure
 * is needed
 *
 * {
 *   "graphConfigFile": "/Users/wf40wc/Developer/gremlin.properties",
 *   "processes": [
 *     {
 *       "qualifiedName": "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(transformation_project)=minimal::(dsjob)=flow2",
 *       "noOfSubprocesses": 2
 *     }
 *   ],
 *   "tables":[
 *     {
 *       "qualifiedName":"(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=WORKPLACE"
 *     }
 *   ],
 *   "columns":[
 *     {
 *       "qualifiedName":"(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=locations.csv::(data_file_record)=locations::(data_file_field)=Name"
 *     },
 *     {
 *       "qualifiedName":"(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=WORKPLACE::(database_column)=LOCID"
 *     }
 *   ]
 * }
 *
 */
public class OpenLineageGraphValidation {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageGraphValidation.class);

    private static final String VERTEX_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + PROPERTY_NAME_QUALIFIED_NAME;
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String PROCESS = "Process";
    private static final String RELATIONAL_TABLE = "RelationalTable";
    private static final String DATA_FILE = "DataFile";
    private static final String TABLE_DATA_FLOW = "TableDataFlow";
    private static final String TABULAR_COLUMN = "TabularColumn";
    private static final String COLUMN_DATA_FLOW = "ColumnDataFlow";
    private static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    private static final String RELATIONAL_COLUMN = "RelationalColumn";
    private static final List<Process> PROCESSES = new ArrayList<>();
    private static final List<Table> TABLES = new ArrayList<>();
    private static final List<Column> COLUMNS = new ArrayList<>();

    static class Process{
        private final String qualifiedName;
        private final long noOfProcesses;

        Process(String qualifiedName, long noOfProcesses){
            this.qualifiedName = qualifiedName;
            this.noOfProcesses = noOfProcesses;
        }

        String getQualifiedName() {
            return qualifiedName;
        }

        long getNoOfProcesses() {
            return noOfProcesses;
        }
    }

    private static final Function<JsonObject, Process> createProcess = processAsJsonObject ->
            new Process(processAsJsonObject.getString(QUALIFIED_NAME),
            processAsJsonObject.getJsonNumber("noOfSubprocesses").longValue());

    static class Table{
        private final String qualifiedName;

        Table(String qualifiedName){
            this.qualifiedName = qualifiedName;
        }

        String getQualifiedName() {
            return qualifiedName;
        }
    }

    private static final Function<JsonObject, Table> createTable = tableAsJsonObject ->
            new Table(tableAsJsonObject.getString(QUALIFIED_NAME));

    static class Column{
        private final String qualifiedName;

        Column(String qualifiedName){
            this.qualifiedName = qualifiedName;
        }

        String getQualifiedName() {
            return qualifiedName;
        }
    }

    private static final Function<JsonObject, Column> createColumn = columnAsJsonObject ->
            new Column(columnAsJsonObject.getString(QUALIFIED_NAME));

    private static <T, R> void addEntitiesToTarget(JsonArray entitiesAsJsonArray, Function<T, R> function, List<R> target){
        for(int i = 0 ; i < entitiesAsJsonArray.size() ; i++){
            JsonObject entityAsJsonObject = entitiesAsJsonArray.getJsonObject(i);
            target.add(function.apply((T) entityAsJsonObject));
        }
    }

    private static JsonObject readJsonObjectFromFile(String arg) throws IOException {
        InputStream fis = new FileInputStream(arg);
        JsonReader jsonReader = Json.createReader(fis);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        fis.close();

        return jsonObject;
    }

    private static void verifyArgs(String[] args){
        if(args.length != 1){
            throw new IllegalArgumentException("Args size must be one. Found " + args.length);
        }
        if(!new File(args[0]).exists()){
            throw new IllegalArgumentException("Test input file does not exists at provided location: " + args[0]);
        }
    }

    public static void main(String[] args) throws Exception{
        verifyArgs(args);

        JsonObject jsonObject = readJsonObjectFromFile(args[0]);

        addEntitiesToTarget(jsonObject.getJsonArray("processes"), createProcess, PROCESSES);
        addEntitiesToTarget(jsonObject.getJsonArray("tables"), createTable, TABLES);
        addEntitiesToTarget(jsonObject.getJsonArray("columns"), createColumn, COLUMNS);

        String graphConfigFile = jsonObject.getString("graphConfigFile");
        OpenLineageGraphValidation graphValidation = new OpenLineageGraphValidation(graphConfigFile);
        graphValidation.validate();
        graphValidation.close();
    }

    private final GraphTraversalSource g;

    private OpenLineageGraphValidation(String configFilePath){
        Graph graph = JanusGraphFactory.open(configFilePath);
        g = graph.traversal();
        log.info("Using graph configured by " + configFilePath);
    }

    private void close() throws Exception {
        g.close();
        g.getGraph().close();
    }

    private void validate(){
        PROCESSES.forEach(this::validateProcess);
        log.info("Validated " + PROCESSES.size() + " processes");

        TABLES.forEach(this::validateTable);
        log.info("Validated " + TABLES.size() + " tables");

        COLUMNS.forEach(this::validateColumn);
        log.info("Validated " + COLUMNS.size() + " columns");
    }

    private void validateProcess(Process process){
        GraphTraversal<Vertex, Vertex> processTraversal = g.V().has(VERTEX_QUALIFIED_NAME, process.getQualifiedName())
                .has(PROPERTY_KEY_LABEL, PROCESS);

        assert processTraversal.hasNext() : "Process not found by qualified name " + process.getQualifiedName();
        Vertex processAsVertex = processTraversal.next();

        boolean processIsOutput = g.V(processAsVertex.id()).in(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        boolean processIsInput = g.V(processAsVertex.id()).out(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                    __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        assert processIsInput && processIsOutput : "Missing connection: " + (processIsOutput? "" : "(process is not output) " )
                + (processIsInput? "" : "(process is not input)" );

        long noOfSubprocesses = g.V(processAsVertex.id()).in("includedIn").count().next();
        List<Vertex> subprocesses = g.V(processAsVertex.id()).in("includedIn").next((int)noOfSubprocesses);

        assert process.getNoOfProcesses() == noOfSubprocesses : "Expected number of subprocesses " + process.getNoOfProcesses()
                + " are different than actual " + noOfSubprocesses;

        for (Vertex subprocess : subprocesses) {
            boolean subprocessIsOutput = g.V(subprocess.id()).in(COLUMN_DATA_FLOW)
                                        .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                                            __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();
            boolean subprocessIsInput = g.V(subprocess.id()).out(COLUMN_DATA_FLOW)
                    .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();

            assert subprocessIsInput && subprocessIsOutput : "Missing connection: " + (subprocessIsOutput? "" :
                    "(subprocess is not output) " ) + (subprocessIsInput? "" : "(subprocess is not input)");
        }

        log.debug("Validated process with qualifiedName " + process.getQualifiedName());
    }

    private void validateTable(Table table){
        GraphTraversal<Vertex, Vertex> tableTraversal = g.V().has(VERTEX_QUALIFIED_NAME, table.getQualifiedName())
                .or(__.has(PROPERTY_KEY_LABEL, DATA_FILE),
                    __.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE));

        assert tableTraversal.hasNext() : "Table not found by qualifiedName " + table.getQualifiedName();
        Vertex tableAsVertex = tableTraversal.next();

        boolean tableIsInput = g.V(tableAsVertex.id()).out(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();
        boolean tableIsOutput = g.V(tableAsVertex.id()).in(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();

        if( tableIsInput && !tableIsOutput ){
            assert processHasOutput(tableAsVertex.id()) : "Table is not input for process. Required to be only input";
        }
        if( !tableIsInput && tableIsOutput ){
            assert processHasInput(tableAsVertex.id()) : "Table is not output for process. Required to be only output";
        }
        if(tableIsInput && tableIsOutput){
            assert processHasOutput(tableAsVertex.id()) && processHasInput(tableAsVertex.id()) :
                    "Table is not input and output for processes. Required to be both" ;
        }

        log.debug("Validated table with qualifiedName " + table.getQualifiedName());
    }

    private boolean processHasOutput(Object tableId){
        Vertex process = g.V(tableId).out(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).next();
        return g.V(process.id()).out(TABLE_DATA_FLOW).or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();
    }

    private boolean processHasInput(Object tableId){
        Vertex process = g.V(tableId).in(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).next();
        return g.V(process.id()).in(TABLE_DATA_FLOW).or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();
    }

    private void validateColumn(Column column){
        GraphTraversal<Vertex, Vertex> columnTraversal = g.V().has(VERTEX_QUALIFIED_NAME, column.getQualifiedName())
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN));

        assert columnTraversal.hasNext() : "Column not found by qualifiedName " + column.getQualifiedName();
        Vertex columnAsVertex= columnTraversal.next();

        if( !g.V(columnAsVertex.id()).has(PROPERTY_KEY_LABEL, TABULAR_COLUMN).in(ATTRIBUTE_FOR_SCHEMA)
                .in("AssetSchemaType").hasLabel(DATA_FILE).hasNext() ){
            // tabular columns that do not reach a DataFile are not eligible for validation
            return;
        }

        boolean isInput = g.V(columnAsVertex.id()).out(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();
        boolean isOutput = g.V(columnAsVertex.id()).in(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();

        assert isInput || isOutput : "Column missing input and output. Required to have at least one of them";

        log.debug("Validated column with qualifiedName " + column.getQualifiedName());
    }

}
