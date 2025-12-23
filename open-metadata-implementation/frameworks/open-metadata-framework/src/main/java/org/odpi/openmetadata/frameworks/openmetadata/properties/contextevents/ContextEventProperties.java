/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContextEventProperties holds properties that are used for displaying details of
 * context event.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContextEventProperties extends ReferenceableProperties
{
    private String eventEffect            = null;
    private Date   plannedStartDate       = null;
    private Date   actualStartDate        = null;
    private long   plannedDuration        = 0L;
    private long   actualDuration         = 0L;
    private long   repeatInterval         = 0L;
    private Date   plannedCompletionDate  = null;
    private Date   actualCompletionDate   = null;
    private Date   referenceEffectiveFrom = null;
    private Date   referenceEffectiveTo   = null;


    /**
     * Default constructor
     */
    public ContextEventProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTEXT_EVENT.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ContextEventProperties(ContextEventProperties template)
    {
        super(template);

        if (template != null)
        {
            eventEffect            = template.getEventEffect();
            plannedStartDate       = template.getPlannedStartDate();
            actualStartDate        = template.getActualStartDate();
            plannedDuration        = template.getPlannedDuration();
            actualDuration         = template.getActualDuration();
            repeatInterval         = template.getRepeatInterval();
            plannedCompletionDate  = template.getPlannedCompletionDate();
            actualCompletionDate   = template.getActualCompletionDate();
            referenceEffectiveFrom = template.getReferenceEffectiveFrom();
            referenceEffectiveTo   = template.getReferenceEffectiveTo();
        }
    }


    /**
     * Set up the effect of the event.
     *
     * @return string version name
     */
    public String getEventEffect()
    {
        return eventEffect;
    }


    /**
     * Set up the effect of the event.
     *
     * @param eventEffect string version name
     */
    public void setEventEffect(String eventEffect)
    {
        this.eventEffect = eventEffect;
    }


    /**
     * Return the planned start date for the event.
     *
     * @return date
     */
    public Date getPlannedStartDate()
    {
        return plannedStartDate;
    }


    /**
     * Set up the planned start date for the event.
     *
     * @param plannedStartDate date
     */
    public void setPlannedStartDate(Date plannedStartDate)
    {
        this.plannedStartDate = plannedStartDate;
    }


    /**
     * Return the actual start date for the event.
     *
     * @return date
     */
    public Date getActualStartDate()
    {
        return actualStartDate;
    }


    /**
     * Set up the actual start date for the event.
     *
     * @param actualStartDate date
     */
    public void setActualStartDate(Date actualStartDate)
    {
        this.actualStartDate = actualStartDate;
    }


    /**
     * Return the number of milliseconds that the event is due to run for.
     *
     * @return long
     */
    public long getPlannedDuration()
    {
        return plannedDuration;
    }


    /**
     * Set up the number of milliseconds that the event is due to run for.
     *
     * @param plannedDuration long
     */
    public void setPlannedDuration(long plannedDuration)
    {
        this.plannedDuration = plannedDuration;
    }


    /**
     * Return the actual duration of the event in milliseconds.
     *
     * @return long
     */
    public long getActualDuration()
    {
        return actualDuration;
    }


    /**
     * Set up  the actual duration of the event in milliseconds.
     *
     * @param actualDuration long
     */
    public void setActualDuration(long actualDuration)
    {
        this.actualDuration = actualDuration;
    }


    /**
     * Return the number of milliseconds between repeated occurrences of the event.
     *
     * @return long
     */
    public long getRepeatInterval()
    {
        return repeatInterval;
    }


    /**
     * Set up the number of milliseconds between repeated occurrences of the event.
     *
     * @param repeatInterval long
     */
    public void setRepeatInterval(long repeatInterval)
    {
        this.repeatInterval = repeatInterval;
    }


    /**
     * Return the expected completion date for this event.
     *
     * @return date
     */
    public Date getPlannedCompletionDate()
    {
        return plannedCompletionDate;
    }


    /**
     * Set up the expected completion date for this event.
     *
     * @param plannedCompletionDate date
     */
    public void setPlannedCompletionDate(Date plannedCompletionDate)
    {
        this.plannedCompletionDate = plannedCompletionDate;
    }


    /**
     * Return the actual date that the event ended.
     *
     * @return date
     */
    public Date getActualCompletionDate()
    {
        return actualCompletionDate;
    }


    /**
     * Set up the actual date that the event ended.
     *
     * @param actualCompletionDate date
     */
    public void setActualCompletionDate(Date actualCompletionDate)
    {
        this.actualCompletionDate = actualCompletionDate;
    }


    /**
     * Return the date to use as an effective from value in elements tied to the context event.
     *
     * @return date
     */
    public Date getReferenceEffectiveFrom()
    {
        return referenceEffectiveFrom;
    }


    /**
     * set up  the date to use as an effective from value in elements tied to the context event.
     *
     * @param referenceEffectiveFrom date
     */
    public void setReferenceEffectiveFrom(Date referenceEffectiveFrom)
    {
        this.referenceEffectiveFrom = referenceEffectiveFrom;
    }


    /**
     * Return the date to use as an effective to value in elements tied to the context event.
     *
     * @return date
     */
    public Date getReferenceEffectiveTo()
    {
        return referenceEffectiveTo;
    }


    /**
     * Set up  the date to use as an effective to value in elements tied to the context event.
     *
     * @param referenceEffectiveTo date
     */
    public void setReferenceEffectiveTo(Date referenceEffectiveTo)
    {
        this.referenceEffectiveTo = referenceEffectiveTo;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ContextEventProperties{" +
                "eventEffect='" + eventEffect + '\'' +
                ", plannedStartDate=" + plannedStartDate +
                ", actualStartDate=" + actualStartDate +
                ", plannedDuration=" + plannedDuration +
                ", actualDuration=" + actualDuration +
                ", repeatInterval=" + repeatInterval +
                ", plannedCompletionDate=" + plannedCompletionDate +
                ", actualCompletionDate=" + actualCompletionDate +
                ", referenceEffectiveFrom=" + referenceEffectiveFrom +
                ", referenceEffectiveTo=" + referenceEffectiveTo +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ContextEventProperties that = (ContextEventProperties) objectToCompare;
        return plannedDuration == that.plannedDuration
                && actualDuration == that.actualDuration
                && repeatInterval == that.repeatInterval
                && Objects.equals(eventEffect, that.eventEffect)
                && Objects.equals(plannedStartDate, that.plannedStartDate)
                && Objects.equals(actualStartDate, that.actualStartDate)
                && Objects.equals(plannedCompletionDate, that.plannedCompletionDate)
                && Objects.equals(actualCompletionDate, that.actualCompletionDate)
                && Objects.equals(referenceEffectiveFrom, that.referenceEffectiveFrom)
                && Objects.equals(referenceEffectiveTo, that.referenceEffectiveTo);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), eventEffect, plannedStartDate,
                            actualStartDate, plannedDuration, actualDuration, repeatInterval, plannedCompletionDate,
                            actualCompletionDate, referenceEffectiveFrom, referenceEffectiveTo);
    }
}