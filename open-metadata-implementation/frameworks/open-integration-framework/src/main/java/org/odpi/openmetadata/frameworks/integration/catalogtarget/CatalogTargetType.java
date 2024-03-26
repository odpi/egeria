/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.catalogtarget;

import java.util.Map;
import java.util.Objects;

/**
 * CatalogTargetType characterises the type of third party technology supported by a specific integration connector.  This enables the capability
 * of an integration connector to be correctly matched to the resources and elements that it works with.
 */
public class CatalogTargetType
{
    /**
     * Catalog target name.
     */
    private String name = null;


    /**
     * The open metadata type name of the element that can be a catalog target.
     */
    private String typeName = null;


    /**
     * The deployed implementation type allows the connector to be more specific about the resources it works with.
     */
    private String deployedImplementationType = null;

    /**
     * A map of property name to property value for values that should match in the catalog target for it to be compatible with this integration
     * connector.
     */
    private Map<String, String> otherPropertyValues = null;


    /**
     * Default constructor
     */
    public CatalogTargetType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CatalogTargetType(CatalogTargetType template)
    {
        if (template != null)
        {
            this.name = template.getName();
            this.typeName = template.getTypeName();
            this.deployedImplementationType = template.getDeployedImplementationType();
            this.otherPropertyValues = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the catalog target name.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the catalog target name.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the type name (or super type name) of a permitted catalog target.
     *
     * @return name of an open metadata type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the type name (or super type name) of a permitted catalog target.
     *
     * @param typeName name of an open metadata type
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return a more specific definition of a permitted catalog target.
     *
     * @return deployed implementation type name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up a more specific definition of a permitted catalog target.
     *
     * @param deployedImplementationType deployed implementation type name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return a map of property name to property value that the catalog target should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value that the catalog target should have to be valid for this integration connector.
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
        return "CatalogTargetType{" +
                "catalogTargetName='" + name + '\'' +
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
        if (! (objectToCompare instanceof CatalogTargetType that))
        {
            return false;
        }
        return Objects.equals(name, that.name) &&
                       Objects.equals(typeName, that.typeName) &&
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
        return Objects.hash(name, typeName, deployedImplementationType, otherPropertyValues);
    }
}
