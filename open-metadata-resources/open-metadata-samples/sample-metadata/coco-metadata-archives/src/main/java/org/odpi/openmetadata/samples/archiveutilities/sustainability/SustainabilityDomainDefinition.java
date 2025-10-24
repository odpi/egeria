/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The GovernanceDomainDefinition is used to feed the definition of the governance domains for
 * Coco Pharmaceuticals.
 */
public enum SustainabilityDomainDefinition
{
    /**
     * Initiatives to improve the operational sustainability of Coco Pharmaceuticals.
     */
    SUSTAINABILITY_REPORTING(9,
                     "Sustainability",
                     "Initiatives to improve the operational sustainability of Coco Pharmaceuticals.  This includes sustainability reports to employees and the regulators, along with education for employees and initiatives to reduce the emission of greenhouse gases.",
                     "Sustainability Community",
                     PersonDefinition.TOM_TALLY),

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
    SustainabilityDomainDefinition(int              domainIdentifier,
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
