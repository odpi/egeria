/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A StarRating defines the rating that a user has placed against an element. This ranges from not recommended
 * through to five stars (excellent).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum StarRating implements Serializable
{
    NO_RECOMMENDATION (0, "X", "No recommendation"),
    ONE_STAR          (1, "*", "Poor"),
    TWO_STARS         (2, "**", "Usable"),
    THREE_STARS       (3, "***", "Good"),
    FOUR_STARS        (4, "****", "Very Good"),
    FIVE_STARS        (5, "*****", "Excellent");

    private static final long     serialVersionUID = 1L;

    private int    ordinal;
    private String symbol;
    private String description;


    /**
     * Typical Constructor
     *
     * @param ordinal identifier
     * @param symbol short name
     * @param description longer description
     */
    StarRating(int ordinal, String symbol, String description)
    {
        /*
         * Save the values supplied
         */
        this.ordinal = ordinal;
        this.symbol = symbol;
        this.description = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int star rating code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default symbol for this enum instance.
     *
     * @return String default symbol
     */
    public String getName()
    {
        return symbol;
    }


    /**
     * Return the default description for the star rating for this enum instance.
     *
     * @return String default description
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
        return "StarRating : " + symbol;
    }
}