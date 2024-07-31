/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;


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
public enum GovernanceDefinitionStatus implements OpenMetadataEnum
{
    /**
     * Draft - The governance definition is still in development.
     */
    DRAFT        (0,  "Draft",      "The governance definition is still in development.", "ff7d7346-2d70-4fe1-acac-ae69f348b6d7"),

    /**
     * Proposed - The governance definition is in review and not yet active.
     */
    PROPOSED     (1,  "Proposed",   "The governance definition is in review and not yet active.", "6edb1aba-b9c3-418a-9277-a69a1a0e936e"),

    /**
     * Active - The governance definition is approved and in use.
     */
    ACTIVE       (2,  "Active",     "The governance definition is approved and in use.", "806aea5c-c432-44a1-a1e4-8d9756787456"),

    /**
     * Deprecated - The governance definition has been superseded.
     */
    DEPRECATED   (3,  "Deprecated", "The governance definition has been superseded.", "6726a279-e0aa-47a2-bab2-b95a9e7112b1"),

    /**
     * Other - The governance definition in a locally defined state.
     */
    OTHER        (99, "Other",      "The governance definition in a locally defined state.", "42b24ef8-b610-44a9-9b41-cb0d5a093ef2");

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final String         descriptionGUID;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     * @param descriptionGUID unique identifier of valid value
     */
    GovernanceDefinitionStatus(int    ordinal,
                               String name,
                               String description,
                               String descriptionGUID)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
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
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
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
        return false;
    }


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
