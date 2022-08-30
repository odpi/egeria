/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

/**
 * TeamMembershipElement contains the properties and header for a role that shows a person is a leader or a member of
 * a team as retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamMembershipElement extends PersonalRoleElement
{
    private static final long     serialVersionUID = 1L;

    private String      position       = null;
    private boolean     leadershipRole = false;
    private ElementStub team           = null;


    /**
     * Default constructor
     */
    public TeamMembershipElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamMembershipElement(TeamMembershipElement template)
    {
        super(template);

        if (template != null)
        {
            position = template.getPosition();
            leadershipRole = template.getLeadershipRole();
            team = template.getTeam();

        }
    }


    /**
     * Return the name of the special position (if any) in the team.
     *
     * @return string name
     */
    public String getPosition()
    {
        return position;
    }


    /**
     * Set up the name of the special position (if any) in the team.
     *
     * @param position string name
     */
    public void setPosition(String position)
    {
        this.position = position;
    }


    /**
     * Return the properties of the userId.
     *
     * @return flag
     */
    public boolean getLeadershipRole()
    {
        return leadershipRole;
    }


    /**
     * Set up the userId properties.
     *
     * @param leadershipRole  flag
     */
    public void setLeadershipRole(boolean leadershipRole)
    {
        this.leadershipRole = leadershipRole;
    }


    /**
     * Return the unique identifier (guid) of the team.
     *
     * @return description of team profile
     */
    public ElementStub getTeam()
    {
        return team;
    }


    /**
     * Set up the unique identifier (guid) of the team.
     *
     * @param team string guid
     */
    public void setTeam(ElementStub team)
    {
        this.team = team;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TeamMembershipElement{" +
                       "position='" + position + '\'' +
                       ", leadershipRole=" + leadershipRole +
                       ", team='" + team + '\'' +
                       ", elementHeader=" + getElementHeader() +
                       ", properties=" + getProperties() +
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
        TeamMembershipElement that = (TeamMembershipElement) objectToCompare;
        return leadershipRole == that.leadershipRole &&
                       Objects.equals(position, that.position) &&
                       Objects.equals(team, that.team);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position, leadershipRole, team);
    }
}
