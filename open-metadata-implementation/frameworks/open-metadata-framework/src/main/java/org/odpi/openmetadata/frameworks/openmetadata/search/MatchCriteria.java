/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The MatchCriteria enum defines how the metadata instances in the metadata store should be matched
 * against the properties supplied on the search request.
 * <ul>
 *     <li>ALL means all properties must match.</li>
 *     <li>ANY means a match on any of properties is good enough.</li>
 *     <li>NONE means return instances where none of the supplied properties match.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum MatchCriteria
{
    /**
     * All properties must match.
     */
    ALL  (0, "All",  "All properties must match."),

    /**
     * A match on any of the properties in the instance is good enough.
     */
    ANY  (1, "Any",  "A match on any of the properties in the instance is good enough."),

    /**
     * Return instances where none of the supplied properties match.
     */
    NONE (2, "None", "Return instances where none of the supplied properties match.");

    private final int     ordinal;
    private final String  name;
    private final String  description;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numerical representation of the match criteria
     * @param name default string name of the match criteria
     * @param description default string description of the match criteria
     */
    MatchCriteria(int  ordinal, String name, String description)
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
        return "MatchCriteria{" + name  + "}";
    }
}
