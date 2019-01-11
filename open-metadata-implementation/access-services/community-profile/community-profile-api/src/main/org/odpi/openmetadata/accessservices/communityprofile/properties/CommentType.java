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
 * The CommentType allows comments to be used to ask and answer questions as well as make suggestions and
 * provide useful information to other users.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommentType implements Serializable
{
    STANDARD_COMMENT (0, "Comment", "General comment about the asset."),
    QUESTION         (1, "Question", "Asks a question to the people owning, managing or using the asset."),
    ANSWER           (2, "Answer", "Answers a question (posted as a reply to the question)."),
    SUGGESTION       (3, "Suggestion", "Provides a suggestion on how to improve the asset or its properties and description."),
    USAGE_EXPERIENCE (4, "Experience", "Describes situations where this asset has been used and related hints and tips.");

    private static final long     serialVersionUID = 1L;

    private int    ordinal;
    private String name;
    private String description;


    /**
     * Typical Constructor
     */
    CommentType(int ordinal, String name, String description)
    {
        /*
         * Save the values supplied
         */
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CommentType : " + name;
    }
}