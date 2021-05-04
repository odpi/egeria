/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProfileInboundEventType describes the different types of events that may be received by the
 * Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityProfileInboundEventType implements Serializable
{
    UNKNOWN_COMMUNITY_PROFILE_EVENT           (0,   "Unknown Event",
                                               "An event that is not recognized by the local server."),
    CREATE_USER_IDENTITY_EVENT                (1,   "Create User Identity",
                                               "Request to add a new user identity."),
    CREATE_REF_USER_IDENTITY_EVENT            (2,   "Create Reference User Identity",
                                               "Request to add a user identity managed by an external system."),
    UPDATE_USER_IDENTITY_EVENT                (3,   "Update User Identity",
                                               "Request to update a user identity."),
    DELETE_USER_IDENTITY_EVENT                (4,   "Delete User Identity",
                                               "Request to delete a user identity."),
    CREATE_PERSONAL_PROFILE_EVENT             (10,   "New Personal Profile",
                                               "Request to add a new personal profile."),
    CREATE_REF_PERSONAL_PROFILE_EVENT         (11,   "New Reference Personal Profile",
                                               "Request to add a personal profile managed by an external system."),
    UPDATE_PERSONAL_PROFILE_EVENT             (12,   "Update Personal Profile",
                                               "Request to update a personal profile."),
    DELETE_PERSONAL_PROFILE_EVENT             (13,   "Delete Personal Profile",
                                               "Request to delete an existing personal profile."),
    ADD_ASSET_TO_COLLECTION_EVENT             (20,  "Add Asset to Collection",
                                               "Add a new asset to a personal collection."),
    REMOVE_ASSET_FROM_COLLECTION_EVENT        (21,  "Remove Asset from Collection",
                                               "Remove an asset from a personal collection."),
    ADD_PROJECT_TO_COLLECTION_EVENT           (22,  "Add Project to Collection",
                                               "Add a new project to a personal collection."),
    REMOVE_PROJECT_FROM_COLLECTION_EVENT      (23,  "Project Removed from Collection",
                                               "Remove a project from a personal collections."),
    ADD_COMMUNITY_TO_COLLECTION_EVENT         (24,  "New CommunityProperties In Collection",
                                               "A new community has been added to a personal collections"),
    REMOVE_COMMUNITY_FROM_COLLECTION_EVENT    (25,  "CommunityProperties Removed From Collection",
                                               "An community has been removed from one of the personal collection."),
    RESOURCE_IN_COLLECTION_EVENT              (30,  "New ResourceProperties In Collection",
                                               "A new community has been added to a sharable collection."),
    RESOURCE_REMOVED_FROM_COLLECTION_EVENT    (31,  "ResourceProperties Removed From Collection",
                                               "An resource has been removed from a shareable collection.");

    private static final long     serialVersionUID = 1L;

    private  int      eventTypeCode;
    private  String   eventTypeName;
    private  String   eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode - int identifier used for indexing based on the enum.
     * @param eventTypeName - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                                     bundle is not available.
     */
    CommunityProfileInboundEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
    {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getEventTypeCode()
    {
        return eventTypeCode;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getEventTypeName()
    {
        return eventTypeName;
    }


    /**
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getEventTypeDescription()
    {
        return eventTypeDescription;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityProfileInboundEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
