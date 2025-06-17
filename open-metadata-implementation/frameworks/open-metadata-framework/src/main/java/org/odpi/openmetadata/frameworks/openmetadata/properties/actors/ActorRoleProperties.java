/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
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
                @JsonSubTypes.Type(value = SolutionRoleProperties.class, name = "SolutionRoleProperties"),
        })
public class ActorRoleProperties extends ReferenceableProperties
{
    private String identifier = null; /* identifier */
    private String scope       = null; /* scope */
    private String name        = null; /* name */
    private String description = null; /* description */



    /**
     * Default constructor
     */
    public ActorRoleProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ACTOR_ROLE.typeName);
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
            this.identifier    = template.getIdentifier();
            this.scope       = template.getScope();
            this.name        = template.getName();
            this.description = template.getDescription();
        }
    }



    /**
     * Return the unique identifier for this job role/appointment typically from an HR system.
     *
     * @return unique identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the unique identifier for this job role/appointment.
     *
     * @param identifier unique identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
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
     * Return the job role title.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the job role title.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the job role.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the job role.
     *
     * @param description string description
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
        return "ActorRoleProperties{" +
                "identifier='" + identifier + '\'' +
                ", scope='" + scope + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
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
        return Objects.equals(identifier, that.identifier) &&
                       Objects.equals(scope, that.scope) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) ;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(identifier, scope, name, description);
    }
}
