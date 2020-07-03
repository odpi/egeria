/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities;

import org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaEntityClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

public abstract class AbstractSubjectAreaEntity<E extends Node> extends AbstractSubjectArea<E> implements SubjectAreaEntityClient<E> {
    protected AbstractSubjectAreaEntity(SubjectAreaRestClient client, String baseUrl) {
        super(client, baseUrl);
    }

    @Override
    public List<Line> getRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String urlTemplate = BASE_URL + "/%s/relationships";
        final String methodInfo = getMethodInfo("getRelationships");

        ParameterizedTypeReference<SubjectAreaOMASAPIResponse<Line>> type =
                new ParameterizedTypeReference<SubjectAreaOMASAPIResponse<Line>>() {};

        SubjectAreaOMASAPIResponse<Line> response = client.findRESTCall(userId, guid, methodInfo,urlTemplate, type, findRequest);
        return response.getResult();
    }
}