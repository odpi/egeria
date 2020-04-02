/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerConfigStewardshipEngineServices;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipEngineServicesConfig;
import org.odpi.openmetadata.adminservices.rest.StewardshipEngineServicesConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigStewardshipServicesResource provides the API for configuring the stewardship services in an OMAG
 * server called the stewardship server.
 *
 * The stewardship engine services are a client to the Stewardship Action Open Metadata Access Services (OMAS).
 * The configuration needs to provide the server name and platform URL root where the metadata server running
 * this service is located.
 *
 * They also need a list of the stewardship engines to run.  The definitions of these stewardship engines
 * are stored in the metadata server and are retrieved when the stewardship server starts.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/stewardship-engine-services")

@Tag(name="Administration Services", description="The administration services support the configuration of the open metadata and governance services within the OMAG Server Platform This configuration determines which of the open metadata and governance services are active.", externalDocs=@ExternalDocumentation(description="Administration Services",url="https://egeria.odpi.org/open-metadata-implementation/admin-services/"))

public class ConfigStewardshipEngineServicesResource
{
    private OMAGServerConfigStewardshipEngineServices adminAPI = new OMAGServerConfigStewardshipEngineServices();

    /**
     * Set up the name and platform URL root for the metadata server supporting this stewardship server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param clientConfig  URL root and server name for the metadata server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/client-config")
    public VoidResponse setClientConfig(@PathVariable String                 userId,
                                        @PathVariable String                 serverName,
                                        @RequestBody  OMAGServerClientConfig clientConfig)
    {
        return adminAPI.setClientConfig(userId, serverName, clientConfig);
    }


    /**
     * Set up the list of stewardship engines that are to run in the stewardship service.
     * The definition of these stewardship engines
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param stewardshipEngines  stewardshipEngines for server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @PostMapping(path = "/stewardship-engines")
    public VoidResponse setStewardshipEngines(@PathVariable String       userId,
                                            @PathVariable String       serverName,
                                            @RequestBody  List<String> stewardshipEngines)
    {
        return adminAPI.setStewardshipEngines(userId, serverName, stewardshipEngines);
    }


    /**
     * Add this service to the server configuration in one call.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "")
    public VoidResponse setStewardshipEngineServicesConfig(@PathVariable String                         userId,
                                                         @PathVariable String                         serverName,
                                                         @RequestBody  StewardshipEngineServicesConfig  servicesConfig)
    {
        return adminAPI.setStewardshipEngineServicesConfig(userId, serverName, servicesConfig);
    }


    /**
     * Get the configuration for the stewardship engine services for this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return  full configuration for the service.
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "")
    public StewardshipEngineServicesConfigResponse getStewardshipEngineServicesConfig(@PathVariable String userId,
                                                                                      @PathVariable String serverName)
    {
        return adminAPI.getStewardshipEngineServicesConfig(userId, serverName);
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "")
    public VoidResponse clearStewardshipEngineServicesConfig(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return adminAPI.clearStewardshipEngineServicesConfig(userId, serverName);
    }
}
