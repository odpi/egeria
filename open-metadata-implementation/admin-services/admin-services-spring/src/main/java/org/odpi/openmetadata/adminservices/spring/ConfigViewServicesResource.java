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
import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForViewServices;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigViewServicesResource provides the configuration for setting up the Open Metadata View
 * Services (OMVSs).
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

public class ConfigViewServicesResource
{
    private final OMAGServerAdminForViewServices adminAPI = new OMAGServerAdminForViewServices();


    /**
     * Return the list of view services that are configured for this server.
     *
     * @param serverName name of server
     * @return list of view service descriptions
     */
    @GetMapping(path = "/view-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfiguredViewServices",
               description="Return the list of view services that are configured for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public RegisteredOMAGServicesResponse getConfiguredViewServices(@PathVariable String serverName)
    {
        return adminAPI.getConfiguredViewServices(serverName);
    }


    /**
     * Return the view services configuration for this server.
     *
     * @param serverName name of server
     * @return response containing the view services configuration
     */
    @GetMapping(path = "/view-services/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getViewServicesConfiguration",
               description="Return the view services configuration for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public ViewServicesResponse getViewServicesConfiguration(@PathVariable String serverName)
    {
        return adminAPI.getViewServicesConfiguration(serverName);
    }


    /**
     * Add the view services configuration for this server as a single call.
     * This operation is used for editing existing view service configuration.
     *
     * @param serverName name of server
     * @param viewServiceConfigs list of configured view services
     * @return void
     */
    @PostMapping(path = "/view-services/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setViewServicesConfiguration",
            description="Add the view services configuration for this server as a single call.  This operation is used for editing existing view service configuration.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/"))

    public VoidResponse setViewServicesConfiguration(@PathVariable String                  serverName,
                                                     @RequestBody  List<ViewServiceConfig> viewServiceConfigs)
    {
        return adminAPI.setViewServicesConfiguration(serverName, viewServiceConfigs);
    }


    /**
     * Return the configuration of a specific view service.
     *
     * @param serverName name of server
     * @param serviceURLMarker string indicating the view service of interest
     * @return response containing the configuration of the view service
     */
    @GetMapping("/view-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getViewServiceConfig",
               description="Return the view services configuration for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public ViewServiceConfigResponse getViewServiceConfig(@PathVariable String serverName,
                                                          @PathVariable String serviceURLMarker)
    {
        return adminAPI.getViewServiceConfig(serverName, serviceURLMarker);
    }


    /**
     * Configure a single view service.
     *
     * @param serverName       local server name.
     * @param serviceURLMarker string indicating which view service it is configuring
     * @param requestBody if specified, the view service config containing the remote OMAGServerName and OMAGServerPlatformRootURL that
     *                    the view service will use.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/view-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureViewService",
               description="Configure a single view service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public VoidResponse configureViewService(@PathVariable  String                 serverName,
                                             @PathVariable  String                 serviceURLMarker,
                                             @RequestBody   ViewServiceRequestBody requestBody)
    {
        return adminAPI.configureViewService(serverName, serviceURLMarker, requestBody);
    }


    /**
     * Enable all view services that are registered with this server platform.
     *
     * @param serverName  local server name.
     * @param requestBody  view service config containing the remote OMAGServerName and OMAGServerPlatformRootURL for view services to use.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/view-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureAllViewServices",
            description="Enable all view services that are registered with this OMAG server platform.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/"))

    public VoidResponse configureAllViewServices(@PathVariable String                 serverName,
                                                 @RequestBody  ViewServiceRequestBody requestBody)
    {
        return adminAPI.configureAllViewServices(serverName, requestBody);
    }


    /**
     * Remove the config for a view service.
     *
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which view service to clear
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/view-services/{serviceURLMarker}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearViewService",
               description="Remove the config for a view service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public VoidResponse clearViewService(@PathVariable String serverName,
                                         @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearViewService(serverName, serviceURLMarker);
    }


    /**
     * Disable the view services.  This removes all configuration for the view services.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/view-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAllViewServices",
               description="Disable the view services.  This removes all configuration for the view services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omvs/"))

    public VoidResponse clearAllViewServices(@PathVariable String serverName)
    {
        return adminAPI.clearAllViewServices(serverName);
    }
}
