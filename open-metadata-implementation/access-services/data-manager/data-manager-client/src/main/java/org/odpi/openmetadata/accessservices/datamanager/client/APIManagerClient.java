/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.APIManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.APIParameterListType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APIManagerClient is the client for managing APIs from an API Manager.
 */
public class APIManagerClient extends SchemaManagerClient implements APIManagerInterface
{
    private static final String apiURLTemplatePrefix       = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/apis";
    private static final String defaultSchemaAttributeName = "APIParameter";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String   serverName,
                            String   serverPlatformURLRoot,
                            AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String serverName,
                            String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String   serverName,
                            String   serverPlatformURLRoot,
                            String   userId,
                            String   password,
                            AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String                serverName,
                            String                serverPlatformURLRoot,
                            DataManagerRESTClient restClient,
                            int                   maxPageSize) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String serverName,
                            String serverPlatformURLRoot,
                            String userId,
                            String password) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * The API is a top level asset in an API manager
     */


    /**
     * Create a new metadata element to represent an API.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param apiProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPI(String        userId,
                            String        apiManagerGUID,
                            String        apiManagerName,
                            boolean       apiManagerIsHome,
                            String        endpointGUID,
                            APIProperties apiProperties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                  = "createAPI";
        final String propertiesParameterName     = "apiProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(apiProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(apiProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "?apiManagerIsHome={2}";
        final String urlTemplateWithEndpoint = serverPlatformURLRoot + apiURLTemplatePrefix + "/for-endpoint/{2}?apiManagerIsHome={3}";

        APIRequestBody requestBody = new APIRequestBody(apiProperties);
        
        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult;

        if (endpointGUID == null)
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplate,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         apiManagerIsHome);
        }
        else
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplateWithEndpoint,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         endpointGUID,
                                                         apiManagerIsHome);
        }

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPIFromTemplate(String             userId,
                                        String             apiManagerGUID,
                                        String             apiManagerName,
                                        boolean            apiManagerIsHome,
                                        String             endpointGUID,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "createAPIFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/from-template/{2}?apiManagerIsHome={3}";
        final String urlTemplateWithEndpoint = serverPlatformURLRoot + apiURLTemplatePrefix + "/for-endpoint/{2}/from-template/{3}?apiManagerIsHome={4}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);
        
        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult;

        if (endpointGUID == null)
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplate,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         templateGUID,
                                                         apiManagerIsHome);
        }
        else
        {
            restResult = restClient.callGUIDPostRESTCall(methodName,
                                                         urlTemplateWithEndpoint,
                                                         requestBody,
                                                         serverName,
                                                         userId,
                                                         endpointGUID,
                                                         templateGUID,
                                                         apiManagerIsHome);
        }

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing an API.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateAPI(String        userId,
                          String        apiManagerGUID,
                          String        apiManagerName,
                          String        apiGUID,
                          boolean       isMergeUpdate,
                          APIProperties apiProperties) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName                  = "updateAPI";
        final String elementGUIDParameterName    = "apiGUID";
        final String propertiesParameterName     = "apiProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(apiProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(apiProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        APIRequestBody requestBody = new APIRequestBody(apiProperties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);
        
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiGUID,
                                        isMergeUpdate);
    }


    /**
     * Update the zones for the api asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishAPI(String userId,
                           String apiGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        final String methodName               = "publishAPI";
        final String elementGUIDParameterName = "apiGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        apiGUID);
    }


    /**
     * Update the zones for the api asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the api is first created).
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawAPI(String userId,
                            String apiGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName               = "withdrawAPI";
        final String elementGUIDParameterName = "apiGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        apiGUID);
    }


    /**
     * Remove the metadata element representing an API.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeAPI(String userId,
                          String apiManagerGUID,
                          String apiManagerName,
                          String apiGUID,
                          String qualifiedName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "removeAPI";
        final String elementGUIDParameterName    = "apiGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/{3}/delete";
        
        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);
        
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiManagerGUID,
                                        apiManagerName,
                                        apiGUID,
                                        qualifiedName);
    }


    /**
     * Retrieve the list of api metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIElement> findAPIs(String userId,
                                     String searchString,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName                = "findAPIs";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        APIsResponse restResult = restClient.callAPIsPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  startFrom,
                                                                  validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of api metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIElement>   getAPIsByName(String userId,
                                            String name,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName        = "getAPIsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        APIsResponse restResult = restClient.callAPIsPostRESTCall(methodName,
                                                                 urlTemplate,
                                                                 requestBody,
                                                                 serverName,
                                                                 userId,
                                                                 startFrom,
                                                                 validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of apis created by this caller.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the api manager
     * @param apiManagerName unique name of software server capability representing the api manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIElement> getAPIsForAPIManager(String userId,
                                                 String apiManagerGUID,
                                                 String apiManagerName,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getAPIsForAPIManager";
        final String apiManagerGUIDParameterName = "apiManagerGUID";
        final String apiManagerNameParameterName = "apiManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiManagerGUID, apiManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(apiManagerName, apiManagerNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-managers/{2}/{3}?startFrom={4}&pageSize={5}";

        APIsResponse restResult = restClient.callAPIsGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 apiManagerGUID,
                                                                 apiManagerName,
                                                                 startFrom,
                                                                 validatedPageSize);

        return restResult.getElements();
    }



    /**
     * Retrieve the list of APIs connected to the requested endpoint.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the endpoint
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIElement> getAPIsForEndpoint(String userId,
                                               String endpointGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "getAPIsForEndpoint";
        final String endpointGUIDParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/by-endpoint/{2}?startFrom={3}&pageSize={4}";

        APIsResponse restResult = restClient.callAPIsGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 endpointGUID,
                                                                 startFrom,
                                                                 validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the api metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public APIElement getAPIByGUID(String userId,
                                   String guid) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "getAPIByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}";

        APIResponse restResult = restClient.callAPIGetRESTCall(methodName,
                                                               urlTemplate,
                                                               serverName,
                                                               userId,
                                                               guid);

        return restResult.getElement();
    }


    /* ============================================================================
     * A api may host one or more API operations depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiGUID unique identifier of the api where the API operation is located
     * @param properties properties about the API operation
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPIOperation(String                 userId,
                                     String                 apiManagerGUID,
                                     String                 apiManagerName,
                                     String                 apiGUID,
                                     APIOperationProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                     = "createAPIOperation";
        final String parentElementGUIDParameterName = "apiGUID";
        final String propertiesParameterName        = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations";

        APIOperationRequestBody requestBody = new APIOperationRequestBody(properties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an API operation using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the api where the API operation is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPIOperationFromTemplate(String             userId,
                                                 String             apiManagerGUID,
                                                 String             apiManagerName,
                                                 String             templateGUID,
                                                 String             apiGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                     = "createAPIOperationFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "apiGUID";
        final String propertiesParameterName        = "templateProperties";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(apiGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations/from-template/{3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a API operation.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateAPIOperation(String                 userId,
                                   String                 apiManagerGUID,
                                   String                 apiManagerName,
                                   String                 apiOperationGUID,
                                   boolean                isMergeUpdate,
                                   APIOperationProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName               = "updateAPIOperation";
        final String elementGUIDParameterName = "apiOperationGUID";
        final String propertiesParameterName  = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/{2}?isMergeUpdate={3}";

        APIOperationRequestBody requestBody = new APIOperationRequestBody(properties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiOperationGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the metadata element representing a API operation.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeAPIOperation(String userId,
                                   String apiManagerGUID,
                                   String apiManagerName,
                                   String apiOperationGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                  = "removeAPIOperation";
        final String elementGUIDParameterName    = "apiOperationGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiOperationGUID,
                                        qualifiedName);
    }


    /**
     * Retrieve the list of API operation metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIOperationElement>   findAPIOperations(String userId,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                = "findAPIOperations";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        APIOperationsResponse restResult = restClient.callAPIOperationsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElements();
    }




    /**
     * Return the list of api-operations associated with an API.
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the api to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the api-operations associated with the requested api
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIOperationElement> getOperationsForAPI(String userId,
                                                         String apiGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                     = "getOperationsForAPI";
        final String parentElementGUIDParameterName = "apiGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations?startFrom={3}&pageSize={4}";

        APIOperationsResponse restResult = restClient.callAPIOperationsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   apiGUID,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of API operation metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIOperationElement>   getAPIOperationsByName(String userId,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName        = "getAPIOperationsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        APIOperationsResponse restResult = restClient.callAPIOperationsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   serverName,
                                                                                   userId,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the API operation metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public APIOperationElement getAPIOperationByGUID(String userId,
                                                     String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getAPIOperationByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/{2}";

        APIOperationResponse restResult = restClient.callAPIOperationGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 guid);

        return restResult.getElement();
    }

    /*
     * An API Operation may support a header, a request and a response parameter list depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation's Parameter list.  This describes the structure of the payload supported by
     * the API's operation. The structure of this API Operation is added using API Parameter schema attributes.   These parameters can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of an APIOperation
     * @param parameterListType is this a header, request or response
     * @param properties properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPIParameterList(String                     userId,
                                         String                     apiManagerGUID,
                                         String                     apiManagerName,
                                         String                     apiOperationGUID,
                                         APIParameterListType parameterListType,
                                         APIParameterListProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                     = "createAPIParameterList";
        final String parentElementGUIDParameterName = "apiOperationGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";
        final String listTypeParameterName          = "parameterListType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateEnum(parameterListType, listTypeParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/{2}/api-parameter-lists/{3}";

        APIParameterListRequestBody requestBody = new APIParameterListRequestBody(properties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiOperationGUID,
                                                                  parameterListType);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an API Parameter List using an existing API Parameter List as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the API Operation where the API Parameter List is located
     * @param parameterListType is this a header, request or response
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API Parameter List
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createAPIParameterListFromTemplate(String               userId,
                                                     String               apiManagerGUID,
                                                     String               apiManagerName,
                                                     String               templateGUID,
                                                     String               apiOperationGUID,
                                                     APIParameterListType parameterListType,
                                                     TemplateProperties   templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                     = "createAPIParameterListFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "apiOperationGUID";
        final String propertiesParameterName        = "templateProperties";
        final String qualifiedNameParameterName     = "qualifiedName";
        final String listTypeParameterName          = "parameterListType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateEnum(parameterListType, listTypeParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/{2}/api-parameter-lists/{3}/from-template/{4}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiOperationGUID,
                                                                  parameterListType,
                                                                  templateGUID);

        return restResult.getGUID();
    }



    /**
     * Update the metadata element representing an API Parameter List.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateAPIParameterList(String                     userId,
                                       String                     apiManagerGUID,
                                       String                     apiManagerName,
                                       String                     apiParameterListGUID,
                                       boolean                    isMergeUpdate,
                                       APIParameterListProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName               = "updateAPIParameterList";
        final String elementGUIDParameterName = "apiParameterListGUID";
        final String propertiesParameterName  = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiParameterListGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/api-parameter-lists/{2}?isMergeUpdate={3}";

        APIParameterListRequestBody requestBody = new APIParameterListRequestBody(properties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiParameterListGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove an API Parameter List and all of its parameters.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeAPIParameterList(String userId,
                                       String apiManagerGUID,
                                       String apiManagerName,
                                       String apiParameterListGUID,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "removeAPIParameterList";
        final String elementGUIDParameterName    = "apiParameterListGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiParameterListGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/api-parameter-lists/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        apiParameterListGUID,
                                        qualifiedName);
    }


    /**
     * Retrieve the list of API Parameter List metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIParameterListElement> findAPIParameterLists(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                = "findAPIParameterLists";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/api-parameter-lists/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        APIParameterListsResponse restResult = restClient.callAPIParameterListsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           requestBody,
                                                                                           serverName,
                                                                                           userId,
                                                                                           startFrom,
                                                                                           validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of API Parameter Lists associated with an API Operation.
     *
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the API Operation to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Parameter Lists associated with the requested API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIParameterListElement> getParameterListsForAPIOperation(String userId,
                                                                          String apiOperationGUID,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                     = "getParameterListsForAPIOperation";
        final String parentElementGUIDParameterName = "apiOperationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiOperationGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations/api-parameter-lists?startFrom={3}&pageSize={4}";

        APIParameterListsResponse restResult = restClient.callAPIParameterListsGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           apiOperationGUID,
                                                                                           startFrom,
                                                                                           validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of API Parameter List metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<APIParameterListElement> getAPIParameterListsByName(String userId,
                                                                    String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName        = "getAPIParameterListsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/api-parameter-lists/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        APIParameterListsResponse restResult = restClient.callAPIParameterListsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            requestBody,
                                                                                            serverName,
                                                                                            userId,
                                                                                            startFrom,
                                                                                            validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the API Parameter List metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public APIParameterListElement getAPIParameterListByGUID(String userId,
                                                             String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName        = "getAPIOperationByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/api-parameter-lists/{2}";

        APIParameterListResponse restResult = restClient.callAPIParameterListGetRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      serverName,
                                                                                      userId,
                                                                                      guid);

        return restResult.getElement();
    }



    /* ===============================================================================
     * An API Parameter List typically contains many API parameters, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a API parameter.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the API parameter is nested underneath
     * @param apiParameterProperties properties for the API parameter
     *
     * @return unique identifier of the new metadata element for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameter(String                 userId,
                                     String                 apiManagerGUID,
                                     String                 apiManagerName,
                                     String                 schemaElementGUID,
                                     APIParameterProperties apiParameterProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "createAPIParameter";
        final String parentElementGUIDParameterName = "schemaElementGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(apiParameterProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(apiParameterProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        return super.createSchemaAttribute(userId, apiManagerGUID, apiManagerName, schemaElementGUID, getSchemaAttributeProperties(apiParameterProperties));
    }


    /**
     * Create a new metadata element to represent a API parameter using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the API parameter is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterFromTemplate(String             userId,
                                                 String             apiManagerGUID,
                                                 String             apiManagerName,
                                                 String             schemaElementGUID,
                                                 String             templateGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return super.createSchemaAttributeFromTemplate(userId, apiManagerGUID, apiManagerName, schemaElementGUID, templateGUID, templateProperties);
    }


    /**
     * Connect a schema type to an API parameter.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of relationship to create
     * @param apiParameterGUID unique identifier of the API parameter
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupSchemaType(String  userId,
                                String  apiManagerGUID,
                                String  apiManagerName,
                                String  relationshipTypeName,
                                String  apiParameterGUID,
                                String  schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        super.setupSchemaType(userId, apiManagerGUID, apiManagerName, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
    }


    /**
     * Convert the properties for an API Parameter into the properties for a schema attribute.
     *
     * @param apiParameterProperties supplied properties
     * @return converted properties
     */
    private SchemaAttributeProperties getSchemaAttributeProperties(APIParameterProperties apiParameterProperties)
    {
        SchemaAttributeProperties properties = new SchemaAttributeProperties(apiParameterProperties);

        if (apiParameterProperties.getParameterType() != null)
        {
            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put("parameterType", apiParameterProperties.getParameterType());

            properties.setExtendedProperties(extendedProperties);
        }

        return properties;
    }


    /**
     * Update the properties of the metadata element representing an API parameter.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterGUID unique identifier of the API parameter to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param apiParameterProperties new properties for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIParameter(String                 userId,
                                   String                 apiManagerGUID,
                                   String                 apiManagerName,
                                   String                 apiParameterGUID,
                                   boolean                isMergeUpdate,
                                   APIParameterProperties apiParameterProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                     = "updateAPIParameter";
        final String elementGUIDParameterName       = "apiParameterGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiParameterGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(apiParameterProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(apiParameterProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        super.updateSchemaAttribute(userId, apiManagerGUID, apiManagerName, apiParameterGUID, isMergeUpdate, getSchemaAttributeProperties(apiParameterProperties));
    }


    /**
     * Remove the metadata element representing a API parameter.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIParameter(String userId,
                                   String apiManagerGUID,
                                   String apiManagerName,
                                   String apiParameterGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        super.removeSchemaAttribute(userId, apiManagerGUID, apiManagerName, apiParameterGUID);
    }


    /**
     * Convert a list of schema attribute elements into a list of API Parameters.
     *
     * @param schemaAttributeElements returned list
     * @return return reformatted list
     */
    private List<APIParameterElement> getAPIParametersFromSchemaAttributes(List<SchemaAttributeElement> schemaAttributeElements)
    {
        if (schemaAttributeElements != null)
        {
            List<APIParameterElement> apiParameterElements = new ArrayList<>();

            for (SchemaAttributeElement schemaAttributeElement : schemaAttributeElements)
            {
                if (schemaAttributeElement != null)
                {
                    APIParameterElement apiParameterElement = new APIParameterElement(schemaAttributeElement);

                    apiParameterElements.add(apiParameterElement);
                }
            }

            if (! apiParameterElements.isEmpty())
            {
                return apiParameterElements;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of API parameter metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param typeName optional type name for the API parameter - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> findAPIParameters(String userId,
                                                       String searchString,
                                                       String typeName,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        List<SchemaAttributeElement> schemaAttributeElements = super.findSchemaAttributes(userId, searchString, typeName, startFrom, pageSize);

        return getAPIParametersFromSchemaAttributes(schemaAttributeElements);
    }


    /**
     * Retrieve the list of API parameters associated with a parameter list or nested underneath another parameter.
     *
     * @param userId calling user
     * @param parentElementGUID unique identifier of the element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> getNestedAPIParameters(String userId,
                                                            String parentElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        List<SchemaAttributeElement> schemaAttributeElements = super.getNestedAttributes(userId, parentElementGUID, startFrom, pageSize);

        return getAPIParametersFromSchemaAttributes(schemaAttributeElements);
    }


    /**
     * Retrieve the list of API parameter metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> getAPIParametersByName(String userId,
                                                            String name,
                                                            String typeName,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        List<SchemaAttributeElement> schemaAttributeElements = super.getSchemaAttributesByName(userId, name, typeName, startFrom, pageSize);

        return getAPIParametersFromSchemaAttributes(schemaAttributeElements);
    }


    /**
     * Retrieve the API parameter metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param apiParameterGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterElement getAPIParameterByGUID(String userId,
                                                     String apiParameterGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        SchemaAttributeElement schemaAttributeElement = super.getSchemaAttributeByGUID(userId, apiParameterGUID);

        if (schemaAttributeElement != null)
        {
            return new APIParameterElement(schemaAttributeElement);
        }
        else
        {
            return null;
        }
    }
}
