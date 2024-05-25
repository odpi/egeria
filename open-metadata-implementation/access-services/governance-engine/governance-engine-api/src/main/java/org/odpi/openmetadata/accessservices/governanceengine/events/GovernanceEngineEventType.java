/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEventType describes the different types of events produced by the Governance Engine OMAS.
 * Events are used to configure the governance servers supporting governance engines (in Egeria these are
 * Open Metadata Engine Services (OMES) running in an Engine Host OMAG Server).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceEngineEventType
{
    /**
     * An event that is not recognized by the local server.
     */
    UNKNOWN_GOVERNANCE_ENGINE_EVENT      (0,
                                         "Unknown Event",
                                         "An event that is not recognized by the local server."),

    /**
     * Metadata change event for watchdog governance action services.
     */
    WATCHDOG_GOVERNANCE_SERVICE_EVENT    (3,
                                          "Metadata change event",
                                          "Metadata change event for watchdog governance action services."),

    ;

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
    GovernanceEngineEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "GovernanceEngineEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
