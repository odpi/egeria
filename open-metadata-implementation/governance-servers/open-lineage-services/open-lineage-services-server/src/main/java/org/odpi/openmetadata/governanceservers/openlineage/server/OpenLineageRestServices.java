/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphQueryingServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageRestServices.class);
    private final OpenLineageInstanceHandler instanceHandler = new OpenLineageInstanceHandler();

    public VoidResponse dumpGraph(String serverName, String userId, String graph) {
        VoidResponse response = new VoidResponse();

        try {
            GraphQueryingServices graphQueryingServices = instanceHandler.queryHandler(serverName);
            graphQueryingServices.dumpGraph(graph);
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
            GraphQueryingServices graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.exportGraph(graph);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }


    public String lineage(String serverName, String userId, String graph, Scope scope, View view, String guid) {
        String response = "";
        try {
            GraphQueryingServices graphServices = instanceHandler.queryHandler(serverName);
            response = graphServices.lineage(graph, scope, view, guid);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
        return response;
    }

}