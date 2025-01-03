/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A StarRating defines the rating that a user has placed against an asset. This ranges from not recommended
 * through to five stars (excellent).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum StarRating implements OpenMetadataEnum
{
    /**
     * Not recommended.
     */
    NOT_RECOMMENDED("cd72592e-cd29-4fb7-ae28-2da10542eb5e", 0, "X", "Not recommended", false),

    /**
     * Poor
     */
    ONE_STAR          ("10d17243-5000-49c0-84fe-f125c3a8557b", 1, "*", "Poor", false),

    /**
     * Usable
     */
    TWO_STARS         ("88bb3fb6-e5a4-4b9d-9039-c9551ef374aa", 2, "**", "Usable", false),

    /**
     * Good
     */
    THREE_STARS       ("f0f86c72-cc02-4202-9c31-e8e592d2e1cb", 3, "***", "Good", false),

    /**
     * Very good
     */
    FOUR_STARS        ("30694a0f-c142-4fac-8286-733cf92871fc", 4, "****", "Very Good", false),

    /**
     * Excellent
     */
    FIVE_STARS        ("4c1535c8-8e78-4dde-be9b-2a732f8609ee", 5, "*****", "Excellent", false);

    private static final String ENUM_TYPE_GUID  = "77fea3ef-6ec1-4223-8408-38567e9d3c93";
    private static final String ENUM_TYPE_NAME  = "StarRating";


    private static final String ENUM_DESCRIPTION = "Level of support or appreciation for an item.";
    private static final String ENUM_DESCRIPTION_GUID = "6c22fabf-077f-40c0-8739-eedf7c516927";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0150_FEEDBACK;

    private final String descriptionGUID;

    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    StarRating(String  descriptionGUID,
               int     ordinal,
               String  name,
               String  description,
               boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
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
    public String getName() { return name; }


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
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "StarRating{starRatingSymbol='" + name + '}';
    }
}