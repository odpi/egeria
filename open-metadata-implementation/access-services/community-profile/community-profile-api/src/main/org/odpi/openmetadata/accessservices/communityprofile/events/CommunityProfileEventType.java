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
 *
 * Events are limited to assets that are in the zones listed in the accessPointZones property
 * passed to the Asset Consumer OMAS at start up (a null value here means all zones).
 *
 * In addition each asset can be associated with asset collections.  Asset collections can be linked to
 * an individual's profile, projects and communities.   The Asset Consumer OMAS sends generic notifications
 * when assets that are members of the access point zones are added and deleted from these collections.
 *
 * In addition, individuals can set up personal notifications related to specific assets and asset collections.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityProfileEventType implements Serializable
{
    UNKNOWN_ASSET_CONSUMER_EVENT        (0,  "Unknown Event",                 "An event that is not recognized by the local server."),
    NEW_ASSET_EVENT                     (1,  "New Asset",                     "A new asset has been added to one of the access point zones."),
    UPDATED_ASSET_EVENT                 (2,  "Updated Asset",                 "An existing asset from one of the access point zones has been updated."),
    DELETED_ASSET_EVENT                 (3,  "Deleted Asset",                 "An existing asset has been deleted."),
    NEW_ASSET_IN_COLLECTION_EVENT       (4,  "New Asset In Collection",       "A new asset has been added to one of the asset collections."),
    ASSET_REMOVED_FROM_COLLECTION_EVENT (5,  "Asset Removed From Collection", "An asset has been removed from one of the asset collections.");



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
