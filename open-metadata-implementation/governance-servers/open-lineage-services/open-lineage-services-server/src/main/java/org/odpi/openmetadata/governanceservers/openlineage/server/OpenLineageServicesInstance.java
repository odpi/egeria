/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.handlers.QueryHandler;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.TestGraphGenerator;

public class OpenLineageServicesInstance {
    private String serverName;
    private TestGraphGenerator testGraphGenerator;
    private QueryHandler queryHandler;

    public OpenLineageServicesInstance(TestGraphGenerator testGraphGenerator,
                                       QueryHandler queryHandler,
                                       String serverName) {
        this.testGraphGenerator = testGraphGenerator;
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

    public TestGraphGenerator getTestGraphGenerator() {
        return this.testGraphGenerator;
    }
}
