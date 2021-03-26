/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ReferenceableElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.*;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetOwnerRESTServices provides part of the server-side support for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other REST services that provide specialized methods for specific types of Asset.
 */
public class AssetOwnerRESTServices
{
    private static AssetOwnerInstanceHandler   instanceHandler      = new AssetOwnerInstanceHandler();
    private static RESTExceptionHandler        restExceptionHandler = new RESTExceptionHandler();
    private static RESTCallLogger              restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetOwnerRESTServices.class),
                                                                                         instanceHandler.getServiceName());

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

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
        final String methodName                 = "addAssetToCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

                if (typeName != null)
                {
                    assetTypeName = typeName;
                }

                String assetTypeGUID = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                instanceHandler.getServiceName(),
                                                                                methodName,
                                                                                instanceHandler.getRepositoryHelper(userId, serverName, methodName));

                int ownerTypeOrdinal = 0;

                if (requestBody.getOwnerType() != null)
                {
                    ownerTypeOrdinal = requestBody.getOwnerType().getOpenTypeOrdinal();
                }
                response.setGUID(handler.createAssetInRepository(userId,
                                                                 null,
                                                                 null,
                                                                 requestBody.getQualifiedName(),
                                                                 requestBody.getDisplayName(),
                                                                 requestBody.getDescription(),
                                                                 requestBody.getZoneMembership(),
                                                                 requestBody.getOwner(),
                                                                 ownerTypeOrdinal,
                                                                 requestBody.getOriginOrganizationGUID(),
                                                                 requestBody.getOriginBusinessCapabilityGUID(),
                                                                 requestBody.getOtherOriginValues(),
                                                                 requestBody.getAdditionalProperties(),
                                                                 assetTypeGUID,
                                                                 assetTypeName,
                                                                 requestBody.getExtendedProperties(),
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
                                                                 OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                 requestBody.getQualifiedName(),
                                                                 qualifiedNameParameterName,
                                                                 requestBody.getDisplayName(),
                                                                 requestBody.getDescription(),
                                                                 requestBody.getNetworkAddress(),
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
    @Deprecated
    public GUIDResponse   addSchemaTypeToAsset(String                serverName,
                                               String                userId,
                                               String                assetGUID,
                                               SchemaTypeRequestBody requestBody)
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
                if (requestBody.getSchemaTypeProperties() != null)
                {
                    response.setGUID(this.addAssociatedSchemaType(userId,
                                                                  serverName,
                                                                  assetGUID,
                                                                  requestBody.getSchemaTypeProperties(),
                                                                  methodName));
                }
                else
                {
                    final String parameterName = "requestBody.getSchemaTypeProperties()";

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

        if (assetGUID != null)
        {
            schemaTypeBuilder.setAnchors(userId, assetGUID, methodName);
        }

        String schemaTypeGUID = handler.addSchemaType(userId,
                                                      null,
                                                      null,
                                                      schemaTypeBuilder,
                                                      methodName);

        if (schemaTypeGUID != null)
        {
            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         assetGUID,
                                         assetGUIDParameterName,
                                         OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                         schemaTypeGUID,
                                         schemaTypeGUIDParameterName,
                                         OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                         OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                         OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                         null,
                                         methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Recursively navigate through the schema type loading up the a new schema type builder to pass to the
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
        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (schemaType.getTypeName() != null)
        {
            typeName = schemaType.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(schemaType.getQualifiedName(),
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

        if (schemaType instanceof LiteralSchemaTypeProperties)
        {
            LiteralSchemaTypeProperties literalSchemaTypeProperties = (LiteralSchemaTypeProperties)schemaType;

            schemaTypeBuilder.setDataType(literalSchemaTypeProperties.getDataType());
            schemaTypeBuilder.setFixedValue(literalSchemaTypeProperties.getFixedValue());
        }
        else if (schemaType instanceof SimpleSchemaTypeProperties)
        {
            SimpleSchemaTypeProperties simpleSchemaTypeProperties = (SimpleSchemaTypeProperties)schemaType;

            schemaTypeBuilder.setDataType(simpleSchemaTypeProperties.getDataType());
            schemaTypeBuilder.setDefaultValue(simpleSchemaTypeProperties.getDefaultValue());

            if (schemaType instanceof EnumSchemaTypeProperties)
            {
                EnumSchemaTypeProperties enumSchemaTypeProperties = (EnumSchemaTypeProperties)schemaType;

                schemaTypeBuilder.setValidValuesSetGUID(enumSchemaTypeProperties.getValidValueSetGUID());
            }
            else if (schemaType instanceof ExternalSchemaTypeProperties)
            {
                ExternalSchemaTypeProperties externalSchemaTypeProperties = (ExternalSchemaTypeProperties)schemaType;

                schemaTypeBuilder.setExternalSchemaTypeGUID(externalSchemaTypeProperties.getExternalSchemaTypeGUID());
            }
        }
        else if (schemaType instanceof SchemaTypeChoiceProperties)
        {
            SchemaTypeChoiceProperties schemaTypeChoiceProperties = (SchemaTypeChoiceProperties)schemaType;

            if (schemaTypeChoiceProperties.getSchemaOptions() != null)
            {
                List<SchemaTypeBuilder> schemaOptionBuilders = new ArrayList<>();

                for (SchemaTypeProperties schemaOption : schemaTypeChoiceProperties.getSchemaOptions())
                {
                    if (schemaOption != null)
                    {
                        schemaOptionBuilders.add(this.getSchemaTypeBuilder(schemaOption,
                                                                           repositoryHelper,
                                                                           serviceName,
                                                                           serverName,
                                                                           methodName));
                    }
                }

                if (! schemaOptionBuilders.isEmpty())
                {
                    schemaTypeBuilder.setSchemaOptions(schemaOptionBuilders);
                }
            }
        }
        else if (schemaType instanceof MapSchemaTypeProperties)
        {
            MapSchemaTypeProperties mapSchemaTypeProperties = (MapSchemaTypeProperties)schemaType;

            SchemaTypeBuilder mapFromBuilder = null;
            SchemaTypeBuilder mapToBuilder = null;

            if (mapSchemaTypeProperties.getMapFromElement() != null)
            {
                mapFromBuilder = this.getSchemaTypeBuilder(mapSchemaTypeProperties.getMapFromElement(),
                                                           repositoryHelper,
                                                           serviceName,
                                                           serverName,
                                                           methodName);
            }

            if (mapSchemaTypeProperties.getMapToElement() != null)
            {
                mapToBuilder = this.getSchemaTypeBuilder(mapSchemaTypeProperties.getMapToElement(),
                                                         repositoryHelper,
                                                         serviceName,
                                                         serverName,
                                                         methodName);
            }

            schemaTypeBuilder.setMapTypes(mapFromBuilder, mapToBuilder);
        }

        return schemaTypeBuilder;
    }


    /**
     * Create a schema attribute and attach it to the supplied parent schema type.  This method has 3 parts to it.
     * First to create the schema attribute.  Then to link the schema attribute to its parent schema so that the attribute
     * count value is visible as soon as possible.  Finally to determine the style of type for the attribute - is it directly linked or
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
        SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> handler =
                instanceHandler.getSchemaAttributeHandler(userId, serverName, methodName);

        String schemaAttributeGUID = null;

        if (schemaAttribute != null)
        {
            int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

            if (schemaAttribute.getSortOrder() != null)
            {
                sortOrder = schemaAttribute.getSortOrder().getOpenTypeOrdinal();
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

            if (assetGUID != null)
            {
                schemaAttributeBuilder.setAnchors(userId, assetGUID, methodName);
            }

            if (schemaAttribute.getAttributeType() != null)
            {
                SchemaTypeProperties schemaTypeProperties = schemaAttribute.getAttributeType();
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

                attributeSchemaTypeBuilder.setAnchors(userId, assetGUID, methodName);
                schemaAttributeBuilder.setSchemaType(userId, attributeSchemaTypeBuilder, methodName);

                final String schemaTypeGUIDParameterName = "schemaTypeGUID";
                final String qualifiedNameParameterName = "schemaAttribute.getQualifiedName()";

                schemaAttributeGUID = handler.createNestedSchemaAttribute(userId,
                                                                          null,
                                                                          null,
                                                                          schemaTypeGUID,
                                                                          schemaTypeGUIDParameterName,
                                                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                          schemaAttribute.getQualifiedName(),
                                                                          qualifiedNameParameterName,
                                                                          schemaAttributeBuilder,
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
     * Unlinks the schema from the asset but does not delete it.  This means it can be be reattached to a different asset.
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
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSchemaAttributes(String                      serverName,
                                            String                      userId,
                                            String                      assetGUID,
                                            String                      parentGUID,
                                            SchemaAttributesRequestBody requestBody)
    {
        final String   methodName = "addSchemaAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null)
                    && (requestBody.getSchemaAttributeProperties() != null)
                    && (! requestBody.getSchemaAttributeProperties().isEmpty()))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                for (SchemaAttributeProperties schemaAttributeProperties : requestBody.getSchemaAttributeProperties())
                {
                    this.addAssociatedSchemaAttribute(userId,
                                                      serverName,
                                                      assetGUID,
                                                      parentGUID,
                                                      schemaAttributeProperties,
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
     * @param requestBody list of schema attribute objects.
     *
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
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
        final String   methodName = "addSchemaAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (! requestBody.isEmpty()))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                for (SchemaAttributeProperties schemaAttributeProperties : requestBody)
                {
                    this.addAssociatedSchemaAttribute(userId,
                                                      serverName,
                                                      assetGUID,
                                                      parentGUID,
                                                      schemaAttributeProperties,
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
        final String   methodName = "addSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                response.setGUID(this.addAssociatedSchemaAttribute(userId,
                                                                   serverName,
                                                                   assetGUID,
                                                                   parentGUID,
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
    public VoidResponse addConnectionToAsset(String                serverName,
                                             String                userId,
                                             String                assetGUID,
                                             ConnectionRequestBody requestBody)
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
                    ConnectionHandler<OpenMetadataAPIDummyBean> connectionHandler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                    connectionHandler.saveConnection(userId,
                                                     null,
                                                     null,
                                                     assetGUID,
                                                     assetGUID,
                                                     assetGUIDParameterName,
                                                     OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                     connection,
                                                     assetSummary,
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
    @SuppressWarnings(value = "unused")
    public VoidResponse  addSemanticAssignment(String          serverName,
                                               String          userId,
                                               String          assetGUID,
                                               String          glossaryTermGUID,
                                               NullRequestBody requestBody)
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

            handler.saveSemanticAssignment(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
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
        final String   methodName = "removeSemanticAssignment";

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
                                             glossaryTermGUID,
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
        final String   methodName = "removeSemanticAssignment";

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
                                             glossaryTermGUID,
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

                handler.removeAssetOrigin(userId, assetGUID, assetGUIDParameterName, methodName);
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

            handler.publishAsset(userId, assetGUID, assetGUIDParameterName, methodName);
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

            handler.withdrawAsset(userId, assetGUID, assetGUIDParameterName, methodName);
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
        final String   assetGUIDParameterName = "assetGUID";
        final String   methodName = "updateAssetOwner";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

                int ownerType = OwnerType.USER_ID.getOpenTypeOrdinal();

                if (requestBody.getOwnerType() != null)
                {
                    ownerType = requestBody.getOwnerType().getOpenTypeOrdinal();
                }

                handler.updateAssetOwner(userId,
                                         assetGUID,
                                         assetGUIDParameterName,
                                         requestBody.getOwnerId(),
                                         ownerType,
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
                                                  requestBody.getDescription(),
                                                  requestBody.getAdditionalProperties(),
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
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of Asset summaries or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetElementsResponse getAssetsByName(String   serverName,
                                                 String   userId,
                                                 String   name,
                                                 int      startFrom,
                                                 int      pageSize)
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
            response.setAssets(handler.getAssetsByName(userId,
                                                       OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                                       OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                       name,
                                                       nameParameterName,
                                                       startFrom,
                                                       pageSize,
                                                       methodName));
            response.setStartingFromElement(startFrom);
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
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    public AssetElementsResponse findAssets(String   serverName,
                                            String   userId,
                                            String   searchString,
                                            int      startFrom,
                                            int      pageSize)
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
            response.setAssets(handler.findAssets(userId, searchString, searchStringParameter, startFrom, pageSize, methodName));
            response.setStartingFromElement(startFrom);
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
            response.setAsset(handler.getBeanFromRepository(userId,
                                                            assetGUID,
                                                            assetGUIDParameter,
                                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
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
     * Return the discovery analysis reports about the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maxPageSize maximum number of elements to return an this call
     *
     * @return list of discovery analysis reports or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public DiscoveryAnalysisReportListResponse getDiscoveryAnalysisReports(String  serverName,
                                                                           String  userId,
                                                                           String  assetGUID,
                                                                           int     startingFrom,
                                                                           int     maxPageSize)
    {
        final String   methodName = "getDiscoveryAnalysisReports";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryAnalysisReportListResponse response = new DiscoveryAnalysisReportListResponse();
        AuditLog                            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DiscoveryAnalysisReportHandler<DiscoveryAnalysisReport> handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId, serverName, methodName);

            response.setDiscoveryAnalysisReports(handler.getDiscoveryAnalysisReports(userId,
                                                                                     assetGUID,
                                                                                     startingFrom,
                                                                                     maxPageSize,
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
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of annotations or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public AnnotationListResponse getDiscoveryReportAnnotations(String            serverName,
                                                                String            userId,
                                                                String            discoveryReportGUID,
                                                                int               startingFrom,
                                                                int               maximumResults,
                                                                StatusRequestBody requestBody)
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        AuditLog               auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AnnotationHandler<Annotation> handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

                int annotationStatus = AnnotationStatus.UNKNOWN_STATUS.getOpenTypeOrdinal();

                if (requestBody.getAnnotationStatus() != null)
                {
                    annotationStatus = requestBody.getAnnotationStatus().getOpenTypeOrdinal();
                }
                response.setAnnotations(handler.getDiscoveryReportAnnotations(userId,
                                                                              discoveryReportGUID,
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
    public AnnotationListResponse  getExtendedAnnotations(String            serverName,
                                                          String            userId,
                                                          String            annotationGUID,
                                                          int               startingFrom,
                                                          int               maximumResults,
                                                          StatusRequestBody requestBody)
    {
        final String   methodName = "getExtendedAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        AuditLog               auditLog = null;

        AnnotationStatus annotationStatus = AnnotationStatus.UNKNOWN_STATUS;
        if (requestBody != null)
        {
            annotationStatus = requestBody.getAnnotationStatus();
        }

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler<Annotation> handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            response.setAnnotations(handler.getExtendedAnnotations(userId,
                                                                   annotationGUID,
                                                                   annotationStatus.getOpenTypeOrdinal(),
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
     *
     * Given the depth of the delete performed by this call, it should be used with care.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the attest to attach the connection to
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
                                           OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           null,
                                           null,
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
    public VoidResponse  linkElementsAsDuplicates(String          serverName,
                                                  String          userId,
                                                  String          element1GUID,
                                                  String          element2GUID,
                                                  NullRequestBody requestBody)
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.linkElementsAsDuplicates(userId,
                                             element1GUID,
                                             element1GUIDParameter,
                                             element2GUID,
                                             element2GUIDParameter,
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
    public VoidResponse  unlinkElementsAsDuplicates(String          serverName,
                                                    String          userId,
                                                    String          element1GUID,
                                                    String          element2GUID,
                                                    NullRequestBody requestBody)
    {
        final String methodName = "unlinkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.unlinkElementsAsDuplicates(userId,
                                               element1GUID,
                                               element1GUIDParameter,
                                               element2GUID,
                                               element2GUIDParameter,
                                               methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
