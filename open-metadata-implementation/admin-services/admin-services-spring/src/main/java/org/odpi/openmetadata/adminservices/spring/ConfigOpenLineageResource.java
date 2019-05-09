/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminForOpenLineage;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigAccessServicesResource provides the configuration for setting up the Open Metadata Access
 * Services (OMASs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class ConfigOpenLineageResource {
    private OMAGServerAdminForOpenLineage adminAPI = new OMAGServerAdminForOpenLineage();


    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param openLineageConfig configuration properties for open lineage server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/open-lineage/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody OpenLineageConfig openLineageConfig) {
        return adminAPI.setOpenLineageConfig(userId, serverName, openLineageConfig);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/open-lineage")
    public VoidResponse enableOpenLineageServices(@PathVariable String userId,
                                             @PathVariable String serverName) {
        return adminAPI.enableOpenLineageService(userId, serverName);
    }


}
