/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;


import org.odpi.openmetadata.adminservices.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.adminservices.properties.VoidResponse;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

/**
 * OMAGServerAdminStoreResource provides the API to configure the destination that should be used to manage
 * configuration.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/stores")
public class OMAGServerAdminStoreResource
{
    private static OMAGServerAdminStoreServices  adminStoreAPI = new OMAGServerAdminStoreServices();


    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real store.
     * @return void response
     */
    @RequestMapping(method = RequestMethod.POST, path = "/connection")

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
    @RequestMapping(method = RequestMethod.GET, path = "/connection")

    public ConnectionResponse getConfigurationStoreConnection(@PathVariable String       userId)
    {
        return adminStoreAPI.getConfigurationStoreConnection(userId);
    }


    /**
     * Clear the connection object for the configuration store.  Null is returned if the server should
     * use the default store.
     *
     * @param userId calling user
     * @return connection response
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/connection")

    public  VoidResponse clearConfigurationStoreConnection(@PathVariable String   userId)
    {
        return adminStoreAPI.clearConfigurationStoreConnection(userId);
    }
}
