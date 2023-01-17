/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LocationProperties is a class for representing a generic location.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LocationProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String identifier  = null;
    private String displayName = null;
    private String description = null;


    /**
     * Default constructor
     */
    public LocationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public LocationProperties(LocationProperties template)
    {
        super(template);

        if (template != null)
        {
            identifier  = template.getIdentifier();
            displayName = template.getDisplayName();
            description = template.getDescription();
        }
    }



    /**
     * Return the code value or symbol used to identify the location - typically unique.
     *
     * @return string identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the code value or symbol used to identify the location - typically unique.
     *
     * @param identifier string identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return a human memorable name for the location.
     *
     * @return string  name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up a human memorable name for the location.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the location.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the location.
     *
     * @param description string text
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
        return "LocationProperties{" +
                       "identifier='" + identifier + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        LocationProperties that = (LocationProperties) objectToCompare;
        return Objects.equals(identifier, that.identifier) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), identifier, displayName, description);
    }
}
