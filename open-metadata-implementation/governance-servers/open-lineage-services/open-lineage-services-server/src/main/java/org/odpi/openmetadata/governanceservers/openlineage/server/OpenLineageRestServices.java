/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.handlers.OpenLineageHandler;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    public VoidResponse dumpGraph(String serverName, String userId, String openLineageGUID, String graph) {
        VoidResponse response = new VoidResponse();
        final String methodName = "dumpGraph";

        try {
            OpenLineageHandler openLineageHandler = instanceHandler.queryHandler(userId,
                    serverName,
                    openLineageGUID,
                    methodName);
            openLineageHandler.dumpGraph(graph);
        } catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

    public String exportGraph(String serverName, String userId, String graph) {
        String response = "";
        try {
            OpenLineageHandler graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.exportGraph(graph);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public LineageResponse lineage(String serverName, String userId, String graph, Scope scope, View view, String guid) {
        LineageResponse response = null;
        try {
            OpenLineageHandler graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.lineage(graph, scope, view, guid);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }

}