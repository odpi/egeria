/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.AbstractSubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.util.List;

@SubjectAreaNodeClient
public class SubjectAreaProjectClient<P extends Project> extends AbstractSubjectAreaNode<P> {
    public SubjectAreaProjectClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "projects");
    }

    public List<Term> getProjectTerms(String userId, String guid) throws PropertyServerException,
                                                                         UserNotAuthorizedException,
                                                                         InvalidParameterException
    {
        final String urlTemplate = BASE_URL + "/%s/terms";
        final String methodInfo = getMethodInfo("getProjectTerms");

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, Term.class);
        ParameterizedTypeReference<GenericResponse<Term>> type = ParameterizedTypeReference.forType(resolvableType.getType());
        GenericResponse<Term> response = client.findRESTCall(userId, guid, methodInfo, urlTemplate, type, EMPTY_FIND_REQUEST);
        return response.getResult();
    }
}