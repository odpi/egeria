/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamMemberProperties describes a person who is a member of a team.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamMemberProperties extends PersonalRoleProperties
{
    private static final long    serialVersionUID = 1L;

    private String    membershipPosition = null;
    private String    teamGUID = null;

    /**
     * Default constructor
     */
    public TeamMemberProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamMemberProperties(TeamMemberProperties template)
    {
        super(template);

        if (template != null)
        {
            membershipPosition = template.getMembershipPosition();
            teamGUID = template.getTeamGUID();
        }
    }


    /**
     * Return details of any special role as a member of the team.
     *
     * @return membership position
     */
    public String getMembershipPosition()
    {
        return membershipPosition;
    }


    /**
     * Set up details of any special role as a member of the team.
     *
     * @param membershipPosition membership position
     */
    public void setMembershipPosition(String membershipPosition)
    {
        this.membershipPosition = membershipPosition;
    }


    /**
     * Return the unique identifier (guid) of the team.
     *
     * @return string guid
     */
    public String getTeamGUID()
    {
        return teamGUID;
    }


    /**
     * Set up the unique identifier (guid) of the team.
     *
     * @param teamGUID string guid
     */
    public void setTeamGUID(String teamGUID)
    {
        this.teamGUID = teamGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TeamMemberProperties{" +
                       "membershipPosition='" + membershipPosition + '\'' +
                       ", teamGUID='" + teamGUID + '\'' +
                       ", domainIdentifier=" + getDomainIdentifier() +
                       ", roleId='" + getRoleId() + '\'' +
                       ", scope='" + getScope() + '\'' +
                       ", title='" + getTitle() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        TeamMemberProperties that = (TeamMemberProperties) objectToCompare;
        return Objects.equals(getMembershipPosition(), that.getMembershipPosition());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMembershipPosition());
    }
}
