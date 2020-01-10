/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerDataPlatformService;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformServicesConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import static org.odpi.openmetadata.commonservices.spring.SpringUtils.createSpringResponse;

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class DataPlatformServiceResource {


    private OMAGServerDataPlatformService adminAPI = new OMAGServerDataPlatformService();

    @PostMapping(path = "/data-platform-service/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody DataPlatformServicesConfig dataPlatformServicesConfig)
    {
        return createSpringResponse(adminAPI.setDataPlatformServiceConfig(userId, serverName, dataPlatformServicesConfig));
    }

    @PostMapping(path = "/data-platform-service")
    public VoidResponse enableDataPlatformService(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return createSpringResponse(adminAPI.enableDataPlatformService(userId, serverName));
    }
}
