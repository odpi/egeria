/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Getter
public enum DataEngineEventType implements Serializable {

    UNKNOWN_DATA_ENGINE_EVENT               (0,   "Unknown Data Engine Job Event",  "An event that is not recognized by the local server."),
    DATA_ENGINE_REGISTRATION_EVENT          (1,   "DataEngineRegistrationEvent",      "An event that register a data engine as external source."),
    LINEAGE_MAPPINGS_EVENT                  (2,   "LineageMappingsEvent",  "An event that add or update lineage mappings."),
    PORT_ALIAS_EVENT                        (3,   "PortAliasEvent",      "An event that add or update port alias"),
    PORT_IMPLEMENTATION_EVENT               (4,   "PortImplementationEvent",  "An event that create or update port implementations."),
    SCHEMA_TYPE_EVENT                       (5,   "SchemaTypeEvent",  "An event that create or update schema types."),
    PROCESS_HIERARCHY_EVENT                 (6,   "ProcessHierarchyEvent", "An event to setup a process hierarchy."),
    DELETE_DATA_ENGINE_EVENT                (7,  "DeleteDataEngineEvent",  "An event that deletes an external data engine."),
    DELETE_SCHEMA_TYPE_EVENT                (8,  "DeleteSchemaTypeEvent",  "An event that deletes a schema type."),
    DELETE_PORT_IMPLEMENTATION_EVENT        (9,  "DeletePortImplementationEvent",  "An event that deletes a port implementation."),
    DELETE_PORT_ALIAS_EVENT                 (10,  "DeletePortAliasesEvent",  "An event that deletes a port alias."),
    DATABASE_EVENT                          (11,  "DatabaseEvent",  "An event that create or update databases."),
    DATABASE_SCHEMA_EVENT                   (12,  "DatabaseSchemaEvent",  "An event that create or update database schemas."),
    RELATIONAL_TABLE_EVENT                  (13,  "RelationalTableEvent",  "An event that create or update relational tables."),
    DATA_FILE_EVENT                         (14,  "DataFileEvent",  "An event that create or update data files."),
    DELETE_DATABASE_EVENT                   (15,  "DeleteDatabaseEvent",  "An event that deletes a database."),
    DELETE_DATABASE_SCHEMA_EVENT            (16,  "DeleteDatabaseSchemaEvent",  "An event that deletes a database schema."),
    DELETE_RELATIONAL_TABLE_EVENT           (17,  "DeleteRelationalTableEvent",  "An event that deletes a relational table."),
    DELETE_DATA_FILE_EVENT                  (18,  "DeleteDataFileEvent",  "An event that deletes a data file."),
    DELETE_FOLDER_EVENT                     (19,  "DeleteFolderEvent",  "An event that deletes a folder."),
    DELETE_CONNECTION_EVENT                 (20,  "DeleteConnectionEvent",  "An event that deletes a connection."),
    DELETE_ENDPOINT_EVENT                   (21,  "DeleteEndpointEvent",  "An event that deletes an endpoint."),
    PROCESS_EVENT                           (22,  "ProcessEvent",  "An event that create or update a process."),
    DELETE_PROCESS_EVENT                    (23,  "DeleteProcessEvent",  "An event that deletes a process.");

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * The int identifier used for indexing based on the enum
     * -- GETTER --
     * Return the int identifier used for indexing based on the enum.
     * @return eventTypeCode identifier code
     */
    private final int eventTypeCode;

    /**
     * The string name used for messages that include the enum
     * -- GETTER --
     * Return the string name used for messages that include the enum.
     * @return event type name
     */
    private final String eventTypeName;

    /**
     * The default description for the enum value - used when natural resource
     * -- GETTER --
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     * @return default description
     */
    private final String eventTypeDescription;

    /**
     * Sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode int identifier used for indexing based on the enum.
     * @param eventTypeName string name used for messages that include the enum.
     * @param eventTypeDescription default description for the enum value - used when natural resource
     *                             bundle is not available.
     */
    DataEngineEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }

}
