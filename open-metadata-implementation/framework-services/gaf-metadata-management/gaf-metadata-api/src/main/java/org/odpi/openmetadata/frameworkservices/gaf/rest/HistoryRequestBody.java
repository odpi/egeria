/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * HistoryRequestBody describes the start and stop time of the historical query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HistoryRequestBody extends EffectiveTimeRequestBody
{
    private Date fromTime = null;
    private Date toTime   = null;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Default constructor
     */
    public HistoryRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public HistoryRequestBody(HistoryRequestBody template)
    {
        super(template);

        if (template != null)
        {
            fromTime = template.getFromTime();
            toTime = template.getToTime();
            forLineage = template.getForLineage();
            forDuplicateProcessing = template.getForDuplicateProcessing();
        }
    }


    /**
     * Return the starting date/time to use for the query.
     *
     * @return date object
     */
    public Date getFromTime()
    {
        return fromTime;
    }


    /**
     * Set up the starting date/time to use for the query.
     *
     * @param fromTime date object
     */
    public void setFromTime(Date fromTime)
    {
        this.fromTime = fromTime;
    }

    /**
     * Return the ending date/time to use for the query.
     *
     * @return date object
     */
    public Date getToTime()
    {
        return toTime;
    }

    /**
     * Set up the engine date/time to use for the query.
     *
     * @param toTime date object
     */
    public void setToTime(Date toTime)
    {
        this.toTime = toTime;
    }




    /**
     * Return whether this request is to update lineage memento elements.
     *
     * @return flag
     */
    public boolean getForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether this request is to update lineage memento elements.
     *
     * @param forLineage flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return whether this request is updating an element as part of a deduplication exercise.
     *
     * @return flag
     */
    public boolean getForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether this request is updating an element as part of a deduplication exercise.
     *
     * @param forDuplicateProcessing flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "HistoryRequestBody{" +
                "fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", forLineage=" + forLineage +
                ", forDuplicateProcessing=" + forDuplicateProcessing +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        HistoryRequestBody that = (HistoryRequestBody) objectToCompare;
        return forLineage == that.forLineage &&
                forDuplicateProcessing == that.forDuplicateProcessing &&
                Objects.equals(fromTime, that.fromTime) &&
               Objects.equals(toTime, that.toTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), forLineage, forDuplicateProcessing, fromTime, toTime);
    }
}
