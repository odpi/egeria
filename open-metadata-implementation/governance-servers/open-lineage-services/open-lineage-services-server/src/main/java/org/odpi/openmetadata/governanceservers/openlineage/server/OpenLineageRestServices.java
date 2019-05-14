/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphBuilder;
import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageOMASAPIResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.PropertyServerException;


public class OpenLineageRestServices {

    private final OpenLineageInstanceHandler  instanceHandler = new OpenLineageInstanceHandler();


    public OpenLineageOMASAPIResponse exportGraph(String serverName, String userId) {
        VoidResponse response = new VoidResponse();

        try {
            GraphBuilder graphBuilder = instanceHandler.graphConstructor(serverName);
            graphBuilder.exportGraph();
        }
        catch (PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }
}