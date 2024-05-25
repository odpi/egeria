/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamPlayerRequestBody provides the request body payload for linking roles to teams.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamPlayerRequestBody extends ExternalSourceRequestBody
{
    private boolean leadershipRole = false;
    private String  position = null;

    /**
     * Default constructor
     */
    public TeamPlayerRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamPlayerRequestBody(TeamPlayerRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.leadershipRole = template.getIsLeadershipRole();
            this.position = template.getPosition();
        }
    }


    /**
     * Return whether it is a TeamLeadership role or a TeamMembership role.
     *
     * @return flag
     */
    public boolean getIsLeadershipRole()
    {
        return leadershipRole;
    }


    /**
     * Set up whether it is a TeamLeadership role or a TeamMembership role.
     *
     * @param leadershipRole flag
     */
    public void setIsLeadershipRole(boolean leadershipRole)
    {
        this.leadershipRole = leadershipRole;
    }


    /**
     * Return the position description for this role.
     *
     * @return name
     */
    public String getPosition()
    {
        return position;
    }


    /**
     * Set up the position description for this role.
     *
     * @param position name
     */
    public void setPosition(String position)
    {
        this.position = position;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TeamPlayerRequestBody{" +
                       "leadershipRole=" + leadershipRole +
                       ", position='" + position + '\'' +
                       ", isLeadershipRole=" + getIsLeadershipRole() +
                       ", externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        TeamPlayerRequestBody that = (TeamPlayerRequestBody) objectToCompare;
        return leadershipRole == that.leadershipRole &&
                       Objects.equals(position, that.position);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), leadershipRole, position);
    }
}
