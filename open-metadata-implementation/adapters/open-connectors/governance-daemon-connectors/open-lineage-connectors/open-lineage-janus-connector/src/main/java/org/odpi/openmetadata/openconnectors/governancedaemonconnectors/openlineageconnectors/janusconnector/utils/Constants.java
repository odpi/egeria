/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_ANTONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CLASSIFICATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_IS_A_RELATIONSHIP;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_RELATED_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_REPLACEMENT_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SYNONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TERM_CATEGORIZATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TRANSLATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ADDITIONAL_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_EXTENDED_PROPERTIES;

public final class Constants {

    private Constants() {
    }

    //Asset Types
    public static final String GLOSSARY = "Glossary";
    public static final String GLOSSARY_TERM = "GlossaryTerm";
    public static final String GLOSSARY_CATEGORY = "GlossaryCategory";
    public static final String RELATIONAL_COLUMN_TYPE = "RelationalColumnType";
    public static final String RELATIONAL_COLUMN = "RelationalColumn";
    public static final String RELATIONAL_TABLE_TYPE = "RelationalTableType";
    public static final String RELATIONAL_TABLE = "RelationalTable";
    public static final String RELATIONAL_DB_SCHEMA_TYPE = "RelationalDBSchemaType";
    public static final String DEPLOYED_DB_SCHEMA_TYPE = "DeployedDatabaseSchema";
    public static final String DATABASE = "Database";
    public static final String TABULAR_COLUMN = "TabularColumn";
    public static final String TABULAR_FILE_COLUMN = "TabularFileColumn";
    public static final String TABULAR_COLUMN_TYPE = "TabularColumnType";
    public static final String TABULAR_SCHEMA_TYPE = "TabularSchemaType";
    public static final String DATA_FILE = "DataFile";
    public static final String AVRO_FILE = "AvroFile";
    public static final String CSV_FILE = "CSVFile";
    public static final String JSON_FILE = "JSONFile";
    public static final String KEYSTORE_FILE = "KeystoreFile";
    public static final String LOG_FILE = "LogFile";
    public static final String MEDIA_FILE = "MediaFile";
    public static final String DOCUMENT = "Document";
    public static final String FILE_FOLDER = "FileFolder";
    public static final String CONNECTION = "Connection";
    public static final String PROCESS = "Process";
    public static final String ENDPOINT = "Endpoint";
    public static final String COLLECTION = "Collection";
    public static final String TOPIC = "Topic";
    public static final String EVENT_SCHEMA_ATTRIBUTE = "EventSchemaAttribute";
    public static final String EVENT_TYPE_LIST = "EventTypeList";
    public static final String EVENT_TYPE = "EventType";

    public static final Collection<String> DATA_FILE_AND_SUBTYPES = Arrays.asList(DATA_FILE, AVRO_FILE, CSV_FILE, JSON_FILE,
            KEYSTORE_FILE, LOG_FILE, MEDIA_FILE, DOCUMENT);

    public static final Collection<String> ASSETS = Arrays.asList(RELATIONAL_TABLE, DATA_FILE, AVRO_FILE, CSV_FILE, JSON_FILE,
            KEYSTORE_FILE, LOG_FILE, MEDIA_FILE, DOCUMENT, TOPIC, PROCESS);

    public static final String RELATIONAL_TABLE_KEY = "relationalTable";
    public static final String TRANSFORMATION_PROJECT_KEY = "transformationProject";
    public static final String SCHEMA_TYPE_KEY = "schema";
    public static final String DATABASE_KEY = "database";
    public static final String DATA_FILE_KEY = "dataFile";
    public static final String FILE_FOLDER_KEY = "fileFolder";
    public static final String CONNECTION_KEY = "connection";
    public static final String GLOSSARY_KEY = "glossary";
    public static final String EVENT_TYPE_LIST_KEY = "eventTypeList";
    public static final String EVENT_TYPE_KEY = "eventType";
    public static final String TOPIC_KEY = "topic";
    //Relationships Type
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String TERM_CATEGORIZATION = "TermCategorization";
    public static final String TERM_ANCHOR = "TermAnchor";
    public static final String PROCESS_HIERARCHY = "ProcessHierarchy";
    public static final String PROCESS_PORT = "ProcessPort";
    public static final String PORT_IMPLEMENTATION = "PortImplementation";
    public static final String DATA_FLOW = "DataFlow";
    public static final String PORT_SCHEMA = "PortSchema";
    public static final String NESTED_FILE = "NestedFile";
    public static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    public static final String FOLDER_HIERARCHY = "FolderHierarchy";
    public static final String SCHEMA_TYPE_OPTION = "SchemaTypeOption";
    public static final String EMPTY_STRING = "";
    public static final String COMMA_SPACE_DELIMITER = ", ";
    public static final String COLUMN_SPACE_DELIMITER = ": ";
    public static final List<String> EMBEDDED_PROPERTIES = Arrays.asList(PROPERTY_KEY_ADDITIONAL_PROPERTIES, PROPERTY_KEY_EXTENDED_PROPERTIES);
    public static final String SUB_GRAPH = "subGraph";
    public static final String GENERIC_QUERY_EXCEPTION = "Exception while querying for guid {}. Executed rollback.";
    public static final String S = "s";
    public static final String INCOMPLETE = "Incomplete";
    public static final String CLASSIFICATION_GRAPH = "classificationGraph";

    public static final String KV = "kv";
    public static final String CLOSE_LINEAGE_GRAPH_EXCEPTION = "Exception while closing lineage graph";
    public static final String EXCEPTION_WHILE_CLOSING_LINEAGE_GRAPH_MESSAGE = CLOSE_LINEAGE_GRAPH_EXCEPTION + ": ";
    public static final String FROM = "from";

    public static final String ASSET_LINEAGE_VARIABLES = "ASSET_LINEAGE_VARIABLES";
    public static final String INPUT_PORT = "INPUT_PORT";
    public static final String PROPERTIES = "properties";
    public static final String V = "v";
    public static final String VERTEX_GUID_NOT_FOUND_WHEN_UPDATE = "When trying to update, vertex with guid {} was not found  ";

    public static final String EDGE_GUID_NOT_FOUND_WHEN_UPDATE = "When trying to update, edge with guid {} was not found";
    public static final String CLASSIFICATION_WITH_GUID_NOT_FOUND = "Classification with guid {} not found";

    public static final String VERTEX_WITH_GUID_IS_NOT_PRESENT = "Vertex with guid is not present {}";
    public static final String VERTEX_WITH_GUID_DELETED = "Vertex with guid {} deleted";
    public static final String EDGE_WITH_GUID_DID_NOT_DELETE = "Edge with guid did not delete {}";
    public static final String EDGE_WITH_GUID_DELETED = "Edge with guid {} deleted";
    public static final String EDGE = "edge";

    public static final String[] RELATIONAL_TABLE_CONTEXT_IN_EDGES = new String[]{ATTRIBUTE_FOR_SCHEMA, ASSET_SCHEMA_TYPE, DATA_CONTENT_FOR_DATA_SET, CONNECTION_TO_ASSET};

    public static final String[] GLOSSARY_TERM_AND_CLASSIFICATION_EDGES = {EDGE_LABEL_SEMANTIC_ASSIGNMENT, EDGE_LABEL_RELATED_TERM,
            EDGE_LABEL_SYNONYM, EDGE_LABEL_ANTONYM, EDGE_LABEL_REPLACEMENT_TERM, EDGE_LABEL_TRANSLATION, EDGE_LABEL_IS_A_RELATIONSHIP,
            EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_TERM_CATEGORIZATION};

    public static final String[] RELATIONAL_COLUMN_AND_CLASSIFICATION_EDGES =
            {NESTED_SCHEMA_ATTRIBUTE, EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_SEMANTIC_ASSIGNMENT};

    public static final String[] TABULAR_COLUMN_AND_CLASSIFICATION_EDGES = {ATTRIBUTE_FOR_SCHEMA, EDGE_LABEL_CLASSIFICATION, EDGE_LABEL_SEMANTIC_ASSIGNMENT};


}
