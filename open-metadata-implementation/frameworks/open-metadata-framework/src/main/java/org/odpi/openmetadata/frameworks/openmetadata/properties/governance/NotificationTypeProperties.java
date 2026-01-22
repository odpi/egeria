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
 * NotificationTypeProperties describes an event or situation that needs to be monitored and acted upon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationTypeProperties extends GovernanceControlProperties
{
    private Date    plannedStartDate               = null;
    private boolean multipleNotificationsPermitted = true;
    private long    minimumNotificationInterval    = 0L;
    private long    notificationInterval           = 0L;
    private Date    nextScheduledNotification      = null;
    private Date    plannedCompletionDate          = null;


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
            plannedStartDate      = template.getPlannedStartDate();
            multipleNotificationsPermitted = template.getMultipleNotificationsPermitted();
            minimumNotificationInterval = template.getMinimumNotificationInterval();
            notificationInterval  = template.getNotificationInterval();
            nextScheduledNotification = template.getNextScheduledNotification();
            plannedCompletionDate = template.getPlannedCompletionDate();
        }
    }


    /**
     * Return the date/time that the monitor can start.  Null means that it can start immediately.
     *
     * @return date
     */
    public Date getPlannedStartDate()
    {
        return plannedStartDate;
    }


    /**
     * Set up the date/time that the monitor can start.  Null means that it can start immediately.
     *
     * @param plannedStartDate date
     */
    public void setPlannedStartDate(Date plannedStartDate)
    {
        this.plannedStartDate = plannedStartDate;
    }


    /**
     * Return whether multiple notifications are permitted.  If false, only one notification will be sent out
     * to a subscriber.
     *
     * @return boolean flag
     */
    public boolean getMultipleNotificationsPermitted()
    {
        return multipleNotificationsPermitted;
    }


    /**
     * Set up whether multiple notifications are permitted.  If false, only one notification will be sent out
     * to a subscriber.
     *
     * @param multipleNotificationsPermitted boolean flag
     */
    public void setMultipleNotificationsPermitted(boolean multipleNotificationsPermitted)
    {
        this.multipleNotificationsPermitted = multipleNotificationsPermitted;
    }


    /**
     * Return the minimum minutes between notifications.  If 0, notifications are sent out whenever the
     * appropriate condition is detected.
     *
     * @return minute count
     */
    public long getMinimumNotificationInterval()
    {
        return minimumNotificationInterval;
    }


    /**
     * Set up the minimum minutes between notifications.  If 0, notifications are sent out whenever the
     * appropriate condition is detected.
     *
     * @param minimumNotificationInterval minute count
     */
    public void setMinimumNotificationInterval(long minimumNotificationInterval)
    {
        this.minimumNotificationInterval = minimumNotificationInterval;
    }

    /**
     * Return the minutes between notifications.  If null, notifications are driven by other events,
     * such as a change to a monitored resource.
     *
     * @return minute count
     */
    public long getNotificationInterval()
    {
        return notificationInterval;
    }


    /**
     * Set up the minutes between notifications.  If null, notifications are driven by other events,
     * such as a change to a monitored resource.
     *
     * @param notificationInterval minute count
     */
    public void setNotificationInterval(long notificationInterval)
    {
        this.notificationInterval = notificationInterval;
    }


    /**
     * Return the date/time that the notifications should be sent out if they are on a fixed schedule.
     * If notificationInterval is 0, then this field is null.
     *
     * @return date
     */
    public Date getNextScheduledNotification()
    {
        return nextScheduledNotification;
    }

    /**
     * Set up the date/time that the notifications should be sent out if they are on a fixed schedule.
     * If notificationInterval is 0, then this field is null.
     *
     * @param nextScheduledNotification date
     */
    public void setNextScheduledNotification(Date nextScheduledNotification)
    {
        this.nextScheduledNotification = nextScheduledNotification;
    }


    /**
     * Return the date/time that the monitor should stop running.
     *
     * @return date
     */
    public Date getPlannedCompletionDate()
    {
        return plannedCompletionDate;
    }


    /**
     * Set up the date/time that the monitor should stop running.
     *
     * @param plannedCompletionDate date
     */
    public void setPlannedCompletionDate(Date plannedCompletionDate)
    {
        this.plannedCompletionDate = plannedCompletionDate;
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
                "plannedStartDate=" + getPlannedStartDate() +
                ", multipleNotificationsPermitted=" + getMultipleNotificationsPermitted() +
                ", minimumNotificationInterval=" + getMinimumNotificationInterval() +
                ", notificationInterval=" + getNotificationInterval() +
                ", nextScheduledNotification=" + getNextScheduledNotification() +
                ", plannedCompletionDate=" + getPlannedCompletionDate() +
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
        return minimumNotificationInterval == that.minimumNotificationInterval &&
                multipleNotificationsPermitted == that.multipleNotificationsPermitted &&
                notificationInterval == that.notificationInterval &&
                Objects.equals(plannedStartDate, that.plannedStartDate) &&
                Objects.equals(nextScheduledNotification, that.nextScheduledNotification) &&
                Objects.equals(plannedCompletionDate, that.plannedCompletionDate);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), plannedStartDate, multipleNotificationsPermitted, minimumNotificationInterval, notificationInterval, nextScheduledNotification, plannedCompletionDate);
    }
}
