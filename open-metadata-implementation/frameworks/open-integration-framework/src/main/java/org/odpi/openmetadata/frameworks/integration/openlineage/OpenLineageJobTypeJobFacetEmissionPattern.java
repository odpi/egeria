/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents how and what a job emits in its OpenLineage events.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobTypeJobFacetEmissionPattern
{
    private String eventTrigger = null;
    private String eventContentMode = null;
    private Long   windowDuration = null;


    /**
     * Default constructor
     */
    public OpenLineageJobTypeJobFacetEmissionPattern()
    {
    }


    /**
     * Return the what triggers event emission, for example TASK, CHECKPOINT or WINDOW.
     *
     * @return string
     */
    public String getEventTrigger()
    {
        return eventTrigger;
    }


    /**
     * Set up the what triggers event emission, for example TASK, CHECKPOINT or WINDOW.
     *
     * @param eventTrigger string
     */
    public void setEventTrigger(String eventTrigger)
    {
        this.eventTrigger = eventTrigger;
    }


    /**
     * Return the whether events carry cumulative or incremental lineage.
     *
     * @return string
     */
    public String getEventContentMode()
    {
        return eventContentMode;
    }


    /**
     * Set up the whether events carry cumulative or incremental lineage.
     *
     * @param eventContentMode string
     */
    public void setEventContentMode(String eventContentMode)
    {
        this.eventContentMode = eventContentMode;
    }


    /**
     * Return the duration of the emission window in seconds, when the trigger is WINDOW.
     *
     * @return long
     */
    public Long getWindowDuration()
    {
        return windowDuration;
    }


    /**
     * Set up the duration of the emission window in seconds, when the trigger is WINDOW.
     *
     * @param windowDuration long
     */
    public void setWindowDuration(Long windowDuration)
    {
        this.windowDuration = windowDuration;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobTypeJobFacetEmissionPattern{" +
                       "eventTrigger='" + eventTrigger + '\'' +
                       ", eventContentMode='" + eventContentMode + '\'' +
                       ", windowDuration=" + windowDuration +
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
        OpenLineageJobTypeJobFacetEmissionPattern that = (OpenLineageJobTypeJobFacetEmissionPattern) objectToCompare;
        return Objects.equals(eventTrigger, that.eventTrigger) &&
                       Objects.equals(eventContentMode, that.eventContentMode) &&
                       Objects.equals(windowDuration, that.windowDuration);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(eventTrigger, eventContentMode, windowDuration);
    }
}
