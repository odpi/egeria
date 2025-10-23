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
 * ValidMetadataValueDetail represents a single valid value for a property that has been retrieved.
 * If the property is a map property, it represents a single valid map name and its map values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidMetadataValueDetail extends ValidMetadataValue
{
    private String                   propertyName       = null;
    private String                   typeName           = null;
    private String                   mapName            = null;
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
            propertyName = template.getPropertyName();
            typeName = template.getTypeName();
            mapName = template.getMapName();
            validMapNameValues = template.getValidMapNameValues();
        }
    }


    /**
     * Return the property name that this valid value is linked to.
     *
     * @return string
     */
    public String getPropertyName()
    {
        return propertyName;
    }


    /**
     * Set up the property name that this valid value is linked to.
     *
     * @param propertyName string
     */
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }


    /**
     * Return a type name that is restricting the use of this valid value.
     *
     * @return string
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up a type name that is restricting the use of this valid value.
     *
     * @param typeName string
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return a map name if the valid value is for the domain of a map.
     *
     * @return string
     */
    public String getMapName()
    {
        return mapName;
    }


    /**
     * Set up a map name if the valid value is for the domain of a map.
     *
     * @param mapName string
     */
    public void setMapName(String mapName)
    {
        this.mapName = mapName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidMetadataValueDetail(ValidMetadataValue template)
    {
        super(template);
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
                "propertyName='" + propertyName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", mapName='" + mapName + '\'' +
                ", validMapNameValues=" + validMapNameValues +
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
        return Objects.equals(propertyName, that.propertyName) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(mapName, that.mapName) &&
                Objects.equals(validMapNameValues, that.validMapNameValues);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propertyName, typeName, mapName, validMapNameValues);
    }
}
