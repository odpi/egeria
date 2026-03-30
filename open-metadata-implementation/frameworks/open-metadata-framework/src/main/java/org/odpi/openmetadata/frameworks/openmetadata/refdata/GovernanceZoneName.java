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
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceZoneName
{
    EGERIA_RUNTIME("egeria-runtime",
                   "Egeria's Runtime Zone",
                   "Resources describing Egeria's runtime components including the OMAG Server Platform, OMAG Servers, Integration Connectors, Governance Engines and the Governance Services."),

    DIGITAL_PRODUCTS("digital-products",
                     "Digital Products Zone",
                     "Resources describing the digital products that are being managed by Egeria."),

    SECURITY("security",
             "Security Zone",
             "Resources used by the security team for audit and diagnostic purposes."),
    ;

    private final String zoneName;
    private final String displayName;
    private final String description;


    /**
     * Typical Constructor
     *
     * @param zoneName name used in the zone membership.
     * @param displayName short name
     * @param description longer explanation
     */
    GovernanceZoneName(String zoneName,
                       String displayName,
                       String description)
    {
        this.zoneName    = zoneName;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the name of the zone used in ZoneMembership classification and
     * the identifier of the security access control.
     *
     * @return string
     */
    public String getZoneName()
    {
        return zoneName;
    }


    /**
     * Return the display name for the zone.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description for the zone.
     *
     * @return String default status description
     */
    public String getDescription()
    {
        return description;
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
