/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;


import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.ClinicalTrialDomainDefinition;

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
    ALL(GovernanceDomain.UNCLASSIFIED.getOrdinal(),
        GovernanceDomain.UNCLASSIFIED.getDisplayName(),
        GovernanceDomain.UNCLASSIFIED.getDescription(),
        "Governance Leadership Community",
        PersonDefinition.JULES_KEEPER),

    /**
     * Data (Information) Governance
     */
    DATA( GovernanceDomain.DATA.getOrdinal(),
          GovernanceDomain.DATA.getDisplayName(),
          GovernanceDomain.DATA.getDescription(),
          "Data Governance Community",
          PersonDefinition.ERIN_OVERVIEW),

    /**
     * Data Privacy
     */
    PRIVACY( GovernanceDomain.PRIVACY.getOrdinal(),
             GovernanceDomain.PRIVACY.getDisplayName(),
             GovernanceDomain.PRIVACY.getDescription(),
             "Data Privacy Community",
             PersonDefinition.FAITH_BROKER),

    /**
     * Information Security
     */
    SECURITY( GovernanceDomain.SECURITY.getOrdinal(),
              GovernanceDomain.SECURITY.getDisplayName(),
              GovernanceDomain.SECURITY.getDescription(),
              "InfoSec Community",
              PersonDefinition.IVOR_PADLOCK),

    /**
     * IT Governance
     */
    IT_GOVERNANCE(GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal(),
                  GovernanceDomain.IT_INFRASTRUCTURE.getDisplayName(),
                  GovernanceDomain.IT_INFRASTRUCTURE.getDescription(),
                  "IT Governance Community",
                  PersonDefinition.GARY_GEEKE),

    /**
     * Software Development Assurance
     */
    SOFTWARE_DEVELOPMENT( GovernanceDomain.SOFTWARE_DEVELOPMENT.getOrdinal(),
                          GovernanceDomain.SOFTWARE_DEVELOPMENT.getDisplayName(),
                          GovernanceDomain.SOFTWARE_DEVELOPMENT.getDescription(),
                          "Software Development Leadership Community",
                          PersonDefinition.POLLY_TASKER),

    /**
     * Corporate Governance
     */
    CORPORATE( GovernanceDomain.CORPORATE.getOrdinal(),
               GovernanceDomain.CORPORATE.getDisplayName(),
               GovernanceDomain.CORPORATE.getDescription(),
               "Corporate Governance Community",
               PersonDefinition.REGGIE_MINT),

    /**
     * Physical Asset Management
     */
    ASSET_MANAGEMENT(GovernanceDomain.ASSET_MANAGEMENT.getOrdinal(),
                     GovernanceDomain.ASSET_MANAGEMENT.getDisplayName(),
                     GovernanceDomain.ASSET_MANAGEMENT.getDescription(),
                     "Asset Management Community",
                     PersonDefinition.SIDNEY_SEEKER),


    /**
     * Development of new treatments by Coco Pharmaceuticals. (Unique to Coco Pharmaceuticals)
     */
    DRUG_DEVELOPMENT(ClinicalTrialDomainDefinition.DRUG_DEVELOPMENT.getDomainIdentifier(),
                     ClinicalTrialDomainDefinition.DRUG_DEVELOPMENT.getDisplayName(),
                     ClinicalTrialDomainDefinition.DRUG_DEVELOPMENT.getDescription(),
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
