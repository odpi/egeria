/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminForEngineHostServices;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigEngineServicesResource provides the configuration for setting up the Open Metadata Engine
 * Services (OMESs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigEngineHostServicesResource
{
    private final OMAGServerAdminForEngineHostServices adminAPI = new OMAGServerAdminForEngineHostServices();


    /**
     * Return the list of registered engine services for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of engine service descriptions
     */
    @GetMapping("/engine-services")

    @Operation(summary="getConfiguredEngineServices",
               description="Return the list of registered engine services for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public RegisteredOMAGServicesResponse getConfiguredEngineServices(@PathVariable String userId,
                                                                      @PathVariable String serverName)
    {
        return adminAPI.getConfiguredEngineServices(userId, serverName);
    }



    /**
     * Return the configuration for the named engine service for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @param serviceURLMarker engine service name used in URL
     * @return response containing the engine services configuration
     */
    @GetMapping("/engine-services/{serviceURLMarker}/configuration")

    @Operation(summary="getEngineServiceConfiguration",
               description="Return the configuration for the named engine service for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public EngineServiceConfigResponse getEngineServiceConfiguration(@PathVariable String userId,
                                                                     @PathVariable String serverName,
                                                                     @PathVariable String serviceURLMarker)
    {
        return adminAPI.getEngineServiceConfiguration(userId, serverName, serviceURLMarker);
    }


    /**
     * Set up the name and platform URL root for the metadata server running the Governance Engine OMAS that provides
     * the governance engine definitions used by the engine services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param clientConfig  URL root and server name for the metadata server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping("/engine-definitions/client-config")

    @Operation(summary="setEngineDefinitionsClientConfig",
               description="Set up the name and platform URL root for the metadata server running the Governance Engine OMAS that provides" +
                                   " the governance engine definitions used by the engine services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse setEngineDefinitionsClientConfig(@PathVariable String                 userId,
                                                         @PathVariable String                 serverName,
                                                         @RequestBody  OMAGServerClientConfig clientConfig)
    {
        return adminAPI.setEngineDefinitionsClientConfig(userId, serverName, clientConfig);
    }


    /**
     * Set up the list of governance engines that will use the metadata from the same metadata access server as the
     * engine host uses for retrieving the engine configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param engines  URL root and server name for the metadata server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping("/engine-list")

    @Operation(summary="setEngineList",
            description="Set up the list of governance engine that will use the metadata from the same metadata access server as the" +
                    " engine host uses for retrieving the engine configuration.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse setEngineList(@PathVariable String             userId,
                                      @PathVariable String             serverName,
                                      @RequestBody  List<EngineConfig> engines)
    {
        return adminAPI.setEngineList(userId, serverName, engines);
    }


    /**
     * Enable a single registered engine service.  This builds the engine service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param serviceURLMarker string indicating which engine service it is configuring
     * @param requestBody  minimum values to configure an engine service
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/engine-services/{serviceURLMarker}")

    @Operation(summary="configureEngineService",
               description="Enable a single registered engine service.  This builds the engine service configuration for the" +
                                   " server's config document.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse configureEngineService(@PathVariable String                   userId,
                                               @PathVariable String                   serverName,
                                               @PathVariable String                   serviceURLMarker,
                                               @RequestBody  EngineServiceRequestBody requestBody)
    {
        return adminAPI.configureEngineService(userId, serverName, serviceURLMarker, requestBody);
    }


    /**
     * Enable all non-deprecated, registered engine service.  This builds the engine service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param requestBody  minimum values to configure an engine service
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/engine-services")

    @Operation(summary="configureAllEngineServices",
            description="Enable all non-deprecated, registered engine service.  This builds the engine service configuration for the" +
                    " server's config document.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omes/"))

    public VoidResponse configureAllEngineServices(@PathVariable String                   userId,
                                                   @PathVariable String                   serverName,
                                                   @RequestBody  EngineServiceRequestBody requestBody)
    {
        return adminAPI.configureAllEngineServices(userId, serverName, requestBody);
    }


    /**
     * Add configuration for a single engine service to the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceConfig  all values to configure an engine service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/engine-services/configuration")

    @Operation(summary="configureEngineService",
               description="Add configuration for a single engine service to the server's config document.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse configureEngineService(@PathVariable String              userId,
                                               @PathVariable String              serverName,
                                               @RequestBody  EngineServiceConfig serviceConfig)
    {
        return adminAPI.configureEngineService(userId, serverName, serviceConfig);
    }


    /**
     * Set up the configuration for all the open metadata engine services (OMESs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param engineServicesConfig  list of configuration properties for each engine service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or engineServicesConfig parameter.
     */
    @PostMapping(path = "/engine-services/configuration/all")

    @Operation(summary="setEngineServicesConfig",
               description="Set up the configuration for all the open metadata engine services (OMESs).  This overrides the current values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse setEngineServicesConfig(@PathVariable String                    userId,
                                                @PathVariable String                    serverName,
                                                @RequestBody  List<EngineServiceConfig> engineServicesConfig)
    {
        return adminAPI.setEngineServicesConfig(userId, serverName, engineServicesConfig);
    }




    /**
     * Remove the configuration for the Governance Engine OMAS Engine client configuration in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/engine-definitions/client-config")

    @Operation(summary="clearEngineDefinitionsClientConfig",
               description="Remove the configuration for the Governance Engine OMAS Engine client configuration in a single call.  " +
                                   "This overrides the current values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse clearEngineDefinitionsClientConfig(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return adminAPI.clearEngineDefinitionsClientConfig(userId, serverName);
    }


    /**
     * Remove the configuration for the engine list in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/engine-list")

    @Operation(summary="clearEngineList",
            description="Remove the configuration for the engine list in a single call.  " +
                    "This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-engine/"))

    public VoidResponse clearEngineList(@PathVariable String userId,
                                        @PathVariable String serverName)
    {
        return adminAPI.clearEngineList(userId, serverName);
    }


    /**
     * Disable the engine services.  This removes all configuration for the engine services in the engine host.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/engine-services")

    @Operation(summary="clearAllEngineServices",
               description="Disable the engine services.  This removes all configuration for the engine services in the engine host.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse clearAllEngineServices(@PathVariable String userId,
                                               @PathVariable String serverName)
    {
        return adminAPI.clearAllEngineServices(userId, serverName);
    }


    /**
     * Remove an engine service.  This removes all configuration for the engine service.  Other configured engine services are untouched.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker engine service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    @DeleteMapping(path = "/engine-services/{serviceURLMarker}")

    @Operation(summary="clearEngineService",
               description="Remove an engine service.  This removes all configuration for the engine service.  Other configured engine services are untouched.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/services/omes/"))

    public VoidResponse clearEngineService(@PathVariable String userId,
                                           @PathVariable String serverName,
                                           @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearEngineService(userId, serverName, serviceURLMarker);
    }


    /**
     * Return the configuration of the specialist services for an Engine Host OMAG Server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the engine host services configuration
     */
    @GetMapping({"/engine-host-services/configuration", "/engine-host-services"})

    @Operation(summary="getEngineHostServicesConfiguration",
            description="Return the configuration of the specialist services for an Engine Host OMAG Server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public EngineHostServicesResponse getEngineHostServicesConfiguration(@PathVariable String userId,
                                                                         @PathVariable String serverName)
    {
        return adminAPI.getEngineHostServicesConfiguration(userId, serverName);
    }


    /**
     * Set up the configuration of the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the engine host server.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/engine-host-services")

    @Operation(summary="setEngineHostServicesConfig",
            description="Set up the configuration of the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public VoidResponse setEngineHostServicesConfig(@PathVariable String                   userId,
                                                    @PathVariable String                   serverName,
                                                    @RequestBody  EngineHostServicesConfig servicesConfig)
    {
        return adminAPI.setEngineHostServicesConfig(userId, serverName, servicesConfig);
    }


    /**
     * Remove the configuration of the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/engine-host-services")

    @Operation(summary="clearEngineHostServicesConfig",
            description="Remove the specialist services for an Engine Host OMAG Server in a single call.  This overrides the current values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-host/"))

    public VoidResponse clearEngineHostServicesConfig(@PathVariable String userId,
                                                      @PathVariable String serverName)
    {
        return adminAPI.clearEngineHostServicesConfig(userId, serverName);
    }
}
