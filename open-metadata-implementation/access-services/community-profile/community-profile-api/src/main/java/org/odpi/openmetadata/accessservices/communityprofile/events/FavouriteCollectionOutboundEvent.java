/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FavouriteCollectionOutboundEvent describes changes to an individual's favourite collections.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FavouriteCollectionOutboundEvent extends PersonalProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    private String  userId = null;
    private String  memberGUID = null;
    private String  memberType = null;

    /**
     * Default constructor
     */
    public FavouriteCollectionOutboundEvent()
    {
        super();
    }


    /**
     * Event publisher constructor.
     *
     * @param type type of event
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     */
    public FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType  type,
                                            PersonalProfile                    bean,
                                            String                             userId,
                                            String                             memberGUID,
                                            String                             memberTypeName)
    {
        super();

        super.setEventType(type);
        super.setPersonalProfile(bean);
        this.setUserId(userId);
        this.setMemberGUID(memberGUID);
        this.setMemberType(memberTypeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FavouriteCollectionOutboundEvent(FavouriteCollectionOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            userId = template.getUserId();
            memberGUID = template.getMemberGUID();
            memberType = template.getMemberType();
        }
    }


    /**
     * Return user identity of associated user.
     *
     * @return user id
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the user id of the associated user.
     *
     * @param userId user id
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return the unique identifier for the member that changed.
     *
     * @return guid
     */
    public String getMemberGUID()
    {
        return memberGUID;
    }


    /**
     * Set up the unique identifier for the member that changed.
     *
     * @param memberGUID guid
     */
    public void setMemberGUID(String memberGUID)
    {
        this.memberGUID = memberGUID;
    }


    /**
     * Return the type name for the member that changed.
     *
     * @return guid
     */
    public String getMemberType()
    {
        return memberType;
    }

    /**
     * Set up the type name for the member that changed.
     *
     * @param memberTypeName name of type
     */
    public void setMemberType(String memberTypeName)
    {
        this.memberType = memberTypeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "FavouriteCollectionOutboundEvent{" +
                       "userId='" + userId + '\'' +
                       ", memberGUID='" + memberGUID + '\'' +
                       ", memberType='" + memberType + '\'' +
                       ", personalProfile=" + getPersonalProfile() +
                       ", eventType=" + getEventType() +
                       ", eventVersionId=" + getEventVersionId() +
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
        FavouriteCollectionOutboundEvent that = (FavouriteCollectionOutboundEvent) objectToCompare;
        return Objects.equals(getUserId(), that.getUserId()) &&
                       Objects.equals(getMemberGUID(), that.getMemberGUID()) &&
                       Objects.equals(getMemberType(), that.getMemberType());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserId(), getMemberGUID(), getMemberType());
    }
}
