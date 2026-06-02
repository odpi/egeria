/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The GovernanceDomainDefinition is used to feed the definition of the governance domains for
 * Coco Pharmaceuticals.
 */
public enum ClinicalTrialDomainDefinition
{

    /**
     * Development of new treatments by Coco Pharmaceuticals. (Unique to Coco Pharmaceuticals)
     */
    DRUG_DEVELOPMENT(20,
                     "Drug Development",
                     "Development of new treatments by Coco Pharmaceuticals."),

    ;


    private final int              domainIdentifier;
    private final String           displayName;
    private final String           description;


    /**
     * GovernanceDomainDefinition constructor creates an instance of the enum
     *
     * @param domainIdentifier   unique Id for the zone
     * @param displayName   text for the zone
     * @param description   description of the assets in the zone
     */
    ClinicalTrialDomainDefinition(int              domainIdentifier,
                                  String           displayName,
                                  String           description)
    {
        this.domainIdentifier = domainIdentifier;
        this.displayName = displayName;
        this.description = description;
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
