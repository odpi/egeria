/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformSecurityServices;
import org.springframework.web.bind.annotation.*;

/**
 * OldPlatformSecurityResource provides the API to configure the security connector that validates
 * platform requests that do not reference an OMAG server.  These requests are used by the
 * team that run the platform as a service.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/platform/security")

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that the OMAG Server Platform is hosting.  " +
                                                   "It is also able to dynamically change the platform metadata security connector.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/platform-services/overview"))

@Deprecated
public class OldPlatformSecurityResource
{
    private final OMAGServerPlatformSecurityServices adminSecurityAPI = new OMAGServerPlatformSecurityServices();


    /**
     * Set up a platform security connector
     *
     * @param userId calling user.
     * @param requestBody requestBody used to create and configure the connector that performs platform security
     * @return void response
     */
    @PostMapping(path = "/connection")

    public VoidResponse setPlatformSecurityConnection(@PathVariable String                      userId,
                                                      @RequestBody  PlatformSecurityRequestBody requestBody)
    {
        return adminSecurityAPI.setPlatformSecurityConnection(userId, requestBody);
    }


    /**
     * Return the connection object for platform security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @param userId calling user
     * @return connection response
     */
    @GetMapping(path = "/connection")

    public ConnectionResponse getPlatformSecurityConnection(@PathVariable String       userId)
    {
        return adminSecurityAPI.getPlatformSecurityConnection(userId);
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up.
     *
     * @param userId calling user
     * @return void response
     */
    @DeleteMapping(path = "/connection")

    public  VoidResponse clearPlatformSecurityConnection(@PathVariable String   userId)
    {
        return adminSecurityAPI.clearPlatformSecurityConnection(userId);
    }
}
