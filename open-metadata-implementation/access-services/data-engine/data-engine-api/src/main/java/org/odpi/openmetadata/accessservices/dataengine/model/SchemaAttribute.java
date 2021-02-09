/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Schema attribute.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaAttribute implements Serializable {
    private static final long serialVersionUID = 1L;
    private String qualifiedName;
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
    private String GUID;

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets qualified name.
     *
     * @param qualifiedName the qualified name
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

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

    /**
     * Is allows duplicate values boolean.
     *
     * @return the boolean
     */
    public boolean isAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    /**
     * Is ordered values boolean.
     *
     * @return the boolean
     */
    public boolean isOrderedValues() {
        return orderedValues;
    }

    /**
     * Gets guid.
     *
     * @return the guid
     */
    public String getGUID() {
        return GUID;
    }

    /**
     * Sets guid.
     *
     * @param GUID the guid
     */
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    @Override
    public String toString() {
        return "SchemaAttribute{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", minCardinality='" + minCardinality + '\'' +
                ", maxCardinality='" + maxCardinality + '\'' +
                ", allowsDuplicateValues='" + allowsDuplicateValues + '\'' +
                ", orderedValues='" + orderedValues + '\'' +
                ", position='" + position + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", anchorGUID='" + anchorGUID + '\'' +
                ", GUID='" + GUID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaAttribute schemaAttribute = (SchemaAttribute) o;
        return Objects.equals(qualifiedName, schemaAttribute.qualifiedName) &&
                Objects.equals(displayName, schemaAttribute.displayName) &&
                Objects.equals(minCardinality, schemaAttribute.minCardinality) &&
                Objects.equals(maxCardinality, schemaAttribute.maxCardinality) &&
                Objects.equals(allowsDuplicateValues, schemaAttribute.allowsDuplicateValues) &&
                Objects.equals(orderedValues, schemaAttribute.orderedValues) &&
                Objects.equals(position, schemaAttribute.position) &&
                Objects.equals(defaultValueOverride, schemaAttribute.defaultValueOverride) &&
                Objects.equals(dataType, schemaAttribute.dataType) &&
                Objects.equals(defaultValue, schemaAttribute.defaultValue) &&
                Objects.equals(anchorGUID, schemaAttribute.anchorGUID) &&
                Objects.equals(GUID, schemaAttribute.GUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, displayName, minCardinality, maxCardinality, allowsDuplicateValues,
                orderedValues, position, defaultValueOverride, dataType, defaultValue, anchorGUID, GUID);
    }
}
