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

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    public int getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    public boolean getAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    public void setAllowsDuplicateValues(boolean allowsDuplicateValues) {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }

    public boolean getOrderedValues() {
        return orderedValues;
    }

    public void setOrderedValues(boolean orderedValues) {
        this.orderedValues = orderedValues;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getAnchorGUID() {
        return anchorGUID;
    }

    public void setAnchorGUID(String anchorGUID) {
        this.anchorGUID = anchorGUID;
    }

    public boolean isAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    public boolean isOrderedValues() {
        return orderedValues;
    }

    public String getGUID() {
        return GUID;
    }

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
