/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.io.Serializable;
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
    private int minCardinality;
    private int maxCardinality;
    private boolean allowsDuplicateValues;
    private boolean orderedValues;
    private int position;
    private String defaultValueOverride;
    private String dataType;
    private String defaultValue;
    private String anchorGUID;
    private String nativeClass;
    private List<String> aliases;
    private DataItemSortOrder sortOrder;
    private String description;
    private int significantDigits;
    private String precision;
    private int length;
    private int minimumLength;
    private boolean isNullable;
    private boolean isDeprecated;
    private String fixedValue;
    private String externalTypeGUID;
    private String validValuesSetGUID;


    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets min cardinality.
     *
     * @return the min cardinality
     */
    public int getMinCardinality() {
        return minCardinality;
    }

    /**
     * Sets min cardinality.
     *
     * @param minCardinality the min cardinality
     */
    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    /**
     * Gets max cardinality.
     *
     * @return the max cardinality
     */
    public int getMaxCardinality() {
        return maxCardinality;
    }

    /**
     * Sets max cardinality.
     *
     * @param maxCardinality the max cardinality
     */
    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    /**
     * Gets allows duplicate values.
     *
     * @return the allows duplicate values
     */
    public boolean getAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    /**
     * Sets allows duplicate values.
     *
     * @param allowsDuplicateValues the allows duplicate values
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues) {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }

    /**
     * Gets ordered values.
     *
     * @return the ordered values
     */
    public boolean getOrderedValues() {
        return orderedValues;
    }

    /**
     * Sets ordered values.
     *
     * @param orderedValues the ordered values
     */
    public void setOrderedValues(boolean orderedValues) {
        this.orderedValues = orderedValues;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets default value override.
     *
     * @return the default value override
     */
    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    /**
     * Sets default value override.
     *
     * @param defaultValueOverride the default value override
     */
    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    /**
     * Gets data type.
     *
     * @return the data type
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets data type.
     *
     * @param dataType the data type
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * Gets default value.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default value.
     *
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets anchor guid.
     *
     * @return the anchor guid
     */
    public String getAnchorGUID() {
        return anchorGUID;
    }

    /**
     * Sets anchor guid.
     *
     * @param anchorGUID the anchor guid
     */
    public void setAnchorGUID(String anchorGUID) {
        this.anchorGUID = anchorGUID;
    }

    public boolean isAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    public boolean isOrderedValues() {
        return orderedValues;
    }

    public String getNativeClass() {
        return nativeClass;
    }

    public void setNativeClass(String nativeClass) {
        this.nativeClass = nativeClass;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public DataItemSortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(DataItemSortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSignificantDigits() {
        return significantDigits;
    }

    public void setSignificantDigits(int significantDigits) {
        this.significantDigits = significantDigits;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getExternalTypeGUID() {
        return externalTypeGUID;
    }

    public void setExternalTypeGUID(String externalTypeGUID) {
        this.externalTypeGUID = externalTypeGUID;
    }

    public String getValidValuesSetGUID() {
        return validValuesSetGUID;
    }

    public void setValidValuesSetGUID(String validValuesSetGUID) {
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
                ", anchorGUID='" + anchorGUID + '\'' +
                ", nativeClass='" + nativeClass + '\'' +
                ", aliases=" + aliases +
                ", sortOrder=" + sortOrder +
                ", description='" + description + '\'' +
                ", significantDigits=" + significantDigits +
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
                significantDigits == attribute.significantDigits &&
                length == attribute.length &&
                minimumLength == attribute.minimumLength &&
                isNullable == attribute.isNullable &&
                isDeprecated == attribute.isDeprecated &&
                Objects.equals(displayName, attribute.displayName) &&
                Objects.equals(defaultValueOverride, attribute.defaultValueOverride) &&
                Objects.equals(dataType, attribute.dataType) &&
                Objects.equals(defaultValue, attribute.defaultValue) &&
                Objects.equals(anchorGUID, attribute.anchorGUID) &&
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
                defaultValueOverride, dataType, defaultValue, anchorGUID, nativeClass, aliases, sortOrder, description, significantDigits, precision, length, minimumLength, isNullable, isDeprecated, fixedValue, externalTypeGUID, validValuesSetGUID);
    }
}
