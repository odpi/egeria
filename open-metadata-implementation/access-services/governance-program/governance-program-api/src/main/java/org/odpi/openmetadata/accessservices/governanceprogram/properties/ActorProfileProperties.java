/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ActorProfileProperties describes the common properties of a Personal Profile, IT Profile and Team Profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ITProfileProperties.class, name = "ITProfileProperties"),
                @JsonSubTypes.Type(value = PersonalProfileProperties.class, name = "PersonalProfileProperties"),
                @JsonSubTypes.Type(value = TeamProfileProperties.class, name = "TeamProfileProperties")
        })
public class ActorProfileProperties extends ReferenceableProperties
{
    private String              knownName            = null;
    private String              description          = null;


    /**
     * Default Constructor
     */
    public ActorProfileProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template object being copied
     */
    public ActorProfileProperties(ActorProfileProperties template)
    {
        super (template);

        if (template != null)
        {
            this.knownName = template.getKnownName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the name that the person/automated agent/team is known as.
     *
     * @return string name
     */
    public String getKnownName()
    {
        return knownName;
    }


    /**
     * Set up the name that the person/automated agent/team is known as.
     *
     * @param knownName string name
     */
    public void setKnownName(String knownName)
    {
        this.knownName = knownName;
    }


    /**
     * Return description of the person/automated agent/team.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up description of the person/automated agent/team.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActorProfileProperties{" +
                       "knownName='" + knownName + '\'' +
                       ", description='" + description + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        ActorProfileProperties that = (ActorProfileProperties) objectToCompare;
        return Objects.equals(knownName, that.knownName) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), knownName, description);
    }
}
