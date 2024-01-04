/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFileProperties describes the property of a single data file.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFileProperties extends DataStoreProperties
{
    private static final long    serialVersionUID = 1L;

    private String   fileType = null;


    /**
     * Default constructor
     */
    public DataFileProperties()
    {
        super();
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
        return "DataFileProperties{" +
                       "fileType='" + fileType + '\'' +
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
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
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
        if (! (objectToCompare instanceof DataFileProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        DataFileProperties that = (DataFileProperties) objectToCompare;

        return fileType != null ? fileType.equals(that.fileType) : that.fileType == null;
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
