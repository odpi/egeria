/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;

import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformOriginServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * ServerPlatformOriginResource provides the Spring wrapper for the origin service that helps the client
 * discover the type of the server platform.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}")
public class ServerPlatformOriginResource
{
    OMAGServerPlatformOriginServices originAPI = new OMAGServerPlatformOriginServices();

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    @Deprecated
    @GetMapping( path = "/server-platform-origin")
    public String getOldServerOrigin(@PathVariable String   userId)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }


    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    @GetMapping( path = "/server-platform/origin")
    public String getServerOrigin(@PathVariable String   userId)
    {
        return originAPI.getServerPlatformOrigin(userId);
    }

}
