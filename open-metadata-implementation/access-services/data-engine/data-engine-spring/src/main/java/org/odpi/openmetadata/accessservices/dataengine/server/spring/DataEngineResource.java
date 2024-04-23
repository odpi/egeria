/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFlowsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessingStateRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.TopicRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes, ports and schema types, with all the needed
 * relationships.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")

@Tag(name = "Metadata Access Server: Data Engine OMAS", description = "The Data Engine OMAS provides APIs and events for data movement/processing engines to record the " +
        "changes made to the data landscape.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/data-engine/overview/"))

public class DataEngineResource {
    private final DataEngineRESTServices restAPI;

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        restAPI = new DataEngineRESTServices();
    }

    /**
     * Registers an external data engine as source of metadata by creating an engine entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     * @return unique identifier of the engine
     */
    @PostMapping(path = "/registration")
    @Operation(summary = "createExternalDataEngine",
            description = "Registers an external data engine as source of metadata by creating an engine entity.",
            externalDocs = @ExternalDocumentation(description = "Engine",
                    url = "https://egeria-project.org/concepts/software-capability/#engine"))
    public GUIDResponse createExternalDataEngine(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @RequestBody DataEngineRegistrationRequestBody requestBody) {
        return restAPI.createExternalDataEngine(serverName, userId, requestBody);
    }

    /**
     * Returns the unique identifier of an external data engine from an engine definition.
     *
     * @param serverName    name of server instance to call
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the engine
     * @return unique identified of the engine
     */
    @GetMapping(path = "/registration/{qualifiedName}")
    @Operation(summary = "getExternalDataEngineByQualifiedName",
            description = "Returns the unique identifier of an external data engine from an engine definition.",
            externalDocs = @ExternalDocumentation(description = "Engine",
                    url = "https://egeria-project.org/concepts/software-capability/#engine"))
    public GUIDResponse getExternalDataEngineByQualifiedName(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String qualifiedName) {
        return restAPI.getExternalDataEngine(serverName, userId, qualifiedName);
    }

    /**
     * Deletes the external data engine
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the data engine
     * @return void response
     */
    @DeleteMapping(path = "/registration")
    @Operation(summary = "deleteExternalDataEngine",
            description = "Deletes the external data engine.",
            externalDocs = @ExternalDocumentation(description = "Engine",
                    url = "https://egeria-project.org/concepts/software-capability/#engine"))
    public VoidResponse deleteExternalDataEngine(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteExternalDataEngine(userId, serverName, requestBody);
    }

    /**
     * Creates a SchemaType entity with schema attributes and relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the schema type
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/schema-types")
    @Operation(summary = "createOrUpdateSchemaType",
            description = "Creates a SchemaType entity with schema attributes and relationships.",
            externalDocs = @ExternalDocumentation(description = "SchemaType",
                    url = "https://egeria-project.org/concepts/schema/"))
    public GUIDResponse createOrUpdateSchemaType(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody SchemaTypeRequestBody requestBody) {
        return restAPI.upsertSchemaType(userId, serverName, requestBody);
    }

    /**
     * Removes a SchemaType entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the schema type
     * @return void response
     */
    @DeleteMapping(path = "/schema-types")
    @Operation(summary = "deleteSchemaType",
            description = "Removes a SchemaType entity with all the needed relationships.",
            externalDocs = @ExternalDocumentation(description = "SchemaType",
                    url = "https://egeria-project.org/concepts/schema/"))
    public VoidResponse deleteSchemaType(@PathVariable("userId") String userId,
                                         @PathVariable("serverName") String serverName,
                                         @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteSchemaType(userId, serverName, requestBody);
    }

    /**
     * Creates the PortImplementation entity
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port implementation
     * @return unique identifier of the created port implementation
     */
    @PostMapping(path = "/port-implementations")
    @Operation(summary = "createOrUpdatePortImplementation",
            description = "Creates the PortImplementation entity.",
            externalDocs = @ExternalDocumentation(description = "Port",
                    url = "https://egeria-project.org/types/2/0217-Ports/"))
    public GUIDResponse createOrUpdatePortImplementation(@PathVariable("userId") String userId,
                                                         @PathVariable("serverName") String serverName,
                                                         @RequestBody PortImplementationRequestBody portImplementationRequestBody) {
        return restAPI.upsertPortImplementation(userId, serverName, portImplementationRequestBody);
    }

    /**
     * Deletes the PortImplementation entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the port implementation
     * @return void response
     */
    @DeleteMapping(path = "/port-implementations")
    @Operation(summary = "deletePortImplementation",
            description = "Deletes the PortImplementation entity.",
            externalDocs = @ExternalDocumentation(description = "Port",
                    url = "https://egeria-project.org/types/2/0217-Ports/"))
    public VoidResponse deletePortImplementation(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deletePort(userId, serverName, requestBody, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Adds a ProcessHierarchy relationship between Process entities
     *
     * @param serverName                  name of server instance to call
     * @param userId                      the name of the calling user
     * @param processHierarchyRequestBody properties of the process hierarchy
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     */
    @PostMapping(path = "/process-hierarchies")
    @Operation(summary = "addProcessHierarchy",
            description = "Adds a ProcessHierarchy relationship between Process entities.",
            externalDocs = @ExternalDocumentation(description = "ProcessHierarchy",
                    url = "https://egeria-project.org/types/2/0215-Software-Components/"))
    public GUIDResponse addProcessHierarchy(@PathVariable("userId") String userId,
                                            @PathVariable("serverName") String serverName,
                                            @RequestBody ProcessHierarchyRequestBody processHierarchyRequestBody) {
        return restAPI.addProcessHierarchy(userId, serverName, processHierarchyRequestBody);
    }

    /**
     * Create or update the Process entity with ports, schema types and all needed relationships
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/processes")
    @Operation(summary = "createOrUpdateProcess",
            description = "Creates or updates the Process entity with ports, schema types and all needed relationships.",
            externalDocs = @ExternalDocumentation(description = "Process",
                    url = "https://egeria-project.org/types/2/0215-Software-Components/"))
    public GUIDResponse createOrUpdateProcess(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.upsertProcess(userId, serverName, processRequestBody);
    }

    /**
     * Deletes the Process
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the process
     * @return void response
     */
    @DeleteMapping(path = "/processes")
    @Operation(summary = "deleteProcess",
            description = "Deletes the Process.",
            externalDocs = @ExternalDocumentation(description = "Process",
                    url = "https://egeria-project.org/types/2/0215-Software-Components/"))
    public VoidResponse deleteProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteProcess(userId, serverName, requestBody);
    }

    /**
     * Adds DataFlow relationships
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param dataFlowsRequestBody properties of the data flows
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/data-flows")
    @Operation(summary = "addDataFlows",
            description = "Adds DataFlow relationships",
            externalDocs = @ExternalDocumentation(description = "DataFlows",
                    url = "https://egeria-project.org/types/7/0750-Data-Passing/"))
    public VoidResponse addDataFlows(@PathVariable("userId") String userId,
                                     @PathVariable("serverName") String serverName,
                                     @RequestBody DataFlowsRequestBody dataFlowsRequestBody) {
        return restAPI.addDataFlows(userId, serverName, dataFlowsRequestBody);
    }


    /***
     * Gets the connection details used to access Data Engine OMAS input topic
     *
     * @param serverName    name of server instance to call
     * @param userId        name of the calling user
     * @return OCF API ConnectionResponse object describing the details for the in topic connection used
     * or
     *      * InvalidParameterException one of the parameters is null or invalid or
     *      * UserNotAuthorizedException user not authorized to issue this request or
     *      * PropertyServerException problem retrieving the discovery engine definition
     *
     */
    @GetMapping(path = "/topics/in-topic-connection")
    @Operation(summary = "getInTopicConnection",
            description = "Gets the connection details used to access Data Engine OMAS input topic.",
            externalDocs = @ExternalDocumentation(description = "InTopic",
                    url = "https://egeria-project.org/concepts/in-topic/"))
    public ConnectionResponse getInTopicConnection(@PathVariable String serverName,
                                                   @PathVariable String userId) {
        return restAPI.getInTopicConnection(serverName, userId);
    }

    /**
     * Creates a Database entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/databases")
    @Operation(summary = "upsertDatabase",
            description = "Creates a Database entity with all the needed relationships.",
            externalDocs = @ExternalDocumentation(description = "Database",
                    url = "https://egeria-project.org/types/2/0224-Databases/"))
    public GUIDResponse upsertDatabase(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DatabaseRequestBody requestBody) {
        return restAPI.upsertDatabase(userId, serverName, requestBody);
    }

    /**
     * Deletes the database
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return void response
     */
    @DeleteMapping(path = "/databases")
    @Operation(summary = "deleteDatabase",
            description = "Deletes the database.",
            externalDocs = @ExternalDocumentation(description = "Database",
                    url = "https://egeria-project.org/types/2/0224-Databases/"))
    public VoidResponse deleteDatabase(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteDatabase(userId, serverName, requestBody);
    }

    /**
     * Creatse a Database Schema entity with a relationship to a database, if provided and not virtual
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/database-schemas")
    @Operation(summary = "upsertDatabaseSchema",
            description = "Creates a Database Schema entity with a relationship to a database, if provided and not virtual.",
            externalDocs = @ExternalDocumentation(description = "DatabaseSchema",
                    url = "https://egeria-project.org/types/2/0224-Databases/#deployeddatabaseschema"))
    public GUIDResponse upsertDatabaseSchema(@PathVariable("userId") String userId,
                                             @PathVariable("serverName") String serverName,
                                             @RequestBody DatabaseSchemaRequestBody requestBody) {
        return restAPI.upsertDatabaseSchema(userId, serverName, requestBody);

    }

    /**
     * Deletes the database schema
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return void response
     */
    @DeleteMapping(path = "/database-schemas")
    @Operation(summary = "deleteDatabaseSchema",
            description = "Deletes the database schema.",
            externalDocs = @ExternalDocumentation(description = "DatabaseSchema",
                    url = "https://egeria-project.org/types/2/0224-Databases/#deployeddatabaseschema"))
    public VoidResponse deleteDatabaseSchema(@PathVariable("userId") String userId,
                                             @PathVariable("serverName") String serverName,
                                             @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteDatabaseSchema(userId, serverName, requestBody);
    }

    /**
     * Creates a RelationalTable entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the relational table
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/relational-tables")
    @Operation(summary = "upsertRelationalTable",
            description = "Creates a RelationalTable entity with all the needed relationships",
            externalDocs = @ExternalDocumentation(description = "RelationalTable",
                    url = "https://egeria-project.org/types/5/0534-Relational-Schemas/"))
    public GUIDResponse upsertRelationalTable(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody RelationalTableRequestBody requestBody) {
        return restAPI.upsertRelationalTable(userId, serverName, requestBody);
    }

    /**
     * Deletes the relational table with columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the relational table
     * @return void response
     */
    @DeleteMapping(path = "/relational-tables")
    @Operation(summary = "deleteRelationalTable",
            description = "Deletes the relational table with columns.",
            externalDocs = @ExternalDocumentation(description = "RelationalTable",
                    url = "https://egeria-project.org/types/5/0534-Relational-Schemas/"))
    public VoidResponse deleteRelationalTable(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteRelationalTable(userId, serverName, requestBody);
    }

    /***
     * Adds a DataFile asset or any of its subtype
     *
     * @param serverName name of server instance to call
     * @param userId name of the calling user
     * @param dataFileRequestBody properties of data file
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/data-files")
    @Operation(summary = "upsertDataFile",
            description = "Adds a DataFile asset or any of its subtype.",
            externalDocs = @ExternalDocumentation(description = "DataFile",
                    url = "https://egeria-project.org/types/2/0220-Files-and-Folders/#datafile"))
    public GUIDResponse upsertDataFile(@PathVariable String serverName, @PathVariable String userId,
                                       @RequestBody DataFileRequestBody dataFileRequestBody) {
        return restAPI.upsertDataFile(serverName, userId, dataFileRequestBody);
    }

    /**
     * Deletes the data file with columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the data file
     * @return void response
     */
    @DeleteMapping(path = "/data-files")
    @Operation(summary = "deleteDataFile",
            description = "Deletes the data file with columns.",
            externalDocs = @ExternalDocumentation(description = "DataFile",
                    url = "https://egeria-project.org/types/2/0220-Files-and-Folders/#datafile"))
    public VoidResponse deleteDataFile(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteDataFile(userId, serverName, requestBody);
    }

    /**
     * Deletes the folder
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the folder
     * @return void response
     */
    @DeleteMapping(path = "/folders")
    @Operation(summary = "deleteFolder",
            description = "Deletes the folder.",
            externalDocs = @ExternalDocumentation(description = "FileFolder",
                    url = "https://egeria-project.org/types/2/0220-Files-and-Folders/#filefolder"))
    public VoidResponse deleteFolder(@PathVariable("userId") String userId,
                                     @PathVariable("serverName") String serverName,
                                     @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteFolder(userId, serverName, requestBody);
    }

    /**
     * Deletes the connection
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the connection
     * @return void response
     */
    @DeleteMapping(path = "/connections")
    @Operation(summary = "deleteConnection",
            description = "Deletes the connection.",
            externalDocs = @ExternalDocumentation(description = "Connection",
                    url = "https://egeria-project.org/types/2/0201-Connectors-and-Connections/"))
    public VoidResponse deleteConnection(@PathVariable("userId") String userId,
                                         @PathVariable("serverName") String serverName,
                                         @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteConnection(userId, serverName, requestBody);
    }

    /**
     * Deletes the endpoint
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the connection
     * @return void response
     */
    @DeleteMapping(path = "/endpoints")
    @Operation(summary = "deleteEndpoint",
            description = "Deletes the endpoint.",
            externalDocs = @ExternalDocumentation(description = "Endpoint",
                    url = "https://egeria-project.org/types/0/0026-Endpoints/"))
    public VoidResponse deleteEndpoint(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteEndpoint(userId, serverName, requestBody);
    }

    /**
     * Find assets
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param findRequestBody properties for the connection
     * @return asset if found
     */
    @PostMapping(path = "/find")
    @Operation(summary = "find",
            description = "Find assets.",
            externalDocs = @ExternalDocumentation(description = "Asset",
                    url = "https://egeria-project.org/concepts/asset/"))
    public GUIDListResponse search(@PathVariable("userId") String userId,
                                   @PathVariable("serverName") String serverName,
                                   @RequestBody FindRequestBody findRequestBody) {
        return restAPI.find(userId, serverName, findRequestBody);
    }


    /**
     * Creates or updates a Topic entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the topic
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/topics")
    @Operation(summary = "upsertTopic",
            description = "Creates or updates a Topic entity with all the needed relationships.",
            externalDocs = @ExternalDocumentation(description = "Topic",
                    url = "https://egeria-project.org/types/2/0223-Events-and-Logs/#topic"))
    public GUIDResponse upsertTopic(@PathVariable("userId") String userId,
                                    @PathVariable("serverName") String serverName,
                                    @RequestBody TopicRequestBody requestBody) {
        return restAPI.upsertTopic(userId, serverName, requestBody);
    }

    /**
     * Deletes the topic
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return void response
     */
    @DeleteMapping(path = "/topics")
    @Operation(summary = "deleteTopic",
            description = "Deletes the topic.",
            externalDocs = @ExternalDocumentation(description = "Topic",
                    url = "https://egeria-project.org/types/2/0223-Events-and-Logs/#topic"))
    public VoidResponse deleteTopic(@PathVariable("userId") String userId,
                                    @PathVariable("serverName") String serverName,
                                    @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteTopic(userId, serverName, requestBody);
    }

    /**
     * Creates or updates an Event Type entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the event type
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/event-types")
    @Operation(summary = "upsertEventType",
            description = "Creates or updates an Event Type entity with all the needed relationships.",
            externalDocs = @ExternalDocumentation(description = "EventType",
                    url = "https://egeria-project.org/types/5/0535-Event-Schemas/#eventtype"))
    public GUIDResponse upsertEventType(@PathVariable("userId") String userId,
                                        @PathVariable("serverName") String serverName,
                                        @RequestBody EventTypeRequestBody requestBody) {
        return restAPI.upsertEventType(userId, serverName, requestBody);
    }

    /**
     * Deletes the event type
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     * @return void response
     */
    @DeleteMapping(path = "/event-types")
    @Operation(summary = "deleteEventType",
            description = "Deletes the EventType.",
            externalDocs = @ExternalDocumentation(description = "EventType",
                    url = "https://egeria-project.org/types/5/0535-Event-Schemas/#eventtype"))
    public VoidResponse deleteEventType(@PathVariable("userId") String userId,
                                        @PathVariable("serverName") String serverName,
                                        @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteEventType(userId, serverName, requestBody);
    }

    /**
     * Creates or updates a data engine's processing state classification with the provided properties
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the processing state
     * @return void response
     */
    @PostMapping(path = "/processing-state")
    @Operation(summary = "upsertProcessingState",
            description = "Creates or updates a data engine's processing state classification with the provided properties",
            externalDocs = @ExternalDocumentation(description = "ProcessingState",
                    url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/#metadata-relationships-and-classifications"))
    public VoidResponse upsertProcessingState(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody ProcessingStateRequestBody requestBody) {
        return restAPI.upsertProcessingState(userId, serverName, requestBody);
    }

    /**
     * Gets the data engine's processing state classification properties
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @return PropertiesResponse response
     */
    @GetMapping(path = "/processing-state")
    @Operation(summary = "upsertProcessingState",
            description = "Gets the data engine's processing state classification properties.",
            externalDocs = @ExternalDocumentation(description = "ProcessingState",
                    url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/#metadata-relationships-and-classifications"))
    public PropertiesResponse getProcessingState(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestParam("dataEngine") String externalSourceName) {
        return restAPI.getProcessingState(userId, serverName, externalSourceName);
    }

}
