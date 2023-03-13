/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.client.rest;


import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.gaf.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * OpenMetadataStoreRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class OpenMetadataStoreRESTClient extends OCFRESTClient
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
    public OpenMetadataStoreRESTClient(String   serverName,
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
    public OpenMetadataStoreRESTClient(String serverName,
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
    public OpenMetadataStoreRESTClient(String   serverName,
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
    public OpenMetadataStoreRESTClient(String serverName,
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
}
