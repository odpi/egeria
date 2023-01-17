/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * SchemaElementMapper provides property name mapping for SchemaElements.
 */
public class SchemaElementMapper
{
    public static final String SCHEMA_ELEMENT_TYPE_GUID                 = "718d4244-8559-49ed-ad5a-10e5c305a656";   /* from Area 5 */
    public static final String SCHEMA_ELEMENT_TYPE_NAME                 = "SchemaElement";
    /* Referenceable */

    public static final String SCHEMA_DISPLAY_NAME_PROPERTY_NAME               = "displayName";          /* from SchemaElement entity */
    public static final String SCHEMA_DESCRIPTION_PROPERTY_NAME                = "description";          /* from SchemaElement entity */
    public static final String SCHEMA_IS_DEPRECATED_PROPERTY_NAME              = "isDeprecated";         /* from SchemaElement entity */

    /* For Schema Type */
    public static final String SCHEMA_TYPE_TYPE_GUID                    = "5bd4a3e7-d22d-4a3d-a115-066ee8e0754f";   /* from Area 5 */
    public static final String SCHEMA_TYPE_TYPE_NAME                    = "SchemaType";
    /* SchemaElement */

    public static final String SCHEMA_VERSION_NUMBER_PROPERTY_NAME             = "versionNumber";        /* from SchemaType entity */
    public static final String SCHEMA_AUTHOR_PROPERTY_NAME                     = "author";               /* from SchemaType entity */
    public static final String SCHEMA_USAGE_PROPERTY_NAME                      = "usage";                /* from SchemaType entity */
    public static final String SCHEMA_ENCODING_STANDARD_PROPERTY_NAME          = "encodingStandard";     /* from SchemaType entity */
    public static final String SCHEMA_NAMESPACE_PROPERTY_NAME                  = "namespace";            /* from SchemaType entity */


    /* For Complex Schema Type */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_GUID            = "786a6199-0ce8-47bf-b006-9ace1c5510e4";    /* from Area 5 */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_NAME            = "ComplexSchemaType";
    /* SchemaType */

    public static final String STRUCT_SCHEMA_TYPE_TYPE_GUID             = "a13b409f-fd67-4506-8d94-14dfafd250a4";    /* from Area 5 */
    public static final String STRUCT_SCHEMA_TYPE_TYPE_NAME             = "StructSchemaType";
    /* ComplexSchemaType */

    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";  /* from Area 5 */
    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "AttributeForSchema";
    /* End1 = ComplexSchemaType; End 2 = SchemaAttribute */

    /* For Literal Schema Type */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_GUID            = "520ebb91-c4eb-4d46-a3b1-974875cdcf0d";  /* from Area 5 */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_NAME            = "LiteralSchemaType";
    /* SchemaType */

    /* For External Schema Type */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_GUID           = "78de00ea-3d69-47ff-a6d6-767587526624";  /* from Area 5 */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_NAME           = "ExternalSchemaType";
    /* SchemaType */

    /* For Schema Type Choice */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_GUID             = "5caf954a-3e33-4cbd-b17d-8b8613bd2db8";  /* from Area 5 */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_NAME             = "SchemaTypeChoice";
    /* SchemaType */

    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID = "eb4f1f98-c649-4560-8a46-da17c02764a9";   /* from Area 5 */
    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME = "SchemaTypeOption";
    /* End1 = SchemaTypeChoice; End 2 = SchemaType */

    /* For Schema Type Choice */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_GUID            = "b5ec6e07-6419-4225-9dc4-fb55aba255c6";  /* from Area 5 */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_NAME            = "SimpleSchemaType";
    /* SchemaType */

    /* For Primitive Schema Type */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_GUID          = "f0f75fba-9136-4082-8352-0ad74f3c36ed";  /* from Area 5 */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_NAME          = "PrimitiveSchemaType";
    /* SimpleSchemaType */

    /* For Enum Schema Type */
    public static final String ENUM_SCHEMA_TYPE_TYPE_GUID               = "24b092ac-42e9-43dc-aeca-eb034ce307d9";  /* from Area 5 */
    public static final String ENUM_SCHEMA_TYPE_TYPE_NAME               = "EnumSchemaType";
    /* SimpleSchemaType */

    public static final String DATA_TYPE_PROPERTY_NAME                  = "dataType";     /* from SimpleSchemaType and LiteralSchemaType entity */
    public static final String DEFAULT_VALUE_PROPERTY_NAME              = "defaultValue"; /* from PrimitiveSchemaType entity */
    public static final String FIXED_VALUE_PROPERTY_NAME                = "fixedValue";   /* from LiteralSchemaType entity */

    /* For Map Schema Type */
    public static final String MAP_SCHEMA_TYPE_TYPE_GUID                = "bd4c85d0-d471-4cd2-a193-33b0387a19fd";   /* from Area 5 */
    public static final String MAP_SCHEMA_TYPE_TYPE_NAME                = "MapSchemaType";
    /* SchemaType */

    public static final String MAP_TO_RELATIONSHIP_TYPE_GUID            = "8b9856b3-451e-45fc-afc7-fddefd81a73a";   /* from Area 5 */
    public static final String MAP_TO_RELATIONSHIP_TYPE_NAME            = "MapToElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    public static final String MAP_FROM_RELATIONSHIP_TYPE_GUID          = "6189d444-2da4-4cd7-9332-e48a1c340b44";   /* from Area 5 */
    public static final String MAP_FROM_RELATIONSHIP_TYPE_NAME          = "MapFromElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    /* For Bounded Schema Type (Deprecated) */
    public static final String BOUNDED_SCHEMA_TYPE_TYPE_GUID            = "77133161-37a9-43f5-aaa3-fd6d7ff92fdb";   /* from Area 5 */
    public static final String BOUNDED_SCHEMA_TYPE_TYPE_NAME            = "BoundedSchemaType";

    public static final String MAX_ELEMENTS_PROPERTY_NAME               = "maximumElements";      /* from BoundedSchemaType entity */

    public static final String BOUNDED_ELEMENT_RELATIONSHIP_TYPE_GUID   = "3e844049-e59b-45dd-8e62-cde1add59f9e";   /* from Area 5 */
    public static final String BOUNDED_ELEMENT_RELATIONSHIP_TYPE_NAME   = "BoundedSchemaElementType";
    /* End1 = BoundedSchemaType; End 2 = SchemaType */

    /* For Schema Attribute */
    public static final String SCHEMA_ATTRIBUTE_TYPE_GUID               = "1a5e159b-913a-43b1-95fe-04433b25fca9";   /* from Area 5 */
    public static final String SCHEMA_ATTRIBUTE_TYPE_NAME               = "SchemaAttribute";
    /* SchemaElement */

    public static final String ATTRIBUTE_NAME_PROPERTY_NAME             = "displayName";           /* from SchemaAttribute entity */
    public static final String OLD_ATTRIBUTE_NAME_PROPERTY_NAME         = "name";                  /* from SchemaAttribute entity */
    public static final String ELEMENT_POSITION_PROPERTY_NAME           = "position";              /* from SchemaAttribute entity */
    public static final String CARDINALITY_PROPERTY_NAME                = "cardinality";           /* from SchemaAttribute entity */
    public static final String MAX_CARDINALITY_PROPERTY_NAME            = "maxCardinality";        /* from SchemaAttribute entity */
    public static final String MIN_CARDINALITY_PROPERTY_NAME            = "minCardinality";        /* from SchemaAttribute entity */
    public static final String DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME     = "defaultValueOverride";  /* from SchemaAttribute entity */
    public static final String ALLOWS_DUPLICATES_PROPERTY_NAME          = "allowsDuplicateValues"; /* from SchemaAttribute entity */
    public static final String ORDERED_VALUES_PROPERTY_NAME             = "orderedValues";         /* from SchemaAttribute entity */
    public static final String NATIVE_CLASS_PROPERTY_NAME               = "nativeClass";           /* from SchemaAttribute entity */
    public static final String ALIASES_PROPERTY_NAME                    = "aliases";               /* from SchemaAttribute entity */
    public static final String SORT_ORDER_PROPERTY_NAME                 = "sortOrder";             /* from SchemaAttribute entity */
    public static final String MIN_LENGTH_PROPERTY_NAME                 = "minimumLength";         /* from SchemaAttribute entity */
    public static final String LENGTH_PROPERTY_NAME                     = "length";                /* from SchemaAttribute entity */
    public static final String SIGNIFICANT_DIGITS_PROPERTY_NAME         = "significantDigits";     /* from SchemaAttribute entity */
    public static final String IS_NULLABLE_PROPERTY_NAME                = "isNullable";            /* from SchemaAttribute entity */

    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID = "2d955049-e59b-45dd-8e62-cde1add59f9e";  /* from Area 5 */
    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME = "SchemaAttributeType";
    /* End1 = SchemaAttribute; End 2 = SchemaType */

    public static final String DATA_ITEM_SORT_ORDER_TYPE_GUID           = "aaa4df8f-1aca-4de8-9abd-1ef2aadba300";  /* from Area 5 */
    public static final String DATA_ITEM_SORT_ORDER_TYPE_NAME           = "DataItemSortOrder";

    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID  = "0ffb9d87-7074-45da-a9b0-ae0859611133";  /* from Area 5 */
    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME  = "NestedSchemaAttribute";
    /* End1 = SchemaAttribute; End 2 = SchemaAttribute */

    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID  = "e2bb76bb-774a-43ff-9045-3a05f663d5d9";  /* from Area 5 */
    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME  = "TypeEmbeddedAttribute";
    /* Linked to SchemaAttribute */
    public static final String TYPE_NAME_PROPERTY_NAME                  = "typeName";      /* from TypeEmbeddedAttribute classification */

    /* For Schema Link */
    public static final String SCHEMA_LINK_TYPE_GUID                    = "67e08705-2d2a-4df6-9239-1818161a41e0";      /* from Area 5 */
    public static final String SCHEMA_LINK_TYPE_NAME                    = "SchemaLinkElement";
    /* SchemaElement */

    public static final String LINK_NAME_PROPERTY_NAME                  = "linkName";             /* from SchemaAttribute entity */
    public static final String LINK_PROPERTIES_PROPERTY_NAME            = "linkProperties";       /* from SchemaAttribute entity */

    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_GUID      = "292125f7-5660-4533-a48a-478c5611922e";     /* from Area 5 */
    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_NAME      = "LinkedType";
    /* End1 = SchemaLinkElement; End 2 = SchemaType */

    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_GUID = "db9583c5-4690-41e5-a580-b4e30a0242d3";     /* from Area 5 */
    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_NAME = "SchemaLinkToType";
    /* End1 = SchemaAttribute; End 2 = SchemaLinkElement */

    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID = "e5d7025d-8b4f-43c7-bcae-1147d650b94b"; /* from Area 5 */
    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME = "DerivedSchemaTypeQueryTarget";
    /* End1 = SchemaElement; End 2 = SchemaElement (target) */

    public static final String QUERY_ID_PROPERTY_NAME                   = "queryId"; /* from DerivedSchemaTypeQueryTarget relationship */
    public static final String QUERY_PROPERTY_NAME                      = "query";   /* from DerivedSchemaTypeQueryTarget relationship */

    /* - Known Subtypes ------------------------------------------------------- */

    public static final String ARRAY_SCHEMA_TYPE_TYPE_GUID              = "ba8d29d2-a8a4-41f3-b29f-91ad924dd944";   /* from Area 5 */
    public static final String ARRAY_SCHEMA_TYPE_TYPE_NAME              = "ArraySchemaType";
    /* BoundedSchemaType */

    public static final String SET_SCHEMA_TYPE_TYPE_GUID                = "b2605d2d-10cd-443c-b3e8-abf15fb051f0";   /* from Area 5 */
    public static final String SET_SCHEMA_TYPE_TYPE_NAME                = "SetSchemaType";
    /* BoundedSchemaType */

    public static final String TABULAR_SCHEMA_TYPE_TYPE_GUID            = "248975ec-8019-4b8a-9caf-084c8b724233";   /* from Area 5 */
    public static final String TABULAR_SCHEMA_TYPE_TYPE_NAME            = "TabularSchemaType";
    /* ComplexSchemaType */

    public static final String TABULAR_COLUMN_TYPE_TYPE_GUID            = "a7392281-348d-48a4-bad7-f9742d7696fe";   /* from Area 5 */
    public static final String TABULAR_COLUMN_TYPE_TYPE_NAME            = "TabularColumnType";
    /* PrimitiveSchemaType */

    public static final String TABULAR_COLUMN_TYPE_GUID                 = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";   /* from Area 5 */
    public static final String TABULAR_COLUMN_TYPE_NAME                 = "TabularColumn";
    /* PrimitiveSchemaType */

    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_GUID           = "33da99cd-8d04-490c-9457-c58908da7794";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_NAME           = "DocumentSchemaType";
    /* ComplexSchemaType */

    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_GUID      = "b5cefb7e-b198-485f-a1d7-8e661012499b";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_NAME      = "DocumentSchemaAttribute";
    /* SchemaAttribute */

    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_GUID           = "42cfccbf-cc68-4980-8c31-0faf1ee002d3";   /* from Area 5 */
    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_NAME           = "SimpleDocumentType";
    /* PrimitiveSchemaType */

    public static final String STRUCT_DOCUMENT_TYPE_TYPE_GUID           = "f6245c25-8f73-45eb-8fb5-fa17a5f27649";   /* from Area 5 */
    public static final String STRUCT_DOCUMENT_TYPE_TYPE_NAME           = "StructDocumentType";
    /* StructSchemaType */

    public static final String MAP_DOCUMENT_TYPE_TYPE_GUID              = "b0f09598-ceb6-415b-befc-563ecadd5727";   /* from Area 5 */
    public static final String MAP_DOCUMENT_TYPE_TYPE_NAME              = "MapDocumentType";
    /* MapSchemaType */

    public static final String OBJECT_SCHEMA_TYPE_TYPE_GUID             = "6920fda1-7c07-47c7-84f1-9fb044ae153e";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_TYPE_TYPE_NAME             = "ObjectSchemaType";
    /* ComplexSchemaType */

    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_GUID        = "ccb408c0-582e-4a3a-a926-7082d53bb669";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_NAME        = "ObjectSchemaAttribute";
    /* SchemaAttribute */

    public static final String GRAPH_SCHEMA_TYPE_TYPE_GUID              = "983c5e72-801b-4e42-bc51-f109527f2317";   /* from Area 5 */
    public static final String GRAPH_SCHEMA_TYPE_TYPE_NAME              = "GraphSchemaType";
    /* ComplexSchemaType */

    public static final String GRAPH_VERTEX_TYPE_GUID                   = "1252ce12-540c-4724-ad70-f70940956de0";   /* from Area 5 */
    public static final String GRAPH_VERTEX_TYPE_NAME                   = "GraphVertex";
    /* SchemaAttribute */

    public static final String GRAPH_EDGE_TYPE_GUID                     = "d4104eb3-4f2d-4d83-aca7-e58dd8d5e0b1";   /* from Area 5 */
    public static final String GRAPH_EDGE_TYPE_NAME                     = "GraphEdge";
    /* SchemaAttribute */

    /* For Graph Edge/Vertex */
    public static final String GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_GUID   = "503b4221-71c8-4ba9-8f3d-6a035b27971c";   /* from Area 5 */
    public static final String GRAPH_EDGE_LINK_RELATIONSHIP_TYPE_NAME   = "GraphEdgeLink";
    /* End1 = GraphEdge; End 2 = GraphVertex */

    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID      = "f20f5f45-1afb-41c1-9a09-34d8812626a4";   /* from Area 5 */
    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME      = "RelationalDBSchemaType";
    /* ComplexSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_TYPE_GUID          = "1321bcc0-dc6a-48ed-9ca6-0c6f934b0b98";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_TYPE_NAME          = "RelationalTableType";
    /* TabularSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_GUID               = "ce7e72b8-396a-4013-8688-f9d973067425";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_NAME               = "RelationalTable";
    /* SchemaAttribute */

    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID = "4814bec8-482d-463d-8376-160b0358e139";
    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME = "CalculatedValue";
    /* Linked to SchemaType */
    public static final String FORMULA_PROPERTY_NAME                     = "formula";          /* from CalculatedValue classification */

    public static final String RELATIONAL_COLUMN_TYPE_GUID              = "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9";   /* from Area 5 */
    public static final String RELATIONAL_COLUMN_TYPE_NAME              = "RelationalColumn";
    /* TabularColumn */

    public static final String RELATIONAL_COLUMN_TYPE_TYPE_GUID         = "f0438d80-6eb9-4fac-bcc1-5efee5babcfc";   /* from Area 5 */
    public static final String RELATIONAL_COLUMN_TYPE_TYPE_NAME         = "RelationalColumnType";
    /* TabularColumnType */

    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_GUID     = "b239d832-50bd-471b-b17a-15a335fc7f40";
    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_NAME     = "PrimaryKey";
    /* Linked to RelationalColumn */
    public static final String PRIMARY_KEY_PATTERN_PROPERTY_NAME        = "keyPattern";    /* from PrimaryKey classification */
    public static final String PRIMARY_KEY_NAME_PROPERTY_NAME           = "name";          /* from PrimaryKey classification */

    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_GUID       = "3cd4e0e7-fdbf-47a6-ae88-d4b3205e0c07"; /* from Area 5 */
    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_NAME       = "ForeignKey";
    /* End1 = RelationalColumn; End 2 = RelationalColumn */
    public static final String FOREIGN_KEY_NAME_PROPERTY_NAME           = "name";          /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME    = "description";   /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME     = "confidence";    /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_STEWARD_PROPERTY_NAME        = "steward";       /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_SOURCE_PROPERTY_NAME         = "source";        /* from ForeignKey relationship */

    /* For Event Type */
    public static final String EVENT_TYPE_TYPE_GUID                     = "bead9aa4-214a-4596-8036-aa78395bbfb1";   /* from Area 5 */
    public static final String EVENT_TYPE_TYPE_NAME                     = "EventType";
    /* ComplexSchemaType */
    public static final String EVENT_SET_TYPE_GUID                      = "8bc88aba-d7e4-4334-957f-cfe8e8eadc32";   /* from Area 5 */
    public static final String EVENT_SET_TYPE_NAME                      = "EventSet";
    /* Collection */

    /* For API Schema Type */
    public static final String API_SCHEMA_TYPE_TYPE_GUID                = "b46cddb3-9864-4c5d-8a49-266b3fc95cb8";   /* from Area 5 */
    public static final String API_SCHEMA_TYPE_TYPE_NAME                = "APISchemaType";
    /* SchemaType */
    public static final String API_OPERATION_TYPE_GUID                  = "f1c0af19-2729-4fac-996e-a7badff3c21c";   /* from Area 5 */
    public static final String API_OPERATION_TYPE_NAME                  = "APIOperation";
    /* SchemaType */
    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_GUID    = "03737169-ceb5-45f0-84f0-21c5929945af"; /* from Area 5 */
    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_NAME    = "APIOperations";
    /* End1 = APISchemaType; End 2 = APIOperation */
    public static final String API_HEADER_RELATIONSHIP_TYPE_GUID        = "e8fb46d1-5f75-481b-aa66-f43ad44e2cc6"; /* from Area 5 */
    public static final String API_HEADER_RELATIONSHIP_TYPE_NAME        = "APIHeader";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_GUID       = "4ab3b466-31bd-48ea-8aa2-75623476f2e2"; /* from Area 5 */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_NAME       = "APIRequest";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_GUID      = "e8001de2-1bb1-442b-a66f-9addc3641eae"; /* from Area 5 */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_NAME      = "APIResponse";
    /* End1 = APIOperation; End 2 = SchemaType */

}
