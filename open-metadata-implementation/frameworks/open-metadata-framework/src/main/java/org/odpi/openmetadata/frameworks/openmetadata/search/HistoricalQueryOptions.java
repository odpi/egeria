/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * QueryOptions carries the date/time for a query along with other common search parameters.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HistoricalQueryOptions extends PagingOptions
{
    private Date    fromTime    = null;
    private Date    toTime      = null;
    private boolean oldestFirst = false;


    /**
     * Default constructor
     */
    public HistoricalQueryOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public HistoricalQueryOptions(HistoricalQueryOptions template)
    {
        super(template);

        if (template != null)
        {
            fromTime    = template.getFromTime();
            toTime      = template.getToTime();
            oldestFirst = template.getOldestFirst();
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
     * Return whether this request is to return the oldest or newest version first.
     *
     * @return flag
     */
    public boolean getOldestFirst()
    {
        return oldestFirst;
    }


    /**
     * Set up whether this request is to return the oldest or newest version first.
     *
     * @param oldestFirst flag
     */
    public void setOldestFirst(boolean oldestFirst)
    {
        this.oldestFirst = oldestFirst;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "HistoricalQueryOptions{" +
                "fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", oldestFirst=" + oldestFirst +
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
        HistoricalQueryOptions that = (HistoricalQueryOptions) objectToCompare;
        return oldestFirst == that.oldestFirst &&
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
        return Objects.hash(super.hashCode(), fromTime, toTime, oldestFirst);
    }
}
