/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;





/**
 * AssetLineageServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetLineageServicesInstance
{
    private String                 serverName;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     */
    public AssetLineageServicesInstance(String        serverName)  {
        this.serverName    = serverName;

        AssetLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        AssetLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
