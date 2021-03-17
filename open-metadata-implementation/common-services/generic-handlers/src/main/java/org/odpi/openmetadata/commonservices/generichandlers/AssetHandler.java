/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryEntitiesIterator;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 *
 * @param <B> class that represents the asset
 */
public class AssetHandler<B> extends ReferenceableHandler<B>
{
    private ConnectionHandler<OpenMetadataAPIDummyBean> connectionHandler;

    /**
     * Construct the asset handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public AssetHandler(OpenMetadataAPIGenericConverter<B> converter,
                        Class<B>                           beanClass,
                        String                             serviceName,
                        String                             serverName,
                        InvalidParameterHandler            invalidParameterHandler,
                        RepositoryHandler                  repositoryHandler,
                        OMRSRepositoryHelper               repositoryHelper,
                        String                             localServerUserId,
                        OpenMetadataServerSecurityVerifier securityVerifier,
                        List<String>                       supportedZones,
                        List<String>                       defaultZones,
                        List<String>                       publishZones,
                        AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        this.connectionHandler = new ConnectionHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                         OpenMetadataAPIDummyBean.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);
    }


    /**
     * Return the list of asset subtype names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTypesOfAssetList()
    {
        return repositoryHelper.getSubTypesOf(serviceName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);
    }


    /**
     * Return the list of asset subtype names mapped to their descriptions.
     *
     * @return list of type names that are subtypes of asset
     */
    public Map<String, String> getTypesOfAssetDescriptions()
    {
        List<String>        assetTypeList = repositoryHelper.getSubTypesOf(serviceName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);
        Map<String, String> assetDescriptions = new HashMap<>();

        if (assetTypeList != null)
        {
            for (String  assetTypeName : assetTypeList)
            {
                if (assetTypeName != null)
                {
                    TypeDef assetTypeDef = repositoryHelper.getTypeDefByName(serviceName, assetTypeName);

                    if (assetTypeDef != null)
                    {
                        assetDescriptions.put(assetTypeName, assetTypeDef.getDescription());
                    }
                }
            }

        }

        if (assetDescriptions.isEmpty())
        {
            return null;
        }

        return assetDescriptions;
    }


    /**
     * Save any associated Connection.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter passing the assetGUID
     * @param assetSummary short description of the asset
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName name of parameter for the connectionGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void attachConnectionToAsset(String userId,
                                         String externalSourceGUID,
                                         String externalSourceName,
                                         String assetGUID,
                                         String assetGUIDParameterName,
                                         String assetSummary,
                                         String connectionGUID,
                                         String connectionGUIDParameterName,
                                         String methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        /*
         * The asset summary property is stored in the relationship between the asset and the connection.
         */
        InstanceProperties properties = null;

        if (assetSummary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                      assetSummary,
                                                                      methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  connectionGUID,
                                  connectionGUIDParameterName,
                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                  properties,
                                  methodName);
    }



    /**
     * Link the schema type and asset.  This is called from outside of AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param schemaTypeGUID identifier for schema Type object
     * @param schemaTypeGUIDParameterName parameter providing the schemaTypeGUID
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  void attachSchemaTypeToAsset(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  assetGUID,
                                         String  assetGUIDParameterName,
                                         String  schemaTypeGUID,
                                         String  schemaTypeGUIDParameterName,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        /*
         * An asset is only allowed one attached schema.  Check that an update is permitted and remove any previously attached schema.
         */
        removeAssociatedSchemaType(userId,
                                   externalSourceGUID,
                                   externalSourceName,
                                   assetGUID,
                                   assetGUIDParameterName,
                                   methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  schemaTypeGUID,
                                  schemaTypeGUIDParameterName,
                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove any associated schema type.  This may be called from outside of AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeAssociatedSchemaType(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String assetGUID,
                                           String assetGUIDParameterName,
                                           String methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        this.unlinkAllElements(userId,
                               false,
                               externalSourceGUID,
                               externalSourceName,
                               assetGUID,
                               assetGUIDParameterName,
                               OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                               supportedZones,
                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                               methodName);
    }


    /**
     * Remove any associated schema type.  This may be called from outside of AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param methodName calling method
     *
     * @return guid of previously attached schema type or null if there is no schema
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String detachSchemaTypeFromAsset(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String assetGUID,
                                            String assetGUIDParameterName,
                                            String methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.unlinkConnectedElement(userId,
                                           false,
                                           externalSourceGUID,
                                           externalSourceName,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           supportedZones,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                           methodName);
    }


    /**
     * Create relationships between the identified glossary terms and an Asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the referenceable that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUIDs list of unique identifiers of the glossary terms
     * @param glossaryTermGUIDsParameter parameter supplying the list of GlossaryTermGUIDs
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignments(String         userId,
                                         String         externalSourceGUID,
                                         String         externalSourceName,
                                         String         beanGUID,
                                         String         beanGUIDParameter,
                                         List<String>   glossaryTermGUIDs,
                                         String         glossaryTermGUIDsParameter,
                                         String         methodName)  throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        if (glossaryTermGUIDs != null)
        {
            for (String glossaryTermGUID : glossaryTermGUIDs)
            {
                if (glossaryTermGUID != null)
                {
                    this.saveSemanticAssignment(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameter,
                                                glossaryTermGUID,
                                                glossaryTermGUIDsParameter,
                                                methodName);
                }
            }
        }
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateGUIDParameterName name of parameter providing the templateGUID
     * @param expectedTypeGUID unique identifier of type (or super type of asset identified by templateGUID)
     * @param expectedTypeName unique name of type (or super type of asset identified by templateGUID)
     * @param qualifiedName unique name for this asset - must not be null
     * @param qualifiedNameParameterName parameter name providing qualifiedName
     * @param displayName the stored display name property for the database - if null, the value from the template is used
     * @param description the stored description property associated with the database - if null, the value from the template is used
     * @param networkAddress if there is a connection object for this asset - update the endpoint network address
     * @param methodName calling method
     *
     * @return unique identifier of the asset in the repository.  If a connection or schema object is provided,
     *         it is stored linked to the asset.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addAssetFromTemplate(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String templateGUID,
                                       String templateGUIDParameterName,
                                       String expectedTypeGUID,
                                       String expectedTypeName,
                                       String qualifiedName,
                                       String qualifiedNameParameterName,
                                       String displayName,
                                       String description,
                                       String networkAddress,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                displayName,
                                                description,
                                                null,
                                                expectedTypeGUID,
                                                expectedTypeName,
                                                null,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        String assetGUID = this.createBeanFromTemplate(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       templateGUID,
                                                       templateGUIDParameterName,
                                                       expectedTypeGUID,
                                                       expectedTypeName,
                                                       qualifiedName,
                                                       OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                       builder,
                                                       methodName);

        if (assetGUID != null)
        {
            Relationship  assetConnectionRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                      assetGUID,
                                                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                                      OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                      OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                      methodName);

            if (assetConnectionRelationship != null)
            {
                String connectionGUID = assetConnectionRelationship.getEntityOneProxy().getGUID();

                Relationship  connectionEndpointRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                             connectionGUID,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                                                             methodName);
                if (connectionEndpointRelationship != null)
                {
                    final String endpointGUIDParameterName = "endpointGUID";

                    String endpointGUID = connectionEndpointRelationship.getEntityOneProxy().getGUID();

                    EntityDetail endpointEntity = this.getEntityFromRepository(userId,
                                                                               endpointGUID,
                                                                               endpointGUIDParameterName,
                                                                               OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                               methodName);

                    String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(endpointEntity, methodName);

                    if (assetGUID.equals(anchorGUID))
                    {
                        InstanceProperties endpointProperties = endpointEntity.getProperties();

                        endpointProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          endpointProperties,
                                                                                          OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                                          networkAddress,
                                                                                          methodName);
                        repositoryHandler.updateEntityProperties(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 endpointGUID,
                                                                 endpointEntity,
                                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                 endpointProperties,
                                                                 methodName);
                    }
                }
            }
        }

        return assetGUID;
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param zoneMembership initial zones for the asset - or null to allow the security module to set it up
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param originOrganizationCapabilityGUID unique identifier of originating organization
     * @param originBusinessCapabilityGUID unique identifier of originating business capability
     * @param otherOriginValues the properties that characterize where this asset is from
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of asset - or null to create standard type
     * @param typeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetInRepository(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               qualifiedName,
                                           String               technicalName,
                                           String               technicalDescription,
                                           List<String>         zoneMembership,
                                           String               owner,
                                           int                  ownerType,
                                           String               originOrganizationCapabilityGUID,
                                           String               originBusinessCapabilityGUID,
                                           Map<String, String>  otherOriginValues,
                                           Map<String, String>  additionalProperties,
                                           String               typeGUID,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return this.createAssetInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            qualifiedName,
                                            technicalName,
                                            technicalDescription,
                                            zoneMembership,
                                            owner,
                                            ownerType,
                                            originOrganizationCapabilityGUID,
                                            originBusinessCapabilityGUID,
                                            otherOriginValues,
                                            additionalProperties,
                                            typeGUID,
                                            typeName,
                                            extendedProperties,
                                            InstanceStatus.ACTIVE,
                                            methodName);
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param zoneMembership initial zones for the asset - or null to allow the security module to set it up
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param originOrganizationCapabilityGUID unique identifier of originating organization
     * @param originBusinessCapabilityGUID unique identifier of originating business capability
     * @param otherOriginValues the properties that characterize where this asset is from
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of asset - or null to create standard type
     * @param typeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetInRepository(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               qualifiedName,
                                           String               technicalName,
                                           String               technicalDescription,
                                           List<String>         zoneMembership,
                                           String               owner,
                                           int                  ownerType,
                                           String               originOrganizationCapabilityGUID,
                                           String               originBusinessCapabilityGUID,
                                           Map<String, String>  otherOriginValues,
                                           Map<String, String>  additionalProperties,
                                           String               typeGUID,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           InstanceStatus       instanceStatus,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        String assetTypeId = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                      serviceName,
                                                                      methodName,
                                                                      repositoryHelper);

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                technicalName,
                                                technicalDescription,
                                                additionalProperties,
                                                assetTypeId,
                                                assetTypeName,
                                                extendedProperties,
                                                instanceStatus,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        if (zoneMembership != null)
        {
            builder.setAssetZones(userId, zoneMembership, methodName);
        }
        builder.setAssetOwnership(userId, owner, ownerType, methodName);
        builder.setAssetOrigin(userId, originOrganizationCapabilityGUID, originBusinessCapabilityGUID, otherOriginValues, methodName);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Database - or null to create standard type
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
       this.updateAsset(userId,
                        externalSourceGUID,
                        externalSourceName,
                        assetGUID,
                        assetGUIDParameterName,
                        qualifiedName,
                        technicalName,
                        technicalDescription,
                        additionalProperties,
                        typeGUID,
                        typeName,
                        supportedZones,
                        extendedProperties,
                        false,
                        methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Database - or null to create standard type
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param isMergeUpdate indicates whether supplied properties should replace
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            boolean              isMergeUpdate,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        this.updateAsset(userId,
                         externalSourceGUID,
                         externalSourceName,
                         assetGUID,
                         assetGUIDParameterName,
                         qualifiedName,
                         technicalName,
                         technicalDescription,
                         additionalProperties,
                         typeGUID,
                         typeName,
                         supportedZones,
                         extendedProperties,
                         isMergeUpdate,
                         methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Asset - or null to create standard type
     * @param typeName name of the type that is a subtype of Asset - or null to create standard type
     * @param suppliedSupportedZones supported zones that are specific to the caller
     * @param extendedProperties properties from any subtype
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            List<String>         suppliedSupportedZones,
                            Map<String, Object>  extendedProperties,
                            boolean              isMergeUpdate,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                technicalName,
                                                technicalDescription,
                                                additionalProperties,
                                                typeGUID,
                                                typeName,
                                                extendedProperties,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    suppliedSupportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    methodName);
    }

    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param displayName the stored display name property for the database
     * @param description the stored description property associated with the database
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Database - or null to create standard type
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param connection connection associated with the asset
     * @param assetSummary description of the asset from the perspective of the connection
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAssetWithConnection(String               userId,
                                          String               externalSourceGUID,
                                          String               externalSourceName,
                                          String               assetGUID,
                                          String               assetGUIDParameterName,
                                          String               qualifiedName,
                                          String               displayName,
                                          String               description,
                                          Map<String, String>  additionalProperties,
                                          String               typeGUID,
                                          String               typeName,
                                          Map<String, Object>  extendedProperties,
                                          String               assetSummary,
                                          Connection           connection,
                                          String               methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        this.updateAsset(userId,
                         externalSourceGUID,
                         externalSourceName,
                         assetGUID,
                         assetGUIDParameterName,
                         qualifiedName,
                         displayName,
                         description,
                         additionalProperties,
                         typeGUID,
                         typeName,
                         extendedProperties,
                         methodName);

        Relationship  assetConnectionRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  assetGUID,
                                                                                                  OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                  methodName);
        if (connection == null)
        {
            this.unlinkConnectedElement(userId,
                                        false,
                                        null,
                                        null,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        typeName,
                                        supportedZones,
                                        OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                        OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                        OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
                                        methodName);
        }
        else /* connection to add */
        {
            String  connectionGUID = connectionHandler.saveConnection(userId,
                                                                      null,
                                                                      null,
                                                                      assetGUID,
                                                                      assetGUID,
                                                                      assetGUIDParameterName,
                                                                      typeName,
                                                                      connection,
                                                                      assetSummary,
                                                                      methodName);

            if (assetConnectionRelationship == null)
            {
                InstanceProperties relationshipProperties = null;

                if (assetSummary != null)
                {
                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                                          assetSummary,
                                                                                          methodName);
                }

                repositoryHandler.createRelationship(userId,
                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                     null,
                                                     null,
                                                     connectionGUID,
                                                     assetGUID,
                                                     relationshipProperties,
                                                     methodName);
            }
            else
            {
                repositoryHandler.updateUniqueRelationshipByType(userId,
                                                                 null,
                                                                 null,
                                                                 connectionGUID,
                                                                 OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                 assetGUID,
                                                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                 methodName);
            }
        }
    }


    /**
     * Add the ReferenceData classification to an asset.  If the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     * @param assetGUIDParameterName name of parameter providing assetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  classifyAssetAsReferenceData(String       userId,
                                              String       assetGUID,
                                              String       assetGUIDParameterName,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.setClassificationInRepository(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                           null,
                                           methodName);
    }


    /**
     * Remove the ReferenceData classification from an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     * @param assetGUIDParameterName name of parameter providing assetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  declassifyAssetAsReferenceData(String       userId,
                                                String       assetGUID,
                                                String       assetGUIDParameterName,
                                                String       methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                assetGUID,
                                                assetGUIDParameterName,
                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                                methodName);
    }


    /**
     * Add the asset origin classification to an asset.  The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param organizationGUIDParameterName parameter name supplying organizationGUID
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param businessCapabilityGUIDParameterName parameter name supplying businessCapabilityGUID
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addAssetOrigin(String                userId,
                                String                assetGUID,
                                String                assetGUIDParameterName,
                                String                organizationGUID,
                                String                organizationGUIDParameterName,
                                String                businessCapabilityGUID,
                                String                businessCapabilityGUIDParameterName,
                                Map<String, String>   otherOriginValues,
                                String                methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        /*
         * Validate the the organization and the business capability exist.
         */
        if (organizationGUID  != null)
        {
            this.validateAnchorEntity(userId,
                                      organizationGUID,
                                      organizationGUIDParameterName,
                                      OpenMetadataAPIMapper.ORGANIZATION_TYPE_NAME,
                                      false,
                                      supportedZones,
                                      methodName);
        }

        if (businessCapabilityGUID != null)
        {
            this.validateAnchorEntity(userId,
                                      businessCapabilityGUID,
                                      businessCapabilityGUIDParameterName,
                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_TYPE_NAME,
                                      false,
                                      supportedZones,
                                      methodName);
        }

        AssetBuilder builder = new AssetBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                           builder.getOriginProperties(organizationGUID,
                                                                       businessCapabilityGUID,
                                                                       otherOriginValues,
                                                                       methodName),
                                           methodName);
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeAssetOrigin(String userId,
                                   String assetGUID,
                                   String assetGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                assetGUID,
                                                assetGUIDParameterName,
                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                methodName);
    }


    /**
     * Update the zones for a specific asset to the list set up in publish zones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void publishAsset(String userId,
                             String assetGUID,
                             String assetGUIDParameterName,
                             String methodName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        updateAssetZones(userId, assetGUID, assetGUIDParameterName, publishZones, methodName);
    }


    /**
     * Update the zones for a specific asset to the list set up in publish zones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void withdrawAsset(String userId,
                              String assetGUID,
                              String assetGUIDParameterName,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        updateAssetZones(userId, assetGUID,  assetGUIDParameterName, defaultZones, methodName);
    }


    /**
     * Update the zones for a specific asset. The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to all zones.
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAssetZones(String        userId,
                                 String        assetGUID,
                                 String        assetGUIDParameterName,
                                 List<String>  assetZones,
                                 String        methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        AssetBuilder builder = new AssetBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                           builder.getZoneMembershipProperties(assetZones, methodName),
                                           methodName);
    }


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param ownerId userId or profileGUID of the owner - or null to clear the field
     * @param ownerType indicator of the type of Id provides above - or null to clear the field
     * @param methodName calling method
     *
     * @throws InvalidParameterException userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAssetOwner(String    userId,
                                 String    assetGUID,
                                 String    assetGUIDParameterName,
                                 String    ownerId,
                                 int       ownerType,
                                 String    methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        AssetBuilder builder = new AssetBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                           builder.getOwnerProperties(userId, ownerId, ownerType, methodName),
                                           methodName);
    }


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user
     * @param connectionGUID  unique identifier for the connection
     * @param connectionGUIDParameterName name of parameter supplying connectionGUID
     * @param serviceSupportedZones list of supported zones for any connected asset
     * @param methodName calling method
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String       userId,
                                         String       connectionGUID,
                                         String       connectionGUIDParameterName,
                                         List<String> serviceSupportedZones,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return this.getAttachedElementGUID(userId,
                                           connectionGUID,
                                           connectionGUIDParameterName,
                                           OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           0,
                                           serviceSupportedZones,
                                           methodName);
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     * @param connectionNameParameter name of parameter supplying
     * @param methodName       calling method
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String userId,
                                             String connectionName,
                                             String connectionNameParameter,
                                             String methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        List<String> connectionGUIDs = this.getBeanGUIDsByValue(userId,
                                                                connectionName,
                                                                connectionNameParameter,
                                                                OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                                OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                specificMatchPropertyNames,
                                                                true,
                                                                supportedZones,
                                                                0,
                                                                invalidParameterHandler.getMaxPagingSize(),
                                                                methodName);

        if (connectionGUIDs != null)
        {
            for (String connectionGUID : connectionGUIDs)
            {
                if (connectionGUID != null)
                {
                    return this.getAssetForConnection(userId, connectionGUID, connectionNameParameter, supportedZones, methodName);
                }
            }
        }

        return null;
    }


    /**
     * Return an asset along with any associated connection.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying assetGUID
     * @param assetTypeName type name of asset
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetWithConnection(String userId,
                                    String assetGUID,
                                    String assetGUIDParameterName,
                                    String assetTypeName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        EntityDetail assetEntity = this.getEntityFromRepository(userId,
                                                                assetGUID,
                                                                assetGUIDParameterName,
                                                                assetTypeName,
                                                                methodName);

        if (assetEntity != null)
        {
            return this.getAssetWithConnectionBean(userId, assetEntity, methodName);
        }

        return null;
    }


    /**
     * Return an asset along with any associated connection.
     *
     * @param userId calling user
     * @param name unique identifier of the asset
     * @param nameParameterName name of parameter supplying name
     * @param assetTypeGUID type identifier of asset
     * @param assetTypeName type name of asset
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetByNameWithConnection(String userId,
                                          String name,
                                          String nameParameterName,
                                          String assetTypeGUID,
                                          String assetTypeName,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             name,
                                                             nameParameterName,
                                                             assetTypeGUID,
                                                             assetTypeName,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             null,
                                                             null,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             methodName);

        if ((results != null) && (results.size() == 1))
        {
            return getAssetWithConnectionBean(userId, results.get(0), methodName);
        }
        else
        {
            errorHandler.handleAmbiguousEntityName(name, nameParameterName, assetTypeName, results, methodName);
        }

        return null;
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param assetTypeGUID subtype of asset required
     * @param assetTypeName subtype of asset required
     * @param startFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call
     * @param methodName calling method
     *
     * @return list of beans.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definitions.
     */
    public List<B> getAllAssetsWithConnection(String userId,
                                              String assetTypeGUID,
                                              String assetTypeName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             assetTypeGUID,
                                                             assetTypeName,
                                                             supportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             methodName);

        if ((entities != null) && (! entities.isEmpty()))
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = this.getAssetWithConnectionBean(userId, entity, methodName);
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param assetEntity entity for root connection object
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getAssetWithConnectionBean(String       userId,
                                         EntityDetail assetEntity,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        if (assetEntity != null)
        {
            EntityDetail connectionEntity = null;
            Relationship relationshipToConnection = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  assetEntity.getGUID(),
                                                                                                  assetEntity.getType().getTypeDefName(),
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                  methodName);

            if (relationshipToConnection != null)
            {
                EntityProxy end1 = relationshipToConnection.getEntityOneProxy();

                if (end1 != null)
                {
                    final String connectionGUIDParameterName = "relationshipToConnection.getEntityOneProxy().getGUID()";

                    connectionEntity = this.getEntityFromRepository(userId,
                                                                    end1.getGUID(),
                                                                    connectionGUIDParameterName,
                                                                    OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                    methodName);
                }
            }

            List<Relationship> supplementaryRelationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities      = new ArrayList<>();

            if ((connectionEntity != null) && (connectionEntity.getType() != null))
            {
                /*
                 * The relationships are retrieved first.  It is not possible to follow the same pattern as SchemaType where
                 * embedded instances are retrieve as beans and then assembled in the final bean at the end because of the problem of
                 * matching the properties in the EmbeddedConnection relationship with the Connection bean it links to.
                 * So the entire graph of instances for the connection are retrieved and passed to the converter.
                 */
                supplementaryEntities.add(connectionEntity);
                supplementaryRelationships.add(relationshipToConnection);

                List<Relationship> connectionRelationships = this.getEmbeddedConnectionRelationships(userId, connectionEntity, methodName);

                if (connectionRelationships != null)
                {
                    supplementaryRelationships.addAll(connectionRelationships);

                    for (Relationship relationship : connectionRelationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null))
                        {
                            EntityProxy entityProxy = null;

                            if ((repositoryHelper.isTypeOf(serviceName,
                                                           relationship.getType().getTypeDefName(),
                                                           OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME)))
                            {
                                entityProxy = relationship.getEntityTwoProxy();
                            }
                            else if (repositoryHelper.isTypeOf(serviceName,
                                                               relationship.getType().getTypeDefName(),
                                                               OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                            {
                                entityProxy = relationship.getEntityOneProxy();
                            }
                            if ((entityProxy != null) && (entityProxy.getGUID() != null) && (entityProxy.getType() != null))
                            {
                                final String entityGUIDParameterName = "embeddedRelationship proxy";
                                EntityDetail supplementaryEntity = this.getEntityFromRepository(userId,
                                                                                                entityProxy.getGUID(),
                                                                                                entityGUIDParameterName,
                                                                                                entityProxy.getType().getTypeDefName(),
                                                                                                methodName);
                                if (supplementaryEntity != null)
                                {
                                    supplementaryEntities.add(supplementaryEntity);
                                }
                            }
                        }
                    }
                }
            }

            if (supplementaryEntities.isEmpty())
            {
                supplementaryEntities = null;
            }

            if (supplementaryRelationships.isEmpty())
            {
                supplementaryEntities = null;
            }

            return converter.getNewComplexBean(beanClass, assetEntity, supplementaryEntities, supplementaryRelationships, methodName);
        }

        return null;
    }


    /**
     * Recursively retrieve the list of relationships within a connection object.  The recursion occurs in VirtualConnections
     * that embed connections within themselves.
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedConnectionRelationships(String        userId,
                                                                  EntitySummary connectionEntity,
                                                                  String        methodName) throws PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        List<Relationship> supplementaryRelationships = new ArrayList<>();

        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           userId,
                                                                                           connectionEntity.getGUID(),
                                                                                           connectionEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);


            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    if ((repositoryHelper.isTypeOf(serviceName,
                                                   relationship.getType().getTypeDefName(),
                                                   OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME))
                    {
                        supplementaryRelationships.add(relationship);

                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedConnectionRelationships(userId,
                                                                                                                         embeddedConnectionEnd,
                                                                                                                         methodName);

                            if (embeddedConnectionRelationships != null)
                            {
                                supplementaryRelationships.addAll(embeddedConnectionRelationships);
                            }
                        }
                    }
                }
            }
        }

        if (supplementaryRelationships.isEmpty())
        {
            return null;
        }

        return supplementaryRelationships;
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDsScan(String       userId,
                                        String       subTypeGUID,
                                        String       subTypeName,
                                        int          startFrom,
                                        int          pageSize,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.assetGUIDZoneScan(userId,
                                      null,
                                      subTypeGUID,
                                      subTypeName,
                                      supportedZones,
                                      startFrom,
                                      pageSize,
                                      methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDsScan(String       userId,
                                        String       subTypeGUID,
                                        String       subTypeName,
                                        List<String> suppliedSupportedZones,
                                        int          startFrom,
                                        int          pageSize,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.assetGUIDZoneScan(userId,
                                      null,
                                      subTypeGUID,
                                      subTypeName,
                                      suppliedSupportedZones,
                                      startFrom,
                                      pageSize,
                                      methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetScan(String       userId,
                              String       subTypeGUID,
                              String       subTypeName,
                              int          startFrom,
                              int          pageSize,
                              String       methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.assetZoneScan(userId,
                                  null,
                                  subTypeGUID,
                                  subTypeName,
                                  supportedZones,
                                  startFrom,
                                  pageSize,
                                  methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetScan(String       userId,
                              String       subTypeGUID,
                              String       subTypeName,
                              List<String> suppliedSupportedZones,
                              int          startFrom,
                              int          pageSize,
                              String       methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.assetZoneScan(userId,
                                  null,
                                  subTypeGUID,
                                  subTypeName,
                                  suppliedSupportedZones,
                                  startFrom,
                                  pageSize,
                                  methodName);
    }


    /**
     * Scan through the repository looking for assets by type and/or zone.  The type name
     * may be null which means, all types of assets will be returned.  The zone name may be null
     * which means all supportedZones for the service are returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetZoneScan(String       userId,
                                  String       zoneName,
                                  String       subTypeGUID,
                                  String       subTypeName,
                                  List<String> suppliedSupportedZones,
                                  int          startFrom,
                                  int          pageSize,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            resultTypeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            resultTypeName = subTypeName;
        }

        List<B>  results = new ArrayList<>();

        if ((zoneName == null) || (suppliedSupportedZones == null) || (suppliedSupportedZones.contains(zoneName)))
        {
            RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                                 userId,
                                                                                 resultTypeGUID,
                                                                                 resultTypeName,
                                                                                 startFrom,
                                                                                 queryPageSize,
                                                                                 methodName);


            while ((iterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
            {
                EntityDetail entity = iterator.getNext();

                if (entity != null)
                {
                    List<String>    assetZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                         OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);


                    if ((assetZones != null) && (assetZones.contains(zoneName)))
                    {
                        results.add(converter.getNewBean(beanClass, entity, methodName));
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Scan through the repository looking for assets by type and/or zone.  The type name
     * may be null which means, all types of assets will be returned.  The zone name may be null
     * which means all supportedZones for the service are returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param methodName calling method
     * @return list of GUIDs for matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDZoneScan(String       userId,
                                           String       zoneName,
                                           String       subTypeGUID,
                                           String       subTypeName,
                                           List<String> suppliedSupportedZones,
                                           int          startFrom,
                                           int          pageSize,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            resultTypeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            resultTypeName = subTypeName;
        }

        List<String>  results = new ArrayList<>();

        if ((zoneName == null) || (suppliedSupportedZones == null) || (suppliedSupportedZones.contains(zoneName)))
        {
            RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                                 userId,
                                                                                 resultTypeGUID,
                                                                                 resultTypeName,
                                                                                 startFrom,
                                                                                 queryPageSize,
                                                                                 methodName);


            while ((iterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
            {
                EntityDetail entity = iterator.getNext();

                if (entity != null)
                {
                    List<String>    assetZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                         OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);


                    if ((assetZones != null) && (assetZones.contains(zoneName)))
                    {
                        results.add(entity.getGUID());
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findAssetsByName(String   userId,
                                    String   typeGUID,
                                    String   typeName,
                                    String   name,
                                    String   nameParameterName,
                                    int      startFrom,
                                    int      pageSize,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.findAssetsByName(userId,
                                     typeGUID,
                                     typeName,
                                     name,
                                     nameParameterName,
                                     supportedZones,
                                     startFrom,
                                     pageSize,
                                     methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findAssetsByName(String        userId,
                                    String        typeGUID,
                                    String        typeName,
                                    String        name,
                                    String        nameParameterName,
                                    List<String>  serviceSupportedZones,
                                    int           startFrom,
                                    int           pageSize,
                                    String        methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this must be an exact match either on the display name or the qualified name
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByName(String   userId,
                                            String   typeGUID,
                                            String   typeName,
                                            String   name,
                                            String   nameParameterName,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAssetGUIDsByName(userId,
                                        typeGUID,
                                        typeName,
                                        name,
                                        nameParameterName,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByName(String       userId,
                                            String       typeGUID,
                                            String       typeName,
                                            String       name,
                                            String       nameParameterName,
                                            List<String> serviceSupportedZones,
                                            int          startFrom,
                                            int          pageSize,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeanGUIDsByValue(userId,
                                        name,
                                        nameParameterName,
                                        resultTypeGUID,
                                        resultTypeName,
                                        specificMatchPropertyNames,
                                        true,
                                        serviceSupportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByName(String   userId,
                                   String   typeGUID,
                                   String   typeName,
                                   String   name,
                                   String   nameParameterName,
                                   int      startFrom,
                                   int      pageSize,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getAssetsByName(userId,
                                    typeGUID,
                                    typeName,
                                    name,
                                    nameParameterName,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this must be an exact match on the display name or qualified name
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByName(String       userId,
                                   String       typeGUID,
                                   String       typeName,
                                   String       name,
                                   String       nameParameterName,
                                   List<String> serviceSupportedZones,
                                   int          startFrom,
                                   int          pageSize,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested metadataCollectionId.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for generic Asset)
     * @param typeName unique name of the asset type to search for (null for generic Asset)
     * @param metadataCollectionId unique identifier of metadataCollection to search for
     * @param metadataCollectionIdParameterName parameter providing the metadata collection id
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<B> getAssetsByMetadataCollectionId(String   userId,
                                            String   typeGUID,
                                            String   typeName,
                                            String   metadataCollectionId,
                                            String   metadataCollectionIdParameterName,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeansByValue(userId,
                                    metadataCollectionId,
                                    metadataCollectionIdParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param networkAddressParameterName parameter name supplying networkAddress
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param methodName calling method
     * @return list of unique identifiers for matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<String> getAssetGUIDsByEndpoint(String userId,
                                                 String networkAddress,
                                                 String networkAddressParameterName,
                                                 int    startFrom,
                                                 int    pageSize,
                                                 String methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        int queryPageSize      = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);
        int maxPageSize        = invalidParameterHandler.getMaxPagingSize();
        int startNextQueryFrom = 0;

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME);

        List<String> relationshipPath = new ArrayList<>();
        relationshipPath.add(OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID);
        relationshipPath.add(OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID);

        List<String>  assetGUIDs     = new ArrayList<>();
        List<String>  resultGUIDs    = new ArrayList<>();
        boolean moreResultsAvailable = true;

        while (moreResultsAvailable && ((queryPageSize == 0) || (resultGUIDs.size() < queryPageSize)))
        {
            List<String> endpointGUIDs = this.getBeanGUIDsByValue(userId,
                                                                  networkAddress,
                                                                  networkAddressParameterName,
                                                                  OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                  specificMatchPropertyNames,
                                                                  true,
                                                                  supportedZones,
                                                                  startNextQueryFrom,
                                                                  maxPageSize,
                                                                  methodName);


            if (endpointGUIDs == null)
            {
                moreResultsAvailable = false;
            }
            else /* from each endpoint, navigate to the asset. The same asset may be returned multiple times */
            {
                for (String endpointGUID : endpointGUIDs)
                {
                    if (endpointGUID != null)
                    {
                        boolean moreResultsFromEndpoint = true;
                        int     endpointStartFrom = 0;

                        while (moreResultsFromEndpoint && ((queryPageSize == 0) || (resultGUIDs.size() < queryPageSize)))
                        {
                            List<String> endpointAssetGUIDs = this.getRelatedEntityGUIDs(userId,
                                                                                         endpointGUID,
                                                                                         networkAddressParameterName,
                                                                                         OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                                         relationshipPath,
                                                                                         OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                         supportedZones,
                                                                                         endpointStartFrom,
                                                                                         maxPageSize,
                                                                                         methodName);

                            if ((endpointAssetGUIDs == null) || (endpointAssetGUIDs.isEmpty()))
                            {
                                moreResultsFromEndpoint = false;
                            }
                            else
                            {
                                /*
                                 * The generic handler will have removed duplicates from the list it returns.
                                 * This method needs to ensure that assets returned from different endpoints do not
                                 * cause the same asset to appear twice in the list.
                                 */
                                for (String endpointAssetGUID : endpointAssetGUIDs)
                                {
                                    if (endpointAssetGUID != null)
                                    {
                                        if (! assetGUIDs.contains(endpointAssetGUID))
                                        {
                                            assetGUIDs.add(endpointAssetGUID);
                                            if (assetGUIDs.size() > startFrom)
                                            {
                                                resultGUIDs.add(endpointAssetGUID);
                                            }
                                        }
                                    }
                                }

                                endpointStartFrom = endpointStartFrom + endpointAssetGUIDs.size();
                            }
                        }
                    }
                }
            }
        }

        if (resultGUIDs.isEmpty())
        {
            return null;
        }
        else
        {
            return resultGUIDs;
        }
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param networkAddressParameterName name of parameter passing the network address
     * @param suppliedTypeName name of asset subtype to validate
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param methodName calling method
     * @return list of  matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<B> getAssetsByEndpoint(String userId,
                                        String networkAddress,
                                        String networkAddressParameterName,
                                        String suppliedTypeName,
                                        int    startFrom,
                                        int    pageSize,
                                        String methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            assetTypeName = suppliedTypeName;
        }

        List<String> assetGUIDs = this.getAssetGUIDsByEndpoint(userId, networkAddress, networkAddressParameterName, startFrom, pageSize, methodName);

        if ((assetGUIDs == null) || (assetGUIDs.isEmpty()))
        {
            return null;
        }
        else
        {
            List<B> assets = new ArrayList<>();

            for (String assetGUID : assetGUIDs)
            {
                if (assetGUID != null)
                {
                    B bean = this.getBeanFromRepository(userId,
                                                        assetGUID,
                                                        networkAddressParameterName,
                                                        assetTypeName,
                                                        supportedZones,
                                                        methodName);

                    assets.add(bean);
                }
            }

            if (assets.isEmpty())
            {
                return null;
            }

            return assets;
        }
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param searchStringParameter name of parameter supplying the search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of unique identifiers for assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String>  findAssetGUIDs(String   userId,
                                        String   searchString,
                                        String   searchStringParameter,
                                        int      startFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.findAssetGUIDs(userId,
                                   OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                   searchString,
                                   searchStringParameter,
                                   startFrom,
                                   pageSize,
                                   methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for - null for generic Asset
     * @param typeName unique name of the asset type to search for - null for generic Asset
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of unique identifiers for assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String>  findAssetGUIDs(String   userId,
                                        String   typeGUID,
                                        String   typeName,
                                        String   searchString,
                                        String   searchStringParameter,
                                        int      startFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanGUIDsByValue(userId,
                                        searchString,
                                        searchStringParameter,
                                        resultTypeGUID,
                                        resultTypeName,
                                        null,
                                        false,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<B>  findAssets(String   userId,
                               String   searchString,
                               String   searchStringParameter,
                               int      startFrom,
                               int      pageSize,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return this.findAssets(userId,
                               OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                               OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                               searchString,
                               searchStringParameter,
                               startFrom,
                               pageSize,
                               methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for
     * @param typeName unique name of the asset type to search for
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<B>  findAssets(String userId,
                               String typeGUID,
                               String typeName,
                               String searchString,
                               String searchStringParameter,
                               int    startFrom,
                               int    pageSize,
                               String methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        /*
         * Notice that the parameter order for findBeans is different from findAssets
         */
        return this.findBeans(userId, searchString, searchStringParameter, resultTypeGUID, resultTypeName, startFrom, pageSize, methodName);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByTag(String       userId,
                                           String       tagGUID,
                                           String       tagGUIDParameterName,
                                           int          startFrom,
                                           int          pageSize,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return this.getAttachedElementGUIDs(userId,
                                            tagGUID,
                                            tagGUIDParameterName,
                                            OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                            OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                            OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                            supportedZones,
                                            startFrom,
                                            pageSize,
                                            methodName);
    }


    /**
     * Return the list of assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByTag(String userId,
                                  String tagGUID,
                                  String tagGUIDParameterName,
                                  int    startFrom,
                                  int    pageSize,
                                  String methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        tagGUID,
                                        tagGUIDParameterName,
                                        OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific keyword either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param keywordGUID unique identifier of keyword.
     * @param keywordGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByKeyword(String       userId,
                                               String       keywordGUID,
                                               String       keywordGUIDParameterName,
                                               int          startFrom,
                                               int          pageSize,
                                               String       methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return this.getAttachedElementGUIDs(userId,
                                            keywordGUID,
                                            keywordGUIDParameterName,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                            supportedZones,
                                            startFrom,
                                            pageSize,
                                            methodName);
    }


    /**
     * Return the list of assets that are linked to a specific keyword.
     *
     * @param userId the name of the calling user.
     * @param keywordGUID unique identifier of keyword.
     * @param keywordGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByKeyword(String       userId,
                                      String       keywordGUID,
                                      String       keywordGUIDParameterName,
                                      int          startFrom,
                                      int          pageSize,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        keywordGUID,
                                        keywordGUIDParameterName,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return the list of assets that are linked to a specific glossary term.
     *
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param termGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByGlossaryTerm(String       userId,
                                           String       termGUID,
                                           String       termGUIDParameterName,
                                           int          startFrom,
                                           int          pageSize,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        termGUID,
                                        termGUIDParameterName,
                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }
}
