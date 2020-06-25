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

/**
 * RESTClient is responsible for issuing calls to the Subject Area OMAS REST APIs.
 *
 * In this class with methods there is a parameter "urnTemplate" below is an example for a better understanding.
 * For example "/servers/%s/open-metadata/access-services/subject-area/users/%s/categories/%s"
 * where first %s - serverName, second %s - userId and third %s - guid
 * This URN will be a continuation of serverPlatformURLRoot, which was set when creating the object
 */
public class SubjectAreaRestClient extends FFDCRESTClient {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRestClient.class);

    private final String serverName;
    private final String serverPlatformURLRoot;

    /**
     * {@inheritDoc}
     */
    public SubjectAreaRestClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    /**
     * {@inheritDoc}
     */
    public SubjectAreaRestClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return SubjectAreaOMASAPIResponse with <T> result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> postRESTCall(String userId,
                                                          String methodName,
                                                          String urnTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          T requestBody) throws PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);
        SubjectAreaOMASAPIResponse<T> response = callPostRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the update object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return SubjectAreaOMASAPIResponse with <T> result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> putRESTCall(String userId,
                                                         String guid,
                                                         String methodName,
                                                         String urnTemplate,
                                                         ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                         T requestBody) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callPutRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the received object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.

     * @return SubjectAreaOMASAPIResponse with <T> result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> getByIdRESTCall(String userId,
                                                             String guid,
                                                             String methodName,
                                                             ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                             String urnTemplate) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Issue a GET REST call that returns a response found objects using the information described in findRequest
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param findRequest {@link FindRequest}
     *
     * @return SubjectAreaOMASAPIResponse with <T> results
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> findRESTCall(String userId,
                                                          String methodName,
                                                          String urnTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          FindRequest findRequest) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String findUrlTemplate = urnTemplate + createFindQuery(methodName, findRequest).toString();
        String expandedURL = String.format(serverPlatformURLRoot + findUrlTemplate, serverName, userId);
        SubjectAreaOMASAPIResponse<T> response = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Issue a GET REST call that returns a response found objects using the information described in findRequest
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object to which the found objects should relate
     * @param methodName  name of the method being called.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param findRequest {@link FindRequest}
     *
     * @return SubjectAreaOMASAPIResponse with <T> results
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> findRESTCall(String userId,
                                                          String guid,
                                                          String methodName,
                                                          String urnTemplate,
                                                          ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                          FindRequest findRequest) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        String findUrlTemplate = urnTemplate + createFindQuery(methodName, findRequest).toString();
        return getByIdRESTCall(userId, guid, methodName, type, findUrlTemplate);
    }

    /**
     * Issue a Delete REST call
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the delete object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     *
     * @return SubjectAreaOMASAPIResponse with <T> result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> deleteRESTCall(String userId,
                                                            String guid,
                                                            String methodName,
                                                            ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                            String urnTemplate) throws PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callDeleteRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        return response;
    }

    /**
     * Issue a restore object
     *
     * @param <T> return type for results in {@link SubjectAreaOMASAPIResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the restore object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link Parametrization#getType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     *
     * @return SubjectAreaOMASAPIResponse with <T> result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> SubjectAreaOMASAPIResponse<T> restoreRESTCall(String userId,
                                                             String guid,
                                                             String methodName,
                                                             ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> type,
                                                             String urnTemplate) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse<T> response = callPostRESTCall(methodName, type, expandedURL, null);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Method for constructing a query (https://en.wikipedia.org/wiki/Query_string) using the information described in the findRequest
     * @param methodName  name of the method being called.
     * @param findRequest {@link FindRequest}
     *
     * @return query
     * */
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