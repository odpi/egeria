/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;


/**
 * Defines the guids and request types for the governance action types that are used in the
 * Open Metadata Product Catalog.  They are set up in the core content pack and then customised
 * by the harvester.
 */
public enum GovernanceActionTypeDefinition
{
    /**
     * monitored-resource-notification
     */
    MONITORED_RESOURCE_WATCHDOG("monitored-resource-notification",
                                "9c6e08d6-a081-4482-a173-1ae7baf3faf1"),

    /**
     * periodic-refresh-notification
     */
    PERIODIC_REFRESH_WATCHDOG("periodic-refresh-notification",
                              "581439e8-c49d-42f2-bd4a-2070b31db8f0"),

    /**
     * one-time-notification
     */
    ONE_TIME_NOTIFICATION("one-time-notification",
                          "e1f5aa39-41d0-4894-aa6d-407fe1189fcd"),

    /**
     * award-karma-points
     */
    AWARD_KARMA_POINTS("award-karma-points",
                          "de681791-d978-4d5a-9828-6e19025e3a17"),

    /**
     * provision-tabular-data-set
     */
    PROVISION_TABULAR("provision-tabular-data-set",
                      "bf03a1c7-ad5e-49b7-8f97-a66624243767"),

    /**
     * create-digital-subscription
     */
    CREATE_SUBSCRIPTION("create-digital-subscription",
                      "369e63b9-56b6-4f31-96a2-3dcf26a21ca8"),

    /**
     * cancel-digital-subscription
     */
    CANCEL_SUBSCRIPTION("cancel-digital-subscription",
                        "88d9516b-6134-4cfd-bfcc-0e2fcd8dab7f"),




    ;

    private final String                      governanceRequestType;
    private final String                      governanceActionTypeGUID;




    /**
     * Return the request type enum value.
     *
     * @param governanceRequestType request type used by the governance
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     */
    GovernanceActionTypeDefinition(String                      governanceRequestType,
                                   String                      governanceActionTypeGUID)
    {
        this.governanceRequestType         = governanceRequestType;
        this.governanceActionTypeGUID      = governanceActionTypeGUID;
    }


    /**
     * Return the Request Type.
     *
     * @return string
     */
    public String getGovernanceRequestType()
    {
        return governanceRequestType;
    }


    /**
     * Return the unique identifier of the governance action type.
     *
     * @return string
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceActionTypeDefinition{" + "name='" + governanceRequestType + '\'' + "}";
    }
}
