/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices.spring;

import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.userinterfaces.adminservices.UIServerAdminStoreServices;
import org.springframework.web.bind.annotation.*;


/**
 * UIConfigStoreResource provides the API to configure the destination that should be used to manage
 * configuration documents.  The default is to use a file for each configured OMAG server.
 */
@RestController
@RequestMapping("/open-metadata/ui-admin-services/users/{userId}/stores")
public class UIConfigStoreResource
{
    private static UIServerAdminStoreServices adminStoreAPI = new UIServerAdminStoreServices();


    /**
     * Override the default location of the configuration documents.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector that interacts with
     *                   the real configStore.
     * @return void response
     */
    @PostMapping( path = "/connection")

    public VoidResponse setConfigurationStoreConnection(@PathVariable String     userId,
                                                        @RequestBody  Connection connection)
    {
        return adminStoreAPI.setConfigurationStoreConnection(userId, connection);
    }

//TODO uncomment code and test to be able to get the connection object for the configStore
//    /**
//     * Return the connection object for the configuration configStore.  Null is returned if the server should
//     * use the default configStore.
//     *
//     * @param userId calling user
//     * @return connection response
//     */
//    @GetMapping( path = "/connection")
//
//    public ConnectionResponse getConfigurationStoreConnection(@PathVariable String       userId)
//    {
//        return adminStoreAPI.getConfigurationStoreConnection(userId);
//    }


    /**
     * Clear the connection object for the configuration configStore which means the platform uses the default configStore.
     *
     * @param userId calling user
     * @return void response
     */
    @DeleteMapping( path = "/connection")

    public  VoidResponse clearConfigurationStoreConnection(@PathVariable String   userId)
    {
        return adminStoreAPI.clearConfigurationStoreConnection(userId);
    }
}
