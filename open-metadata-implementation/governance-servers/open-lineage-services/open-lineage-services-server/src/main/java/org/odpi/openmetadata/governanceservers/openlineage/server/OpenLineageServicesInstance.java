/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphBuilder;

public class OpenLineageServicesInstance
{
    private String serverName;
    private GraphBuilder graphBuilder;

    public OpenLineageServicesInstance(GraphBuilder graphBuilder,
                                       String serverName)  {
        this.graphBuilder = graphBuilder;
        this.serverName    = serverName;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    public GraphBuilder getGraphBuilder()  {
        return this.graphBuilder;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
