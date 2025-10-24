/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The GovernanceDomainDefinition is used to feed the definition of the governance domains for
 * Coco Pharmaceuticals.
 */
public enum GovernanceDomainDefinition
{
    /**
     * All Governance Domains
     */
    ALL(0,
        "All Governance Domains",
        "Relevant to all governance domains.",
        "Governance Leadership Community",
        PersonDefinition.JULES_KEEPER),

    /**
     * Data (Information) Governance
     */
    DATA( 1,
          "Data (Information) Governance",
          "Management and use of data.",
          "Data Governance Community",
          PersonDefinition.ERIN_OVERVIEW),

    /**
     * Data Privacy
     */
    PRIVACY( 2,
                "Data Privacy",
                "Ensuring individual privacy both in research and operations.",
             "Data Privacy Community",
             PersonDefinition.FAITH_BROKER),

    /**
     * Information Security
     */
    SECURITY( 3,
                "Information Security",
                "Ensuring the security of Coco Pharmaceuticals' systems, data and processes.",
              "InfoSec Community",
              PersonDefinition.IVOR_PADLOCK),

    /**
     * IT Governance
     */
    IT_INFRASTRUCTURE( 4,
                "IT Governance",
                "Management of IT infrastructure such as hosts, servers, applications, databases and network infrastructure descriptions.",
                       "IT Governance Community",
                       PersonDefinition.GARY_GEEKE),

    /**
     * Software Development Assurance
     */
    SOFTWARE_DEVELOPMENT( 5,
                "Software Development Assurance",
                "Managing the development of new software and systems.",
                          "Software Development Leadership Community",
                          PersonDefinition.POLLY_TASKER),

    /**
     * Corporate Governance
     */
    CORPORATE( 6,
                "Corporate Governance",
                "Ensuring the legal operation of Coco Pharmaceuticals.",
               "Corporate Governance Community",
               PersonDefinition.REGGIE_MINT),

    /**
     * Physical Asset Management
     */
    ASSET_MANAGEMENT(7,
                "Physical Asset Management",
                "Management of assets that describe the physical equipment of Coco Pharmaceuticals.",
                     "Asset Management Community",
                     PersonDefinition.SIDNEY_SEEKER),


    /**
     * Development of new treatments by Coco Pharmaceuticals.
     */
    DRUG_DEVELOPMENT(8,
                     "Drug Development",
                     "Development of new treatments by Coco Pharmaceuticals.",
                     "Drug Development Community",
                     PersonDefinition.TESSA_TUBE),

    ;


    private final int              domainIdentifier;
    private final String           displayName;
    private final String           description;
    private final String           communityName;
    private final PersonDefinition governanceOfficer;


    /**
     * GovernanceDomainDefinition constructor creates an instance of the enum
     *
     * @param domainIdentifier   unique Id for the zone
     * @param displayName   text for the zone
     * @param description   description of the assets in the zone
     * @param communityName name of community driving the
     */
    GovernanceDomainDefinition(int              domainIdentifier,
                               String           displayName,
                               String           description,
                               String           communityName,
                               PersonDefinition governanceOfficer)
    {
        this.domainIdentifier = domainIdentifier;
        this.displayName = displayName;
        this.description = description;
        this.communityName = communityName;
        this.governanceOfficer = governanceOfficer;
    }


    /**
     * Returns the unique name for the zone entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(null,
                                                OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                null,
                                                Integer.toString(domainIdentifier));
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return identifier for domain
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Returns a descriptive name of the zone.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the assets within the zone.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the name of the community that will coordinate the governance domain.
     *
     * @return string name
     */
    public String getCommunityName()
    {
        return communityName;
    }


    /**
     * Return the person who should be appointed as a governance officer.
     *
     * @return person description
     */
    public PersonDefinition getGovernanceOfficer()
    {
        return governanceOfficer;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceDomain{" + displayName + '}';
    }
}
