/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

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
public enum DataManagerOutboundEventType implements Serializable
{
    UNKNOWN_DATA_MANAGER_EVENT                (0,  "Unknown Event",
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
                                               "An existing classification has been removed from an element.");

    private static final long     serialVersionUID = 1L;

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
