/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigStoreResource provides the API to configure the connector that should be used to manage
 * configuration documents.  The default is to use a file for each configured OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/stores")

@Tag(name="Administration Services - Platform Configuration", description="The platform configuration administration services support the " +
        "configuration of the security and configuration store connectors for an OMAG Server Platform.",
        externalDocs=@ExternalDocumentation(description="Further information",
        url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigStoreResource
{
    private final OMAGServerAdminStoreServices  adminStoreAPI = new OMAGServerAdminStoreServices();


    /**
     * Override the default implementation or configuration of the configuration document store.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    @PostMapping(path = "/connection")

    public VoidResponse setConfigurationStoreConnection(@PathVariable String     userId,
                                                        @RequestBody  Connection connection)
    {
        return adminStoreAPI.setConfigurationStoreConnection(userId, connection);
    }


    /**
     * Return the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     */
    @GetMapping(path = "/connection")

    public ConnectionResponse getConfigurationStoreConnection(@PathVariable String       userId)
    {
        return adminStoreAPI.getConfigurationStoreConnection(userId);
    }


    /**
     * Clear the connection object for the configuration store which means the platform uses the default store.
     *
     * @param userId calling user
     * @return void response
     */
    @DeleteMapping(path = "/connection")

    public  VoidResponse clearConfigurationStoreConnection(@PathVariable String   userId)
    {
        return adminStoreAPI.clearConfigurationStoreConnection(userId);
    }
}
