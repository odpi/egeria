/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LatestChangeTarget identifies the target of a change to an asset and its connected entities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum LatestChangeTarget implements Serializable
{
    ENTITY_PROPERTY           (0, 0, "EntityProperty",
                               "A property in the anchor entity has changed."),
    ENTITY_CLASSIFICATION     (1, 1, "EntityClassification",
                               "A classification attached to the anchor entity has changed."),
    ENTITY_RELATIONSHIP       (2, 2, "EntityRelationship",
                               "A relationship linking the anchor entity to an attachment has changed."),
    ATTACHMENT                (3, 3, "Attachment",
                               "An entity attached either directly or indirectly to the anchor entity has changed."),
    ATTACHMENT_PROPERTY       (4, 4, "AttachmentProperty",
                               "A property in an entity attached either directly or indirectly to the anchor entity has changed."),
    ATTACHMENT_CLASSIFICATION (5, 5, "AttachmentClassification",
                               "A classification attached to an entity that is, in turn, attached either directly or indirectly to " +
                                         "the anchor entity has changed."),
    ATTACHMENT_RELATIONSHIP   (6, 6, "AttachmentRelationship",
                               "A relationship linking to an entity that is, in turn, attached either directly or indirectly to " +
                                      "the anchor entity has changed."),
    OTHER                     (99, 99,  "Other",
                               "Another type of change.");

    public static final String ENUM_TYPE_GUID = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
    public static final String ENUM_TYPE_NAME = "LatestChangeTarget";

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
    LatestChangeTarget(int    ordinal,
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
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
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
        return "LatestChangeTarget{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
