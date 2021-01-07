/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerAdminForIntegrationServices;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.IntegrationServicesResponse;
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
                url="https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))

public class ConfigIntegrationServicesResource
{
    private OMAGServerAdminForIntegrationServices adminAPI = new OMAGServerAdminForIntegrationServices();


    /**
     * Return the list of registered integration services for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return list of integration service descriptions
     */
    @GetMapping("/integration-services")

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

    public IntegrationServiceConfigResponse getIntegrationServiceConfiguration(@PathVariable String userId,
                                                                               @PathVariable String serverName,
                                                                               @PathVariable String serviceURLMarker)
    {
        return adminAPI.getIntegrationServiceConfiguration(userId, serverName, serviceURLMarker);
    }


    /**
     * Enable a single registered integration service.  This builds the integration service configuration for the
     * server's config document.
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

    public VoidResponse configureIntegrationService(@PathVariable String                        userId,
                                                    @PathVariable String                        serverName,
                                                    @PathVariable String                        serviceURLMarker,
                                                    @RequestBody  IntegrationServiceRequestBody requestBody)
    {
        return adminAPI.configureIntegrationService(userId, serverName, serviceURLMarker, requestBody);
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

    public VoidResponse configureIntegrationService(@PathVariable String                   userId,
                                                    @PathVariable String                   serverName,
                                                    @RequestBody  IntegrationServiceConfig serviceConfig)
    {
        return adminAPI.configureIntegrationService(userId, serverName, serviceConfig);
    }


    /**
     * Set up the configuration for all of the open metadata integration services (OMISs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param integrationServicesConfig  list of configuration properties for each integration service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationServicesConfig parameter.
     */
    @PostMapping(path = "/integration-services/configuration/all")

    public VoidResponse setIntegrationServicesConfig(@PathVariable String                         userId,
                                                     @PathVariable String                         serverName,
                                                     @RequestBody  List<IntegrationServiceConfig> integrationServicesConfig)
    {
        return adminAPI.setIntegrationServicesConfig(userId, serverName, integrationServicesConfig);
    }


    /**
     * Disable the integration services.  This removes all configuration for the integration services nad
     * hence the integration daemon.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/integration-services")
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

    public VoidResponse clearIntegrationService(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearIntegrationService(userId, serverName, serviceURLMarker);
    }
}
