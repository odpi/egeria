/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * BusinessCapabilityProperties represents one of an organization's business capabilities.  These can be linked to the teams and digital services
 * that support this capability.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BusinessCapabilityProperties extends ReferenceableProperties
{
    private String displayName = null;
    private String description = null;
    private String identifier  = null;

    /**
     * Default constructor
     */
    public BusinessCapabilityProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BusinessCapabilityProperties(BusinessCapabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            this.identifier = template.getIdentifier();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the identifier assigned by the organization to this capability.
     *
     * @return String
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the identifier assigned by the organization to this capability.
     *
     * @param identifier String
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return the display name for this asset (normally a shortened for of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this asset (normally a shortened for of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this asset.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this asset.
     *
     * @param description string
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
        return "BusinessCapabilityProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", identifier='" + identifier + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof BusinessCapabilityProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) && Objects.equals(identifier, that.identifier);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, identifier);
    }
}
