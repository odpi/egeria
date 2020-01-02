/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UserIdentityOutboundEvent defines the payload of the events that report on changes to the userIdentity entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserIdentityOutboundEvent extends CommunityProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    private UserIdentity userIdentity = null;

    /**
     * Default constructor
     */
    public UserIdentityOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UserIdentityOutboundEvent(UserIdentityOutboundEvent template)
    {
        super(template);

        if (template != null)
        {
            userIdentity = template.getUserIdentity();
        }
    }


    /**
     * Return the user identity that is the subject of this event.
     *
     * @return UserIdentity bean
     */
    public UserIdentity getUserIdentity()
    {
        return userIdentity;
    }


    /**
     * Set up the user identity that is the subject of this event.
     *
     * @param userIdentity UserIdentify bean
     */
    public void setUserIdentity(UserIdentity userIdentity)
    {
        this.userIdentity = userIdentity;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UserIdentityOutboundEvent{" +
                       "userIdentity=" + userIdentity +
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
        UserIdentityOutboundEvent that = (UserIdentityOutboundEvent) objectToCompare;
        return Objects.equals(getUserIdentity(), that.getUserIdentity());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserIdentity());
    }
}
