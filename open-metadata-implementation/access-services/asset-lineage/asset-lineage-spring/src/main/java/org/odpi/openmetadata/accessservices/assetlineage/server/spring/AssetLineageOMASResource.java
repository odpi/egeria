/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;


import org.odpi.openmetadata.accessservices.assetlineage.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.assetlineage.responses.VoidResponse;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/")
public class AssetLineageOMASResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();

    public AssetLineageOMASResource() {

    }

    /**
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return
     */
    @PostMapping(path = "/report")
    public VoidResponse submitReport(@PathVariable("serverName") String serverName,
                                     @PathVariable("userId") String userId,
                                     @RequestBody ReportRequestBody requestBody) {
        return restAPI.submitReport(serverName, userId, requestBody);
    }

}
