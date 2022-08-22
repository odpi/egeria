/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securitymanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securitymanager.properties.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileLocationElement contains the properties and header for a relationship between a profile and a user identity retrieved
 * from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileLocationElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ProfileLocationProperties properties = null;
    private ElementStub               location   = null;


    /**
     * Default constructor
     */
    public ProfileLocationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileLocationElement(ProfileLocationElement template)
    {
        if (template != null)
        {
            properties = template.getProperties();
            location = template.getLocation();
        }
    }


    /**
     * Return the properties from the profile location relationship.
     *
     * @return profile identity
     */
    public ProfileLocationProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the profile location relationship.
     *
     * @param properties profile identity
     */
    public void setProperties(ProfileLocationProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the properties of the location.
     *
     * @return properties
     */
    public ElementStub getLocation()
    {
        return location;
    }


    /**
     * Set up the userId properties.
     *
     * @param location  properties
     */
    public void setLocation(ElementStub location)
    {
        this.location = location;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProfileLocationElement{" +
                       "properties=" + properties +
                       ", location=" + location +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ProfileLocationElement that = (ProfileLocationElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                       Objects.equals(location, that.location);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, location);
    }
}
