/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * The CommentType allows comments to be used to ask and answer questions as well as make suggestions and
 * provide useful information to other users.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommentType implements OpenMetadataEnum
{
    /**
     * Comment - General comment about the element.
     */
    STANDARD_COMMENT (0,  "4bbcf72c-4c88-482b-8331-07422542d5c0", "Comment", "General comment about the element.", true),

    /**
     * Question - Asks a question to the people owning, managing or using the element.
     */
    QUESTION         (1,  "e0f0bb6d-ebc2-4d9d-8a76-95d26cd67b22", "Question", "Asks a question to the people owning, managing or using the element.", false),

    /**
     * Answer - Answers a question (posted as a reply to the question).
     */
    ANSWER           (2,  "ed115faa-aefc-4328-a196-1e0336d3ff2b", "Answer", "Answers a question (posted as a reply to the question).", false),

    /**
     * Suggestion - Provides a suggestion on how to improve the element or its properties and description.
     */
    SUGGESTION       (3,  "ae22e098-29e3-46d5-a733-7c411276dc5d", "Suggestion", "Provides a suggestion on how to improve the element or its properties and description.", false),

    /**
     * Experience - Describes situations where this asset has been used and related hints and tips.
     */
    USAGE_EXPERIENCE (4,  "072f1357-f9fc-45a1-8c9d-27ee400ad2b2", "Experience", "Describes situations where this asset has been used and related hints and tips.", false),

    /**
     * Requirement - Describes mandatory changes that must be made to the attached element(s).
     */
    REQUIREMENT (5,  "d1a1fc66-5a34-4f4b-99c3-91c4296e6c11", "Requirement", "Describes mandatory changes that must be made to the attached element(s).", false),

    /**
     * Other - Unknown comment type.
     */
    OTHER            (99, "dea55b37-ba37-4aff-a193-424a67b99111", "Other", "Unknown comment type.", false);


    private static final String ENUM_TYPE_GUID  = "06d5032e-192a-4f77-ade1-a4b97926e867";
    private static final String ENUM_TYPE_NAME  = "CommentType";

    private static final String ENUM_DESCRIPTION = "Defines the sequential order in which bytes are arranged into larger numerical values when stored in memory or when transmitted over digital links.";
    private static final String ENUM_DESCRIPTION_GUID = "5e8e769a-9c8b-4a84-9006-53c49d6efc72";
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
    CommentType(int     ordinal,
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.COMMENT_TYPE.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueNamespace(ENUM_TYPE_NAME,
                                            OpenMetadataProperty.COMMENT_TYPE.name,
                                            null);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CommentType{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';

    }
}
