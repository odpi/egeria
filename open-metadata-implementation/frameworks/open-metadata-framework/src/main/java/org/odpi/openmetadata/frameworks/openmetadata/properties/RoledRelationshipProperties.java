/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LineageRelationshipProperties describe the common properties for a lineage relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ImplementedByProperties.class, name = "ImplementedByProperties"),
                @JsonSubTypes.Type(value = ImplementationResourceProperties.class, name = "ImplementationResourceProperties"),
                @JsonSubTypes.Type(value = SolutionComponentActorProperties.class, name = "SolutionComponentActorProperties"),
                @JsonSubTypes.Type(value = SolutionComponentPortProperties.class, name = "SolutionComponentPortProperties"),
                @JsonSubTypes.Type(value = SolutionCompositionProperties.class, name = "SolutionCompositionProperties"),
                @JsonSubTypes.Type(value = SolutionPortDelegationProperties.class, name = "SolutionPortDelegationProperties"),
        })
public class RoledRelationshipProperties extends RelationshipBeanProperties
{
    private String role        = null;
    private String description = null;


    /**
     * Default constructor
     */
    public RoledRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public RoledRelationshipProperties(RoledRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            role        = template.getRole();
            description = template.getDescription();
        }
    }


    /**
     * Return the role used when displaying this relationship.
     *
     * @return string
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Set up the role used when displaying this relationship.
     *
     * @param role string
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * Return the description of the relationship.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the relationship.
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
        return "LabeledRelationshipProperties{" +
                "label='" + role + '\'' +
                ", description='" + description + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare)) return false;
        RoledRelationshipProperties that = (RoledRelationshipProperties) objectToCompare;
        return Objects.equals(getRole(), that.getRole()) &&
                       Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), role, description);
    }
}