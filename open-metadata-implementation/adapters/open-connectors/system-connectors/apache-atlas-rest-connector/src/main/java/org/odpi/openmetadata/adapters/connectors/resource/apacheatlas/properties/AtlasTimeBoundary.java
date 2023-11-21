/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTimeBoundary describes the effective time for an Atlas entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasTimeBoundary
{
    public static final String TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private String startTime;
    private String endTime;
    private String timeZone; // null for local-time; or a valid ID for TimeZone.getTimeZone(id)


    public AtlasTimeBoundary()
    {
    }


    public String getStartTime()
    {
        return startTime;
    }


    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }


    public String getEndTime()
    {
        return endTime;
    }


    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }


    public String getTimeZone()
    {
        return timeZone;
    }


    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }


    @Override
    public String toString()
    {
        return "AtlasTimeBoundary{" +
                       "startTime='" + startTime + '\'' +
                       ", endTime='" + endTime + '\'' +
                       ", timeZone='" + timeZone + '\'' +
                       '}';
    }
}
