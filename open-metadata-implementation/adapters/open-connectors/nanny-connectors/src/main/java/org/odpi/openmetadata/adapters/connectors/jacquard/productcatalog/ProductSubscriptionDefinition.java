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
                            false,
                            0,
                            ProductGlossaryTermDefinition.EVALUATION_SUBSCRIPTION),

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
                               true,
                               24 * 60,
                               ProductGlossaryTermDefinition.DAILY_REFRESH_SUBSCRIPTION),

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
                                true,
                                7 * 24 * 60,
                                ProductGlossaryTermDefinition.WEEKLY_REFRESH_SUBSCRIPTION),


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
                   true,
                   10,
                   ProductGlossaryTermDefinition.ONGOING_UPDATE_SUBSCRIPTION),
    ;


    private final String                        governanceActionTypeGUID;
    private final String                        identifier;
    private final String                        displayName;
    private final String                        description;
    private final String                        category;
    private final ProductGovernanceDefinition   serviceLevelObjective;
    private final boolean                       addMonitoredResource;
    private final boolean                       multipleNotificationsPermitted;
    private final long                          minimumNotificationInterval;
    private final ProductGlossaryTermDefinition glossaryTerm;


    /**
     * Constructor for enum value.
     *
     * @param governanceActionTypeGUID    governance action type that fulfils the subscription
     * @param identifier                  identifier of this subscription type
     * @param displayName                 display name for subscription type
     * @param description                 description of subscription type
     * @param category                    category of subscription type
     * @param serviceLevelObjective       behaviour of subscription type
     * @param addMonitoredResource        should the subscription monitor its resource for changes?
     * @param multipleNotificationsPermitted whether multiple notifications are permitted
     * @param minimumNotificationInterval minimum time between notifications
     * @param glossaryTerm                glossary term describing this subscription type
     */
    ProductSubscriptionDefinition(String                        governanceActionTypeGUID,
                                  String                        identifier,
                                  String                        displayName,
                                  String                        description,
                                  String                        category,
                                  ProductGovernanceDefinition   serviceLevelObjective,
                                  boolean                       addMonitoredResource,
                                  boolean                       multipleNotificationsPermitted,
                                  long                          minimumNotificationInterval,
                                  ProductGlossaryTermDefinition glossaryTerm)
    {
        this.governanceActionTypeGUID       = governanceActionTypeGUID;
        this.identifier                     = identifier;
        this.displayName                    = displayName;
        this.description                    = description;
        this.category                       = category;
        this.serviceLevelObjective          = serviceLevelObjective;
        this.addMonitoredResource           = addMonitoredResource;
        this.multipleNotificationsPermitted = multipleNotificationsPermitted;
        this.minimumNotificationInterval    = minimumNotificationInterval;
        this.glossaryTerm                   = glossaryTerm;
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
     * Return whether a monitored resource should be added to the subscription.
     *
     * @return boolean flag
     */
    public boolean isAddMonitoredResource()
    {
        return addMonitoredResource;
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
     * Return the optional glossary term for this subscription.
     *
     * @return glossary term definition
     */
    public ProductGlossaryTermDefinition getGlossaryTerm()
    {
        return glossaryTerm;
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
                "governanceActionTypeGUID='" + governanceActionTypeGUID + '\'' +
                ", identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", serviceLevelObjective=" + serviceLevelObjective +
                ", multipleNotificationsPermitted=" + multipleNotificationsPermitted +
                ", minimumNotificationInterval=" + minimumNotificationInterval +
                ", glossaryTerm=" + glossaryTerm +
                "} " + super.toString();
    }
}
