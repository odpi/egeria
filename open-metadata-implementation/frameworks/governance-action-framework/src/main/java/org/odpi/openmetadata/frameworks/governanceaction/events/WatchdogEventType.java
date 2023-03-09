/* SPDX-License-Identifier: Apache-2.0 */
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
    UNKNOWN_EVENT                     ( 0,  "Unknown Event",         "An event that is not recognized by the local server."),

    NEW_ELEMENT                       ( 1,  "New Element",                     "A new metadata element has been created in the metadata store."),
    REFRESHED_ELEMENT                 ( 2,  "Refreshed Element",               "A metadata element has been distributed around the cohort - could be for the first time."),
    UPDATED_ELEMENT_PROPERTIES        ( 3,  "Updated Element Properties",      "The properties of an existing metadata element have been updated."),
    NEW_CLASSIFICATION                ( 4,  "New Classification",              "A new classification has been added to a metadata element."),
    UPDATED_CLASSIFICATION_PROPERTIES ( 5,  "Updated Classification",          "The properties of one of the classifications attached to a metadata element have been updated."),
    DELETED_CLASSIFICATION            ( 6,  "Deleted Classification",          "One of the classifications has been removed from a metadata element."),
    DELETED_ELEMENT                   ( 7,  "Deleted Element",                 "A metadata element has been deleted."),
    ELEMENT_RESTORED                  ( 8,  "Element Restored",                "An element that was once deleted has been restored."),
    NEW_RELATIONSHIP                  ( 9,  "New Relationship",                "A new relationship linking two metadata elements together has been created."),
    REFRESHED_RELATIONSHIP            (10,  "Refreshed Relationship",          "A relationship linking two metadata elements together has been distributed around the cohort - could be for the first time."),
    UPDATED_RELATIONSHIP_PROPERTIES   (11,  "Updated Relationship Properties", "The properties of a relationship linking two metadata elements have been updated."),
    RELATIONSHIP_GUID_CHANGED         (12,  "Relationship GUID Changed",       "An relationship's GUID has changed."),
    RELATIONSHIP_TYPE_CHANGED         (13,  "Relationship Type Changed",       "An relationship's type has changed."),
    RELATIONSHIP_HOME_CHANGED         (14,  "Relationship Home Changed",       "An relationship's home has changed."),
    DELETED_RELATIONSHIP              (15,  "Deleted Relationship",            "A relationship between two metadata elements has been deleted."),

    ;

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
