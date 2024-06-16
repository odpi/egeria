/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataManagerOutboundEventType describes the different types of events produced by the Data Manager OMAS.
 * Events are limited to assets that are in the zones listed in the supportedZones property
 * passed to the Data Manager OMAS at start up (a null value here means all zones).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataManagerOutboundEventType
{
    UNKNOWN_EVENT          (0,  "Unknown Event",         "An event that is not recognized by the local server."),
    REFRESH_ELEMENT_EVENT  (1,  "Refresh Element",       "An element has been distributed around the cohort - could be for the first time."),
    NEW_ELEMENT_CREATED    (2,  "New Element",           "A new element has been created."),
    ELEMENT_UPDATED        (3,  "Element Updated",       "An element's properties has been updated."),
    ELEMENT_DELETED        (4,  "Element Deleted",       "An element and all its anchored elements have been deleted."),
    ELEMENT_CLASSIFIED     (5,  "Element Classified",    "A classification has been added to an element."),
    ELEMENT_RECLASSIFIED   (6,  "Element Reclassified",  "The properties for a classification attached to an element have been updated."),
    ELEMENT_DECLASSIFIED   (7,  "Element Declassified",  "A classification has been removed from an element."),
    ELEMENT_RESTORED       (8,  "Element Restored",      "An element that was once deleted has been restored."),
    ELEMENT_GUID_CHANGED   (9,  "Element GUID Changed",  "An element's GUID has changed."),
    ELEMENT_TYPE_CHANGED   (10, "Element Type Changed",  "An element's type has changed."),
    ELEMENT_HOME_CHANGED   (11, "Element Home Changed",  "An element's home has changed."),
    ;

    private final int      eventTypeCode;
    private final String   eventTypeName;
    private final String   eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode - int identifier used for indexing based on the enum.
     * @param eventTypeName - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                                     bundle is not available.
     */
    DataManagerOutboundEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "DataManagerOutboundEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
