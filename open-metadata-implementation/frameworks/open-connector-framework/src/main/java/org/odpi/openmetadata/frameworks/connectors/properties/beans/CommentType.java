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
 * The CommentType allows comments to be used to ask and answer questions as well as make suggestions and
 * provide useful information to other users.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommentType implements Serializable
{
    STANDARD_COMMENT (0,  0, "Comment", "General comment about the asset."),
    QUESTION         (1,  1, "Question", "Asks a question to the people owning, managing or using the asset."),
    ANSWER           (2,  2, "Answer", "Answers a question (posted as a reply to the question)."),
    SUGGESTION       (3,  3, "Suggestion", "Provides a suggestion on how to improve the asset or its properties and description."),
    USAGE_EXPERIENCE (4,  4, "Experience", "Describes situations where this asset has been used and related hints and tips."),
    OTHER            (99, 99, "Other", "Unknown comment type.");

    private static final long     serialVersionUID = 1L;

    public static final String ENUM_TYPE_GUID  = "06d5032e-192a-4f77-ade1-a4b97926e867";
    public static final String ENUM_TYPE_NAME  = "CommentType";

    private int    openTypeOrdinal;

    private int    ordinal;
    private String name;
    private String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    CommentType(int    ordinal,
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
     * @return int comment type code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default type name for this enum instance.
     *
     * @return String default type name
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
        return "CommentType{" +
                "commentTypeCode=" + ordinal +
                ", commentType='" + name + '\'' +
                ", commentTypeDescription='" + description + '\'' +
                '}';
    }
}