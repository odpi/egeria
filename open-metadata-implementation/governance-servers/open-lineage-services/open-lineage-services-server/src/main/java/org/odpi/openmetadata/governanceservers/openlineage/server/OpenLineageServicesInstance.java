/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;

import org.odpi.openmetadata.governanceservers.openlineage.services.GraphQueryingServices;

public class OpenLineageServicesInstance {
    private String serverName;
    private GraphQueryingServices graphQueryingServices;

    public OpenLineageServicesInstance(GraphQueryingServices graphQueryingServices,
                                       String serverName) {
        this.serverName = serverName;
        this.graphQueryingServices = graphQueryingServices;

        OpenLineageServicesInstanceMap.setNewInstanceForJVM(serverName, this);
    }

    public GraphQueryingServices getGraphQueryingServices() {
        return this.graphQueryingServices;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        OpenLineageServicesInstanceMap.removeInstanceForJVM(serverName);
    }

}
