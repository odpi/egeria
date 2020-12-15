/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerAdminForEngineServices;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.rest.EngineServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.EngineServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigEngineServicesResource provides the configuration for setting up the Open Metadata Engine
 * Services (OMISs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))

public class ConfigEngineServicesResource
{
    private OMAGServerAdminForEngineServices adminAPI = new OMAGServerAdminForEngineServices();


    /**
     * Return the list of registered engine services for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of engine service descriptions
     */
    @GetMapping("/engine-services")

    public RegisteredOMAGServicesResponse getRegisteredEngineServices(@PathVariable String userId,
                                                                      @PathVariable String serverName)
    {
        return adminAPI.getRegisteredEngineServices(userId, serverName);
    }


    /**
     * Return the engine services configuration for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the engine services configuration
     */
    @GetMapping("/engine-services/configuration")

    public EngineServicesResponse getEngineServicesConfiguration(@PathVariable String userId,
                                                                 @PathVariable String serverName)
    {
        return adminAPI.getEngineServicesConfiguration(userId, serverName);
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

    public EngineServiceConfigResponse getEngineServiceConfiguration(@PathVariable String userId,
                                                                     @PathVariable String serverName,
                                                                     @PathVariable String serviceURLMarker)
    {
        return adminAPI.getEngineServiceConfiguration(userId, serverName, serviceURLMarker);
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

    public VoidResponse configureEngineService(@PathVariable String                   userId,
                                               @PathVariable String                   serverName,
                                               @PathVariable String                   serviceURLMarker,
                                               @RequestBody  EngineServiceRequestBody requestBody)
    {
        return adminAPI.configureEngineService(userId, serverName, serviceURLMarker, requestBody);
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

    public VoidResponse configureEngineService(@PathVariable String              userId,
                                               @PathVariable String              serverName,
                                               @RequestBody  EngineServiceConfig serviceConfig)
    {
        return adminAPI.configureEngineService(userId, serverName, serviceConfig);
    }


    /**
     * Set up the configuration for all of the open metadata engine services (OMISs).  This overrides
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

    public VoidResponse setEngineServicesConfig(@PathVariable String                    userId,
                                                @PathVariable String                    serverName,
                                                @RequestBody  List<EngineServiceConfig> engineServicesConfig)
    {
        return adminAPI.setEngineServicesConfig(userId, serverName, engineServicesConfig);
    }


    /**
     * Disable the engine services.  This removes all configuration for the engine services nad
     * hence the engine daemon.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/engine-services")
    public VoidResponse clearAllEngineServices(@PathVariable String userId,
                                               @PathVariable String serverName)
    {
        return adminAPI.clearAllEngineServices(userId, serverName);
    }


    /**
     * Remove an engine service.  This removes all configuration for the engine service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker engine service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    @DeleteMapping(path = "/engine-services/{serviceURLMarker}")

    public VoidResponse clearEngineService(@PathVariable String userId,
                                           @PathVariable String serverName,
                                           @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearEngineService(userId, serverName, serviceURLMarker);
    }
}
