/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.handlers.QueryHandler;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.MockGraphGenerator;

public class OpenLineageServicesInstance {
    private String serverName;
    private MockGraphGenerator mockGraphGenerator;
    private QueryHandler queryHandler;

    public OpenLineageServicesInstance(MockGraphGenerator mockGraphGenerator,
                                       QueryHandler queryHandler,
                                       String serverName) {
        this.mockGraphGenerator = mockGraphGenerator;
        this.serverName = serverName;
        this.queryHandler = queryHandler;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    public QueryHandler getQueryHandler() {
        return this.queryHandler;
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
