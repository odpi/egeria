/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes, ports and schema types, with all the needed
 * relationships.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")
public class DataEngineResource {
    private DataEngineRESTServices restAPI;

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        restAPI = new DataEngineRESTServices();
    }

    /**
     * Create a software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the entity
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/software-server-capabilities")
    public GUIDResponse createSoftwareServerCapability(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @RequestBody SoftwareServerCapabilityRequestBody requestBody) {
        return restAPI.createSoftwareServer(serverName, userId, requestBody);
    }

    /**
     * Return the unique identifier from a software server capability definition.
     *
     * @param serverName    name of server instance to call
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the software server capability
     *
     * @return unique identified of the software server
     */
    @GetMapping(path = "/software-server-capabilities/{qualifiedName}")
    public GUIDResponse getSoftwareServerCapabilityByQualifiedName(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String qualifiedName) {
        return restAPI.getSoftwareServerByQualifiedName(serverName, userId, qualifiedName);
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
