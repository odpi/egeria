/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.QueryHandler;
import org.odpi.openmetadata.governanceservers.openlineage.performanceTesting.MockGraphGenerator;
import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageAPIResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {


    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
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

    public String exportGraph(String serverName, String userId, String graph) {
        String response = "";
        try {
            QueryHandler queryHandler = instanceHandler.queryHandler(serverName);
            response = queryHandler.exportGraph(graph);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }


    public String initialGraph(String serverName, String userId, String lineageType, String guid) {
        String response = "";
        try {
            QueryHandler queryHandler = instanceHandler.queryHandler(serverName);
            response = queryHandler.getInitialGraph(lineageType, guid);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
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
