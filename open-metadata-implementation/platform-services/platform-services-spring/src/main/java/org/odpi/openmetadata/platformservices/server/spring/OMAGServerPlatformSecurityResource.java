/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformSecurityServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerPlatformSecurityResource provides the API to configure the security connector that validates
 * platform requests that do not reference an OMAG server.  These requests are used by the
 * team that run the platform as a service.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/server-platform/security")
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

public class OMAGServerPlatformSecurityResource
{
    private final OMAGServerPlatformSecurityServices adminSecurityAPI = new OMAGServerPlatformSecurityServices();


    /**
     * Set up a platform metadata security connector
     *
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPlatformSecurityConnection",
               description="Set up a platform metadata security connector to control access to the platform and admin services.  This overrides the ",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/platform-services/overview/#dynamically-configuring-the-platform-metadata-security-connector"))

    public VoidResponse setPlatformSecurityConnection(@RequestBody  PlatformSecurityRequestBody requestBody)
    {
        return adminSecurityAPI.setPlatformSecurityConnection(requestBody);
    }


    /**
     * Return the connection object for platform metadata security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @return connection response
     */
    @GetMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getPlatformSecurityConnection",
               description="Return the connection object for platform metadata security connector.  Null is returned if no connector has been set up.",
               externalDocs=@ExternalDocumentation(description="Metadata Security",
                                                   url="https://egeria-project.org/features/metadata-security/overview/"))


    public ConnectionResponse getPlatformSecurityConnection()
    {
        return adminSecurityAPI.getPlatformSecurityConnection();
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up.
     *
     * @return void response
     */
    @DeleteMapping(path = "/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearPlatformSecurityConnection",
               description="Clear the connection object for platform security.  This means there is no platform security set up. Note that this command must be issued by a user that has permission to operate the OMAG Server Platform.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/platform-services/overview/#dynamically-configuring-the-platform-metadata-security-connector"))

    public  VoidResponse clearPlatformSecurityConnection()
    {
        return adminSecurityAPI.clearPlatformSecurityConnection();
    }
}
