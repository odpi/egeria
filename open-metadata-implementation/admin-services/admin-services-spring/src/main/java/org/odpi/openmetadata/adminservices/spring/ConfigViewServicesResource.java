/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerAdminForViewServices;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ConfigViewServicesResource provides the configuration for setting up the Open Metadata View
 * Services (OMVSs).
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param userId calling user
     * @param serverName name of server
     * @return list of view service descriptions
     */
    @GetMapping(path = "/view-services")
    public RegisteredOMAGServicesResponse getConfiguredViewServices(@PathVariable String userId,
                                                                    @PathVariable String serverName)
    {
        return adminAPI.getConfiguredViewServices(userId, serverName);
    }


    /**
     * Return the view services configuration for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     * @return response containing the view services configuration
     */
    @GetMapping(path = "/view-services/configuration")
    public ViewServicesResponse getViewServicesConfiguration(@PathVariable String userId,
                                                             @PathVariable String serverName)
    {
        return adminAPI.getViewServicesConfiguration(userId, serverName);
    }


    /**
     * Return the configuration of a specific view service.
     *
     * @param userId calling user
     * @param serverName name of server
     * @param serviceURLMarker string indicating the view service of interest
     * @return response containing the configuration of the view service
     */
    @GetMapping("/view-services/{serviceURLMarker}")
    public ViewServiceConfigResponse getViewServiceConfig(@PathVariable String              userId,
                                                     @PathVariable String                   serverName,
                                                     @PathVariable String                   serviceURLMarker)
    {
        return adminAPI.getViewServiceConfig(userId, serverName, serviceURLMarker);
    }


    /**
     * Configure a single view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName       local server name.
     * @param serviceURLMarker string indicating which view service it is configuring
     * @param requestBody if specified, the view service config containing the remote OMAGServerName and OMAGServerPlatformRootURL that
     *                    the view service will use.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/view-services/{serviceURLMarker}")
    public VoidResponse configureViewService(@PathVariable  String            userId,
                                             @PathVariable  String            serverName,
                                             @PathVariable  String            serviceURLMarker,
                                             @RequestBody   ViewServiceConfig requestBody)
    {
        return adminAPI.configureViewService(userId, serverName, serviceURLMarker, requestBody);
    }


    // ============================================================================================================================== //
    //   NOTE: This API has been removed, possibly temporarily, until the design is completed for how multiple view services          //
    //         can be configured with the same config properties or default properties. See issue #3836 for details.                  //
    // ============================================================================================================================== //
    //
    // /**
    //  * Enable all view services that are registered with this server platform.
    //  * The view services are set up to use the default event bus.
    //  *
    //  * @param userId      user that is issuing the request.
    //  * @param serverName  local server name.
    //  * @param requestBody  view service config containing the remote OMAGServerName and OMAGServerPlatformRootURL for view services to use.
    //  * @return void response or
    //  * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
    //  * OMAGConfigurationErrorException the event bus has not been configured or
    //  * OMAGInvalidParameterException invalid serverName parameter.
    //  */
    // @PostMapping(path = "/view-services")
    // public VoidResponse configureAllViewServices(@PathVariable String            userId,
    //                                              @PathVariable String            serverName,
    //                                              @RequestBody  ViewServiceConfig requestBody)
    // {
    //     return adminAPI.configureAllViewServices(userId, serverName, requestBody);
    // }


    /**
     * Set up the configuration for selected open metadata view services (OMVSs).  This overrides
     * the current default values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param viewServicesConfig    list of configuration properties for each view service.
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @PostMapping(path = "/view-services/configuration")
    public VoidResponse setViewServicesConfig(@PathVariable String                  userId,
                                              @PathVariable String                  serverName,
                                              @RequestBody  List<ViewServiceConfig> viewServicesConfig)
    {
        return adminAPI.setViewServicesConfig(userId, serverName, viewServicesConfig);
    }

    /**
     * Remove the config for a view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker string indicating which view service to clear
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/view-services/{serviceURLMarker}")
    public VoidResponse clearViewService(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @PathVariable String serviceURLMarker)
    {
        return adminAPI.clearViewService(userId, serverName, serviceURLMarker);
    }



    /**
     * Disable the view services.  This removes all configuration for the view services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    @DeleteMapping(path = "/view-services")
    public VoidResponse clearAllViewServices(@PathVariable String userId,
                                             @PathVariable String serverName)
    {
        return adminAPI.clearAllViewServices(userId, serverName);
    }
}
