/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphBuilder;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.GraphTester;

public class OpenLineageServicesInstance {
    private String serverName;
    private GraphBuilder graphBuilder;
    private GraphTester graphTester;

    public OpenLineageServicesInstance(GraphBuilder graphBuilder,
                                       GraphTester graphTester,
                                       String serverName) {
        this.graphBuilder = graphBuilder;
        this.graphTester = graphTester;
        this.serverName = serverName;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }


    public GraphBuilder getGraphBuilder() {
        return this.graphBuilder;
    }

    public GraphTester getGraphTester() {
        return this.graphTester;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
