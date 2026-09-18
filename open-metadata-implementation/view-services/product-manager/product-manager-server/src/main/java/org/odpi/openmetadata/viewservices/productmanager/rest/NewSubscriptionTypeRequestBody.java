/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewSubscriptionTypeRequestBody describes a new subscription type for a digital product.  A subscription type is
 * the notification type that drives the subscribers' notifications, registered with the subscription manager, and
 * the governance action process that a subscriber runs to take out a subscription of that type.
 * Every field is optional; the meaning of notificationInterval and monitoredResourceGUIDs depends on the kind of
 * subscription type being created.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewSubscriptionTypeRequestBody extends MetadataSourceRequestBody
{
    private String       subscriptionManagerGUID   = null;
    private String       identifier                = null;
    private String       displayName               = null;
    private String       description               = null;
    private String       licenseTypeGUID           = null;
    private String       serviceLevelObjectiveGUID = null;
    private long         notificationInterval      = 0L;
    private List<String> monitoredResourceGUIDs    = null;


    /**
     * Default constructor
     */
    public NewSubscriptionTypeRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewSubscriptionTypeRequestBody(NewSubscriptionTypeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.subscriptionManagerGUID   = template.getSubscriptionManagerGUID();
            this.identifier                = template.getIdentifier();
            this.displayName               = template.getDisplayName();
            this.description               = template.getDescription();
            this.licenseTypeGUID           = template.getLicenseTypeGUID();
            this.serviceLevelObjectiveGUID = template.getServiceLevelObjectiveGUID();
            this.notificationInterval      = template.getNotificationInterval();
            this.monitoredResourceGUIDs    = template.getMonitoredResourceGUIDs();
        }
    }


    /**
     * Return the unique identifier of the integration connector that notifies the subscribers.  Null means the
     * Baudot Digital Product Subscription Manager from the digital products content pack.
     *
     * @return guid
     */
    public String getSubscriptionManagerGUID()
    {
        return subscriptionManagerGUID;
    }


    /**
     * Set up the unique identifier of the integration connector that notifies the subscribers.
     *
     * @param subscriptionManagerGUID guid
     */
    public void setSubscriptionManagerGUID(String subscriptionManagerGUID)
    {
        this.subscriptionManagerGUID = subscriptionManagerGUID;
    }


    /**
     * Return the identifier of the subscription type.  Null means the default identifier for the kind of
     * subscription type.  A product that offers more than one subscription type of the same kind needs a distinct
     * identifier for each.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the identifier of the subscription type.
     *
     * @param identifier string
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return the display name of the subscription type.  Null means a default name.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the subscription type.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the subscription type.  Null means a default description.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the subscription type.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier of the license type granted to the subscriber's asset.  Null means the first
     * license type that the product is governed by.
     *
     * @return guid
     */
    public String getLicenseTypeGUID()
    {
        return licenseTypeGUID;
    }


    /**
     * Set up the unique identifier of the license type granted to the subscriber's asset.
     *
     * @param licenseTypeGUID guid
     */
    public void setLicenseTypeGUID(String licenseTypeGUID)
    {
        this.licenseTypeGUID = licenseTypeGUID;
    }


    /**
     * Return the unique identifier of the service level objective offered by the subscription.  Null means the
     * first service level objective that the product is governed by.
     *
     * @return guid
     */
    public String getServiceLevelObjectiveGUID()
    {
        return serviceLevelObjectiveGUID;
    }


    /**
     * Set up the unique identifier of the service level objective offered by the subscription.
     *
     * @param serviceLevelObjectiveGUID guid
     */
    public void setServiceLevelObjectiveGUID(String serviceLevelObjectiveGUID)
    {
        this.serviceLevelObjectiveGUID = serviceLevelObjectiveGUID;
    }


    /**
     * Return the notification interval in minutes.  For a periodic subscription type this is the time between
     * notifications; for an ongoing update subscription type it is the minimum time between notifications.  It is
     * not used by a one-time subscription type.
     *
     * @return minutes
     */
    public long getNotificationInterval()
    {
        return notificationInterval;
    }


    /**
     * Set up the notification interval in minutes.
     *
     * @param notificationInterval minutes
     */
    public void setNotificationInterval(long notificationInterval)
    {
        this.notificationInterval = notificationInterval;
    }


    /**
     * Return the unique identifiers of the metadata elements whose changes trigger a notification.  Only used by an
     * ongoing update subscription type.
     *
     * @return list of guids
     */
    public List<String> getMonitoredResourceGUIDs()
    {
        return monitoredResourceGUIDs;
    }


    /**
     * Set up the unique identifiers of the metadata elements whose changes trigger a notification.
     *
     * @param monitoredResourceGUIDs list of guids
     */
    public void setMonitoredResourceGUIDs(List<String> monitoredResourceGUIDs)
    {
        this.monitoredResourceGUIDs = monitoredResourceGUIDs;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewSubscriptionTypeRequestBody{" +
                "subscriptionManagerGUID='" + subscriptionManagerGUID + '\'' +
                ", identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", licenseTypeGUID='" + licenseTypeGUID + '\'' +
                ", serviceLevelObjectiveGUID='" + serviceLevelObjectiveGUID + '\'' +
                ", notificationInterval=" + notificationInterval +
                ", monitoredResourceGUIDs=" + monitoredResourceGUIDs +
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
        if (! (objectToCompare instanceof NewSubscriptionTypeRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return notificationInterval == that.notificationInterval &&
                Objects.equals(subscriptionManagerGUID, that.subscriptionManagerGUID) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(licenseTypeGUID, that.licenseTypeGUID) &&
                Objects.equals(serviceLevelObjectiveGUID, that.serviceLevelObjectiveGUID) &&
                Objects.equals(monitoredResourceGUIDs, that.monitoredResourceGUIDs);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), subscriptionManagerGUID, identifier, displayName, description,
                            licenseTypeGUID, serviceLevelObjectiveGUID, notificationInterval, monitoredResourceGUIDs);
    }
}
