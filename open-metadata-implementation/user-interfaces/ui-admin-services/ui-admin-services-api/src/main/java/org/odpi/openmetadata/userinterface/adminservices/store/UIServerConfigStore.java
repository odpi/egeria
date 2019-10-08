/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.store;


import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;

/**
 * UIServerConfigStore provides the interface to the configuration for a UI Server.  This is accessed
 * through a connector.
 */
public interface UIServerConfigStore
{
    /**
     * Save the server configuration.
     *
     * @param configuration configuration properties to save
     */
    void saveServerConfig(UIServerConfig configuration);


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @return server configuration
     */
    UIServerConfig  retrieveServerConfig();


    /**
     * Remove the server configuration.
     */
    void removeServerConfig();
}
