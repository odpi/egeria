/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * Team provides the detailed properties for a team including its members and leaders.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Organization.class, name = "Organization")
})
public class Team extends TeamSummary
{
    private static final long    serialVersionUID = 1L;

    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public Team()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Team(Team   template)
    {
        super(template);

        if (template != null)
        {
            this.additionalProperties = template.getAdditionalProperties();
            this.extendedProperties = template.getExtendedProperties();
        }
    }


    /**
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any properties associated with the subclass of this element.
     *
     * @param extendedProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Team{" +
                "extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", teamType='" + getTeamType() + '\'' +
                ", superTeam='" + getSuperTeam() + '\'' +
                ", subTeams=" + getSubTeams() +
                ", contactDetails=" + getContactDetails() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        Team team = (Team) objectToCompare;
        return Objects.equals(getExtendedProperties(), team.getExtendedProperties()) &&
                 Objects.equals(getAdditionalProperties(), team.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getExtendedProperties(), getAdditionalProperties());
    }
}
