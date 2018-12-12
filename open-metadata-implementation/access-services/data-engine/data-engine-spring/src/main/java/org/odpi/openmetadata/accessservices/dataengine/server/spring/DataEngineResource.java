/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.GUIDResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-engine/users/{userId}")
public class DataEngineResource {
    private DataEngineService restAPI = new DataEngineService();

    /**
     * Default Constructor
     */
    public DataEngineResource() {

    }

    @RequestMapping(method = RequestMethod.POST, path = "/create-process")
    public GUIDResponse createProcess(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName,
                                      @RequestBody ProcessRequestBody processRequestBody) {
        return restAPI.createProcess(userId, serverName, processRequestBody.getName(), processRequestBody.getDisplayName());
    }
}
