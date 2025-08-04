/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamMembershipProperties describes the properties for the TeamMembership relationship between a team
 * and a team role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamMembershipProperties extends RelationshipBeanProperties
{
    private String positionName = null;


    /**
     * Default constructor
     */
    public TeamMembershipProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamMembershipProperties(TeamMembershipProperties template)
    {
        super(template);

        if (template != null)
        {
            positionName = template.getPositionName();
        }
    }


    /**
     * Return the name of the position within the team.
     *
     * @return string
     */
    public String getPositionName()
    {
        return positionName;
    }


    /**
     * Set up the name of the position within the team.
     *
     * @param positionName string
     */
    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TeamMembershipProperties{" +
                "positionName='" + positionName + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        TeamMembershipProperties that = (TeamMembershipProperties) objectToCompare;
        return Objects.equals(positionName, that.positionName);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), positionName);
    }
}
