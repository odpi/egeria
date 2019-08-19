/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.services.GraphServices;
import org.odpi.openmetadata.governanceservers.openlineage.mockdata.MockGraphGenerator;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();

    public VoidResponse dumpGraph(String serverName, String userId, String graph) {
        VoidResponse response = new VoidResponse();

        try {
            GraphServices graphServices = instanceHandler.queryHandler(serverName);
            graphServices.dumpGraph(graph);
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

    public String exportGraph(String serverName, String userId, String graph) {
        String response = "";
        try {
            GraphServices graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.exportGraph(graph);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }


    public String queryLineage(String serverName, String userId, String scope, String lineageQuery, String graph, String guid) {
        String response = "";
        try {
            GraphServices graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.queryLineage(scope, lineageQuery, graph, guid);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public VoidResponse generateGraph(String serverName, String userId) {
        VoidResponse response = new VoidResponse();

        try {
            MockGraphGenerator mockGraphGenerator = instanceHandler.testGraphGenerator(serverName);
            mockGraphGenerator.generate();
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }
}
