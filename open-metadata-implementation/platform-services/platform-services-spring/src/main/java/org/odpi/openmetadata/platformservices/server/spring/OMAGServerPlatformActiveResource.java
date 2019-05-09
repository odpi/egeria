/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.platformservices.rest.ServerServicesListResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformActiveServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * OMAGServerPlatformActiveServices allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}")
public class OMAGServerPlatformActiveResource
{
    OMAGServerPlatformActiveServices  platformAPI = new OMAGServerPlatformActiveServices();

    /**
     * Return the list of OMAG Servers that are active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @return list of server names
     */
    @RequestMapping(method = RequestMethod.GET, path = "/server-platform/servers")

    public ServerListResponse getActiveServerList(@PathVariable String    userId)
    {
        return platformAPI.getActiveServerList(userId);
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return server name and list od services running within
     */
    @RequestMapping(method = RequestMethod.GET, path = "/server-platform/servers/{serverName}/services")

    public ServerServicesListResponse getActiveServiceListForServer(@PathVariable String    userId,
                                                                    @PathVariable String    serverName)
    {
        return platformAPI.getActiveServiceListForServer(userId, serverName);
    }
}
