/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityMembershipProperties provides a details of the purpose (and privileges) of the membership role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityMembershipProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    CommunityMembershipType membershipType = null;

    /**
     * Default constructor
     */
    public CommunityMembershipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityMembershipProperties(CommunityMembershipProperties template)
    {
        super(template);

        if (template != null)
        {
            this.membershipType = template.getMembershipType();
        }
    }


    /**
     * Return the membership type.
     *
     * @return membership type
     */
    public CommunityMembershipType getMembershipType()
    {
        return membershipType;
    }


    /**
     * Set up the membership type.
     *
     * @param membershipType membership type
     */
    public void setMembershipType(CommunityMembershipType membershipType)
    {
        this.membershipType = membershipType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityMembershipProperties{" +
                       "membershipType=" + membershipType +
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
        CommunityMembershipProperties that = (CommunityMembershipProperties) objectToCompare;
        return Objects.equals(membershipType, that.membershipType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), membershipType);
    }
}
