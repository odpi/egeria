/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActorRoleProperties provides a structure to describe a role assigned to an actor profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PersonRoleProperties.class, name = "PersonRoleProperties"),
                @JsonSubTypes.Type(value = TeamRoleProperties.class, name = "TeamRoleProperties"),
                @JsonSubTypes.Type(value = ITProfileRoleProperties.class, name = "ITProfileRoleProperties"),
                @JsonSubTypes.Type(value = SolutionActorRoleProperties.class, name = "SolutionActorRoleProperties"),
        })
public class ActorRoleProperties extends ActorProperties
{
    private String scope = null;


    /**
     * Default constructor
     */
    public ActorRoleProperties()
    {
        super();
        super.typeName = OpenMetadataType.ACTOR_ROLE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorRoleProperties(ActorProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.ACTOR_ROLE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActorRoleProperties(ActorRoleProperties template)
    {
        super(template);

        if (template != null)
        {
            this.scope = template.getScope();
        }
    }


    /**
     * Return the context in which the person is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @return string description
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the context in which the person is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @param scope string description
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActorRoleProperties{" +
                "scope='" + scope + '\'' +
                "} " + super.toString();
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
        ActorRoleProperties that = (ActorRoleProperties) objectToCompare;
        return Objects.equals(scope, that.scope);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), scope);
    }
}
