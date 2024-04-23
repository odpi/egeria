/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

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
public enum GovernanceDomain
{
    /**
     * The governance domain is not specified.
     */
    UNCLASSIFIED         (0,  "Unclassified",         "The governance domain is not specified."),

    /**
     * The data (information) governance domain.
     */
    DATA                 (1,  "Data",                 "The data (information) governance domain."),

    /**
     * The data privacy governance domain.
     */
    PRIVACY              (2,  "Privacy",              "The data privacy governance domain."),

    /**
     * The security governance domain.
     */
    SECURITY             (3,  "Security",             "The security governance domain."),

    /**
     * The IT infrastructure management governance domain.
     */
    IT_INFRASTRUCTURE    (4,  "IT Infrastructure",    "The IT infrastructure management governance domain."),

    /**
     * The software development lifecycle (SDLC) governance domain.
     */
    SOFTWARE_DEVELOPMENT (5,  "Software Development", "The software development lifecycle (SDLC) governance domain."),

    /**
     * The corporate governance domain.
     */
    CORPORATE            (6,  "Corporate",            "The corporate governance domain."),

    /**
     * The physical asset management governance domain.
     */
    ASSET_MANAGEMENT     (7,  "Asset Management",     "The physical asset management governance domain."),

    /**
     * The governance domain is locally defined.
     */
    OTHER                (99, "Other",                "The governance domain is locally defined.");

    private static final long serialVersionUID = 1L;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    GovernanceDomain(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() { return description; }


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
