/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConfigurationPropertyType characterises one of the configuration parameters supported by a specific connector.
 * This enables the capability of a connector to be correctly matched to the resources and
 * elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConfigurationPropertyType extends SpecificationProperty
{
    /**
     * The type name of the value for this configuration property.
     */
    private String dataType = null;

    /**
     * An example values for this configuration property.
     */
    private String example = null;

    /**
     * Is this configuration property required for the service to work successfully.
     */
    private boolean required = false;


    /**
     * Default constructor
     */
    public ConfigurationPropertyType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConfigurationPropertyType(ConfigurationPropertyType template)
    {
        super(template);

        if (template != null)
        {
            this.dataType    = template.getDataType();
            this.example     = template.getExample();
            this.required    = template.getRequired();
        }
    }


    /**
     * Return the data type name for the configuration property value.
     *
     * @return data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up data type name for the configuration property value.
     *
     * @param dataType data type
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return an example configuration property value.
     *
     * @return string
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Set up an example configuration property value.
     *
     * @param example string
     */
    public void setExample(String example)
    {
        this.example = example;
    }


    /**
     * Return whether this configuration property is required for this connector to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether this configuration property is required for this connector to work successful.
     *
     * @param required boolean flag
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConfigurationPropertyType{" +
                "dataType='" + dataType + '\'' +
                ", example='" + example + '\'' +
                ", required=" + required +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ConfigurationPropertyType that = (ConfigurationPropertyType) objectToCompare;
        return required == that.required &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(example, that.example);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataType, example, required);
    }
}
