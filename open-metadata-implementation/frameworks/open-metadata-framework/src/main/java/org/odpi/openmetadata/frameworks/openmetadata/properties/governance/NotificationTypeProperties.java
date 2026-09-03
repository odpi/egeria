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

    /**
     * @deprecated the schedule no longer belongs to the notification type - see the getter.
     */
    @Deprecated
    private long    notificationInterval           = 0L;
    private Date    lastNotification               = null;
    private Date    nextScheduledNotification      = null;
    private long    notificationCount              = 0L;
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
            plannedStartDate               = template.getPlannedStartDate();
            multipleNotificationsPermitted = template.getMultipleNotificationsPermitted();
            minimumNotificationInterval    = template.getMinimumNotificationInterval();
            notificationInterval           = template.getNotificationInterval();
            lastNotification               = template.getLastNotification();
            nextScheduledNotification      = template.getNextScheduledNotification();
            notificationCount              = template.getNotificationCount();
            plannedCompletionDate          = template.getPlannedCompletionDate();
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
     * Return how frequently a notification should be sent, in minutes.
     *
     * @return minute count
     *
     * @deprecated whether a notification may fire is assessed by the notification manager, from
     * minimumNotificationInterval and nextScheduledNotification together with each subscriber's own state.
     * The notification type no longer carries a schedule of its own.  The property is retained so that a
     * repository holding a value for it can still read its instances - see the deprecated attribute on the
     * NotificationType entity in OpenMetadataTypesArchive.
     */
    @Deprecated
    public long getNotificationInterval()
    {
        return notificationInterval;
    }


    /**
     * Set up how frequently a notification should be sent, in minutes.
     *
     * @param notificationInterval minute count
     *
     * @deprecated see {@link #getNotificationInterval()}.
     */
    @Deprecated
    public void setNotificationInterval(long notificationInterval)
    {
        this.notificationInterval = notificationInterval;
    }


    /**
     * Return the last notification time
     *
     * @return date
     */
    public Date getLastNotification()
    {
        return lastNotification;
    }


    /**
     * Set up the last notification time
     *
     * @param lastNotification date
     */
    public void setLastNotification(Date lastNotification)
    {
        this.lastNotification = lastNotification;
    }


    /**
     * Return the date/time that the notifications should be sent out if they are on a fixed schedule.
     * This is null when the notification type is not on a schedule.
     *
     * @return date
     */
    public Date getNextScheduledNotification()
    {
        return nextScheduledNotification;
    }


    /**
     * Set up the date/time that the notifications should be sent out if they are on a fixed schedule.
     * This is null when the notification type is not on a schedule.
     *
     * @param nextScheduledNotification date
     */
    public void setNextScheduledNotification(Date nextScheduledNotification)
    {
        this.nextScheduledNotification = nextScheduledNotification;
    }


    /**
     * Return the number of notifications that have been triggered.
     *
     * @return long
     */
    public long getNotificationCount()
    {
        return notificationCount;
    }


    /**
     * Set up yhe number of notifications that have been triggered.
     *
     * @param notificationCount long
     */
    public void setNotificationCount(long notificationCount)
    {
        this.notificationCount = notificationCount;
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
                "plannedStartDate=" + plannedStartDate +
                ", multipleNotificationsPermitted=" + multipleNotificationsPermitted +
                ", minimumNotificationInterval=" + minimumNotificationInterval +
                ", notificationInterval=" + notificationInterval +
                ", lastNotification=" + lastNotification +
                ", nextScheduledNotification=" + nextScheduledNotification +
                ", notificationCount=" + notificationCount +
                ", plannedCompletionDate=" + plannedCompletionDate +
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
                notificationInterval == that.notificationInterval &&
                multipleNotificationsPermitted == that.multipleNotificationsPermitted &&
                notificationCount == that.notificationCount &&
                Objects.equals(plannedStartDate, that.plannedStartDate) &&
                Objects.equals(lastNotification, that.lastNotification) &&
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
        return Objects.hash(super.hashCode(), plannedStartDate, multipleNotificationsPermitted,
                            minimumNotificationInterval, notificationInterval, notificationCount,
                            lastNotification, nextScheduledNotification, plannedCompletionDate);
    }
}
