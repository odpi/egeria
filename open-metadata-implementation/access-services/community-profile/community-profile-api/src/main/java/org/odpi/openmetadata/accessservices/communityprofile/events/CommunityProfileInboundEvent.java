/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProfileInboundEvent describes the structure of the events received by the Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class CommunityProfileInboundEvent extends CommunityProfileEventHeader
{
    private static final long    serialVersionUID = 1L;

    private CommunityProfileInboundEventType eventType = null;

    /**
     * Default constructor
     */
    public CommunityProfileInboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityProfileInboundEvent(CommunityProfileInboundEvent template)
    {
        super(template);

        if (template != null)
        {
            this.eventType = template.getEventType();
        }
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public CommunityProfileInboundEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(CommunityProfileInboundEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityProfileInboundEvent{" +
                "eventType=" + eventType +
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
        if (!(objectToCompare instanceof CommunityProfileInboundEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CommunityProfileInboundEvent that = (CommunityProfileInboundEvent) objectToCompare;
        return getEventType() == that.getEventType();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEventType());
    }
}
