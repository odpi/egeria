/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NotificationTypeProperties describes an event or situation that needs to be monitored, and acted upon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationTypeProperties extends GovernanceControlProperties
{
    private Date startDate             = null;
    private long refreshTimeInterval   = 0L;
    private Date connectorShutdownDate = null;



    /**
     * Default constructor does nothing.
     */
    public NotificationTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.NOTIFICATION_TYPE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NotificationTypeProperties(NotificationTypeProperties template)
    {
        if (template != null)
        {
            startDate             = template.getStartDate();
            refreshTimeInterval   = template.getRefreshTimeInterval();
            connectorShutdownDate = template.getConnectorShutdownDate();
        }
    }


    /**
     * Return the date/time that the monitor can start.  Null means that it can start immediately.
     *
     * @return date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time that the monitor can start.  Null means that it can start immediately.
     *
     * @param startDate date
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the number of minutes between each call to the monitor to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh call is made explicitly.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the number of minutes between each call to the monitor to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh request is made explicitly.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    public void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return the date/time that the monitor should stop running.
     *
     * @return date
     */
    public Date getConnectorShutdownDate()
    {
        return connectorShutdownDate;
    }


    /**
     * Set up the date/time that the monitor should stop running.
     *
     * @param connectorShutdownDate date
     */
    public void setConnectorShutdownDate(Date connectorShutdownDate)
    {
        this.connectorShutdownDate = connectorShutdownDate;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "NotificationTypeProperties{" +
                "startDate=" + startDate +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", connectorShutdownDate=" + connectorShutdownDate +
                "} " + super.toString();
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        NotificationTypeProperties that = (NotificationTypeProperties) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(connectorShutdownDate, that.connectorShutdownDate);
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), startDate, refreshTimeInterval, connectorShutdownDate);
    }
}
