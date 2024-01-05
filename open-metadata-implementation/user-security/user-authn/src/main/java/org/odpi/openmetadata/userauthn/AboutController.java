/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The AboutController provides build-related information such as group and artifact.
 */
@RestController
@RequestMapping("/api/about")

@Tag(name="API: User Security Services", description="The user security services provide user authentication services to a client program, plus identification details about this runtime implementation.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/features/metadata-security/overview"))

public class AboutController
{
    @Autowired( required = false )
    BuildProperties buildProperties;


    /**
     * Retrieve information about this build.
     *
     * @param request incoming request
     * @return build properties
     * @throws HttpClientErrorException problem accessing build details
     */

    @Operation(summary="getBuildProperties",
               description="Return the build properties of this server-side implementation.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/metadata-security/overview"))

    @GetMapping
    public BuildProperties getBuildProperties(HttpServletRequest request) throws HttpClientErrorException
    {
        return buildProperties;
    }
}
