/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataStoreProperties is a class for representing a generic data store.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseProperties.class, name = "DatabaseProperties"),
                @JsonSubTypes.Type(value = DataFileProperties.class, name = "DataFileProperties"),
                @JsonSubTypes.Type(value = FileFolderProperties.class, name = "FileFolderProperties"),
        })
public class DataStoreProperties extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

    private String              pathName            = null;
    private Date                createTime          = null;
    private Date                modifiedTime        = null;
    private String              encodingType        = null;
    private String              encodingLanguage    = null;
    private String              encodingDescription = null;
    private Map<String, String> encodingProperties  = null;


    /**
     * Default constructor
     */
    public DataStoreProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataStoreProperties(DataStoreProperties template)
    {
        super(template);

        if (template != null)
        {
            pathName            = template.getPathName();
            createTime          = template.getCreateTime();
            modifiedTime        = template.getModifiedTime();
            encodingType        = template.getEncodingType();
            encodingLanguage    = template.getEncodingLanguage();
            encodingDescription = template.getEncodingDescription();
            encodingProperties  = template.getEncodingProperties();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataStoreProperties(AssetProperties template)
    {
        super(template);
    }


    /**
     * Return the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @return string name
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Set up the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @param pathName string name
     */
    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }


    /**
     * Return the time that the data store was created.
     *
     * @return date
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the time that the data store was created.
     *
     * @param createTime date
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the last known time the data store was modified.
     *
     * @return date
     */
    public Date getModifiedTime()
    {
        return modifiedTime;
    }


    /**
     * Setup the last known time the data store was modified.
     *
     * @param modifiedTime date
     */
    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }


    /**
     * Return the name of the encoding style used in the data store.
     *
     * @return string name
     */
    public String getEncodingType()
    {
        return encodingType;
    }


    /**
     * Set up the name of the encoding style used in the data store.
     *
     * @param encodingType string name
     */
    public void setEncodingType(String encodingType)
    {
        this.encodingType = encodingType;
    }


    /**
     * Return the name of the natural language used for text strings within the data store.
     *
     * @return string language name
     */
    public String getEncodingLanguage()
    {
        return encodingLanguage;
    }


    /**
     * Set up the name of the natural language used for text strings within the data store.
     *
     * @param encodingLanguage string language name
     */
    public void setEncodingLanguage(String encodingLanguage)
    {
        this.encodingLanguage = encodingLanguage;
    }


    /**
     * Return the description of the encoding used in the data store.
     *
     * @return string text
     */
    public String getEncodingDescription()
    {
        return encodingDescription;
    }


    /**
     * Set up the description of the encoding used in the data store.
     *
     * @param encodingDescription string text
     */
    public void setEncodingDescription(String encodingDescription)
    {
        this.encodingDescription = encodingDescription;
    }


    /**
     * Return the additional properties associated with the encoding process.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getEncodingProperties()
    {
        if (encodingProperties == null)
        {
            return null;
        }
        else if (encodingProperties.isEmpty())
        {
            return null;
        }
        return encodingProperties;
    }


    /**
     * Set up the additional properties associated with the encoding process.
     *
     * @param encodingProperties map of name-value pairs
     */
    public void setEncodingProperties(Map<String, String> encodingProperties)
    {
        this.encodingProperties = encodingProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataStoreProperties{" +
                       "pathName=" + pathName +
                       ", createTime=" + createTime +
                       ", modifiedTime=" + modifiedTime +
                       ", encodingType='" + encodingType + '\'' +
                       ", encodingLanguage='" + encodingLanguage + '\'' +
                       ", encodingDescription='" + encodingDescription + '\'' +
                       ", encodingProperties='" + encodingProperties + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerCategory=" + getOwnerCategory() +
                       ", zoneMembership=" + getZoneMembership() +
                       ", origin=" + getOtherOriginValues() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        DataStoreProperties that = (DataStoreProperties) objectToCompare;
        return Objects.equals(pathName, that.pathName) &&
                       Objects.equals(createTime, that.createTime) &&
                       Objects.equals(modifiedTime, that.modifiedTime) &&
                       Objects.equals(encodingType, that.encodingType) &&
                       Objects.equals(encodingLanguage, that.encodingLanguage) &&
                       Objects.equals(encodingDescription, that.encodingDescription) &&
                       Objects.equals(encodingProperties, that.encodingProperties);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), pathName, createTime, modifiedTime, encodingType,
                            encodingLanguage, encodingDescription, encodingProperties);
    }
}
