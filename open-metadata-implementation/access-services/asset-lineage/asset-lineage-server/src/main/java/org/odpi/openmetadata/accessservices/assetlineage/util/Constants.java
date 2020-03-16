/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {

    private Constants() {}

    public static final String ASSET_LINEAGE_OMAS = "AssetLineageOmas";
    public static final String REFERENCEABLE = "Referenceable";
    public static final String GUID_PARAMETER = "guid";
    public static final String VALUE_FOR_ACTIVE = "Active";

    public static final String SCHEMA_ELEMENT = "SchemaElement";
    public static final String GLOSSARY_TERM = "GlossaryTerm";
    public static final String SCHEMA_TYPE = "SchemaType";
    public static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";

    //Area 5 Types
    public static final String RELATIONAL_COLUMN = "RelationalColumn";
    public static final String RELATIONAL_TABLE = "RelationalTable";
    public static final String DEPLOYED_DB_SCHEMA_TYPE = "DeployedDatabaseSchema";
    public static final String DATA_STORE = "DataStore";
    public static final String DATABASE = "Database";
    public static final String FILE_FOLDER = "FileFolder";

    public static final String PROCESS = "Process";
    public static final String PORT_ALIAS = "PortAlias";
    public static final String PORT_IMPLEMENTATION = "PortImplementation";
    public static final String TABULAR_SCHEMA_TYPE = "TabularSchemaType";
    public static final String TABULAR_COLUMN_TYPE = "TabularColumnType";
    public static final String TABULAR_COLUMN = "TabularColumn";
    public static final String DATA_FILE = "DataFile";
    public static final String CONNECTION = "Connection";

    //Relationships Type
    public static final String SCHEMA_ATTRIBUTE_TYPE = "SchemaAttributeType";
    public static final String SCHEMA_ATTRIBUTE = "SchemaAttribute";
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    public static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String PORT_DELEGATION = "PortDelegation";
    public static final String PROCESS_PORT = "ProcessPort";
    public static final String LINEAGE_MAPPING = "LineageMapping";
    public static final String PORT_SCHEMA = "PortSchema";
    public static final String NESTED_FILE = "NestedFile";
    public static final String FOLDER_HIERARCHY = "FolderHierarchy";

    //Classification Types
    public static final String CLASSIFIED_ENTITY = "ClassifiedEntity";
    public static final String CLASSIFIED_ENTITY_GUID = "b04047c5-50fc-4010-a07a-544bead159dc";
    public static final String TYPE_EMBEDDED_ATTRIBUTE = "TypeEmbeddedAttribute";

    private static final List<String> qualifiedLineageClassifications = new ArrayList<>();

    static {
        qualifiedLineageClassifications.add(TYPE_EMBEDDED_ATTRIBUTE);
    }

    public static final ImmutableList<String> immutableQualifiedLineageClassifications = ImmutableList.copyOf(qualifiedLineageClassifications);


    // Map of entities to relationship types
    private static final Map<String, String> processRelationshipsTypes = new HashMap<>();

    static {
        processRelationshipsTypes.put(PORT_ALIAS, PORT_DELEGATION);
        processRelationshipsTypes.put(PORT_IMPLEMENTATION, PORT_SCHEMA);
        processRelationshipsTypes.put(TABULAR_SCHEMA_TYPE, ATTRIBUTE_FOR_SCHEMA);
        processRelationshipsTypes.put(SCHEMA_ATTRIBUTE, LINEAGE_MAPPING);
    }

    public static final ImmutableMap<String, String> immutableProcessRelationshipsTypes = ImmutableMap.copyOf(processRelationshipsTypes);

    private static final List<String> validLineageEntityEvents = new ArrayList<>();

    static{
        validLineageEntityEvents.add(GLOSSARY_TERM);
        validLineageEntityEvents.add(TABULAR_COLUMN);
        validLineageEntityEvents.add(RELATIONAL_COLUMN);
        validLineageEntityEvents.add(RELATIONAL_TABLE);
        validLineageEntityEvents.add(DATA_FILE);
        validLineageEntityEvents.add(PROCESS);
    }

    public static final ImmutableList<String> immutableValidLineageEntityEvents = ImmutableList.copyOf(validLineageEntityEvents);

    public static final ImmutableList<String> immutableValidLineageRelationshipTypes = ImmutableList.copyOf(Arrays.asList(SCHEMA_ATTRIBUTE,
            ATTRIBUTE_FOR_SCHEMA, COMPLEX_SCHEMA_TYPE, ASSET_SCHEMA_TYPE, CONNECTION_TO_ASSET, CONNECTION_ENDPOINT, DATA_CONTENT_FOR_DATA_SET,
            SEMANTIC_ASSIGNMENT, PORT_DELEGATION, PROCESS_PORT, LINEAGE_MAPPING, PORT_SCHEMA, NESTED_FILE, FOLDER_HIERARCHY));
}