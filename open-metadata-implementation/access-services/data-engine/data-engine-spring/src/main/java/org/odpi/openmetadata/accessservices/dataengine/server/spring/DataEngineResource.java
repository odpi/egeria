/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes and input/output relationships between the processes
 * and the data sets.
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

    @PostMapping(path = "/software-server-capability")
    public GUIDResponse createSoftwareServerCapability(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @RequestBody SoftwareServerCapabilityRequestBody requestBody) {
        return restAPI.createSoftwareServer(serverName, userId, requestBody);
    }

    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName    name of the service to route the request to.
     * @param userId        identifier of calling user.
     * @param qualifiedName unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @GetMapping(path = "/software-server-capability/{qualifiedName}")
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
    public GUIDResponse createSchemaType(@PathVariable("userId") String userId,
                                         @PathVariable("serverName") String serverName,
                                         @RequestBody SchemaTypeRequestBody requestBody) {
        return restAPI.createSchemaType(userId, serverName, requestBody);
    }


    /**
     * Create the Port entity
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/port-implementations")
    public GUIDResponse createPortImplementation(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @RequestBody PortImplementationRequestBody portImplementationRequestBody) {
        return restAPI.createPortImplementation(userId, serverName, portImplementationRequestBody);
    }


    /**
     * Create the Port entity
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/port-aliases")
    public GUIDResponse createPortAlias(@PathVariable("userId") String userId,
                                        @PathVariable("serverName") String serverName,
                                        @RequestBody PortRequestBody portRequestBody) {
        return restAPI.createPortAlias(userId, serverName, portRequestBody);
    }

    /**
     * Create the Port entity
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/port-aliases/of-port-alias/{portAliasGUID}")
    public GUIDResponse createPortAliasOfPortAlias(@PathVariable("userId") String userId,
                                                   @PathVariable("serverName") String serverName,
                                                   @PathVariable("portAliasGUID") String portAliasGUID,
                                                   @RequestBody PortRequestBody portRequestBody) {
        return restAPI.createPortAliasOfPortAlias(userId, serverName, portRequestBody, portAliasGUID);
    }

    /**
     * Create the Port entity
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     *
     * @return unique identifier of the created process
     */
    @PostMapping(path = "/port-implementations/of-port-alias/{portAliasGUID}")
    public GUIDResponse createPortImplementationOfPortAlias(@PathVariable("userId") String userId,
                                                            @PathVariable("serverName") String serverName,
                                                            @PathVariable("portAliasGUID") String portAliasGUID,
                                                            @RequestBody PortImplementationRequestBody portImplementationRequestBody) {
        return restAPI.createPortImplementationOfPortAlias(userId, serverName, portImplementationRequestBody,
                portAliasGUID);
    }

    /**
     * Create the Process entity with all the needed relationships
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return unique identifier of the created entity
     */
    @PostMapping(path = "/processes")
    public GUIDResponse createProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Add ports to an existing Process entity
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param processGuid         the guid of the process
     * @param portListRequestBody list of port guids
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
}
