/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceserver.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceServerEventType describes the different types of events produced by the Governance Server OMAS.
 * Events are used to configure the Integration Daemon and Engine Host governance servers.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceServerEventType implements Serializable
{
    /**
     * An event that is not recognized by the local server.
     */
    UNKNOWN_GOVERNANCE_SERVER_EVENT(0,
                                    "Unknown Event",
                                    "An event that is not recognized by the local server."),

    /**
     * The configuration for a Governance Engine has changed.
     */
    REFRESH_GOVERNANCE_ENGINE_EVENT      (1,
                                         "Refresh Governance Engine Configuration",
                                         "The configuration for a Governance Engine has changed."),

    /**
     * The configuration for a governance service has changed.
     */
    REFRESH_GOVERNANCE_SERVICE_EVENT     (2,
                                          "Refresh Governance Service Configuration",
                                          "The configuration for a governance service has changed."),

    /**
     * The configuration for an Integration Group has changed.
     */
    REFRESH_INTEGRATION_GROUP_EVENT      (1,
                                          "Refresh Integration Group Configuration",
                                          "The configuration for an Integration Group has changed."),

    /**
     * The configuration for an integration connector has changed.
     */
    REFRESH_INTEGRATION_CONNECTOR_EVENT     (2,
                                          "Refresh Integration Connector Configuration",
                                          "The configuration for am Integration Connector has changed."),


    /**
     * New request to run a governance service.
     */
    REQUESTED_ENGINE_ACTION_EVENT(5,
                                  "Requested Engine Action",
                                  "New request to run a governance service."),

    /**
     * New request to run a governance service.
     */
    CANCELLED_ENGINE_ACTION_EVENT(5,
                                  "Cancelled Engine Action",
                                  "Request to stop a governance service."),

    ;

    private static final long     serialVersionUID = 1L;

    private  final int      eventTypeCode;
    private  final String   eventTypeName;
    private  final String   eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode - int identifier used for indexing based on the enum.
     * @param eventTypeName - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                                     bundle is not available.
     */
    GovernanceServerEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "GovernanceServerEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
