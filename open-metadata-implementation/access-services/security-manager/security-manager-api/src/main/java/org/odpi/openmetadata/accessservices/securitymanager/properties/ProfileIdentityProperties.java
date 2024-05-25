/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileIdentityElement contains the properties and header for a relationship between a profile and a user identity retrieved
 * from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileIdentityProperties
{
    private String roleTypeName = null;
    private String roleGUID     = null;
    private String description  = null;


    /**
     * Default constructor
     */
    public ProfileIdentityProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileIdentityProperties(ProfileIdentityProperties template)
    {
        if (template != null)
        {
            roleTypeName = template.getRoleTypeName();
            roleGUID = template.getRoleGUID();
            description = template.getDescription();
        }
    }


    /**
     * Return the type of the role.
     *
     * @return name
     */
    public String getRoleTypeName()
    {
        return roleTypeName;
    }


    /**
     * Set up the type of the role.
     *
     * @param roleTypeName name
     */
    public void setRoleTypeName(String roleTypeName)
    {
        this.roleTypeName = roleTypeName;
    }


    /**
     * Return the instance of the role.
     *
     * @return guid
     */
    public String getRoleGUID()
    {
        return roleGUID;
    }


    /**
     * Set up the instance of the role.
     *
     * @param roleGUID guid
     */
    public void setRoleGUID(String roleGUID)
    {
        this.roleGUID = roleGUID;
    }


    /**
     * Return the description of the user identity with respect to the individual.
     *
     * @return value string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the user identity with respect to the individual.
     *
     * @param description value string
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
        return "ProfileIdentityProperties{" +
                       "roleTypeName='" + roleTypeName + '\'' +
                       ", roleGUID='" + roleGUID + '\'' +
                       ", description='" + description + '\'' +
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
        ProfileIdentityProperties that = (ProfileIdentityProperties) objectToCompare;
        return Objects.equals(roleTypeName, that.roleTypeName) &&
                       Objects.equals(roleGUID, that.roleGUID) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(roleTypeName, roleGUID, description);
    }
}
