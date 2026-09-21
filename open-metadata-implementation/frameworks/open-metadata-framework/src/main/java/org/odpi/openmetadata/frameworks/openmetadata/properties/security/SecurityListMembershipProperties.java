/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityListMembershipProperties holds the list of names that the attached user identity is a member of.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityListMembershipProperties extends ClassificationBeanProperties
{
    private List<String> securityGroups = null;
    private List<String> securityRoles  = null;


    /**
     * Default constructor
     */
    public SecurityListMembershipProperties()
    {
        super();
        super.typeName = OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityListMembershipProperties(SecurityListMembershipProperties template)
    {
        super(template);

        if (template != null)
        {
            this.securityGroups = template.getSecurityGroups();
            this.securityRoles  = template.getSecurityRoles();
        }
    }


    /**
     * Return the list of security group  names for the user.
     *
     * @return list of  names
     */
    public List<String> getSecurityGroups()
    {
        return securityGroups;
    }


    /**
     * Set up the list of security group names for the user.
     *
     * @param securityGroups list of  names
     */
    public void setSecurityGroups(List<String> securityGroups)
    {
        this.securityGroups = securityGroups;
    }



    /**
     * Return the list of security role names for the user.
     *
     * @return list of  names
     */
    public List<String> getSecurityRoles()
    {
        return securityRoles;
    }


    /**
     * Set up the list of security role names for the user.
     *
     * @param securityRoles list of  names
     */
    public void setSecurityRoles(List<String> securityRoles)
    {
        this.securityRoles = securityRoles;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecurityListMembershipProperties{" +
                "securityGroups=" + securityGroups +
                ", securityRoles=" + securityRoles +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SecurityListMembershipProperties that = (SecurityListMembershipProperties) objectToCompare;
        return Objects.equals(securityGroups, that.securityGroups) &&
                Objects.equals(securityRoles, that.securityRoles);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), securityGroups, securityRoles);
    }
}
