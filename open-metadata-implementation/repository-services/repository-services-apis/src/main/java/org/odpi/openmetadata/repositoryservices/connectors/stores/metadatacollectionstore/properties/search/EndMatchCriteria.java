/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The EndMatchCriteria enum defines how the end guids of a relationship should match.
 * <ul>
 *     <li>BOTH means an end 1 guid and an end 2 guid must match.</li>
 *     <li>ANY means a match on any of the end guids is good enough.</li>
 *     <li>NONE means return relationships where none of the supplied end guids match.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum EndMatchCriteria
{
    /**
     * Both - Both ends must match.
     */
    BOTH  (0, "Both",  "Both ends must match."),

    /**
     * Any - A match on any of the end guids in the relationship is good enough.
     */
    ANY  (1, "Any",  "A match on any of the end guids in the relationship is good enough."),

    /**
     * None - Return relationships where the guids at neither end of the relationship match.
     */
    NONE (2, "None", "Return relationships where the guids at neither end of the relationship match");


    private final int     ordinal;
    private final String  name;
    private final String  description;

    /**
     * Constructor to set up a single instance of the enum.
     *
     * @param ordinal numerical representation of the match criteria
     * @param name default string name of the match criteria
     * @param description default string description of the match criteria
     */
    EndMatchCriteria(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the match criteria.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the match criteria.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the match criteria.
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
        return "MatchCriteria{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
