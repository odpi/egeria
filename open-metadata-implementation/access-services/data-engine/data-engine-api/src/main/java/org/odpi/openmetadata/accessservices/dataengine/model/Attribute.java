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
 * The type Attribute.
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
     * -- GETTER --
     * Return the simple name of the schema element.
     * @return string name
     * -- SETTER --
     * Set up the simple name of the schema element.
     * @param name String display name
     */
    private String displayName;

    /**
     * -- GETTER --
     * Returns the stored description property for the schema element.
     * @return string description
     * -- SETTER --
     * Set up the stored description property for the schema element.
     * @param description string description
     */
    private String description;

    /**
     * -- GETTER --
     * Returns true if the schema element deprecated
     * @return boolean flag
     * -- SETTER --
     * Set whether the schema element deprecated or not.  Default is false.
     * @param deprecated boolean flag
     */
    private boolean isDeprecated;

    /**
     * -- GETTER --
     * Return the position of this schema attribute in its parent schema.
     * @return int position in schema - 0 means first
     * -- SETTER --
     * Set up the position of this schema attribute in its parent schema.
     * @param position int position in schema - 0 means first
     */
    private int position;

    /**
     * -- GETTER --
     * Return this minimum number of instances allowed for this attribute.
     * @return int
     * -- SETTER --
     * Set up the minimum number of instances allowed for this attribute.
     * @param minCardinality int
     */
    private int minCardinality;

    /**
     * -- GETTER --
     * Return the maximum number of instances allowed for this attribute.
     * @return int (-1 means infinite)
     *
     * -- SETTER --
     * Set up the maximum number of instances allowed for this attribute.
     * @param maxCardinality int (-1 means infinite)
     */
    private int maxCardinality;

    /**
     * -- GETTER --
     * Return whether the same value can be used by more than one instance of this attribute.
     * @return boolean flag
     * -- SETTER --
     * Set up whether the same value can be used by more than one instance of this attribute.
     * @param allowsDuplicateValues boolean flag
     */
    private boolean allowsDuplicateValues;

    /**
     * -- GETTER --
     * Return whether the attribute instances are arranged in an order.
     * @return boolean flag
     * -- SETTER --
     * Set up whether the attribute instances are arranged in an order.
     * @param orderedValues boolean flag
     */
    private boolean orderedValues;

    /**
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
     * -- GETTER --
     * Return the order that the attribute instances are arranged in - if any.
     * @return DataItemSortOrder enum
     * -- SETTER --
     * Set up the order that the attribute instances are arranged in - if any.
     * @param sortOrder DataItemSortOrder enum
     */
    private DataItemSortOrder sortOrder;

    /**
     * -- GETTER --
     * Return the minimum length of the data.
     * @return int
     * -- SETTER --
     * Set up the minimum length of the data.
     * @param minimumLength int
     */
    private int minimumLength;

    /**
     * -- GETTER --
     * Return the length of the data field.
     * @return int
     * -- SETTER --
     * Set up the length of the data field.
     * @param length int
     */
    private int length;

    /**
     * -- GETTER --
     * Return the number of significant digits to the right of decimal point.
     * @return int
     * -- SETTER --
     * Set up the number of significant digits to the right of decimal point.
     * @param precision int
     */
    private int precision;

    /**
     * -- GETTER --
     * Return whether the field is nullable or not.
     * @return boolean
     * -- SETTER --
     * Set up whether the field is nullable or not.
     * @param nullable boolean
     */
    private boolean isNullable;

    /**
     * -- GETTER --
     * Return the name of the Java class to use to represent this type.
     * @return fully qualified Java class name
     * -- SETTER --
     * Set up the name of the Java class to use to represent this type.
     * @param nativeClass fully qualified Java class name
     */
    private String nativeClass;

    /**
     * -- GETTER --
     * Return a list of alternative names for the attribute.
     * @return list of names
     * -- SETTER --
     * Set up a list of alternative names for the attribute.
     * @param aliases list of names
     */
    private List<String> aliases;

    /**
     * -- GETTER --
     * Return the data type for this element.  Null means unknown data type.
     * @return string data type name
     * -- SETTER --
     * Set up the data type for this element.  Null means unknown data type.
     * @param dataType data type name
     */
    private String dataType;

    /**
     * -- GETTER --
     * Return the default value for the element.  Null means no default value set up.
     * @return string containing default value
     * -- SETTER --
     * Set up the default value for the element.  Null means no default value set up.
     * @param defaultValue String containing default value
     */
    private String defaultValue;

    /**
     * -- GETTER --
     * Return a fixed literal value - an alternative to default value.
     * @return string value
     * -- SETTER --
     * If the column contains a fixed literal value, set this value here - an alternative to default value.
     * @param fixedValue string
     */
    private String fixedValue;

    /**
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
     * -- GETTER --
     * Return the set of valid values for this column.
     * @return unique identifier (guid) of the valid values set
     * -- SETTER --
     * If the type is controlled by a fixed set of values, set up the unique identifier of the valid values set
     * that lists the valid values.
     * @param validValuesSetGUID unique identifier (guid) of the valid values set
     */
    private String validValuesSetGUID;

}
