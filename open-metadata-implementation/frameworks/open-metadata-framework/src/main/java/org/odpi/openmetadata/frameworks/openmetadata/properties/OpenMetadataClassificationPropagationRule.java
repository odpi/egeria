/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataClassificationPropagationRule is part of a relationship definition (OpenMetadataRelationshipDef).
 * It indicates whether classifications from one entity should propagate across a relationship instance.
 * It allows classification for, say confidentiality to be propagated to related entities.
 * <p>
 *     The propagation rule defines the direction of propagation:
 * </p>
 * <ul>
 *     <li>NONE: no propagation of classifications across the relationship.</li>
 *     <li>ONE_TO_TWO: from entity at end 1 of the relationship to the entity at end 2 of the relationship.</li>
 *     <li>TWO_TO_ONE: from entity at end 2 of the relationship to the entity at end 1 of the relationship.</li>
 *     <li>BOTH: two way propagation.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataClassificationPropagationRule
{
    /**
     * No classification propagation.
     */
    NONE       (0, "NONE",       "No classification propagation"),

    /**
     * Classification propagation direction is one way from entity one to entity two.
     */
    ONE_TO_TWO (1, "ONE_TO_TWO", "Classification propagation direction is one way from entity one to entity two"),

    /**
     * Classification propagation direction is one way from entity two to entity one.
     */
    TWO_TO_ONE (2, "TWO_TO_ONE", "Classification propagation direction is one way from entity two to entity one"),

    /**
     * Classification propagation in both directions.
     */
    BOTH       (3, "BOTH",       "Classification propagation in both directions");

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numerical representation of the propagation rule
     * @param name default string name of the propagation rule
     * @param description default string description of the propagation rule
     */
    OpenMetadataClassificationPropagationRule(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the propagation rule.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the propagation rule.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the propagation rule.
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
        return "OpenMetadataClassificationPropagationRule{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
