/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerConfigDiscoveryEngineServices;
import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryEngineServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.rest.DiscoveryEngineServicesConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigDiscoveryEngineServicesResource provides the API for configuring the discovery engine services.
 * These services support the operation of one or more discovery engines in an OMAG server called the Discovery Server.
 *
 * The discovery engine services are a client to the Discovery Engine Open Metadata Access Services (OMAS).
 * The configuration needs to provide the server name and platform URL root where the metadata server running
 * this service is located.
 *
 * They also need a list of the discovery engines to run.  The definitions of these discovery engines
 * are stored in the metadata server and are retrieved when the discovery server starts.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/discovery-engine-services")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))

public class ConfigDiscoveryEngineServicesResource
{
    private OMAGServerConfigDiscoveryEngineServices adminAPI = new OMAGServerConfigDiscoveryEngineServices();

    /**
     * Set up the name and platform URL root for the metadata server supporting this discovery server.
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
     * Set up the list of discovery engines that are to run in the discovery service.
     * The definition of these discovery engines
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param discoveryEngines  discoveryEngines for server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     * OMAGConfigurationErrorException unexpected exception.
     */
    @PostMapping(path = "/discovery-engines")
    public VoidResponse setDiscoveryEngines(@PathVariable String       userId,
                                            @PathVariable String       serverName,
                                            @RequestBody  List<String> discoveryEngines)
    {
        return adminAPI.setDiscoveryEngines(userId, serverName, discoveryEngines);
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
    public VoidResponse setDiscoveryEngineServicesConfig(@PathVariable String                         userId,
                                                         @PathVariable String                         serverName,
                                                         @RequestBody  DiscoveryEngineServicesConfig  servicesConfig)
    {
        return adminAPI.setDiscoveryEngineServicesConfig(userId, serverName, servicesConfig);
    }


    /**
     * Get the configuration for the discovery engine services for this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return full configuration for the service.
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "")
    public DiscoveryEngineServicesConfigResponse getDiscoveryEngineServicesConfig(@PathVariable String userId,
                                                                                  @PathVariable String serverName)
    {
        return adminAPI.getDiscoveryEngineServicesConfig(userId, serverName);
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
    public VoidResponse clearDiscoveryEngineServicesConfig(@PathVariable String userId,
                                                           @PathVariable String serverName)
    {
        return adminAPI.clearDiscoveryEngineServicesConfig(userId, serverName);
    }
}
