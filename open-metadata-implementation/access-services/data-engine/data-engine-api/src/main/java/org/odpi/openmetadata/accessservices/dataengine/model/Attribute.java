/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Attribute.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute extends Referenceable {
    private String displayName;
    private String description;
    private boolean isDeprecated;
    private int position;
    private int minCardinality;
    private int maxCardinality;
    private boolean allowsDuplicateValues;
    private boolean orderedValues;
    private String defaultValueOverride;
    private DataItemSortOrder sortOrder;
    private int minimumLength;
    private int length;
    private int precision;
    private boolean isNullable;
    private String nativeClass;
    private List<String> aliases;
    private String dataType;
    private String defaultValue;
    private String fixedValue;
    private String externalTypeGUID;
    private String validValuesSetGUID;

    /**
     * Return the simple name of the schema element.
     *
     * @return string name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set up the simple name of the schema element.
     *
     * @param name String display name
     */
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    /**
     * Returns the stored description property for the schema element.
     *
     * @return string description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set up the stored description property for the schema element.
     *
     * @param description string description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns true if the schema element deprecated
     *
     * @return boolean flag
     */
    public boolean isDeprecated() {
        return isDeprecated;
    }

    /**
     * Set whether the schema element deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }

    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema - 0 means first
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set up the position of this schema attribute in its parent schema.
     *
     * @param position int position in schema - 0 means first
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Return this minimum number of instances allowed for this attribute.
     *
     * @return int
     */
    public int getMinCardinality() {
        return minCardinality;
    }

    /**
     * Set up the minimum number of instances allowed for this attribute.
     *
     * @param minCardinality int
     */
    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    /**
     * Return the maximum number of instances allowed for this attribute.
     *
     * @return int (-1 means infinite)
     */
    public int getMaxCardinality() {
        return maxCardinality;
    }

    /**
     * Set up the maximum number of instances allowed for this attribute.
     *
     * @param maxCardinality int (-1 means infinite)
     */
    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public boolean getAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    /**
     * Set up whether the same value can be used by more than one instance of this attribute.
     *
     * @param allowsDuplicateValues boolean flag
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues) {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }

    /**
     * Return whether the attribute instances are arranged in an order.
     *
     * @return boolean flag
     */
    public boolean getOrderedValues() {
        return orderedValues;
    }

    /**
     * Set up whether the attribute instances are arranged in an order.
     *
     * @param orderedValues boolean flag
     */
    public void setOrderedValues(boolean orderedValues) {
        this.orderedValues = orderedValues;
    }

    /**
     * Return the order that the attribute instances are arranged in - if any.
     *
     * @return DataItemSortOrder enum
     */
    public DataItemSortOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * Set up the order that the attribute instances are arranged in - if any.
     *
     * @param sortOrder DataItemSortOrder enum
     */
    public void setSortOrder(DataItemSortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Return the minimum length of the data.
     *
     * @return int
     */
    public int getMinimumLength() {
        return minimumLength;
    }

    /**
     * Set up the minimum length of the data.
     *
     * @param minimumLength int
     */
    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    /**
     * Return the length of the data field.
     *
     * @return int
     */
    public int getLength() {
        return length;
    }

    /**
     * Set up the length of the data field.
     *
     * @param length int
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Return the number of significant digits to the right of decimal point.
     *
     * @return int
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Set up the number of significant digits to the right of decimal point.
     *
     * @param precision int
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Return whether the field is nullable or not.
     *
     * @return boolean
     */
    public boolean isNullable() {
        return isNullable;
    }

    /**
     * Set up whether the field is nullable or not.
     *
     * @param nullable boolean
     */
    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return defaultValueOverride; }

    /**
     * Set up any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @param defaultValueOverride String default value override
     */
    public void setDefaultValueOverride(String defaultValueOverride)
    {
        this.defaultValueOverride = defaultValueOverride;
    }

    /**
     * Return the name of the Java class to use to represent this type.
     *
     * @return fully qualified Java class name
     */
    public String getNativeClass() {
        return nativeClass;
    }

    /**
     * Set up the name of the Java class to use to represent this type.
     *
     * @param nativeClass fully qualified Java class name
     */
    public void setNativeClass(String nativeClass) {
        this.nativeClass = nativeClass;
    }

    /**
     * Return a list of alternative names for the attribute.
     *
     * @return list of names
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set up a list of alternative names for the attribute.
     *
     * @param aliases list of names
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return string data type name
     */
    public String getDataType() { return dataType; }

    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return string containing default value
     */
    public String getDefaultValue() { return defaultValue; }

    /**
     * Set up the default value for the element.  Null means no default value set up.
     *
     * @param defaultValue String containing default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Return a fixed literal value - an alternative to default value.
     *
     * @return string value
     */
    public String getFixedValue()
    {
        return fixedValue;
    }

    /**
     * If the column contains a fixed literal value, set this value here - an alternative to default value.
     *
     * @param fixedValue string
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }

    /**
     * Return the unique identifier of this column's type.
     *
     * @return unique identifier (guid) of the external schema type
     */
    public String getExternalTypeGUID()
    {
        return externalTypeGUID;
    }

    /**
     * If the type of this column is represented by an external (standard type) put its value here.  No need to set
     * dataType, FixedType or defaultType
     *
     * @param externalTypeGUID unique identifier (guid) of the external schema type
     */
    public void setExternalTypeGUID(String externalTypeGUID)
    {
        this.externalTypeGUID = externalTypeGUID;
    }

    /**
     * Return the set of valid values for this column.
     *
     * @return unique identifier (guid) of the valid values set
     */
    public String getValidValuesSetGUID()
    {
        return validValuesSetGUID;
    }

    /**
     * If the type is controlled by a fixed set of values, set up the unique identifier of the valid values set
     * that lists the valid values.
     *
     * @param validValuesSetGUID unique identifier (guid) of the valid values set
     */
    public void setValidValuesSetGUID(String validValuesSetGUID)
    {
        this.validValuesSetGUID = validValuesSetGUID;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "displayName='" + displayName + '\'' +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                ", allowsDuplicateValues=" + allowsDuplicateValues +
                ", orderedValues=" + orderedValues +
                ", position=" + position +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", nativeClass='" + nativeClass + '\'' +
                ", aliases=" + aliases +
                ", sortOrder=" + sortOrder +
                ", description='" + description + '\'' +
                ", precision='" + precision + '\'' +
                ", length=" + length +
                ", minimumLength=" + minimumLength +
                ", isNullable=" + isNullable +
                ", isDeprecated=" + isDeprecated +
                ", fixedValue='" + fixedValue + '\'' +
                ", externalTypeGUID='" + externalTypeGUID + '\'' +
                ", validValuesSetGUID='" + validValuesSetGUID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Attribute attribute = (Attribute) o;
        return minCardinality == attribute.minCardinality &&
                maxCardinality == attribute.maxCardinality &&
                allowsDuplicateValues == attribute.allowsDuplicateValues &&
                orderedValues == attribute.orderedValues &&
                position == attribute.position &&
                length == attribute.length &&
                minimumLength == attribute.minimumLength &&
                isNullable == attribute.isNullable &&
                isDeprecated == attribute.isDeprecated &&
                Objects.equals(displayName, attribute.displayName) &&
                Objects.equals(defaultValueOverride, attribute.defaultValueOverride) &&
                Objects.equals(dataType, attribute.dataType) &&
                Objects.equals(defaultValue, attribute.defaultValue) &&
                Objects.equals(nativeClass, attribute.nativeClass) &&
                Objects.equals(aliases, attribute.aliases) &&
                sortOrder == attribute.sortOrder &&
                Objects.equals(description, attribute.description) &&
                Objects.equals(precision, attribute.precision) &&
                Objects.equals(fixedValue, attribute.fixedValue) &&
                Objects.equals(externalTypeGUID, attribute.externalTypeGUID) &&
                Objects.equals(validValuesSetGUID, attribute.validValuesSetGUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), displayName, minCardinality, maxCardinality, allowsDuplicateValues, orderedValues, position,
                defaultValueOverride, dataType, defaultValue, nativeClass, aliases, sortOrder, description, precision, length,
                minimumLength, isNullable, isDeprecated, fixedValue, externalTypeGUID, validValuesSetGUID);
    }
}
