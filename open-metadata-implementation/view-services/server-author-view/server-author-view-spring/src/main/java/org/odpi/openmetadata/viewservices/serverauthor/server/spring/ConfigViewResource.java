package org.odpi.openmetadata.viewservices.serverauthor.server.spring;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorPlatformsResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * OMAGServerConfigResource returns the current configuration document for the server.  If the
 * configuration document is not found, a new one is created.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}")

@Tag(name="View Server: Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/omvs/server-author/overview"))

public class ConfigViewResource
{
    private final ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();

    /**
     * Return the known platforms
     *
     * @param userId  user that is issuing the request
     * @param serverName local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/platforms")
    public ServerAuthorPlatformsResponse getKnownPlatforms(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return adminAPI.getKnownPlatforms(userId, serverName);
    }

    /**
     * Return the stored configuration document for the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param serverBeingRetrievedName name of the server to be retrieved for configuration.
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/servers/{serverBeingRetrievedName}/configuration")
    public ServerAuthorConfigurationResponse getStoredConfiguration(@PathVariable String userId,
                                                                    @PathVariable String serverName,
                                                                    @PathVariable String serverBeingRetrievedName)
    {
        return adminAPI.getStoredConfiguration(userId, serverName, serverBeingRetrievedName);
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param serverBeingConfiguredName name of the server to be configured
     * @param omagServerConfig  configuration for the server
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    @PostMapping(path = "/servers/{serverBeingConfiguredName}/configuration")
    public ServerAuthorConfigurationResponse setOMAGServerConfig(@PathVariable String           userId,
                                            @PathVariable String           serverName,
                                            @PathVariable String           serverBeingConfiguredName,
                                            @RequestBody  OMAGServerConfig omagServerConfig)
    {
        return adminAPI.setOMAGServerConfig(userId, serverName, serverBeingConfiguredName, omagServerConfig);
    }

    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param serverBeingDeployedName name of the server to be configured.
     * @param destinationPlatformName  name of the platform where the config is to be deployed to
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration or
     * OMAGInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    @PostMapping(path = "/servers/{serverBeingDeployedName}/configuration/deploy")
    public ServerAuthorConfigurationResponse deployOMAGServerConfig(@PathVariable String           userId,
                                               @PathVariable String           serverName,
                                               @PathVariable String           serverBeingDeployedName,
                                               @RequestParam(value = "destinationPlatformName") String destinationPlatformName)
    {
        return adminAPI.deployOMAGServerConfig(userId, serverName, destinationPlatformName, serverBeingDeployedName);
    }
}
