/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ControlFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.DataFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.LineageMappingElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.PortElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessCallElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.*;
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
import org.odpi.openmetadata.commonservices.generichandlers.ProcessHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelatedAssetHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * ITAssetRESTService is the server-side of ExternalSourceClientBase.  It is called from the specific clients that manage the
 * specializations of IT Infrastructure assets.
 */
public class ITAssetRESTService
{
    private static final ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();
    private static final RESTCallLogger                  restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(ITAssetRESTService.class),
                                                                                              instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


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
     * Create a new metadata element to represent an asset.
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
                                                                requestBody.getName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                this.getInstanceStatus(requestBody.getInitialStatus()),
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo(),
                                                                new Date(),
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
                                                     (InstanceProperties) null,
                                                     null,
                                                     null,
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
                                                                requestBody.getName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                this.getInstanceStatus(requestBody.getInitialStatus()),
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo(),
                                                                new Date(),
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
                                                     (InstanceProperties) null,
                                                     null,
                                                     null,
                                                     null,
                                                     methodName);
                    }
                }

                if (assetGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                assetGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
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
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                requestBody.getPathName(),
                                                                requestBody.getNetworkAddress(),
                                                                false,
                                                                false,
                                                                new Date(),
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
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
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
     * Update the metadata element representing an asset.
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
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    requestBody.getExtendedProperties(),
                                    requestBody.getEffectiveFrom(),
                                    requestBody.getEffectiveTo(),
                                    isMergeUpdate,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                assetGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
                                                     false,
                                                     false,
                                                     this.getInstanceStatus(requestBody.getElementStatus()),
                                                     statusParameterName,
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
     * Create a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param requestBody request body
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
     * Update a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param relationshipTypeName name of the relationship type
     * @param relationshipGUID unique identifier of the asset
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
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
     * Remove a relationship between an asset and a related asset.
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
     * Update the properties of a classification for an asset.
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
                                                      true,
                                                      requestBody.getProperties(),
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
     * Update the properties of a classification for an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
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
     * Update the zones for the asset so that it becomes visible to consumers.
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

            handler.publishAsset(userId, assetGUID, elementGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
     }


    /**
     * Update the zones for the  asset so that it is no longer visible to consumers.
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

            handler.withdrawAsset(userId, assetGUID, elementGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing an asset.
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
                                                                   false,
                                                                   false,
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
                                                                        false,
                                                                        false,
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
                                                                            null,
                                                                            null,
                                                                            2,
                                                                            false,
                                                                            false,
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
     * Return the list of relationships between assets.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type of retrieved assets
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
    public AssetRelationshipListResponse getAssetRelationships(String                   serverName,
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
        // todo
        return null;
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
                                                                            false,
                                                                            false,
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
                                                                       false,
                                                                       false,
                                                                       new Date(),
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




    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupDataFlow(String              serverName,
                                      String              userId,
                                      String              dataSupplierGUID,
                                      String              dataConsumerGUID,
                                      boolean             infrastructureManagerIsHome,
                                      DataFlowRequestBody requestBody)
    {
        final String methodName = "setupDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    if (infrastructureManagerIsHome)
                    {
                        response.setGUID(handler.setupDataFlow(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               dataSupplierGUID,
                                                               dataSupplierGUIDParameterName,
                                                               dataConsumerGUID,
                                                               dataConsumerGUIDParameterName,
                                                               requestBody.getProperties().getEffectiveFrom(),
                                                               requestBody.getProperties().getEffectiveTo(),
                                                               requestBody.getProperties().getQualifiedName(),
                                                               requestBody.getProperties().getDescription(),
                                                               requestBody.getProperties().getFormula(),
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
                    }
                    else
                    {
                        response.setGUID(handler.setupDataFlow(userId,
                                                               null,
                                                               null,
                                                               dataSupplierGUID,
                                                               dataSupplierGUIDParameterName,
                                                               dataConsumerGUID,
                                                               dataConsumerGUIDParameterName,
                                                               requestBody.getProperties().getEffectiveFrom(),
                                                               requestBody.getProperties().getEffectiveTo(),
                                                               requestBody.getProperties().getQualifiedName(),
                                                               requestBody.getProperties().getDescription(),
                                                               requestBody.getProperties().getFormula(),
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
                    }
                }
                else
                {
                    response.setGUID(handler.setupDataFlow(userId,
                                                           null,
                                                           null,
                                                           dataSupplierGUID,
                                                           dataSupplierGUIDParameterName,
                                                           dataConsumerGUID,
                                                           dataConsumerGUIDParameterName,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
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
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param requestBody optional name to search for
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementResponse getDataFlow(String          serverName,
                                               String          userId,
                                               String          dataSupplierGUID,
                                               String          dataConsumerGUID,
                                               NameRequestBody requestBody)
    {
        final String methodName = "getDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementResponse response = new DataFlowElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataFlow(userId,
                                                        dataSupplierGUID,
                                                        dataSupplierGUIDParameterName,
                                                        dataConsumerGUID,
                                                        dataConsumerGUIDParameterName,
                                                        requestBody.getName(),
                                                        false,
                                                        false,
                                                        requestBody.getEffectiveTime(),
                                                        methodName));
            }
            else
            {
                response.setElement(handler.getDataFlow(userId,
                                                        dataSupplierGUID,
                                                        dataSupplierGUIDParameterName,
                                                        dataConsumerGUID,
                                                        dataConsumerGUIDParameterName,
                                                        null,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName));
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
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDataFlow(String              serverName,
                                       String              userId,
                                       String              dataFlowGUID,
                                       DataFlowRequestBody requestBody)
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";
        final String methodName = "updateDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.updateDataFlow(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           dataFlowGUID,
                                           dataFlowGUIDParameterName,
                                           requestBody.getProperties().getEffectiveFrom(),
                                           requestBody.getProperties().getEffectiveTo(),
                                           requestBody.getProperties().getQualifiedName(),
                                           requestBody.getProperties().getDescription(),
                                           requestBody.getProperties().getFormula(),
                                           false,
                                           false,
                                           requestBody.getEffectiveTime(),
                                           methodName);
                }
                else
                {
                    handler.updateDataFlow(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           dataFlowGUID,
                                           dataFlowGUIDParameterName,
                                           null,
                                           null,
                                           null,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
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
     * Remove the data flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearDataFlow(String                                 serverName,
                                      String                                 userId,
                                      String                                 dataFlowGUID,
                                      EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";
        final String methodName = "clearDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearDataFlow(userId,
                                      requestBody.getExternalSourceGUID(),
                                      requestBody.getExternalSourceName(),
                                      dataFlowGUID,
                                      dataFlowGUIDParameterName,
                                      false,
                                      false,
                                      requestBody.getEffectiveTime(),
                                      methodName);
            }
            else
            {
                handler.clearDataFlow(userId,
                                      null,
                                      null,
                                      dataFlowGUID,
                                      dataFlowGUIDParameterName,
                                      false,
                                      false,
                                      new Date(),
                                      methodName);
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
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementsResponse getDataFlowConsumers(String                   serverName,
                                                         String                   userId,
                                                         String                   dataSupplierGUID,
                                                         int                      startFrom,
                                                         int                      pageSize,
                                                         EffectiveTimeRequestBody requestBody)
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String methodName = "getDataFlowConsumers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementsResponse response = new DataFlowElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getDataFlowConsumers(userId,
                                                                     dataSupplierGUID,
                                                                     dataSupplierGUIDParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     false,
                                                                     false,
                                                                     requestBody.getEffectiveTime(),
                                                                     methodName));
            }
            else
            {
                response.setElementList(handler.getDataFlowConsumers(userId,
                                                                     dataSupplierGUID,
                                                                     dataSupplierGUIDParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName));
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
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementsResponse getDataFlowSuppliers(String                   serverName,
                                                         String                   userId,
                                                         String                   dataConsumerGUID,
                                                         int                      startFrom,
                                                         int                      pageSize,
                                                         EffectiveTimeRequestBody requestBody)
    {
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";
        final String methodName = "getDataFlowSuppliers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementsResponse response = new DataFlowElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getDataFlowSuppliers(userId,
                                                                     dataConsumerGUID,
                                                                     dataConsumerGUIDParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     false,
                                                                     false,
                                                                     requestBody.getEffectiveTime(),
                                                                     methodName));
            }
            else
            {
                response.setElementList(handler.getDataFlowSuppliers(userId,
                                                                     dataConsumerGUID,
                                                                     dataConsumerGUIDParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName));
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
     * Link two elements to show that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier for the control flow relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupControlFlow(String                 serverName,
                                         String                 userId,
                                         String                 currentStepGUID,
                                         String                 nextStepGUID,
                                         boolean                infrastructureManagerIsHome,
                                         ControlFlowRequestBody requestBody)
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";
        final String methodName = "setupControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    if (infrastructureManagerIsHome)
                    {
                        response.setGUID(handler.setupControlFlow(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  currentStepGUID,
                                                                  currentStepGUIDParameterName,
                                                                  nextStepGUID,
                                                                  nextStepGUIDParameterName,
                                                                  requestBody.getProperties().getEffectiveFrom(),
                                                                  requestBody.getProperties().getEffectiveTo(),
                                                                  requestBody.getProperties().getQualifiedName(),
                                                                  requestBody.getProperties().getDescription(),
                                                                  requestBody.getProperties().getGuard(),
                                                                  false,
                                                                  false,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                    }
                    else
                    {
                        response.setGUID(handler.setupControlFlow(userId,
                                                                  null,
                                                                  null,
                                                                  currentStepGUID,
                                                                  currentStepGUIDParameterName,
                                                                  nextStepGUID,
                                                                  nextStepGUIDParameterName,
                                                                  requestBody.getProperties().getEffectiveFrom(),
                                                                  requestBody.getProperties().getEffectiveTo(),
                                                                  requestBody.getProperties().getQualifiedName(),
                                                                  requestBody.getProperties().getDescription(),
                                                                  requestBody.getProperties().getGuard(),
                                                                  false,
                                                                  false,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                    }
                }
                else
                {
                    response.setGUID(handler.setupControlFlow(userId,
                                                              null,
                                                              null,
                                                              currentStepGUID,
                                                              currentStepGUIDParameterName,
                                                              nextStepGUID,
                                                              nextStepGUIDParameterName,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
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
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementResponse getControlFlow(String          serverName,
                                                     String          userId,
                                                     String          currentStepGUID,
                                                     String          nextStepGUID,
                                                     NameRequestBody requestBody)
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";
        final String methodName = "getControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementResponse response = new ControlFlowElementResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getControlFlow(userId,
                                                           currentStepGUID,
                                                           currentStepGUIDParameterName,
                                                           nextStepGUID,
                                                           nextStepGUIDParameterName,
                                                           requestBody.getName(),
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
            }
            else
            {
                response.setElement(handler.getControlFlow(userId,
                                                           currentStepGUID,
                                                           currentStepGUIDParameterName,
                                                           nextStepGUID,
                                                           nextStepGUIDParameterName,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
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
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateControlFlow(String                 serverName,
                                          String                 userId,
                                          String                 controlFlowGUID,
                                          ControlFlowRequestBody requestBody)
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";
        final String methodName = "updateControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                handler.updateControlFlow(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          controlFlowGUID,
                                          controlFlowGUIDParameterName,
                                          requestBody.getProperties().getEffectiveFrom(),
                                          requestBody.getProperties().getEffectiveTo(),
                                          requestBody.getProperties().getQualifiedName(),
                                          requestBody.getProperties().getDescription(),
                                          requestBody.getProperties().getGuard(),
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
     * Remove the control flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody effective time and external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearControlFlow(String                                 serverName,
                                         String                                 userId,
                                         String                                 controlFlowGUID,
                                         EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";
        final String methodName = "clearControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearControlFlow(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         controlFlowGUID,
                                         controlFlowGUIDParameterName,
                                         false,
                                         false,
                                         requestBody.getEffectiveTime(),
                                         methodName);
            }
            else
            {
                handler.clearControlFlow(userId,
                                         null,
                                         null,
                                         controlFlowGUID,
                                         controlFlowGUIDParameterName,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
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
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody null request body
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementsResponse getControlFlowNextSteps(String                   serverName,
                                                               String                   userId,
                                                               String                   currentStepGUID,
                                                               int                      startFrom,
                                                               int                      pageSize,
                                                               EffectiveTimeRequestBody requestBody)
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String methodName = "getControlFlowNextSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementsResponse response = new ControlFlowElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getControlFlowNextSteps(userId,
                                                                        currentStepGUID,
                                                                        currentStepGUIDParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        requestBody.getEffectiveTime(),
                                                                        methodName));
            }
            else
            {
                response.setElementList(handler.getControlFlowNextSteps(userId,
                                                                        currentStepGUID,
                                                                        currentStepGUIDParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));
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
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementsResponse getControlFlowPreviousSteps(String                   serverName,
                                                                   String                   userId,
                                                                   String                   currentStepGUID,
                                                                   int                      startFrom,
                                                                   int                      pageSize,
                                                                   EffectiveTimeRequestBody requestBody)
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String methodName = "getControlFlowPreviousSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementsResponse response = new ControlFlowElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getControlFlowPreviousSteps(userId,
                                                                            currentStepGUID,
                                                                            currentStepGUIDParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            false,
                                                                            false,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName));
            }
            else
            {
                response.setElementList(handler.getControlFlowPreviousSteps(userId,
                                                                            currentStepGUID,
                                                                            currentStepGUIDParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            false,
                                                                            false,
                                                                            new Date(),
                                                                            methodName));
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
     * Link two elements together to show a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupProcessCall(String                 serverName,
                                         String                 userId,
                                         String                 callerGUID,
                                         String                 calledGUID,
                                         boolean                infrastructureManagerIsHome,
                                         ProcessCallRequestBody requestBody)
    {
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";
        final String methodName = "setupProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    if (infrastructureManagerIsHome)
                    {
                        response.setGUID(handler.setupProcessCall(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  callerGUID,
                                                                  callerGUIDParameterName,
                                                                  calledGUID,
                                                                  calledGUIDParameterName,
                                                                  requestBody.getProperties().getEffectiveFrom(),
                                                                  requestBody.getProperties().getEffectiveTo(),
                                                                  requestBody.getProperties().getQualifiedName(),
                                                                  requestBody.getProperties().getDescription(),
                                                                  requestBody.getProperties().getFormula(),
                                                                  false,
                                                                  false,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                    }
                    else
                    {
                        response.setGUID(handler.setupProcessCall(userId,
                                                                  null,
                                                                  null,
                                                                  callerGUID,
                                                                  callerGUIDParameterName,
                                                                  calledGUID,
                                                                  calledGUIDParameterName,
                                                                  requestBody.getProperties().getEffectiveFrom(),
                                                                  requestBody.getProperties().getEffectiveTo(),
                                                                  requestBody.getProperties().getQualifiedName(),
                                                                  requestBody.getProperties().getDescription(),
                                                                  requestBody.getProperties().getFormula(),
                                                                  false,
                                                                  false,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                    }
                }
                else
                {
                    response.setGUID(handler.setupProcessCall(userId,
                                                              null,
                                                              null,
                                                              callerGUID,
                                                              callerGUIDParameterName,
                                                              calledGUID,
                                                              calledGUIDParameterName,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
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
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param requestBody qualified name to disambiguate request
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementResponse getProcessCall(String          serverName,
                                                     String          userId,
                                                     String          callerGUID,
                                                     String          calledGUID,
                                                     NameRequestBody requestBody)
    {
        final String callerGUIDParameterName  = "callerGUID";
        final String calledGUIDParameterName  = "calledGUID";
        final String methodName = "getProcessCallers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementResponse response = new ProcessCallElementResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getProcessCall(userId,
                                                           callerGUID,
                                                           callerGUIDParameterName,
                                                           calledGUID,
                                                           calledGUIDParameterName,
                                                           requestBody.getName(),
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
            }
            else
            {
                response.setElement(handler.getProcessCall(userId,
                                                           callerGUID,
                                                           callerGUIDParameterName,
                                                           calledGUID,
                                                           calledGUIDParameterName,
                                                           null,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
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
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateProcessCall(String                 serverName,
                                          String                 userId,
                                          String                 processCallGUID,
                                          ProcessCallRequestBody requestBody)
    {
        final String processCallGUIDParameterName = "processCallGUID";
        final String methodName = "updateProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                ProcessHandler<ProcessElement,
                                      PortElement,
                                      DataFlowElement,
                                      ControlFlowElement,
                                      ProcessCallElement,
                                      LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

                handler.updateProcessCall(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          processCallGUID,
                                          processCallGUIDParameterName,
                                          requestBody.getProperties().getEffectiveFrom(),
                                          requestBody.getProperties().getEffectiveTo(),
                                          requestBody.getProperties().getQualifiedName(),
                                          requestBody.getProperties().getDescription(),
                                          requestBody.getProperties().getFormula(),
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
     * Remove the process call relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProcessCall(String                                 serverName,
                                         String                                 userId,
                                         String                                 processCallGUID,
                                         EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String processCallGUIDParameterName = "processCallGUID";
        final String methodName = "clearProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearProcessCall(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         processCallGUID,
                                         processCallGUIDParameterName,
                                         false,
                                         false,
                                         requestBody.getEffectiveTime(),
                                         methodName);
            }
            else
            {
                handler.clearProcessCall(userId,
                                         null,
                                         null,
                                         processCallGUID,
                                         processCallGUIDParameterName,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
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
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementsResponse getProcessCalled(String                   serverName,
                                                        String                   userId,
                                                        String                   callerGUID,
                                                        int                      startFrom,
                                                        int                      pageSize,
                                                        EffectiveTimeRequestBody requestBody)
    {
        final String callerGUIDParameterName = "callerGUID";
        final String methodName = "getProcessCalled";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementsResponse response = new ProcessCallElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getProcessCalled(userId,
                                                                 callerGUID,
                                                                 callerGUIDParameterName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 false,
                                                                 false,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName));
            }
            else
            {
                response.setElementList(handler.getProcessCalled(userId,
                                                                 callerGUID,
                                                                 callerGUIDParameterName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName));
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
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementsResponse getProcessCallers(String                   serverName,
                                                         String                   userId,
                                                         String                   calledGUID,
                                                         int                      startFrom,
                                                         int                      pageSize,
                                                         EffectiveTimeRequestBody requestBody)
    {
        final String callerGUIDParameterName = "callerGUID";
        final String methodName = "getProcessCallers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementsResponse response = new ProcessCallElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getProcessCallers(userId,
                                                                  calledGUID,
                                                                  callerGUIDParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  false,
                                                                  false,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
            }
            else
            {
                response.setElementList(handler.getProcessCallers(userId,
                                                                  calledGUID,
                                                                  callerGUIDParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
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
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupLineageMapping(String                    serverName,
                                            String                    userId,
                                            String                    sourceElementGUID,
                                            String                    destinationElementGUID,
                                            LineageMappingRequestBody requestBody)
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";
        final String methodName = "setupLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupLineageMapping(userId,
                                                sourceElementGUID,
                                                sourceElementGUIDParameterName,
                                                destinationElementGUID,
                                                destinationElementGUIDParameterName,
                                                requestBody.getProperties().getQualifiedName(),
                                                requestBody.getProperties().getDescription(),
                                                requestBody.getProperties().getEffectiveFrom(),
                                                requestBody.getProperties().getEffectiveTo(),
                                                false,
                                                false,
                                                requestBody.getEffectiveTime(),
                                                methodName);
                }
                else
                {
                    handler.setupLineageMapping(userId,
                                                sourceElementGUID,
                                                sourceElementGUIDParameterName,
                                                destinationElementGUID,
                                                destinationElementGUIDParameterName,
                                                null,
                                                null,
                                                null,
                                                null,
                                                false,
                                                false,
                                                requestBody.getEffectiveTime(),
                                                methodName);
                }
            }
            else
            {
                handler.setupLineageMapping(userId,
                                            sourceElementGUID,
                                            sourceElementGUIDParameterName,
                                            destinationElementGUID,
                                            destinationElementGUIDParameterName,
                                            null,
                                            null,
                                            null,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementResponse getLineageMapping(String          serverName,
                                                           String          userId,
                                                           String          sourceElementGUID,
                                                           String          destinationElementGUID,
                                                           NameRequestBody requestBody)
    {
        final String methodName = "getLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementResponse response = new LineageMappingElementResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getLineageMapping(userId,
                                                              sourceElementGUID,
                                                              sourceElementGUIDParameterName,
                                                              destinationElementGUID,
                                                              destinationElementGUIDParameterName,
                                                              requestBody.getName(),
                                                              false,
                                                              false,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                response.setElement(handler.getLineageMapping(userId,
                                                              sourceElementGUID,
                                                              sourceElementGUIDParameterName,
                                                              destinationElementGUID,
                                                              destinationElementGUIDParameterName,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
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
     * Remove the lineage mapping between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param lineageMappingGUID unique identifier of the relationship
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearLineageMapping(String                                 serverName,
                                            String                                 userId,
                                            String                                 lineageMappingGUID,
                                            EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        final String lineageMappingGUIDParameterName      = "lineageMappingGUID";
        final String methodName = "clearLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearLineageMapping(userId,
                                            lineageMappingGUID,
                                            lineageMappingGUIDParameterName,
                                            false,
                                            false,
                                            requestBody.getEffectiveTime(),
                                            methodName);
            }
            else
            {
                handler.clearLineageMapping(userId,
                                            lineageMappingGUID,
                                            lineageMappingGUIDParameterName,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementsResponse getDestinationLineageMappings(String                   serverName,
                                                                        String                   userId,
                                                                        String                   sourceElementGUID,
                                                                        int                      startFrom,
                                                                        int                      pageSize,
                                                                        EffectiveTimeRequestBody requestBody)
    {
        final String sourceElementGUIDParameterName = "sourceElementGUID";
        final String methodName = "getDestinationLineageMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementsResponse response = new LineageMappingElementsResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getDestinationLineageMappings(userId,
                                                                              sourceElementGUID,
                                                                              sourceElementGUIDParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              requestBody.getEffectiveTime(),
                                                                              methodName));
            }
            else
            {
                response.setElementList(handler.getDestinationLineageMappings(userId,
                                                                              sourceElementGUID,
                                                                              sourceElementGUIDParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName));
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
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementsResponse getSourceLineageMappings(String                   serverName,
                                                                   String                   userId,
                                                                   String                   destinationElementGUID,
                                                                   int                      startFrom,
                                                                   int                      pageSize,
                                                                   EffectiveTimeRequestBody requestBody)
    {
        final String destinationElementGUIDParameterName = "destinationElementGUID";
        final String methodName = "getSourceLineageMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementsResponse response = new LineageMappingElementsResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> handler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSourceLineageMappings(userId,
                                                                         destinationElementGUID,
                                                                         destinationElementGUIDParameterName,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         requestBody.getEffectiveTime(),
                                                                         methodName));
            }
            else
            {
                response.setElementList(handler.getSourceLineageMappings(userId,
                                                                         destinationElementGUID,
                                                                         destinationElementGUIDParameterName,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
