/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
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
                @JsonSubTypes.Type(value = DataFile.class, name = "DataFile"),
                @JsonSubTypes.Type(value = CSVFile.class, name = "CSVFile")
        })
public class DataFile extends DataStore {

    private String fileType;
    private SchemaType schema;
    private List<Attribute> columns;

    //needed by handler
    private String description;
    private String pathName;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public SchemaType getSchema() {
        return schema;
    }

    public void setSchema(SchemaType schema) {
        this.schema = schema;
    }

    public List<Attribute> getColumns() {
        return columns;
    }

    public void setColumns(List<Attribute> columns) {
        this.columns = columns;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                ", fileType='" + fileType + "'" +
                ", schema='" + schema + "'" +
                ", columns='" + columns + "'" +
                ", description='" + description + "'" +
                ", path='" + pathName + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFile dataFile = (DataFile) o;

        return Objects.equals(fileType, dataFile.fileType) &&
                Objects.equals(schema, dataFile.schema) &&
                Objects.equals(columns, dataFile.columns) &&
                Objects.equals(description, dataFile.description) &&
                Objects.equals(pathName, dataFile.pathName);
    }

}
