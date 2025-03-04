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
 * LatestChangeTarget identifies the target of a change to an asset and its connected entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum LatestChangeTarget implements OpenMetadataEnum
{
    /**
     * The status of the anchor entity has changed.
     */
    ENTITY_STATUS           ("b8355e0a-e7b4-4c7c-bc3c-7915e71a9830", 0, "EntityStatus",
                               "The status of the anchor entity has changed.", false),

    /**
     * A property in the anchor entity has changed.
     */
    ENTITY_PROPERTY           ("951fd3a0-98c9-457e-bc65-e0fb50d20bf3", 1, "EntityProperty",
                               "A property in the anchor entity has changed.", false),

    /**
     * A classification attached to the anchor entity has changed.
     */
    ENTITY_CLASSIFICATION     ("3296c857-0b19-4d18-80ac-c98b9fb33965", 2, "EntityClassification",
                               "A classification attached to the anchor entity has changed.", false),

    /**
     * A relationship linking the anchor entity to an attachment has changed.
     */
    ENTITY_RELATIONSHIP       ("a7fdc77d-2cb3-439d-ad73-e682ee568a40", 3, "EntityRelationship",
                               "A relationship linking the anchor entity to an attachment has changed.", false),

    /**
     * An entity attached either directly or indirectly to the anchor entity has changed.
     */
    ATTACHMENT                ("f39de9c0-e97b-4225-898a-fd278c9e5394", 4, "Attachment",
                               "An entity attached either directly or indirectly to the anchor entity has changed.", false),

    /**
     * The status of an entity attached either directly or indirectly to the anchor entity has changed.
     */
    ATTACHMENT_STATUS       ("088d5d94-121e-4ff5-bce6-faf313dace4f", 5, "AttachmentStatus",
                               "The status of an entity attached either directly or indirectly to the anchor entity has changed.", false),

    /**
     * A property in an entity attached either directly or indirectly to the anchor entity has changed.
     */
    ATTACHMENT_PROPERTY       ("adaf970b-081c-4f79-844d-3b742b8c761d", 6, "AttachmentProperty",
                               "A property in an entity attached either directly or indirectly to the anchor entity has changed.", false),

    /**
     * A classification attached to an entity that is, in turn, attached either directly or indirectly to the anchor entity has changed.
     */
    ATTACHMENT_CLASSIFICATION ("3f12a872-5613-491b-b104-4aa74cde50b5", 7, "AttachmentClassification",
                               "A classification attached to an entity that is, in turn, attached either directly or indirectly to " +
                                         "the anchor entity has changed.", false),

    /**
     * A relationship linking to an entity that is, in turn, attached either directly or indirectly to the anchor entity has changed.
     */
    ATTACHMENT_RELATIONSHIP   ("3fa91775-76ca-4c95-87e1-0cb3ad34cec0", 8, "AttachmentRelationship",
                               "A relationship linking to an entity that is, in turn, attached either directly or indirectly to " +
                                      "the anchor entity has changed.", false),

    /**
     * Another type of change.
     */
    OTHER                     ("6ba778bd-7f30-4a9b-8592-e7740c8ca493", 99,  "Other",
                               "Another type of change.",false);

    private static final String ENUM_TYPE_GUID = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
    private static final String ENUM_TYPE_NAME = "LatestChangeTarget";
    private static final String ENUM_DESCRIPTION = "Defines the type of repository element that has changed.";
    private static final String ENUM_DESCRIPTION_GUID = "868fee98-4d44-46a6-815e-3a507cf864b8";
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
    LatestChangeTarget(String  descriptionGUID,
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
        return "LatestChangeTarget{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
