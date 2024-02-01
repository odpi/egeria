/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.actiontargettype;

import java.util.Map;
import java.util.Objects;

/**
 * ActionTargetType characterises the type of third party technology supported by a specific governance service.
 * This enables the capability
 * of a governance service to be correctly matched to the resources and elements that it works with.
 */
public class ActionTargetType
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
     * A map of property name to property value for values that should match in the action target for it to be
     * compatible with this governance service.
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
            this.typeName = template.getTypeName();
            this.deployedImplementationType = template.getDeployedImplementationType();
            this.otherPropertyValues = template.getOtherPropertyValues();
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
     * Return a map of property name to property value that the action target should have to be valid for this governance service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value that the action target should have to be valid for this governance service.
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
                       "typeName='" + typeName + '\'' +
                       "deployedImplementationType='" + deployedImplementationType + '\'' +
                       ", otherPropertyValues='" + otherPropertyValues + '\'' +
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
                       Objects.equals(deployedImplementationType, that.deployedImplementationType) &&
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
        return Objects.hash(typeName, deployedImplementationType, otherPropertyValues);
    }
}
