/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidMetadataValueDetail retrieves a single valid value for a property.  If the property is a map property, it represents a single valid map name
 * and its map values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidMetadataValueDetail extends ValidMetadataValue
{
    private List<ValidMetadataValue> validMapNameValues = null;


    /**
     * Constructor
     */
    public ValidMetadataValueDetail()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidMetadataValueDetail(ValidMetadataValueDetail template)
    {
        super(template);

        if (template != null)
        {
            validMapNameValues = template.getValidMapNameValues();
        }
    }


    /**
     * Return the related map values.
     *
     * @return list of valid metadata values
     */
    public List<ValidMetadataValue> getValidMapNameValues()
    {
        return validMapNameValues;
    }


    /**
     * Set up the related map values.
     *
     * @param validMapNameValues list of valid metadata values
     */
    public void setValidMapNameValues(List<ValidMetadataValue> validMapNameValues)
    {
        this.validMapNameValues = validMapNameValues;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ValidMetadataValueDetail{" +
                "validMapNameValues=" + validMapNameValues +
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
        if (! (objectToCompare instanceof ValidMetadataValueDetail that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(validMapNameValues, that.validMapNameValues);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), validMapNameValues);
    }
}
