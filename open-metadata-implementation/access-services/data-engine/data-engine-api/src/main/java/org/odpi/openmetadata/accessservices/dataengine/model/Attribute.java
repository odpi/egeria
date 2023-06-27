/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Attribute is a java bean used to create schema attributes associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Attribute extends Referenceable {

    /**
     * The simple name of the schema element
     * -- GETTER --
     * Return the simple name of the schema element.
     * @return displayName string name
     * -- SETTER --
     * Set up the simple name of the schema element.
     * @param displayName String display name
     */
    private String displayName;

    /**
     * The stored description property for the schema element
     * -- GETTER --
     * Returns the stored description property for the schema element.
     * @return description string description
     * -- SETTER --
     * Set up the stored description property for the schema element.
     * @param description string description
     */
    private String description;

    /**
     * Indicates if the schema element is deprecated
     * -- GETTER --
     * Returns true if the schema element deprecated
     * @return isDeprecated boolean flag
     * -- SETTER --
     * Set whether the schema element deprecated or not.  Default is false.
     * @param isDeprecated boolean flag
     */
    private boolean isDeprecated;

    /**
     * The position of this schema attribute in its parent schema
     * -- GETTER --
     * Return the position of this schema attribute in its parent schema.
     * @return position int position in schema - 0 means first
     * -- SETTER --
     * Set up the position of this schema attribute in its parent schema.
     * @param position int position in schema - 0 means first
     */
    private int position;

    /**
     * This minimum number of instances allowed for this attribute
     * -- GETTER --
     * Return this minimum number of instances allowed for this attribute.
     * @return minCardinality int
     * -- SETTER --
     * Set up the minimum number of instances allowed for this attribute.
     * @param minCardinality int
     */
    private int minCardinality;

    /**
     * The maximum number of instances allowed for this attribute
     * -- GETTER --
     * Return the maximum number of instances allowed for this attribute.
     * @return maxCardinality int (-1 means infinite)
     * -- SETTER --
     * Set up the maximum number of instances allowed for this attribute.
     * @param maxCardinality int (-1 means infinite)
     */
    private int maxCardinality;

    /**
     * Indicates whether the same value can be used by more than one instance of this attribute
     * -- GETTER --
     * Return whether the same value can be used by more than one instance of this attribute.
     * @return allowsDuplicateValues boolean flag
     * -- SETTER --
     * Set up whether the same value can be used by more than one instance of this attribute.
     * @param allowsDuplicateValues boolean flag
     */
    private boolean allowsDuplicateValues;

    /**
     * Indicates whether the attribute instances are arranged in an order
     * -- GETTER --
     * Return whether the attribute instances are arranged in an order.
     * @return orderedValues boolean flag
     * -- SETTER --
     * Set up whether the attribute instances are arranged in an order.
     * @param orderedValues boolean flag
     */
    private boolean orderedValues;

    /**
     * Default value override
     * -- GETTER --
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     * @return String default value override
     * -- SETTER --
     * Set up any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     * @param defaultValueOverride String default value override
     */
    private String defaultValueOverride;

    /**
     * The order that the attribute instances are arranged in
     * -- GETTER --
     * Return the order that the attribute instances are arranged in - if any.
     * @return DataItemSortOrder enum
     * -- SETTER --
     * Set up the order that the attribute instances are arranged in - if any.
     * @param sortOrder DataItemSortOrder enum
     */
    private DataItemSortOrder sortOrder;

    /**
     * The minimum length of the data
     * -- GETTER --
     * Return the minimum length of the data.
     * @return int
     * -- SETTER --
     * Set up the minimum length of the data.
     * @param minimumLength int
     */
    private int minimumLength;

    /**
     * The length of the data field
     * -- GETTER --
     * Return the length of the data field.
     * @return int
     * -- SETTER --
     * Set up the length of the data field.
     * @param length int
     */
    private int length;

    /**
     * The number of significant digits to the right of decimal point
     * -- GETTER --
     * Return the number of significant digits to the right of decimal point.
     * @return int
     * -- SETTER --
     * Set up the number of significant digits to the right of decimal point.
     * @param precision int
     */
    private int precision;

    /**
     * Indicates whether the field is nullable or not
     * -- GETTER --
     * Return whether the field is nullable or not.
     * @return boolean
     * -- SETTER --
     * Set up whether the field is nullable or not.
     * @param isNullable boolean
     */
    private boolean isNullable;

    /**
     * The name of the Java class to use to represent this type
     * -- GETTER --
     * Return the name of the Java class to use to represent this type.
     * @return fully qualified Java class name
     * -- SETTER --
     * Set up the name of the Java class to use to represent this type.
     * @param nativeClass fully qualified Java class name
     */
    private String nativeClass;

    /**
     * A list of alternative names for the attribute
     * -- GETTER --
     * Return a list of alternative names for the attribute.
     * @return list of names
     * -- SETTER --
     * Set up a list of alternative names for the attribute.
     * @param aliases list of names
     */
    private List<String> aliases;

    /**
     * The data type for this element
     * -- GETTER --
     * Return the data type for this element.  Null means unknown data type.
     * @return string data type name
     * -- SETTER --
     * Set up the data type for this element.  Null means unknown data type.
     * @param dataType data type name
     */
    private String dataType;

    /**
     * The default value for the element
     * -- GETTER --
     * Return the default value for the element.  Null means no default value set up.
     * @return string containing default value
     * -- SETTER --
     * Set up the default value for the element.  Null means no default value set up.
     * @param defaultValue String containing default value
     */
    private String defaultValue;

    /**
     * A fixed literal value - an alternative to default value
     * -- GETTER --
     * Return a fixed literal value - an alternative to default value.
     * @return string value
     * -- SETTER --
     * If the column contains a fixed literal value, set this value here - an alternative to default value.
     * @param fixedValue string
     */
    private String fixedValue;

    /**
     * The unique identifier of this column's type
     * -- GETTER --
     * Return the unique identifier of this column's type.
     * @return unique identifier (guid) of the external schema type
     * -- SETTER --
     * If the type of this column is represented by an external (standard type) put its value here.  No need to set
     * dataType, FixedType or defaultType
     * @param externalTypeGUID unique identifier (guid) of the external schema type
     */
    private String externalTypeGUID;

    /**
     * The set of valid values for this column
     * -- GETTER --
     * Return the set of valid values for this column.
     * @return unique identifier (guid) of the valid values set
     * -- SETTER --
     * If the type is controlled by a fixed set of values, set up the unique identifier of the valid values set
     * that lists the valid values.
     * @param validValuesSetGUID unique identifier (guid) of the valid values set
     */
    private String validValuesSetGUID;

    /**
     * The name of the type for this schema element
     * -- GETTER --
     * Return name of the type of this schema element
     * @return name of the type
     * -- SETTER --
     * Set the name of the type for this schema element
     * @param typeName the name of the type of data
     */
    private String typeName;

    /**
     * Unique identifier of the type for this schema element
     * -- GETTER --
     * Return identifier of the type of this schema element
     * @return identifier of the type for this schema element
     * -- SETTER --
     * Set the value for the type identifier
     * @param typeGuid unique identifier (guid) of the schema element
     */
    private String typeGuid;
}
