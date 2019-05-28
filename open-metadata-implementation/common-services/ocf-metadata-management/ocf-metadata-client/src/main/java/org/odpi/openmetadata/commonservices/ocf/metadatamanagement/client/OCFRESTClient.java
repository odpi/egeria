/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * RESTClient is responsible for issuing calls to the Community Profile OMAS REST APIs.
 */
class RESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot) throws InvalidParameterException
    {
        super (serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot,
               String userId,
               String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns an AssetResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return AssetResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    AssetResponse callAssetGetRESTCall(String    methodName,
                                       String    urlTemplate,
                                       Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, AssetResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an CertificationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return CertificationsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    CertificationsResponse callCertificationGetRESTCall(String    methodName,
                                                        String    urlTemplate,
                                                        Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, CertificationsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an CommentsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return CommentsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    CommentsResponse callCommentGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, CommentsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an SchemaTypeResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SchemaTypeResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    SchemaTypeResponse callSchemaTypeGetRESTCall(String    methodName,
                                                 String    urlTemplate,
                                                 Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, SchemaTypeResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an ConnectionsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ConnectionsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    ConnectionsResponse callConnectionGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, ConnectionsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an ExternalIdentifiersResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ExternalIdentifiersResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    ExternalIdentifiersResponse callExternalIdentifierGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, ExternalIdentifiersResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an ExternalReferencesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ExternalReferencesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    ExternalReferencesResponse callExternalReferenceGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, ExternalReferencesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an InformalTagsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return InformalTagsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    InformalTagsResponse callInformalTagGetRESTCall(String    methodName,
                                                    String    urlTemplate,
                                                    Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, InformalTagsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an LicensesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LicensesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    LicensesResponse callLicenseGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, LicensesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an LikesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LikesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    LikesResponse callLikeGetRESTCall(String    methodName,
                                      String    urlTemplate,
                                      Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, LikesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an LocationsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return LocationsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    LocationsResponse callLocationGetRESTCall(String    methodName,
                                              String    urlTemplate,
                                              Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, LocationsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an NoteLogsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return NoteLogsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    NoteLogsResponse callNoteLogGetRESTCall(String    methodName,
                                            String    urlTemplate,
                                            Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, NoteLogsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an NotesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return NotesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    NotesResponse callNoteGetRESTCall(String    methodName,
                                      String    urlTemplate,
                                      Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, NotesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an RatingsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RatingsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    RatingsResponse callRatingGetRESTCall(String    methodName,
                                          String    urlTemplate,
                                          Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, RatingsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an RelatedAssetsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RelatedAssetsResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    RelatedAssetsResponse callRelatedAssetGetRESTCall(String    methodName,
                                                      String    urlTemplate,
                                                      Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, RelatedAssetsResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an RelatedMediaReferencesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return RelatedMediaReferencesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    RelatedMediaReferencesResponse callRelatedMediaReferenceGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, RelatedMediaReferencesResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns an SchemaAttributesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SchemaAttributesResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    SchemaAttributesResponse callSchemaAttributeGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, SchemaAttributesResponse.class, urlTemplate, params);
    }
}
