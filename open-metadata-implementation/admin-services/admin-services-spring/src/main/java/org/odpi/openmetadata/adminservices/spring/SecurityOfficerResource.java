/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerSecurityOfficerService;
import org.odpi.openmetadata.adminservices.configuration.properties.SecurityOfficerConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services", description="The administration services support the configuration of the open metadata and governance services within the OMAG Server Platform This configuration determines which of the open metadata and governance services are active.", externalDocs=@ExternalDocumentation(description="Administration Services",url="https://egeria.odpi.org/open-metadata-implementation/admin-services/"))

public class SecurityOfficerResource {

    private OMAGServerSecurityOfficerService adminAPI = new OMAGServerSecurityOfficerService();

    /**
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param securityOfficerConfig configuration properties for security officer server
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    @PostMapping(path = "/security-officer-service/configuration")
    public VoidResponse setAccessServicesConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestBody  SecurityOfficerConfig securityOfficerConfig)
    {
        return adminAPI.setSecurityOfficerConfig(userId, serverName, securityOfficerConfig);
    }

    @PostMapping(path = "/security-officer-service")
    public VoidResponse enableSecuritySyncService(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return adminAPI.enableSecurityOfficerService(userId, serverName);
    }
}
