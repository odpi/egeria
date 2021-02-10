/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.mappers;

/**
 * SchemaTypePropertiesMapper provides property name mapping for SchemaTypes and their relationships.
 */
public class SchemaTypePropertiesMapper {
    public static final String LINEAGE_MAPPINGS_TYPE_NAME = "LineageMapping";

    public static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    public static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    public static final String GUID_PROPERTY_NAME = "guid";

    public static final String TYPE_EMBEDDED_ATTRIBUTE_NAME = "TypeEmbeddedAttribute";
    public static final String DATA_TYPE = "dataType";

    public static final String SCHEMA_TYPE_TYPE_NAME = "SchemaType";
    public static final String SCHEMA_ATTRIBUTE_TYPE_NAME = "SchemaAttribute";
    public static final String TABULAR_COLUMN_TYPE_NAME = "TabularColumn";

    public static final String TABULAR_SCHEMA_TYPE_TYPE_NAME = "TabularSchemaType";
    public static final String TABULAR_SCHEMA_TYPE_TYPE_GUID = "248975ec-8019-4b8a-9caf-084c8b724233";

    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "AttributeForSchema";

    private SchemaTypePropertiesMapper() {
    }
}
