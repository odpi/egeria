/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogGovernanceServiceEvent holds a formatted event for a watchdog governance action service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WatchdogGovernanceServiceEvent extends GovernanceEngineEvent
{
    private static final long serialVersionUID = 1L;

    private WatchdogGovernanceEvent watchdogGovernanceEvent = null;

    /**
     * Default constructor
     */
    public WatchdogGovernanceServiceEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public WatchdogGovernanceServiceEvent(WatchdogGovernanceServiceEvent template)
    {
        super(template);

        if (template != null)
        {
            watchdogGovernanceEvent = template.getWatchdogGovernanceEvent();
        }
    }


    /**
     * Return the event for running Watchdog Governance Action Services.
     *
     * @return GAF event structure
     */
    public WatchdogGovernanceEvent getWatchdogGovernanceEvent()
    {
        return watchdogGovernanceEvent;
    }


    /**
     * Set up the event for running Watchdog Governance Action Services.
     *
     * @param watchdogGovernanceEvent GAF event structure
     */
    public void settWatchdogGovernanceEvent(WatchdogGovernanceEvent watchdogGovernanceEvent)
    {
        this.watchdogGovernanceEvent = watchdogGovernanceEvent;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "WatchdogGovernanceServiceEvent{" +
                       "watchdogGovernanceEvent=" + watchdogGovernanceEvent +
                       ", eventVersionId=" + getEventVersionId() +
                       ", eventType=" + getEventType() +
                       ", governanceEngineGUID='" + getGovernanceEngineGUID() + '\'' +
                       ", governanceEngineName='" + getGovernanceEngineName() + '\'' +
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
        WatchdogGovernanceServiceEvent that = (WatchdogGovernanceServiceEvent) objectToCompare;
        return Objects.equals(watchdogGovernanceEvent, that.watchdogGovernanceEvent);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), watchdogGovernanceEvent);
    }
}
