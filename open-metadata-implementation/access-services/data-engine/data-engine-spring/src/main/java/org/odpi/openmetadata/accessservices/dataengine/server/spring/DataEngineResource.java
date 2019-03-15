/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRestServices;
import org.springframework.web.bind.annotation.*;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This interface facilitates the creation of processes and input/output relationships between the processes
 * and the data sets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")
public class DataEngineResource {
    private DataEngineRestServices restAPI;

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        restAPI = new DataEngineRestServices();
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
    @RequestMapping(method = RequestMethod.POST, path = "/processes")
    public GUIDResponse createProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Create the Process entity with all the needed relationships using an array of existing ports
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/processes/by-ports")
    public GUIDResponse createProcessWithPorts(@PathVariable("userId") String userId,
                                               @PathVariable("serverName") String serverName,
                                               @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Create the Process entity with all the needed relationships using an array of existing deployed apis
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/processes/by-deployed-apis")
    public GUIDResponse createProcessWithDeployedApis(@PathVariable("userId") String userId,
                                                      @PathVariable("serverName") String serverName,
                                                      @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Create the Process entity with all the needed relationships using an array of existing assets
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/processes/by-assets")
    public GUIDResponse createProcessWithAssets(@PathVariable("userId") String userId,
                                                @PathVariable("serverName") String serverName,
                                                @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Create the DeployedAPI entity
     *
     * @param serverName             name of server instance to call
     * @param userId                 the name of the calling user
     * @param deployedAPIRequestBody properties of the deployed api
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/deployed-apis")
    public GUIDResponse createDeployedAPI(@PathVariable("userId") String userId,
                                          @PathVariable("serverName") String serverName,
                                          @RequestBody DeployedAPIRequestBody deployedAPIRequestBody) {
        return restAPI.createDeployedAPI(userId, serverName, deployedAPIRequestBody);
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
    @RequestMapping(method = RequestMethod.POST, path = "/ports")
    public GUIDResponse createPort(@PathVariable("userId") String userId,
                                   @PathVariable("serverName") String serverName,
                                   @RequestBody PortRequestBody portRequestBody) {
        return restAPI.createPort(userId, serverName, portRequestBody);
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
    @RequestMapping(method = RequestMethod.POST, path = "/processes/{processGuid}/ports")
    public GUIDResponse addPortsToProcess(@PathVariable("userId") String userId,
                                          @PathVariable("serverName") String serverName,
                                          @PathVariable("processGuid") String processGuid,
                                          @RequestBody PortListRequestBody portListRequestBody) {
        return restAPI.addPortsToProcess(userId, serverName, processGuid, portListRequestBody);
    }
}
