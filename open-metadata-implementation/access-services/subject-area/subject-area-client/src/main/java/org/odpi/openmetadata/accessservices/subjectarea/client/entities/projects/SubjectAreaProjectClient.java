/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.AbstractSubjectAreaEntity;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@SubjectAreaNodeClient
public class SubjectAreaProjectClient extends AbstractSubjectAreaEntity<Project> {
    public SubjectAreaProjectClient(SubjectAreaRestClient client)
    {
        super(client, SUBJECT_AREA_BASE_URL + "projects");
    }

    @Override
    public Class<Project> type() {
        return Project.class;
    }

    public List<Term> getProjectTerms(String userId, String guid) throws PropertyServerException,
                                                                         UserNotAuthorizedException,
                                                                         InvalidParameterException
    {
        final String urlTemplate = BASE_URL + "/%s/terms";
        final String methodInfo = getMethodInfo("getProjectTerms");

        ParameterizedTypeReference<SubjectAreaOMASAPIResponse<Term>> type =
                new ParameterizedTypeReference<SubjectAreaOMASAPIResponse<Term>>() {};

        SubjectAreaOMASAPIResponse<Term> response = client.findRESTCall(userId, methodInfo, urlTemplate, type, EMPTY_FIND_REQUEST);
        return response.getResult();
    }
}