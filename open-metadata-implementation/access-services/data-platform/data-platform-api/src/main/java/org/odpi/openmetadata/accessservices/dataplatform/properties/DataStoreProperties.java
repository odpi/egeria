/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
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
        })
public class DataStoreProperties extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

    private Date   createTime          = null;
    private Date   modifiedTime        = null;
    private String encodingType        = null;
    private String encodingLanguage    = null;
    private String encodingDescription = null;


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
            createTime          = template.getCreateTime();
            modifiedTime        = template.getModifiedTime();
            encodingType        = template.getEncodingType();
            encodingLanguage    = template.getEncodingLanguage();
            encodingDescription = template.getEncodingDescription();
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataStoreProperties{" +
                "createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", encodingType='" + encodingType + '\'' +
                ", encodingLanguage='" + encodingLanguage + '\'' +
                ", encodingDescription='" + encodingDescription + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", latestChange='" + getLatestChange() + '\'' +
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
        return Objects.equals(createTime, that.createTime) &&
                Objects.equals(modifiedTime, that.modifiedTime) &&
                Objects.equals(encodingType, that.encodingType) &&
                Objects.equals(encodingLanguage, that.encodingLanguage) &&
                Objects.equals(encodingDescription, that.encodingDescription);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), createTime, modifiedTime, encodingType, encodingLanguage, encodingDescription);
    }
}
