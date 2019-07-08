/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerDataEngineProxyService;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * DataEngineProxyResource provides the API for configuring a data engine proxy in an OMAG
 * server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class DataEngineProxyResource {

    private OMAGServerDataEngineProxyService adminAPI = new OMAGServerDataEngineProxyService();

    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param dataEngineProxyConfig configuration properties for the data engine proxy
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/data-engine-proxy-service/configuration")
    public VoidResponse setDataEngineProxyConfig(@PathVariable String userId,
                                                 @PathVariable String serverName,
                                                 @RequestBody DataEngineProxyConfig dataEngineProxyConfig) {
        return adminAPI.setDataEngineProxyConfig(userId, serverName, dataEngineProxyConfig);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/data-engine-proxy-service")
    public VoidResponse enableDataEngineProxy(@PathVariable String userId,
                                              @PathVariable String serverName) {
        return adminAPI.enableDataEngineProxy(userId, serverName);
    }

}
