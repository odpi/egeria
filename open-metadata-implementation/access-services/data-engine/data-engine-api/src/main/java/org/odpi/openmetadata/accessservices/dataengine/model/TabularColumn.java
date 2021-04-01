/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type TabularColumn
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TabularColumn implements Serializable {

    //Referenceable
    protected String qualifiedName;
    protected Map<String,String> additionalProperties;

    //SchemaElement
    protected String anchorGuid;
    protected String displayName;
    protected String description;

    //SchemaAttribute
    protected int position;
    protected int minCardinality;
    protected int maxCardinality;
    protected boolean allowsDuplicateValues;
    protected boolean orderedValues;
    protected String defaultValueOverride;
    protected String nativeClass;
    protected String name;
    protected String[] aliases;
    protected DataItemSortOrder dataItemSortOrder;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getAnchorGuid() {
        return anchorGuid;
    }

    public void setAnchorGuid(String anchorGuid) {
        this.anchorGuid = anchorGuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public boolean isAllowsDuplicateValues() {
        return allowsDuplicateValues;
    }

    public void setAllowsDuplicateValues(boolean allowsDuplicateValues) {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }

    public boolean isOrderedValues() {
        return orderedValues;
    }

    public void setOrderedValues(boolean orderedValues) {
        this.orderedValues = orderedValues;
    }

    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    public String getNativeClass() {
        return nativeClass;
    }

    public void setNativeClass(String nativeClass) {
        this.nativeClass = nativeClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public DataItemSortOrder getDataItemSortOrder() {
        return dataItemSortOrder;
    }

    public void setDataItemSortOrder(DataItemSortOrder dataItemSortOrder) {
        this.dataItemSortOrder = dataItemSortOrder;
    }

    @Override
    public String toString() {
        return "SchemaType{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties='" + additionalProperties + '\'' +
                ", anchorGuid='" + anchorGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", position='" + position + '\'' +
                ", minCardinality='" + minCardinality + '\'' +
                ", maxCardinality='" + maxCardinality + '\'' +
                ", allowsDuplicateValues='" + allowsDuplicateValues + '\'' +
                ", orderedValues='" + orderedValues + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", nativeClass='" + nativeClass + '\'' +
                ", name='" + name + '\'' +
                ", aliases='" + Arrays.toString(aliases) + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabularColumn that = (TabularColumn) o;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(anchorGuid, that.anchorGuid) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(position, that.position) &&
                Objects.equals(minCardinality, that.minCardinality) &&
                Objects.equals(maxCardinality, that.maxCardinality) &&
                Objects.equals(allowsDuplicateValues, that.allowsDuplicateValues) &&
                Objects.equals(orderedValues, that.orderedValues) &&
                Objects.equals(defaultValueOverride, that.defaultValueOverride) &&
                Objects.equals(nativeClass, that.nativeClass) &&
                Objects.equals(name, that.name) &&
                Arrays.equals(aliases, that.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, additionalProperties, anchorGuid, displayName, description, position,
                minCardinality, maxCardinality, allowsDuplicateValues, orderedValues, defaultValueOverride, nativeClass,
                name, aliases);
    }

}
