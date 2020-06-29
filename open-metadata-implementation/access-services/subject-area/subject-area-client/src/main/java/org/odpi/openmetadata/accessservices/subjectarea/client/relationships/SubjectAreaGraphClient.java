/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.client.Parametrization;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.NeighborhoodHistoricalFindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea.SUBJECT_AREA_BASE_URL;

public class SubjectAreaGraphClient implements SubjectAreaGraph, Parametrization<Graph> {
    private static final String BASE_URL = SUBJECT_AREA_BASE_URL + "nodes";
    protected final SubjectAreaRestClient client;
    public SubjectAreaGraphClient(SubjectAreaRestClient client) {
        this.client = client;
    }

    @Override
    public Class<Graph> type() {
        return Graph.class;
    }

    @Override
    public Graph getGraph(String userId, String guid, NeighborhoodHistoricalFindRequest request) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getGraph";;

        String urlTemplate = BASE_URL + "/%s" + createGraphQuery(request).toString();
        SubjectAreaOMASAPIResponse<Graph> response = client.getByIdRESTCall(userId, guid, methodName, getParametrizedType(), urlTemplate);
        return response.getHead();
    }

    public QueryBuilder createGraphQuery(NeighborhoodHistoricalFindRequest request) {
        QueryBuilder queryBuilder = new QueryBuilder();
        String nodeFilter = request.getNodeFilter().stream().map(NodeType::name).collect(Collectors.joining(","));
        String lineFilter = request.getLineFilter().stream().map(LineType::name).collect(Collectors.joining(","));

        if (!nodeFilter.isEmpty())
            queryBuilder.addParam("nodeFilter", nodeFilter);

        if(!lineFilter.isEmpty())
            queryBuilder.addParam("lineFilter", lineFilter);

        return queryBuilder
                .addParam("asOfTime", request.getAsOfTime())
                .addParam("statusFilter", request.getStatusFilter().name())
                .addParam("level", request.getLevel());
    }
}
