/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamSummary provides the core description of a team used when returning team structures.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Team.class, name = "Team")
})
public class TeamSummary extends ActorHeader
{
    private static final long    serialVersionUID = 1L;

    private String       teamType = null;
    private String       superTeam = null;
    private List<String> subTeams = null;

    /**
     * Default constructor
     */
    public TeamSummary()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamSummary(TeamSummary   template)
    {
        super(template);

        if (template != null)
        {
            teamType = template.getTeamType();
            superTeam = template.getSuperTeam();
            subTeams = template.getSubTeams();
        }
    }


    /**
     * Return a descriptive name that describes the type of team eg "department").
     *
     * @return team type name
     */
    public String getTeamType()
    {
        return teamType;
    }


    /**
     * Set up a descriptive name that describes the type of team eg "department").
     *
     * @param teamType team type name
     */
    public void setTeamType(String teamType)
    {
        this.teamType = teamType;
    }


    /**
     * Return the unique identifier (guid) of the team that this team reports to - null means top level team.
     *
     * @return guid
     */
    public String getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Set up the unique identifier (guid) of the team that this team reports to - null means top level team.
     *
     * @param superTeam guid
     */
    public void setSuperTeam(String superTeam)
    {
        this.superTeam = superTeam;
    }


    /**
     * Return the list of unique identifiers (guids) for the teams that report to this team.
     *
     * @return list of guids
     */
    public List<String> getSubTeams()
    {
        if (subTeams == null)
        {
            return null;
        }
        else if (subTeams.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(subTeams);
        }
    }


    /**
     * Set up the list of unique identifiers (guids) for the teams that report to this team.
     *
     * @param subTeams list of guids
     */
    public void setSubTeams(List<String> subTeams)
    {
        this.subTeams = subTeams;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TeamSummary{" +
                "teamType='" + teamType + '\'' +
                ", superTeam='" + superTeam + '\'' +
                ", subTeams=" + subTeams +
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
        TeamSummary that = (TeamSummary) objectToCompare;
        return Objects.equals(getTeamType(), that.getTeamType()) &&
                Objects.equals(getSuperTeam(), that.getSuperTeam()) &&
                Objects.equals(getSubTeams(), that.getSubTeams());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTeamType(), getSuperTeam(), getSubTeams());
    }
}
