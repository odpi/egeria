/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFileProperties describes the property of a single data file.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArchiveFileProperties.class, name = "ArchiveFileProperties"),
        @JsonSubTypes.Type(value = AvroFileProperties.class, name = "AvroFileProperties"),
        @JsonSubTypes.Type(value = BuildInstructionFileProperties.class, name = "BuildInstructionFileProperties"),
        @JsonSubTypes.Type(value = CSVFileProperties.class, name = "CSVFileProperties"),
        @JsonSubTypes.Type(value = ExecutableFileProperties.class, name = "ExecutableFileProperties"),
        @JsonSubTypes.Type(value = JSONFileProperties.class, name = "JSONFileProperties"),
        @JsonSubTypes.Type(value = KeyStoreFileProperties.class, name = "KeyStoreFileProperties"),
        @JsonSubTypes.Type(value = LogFileProperties.class, name = "LogFileProperties"),
        @JsonSubTypes.Type(value = MediaFileProperties.class, name = "MediaFileProperties"),
        @JsonSubTypes.Type(value = ParquetFileProperties.class, name = "ParquetFileProperties"),
        @JsonSubTypes.Type(value = PropertiesFileProperties.class, name = "PropertiesFileProperties"),
        @JsonSubTypes.Type(value = ScriptFileProperties.class, name = "ScriptFileProperties"),
        @JsonSubTypes.Type(value = SourceCodeFileProperties.class, name = "SourceCodeFileProperties"),
        @JsonSubTypes.Type(value = SpreadsheetFileProperties.class, name = "SpreadsheetFileProperties"),
        @JsonSubTypes.Type(value = XMLFileProperties.class, name = "XMLFileProperties"),
})
public class DataFileProperties extends DataStoreProperties
{
    private String fileType      = null;
    private String fileName      = null;
    private String fileExtension = null;

    /**
     * Default constructor
     */
    public DataFileProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_FILE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFileProperties(DataFileProperties template)
    {
        super(template);

        if (template != null)
        {
            fileType      = template.getFileType();
            fileName      = template.getFileName();
            fileExtension = template.getFileExtension();
        }
    }


    /**
     * Return the file type of the file if known.
     *
     * @return file type string
     */
    public String getFileType()
    {
        return fileType;
    }


    /**
     * Set up the file type of the file if known.
     *
     * @param fileType string
     */
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }


    /**
     * Return the name of the file (do not want to rely on Name).
     *
     * @return string
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Set up the name of the file (do not want to rely on Name).
     *
     * @param fileName string
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    /**
     * Return the file extension, if any.
     *
     * @return string
     */
    public String getFileExtension()
    {
        return fileExtension;
    }


    /**
     * Set up the file extension, if any.
     *
     * @param fileExtension string
     */
    public void setFileExtension(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataFileProperties{" +
                "fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataFileProperties that = (DataFileProperties) objectToCompare;
        return Objects.equals(fileType, that.fileType) && Objects.equals(fileName, that.fileName) && Objects.equals(fileExtension, that.fileExtension);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fileType, fileName, fileExtension);
    }
}
