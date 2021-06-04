/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataEngineEventType describes the different types of events can be consumed by the Data Engine OMAS.
 * Events are limited to assets that are in the zones listed in the supportedZones property
 * passed to the Data Engine OMAS at start up (a null value here means all zones).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataEngineEventType implements Serializable
{
    UNKNOWN_DATA_ENGINE_EVENT               (0,   "Unknown Data Engine Job Event",  "An event that is not recognized by the local server."),
    DATA_ENGINE_REGISTRATION_EVENT          (1,   "DataEngineRegistrationEvent",      "An event that register a data engine as external source."),
    LINEAGE_MAPPINGS_EVENT                  (2,   "LineageMappingsEvent",  "An event that add or update lineage mappings."),
    PORT_ALIAS_EVENT                        (3,   "PortAliasEvent",      "An event that add or update port alias"),
    PORT_IMPLEMENTATION_EVENT               (4,   "PortImplementationEvent",  "An event that create or update port implementations."),
    PROCESSES_EVENT                         (6,   "ProcessesEvent",  "An event that create or update processes."),
    SCHEMA_TYPE_EVENT                       (7,   "SchemaTypeEvent",  "An event that create or update schema types."),
    PROCESS_HIERARCHY_EVENT                 (8,   "ProcessHierarchyEvent", "An event to setup a process hierarchy."),
    DELETE_PROCESSES_EVENT                  (9,   "DeleteProcessesEvent",  "An event that deletes processes."),
    DELETE_DATA_ENGINE_EVENT                (10,  "DeleteDataEngineEvent",  "An event that deletes an external data engine."),
    DELETE_SCHEMA_TYPE_EVENT                (11,  "DeleteSchemaTypeEvent",  "An event that deletes a schema type."),
    DELETE_PORT_IMPLEMENTATION_EVENT        (12,  "DeletePortImplementationEvent",  "An event that deletes a port implementation."),
    DELETE_PORT_ALIAS_EVENT                 (13,  "DeletePortAliasesEvent",  "An event that deletes a port alias.");

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
    DataEngineEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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
        return "DataEngineEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
