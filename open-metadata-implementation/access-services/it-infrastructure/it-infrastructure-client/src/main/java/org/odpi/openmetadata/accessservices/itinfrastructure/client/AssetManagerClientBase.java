/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.RelatedAssetsRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AssetManagerClientBase supports the APIs to maintain assets and their related objects.  It is called from the specific clients
 * that manage the specializations of asset.
 */
public abstract class AssetManagerClientBase
{
    private static final String assetURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}/assets";

    private String   serverName;               /* Initialized in constructor */
    private String   serverPlatformURLRoot;    /* Initialized in constructor */

    private InvalidParameterHandler     invalidParameterHandler = new InvalidParameterHandler();
    private ITInfrastructureRESTClient  restClient;               /* Initialized in constructor */

    private NullRequestBody nullRequestBody = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    AssetManagerClientBase(String   serverName,
                           String   serverPlatformURLRoot,
                           AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    AssetManagerClientBase(String serverName,
                           String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot);
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
    AssetManagerClientBase(String serverName,
                           String serverPlatformURLRoot,
                           String userId,
                           String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password);
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
    AssetManagerClientBase(String   serverName,
                           String   serverPlatformURLRoot,
                           String   userId,
                           String   password,
                           AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetManagerClientBase(String                     serverName,
                                  String                     serverPlatformURLRoot,
                                  ITInfrastructureRESTClient restClient,
                                  int                        maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }



    /* =====================================================================================================================
     * The asset describes the computer or container that provides the operating system for the platforms.
     */


    /**
     * Create a new metadata element to represent a asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param assetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAsset(String         userId,
                              String         infrastructureManagerGUID,
                              String         infrastructureManagerName,
                              boolean        infrastructureManagerIsHome,
                              AssetProperties assetProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "createAsset";
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "?infrastructureManagerIsHome={2}";

        AssetRequestBody requestBody = new AssetRequestBody(assetProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a asset using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAssetFromTemplate(String             userId,
                                          String             infrastructureManagerGUID,
                                          String             infrastructureManagerName,
                                          boolean            infrastructureManagerIsHome,
                                          String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "createAssetFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/from-template/{2}?infrastructureManagerIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param assetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String         userId,
                            String         infrastructureManagerGUID,
                            String         infrastructureManagerName,
                            String         assetGUID,
                            boolean        isMergeUpdate,
                            AssetProperties assetProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "updateAsset";
        final String elementGUIDParameterName    = "assetGUID";
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        AssetRequestBody requestBody = new AssetRequestBody(assetProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        isMergeUpdate);
    }



    /**
     * Create a relationship between a asset and a asseted asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetGUID unique identifier of the related asset
     * @param relationshipProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupRelatedAsset(String              userId,
                                  String              infrastructureManagerGUID,
                                  String              infrastructureManagerName,
                                  String              assetGUID,
                                  String              relationshipTypeName,
                                  String              relatedAssetGUID,
                                  Map<String, Object> relationshipProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                    = "setupRelatedAsset";
        final String assetGUIDParameterName        = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(relatedAssetGUID, relatedAssetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + relationshipTypeName + "/{3}";

        RelatedAssetsRequestBody requestBody = new RelatedAssetsRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(relationshipProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        relatedAssetGUID);
    }


    /**
     * Remove a relationship between a asset and a related asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetGUID unique identifier of the related asset
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearRelatedAsset(String userId,
                                  String infrastructureManagerGUID,
                                  String infrastructureManagerName,
                                  String assetGUID,
                                  String relationshipTypeName,
                                  String relatedAssetGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "clearRelatedAsset";
        final String assetGUIDParameterName       = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(relatedAssetGUID, relatedAssetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + relationshipTypeName + "/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        relatedAssetGUID);
    }


    /**
     * Update the zones for the asset asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishAsset(String userId,
                             String assetGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String methodName               = "publishAsset";
        final String elementGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the zones for the asset asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the asset is first created).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawAsset(String userId,
                              String assetGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName               = "withdrawAsset";
        final String elementGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Remove the metadata element representing a asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAsset(String userId,
                            String infrastructureManagerGUID,
                            String infrastructureManagerName,
                            String assetGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        final String methodName = "removeAsset";
        final String elementGUIDParameterName  = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }



    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param assetTypeName name of type for the asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement> findAssets(String userId,
                                         String searchString,
                                         String assetTypeName,
                                         Date   effectiveTime,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                = "findAssets";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param assetTypeName name of type for the asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement> getAssetsByName(String userId,
                                              String name,
                                              String assetTypeName,
                                              Date   effectiveTime,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName        = "getAssetsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + assetTypeName + "?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of assets created by this caller.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement> getAssetsForInfrastructureManager(String userId,
                                                                String infrastructureManagerGUID,
                                                                String infrastructureManagerName,
                                                                String assetTypeName,
                                                                Date   effectiveTime,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getAssetsForInfrastructureManager";
        final String infrastructureManagerGUIDParameterName = "infrastructureManagerGUID";
        final String infrastructureManagerNameParameterName = "infrastructureManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(infrastructureManagerGUID, infrastructureManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(infrastructureManagerName, infrastructureManagerNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + assetTypeName + "/infrastructureManagers/{2}/{3}/assets?startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            infrastructureManagerGUID,
                                                                            infrastructureManagerName,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public AssetElement getAssetByGUID(String userId,
                                       String assetTypeName,
                                       String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "getAssetByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + assetTypeName + "/{2}";

        AssetResponse restResult = restClient.callAssetGetRESTCall(methodName,
                                                                   urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   guid);

        return restResult.getElement();
    }


    /**
     * Return the list of assets linked by another asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement> getRelatedAssets(String userId,
                                               String assetGUID,
                                               int    startingEnd,
                                               String relationshipTypeName,
                                               Date   effectiveTime,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "getRelatedAssets";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/" + relationshipTypeName + "?startingEnd={3}&startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetListResponse restResult = restClient.callAssetListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            assetGUID,
                                                                            startingEnd,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }
}
