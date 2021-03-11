/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph.validations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph.validations.model.GlossaryValidations;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_QUALIFIED_NAME;

/**
 * Based on provided information, the graph will be interrogated on assets of type Process, DataFile, RelationalColumn,
 * TabularColumn and RelationalColumn, GlossaryTerm, GlossaryCategory and the graph structure around them will be validated. The value of constant
 * {@code INPUT_FOLDER} holds the path to the resource folder where json files of the following structure are kept and
 * used for graph interrogation
 *
 * {
 *   "graphConfigFile": "/Users/myself/Developer/gremlin.properties",
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
 *   },
 *   "glossaryTerms": {
 *     "(category)=Egeria::(term)=Transaction Value Date": {
 *       "tabularColumn": [
 *       ],
 *       "relationalColumn": [
 *       ],
 *       "glossaries": [
 *         "gen!GL@(category)=Egeria"
 *       ],
 *       "glossaryCategories": [
 *         "(category)=Egeria::(category)=Subject Area::(category)=Payment Transfer"
 *       ]
 *     }
 *   },
 *   "glossaryCategories": {
 *     "(category)=Egeria::(category)=Subject Area::(category)=Payment Transfer": [
 *       "(category)=Egeria::(term)=Payment Settled Amount",
 *       "(category)=Egeria::(term)=Transaction Value Date",
 *       "(category)=Egeria::(term)=Payment Settled Amount"
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
    private static final String GLOSSARY_TERM_ERROR_MESSAGE_FORMAT = "The glossary term %s with the qualifiedName %s does not have the correct neighbours %s with type %s";
    private static final ObjectMapper objectMapper  = new ObjectMapper();


    static {
        REQUIRED_JSON_FIELDS.add("graphConfigFile");
        REQUIRED_JSON_FIELDS.add("processes");
        REQUIRED_JSON_FIELDS.add("inTables");
        REQUIRED_JSON_FIELDS.add("outTables");
        REQUIRED_JSON_FIELDS.add("columns");
        REQUIRED_JSON_FIELDS.add("glossaryTerms");
        REQUIRED_JSON_FIELDS.add("glossaryCategories");
    }

    private static final List<String> PROCESSES = new ArrayList<>();
    private static Stream<Arguments> getProcesses(){
        return PROCESSES.stream().map(Arguments::of).collect(Collectors.toList()).stream();
    }

    private static final List<String> IN_TABLES = new ArrayList<>();
    private static Stream<Arguments> getInTables(){
        return IN_TABLES.stream().map(Arguments::of).collect(Collectors.toList()).stream();
    }

    private static final List<String> OUT_TABLES = new ArrayList<>();
    private static Stream<Arguments> getOutTables(){
        return OUT_TABLES.stream().map(Arguments::of).collect(Collectors.toList()).stream();
    }

    private static final Map<String, List<String>> COLUMNS = new HashMap<>();
    private static Stream<Arguments> getColumns(){
        List<Arguments> arguments = new ArrayList<>();
        COLUMNS.forEach( (k, v) -> arguments.add(Arguments.of(k, v)) );
        return arguments.stream();
    }

    private static final Map<String, GlossaryValidations> GLOSSARY_TERMS_VALIDATIONS = new HashMap<>();
    private static Stream<Arguments> getGlossaryTerms(){
        List<Arguments> arguments = new ArrayList<>();
        GLOSSARY_TERMS_VALIDATIONS.forEach( (k, v) -> arguments.add(Arguments.of(k, v)) );
        return arguments.stream();
    }

    private static final Map<String, List<String>> GLOSSARY_CATEGORIES = new HashMap<>();
    private static Stream<Arguments> getGlossaryCategories(){
        List<Arguments> arguments = new ArrayList<>();
        GLOSSARY_CATEGORIES.forEach( (k, v) -> arguments.add(Arguments.of(k, v)) );
        return arguments.stream();
    }

    private static GraphTraversalSource g;

    @BeforeAll
    public static void parseInputAndSetupTestData() throws Exception {
        verifyInputFolderAndJsonFiles();
        for(File file : INPUT_FILES){
            fillTargetsAndInitGraph(file);
        }
    }

    private static void verifyInputFolderAndJsonFiles(){
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
        } catch (IOException ioe){
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

    public static void fillTargetsAndInitGraph(File inputFile) throws Exception{
        JsonObject jsonObject = readJsonObjectFromFile(inputFile);
        fillTargets(jsonObject);
        initGraph(jsonObject.getString("graphConfigFile"));
    }

    private static void fillTargets(JsonObject jsonObject) throws JsonProcessingException {
        addEntitiesToTarget(jsonObject.getJsonArray("processes"), PROCESSES);
        addEntitiesToTarget(jsonObject.getJsonArray("inTables"), IN_TABLES);
        addEntitiesToTarget(jsonObject.getJsonArray("outTables"), OUT_TABLES);
        addEntitiesToTarget(jsonObject.getJsonObject("columns"), COLUMNS);
        addGlossaryEntitiesToTarget(jsonObject.getJsonObject("glossaryTerms"), GLOSSARY_TERMS_VALIDATIONS);
        addEntitiesToTarget(jsonObject.getJsonObject("glossaryCategories"), GLOSSARY_CATEGORIES);
    }

    private static void addEntitiesToTarget(JsonArray entitiesAsJsonArray, Collection<String> target){
        for(int i = 0 ; i < entitiesAsJsonArray.size() ; i++){
            String value = entitiesAsJsonArray.getJsonString(i).getString();
            target.add(value);
        }
    }

    private static void addEntitiesToTarget(JsonObject entitiesAsJsonObject, Map<String, List<String>> target){
        for(String inColumnGuid : entitiesAsJsonObject.keySet()){
            List<String> outColumnGuids = new ArrayList<>();
            JsonArray outColumnGuidsAsJsonArray = entitiesAsJsonObject.getJsonArray(inColumnGuid);
            addEntitiesToTarget(outColumnGuidsAsJsonArray, outColumnGuids);

            target.put(inColumnGuid, outColumnGuids);
        }
    }

    private static void addGlossaryEntitiesToTarget(JsonObject entitiesAsJsonObject, Map<String, GlossaryValidations> target) throws JsonProcessingException {
        for (String inColumnGuid : entitiesAsJsonObject.keySet()) {
            JsonObject outColumnGuidsAsJsonArray = entitiesAsJsonObject.getJsonObject(inColumnGuid);
            GlossaryValidations glossaryValidations = objectMapper.readValue(outColumnGuidsAsJsonArray.toString(), GlossaryValidations.class);
            target.put(inColumnGuid, glossaryValidations);
        }
    }

    private static void initGraph(String configFilePath){
        if(g != null){
            return;
        }
        Graph graph = JanusGraphFactory.open(configFilePath);
        g = graph.traversal();
        log.info("Using graph configured by " + configFilePath);
    }

    @AfterAll
    public static void emptyTargetAndCloseGraph() throws Exception{
        emptyTargets();
        closeGraph();
    }

    private static void emptyTargets(){
        PROCESSES.clear();
        IN_TABLES.clear();
        OUT_TABLES.clear();
        COLUMNS.clear();
        GLOSSARY_TERMS_VALIDATIONS.clear();
        GLOSSARY_CATEGORIES.clear();
    }

    private static void closeGraph() throws Exception {
        g.close();
        g.getGraph().close();
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getProcesses")
    public void validateProcess(String processQualifiedName){
        GraphTraversal<Vertex, Vertex> processTraversal = g.V().has(VERTEX_QUALIFIED_NAME, processQualifiedName)
                .has(PROPERTY_KEY_LABEL, PROCESS);

        assertTrue(processTraversal.hasNext(), "Process not found by qualified name " + processQualifiedName);
        Vertex processAsVertex = processTraversal.next();

        boolean processIsOutput = g.V(processAsVertex.id()).in(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        boolean processIsInput = g.V(processAsVertex.id()).out(TABLE_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.has(PROPERTY_KEY_LABEL, DATA_FILE)).hasNext();

        assertAll(() -> assertThat("Missing connection: " + (processIsOutput? "" : "(process is not output) " ) +
                (processIsInput? "" : "(process is not input)" ), processIsInput && processIsOutput));

        log.debug("Validated process with qualifiedName " + processQualifiedName);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getInTables")
    public void validateInTables(String tableQualifiedName){
        validateTable(tableQualifiedName);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getOutTables")
    public void validateOutTables(String tableQualifiedName){
        validateTable(tableQualifiedName);
    }

    private void validateTable(String tableQualifiedName){
        GraphTraversal<Vertex, Vertex> tableTraversal = g.V().has(VERTEX_QUALIFIED_NAME, tableQualifiedName)
                .or(__.has(PROPERTY_KEY_LABEL, DATA_FILE),
                        __.has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE));

        assertTrue(tableTraversal.hasNext(), "Table not found by qualifiedName " + tableQualifiedName);
        Vertex tableAsVertex = tableTraversal.next();

        boolean tableIsInput = g.V(tableAsVertex.id()).out(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();
        boolean tableIsOutput = g.V(tableAsVertex.id()).in(TABLE_DATA_FLOW).has(PROPERTY_KEY_LABEL, PROCESS).hasNext();

        if( tableIsInput && !tableIsOutput ){
            assertAll(() -> assertThat("Table is not input for process. Required to be only input",
                    processHasOutput(tableAsVertex.id())));
        }
        if( !tableIsInput && tableIsOutput ){
            assertAll(() -> assertThat("Table is not output for process. Required to be only output",
                    processHasInput(tableAsVertex.id())));
        }
        if(tableIsInput && tableIsOutput){
            assertAll(() -> assertThat("Table is not input and output for processes. Required to be both",
                    processHasOutput(tableAsVertex.id()) && processHasInput(tableAsVertex.id())));
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

    @Disabled
    @ParameterizedTest
    @MethodSource("getColumns")
    public void validateColumns(String inputColumnQualifiedName, List<String> outputColumnsQualifiedNames) {
        BiFunction<GraphTraversalSource, Vertex, Boolean> isInputColumn =
                (g, v) -> g.V(v.id()).out(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();
        validateColumn(inputColumnQualifiedName, isInputColumn);

        BiFunction<GraphTraversalSource, Vertex, Boolean> isOutputColumn =
                (g, v) -> g.V(v.id()).in(COLUMN_DATA_FLOW).hasLabel("subProcess").hasNext();
        outputColumnsQualifiedNames.forEach(
                qn -> {
                    this.validateColumn(qn, isOutputColumn);
                    this.validateSubprocess(inputColumnQualifiedName, qn);
                });
    }

    private void validateSubprocess(String inputColumnQualifiedName, String outputColumnQualifiedName) {
        GraphTraversal<Vertex, Vertex> subprocessTraversal =
                g.V().has(VERTEX_QUALIFIED_NAME, outputColumnQualifiedName).in(COLUMN_DATA_FLOW);

        assertTrue(subprocessTraversal.hasNext(),
                "Subprocess not found of output column with qualified name " + outputColumnQualifiedName);
        Vertex subprocessAsVertex = subprocessTraversal.next();

        boolean subprocessIsOutput = g.V(subprocessAsVertex.id()).in(COLUMN_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();
        boolean subprocessIsInput = g.V(subprocessAsVertex.id()).out(COLUMN_DATA_FLOW)
                .or(__.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN)).hasNext();

        assertAll(() -> assertThat(
                "Missing connection: " + (subprocessIsOutput ? "" : "(subprocess is not output) ") +
                        (subprocessIsInput ? "" : "(subprocess is not input)"),
                subprocessIsInput && subprocessIsOutput));
    }

    private void validateColumn(String columnQualifiedName,
                                BiFunction<GraphTraversalSource, Vertex, Boolean> isInputOrOutputColumnFunction) {
        GraphTraversal<Vertex, Vertex> columnTraversal = g.V().has(VERTEX_QUALIFIED_NAME, columnQualifiedName)
                .or(__.has(PROPERTY_KEY_LABEL, RELATIONAL_COLUMN),
                        __.has(PROPERTY_KEY_LABEL, TABULAR_COLUMN));

        assertTrue(columnTraversal.hasNext(), "Column not found by qualifiedName " + columnQualifiedName);
        Vertex columnAsVertex = columnTraversal.next();

        if (!g.V(columnAsVertex.id()).has(PROPERTY_KEY_LABEL, TABULAR_COLUMN).in(ATTRIBUTE_FOR_SCHEMA)
                .in("AssetSchemaType").hasLabel(DATA_FILE).hasNext()) {
            // tabular columns that do not reach a DataFile are not eligible for validation
            return;
        }

        assertAll(() -> assertThat("Provided column must be input or output",
                isInputOrOutputColumnFunction.apply(g, columnAsVertex)));

        log.debug("Validated column with qualifiedName " + columnQualifiedName);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getGlossaryTerms")
    public void validateGlossaryTermGlossary(String glossaryTermQualifiedName, GlossaryValidations validations) {
        Vertex glossaryTermVertex = getGlossaryTermVertex(glossaryTermQualifiedName);
        validateGlossaryTermNeighbours(glossaryTermVertex, validations.getGlossaries(), GLOSSARY);
        log.debug("Validated glossaryTerm with qualifiedName " + glossaryTermQualifiedName + " and relations to Glossary");
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getGlossaryTerms")
    public void validateGlossaryTermGlossaryCategory(String glossaryTermQualifiedName, GlossaryValidations validations) {
        Vertex glossaryTermVertex = getGlossaryTermVertex(glossaryTermQualifiedName);
        validateGlossaryTermNeighbours(glossaryTermVertex, validations.getGlossaryCategories(), GLOSSARY_CATEGORY);
        log.debug("Validated glossaryTerm with qualifiedName " + glossaryTermQualifiedName + " and relations to GlossaryCategories");
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getGlossaryTerms")
    public void validateGlossaryTermTabularColumn(String glossaryTermQualifiedName, GlossaryValidations validations) {
        Vertex glossaryTermVertex = getGlossaryTermVertex(glossaryTermQualifiedName);
        validateGlossaryTermNeighbours(glossaryTermVertex, validations.getTabularColumn(), TABULAR_COLUMN);
        log.debug("Validated glossaryTerm with qualifiedName " + glossaryTermQualifiedName + " and relations to TabularColumn");
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getGlossaryTerms")
    public void validateGlossaryTermRelationalColumn(String glossaryTermQualifiedName, GlossaryValidations validations) {
        Vertex glossaryTermVertex = getGlossaryTermVertex(glossaryTermQualifiedName);
        validateGlossaryTermNeighbours(glossaryTermVertex, validations.getRelationalColumn(), RELATIONAL_COLUMN);
        log.debug("Validated glossaryTerm with qualifiedName " + glossaryTermQualifiedName + " and relations to RelationalColumn");
    }

    private Vertex getGlossaryTermVertex(String glossaryTermQualifiedName) {
        GraphTraversal<Vertex, Vertex> glossaryTermTraversal = g.V().has(VERTEX_QUALIFIED_NAME, glossaryTermQualifiedName)
                .has(PROPERTY_KEY_LABEL, GLOSSARY_TERM);
        assertTrue(glossaryTermTraversal.hasNext(), "GlossaryTerm not found by qualified name " + glossaryTermQualifiedName);
        return glossaryTermTraversal.next();
    }

    private void validateGlossaryTermNeighbours(Vertex glossaryTermVertex, List<String> validationQualifiedNames, String neighboursType) {
        String glossaryTermQualifiedName = (String) glossaryTermVertex.properties(VERTEX_QUALIFIED_NAME).next().value();
        String glossaryTermDisplayName = (String) glossaryTermVertex.properties(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).next().value();

        if (validationQualifiedNames != null && !validationQualifiedNames.isEmpty()) {
            GraphTraversal<Vertex, Vertex> neighboursTraversal = g.V(glossaryTermVertex.id()).inE().outV()
                    .has(PROPERTY_KEY_LABEL, neighboursType);
            List<String> verticesQualifiedNames = new ArrayList<>();
            while (neighboursTraversal.hasNext()) {
                Vertex next = neighboursTraversal.next();
                verticesQualifiedNames.add((String) next.properties(VERTEX_QUALIFIED_NAME).next().value());
            }
            String errorMessage = String.format(GLOSSARY_TERM_ERROR_MESSAGE_FORMAT,
                    glossaryTermDisplayName, glossaryTermQualifiedName, validationQualifiedNames, neighboursType);

            assertTrue(verticesQualifiedNames.containsAll(validationQualifiedNames), errorMessage);
        }
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("getGlossaryCategories")
    public void validateGlossaryCategories(String glossaryCategoryQualifiedName, List<String> glossaryTermQualifiedNames) {
        GraphTraversal<Vertex, Vertex> glossaryCategoryTraversal = g.V().has(VERTEX_QUALIFIED_NAME, glossaryCategoryQualifiedName)
                .has(PROPERTY_KEY_LABEL, GLOSSARY_CATEGORY);
        assertTrue(glossaryCategoryTraversal.hasNext(), "GlossaryCategory not found by qualified name " + glossaryCategoryQualifiedName);
        Vertex glossaryCategoryVertex = glossaryCategoryTraversal.next();
        GraphTraversal<Vertex, Vertex> glossaryTermsNeighbours = g.V(glossaryCategoryVertex.id()).outE().inV().has(PROPERTY_KEY_LABEL, GLOSSARY_TERM);
        List <String> glossaryTerms = new ArrayList<>();

        while (glossaryTermsNeighbours.hasNext()) {
            glossaryTerms.add((String) glossaryTermsNeighbours.next().properties(VERTEX_QUALIFIED_NAME).next().value());
        }

        List<String> glossaryTermsNotInGraph = new ArrayList<>(glossaryTermQualifiedNames);
        glossaryTermsNotInGraph.removeAll(glossaryTerms);
        assertTrue(glossaryTerms.containsAll(glossaryTermQualifiedNames), "The GlossaryCategory " + glossaryCategoryQualifiedName +
                " is not assigned to the following GlossaryTerms " + glossaryTermsNotInGraph);
    }

}
