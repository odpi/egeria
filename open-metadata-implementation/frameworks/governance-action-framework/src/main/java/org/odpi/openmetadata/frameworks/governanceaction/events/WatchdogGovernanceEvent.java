/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogGovernanceEvent describes the structure of the events passed to the WatchdogGovernanceActionService.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = WatchdogMetadataElementEvent.class, name = "WatchdogMetadataElementEvent"),
                @JsonSubTypes.Type(value = WatchdogRelatedElementsEvent.class, name = "WatchdogRelatedElementsEvent"),
        })
public abstract class WatchdogGovernanceEvent implements Serializable
{
    private static final long      serialVersionUID = 1L;

    private WatchdogEventType eventType = null;


    /**
     * Default constructor
     */
    public WatchdogGovernanceEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WatchdogGovernanceEvent(WatchdogGovernanceEvent template)
    {
        if (template != null)
        {
            this.eventType = template.getEventType();
        }
    }


    /**
     * Return the type of event (used to get the class to cast to).
     *
     * @return event type
     */
    public WatchdogEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the event type. This should tie up with the subclass of the event.
     *
     * @param eventType event type
     */
    public void setEventType(WatchdogEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogGovernanceEvent{" +
                       "eventType=" + eventType +
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
        WatchdogGovernanceEvent that = (WatchdogGovernanceEvent) objectToCompare;
        return eventType == that.eventType;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(eventType);
    }
}
