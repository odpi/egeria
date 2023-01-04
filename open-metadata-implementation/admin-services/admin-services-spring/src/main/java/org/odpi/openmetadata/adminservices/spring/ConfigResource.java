/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
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
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/configuration")
    public OMAGServerConfigResponse getStoredConfiguration(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return adminAPI.getStoredConfiguration(userId, serverName);
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param omagServerConfig  configuration for the server
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    @PostMapping(path = "/configuration")
    public VoidResponse setOMAGServerConfig(@PathVariable String           userId,
                                            @PathVariable String           serverName,
                                            @RequestBody  OMAGServerConfig omagServerConfig)
    {
        return adminAPI.setOMAGServerConfig(userId, serverName, omagServerConfig);
    }


    /**
     * Clear the configuration properties for an OMAG Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    @DeleteMapping(path = "/configuration")
    public VoidResponse clearOMAGServerConfig(@PathVariable String userId,
                                              @PathVariable String serverName)
    {
        return adminAPI.clearOMAGServerConfig(userId, serverName);
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatform  location of the platform where the config is to be deployed to
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration or
     * OMAGInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    @PostMapping(path = "/configuration/deploy")
    public VoidResponse deployOMAGServerConfig(@PathVariable String           userId,
                                               @PathVariable String           serverName,
                                               @RequestBody  URLRequestBody   destinationPlatform)
    {
        return adminAPI.deployOMAGServerConfig(userId, serverName, destinationPlatform);
    }
}
