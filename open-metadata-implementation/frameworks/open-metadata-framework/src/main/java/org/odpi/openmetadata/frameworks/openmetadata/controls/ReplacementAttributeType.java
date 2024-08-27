/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReplacementAttributeType characterises one of the attribute values that should be provided when using a specific
 * template.  A replacement attribute value is ued to update the principle entity of a template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReplacementAttributeType
{
    /**
     * Value to use for the name of the replacement attribute.
     */
    private String name = null;

    /**
     * Description of the replacement attribute.
     */
    private String description = null;

    /**
     * The type name of the value for this attribute.
     */
    private String dataType = null;

    /**
     * An example values for this parameter.
     */
    private String example = null;


    /**
     * Is this replacement attribute required for the service to work successfully.
     */
    private boolean required = false;

    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;

    /**
     * Default constructor
     */
    public ReplacementAttributeType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReplacementAttributeType(ReplacementAttributeType template)
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
     * Return the string to use as the name of the replacement attribute.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the name of the replacement attribute.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /**
     * Return the description of the replacement attribute.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the replacement attribute.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the data type name for the parameter value.
     *
     * @return data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up data type name for the parameter value.
     *
     * @param dataType data type
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return an example parameter value.
     *
     * @return string
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Set up an example parameter value.
     *
     * @param example string
     */
    public void setExample(String example)
    {
        this.example = example;
    }


    /**
     * Return whether this replacement attribute is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether this replacement attribute is required for this service to work successful.
     *
     * @param required boolean flag
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }


    /**
     * Return a map of attribute name to attribute value to provide additional information for this service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of attribute name to attribute value to provide additional information for this service.
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
     * @return return string containing the attribute names and values
     */
    @Override
    public String toString()
    {
        return "ReplacementAttributeType{" +
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
        if (! (objectToCompare instanceof ReplacementAttributeType that))
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
