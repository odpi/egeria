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
 * A KeyPattern defines the type of External Identifier in use for an asset, or the type of Primary Key used within an
 * asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum KeyPattern implements OpenMetadataEnum
{
    /**
     * Unique key allocated and used within the scope of a single system.
     */
    LOCAL_KEY     ("b5bcf0a5-19a3-4755-ac52-518bd4783952",  0, "Local Key", "Unique key allocated and used within the scope of a single system.", false),

    /**
     * Key allocated and used within the scope of a single system that is periodically reused for different records.
     */
    RECYCLED_KEY  ("fe1e739e-4825-49a2-84af-aeb5096c6a75",  1, "Recycled Key", "Key allocated and used within the scope of a single system that " +
                                                                                "is periodically reused for different records.", false),

    /**
     * Key derived from an attribute of the entity, such as email address, passport number.
     */
    NATURAL_KEY   ("8828af2a-dcaa-4024-8b14-3d4082779d5c",  2, "Natural Key", "Key derived from an attribute of the entity, such as email address, passport number.", false),

    /**
     * Key value copied from another system.
     */
    MIRROR_KEY    ("840a9ccb-45d4-4569-b76b-b88c3a814d1a",  3, "Mirror Key", "Key value copied from another system.", false),

    /**
     * Key formed by combining keys from multiple systems.
     */
    AGGREGATE_KEY ("d9a3e37f-6bd8-414b-a37c-f24b5af08d27",  4, "Aggregate Key", "Key formed by combining keys from multiple systems.", false),

    /**
     * Key from another system can bey used if system name provided.
     */
    CALLERS_KEY   ("292fd669-f3c9-436e-af61-2a4ad184dbb7",  5, "Caller's Key", "Key from another system can bey used if system name provided.", false),

    /**
     * Key value will remain active even if records are merged.
     */
    STABLE_KEY    ("1820a91d-0d8a-4567-9fbd-d27d444f646a",  6, "Stable Key", "Key value will remain active even if records are merged.", false),

    /**
     * Another key pattern.
     */
    OTHER         ("d97a668e-bfc9-4ed6-a3a9-b133a4c2ef49", 99, "Other", "Another key pattern.", false);

    private static final String ENUM_TYPE_GUID  = "8904df8f-1aca-4de8-9abd-1ef2aadba300";
    private static final String ENUM_TYPE_NAME  = "KeyPattern";

    private static final String ENUM_DESCRIPTION = "Defines the type of identifier used for a digital resource.";
    private static final String ENUM_DESCRIPTION_GUID = "c6812f55-cc75-4e60-af29-74b53f03ddb5";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0017_EXTERNAL_IDENTIFIERS;

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
    KeyPattern(String  descriptionGUID,
               int     ordinal,
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
        return "KeyPattern{keyPatternName='" + name + '}';
    }
}