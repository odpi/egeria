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
    private String pathName;

    /**
     * Gets file type
     *
     * @return type
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Sets the file type
     *
     * @param fileType type
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Gets the file schema
     *
     * @return schema
     */
    public SchemaType getSchema() {
        return schema;
    }

    /**
     * Sets the file schema
     *
     * @param schema schema
     */
    public void setSchema(SchemaType schema) {
        this.schema = schema;
    }

    /**
     * Gets the file columns
     *
     * @return columns
     */
    public List<Attribute> getColumns() {
        return columns;
    }

    /**
     * Sets the file columns
     *
     * @param columns columns
     */
    public void setColumns(List<Attribute> columns) {
        this.columns = columns;
    }

    /**
     * Gets the file path
     *
     * @return path
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Sets the file path
     *
     * @param pathName path
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    @Override
    public String toString() {
        return "DataFile{" +
                ", fileType='" + fileType + "'" +
                ", schema='" + schema + "'" +
                ", columns='" + columns + "'" +
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
                Objects.equals(pathName, dataFile.pathName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileType, schema, columns, pathName);
    }

}
