/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceZoneName lists the predefined governance zones defined by Egeria to control access to open metadata.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum GovernanceZoneName
{
    EGERIA_RUNTIME("egeria-runtime",
                   "Egeria's Runtime Zone",
                   "Resources describing Egeria's runtime components including the OMAG Server Platform, OMAG Servers, Integration Connectors, Governance Engines and the Governance Services.",
                   "Elements catalogued by the OMAGServerPlatformCataloguer.",
                   GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal()),

    DIGITAL_PRODUCTS("digital-products",
                     "Digital Products Zone",
                     "Resources describing the digital products that are being managed by Egeria.",
                     "Elements related to digital products.",
                     GovernanceDomain.UNCLASSIFIED.getOrdinal()),

    SECURITY("security",
             "Security Zone",
             "Resources used by the security team for audit and diagnostic purposes.",
             "Elements describing access and authorization - for the security team only.",
             GovernanceDomain.SECURITY.getOrdinal()),
    ;

    private final String zoneName;
    private final String displayName;
    private final String description;
    private final String criteria;
    private final int    domainIdentifier;

    /**
     * Typical Constructor
     *
     * @param zoneName    name used in the zone membership.
     * @param displayName short name
     * @param description longer explanation
     */
    GovernanceZoneName(String zoneName,
                       String displayName,
                       String description,
                       String criteria,
                       int domainIdentifier)
    {
        this.zoneName         = zoneName;
        this.displayName      = displayName;
        this.description      = description;
        this.criteria         = criteria;
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Returns the unique name for the zone entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "GovernanceZone::" + zoneName;
    }


    /**
     * Returns the unique name for the zone.
     *
     * @return zone name
     */
    public String getZoneName()
    {
        return zoneName;
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
     * Returns a description of the criteria for including assets in the zone.
     *
     * @return criteria
     */
    public String getCriteria()
    {
        return criteria;
    }


    /**
     * Returns the domain identifier for the zone.
     *
     * @return domain identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceZoneName{" +
                "zoneName='" + zoneName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
