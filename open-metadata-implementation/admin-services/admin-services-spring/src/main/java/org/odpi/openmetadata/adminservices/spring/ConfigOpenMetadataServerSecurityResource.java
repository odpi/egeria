/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.OMAGServerAdminSecurityServices;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigOpenMetadataServerSecurityResource provides the API to configure the security connector that validates
 * Open Metadata and Governance requests issued to a specific OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/security")

@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigOpenMetadataServerSecurityResource
{
    private final OMAGServerAdminSecurityServices adminSecurityAPI = new OMAGServerAdminSecurityServices();

    /**
     * Override the default server security connector.
     *
     * @param userId calling user.
     * @param serverName server to configure
     * @param connection connection used to create and configure the connector.
     * @return void response
     */
    @PostMapping(path = "/connection")

    public synchronized VoidResponse setServerSecurityConnection(@PathVariable String       userId,
                                                                 @PathVariable String       serverName,
                                                                 @RequestBody  Connection   connection)
    {
          return adminSecurityAPI.setServerSecurityConnection(userId, serverName, connection);
    }


    /**
     * Return the connection object for the server security connector.
     *
     * @param userId calling user
     * @param serverName server to retrieve configuration from
     * @return connection response
     */
    @GetMapping(path = "/connection")

    public synchronized ConnectionResponse getServerSecurityConnection(@PathVariable  String       userId,
                                                                       @PathVariable  String       serverName)
    {
        return adminSecurityAPI.getServerSecurityConnection(userId, serverName);
    }


    /**
     * Clear the connection object for the server security connector.
     * This sets the server security back to default.
     *
     * @param userId calling user
     * @param serverName server to configure
     * @return connection response
     */
    @DeleteMapping(path = "/connection")

    public synchronized VoidResponse clearServerSecurityConnection(@PathVariable  String   userId,
                                                                   @PathVariable  String   serverName)
    {
        return adminSecurityAPI.clearServerSecurityConnection(userId, serverName);
    }
}
