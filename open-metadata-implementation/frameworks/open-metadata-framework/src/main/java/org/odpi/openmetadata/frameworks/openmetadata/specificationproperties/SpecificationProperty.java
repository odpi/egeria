/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SpecificationProperty characterises one of the specification properties used to explain to a caller
 * how to use a template, or a connector.  The type of specification properties linked to an element
 * depends on its type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActionTargetType.class, name = "ActionTargetType"),
                @JsonSubTypes.Type(value = AnalysisStepType.class, name = "AnalysisStepType"),
                @JsonSubTypes.Type(value = AnnotationTypeType.class, name = "AnnotationTypeType"),
                @JsonSubTypes.Type(value = ConfigurationPropertyType.class, name = "ConfigurationPropertyType"),
                @JsonSubTypes.Type(value = GuardType.class, name = "GuardType"),
                @JsonSubTypes.Type(value = PlaceholderPropertyType.class, name = "PlaceholderPropertyType"),
                @JsonSubTypes.Type(value = ReplacementAttributeType.class, name = "ReplacementAttributeType"),
                @JsonSubTypes.Type(value = RequestParameterType.class, name = "RequestParameterType"),
                @JsonSubTypes.Type(value = RequestTypeType.class, name = "RequestTypeType"),
                @JsonSubTypes.Type(value = TemplateType.class, name = "TemplateType"),
        })
public class SpecificationProperty
{
    /**
     * Defines the type of specification property.
     */
    private SpecificationPropertyType specificationPropertyType = null;

    /**
     * Value to use for the specification property.
     */
    private String name = null;


    /**
     * Description of the specification property.
     */
    private String description = null;

    /**
     * A map of additional property name to property value for this specification property.
     */
    private Map<String, String> otherPropertyValues = null;



    /**
     * Default constructor
     */
    public SpecificationProperty()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SpecificationProperty(SpecificationProperty template)
    {
        if (template != null)
        {
            this.specificationPropertyType = template.getSpecificationPropertyType();
            this.name                      = template.getName();
            this.description               = template.getDescription();
            this.otherPropertyValues       = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the type of specification property.
     *
     * @return enum
     */
    public SpecificationPropertyType getSpecificationPropertyType()
    {
        return specificationPropertyType;
    }


    /**
     * Set up the type of specification property.
     *
     * @param specificationPropertyType enum
     */
    public void setSpecificationPropertyType(SpecificationPropertyType specificationPropertyType)
    {
        this.specificationPropertyType = specificationPropertyType;
    }


    /**
     * Return the string to use as the specification property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the specification property.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the specification property.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the specification property.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
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
        return "SpecificationProperty{" +
                "specificationPropertyType=" + specificationPropertyType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
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
        if (! (objectToCompare instanceof SpecificationProperty that))
        {
            return false;
        }
        return this.specificationPropertyType == that.specificationPropertyType &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
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
        return Objects.hash(specificationPropertyType, name, description, otherPropertyValues);
    }
}
