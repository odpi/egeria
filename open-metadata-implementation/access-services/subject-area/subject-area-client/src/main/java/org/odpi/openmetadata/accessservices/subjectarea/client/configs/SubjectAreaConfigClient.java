/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.configs;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import static org.odpi.openmetadata.accessservices.subjectarea.client.AbstractSubjectArea.SUBJECT_AREA_BASE_URL;

public class SubjectAreaConfigClient implements SubjectAreaConfig, ResponseParameterization<Config> {
    private static final String BASE_URL = SUBJECT_AREA_BASE_URL + "configs";
    protected final SubjectAreaRestClient client;
    public SubjectAreaConfigClient(SubjectAreaRestClient client) {
        this.client = client;
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }

    @Override
    public Config getConfig(String userId) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getConfig";

        String urlTemplate = BASE_URL + "/%s";
        GenericResponse<Config> response = client.getByIdRESTCall(userId, "current", methodName, getParameterizedType(), urlTemplate);
        return response.head().get();
    }
}