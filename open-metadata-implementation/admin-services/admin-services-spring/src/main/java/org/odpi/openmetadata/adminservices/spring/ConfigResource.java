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
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * OMAGServerConfigResource returns the current configuration document for the server.  If the
 * configuration document is not found, a new one is created.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/servers/{serverName}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();

    /**
     * Return the stored configuration document for the server.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfig properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getStoredConfiguration",
               description="Return the stored configuration document for the server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigResponse getStoredConfiguration(@PathVariable String serverName,
                                                           @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getStoredConfiguration(serverName, delegatingUserId);
    }


    /**
     * Return the stored configuration document for the server.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfig properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/configuration/resolved")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getResolvedConfiguration",
            description="Return the configuration document for the server with the placeholders resolved.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/configuration-document/"))

    public OMAGServerConfigResponse getResolvedConfiguration(@PathVariable String serverName,
                                                             @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getResolvedConfiguration(serverName, delegatingUserId);
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param omagServerConfig  configuration for the server
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    @PostMapping(path = "/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setOMAGServerConfig",
               description="Set up the configuration properties for an OMAG Server in a single command.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse setOMAGServerConfig(@PathVariable String           serverName,
                                            @RequestParam(required = false) String delegatingUserId,
                                            @RequestBody  OMAGServerConfig omagServerConfig)
    {
        return adminAPI.setOMAGServerConfig(serverName, delegatingUserId, omagServerConfig);
    }


    /**
     * Clear the configuration properties for an OMAG Server in a single command.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    @DeleteMapping(path = "/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearOMAGServerConfig",
               description="Clear the configuration properties for an OMAG Server in a single command.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse clearOMAGServerConfig(@PathVariable String serverName,
                                              @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearOMAGServerConfig(serverName, delegatingUserId);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param destinationPlatform  location of the platform where the config is to be deployed to
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException a problem using the supplied configuration or
     * InvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    @PostMapping(path = "/configuration/deploy")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deployOMAGServerConfig",
               description="Push the configuration for the server to another OMAG Server Platform.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/configuration-document/"))

    public VoidResponse deployOMAGServerConfig(@PathVariable String           serverName,
                                               @RequestParam(required = false) String delegatingUserId,
                                               @RequestBody  URLRequestBody   destinationPlatform)
    {
        return adminAPI.deployOMAGServerConfig(serverName, delegatingUserId, destinationPlatform);
    }
}
