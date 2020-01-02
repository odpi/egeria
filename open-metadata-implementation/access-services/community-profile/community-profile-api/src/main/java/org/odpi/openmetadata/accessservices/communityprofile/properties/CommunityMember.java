/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityMember describes an individual who is a member of a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityMember extends PersonalRole
{
    private static final long    serialVersionUID = 1L;

    private CommunityMembershipType   membershipType = null;
    private String                    communityGUID = null;


    /**
     * Default constructor
     */
    public CommunityMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityMember(CommunityMember template)
    {
        super(template);

        if (template != null)
        {
            membershipType = template.getMembershipType();
            communityGUID = template.getCommunityGUID();
        }
    }


    /**
     * Return the membership type.
     *
     * @return membership type enum
     */
    public CommunityMembershipType getMembershipType()
    {
        return membershipType;
    }


    /**
     * Set up the membership type.
     *
     * @param membershipType membership type enum
     */
    public void setMembershipType(CommunityMembershipType membershipType)
    {
        this.membershipType = membershipType;
    }


    /**
     * Return the unique identifier (guid) for the community.
     *
     * @return string guid
     */
    public String getCommunityGUID()
    {
        return communityGUID;
    }


    /**
     * Set up the unique identifier (guid) for the community.
     *
     * @param communityGUID string guid
     */
    public void setCommunityGUID(String communityGUID)
    {
        this.communityGUID = communityGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityMember{" +
                "membershipType=" + membershipType +
                ", communityGUID='" + communityGUID + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", originId='" + getOriginId() + '\'' +
                ", originName='" + getOriginName() + '\'' +
                ", originType='" + getOriginType() + '\'' +
                ", originLicense='" + getOriginLicense() + '\'' +
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
        CommunityMember that = (CommunityMember) objectToCompare;
        return getMembershipType() == that.getMembershipType() &&
                Objects.equals(getCommunityGUID(), that.getCommunityGUID());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMembershipType(), getCommunityGUID());
    }
}
