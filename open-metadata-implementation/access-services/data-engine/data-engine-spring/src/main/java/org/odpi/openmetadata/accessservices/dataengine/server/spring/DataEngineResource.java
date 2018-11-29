/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.odpi.openmetadata.accessservices.dataengine.rest.RequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/open-metadata/access-services/data-engine/users/{userId}")
public class DataEngineResource {
    private DataEngineService restAPI = new DataEngineService();

    /**
     * Default Constructor
     */
    public DataEngineResource() {

    }

    @RequestMapping(method = RequestMethod.POST, path = "/create-process")
    public void submitFile(@PathVariable("userId") String userId,
                           @org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) {
        //restAPI.createProcess(userId, requestBody);
    }
}
