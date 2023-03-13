/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminSecurityServices;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigOpenMetadataPlatformSecurityResource provides the API to configure the security connector that validates
 * platform requests that do not reference an OMAG server.  These requests are used by the
 * team that run the platform as a service.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/platform/security")

@Tag(name="Administration Services - Platform Configuration", description="The platform configuration administration services support the " +
        "configuration of the security and configuration store connectors for an OMAG Server Platform.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/configuring-the-omag-server-platform/"))

public class ConfigOpenMetadataPlatformSecurityResource
{
    private final OMAGServerAdminSecurityServices adminSecurityAPI = new OMAGServerAdminSecurityServices();


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
