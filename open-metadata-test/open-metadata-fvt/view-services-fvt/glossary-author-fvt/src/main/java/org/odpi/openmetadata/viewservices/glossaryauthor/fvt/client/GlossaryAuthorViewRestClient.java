/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryParams;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map;

public class GlossaryAuthorViewRestClient extends FFDCRESTClient {
    private static final Logger log = LoggerFactory.getLogger(GlossaryAuthorViewRestClient.class);
    protected static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final String serverName;
    private final String serverPlatformURLRoot;

    public GlossaryAuthorViewRestClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object
     * @param methodName  name of the method being called.
     * @param type        class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;
        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
//        System.out.println(" expandedURL  " + expandedURL);
        completeResponse = callGetRESTCall(methodName, type, expandedURL);//,null);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the received object
     * @param methodName  name of the method being called.
     * @param type        class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    //get by GUID
    public <T> GenericResponse<T> getByGUIdRESTCall(String userId,
                                                    String guid,
                                                    String methodName,
                                                    ParameterizedTypeReference<GenericResponse<T>> type,
                                                    String urnTemplate) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);

//        System.out.println(" ExpandedURL " + expandedURL);

        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

    /**
     * Issue a POST REST call that returns a response object.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param type        class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param guid        unique identifier of the  object
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    public <T> GenericResponse<T> postRESTCall(String userId,
                                               String methodName,
                                               String urlTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               String guid)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        completeResponse = callPostRESTCall(methodName,
                type,
                expandedURL,
                null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

    /**
     * Issue a POST REST call that returns a response object. The object to be created is passed.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param create      name of the action/method been called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param parameterizedType        class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param t           object to be creates
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    public <T> GenericResponse<T> postRESTCall(String userId,
                                               String create,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                               T t)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + create + ",userId=" + userId);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);

        completeResponse = callPostRESTCall(create,
                parameterizedType,
                expandedURL,
                t);

        exceptionHandler.detectAndThrowStandardExceptions(create, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + create + ",userId=" + userId);
        }

        return completeResponse;
    }

    /**
     * Issue a POST REST call that returns a response array. The array of objects to be created is passed.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param guid        unique identifier of the  glossary object
     * @param type        class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param termArray   Array of terms to be creates
     * @return Array of type SubjectAreaOMASAPIResponse
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */
    public SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>> postRESTCallArr(String userId,
                                                                                        String methodName,
                                                                                        String urlTemplate,
                                                                                        String guid,
                                                                                        ParameterizedTypeReference<SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>>> type,
                                                                                        Term[] termArray)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate , serverName, userId, guid);

//        System.out.println(expandedURL);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId);
        }

        SubjectAreaOMASAPIResponse<SubjectAreaOMASAPIResponse<Term>>  completeResponse = callPostRESTCall(methodName,type,expandedURL, termArray);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

    /**
     * Issue a PUT REST call that returns a response object. The object to be updated is passed.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the  object
     * @param methodName      name of the method being called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param parameterizedType   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param glossary           Glossary to be creates
     * @param params           additional parameters to be passed
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> putRESTCall(String userId,
                                              String guid,
                                              String methodName,
                                              String urnTemplate,
                                              ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                              Glossary glossary,
                                              Object... params)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        //String guid = glossary.getSystemAttributes().getGUID();
        //String urlString = String.format(BASE_URL + "/%s",guid);
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate + "/%s", serverName, userId, guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId);
        }

        return callPutRESTCall(methodName,
                parameterizedType,
                expandedURL,
                glossary,
                params);
    }

    /**
     * Issue a PUT REST call that returns a response object. The object to be updated is passed.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the  object
     * @param methodName      name of the method being called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param parameterizedType   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param category           Category to be creates
     * @param params           additional parameters to be passed
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    public <T> GenericResponse<T> putRESTCall(String userId,
                                              String guid,
                                              String methodName,
                                              String urnTemplate,
                                              ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                              Category category,
                                              Object... params)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        //String guid = glossary.getSystemAttributes().getGUID();
        //String urlString = String.format(BASE_URL + "/%s",guid);
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate + "/%s", serverName, userId, guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId);
        }

        return callPutRESTCall(methodName,
                parameterizedType,
                expandedURL,
                category,
                params);
    }
    /**
     * Issue a PUT REST call that returns a response object. The object to be updated is passed.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the  object
     * @param methodName      name of the method being called.
     * @param urlTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param parameterizedType   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param t           type of object to be creates
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */

    public <T> GenericResponse<T> putRESTCall(String userId,
                                              String guid,
                                              String methodName,
                                              String urlTemplate,
                                              ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                              T t)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }


        String expandedURL = String.format(serverPlatformURLRoot + urlTemplate , serverName, userId, guid);

//        System.out.println(expandedURL);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId);
        }

        return callPutRESTCall(methodName,
                parameterizedType,
                expandedURL,
                t,
                true);
    }

    /**
     * Issue a DEL REST call that returns a response object.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param parameterizedType   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param methodName      name of the method being called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param guid        unique identifier of the  object
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public <T> GenericResponse<T> delRESTCall(String userId,
                                              ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                              String methodName,
                                              String urnTemplate,
                                              String guid) throws PropertyServerException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guId=" + guid);
        }

        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate + "/%s", serverName, userId, guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId + ",guId=" + guid);
        }

        return callDeleteRESTCall(methodName,
                parameterizedType,
                expandedURL);
    }

    /**
     * Method for constructing a query (https://en.wikipedia.org/wiki/Query_string) using the information described in the findRequest
     * page size and startingFrom need to set by the caller.
     *
     * @param methodName  name of the method being called.
     * @param findRequest {@link FindRequest}
     * @return query
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public QueryBuilder createFindQuery(String methodName, FindRequest findRequest) throws InvalidParameterException {
        return createFindQuery(methodName, findRequest, false, true);
    }

    public QueryBuilder createFindQuery(String methodName, FindRequest findRequest, boolean exactValue, boolean ignoreCase) throws InvalidParameterException {
        QueryBuilder queryBuilder = new QueryBuilder();
        SequencingOrder sequencingOrder = findRequest.getSequencingOrder();
        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        String sequencingOrderName = QueryUtils.encodeParams(methodName, "sequencingOrder", sequencingOrder.name());
        queryBuilder.addParam("sequencingOrder", sequencingOrderName);
        Integer pageSize = findRequest.getPageSize();
        if (pageSize != null) {
            queryBuilder.addParam("pageSize", pageSize);
        }
        queryBuilder.addParam("startingFrom", findRequest.getStartingFrom());

        String searchCriteria = findRequest.getSearchCriteria();
        if (searchCriteria != null) {
            searchCriteria = QueryUtils.encodeParams(methodName, "searchCriteria", searchCriteria);
            queryBuilder.addParam("searchCriteria", searchCriteria);
        }
        String property = findRequest.getSequencingProperty();
        if (property != null) {
            property = QueryUtils.encodeParams(methodName, "sequencingProperty", property);
            queryBuilder.addParam("sequencingProperty", property);
        }
        queryBuilder.addParam("exactValue", exactValue);
        queryBuilder.addParam("ignoreCase", ignoreCase);
        return queryBuilder;
    }

    /**
     * Method for constructing a query (https://en.wikipedia.org/wiki/Query_string) using the information described in the findRequest
     * page size and startingFrom need to set by the caller.
     *
     * @param methodName  name of the method being called.
     * @param findRequest {@link FindRequest}
     * @param queryParams query parameters
     *
     * @return query
     * @throws InvalidParameterException one of the parameters is null or invalid
     * */
    public QueryBuilder createFindQuery(String methodName,
                                        FindRequest findRequest,
                                        QueryParams queryParams) throws InvalidParameterException {
        QueryBuilder queryBuilder = new QueryBuilder();
        SequencingOrder sequencingOrder = findRequest.getSequencingOrder();
        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        String sequencingOrderName = QueryUtils.encodeParams(methodName, "sequencingOrder", sequencingOrder.name());
        queryBuilder.addParam("sequencingOrder", sequencingOrderName);
        Integer pageSize = findRequest.getPageSize();
        if (pageSize != null) {
            queryBuilder.addParam("pageSize", pageSize);
        }
        queryBuilder.addParam("startingFrom",  findRequest.getStartingFrom());

        String searchCriteria = findRequest.getSearchCriteria();
        if (searchCriteria != null) {
            searchCriteria = QueryUtils.encodeParams(methodName, "searchCriteria", searchCriteria);
            queryBuilder.addParam("searchCriteria", searchCriteria);
        }
        String property = findRequest.getSequencingProperty();
        if (property != null) {
            property = QueryUtils.encodeParams(methodName, "sequencingProperty", property);
            queryBuilder.addParam("sequencingProperty", property);
        }
        queryBuilder.addParams(queryParams);
        return queryBuilder;
    }


    /**
     * Issue a GET REST call that does a find and returns results in a Generic Response.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName      name of the method being called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param type   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param findRequest {@link FindRequest}
     * @param exactValue  boolean to determine search parameters should return exact values
     * @param ignoreCase boolean to determine search parameters should ignore case for teh search criteris
     * @param maximumPageSizeOnRestCall Page size for results
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> findRESTCall(String userId,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               FindRequest findRequest,
                                               boolean exactValue,
                                               boolean ignoreCase,
                                               Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        GenericResponse<T> completeResponse = null;
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }

        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
        int requestedPageSize = findRequest.getPageSize();
        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall < 1 || maximumPageSizeOnRestCall >= requestedPageSize) {
            // Only need to make one call
//            System.out.println("serverPlatformURLRoot + urnTemplate " + serverPlatformURLRoot + urnTemplate);
            String findUrlTemplate = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);
            // the searchCriteria could contain utf-8 characters that could include % characters, that format would incorrectly interpret. So we add the query params after the format
            String expandedURL = findUrlTemplate + createFindQuery(methodName, findRequest, exactValue, ignoreCase).toString();
//            System.out.println("++++ expandedURL " + expandedURL);
            completeResponse = callGetRESTCall(methodName, type, expandedURL);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);
        } else {
            completeResponse = getAccumulatedResponse(userId, methodName, urnTemplate, type, findRequest, maximumPageSizeOnRestCall, requestedPageSize);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return completeResponse;
    }

    /**
     * Issue a GET REST call that does a find and returns results in a Generic Response.
     *
     * @param <T>         return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName      name of the method being called.
     * @param urnTemplate template of the URN for the REST API call with place-holders for the parameters.
     * @param type   class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                    An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param findRequest {@link FindRequest}
     * @param exactValue  boolean to determine search parameters should return exact values
     * @param ignoreCase boolean to determine search parameters should ignore case for teh search criteris
     * @param maximumPageSizeOnRestCall Page size for results
     * @param guid Object guid
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> findRESTCallById(String userId,
                                                   String methodName,
                                                   String urnTemplate,
                                                   ParameterizedTypeReference<GenericResponse<T>> type,
                                                   FindRequest findRequest,
                                                   boolean exactValue,
                                                   boolean ignoreCase,
                                                   Integer maximumPageSizeOnRestCall,
                                                   String guid) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        GenericResponse<T> completeResponse = null;
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }

        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
        int requestedPageSize = findRequest.getPageSize();
        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall < 1 || maximumPageSizeOnRestCall >= requestedPageSize) {
            // Only need to make one call
            String findUrlTemplate = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
            // the searchCriteria could contain utf-8 characters that could include % characters, that format would incorrectly interpret. So we add the query params after the format
            String expandedURL = findUrlTemplate + createFindQuery(methodName, findRequest, exactValue, ignoreCase).toString();
            completeResponse = callGetRESTCall(methodName, type, expandedURL);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);
        } else {
            completeResponse = getAccumulatedResponse(userId, methodName, urnTemplate, type, findRequest, maximumPageSizeOnRestCall, requestedPageSize);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return completeResponse;
    }


    private <T> GenericResponse<T> getAccumulatedResponse(String userId, String methodName, String urnTemplate, ParameterizedTypeReference<GenericResponse<T>> type, FindRequest findRequest, Integer maximumPageSizeOnRestCall, int requestedPageSize) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        // issue multiple calls to build up the requested page
        GenericResponse<T> completeResponse = null;
        int startingFrom = findRequest.getStartingFrom();
        int totalNumberRetrieved = 0;
        // while we have got less than the requested page size continuing getting more by issuing rest calls
        while (totalNumberRetrieved < requestedPageSize) {
            // set the page size to the maximum that the downstream server accepts.
            findRequest.setPageSize(maximumPageSizeOnRestCall);
            // we need to amend the startingFrom portions when issuing subsequent requests.
            findRequest.setStartingFrom(startingFrom + totalNumberRetrieved);
            // encode a url with the new find request
            String findUrlTemplate = urnTemplate + createFindQuery(methodName, findRequest).toString();
            String expandedURL = String.format(serverPlatformURLRoot + findUrlTemplate, serverName, userId);
            // issue the get call
            GenericResponse<T> responseForPart = callGetRESTCall(methodName, type, expandedURL);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, responseForPart);

            int numberRetrieved = responseForPart.results().size();

            if (getMore(numberRetrieved, maximumPageSizeOnRestCall, requestedPageSize)) {
                // there are more to get
                totalNumberRetrieved = totalNumberRetrieved + numberRetrieved;
            } else {
                // Don't get any more
                totalNumberRetrieved = requestedPageSize;
            }
            // update the completeResponse
            if (completeResponse == null) {
                // first time in initialise the complete response
                completeResponse = responseForPart;
            } else {
                // add the results from this get to the complete response
                completeResponse.addAllResults(responseForPart.results());
            }
        }
        return completeResponse;
    }

    boolean getMore(int numberRetrieved, int maximumPageSizeOnRestCall, int requestedPageSize) {
        return (numberRetrieved == maximumPageSizeOnRestCall) && (requestedPageSize > maximumPageSizeOnRestCall);
    }


    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T>                       return type for results in {@link GenericResponse}
     * @param userId                    unique identifier for requesting user, under which the request is performed
     * @param guid                      unique identifier of the object
     * @param methodName                name of the method being called.
     * @param type                      class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *                                  An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate               template of the URN for the REST API call with place-holders for the parameters.
     * @param findRequest {@link FindRequest}
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @param params           additional parameters to be passed
     * @return GenericResponse with T result
     * @throws PropertyServerException    something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException  one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate,
                                                  FindRequest findRequest,
                                                  Integer maximumPageSizeOnRestCall,
                                                  Map<String, String> params) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        int requestedPageSize = 0;
        if (findRequest != null) {
            if (findRequest.getPageSize() == null) {
                findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
            }
            invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
            requestedPageSize = findRequest.getPageSize();
        }

        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall < 1 || maximumPageSizeOnRestCall >= requestedPageSize) {
            // Only need to make one call
            String expandedURL;
            if (findRequest == null) {
                expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
            } else {
                QueryBuilder queryBuilder = createFindQuery(methodName, findRequest);
                if (params != null && params.keySet().size() > 0) {
                    for (String param : params.keySet()) {
                        queryBuilder.addParam(param, params.get(param));
                    }
                }

                String findUrlTemplate = urnTemplate + queryBuilder.toString();
                expandedURL = String.format(serverPlatformURLRoot + findUrlTemplate, serverName, userId, guid);
            }
            completeResponse = callGetRESTCall(methodName, type, expandedURL);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);
        } else {
            // amend the urnTemplate to replace the guid - which will be the last %s, note there might be characters after this %s we need to keep.
            int lastIndex = urnTemplate.lastIndexOf("%s");
            String lastBit = urnTemplate.substring(lastIndex);
            String lastBitWithGuid = lastBit.replace("%s", guid);
            urnTemplate = urnTemplate.substring(0, lastIndex) + lastBitWithGuid;
            completeResponse = getAccumulatedResponse(userId, methodName, urnTemplate, type, findRequest, maximumPageSizeOnRestCall, requestedPageSize);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return completeResponse;
    }
    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param findRequest {@link FindRequest}
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @param queryParams           additional parameters to be passed
     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate,
                                                  FindRequest findRequest,
                                                  Integer maximumPageSizeOnRestCall,
                                                  QueryParams queryParams) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        int requestedPageSize = 0;
        if (findRequest != null) {
            if (findRequest.getPageSize() == null) {
                findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
            }
            invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
            requestedPageSize = findRequest.getPageSize();
        }

        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall < 1 || maximumPageSizeOnRestCall >= requestedPageSize ) {
            // Only need to make one call
            String expandedURL;
            if (findRequest == null) {
                expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
            } else {
                QueryBuilder queryBuilder = createFindQuery(methodName, findRequest, queryParams);
                String findUrlTemplate = urnTemplate + queryBuilder.toString();
                expandedURL = String.format(serverPlatformURLRoot + findUrlTemplate, serverName, userId, guid);
            }
            completeResponse = callGetRESTCall(methodName, type, expandedURL);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);
        } else {
            // amend the urnTemplate to replace the guid - which will be the last %s, note there might be characters after this %s we need to keep.
            int lastIndex = urnTemplate.lastIndexOf("%s");
            String lastBit =urnTemplate.substring(lastIndex);
            String lastBitWithGuid = lastBit.replace("%s",guid);
            urnTemplate = urnTemplate.substring(0, lastIndex) + lastBitWithGuid;
            completeResponse = getAccumulatedResponse(userId, methodName, urnTemplate, type, findRequest, maximumPageSizeOnRestCall, requestedPageSize);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return completeResponse;
    }


    /**
     * Issue a GET REST call that returns a Config object by guid.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @return OMAGServerConfigResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    public OMAGServerConfigResponse getConfigRESTCall(String userId,
                                                      String guid,
                                                      String methodName,
                                                      Class<OMAGServerConfigResponse> type,
                                                      String urnTemplate)
            throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        OMAGServerConfigResponse completeResponse = null;
        String expandedURL;
/////servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/categories")
        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, userId, serverName);//, guid);
//        System.out.println(" expandedURL  " + expandedURL);
        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;

    }
    /**
     * Issue a GET REST call that returns a View Config object by guid.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @return ViewServiceConfigResponse object
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public ViewServiceConfigResponse getViewServiceConfigRESTCall(String userId,
                                                                  String guid,
                                                                  String methodName,
                                                                  Class<ViewServiceConfigResponse> type,
                                                                  String urnTemplate)
                                                        throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        ViewServiceConfigResponse completeResponse = null;
        String expandedURL;
/////servers/{serverName}/open-metadata/view-services/glossary-author/users/{userId}/categories")
        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, userId, serverName);//, guid);
//        System.out.println(" expandedURL  " + expandedURL);
        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;

    }
    /**
     * Issue a GET REST call that returns a View Config object by guid.
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @return ViewServicesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public ViewServicesResponse getViewConfigRESTCall(String userId,
                                                      String methodName,
                                                      Class<ViewServicesResponse> type,
                                                      String urnTemplate) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }

        ViewServicesResponse completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, userId, serverName);//, guid);
//        System.out.println(" expandedURL  " + expandedURL);
        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;

    }
}
