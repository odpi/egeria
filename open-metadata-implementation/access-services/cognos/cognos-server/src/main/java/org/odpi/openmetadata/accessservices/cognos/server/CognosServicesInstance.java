/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;


import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;

/**
 * CognosServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CognosServicesInstance
{
    private DatabaseContextHandler databaseContextHandler;
    private String serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     * @param databaseContextHandler to process database content.
     * @param serverName server identifier
     */
    public CognosServicesInstance( DatabaseContextHandler databaseContextHandler, String serverName) {
        this.databaseContextHandler = databaseContextHandler;
        this.serverName    = serverName;

        CognosServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    public DatabaseContextHandler getContextBuilder() {
        return databaseContextHandler;
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        CognosServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
