/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CommunityDefinition is used to feed the definition of the communities associated with the open metadata
 * product catalog.
 */
public enum ProductCommunityDefinition
{
    /**
     * ReferenceDataSIG - Community of people focused on reference data products derived from the open metadata ecosystem.
     */
    REFERENCE_DATA_SIG("ReferenceDataSIG",
                       "Open Metadata Reference Data special interest group",
                       "Community of people focused on reference data products derived from the open metadata ecosystem.",
                       null),

    /**
     * MasterDataSIG - Community of people focused on master data products derived from the open metadata ecosystem.
     */
    MASTER_DATA_SIG("MasterDataSIG",
                    "Open Metadata Master Data special interest group",
                    "Community of people focused on master data products derived from the open metadata ecosystem.",
                    null),


    /**
     * ObservabilitySIG - Community of people focused on the observability products derived from the open metadata ecosystem.
     */
    OBSERVABILITY_SIG("ObservabilitySIG",
                      "Open Metadata Observability special interest group",
                      "Community of people focused on the observability products derived from the open metadata ecosystem.",
                      null),

    /**
     * GovernanceSIG - Community of people focused on the governance products derived from the open metadata ecosystem.
     */
    GOVERNANCE_SIG("GovernanceSIG",
                      "Open Metadata Observability special interest group",
                      "Community of people focused on the governance products derived from the open metadata ecosystem.",
                      null),

    /**
     * SecuritySIG - Community of people focused on the security of the open metadata ecosystem.
     */
    SECURITY_SIG("SecuritySIG",
                 "Security products special interest group",
                 "Community of people focused on the security of the open metadata ecosystem.",
                  new String[]{GovernanceZoneName.SECURITY.getZoneName()}),

    ;

    private final String   identifier;
    private final String   displayName;
    private final String   description;
    private final String[] zoneMembership;


    /**
     * The constructor creates an instance of the enum
     *
     * @param identifier   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of how to use this value
     */
    ProductCommunityDefinition(String   identifier,
                               String   displayName,
                               String   description,
                               String[] zoneMembership)
    {
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;
        this.zoneMembership = zoneMembership;
    }

    /**
     * Return the qualified name to use for the community.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "Community::Jacquard::" + identifier;
    }

    /**
     * Return the identifier of the community.
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name of the community.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the community.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the zone membership for the product.
     *
     * @return list of zone names (default = ["digital-products"])
     */
    public List<String> zoneMembership()
    {
        if (zoneMembership != null)
        {
            return new ArrayList<>(Arrays.asList(zoneMembership));
        }

        return List.of(GovernanceZoneName.DIGITAL_PRODUCTS.getZoneName());
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CommunityDefinition{" + displayName + '}';
    }
}
