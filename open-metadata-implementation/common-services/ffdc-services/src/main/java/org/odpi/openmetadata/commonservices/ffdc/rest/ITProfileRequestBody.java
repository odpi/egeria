/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITProfileRequestBody provides the request body payload for working on ActorProfile entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ITProfileRequestBody extends ExternalSourceRequestBody
{
    private String              itInfrastructureGUID = null;
    private String              itUserId = null;
    private ITProfileProperties properties = null;

    /**
     * Default constructor
     */
    public ITProfileRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ITProfileRequestBody(ITProfileRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.itInfrastructureGUID = template.getItInfrastructureGUID();
            this.itUserId = template.getItUserId();
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the optional IT Infrastructure asset to connect the profile to.
     *
     * @return guid
     */
    public String getItInfrastructureGUID()
    {
        return itInfrastructureGUID;
    }


    /**
     * Set up the optional IT Infrastructure asset to connect the profile to.
     *
     * @param itInfrastructureGUID guid
     */
    public void setItInfrastructureGUID(String itInfrastructureGUID)
    {
        this.itInfrastructureGUID = itInfrastructureGUID;
    }


    /**
     * Return the option userId for the profile which will be created (if needed) and linked to the
     * new IT profile.
     *
     * @return string userId
     */
    public String getItUserId()
    {
        return itUserId;
    }


    /**
     * Set up the option userId for the profile which will be created (if needed) and linked to the
     * new IT profile.
     *
     * @param itUserId string userId
     */
    public void setItUserId(String itUserId)
    {
        this.itUserId = itUserId;
    }


    /**
     * Return the properties for this profile.
     *
     * @return properties bean
     */
    public ITProfileProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for this profile.
     *
     * @param properties properties bean
     */
    public void setProperties(ITProfileProperties properties)
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
        return "ITProfileRequestBody{" +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ITProfileRequestBody that = (ITProfileRequestBody) objectToCompare;
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
