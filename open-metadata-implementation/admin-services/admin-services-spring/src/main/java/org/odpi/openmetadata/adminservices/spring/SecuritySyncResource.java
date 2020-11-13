/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerSecuritySyncService;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))

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
        return adminAPI.setSecuritySyncConfig(userId, serverName, securitySyncConfig);
    }

    @PostMapping(path = "/security-sync-service")
    public VoidResponse enableSecuritySyncService(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return adminAPI.enableSecuritySyncService(userId, serverName);
    }
}
