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
 * CommunityProfileOutboundEventType describes the different types of events produced by the Community Profile OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityProfileOutboundEventType implements Serializable
{
    UNKNOWN_COMMUNITY_PROFILE_EVENT           (0,   "Unknown Event",
                                               "An event that is not recognized by the local server."),
    NEW_ELEMENT_EVENT                         (1,   "New Element",
                                               "A new element has been added through open metadata."),
    UPDATED_ELEMENT_EVENT                     (2,   "Updated Element",
                                               "An element has been updated."),
    DELETED_ELEMENT_EVENT                     (3,   "Deleted Element",
                                               "An existing user identity has been deleted."),
    CLASSIFIED_ELEMENT_EVENT                  (4,   "Classified Element",
                                               "A new classification has been added to an element through open metadata."),
    RECLASSIFIED_ELEMENT_EVENT                (5,   "Reclassified Element",
                                               "A classification attached to an element has been updated."),
    DECLASSIFIED_ELEMENT_EVENT                (6,   "Declassified element",
                                               "An existing classification has been removed from an element."),
    KARMA_POINT_PLATEAU_EVENT                 (10,  "Karma Point Plateau",
                                               "An individual has passed a karma point plateau.");

    private static final long serialVersionUID = 1L;

    private int    eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode int identifier used for indexing based on the enum.
     * @param eventTypeName string name used for messages that include the enum.
     * @param eventTypeDescription default description for the enum value - used when natural language resource bundle is not available.
     */
    CommunityProfileOutboundEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "CommunityProfileOutboundEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
