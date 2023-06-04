/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

/**
 * Constants for Open Metadata Types names used to build lineage functionality
 */
public final class AssetLineageConstants {

    public static final String LINEAGE_CLASSIFICATION_TYPES_KEY = "LineageClassificationTypes";
    public static final String ASSET_LINEAGE_OMAS = "AssetLineageOmas";
    public static final String REFERENCEABLE = "Referenceable";
    public static final String GUID_PARAMETER = "guid";
    public static final String VALUE_FOR_ACTIVE = "Active";
    public static final String GLOSSARY_CATEGORY = "GlossaryCategory";
    public static final String GLOSSARY_TERM = "GlossaryTerm";
    public static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    public static final String ZONE_MEMBERSHIP = "zoneMembership";
    //Area 5 Types
    public static final String RELATIONAL_COLUMN = "RelationalColumn";
    public static final String RELATIONAL_TABLE = "RelationalTable";
    public static final String DATABASE = "Database";
    public static final String FILE_FOLDER = "FileFolder";
    public static final String PROCESS = "Process";
    public static final String PORT_IMPLEMENTATION = "PortImplementation";
    public static final String TABULAR_SCHEMA_TYPE = "TabularSchemaType";
    public static final String TABULAR_COLUMN = "TabularColumn";
    public static final String ASSET = "Asset";
    public static final String TABULAR_FILE_COLUMN = "TabularFileColumn";
    public static final String DATA_STORE = "DataStore";
    public static final String DATA_FILE = "DataFile";
    public static final String CONNECTION = "Connection";
    public static final String ENDPOINT = "Endpoint";
    public static final String SCHEMA_ATTRIBUTE = "SchemaAttribute";
    public static final String TOPIC = "Topic";
    public static final String EVENT_SCHEMA_ATTRIBUTE = "EventSchemaAttribute";
    public static final String PORT = "Port";

    //Relationships Type
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
    public static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String DATA_CONTENT_FOR_DATA_SET = "DataContentForDataSet";
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";
    public static final String TERM_CATEGORIZATION = "TermCategorization";
    public static final String TERM_ANCHOR = "TermAnchor";
    public static final String CATEGORY_ANCHOR = "CategoryAnchor";
    public static final String PORT_DELEGATION = "PortDelegation";
    public static final String PROCESS_PORT = "ProcessPort";
    public static final String COLLECTION_MEMBERSHIP = "CollectionMembership";
    public static final String DATA_FLOW = "DataFlow";
    public static final String LINEAGE_MAPPING = "LineageMapping";
    public static final String PORT_SCHEMA = "PortSchema";
    public static final String NESTED_FILE = "NestedFile";
    public static final String FOLDER_HIERARCHY = "FolderHierarchy";
    public static final String PROCESS_HIERARCHY = "ProcessHierarchy";
    public static final String SCHEMA_TYPE_OPTION = "SchemaTypeOption";
    public static final String CLASSIFICATION = "Classification";
    public static final String CLASSIFICATION_NAME_CONFIDENTIALITY = "Confidentiality";
    public static final String CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP = "AssetZoneMembership";
    public static final String CLASSIFICATION_NAME_SUBJECT_AREA = "SubjectArea";
    public static final String CLASSIFICATION_NAME_ASSET_OWNERSHIP = "AssetOwnership";
    public static final String CLASSIFICATION_NAME_INCOMPLETE = "Incomplete";
    public static final String CLASSIFICATION_NAME_PRIMARY_CATEGORY = "PrimaryCategory";
    public static final String UPDATE_TIME = "updateTime";
    public static final String ANCHOR_GUID = "anchorGUID";

    private AssetLineageConstants() {
    }
}