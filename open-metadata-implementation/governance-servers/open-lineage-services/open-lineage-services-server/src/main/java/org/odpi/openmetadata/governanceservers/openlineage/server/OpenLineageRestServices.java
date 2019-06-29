/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.handlers.QueryHandler;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.MockGraphGenerator;
import org.odpi.openmetadata.governanceservers.openlineage.responses.GraphsonResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageAPIResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;


public class OpenLineageRestServices {

    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();

    public OpenLineageAPIResponse dumpGraph(String serverName, String userId, String graph) {
        VoidResponse response = new VoidResponse();

        try {
            QueryHandler queryHandler = instanceHandler.queryHandler(serverName);
            queryHandler.dumpGraph(graph);
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

    public OpenLineageAPIResponse exportGraph(String serverName, String userId, String graph) {
        GraphsonResponse response = new GraphsonResponse();
        try {
            QueryHandler queryHandler = instanceHandler.queryHandler(serverName);
            String graphson = queryHandler.exportGraph(graph);
            response.setResponse(graphson);
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }


    public OpenLineageAPIResponse initialGraph(String serverName, String userId, String lineageType, String guid) {
        GraphsonResponse response = new GraphsonResponse();
        try {
            QueryHandler queryHandler = instanceHandler.queryHandler(serverName);
            String graphson = queryHandler.getInitialGraph(lineageType, guid);
            response.setResponse(graphson);
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }
        return response;
    }

    public OpenLineageAPIResponse generateGraph(String serverName, String userId) {
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
