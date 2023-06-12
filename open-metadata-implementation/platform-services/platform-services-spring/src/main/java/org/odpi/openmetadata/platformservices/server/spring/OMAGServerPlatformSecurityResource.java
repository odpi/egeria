/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformSecurityServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OMAGServerPlatformSecurityResource provides the API to configure the security connector that validates
 * platform requests that do not reference an OMAG server.  These requests are used by the
 * team that run the platform as a service.
 */
@RestController
@RequestMapping("/open-metadata/platform-services/users/{userId}/server-platform/security")

@Tag(name="Platform Services", description="The platform services provides the APIs for querying the Open Metadata and Governance (OMAG) " +
                                                   "Server Platform. It is able to start an stop OMAG Servers and discovering information " +
                                                   "about the OMAG Servers that it is hosting.",
     externalDocs=@ExternalDocumentation(description="Platform Services",url="https://egeria-project.org/services/platform-services/overview"))

public class OMAGServerPlatformSecurityResource
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

    @Operation(summary="setPlatformSecurityConnection",
               description="Set up a platform security connector to control access to the platform and admin services.",
               externalDocs=@ExternalDocumentation(description="Metadata Security",
                                                   url="https://egeria-project.org/features/metadata-security/overview/"))

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

    @Operation(summary="getPlatformSecurityConnection",
               description="Return the connection object for platform security connector.  Null is returned if no platform security has been set up.",
               externalDocs=@ExternalDocumentation(description="Metadata Security",
                                                   url="https://egeria-project.org/features/metadata-security/overview/"))


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

    @Operation(summary="clearPlatformSecurityConnection",
               description="Clear the connection object for platform security.  This means there is no platform security set up. Note that this command must be issued by a user that has permission to operate the OMAG Server Platform.",
               externalDocs=@ExternalDocumentation(description="Metadata Security",
                                                   url="https://egeria-project.org/features/metadata-security/overview/"))

    public  VoidResponse clearPlatformSecurityConnection(@PathVariable String   userId)
    {
        return adminSecurityAPI.clearPlatformSecurityConnection(userId);
    }
}
