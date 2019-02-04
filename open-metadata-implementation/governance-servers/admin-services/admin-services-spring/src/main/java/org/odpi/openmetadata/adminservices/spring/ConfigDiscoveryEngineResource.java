/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerConfigDiscoveryEngine;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

/**
 * OMAGServerConfigDiscoveryEngine provides the API for configuring a discovery engine in an OMAG
 * server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/discovery-engine")
public class ConfigDiscoveryEngineResource
{
    private OMAGServerConfigDiscoveryEngine adminAPI = new OMAGServerConfigDiscoveryEngine();

    /**
     * Set up the root URL of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceRootURL  URL root for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "access-service-root-url")

    public VoidResponse setAccessServiceRootURL(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @RequestParam String accessServiceRootURL)
    {
        return adminAPI.setAccessServiceRootURL(userId, serverName, accessServiceRootURL);
    }


    /**
     * Set up the server name of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceServerName  server name for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "access-service-server-name")

    public VoidResponse setAccessServiceServerName(@PathVariable String userId,
                                                   @PathVariable String serverName,
                                                   @RequestParam String accessServiceServerName)
    {
        return adminAPI.setAccessServiceServerName(userId, serverName, accessServiceServerName);
    }


    /**
     * Set up the server name of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection for topic.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "inbound-request-connection")

    public VoidResponse setInboundRequestConnection(@PathVariable String     userId,
                                                    @PathVariable String     serverName,
                                                    @RequestBody  Connection connection)
    {
        return adminAPI.setInboundRequestConnection(userId, serverName, connection);
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "")

    VoidResponse deleteService(@PathVariable String userId,
                               @PathVariable String serverName)
    {
        return adminAPI.deleteService(userId, serverName);
    }
}
