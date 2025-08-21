/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestParameterType characterises one of the request parameters supported by a specific governance service.
 * This enables the capability of a governance service to be correctly matched to the resources and
 * elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestParameterType
{
    /**
     * Value to use for the name of the request parameter.
     */
    private String name = null;

    /**
     * Description of the request parameter.
     */
    private String description = null;

    /**
     * The type name of the value for this parameter.
     */
    private String dataType = null;

    /**
     * An example values for this parameter.
     */
    private String example = null;


    /**
     * Is this request parameter required for the service to work successfully.
     */
    private boolean required = false;

    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;

    /**
     * Default constructor
     */
    public RequestParameterType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestParameterType(RequestParameterType template)
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
     * Return the string to use as the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the name of the request parameter.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /**
     * Return the description of the request parameter.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the request parameter.
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
     * Return whether this request parameter is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether this request parameter is required for this service to work successful.
     *
     * @param required boolean flag
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }


    /**
     * Return a map of property name to property value to provide additional information for this governance service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value to provide additional information for this governance service.
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
        if (! (objectToCompare instanceof RequestParameterType that))
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
