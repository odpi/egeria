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
 * ReplacementAttributeType characterises one of the attribute values that should be provided when using a specific
 * template.  A replacement attribute value is ued to update the principle entity of a template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReplacementAttributeType extends SpecificationProperty
{
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
        super(template);

        if (template != null)
        {
            this.dataType    = template.getDataType();
            this.example     = template.getExample();
            this.required    = template.getRequired();
        }
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
     * JSON-style toString
     *
     * @return return string containing the attribute names and values
     */
    @Override
    public String toString()
    {
        return "ReplacementAttributeType{" +
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
        ReplacementAttributeType that = (ReplacementAttributeType) objectToCompare;
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
