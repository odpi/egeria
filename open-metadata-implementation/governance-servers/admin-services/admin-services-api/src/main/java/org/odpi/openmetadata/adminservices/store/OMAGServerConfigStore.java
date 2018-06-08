/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

/**
 * OMAGServerConfigStore provides the interface to the configuration for an OMAG Server.  This is accessed
 * through a connector.
 */
public interface OMAGServerConfigStore
{
    /**
     * Save the server configuration.
     *
     * @param configuration - configuration properties to save
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
}
