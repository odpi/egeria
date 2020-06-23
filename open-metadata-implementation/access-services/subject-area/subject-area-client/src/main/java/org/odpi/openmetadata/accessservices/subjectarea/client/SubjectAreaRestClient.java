/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;

public class SubjectAreaRestClient extends FFDCRESTClient {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRestClient.class);

    private final String serverName;
    private final String serverPlatformURLRoot;

    public SubjectAreaRestClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    public SubjectAreaRestClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    public <T> SubjectAreaOMASAPIResponse<T> postRESTCall(String userId,
                                                          String methodName,
                                                          String urlTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          T requestBody) throws PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId);
        SubjectAreaOMASAPIResponse<T> response = callPostRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    public <T> SubjectAreaOMASAPIResponse<T> putRESTCall(String userId,
                                                         String guid,
                                                         String methodName,
                                                         String urlTemplate,
                                                         ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                         T requestBody) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callPutRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    public <T> SubjectAreaOMASAPIResponse<T> getByIdRESTCall(String userId,
                                                             String guid,
                                                             String methodName,
                                                             ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                             String urlTemplate) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    public <T> SubjectAreaOMASAPIResponse<T> findRESTCall(String userId,
                                                          String methodName,
                                                          String urlTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          FindRequest findRequest) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String findUrlTemplate = urlTemplate + createFindQuery(methodName, findRequest).toString();
        String expandedURL = String.format(serverPlatformURLRoot + findUrlTemplate, serverName, userId);
        SubjectAreaOMASAPIResponse<T> response = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    public <T> SubjectAreaOMASAPIResponse<T> findRESTCall(String userId,
                                                          String guid,
                                                          String methodName,
                                                          String urlTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          FindRequest findRequest) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        String findUrlTemplate = urlTemplate + createFindQuery(methodName, findRequest).toString();
        return getByIdRESTCall(userId, guid, methodName, type, findUrlTemplate);
    }

    public <T> SubjectAreaOMASAPIResponse<T> deleteRESTCall(String userId,
                                                            String guid,
                                                            String methodName,
                                                            ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                            String urlTemplate) throws PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callDeleteRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        return response;
    }

    public <T> SubjectAreaOMASAPIResponse<T> restoreRESTCall(String userId,
                                                             String guid,
                                                             String methodName,
                                                             ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                             String urlTemplate) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callPostRESTCall(methodName, type, expandedURL, null);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    public QueryBuilder createFindQuery(String methodName, FindRequest findRequest) throws InvalidParameterException {
        String searchCriteria = findRequest.getSearchCriteria();
        if (searchCriteria != null) {
            searchCriteria = QueryUtils.encodeParams(methodName, "searchCriteria", searchCriteria);
        }
        String property = findRequest.getSequencingProperty();
        if (property != null) {
            property = QueryUtils.encodeParams(methodName, "sequencingProperty", property);
        }

        SequencingOrder sequencingOrder = findRequest.getSequencingOrder();
        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }

        return new QueryBuilder()
                .addParam("sequencingOrder", sequencingOrder.name())
                .addParam("asOfTime", findRequest.getAsOfTime())
                .addParam("searchCriteria", searchCriteria)
                .addParam("offset", findRequest.getOffset())
                .addParam("pageSize", findRequest.getPageSize())
                .addParam("sequencingProperty", property);
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerPlatformURLRoot() {
        return serverPlatformURLRoot;
    }
}