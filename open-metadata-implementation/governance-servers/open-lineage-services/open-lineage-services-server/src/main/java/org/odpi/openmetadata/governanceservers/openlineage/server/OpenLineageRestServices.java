/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();

    public VoidResponse dumpGraph(String serverName, String userId, String graph) {
        VoidResponse response = new VoidResponse();

        return response;
    }

    public String exportGraph(String serverName, String userId, String graph) {
        String response = "";
        return response;
    }


    public String lineage(String serverName, String userId, String graph, Scope scope, View view, String guid) {
        String response = "";
        return response;
    }

    public VoidResponse generateGraph(String serverName, String userId) {
        VoidResponse response = new VoidResponse();

        return response;
    }
}
