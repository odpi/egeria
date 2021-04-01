/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type DataFile
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataFile.class, name = "dataFile"),
                @JsonSubTypes.Type(value = CSVFile.class, name = "csvFile")
        })
public class DataFile implements Serializable {

    //Referenceable
    protected String qualifiedName;
    protected Map<String,String> additionalProperties;

    //Asset
    protected String name;
    protected String owner;
    protected OwnerType ownerType;
    protected List<String> zoneMembership;
    protected String latestChange;

    //DataStore
    protected Date createTime;
    protected Date modifiedTime;

    // DataFile
    protected String fileType;

    //needed by handler
    protected String description;
    protected String path;

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String,String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String,String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    public String getLatestChange() {
        return latestChange;
    }

    public void setLatestChange(String latestChange) {
        this.latestChange = latestChange;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                "qualifiedName='" + qualifiedName + "'" +
                ", additionalProperties='" + additionalProperties + "'" +
                ", name='" + name + "'" +
                ", owner='" + owner + "'" +
                ", ownerType='" + ownerType + "'" +
                ", zoneMembership='" + zoneMembership + "'" +
                ", latestChange='" + latestChange + "'" +
                ", createTime='" + createTime + "'" +
                ", modifiedTime='" + modifiedTime + "'" +
                ", fileType='" + fileType + "'" +
                ", description='" + description + "'" +
                ", path='" + path + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFile dataFile = (DataFile) o;

        return Objects.equals(qualifiedName, dataFile.qualifiedName) &&
                Objects.equals(additionalProperties, dataFile.additionalProperties) &&
                Objects.equals(fileType, dataFile.fileType) &&
                Objects.equals(name, dataFile.name) &&
                Objects.equals(owner, dataFile.owner) &&
                Objects.equals(ownerType, dataFile.ownerType) &&
                Objects.equals(zoneMembership, dataFile.zoneMembership) &&
                Objects.equals(latestChange, dataFile.latestChange) &&
                Objects.equals(createTime, dataFile.modifiedTime) &&
                Objects.equals(modifiedTime, dataFile.modifiedTime) &&
                Objects.equals(description, dataFile.description) &&
                Objects.equals(path, dataFile.path);
    }

}
