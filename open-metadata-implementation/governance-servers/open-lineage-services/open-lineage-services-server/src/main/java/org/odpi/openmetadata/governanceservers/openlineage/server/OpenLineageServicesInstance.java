/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.services.GraphServices;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.MockGraphGenerator;

public class OpenLineageServicesInstance {
    private String serverName;
    private MockGraphGenerator mockGraphGenerator;
    private GraphServices graphServices;

    public OpenLineageServicesInstance(MockGraphGenerator mockGraphGenerator,
                                       GraphServices graphServices,
                                       String serverName) {
        this.mockGraphGenerator = mockGraphGenerator;
        this.serverName = serverName;
        this.graphServices = graphServices;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    public GraphServices getGraphServices() {
        return this.graphServices;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }

    public MockGraphGenerator getMockGraphGenerator() {
        return this.mockGraphGenerator;
    }
}
