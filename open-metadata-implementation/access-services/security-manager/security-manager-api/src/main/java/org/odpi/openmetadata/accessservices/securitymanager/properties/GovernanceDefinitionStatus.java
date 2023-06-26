/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDefinitionStatus indicates whether the definition is complete and operational or in a state that means
 * it is either under development or obsolete.
 * <ul>
 *     <li>DRAFT - The governance definition is still in development.</li>
 *     <li>PROPOSED - The governance definition is in review and not yet active.</li>
 *     <li>ACTIVE - The governance definition is approved and in use.</li>
 *     <li>DEPRECATED - The governance definition has been superseded.</li>
 *     <li>OTHER - The governance definition in a locally defined state.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceDefinitionStatus
{
    /**
     * Draft - The governance definition is still in development.
     */
    DRAFT        (0,  "Draft",      "The governance definition is still in development."),

    /**
     * Proposed - The governance definition is in review and not yet active.
     */
    PROPOSED     (1,  "Proposed",   "The governance definition is in review and not yet active."),

    /**
     * Active - The governance definition is approved and in use.
     */
    ACTIVE       (2,  "Active",     "The governance definition is approved and in use."),

    /**
     * Deprecated - The governance definition has been superseded.
     */
    DEPRECATED   (3,  "Deprecated", "The governance definition has been superseded."),

    /**
     * Other - The governance definition in a locally defined state.
     */
    OTHER        (99, "Other",      "The governance definition in a locally defined state.");

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
    GovernanceDefinitionStatus(int  ordinal, String name, String description)
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
        return "GovernanceDefinitionStatus : " + name;
    }
}
