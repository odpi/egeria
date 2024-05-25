/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Location describes where the asset is located.  The model allows a very flexible definition of location
 * that can be set up at different levels of granularity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Location extends Referenceable
{
    /*
     * Properties that make up the location of the asset.
     */
    protected String identifier = null;
    protected String displayName = null;
    protected String description = null;


    /**
     * Default constructor
     */
    public Location()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param templateLocation template object to copy.
     */
    public Location(Location templateLocation)
    {
        super(templateLocation);

        if (templateLocation != null)
        {
            identifier = templateLocation.getIdentifier();
            displayName = templateLocation.getDisplayName();
            description = templateLocation.getDescription();
        }
    }


    /**
     * Return the unique identifier of the location.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the unique identifier of the location.
     *
     * @param identifier string
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return the stored display name property for the location.
     * If no display name is available then null is returned.
     *
     * @return String location name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the location.
     *
     * @param displayName String location name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the location.
     * If no description is provided then null is returned.
     *
     * @return location description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the location.
     *
     * @param description location description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Location{" +
                       "url='" + url + '\'' +
                       ", extendedProperties=" + extendedProperties +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", identifier='" + identifier + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Location location = (Location) objectToCompare;
        return Objects.equals(getDisplayName(), location.getDisplayName()) &&
                       Objects.equals(getDescription(), location.getDescription());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription());
    }
}