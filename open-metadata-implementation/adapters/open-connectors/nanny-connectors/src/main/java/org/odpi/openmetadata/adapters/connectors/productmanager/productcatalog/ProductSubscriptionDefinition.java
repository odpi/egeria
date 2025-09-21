/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;


/**
 * The ProductSubscriptionDefinition describes the types of subscription supported by the open metadata product catalog.
 */
public enum ProductSubscriptionDefinition
{
    /**
     * This subscription delivers the data to the target destination just once to allow an evaluation of the product data.
     */
    EVALUATION_SUBSCRIPTION(GovernanceActionTypeDefinition.ONE_TIME_NOTIFICATION.getGovernanceActionTypeGUID(),
                            "EVALUATION-SUBSCRIPTION",
                            "Evaluation subscription",
                            "This subscription delivers the data to the target destination just once to allow an evaluation of the product data.",
                            null,
                            ProductGovernanceDefinition.ONE_TIME_SLO),

    /**
     * This subscription delivers the data to the target destination once a day.
     */
    DAILY_REFRESH_SUBSCRIPTION(GovernanceActionTypeDefinition.PERIODIC_REFRESH_WATCHDOG.getGovernanceActionTypeGUID(),
                               "DAILY-REFRESH-SUBSCRIPTION",
                               "Daily refresh subscription",
                               "This subscription delivers the data to the target destination once a day.",
                               null,
                               ProductGovernanceDefinition.DAILY_REFRESH_SLO),


    /**
     * This subscription delivers data updates to the target destination within on hour of receiving the new data.
     */
    ONGOING_UPDATE(GovernanceActionTypeDefinition.MONITORED_RESOURCE_WATCHDOG.getGovernanceActionTypeGUID(),
                   "ONGOING-UPDATE-SUBSCRIPTION",
                   "Ongoing update subscription",
                   "This subscription delivers data updates to the target destination within on hour of receiving the new data.",
                   null,
                   ProductGovernanceDefinition.MONITORED_RESOURCE_SLO),


    ;


    private final String governanceActionTypeGUID;
    private final String identifier;
    private final String displayName;
    private final String description;
    private final String category;
    private final ProductGovernanceDefinition serviceLevelObjective;


    ProductSubscriptionDefinition(String                      governanceActionTypeGUID,
                                  String                      identifier,
                                  String                      displayName,
                                  String                      description,
                                  String                      category,
                                  ProductGovernanceDefinition serviceLevelObjective)
    {
        this.governanceActionTypeGUID = governanceActionTypeGUID;
        this.identifier               = identifier;
        this.displayName              = displayName;
        this.description              = description;
        this.category                 = category;
        this.serviceLevelObjective    = serviceLevelObjective;
    }


    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "OpenMetadataProductCatalog::DigitalSubscription::" + identifier + "::" + displayName;
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


    public ProductGovernanceDefinition getServiceLevelObjective()
    {
        return serviceLevelObjective;
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
                "connectorProviderClassName='" + governanceActionTypeGUID + '\'' +
                ", identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }
}
