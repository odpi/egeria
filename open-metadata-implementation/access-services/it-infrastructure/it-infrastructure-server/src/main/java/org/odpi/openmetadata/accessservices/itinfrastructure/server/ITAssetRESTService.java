/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementStatus;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetExtensionsRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EffectiveTimeMetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ElementStatusRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.RelatedAssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.RelatedAssetHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * ITAssetRESTService is the server-side of AssetManagerClientBase.  It is called from the specific clients that manage the
 * specializations of IT Infrastructure assets.
 */
public class ITAssetRESTService
{
    private static ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();
    private static RESTCallLogger                  restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(ITAssetRESTService.class),
                                                                                    instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public ITAssetRESTService()
    {
    }



    /* =====================================================================================================================
     * The asset describes the computer or container that provides the operating system for the platforms.
     */


    /**
     * Create a new metadata element to represent a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAsset(String           serverName,
                                    String           userId,
                                    boolean          infrastructureManagerIsHome,
                                    AssetRequestBody requestBody)
    {
        final String methodName                  = "createAsset";
        final String capabilityGUIDParameterName = "infrastructureManagerGUID";
        final String assetGUIDParameterName      = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                String assetGUID;

                if (infrastructureManagerIsHome)
                {
                    assetGUID = handler.createAssetInRepository(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                this.getInstanceStatus(requestBody.getInitialStatus()),
                                                                methodName);

                    if ((assetGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     requestBody.getExternalSourceGUID(),
                                                     capabilityGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                     assetGUID,
                                                     assetGUIDParameterName,
                                                     OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                     false,
                                                     false,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }
                else
                {
                    assetGUID = handler.createAssetInRepository(userId,
                                                                null,
                                                                null,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                this.getInstanceStatus(requestBody.getInitialStatus()),
                                                                methodName);

                    if ((assetGUID != null) && (requestBody.getExternalSourceGUID() != null))
                    {
                        handler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     requestBody.getExternalSourceGUID(),
                                                     capabilityGUIDParameterName,
                                                     OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                     assetGUID,
                                                     assetGUIDParameterName,
                                                     OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                     false,
                                                     false,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                     null,
                                                     methodName);
                    }
                }

                if (assetGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                assetGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }

                response.setGUID(assetGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;

    }


    /**
     * Convert the ElementStatus to an InstanceStatus understood by the repository services.
     *
     * @param status status from caller
     * @return instance status
     */
    private InstanceStatus getInstanceStatus(ElementStatus status)
    {
        if (status != null)
        {
            switch (status)
            {
                case UNKNOWN:
                    return InstanceStatus.UNKNOWN;

                case DRAFT:
                    return InstanceStatus.DRAFT;

                case PREPARED:
                    return InstanceStatus.PREPARED;

                case PROPOSED:
                    return InstanceStatus.PROPOSED;

                case APPROVED:
                    return InstanceStatus.APPROVED;

                case REJECTED:
                    return InstanceStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return InstanceStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return InstanceStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return InstanceStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return InstanceStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return InstanceStatus.STANDBY;

                case ACTIVE:
                    return InstanceStatus.ACTIVE;

                case FAILED:
                    return InstanceStatus.FAILED;

                case DISABLED:
                    return InstanceStatus.DISABLED;

                case COMPLETE:
                    return InstanceStatus.COMPLETE;

                case DEPRECATED:
                    return InstanceStatus.DEPRECATED;

                case OTHER:
                    return InstanceStatus.OTHER;
            }
        }

        return InstanceStatus.ACTIVE;
    }


    /**
     * Create a new metadata element to represent a asset using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createAssetFromTemplate(String              serverName,
                                                String              userId,
                                                String              templateGUID,
                                                boolean             infrastructureManagerIsHome,
                                                TemplateRequestBody requestBody)
    {
        final String methodName                  = "createAssetFromTemplate";
        final String capabilityGUIDParameterName = "infrastructureManagerGUID";
        final String assetGUIDParameterName      = "assetGUID";
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String assetGUID = handler.addAssetFromTemplate(userId,
                                                                handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceGUID()),
                                                                handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceName()),
                                                                templateGUID,
                                                                templateGUIDParameterName,
                                                                OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                requestBody.getQualifiedName(),
                                                                qualifiedNameParameterName,
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                requestBody.getNetworkAddress(),
                                                                methodName);

                if ((assetGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 capabilityGUIDParameterName,
                                                 OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 null,
                                                 methodName);
                }


                response.setGUID(assetGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for this element
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAsset(String           serverName,
                                    String           userId,
                                    String           assetGUID,
                                    boolean          isMergeUpdate,
                                    AssetRequestBody requestBody)
    {
        final String methodName                  = "updateAsset";
        final String elementGUIDParameterName    = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    assetGUID,
                                    elementGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getDisplayName(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    requestBody.getExtendedProperties(),
                                    isMergeUpdate,
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                assetGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the status of the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to update
     * @param requestBody new status for the process
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAssetStatus(String                   serverName,
                                          String                   userId,
                                          String                   assetTypeName,
                                          String                   assetGUID,
                                          ElementStatusRequestBody requestBody)
    {
        final String methodName = "updateAssetStatus";
        final String assetGUIDParameterName = "assetGUID";
        final String statusParameterName = "newStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                handler.updateBeanStatusInRepository(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     assetGUID,
                                                     assetGUIDParameterName,
                                                     typeDef.getGUID(),
                                                     typeDef.getName(),
                                                     this.getInstanceStatus(requestBody.getElementStatus()),
                                                     statusParameterName,
                                                     methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between a asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param relatedAssetGUID unique identifier of the related asset
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupRelatedAsset(String                     serverName,
                                          String                     userId,
                                          String                     assetTypeName,
                                          String                     assetGUID,
                                          String                     relationshipTypeName,
                                          String                     relatedAssetTypeName,
                                          String                     relatedAssetGUID,
                                          boolean                    infrastructureManagerIsHome,
                                          AssetExtensionsRequestBody requestBody)
    {
        final String methodName                    = "setupRelatedAsset";
        final String assetGUIDParameterName        = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                TypeDef typeDef = handler.getTypeDefByName(relationshipTypeName, null);

                handler.linkElementToElement(userId,
                                             handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceGUID()),
                                             handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceName()),
                                             assetGUID,
                                             assetGUIDParameterName,
                                             assetTypeName,
                                             relatedAssetGUID,
                                             relatedAssetGUIDParameterName,
                                             relatedAssetTypeName,
                                             false,
                                             false,
                                             typeDef.getGUID(),
                                             typeDef.getName(),
                                             requestBody.getEffectiveFrom(),
                                             requestBody.getEffectiveTo(),
                                             requestBody.getProperties(),
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
     }



    /**
     * Update a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param relationshipTypeName name of the relationship type
     * @param relationshipGUID unique identifier of the asset
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing the just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param requestBody new properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAssetRelationship(String                     serverName,
                                                String                     userId,
                                                String                     relationshipTypeName,
                                                String                     relationshipGUID,
                                                boolean                    isMergeUpdate,
                                                AssetExtensionsRequestBody requestBody)
    {
        final String methodName                    = "updateAssetRelationship";
        final String relationshipGUIDParameterName = "relationshipGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateElementToElementLink(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   relationshipGUID,
                                                   relationshipGUIDParameterName,
                                                   relationshipTypeName,
                                                   false,
                                                   false,
                                                   isMergeUpdate,
                                                   requestBody.getEffectiveFrom(),
                                                   requestBody.getEffectiveTo(),
                                                   requestBody.getProperties(),
                                                   methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param requestBody unique identifier/name of software server capability representing the infrastructure manager
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearRelatedAsset(String                                 serverName,
                                          String                                 userId,
                                          String                                 assetTypeName,
                                          String                                 assetGUID,
                                          String                                 relationshipTypeName,
                                          String                                 relatedAssetTypeName,
                                          String                                 relatedAssetGUID,
                                          EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String methodName                    = "clearRelatedAsset";
        final String assetGUIDParameterName        = "assetGUID";
        final String relatedAssetGUIDParameterName = "relatedAssetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                TypeDef relationshipTypeDef = handler.getTypeDefByName(relationshipTypeName, null);
                TypeDef relatedAssetTypeDef = handler.getTypeDefByName(relatedAssetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 assetTypeName,
                                                 relatedAssetGUID,
                                                 relatedAssetGUIDParameterName,
                                                 relatedAssetTypeDef.getGUID(),
                                                 relatedAssetTypeName,
                                                 false,
                                                 false,
                                                 relationshipTypeDef.getGUID(),
                                                 relationshipTypeDef.getName(),
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of a classification for a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse addClassification(String                     serverName,
                                          String                     userId,
                                          String                     assetTypeName,
                                          String                     assetGUID,
                                          String                     classificationName,
                                          boolean                    infrastructureManagerIsHome,
                                          AssetExtensionsRequestBody requestBody)
    {
        final String methodName = "addClassification";

        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                TypeDef classificationTypeDef = handler.getTypeDefByName(classificationName, null);

                handler.setClassificationInRepository(userId,
                                                      handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceGUID()),
                                                      handler.getExternalSourceID(infrastructureManagerIsHome, requestBody.getExternalSourceName()),
                                                      assetGUID,
                                                      elementGUIDParameterName,
                                                      assetTypeName,
                                                      classificationTypeDef.getGUID(),
                                                      classificationTypeDef.getName(),
                                                      requestBody.getEffectiveFrom(),
                                                      requestBody.getEffectiveTo(),
                                                      false,
                                                      false,
                                                      false,
                                                      requestBody.getProperties(),
                                                      methodName);

            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of a classification for a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing the just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateClassification(String                     serverName,
                                             String                     userId,
                                             String                     assetTypeName,
                                             String                     assetGUID,
                                             String                     classificationName,
                                             boolean                    isMergeUpdate,
                                             AssetExtensionsRequestBody requestBody)
    {
        final String methodName = "updateClassification";

        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                TypeDef classificationTypeDef = handler.getTypeDefByName(classificationName, null);

                handler.setClassificationInRepository(userId,
                                                      requestBody.getExternalSourceGUID(),
                                                      requestBody.getExternalSourceName(),
                                                      assetGUID,
                                                      elementGUIDParameterName,
                                                      assetTypeName,
                                                      classificationTypeDef.getGUID(),
                                                      classificationTypeDef.getName(),
                                                      requestBody.getEffectiveFrom(),
                                                      requestBody.getEffectiveTo(),
                                                      false,
                                                      false,
                                                      isMergeUpdate,
                                                      requestBody.getProperties(),
                                                      methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a classification from an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearClassification(String                                 serverName,
                                            String                                 userId,
                                            String                                 assetTypeName,
                                            String                                 assetGUID,
                                            String                                 classificationName,
                                            EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String methodName                  = "clearClassification";
        final String elementGUIDParameterName    = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                TypeDef classificationTypeDef = handler.getTypeDefByName(classificationName, null);

                handler.removeClassificationFromRepository(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           assetGUID,
                                                           elementGUIDParameterName,
                                                           assetTypeName,
                                                           classificationTypeDef.getGUID(),
                                                           classificationTypeDef.getName(),
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the zones for the asset asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishAsset(String          serverName,
                                     String          userId,
                                     String          assetGUID,
                                     NullRequestBody requestBody)
    {
        final String methodName               = "publishAsset";
        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.publishAsset(userId, assetGUID, elementGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
     }


    /**
     * Update the zones for the asset asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the asset is first created).
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawAsset(String          serverName,
                                      String          userId,
                                      String          assetGUID,
                                      NullRequestBody requestBody)
    {
        final String methodName               = "withdrawAsset";
        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, assetGUID, elementGUIDParameterName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the infrastructure manager
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeAsset(String                    serverName,
                                    String                    userId,
                                    String                    assetGUID,
                                    MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeAsset";
        final String elementGUIDParameterName  = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               assetGUID,
                                               elementGUIDParameterName,
                                               OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                               OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
                                               methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public AssetListResponse findAssets(String                  serverName,
                                        String                  userId,
                                        String                  assetTypeName,
                                        int                     startFrom,
                                        int                     pageSize,
                                        SearchStringRequestBody requestBody)
    {
        final String methodName                = "findAssets";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetListResponse response = new AssetListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                if (typeDef == null)
                {
                    restExceptionHandler.handleBadType(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME, instanceHandler.getServiceName(), methodName);
                }
                else
                {
                    List<AssetElement> assets = handler.findAssets(userId,
                                                                   typeDef.getGUID(),
                                                                   typeDef.getName(),
                                                                   requestBody.getSearchString(),
                                                                   searchStringParameterName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName);

                    setUpVendorProperties(userId, assets, handler, methodName);
                    response.setElementList(assets);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public AssetListResponse getAssetsByName(String          serverName,
                                             String          userId,
                                             String          assetTypeName,
                                             int             startFrom,
                                             int             pageSize,
                                             NameRequestBody requestBody)
    {
        final String methodName        = "getAssetsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetListResponse response = new AssetListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                if (typeDef == null)
                {
                    restExceptionHandler.handleBadType(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME, instanceHandler.getServiceName(), methodName);
                }
                else
                {
                    List<AssetElement> assets = handler.getAssetsByName(userId,
                                                                        typeDef.getGUID(),
                                                                        typeDef.getName(),
                                                                        requestBody.getName(),
                                                                        nameParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        requestBody.getEffectiveTime(),
                                                                        methodName);

                    setUpVendorProperties(userId, assets, handler, methodName);
                    response.setElementList(assets);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of assets created by this caller.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public AssetListResponse getAssetsForInfrastructureManager(String                   serverName,
                                                               String                   userId,
                                                               String                   infrastructureManagerGUID,
                                                               String                   infrastructureManagerName,
                                                               String                   assetTypeName,
                                                               int                      startFrom,
                                                               int                      pageSize,
                                                               EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getAssetsForInfrastructureManager";
        final String infrastructureManagerGUIDParameterName = "infrastructureManagerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetListResponse response = new AssetListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                if (typeDef == null)
                {
                    restExceptionHandler.handleBadType(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME, instanceHandler.getServiceName(),
                                                       methodName);
                }
                else
                {
                    List<AssetElement> assets = handler.getAttachedElements(userId,
                                                                            infrastructureManagerGUID,
                                                                            infrastructureManagerGUIDParameterName,
                                                                            OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                            OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                            OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                            typeDef.getName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName);

                    setUpVendorProperties(userId, assets, handler, methodName);
                    response.setElementList(assets);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param guid unique identifier of the requested metadata element
     * @param requestBody effectiveTime
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public AssetResponse getAssetByGUID(String                   serverName,
                                        String                   userId,
                                        String                   assetTypeName,
                                        String                   guid,
                                        EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getAssetByGUID";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetResponse response = new AssetResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);

                if (typeDef == null)
                {
                    restExceptionHandler.handleBadType(assetTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME, instanceHandler.getServiceName(),
                                                       methodName);
                }
                else
                {
                    AssetElement assetAsset = handler.getBeanFromRepository(userId,
                                                                            guid,
                                                                            guidParameterName,
                                                                            typeDef.getName(),
                                                                            false,
                                                                            false,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName);

                    response.setElement(setUpVendorProperties(userId, assetAsset, handler, methodName));
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of assets linked by another asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type for the related asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedAssetListResponse getRelatedAssets(String                   serverName,
                                                     String                   userId,
                                                     String                   assetTypeName,
                                                     String                   assetGUID,
                                                     String                   relationshipTypeName,
                                                     String                   relatedAssetTypeName,
                                                     int                      startingEnd,
                                                     int                      startFrom,
                                                     int                      pageSize,
                                                     EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getRelatedAssets";
        final String guidParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedAssetListResponse response = new RelatedAssetListResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelatedAssetHandler<RelatedAssetElement> handler = instanceHandler.getRelatedAssetHandler(userId, serverName, methodName);

                TypeDef typeDef = handler.getTypeDefByName(relationshipTypeName, null);

                String typeGUID = null;
                String typeName = null;

                if (typeDef != null)
                {
                    typeGUID = typeDef.getGUID();
                    typeName = typeDef.getName();
                }

                List<RelatedAssetElement> assets = handler.getRelatedAssets(userId,
                                                                            assetGUID,
                                                                            guidParameterName,
                                                                            assetTypeName,
                                                                            typeGUID,
                                                                            typeName,
                                                                            relatedAssetTypeName,
                                                                            instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                            startingEnd,
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName);

                if (assets != null)
                {
                    AssetHandler<AssetElement> assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                    for (RelatedAssetElement element : assets)
                    {
                        if (element != null)
                        {
                            AssetElement assetElement = element.getRelatedAsset();

                            if (assetElement != null)
                            {
                                setUpVendorProperties(userId, assetElement, assetHandler, methodName);
                            }
                        }
                    }
                }

                response.setElementList(assets);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private AssetElement setUpVendorProperties(String                     userId,
                                               AssetElement               element,
                                               AssetHandler<AssetElement> handler,
                                               String                     methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            AssetProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       methodName));
        }

        return element;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void setUpVendorProperties(String                     userId,
                                       List<AssetElement>         retrievedResults,
                                       AssetHandler<AssetElement> handler,
                                       String                     methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (AssetElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }
    }
}
