/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerOriginServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerOriginResource provides the Spring wrapper for the origin service that helps the client
 * discover the type of the server.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/admin-services/users/{userId}")
public class OMAGServerOriginResource
{
    OMAGServerOriginServices   originAPI = new OMAGServerOriginServices();

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server that the request is for
     * @return String description
     */
    @RequestMapping(method = RequestMethod.GET, path = "/server-origin")
    public String getServerOrigin(@PathVariable String   userId,
                                  @PathVariable String   serverName)
    {
        return originAPI.getServerOrigin(userId, serverName);
    }

}
