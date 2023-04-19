/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A StarRating defines the rating that a user has placed against an asset. This ranges from not recommended
 * through to five stars (excellent).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum StarRating implements Serializable
{
    /**
     * No recommendation.
     */
    NO_RECOMMENDATION (0, 0, "X", "No recommendation"),

    /**
     * Poor
     */
    ONE_STAR          (1, 1, "*", "Poor"),

    /**
     * Usable
     */
    TWO_STARS         (2, 2, "**", "Usable"),

    /**
     * Good
     */
    THREE_STARS       (3, 3, "***", "Good"),

    /**
     * Very good
     */
    FOUR_STARS        (4, 4, "****", "Very Good"),

    /**
     * Excellent
     */
    FIVE_STARS        (5, 5, "*****", "Excellent");

    private static final String ENUM_TYPE_GUID  = "77fea3ef-6ec1-4223-8408-38567e9d3c93";
    private static final String ENUM_TYPE_NAME  = "StarRating";

    private final int    openTypeOrdinal;

    private final int    ordinal;
    private final String name;
    private final String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    StarRating(int    ordinal,
               int    openTypeOrdinal,
               String name,
               String description)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
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
        return name;
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
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }


    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "StarRating{" +
                "starRatingCode=" + ordinal +
                ", starRatingSymbol='" + name + '\'' +
                ", starRatingDescription='" + description + '\'' +
                '}';
    }
}