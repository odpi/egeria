/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerInstanceHistory documents the start and end of a server instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerInstanceHistory implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private Date startTime = null;
    private Date endTime   = null;


    /**
     * Default constructor for Jackson
     */
    public OMAGServerInstanceHistory()
    {
    }


    /**
     * Constructor used to create the history.
     *
     * @param startTime time the server instance started
     * @param endTime time the server instance ended.
     */
    public OMAGServerInstanceHistory(Date startTime, Date endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
    }


    /**
     * Return the time that this instance of the server started.
     *
     * @return date/time object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that this instance of the server started.
     *
     * @param startTime date/time object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the time when this instance of the server ended.
     *
     * @return date/time object
     */
    public Date getEndTime()
    {
        return endTime;
    }


    /**
     * Set up the time when this instance of the server ended.
     *
     * @param endTime date/time object
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    /**
     * JSON like toString method
     *
     * @return string representing the local variables
     */
    @Override
    public String toString()
    {
        return "OMAGServerInstanceHistory{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
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
        OMAGServerInstanceHistory that = (OMAGServerInstanceHistory) objectToCompare;
        return Objects.equals(startTime, that.startTime) &&
                       Objects.equals(endTime, that.endTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(startTime, endTime);
    }
}
