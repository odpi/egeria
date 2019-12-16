/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerConfigDiscoveryServer;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigDiscoveryEngineResource provides the API for configuring a discovery engine in an OMAG
 * server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/discovery-server")
public class ConfigDiscoveryEngineResource
{
    private OMAGServerConfigDiscoveryServer adminAPI = new OMAGServerConfigDiscoveryServer();

    /**
     * Set up the root URL of the OMAG Server Platform where the metadata repository for
     * the discovery engines is located.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceRootURL  URL root for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @PostMapping( path = "access-service-root-url")

    public VoidResponse setAccessServiceRootURL(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestParam String accessServiceRootURL)
    {
        return adminAPI.setAccessServiceRootURL(userId, serverName, accessServiceRootURL);
    }


    /**
     * Set up the server name of the Discovery Engine OMAS.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceServerName  server name for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @PostMapping( path = "access-service-server-name")

    public VoidResponse setAccessServiceServerName(@PathVariable String userId,
                                                   @PathVariable String serverName,
                                                   @RequestParam String accessServiceServerName)
    {
        return adminAPI.setAccessServiceServerName(userId, serverName, accessServiceServerName);
    }


    /**
     * Set up the list of discovery engines that are to run in the discovery service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param discoveryEngines  discoveryEngines for topic.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @PostMapping( path = "set-discovery-engines")

    public VoidResponse setDiscoveryEngines(@PathVariable String       userId,
                                            @PathVariable String       serverName,
                                            @RequestBody  List<String> discoveryEngines)
    {
        return adminAPI.setDiscoveryEngines(userId, serverName, discoveryEngines);
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "")

    VoidResponse deleteService(@PathVariable String userId,
                               @PathVariable String serverName)
    {
        return adminAPI.deleteService(userId, serverName);
    }
}
