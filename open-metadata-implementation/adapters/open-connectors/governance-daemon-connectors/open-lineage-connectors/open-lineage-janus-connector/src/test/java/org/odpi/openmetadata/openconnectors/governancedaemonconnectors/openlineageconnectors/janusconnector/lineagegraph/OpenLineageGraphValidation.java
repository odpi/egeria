/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static org.junit.Assert.assertTrue;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_QUALIFIED_NAME;

/**
 * Based on provided information, the graph will be interrogated on assets of type Process, DataFile, RelationalColumn,
 * TabularColumn and RelationalColumn, and the graph structure around them will be validated. The value of constant
 * {@code INPUT_FOLDER} holds the path to the resource folder where json files of the following structure are kept and
 * used for graph interogation
 *
 * {
 *   "graphConfigFile": "/Users/wf40wc/Developer/gremlin.properties",
 *   "processes": [
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(transformation_project)=minimal::(dsjob)=flow1"
 *   ],
 *   "inTables": [
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv"
 *   ],
 *   "outTables": [
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME"
 *   ],
 *   "columns" : {
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Id" :
 *     [
 *       "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME::(database_column)=EMPID"
 *     ],
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=First" :
 *     [
 *       "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME::(database_column)=FNAME"
 *     ],
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Last" :
 *     [
 *       "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME::(database_column)=SURNAME"
 *     ],
 *     "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(data_file_folder)=/::(data_file_folder)=data::(data_file_folder)=files::(data_file_folder)=minimal::(data_file)=names.csv::(data_file_record)=names::(data_file_field)=Location" :
 *     [
 *       "(host_(engine))=IS115.OPENMETADATA.IBMCLOUD.COM::(database)=MINIMAL::(database_schema)=DB2INST1::(database_table)=EMPLNAME::(database_column)=LOCID"
 *     ]
 *   }
 * }
 *
 */
public class OpenLineageGraphValidation {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageGraphValidation.class);

    private static final String INPUT_FOLDER = "samples/minimal";
    private static final List<String> REQUIRED_JSON_FIELDS = new ArrayList<>();
    private static final List<File> INPUT_FILES = new ArrayList<>();

    private static final String VERTEX_QUALIFIED_NAME = PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY + PROPERTY_NAME_QUALIFIED_NAME;
    private static final String PROCESS = "Process";
    private static final String RELATIONAL_TABLE = "RelationalTable";
    private static final String DATA_FILE = "DataFile";
    private static final String TABLE_DATA_FLOW = "TableDataFlow";
    private static final String TABULAR_COLUMN = "TabularColumn";
    private static final String COLUMN_DATA_FLOW = "ColumnDataFlow";
    private static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    private static final String RELATIONAL_COLUMN = "RelationalColumn";

    static{
        REQUIRED_JSON_FIELDS.add("graphConfigFile");
        REQUIRED_JSON_FIELDS.add("processes");
        REQUIRED_JSON_FIELDS.add("inTables");
        REQUIRED_JSON_FIELDS.add("outTables");
        REQUIRED_JSON_FIELDS.add("columns");
    }

    @BeforeClass
    public static void verifyInputFolderAndJsonFiles(){
        File inputFolder = new File(Objects.requireNonNull(
                OpenLineageGraphValidation.class.getClassLoader().getResource(INPUT_FOLDER)).getFile());
        verifyInputFolder(inputFolder);

        for( File file : Objects.requireNonNull(inputFolder.listFiles())) {
            if(!valid(file)){
                continue;
            }
            INPUT_FILES.add(file);
        }
        if(INPUT_FILES.isEmpty()){
            throw new RuntimeException("Exiting - Test input folder is missing proper json files");
        }
    }

    private static void verifyInputFolder(File inputFolder){
        if(!inputFolder.exists()){
            throw new IllegalArgumentException("Exiting - Test input folder is missing at provided location: " + INPUT_FOLDER);
        }
        if(!inputFolder.isDirectory()){
            throw new IllegalArgumentException("Exiting - Test input must be a folder: " + INPUT_FOLDER);
        }
        if(Objects.requireNonNull(inputFolder.listFiles()).length < 1){
            throw new IllegalArgumentException("Exiting - Test input folder is empty: " + INPUT_FOLDER);
        }
    }

    private static boolean valid(File inputFile){
        JsonObject jsonObject;
        try{
            jsonObject = readJsonObjectFromFile(inputFile);
        }catch (IOException ioe){
            log.warn( "Unable to read json, skipping file " + inputFile.getPath() + inputFile.getName());
            return false;
        }

        for(String jsonField : REQUIRED_JSON_FIELDS){
            if(!jsonObject.containsKey(jsonField)){
                log.warn( "Missing json field " + jsonField + ", skipping file " + inputFile.getPath() + inputFile.getName());
                return false;
            }
        }

        return true;
    }

    private static JsonObject readJsonObjectFromFile(File inputFile) throws IOException {
        InputStream fis = new FileInputStream(inputFile.getPath());
        JsonReader jsonReader = Json.createReader(fis);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        fis.close();

        return jsonObject;
    }

    private final List<String> processes = new ArrayList<>();
    private final List<String> inTables = new ArrayList<>();
    private final List<String> outTables = new ArrayList<>();
    private final Map<String, List<String>> columns = new HashMap<>();

    private GraphTraversalSource g;

    @Ignore
    @Test
    public void validateGraph() throws Exception{

        for(File file : INPUT_FILES){
            JsonObject jsonObject = readJsonObjectFromFile(file);

            fillTargets(jsonObject);

            initGraph(jsonObject.getString("graphConfigFile"));
            validate();
            closeGraph();

            emptyTargets();
        }

    }

    private void fillTargets(JsonObject jsonObject) {
        addEntitiesToTarget(jsonObject.getJsonArray("processes"), processes);
        addEntitiesToTarget(jsonObject.getJsonArray("inTables"), inTables);
        addEntitiesToTarget(jsonObject.getJsonArray("outTables"), outTables);
        addEntitiesToTarget(jsonObject.getJsonObject("columns"), columns);
    }

    private void addEntitiesToTarget(JsonArray entitiesAsJsonArray, Collection<String> target){
        for(int i = 0 ; i < entitiesAsJsonArray.size() ; i++){
            String value = entitiesAsJsonArray.getJsonString(i).getString();
            target.add(value);
        }
    }

    private void addEntitiesToTarget(JsonObject entitiesAsJsonObject, Map<String, List<String>> target){
        for(String inColumnGuid : entitiesAsJsonObject.keySet()){
            List<String> outColumnGuids = new ArrayList<>();
            JsonArray outColumnGuidsAsJsonArray = entitiesAsJsonObject.getJsonArray(inColumnGuid);
            addEntitiesToTarget(outColumnGuidsAsJsonArray, outColumnGuids);

            columns.put(inColumnGuid, outColumnGuids);
        }
    }

    private void emptyTargets(){
        processes.clear();
        inTables.clear();
        outTables.clear();
        columns.clear();
    }

    private void initGraph(String configFilePath){
        Graph graph = JanusGraphFactory.open(configFilePath);
        g = graph.traversal();
        log.info("Using graph configured by " + configFilePath);
    }

    private void closeGraph() throws Exception {
        g.close();
        g.getGraph().close();
    }

    private void validate(){
        processes.forEach(this::validateProcess);
        log.info("Validated " + processes.size() + " processes");

        inTables.forEach(this::validateTable);
        log.info("Validated " + inTables.size() + " in-tables");

        outTables.forEach(this::validateTable);
        log.info("Validated " + outTables.size() + " out-tables");

        columns.forEach(this::validateColumns);
        log.info("Validated " + columns.size() + " in-columns");
    }

    private void validateProcess(String processQualifiedName){
        GraphTraversal<Vertex, Vertex> processTraversal = g.V().has(VERTEX_QUALIFIED_NAME, processQualifiedName)
                .has(PROPERTY_KEY_LABEL, PROCESS);

        assertTrue("Process not found by qualified name " + processQualifiedName, processTraversal.hasNext());
        Vertex processAsVertex = processTraversal.next();

        boolean processIsOutput = g.V(processAsVertex.id()).in(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        boolean processIsInput = g.V(processAsVertex.id()).out(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                    __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        assertTrue( "Missing connection: " + (processIsOutput? "" : "(process is not output) " )
                + (processIsInput? "" : "(process is not input)" ), processIsInput && processIsOutput) ;

        log.debug("Validated process with qualifiedName " + processQualifiedName);
    }

    private void validateTable(String tableQualifiedName){
        GraphTraversal<Vertex, Vertex> tableTraversal = g.V().has(VERTEX_QUALIFIED_NAME, tableQualifiedName)
                .or(__.has(PROPERTY_KEY_LABEL, DATA_FILE),
                    __.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE));

        assertTrue("Table not found by qualifiedName " + tableQualifiedName, tableTraversal.hasNext() );
        Vertex tableAsVertex = tableTraversal.next();

        boolean tableIsInput = g.V(tableAsVertex.id()).out(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();
        boolean tableIsOutput = g.V(tableAsVertex.id()).in(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();

        if( tableIsInput && !tableIsOutput ){
            assertTrue("Table is not input for process. Required to be only input", processHasOutput(tableAsVertex.id()));
        }
        if( !tableIsInput && tableIsOutput ){
            assertTrue("Table is not output for process. Required to be only output", processHasInput(tableAsVertex.id()));
        }
        if(tableIsInput && tableIsOutput){
            assertTrue("Table is not input and output for processes. Required to be both",
                    processHasOutput(tableAsVertex.id()) && processHasInput(tableAsVertex.id()));
        }

        log.debug("Validated table with qualifiedName " + tableQualifiedName);
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

    private void validateColumns(String inputColumnQualifiedName, List<String> outputColumnsQualifiedNames) {
        BiFunction<GraphTraversalSource, Vertex, Boolean> inputColumn =
                (g, v) -> g.V(v.id()).out(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();
        validateColumn(inputColumnQualifiedName, inputColumn);

        BiFunction<GraphTraversalSource, Vertex, Boolean> outputColumn =
                (g, v) -> g.V(v.id()).in(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();
        outputColumnsQualifiedNames.forEach(
                qn -> {
                    this.validateColumn(qn, outputColumn);
                    this.validateSubprocess(inputColumnQualifiedName, qn);
                });
    }

    private void validateSubprocess(String inputColumnQualifiedName, String outputColumnQualifiedName){
        GraphTraversal<Vertex, Vertex> subprocessTraversal = g.V().has(VERTEX_QUALIFIED_NAME, outputColumnQualifiedName).in(COLUMN_DATA_FLOW);

        assertTrue("Subprocess not found of output column with qualified name " + outputColumnQualifiedName, subprocessTraversal.hasNext());
        Vertex subprocess = subprocessTraversal.next();

        boolean subprocessIsOutput = g.V(subprocess.id()).in(COLUMN_DATA_FLOW)
                            .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                                __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();
        boolean subprocessIsInput = g.V(subprocess.id()).out(COLUMN_DATA_FLOW)
                            .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                                __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();

        assertTrue( "Missing connection: " + (subprocessIsOutput? "" : "(subprocess is not output) " )
        + (subprocessIsInput? "" : "(subprocess is not input)"), subprocessIsInput && subprocessIsOutput);
    }

    private void validateColumn(String columnQualifiedName, BiFunction<GraphTraversalSource, Vertex, Boolean> isInputOrOutputFunction){
        GraphTraversal<Vertex, Vertex> columnTraversal = g.V().has(VERTEX_QUALIFIED_NAME, columnQualifiedName)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN));

        assertTrue("Column not found by qualifiedName " + columnQualifiedName, columnTraversal.hasNext());
        Vertex inColumnAsVertex= columnTraversal.next();

        if( !g.V(inColumnAsVertex.id()).has(PROPERTY_KEY_LABEL, TABULAR_COLUMN).in(ATTRIBUTE_FOR_SCHEMA)
                .in("AssetSchemaType").hasLabel(DATA_FILE).hasNext() ){
            // tabular columns that do not reach a DataFile are not eligible for validation
            return;
        }
        assertTrue("Provided column must be input or output, although required",isInputOrOutputFunction.apply(g, inColumnAsVertex));

        log.debug("Validated column with qualifiedName " + columnQualifiedName);
    }

}
