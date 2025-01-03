/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AccessOperation enum defines the type of access granted to user to work with metadata elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AccessOperation
{
    /**
     * The user requests to read the metadata element in the catalog.
     */
    READ             (0,    "Read",         "The user requests to read the metadata element in the catalog."),

    /**
     * The user requests to create a metadata element in the catalog.
     */
    CREATE           (1,    "Create",        "The user requests to create a metadata element in the catalog."),

    /**
     * The user requests to change the description of the metadata element in the catalog.
     */
    UPDATE_PROPERTIES(2, "UpdateProperties", "The user requests to change the properties of the metadata element in the catalog."),

    /**
     * The user requests to change the status of the metadata element in the catalog.
     */
    UPDATE_STATUS(3, "Update", "The user requests to change the status of the metadata element in the catalog."),

    /**
     * The user requests to remove the metadata element from the catalog (archive, soft-delete or purge).
     */
    DELETE           (4,   "Delete",          "The user requests to remove the metadata element from the catalog (archive, soft-delete or purge)."),

    /**
     * The user requests to attach a non-anchored element to the metadata element in the catalog.
     */
    ATTACH           (5,   "Attach",          "The user requests to attach a non-anchored element to the metadata element in the catalog."),

    /**
     * The user requests to detach a non-anchored element from the metadata element in the catalog.
     */
    DETACH           (6,   "Detach",          "The user requests to detach a non-anchored element from the metadata element in the catalog."),

    /**
     * The user requests to attach an anchored element to the metadata element in the catalog.
     */
    ADD_MEMBER(7, "AddMember", "The user requests to attach an anchored element to the metadata element in the catalog."),

    /**
     * The user requests to remove an anchored element from the metadata element in the catalog.
     */
    DELETE_MEMBER(8, "DeleteMember", "The user requests to remove an anchored element from the metadata element in the catalog."),

    /**
     * The user requests to attach feedback elements (comments, tags, likes, reviews) to the metadata element in the catalog.
     */
    ADD_FEEDBACK(9, "AddFeedback", "The user requests to attach feedback elements (comments, tags, likes, reviews) to the metadata element in the catalog."),

    /**
     * The user requests to detach feedback elements (comments, tags, likes, reviews) from the metadata element in the catalog.
     */
    DELETE_FEEDBACK(10, "DeleteFeedback", "The user requests to detach feedback elements (comments, tags, likes, reviews) from the metadata element in the catalog."),

    /**
     * The user requests to attach a classification to the metadata element in the catalog, or update an existing classification's properties.
     */
    CLASSIFY(11, "Classify", "The user requests to attach a classification to the metadata element in the catalog, or update an existing classification's properties."),

    /**
     * The user requests to remove a classification from the metadata element in the catalog.
     */
    DECLASSIFY(12, "Declassify", "The user requests to remove a classification from the metadata element in the catalog."),

    ;

    private  final int    ordinal;
    private  final String name;
    private  final String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name     String name
     * @param description String description
     */
    AccessOperation(int     ordinal,
                    String  name,
                    String  description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "AccessOperation{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
