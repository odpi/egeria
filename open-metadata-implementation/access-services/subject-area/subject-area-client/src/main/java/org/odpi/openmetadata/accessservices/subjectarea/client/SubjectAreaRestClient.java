/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryParams;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
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
    protected static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final String serverName;
    private final String serverPlatformURLRoot;

    /**
     * Constructor
     */
    public SubjectAreaRestClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    /**
     * Constructor
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
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> postRESTCall(String userId,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               T requestBody) throws PropertyServerException,
                                                                     UserNotAuthorizedException,
                                                                     InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);
        GenericResponse<T> response = callPostRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the update object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> putRESTCall(String userId,
                                              String guid,
                                              String methodName,
                                              String urnTemplate,
                                              ParameterizedTypeReference<GenericResponse<T>> type,
                                              T requestBody) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        GenericResponse<T> response = callPutRESTCall(methodName, type, expandedURL, requestBody);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Issue a GET REST call that returns a response object by guid. This is a get for a single object.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the received object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.

     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return getByIdRESTCall(userId, guid, methodName, type, urnTemplate, null, null, null);
    }
    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the received object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param findRequest search specification
     *
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
                                                  FindRequest findRequest) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
       return getByIdRESTCall(userId, guid, methodName, type, urnTemplate, findRequest, null, null);
    }
    /**
     * Issue a GET REST call that returns a response object by guid.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the received object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @param findRequest search specification
     * @param queryParams {@link QueryParams}
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
     * Issue a GET REST call that returns a response found objects using the information described in findRequest,
     * specifying the maximum amount that the downstream server supports.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param findRequest {@link FindRequest}
     * @param queryParams {@link QueryParams}
     *
     * @return GenericResponse with T results
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> findRESTCall(String userId,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               FindRequest findRequest,
                                               QueryParams queryParams) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException {
        return findRESTCall(userId, methodName, urnTemplate, type, findRequest, queryParams, null);
    }
    /**
     * Issue a GET REST call that returns a response found objects using the information described in findRequest,
     * specifying the maximum amount that the downstream server supports.
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param methodName  name of the method being called.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param findRequest {@link FindRequest}
     * @param maximumPageSizeOnRestCall maximum page size that can be used on rest calls, null and 0 mean no limit set.
     * @param queryParams {@link QueryParams}
     *
     * @return GenericResponse with T results
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> findRESTCall(String userId,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               FindRequest findRequest,
                                               QueryParams queryParams,
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
        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall < 1 || maximumPageSizeOnRestCall >= requestedPageSize ) {
            // Only need to make one call
            String findUrlTemplate = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);
            // the searchCriteria could contain utf-8 characters that could include % characters, that format would incorrectly interpret. So we add the query params after the format
            String expandedURL = findUrlTemplate + createFindQuery(methodName, findRequest, queryParams).toString();
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
       return  (numberRetrieved == maximumPageSizeOnRestCall) && (requestedPageSize > maximumPageSizeOnRestCall);
    }

    /**
     * Issue a GET REST call that returns a response found objects using the information described in findRequest
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the object to which the found objects should relate
     * @param methodName  name of the method being called.
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param findRequest {@link FindRequest}
     * @param maximumPageSizeOnRestCall maximum number of results
     *
     * @return GenericResponse with T results
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> findRESTCall(String userId,
                                               String guid,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> type,
                                               FindRequest findRequest,
                                               Integer maximumPageSizeOnRestCall) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        GenericResponse<T> completeResponse = null;
        if (findRequest.getPageSize() == null) {
            findRequest.setPageSize(invalidParameterHandler.getMaxPagingSize());
        }

        invalidParameterHandler.validatePaging(findRequest.getStartingFrom(), findRequest.getPageSize(), methodName);
        int requestedPageSize = findRequest.getPageSize();
        int startingFrom = findRequest.getStartingFrom();
        if (maximumPageSizeOnRestCall == null || maximumPageSizeOnRestCall == 0 || maximumPageSizeOnRestCall >= requestedPageSize ) {
            // only need to issued one rest call
            completeResponse = getByIdRESTCall(userId, guid, methodName, type, urnTemplate, findRequest);
            exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);
        } else {
            // issue multiple calls to build up the requested page
            int totalNumberRetrieved = 0;
            // while we have got less than the requested page size continuing getting more by issuing rest calls
            while (totalNumberRetrieved < requestedPageSize) {
                // set the page size to the maximum that the downstream server accepts.
                findRequest.setPageSize(maximumPageSizeOnRestCall);
                // we need to amend the startingFrom potions when issuing subsequent requests.
                findRequest.setStartingFrom(totalNumberRetrieved);
                // issue the get call
                GenericResponse<T> responseForPart = getByIdRESTCall(userId, guid, methodName, type, urnTemplate, findRequest);
                exceptionHandler.detectAndThrowStandardExceptions(methodName, responseForPart);

                int numberRetrieved = responseForPart.results().size();
                if (numberRetrieved == 0) {
                    // break out of the while loop as there is no more to retrieve.
                    totalNumberRetrieved = requestedPageSize;
                } else {
                    // there may be more to get
                    totalNumberRetrieved = totalNumberRetrieved + numberRetrieved;
                    if (completeResponse == null) {
                        // first time in initialise the complete response
                        completeResponse = responseForPart;
                    } else {
                        // add the results from this get to the complete response
                        completeResponse.addAllResults(responseForPart.results());
                    }
                    // amend the starting from
                    startingFrom= startingFrom + numberRetrieved;
                }
            }
        }
        return completeResponse;
    }

    /**
     * Issue a Delete REST call
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the delete object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     *
     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> deleteRESTCall(String userId,
                                                 String guid,
                                                 String methodName,
                                                 ParameterizedTypeReference<GenericResponse<T>> type,
                                                 String urnTemplate) throws PropertyServerException,
                                                                            UserNotAuthorizedException,
                                                                            InvalidParameterException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        GenericResponse<T> response = callDeleteRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        return response;
    }

    /**
     * Issue a restore object
     *
     * @param <T> return type for results in {@link GenericResponse}
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        unique identifier of the restore object
     * @param methodName  name of the method being called.
     * @param type class of the response for generic object. Descried using {@link ParameterizedTypeReference}
     *             An example can be seen here {@link ResponseParameterization#getParameterizedType()}
     * @param urnTemplate  template of the URN for the REST API call with place-holders for the parameters.
     *
     * @return GenericResponse with T result
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public <T> GenericResponse<T> restoreRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);
        GenericResponse<T> response = callPostRESTCall(methodName, type, expandedURL, null);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, response);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Method for constructing a query (https://en.wikipedia.org/wiki/Query_string) using the information described in the findRequest
     * page size and startingFrom need to set by the caller.
     *
     * @param methodName  name of the method being called.
     * @param findRequest {@link FindRequest}
     *
     * @return query
     * @throws InvalidParameterException one of the parameters is null or invalid
     * */
    public QueryBuilder createFindQuery(String methodName, FindRequest findRequest) throws InvalidParameterException {
        return createFindQuery(methodName, findRequest, null);
    }

    /**
     * Method for constructing a query (https://en.wikipedia.org/wiki/Query_string) using the information described in the findRequest
     * page size and startingFrom need to set by the caller.
     *
     * @param methodName  name of the method being called.
     * @param findRequest {@link FindRequest}
     * @param queryParams {@link QueryParams}
     *
     * @return query
     * @throws InvalidParameterException one of the parameters is null or invalid
     * */
    public QueryBuilder createFindQuery(String methodName, FindRequest findRequest, QueryParams queryParams) throws InvalidParameterException {
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

    public String getServerName() {
        return serverName;
    }

    public String getServerPlatformURLRoot() {
        return serverPlatformURLRoot;
    }

}