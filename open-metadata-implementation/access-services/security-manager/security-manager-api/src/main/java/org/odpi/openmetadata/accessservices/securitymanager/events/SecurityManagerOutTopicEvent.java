/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.securitymanager.metadataelements.ElementHeader;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityManagerEventHeader provides a common base for all events from the Security Manager OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SecurityManagerOutTopicEvent implements java.io.Serializable
{
    private long                 eventVersionId = 1L;
    private SecurityManagerEventType eventType      = null;
    private ElementHeader        elementHeader  = null;


    /**
     * Default Constructor
     */
    public SecurityManagerOutTopicEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityManagerOutTopicEvent(SecurityManagerOutTopicEvent template)
    {
        if (template != null)
        {
            eventVersionId = template.getEventVersionId();
            eventType = template.getEventType();
            elementHeader = template.getElementHeader();
        }
    }


    /**
     * Return the event version id.
     *
     * @return long
     */
    public long getEventVersionId()
    {
        return eventVersionId;
    }


    /**
     * Set up the event version id.
     *
     * @param eventVersionId long
     */
    public void setEventVersionId(long eventVersionId)
    {
        /* provided for Jackson */
    }


    /**
     * Return the event type.
     *
     * @return the event type enum
     */
    public SecurityManagerEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the event type.
     *
     * @param eventType event type enum
     */
    public void setEventType(SecurityManagerEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return details of the subject of the event.
     *
     * @return element header
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up details of the subject of the event.
     *
     * @param elementHeader element header
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecurityManagerOutTopicEvent{" +
                "eventVersionId=" + eventVersionId +
                ", eventType=" + eventType +
                ", elementHeader=" + elementHeader +
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
        SecurityManagerOutTopicEvent that = (SecurityManagerOutTopicEvent) objectToCompare;
        return eventVersionId == that.eventVersionId &&
                eventType == that.eventType &&
                Objects.equals(elementHeader, that.elementHeader);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(eventVersionId, eventType, elementHeader);
    }
}
