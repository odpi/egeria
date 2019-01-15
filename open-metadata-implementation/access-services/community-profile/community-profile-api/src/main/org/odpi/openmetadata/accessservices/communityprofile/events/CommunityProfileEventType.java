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
 * CommunityProfileEventType describes the different types of events produced by the Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityProfileEventType implements Serializable
{
    UNKNOWN_COMMUNITY_PROFILE_EVENT           (0,   "Unknown Event",
                                                    "An event that is not recognized by the local server."),
    NEW_PERSONAL_PROFILE_EVENT                (1,   "New Personal Profile",
                                                    "A new personal profile has been added through open metadata."),
    NEW_REF_PERSONAL_PROFILE_EVENT            (2,   "New Reference Personal Profile",
                                                    "A personal profile managed by an external system has been added through open metadata."),
    UPDATED_PERSONAL_PROFILE_EVENT            (3,   "Updated Personal Profile",
                                                    "A personal profile has been updated."),
    DELETED_PERSONAL_PROFILE_EVENT            (4,   "Deleted Personal Profile",
                                                    "An existing personal profile has been deleted."),
    NEW_ASSET_IN_COLLECTION_EVENT             (20,  "New Asset In Collection",
                                                    "A new asset has been added to one of the personal collections."),
    ASSET_REMOVED_FROM_COLLECTION_EVENT       (21,  "Asset Removed From Collection",
                                                    "An asset has been removed from one of the personal collections."),
    NEW_PROJECT_IN_COLLECTION_EVENT           (22,  "New Project In Collection",
                                                    "A new project has been added to one of the personal collections."),
    PROJECT_REMOVED_FROM_COLLECTION_EVENT     (23,  "Project Removed From Collection",
                                                    "An project has been removed from one of the personal collections."),
    NEW_COMMUNITY_IN_COLLECTION_EVENT         (24,  "New Community In Collection",
                                                    "A new community has been added to one of the personal collections."),
    COMMUNITY_REMOVED_FROM_COLLECTION_EVENT   (25,  "Community Removed From Collection",
                                                    "An community has been removed from one of the personal collections."),
    RESOURCE_IN_COLLECTION_EVENT              (30,  "New Community In Collection",
                                                    "A new community has been added to one of the sharable collections."),
    RESOURCE_REMOVED_FROM_COLLECTION_EVENT    (31,  "Resource Removed From Collection",
                                                    "An resource has been removed from one of the shareable collections.");



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
    CommunityProfileEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "AssetConsumerEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
