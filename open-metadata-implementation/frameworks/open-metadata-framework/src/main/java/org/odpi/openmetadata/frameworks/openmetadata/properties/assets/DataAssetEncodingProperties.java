/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataAssetEncodingProperties provides the JavaBean for describing a data asset's encoding.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class DataAssetEncodingProperties extends ClassificationBeanProperties
{
    private String              encodingType        = null;
    private String              encodingLanguage    = null;
    private String              encodingDescription = null;
    private Map<String, String> encodingProperties  = null;


    /**
     * Default constructor
     */
    public DataAssetEncodingProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataAssetEncodingProperties(DataAssetEncodingProperties template)
    {
        super(template);

        if (template != null)
        {
            encodingType        = template.getEncodingType();
            encodingLanguage    = template.getEncodingLanguage();
            encodingDescription = template.getEncodingDescription();
            encodingProperties  = template.getEncodingProperties();
        }
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
        return "DataAssetEncodingProperties{" +
                "encodingType='" + encodingType + '\'' +
                ", encodingLanguage='" + encodingLanguage + '\'' +
                ", encodingDescription='" + encodingDescription + '\'' +
                ", encodingProperties=" + encodingProperties +
                "} " + super.toString();
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
        DataAssetEncodingProperties that = (DataAssetEncodingProperties) objectToCompare;
        return Objects.equals(encodingType, that.encodingType) &&
                       Objects.equals(encodingLanguage, that.encodingLanguage) &&
                       Objects.equals(encodingDescription, that.encodingDescription) &&
                       Objects.equals(encodingProperties, that.encodingProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), encodingType, encodingLanguage,
                            encodingDescription, encodingProperties);
    }
}
