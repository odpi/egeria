/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectTeamProperties provides a details of the purpose (and privileges) of the membership role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectTeamProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    String teamRole = null;

    /**
     * Default constructor
     */
    public ProjectTeamProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectTeamProperties(ProjectTeamProperties template)
    {
        super(template);

        if (template != null)
        {
            this.teamRole = template.getTeamRole();
        }
    }


    /**
     * Return the membership type.
     *
     * @return membership type
     */
    public String getTeamRole()
    {
        return teamRole;
    }


    /**
     * Set up the membership type.
     *
     * @param teamRole membership type
     */
    public void setTeamRole(String teamRole)
    {
        this.teamRole = teamRole;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectTeamProperties{" +
                       "membershipType=" + teamRole +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ProjectTeamProperties that = (ProjectTeamProperties) objectToCompare;
        return Objects.equals(teamRole, that.teamRole);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), teamRole);
    }
}
