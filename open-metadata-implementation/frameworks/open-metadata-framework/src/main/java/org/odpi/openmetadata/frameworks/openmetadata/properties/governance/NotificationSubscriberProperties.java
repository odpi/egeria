/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NotificationSubscriberProperties describes the properties for the NotificationSubscriber relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationSubscriberProperties extends LabeledRelationshipProperties
{
    private ActivityStatus activityStatus = ActivityStatus.IN_PROGRESS;



    /**
     * Default constructor
     */
    public NotificationSubscriberProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NotificationSubscriberProperties(NotificationSubscriberProperties template)
    {
        super(template);

        if (template != null)
        {
            this.activityStatus = template.getActivityStatus();
        }
    }



    /**
     * Return the current status of the subscriber.  Notifications will be passed to the
     * subscriber while this relationship is in IN_PROGRESS status.
     *
     * @return status enum
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }


    /**
     * Set up the current status of the subscriber.  Notifications will be passed to the
     * subscriber while this relationship is in IN_PROGRESS status.
     *
     * @param activityStatus enum
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }




    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NotificationSubscriberProperties{" +
                "status=" + activityStatus +
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
        NotificationSubscriberProperties that = (NotificationSubscriberProperties) objectToCompare;
        return  activityStatus == that.activityStatus;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), activityStatus);
    }
}
