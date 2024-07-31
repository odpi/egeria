/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileIdentityRequestBody provides the request body payload for working with ProfileIdentity relationships.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileIdentityRequestBody extends ExternalSourceRequestBody
{
    private ProfileIdentityProperties properties = null;

    /**
     * Default constructor
     */
    public ProfileIdentityRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileIdentityRequestBody(ProfileIdentityRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the properties for this profile identity.
     *
     * @return properties bean
     */
    public ProfileIdentityProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for this profile identity.
     *
     * @param properties properties bean
     */
    public void setProperties(ProfileIdentityProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ProfileIdentityRequestBody{" +
                       "properties=" + properties +
                       ", externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ProfileIdentityRequestBody that = (ProfileIdentityRequestBody) objectToCompare;
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
