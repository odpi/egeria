/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

public class DataPlatformServicesInstance {

    private String                 serverName;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     */
    public DataPlatformServicesInstance(String        serverName)  {
        this.serverName    = serverName;

        DataPlatformServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        DataPlatformServicesInstanceMap.removeInstanceForJVM(serverName);
    }


}
