/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ADDITIONAL_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_EXTENDED_PROPERTIES;
import java.util.List;

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
            KEYSTORE_FILE, LOG_FILE, MEDIA_FILE, DOCUMENT, TOPIC);

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
    public static final String SCHEMA_ATTRIBUTE_TYPE = "SchemaAttributeType";
    public static final String SCHEMA_ATTRIBUTE = "SchemaAttribute";
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    public static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String CONNECTION_CONNECTOR_TYPE = "ConnectionConnectorType";
    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String TERM_CATEGORIZATION = "TermCategorization";
    public static final String PORT_DELEGATION = "PortDelegation";
    public static final String PROCESS_PORT = "ProcessPort";
    public static final String PORT_IMPLEMENTATION = "PortImplementation";
    public static final String LINEAGE_MAPPING = "LineageMapping";
    public static final String SCHEMA_TYPE = "SchemaType";
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
    public static final String GENERIC_QUERY_EXCEPTION = "Exception while querying {} of guid {}: {}. Executed rollback.";
    public static final String ULTIMATE_DESTINATION_HORIZONTAL_LINEAGE = "ultimate destination horizontal lineage";
    public static final String TABULAR_COLUMN_VERTICAL_LINEAGE = "tabular column vertical lineage";
    public static final String RELATIONAL_COLUMN_VERTICAL_LINEAGE = "relational column vertical lineage";
    public static final String GLOSSARY_TERM_VERTICAL_LINEAGE = "glossary term vertical lineage";
    public static final String END_TO_END_HORIZONTAL_LINEAGE = "end to end horizontal lineage";
    public static final String ULTIMATE_SOURCE_HORIZONTAL_LINEAGE = "ultimate source horizontal lineage";
    public static final String S = "s";
    public static final String INCOMPLETE = "Incomplete";
    public static final String CLASSIFICATION_GRAPH = "classificationGraph";

}