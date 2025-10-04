/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The TeamProperties describes an team of people.  Information about the
 * team is stored as a Team entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OrganizationProperties.class, name = "OrganizationProperties"),
        })
public class TeamProperties extends ActorProfileProperties
{
    private String teamType    = null;
    private String identifier  = null;


    /**
     * Default Constructor
     */
    public TeamProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TEAM.typeName);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public TeamProperties(TeamProperties template)
    {
        super (template);

        if (template != null)
        {
            this.teamType = template.getTeamType();
        }
    }


    /**
     * Return the type of team.
     *
     * @return string name
     */
    public String getTeamType()
    {
        return teamType;
    }


    /**
     * Set up the type of team.
     *
     * @param teamType string name
     */
    public void setTeamType(String teamType)
    {
        this.teamType = teamType;
    }


    /**
     * Return the code that uniquely identifies the team.
     *
     * @return string code
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the code that uniquely identifies the team.
     *
     * @param identifier string code
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TeamProperties{" +
                       "teamType='" + teamType + '\'' +
                       ", knownName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        TeamProperties that = (TeamProperties) objectToCompare;
        return Objects.equals(teamType, that.teamType);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), teamType);
    }
}
