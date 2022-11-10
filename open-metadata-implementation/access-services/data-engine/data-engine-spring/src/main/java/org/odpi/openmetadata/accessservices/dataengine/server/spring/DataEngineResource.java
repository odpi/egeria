/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
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
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_ALIAS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes, ports and schema types, with all the needed
 * relationships.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")

@Tag(name = "Data Engine OMAS", description = "The Data Engine OMAS provides APIs and events for data movement/processing engines to record the " +
        "changes made to the data landscape.", externalDocs = @ExternalDocumentation(description = "Data Engine Open Metadata Access Service (OMAS)"
        , url = "https://egeria-project.org/services/omas/data-engine/overview/"))

public class DataEngineResource {
    private DataEngineRESTServices restAPI;

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        restAPI = new DataEngineRESTServices();
    }

    /**
     * Register external data engine as source of metadata by creating an engine entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     *
     * @return unique identifier of the engine
     */
    @PostMapping(path = "/registration")
    public GUIDResponse createExternalDataEngine(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @RequestBody DataEngineRegistrationRequestBody requestBody) {
        return restAPI.createExternalDataEngine(serverName, userId, requestBody);
    }

    /**
     * Return the unique identifier of an external data engine from an engine definition.
     *
     * @param serverName    name of server instance to call
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the engine
     *
     * @return unique identified of the engine
     */
    @GetMapping(path = "/registration/{qualifiedName}")
    public GUIDResponse getExternalDataEngineByQualifiedName(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String qualifiedName) {
        return restAPI.getExternalDataEngine(serverName, userId, qualifiedName);
    }

    /**
     * Delete the external data engine
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the data engine
     *
     * @return void response
     */
    @DeleteMapping(path = "/registration")
    public VoidResponse deleteExternalDataEngine(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteExternalDataEngine(userId, serverName, requestBody);
    }

    /**
     * Create a SchemaType entity with schema attributes and relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the schema type
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/schema-types")
    public GUIDResponse createOrUpdateSchemaType(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody SchemaTypeRequestBody requestBody) {
        return restAPI.upsertSchemaType(userId, serverName, requestBody);
    }

    /**
     * Remove a SchemaType entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the schema type
     *
     * @return void response
     */
    @DeleteMapping(path = "/schema-types")
    public VoidResponse deleteSchemaType(@PathVariable("userId") String userId,
                                         @PathVariable("serverName") String serverName,
                                         @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteSchemaType(userId, serverName, requestBody);
    }

    /**
     * Create the PortImplementation entity
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port implementation
     *
     * @return unique identifier of the created port implementation
     */
    @PostMapping(path = "/port-implementations")
    public GUIDResponse createOrUpdatePortImplementation(@PathVariable("userId") String userId,
                                                         @PathVariable("serverName") String serverName,
                                                         @RequestBody PortImplementationRequestBody portImplementationRequestBody) {
        return restAPI.upsertPortImplementation(userId, serverName, portImplementationRequestBody);
    }

    /**
     * Delete the PortImplementation entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the port implementation
     *
     * @return void response
     */
    @DeleteMapping(path = "/port-implementations")
    public VoidResponse deletePortImplementation(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deletePort(userId, serverName, requestBody, PORT_IMPLEMENTATION_TYPE_NAME);
    }

    /**
     * Create the PortAlias entity
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param portAliasRequestBody properties of the port alias
     *
     * @return unique identifier of the created port alias
     */
    @PostMapping(path = "/port-aliases")
    public GUIDResponse createOrUpdatePortAlias(@PathVariable("userId") String userId,
                                                @PathVariable("serverName") String serverName,
                                                @RequestBody PortAliasRequestBody portAliasRequestBody) {
        return restAPI.upsertPortAlias(userId, serverName, portAliasRequestBody);
    }

    /**
     * Delete the PortAlias entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the port implementation
     *
     * @return void response
     */
    @DeleteMapping(path = "/port-aliases")
    public VoidResponse deletePortAliases(@PathVariable("userId") String userId,
                                          @PathVariable("serverName") String serverName,
                                          @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deletePort(userId, serverName, requestBody, PORT_ALIAS_TYPE_NAME);
    }

    /**
     * Add a ProcessHierarchy relationship between Process entities
     *
     * @param serverName                  name of server instance to call
     * @param userId                      the name of the calling user
     * @param processHierarchyRequestBody properties of the process hierarchy
     *
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     */
    @PostMapping(path = "/process-hierarchies")
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
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/processes")
    public GUIDResponse createOrUpdateProcess(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.upsertProcess(userId, serverName, processRequestBody);
    }

    /**
     * Delete the Process
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the process
     *
     * @return void response
     */
    @DeleteMapping(path = "/processes")
    public VoidResponse deleteProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteProcess(userId, serverName, requestBody);
    }

    /**
     * Add LineageMapping relationships
     *
     * @param serverName                 name of server instance to call
     * @param userId                     the name of the calling user
     * @param lineageMappingsRequestBody properties of the mappings
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/lineage-mappings")
    public VoidResponse addLineageMappings(@PathVariable("userId") String userId,
                                           @PathVariable("serverName") String serverName,
                                           @RequestBody LineageMappingsRequestBody lineageMappingsRequestBody) {
        return restAPI.addLineageMappings(userId, serverName, lineageMappingsRequestBody);
    }


    /***
     * Get connection details used to access Data Engine OMAS input topic
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

    public ConnectionResponse getInTopicConnection(@PathVariable String serverName,
                                                   @PathVariable String userId) {
        return restAPI.getInTopicConnection(serverName, userId);
    }

    /**
     * Create a Database entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/databases")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/databases")
    public VoidResponse deleteDatabase(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteDatabase(userId, serverName, requestBody);
    }

    /**
     * Create a Database Schema entity with all the a relationship to a database, if provided and not virtual
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/database-schemas")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/database-schemas")
    public VoidResponse deleteDatabaseSchema(@PathVariable("userId") String userId,
                                             @PathVariable("serverName") String serverName,
                                             @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteDatabaseSchema(userId, serverName, requestBody);
    }

    /**
     * Create a RelationalTable entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the relational table
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/relational-tables")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/relational-tables")
    public VoidResponse deleteRelationalTable(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteRelationalTable(userId, serverName, requestBody);
    }

    /***
     * Add a DataFile asset or any of its subtype
     *
     * @param serverName name of server instance to call
     * @param userId name of the calling user
     * @param dataFileRequestBody properties of data file
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/data-files")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/data-files")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/folders")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/connections")
    public VoidResponse deleteConnection(@PathVariable("userId") String userId,
                                         @PathVariable("serverName") String serverName,
                                         @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteConnection(userId, serverName, requestBody);
    }

    /**
     * Deletes the connection
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the connection
     *
     * @return void response
     */
    @DeleteMapping(path = "/endpoints")
    public VoidResponse deleteEndpoint(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteEndpoint(userId, serverName, requestBody);
    }

    /**
     * Find assets
     *
     * @param serverName        name of server instance to call
     * @param userId            the name of the calling user
     * @param findRequestBody properties for the connection
     *
     * @return asset if found
     */
    @PostMapping(path = "/find")
    public GUIDListResponse search(@PathVariable("userId") String userId,
                                   @PathVariable("serverName") String serverName,
                                   @RequestBody FindRequestBody findRequestBody){
        return restAPI.find(userId, serverName, findRequestBody);
    }


    /**
     * Create or update a Topic entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the topic
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/topics")
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
     *
     * @return void response
     */
    @DeleteMapping(path = "/topics")
    public VoidResponse deleteTopic(@PathVariable("userId") String userId,
                                       @PathVariable("serverName") String serverName,
                                       @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteTopic(userId, serverName, requestBody);
    }

    /**
     * Create or update a Event Type entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the event type
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/event-types")
    public GUIDResponse upsertEventType(@PathVariable("userId") String userId,
                                     @PathVariable("serverName") String serverName,
                                     @RequestBody EventTypeRequestBody requestBody) {
        return restAPI.upsertEventType(userId, serverName, requestBody);
    }

    /**
     * Deletes the topic
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the database
     *
     * @return void response
     */
    @DeleteMapping(path = "/event-types")
    public VoidResponse deleteEventType(@PathVariable("userId") String userId,
                                        @PathVariable("serverName") String serverName,
                                        @RequestBody DeleteRequestBody requestBody) {
        return restAPI.deleteEventType(userId, serverName, requestBody);
    }

    /**
     * Create or update a processing state entity with the provided properties
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the processing state
     * @return void response
     */
    @PostMapping(path = "/processing-state")
    public VoidResponse upsertProcessingState(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody ProcessingStateRequestBody requestBody) {
        return restAPI.upsertProcessingState(userId, serverName, requestBody);
    }

    /**
     * Get the data engine's processing state classification properties
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @return PropertiesResponse response
     */
    @GetMapping(path = "/processing-state")
    public PropertiesResponse getProcessingState(@PathVariable("userId") String userId,
                                                    @PathVariable("serverName") String serverName,
                                                    @RequestParam("dataEngine") String externalSourceName) {
        return restAPI.getProcessingState(userId, serverName, externalSourceName);
    }

}
