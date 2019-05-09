/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerVirtualizationService;
import org.odpi.openmetadata.adminservices.configuration.properties.VirtualizationConfig;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class VirtualizationResource {

    private OMAGServerVirtualizationService adminAPI = new OMAGServerVirtualizationService();

    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param virtualizationConfig configuration properties for security sync server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/virtualization-service/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody VirtualizationConfig virtualizationConfig) {
        return adminAPI.setVirtualizerConfig(userId, serverName, virtualizationConfig);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/virtualization-service")
    public VoidResponse enableVirtualizationService(@PathVariable String userId,
                                                  @PathVariable String serverName) {
        return adminAPI.enableVirtualizationService(userId, serverName);
    }

}
