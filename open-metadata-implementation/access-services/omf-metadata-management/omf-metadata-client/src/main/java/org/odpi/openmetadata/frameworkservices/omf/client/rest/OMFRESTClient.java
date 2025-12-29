/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.client.rest;


import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;


/**
 * OMFRESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class OMFRESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public OMFRESTClient(String   serverName,
                         String   serverPlatformURLRoot,
                         String   secretsStoreProvider,
                         String   secretsStoreLocation,
                         String   secretsStoreCollection,
                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, auditLog);
    }


    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public OMFRESTClient(String                             serverName,
                         String                             serverPlatformURLRoot,
                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                         AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreConnectorMap, auditLog);
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
     * Issue a POST REST call that returns a AttachedClassificationsResponse object.
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
    public AttachedClassificationsResponse callAttachedClassificationsPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        AttachedClassificationsResponse restResult = this.callPostRESTCall(methodName,
                                                                           AttachedClassificationsResponse.class,
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
}
