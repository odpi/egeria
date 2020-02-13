/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerConfigOpenLineage;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageServerConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata Access
 * Services (OMASs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class ConfigOpenLineageResource
{
    private OMAGServerConfigOpenLineage adminAPI = new OMAGServerConfigOpenLineage();


    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param openLineageServerConfig configuration properties for open lineage server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/open-lineage/configuration")
    public VoidResponse setOpenLineageServicesConfig(@PathVariable String userId,
                                                     @PathVariable String serverName,
                                                     @RequestBody OpenLineageServerConfig openLineageServerConfig)
    {
        return adminAPI.setOpenLineageConfig(userId, serverName, openLineageServerConfig);
    }

    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    @DeleteMapping(path = "/open-lineage/configuration")
    public VoidResponse shutdown(@PathVariable String userId,
                               @PathVariable String serverName)
    {
        return adminAPI.removeOpenLineageConfig(userId, serverName);
    }


}
