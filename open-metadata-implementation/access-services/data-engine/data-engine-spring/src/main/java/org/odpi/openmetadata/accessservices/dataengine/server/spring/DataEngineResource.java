/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.GUIDResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The DataEngineResource provides the server-side implementation of the Data Engine Open Metadata Assess Service (OMAS).
 * This interface facilitates the searching for assets, provides details about specific assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")
public class DataEngineResource {
    private DataEngineService restAPI = new DataEngineService();

    private static final Logger LOGGER = LoggerFactory.getLogger(DataEngineResource.class);

    /**
     * Default Constructor
     */
    public DataEngineResource() {
        final String methodName = "initialize";

        LOGGER.debug(">>" + methodName);

        LOGGER.debug("<<" + methodName);
    }

    /**
     * Create the process.
     *
     * @param serverName         name of server instance to call.
     * @param userId             the name of the calling user.
     * @param processRequestBody properties of the process.
     * @return unique identifier of the created process
     */
    @RequestMapping(method = RequestMethod.POST, path = "/create-process")
    public GUIDResponse createProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody ProcessRequestBody processRequestBody) {

        return restAPI.createProcess(userId, serverName, processRequestBody);
    }
}
