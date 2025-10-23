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
 * ActionTargetType characterises one of the types of open metadata element supported by a specific governance service.
 * This enables the capability of a governance service to be correctly matched to the resources and elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetType extends SpecificationProperty
{
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
        super(template);

        if (template != null)
        {
            this.typeName                   = template.getTypeName();
            this.deployedImplementationType = template.getDeployedImplementationType();
            this.required                   = template.getRequired();
        }
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionTargetType{" +
                "typeName='" + typeName + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
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
        ActionTargetType that = (ActionTargetType) objectToCompare;
        return required == that.required &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(deployedImplementationType, that.deployedImplementationType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeName, deployedImplementationType, required);
    }
}
