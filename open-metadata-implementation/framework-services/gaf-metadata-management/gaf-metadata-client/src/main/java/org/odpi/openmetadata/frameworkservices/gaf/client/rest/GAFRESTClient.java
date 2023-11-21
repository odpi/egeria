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
     * Issue a POST REST call that returns a ElementHeaderResponse object.
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
    public ElementHeaderResponse callElementHeaderPostRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object    requestBody,
                                                               Object... params) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ElementHeaderResponse restResult = this.callPostRESTCall(methodName,
                                                                 ElementHeaderResponse.class,
                                                                 urlTemplate,
                                                                 requestBody,
                                                                 params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a ElementHeadersResponse object.
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
    public ElementHeadersResponse callElementHeadersPostRESTCall(String    methodName,
                                                                 String    urlTemplate,
                                                                 Object    requestBody,
                                                                 Object... params) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ElementHeadersResponse restResult = this.callPostRESTCall(methodName,
                                                                  ElementHeadersResponse.class,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
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
     * Issue a POST REST call that returns a RelatedMetadataElementsListResponse object.
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
    public RelatedMetadataElementsListResponse callRelatedMetadataElementsListPostRESTCall(String    methodName,
                                                                                           String    urlTemplate,
                                                                                           Object    requestBody,
                                                                                           Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        RelatedMetadataElementsListResponse restResult = this.callPostRESTCall(methodName,
                                                                               RelatedMetadataElementsListResponse.class,
                                                                               urlTemplate,
                                                                               requestBody,
                                                                               params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a RelatedMetadataElementsResponse object.
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
    public RelatedMetadataElementsResponse callRelatedMetadataElementsPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        RelatedMetadataElementsResponse restResult = this.callPostRESTCall(methodName,
                                                                           RelatedMetadataElementsResponse.class,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedMetadataElementsListResponse object.
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
    public RelatedMetadataElementsListResponse callRelatedMetadataElementsListGetRESTCall(String    methodName,
                                                                                      String    urlTemplate,
                                                                                      Object... params) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        RelatedMetadataElementsListResponse restResult = this.callGetRESTCall(methodName, RelatedMetadataElementsListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedMetadataElementsResponse object.
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
    public RelatedMetadataElementsResponse callRelatedMetadataElementGetRESTCall(String    methodName,
                                                                                 String    urlTemplate,
                                                                                 Object... params) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        RelatedMetadataElementsResponse restResult = this.callGetRESTCall(methodName, RelatedMetadataElementsResponse.class, urlTemplate, params);

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
     * Issue a GET REST call that returns a GovernanceActionProcessElementsResponse object.
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
    public GovernanceActionProcessElementsResponse callGovernanceActionProcessesGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        GovernanceActionProcessElementsResponse restResult = this.callGetRESTCall(methodName, GovernanceActionProcessElementsResponse.class, urlTemplate, params);

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
}
