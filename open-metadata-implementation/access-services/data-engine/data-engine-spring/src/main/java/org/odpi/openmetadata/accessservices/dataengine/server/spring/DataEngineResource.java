/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesDeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        , url = "https://egeria.odpi.org/open-metadata-implementation/access-services/data-engine/"))

public class DataEngineResource {
    private DataEngineRESTServices restAPI;

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        restAPI = new DataEngineRESTServices();
    }

    /**
     * Register external data engine as source of metadata by creating a software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     *
     * @return unique identifier of the software server capability
     */
    @PostMapping(path = "/registration")
    public GUIDResponse createExternalDataEngine(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @RequestBody DataEngineRegistrationRequestBody requestBody) {
        return restAPI.createExternalDataEngine(serverName, userId, requestBody);
    }

    /**
     * Return the unique identifier of an external data engine from a software server capability definition.
     *
     * @param serverName    name of server instance to call
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the software server capability
     *
     * @return unique identified of the software server capability
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
     * @param requestBody properties of the port implementation
     *
     * @return unique identifier of the created port implementation
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
     * @return unique identifier of the created entity
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
     * @return unique identifier of the created port implementation
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
     * @return unique identifier of the created port alias
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
     * Create or update  the Process entities with ports, schema types and all needed relationships
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param processesRequestBody properties of the process
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/processes")
    public GUIDListResponse createOrUpdateProcesses(@PathVariable("userId") String userId,
                                                    @PathVariable("serverName") String serverName,
                                                    @RequestBody ProcessesRequestBody processesRequestBody) {
        return restAPI.upsertProcesses(userId, serverName, processesRequestBody);
    }

    /**
     * Delete the Process
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param requestBody properties of the process
     *
     * @return unique identifier of the created process
     */
    @DeleteMapping(path = "/processes")
    public VoidResponse deleteProcesses(@PathVariable("userId") String userId,
                                                    @PathVariable("serverName") String serverName,
                                                    @RequestBody ProcessesDeleteRequestBody requestBody) {
        return restAPI.deleteProcesses(userId, serverName, requestBody);
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
     * Create a RelationalTable entity with all the needed relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties for the schema type
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/relational-tables")
    public GUIDResponse upsertRelationalTable(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @RequestBody RelationalTableRequestBody requestBody) {
        return restAPI.upsertRelationalTable(userId, serverName, requestBody);
    }

    /***
     * Add a DataFile asset or any of its subtype
     *
     * @param serverName name of server instance to call
     * @param userId name of the calling user
     * @param dataFileRequestBody properties of data file
     *
     * @return file guid
     */
    @PostMapping(path = "/data-files")
    public GUIDResponse upsertDataFile(@PathVariable String serverName, @PathVariable String userId,
                                       @RequestBody DataFileRequestBody dataFileRequestBody) {
        return restAPI.upsertDataFile(serverName, userId, dataFileRequestBody);
    }

}
