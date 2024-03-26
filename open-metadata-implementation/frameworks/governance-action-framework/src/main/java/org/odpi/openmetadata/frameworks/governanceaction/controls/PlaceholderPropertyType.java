/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PlaceholderPropertyType characterises one of the placeholder properties used in a template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlaceholderPropertyType
{
    /**
     * Value to use for the name of the placeholder property.
     */
    private String name = null;

    /**
     * Description of the placeholder property.
     */
    private String description = null;

    /**
     * The type name of the value for this placeholder property.
     */
    private String dataType = null;

    /**
     * An example values for this placeholder property.
     */
    private String example = null;


    /**
     * Is this placeholder property required for the service to work successfully.
     */
    private boolean required = false;

    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;

    /**
     * Default constructor
     */
    public PlaceholderPropertyType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PlaceholderPropertyType(PlaceholderPropertyType template)
    {
        if (template != null)
        {
            this.name        = template.getName();
            this.description = template.getDescription();
            this.dataType    = template.getDataType();
            this.example     = template.getExample();
            this.required    = template.getRequired();
            this.otherPropertyValues = template.getOtherPropertyValues();
        }
    }

    /**
     * Return the string to use as the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the name of the placeholder property.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /**
     * Return the description of the placeholder property.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the placeholder property.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the data type name for the placeholder property value.
     *
     * @return data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up data type name for the placeholder property value.
     *
     * @param dataType data type
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return an example placeholder property value.
     *
     * @return string
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Set up an example placeholder property value.
     *
     * @param example string
     */
    public void setExample(String example)
    {
        this.example = example;
    }


    /**
     * Return whether this placeholder property is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether this placeholder property is required for this service to work successful.
     *
     * @param required boolean flag
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }


    /**
     * Return a map of property name to property value to provide additional information for this service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value to provide additional information for this service.
     *
     * @param otherPropertyValues map of string to string
     */
    public void setOtherPropertyValues(Map<String, String> otherPropertyValues)
    {
        this.otherPropertyValues = otherPropertyValues;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestParameterType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dataType='" + dataType + '\'' +
                ", example='" + example + '\'' +
                ", required=" + required +
                ", otherPropertyValues=" + otherPropertyValues +
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
        if (! (objectToCompare instanceof PlaceholderPropertyType that))
        {
            return false;
        }
        return Objects.equals(dataType, that.dataType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(example, that.example) &&
                (required == that.required) &&
                Objects.equals(otherPropertyValues, that.otherPropertyValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, dataType, required, example, otherPropertyValues);
    }
}
