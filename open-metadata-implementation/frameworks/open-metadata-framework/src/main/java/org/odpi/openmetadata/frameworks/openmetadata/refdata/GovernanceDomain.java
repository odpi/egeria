/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDomain defines the different governance domains that open metadata seeks to unite.
 * It is used in a governance definition and in the governance officer.
 * This gives an indication of which of the governance officers are responsible for defining
 * which governance definition.
 * <ul>
 *     <li>UNCLASSIFIED - The governance domain is not specified.</li>
 *     <li>DATA - The data (information) governance domain.</li>
 *     <li>PRIVACY - The data privacy governance domain.</li>
 *     <li>SECURITY - The governance definition is approved and in use.</li>
 *     <li>IT_INFRASTRUCTURE - The IT infrastructure management governance domain.</li>
 *     <li>SOFTWARE_DEVELOPMENT - The software development lifecycle governance domain.</li>
 *     <li>CORPORATE - The corporate governance domain.</li>
 *     <li>ASSET_MANAGEMENT - The physical asset management governance domain.</li>
 *     <li>OTHER - The governance domain is locally defined.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceDomain implements OpenMetadataRefData
{
    /**
     * The governance domain is not specified.
     */
    UNCLASSIFIED         (0,"8aace3ca-1db4-4927-ad45-9766770a92c4", "Unclassified",         "The governance domain is not specified because the element is relevant to all domains.", true),

    /**
     * The data (information) governance domain.
     */
    DATA                 (1, "9c465654-ad9d-4c84-bbcb-b65eaf6b9c27",  "Data",                 "The data (information) governance domain.", false),

    /**
     * The data privacy governance domain.
     */
    PRIVACY              (2, "dc502370-2bcf-470c-b8d3-d523f7b4bbc1",    "Privacy",              "The data privacy governance domain.", false),

    /**
     * The security governance domain.
     */
    SECURITY             (3, "27c1fa77-9ee7-4611-8bb5-b1aa7f0a03bd",    "Security",             "The security governance domain.", false),

    /**
     * The IT infrastructure management governance domain.
     */
    IT_INFRASTRUCTURE    (4, "032ccb98-88af-4b60-87c2-46c95cc3ab71",    "IT Infrastructure",    "The IT infrastructure management governance domain.", false),

    /**
     * The software development lifecycle (SDLC) governance domain.
     */
    SOFTWARE_DEVELOPMENT (5, "463fec3c-95c7-47ba-bb50-c89ea52023cc",    "Software Development", "The software development lifecycle (SDLC) governance domain.", false),

    /**
     * The corporate governance domain.
     */
    CORPORATE            (6, "7d5a47a6-008c-409d-bcc9-de55d39ea778",    "Corporate",            "The corporate governance domain.", false),

    /**
     * The physical asset management governance domain.
     */
    ASSET_MANAGEMENT     (7, "831e21d5-8e7c-448d-aa38-f977437f2a9e",    "Asset Management",     "The physical asset management governance domain.", false),

    /**
     * The development and management lifecycle for data sharing mechanisms including digital products.
     */
    DATA_SHARING(8, "d4eb2884-b369-4500-a6e1-36798c0cec7c", "Data Sharing", "The development and management lifecycle for data sharing mechanism including digital products.", false),

    /**
     * Initiatives to improve the operational sustainability of the organization.
     */
    SUSTAINABILITY       (9, "3bcfbce9-eaa5-4e30-9cdf-c426eb0db6b6", "Sustainability", "Initiatives to improve the operational sustainability of the organization.  This includes sustainability reports to employees and the regulators, along with education for employees and initiatives to reduce the emission of greenhouse gases.", false),

    /**
     * The governance domain is locally defined or unknown.
     */
    OTHER                (99, "c34e17d6-b924-4a7d-a5fd-13425a1de422",   "Other",                "The governance domain is locally defined.", false);



    private final String  descriptionGUID;
    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final boolean isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    GovernanceDomain(int     ordinal,
                     String  descriptionGUID,
                     String  name,
                     String  description,
                     boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault       = isDefault;
    }


    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    @Override
    public String getDisplayName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return isDefault;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GovernanceDomain : " + name;
    }
}
