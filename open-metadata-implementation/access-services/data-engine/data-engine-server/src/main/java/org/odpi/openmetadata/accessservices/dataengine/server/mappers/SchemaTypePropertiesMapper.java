/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.mappers;

/**
 * SchemaTypePropertiesMapper provides property name mapping for SchemaTypes and their relationships.
 */
public class SchemaTypePropertiesMapper {
    public static final String LINEAGE_MAPPINGS_TYPE_GUID = "a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff";
    public static final String LINEAGE_MAPPINGS_TYPE_NAME = "LineageMapping";

    public static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    public static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    public static final String GUID_PROPERTY_NAME = "guid";

    public static final String MIN_CARDINALITY = "minCardinality";
    public static final String MAX_CARDINALITY = "maxCardinality";
    public static final String ALLOWS_DUPLICATES = "allowsDuplicateValues";
    public static final String ORDERED_VALUES = "orderedValues";
    public static final String TYPE_EMBEDDED_ATTRIBUTE_NAME = "TypeEmbeddedAttribute";
    public static final String DATA_TYPE = "dataType";

    private SchemaTypePropertiesMapper() {
    }
}
