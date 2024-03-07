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
 * ActionTargetType characterises one of the types of open metadata element supported by a specific governance service.
 * This enables the capability of a governance service to be correctly matched to the resources and elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetType
{
    /**
     * Value to use for the name of the action target.
     */
    private String name = null;


    /**
     * Description of the action target type.
     */
    private String description = null;

    /**
     * The open metadata type name of the element that can be an action target.
     */
    private String typeName = null;

    /**
     * The deployed implementation type allows the service to be more specific about the resources it works with.
     */
    private String deployedImplementationType = null;


    /**
     * Is this ActionTarget required for the service to work successfully.
     */
    private boolean required = false;


    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;


    /**
     * Default constructor
     */
    public ActionTargetType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActionTargetType(ActionTargetType template)
    {
        if (template != null)
        {
            this.name        = template.getName();
            this.description = template.getDescription();
            this.typeName = template.getTypeName();
            this.deployedImplementationType = template.getDeployedImplementationType();
            this.required = template.getRequired();
            this.otherPropertyValues = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the string to use as the name of the action target.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the string to use as the name of the action target.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }



    /**
     * Return the description of the action target.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the action target.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the type name (or super type name) of a permitted action target.
     *
     * @return name of an open metadata type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the type name (or super type name) of a permitted action target.
     *
     * @param typeName name of an open metadata type
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return a more specific definition of a permitted action target.
     *
     * @return deployed implementation type name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up a more specific definition of a permitted action target.
     *
     * @param deployedImplementationType deployed implementation type name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return whether this action target type is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Set up whether this action target type is required for this service to work successful.
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
     * Set up a map of property name to property value to provide additional information for this governance service..
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
        return "ActionTargetType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeName='" + typeName + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
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
        if (! (objectToCompare instanceof ActionTargetType that))
        {
            return false;
        }
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(deployedImplementationType, that.deployedImplementationType) &&
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
        return Objects.hash(name, description, typeName, required, deployedImplementationType, otherPropertyValues);
    }
}
