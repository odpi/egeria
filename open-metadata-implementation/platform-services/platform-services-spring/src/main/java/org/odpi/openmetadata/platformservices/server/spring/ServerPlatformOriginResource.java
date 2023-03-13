/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformOriginServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * ServerPlatformOriginResource provides the Spring wrapper for the origin service that helps the client
 * discover the type of the server platform.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}")

@Tag(name="Platform Services",
        description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) Server Platform and discovering information about the OMAG Servers that it is hosting.",
        externalDocs=@ExternalDocumentation(description="Platform Services",url="https://egeria-project.org/services/platform-services/overview"))

public class ServerPlatformOriginResource
{
    OMAGServerPlatformOriginServices originAPI = new OMAGServerPlatformOriginServices();

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    @Deprecated
    @GetMapping(path = "/server-platform-origin")
    public String getOldServerOrigin(@PathVariable String   userId)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }


    /**
     * Return the origin of this server platform implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    @GetMapping(path = "/server-platform/origin")

    @Operation( summary = "Get origin description of this OMAG Server Platform",
            description="Retrieve a string that details the provider and version of this platform",
            responses = {
                    @ApiResponse(responseCode = "200",description="server platform origin description",
                            content = @Content(mediaType ="text/plain"))
            })

    public String getServerOrigin(@Parameter(description="calling user") @PathVariable String   userId)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }

}
