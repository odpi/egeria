/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.APIManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIOperationElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIParameterListElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * APIManagerClient is the client for managing apis from an Event Manager.
 */
public class APIManagerClient extends SchemaManagerClient implements APIManagerInterface
{
    private static final String apiURLTemplatePrefix       = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/apis";
    private static final String defaultSchemaAttributeName = "APIParameter";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public APIManagerClient(String                serverName,
                            String                serverPlatformURLRoot,
                            DataManagerRESTClient restClient,
                            int                   maxPageSize,
                            AuditLog              auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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

        APIRequestBody requestBody = new APIRequestBody(apiProperties);
        
        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
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
        
        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);
        
        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  apiManagerIsHome);

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
        invalidParameterHandler.validateName(apiProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

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
        final String apiManagerGUIDParameterName = "apiManagerGUID";
        final String apiManagerNameParameterName = "apiManagerName";
        final String elementGUIDParameterName    = "apiGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(apiManagerGUID, apiManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(apiManagerName, apiManagerNameParameterName, methodName);
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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/by-search-string/{2}?startFrom={3}&pageSize={4}";

        APIsResponse restResult = restClient.callAPIsGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 searchString,
                                                                 startFrom,
                                                                 validatedPageSize);

        return restResult.getElementList();
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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/by-name/{2}?startFrom={3}&pageSize={4}";

        APIsResponse restResult = restClient.callAPIsGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 name,
                                                                 startFrom,
                                                                 validatedPageSize);

        return restResult.getElementList();
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

        return restResult.getElementList();
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
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
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
                                     boolean                apiManagerIsHome,
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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations?apiManagerIsHome={3}";

        APIOperationRequestBody requestBody = new APIOperationRequestBody(properties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiGUID,
                                                                  apiManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a API operation using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager
     * @param apiManagerName unique name of software server capability representing the API manager
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
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
                                                 boolean            apiManagerIsHome,
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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/{2}/api-operations/from-template/{3}?apiManagerIsHome={4}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(apiManagerGUID);
        requestBody.setExternalSourceName(apiManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  apiGUID,
                                                                  templateGUID,
                                                                  apiManagerIsHome);

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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/by-search-string/{2}?startFrom={3}&pageSize={4}";

        APIOperationsResponse restResult = restClient.callAPIOperationsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   searchString,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElementList();
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

        return restResult.getElementList();
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

        final String urlTemplate = serverPlatformURLRoot + apiURLTemplatePrefix + "/api-operations/by-name/{2}?startFrom={3}&pageSize={4}";

        APIOperationsResponse restResult = restClient.callAPIOperationsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   name,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElementList();
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
     * A API Operation may support a header, a request and a response parameter list of operations depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation's Parameter list.  This describes the structure of the payload supported by
     * the API's operation. The structure of this API Operation is added using API Parameter schema attributes.   These parameters can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param apiOperationGUID unique identifier of an APIOperation
     * @param parameterListType is this is a header, request of response
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
                                         boolean                    apiManagerIsHome,
                                         String                     apiOperationGUID,
                                         APIParameterListType       parameterListType,
                                         APIParameterListProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return null;
    }


    /**
     * Create a new metadata element to represent a an API Parameter List using an existing API Parameter List as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiManagerIsHome should the API operation be marked as owned by the API manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the API Operation where the API Parameter List is located
     * @param parameterListType is this is a header, request of response
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
                                                     boolean              apiManagerIsHome,
                                                     String               templateGUID,
                                                     String               apiOperationGUID,
                                                     APIParameterListType parameterListType,
                                                     TemplateProperties   templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return null;
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
        return null;
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
        return null;
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
        return null;
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
        return null;
    }
}
