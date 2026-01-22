/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


/**
 * The ProductSubscriptionDefinition describes the types of subscription supported by the Open Metadata Digital Product Catalog.
 */
public enum ProductSubscriptionDefinition
{
    /**
     * This subscription delivers the data to the target destination just once to allow an evaluation of the product data.
     */
    EVALUATION_SUBSCRIPTION(GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceActionTypeGUID(),
                            "EVALUATION-SUBSCRIPTION",
                            "Evaluation subscription",
                            "This subscription delivers the data to the target destination just once to allow an evaluation of the product data.",
                            null,
                            ProductGovernanceDefinition.ONE_TIME_SLO,
                            false,
                            10,
                            0),

    /**
     * This subscription delivers the data to the target destination once a day.
     */
    DAILY_REFRESH_SUBSCRIPTION(GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceActionTypeGUID(),
                               "DAILY-REFRESH-SUBSCRIPTION",
                               "Daily refresh subscription",
                               "This subscription delivers the data to the target destination once a day.",
                               null,
                               ProductGovernanceDefinition.DAILY_REFRESH_SLO,
                               true,
                               10,
                               24 * 60),

    /**
     * This subscription delivers the data to the target destination once a week.
     */
    WEEKLY_REFRESH_SUBSCRIPTION(GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceActionTypeGUID(),
                               "WEEKLY-REFRESH-SUBSCRIPTION",
                               "Weekly refresh subscription",
                               "This subscription delivers the data to the target destination once a week.",
                               null,
                               ProductGovernanceDefinition.WEEKLY_REFRESH_SLO,
                                true,
                                10,
                                7 * 24 * 60),


    /**
     * This subscription delivers data updates to the target destination within an hour of receiving the new data.
     */
    ONGOING_UPDATE(GovernanceActionTypeDefinition.BAUDOT_SUBSCRIPTION_MANAGER.getGovernanceActionTypeGUID(),
                   "ONGOING-UPDATE-SUBSCRIPTION",
                   "Ongoing update subscription",
                   "This subscription delivers data updates to the target destination within an hour of receiving the new data.",
                   null,
                   ProductGovernanceDefinition.MONITORED_RESOURCE_SLO,
                   true,
                   10,
                   0),
    ;


    private final String                      governanceActionTypeGUID;
    private final String                      identifier;
    private final String                      displayName;
    private final String                      description;
    private final String                      category;
    private final ProductGovernanceDefinition serviceLevelObjective;
    private final boolean                     multipleNotificationsPermitted;
    private final long                        minimumNotificationInterval ;
    private final long                        notificationInterval;


    /**
     * Constructor for enum value.
     *
     * @param governanceActionTypeGUID    governance action type that fulfils the subscription
     * @param identifier                  identifier of this subscription type
     * @param displayName                 display name for subscription type
     * @param description                 description of subscription type
     * @param category                    category of subscription type
     * @param serviceLevelObjective       behaviour of subscription type
     * @param multipleNotificationsPermitted whether multiple notifications are permitted
     * @param minimumNotificationInterval minimum time between notifications
     * @param notificationInterval        time between notifications for periodic notification pattern
     */
    ProductSubscriptionDefinition(String                      governanceActionTypeGUID,
                                  String                      identifier,
                                  String                      displayName,
                                  String                      description,
                                  String                      category,
                                  ProductGovernanceDefinition serviceLevelObjective,
                                  boolean                     multipleNotificationsPermitted,
                                  long                        minimumNotificationInterval,
                                  long                        notificationInterval)
    {
        this.governanceActionTypeGUID       = governanceActionTypeGUID;
        this.identifier                     = identifier;
        this.displayName                    = displayName;
        this.description                    = description;
        this.category                       = category;
        this.serviceLevelObjective          = serviceLevelObjective;
        this.multipleNotificationsPermitted = multipleNotificationsPermitted;
        this.minimumNotificationInterval    = minimumNotificationInterval;
        this.notificationInterval           = notificationInterval;
    }



    /**
     * Returns the unique identifier for the subscription manager's governance action type.
     *
     * @return type name
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Return the description of the service level objectives (SLO).
     *
     * @return governance definition
     */
    public ProductGovernanceDefinition getServiceLevelObjective()
    {
        return serviceLevelObjective;
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
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "ProductSubscriptionDefinition{" +
                "governanceActionTypeGUID='" + getGovernanceActionTypeGUID() + '\'' +
                ", identifier='" + getIdentifier() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", category='" + getCategory() + '\'' +
                ", serviceLevelObjective=" + getServiceLevelObjective() +
                ", multipleNotificationsPermitted=" + getMultipleNotificationsPermitted() +
                ", minimumNotificationInterval=" + getMinimumNotificationInterval() +
                ", notificationInterval=" + getNotificationInterval() +
                "} " + super.toString();
    }
}
