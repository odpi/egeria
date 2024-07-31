/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.DeploymentManagementInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.api.ServerPurposeManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetRelationshipElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DeploymentElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeploymentProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.AssetRelationshipsResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ITInfrastructureClientBase supports the APIs to maintain assets and their related objects.  It is called from the specific clients
 * that manage the specializations of asset.
 */
public abstract class ITInfrastructureClientBase implements ServerPurposeManagerInterface, DeploymentManagementInterface
{
    static final String baseURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/it-infrastructure/users/{1}";
    static final String referencableTypeName  = "Referenceable";
    static final String assetTypeName         = "Asset";
    static final String itAssetTypeName        = "ITInfrastructure";
    static final String deployedOnRelationship = "DeployedOn";

    private static final String assetURLTemplatePrefix = baseURLTemplatePrefix + "/assets";

    String   serverName;               /* Initialized in constructor */
    String   serverPlatformURLRoot;    /* Initialized in constructor */

    ITInfrastructureRESTClient  restClient;               /* Initialized in constructor */
    private final NullRequestBody nullRequestBody = new NullRequestBody();

    InvalidParameterHandler     invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    ITInfrastructureClientBase(String   serverName,
                               String   serverPlatformURLRoot,
                               AuditLog auditLog,
                               int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    ITInfrastructureClientBase(String serverName,
                               String serverPlatformURLRoot,
                               int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    ITInfrastructureClientBase(String serverName,
                               String serverPlatformURLRoot,
                               String userId,
                               String password,
                               int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    ITInfrastructureClientBase(String   serverName,
                               String   serverPlatformURLRoot,
                               String   userId,
                               String   password,
                               AuditLog auditLog,
                               int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new ITInfrastructureRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    ITInfrastructureClientBase(String                     serverName,
                               String                     serverPlatformURLRoot,
                               ITInfrastructureRESTClient restClient,
                               int                        maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
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
     * Create a new metadata element to represent an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param assetProperties properties to store
     * @param initialStatus optional initial status
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAsset(String          userId,
                       String          infrastructureManagerGUID,
                       String          infrastructureManagerName,
                       boolean         infrastructureManagerIsHome,
                       AssetProperties assetProperties,
                       ElementStatus initialStatus,
                       String          methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "?infrastructureManagerIsHome={2}";

        AssetRequestBody requestBody = new AssetRequestBody(assetProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setInitialStatus(initialStatus);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAssetFromTemplate(String             userId,
                                   String             infrastructureManagerGUID,
                                   String             infrastructureManagerName,
                                   boolean            infrastructureManagerIsHome,
                                   String             templateGUID,
                                   TemplateProperties templateProperties,
                                   String             methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/from-template/{2}?infrastructureManagerIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody();

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
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param assetProperties new properties for this element
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAsset(String          userId,
                     String          infrastructureManagerGUID,
                     String          infrastructureManagerName,
                     String          assetGUID,
                     boolean         isMergeUpdate,
                     AssetProperties assetProperties,
                     String          methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String elementGUIDParameterName    = "assetGUID";
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

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
     * Update the status of the metadata element representing an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to update
     * @param newStatus new status for the process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAssetStatus(String        userId,
                           String        infrastructureManagerGUID,
                           String        infrastructureManagerName,
                           String        assetTypeName,
                           String        assetGUID,
                           ElementStatus newStatus,
                           String        methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String elementGUIDParameterName    = "assetGUID";
        final String statusParameterName         = "newStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(newStatus, statusParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/status";

        ElementStatusRequestBody requestBody = new ElementStatusRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setElementStatus(newStatus);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Create a relationship between an asset and a related asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param relationshipProperties properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupRelatedAsset(String              userId,
                           String              infrastructureManagerGUID,
                           String              infrastructureManagerName,
                           boolean             infrastructureManagerIsHome,
                           String              assetTypeName,
                           String              assetGUID,
                           String              relationshipTypeName,
                           String              relatedAssetTypeName,
                           String              relatedAssetGUID,
                           Date                effectiveFrom,
                           Date                effectiveTo,
                           Map<String, Object> relationshipProperties,
                           String              methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String assetGUIDParameterName        = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(relatedAssetGUID, relatedAssetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/" + relationshipTypeName + "/" + relatedAssetTypeName + "/{3}?infrastructureManagerIsHome={4}";

        AssetExtensionsRequestBody requestBody = new AssetExtensionsRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(relationshipProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        relatedAssetGUID,
                                        infrastructureManagerIsHome);
    }


    /**
     * Update the properties of a relationship between an asset and a related asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param relationshipGUID unique identifier of the relationship
     * @param relationshipTypeName name of the relationship type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAssetRelationship(String              userId,
                                 String              infrastructureManagerGUID,
                                 String              infrastructureManagerName,
                                 String              relationshipGUID,
                                 String              relationshipTypeName,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 boolean             isMergeUpdate,
                                 Map<String, Object> relationshipProperties,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String relationshipGUIDParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/relationships/" + relationshipTypeName + "/{2}/update?isMergeUpdate={3}";

        AssetExtensionsRequestBody requestBody = new AssetExtensionsRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(relationshipProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove a relationship between an asset and a related asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param effectiveTime effective time of the relationship to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearRelatedAsset(String userId,
                           String infrastructureManagerGUID,
                           String infrastructureManagerName,
                           String assetTypeName,
                           String assetGUID,
                           String relationshipTypeName,
                           String relatedAssetTypeName,
                           String relatedAssetGUID,
                           Date   effectiveTime,
                           String methodName) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        final String assetGUIDParameterName        = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(relatedAssetGUID, relatedAssetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/" + relationshipTypeName + "/" + relatedAssetTypeName + "/{3}/delete";

        org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeQueryRequestBody requestBody = new org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeQueryRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        relatedAssetGUID);
    }


    /**
     * Add a classification to an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param classificationProperties properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addClassification(String              userId,
                           String              infrastructureManagerGUID,
                           String              infrastructureManagerName,
                           boolean             infrastructureManagerIsHome,
                           String              assetTypeName,
                           String              assetGUID,
                           String              classificationName,
                           Date                effectiveFrom,
                           Date                effectiveTo,
                           Map<String, Object> classificationProperties,
                           String              methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/classify/" + classificationName + "?infrastructureManagerIsHome={3}";

        AssetExtensionsRequestBody requestBody = new AssetExtensionsRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(classificationProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        infrastructureManagerIsHome);
    }


    /**
     * Update the properties of a classification for an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param classificationProperties properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateClassification(String              userId,
                              String              infrastructureManagerGUID,
                              String              infrastructureManagerName,
                              String              assetTypeName,
                              String              assetGUID,
                              String              classificationName,
                              Date                effectiveFrom,
                              Date                effectiveTo,
                              boolean             isMergeUpdate,
                              Map<String, Object> classificationProperties,
                              String              methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/reclassify/" + classificationName + "?isMergeUpdate={3}";

        AssetExtensionsRequestBody requestBody = new AssetExtensionsRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(classificationProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove a classification from an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveTime effective time of the classification to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearClassification(String userId,
                             String infrastructureManagerGUID,
                             String infrastructureManagerName,
                             String assetTypeName,
                             String assetGUID,
                             String classificationName,
                             Date   effectiveTime,
                             String methodName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/declassify/" + classificationName;

        EffectiveTimeQueryRequestBody requestBody = new EffectiveTimeQueryRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Update the zones for the asset asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishAsset(String userId,
                      String assetGUID,
                      String methodName) throws InvalidParameterException,
                                                UserNotAuthorizedException,
                                                PropertyServerException
    {
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
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawAsset(String userId,
                       String assetGUID,
                       String methodName) throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException
    {
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
     * Remove the metadata element representing an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAsset(String userId,
                     String infrastructureManagerGUID,
                     String infrastructureManagerName,
                     String assetGUID,
                     String methodName) throws InvalidParameterException,
                                               UserNotAuthorizedException,
                                               PropertyServerException
    {
        final String elementGUIDParameterName  = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/{2}/delete";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<AssetElement> findAssets(String userId,
                                  String searchString,
                                  String assetTypeName,
                                  Date   effectiveTime,
                                  int    startFrom,
                                  int    pageSize,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElements();
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<AssetElement> getAssetsByName(String userId,
                                       String name,
                                       String assetTypeName,
                                       Date   effectiveTime,
                                       int    startFrom,
                                       int    pageSize,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElements();
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<AssetElement> getAssetsByDeployedImplementationType(String userId,
                                                             String name,
                                                             String assetTypeName,
                                                             Date   effectiveTime,
                                                             int    startFrom,
                                                             int    pageSize,
                                                             String methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/by-deployed-implementation-type?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElements();
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<AssetElement> getAssetsForInfrastructureManager(String userId,
                                                         String infrastructureManagerGUID,
                                                         String infrastructureManagerName,
                                                         String assetTypeName,
                                                         Date   effectiveTime,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         String methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String infrastructureManagerGUIDParameterName = "infrastructureManagerGUID";
        final String infrastructureManagerNameParameterName = "infrastructureManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(infrastructureManagerGUID, infrastructureManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(infrastructureManagerName, infrastructureManagerNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/infrastructure-managers/{2}/{3}/assets/" + assetTypeName + "?startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetElementsResponse restResult = restClient.callAssetElementsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    infrastructureManagerGUID,
                                                                                    infrastructureManagerName,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    AssetElement getAssetByGUID(String userId,
                                String assetTypeName,
                                String guid,
                                Date   effectiveTime,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}";

        AssetElementResponse restResult = restClient.callAssetPostRESTCall(methodName,
                                                                    urlTemplate,
                                                                    requestBody,
                                                                    serverName,
                                                                    userId,
                                                                    guid);

        return restResult.getElement();
    }



    /**
     * Return the list of relationships between assets.
     *
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type of retrieved assets
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<AssetRelationshipElement> getAssetRelationships(String userId,
                                                         String assetTypeName,
                                                         String assetGUID,
                                                         int    startingEnd,
                                                         String relationshipTypeName,
                                                         String relatedAssetTypeName,
                                                         Date   effectiveTime,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         String methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/" + relationshipTypeName + "/" + relatedAssetTypeName + "/relationships?startingEnd={3}&startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        AssetRelationshipsResponse restResult = restClient.callAssetRelationshipsPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              userId,
                                                                                              assetGUID,
                                                                                              startingEnd,
                                                                                              startFrom,
                                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of assets linked by another asset.
     *
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type of retrieved assets
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedAssetElement> getRelatedAssets(String userId,
                                               String assetTypeName,
                                               String assetGUID,
                                               int    startingEnd,
                                               String relationshipTypeName,
                                               String relatedAssetTypeName,
                                               Date   effectiveTime,
                                               int    startFrom,
                                               int    pageSize,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetURLTemplatePrefix + "/" + assetTypeName + "/{2}/" + relationshipTypeName + "/" + relatedAssetTypeName + "?startingEnd={3}&startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        RelatedAssetsResponse restResult = restClient.callRelatedAssetsPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       assetGUID,
                                                                                       startingEnd,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElements();
    }


    /*
     * Server purposes
     */


    /**
     * Add a Server Purpose classification to an IT asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param itAssetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addServerPurpose(String              userId,
                                 String              infrastructureManagerGUID,
                                 String              infrastructureManagerName,
                                 boolean             infrastructureManagerIsHome,
                                 String              itAssetGUID,
                                 String              classificationName,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "addServerPurpose";

        this.addClassification(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, itAssetTypeName, itAssetGUID, classificationName, effectiveFrom, effectiveTo, classificationProperties, methodName);
    }


    /**
     * Update the properties of a classification for an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateServerPurpose(String              userId,
                                    String              infrastructureManagerGUID,
                                    String              infrastructureManagerName,
                                    String              assetTypeName,
                                    String              assetGUID,
                                    String              classificationName,
                                    Date                effectiveFrom,
                                    Date                effectiveTo,
                                    boolean             isMergeUpdate,
                                    Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "updateServerPurpose";

        this.updateClassification(userId, infrastructureManagerGUID, infrastructureManagerName, assetTypeName, assetGUID, classificationName, effectiveFrom, effectiveTo, isMergeUpdate, classificationProperties, methodName);
    }


    /**
     * Remove a server purpose classification.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveTime effective time of the classification to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearServerPurpose(String userId,
                                   String infrastructureManagerGUID,
                                   String infrastructureManagerName,
                                   String assetTypeName,
                                   String assetGUID,
                                   String classificationName,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearServerPurpose";

        this.clearClassification(userId, infrastructureManagerGUID, infrastructureManagerName, assetTypeName, assetGUID, classificationName, effectiveTime, methodName);
    }


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the relationship be marked as owned by the infrastructure manager so others can not update?
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param deploymentProperties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void deployITAsset(String               userId,
                              String               infrastructureManagerGUID,
                              String               infrastructureManagerName,
                              boolean              infrastructureManagerIsHome,
                              String               itAssetGUID,
                              String               destinationGUID,
                              DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "deployITAsset";

        Map<String, Object> propertyMap   = null;
        Date                effectiveFrom = null;
        Date                effectiveTo   = null;

        if (deploymentProperties != null)
        {
            propertyMap = deploymentProperties.cloneToMap();
            effectiveFrom = deploymentProperties.getEffectiveFrom();
            effectiveTo = deploymentProperties.getEffectiveTo();
        }

        this.setupRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, itAssetTypeName, itAssetGUID, deployedOnRelationship, assetTypeName, destinationGUID, effectiveFrom, effectiveTo, propertyMap, methodName);
    }


    /**
     * Update a deployment relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param deploymentGUID unique identifier of the relationship
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param deploymentProperties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateITAssetDeployment(String               userId,
                                        String               infrastructureManagerGUID,
                                        String               infrastructureManagerName,
                                        String               deploymentGUID,
                                        boolean              isMergeUpdate,
                                        DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "updateITAssetDeployment";

        Map<String, Object> propertyMap   = null;
        Date                effectiveFrom = null;
        Date                effectiveTo   = null;

        if (deploymentProperties != null)
        {
            propertyMap = deploymentProperties.cloneToMap();
            effectiveFrom = deploymentProperties.getEffectiveFrom();
            effectiveTo = deploymentProperties.getEffectiveTo();
        }

        this.updateAssetRelationship(userId, infrastructureManagerGUID, infrastructureManagerName, deploymentGUID,
                                     deployedOnRelationship, effectiveFrom, effectiveTo, isMergeUpdate, propertyMap, methodName);
    }



    /**
     * Remove a deployment relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param effectiveTime time when the deployment is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearDeployment(String userId,
                                String infrastructureManagerGUID,
                                String infrastructureManagerName,
                                String itAssetGUID,
                                String destinationGUID,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "clearDeployment";

        this.clearRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, itAssetTypeName, itAssetGUID,
                               deployedOnRelationship, assetTypeName, destinationGUID, effectiveTime, methodName);
    }


    /**
     * Return the list of assets deployed on a particular destination.
     *
     * @param userId calling user
     * @param destinationGUID unique identifier of the destination asset to query
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
    @Override
    public List<DeploymentElement> getDeployedITAssets(String userId,
                                                       String destinationGUID,
                                                       Date   effectiveTime,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getDeployedITAssets";

        return this.convertRelatedAssetElements(this.getRelatedAssets(userId, assetTypeName, destinationGUID, 2, deployedOnRelationship, itAssetTypeName, effectiveTime, startFrom, pageSize, methodName));
    }




    /**
     * Return the list of destinations that a particular IT infrastructure asset is deployed to.
     *
     * @param userId calling user
     * @param itAssetGUID unique identifier of the IT infrastructure asset to query
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
    @Override
    public List<DeploymentElement> getDeploymentDestinations(String userId,
                                                             String itAssetGUID,
                                                             Date   effectiveTime,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getDeploymentDestinations";

        return this.convertRelatedAssetElements(this.getRelatedAssets(userId, itAssetTypeName, itAssetGUID, 1, deployedOnRelationship, assetTypeName, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Convert a list of RelatedAssetElements into a list of HostElements.
     *
     * @param relatedAssetElements returned assets
     * @return result for caller
     */
    private List<DeploymentElement> convertRelatedAssetElements(List<RelatedAssetElement> relatedAssetElements)
    {
        if (relatedAssetElements != null)
        {
            List<DeploymentElement> deploymentElements = new ArrayList<>();

            for (RelatedAssetElement relatedAssetElement : relatedAssetElements)
            {
                DeploymentElement element = new DeploymentElement();

                element.setElementHeader(relatedAssetElement.getElementHeader());
                element.setDeploymentProperties(new DeploymentProperties(relatedAssetElement.getProperties(),
                                                                         relatedAssetElement.getEffectiveFrom(),
                                                                         relatedAssetElement.getEffectiveTo()));
                element.setAssetElement(relatedAssetElement.getRelatedAsset());
                deploymentElements.add(element);
            }

            if (! deploymentElements.isEmpty())
            {
                return deploymentElements;
            }
        }

        return null;
    }


}
