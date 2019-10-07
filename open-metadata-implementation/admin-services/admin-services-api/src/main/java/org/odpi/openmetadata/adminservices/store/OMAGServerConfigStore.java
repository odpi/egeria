/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.io.Serializable;

/**
 * OMAGServerConfigStore provides the interface to the configuration for an OMAG Server.  This is accessed
 * through a connector.
 */
public interface OMAGServerConfigStore
{
    /**
     * Save the server configuration.
     *
     * @param configuration configuration properties to save
     */
    <T> void saveServerConfig(T configuration);


    /**
     * Retrieve the configuration saved from a previous run of the OMAG Server Platform.
     * (Note: this exists primarily for backwards compatibility -- wherever possible, use the parameterized version
     * of this method.)
     *
     * @return server configuration
     * @see #retrieveServerConfig(Class)
     */
    OMAGServerConfig retrieveServerConfig();


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @param <T> class name
     * @return server configuration
     */
    <T> T  retrieveServerConfig( Class<T> clazz);


    /**
     * Remove the server configuration.
     */
    void removeServerConfig();
}
