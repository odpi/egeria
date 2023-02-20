/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigsResponse;
import org.springframework.web.bind.annotation.*;


/**
 * OMAGServerConfigsResource exposes APIs for server configurations.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/configurations")

@Tag(name="Administration Services - Server Configuration", description="The server configurations administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. A configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigsResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

    /**
     * Return all the server configuration documents
     *
     * @param userId  user that is issuing the request
     * @return OMAGServerConfigs properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid parameter occurred while processing.
     */
    @GetMapping()
    public OMAGServerConfigsResponse getStoredConfigurations(@PathVariable String userId)
    {
        return adminAPI.retrieveAllServerConfigs(userId);
    }
}
