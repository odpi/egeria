/* SPDX-License-Identifier: Apache-2.0 */
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
            displayName = templateLocation.getDisplayName();
            description = templateLocation.getDescription();
        }
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
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof Location))
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
}