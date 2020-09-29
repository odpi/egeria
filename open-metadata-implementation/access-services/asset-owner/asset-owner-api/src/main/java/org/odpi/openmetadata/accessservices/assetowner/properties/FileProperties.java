/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


import java.util.Objects;

/**
 * FileProperties describes the property of a single data file.
 */
public class FileProperties extends DataStoreProperties
{
    private static final long    serialVersionUID = 1L;

    private String   fileType = null;


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
            fileType = template.getFileType();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FileProperties{" +
                "fileType='" + fileType + '\'' +
                ", createTime=" + getCreateTime() +
                ", modifiedTime=" + getModifiedTime() +
                ", encodingType='" + getEncodingType() + '\'' +
                ", encodingLanguage='" + getEncodingLanguage() + '\'' +
                ", encodingDescription='" + getEncodingDescription() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerType=" + getOwnerType() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOtherOriginValues() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FileProperties that = (FileProperties) objectToCompare;
        return Objects.equals(fileType, that.fileType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fileType);
    }
}
