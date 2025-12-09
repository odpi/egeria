/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformOriginServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * OMAGServerPlatformOriginResource provides the Spring wrapper for the origin service that helps the client
 * discover the type of the server platform.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/server-platform")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerPlatformOriginResource
{
    OMAGServerPlatformOriginServices originAPI = new OMAGServerPlatformOriginServices();

    /**
     * Return the origin of this server platform implementation.
     *
     * @return String description
     */
    @GetMapping(path = "/origin")

    @Operation( summary = "getServerPlatformOrigin",
            description="Retrieve a string that details the provider and version of this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="OMAG Server Platform origin description showing Egeria release",
                            content = @Content(mediaType ="text/plain"))
            })

    public String getServerPlatformOrigin() throws UserNotAuthorizedException
    {
        return originAPI.getServerPlatformOrigin();
    }


    /**
     * Retrieve the name of the organization running this platform.
     *
     * @return String description
     */
    @GetMapping(path = "/organization-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation( summary = "getServerPlatformOrganizationName",
            description="Retrieve the name of the organization running this platform.",
            responses = {
                    @ApiResponse(responseCode = "200",description="OMAG Server Platform Owning Organization",
                            content = @Content(mediaType ="text/plain"))
            })

    public String getServerPlatformOrganizationName() throws UserNotAuthorizedException
    {
        return originAPI.getServerPlatformOrganizationName();
    }
}
