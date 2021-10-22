package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaRestClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryBuilder;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

public class GlossaryAuthorViewRestClient extends FFDCRESTClient {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRestClient.class);
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

    //Get Relationships
    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate)    throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);

        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }
    //get by GUID
    public <T> GenericResponse<T> getByGUIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate)    throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);

        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }
    //Restore
    public <T> GenericResponse<T> postRESTCall(String userId,
                                               String methodName,
                                               String urlTemplate,
                                               Class<SubjectAreaOMASAPIResponse> type,
                                               String guid)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
        completeResponse = callPostRESTCallNoParams(methodName,
                            type,
                            urlTemplate,
                            null);


//                callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

    //add
    public <T> GenericResponse<T> postRESTCall(String userId,
                                               String create,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                               Glossary glossary)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + create + ",userId=" + userId );
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId);

        completeResponse = callPostRESTCall(create,
                                            parameterizedType,
                                            expandedURL,
                                            glossary);

        exceptionHandler.detectAndThrowStandardExceptions(create, completeResponse);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + create + ",userId=" + userId);
        }

        return completeResponse;
    }

    //Replace
    public <T> GenericResponse<T> putRESTCall(String userId,
                                               String methodName,
                                               String urnTemplate,
                                               ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                               Glossary glossary,
                                              Object ... params)
            throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId );
        }

        String guid = glossary.getSystemAttributes().getGUID();
        //String urlString = String.format(BASE_URL + "/%s",guid);
        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate + "/%s", serverName, userId,guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId);
        }

        return callPutRESTCall( methodName,
                                parameterizedType,
                                urnTemplate,
                                glossary,
                                params);
    }

    //delete
    public <T> GenericResponse<T> delRESTCall(String userId,
                                              ParameterizedTypeReference<GenericResponse<T>> parameterizedType,
                                              String methodName,
                                              String urnTemplate,
                                              String guid) throws PropertyServerException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guId=" + guid );
        }

        String expandedURL = String.format(serverPlatformURLRoot + urnTemplate + "/%s", serverName, userId,guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Glossary successful method : " + methodName + ",userId=" + userId + ",guId=" + guid);
        }

        return callDeleteRESTCall(methodName,
                                    parameterizedType,
                                    expandedURL);
    }
/*
// Get Relationships
    public <T> GenericResponse<T> getRelByGUIdRESTCall(String userId,
                                                    String guid,
                                                    String methodName,
                                                    ParameterizedTypeReference<GenericResponse<T>> type,
                                                    String urnTemplate)    throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException {
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        GenericResponse<T> completeResponse = null;
        String expandedURL;

        expandedURL = String.format(serverPlatformURLRoot + urnTemplate, serverName, userId, guid);

        completeResponse = callGetRESTCall(methodName, type, expandedURL);
        exceptionHandler.detectAndThrowStandardExceptions(methodName, completeResponse);

        public SubjectAreaOMASAPIResponse<Relationship> getGlossaryRelationships(@PathVariable String serverName, @PathVariable String userId,
                @PathVariable String guid,
                @RequestParam(value = "asOfTime", required = false) Date asOfTime,
        @RequestParam(value = "startingFrom", required = false, defaultValue = "0") int startingFrom,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
        @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
    )
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }

        return completeResponse;
    }

*/

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
         * @return GenericResponse with T result
         * @throws PropertyServerException something went wrong with the REST call stack.
         * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
         * @throws InvalidParameterException one of the parameters is null or invalid
         */
/*    public <T> GenericResponse<T> getByIdRESTCall(String userId,
                                                  String guid,
                                                  String methodName,
                                                  ParameterizedTypeReference<GenericResponse<T>> type,
                                                  String urnTemplate,
                                                  FindRequest findRequest,
                                                  Integer maximumPageSizeOnRestCall,
                                                  Map<String, String> params) throws InvalidParameterException,
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
                QueryBuilder queryBuilder = createFindQuery(methodName, findRequest);
                if (params != null && params.keySet().size() >0) {
                    for (String param: params.keySet()) {
                        queryBuilder.addParam(param,params.get(param));
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
            String lastBit =urnTemplate.substring(lastIndex);
            String lastBitWithGuid = lastBit.replace("%s",guid);
            urnTemplate = urnTemplate.substring(0, lastIndex) + lastBitWithGuid;
            completeResponse = getAccumulatedResponse(userId, methodName, urnTemplate, type, findRequest, maximumPageSizeOnRestCall, requestedPageSize);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return completeResponse;
    }*/
}
