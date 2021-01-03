/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogEventType describes the type of event that the WatchdogGovernanceActionService can listen for.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum WatchdogEventType
{
    NEW_ELEMENT                       ( 0,  "New Element",                     "A new metadata element has been created in the metadata store."),
    REFRESHED_ELEMENT                 ( 1,  "Refreshed Element",               "A metadata element has been distributed around the cohort - could be for the first time."),
    UPDATED_ELEMENT_PROPERTIES        ( 2,  "Updated Element Properties",      "The properties of an existing metadata element have been updated."),
    DELETED_ELEMENT                   ( 3,  "Deleted Element",                 "A metadata element has been deleted."),
    NEW_CLASSIFICATION                ( 4,  "New Classification",              "A new classification has been added to a metadata element."),
    UPDATED_CLASSIFICATION_PROPERTIES ( 5,  "Updated Classification",          "The properties of one of the classifications attached to a metadata element have been updated."),
    DELETED_CLASSIFICATION            ( 6,  "Deleted Classification",          "One of the classifications has been removed from a metadata element."),
    NEW_RELATIONSHIP                  ( 7,  "New Relationship",                "A new relationship linking two metadata elements together has been created."),
    REFRESHED_RELATIONSHIP            ( 8,  "Refreshed Relationship",          "A relationship linking two metadata elements together has been distributed around the cohort - could be for the first time."),
    UPDATED_RELATIONSHIP_PROPERTIES   ( 9,  "Updated Relationship Properties", "The properties of a relationship linking two metadata elements have been updated."),
    DELETED_RELATIONSHIP              (10,  "Deleted Relationship",            "A relationship between two metadata elements has been deleted.");

    private static final long serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name String name
     * @param description String description
     */
    WatchdogEventType(int     ordinal,
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
        return "WatchdogEventType{" +
                       "ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }
}
