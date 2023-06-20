/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformOriginServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * OldServerOriginResource provides the Spring wrapper for the origin service that helps the client
 * discover the type of the server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}")

@Tag(name="Administration Services - Operational - Deprecated",
     description="Use the Platform Services.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/platform-services/overview/"))
@Deprecated
public class OldServerOriginResource
{
    OMAGServerPlatformOriginServices originAPI = new OMAGServerPlatformOriginServices();

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server that the request is for
     * @return String description
     */
    @GetMapping(path = "/servers/{serverName}/server-origin")
    @Deprecated
    @SuppressWarnings(value = "unused")
    public String getServerOrigin(@PathVariable String   userId,
                                  @PathVariable String   serverName)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    @GetMapping(path = "/server-origin")
    @Deprecated
    public String getServerOrigin(@PathVariable String   userId)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }
}
