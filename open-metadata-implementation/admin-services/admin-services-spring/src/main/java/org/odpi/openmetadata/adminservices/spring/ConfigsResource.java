/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigsResponse;
import org.springframework.web.bind.annotation.*;


/**
 * OMAGServerConfigsResource exposes APIs for server configurations.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/configurations")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Server Configuration", description="The server configurations administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. A configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigsResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

    /**
     * Return all the server configuration documents.
     *
     * @return OMAGServerConfigs properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid parameter occurred while processing.
     */
    @GetMapping()
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getStoredConfigurations",
               description="Return all the server configuration documents.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigsResponse getStoredConfigurations()
    {
        return adminAPI.retrieveAllServerConfigs();
    }
}
