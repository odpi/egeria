/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.mappers;

/**
 * SchemaElementMapper provides property name mapping for SchemaElements.
 */
public class SchemaElementMapper
{
    public static final String ASSET_TO_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME  = "AssetSchemaType";      /* from Area 5 */

    /* Common */
    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */

    /* For Schema Type */
    public static final String SCHEMA_TYPE_TYPE_NAME                    = "SchemaType";           /* from Area 5 */
    public static final String DISPLAY_NAME_PROPERTY_NAME               = "displayName";          /* from SchemaType entity */
    public static final String VERSION_NUMBER_PROPERTY_NAME             = "description";          /* from SchemaType entity */
    public static final String AUTHOR_PROPERTY_NAME                     = "author";               /* from SchemaType entity */
    public static final String USAGE_PROPERTY_NAME                      = "usage";                /* from SchemaType entity */
    public static final String ENCODING_STANDARD_PROPERTY_NAME          = "encodingStandard";     /* from SchemaType entity */

    /* For Complex Schema Type (eg Struct) */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_NAME            = "ComplexSchemaType";    /* from Area 5 */
    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "AttributeForSchema";   /* from Area 5 */

    /* For Primitive Schema Type */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_NAME          = "PrimitiveSchemaType";  /* from Area 5 */
    public static final String DATA_TYPE_PROPERTY_NAME                  = "dataType";             /* from PrimitiveSchemaType entity */
    public static final String DEFAULT_VALUE_PROPERTY_NAME              = "defaultValue";         /* from PrimitiveSchemaType classification */

    /* For Map Schema Type */
    public static final String MAP_SCHEMA_TYPE_TYPE_NAME                = "MapSchemaType";        /* from Area 5 */
    public static final String MAP_TO_RELATIONSHIP_TYPE_NAME            = "MapToElementType";     /* from Area 5 */
    public static final String MAP_FROM_RELATIONSHIP_TYPE_NAME          = "MapFromElementType";   /* from Area 5 */

    /* For Bounded Schema Type */
    public static final String BOUNDED_SCHEMA_TYPE_TYPE_NAME            = "BoundedSchemaType";    /* from Area 5 */
    public static final String BOUNDED_ELEMENT_RELATIONSHIP_TYPE_NAME   = "BoundedSchemaElementType"; /* from Area 5 */

    /* For Schema Attribute */
    public static final String SCHEMA_ATTRIBUTE_TYPE_NAME               = "SchemaAttribute";      /* from Area 5 */
    public static final String NAME_PROPERTY_NAME                       = "name";                 /* from SchemaAttribute entity */
    public static final String POSITION_PROPERTY_NAME                   = "position";             /* from SchemaAttribute entity */
    public static final String CARDINALITY_PROPERTY_NAME                = "cardinality";          /* from SchemaAttribute entity */
    public static final String DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME     = "defaultValueOverride"; /* from SchemaAttribute entity */
    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME = "SchemaAttributeType";  /* from Area 5 */
    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_NAME = "SchemaLinkToType";     /* from Area 5 */

    /* For Derived Schema Attribute */
    public static final String SCHEMA_QUERY_IMPL_RELATIONSHIP_TYPE_NAME = "SchemaQueryImplementation"; /* from Area 5 */

    public static final String QUERY_PROPERTY_NAME                      = "query";                /* from SchemaQueryImplementation relationship */
    public static final String FORMULA_PROPERTY_NAME                    = "formula";              /* from DerivedSchemaAttribute entity */

    /* For Schema Link */
    public static final String LINK_NAME_PROPERTY_NAME                  = "linkName";              /* from DerivedSchemaAttribute entity */
    public static final String LINK_PROPERTIES_PROPERTY_NAME            = "linkProperties";        /* from DerivedSchemaAttribute entity */
    public static final String LINKED_TYPE_RELATIONSHIP_TYPE_NAME       = "LinkedType";            /* from Area 5 */


}
