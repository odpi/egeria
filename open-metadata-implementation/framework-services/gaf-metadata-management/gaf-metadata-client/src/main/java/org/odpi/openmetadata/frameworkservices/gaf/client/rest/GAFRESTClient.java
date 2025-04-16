/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client.rest;


import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * GAFRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class GAFRESTClient extends OCFRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GAFRESTClient(String   serverName,
                         String   serverPlatformURLRoot,
                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GAFRESTClient(String serverName,
                         String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GAFRESTClient(String   serverName,
                         String   serverPlatformURLRoot,
                         String   userId,
                         String   password,
                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GAFRESTClient(String serverName,
                         String serverPlatformURLRoot,
                         String userId,
                         String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * Issue a GET REST call that returns a TypeDefGalleryResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param params a list of parameters that are slotted into the url template
     * @return TypeDefGalleryResponseObject
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public  TypeDefGalleryResponse callTypeDefGalleryGetRESTCall(String    methodName,
                                                                 String    operationSpecificURL,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        TypeDefGalleryResponse restResult = this.callGetRESTCall(methodName,
                                                                 TypeDefGalleryResponse.class,
                                                                 operationSpecificURL,
                                                                 params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a AttributeTypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return AttributeTypeDefListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public  AttributeTypeDefListResponse callAttributeTypeDefListGetRESTCall(String    methodName,
                                                                             String    operationSpecificURL,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        AttributeTypeDefListResponse restResult = this.callGetRESTCall(methodName,
                                                                       AttributeTypeDefListResponse.class,
                                                                       operationSpecificURL,
                                                                       params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a AttributeTypeDefResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return AttributeTypeDefResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public AttributeTypeDefResponse callAttributeTypeDefGetRESTCall(String    methodName,
                                                                    String    operationSpecificURL,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        AttributeTypeDefResponse restResult =  this.callGetRESTCall(methodName,
                                                                    AttributeTypeDefResponse.class,
                                                                    operationSpecificURL,
                                                                    params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public  TypeDefListResponse callTypeDefListGetRESTCall(String    methodName,
                                                           String    operationSpecificURL,
                                                           Object... params) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        TypeDefListResponse restResult = this.callGetRESTCall(methodName,
                                                              TypeDefListResponse.class,
                                                              operationSpecificURL,
                                                              params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a TypeDefListResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public  TypeDefListResponse callTypeDefListPostRESTCall(String    methodName,
                                                            String    operationSpecificURL,
                                                            Object    requestBody,
                                                            Object... params) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        TypeDefListResponse restResult = this.callPostRESTCall(methodName,
                                                               TypeDefListResponse.class,
                                                               operationSpecificURL,
                                                               requestBody,
                                                               params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TypeDefResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TypeDefResponse callTypeDefGetRESTCall(String    methodName,
                                                  String    operationSpecificURL,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        TypeDefResponse restResult = this.callGetRESTCall(methodName,
                                                          TypeDefResponse.class,
                                                          operationSpecificURL,
                                                          params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a TypeDefResponse object.
     *
     * @param methodName name of the method being called
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param requestBody request body object
     * @param params a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public  TypeDefResponse callTypeDefPostRESTCall(String    methodName,
                                                    String    operationSpecificURL,
                                                    Object    requestBody,
                                                    Object... params) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        TypeDefResponse restResult = this.callPostRESTCall(methodName,
                                                           TypeDefResponse.class,
                                                           operationSpecificURL,
                                                           requestBody,
                                                           params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a OpenMetadataElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementsResponse callOpenMetadataElementsPostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        OpenMetadataElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                        OpenMetadataElementsResponse.class,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);
        return restResult;
    }


    /**
     * Issue a POST REST call that returns a OpenMetadataElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementResponse callOpenMetadataElementPostRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object    requestBody,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        OpenMetadataElementResponse restResult = this.callPostRESTCall(methodName,
                                                                       OpenMetadataElementResponse.class,
                                                                       urlTemplate,
                                                                       requestBody,
                                                                       params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a AnchorSearchMatchesListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public AnchorSearchMatchesListResponse callAnchorSearchMatchesListPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        AnchorSearchMatchesListResponse restResult = this.callPostRESTCall(methodName,
                                                                           AnchorSearchMatchesListResponse.class,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);
        return restResult;
    }


    /**
     * Issue a POST REST call that returns a AnchorSearchMatchesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public AnchorSearchMatchesResponse callAnchorSearchMatchesPostRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object    requestBody,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        AnchorSearchMatchesResponse restResult = this.callPostRESTCall(methodName,
                                                                       AnchorSearchMatchesResponse.class,
                                                                       urlTemplate,
                                                                       requestBody,
                                                                       params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a OpenMetadataGraphResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataGraphResponse callOpenMetadataGraphPostRESTCall(String    methodName,
                                                                       String    urlTemplate,
                                                                       Object    requestBody,
                                                                       Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        OpenMetadataGraphResponse restResult = this.callPostRESTCall(methodName,
                                                                     OpenMetadataGraphResponse.class,
                                                                     urlTemplate,
                                                                     requestBody,
                                                                     params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OpenMetadataElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataElementResponse callOpenMetadataElementGetRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        OpenMetadataElementResponse restResult = this.callGetRESTCall(methodName, OpenMetadataElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a OpenMetadataRelationshipListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataRelationshipListResponse callOpenMetadataRelationshipListPostRESTCall(String    methodName,
                                                                                             String    urlTemplate,
                                                                                             Object    requestBody,
                                                                                             Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        OpenMetadataRelationshipListResponse restResult = this.callPostRESTCall(methodName,
                                                                                OpenMetadataRelationshipListResponse.class,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a OpenMetadataRelationshipResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataRelationshipResponse callOpenMetadataRelationshipPostRESTCall(String    methodName,
                                                                                     String    urlTemplate,
                                                                                     Object    requestBody,
                                                                                     Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        OpenMetadataRelationshipResponse restResult = this.callPostRESTCall(methodName,
                                                                            OpenMetadataRelationshipResponse.class,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OpenMetadataRelationshipListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataRelationshipListResponse callRelatedMetadataElementsListGetRESTCall(String    methodName,
                                                                                           String    urlTemplate,
                                                                                           Object... params) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        OpenMetadataRelationshipListResponse restResult = this.callGetRESTCall(methodName, OpenMetadataRelationshipListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a OpenMetadataRelationshipResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public OpenMetadataRelationshipResponse callRelatedMetadataElementGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        OpenMetadataRelationshipResponse restResult = this.callGetRESTCall(methodName, OpenMetadataRelationshipResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a RelatedMetadataElementListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedMetadataElementListResponse callRelatedMetadataElementListPostRESTCall(String    methodName,
                                                                                         String    urlTemplate,
                                                                                         Object    requestBody,
                                                                                         Object... params) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        RelatedMetadataElementListResponse restResult = this.callPostRESTCall(methodName,
                                                                              RelatedMetadataElementListResponse.class,
                                                                              urlTemplate,
                                                                              requestBody,
                                                                              params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedMetadataElementListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedMetadataElementListResponse callRelatedMetadataElementListGetRESTCall(String    methodName,
                                                                                        String    urlTemplate,
                                                                                        Object... params) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        RelatedMetadataElementListResponse restResult = this.callGetRESTCall(methodName, RelatedMetadataElementListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TranslationDetailResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TranslationDetailResponse callTranslationDetailGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        TranslationDetailResponse restResult = this.callGetRESTCall(methodName, TranslationDetailResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a TranslationListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TranslationListResponse callTranslationListGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        TranslationListResponse restResult = this.callGetRESTCall(methodName, TranslationListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidMetadataValueDetailListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidMetadataValueDetailListResponse callValidMetadataValueDetailListGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        ValidMetadataValueDetailListResponse restResult = this.callGetRESTCall(methodName, ValidMetadataValueDetailListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidMetadataValueListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidMetadataValueListResponse callValidMetadataValueListGetRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object... params) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        ValidMetadataValueListResponse restResult = this.callGetRESTCall(methodName, ValidMetadataValueListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ValidMetadataValueResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidMetadataValueResponse callValidMetadataValueGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        ValidMetadataValueResponse restResult = this.callGetRESTCall(methodName, ValidMetadataValueResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a BooleanResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public BooleanResponse callBooleanGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        BooleanResponse restResult = this.callGetRESTCall(methodName, BooleanResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a BooleanResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call, with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public BooleanResponse callBooleanPostRESTCall(String    methodName,
                                                   String    urlTemplate,
                                                   Object    requestBody,
                                                   Object... params) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        BooleanResponse restResult = this.callPostRESTCall(methodName, BooleanResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EngineActionElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EngineActionElementResponse callEngineActionGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        EngineActionElementResponse restResult = this.callGetRESTCall(methodName, EngineActionElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a EngineActionElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EngineActionElementsResponse callEngineActionsGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        EngineActionElementsResponse restResult = this.callGetRESTCall(methodName, EngineActionElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a EngineActionElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public EngineActionElementsResponse callEngineActionsPostRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object    requestBody,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        EngineActionElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                        EngineActionElementsResponse.class,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceTypeResponseElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionTypeResponse callGovernanceActionTypeGetRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        GovernanceActionTypeResponse restResult = this.callGetRESTCall(methodName, GovernanceActionTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a GovernanceActionTypesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionTypesResponse callGovernanceTypesPostRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object    requestBody,
                                                                         Object... params) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        GovernanceActionTypesResponse restResult = this.callPostRESTCall(methodName,
                                                                         GovernanceActionTypesResponse.class,
                                                                         urlTemplate,
                                                                         requestBody,
                                                                         params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceActionProcessStepResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessStepResponse callGovernanceProcessStepResponseGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        GovernanceActionProcessStepResponse restResult = this.callGetRESTCall(methodName, GovernanceActionProcessStepResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceProcessStepResponseElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessStepResponse callGovernanceActionProcessStepGetRESTCall(String    methodName,
                                                                                          String    urlTemplate,
                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        GovernanceActionProcessStepResponse restResult = this.callGetRESTCall(methodName, GovernanceActionProcessStepResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceProcessStepResponseElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public FirstGovernanceActionProcessStepResponse callFirstGovernanceActionProcessStepGetRESTCall(String    methodName,
                                                                                                    String    urlTemplate,
                                                                                                    Object... params) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        FirstGovernanceActionProcessStepResponse restResult = this.callGetRESTCall(methodName, FirstGovernanceActionProcessStepResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a GovernanceActionProcessStepsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessStepsResponse callGovernanceProcessStepsPostRESTCall(String    methodName,
                                                                                       String    urlTemplate,
                                                                                       Object    requestBody,
                                                                                       Object... params) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        GovernanceActionProcessStepsResponse restResult = this.callPostRESTCall(methodName,
                                                                                GovernanceActionProcessStepsResponse.class,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a NextGovernanceActionProcessStepsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public NextGovernanceActionProcessStepsResponse callNextGovernanceActionProcessStepsGetRESTCall(String    methodName,
                                                                                                    String    urlTemplate,
                                                                                                    Object... params) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        NextGovernanceActionProcessStepsResponse restResult = this.callGetRESTCall(methodName,
                                                                                   NextGovernanceActionProcessStepsResponse.class,
                                                                                   urlTemplate,
                                                                                   params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceActionProcessElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessElementResponse callGovernanceActionProcessGetRESTCall(String    methodName,
                                                                                         String    urlTemplate,
                                                                                         Object... params) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        GovernanceActionProcessElementResponse restResult = this.callGetRESTCall(methodName, GovernanceActionProcessElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceActionProcessGraphResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessGraphResponse callGovernanceActionProcessGraphPostRESTCall(String    methodName,
                                                                                             String    urlTemplate,
                                                                                             Object    requestBody,
                                                                                             Object... params) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        GovernanceActionProcessGraphResponse restResult = this.callPostRESTCall(methodName,
                                                                                GovernanceActionProcessGraphResponse.class,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a GovernanceActionProcessElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceActionProcessElementsResponse callGovernanceActionProcessElementsPostRESTCall(String    methodName,
                                                                                                   String    urlTemplate,
                                                                                                   Object    requestBody,
                                                                                                   Object... params) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        GovernanceActionProcessElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                                   GovernanceActionProcessElementsResponse.class,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a GovernanceEngineElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementResponse callGovernanceEnginePostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceEngineElementResponse restResult = this.callPostRESTCall(methodName,
                                                                            GovernanceEngineElementResponse.class,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceEngineElementResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters
     * @param params      a list of parameters that are slotted into the url template
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementResponse callGovernanceEngineGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        GovernanceEngineElementResponse restResult = this.callGetRESTCall(methodName, GovernanceEngineElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceEngineElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceEngineElementsResponse callGovernanceEnginesGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceEngineElementsResponse restResult = this.callGetRESTCall(methodName, GovernanceEngineElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a GovernanceServiceElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceServiceElementResponse callGovernanceServicePostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        GovernanceServiceElementResponse restResult = this.callPostRESTCall(methodName,
                                                                            GovernanceServiceElementResponse.class,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceServiceElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceServiceElementResponse callGovernanceServiceGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        GovernanceServiceElementResponse restResult = this.callGetRESTCall(methodName, GovernanceServiceElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceServiceElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceServiceElementsResponse callGovernanceServicesGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        GovernanceServiceElementsResponse restResult = this.callGetRESTCall(methodName, GovernanceServiceElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a RegisteredGovernanceServiceResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RegisteredGovernanceServiceResponse callRegisteredGovernanceServiceGetRESTCall(String    methodName,
                                                                                          String    urlTemplate,
                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        RegisteredGovernanceServiceResponse restResult = this.callGetRESTCall(methodName, RegisteredGovernanceServiceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredGovernanceServicesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RegisteredGovernanceServicesResponse callRegisteredGovernanceServicesGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        RegisteredGovernanceServicesResponse restResult = this.callGetRESTCall(methodName, RegisteredGovernanceServicesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a POST REST call that returns a MetadataCorrelationHeadersResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody object that passes additional parameters
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public MetadataCorrelationHeadersResponse callCorrelationHeadersPostRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object    requestBody,
                                                                                 Object... params) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        MetadataCorrelationHeadersResponse restResult = this.callPostRESTCall(methodName, MetadataCorrelationHeadersResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a GET REST call that returns a IntegrationGroupElementResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters
     * @param params      a list of parameters that are slotted into the url template
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public IntegrationGroupElementResponse callIntegrationGroupGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        IntegrationGroupElementResponse restResult = this.callGetRESTCall(methodName, IntegrationGroupElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a IntegrationGroupElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public IntegrationGroupElementsResponse callIntegrationGroupsGetRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        IntegrationGroupElementsResponse restResult = this.callGetRESTCall(methodName, IntegrationGroupElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a IntegrationConnectorElementResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public IntegrationConnectorElementResponse callIntegrationConnectorGetRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        IntegrationConnectorElementResponse restResult = this.callGetRESTCall(methodName, IntegrationConnectorElementResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a IntegrationConnectorElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public IntegrationConnectorElementsResponse callIntegrationConnectorsGetRESTCall(String    methodName,
                                                                                     String    urlTemplate,
                                                                                     Object... params) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        IntegrationConnectorElementsResponse restResult = this.callGetRESTCall(methodName, IntegrationConnectorElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredIntegrationConnectorResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RegisteredIntegrationConnectorResponse callRegisteredIntegrationConnectorGetRESTCall(String    methodName,
                                                                                                String    urlTemplate,
                                                                                                Object... params) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException
    {
        RegisteredIntegrationConnectorResponse restResult = this.callGetRESTCall(methodName, RegisteredIntegrationConnectorResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RegisteredIntegrationConnectorsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RegisteredIntegrationConnectorsResponse callRegisteredIntegrationConnectorsGetRESTCall(String    methodName,
                                                                                                  String    urlTemplate,
                                                                                                  Object... params) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        RegisteredIntegrationConnectorsResponse restResult = this.callGetRESTCall(methodName, RegisteredIntegrationConnectorsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CatalogTargetResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CatalogTargetResponse callCatalogTargetGetRESTCall(String    methodName,
                                                              String    urlTemplate,
                                                              Object... params) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        CatalogTargetResponse restResult = this.callGetRESTCall(methodName, CatalogTargetResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a CatalogTargetsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CatalogTargetsResponse callCatalogTargetsGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        CatalogTargetsResponse restResult = this.callGetRESTCall(methodName, CatalogTargetsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }
}
