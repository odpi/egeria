/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.AssetBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectionMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.MeaningMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AssetHandler manages Asset objects and optionally connections in the property server.  It runs server-side in
 * OMAS and retrieves Assets and Connections through the OMRSRepositoryConnector.
 */
public class AssetHandler
{
    private String                    serviceName;
    private OMRSRepositoryHelper      repositoryHelper;
    private String                    serverName;
    private InvalidParameterHandler   invalidParameterHandler;
    private RepositoryHandler         repositoryHandler;
    private CertificationHandler      certificationHandler;
    private CommentHandler            commentHandler;
    private ConnectionHandler         connectionHandler;
    private ExternalIdentifierHandler externalIdentifierHandler;
    private ExternalReferenceHandler  externalReferenceHandler;
    private InformalTagHandler        informalTagHandler;
    private LicenseHandler            licenseHandler;
    private LikeHandler               likeHandler;
    private LocationHandler           locationHandler;
    private NoteLogHandler            noteLogHandler;
    private RatingHandler             ratingHandler;
    private RelatedMediaHandler       relatedMediaHandler;
    private SchemaTypeHandler         schemaTypeHandler;

    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

    protected List<String>            supportedZones;
    protected List<String>            defaultZones;


    /**
     * Construct the connection handler with information needed to work with Connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param certificationHandler handler for certification objects
     * @param commentHandler handler for comment objects
     * @param connectionHandler handler for connection objects
     * @param externalIdentifierHandler handler for external identifier objects
     * @param externalReferenceHandler handler for external reference objects
     * @param informalTagHandler handler for informal tag objects
     * @param licenseHandler  handler for license objects
     * @param likeHandler  handler for like objects
     * @param locationHandler  handler for location objects
     * @param noteLogHandler  handler for note log objects
     * @param ratingHandler  handler for rating objects
     * @param relatedMediaHandler  handler for related media objects
     * @param schemaTypeHandler  handler for schemaType objects
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     */
    public AssetHandler(String                    serviceName,
                        String                    serverName,
                        InvalidParameterHandler   invalidParameterHandler,
                        RepositoryHandler         repositoryHandler,
                        OMRSRepositoryHelper      repositoryHelper,
                        CertificationHandler      certificationHandler,
                        CommentHandler            commentHandler,
                        ConnectionHandler         connectionHandler,
                        ExternalIdentifierHandler externalIdentifierHandler,
                        ExternalReferenceHandler  externalReferenceHandler,
                        InformalTagHandler        informalTagHandler,
                        LicenseHandler            licenseHandler,
                        LikeHandler               likeHandler,
                        LocationHandler           locationHandler,
                        NoteLogHandler            noteLogHandler,
                        RatingHandler             ratingHandler,
                        RelatedMediaHandler       relatedMediaHandler,
                        SchemaTypeHandler         schemaTypeHandler,
                        List<String>              supportedZones,
                        List<String>              defaultZones)
    {
        this.serviceName               = serviceName;
        this.repositoryHelper          = repositoryHelper;
        this.serverName                = serverName;
        this.invalidParameterHandler   = invalidParameterHandler;
        this.repositoryHandler         = repositoryHandler;
        this.certificationHandler      = certificationHandler;
        this.commentHandler            = commentHandler;
        this.connectionHandler         = connectionHandler;
        this.externalIdentifierHandler = externalIdentifierHandler;
        this.externalReferenceHandler  = externalReferenceHandler;
        this.informalTagHandler        = informalTagHandler;
        this.licenseHandler            = licenseHandler;
        this.likeHandler               = likeHandler;
        this.locationHandler           = locationHandler;
        this.noteLogHandler            = noteLogHandler;
        this.ratingHandler             = ratingHandler;
        this.relatedMediaHandler       = relatedMediaHandler;
        this.schemaTypeHandler         = schemaTypeHandler;
        this.supportedZones            = supportedZones;
        this.defaultZones              = defaultZones;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }


    /**
     * Create an empty asset bean with its header filled out with the correct type information.
     * This bean can then be used with saveAsset() once the qualified name is filled in.
     *
     * @param assetTypeGUID guid of the asset's type (see AssetMapper)
     * @param assetTypeName name of the asset's type (see AssetMapper)
     * @return empty asset bean
     */
    public  Asset  createEmptyAsset(String   assetTypeGUID,
                                    String   assetTypeName)
    {
        Asset       asset = new Asset();
        ElementType elementType = new ElementType();


        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(assetTypeGUID);
        elementType.setElementTypeName(assetTypeName);

        asset.setType(elementType);

        return asset;
    }


    /**
     * Find out if the asset object is already stored in the repository.  If the asset's
     * guid is set, it uses it to retrieve the entity.  If the GUID is not set, it tries the
     * fully qualified name.  If neither are set it throws an exception.
     *
     * @param userId calling user
     * @param asset object to find
     * @param methodName calling method
     *
     * @return unique identifier of the asset or null
     *
     * @throws InvalidParameterException the asset bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String findAsset(String               userId,
                             Asset                asset,
                             String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String  guidParameterName = "asset.getGUID";
        final String  qualifiedNameParameter = "asset.getQualifiedName";

        if (asset != null)
        {
            if (asset.getGUID() != null)
            {
                if (repositoryHandler.validateEntityGUID(userId,
                                                         asset.getGUID(),
                                                         AssetMapper.ASSET_TYPE_NAME,
                                                         methodName,
                                                         guidParameterName) != null)
                {
                    return asset.getGUID();
                }
            }

            invalidParameterHandler.validateName(asset.getQualifiedName(), qualifiedNameParameter, methodName);

            AssetBuilder assetBuilder = new AssetBuilder(asset.getQualifiedName(),
                                                         asset.getDisplayName(),
                                                         asset.getDescription(),
                                                         repositoryHelper,
                                                         serviceName,
                                                         serverName);

            EntityDetail existingAsset = repositoryHandler.getUniqueEntityByName(userId,
                                                                                 asset.getQualifiedName(),
                                                                                 qualifiedNameParameter,
                                                                                 assetBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                                 AssetMapper.ASSET_TYPE_GUID,
                                                                                 AssetMapper.ASSET_TYPE_NAME,
                                                                                 methodName);
            if (existingAsset != null)
            {
                return existingAsset.getGUID();
            }
        }

        return null;
    }


    /**
     * Determine if the Asset object is stored in the repository and create it if it is not.
     * If the asset is located, there is no check that the asset values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param asset object to add
     * @param schemaType optional object to add
     * @param schemaAttributes optional object to add
     * @param connection optional object to add
     *
     * @return unique identifier of the asset in the repository.  If a connection object is provided,
     *         it is stored liked to the asset.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveAsset(String                  userId,
                             Asset                   asset,
                             SchemaType              schemaType,
                             List<SchemaAttribute>   schemaAttributes,
                             Connection              connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String  methodName = "saveAsset";

        String assetGUID = this.findAsset(userId, asset, methodName);
        if (assetGUID == null)
        {
            assetGUID = addAsset(userId, asset, connection);
        }
        else
        {
            assetGUID = updateAsset(userId, assetGUID, asset, connection);
        }

        if (schemaType != null)
        {
            this.saveAssociatedSchemaType(userId,
                                          assetGUID,
                                          schemaType,
                                          schemaAttributes,
                                          methodName);
        }

        return assetGUID;
    }


    /**
     * Determine if the Asset object is stored in the repository and create it if it is not.
     * If the asset is located, there is no check that the asset values are equal to those in
     * the supplied object.
     *
     * @param userId calling userId
     * @param asset object to add
     * @param connection optional object to add
     *
     * @return unique identifier of the asset in the repository.  If a connection object is provided,
     *         it is stored liked to the asset.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  saveAsset(String             userId,
                             Asset              asset,
                             Connection         connection) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName        = "saveAsset";
        
        String existingAsset = this.findAsset(userId, asset, methodName);
        if (existingAsset == null)
        {
            return addAsset(userId, asset, connection);
        }
        else
        {
            return updateAsset(userId, existingAsset, asset, connection);
        }
    }


    /**
     * Save any associated Connection.
     * 
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetSummary short description of the asset
     * @param connection connection object or null
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void saveAssociatedConnection(String                   userId,
                                         String                   assetGUID,
                                         String                   assetSummary,
                                         Connection               connection,
                                         String                   methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if (connection != null)
        {
            String connectionGUID = connectionHandler.saveConnection(userId, connection);;

            if (connectionGUID != null)
            {
                InstanceProperties properties = null;
                
                if (assetSummary != null) 
                {
                    properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              null,
                                                                              AssetMapper.SHORT_DESCRIPTION_PROPERTY_NAME,
                                                                              assetSummary,
                                                                              methodName);
                }
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                     connectionGUID,
                                                     assetGUID,
                                                     properties,
                                                     methodName);
            }
        }
    }


    /**
     * Save any associated schema type.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param schemaType schema Type object or null
     * @param schemaAttributes list of nested schema attribute objects or null
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  void saveAssociatedSchemaType(String                   userId,
                                          String                   assetGUID,
                                          SchemaType               schemaType,
                                          List<SchemaAttribute>    schemaAttributes,
                                          String                   methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if (schemaType != null)
        {
            String schemaTypeGUID = schemaTypeHandler.saveSchemaType(userId, schemaType, schemaAttributes, methodName);

            if (schemaTypeGUID != null)
            {
                repositoryHandler.createRelationship(userId,
                                                     AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                     schemaTypeGUID,
                                                     assetGUID,
                                                     null,
                                                     methodName);
            }
        }
    }


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String          userId,
                                        String          assetGUID,
                                        String          glossaryTermGUID,
                                        String          assetElementGUID,
                                        String          methodName)  throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String  assetGUIDParameter = "assetGUID";
        final String  glossaryTermGUIDParameter = "glossaryTermGUID";
        final String  assetElementGUIDParameter = "assetElementGUID";

        repositoryHandler.validateEntityGUID(userId,
                                             assetGUID,
                                             AssetMapper.ASSET_TYPE_NAME,
                                             methodName,
                                             assetGUIDParameter);

        repositoryHandler.validateEntityGUID(userId,
                                             glossaryTermGUID,
                                             MeaningMapper.MEANING_TYPE_NAME,
                                             methodName,
                                             glossaryTermGUIDParameter);

        repositoryHandler.validateEntityGUID(userId,
                                             assetElementGUID,
                                             ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                             methodName,
                                             assetElementGUIDParameter);

        repositoryHandler.createRelationship(userId,
                                             ReferenceableMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                             assetElementGUID,
                                             glossaryTermGUID,
                                             null,
                                             methodName);
    }


    /**
     * Add a simple asset description to the metadata repository.
     *
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param qualifiedName unique name for the asset in the catalog
     * @param displayName display name for the asset in the catalog
     * @param description description of the asset in the catalog
     * @param methodName calling method
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  addAsset(String               userId,
                            String               typeName,
                            String               qualifiedName,
                            String               displayName,
                            String               description,
                            Map<String, String>  additionalProperties,
                            Map<String, Object>  extendedProperties,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        // todo
        return null;
    }



    /**
     * Create a new Asset object and return its unique identifier (guid).
     * If the connection is supplied, it is connected to the asset.
     *
     * @param userId calling userId
     * @param connection object to add
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String addAsset(String             userId,
                            Asset              asset,
                            Connection         connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String  methodName        = "addAsset";

        String                   assetTypeGUID  = AssetMapper.ASSET_TYPE_GUID;
        String                   assetTypeName  = AssetMapper.ASSET_TYPE_NAME;

        ElementType   assetType = asset.getType();

        if (assetType != null)
        {
            if (assetType.getElementTypeId() != null)
            {
                assetTypeGUID = assetType.getElementTypeId();
            }

            if (assetType.getElementTypeName() != null)
            {
                assetTypeName = assetType.getElementTypeName();
            }
        }


        AssetBuilder assetBuilder = new AssetBuilder(asset.getQualifiedName(),
                                                     asset.getDisplayName(),
                                                     asset.getDescription(),
                                                     asset.getOwner(),
                                                     asset.getOwnerType(),
                                                     asset.getZoneMembership(),
                                                     asset.getLatestChange(),
                                                     asset.getAdditionalProperties(),
                                                     asset.getExtendedProperties(),
                                                     repositoryHelper,
                                                     serviceName,
                                                     serverName);

        String assetGUID = repositoryHandler.createEntity(userId,
                                                          assetTypeGUID,
                                                          assetTypeName,
                                                          assetBuilder.getInstanceProperties(methodName),
                                                          methodName);

        this.saveAssociatedConnection(userId,
                                      assetGUID,
                                      asset.getShortDescription(),
                                      connection,
                                      methodName);

        return assetGUID;
    }


    /**
     * Update a stored connection.
     *
     * @param userId userId
     * @param existingAssetGUID unique identifier of the existing connection entity
     * @param asset new asset values
     * @param connection new connection values
     *
     * @return unique identifier of the connection in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private String updateAsset(String      userId,
                               String      existingAssetGUID,
                               Asset       asset,
                               Connection  connection) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String  methodName        = "updateAsset";

        String                   assetTypeGUID  = AssetMapper.ASSET_TYPE_GUID;
        String                   assetTypeName  = AssetMapper.ASSET_TYPE_NAME;

        AssetBuilder assetBuilder = new AssetBuilder(asset.getQualifiedName(),
                                                     asset.getDisplayName(),
                                                     asset.getDescription(),
                                                     asset.getOwner(),
                                                     asset.getOwnerType(),
                                                     asset.getZoneMembership(),
                                                     asset.getLatestChange(),
                                                     asset.getAdditionalProperties(),
                                                     asset.getExtendedProperties(),
                                                     repositoryHelper,
                                                     serviceName,
                                                     serverName);
        repositoryHandler.updateEntity(userId,
                                       existingAssetGUID,
                                       assetTypeGUID,
                                       assetTypeName,
                                       assetBuilder.getInstanceProperties(methodName),
                                       methodName);

        this.saveAssociatedConnection(userId,
                                      existingAssetGUID,
                                      asset.getShortDescription(),
                                      connection,
                                      methodName);

        return existingAssetGUID;
    }


    /**
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addAssetOrigin(String                userId,
                                String                assetGUID,
                                String                organizationGUID,
                                String                businessCapabilityGUID,
                                Map<String, String>   otherOriginValues,
                                String                methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        // todo
    }


    /**
     * Update the zones for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAssetZones(String        userId,
                                 String        assetGUID,
                                 List<String>  assetZones,
                                 String        methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        // todo
    }


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
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
                                 String    ownerId,
                                 OwnerType ownerType,
                                 String    methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        // todo
    }


    /**
     * Remove the requested Asset.  This also removes any connected Connection objects if they are not connected
     * to any other asset definition.  This in turn may ripple down to deleting the endpoints, connector types and
     * any embedded connections that would be left isolated.
     *
     * @param userId calling user
     * @param assetGUID object to delete
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeAsset(String   userId,
                            String   assetGUID,
                            String   methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        final String  validatingParameterName = "qualifiedName";

        Asset asset = this.getAsset(userId, assetGUID, methodName);

        if (asset != null)
        {
            securityVerifier.validateUserForAssetDelete(userId, asset);

            /*
             * Locate the linked connections.
             */
            List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                        assetGUID,
                                                                                        AssetMapper.ASSET_TYPE_NAME,
                                                                                        AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                        AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                        methodName);

            /*
             * Remove the asset
             */
            repositoryHandler.deleteEntity(userId,
                                           assetGUID,
                                           AssetMapper.ASSET_TYPE_GUID,
                                           AssetMapper.ASSET_TYPE_NAME,
                                           validatingParameterName,
                                           asset.getQualifiedName(),
                                           methodName);

            if (relationships != null)
            {
                for (Relationship  relationship : relationships)
                {
                    if (relationship != null)
                    {
                        EntityProxy entityProxy = relationship.getEntityOneProxy();

                        if (entityProxy != null)
                        {
                            repositoryHandler.deleteRelationshipBetweenEntities(userId,
                                                                                AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                entityProxy.getGUID(),
                                                                                ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                assetGUID,
                                                                                methodName);

                            removeDisconnectedConnection(userId, entityProxy.getGUID(), methodName);
                        }
                    }
                }
            }
        }
    }



    /**
     * Remove the requested Connection if it is no longer connected to any other asset definition.
     *
     * @param userId calling user
     * @param connectionGUID potential object to delete
     * @param methodName calling method
     *
     * @throws InvalidParameterException the connection guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void  removeDisconnectedConnection(String       userId,
                                               String       connectionGUID,
                                               String       methodName)  throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        if (connectionGUID != null)
        {
            /*
             * Locate the linked assets.
             */
            List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                        connectionGUID,
                                                                                        ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                        AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                        AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                        methodName);
            if ((relationships == null) || relationships.isEmpty())
            {
                connectionHandler.removeConnection(userId, connectionGUID);
            }
        }
    }



    /**
     * Retrieve the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object.
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getAsset(String                 userId,
                          String                 assetGUID,
                          String                 methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String  guidParameterName = "assetGUID";

        EntityDetail assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                     assetGUID,
                                                                     guidParameterName,
                                                                     AssetMapper.ASSET_TYPE_NAME,
                                                                     methodName);

        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  assetGUID,
                                                                                  AssetMapper.ASSET_TYPE_NAME,
                                                                                  AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                  AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                  methodName);

        AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                           relationship,
                                                           repositoryHelper,
                                                           methodName);

        return assetConverter.getAssetBean();
    }


    /**
     * Retrieve the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object.
     * @param connectionGUID unique identifier of the attached connection object.
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getAsset(String                 userId,
                          String                 assetGUID,
                          String                 connectionGUID,
                          String                 methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String  guidParameterName = "assetGUID";

        EntityDetail assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                     assetGUID,
                                                                     guidParameterName,
                                                                     AssetMapper.ASSET_TYPE_NAME,
                                                                     methodName);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                     assetGUID,
                                                                                     AssetMapper.ASSET_TYPE_NAME,
                                                                                     connectionGUID,
                                                                                     AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                     AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                     methodName);

        AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                           relationship,
                                                           repositoryHelper,
                                                           methodName);

        return assetConverter.getAssetBean();
    }


    /**
     * Retrieve the connection object attached to the requested asset object.
     * This call assumes there is only one connection
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object.
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Connection getConnectionForAsset(String                 userId,
                                            String                 assetGUID) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName = "getConnectionForAsset";

        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  assetGUID,
                                                                                  AssetMapper.ASSET_TYPE_NAME,
                                                                                  AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                  AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                return connectionHandler.getConnection(userId, entityProxy.getGUID());
            }
        }

        return null;
    }


    /**
     * Retrieve the list of connection objects attached to the requested asset object.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object.
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Connection> getConnectionsForAsset(String                 userId,
                                                   String                 assetGUID) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String  methodName = "getConnectionsForAsset";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                   assetGUID,
                                                                                   AssetMapper.ASSET_TYPE_NAME,
                                                                                   AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                   AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                   methodName);

        List<Connection> connections = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship  relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = relationship.getEntityOneProxy();

                    if (entityProxy != null)
                    {
                        Connection linkedConnection = connectionHandler.getConnection(userId, entityProxy.getGUID());

                        if (linkedConnection != null)
                        {
                            connections.add(linkedConnection);
                        }
                    }
                }
            }
        }

        if (connections.isEmpty())
        {
            return null;
        }
        else
        {
            return connections;
        }
    }


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final  String   methodName = "getAssetForConnection";
        final  String   guidParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        Relationship  relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                   connectionGUID,
                                                                                   ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                   AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                   AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                   methodName);

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                return entityProxy.getGUID();
            }
        }

        return null;
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String userId,
                                             String connectionName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        Connection connection = connectionHandler.getConnectionByName(userId, connectionName);

        if ((connection != null) && (connection.getGUID() != null))
        {
            return this.getAssetForConnection(userId, connection.getGUID());
        }

        return null;
    }


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of Asset summaries
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Asset> getAssetsByName(String   userId,
                                       String   name,
                                       int      startFrom,
                                       int      pageSize,
                                       String   methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        AssetBuilder builder = new AssetBuilder(name,
                                                name,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                   builder.getQualifiedNameInstanceProperties(methodName),
                                                                                   AssetMapper.ASSET_TYPE_GUID,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   methodName);
        if (retrievedEntities == null)
        {
            retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                    builder.getNameInstanceProperties(methodName),
                                                                    AssetMapper.ASSET_TYPE_GUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName);
        }

        List<Asset>  results = new ArrayList<>();
        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {
                if (entity != null)
                {
                    AssetConverter  converter = new AssetConverter(entity, null, repositoryHelper, serviceName);

                    results.add(converter.getAssetBean());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the assets attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<RelatedAsset>  getRelatedAssets(String   userId,
                                                String   anchorGUID,
                                                int      startFrom,
                                                int      pageSize,
                                                String   methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        // todo
        return null;
    }



    /**
     * Return the count of attached certifications.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getCertificationCount(String   userId,
                                     String   anchorGUID,
                                     String   methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return certificationHandler.countCertifications(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of attached comments.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getCommentCount(String   userId,
                               String   anchorGUID,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return commentHandler.countAttachedComments(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of connections for the asset.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getConnectionCount(String   userId,
                                  String   anchorGUID,
                                  String   methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return connectionHandler.countAttachedConnections(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of external identifiers for this asset.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getExternalIdentifierCount(String   userId,
                                          String   anchorGUID,
                                          String   methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return externalIdentifierHandler.countExternalIdentifiers(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of attached external references.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getExternalReferencesCount(String   userId,
                                          String   anchorGUID,
                                          String   methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return externalReferenceHandler.countExternalReferences(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of attached informal tags.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getInformalTagCount(String   userId,
                                   String   anchorGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return informalTagHandler.countTags(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of license for this asset.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getLicenseCount(String   userId,
                               String   anchorGUID,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return licenseHandler.countLicenses(userId, anchorGUID, methodName);
    }


    /**
     * Return the number of likes for the asset.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getLikeCount(String   userId,
                            String   anchorGUID,
                            String   methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        return likeHandler.countLikes(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of known locations.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getKnownLocationsCount(String   userId,
                                      String   anchorGUID,
                                      String   methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return locationHandler.countKnownLocations(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of attached note logs.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getNoteLogsCount(String   userId,
                                String   anchorGUID,
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return noteLogHandler.countAttachedNoteLogs(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of attached ratings.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getRatingsCount(String   userId,
                               String   anchorGUID,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return ratingHandler.countRatings(userId, anchorGUID, methodName);
    }


    /**
     * Return the count of related assets.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getRelatedAssetCount(String   userId,
                                    String   anchorGUID,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        // todo - not sure this relationship exists
        return 0;
    }


    /**
     * Return the count of related media references.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int getRelatedMediaReferenceCount(String   userId,
                                             String   anchorGUID,
                                             String   methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return relatedMediaHandler.countRelatedMedia(userId, anchorGUID, methodName);
    }


    /**
     * Is there an attached schema for this asset?
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public SchemaType getSchemaType(String   userId,
                                    String   anchorGUID,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return schemaTypeHandler.getSchemaTypeForAsset(userId, anchorGUID, methodName);
    }
}
