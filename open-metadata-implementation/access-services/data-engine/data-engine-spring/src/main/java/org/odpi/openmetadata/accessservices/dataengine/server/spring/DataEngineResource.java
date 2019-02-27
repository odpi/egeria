/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRestServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * Create the Process entity.
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create-process")
    public GUIDResponse createProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody ProcessRequestBody processRequestBody) {

        return restAPI.createProcess(userId, serverName, processRequestBody);
    }

    /**
     * Create the DeployedAPI entity.
     *
     * @param serverName             name of server instance to call
     * @param userId                 the name of the calling user
     * @param deployedAPIRequestBody properties of the deployed api
     *
     * @return unique identifier of the created entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create-deployed-api")
    public GUIDResponse createDeployedAPI(@PathVariable("userId") String userId,
                                          @PathVariable("serverName") String serverName,
                                          @RequestBody DeployedAPIRequestBody deployedAPIRequestBody) {

        return restAPI.createDeployedAPI(userId, serverName, deployedAPIRequestBody);
    }

    /**
     * Create the Port entity.
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return unique identifier of the created process
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create-port")
    public GUIDResponse createPort(@PathVariable("userId") String userId,
                                   @PathVariable("serverName") String serverName,
                                   @RequestBody PortRequestBody portRequestBody) {

        return restAPI.createPort(userId, serverName, portRequestBody);
    }
    /**
     * Add ports to an existing Process entity.
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portListRequestBody guid of ports and process
     *
     * @return unique identifier of the updated process
     */
    @RequestMapping(method = RequestMethod.POST, path = "/add-ports-to-process")
    public GUIDResponse addPortsToProcess(@PathVariable("userId") String userId,
                                   @PathVariable("serverName") String serverName,
                                   @RequestBody PortListRequestBody portListRequestBody) {

        return restAPI.addPortsToProcess(userId, serverName, portListRequestBody);
    }
}
