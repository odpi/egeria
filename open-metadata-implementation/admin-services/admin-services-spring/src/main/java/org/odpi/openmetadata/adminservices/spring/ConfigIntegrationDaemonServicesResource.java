/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationDaemonServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.rest.IntegrationDaemonServicesResponse;
import org.odpi.openmetadata.adminservices.rest.IntegrationGroupsResponse;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForIntegrationDaemonServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigIntegrationServicesResource provides the configuration for setting up the dynamic integration groups.
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

public class ConfigIntegrationDaemonServicesResource
{
    private final OMAGServerAdminForIntegrationDaemonServices adminAPI = new OMAGServerAdminForIntegrationDaemonServices();


    /**
     * Return the integration groups configuration for this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return response containing the integration groups configuration
     */
    @GetMapping("/integration-groups/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getIntegrationGroupsConfiguration",
            description="Return the integration groups configuration for this server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public IntegrationGroupsResponse getIntegrationGroupsConfiguration(@PathVariable String serverName,
                                                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getIntegrationGroupsConfiguration(serverName, delegatingUserId);
    }


    /**
     * Add configuration for a single integration group to the server's config document.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param groupConfig  all values to configure an integration group
     *
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-groups/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="configureIntegrationGroup",
            description="Add configuration for a single integration group to the server's config document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse configureIntegrationGroup(@PathVariable String                 serverName,
                                                  @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                  @RequestBody IntegrationGroupConfig groupConfig)
    {
        return adminAPI.configureIntegrationGroup(serverName, delegatingUserId, groupConfig);
    }


    /**
     * Set up the configuration for all the open metadata integration groups.  This overrides the current values.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     * @param integrationGroupsConfig  list of configuration properties for each integration group.
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    @PostMapping(path = "/integration-groups/configuration/all")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setIntegrationGroupsConfig",
            description="Set up the configuration for all the open metadata integration groups.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse setIntegrationGroupsConfig(@PathVariable String                       serverName,
                                                   @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                   @RequestBody  List<IntegrationGroupConfig> integrationGroupsConfig)
    {
        return adminAPI.setIntegrationGroupsConfig(serverName, delegatingUserId, integrationGroupsConfig);
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/integration-groups")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAllIntegrationGroups",
            description="Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse clearAllIntegrationGroups(@PathVariable String serverName,
                                                  @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearAllIntegrationGroups(serverName, delegatingUserId);
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param groupQualifiedName integration group name used in URL
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName  parameter.
     */
    @DeleteMapping(path = "/integration-groups/{groupQualifiedName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearIntegrationGroup",
            description="Remove an integration group.  This removes all configuration for the integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse clearIntegrationGroup(@PathVariable String serverName,
                                              @PathVariable String groupQualifiedName,
                                              @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearIntegrationGroup(serverName, delegatingUserId, groupQualifiedName);
    }


    /**
     * Return the configuration of the specialist services for an Integration Daemon OMAG Server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return response containing the integration daemon services configuration
     */
    @GetMapping("/integration-daemon-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getIntegrationDaemonServicesConfiguration",
            description="Return the configuration of the specialist services for an Integration Daemon OMAG Server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public IntegrationDaemonServicesResponse getIntegrationDaemonServicesConfiguration(@PathVariable String serverName,
                                                                                       @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.getIntegrationDaemonServicesConfiguration(serverName, delegatingUserId);
    }


    /**
     * Set up the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param servicesConfig full configuration for the integration daemon server.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-daemon-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setIntegrationDaemonServicesConfig",
            description="Set up the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse setIntegrationDaemonServicesConfig(@PathVariable String                         serverName,
                                                           @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId,
                                                           @RequestBody IntegrationDaemonServicesConfig servicesConfig)
    {
        return adminAPI.setIntegrationDaemonServicesConfig(serverName, delegatingUserId, servicesConfig);
    }


    /**
     * Remove the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/integration-daemon-services")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearIntegrationDaemonServicesConfig",
            description="Remove the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse clearIntegrationDaemonServicesConfig(@PathVariable String serverName,
                                                             @Parameter(description="delegating user id")  @RequestParam(required = false) String delegatingUserId)
    {
        return adminAPI.clearIntegrationDaemonServicesConfig(serverName, delegatingUserId);
    }
}
