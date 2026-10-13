/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describes why a relationship type exists.  The purpose is informational for the repositories.
 * Open metadata uses it to decide how far an operation that starts on an anchored element
 * (delete, archive, create from template) navigates through the anchored graph: only
 * STRUCTURAL and ATTACHMENT relationships are followed, and only to elements that share the same anchor.
 * It is also useful when visualizing the relationships around an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataRelationshipPurpose implements OpenMetadataEnum
{
    /**
     * Part of what the element is.  When an anchor operation starts on an anchored element, it is followed to elements in the same anchored graph.
     */
    STRUCTURAL(0, "deddbad4-b0cc-4060-998d-ac5277cbefb0", "Structural",
               "Part of what the element is.  When an anchor operation starts on an anchored element, it is followed to elements in the same anchored graph.",
               true, false),

    /**
     * Information attached to, and owned by, the element.  Followed with STRUCTURAL relationships under the same conditions.
     */
    ATTACHMENT(1, "5d8e4673-2efd-47e2-b75f-e8ea8149782a", "Attachment",
               "Information attached to, and owned by, the element.  Followed with STRUCTURAL relationships under the same conditions.",
               true, false),

    /**
     * Links the element to independent information that describes or governs it.
     */
    ASSIGNMENT(2, "939ecb8a-f511-42fe-8fb4-4e9cf53dc514", "Assignment",
               "Links the element to independent information that describes or governs it.",
               false, false),

    /**
     * Groups or organizes elements without owning them.
     */
    MEMBERSHIP(3, "92f7b4a6-6683-40be-b020-a5713151992d", "Membership",
               "Groups or organizes elements without owning them.",
               false, false),

    /**
     * Relates elements that keep their own identity, often in one direction.
     */
    ASSOCIATION(4, "552a1cfd-5806-49b3-8e2a-548a3cd48a93", "Association",
                "Relates elements that keep their own identity, often in one direction.",
                false, true),

    /**
     * A purely peer link between elements of equal standing.
     */
    PEER(5, "eab0d30e-44ba-47e4-a202-377b506c0d22", "Peer",
         "A purely peer link between elements of equal standing.",
         false, false),

    /**
     * Links a design or specification to its implementation.
     */
    DEFINITION(6, "e2ce3f9e-4683-47a5-91bd-18dd5fe35e7d", "Definition",
               "Links a design or specification to its implementation.",
               false, false),

    /**
     * Where something runs or is stored.
     */
    DEPLOYMENT(7, "0a1c9b07-be73-426b-a4fa-7db39fbcaac6", "Deployment",
               "Where something runs or is stored.",
               false, false),

    /**
     * How data and control are designed to flow (LineageRelationship and its subtypes).
     */
    DESIGN_LINEAGE(8, "ce1835de-eac1-43ce-9d61-10c3bb005031", "DesignLineage",
                   "How data and control are designed to flow (LineageRelationship and its subtypes).",
                   false, false),

    /**
     * Information gathered so that action can be taken.
     */
    ACTION(9, "5be93599-ce34-420d-a5e1-581cde216df0", "Action",
           "Information gathered so that action can be taken.",
           false, false),

    /**
     * A record of action that took place.
     */
    ACTIVITY(10, "47c05d46-566c-427a-b2a7-a2e1d360bce3", "Activity",
             "A record of action that took place.",
             false, false),
    ;


    private static final String ENUM_TYPE_GUID  = "71eac90c-5888-4018-8c6c-036a7edc8104";
    private static final String ENUM_TYPE_NAME  = "OpenMetadataRelationshipPurpose";

    private static final String ENUM_DESCRIPTION = "Describes why a relationship type exists, and so whether operations starting on an anchored element follow it.";
    private static final String ENUM_DESCRIPTION_GUID = "f6bbdb46-a3e4-4f37-ab57-75c704b83984";
    private static final String ENUM_DESCRIPTION_WIKI = "https://egeria-project.org/concepts/relationship-purpose/";

    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  descriptionGUID;
    private final boolean partOfAnchoredGraph;
    private final boolean isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param partOfAnchoredGraph is this relationship followed by operations that start on an anchored element?
     * @param isDefault is this the default value for the enum?
     */
    OpenMetadataRelationshipPurpose(int     ordinal,
                                    String  descriptionGUID,
                                    String  name,
                                    String  description,
                                    boolean partOfAnchoredGraph,
                                    boolean isDefault)
    {
        this.ordinal             = ordinal;
        this.name                = name;
        this.descriptionGUID     = descriptionGUID;
        this.description         = description;
        this.partOfAnchoredGraph = partOfAnchoredGraph;
        this.isDefault           = isDefault;
    }


    /**
     * Return the purpose with the supplied enum name (for example, as stored in a RelationshipDef).
     *
     * @param purposeName name of the enum value
     * @return matching enum value or null if the name is null or not recognized
     */
    public static OpenMetadataRelationshipPurpose fromName(String purposeName)
    {
        if (purposeName != null)
        {
            for (OpenMetadataRelationshipPurpose purpose : OpenMetadataRelationshipPurpose.values())
            {
                if (purpose.name().equals(purposeName))
                {
                    return purpose;
                }
            }
        }

        return null;
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
    public String getDisplayName() { return name; }


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
     * Return whether an operation (delete, archive, create from template) that starts on an anchored element
     * follows relationships with this purpose to other elements in the same anchored graph.
     *
     * @return boolean
     */
    public boolean isPartOfAnchoredGraph()
    {
        return partOfAnchoredGraph;
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
     * Return the link to the documentation for the open metadata enum type that this enum class represents.
     *
     * @return string url
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
        return "OpenMetadataRelationshipPurpose{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", partOfAnchoredGraph=" + partOfAnchoredGraph +
            ", isDefault=" + isDefault +
            '}';
    }
}
