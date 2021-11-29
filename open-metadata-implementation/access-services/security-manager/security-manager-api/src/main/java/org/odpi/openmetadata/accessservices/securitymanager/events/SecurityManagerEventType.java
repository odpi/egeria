/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityManagerEventType describes the different types of events produced by the Security Manager OMAS.
 * Events are limited to assets that are in the zones listed in the supportedZones property
 * passed to the Security Manager OMAS at start up (a null value here means all zones).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SecurityManagerEventType implements Serializable
{
    UNKNOWN_SECURITY_MANAGER_EVENT    (0,  "Unknown Event",  "An event that is not recognized by the local server."),

    NEW_SECURITY_GROUP_EVENT          (1,  "New Security Group Event", "A new security group definition has been created."),
    UPDATED_SECURITY_GROUP_EVENT      (2,  "Updated Security Group Event", "The properties of a security group definition have been updated. "),
    DELETED_SECURITY_GROUP_EVENT      (3,  "Deleted Security Group Event", "A security group definition has been deleted. "),

    NEW_USER_IDENTITY_EVENT           (11, "New User Identity Event", "A new user identity has been created."),
    UPDATED_USER_IDENTITY_EVENT       (12, "Updated User Identity Event", "The properties of a user identity have been updated. "),
    GROUP_CHANGE_USER_IDENTITY_EVENT  (13, "Group Change User Identity Event", "The groups of a user identity have been updated. "),
    DELETED_USER_IDENTITY_EVENT       (14, "Deleted User Identity Event", "A user identity has been deleted. "),

    NEW_SECURITY_TAG_EVENT            (21,  "New Security Tag Event", "A new security tag definition has been attached to an element."),
    UPDATED_SECURITY_TAG_EVENT        (22,  "Updated Security Tag Event", "The properties of a security tag classification have been updated. "),
    DELETED_SECURITY_TAG_EVENT        (23,  "Deleted Security Tag Event", "A security tag classification has been removed from an element. ");

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
    SecurityManagerEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "SecurityManagerEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
