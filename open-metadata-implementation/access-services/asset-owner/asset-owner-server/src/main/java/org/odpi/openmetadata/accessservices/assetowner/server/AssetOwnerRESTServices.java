/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.commonservices.generichandlers.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetowner.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataContentForDataSetProperties;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.properties.SurveyReport;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AssetOwnerRESTServices provides part of the server-side support for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other REST services that provide specialized methods for specific types of Asset.
 */
public class AssetOwnerRESTServices
{
    private static final AssetOwnerInstanceHandler   instanceHandler      = new AssetOwnerInstanceHandler();
    private static final RESTExceptionHandler        restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger              restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetOwnerRESTServices.class),
                                                                                               instanceHandler.getServiceName());

    private final static String schemaTypeGUIDParameterName      = "schemaTypeGUID";
    private final static String schemaAttributeGUIDParameterName = "schemaAttributeGUID";


    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public AssetOwnerRESTServices()
    {
    }


    /*
     * ==============================================
     * AssetKnowledgeInterface
     * ==============================================
     */

    /**
     * Return the asset subtype names.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    public NameListResponse  getTypesOfAsset(String serverName,
                                             String userId)
    {
        final String   methodName = "getTypesOfAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NameListResponse response = new NameListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setNames(handler.getTypesOfAssetList());
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the asset subtype names with their descriptions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    public StringMapResponse getTypesOfAssetDescriptions(String serverName,
                                                         String userId)
    {
        final String   methodName = "getTypesOfAssetDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StringMapResponse response = new StringMapResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setStringMap(handler.getTypesOfAssetDescriptions());
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ==============================================
     * AssetOnboardingInterface
     * ==============================================
     */


    /**
     * Add a simple asset description to the catalog.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param requestBody other properties for asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse  addAssetToCatalog(String           serverName,
                                           String           userId,
                                           String           typeName,
                                           AssetProperties  requestBody)
    {
        final String methodName  = "addAssetToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                String assetTypeName = OpenMetadataType.ASSET.typeName;

                if (typeName != null)
                {
                    assetTypeName = typeName;
                }

                Date effectiveTime = new Date();
                String assetGUID = handler.createAssetInRepository(userId,
                                                                   null,
                                                                   null,
                                                                   requestBody.getQualifiedName(),
                                                                   requestBody.getName(),
                                                                   requestBody.getVersionIdentifier(),
                                                                   requestBody.getResourceDescription(),
                                                                   requestBody.getDeployedImplementationType(),
                                                                   requestBody.getAdditionalProperties(),
                                                                   assetTypeName,
                                                                   requestBody.getExtendedProperties(),
                                                                   InstanceStatus.ACTIVE,
                                                                   null,
                                                                   null,
                                                                   effectiveTime,
                                                                   methodName);
                if (assetGUID != null)
                {
                    final String assetGUIDParameterName = "assetGUID";

                    this.maintainSupplementaryProperties(userId,
                                                         assetGUID,
                                                         assetGUIDParameterName,
                                                         OpenMetadataType.ASSET.typeName,
                                                         OpenMetadataType.ASSET.typeName,
                                                         requestBody.getQualifiedName(),
                                                         requestBody,
                                                         true,
                                                         effectiveTime,
                                                         handler,
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
     * Create a new metadata element to represent an asset using an existing asset as a template.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse  addAssetToCatalogUsingTemplate(String             serverName,
                                                        String             userId,
                                                        String             templateGUID,
                                                        TemplateProperties requestBody)
    {
        final String methodName = "addAssetToCatalogUsingTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                response.setGUID(handler.addAssetFromTemplate(userId,
                                                              null,
                                                              null,
                                                              templateGUID,
                                                              templateGUIDParameterName,
                                                              OpenMetadataType.ASSET.typeGUID,
                                                              OpenMetadataType.ASSET.typeName,
                                                              requestBody.getQualifiedName(),
                                                              qualifiedNameParameterName,
                                                              requestBody.getDisplayName(),
                                                              requestBody.getVersionIdentifier(),
                                                              requestBody.getDescription(),
                                                              null,
                                                              requestBody.getPathName(),
                                                              requestBody.getNetworkAddress(),
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
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
     * Update a simple asset description to the catalog.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user (assumed to be the owner)
     * @param assetGUID unique identifier of the asset
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody other properties for asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  updateAsset(String           serverName,
                                     String           userId,
                                     String           assetGUID,
                                     boolean          isMergeUpdate,
                                     AssetProperties  requestBody)
    {
        final String methodName             = "updateAsset";
        final String assetGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                Date effectiveTime = new Date();
                handler.updateAsset(userId,
                                    null,
                                    null,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getName(),
                                    requestBody.getResourceName(),
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getResourceDescription(),
                                    requestBody.getDeployedImplementationType(),
                                    requestBody.getAdditionalProperties(),
                                    requestBody.getTypeName(),
                                    requestBody.getExtendedProperties(),
                                    null,
                                    null,
                                    isMergeUpdate,
                                    false,
                                    false,
                                    effectiveTime,
                                    methodName);

                    this.maintainSupplementaryProperties(userId,
                                                         assetGUID,
                                                         assetGUIDParameterName,
                                                         OpenMetadataType.ASSET.typeName,
                                                         OpenMetadataType.ASSET.typeName,
                                                         requestBody.getQualifiedName(),
                                                         requestBody,
                                                         isMergeUpdate,
                                                         effectiveTime,
                                                         handler,
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
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupRelatedAsset(String                  serverName,
                                          String                  userId,
                                          String                  relationshipTypeName,
                                          String                  fromAssetGUID,
                                          String                  toAssetGUID,
                                          RelationshipRequestBody requestBody)
    {
        final String methodName  = "setupRelatedDataAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String fromAssetGUIDParameterName = "fromAssetGUID";
            final String toAssetGUIDParameterName   = "toAssetGUID";
            final String typeNameParameterName      = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   handler.getServiceName(),
                                                                                   methodName,
                                                                                   handler.getRepositoryHelper());

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataContentForDataSetProperties dataContentForDataSetProperties)
                {
                    InstanceProperties instanceProperties = handler.getRepositoryHelper().addStringPropertyToInstance(handler.getServiceName(),
                                                                                                                      null,
                                                                                                                      OpenMetadataType.QUERY_ID_PROPERTY_NAME,
                                                                                                                      dataContentForDataSetProperties.getQueryId(),
                                                                                                                      methodName);
                    instanceProperties = handler.getRepositoryHelper().addStringPropertyToInstance(handler.getServiceName(),
                                                                                                   instanceProperties,
                                                                                                   OpenMetadataType.QUERY_PROPERTY_NAME,
                                                                                                   dataContentForDataSetProperties.getQuery(),
                                                                                                   methodName);

                    response.setGUID(handler.linkElementToElement(userId,
                                                                  null,
                                                                  null,
                                                                  fromAssetGUID,
                                                                  fromAssetGUIDParameterName,
                                                                  OpenMetadataType.ASSET.typeName,
                                                                  toAssetGUID,
                                                                  toAssetGUIDParameterName,
                                                                  OpenMetadataType.ASSET.typeName,
                                                                  false,
                                                                  false,
                                                                  handler.getSupportedZones(),
                                                                  relationshipTypeGUID,
                                                                  relationshipTypeName,
                                                                  instanceProperties,
                                                                  null,
                                                                  null,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                }
                else
                {
                    response.setGUID(handler.linkElementToElement(userId,
                                                                  null,
                                                                  null,
                                                                  fromAssetGUID,
                                                                  fromAssetGUIDParameterName,
                                                                  OpenMetadataType.ASSET.typeName,
                                                                  toAssetGUID,
                                                                  toAssetGUIDParameterName,
                                                                  OpenMetadataType.ASSET.typeName,
                                                                  false,
                                                                  false,
                                                                  handler.getSupportedZones(),
                                                                  relationshipTypeGUID,
                                                                  relationshipTypeName,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
                }
            }
            else
            {
                response.setGUID(handler.linkElementToElement(userId,
                                                              null,
                                                              null,
                                                              fromAssetGUID,
                                                              fromAssetGUIDParameterName,
                                                              OpenMetadataType.ASSET.typeName,
                                                              toAssetGUID,
                                                              toAssetGUIDParameterName,
                                                              OpenMetadataType.ASSET.typeName,
                                                              false,
                                                              false,
                                                              handler.getSupportedZones(),
                                                              relationshipTypeGUID,
                                                              relationshipTypeName,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
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
     * Retrieve the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param requestBody optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementResponse getAssetRelationship(String                        serverName,
                                                            String                        userId,
                                                            String                        relationshipTypeName,
                                                            String                        fromAssetGUID,
                                                            String                        toAssetGUID,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementResponse response = new RelationshipElementResponse();
        AuditLog                    auditLog  = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String fromAssetGUIDParameterName = "fromAssetGUID";
            final String toAssetGUIDParameterName   = "toAssetGUID";
            final String typeNameParameterName      = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   instanceHandler.getServiceName(),
                                                                                   methodName,
                                                                                   handler.getRepositoryHelper());

            Relationship relationship;

            if (requestBody != null)
            {
                relationship = handler.getUniqueAttachmentLink(userId,
                                                               fromAssetGUID,
                                                               fromAssetGUIDParameterName,
                                                               OpenMetadataType.ASSET.typeName,
                                                               relationshipTypeGUID,
                                                               relationshipTypeName,
                                                               toAssetGUID,
                                                               OpenMetadataType.ASSET.typeName,
                                                               2,
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               false,
                                                               false,
                                                               requestBody.getEffectiveTime(),
                                                               methodName);
            }
            else
            {
                relationship = handler.getUniqueAttachmentLink(userId,
                                                               fromAssetGUID,
                                                               fromAssetGUIDParameterName,
                                                               OpenMetadataType.ASSET.typeName,
                                                               relationshipTypeGUID,
                                                               relationshipTypeName,
                                                               toAssetGUID,
                                                               OpenMetadataType.ASSET.typeName,
                                                               2,
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               false,
                                                               false,
                                                               null,
                                                               methodName);
            }

            response.setElement(getRelationshipElement(relationship, handler.getRepositoryHelper(), serverName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request that the asset handler creates a schema type and links it to the asset.
     * Update relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param requestBody description and/or purpose of the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAssetRelationship(String                  serverName,
                                                String                  userId,
                                                String                  relationshipTypeName,
                                                String                  relationshipGUID,
                                                boolean                 isMergeUpdate,
                                                RelationshipRequestBody requestBody)
    {
        final String methodName = "updateAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String relationshipGUIDParameterName = "relationshipGUID";
            final String typeNameParameterName         = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            if (requestBody != null)
            {
                InstanceProperties instanceProperties = null;

                if (requestBody.getProperties() != null)
                {
                    if (requestBody.getProperties() instanceof DataContentForDataSetProperties dataContentForDataSetProperties)
                    {
                        instanceProperties = handler.getRepositoryHelper().addStringPropertyToInstance(handler.getServiceName(), null, OpenMetadataType.QUERY_ID_PROPERTY_NAME, dataContentForDataSetProperties.getQueryId(), methodName);
                        instanceProperties = handler.getRepositoryHelper().addStringPropertyToInstance(handler.getServiceName(), instanceProperties, OpenMetadataType.QUERY_PROPERTY_NAME, dataContentForDataSetProperties.getQuery(), methodName);
                    }
                }

                handler.updateElementToElementLink(userId,
                                                   null,
                                                   null,
                                                   relationshipGUID,
                                                   relationshipGUIDParameterName,
                                                   relationshipTypeName,
                                                   false,
                                                   false,
                                                   handler.getSupportedZones(),
                                                   isMergeUpdate,
                                                   instanceProperties,
                                                   requestBody.getEffectiveTime(),
                                                   methodName);
            }
            else
            {
                handler.updateElementToElementLink(userId,
                                                   null,
                                                   null,
                                                   relationshipGUID,
                                                   relationshipGUIDParameterName,
                                                   relationshipTypeName,
                                                   false,
                                                   false,
                                                   handler.getSupportedZones(),
                                                   isMergeUpdate,
                                                   null,
                                                   null,
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
     * Remove the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     * @param requestBody external source ids
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetRelationship(String                        serverName,
                                               String                        userId,
                                               String                        relationshipTypeName,
                                               String                        relationshipGUID,
                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAssetRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String relationshipGUIDParameterName = "relationshipGUID";
            final String typeNameParameterName         = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            if (requestBody != null)
            {
                handler.deleteRelationship(userId,
                                           null,
                                           null,
                                           relationshipGUID,
                                           relationshipGUIDParameterName,
                                           relationshipTypeName,
                                           false,
                                           false,
                                           handler.getSupportedZones(),
                                           requestBody.getEffectiveTime(),
                                           methodName);
            }
            else
            {
                handler.deleteRelationship(userId,
                                           null,
                                           null,
                                           relationshipGUID,
                                           relationshipGUIDParameterName,
                                           relationshipTypeName,
                                           false,
                                           false,
                                           handler.getSupportedZones(),
                                           null,
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
     * Retrieve the requested relationships linked from a specific element at end 2.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementsResponse getRelatedAssetsAtEnd2(String                        serverName,
                                                               String                        userId,
                                                               String                        relationshipTypeName,
                                                               String                        fromAssetGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName  = "getRelatedAssetsAtEnd2";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementsResponse response = new RelationshipElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String fromAssetGUIDParameterName = "fromAssetGUID";
            final String typeNameParameterName      = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   instanceHandler.getServiceName(),
                                                                                   methodName,
                                                                                   handler.getRepositoryHelper());

            List<Relationship> relationships;
            if (requestBody != null)
            {
                relationships = handler.getAttachmentLinks(userId,
                                                           fromAssetGUID,
                                                           fromAssetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           relationshipTypeGUID,
                                                           relationshipTypeName,
                                                           null,
                                                           OpenMetadataType.ASSET.typeName,
                                                           2,
                                                           null,
                                                           null,
                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                           null,
                                                           false,
                                                           false,
                                                           startFrom,
                                                           pageSize,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
            }
            else
            {
                relationships = handler.getAttachmentLinks(userId,
                                                           fromAssetGUID,
                                                           fromAssetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           relationshipTypeGUID,
                                                           relationshipTypeName,
                                                           null,
                                                           OpenMetadataType.ASSET.typeName,
                                                           2,
                                                           null,
                                                           null,
                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                           null,
                                                           false,
                                                           false,
                                                           startFrom,
                                                           pageSize,
                                                           null,
                                                           methodName);
            }

            if (relationships != null)
            {
                List<RelationshipElement> relationshipElements = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    relationshipElements.add(getRelationshipElement(relationship, handler.getRepositoryHelper(), serverName, methodName));
                }

                response.setElements(relationshipElements);
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
     * Retrieve the relationships linked from a specific element at end 2 of the relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElementsResponse getRelatedAssetsAtEnd1(String                        serverName,
                                                               String                        userId,
                                                               String                        relationshipTypeName,
                                                               String                        toAssetGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getRelatedAssetsAtEnd1";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelationshipElementsResponse response = new RelationshipElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            final String toAssetGUIDParameterName = "toAssetGUID";
            final String typeNameParameterName    = "relationshipTypeName";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
            invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

            String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                   null,
                                                                                   instanceHandler.getServiceName(),
                                                                                   methodName,
                                                                                   handler.getRepositoryHelper());

            List<Relationship> relationships;
            if (requestBody != null)
            {
                relationships = handler.getAttachmentLinks(userId,
                                                           toAssetGUID,
                                                           toAssetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           relationshipTypeGUID,
                                                           relationshipTypeName,
                                                           null,
                                                           OpenMetadataType.ASSET.typeName,
                                                           1,
                                                           null,
                                                           null,
                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                           null,
                                                           false,
                                                           false,
                                                           startFrom,
                                                           pageSize,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
            }
            else
            {
                relationships = handler.getAttachmentLinks(userId,
                                                           toAssetGUID,
                                                           toAssetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           relationshipTypeGUID,
                                                           relationshipTypeName,
                                                           null,
                                                           OpenMetadataType.ASSET.typeName,
                                                           1,
                                                           null,
                                                           null,
                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                           null,
                                                           false,
                                                           false,
                                                           startFrom,
                                                           pageSize,
                                                           null,
                                                           methodName);
            }

            if (relationships != null)
            {
                List<RelationshipElement> relationshipElements = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    relationshipElements.add(getRelationshipElement(relationship, handler.getRepositoryHelper(), serverName, methodName));
                }

                response.setElements(relationshipElements);
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
     * Concert an OMRS relationship into an Asset Manager's RelationshipElement.
     *
     * @param relationship retrieved relationship
     * @param repositoryHelper utility methods
     * @param methodName calling method
     * @return element to return
     */
    private RelationshipElement getRelationshipElement(Relationship         relationship,
                                                       OMRSRepositoryHelper repositoryHelper,
                                                       String               serverName,
                                                       String               methodName) throws PropertyServerException
    {
        RelationshipElement relationshipElement = null;

        if (relationship != null)
        {
            ElementHeaderConverter<ElementHeader> elementHeaderConverter = new ElementHeaderConverter<>(repositoryHelper, instanceHandler.getServiceName(), serverName);

            relationshipElement = new RelationshipElement();

            relationshipElement.setRelationshipHeader(elementHeaderConverter.getNewBean(ElementHeader.class, relationship, methodName));
            relationshipElement.setEnd1GUID(elementHeaderConverter.getNewBean(ElementHeader.class, relationship.getEntityOneProxy(), methodName));
            relationshipElement.setEnd2GUID(elementHeaderConverter.getNewBean(ElementHeader.class, relationship.getEntityTwoProxy(), methodName));

            if (relationship.getProperties() != null)
            {
                if (OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName.equals(relationship.getType().getTypeDefName()))
                {
                    DataContentForDataSetProperties properties = new DataContentForDataSetProperties();

                    properties.setQueryId(repositoryHelper.getStringProperty(instanceHandler.getServiceName(), OpenMetadataType.QUERY_ID_PROPERTY_NAME, relationship.getProperties(), methodName));
                    properties.setQuery(repositoryHelper.getStringProperty(instanceHandler.getServiceName(), OpenMetadataType.QUERY_PROPERTY_NAME, relationship.getProperties(), methodName));

                    properties.setEffectiveFrom(relationship.getProperties().getEffectiveFromTime());
                    properties.setEffectiveTo(relationship.getProperties().getEffectiveFromTime());

                    relationshipElement.setRelationshipProperties(properties);
                }
                else
                {
                    RelationshipProperties properties = new RelationshipProperties();

                    properties.setEffectiveFrom(relationship.getProperties().getEffectiveFromTime());
                    properties.setEffectiveTo(relationship.getProperties().getEffectiveFromTime());

                    relationshipElement.setRelationshipProperties(properties);
                }
            }
        }

        return relationshipElement;
    }



    /**
     * Stores the supplied schema details in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  If more attributes need to be added in addition to the
     * ones supplied then this can be done with addSchemaAttributesToSchemaType().
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody schema type to create and attach directly to the asset.
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse   addCombinedSchemaToAsset(String                    serverName,
                                                   String                    userId,
                                                   String                    assetGUID,
                                                   CombinedSchemaRequestBody requestBody)
    {
        final String   methodName = "addCombinedSchemaToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getSchemaType() != null)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    response.setGUID(this.addAssociatedSchemaType(userId,
                                                                  serverName,
                                                                  assetGUID,
                                                                  requestBody.getSchemaType(),
                                                                  methodName));


                    if (requestBody.getSchemaAttributes() != null)
                    {
                        for (SchemaAttributeProperties schemaAttributeProperties : requestBody.getSchemaAttributes())
                        {
                            this.addAssociatedSchemaAttribute(userId,
                                                              serverName,
                                                              assetGUID,
                                                              response.getGUID(),
                                                              schemaAttributeProperties,
                                                              methodName);
                        }
                    }
                }
                else
                {
                    final String parameterName = "requestBody.getSchemaType()";

                    restExceptionHandler.handleMissingValue(parameterName, methodName);
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
     * Stores the supplied schema type in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody schema type to create and attach directly to the asset.
     *
     * @return guid of the new schema type or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse   addSchemaTypeToAsset(String               serverName,
                                               String               userId,
                                               String               assetGUID,
                                               SchemaTypeProperties requestBody)
    {
        final String   methodName = "addSchemaTypeToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(this.addAssociatedSchemaType(userId,
                                                              serverName,
                                                              assetGUID,
                                                              requestBody,
                                                              methodName));
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
     * Request that the asset handler creates a schema type and links it to the asset.
     *
     * @param userId calling user
     * @param serverName name of the server instance to connect to
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType properties for a schema type
     * @param methodName calling method
     * @return unique identifier of the newly created schema type
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException calling user is not permitted to perform this operation
     * @throws PropertyServerException there was a problem in one of the repositories
     */
    private String addAssociatedSchemaType(String               userId,
                                           String               serverName,
                                           String               assetGUID,
                                           SchemaTypeProperties schemaType,
                                           String               methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

        SchemaTypeBuilder schemaTypeBuilder = getSchemaTypeBuilder(schemaType,
                                                                   handler.getRepositoryHelper(),
                                                                   handler.getServiceName(),
                                                                   serverName,
                                                                   methodName);

        EntityDetail  assetEntity = handler.getEntityFromRepository(userId,
                                                                    assetGUID,
                                                                    assetGUIDParameterName,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    null,
                                                                    methodName);
        if (assetEntity != null)
        {
            schemaTypeBuilder.setAnchors(userId,
                                         assetGUID,
                                         assetEntity.getType().getTypeDefName(),
                                         OpenMetadataType.ASSET.typeName,
                                         methodName);
        }

        String schemaTypeGUID = handler.addSchemaType(userId,
                                                      null,
                                                      null,
                                                      schemaTypeBuilder,
                                                      null,
                                                      null,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);

        if (schemaTypeGUID != null)
        {
            handler.uncheckedLinkElementToElement(userId,
                                                  null,
                                                  null,
                                                  assetGUID,
                                                  assetGUIDParameterName,
                                                  schemaTypeGUID,
                                                  schemaTypeGUIDParameterName,
                                                  OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                  null,
                                                  methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Recursively navigate through the schema type loading up a new schema type builder to pass to the
     * schemaTypeHandler.
     *
     * @param schemaType supplied schema type
     * @param repositoryHelper repository helper for this server
     * @param serviceName calling service name
     * @param serverName this server instance
     * @param methodName calling method
     *
     * @return schema type builder
     * @throws InvalidParameterException invalid type in one of the schema types
     */
    private SchemaTypeBuilder getSchemaTypeBuilder(SchemaTypeProperties schemaType,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               serverName,
                                                   String               methodName) throws InvalidParameterException
    {
        String typeName = OpenMetadataType.SCHEMA_TYPE_TYPE_NAME;

        if (schemaType.getTypeName() != null)
        {
            typeName = schemaType.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        return new SchemaTypeBuilder(schemaType.getQualifiedName(),
                                     schemaType.getDisplayName(),
                                     schemaType.getDescription(),
                                     schemaType.getVersionNumber(),
                                     schemaType.getIsDeprecated(),
                                     schemaType.getAuthor(),
                                     schemaType.getUsage(),
                                     schemaType.getEncodingStandard(),
                                     schemaType.getNamespace(),
                                     schemaType.getAdditionalProperties(),
                                     typeGUID,
                                     typeName,
                                     schemaType.getExtendedProperties(),
                                     repositoryHelper,
                                     serviceName,
                                     serverName);
    }


    /**
     * Create a schema attribute and attach it to the supplied parent schema type.  This method has 3 parts to it.
     * First to create the schema attribute.  Then to link the schema attribute to its parent schema so that the attribute
     * count value is visible as soon as possible.  Finally, to determine the style of type for the attribute - is it directly linked or
     * is it indirectly linked through a schema link entity - and create these elements.
     *
     * @param userId calling user
     * @param serverName this server
     * @param assetGUID anchor GUID for the new schema type
     * @param schemaAttribute properties for the new attribute
     * @param methodName calling method
     * @return unique identifier for the schema type
     * @throws InvalidParameterException one of the properties is invalid
     * @throws UserNotAuthorizedException the calling user is not authorized to perform this request
     * @throws PropertyServerException there was a problem in the repositories
     */
    private String addAssociatedSchemaAttribute(String                    userId,
                                                String                    serverName,
                                                String                    assetGUID,
                                                String                    schemaTypeGUID,
                                                SchemaAttributeProperties schemaAttribute,
                                                String                    methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler =
                instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

        String schemaAttributeGUID = null;

        if (schemaAttribute != null)
        {
            int sortOrder = DataItemSortOrder.UNSORTED.getOrdinal();

            if (schemaAttribute.getSortOrder() != null)
            {
                sortOrder = schemaAttribute.getSortOrder().getOrdinal();
            }

            SchemaAttributeBuilder schemaAttributeBuilder =
                    new SchemaAttributeBuilder(schemaAttribute.getQualifiedName(),
                                               schemaAttribute.getDisplayName(),
                                               schemaAttribute.getDescription(),
                                               schemaAttribute.getElementPosition(),
                                               schemaAttribute.getMinCardinality(),
                                               schemaAttribute.getMaxCardinality(),
                                               schemaAttribute.getIsDeprecated(),
                                               schemaAttribute.getDefaultValueOverride(),
                                               schemaAttribute.getAllowsDuplicateValues(),
                                               schemaAttribute.getOrderedValues(),
                                               sortOrder,
                                               schemaAttribute.getMinimumLength(),
                                               schemaAttribute.getLength(),
                                               schemaAttribute.getPrecision(),
                                               schemaAttribute.getIsNullable(),
                                               schemaAttribute.getNativeJavaClass(),
                                               schemaAttribute.getAliases(),
                                               schemaAttribute.getAdditionalProperties(),
                                               null,
                                               schemaAttribute.getTypeName(),
                                               schemaAttribute.getExtendedProperties(),
                                               handler.getRepositoryHelper(),
                                               handler.getServiceName(),
                                               serverName);

            String assetTypeName = null;
            if (assetGUID != null)
            {
                EntityDetail anchorEntity = handler.getEntityFromRepository(userId,
                                                                            assetGUID,
                                                                            assetGUIDParameterName,
                                                                            OpenMetadataType.ASSET.typeName,
                                                                            null,
                                                                            null,
                                                                            false,
                                                                            false,
                                                                            new Date(),
                                                                            methodName);

                if (anchorEntity != null)
                {
                    assetTypeName = anchorEntity.getType().getTypeDefName();
                }

                schemaAttributeBuilder.setAnchors(userId,
                                                  assetGUID,
                                                  assetTypeName,
                                                  OpenMetadataType.ASSET.typeName,
                                                  methodName);
            }

            if (schemaAttribute.getSchemaType() != null)
            {
                SchemaTypeProperties schemaTypeProperties = schemaAttribute.getSchemaType();
                SchemaTypeBuilder attributeSchemaTypeBuilder = new SchemaTypeBuilder(schemaTypeProperties.getQualifiedName(),
                                                                                     schemaTypeProperties.getDisplayName(),
                                                                                     schemaTypeProperties.getDescription(),
                                                                                     schemaTypeProperties.getVersionNumber(),
                                                                                     schemaTypeProperties.getIsDeprecated(),
                                                                                     schemaTypeProperties.getAuthor(),
                                                                                     schemaTypeProperties.getUsage(),
                                                                                     schemaTypeProperties.getEncodingStandard(),
                                                                                     schemaTypeProperties.getNamespace(),
                                                                                     schemaTypeProperties.getAdditionalProperties(),
                                                                                     null,
                                                                                     schemaTypeProperties.getTypeName(),
                                                                                     schemaTypeProperties.getExtendedProperties(),
                                                                                     handler.getRepositoryHelper(),
                                                                                     handler.getServiceName(),
                                                                                     serverName);

                if (assetGUID != null)
                {
                    attributeSchemaTypeBuilder.setAnchors(userId,
                                                          assetGUID,
                                                          assetTypeName,
                                                          OpenMetadataType.ASSET.typeName,
                                                          methodName);
                }
                schemaAttributeBuilder.setSchemaType(userId, attributeSchemaTypeBuilder, methodName);

                final String schemaTypeGUIDParameterName = "schemaTypeGUID";
                final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";

                schemaAttributeGUID = handler.createNestedSchemaAttribute(userId,
                                                                          null,
                                                                          null,
                                                                          schemaTypeGUID,
                                                                          schemaTypeGUIDParameterName,
                                                                          OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                          OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                          OpenMetadataType.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                          schemaAttribute.getQualifiedName(),
                                                                          qualifiedNameParameterName,
                                                                          schemaAttributeBuilder,
                                                                          null,
                                                                          null,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);
            }
            else
            {
                final String parameterName = "attribute schema type";

                restExceptionHandler.handleMissingValue(parameterName, methodName);
            }
        }

        return schemaAttributeGUID;
    }


    /**
     * Links the supplied schema type directly to the asset.  If this schema is either not found, or
     * already attached to an asset, then an error occurs.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaTypeGUID unique identifier of the schema type to attach
     * @param requestBody null
     *
     * @return void or
     * InvalidParameterException full path or userId or one of the GUIDs is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   attachSchemaTypeToAsset(String            serverName,
                                                  String            userId,
                                                  String            assetGUID,
                                                  String            schemaTypeGUID,
                                                  NullRequestBody   requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   schemaTypeGUIDParameterName = "schemaTypeGUID";
        final String   methodName = "attachSchemaTypeToAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.attachSchemaTypeToAsset(userId,
                                            null,
                                            null,
                                            assetGUID,
                                            assetGUIDParameterName,
                                            schemaTypeGUID,
                                            schemaTypeGUIDParameterName,
                                            null,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unlinks the schema from the asset but does not delete it.  This means it can be reattached to a different asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody null
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId or one of the GUIDs is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public GUIDResponse   detachSchemaTypeFromAsset(String          serverName,
                                                    String          userId,
                                                    String          assetGUID,
                                                    NullRequestBody requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "detachSchemaTypeFromAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            response.setGUID(handler.detachSchemaTypeFromAsset(userId,
                                                               null,
                                                               null,
                                                               assetGUID,
                                                               assetGUIDParameterName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detaches and deletes an asset's schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody null
     *
     * @return void or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  deleteAssetSchemaType(String          serverName,
                                               String          userId,
                                               String          assetGUID,
                                               NullRequestBody requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName             = "deleteAssetSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.removeAssociatedSchemaType(userId,
                                               null,
                                               null,
                                               assetGUID,
                                               assetGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaType(String               serverName,
                                         String               userId,
                                         String               anchorGUID,
                                         SchemaTypeProperties requestBody)
    {
        final String methodName = "createSchemaType";
        final String  anchorGUIDParameterName     = "anchorGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                final String propertiesParameterName     = "schemaTypeProperties";
                final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateObject(requestBody, propertiesParameterName, methodName);
                invalidParameterHandler.validateName(requestBody.getQualifiedName(), qualifiedNameParameterName, methodName);

                SchemaTypeBuilder builder = this.getSchemaTypeBuilder(requestBody,
                                                                      instanceHandler.getRepositoryHelper(userId, serverName, methodName),
                                                                      instanceHandler.getServiceName(),
                                                                      serverName,
                                                                      methodName);

                builder.setEffectivityDates(requestBody.getEffectiveFrom(), requestBody.getEffectiveTo());

                if (anchorGUID != null)
                {
                    EntityDetail anchorEntity = handler.getEntityFromRepository(userId,
                                                                                anchorGUID,
                                                                                anchorGUIDParameterName,
                                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                                null,
                                                                                null,
                                                                                false,
                                                                                false,
                                                                                null,
                                                                                methodName);

                    if (anchorEntity != null)
                    {
                        builder.setAnchors(userId,
                                           anchorGUID,
                                           anchorEntity.getType().getTypeDefName(),
                                           OpenMetadataType.ASSET.typeName,
                                           methodName);
                    }
                }

                response.setGUID(handler.addSchemaType(userId,
                                                       null,
                                                       null,
                                                       builder,
                                                       requestBody.getEffectiveFrom(),
                                                       requestBody.getEffectiveTo(),
                                                       false,
                                                       false,
                                                       null,
                                                       methodName));
                
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
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaTypeFromTemplate(String              serverName,
                                                     String              userId,
                                                     String              templateGUID,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                final String templateGUIDParameterName   = "templateGUID";
                final String propertiesParameterName     = "templateProperties";
                final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
                invalidParameterHandler.validateObject(requestBody.getElementProperties(), propertiesParameterName, methodName);
                invalidParameterHandler.validateName(requestBody.getElementProperties().getQualifiedName(), qualifiedNameParameterName, methodName);

                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                response.setGUID(handler.createSchemaTypeFromTemplate(userId,
                                                                      null, 
                                                                      null,
                                                                      templateGUID,
                                                                      requestBody.getElementProperties().getQualifiedName(),
                                                                      requestBody.getElementProperties().getDisplayName(),
                                                                      requestBody.getElementProperties().getDescription(),
                                                                      methodName));
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
     * Update the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateSchemaType(String                serverName,
                                         String                userId,
                                         String                schemaTypeGUID,
                                         boolean               isMergeUpdate,
                                         SchemaTypeProperties  requestBody)
    {
        final String methodName = "updateSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                final String propertiesParameterName     = "schemaTypeProperties";
                final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
                invalidParameterHandler.validateObject(requestBody, propertiesParameterName, methodName);
                if (! isMergeUpdate)
                {
                    invalidParameterHandler.validateName(requestBody.getQualifiedName(), qualifiedNameParameterName, methodName);
                }

                SchemaTypeBuilder builder = this.getSchemaTypeBuilder(requestBody,
                                                                      instanceHandler.getRepositoryHelper(userId, serverName, methodName),
                                                                      instanceHandler.getServiceName(),
                                                                      serverName,
                                                                      methodName);

                handler.updateSchemaType(userId,
                                         null,
                                         null,
                                         schemaTypeGUID,
                                         schemaTypeGUIDParameterName,
                                         builder,
                                         isMergeUpdate,
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
     * Connect a schema type to a data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupSchemaTypeParent(String                  serverName,
                                              String                  userId,
                                              String                  parentElementGUID,
                                              String                  parentElementTypeName,
                                              String                  schemaTypeGUID,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName = "setupSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.setupSchemaTypeParent(userId,
                                                  null,
                                                  null,
                                                  schemaTypeGUID,
                                                  parentElementGUID,
                                                  parentElementTypeName,
                                                  requestBody.getProperties().getEffectiveFrom(),
                                                  requestBody.getProperties().getEffectiveTo(),
                                                  false,
                                                  false,
                                                  null,
                                                  methodName);
                }
                else
                {
                    handler.setupSchemaTypeParent(userId,
                                                  null,
                                                  null,
                                                  schemaTypeGUID,
                                                  parentElementGUID,
                                                  parentElementTypeName,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  null,
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
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaTypeParent(String                        serverName,
                                              String                        userId,
                                              String                        parentElementGUID,
                                              String                        parentElementTypeName,
                                              String                        schemaTypeGUID,
                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                handler.clearSchemaTypeParent(userId,
                                              null,
                                              null,
                                              schemaTypeGUID,
                                              parentElementGUID,
                                              parentElementTypeName,
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
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupSchemaElementRelationship(String                  serverName,
                                                       String                  userId,
                                                       String                  endOneGUID,
                                                       String                  relationshipTypeName,
                                                       String                  endTwoGUID,
                                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "setupSchemaElementRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupSchemaElementRelationship(userId,
                                                           null,
                                                           null,
                                                           endOneGUID,
                                                           endTwoGUID,
                                                           relationshipTypeName,
                                                           requestBody.getProperties().getExtendedProperties(),
                                                           requestBody.getProperties().getEffectiveFrom(),
                                                           requestBody.getProperties().getEffectiveTo(),
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
                }
                else
                {
                    handler.setupSchemaElementRelationship(userId,
                                                           null,
                                                           null,
                                                           endOneGUID,
                                                           endTwoGUID,
                                                           relationshipTypeName,
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
                handler.setupSchemaElementRelationship(userId,
                                                       null,
                                                       null,
                                                       endOneGUID,
                                                       endTwoGUID,
                                                       relationshipTypeName,
                                                       null,
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       null,
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
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaElementRelationship(String                        serverName,
                                                       String                        userId,
                                                       String                        endOneGUID,
                                                       String                        relationshipTypeName,
                                                       String                        endTwoGUID,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearSchemaElementRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearSchemaElementRelationship(userId,
                                                       null,
                                                       null,
                                                       endTwoGUID,
                                                       endOneGUID,
                                                       relationshipTypeName,
                                                       false,
                                                       false,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
            }
            else
            {
                handler.clearSchemaElementRelationship(userId,
                                                       null,
                                                       null,
                                                       endTwoGUID,
                                                       endOneGUID,
                                                       relationshipTypeName,
                                                       false,
                                                       false,
                                                       null,
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
     * Remove the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaType(String            serverName,
                                         String            userId,
                                         String            schemaTypeGUID,
                                         UpdateRequestBody requestBody)
    {
        final String methodName = "removeSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               schemaTypeGUID,
                                               schemaTypeGUIDParameterName,
                                               OpenMetadataType.SCHEMA_TYPE_TYPE_GUID,
                                               OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               requestBody.getEffectiveTime(),
                                               methodName);
            }
            else
            {
                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               schemaTypeGUID,
                                               schemaTypeGUIDParameterName,
                                               OpenMetadataType.SCHEMA_TYPE_TYPE_GUID,
                                               OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
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
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementsResponse findSchemaType(String                  serverName,
                                                     String                  userId,
                                                     int                     startFrom,
                                                     int                     pageSize,
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementsResponse response = new SchemaTypeElementsResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                response.setElements(handler.findSchemaTypes(userId,
                                                             null,
                                                             requestBody.getSearchString(),
                                                             startFrom,
                                                             pageSize,
                                                             false,
                                                             false,
                                                             requestBody.getEffectiveTime(),
                                                             methodName));
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
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return metadata element describing the schema type associated with the requested parent element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementResponse getSchemaTypeForElement(String                        serverName,
                                                             String                        userId,
                                                             String                        parentElementGUID,
                                                             String                        parentElementTypeName,
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeForElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementResponse response = new SchemaTypeElementResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaTypeForElement(userId,
                                                                    parentElementGUID,
                                                                    parentElementTypeName,
                                                                    false,
                                                                    false,
                                                                    null,
                                                                    methodName));
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
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for plus identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementsResponse   getSchemaTypeByName(String          serverName,
                                                            String          userId,
                                                            int             startFrom,
                                                            int             pageSize,
                                                            NameRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementsResponse response = new SchemaTypeElementsResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                response.setElements(handler.getSchemaTypeByName(userId,
                                                                 null,
                                                                 requestBody.getName(),
                                                                 startFrom,
                                                                 pageSize,
                                                                 false,
                                                                 false,
                                                                 null,
                                                                 methodName));
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
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElementResponse getSchemaTypeByGUID(String                        serverName,
                                                         String                        userId,
                                                         String                        schemaTypeGUID,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaTypeElementResponse response = new SchemaTypeElementResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSchemaType(userId,
                                                          schemaTypeGUID,
                                                          guidParameterName,
                                                          false,
                                                          false,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                response.setElement(handler.getSchemaType(userId,
                                                          schemaTypeGUID,
                                                          guidParameterName,
                                                          false,
                                                          false,
                                                          null,
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
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return header for parent element (data asset, process, port) or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeaderResponse getSchemaTypeParent(String                        serverName,
                                                     String                        userId,
                                                     String                        schemaTypeGUID,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementHeaderResponse response = new ElementHeaderResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaTypeHandler<SchemaTypeElement> handler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                final String guidParameterName = "schemaTypeGUID";

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

                OMRSRepositoryHelper repositoryHelper = handler.getRepositoryHelper();

                RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(handler.getRepositoryHandler(),
                                                                                               invalidParameterHandler,
                                                                                               userId,
                                                                                               schemaTypeGUID,
                                                                                               OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                                               null,
                                                                                               null,
                                                                                               1,
                                                                                               null,
                                                                                               null,
                                                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                                                               null,
                                                                                               false,
                                                                                               false,
                                                                                               0,
                                                                                               invalidParameterHandler.getMaxPagingSize(),
                                                                                               requestBody.getEffectiveTime(),
                                                                                               methodName);

                while (iterator.moreToReceive())
                {
                    Relationship relationship = iterator.getNext();

                    if ((relationship != null) && (relationship.getType() != null) &&
                                ((repositoryHelper.isTypeOf(handler.getServiceName(),
                                                            relationship.getType().getTypeDefName(),
                                                            OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME)) ||
                                         (repositoryHelper.isTypeOf(handler.getServiceName(),
                                                                    relationship.getType().getTypeDefName(),
                                                                    OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME))))
                    {
                        final String parentGUIDParameterName = "relationship.getEntityOneProxy().getGUID()";

                        EntityDetail parentEntity = handler.getEntityFromRepository(userId,
                                                                                    relationship.getEntityOneProxy().getGUID(),
                                                                                    parentGUIDParameterName,
                                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    false,
                                                                                    false,
                                                                                    requestBody.getEffectiveTime(),
                                                                                    methodName);

                        ElementHeaderConverter<ElementHeader> headerConverter = new ElementHeaderConverter<>(repositoryHelper, handler.getServiceName(), serverName);
                        response.setElement(headerConverter.getNewBean(ElementHeader.class, parentEntity, methodName));
                    }
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


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */


    /**
     * Adds attributes to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     * The schema type may be attached both directly or indirectly via nested schema elements to the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param requestBody list of schema attribute objects.
     *
     * @return list of unique identifier for the new schema attribute returned in the same order as the supplied attribute or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSchemaAttributes(String                          serverName,
                                            String                          userId,
                                            String                          assetGUID,
                                            String                          parentGUID,
                                            List<SchemaAttributeProperties> requestBody)
    {
        final String methodName = "addSchemaAttributes";
        final String anchorGUIDParameterName     = "assetGUID";
        final String schemaElementGUIDParameterName = "parentGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (! requestBody.isEmpty()))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                for (SchemaAttributeProperties schemaAttributeProperties : requestBody)
                {
                    SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                                   schemaAttributeProperties,
                                                                                                   handler.getRepositoryHelper(),
                                                                                                   handler.getServerName(),
                                                                                                   methodName);

                    if (assetGUID != null)
                    {
                        EntityDetail anchorEntity = handler.getEntityFromRepository(userId,
                                                                                    assetGUID,
                                                                                    anchorGUIDParameterName,
                                                                                    OpenMetadataType.ASSET.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    false,
                                                                                    false,
                                                                                    null,
                                                                                    methodName);

                        if (anchorEntity != null)
                        {
                            schemaAttributeBuilder.setAnchors(userId,
                                                              assetGUID,
                                                              anchorEntity.getType().getTypeDefName(),
                                                              OpenMetadataType.ASSET.typeName,
                                                              methodName);
                        }
                    }

                    handler.createNestedSchemaAttribute(userId,
                                                        null,
                                                        null,
                                                        parentGUID,
                                                        schemaElementGUIDParameterName,
                                                        schemaAttributeProperties.getQualifiedName(),
                                                        qualifiedNameParameterName,
                                                        schemaAttributeBuilder,
                                                        schemaAttributeProperties.getEffectiveFrom(),
                                                        schemaAttributeProperties.getEffectiveTo(),
                                                        false,
                                                        false,
                                                        null,
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
     * Adds attributes to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     * The schema type may be attached both directly or indirectly via nested schema elements to the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param requestBody schema attribute object.
     *
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse addSchemaAttribute(String                     serverName,
                                           String                     userId,
                                           String                     assetGUID,
                                           String                     parentGUID,
                                           SchemaAttributeProperties  requestBody)
    {
        final String methodName = "addSchemaAttribute";
        final String anchorGUIDParameterName     = "assetGUID";
        final String schemaElementGUIDParameterName = "parentGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                               requestBody,
                                                                                               handler.getRepositoryHelper(),
                                                                                               handler.getServerName(),
                                                                                               methodName);

                if (assetGUID != null)
                {
                    EntityDetail anchorEntity = handler.getEntityFromRepository(userId,
                                                                                assetGUID,
                                                                                anchorGUIDParameterName,
                                                                                OpenMetadataType.ASSET.typeName,
                                                                                null,
                                                                                null,
                                                                                false,
                                                                                false,
                                                                                null,
                                                                                methodName);

                    if (anchorEntity != null)
                    {
                        schemaAttributeBuilder.setAnchors(userId,
                                                          assetGUID,
                                                          anchorEntity.getType().getTypeDefName(),
                                                          OpenMetadataType.ASSET.typeName,
                                                          methodName);
                    }
                }

                response.setGUID(handler.createNestedSchemaAttribute(userId,
                                                                     null,
                                                                     null,
                                                                     parentGUID,
                                                                     schemaElementGUIDParameterName,
                                                                     requestBody.getQualifiedName(),
                                                                     qualifiedNameParameterName,
                                                                     schemaAttributeBuilder,
                                                                     requestBody.getEffectiveFrom(),
                                                                     requestBody.getEffectiveTo(),
                                                                     false,
                                                                     false,
                                                                     null,
                                                                     methodName));
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
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSchemaAttributeFromTemplate(String              serverName,
                                                          String              userId,
                                                          String              schemaElementGUID,
                                                          String              templateGUID,
                                                          TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaAttributeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                final String schemaElementGUIDParameterName = "schemaElementGUID";
                final String templateGUIDParameterName   = "templateGUID";
                final String propertiesParameterName     = "templateProperties";
                final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
                invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
                invalidParameterHandler.validateObject(requestBody.getElementProperties(), propertiesParameterName, methodName);
                invalidParameterHandler.validateName(requestBody.getElementProperties().getQualifiedName(), qualifiedNameParameterName, methodName);


                response.setGUID(handler.createSchemaAttributeFromTemplate(userId,
                                                                           null,
                                                                           null,
                                                                           schemaElementGUID,
                                                                           schemaElementGUIDParameterName,
                                                                           templateGUID,
                                                                           requestBody.getElementProperties().getQualifiedName(),
                                                                           requestBody.getElementProperties().getDisplayName(),
                                                                           requestBody.getElementProperties().getDescription(),
                                                                           false,
                                                                           false,
                                                                           null,
                                                                           methodName));
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
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the schema attribute
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateSchemaAttribute(String                     serverName,
                                              String                     userId,
                                              String                     schemaAttributeGUID,
                                              boolean                    isMergeUpdate,
                                              SchemaAttributeProperties requestBody)
    {
        final String methodName  = "updateSchemaAttribute";
        final String qualifiedNameParameterName       = "schemaAttributeProperties.qualifiedName";
        final String propertiesParameterName          = "schemaAttributeProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
                invalidParameterHandler.validateObject(requestBody, propertiesParameterName, methodName);
                if (! isMergeUpdate)
                {
                    invalidParameterHandler.validateName(requestBody.getQualifiedName(), qualifiedNameParameterName, methodName);
                }

                SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                               requestBody,
                                                                                               handler.getRepositoryHelper(),
                                                                                               serverName,
                                                                                               methodName);
                handler.updateSchemaAttribute(userId,
                                              null,
                                              null,
                                              schemaAttributeGUID,
                                              schemaAttributeGUIDParameterName,
                                              requestBody.getQualifiedName(),
                                              qualifiedNameParameterName,
                                              schemaAttributeBuilder,
                                              schemaAttributeBuilder.getTypeName(),
                                              isMergeUpdate,
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
     * Return a schema attribute builder packed with the knowledge of the schema attribute from the schema attribute properties
     *
     * @param userId calling user
     * @param schemaAttributeProperties properties from the caller
     * @param repositoryHelper utility methods
     * @param serverName name of called server
     * @param methodName calling method
     *
     * @return schema attributes properties in a builder
     * @throws InvalidParameterException schema type is invalid
     */
    private SchemaAttributeBuilder getSchemaAttributeBuilder(String                    userId,
                                                             SchemaAttributeProperties schemaAttributeProperties,
                                                             OMRSRepositoryHelper      repositoryHelper,
                                                             String                    serverName,
                                                             String                    methodName) throws InvalidParameterException
    {
        String typeName = OpenMetadataType.SCHEMA_ATTRIBUTE.typeName;

        if (schemaAttributeProperties.getTypeName() != null)
        {
            typeName = schemaAttributeProperties.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                   instanceHandler.getServiceName(),
                                                                   methodName,
                                                                   repositoryHelper);

        int sortOrder = 0;
        if (schemaAttributeProperties.getSortOrder() != null)
        {
            sortOrder = schemaAttributeProperties.getSortOrder().getOrdinal();
        }

        SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(schemaAttributeProperties.getQualifiedName(),
                                                                                   schemaAttributeProperties.getDisplayName(),
                                                                                   schemaAttributeProperties.getDescription(),
                                                                                   schemaAttributeProperties.getElementPosition(),
                                                                                   schemaAttributeProperties.getMinCardinality(),
                                                                                   schemaAttributeProperties.getMaxCardinality(),
                                                                                   schemaAttributeProperties.getIsDeprecated(),
                                                                                   schemaAttributeProperties.getDefaultValueOverride(),
                                                                                   schemaAttributeProperties.getAllowsDuplicateValues(),
                                                                                   schemaAttributeProperties.getOrderedValues(),
                                                                                   sortOrder,
                                                                                   schemaAttributeProperties.getMinimumLength(),
                                                                                   schemaAttributeProperties.getLength(),
                                                                                   schemaAttributeProperties.getPrecision(),
                                                                                   schemaAttributeProperties.getIsNullable(),
                                                                                   schemaAttributeProperties.getNativeJavaClass(),
                                                                                   schemaAttributeProperties.getAliases(),
                                                                                   schemaAttributeProperties.getAdditionalProperties(),
                                                                                   typeGUID,
                                                                                   typeName,
                                                                                   schemaAttributeProperties.getExtendedProperties(),
                                                                                   repositoryHelper,
                                                                                   instanceHandler.getServiceName(),
                                                                                   serverName);

        if (schemaAttributeProperties.getSchemaType() != null)
        {
            SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaAttributeProperties.getSchemaType(),
                                                                            repositoryHelper,
                                                                            instanceHandler.getServiceName(),
                                                                            serverName,
                                                                            methodName);

            schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);
        }

        return schemaAttributeBuilder;
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setSchemaElementAsCalculatedValue(String                                   serverName,
                                                          String                                   userId,
                                                          String                                   schemaElementGUID,
                                                          CalculatedValueClassificationRequestBody requestBody)
    {
        final String methodName = "setSchemaElementAsCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);


            if (requestBody != null)
            {
                InstanceProperties properties = instanceHandler.getRepositoryHelper(userId, serverName, methodName).addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                                                                                                null,
                                                                                                                                                OpenMetadataProperty.FORMULA.name,
                                                                                                                                                requestBody.getFormula(),
                                                                                                                                                methodName);
                handler.setClassificationInRepository(userId,
                                                      null,
                                                      null,
                                                      schemaElementGUID,
                                                      schemaElementGUIDParameterName,
                                                      OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                      OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                      OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                      properties,
                                                      false,
                                                      false,
                                                      false,
                                                      requestBody.getEffectiveTime(),
                                                      methodName);
            }
            else
            {
                handler.setClassificationInRepository(userId,
                                                      null,
                                                      null,
                                                      schemaElementGUID,
                                                      schemaElementGUIDParameterName,
                                                      OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                      OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                      OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                      null,
                                                      false,
                                                      false,
                                                      false,
                                                      null,
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
     * Remove the calculated value designation from the schema element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSchemaElementAsCalculatedValue(String            serverName,
                                                            String            userId,
                                                            String            schemaElementGUID,
                                                            UpdateRequestBody requestBody)
    {
        final String methodName = "clearSchemaElementAsCalculatedValue";
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeClassificationFromRepository(userId,
                                                           null,
                                                           null,
                                                           schemaElementGUID,
                                                           schemaElementGUIDParameterName,
                                                           OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                           OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
            }
            else
            {
                handler.removeClassificationFromRepository(userId,
                                                           null,
                                                           null,
                                                           schemaElementGUID,
                                                           schemaElementGUIDParameterName,
                                                           OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                           OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                           false,
                                                           false,
                                                           null,
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
     * Remove the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSchemaAttribute(String            serverName,
                                              String            userId,
                                              String            schemaAttributeGUID,
                                              UpdateRequestBody requestBody)
    {
        final String methodName = "removeSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                handler.deleteBeanInRepository(userId,
                                               null,
                                               null,
                                               schemaAttributeGUID,
                                               schemaAttributeGUIDParameterName,
                                               OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                               OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
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
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse findSchemaAttributes(String                  serverName,
                                                         String                  userId,
                                                         int                     startFrom,
                                                         int                     pageSize,
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findSchemaAttributes";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                response.setElements(handler.findSchemaAttributes(userId,
                                                                  requestBody.getSearchString(),
                                                                  searchStringParameterName,
                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                  null,
                                                                  null,
                                                                  startFrom,
                                                                  pageSize,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  methodName));
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
     * Retrieve the list of schema attributes associated with a schemaType.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse getNestedAttributes(String                        serverName,
                                                        String                        userId,
                                                        String                        schemaTypeGUID,
                                                        int                           startFrom,
                                                        int                           pageSize,
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNestedAttributes";
        final String elementGUIDParameterName    = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                response.setElements(handler.getAttachedSchemaAttributes(userId,
                                                                         schemaTypeGUID,
                                                                         elementGUIDParameterName,
                                                                         OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         null,
                                                                         methodName));
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
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributesResponse getSchemaAttributesByName(String          serverName,
                                                              String          userId,
                                                              int             startFrom,
                                                              int             pageSize,
                                                              NameRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                response.setElements(handler.getSchemaAttributesByName(userId,
                                                                       OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                                       OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                       requestBody.getName(),
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       null,
                                                                       methodName));
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
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeResponse getSchemaAttributeByGUID(String                        serverName,
                                                            String                        userId,
                                                            String                        schemaAttributeGUID,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributeByGUID";
        final String guidParameterName = "schemaAttributeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SchemaAttributeResponse response = new SchemaAttributeResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

                response.setElement(handler.getSchemaAttribute(userId,
                                                               schemaAttributeGUID,
                                                               guidParameterName,
                                                               OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                               null,
                                                               null,
                                                               false,
                                                               false,
                                                               null,
                                                               methodName));
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
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param requestBody request body including a summary and connection object.
     *                   If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addConnectionToAsset(String                   serverName,
                                             String                   userId,
                                             String                   assetGUID,
                                             OCFConnectionRequestBody requestBody)
    {
        final String   methodName = "addConnectionToAsset";
        final String   assetGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                String     assetSummary = requestBody.getShortDescription();
                Connection connection   = requestBody.getConnection();

                if (connection != null)
                {
                    ConnectionHandler<ConnectionElement> connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                    connectionHandler.saveConnection(userId,
                                                     null,
                                                     null,
                                                     assetGUID,
                                                     assetGUID,
                                                     assetGUIDParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     null,
                                                     connection,
                                                     assetSummary,
                                                     false,
                                                     false,
                                                     connectionHandler.getSupportedZones(),
                                                     new Date(),
                                                     methodName);
                }
                else
                {
                    final String connectionParameterName = "requestBody.getConnection()";
                    restExceptionHandler.handleMissingValue(connectionParameterName, methodName);
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


    /*
     * ==============================================
     * AssetClassificationInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between a glossary term and an Asset description.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody null request body to satisfy POST request.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  addSemanticAssignment(String                       serverName,
                                               String                       userId,
                                               String                       assetGUID,
                                               String                       glossaryTermGUID,
                                               SemanticAssignmentProperties requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   glossaryTermGUIDParameterName = "glossaryTermGUID";
        final String   methodName = "addSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.saveSemanticAssignment(userId,
                                               null,
                                               null,
                                               assetGUID,
                                               assetGUIDParameterName,
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else if (requestBody.getStatus() == null)
            {
                handler.saveSemanticAssignment(userId,
                                               null,
                                               null,
                                               assetGUID,
                                               assetGUIDParameterName,
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               requestBody.getDescription(),
                                               requestBody.getExpression(),
                                               3,
                                               requestBody.getConfidence(),
                                               requestBody.getCreatedBy(),
                                               requestBody.getSteward(),
                                               requestBody.getSource(),
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                handler.saveSemanticAssignment(userId,
                                               null,
                                               null,
                                               assetGUID,
                                               assetGUIDParameterName,
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               requestBody.getDescription(),
                                               requestBody.getExpression(),
                                               requestBody.getStatus().getOrdinal(),
                                               requestBody.getConfidence(),
                                               requestBody.getCreatedBy(),
                                               requestBody.getSteward(),
                                               requestBody.getSource(),
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
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody null request body to satisfy POST request.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  addSemanticAssignment(String          serverName,
                                               String          userId,
                                               String          assetGUID,
                                               String          glossaryTermGUID,
                                               String          assetElementGUID,
                                               NullRequestBody requestBody)
    {
        final String   assetElementGUIDParameterName = "assetElementGUID";
        final String   glossaryTermGUIDParameterName = "glossaryTermGUID";
        final String   methodName = "addSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.saveSemanticAssignment(userId,
                                           null,
                                           null,
                                           assetElementGUID,
                                           assetElementGUIDParameterName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  removeSemanticAssignment(String          serverName,
                                                  String          userId,
                                                  String          assetGUID,
                                                  String          glossaryTermGUID,
                                                  NullRequestBody requestBody)
    {
        final String methodName = "removeSemanticAssignment";
        final String assetGUIDParameterName = "assetGUID";
        final String glossaryTermGUIDParameterName = "glossaryTermGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.removeSemanticAssignment(userId,
                                             null,
                                             null,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             glossaryTermGUID,
                                             glossaryTermGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  removeSemanticAssignment(String          serverName,
                                                  String          userId,
                                                  String          assetGUID,
                                                  String          glossaryTermGUID,
                                                  String          assetElementGUID,
                                                  NullRequestBody requestBody)
    {
        final String methodName = "removeSemanticAssignment";
        final String assetElementGUIDParameterName = "assetElementGUID";
        final String glossaryTermGUIDParameterName = "glossaryTermGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.removeSemanticAssignment(userId,
                                             null,
                                             null,
                                             assetElementGUID,
                                             assetElementGUIDParameterName,
                                             glossaryTermGUID,
                                             glossaryTermGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Set up the labels that classify an asset's origin.  This will override any existing value.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody Descriptive labels describing origin of the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  addAssetOrigin(String            serverName,
                                        String            userId,
                                        String            assetGUID,
                                        OriginRequestBody requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   organizationGUIDParameterName = "organizationGUID";
        final String   businessCapabilityGUIDParameterName = "businessCapabilityGUID";
        final String   methodName = "addAssetOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement>  handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                handler.addAssetOrigin(userId,
                                       assetGUID,
                                       assetGUIDParameterName,
                                       requestBody.getOrganizationGUID(),
                                       organizationGUIDParameterName,
                                       requestBody.getBusinessCapabilityGUID(),
                                       businessCapabilityGUIDParameterName,
                                       requestBody.getOtherOriginValues(),
                                       null,
                                       null,
                                       true,
                                       false,
                                       false,
                                       new Date(),
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
     * Remove the asset origin classification to an asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  removeAssetOrigin(String                serverName,
                                           String                userId,
                                           String                assetGUID,
                                           NullRequestBody       requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "removeAssetOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                handler.removeAssetOrigin(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
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
     * Update the zones for a specific asset to the zone list specified in the publishZones
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishAsset(String          serverName,
                                     String          userId,
                                     String          assetGUID,
                                     NullRequestBody requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "publishAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.publishAsset(userId,
                                 assetGUID,
                                 assetGUIDParameterName,
                                 false,
                                 false,
                                 new Date(),
                                 methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the zones for a specific asset to the zone list specified in the defaultZones
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawAsset(String          serverName,
                                      String          userId,
                                      String          assetGUID,
                                      NullRequestBody requestBody)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "withdrawAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  false,
                                  false,
                                  new Date(),
                                  methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the zones for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateAssetZones(String        serverName,
                                         String        userId,
                                         String        assetGUID,
                                         List<String>  assetZones)
    {
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "updateAssetZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.updateAssetZones(userId,
                                     assetGUID,
                                     assetGUIDParameterName,
                                     assetZones,
                                     true,
                                     false,
                                     false,
                                     new Date(),
                                     methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the owner information for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody values describing the new owner
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  updateAssetOwner(String           serverName,
                                          String           userId,
                                          String           assetGUID,
                                          OwnerRequestBody requestBody)
    {
        final String assetGUIDParameterName = "assetGUID";
        final String methodName = "updateAssetOwner";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                handler.addOwner(userId,
                                 assetGUID,
                                 assetGUIDParameterName,
                                 OpenMetadataType.ASSET.typeName,
                                 requestBody.getOwnerId(),
                                 requestBody.getOwnerTypeName(),
                                 requestBody.getOwnerPropertyName(),
                                 false,
                                 false,
                                 new Date(),
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
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse  addSecurityTags(String                  serverName,
                                         String                  userId,
                                         String                  assetGUID,
                                         SecurityTagsRequestBody requestBody)
    {
        final String methodName = "addSecurityTags";
        final String assetGUIDParameterName = "assetGUID";
        final String assetTypeName = "Asset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                handler.addSecurityTags(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        assetTypeName,
                                        requestBody.getSecurityLabels(),
                                        requestBody.getSecurityProperties(),
                                        requestBody.getAccessGroups(),
                                        false,
                                        false,
                                        new Date(),
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
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  addSecurityTags(String                  serverName,
                                         String                  userId,
                                         String                  assetGUID,
                                         String                  assetElementGUID,
                                         SecurityTagsRequestBody requestBody)
    {
        final String assetElementGUIDParameterName = "assetElementGUID";
        final String assetElementTypeName          = "Referenceable";
        final String methodName                    = "addSecurityTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                handler.addSecurityTags(userId,
                                        assetElementGUID,
                                        assetElementGUIDParameterName,
                                        assetElementTypeName,
                                        requestBody.getSecurityLabels(),
                                        requestBody.getSecurityProperties(),
                                        requestBody.getAccessGroups(),
                                        false,
                                        false,
                                        new Date(),
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
     * Remove the security tags classification from an asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  removeSecurityTags(String                serverName,
                                            String                userId,
                                            String                assetGUID,
                                            NullRequestBody       requestBody)
    {
        final String methodName = "removeSecurityTags";
        final String assetGUIDParameterName = "assetGUID";
        final String assetTypeName = "Asset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.removeSecurityTags(userId,
                                       assetGUID,
                                       assetGUIDParameterName,
                                       assetTypeName,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the security tags classification to one of an asset's elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element where the security tags need to be removed.
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse  removeSecurityTags(String                serverName,
                                            String                userId,
                                            String                assetGUID,
                                            String                assetElementGUID,
                                            NullRequestBody       requestBody)
    {
        final String methodName                    = "removeSecurityTags";
        final String assetElementGUIDParameterName = "assetElementGUID";
        final String assetElementTypeName          = "Referenceable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.removeSecurityTags(userId,
                                       assetElementGUID,
                                       assetElementGUIDParameterName,
                                       assetElementTypeName,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to classify
     * @param requestBody  properties of the template
     *
     * @return void or
     *  InvalidParameterException asset or element not known, null userId or guid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse addTemplateClassification(String                            serverName,
                                                  String                            userId,
                                                  String                            assetGUID,
                                                  TemplateClassificationRequestBody requestBody)
    {
        final String methodName = "addTemplateClassification";
        final String assetGUIDParameterName = "assetGUID";
        final String assetTypeName = "Asset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addTemplateClassification(userId,
                                                  assetGUID,
                                                  assetGUIDParameterName,
                                                  assetTypeName,
                                                  requestBody.getName(),
                                                  requestBody.getVersionIdentifier(),
                                                  requestBody.getDescription(),
                                                  requestBody.getAdditionalProperties(),
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
            }
            else
            {
                handler.addTemplateClassification(userId,
                                                  assetGUID,
                                                  assetGUIDParameterName,
                                                  assetTypeName,
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
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to declassify
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException asset or element not known, null userId or guid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeTemplateClassification(String          serverName,
                                                     String          userId,
                                                     String          assetGUID,
                                                     NullRequestBody requestBody)
    {
        final String methodName = "removeTemplateClassification";
        final String assetGUIDParameterName = "assetGUID";
        final String assetTypeName = "Asset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ReferenceableElement> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.removeTemplateClassification(userId,
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 assetTypeName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ==============================================
     * AssetReviewInterface
     * ==============================================
     */


    /**
     * Return a list of assets with the requested name.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of Asset summaries or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetElementsResponse getAssetsByName(String          serverName,
                                                 String          userId,
                                                 NameRequestBody requestBody,
                                                 int             startFrom,
                                                 int             pageSize)
    {
        final String nameParameterName = "name";
        final String methodName        = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetElementsResponse response = new AssetElementsResponse();
        AuditLog              auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                List<AssetElement> assets = handler.getAssetsByName(userId,
                                                                    OpenMetadataType.ASSET.typeGUID,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    requestBody.getName(),
                                                                    nameParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

                addSupplementaryProperties(assets, handler, methodName);
                response.setElements(assets);
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
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetElementsResponse findAssets(String                  serverName,
                                            String                  userId,
                                            SearchStringRequestBody requestBody,
                                            int                     startFrom,
                                            int                     pageSize)
    {
        final String searchStringParameter = "searchString";
        final String methodName            = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetElementsResponse response = new AssetElementsResponse();
        AuditLog              auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                List<AssetElement> assets = handler.findAssets(userId,
                                                               requestBody.getSearchString(),
                                                               searchStringParameter,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName);

                addSupplementaryProperties(assets, handler, methodName);
                response.setElements(assets);
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
     * Return the basic attributes of an asset.
     *
     * @param serverName server called
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return basic asset properties
     * InvalidParameterException one of the parameters is null or invalid.
     * UserNotAuthorizedException user not authorized to issue this request.
     * PropertyServerException there was a problem that occurred within the property server.
     */
    public AssetElementResponse getAssetSummary(String  serverName,
                                                String  userId,
                                                String  assetGUID)
    {
        final String methodName = "getAssetSummary";
        final String assetGUIDParameter = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetElementResponse response = new AssetElementResponse();
        AuditLog             auditLog = null;

        try
        {
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetElement asset = handler.getBeanFromRepository(userId,
                                                               assetGUID,
                                                               assetGUIDParameter,
                                                               OpenMetadataType.ASSET.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName);

            if (asset != null)
            {
                getSupplementaryProperties(assetGUID,
                                           assetGUIDParameter,
                                           asset.getProperties(),
                                           handler,
                                           handler.getRepositoryHelper(),
                                           handler.getServiceName(),
                                           methodName);
            }

            response.setElement(asset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maxPageSize maximum number of elements to return on this call
     *
     * @return list of discovery analysis reports or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public SurveyReportsResponse getSurveyReports(String  serverName,
                                                  String  userId,
                                                  String  assetGUID,
                                                  int     startingFrom,
                                                  int     maxPageSize)
    {
        final String   methodName = "getSurveyReports";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SurveyReportsResponse response = new SurveyReportsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SurveyReportHandler<SurveyReport> handler = instanceHandler.getSurveyReportHandler(userId, serverName, methodName);

            response.setSurveyReports(handler.getSurveyReports(userId,
                                                               assetGUID,
                                                               startingFrom,
                                                               maxPageSize,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param surveyReportGUID identifier of the survey report.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of annotations or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public AnnotationsResponse getSurveyReportAnnotations(String            serverName,
                                                          String            userId,
                                                          String            surveyReportGUID,
                                                          int               startingFrom,
                                                          int               maximumResults,
                                                          AnnotationStatusRequestBody requestBody)
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationsResponse response = new AnnotationsResponse();
        AuditLog            auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AnnotationHandler<Annotation> handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

                int annotationStatus = AnnotationStatus.UNKNOWN_STATUS.getOrdinal();

                if (requestBody.getAnnotationStatus() != null)
                {
                    annotationStatus = requestBody.getAnnotationStatus().getOrdinal();
                }
                response.setElements(handler.getSurveyReportAnnotations(userId,
                                                                        surveyReportGUID,
                                                                        annotationStatus,
                                                                        startingFrom,
                                                                        maximumResults,
                                                                        methodName));
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
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of Annotation objects or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public AnnotationsResponse getExtendedAnnotations(String            serverName,
                                                      String            userId,
                                                      String            annotationGUID,
                                                      int               startingFrom,
                                                      int               maximumResults,
                                                      AnnotationStatusRequestBody requestBody)
    {
        final String   methodName = "getExtendedAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationsResponse response = new AnnotationsResponse();
        AuditLog            auditLog = null;

        AnnotationStatus annotationStatus = AnnotationStatus.UNKNOWN_STATUS;
        if (requestBody != null)
        {
            annotationStatus = requestBody.getAnnotationStatus();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler<Annotation> handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            response.setElements(handler.getExtendedAnnotations(userId,
                                                                annotationGUID,
                                                                annotationStatus.getOrdinal(),
                                                                startingFrom,
                                                                maximumResults,
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * ==============================================
     * AssetDecommissioningInterface
     * ==============================================
     */


    /**
     * Deletes an asset and all of its associated elements such as schema, connections (unless they are linked to
     * another asset), discovery reports and associated feedback.
     * Given the depth of the delete request performed by this call, it should be used with care.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException full path or userId is null or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteAsset(String          serverName,
                                    String          userId,
                                    String          assetGUID,
                                    NullRequestBody requestBody)
    {
        final String methodName = "deleteAsset";
        final String assetGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataType.ASSET.typeGUID,
                                           OpenMetadataType.ASSET.typeName,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /*
     * ==============================================
     * AssetDuplicateManagementInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse linkElementsAsPeerDuplicates(String          serverName,
                                                     String          userId,
                                                     String          element1GUID,
                                                     String          element2GUID,
                                                     NullRequestBody requestBody)
    {
        final String methodName = "linkElementsAsPeerDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.linkElementsAsPeerDuplicates(userId,
                                                 element1GUID,
                                                 element1GUIDParameter,
                                                 element2GUID,
                                                 element2GUIDParameter,
                                                 true,
                                                 1,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                 methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unlinkElementsAsPeerDuplicates(String          serverName,
                                                       String          userId,
                                                       String          element1GUID,
                                                       String          element2GUID,
                                                       NullRequestBody requestBody)
    {
        final String methodName = "unlinkElementsAsPeerDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.unlinkElementsAsPeerDuplicates(userId,
                                                   element1GUID,
                                                   element1GUIDParameter,
                                                   element2GUID,
                                                   element2GUIDParameter,
                                                   instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                   methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* ========================================================
     * Managing the supplementary properties for technical metadata (assets, software server capabilities etc).
     */


    /**
     * Maintain the supplementary properties in a glossary term linked to the supplied element.
     * The glossary term needs to be connected to the
     * @param userId calling user
     * @param elementGUID unique identifier for the element connected to the supplementary properties
     * @param elementGUIDParameterName name of guid parameter
     * @param elementTypeName type of element
     * @param elementDomainName type of element
     * @param elementQualifiedName unique name for the element connected to the supplementary properties
     * @param supplementaryProperties properties to save
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param effectiveTime time to issue queries for
     * @param assetHandler handler for working with the metadata store
     * @param methodName calling method
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    void maintainSupplementaryProperties(String                     userId,
                                         String                     elementGUID,
                                         String                     elementGUIDParameterName,
                                         String                     elementTypeName,
                                         String                     elementDomainName,
                                         String                     elementQualifiedName,
                                         SupplementaryProperties supplementaryProperties,
                                         boolean                    isMergeUpdate,
                                         Date                       effectiveTime,
                                         AssetHandler<AssetElement> assetHandler,
                                         String                     methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        if (supplementaryProperties != null)
        {
            assetHandler.maintainSupplementaryProperties(userId,
                                                         elementGUID,
                                                         elementGUIDParameterName,
                                                         elementTypeName,
                                                         elementDomainName,
                                                         elementQualifiedName,
                                                         supplementaryProperties.getDisplayName(),
                                                         supplementaryProperties.getDisplaySummary(),
                                                         supplementaryProperties.getDisplayDescription(),
                                                         supplementaryProperties.getAbbreviation(),
                                                         supplementaryProperties.getUsage(),
                                                         isMergeUpdate,
                                                         false,
                                                         false,
                                                         effectiveTime,
                                                         methodName);
        }
        else if (! isMergeUpdate)
        {
            assetHandler.maintainSupplementaryProperties(userId,
                                                         elementGUID,
                                                         elementGUIDParameterName,
                                                         elementTypeName,
                                                         elementDomainName,
                                                         elementQualifiedName,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         false,
                                                         false,
                                                         false,
                                                         effectiveTime,
                                                         methodName);
        }
    }


    /**
     * Extract any supplementary properties from the connected glossary term.
     *
     * @param assets returned assets
     * @param assetHandler asset handler
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem calling the server
     */
    void addSupplementaryProperties(List<AssetElement>         assets,
                                    AssetHandler<AssetElement> assetHandler,
                                    String                     methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if ((assets != null) && (! assets.isEmpty()))
        {
            for (AssetElement element : assets)
            {
                getSupplementaryProperties(element.getElementHeader().getGUID(),
                                           elementGUIDParameterName,
                                           element.getProperties(),
                                           assetHandler,
                                           assetHandler.getRepositoryHelper(),
                                           assetHandler.getServiceName(),
                                           methodName);
            }
        }
    }

    /**
     * Retrieve the supplementary properties for an element.
     *
     * @param elementGUID unique identifier for the element connected to the supplementary properties
     * @param elementGUIDParameterName name of guid parameter
     * @param supplementaryProperties properties to save
     * @param assetHandler handler for working with the metadata store
     * @param repositoryHelper repository helper
     * @param serviceName name of this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem calling the server
     */
    private void getSupplementaryProperties(String                     elementGUID,
                                            String                     elementGUIDParameterName,
                                            SupplementaryProperties    supplementaryProperties,
                                            AssetHandler<AssetElement> assetHandler,
                                            OMRSRepositoryHelper       repositoryHelper,
                                            String                     serviceName,
                                            String                     methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        EntityDetail glossaryEntity = assetHandler.getSupplementaryProperties(elementGUID,
                                                                              elementGUIDParameterName,
                                                                              OpenMetadataType.ASSET.typeName,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

        if ((glossaryEntity != null) && (glossaryEntity.getProperties() != null))
        {
            supplementaryProperties.setDisplayName(repositoryHelper.getStringProperty(serviceName,
                                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                      glossaryEntity.getProperties(),
                                                                                      methodName));

            supplementaryProperties.setDisplaySummary(repositoryHelper.getStringProperty(serviceName,
                                                                                         OpenMetadataProperty.SUMMARY.name,
                                                                                         glossaryEntity.getProperties(),
                                                                                         methodName));
            supplementaryProperties.setDisplayDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                                                             glossaryEntity.getProperties(),
                                                                                             methodName));
            supplementaryProperties.setAbbreviation(repositoryHelper.getStringProperty(serviceName,
                                                                                       OpenMetadataProperty.ABBREVIATION.name,
                                                                                       glossaryEntity.getProperties(),
                                                                                       methodName));
            supplementaryProperties.setUsage(repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.USAGE.name,
                                                                                glossaryEntity.getProperties(),
                                                                                methodName));
        }
    }
}
