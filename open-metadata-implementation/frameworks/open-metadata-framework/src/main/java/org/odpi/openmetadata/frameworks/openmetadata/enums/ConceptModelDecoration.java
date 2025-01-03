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
 * ConceptModelDecoration describes the type of relationship end.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConceptModelDecoration implements OpenMetadataEnum
{
    /**
     * The business capability has not been classified.
     */
    NONE     (0,  "a4c4b33e-4013-4353-9b30-5ae817d7d04d",  "None",    "The relationship links two concept beads together.", true),

    /**
     * A functional business capability.
     */
    AGGREGATION  (1,  "270e50f3-730c-46c8-9fd4-0c87aea5a72d",  "Aggregation", "The relationship links an independent concept bead to a collection of concept beads.", false),

    /**
     * A collection of related business services.
     */
    COMPOSITION  (2,  "3bae54fa-8eae-4890-959c-c5d3cc01bda9",  "Composition", "The relationship links a concept bead the other concept beads that describe its parts.", false),

    /**
     * Another business capability type.
     */
    EXTENSION  (3,  "349fe0bc-9972-49f7-8f7f-91158dab6c99",  "Extension", "The relationship links an extension to a base concept bead.", false),

    ;

    private static final String ENUM_TYPE_GUID  = "a97d9167-7dd6-4dea-a8cf-c73c57a0f470";
    private static final String ENUM_TYPE_NAME  = "ConceptModelDecoration";

    private static final String ENUM_DESCRIPTION = "Describes the type of relationship end in a concept model.";
    private static final String ENUM_DESCRIPTION_GUID = "50671892-e2be-43d6-bcee-d82ece6a8c19";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0571_CONCEPT_MODELS;

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
    ConceptModelDecoration(int     ordinal,
                           String  descriptionGUID,
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "BusinessCapabilityType : " + name;
    }
}
