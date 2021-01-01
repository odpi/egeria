/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public abstract class AbstractSubjectArea<T> implements SubjectAreaClient<T>, ResponseParameterization<T> {
    public static final String SUBJECT_AREA_BASE_URL = "/servers/%s/open-metadata/access-services/subject-area/users/%s/";
    public static final String BASE_RELATIONSHIPS_URL = SUBJECT_AREA_BASE_URL + "relationships";

    protected final String BASE_URL;
    protected final SubjectAreaRestClient client;

    protected AbstractSubjectArea(SubjectAreaRestClient client, String baseUrl) {
        this.BASE_URL = baseUrl;
        this.client = client;
    }

    @Override
    public T getByGUID(String userId, String guid) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String urlTemplate = BASE_URL + "/%s";
        GenericResponse<T> response = client.getByIdRESTCall(userId, guid, getMethodInfo("getByGUID"), getParameterizedType(), urlTemplate);
        return response.head().get();
    }

    @Override
    public T create(String userId, T supplied) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        GenericResponse<T> response = client.postRESTCall(userId, getMethodInfo("create"), BASE_URL, getParameterizedType(), supplied);
        return response.head().get();
    }

    @Override
    public List<T> find(String userId, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        GenericResponse<T> response = client.findRESTCall(userId, getMethodInfo("find"), BASE_URL, getParameterizedType(), findRequest);
        return response.results();
    }
    @Override
    public List<T> find(String userId, FindRequest findRequest , Integer maximumPageSizeOnRestCall) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        GenericResponse<T> response = client.findRESTCall(userId, getMethodInfo("find"), BASE_URL, getParameterizedType(), findRequest, maximumPageSizeOnRestCall);
        return response.results();
    }

    @Override
    public T update(String userId, String guid, T supplied, boolean isReplace) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String urlTemplate = BASE_URL + "/%s?isReplace=" + Boolean.toString(isReplace);
        String methodInfo = getMethodInfo("update(isReplace=" + isReplace + ")");
        GenericResponse<T> response = client.putRESTCall(userId, guid, methodInfo, urlTemplate, getParameterizedType(), supplied);
        return response.head().get();
    }

    @Override
    public void delete(String userId, String guid, boolean isPurge) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String urlTemplate = BASE_URL + "/%s?isPurge=" + Boolean.toString(isPurge);
        client.deleteRESTCall(userId, guid, getMethodInfo("delete(isPurge=" + isPurge + ")"), getParameterizedType(), urlTemplate);
    }

    @Override
    public T restore(String userId, String guid) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        final String urlTemplate = BASE_URL + "/%s";
        GenericResponse<T> response = client.restoreRESTCall(userId, guid, getMethodInfo("restore"), getParameterizedType(), urlTemplate);
        return response.head().get();
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }

    protected String getMethodInfo(String methodName) {
        return methodName + " for " + resultType().getSimpleName();
    }

}