/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type TabularSchemaType
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TabularSchemaType implements Serializable {

    //Referenceable
    protected String qualifiedName;
    protected Map<String,String> additionalProperties;

    //SchemaElement
    protected String anchorGuid;
    protected String displayName;
    protected String description;

    //SchemaType
    protected String namespace;
    protected String versionNumber;
    protected String author;
    protected String usage;
    protected String encodingStandard;

    protected List<TabularColumn> tabularColumns;


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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getEncodingStandard() {
        return encodingStandard;
    }

    public void setEncodingStandard(String encodingStandard) {
        this.encodingStandard = encodingStandard;
    }

    public List<TabularColumn> getTabularColumns() {
        return tabularColumns;
    }

    public void setTabularColumns(List<TabularColumn> tabularColumns) {
        this.tabularColumns = tabularColumns;
    }

    @Override
    public String toString() {
        return "SchemaType{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties='" + additionalProperties + '\'' +
                ", anchorGuid='" + anchorGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", namespace='" + namespace + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", tabularColumns='" + tabularColumns + '\'' +
        '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabularSchemaType that = (TabularSchemaType) o;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(anchorGuid, that.anchorGuid) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(versionNumber, that.versionNumber) &&
                Objects.equals(author, that.author) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(encodingStandard, that.encodingStandard) &&
                Objects.equals(tabularColumns, that.tabularColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, additionalProperties, anchorGuid, displayName, description, namespace,
                versionNumber, author, usage, encodingStandard, tabularColumns);
    }

}
