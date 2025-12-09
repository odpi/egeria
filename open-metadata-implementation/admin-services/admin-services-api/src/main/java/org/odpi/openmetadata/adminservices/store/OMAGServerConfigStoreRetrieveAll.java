/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Set;

/**
 * OMAGServerConfigStoreQueryAll provides a method to retrieve all the stored servers. It is an extension to the OMAGServerConfigStore interface
 * to the configuration for an OMAG Server. This is accessed through a connector.
 *
 * This function has been added to a new interface so existing implementations using OMAGServerConfigStore continue to work.
 */
public interface OMAGServerConfigStoreRetrieveAll extends OMAGServerConfigStore
{
    /**
     * Retrieve all the stored server configurations
     *
     * @return the set of server configurations present in this OMAG Server Config store
     */
    Set<OMAGServerConfig> retrieveAllServerConfigs();

}
