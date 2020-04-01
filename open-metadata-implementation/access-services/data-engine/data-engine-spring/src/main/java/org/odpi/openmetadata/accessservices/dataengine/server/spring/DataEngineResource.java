/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes, ports and schema types, with all the needed
 * relationships.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")

@Tag(name="Data Engine OMAS", description="The Data Engine OMAS provides APIs and events for data movement/processing engines to record the changes made to the data landscape.", externalDocs=@ExternalDocumentation(description="Data Engine Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-engine/"))

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
     * @return unique identifier of the created process
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
     * @return unique identified of the software server
     */
    @GetMapping(path = "/registration/{qualifiedName}")
    public GUIDResponse getExternalDataEngineByQualifiedName(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String qualifiedName) {
        return restAPI.getExternalDataEngineByQualifiedName(serverName, userId, qualifiedName);
    }

    /**
     * Create a SchemaType entity with all the needed relationships
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
        return restAPI.createOrUpdateSchemaType(userId, serverName, requestBody);
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
        return restAPI.createOrUpdatePortImplementation(userId, serverName, portImplementationRequestBody);
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
        return restAPI.createOrUpdatePortAlias(userId, serverName, portAliasRequestBody);
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
        return restAPI.createOrUpdateProcesses(userId, serverName, processesRequestBody);
    }

    /**
     * Add ports to an existing Process entity
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param processGuid         the guid of the process
     * @param portListRequestBody list of port unique identifiers
     *
     * @return unique identifier of the updated process
     */
    @PostMapping(path = "/processes/{processGuid}/ports")
    public GUIDResponse addPortsToProcess(@PathVariable("userId") String userId,
                                          @PathVariable("serverName") String serverName,
                                          @PathVariable("processGuid") String processGuid,
                                          @RequestBody PortListRequestBody portListRequestBody) {
        return restAPI.addPortsToProcess(userId, serverName, processGuid, portListRequestBody);
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
}
