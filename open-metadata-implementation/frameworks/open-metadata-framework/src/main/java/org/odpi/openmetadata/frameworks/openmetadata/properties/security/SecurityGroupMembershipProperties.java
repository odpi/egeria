/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityGroupMembershipProperties holds the list of distinguished names that the attached user identity is a member of.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityGroupMembershipProperties extends ClassificationProperties
{
    private List<String> groups = null;


    /**
     * Default constructor
     */
    public SecurityGroupMembershipProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityGroupMembershipProperties(SecurityGroupMembershipProperties template)
    {
        super(template);

        if (template != null)
        {
            this.groups = template.getGroups();
        }
    }


    /**
     * Return the list of security group distinguished names for the user.
     *
     * @return list of distinguished names
     */
    public List<String> getGroups()
    {
        return groups;
    }


    /**
     * Set up the list of security group distinguished names for the user.
     *
     * @param groups list of distinguished names
     */
    public void setGroups(List<String> groups)
    {
        this.groups = groups;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecurityGroupMembershipProperties{" +
                "groups=" + groups +
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
        SecurityGroupMembershipProperties that = (SecurityGroupMembershipProperties) objectToCompare;
        return Objects.equals(groups, that.groups);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), groups);
    }
}
