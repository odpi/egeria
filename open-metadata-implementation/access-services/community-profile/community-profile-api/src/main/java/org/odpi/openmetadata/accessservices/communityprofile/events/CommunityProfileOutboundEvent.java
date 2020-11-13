/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProfileOutboundEvent describes the structure of the events emitted by the Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = CommunityOutboundEvent.class, name = "CommunityOutboundEvent"),
                      @JsonSubTypes.Type(value = PersonalProfileOutboundEvent.class, name = "PersonalProfileOutboundEvent"),
                      @JsonSubTypes.Type(value = UserIdentityOutboundEvent.class, name = "UserIdentityOutboundEvent")
              })
public abstract class CommunityProfileOutboundEvent extends CommunityProfileEventHeader
{
    private static final long    serialVersionUID = 1L;

    private CommunityProfileOutboundEventType eventType = null;

    /**
     * Default constructor
     */
    public CommunityProfileOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityProfileOutboundEvent(CommunityProfileOutboundEvent template)
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
    public CommunityProfileOutboundEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(CommunityProfileOutboundEventType eventType)
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
        return "CommunityProfileOutboundEvent{" +
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
        if (!(objectToCompare instanceof CommunityProfileOutboundEvent))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CommunityProfileOutboundEvent that = (CommunityProfileOutboundEvent) objectToCompare;
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
