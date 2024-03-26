/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationDaemonServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForIntegrationDaemonServices;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigIntegrationServicesResource provides the configuration for setting up the Open Metadata Integration
 * Services (OMISs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the integration groups configuration
     */
    @GetMapping("/integration-groups/configuration")

    @Operation(summary="getIntegrationGroupsConfiguration",
            description="Return the integration groups configuration for this server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public IntegrationGroupsResponse getIntegrationGroupsConfiguration(@PathVariable String userId,
                                                                       @PathVariable String serverName)
    {
        return adminAPI.getIntegrationGroupsConfiguration(userId, serverName);
    }


    /**
     * Add configuration for a single integration group to the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param groupConfig  all values to configure an integration group
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-groups/configuration")

    @Operation(summary="configureIntegrationGroup",
            description="Add configuration for a single integration group to the server's config document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse configureIntegrationGroup(@PathVariable String                 userId,
                                                  @PathVariable String                 serverName,
                                                  @RequestBody IntegrationGroupConfig groupConfig)
    {
        return adminAPI.configureIntegrationGroup(userId, serverName, groupConfig);
    }


    /**
     * Set up the configuration for all the open metadata integration groups.  This overrides the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param integrationGroupsConfig  list of configuration properties for each integration group.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    @PostMapping(path = "/integration-groups/configuration/all")

    @Operation(summary="setIntegrationGroupsConfig",
            description="Set up the configuration for all the open metadata integration groups.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse setIntegrationGroupsConfig(@PathVariable String                       userId,
                                                   @PathVariable String                       serverName,
                                                   @RequestBody  List<IntegrationGroupConfig> integrationGroupsConfig)
    {
        return adminAPI.setIntegrationGroupsConfig(userId, serverName, integrationGroupsConfig);
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/integration-groups")

    @Operation(summary="clearAllIntegrationGroups",
            description="Disable the integration groups.  This removes all configuration for the integration groups from the integration daemon.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse clearAllIntegrationGroups(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return adminAPI.clearAllIntegrationGroups(userId, serverName);
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param groupQualifiedName integration group name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    @DeleteMapping(path = "/integration-groups/{groupQualifiedName}")

    @Operation(summary="clearIntegrationGroup",
            description="Remove an integration group.  This removes all configuration for the integration group.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-group/"))

    public VoidResponse clearIntegrationGroup(@PathVariable String userId,
                                              @PathVariable String serverName,
                                              @PathVariable String groupQualifiedName)
    {
        return adminAPI.clearIntegrationGroup(userId, serverName, groupQualifiedName);
    }


    /**
     * Return the list of registered integration services for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of integration service descriptions
     */
    @GetMapping("/integration-services")

    @Operation(summary="getRegisteredIntegrationServices",
               description="Return the list of registered integration services for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public RegisteredOMAGServicesResponse getRegisteredIntegrationServices(@PathVariable String userId,
                                                                           @PathVariable String serverName)
    {
        return adminAPI.getRegisteredIntegrationServices(userId, serverName);
    }


    /**
     * Return the integration services configuration for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the integration services configuration
     */
    @GetMapping("/integration-services/configuration")

    @Operation(summary="getIntegrationServicesConfiguration",
               description="Return the integration services configuration for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public IntegrationServicesResponse getIntegrationServicesConfiguration(@PathVariable String userId,
                                                                           @PathVariable String serverName)
    {
        return adminAPI.getIntegrationServicesConfiguration(userId, serverName);
    }


    /**
     * Return the configuration for the named integration service for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @param serviceURLMarker integration service name used in URL
     * @return response containing the integration services configuration
     */
    @GetMapping("/integration-services/{serviceURLMarker}/configuration")

    @Operation(summary="getIntegrationServiceConfiguration",
               description="Return the configuration for the named integration service for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public IntegrationServiceConfigResponse getIntegrationServiceConfiguration(@PathVariable String userId,
                                                                               @PathVariable String serverName,
                                                                               @PathVariable String serviceURLMarker)
    {
        return adminAPI.getIntegrationServiceConfiguration(userId, serverName, serviceURLMarker);
    }


    /**
     * Enable a single registered integration service.  This builds the integration service configuration for the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param serviceURLMarker string indicating which integration service it is configuring
     * @param requestBody  minimum values to configure an integration service
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-services/{serviceURLMarker}")

    @Operation(summary="configureIntegrationService",
               description="Enable a single registered integration service.  " +
                                   "This builds the integration service configuration for the server's config document.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public VoidResponse configureIntegrationService(@PathVariable String                        userId,
                                                    @PathVariable String                        serverName,
                                                    @PathVariable String                        serviceURLMarker,
                                                    @RequestBody  IntegrationServiceRequestBody requestBody)
    {
        return adminAPI.configureIntegrationService(userId, serverName, serviceURLMarker, requestBody);
    }


    /**
     * Enable all non-deprecated integration services.  This builds the integration service configuration for the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param requestBody  minimum values to configure an integration service
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-services")

    @Operation(summary="configureAllIntegrationServices",
            description="Enable all non-deprecated, registered integration services.  " +
                    "This builds the integration service configuration for the server's config document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omis/"))

    public VoidResponse configureAllIntegrationServices(@PathVariable String                        userId,
                                                        @PathVariable String                        serverName,
                                                        @RequestBody  IntegrationServiceRequestBody requestBody)
    {
        return adminAPI.configureAllIntegrationServices(userId, serverName, requestBody);
    }


    /**
     * Add configuration for a single integration service to the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceConfig  all values to configure an integration service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-services/configuration")

    @Operation(summary="configureIntegrationService",
               description="Add configuration for a single integration service to the server's config document.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public VoidResponse configureIntegrationService(@PathVariable String                   userId,
                                                    @PathVariable String                   serverName,
                                                    @RequestBody  IntegrationServiceConfig serviceConfig)
    {
        return adminAPI.configureIntegrationService(userId, serverName, serviceConfig);
    }


    /**
     * Set up the configuration for all the open metadata integration services (OMISs).  This overrides the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param integrationServicesConfig  list of configuration properties for each integration service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationServicesConfig parameter.
     */
    @PostMapping(path = "/integration-services/configuration/all")

    @Operation(summary="setIntegrationServicesConfig",
               description="Set up the configuration for all the open metadata integration services (OMISs).  This overrides the current values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public VoidResponse setIntegrationServicesConfig(@PathVariable String                         userId,
                                                     @PathVariable String                         serverName,
                                                     @RequestBody  List<IntegrationServiceConfig> integrationServicesConfig)
    {
        return adminAPI.setIntegrationServicesConfig(userId, serverName, integrationServicesConfig);
    }


    /**
     * Disable the integration services.  This removes all configuration for the integration services from the integration daemon.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/integration-services")

    @Operation(summary="clearAllIntegrationServices",
               description="Disable the integration services.  This removes all configuration for the integration services from the integration daemon.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public VoidResponse clearAllIntegrationServices(@PathVariable String userId,
                                                    @PathVariable String serverName)
    {
        return adminAPI.clearAllIntegrationServices(userId, serverName);
    }


    /**
     * Remove an integration service.  This removes all configuration for the integration service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker integration service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    @DeleteMapping(path = "/integration-services/{serviceURLMarker}")

    @Operation(summary="clearIntegrationService",
               description="Remove an integration service.  This removes all configuration for the integration service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omis/"))

    public VoidResponse clearIntegrationService(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearIntegrationService(userId, serverName, serviceURLMarker);
    }


    /**
     * Return the configuration of the specialist services for an Integration Daemon OMAG Server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the integration daemon services configuration
     */
    @GetMapping("/integration-daemon-services")

    @Operation(summary="getIntegrationDaemonServicesConfiguration",
            description="Return the configuration of the specialist services for an Integration Daemon OMAG Server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public IntegrationDaemonServicesResponse getIntegrationDaemonServicesConfiguration(@PathVariable String userId,
                                                                                       @PathVariable String serverName)
    {
        return adminAPI.getIntegrationDaemonServicesConfiguration(userId, serverName);
    }


    /**
     * Set up the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the integration daemon server.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/integration-daemon-services")

    @Operation(summary="setIntegrationDaemonServicesConfig",
            description="Set up the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse setIntegrationDaemonServicesConfig(@PathVariable String                         userId,
                                                           @PathVariable String                         serverName,
                                                           @RequestBody IntegrationDaemonServicesConfig servicesConfig)
    {
        return adminAPI.setIntegrationDaemonServicesConfig(userId, serverName, servicesConfig);
    }


    /**
     * Remove the configuration of the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/integration-daemon-services")

    @Operation(summary="clearIntegrationDaemonServicesConfig",
            description="Remove the specialist services for an Integration Daemon OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-daemon/"))

    public VoidResponse clearIntegrationDaemonServicesConfig(@PathVariable String userId,
                                                             @PathVariable String serverName)
    {
        return adminAPI.clearIntegrationDaemonServicesConfig(userId, serverName);
    }
}
