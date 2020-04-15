/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import java.util.*;

public final class Constants {

    private Constants() {
    }

    //Asset Types
    public static final String ASSET = "Asset";
    public static final String GLOSSARY_TERM = "GlossaryTerm";

    public static final String RELATIONAL_COLUMN_TYPE = "RelationalColumnType";
    public static final String RELATIONAL_COLUMN = "RelationalColumn";
    public static final String RELATIONAL_TABLE_TYPE = "RelationalTableType";
    public static final String RELATIONAL_TABLE = "RelationalTable";
    public static final String RELATIONAL_DB_SCHEMA_TYPE = "RelationalDBSchemaType";
    public static final String DEPLOYED_DB_SCHEMA_TYPE = "DeployedDatabaseSchema";
    public static final String DATABASE = "Database";
    public static final String DATA_STORE = "DataStore";
    public static final String TABULAR_COLUMN = "TabularColumn";
    public static final String TABULAR_COLUMN_TYPE = "TabularColumnType";
    public static final String TABULAR_SCHEMA_TYPE = "TabularSchemaType";
    public static final String DATA_FILE = "DataFile";
    public static final String FILE_FOLDER = "FileFolder";
    public static final String CONNECTION = "Connection";

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

    //Instance Properties fields
    public static final String TYPE = "dataType";
    public static final String QUALIFIED_NAME = "qualifiedName";
    public static final String NAME = "name";

    //File names
    public static final String GRAPHML = "lineageGraph.graphml";

    // Map of names to property key names
    public static final Map<String, String> entitiesRelationshipTypes = new HashMap<String,String>() {{
        put(GLOSSARY_TERM, SEMANTIC_ASSIGNMENT);
        put(RELATIONAL_COLUMN_TYPE, SCHEMA_ATTRIBUTE_TYPE);
        put(RELATIONAL_COLUMN, ATTRIBUTE_FOR_SCHEMA);
        put(RELATIONAL_TABLE_TYPE, SCHEMA_ATTRIBUTE_TYPE);
        put(RELATIONAL_TABLE, ATTRIBUTE_FOR_SCHEMA);
        put(RELATIONAL_DB_SCHEMA_TYPE, ASSET_SCHEMA_TYPE);
        put(DEPLOYED_DB_SCHEMA_TYPE, DATA_CONTENT_FOR_DATA_SET);
    }};


    public static final List<String> edgesForRelationalColumn = new ArrayList(Arrays.asList(SCHEMA_ATTRIBUTE_TYPE, ATTRIBUTE_FOR_SCHEMA, SCHEMA_ATTRIBUTE_TYPE,
            ATTRIBUTE_FOR_SCHEMA, ASSET_SCHEMA_TYPE, DATA_CONTENT_FOR_DATA_SET));

    public static final List<String> edgesForTabularColumn = new ArrayList(Arrays.asList(SCHEMA_ATTRIBUTE_TYPE, ATTRIBUTE_FOR_SCHEMA,
            ASSET_SCHEMA_TYPE, NESTED_FILE));

    public static final List<String> orderRelational = Arrays.asList(RELATIONAL_COLUMN_TYPE, RELATIONAL_COLUMN, RELATIONAL_TABLE_TYPE,
            RELATIONAL_TABLE, RELATIONAL_DB_SCHEMA_TYPE, DEPLOYED_DB_SCHEMA_TYPE, DATABASE);

    public static final List<String> orderTabular = Arrays.asList(TABULAR_COLUMN_TYPE, TABULAR_COLUMN, TABULAR_SCHEMA_TYPE,
            DATA_FILE,FILE_FOLDER);


}