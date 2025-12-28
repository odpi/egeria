/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Set;

/**
 * OMAGServerConfigStore provides the interface to the configuration for an OMAG Server.  This is accessed
 * through a connector.
 */
public interface OMAGServerConfigStore
{
    /**
     * Set up the name of the server for this configuration document.
     *
     * @param serverName name of the server
     */
    void setServerName(String  serverName);


    /**
     * Save the server configuration.
     *
     * @param configuration configuration properties to save
     */
    void saveServerConfig(OMAGServerConfig   configuration);


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @return server configuration
     */
    OMAGServerConfig  retrieveServerConfig();


    /**
     * Remove the server configuration.
     */
    void removeServerConfig();

    /**
     * Retrieve all the stored server configurations
     *
     * @return the set of server configurations present in this OMAG Server Config store
     */
    Set<OMAGServerConfig> retrieveAllServerConfigs();
}
