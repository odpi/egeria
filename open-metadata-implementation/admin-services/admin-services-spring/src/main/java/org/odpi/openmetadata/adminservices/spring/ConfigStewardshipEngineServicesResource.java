/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerConfigStewardshipEngineServices;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipEngineServicesConfig;
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
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @PostMapping(path = "/access-service")

    public VoidResponse setAccessServiceLocation(@PathVariable String                    userId,
                                                 @PathVariable String                    serverName,
                                                 @RequestBody OMAGServerClientConfig clientConfig)
    {
        return adminAPI.setAccessServiceLocation(userId, serverName, clientConfig);
    }


    /**
     * Set up the list of stewardship engines that are to run in the stewardship service.
     * The definition of these stewardship engines
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param stewardshipEngines  stewardship engines for server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
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
     * @return void response
     */
    @PostMapping(path = "")

    VoidResponse addService(@PathVariable String                          userId,
                            @PathVariable String                          serverName,
                            @RequestBody  StewardshipEngineServicesConfig servicesConfig)
    {
        return adminAPI.addService(userId, serverName, servicesConfig);
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    @DeleteMapping(path = "")

    VoidResponse deleteService(@PathVariable String userId,
                               @PathVariable String serverName)
    {
        return adminAPI.deleteService(userId, serverName);
    }
}
