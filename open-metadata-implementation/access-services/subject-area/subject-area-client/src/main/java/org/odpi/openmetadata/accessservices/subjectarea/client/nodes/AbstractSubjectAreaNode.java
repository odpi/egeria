/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes;

import org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @param <E> inherited from {@link Node} type
 *            Abstract class for Subject Area client node operations
 */
public abstract class AbstractSubjectAreaNode<E extends Node> extends AbstractSubjectArea<E> implements SubjectAreaNodeClient<E> {
    protected AbstractSubjectAreaNode(SubjectAreaRestClient client, String baseUrl) {
        super(client, baseUrl);
    }

    @Override
    public List<Relationship> getRelationships(String userId, String guid, FindRequest findRequest, Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
                                                                                                                                              PropertyServerException,
                                                                                                                                              UserNotAuthorizedException {
        final String urlTemplate = BASE_URL + "/%s/relationships";
        final String methodInfo = getMethodInfo("getRelationships");

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Relationship.class);
        ParameterizedTypeReference<GenericResponse<Relationship>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Relationship> response = client.findRESTCall(userId, guid, methodInfo, urlTemplate, type, findRequest, maximumPageSizeOnRestCall);
        return response.results();
    }

    @Override
    public List<Relationship> getRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException {
        return getRelationships(userId, guid, findRequest, null);
    }

}