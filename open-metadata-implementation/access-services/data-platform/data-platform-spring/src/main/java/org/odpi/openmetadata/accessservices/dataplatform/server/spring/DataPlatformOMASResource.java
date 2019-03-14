/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformOMASAPIResponse;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRestServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}/")
public class DataPlatformOMASResource {

    private final DataPlatformRestServices restAPI = new DataPlatformRestServices();


    /**
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @return
     */
    @PostMapping(path = "/test")
    public String submitReport(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId) {
        return userId;
    }
}