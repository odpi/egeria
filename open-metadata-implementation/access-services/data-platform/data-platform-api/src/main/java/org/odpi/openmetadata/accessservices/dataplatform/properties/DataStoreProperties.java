/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

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
        })
public class DataStoreProperties extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

    private Date                createTime          = null;
    private Date                modifiedTime        = null;
    private String              encoding            = null;
    private String              language            = null;
    private String              encodingDescription = null;
    private Map<String, String> dataStoreProperties = null;


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
            createTime = template.getCreateTime();
            modifiedTime = template.getModifiedTime();
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
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Set up the name of the encoding style used in the data store.
     *
     * @param encoding string name
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    /**
     * Return the name of the natural language used for text strings within the data store.
     *
     * @return string language name
     */
    public String getLanguage()
    {
        return language;
    }


    /**
     * Set up the name of the natural language used for text strings within the data store.
     *
     * @param language string language name
     */
    public void setLanguage(String language)
    {
        this.language = language;
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
     * Return properties about the data store.
     *
     * @return name value pairs
     */
    public Map<String, String> getDataStoreProperties()
    {
        if (dataStoreProperties == null)
        {
            return null;
        }
        else if (dataStoreProperties.isEmpty())
        {
            return null;
        }

        return dataStoreProperties;
    }


    /**
     * Set up properties about the data store.
     *
     * @param dataStoreProperties name value pairs
     */
    public void setDataStoreProperties(Map<String, String> dataStoreProperties)
    {
        this.dataStoreProperties = dataStoreProperties;
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
                ", encoding='" + encoding + '\'' +
                ", language='" + language + '\'' +
                ", encodingDescription='" + encodingDescription + '\'' +
                ", dataStoreProperties=" + dataStoreProperties +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerCategory=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", origin=" + getOrigin() +
                ", latestChange='" + getLatestChange() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", classifications=" + getClassifications() +
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
                Objects.equals(encoding, that.encoding) &&
                Objects.equals(language, that.language) &&
                Objects.equals(encodingDescription, that.encodingDescription) &&
                Objects.equals(dataStoreProperties, that.dataStoreProperties);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), createTime, modifiedTime, encoding, language, encodingDescription, dataStoreProperties);
    }
}
