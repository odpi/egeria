/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphConstructor;

public class OpenLineageServicesInstance
{
    private String serverName;
    private GraphConstructor graphConstructor;

    public OpenLineageServicesInstance(GraphConstructor graphConstructor,
                                       String serverName)  {
        this.graphConstructor = graphConstructor;
        this.serverName    = serverName;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    public GraphConstructor getGraphConstructor()  {
        return this.graphConstructor;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
