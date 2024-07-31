/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FileProperties describes the property of a single data file.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FileProperties extends DataStoreProperties
{
    private String fileType      = null;
    private String fileName      = null;
    private String fileExtension = null;

    /**
     * Default constructor
     */
    public FileProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FileProperties(FileProperties template)
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
        return "FileProperties{" +
                "fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", deployedImplementationType='" + getDeployedImplementationType() + '\'' +
                ", pathName='" + getPathName() + '\'' +
                ", createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encodingType='" + getEncodingType() + '\'' +
                ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", encodingProperties=" + getEncodingProperties() +
                ", name='" + getName() + '\'' +
                ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                ", description='" + getResourceDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", effectiveFrom=" + getEffectiveFrom() +
                ", effectiveTo=" + getEffectiveTo() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
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
        FileProperties that = (FileProperties) objectToCompare;
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
