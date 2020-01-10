/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerSecuritySyncService;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import static org.odpi.openmetadata.commonservices.spring.SpringUtils.createSpringResponse;

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class SecuritySyncResource
{
    private OMAGServerSecuritySyncService adminAPI = new OMAGServerSecuritySyncService();

    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param securitySyncConfig configuration properties for security sync server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/security-sync-service/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody  SecuritySyncConfig securitySyncConfig)
    {
        return createSpringResponse(adminAPI.setSecuritySyncConfig(userId, serverName, securitySyncConfig));
    }

    @PostMapping(path = "/security-sync-service")
    public VoidResponse enableSecuritySyncService(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return createSpringResponse(adminAPI.enableSecuritySyncService(userId, serverName));
    }
}
