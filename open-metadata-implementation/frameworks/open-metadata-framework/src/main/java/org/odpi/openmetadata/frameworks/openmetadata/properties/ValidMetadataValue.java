/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidMetadataValue provides the properties for a valid metadata value.  The preferredValue is the
 * value that is used in the open metadata property.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidMetadataValue
{
    private String  category       = Category.VALID_METADATA_VALUES.getName();
    private String  displayName    = null;
    private String  description    = null;
    private String  preferredValue = null;
    private String  dataType       = DataType.STRING.getName();
    private boolean isCaseSensitive = false;

    private Map<String, String> additionalProperties = null;

    private Date effectiveFrom = null;
    private Date effectiveTo   = null;

    /**
     * Constructor
     */
    public ValidMetadataValue()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidMetadataValue(ValidMetadataValue template)
    {
        if (template != null)
        {
            category = template.getCategory();
            displayName = template.getDisplayName();
            description = template.getDescription();
            preferredValue = template.getPreferredValue();
            dataType = template.getDataType();
            isCaseSensitive = template.getIsCaseSensitive();
            additionalProperties = template.getAdditionalProperties();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
        }
    }


    /**
     * Return the category of reference data.
     *
     * @return string name
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Set up the category of reference data.
     *
     * @param category string name
     */
    public void setCategory(String category)
    {
        this.category = category;
    }


    /**
     * Returns the stored display name property for the valid value.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the valid value.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the valid value.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the valid value.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the preferred values to use in implementations (normally used with definitions)
     *
     * @return String value
     */
    public String getPreferredValue()
    {
        return preferredValue;
    }


    /**
     * Set up the preferred values to use in implementations (normally used with definitions)
     *
     * @param preferredValue String value
     */
    public void setPreferredValue(String preferredValue)
    {
        this.preferredValue = preferredValue;
    }


    /**
     * Returns the data type of the preferred value.
     *
     * @return string
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up the data type of the preferred value.
     *
     * @param dataType string
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return whether this valid value is case-sensitive, or will match irrespective of case.
     *
     * @return boolean flag
     */
    public boolean getIsCaseSensitive()
    {
        return isCaseSensitive;
    }


    /**
     * Set up whether this valid value is case-sensitive, or will match irrespective of case.
     *
     * @param caseSensitive boolean flag
     */
    public void setIsCaseSensitive(boolean caseSensitive)
    {
        isCaseSensitive = caseSensitive;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ValidMetadataValue{" +
                       "category='" + category + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", preferredValue='" + preferredValue + '\'' +
                       ", dataType='" + dataType + '\'' +
                       ", isCaseSensitive=" + isCaseSensitive +
                       ", additionalProperties=" + additionalProperties +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
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
        if (! (objectToCompare instanceof ValidMetadataValue that))
        {
            return false;
        }
        return isCaseSensitive == that.isCaseSensitive &&
                       Objects.equals(category, that.category) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(preferredValue, that.preferredValue) &&
                       Objects.equals(dataType, that.dataType) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(category, displayName, description, preferredValue, dataType, isCaseSensitive, additionalProperties, effectiveFrom, effectiveTo);
    }
}
