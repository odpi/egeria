/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Type object holds properties that are used for displaying the Open Metadata Types
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type
{
    /**
     * The type name
     */
    private String name;

    /**
     * The description of the type
     */
    private String description;

    /**
     * The version of the type
     */
    private Long version;

    /**
     * The super type of the current type
     */
    private String superType;


    /**
     * Default constructor.
     */
    public Type()
    {
    }


    /**
     * Returns the type name.
     *
     * @return the type name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the type name.
     *
     * @param name the type name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns the description of the type.
     *
     * @return the description of the type
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the type.
     *
     * @param description the description of the type
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Returns the version of the type.
     *
     * @return the version of the type
     */
    public Long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the type.
     *
     * @param version the version of the type
     */
    public void setVersion(Long version)
    {
        this.version = version;
    }


    /**
     * Returns the super type of the current type.
     *
     * @return the super type of the current type
     */
    public String getSuperType()
    {
        return superType;
    }


    /**
     * Set up the super type of the current type.
     *
     * @param superType the super type of the current type
     */
    public void setSuperType(String superType)
    {
        this.superType = superType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Type{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", version=" + version +
                       ", superType='" + superType + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof Type type))
        {
            return false;
        }
        return Objects.equals(name, type.name) &&
                       Objects.equals(description, type.description) &&
                       Objects.equals(version, type.version) &&
                       Objects.equals(superType, type.superType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, version, superType);
    }
}
