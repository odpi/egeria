/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.AssetBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssetHandler manages Asset objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 */
public class AssetHandler
{
    private String                    localServerUserId;
    private String                    serviceName;
    private OMRSRepositoryHelper      repositoryHelper;
    private String                    serverName;
    private InvalidParameterHandler   invalidParameterHandler;
    private RepositoryHandler         repositoryHandler;
    private CertificationHandler      certificationHandler;
    private CommentHandler            commentHandler;
    private ConnectionHandler         connectionHandler;
    private EndpointHandler           endpointHandler;
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
    private ValidValuesHandler        validValuesHandler;

    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

    protected List<String>            supportedZones;
    protected List<String>            defaultZones;


    /**
     * Construct the asset handler with information needed to work with Asset objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param localServerUserId name of the userId for system initiated updates to assets.
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param certificationHandler handler for certification objects
     * @param commentHandler handler for comment objects
     * @param connectionHandler handler for connection objects
     * @param endpointHandler handler for endpoint objects
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
     * @param validValuesHandler  handler for valid values and reference data objects
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @param defaultZones list of zones that DiscoveryEngine should set in all new Assets.
     */
    public AssetHandler(String                    serviceName,
                        String                    serverName,
                        String                    localServerUserId,
                        InvalidParameterHandler   invalidParameterHandler,
                        RepositoryHandler         repositoryHandler,
                        OMRSRepositoryHelper      repositoryHelper,
                        CertificationHandler      certificationHandler,
                        CommentHandler            commentHandler,
                        ConnectionHandler         connectionHandler,
                        EndpointHandler           endpointHandler,
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
                        ValidValuesHandler        validValuesHandler,
                        List<String>              supportedZones,
                        List<String>              defaultZones)
    {
        this.serviceName               = serviceName;
        this.repositoryHelper          = repositoryHelper;
        this.serverName                = serverName;
        this.localServerUserId         = localServerUserId;
        this.invalidParameterHandler   = invalidParameterHandler;
        this.repositoryHandler         = repositoryHandler;
        this.certificationHandler      = certificationHandler;
        this.commentHandler            = commentHandler;
        this.connectionHandler         = connectionHandler;
        this.endpointHandler           = endpointHandler;
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
        this.validValuesHandler        = validValuesHandler;
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
     * Return the list of supported zones for this asset.  This originates from the configuration of the access server.
     * but may be changed by the security verifier.
     *
     * @param userId calling user
     * @param serviceName called service
     * @return list of zone names
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     */
    private List<String> getSupportedZones(String      userId,
                                           String      serviceName) throws InvalidParameterException,
                                                                           PropertyServerException
    {
        return securityVerifier.setSupportedZonesForUser(supportedZones, serviceName, userId);
    }


    /**
     * Return the list of asset subtype names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTypesOfAssetList()
    {
        return repositoryHelper.getSubTypesOf(serviceName, AssetMapper.ASSET_TYPE_NAME);
    }


    /**
     * Return the list of asset subtype names mapped to their descriptions.
     *
     * @return list of type names that are subtypes of asset
     */
    public Map<String, String> getTypesOfAssetDescriptions()
    {
        List<String>        assetTypeList = repositoryHelper.getSubTypesOf(serviceName, AssetMapper.ASSET_TYPE_NAME);
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
     * Create an empty asset bean with its header filled out with the correct type information.
     * This bean can then be used with saveAsset() once the qualified name is filled in.
     *
     * @param requestedTypeName name of the asset's type (see AssetMapper)
     * @param methodName calling method
     * @return empty asset bean
     * @throws InvalidParameterException bad typeName
     */
    public  Asset  createEmptyAsset(String   requestedTypeName,
                                    String   methodName) throws InvalidParameterException
    {
        Asset       asset         = new Asset();
        ElementType elementType   = new ElementType();
        String      assetTypeName = AssetMapper.ASSET_TYPE_NAME;

        if (requestedTypeName != null)
        {
            assetTypeName = requestedTypeName;
        }

        String  assetTypeGUID = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                         AssetMapper.ASSET_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(assetTypeGUID);
        elementType.setElementTypeName(assetTypeName);

        asset.setType(elementType);

        return asset;
    }


    /**
     * Create an empty asset bean with its header filled out with the correct type information
     * and origin information.
     * This bean can then be used with saveAsset() once the qualified name is filled in.
     *
     * @param assetTypeName name of the asset's type (see AssetMapper)
     * @param elementOrigin type of origin
     * @param externalSourceGUID guid of the software server capability entity that represented the external source
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param methodName calling method
     * @return empty asset bean
     * @throws InvalidParameterException bad typeName
     */
    public  Asset  createEmptyExternalAsset(String        assetTypeName,
                                            ElementOrigin elementOrigin,
                                            String        externalSourceGUID,
                                            String        externalSourceName,
                                            String        methodName) throws InvalidParameterException
    {
        Asset       asset = this.createEmptyAsset(assetTypeName, methodName);

        if (asset != null)
        {
            ElementType elementType = asset.getType();

            elementType.setElementOrigin(elementOrigin);
            elementType.setElementHomeMetadataCollectionId(externalSourceGUID);
            elementType.setElementHomeMetadataCollectionName(externalSourceName);
        }

        return asset;
    }


    /**
     * Basic retrieval of an asset.  The connection relationship is passed to set up assetSummary.
     * If it is null, assetSummary is null.
     *
     * @param userId calling user
     * @param assetGUID unique
     * @param guidParameterName parameter that passed the guid
     * @param connectionRelationship optional link to connection in order to retrieve assetSummary
     * @param methodName calling method
     *
     * @return AssetConverter
     * @throws InvalidParameterException the asset is not found
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private AssetConverter  retrieveAssetConverterFromRepositoryByGUID(String       userId,
                                                                       String       assetGUID,
                                                                       String       guidParameterName,
                                                                       Relationship connectionRelationship,
                                                                       String       methodName) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        EntityDetail assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                     assetGUID,
                                                                     guidParameterName,
                                                                     AssetMapper.ASSET_TYPE_NAME,
                                                                     methodName);

        AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                           connectionRelationship,
                                                           repositoryHelper,
                                                           methodName);

        return assetConverter;
    }


    /**
     * Basic retrieval of an asset.  The connection relationship is passed to set up assetSummary.
     * If it is null, assetSummary is null.
     *
     * @param userId calling user
     * @param assetGUID unique
     * @param guidParameterName parameter that passed the guid
     * @param connectionRelationship optional link to connection in order to retrieve assetSummary
     * @param methodName calling method
     *
     * @return Asset
     * @throws InvalidParameterException the asset is not found
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private Asset  retrieveAssetFromRepositoryByGUID(String       userId,
                                                     String       assetGUID,
                                                     String       guidParameterName,
                                                     Relationship connectionRelationship,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        AssetConverter assetConverter = this.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                        assetGUID,
                                                                                        guidParameterName,
                                                                                        connectionRelationship,
                                                                                        methodName);

        if (assetConverter != null)
        {
            return assetConverter.getAssetBean();
        }

        return null;
    }


    /**
     * Retrieve the requested asset converter by name.
     *
     * @param userId calling userId
     * @param qualifiedName name to search for
     * @param qualifiedNameParameter parameter that passed the name
     * @param connectionRelationship link to connection (optional)
     * @param methodName calling method.
     * @return AssetConverter object
     * @throws InvalidParameterException the asset bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private AssetConverter retrieveAssetConverterFromRepositoryByName(String       userId,
                                                                      String       qualifiedName,
                                                                      String       qualifiedNameParameter,
                                                                      Relationship connectionRelationship,
                                                                      String       methodName) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        AssetBuilder assetBuilder = new AssetBuilder(qualifiedName,
                                                     null,
                                                     repositoryHelper,
                                                     serviceName,
                                                     serverName);

        EntityDetail assetEntity = repositoryHandler.getUniqueEntityByName(userId,
                                                                           qualifiedName,
                                                                           qualifiedNameParameter,
                                                                           assetBuilder.getQualifiedNameInstanceProperties(methodName),
                                                                           AssetMapper.ASSET_TYPE_GUID,
                                                                           AssetMapper.ASSET_TYPE_NAME,
                                                                           methodName);

        AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                           connectionRelationship,
                                                           repositoryHelper,
                                                           methodName);

        return assetConverter;
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
        final String  guidParameterName = "assetGUID";

        this.saveAssociatedConnection(userId,
                                      this.retrieveAssetFromRepositoryByGUID(userId,
                                                                             assetGUID,
                                                                             guidParameterName,
                                                                             null,
                                                                             methodName),
                                      assetSummary,
                                      connection,
                                      methodName);
    }


    /**
     * Save any associated Connection.
     *
     * @param userId calling user
     * @param asset unique identifier of the asset
     * @param assetSummary short description of the asset
     * @param connection connection object or null
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void saveAssociatedConnection(String                   userId,
                                          Asset                    asset,
                                          String                   assetSummary,
                                          Connection               connection,
                                          String                   methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String  assetGUIDParameter = "assetGUID";

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

            if (connection != null)
            {
                String connectionGUID = connectionHandler.saveConnection(userId, connection);

                if (connectionGUID != null)
                {
                    /*
                     * The connection object has been saved - or updated - depending on what is currently in the metadata repositories.
                     * Check that if a relationship does not exist between the asset and this connection then create it.
                     */
                    Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                                 connectionGUID,
                                                                                                 ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                                 asset.getGUID(),
                                                                                                 AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                                 AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                                 methodName);

                    if (relationship == null)
                    {
                        /*
                         * The asset summary property is stored in the relationship between the asset and the connection.
                         */
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
                                                             asset.getGUID(),
                                                             properties,
                                                             methodName);
                    }
                }
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
        final String  assetGUIDParameter = "assetGUID";

        this.saveAssociatedSchemaType(userId,
                                      this.retrieveAssetFromRepositoryByGUID(userId,
                                                                             assetGUID,
                                                                             assetGUIDParameter,
                                                                             null,
                                                                             methodName),
                                      schemaType,
                                      schemaAttributes,
                                      methodName);
    }


    /**
     * Save any associated schema type.
     *
     * @param userId calling user
     * @param asset properties of the asset
     * @param schemaType schema Type object or null
     * @param schemaAttributes list of nested schema attribute objects or null
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private  void saveAssociatedSchemaType(String                  userId,
                                          Asset                    asset,
                                          SchemaType               schemaType,
                                          List<SchemaAttribute>    schemaAttributes,
                                          String                   methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String  assetGUIDParameter = "assetGUID";

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

            if (schemaType != null)
            {
                String schemaTypeGUID = schemaTypeHandler.saveSchemaType(userId,
                                                                         schemaType,
                                                                         schemaAttributes,
                                                                         methodName);

                if (schemaTypeGUID != null)
                {
                    repositoryHandler.createRelationship(userId,
                                                         AssetMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                         schemaTypeGUID,
                                                         asset.getGUID(),
                                                         null,
                                                         methodName);
                }
            }
        }
    }


    /**
     * Validate that the user is able to make changes to the a.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void validateUserForAssetAttachmentUpdate(String userId,
                                                     String assetGUID,
                                                     String methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String  assetGUIDParameter = "assetGUID";

        Asset asset = this.retrieveAssetFromRepositoryByGUID(userId,
                                                             assetGUID,
                                                             assetGUIDParameter,
                                                             null,
                                                             methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        assetGUID,
                                                        AssetMapper.ASSET_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * an attribute in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String          userId,
                                        String          assetGUID,
                                        String          glossaryTermGUID,
                                        String          methodName)  throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String  assetGUIDParameter = "assetGUID";
        final String  glossaryTermGUIDParameter = "glossaryTermGUID";

        Asset  asset = this.retrieveAssetFromRepositoryByGUID(userId,
                                                              assetGUID,
                                                              assetGUIDParameter,
                                                              null,
                                                              methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

            repositoryHandler.validateEntityGUID(userId,
                                                 glossaryTermGUID,
                                                 MeaningMapper.MEANING_TYPE_NAME,
                                                 methodName,
                                                 glossaryTermGUIDParameter);

            repositoryHandler.createRelationship(userId,
                                                 ReferenceableMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                                 assetGUID,
                                                 glossaryTermGUID,
                                                 null,
                                                 methodName);
        }
    }


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * an attribute in the schema).
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

        Asset  asset = this.retrieveAssetFromRepositoryByGUID(userId,
                                                              assetGUID,
                                                              assetGUIDParameter,
                                                              null,
                                                              methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

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
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSemanticAssignment(String    userId,
                                          String    assetGUID,
                                          String    glossaryTermGUID,
                                          String    assetElementGUID,
                                          String    methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String  assetGUIDParameter = "assetGUID";
        final String  glossaryTermGUIDParameter = "glossaryTermGUID";
        final String  assetElementGUIDParameter = "assetElementGUID";

        Asset  asset = this.retrieveAssetFromRepositoryByGUID(userId,
                                                              assetGUID,
                                                              assetGUIDParameter,
                                                              null,
                                                              methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

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
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSemanticAssignment(String    userId,
                                          String    assetGUID,
                                          String    glossaryTermGUID,
                                          String    methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String  assetGUIDParameter = "assetGUID";
        final String  glossaryTermGUIDParameter = "glossaryTermGUID";
        final String  assetElementGUIDParameter = "assetElementGUID";

        Asset  asset = this.retrieveAssetFromRepositoryByGUID(userId,
                                                              assetGUID,
                                                              assetGUIDParameter,
                                                              null,
                                                              methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameter,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);

            repositoryHandler.validateEntityGUID(userId,
                                                 glossaryTermGUID,
                                                 MeaningMapper.MEANING_TYPE_NAME,
                                                 methodName,
                                                 glossaryTermGUIDParameter);

            repositoryHandler.createRelationship(userId,
                                                 ReferenceableMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                                 assetGUID,
                                                 glossaryTermGUID,
                                                 null,
                                                 methodName);
        }
    }


    /**
     * Add a simple asset description to the metadata repository.
     *
     * @param userId calling user (assumed to be the owner)
     * @param requestedTypeName specific type of the asset - this must match a defined subtype
     * @param qualifiedName unique name for the asset in the catalog
     * @param displayName display name for the asset in the catalog
     * @param description description of the asset in the catalog
     * @param additionalProperties user chosen additional properties
     * @param extendedProperties properties for a subtype of asset
     * @param methodName calling method
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  addAsset(String               userId,
                            String               requestedTypeName,
                            String               qualifiedName,
                            String               displayName,
                            String               description,
                            Map<String, String>  additionalProperties,
                            Map<String, Object>  extendedProperties,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        Asset asset = createEmptyAsset(requestedTypeName, methodName);

        asset.setQualifiedName(qualifiedName);
        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setAdditionalProperties(additionalProperties);
        asset.setExtendedProperties(extendedProperties);

        return this.addAsset(userId, asset, null, null, null, methodName);
    }



    /**
     * Create a new Asset object and return its unique identifier (guid).
     * If the connection is supplied, it is connected to the asset.
     *
     * @param userId calling userId
     * @param asset asset properties to add
     * @param connection object to add
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addAsset(String             userId,
                           Asset              asset,
                           Connection         connection,
                           String             methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.addAsset(userId,
                             asset,
                             null,
                             null,
                             connection,
                             methodName);
    }




    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling userId
     * @param asset object to add
     * @param schemaType optional object to add
     * @param schemaAttributes optional object to add
     * @param connection optional object to add
     * @param methodName calling method
     *
     * @return unique identifier of the asset in the repository.  If a connection or schema object is provided,
     *         it is stored linked to the asset.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  addAsset(String                  userId,
                            Asset                   asset,
                            SchemaType              schemaType,
                            List<SchemaAttribute>   schemaAttributes,
                            Connection              connection,
                            String                  methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String  qualifiedNameParameter = "asset.qualifiedName";

        if (asset != null)
        {
            /*
             * Check that the qualified name is not null
             */
            invalidParameterHandler.validateName(asset.getQualifiedName(), qualifiedNameParameter, methodName);

            /*
             * Use the calling user's id if no owner is requested
             */
            if (asset.getOwner() == null)
            {
                asset.setOwner(userId);
                asset.setOwnerType(OwnerType.USER_ID);
            }

            /*
             * Set up the latest change if not supplied.
             */
            final String defaultLatestChange = "Asset created";

            if (asset.getLatestChange() == null)
            {
                asset.setLatestChange(defaultLatestChange);
            }

            /*
             * Initialize the asset's zone membership
             */
            asset.setZoneMembership(securityVerifier.initializeAssetZones(defaultZones, asset));

            // todo validate the zone to ensure it is a defined zone and in the supported zones list.

            securityVerifier.validateUserForAssetCreate(userId, asset);

            String assetTypeGUID = null;
            String assetTypeName = null;

            ElementType assetType = asset.getType();

            if (assetType != null)
            {
                assetTypeGUID = assetType.getElementTypeId();
                assetTypeName = assetType.getElementTypeName();
            }

            AssetBuilder assetBuilder = new AssetBuilder(asset.getQualifiedName(),
                                                         asset.getDisplayName(),
                                                         asset.getDescription(),
                                                         asset.getOwner(),
                                                         asset.getOwnerType(),
                                                         asset.getZoneMembership(),
                                                         asset.getOrigin(),
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

            asset.setGUID(assetGUID);

            this.reclassifyAsset(userId,
                                 null,
                                 asset,
                                 assetBuilder.getZoneMembershipProperties(methodName),
                                 assetBuilder.getOwnerProperties(methodName),
                                 assetBuilder.getOriginProperties(methodName),
                                 methodName);

            this.saveAssociatedConnection(userId,
                                          assetGUID,
                                          asset.getShortDescription(),
                                          connection,
                                          methodName);

            this.saveAssociatedSchemaType(userId,
                                          assetGUID,
                                          schemaType,
                                          schemaAttributes,
                                          methodName);

            return assetGUID;
        }

        return null;
    }


    /**
     * Update a stored asset.
     *
     * @param userId userId
     * @param originalAsset current content of the asset
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param updatedAsset new asset values
     * @param schemaType optional object to add
     * @param schemaAttributes optional object to add
     * @param connection new connection values
     * @param methodName calling method
     *
     * @return unique identifier of the connection in the repository.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  String updateAsset(String                  userId,
                               Asset                   originalAsset,
                               AssetAuditHeader        originalAssetAuditHeader,
                               Asset                   updatedAsset,
                               SchemaType              schemaType,
                               List<SchemaAttribute>   schemaAttributes,
                               Connection              connection,
                               String                  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if (originalAsset != null)
        {
            updatedAsset.setZoneMembership(securityVerifier.verifyAssetZones(defaultZones,
                                                                             this.getSupportedZones(userId, serviceName),
                                                                             originalAsset,
                                                                             updatedAsset));

            securityVerifier.validateUserForAssetDetailUpdate(userId, originalAsset, originalAssetAuditHeader, updatedAsset);


            ElementType type = originalAsset.getType();

            String assetTypeGUID = type.getElementTypeId();
            String assetTypeName = type.getElementTypeName();

            AssetBuilder assetBuilder = new AssetBuilder(updatedAsset.getQualifiedName(),
                                                         updatedAsset.getDisplayName(),
                                                         updatedAsset.getDescription(),
                                                         updatedAsset.getOwner(),
                                                         updatedAsset.getOwnerType(),
                                                         updatedAsset.getZoneMembership(),
                                                         updatedAsset.getOrigin(),
                                                         updatedAsset.getLatestChange(),
                                                         updatedAsset.getAdditionalProperties(),
                                                         updatedAsset.getExtendedProperties(),
                                                         repositoryHelper,
                                                         serviceName,
                                                         serverName);
            repositoryHandler.updateEntity(userId,
                                           originalAsset.getGUID(),
                                           assetTypeGUID,
                                           assetTypeName,
                                           assetBuilder.getInstanceProperties(methodName),
                                           methodName);

            this.reclassifyAsset(userId,
                                 originalAsset,
                                 updatedAsset,
                                 assetBuilder.getZoneMembershipProperties(methodName),
                                 assetBuilder.getOwnerProperties(methodName),
                                 assetBuilder.getOriginProperties(methodName),
                                 methodName);

            this.saveAssociatedConnection(userId,
                                          originalAsset,
                                          updatedAsset.getShortDescription(),
                                          connection,
                                          methodName);

            this.saveAssociatedSchemaType(userId,
                                          originalAsset,
                                          schemaType,
                                          schemaAttributes,
                                          methodName);

            return originalAsset.getGUID();
        }

        return null;
    }


    /**
     * Add the ReferenceData classification to an asset.  IF the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     * @param supportedZones list of visible zones.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  classifyAssetAsReferenceData(String       userId,
                                              String       assetGUID,
                                              List<String> supportedZones,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        Asset asset = getValidatedVisibleAsset(userId, supportedZones, assetGUID, serviceName, methodName);
        if (asset != null)
        {
            repositoryHandler.classifyEntity(userId,
                                             assetGUID,
                                             AssetMapper.REFERENCE_DATA_CLASSIFICATION_GUID,
                                             AssetMapper.REFERENCE_DATA_CLASSIFICATION_NAME,
                                             null,
                                             methodName);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, serviceName, serverName, methodName);
        }
    }


    /**
     * Remove the ReferenceData classification form an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     * @param supportedZones list of visible zones.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  declassifyAssetAsReferenceData(String       userId,
                                                String       assetGUID,
                                                List<String> supportedZones,
                                                String       methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);

        Asset asset = getValidatedVisibleAsset(userId, supportedZones, assetGUID, serviceName, methodName);
        if (asset != null)
        {
            repositoryHandler.declassifyEntity(userId,
                                               assetGUID,
                                               AssetMapper.REFERENCE_DATA_CLASSIFICATION_GUID,
                                               AssetMapper.REFERENCE_DATA_CLASSIFICATION_NAME,
                                               methodName);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, serviceName, serverName, methodName);
        }
    }


    /**
     * Reclassifies an asset based on the new properties.
     *
     * @param userId userId
     * @param originalAsset current content of the asset
     * @param updatedAsset new asset values
     * @param zoneMembershipProperties zone membership properties
     * @param ownerProperties owner properties
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    @Deprecated
    public void reclassifyAsset(String             userId,
                                Asset              originalAsset,
                                Asset              updatedAsset,
                                InstanceProperties zoneMembershipProperties,
                                InstanceProperties ownerProperties,
                                String             methodName) throws UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        reclassifyAsset(userId, originalAsset, updatedAsset, zoneMembershipProperties, ownerProperties, null, methodName);
    }


    /**
     * Reclassifies an asset based on the new properties.
     *
     * @param userId userId
     * @param originalAsset current content of the asset
     * @param updatedAsset new asset values
     * @param zoneMembershipProperties zone membership properties
     * @param ownerProperties owner properties
     * @param originProperties origin properties
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
     public void reclassifyAsset(String             userId,
                                 Asset              originalAsset,
                                 Asset              updatedAsset,
                                 InstanceProperties zoneMembershipProperties,
                                 InstanceProperties ownerProperties,
                                 InstanceProperties originProperties,
                                 String             methodName) throws UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        if ((originalAsset == null) || (originalAsset.getZoneMembership() == null))
        {
            if (updatedAsset.getZoneMembership() != null)
            {
                repositoryHandler.classifyEntity(userId,
                                                 updatedAsset.getGUID(),
                                                 AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                 AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                 zoneMembershipProperties,
                                                 methodName);
            }
        }
        else
        {
            if (updatedAsset.getZoneMembership() == null)
            {
                repositoryHandler.declassifyEntity(userId,
                                                   updatedAsset.getGUID(),
                                                   AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                   methodName);
            }
            else if (!(originalAsset.getZoneMembership().equals(updatedAsset.getZoneMembership())))
            {
                repositoryHandler.reclassifyEntity(userId,
                                                   updatedAsset.getGUID(),
                                                   AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                   zoneMembershipProperties,
                                                   methodName);
            }
        }

        if ((originalAsset == null) || (originalAsset.getOwner() == null))
        {
            if (updatedAsset.getOwner() != null)
            {
                repositoryHandler.classifyEntity(userId,
                                                 updatedAsset.getGUID(),
                                                 AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                 AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                 ownerProperties,
                                                 methodName);
            }
        }
        else
        {
            if (updatedAsset.getOwner() == null)
            {
                repositoryHandler.declassifyEntity(userId,
                                                   originalAsset.getGUID(),
                                                   AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                   methodName);
            }
            else if (! originalAsset.getOwner().equals(updatedAsset.getOwner()))
            {
                repositoryHandler.reclassifyEntity(userId,
                                                   originalAsset.getGUID(),
                                                   AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                   ownerProperties,
                                                   methodName);
            }
        }

        if ((originalAsset == null) || (originalAsset.getOrigin() == null))
        {
            if (updatedAsset.getOrigin() != null)
            {
                repositoryHandler.classifyEntity(userId,
                                                 updatedAsset.getGUID(),
                                                 AssetMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                                 AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                 originProperties,
                                                 methodName);
            }
        }
        else
        {
            if (updatedAsset.getOrigin() == null)
            {
                repositoryHandler.declassifyEntity(userId,
                                                   originalAsset.getGUID(),
                                                   AssetMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                   methodName);
            }
            else if (!(originalAsset.getOrigin().equals(updatedAsset.getOrigin())))
            {
                repositoryHandler.reclassifyEntity(userId,
                                                   originalAsset.getGUID(),
                                                   AssetMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                                   AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                   originProperties,
                                                   methodName);
            }
        }
    }


    /**
     * Add the asset origin classification to an asset.  The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
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
        final String assetGUIDParameterName = "assetGUID";
        final String organizationGUIDParameterName = "assetGUID";
        final String businessCapabilityGUIDParameterName = "businessCapabilityGUID";

        AssetConverter converter = this.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                   assetGUID,
                                                                                   assetGUIDParameterName,
                                                                                   null,
                                                                                   methodName);

        if (converter != null)
        {
            Asset assetBean = converter.getAssetBean();

            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameterName,
                                                                 assetBean.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            Asset               updatedAsset  = new Asset(assetBean);
            InstanceProperties  properties    = null;
            Map<String, Object> propertiesMap = new HashMap<>();

            if (organizationGUID != null)
            {
                repositoryHandler.validateEntityGUID(userId,
                                                     organizationGUID,
                                                     OrganizationMapper.ORGANIZATION_TYPE_NAME,
                                                     methodName,
                                                     organizationGUIDParameterName);

                propertiesMap.put(AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME, organizationGUID);
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          null,
                                                                          AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                          organizationGUID,
                                                                          methodName);
            }

            if (businessCapabilityGUID != null)
            {
                repositoryHandler.validateEntityGUID(userId,
                                                     businessCapabilityGUID,
                                                     BusinessCapabilityMapper.BUSINESS_CAPABILITY_TYPE_NAME,
                                                     methodName,
                                                     businessCapabilityGUIDParameterName);
                propertiesMap.put(AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME, businessCapabilityGUID);
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          properties,
                                                                          AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                          businessCapabilityGUID,
                                                                          methodName);
            }

            if ((otherOriginValues != null) && (!otherOriginValues.isEmpty()))
            {
                propertiesMap.put(AssetMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME, otherOriginValues);
                properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             properties,
                                                                             AssetMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                             otherOriginValues,
                                                                             methodName);
            }

            Classification classification = new Classification();
            classification.setClassificationName(AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME);
            classification.setClassificationProperties(propertiesMap);

            List<Classification> assetClassifications = assetBean.getClassifications();

            if (assetClassifications == null)
            {
                assetClassifications = new ArrayList<>();
            }

            assetClassifications.add(classification);
            updatedAsset.setClassifications(assetClassifications);

            updatedAsset.setZoneMembership(securityVerifier.verifyAssetZones(defaultZones,
                                                                             this.getSupportedZones(userId, serviceName),
                                                                             assetBean,
                                                                             updatedAsset));

            securityVerifier.validateUserForAssetDetailUpdate(userId, assetBean, converter.getAssetAuditHeader(),
                                                              updatedAsset);

            repositoryHandler.classifyEntity(userId,
                                             assetGUID,
                                             AssetMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                             AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                             properties,
                                             methodName);
        }
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeAssetOrigin(String                userId,
                                   String                assetGUID,
                                   String                methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        // todo validate parameters and set up last attachment

        repositoryHandler.declassifyEntity(userId,
                                           assetGUID,
                                           AssetMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                           AssetMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                           methodName);
    }


    /**
     * Update the zones for a specific asset. The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
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
        final String assetGUIDParameterName = "assetGUID";

        AssetConverter converter = this.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                   assetGUID,
                                                                                   assetGUIDParameterName,
                                                                                   null,
                                                                                   methodName);

        if (converter != null)
        {
            Asset originalAsset = converter.getAssetBean();

            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameterName,
                                                                 originalAsset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            Asset updatedAsset = new Asset(originalAsset);

            updatedAsset.setZoneMembership(assetZones);
            updatedAsset.setLatestChange("New Zone Setting");

            this.updateAsset(userId,
                             originalAsset,
                             converter.getAssetAuditHeader(),
                             updatedAsset,
                             null,
                             null,
                             null,
                             methodName);
        }
    }


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties Descriptive labels describing origin of the asset
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                userId,
                                 String                assetGUID,
                                 List<String>          securityLabels,
                                 Map<String, Object>   securityProperties,
                                 String                methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        // todo
    }




    /**
     * Remove the security tags classification to an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecurityTags(String                userId,
                                    String                assetGUID,
                                    String                methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        // todo validate parameters and set up last attachment

        repositoryHandler.declassifyEntity(userId,
                                           assetGUID,
                                           ReferenceableMapper.SECURITY_TAG_TYPE_GUID,
                                           ReferenceableMapper.SECURITY_TAG_TYPE_NAME,
                                           methodName);
    }


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties Descriptive labels describing origin of the asset
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                userId,
                                 String                assetGUID,
                                 String                assetElementGUID,
                                 List<String>          securityLabels,
                                 Map<String, Object>   securityProperties,
                                 String                methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        // todo
    }


    /**
     * Remove the security tags classification to an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element where the security tags need to be removed.
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecurityTags(String                userId,
                                    String                assetGUID,
                                    String                assetElementGUID,
                                    String                methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        // todo validate parameters and set up last attachment

        repositoryHandler.declassifyEntity(userId,
                                           assetElementGUID,
                                           ReferenceableMapper.SECURITY_TAG_TYPE_GUID,
                                           ReferenceableMapper.SECURITY_TAG_TYPE_NAME,
                                           methodName);
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
        final String assetGUIDParameterName = "assetGUID";

        AssetConverter converter = this.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                   assetGUID,
                                                                                   assetGUIDParameterName,
                                                                                   null,
                                                                                   methodName);

        if (converter != null)
        {
            Asset originalAsset = converter.getAssetBean();

            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameterName,
                                                                 originalAsset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            Asset updatedAsset = new Asset(originalAsset);

            updatedAsset.setOwner(ownerId);
            updatedAsset.setOwnerType(ownerType);
            updatedAsset.setLatestChange("New Owner");

            updateAsset(userId,
                        originalAsset,
                        converter.getAssetAuditHeader(),
                        updatedAsset,
                        null,
                        null,
                        null,
                        methodName);
        }
    }


    /**
     * Update the latest change field in an asset because something has changed in the attached elements.
     * Notice that the security verifier is not called since the user attaching an element may not have
     * write access to the asset.
     *
     * @param callingUserId calling user
     * @param asset asset that has changed
     * @param latestChange description of latest call
     * @param methodName calling method
     */
    private void updateAssetLatestAttachment(String   callingUserId,
                                             Asset    asset,
                                             String   latestChange,
                                             String   methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        String userId = callingUserId;

        if (localServerUserId != null)
        {
            userId = localServerUserId;
        }

        ElementType type = asset.getType();

        String assetTypeGUID = type.getElementTypeId();
        String assetTypeName = type.getElementTypeName();

        AssetBuilder assetBuilder = new AssetBuilder(asset.getQualifiedName(),
                                                     asset.getDisplayName(),
                                                     asset.getDescription(),
                                                     asset.getOwner(),
                                                     asset.getOwnerType(),
                                                     asset.getZoneMembership(),
                                                     asset.getOrigin(),
                                                     latestChange,
                                                     asset.getAdditionalProperties(),
                                                     asset.getExtendedProperties(),
                                                     repositoryHelper,
                                                     serviceName,
                                                     serverName);
        repositoryHandler.updateEntity(userId,
                                       asset.getGUID(),
                                       assetTypeGUID,
                                       assetTypeName,
                                       assetBuilder.getInstanceProperties(methodName),
                                       methodName);
    }


    /**
     * Remove the requested Asset.  This also removes any connected Connection objects if they are not connected
     * to any other asset definition.  This in turn may ripple down to deleting the endpoints, connector types and
     * any embedded connections that would be left isolated.
     *
     * @param userId calling user
     * @param assetGUID object to delete
     * @param methodName calling method
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
        final String assetGUIDParameterName  = "assetGUID";
        final String validatingParameterName = "qualifiedName";

        Asset asset = this.getValidatedVisibleAsset(userId,
                                                    this.getSupportedZones(userId, serviceName),
                                                    assetGUID,
                                                    serviceName,
                                                    methodName);

        if (asset != null)
        {
            invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                                 assetGUIDParameterName,
                                                                 asset.getZoneMembership(),
                                                                 this.getSupportedZones(userId, serviceName),
                                                                 serviceName,
                                                                 methodName);

            securityVerifier.validateUserForAssetDelete(userId, asset);

            // todo needs to delete much more than connection
            // todo discovery engine needs to delete discovery reports (listen for relationships)

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
            repositoryHandler.removeEntity(userId,
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
                            repositoryHandler.removeRelationshipBetweenEntities(userId,
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
     * Determine if an asset is visible.
     *
     * @param userId calling user
     * @param supportedZones supported zones
     * @param guidParameterName parameter name for supplied guid
     * @param retrievedAsset retrieved asset
     * @param serviceName calling server
     * @param methodName calling method
     * @return asset
     * @throws InvalidParameterException asset is not in the zone
     * @throws UserNotAuthorizedException user is not authorized to access the asset.
     * @throws PropertyServerException problem managing the supported zones.
     */
    protected  Asset  validatedVisibleAsset(String        userId,
                                            List<String>  supportedZones,
                                            String        guidParameterName,
                                            Asset         retrievedAsset,
                                            String        serviceName,
                                            String        methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        invalidParameterHandler.validateAssetInSupportedZone(retrievedAsset.getGUID(),
                                                             guidParameterName,
                                                             retrievedAsset.getZoneMembership(),
                                                             securityVerifier.setSupportedZonesForUser(supportedZones,
                                                                                                       serviceName,
                                                                                                       userId),
                                                             serviceName,
                                                             methodName);

        securityVerifier.validateUserForAssetRead(userId, retrievedAsset);

        return retrievedAsset;
    }



    /**
     * Determine if the retrieved entity is for an asset and if so, is it visible?.
     *
     * @param userId calling user
     * @param supportedZones supported zones
     * @param guidParameterName parameter name for supplied guid
     * @param retrievedEntity retrieved asset
     * @param serviceName calling server
     * @param methodName calling method
     * @return asset
     * @throws InvalidParameterException asset is not in the zone
     * @throws UserNotAuthorizedException user is not authorized to access the asset.
     * @throws PropertyServerException problem managing the supported zones.
     */
    protected  Referenceable  validatedVisibleReferenceable(String        userId,
                                                            List<String>  supportedZones,
                                                            String        guidParameterName,
                                                            EntityDetail  retrievedEntity,
                                                            String        serviceName,
                                                            String        methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        Referenceable result = null;

        if (retrievedEntity != null)
        {
            InstanceType type = retrievedEntity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(serviceName, type.getTypeDefName(), AssetMapper.ASSET_TYPE_NAME))
                {
                    AssetConverter converter = new AssetConverter(retrievedEntity, null, repositoryHelper, serviceName);

                    result = this.validatedVisibleAsset(userId ,supportedZones, guidParameterName, converter.getAssetBean(), serviceName, methodName);
                }
                else
                {
                    ReferenceableConverter converter = new ReferenceableConverter(retrievedEntity, repositoryHelper, serviceName);

                    result = converter.getBean();
                }
            }
        }

        return result;
    }


    /**
     * Retrieve the requested asset object as long as it is visible to the user.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param methodName calling method
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getAssetBean(String                 userId,
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
     * Retrieve the requested asset object as long as it is visible to the user - the
     * UserNotAuthorizedException exception is thrown if it is not visible.
     *
     * @param userId calling user
     * @param supportedZones supported zones
     * @param assetGUID unique identifier of the asset object
     * @param serviceName calling service
     * @param methodName calling method
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getValidatedVisibleAsset(String                 userId,
                                          List<String>           supportedZones,
                                          String                 assetGUID,
                                          String                 serviceName,
                                          String                 methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String  guidParameterName = "assetGUID";

        Asset assetBean = this.getAssetBean(userId, assetGUID, methodName);

        return validatedVisibleAsset(userId,
                                     supportedZones,
                                     guidParameterName,
                                     assetBean,
                                     serviceName,
                                     methodName);
    }


    /**
     * Retrieve the requested asset object.
     *
     * @param userId calling user
     * @param supportedZones override the default supported zones.
     * @param assetConnectionRelationship link between the asset and the connection.
     * @param serviceName calling service
     * @param methodName calling method
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getValidatedVisibleAsset(String                 userId,
                                          List<String>           supportedZones,
                                          Relationship           assetConnectionRelationship,
                                          String                 serviceName,
                                          String                 methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String  guidParameterName = "assetGUID";

        if (assetConnectionRelationship != null)
        {
            EntityProxy connectionProxy = assetConnectionRelationship.getEntityOneProxy();
            EntityProxy assetProxy      = assetConnectionRelationship.getEntityTwoProxy();

            if ((connectionProxy != null) && (assetProxy != null))
            {
                /*
                 * This method throws exceptions if the asset entity can not be found
                 */
                EntityDetail assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                             assetProxy.getGUID(),
                                                                             guidParameterName,
                                                                             AssetMapper.ASSET_TYPE_NAME,
                                                                             methodName);


                AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                                   assetConnectionRelationship,
                                                                   repositoryHelper,
                                                                   methodName);

                return validatedVisibleAsset(userId,
                                             supportedZones,
                                             guidParameterName,
                                             assetConverter.getAssetBean(),
                                             serviceName,
                                             methodName);
            }
        }

        return null;
    }


    /**
     * Retrieve the requested asset object.
     *
     * @param userId calling user
     * @param supportedZones override the default supported zones.
     * @param assetGUID unique identifier of the asset object.
     * @param connectionGUID unique identifier of the attached connection object.
     * @param serviceName calling service
     * @param methodName calling method
     * @return Asset bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public Asset getValidatedVisibleAsset(String                 userId,
                                          List<String>           supportedZones,
                                          String                 assetGUID,
                                          String                 connectionGUID,
                                          String                 serviceName,
                                          String                 methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String  guidParameterName = "assetGUID";

        /*
         * This method throws exceptions if the asset entity can not be found
         */
        EntityDetail assetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                     assetGUID,
                                                                     guidParameterName,
                                                                     AssetMapper.ASSET_TYPE_NAME,
                                                                     methodName);

        Relationship relationship = null;

        if (connectionGUID != null)
        {
            relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                            assetGUID,
                                                                            AssetMapper.ASSET_TYPE_NAME,
                                                                            connectionGUID,
                                                                            AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                            AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                            methodName);
        }

        AssetConverter assetConverter = new AssetConverter(assetEntity,
                                                           relationship,
                                                           repositoryHelper,
                                                           methodName);

        return validatedVisibleAsset(userId,
                                     supportedZones,
                                     guidParameterName,
                                     assetConverter.getAssetBean(),
                                     serviceName,
                                     methodName);
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
     * Page through the list of valid values used to maintain the contents of a asset.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset to query
     * @param supportedZones list of zones that the asset must belong to one of
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param methodName calling method
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueImplementationDefinition> getValidValuesImplementationDefinitions(String       userId,
                                                                                            String       assetGUID,
                                                                                            List<String> supportedZones,
                                                                                            int          startFrom,
                                                                                            int          pageSize,
                                                                                            String       methodName) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        Asset asset = this.getValidatedVisibleAsset(userId, supportedZones, assetGUID, serviceName, methodName);

        if (asset != null)
        {
            return validValuesHandler.getValidValuesImplementationDefinitions(userId, assetGUID, startFrom, pageSize, methodName);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, serviceName, serverName, methodName);
            return null;
        }
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
     * @param methodName       calling method
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String userId,
                                             String connectionName,
                                             String methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        Connection connection = connectionHandler.getConnectionByName(userId, connectionName, methodName);

        if ((connection != null) && (connection.getGUID() != null))
        {
            return this.getAssetForConnection(userId, connection.getGUID());
        }

        return null;
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
    public List<String>  assetGUIDsScan(String   userId,
                                        String   subTypeGUID,
                                        String   subTypeName,
                                        int      startFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return(this.getGUIDs(assetScan(userId, subTypeGUID, subTypeName, startFrom, pageSize, methodName)));
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
    public List<Asset>  assetScan(String   userId,
                                  String   subTypeGUID,
                                  String   subTypeName,
                                  int      startFrom,
                                  int      pageSize,
                                  String   methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        String typeGUID = AssetMapper.ASSET_TYPE_GUID;
        String typeName = AssetMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            typeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            typeName = subTypeName;
        }

        List<Asset>  results = new ArrayList<>();
        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        while (moreResultsAvailable && (results.size() < pageSize))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesForType(userId, typeGUID, typeName, startFrom, pageSize, methodName);

            if (retrievedEntities != null)
            {
                results = this.getAssetList(userId,
                                            retrievedEntities,
                                            results,
                                            pageSize,
                                            methodName);

                moreResultsAvailable = (retrievedEntities.size() == pageSize);
                startNextQueryFrom = startNextQueryFrom + pageSize;
            }
            else
            {
                moreResultsAvailable = false;
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
     * Scan through the repository looking for assets by type and/or zone.  The zone and/or type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
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
    public List<Asset>  assetZoneScan(String   userId,
                                      String   zoneName,
                                      String   subTypeGUID,
                                      String   subTypeName,
                                      int      startFrom,
                                      int      pageSize,
                                      String   methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        String typeGUID = AssetMapper.ASSET_TYPE_GUID;
        String typeName = AssetMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            typeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            typeName = subTypeName;
        }

        List<String> zonesForVisibleAssets = this.getSupportedZones(userId, serviceName);

        if (zonesForVisibleAssets.contains(zoneName))
        {
            List<Asset>  results = new ArrayList<>();
            boolean      moreResultsAvailable = true;
            int          startNextQueryFrom = startFrom;

            while (moreResultsAvailable && (results.size() < pageSize))
            {
                List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesForType(userId, typeGUID, typeName, startFrom, pageSize, methodName);

                if (retrievedEntities != null)
                {
                    for (EntityDetail entity : retrievedEntities)
                    {
                        if ((entity != null) && (results.size() < pageSize))
                        {
                            AssetConverter  converter = new AssetConverter(entity, null, repositoryHelper, serviceName);
                            Asset           asset = converter.getAssetBean();
                            List<String>    assetZones = asset.getZoneMembership();

                            if ((assetZones != null) && (assetZones.contains(zoneName)))
                            {
                                results.add(asset);
                            }
                        }
                    }

                    moreResultsAvailable = (retrievedEntities.size() == pageSize);
                    startNextQueryFrom = startNextQueryFrom + pageSize;
                }
                else
                {
                    moreResultsAvailable = false;
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
     * Return a list of GUIDs based on a list of Assets.
     *
     * @param assets list of assets
     * @return list of GUIDs
     */
    private List<String> getGUIDs(List<Asset>  assets)
    {
        if ((assets != null) && (! assets.isEmpty()))
        {
            List<String>  results = new ArrayList<>();

            for (Asset asset : assets)
            {
                if (asset != null)
                {
                    String guid = asset.getGUID();

                    if (guid != null)
                    {
                        results.add(guid);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return  results;
            }

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
     * @return list of unique identifiers of Asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByQualifiedName(String   userId,
                                                     String   name,
                                                     int      startFrom,
                                                     int      pageSize,
                                                     String   methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return (getGUIDs(this.getAssetsByQualifiedName(userId, name, startFrom, pageSize, methodName)));
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
    public List<Asset> getAssetsByQualifiedName(String   userId,
                                                String   name,
                                                int      startFrom,
                                                int      pageSize,
                                                String   methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        AssetBuilder builder = new AssetBuilder(name,
                                                name,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        List<Asset>  results = new ArrayList<>();
        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        while (moreResultsAvailable && (queryPageSize == 0 || results.size() < queryPageSize))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                       builder.getQualifiedNameInstanceProperties(methodName),
                                                                                       AssetMapper.ASSET_TYPE_GUID,
                                                                                       startFrom,
                                                                                       queryPageSize,
                                                                                       methodName);

            if (retrievedEntities != null)
            {
                results = this.getAssetList(userId,
                                            retrievedEntities,
                                            results,
                                            queryPageSize,
                                            methodName);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
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
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of unique identifiers of Asset
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByName(String   userId,
                                            String   name,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getGUIDs(this.getAssetsByName(userId, name, startFrom, pageSize, methodName));
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
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        AssetBuilder builder = new AssetBuilder(name,
                                                name,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        List<Asset>  results = new ArrayList<>();
        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        while (moreResultsAvailable && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                       builder.getQualifiedNameInstanceProperties(methodName),
                                                                                       AssetMapper.ASSET_TYPE_GUID,
                                                                                       startFrom,
                                                                                       queryPageSize,
                                                                                       methodName);
            if (retrievedEntities == null)
            {
                retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                        builder.getNameInstanceProperties(methodName),
                                                                        AssetMapper.ASSET_TYPE_GUID,
                                                                        startFrom,
                                                                        queryPageSize,
                                                                        methodName);
            }

            if (retrievedEntities != null)
            {
                results = this.getAssetList(userId,
                                            retrievedEntities,
                                            results,
                                            queryPageSize,
                                            methodName);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
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
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param methodName calling method
     * @return list of unique identifiers for matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<String> getAssetGUIDsByEndpoint(String   userId,
                                                 String   networkAddress,
                                                 int      startFrom,
                                                 int      pageSize,
                                                 String   methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return(this.getGUIDs(this.getAssetsByEndpoint(userId, networkAddress, startFrom, pageSize, methodName)));
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param methodName calling method
     * @return list of  matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<Asset> getAssetsByEndpoint(String   userId,
                                            String   networkAddress,
                                            int      startFrom,
                                            int      pageSize,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String addressParameterName = "networkAddress";

        invalidParameterHandler.validateName(networkAddress, addressParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<Asset>     assets    = new ArrayList<>();
        boolean         moreResultsAvailable = true;
        int             startNextQueryFrom = startFrom;


        while (moreResultsAvailable && ((queryPageSize == 0) || (assets.size() < queryPageSize)))
        {
            List<Endpoint> endpoints = endpointHandler.getEndpointsByNetworkAddress(userId, networkAddress, startFrom, queryPageSize, methodName);

            if (endpoints != null)
            {
                for (Endpoint endpoint : endpoints)
                {
                    if (endpoint != null)
                    {
                        List<EntityDetail> connectionEntities = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                                                 endpoint.getGUID(),
                                                                                                                 EndpointMapper.ENDPOINT_TYPE_NAME,
                                                                                                                 ConnectionMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                                                                                 ConnectionMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                                                                 startNextQueryFrom,
                                                                                                                 queryPageSize,
                                                                                                                 methodName);

                        if (connectionEntities != null)
                        {
                            for (EntityDetail connectionEntity : connectionEntities)
                            {
                                if (connectionEntity != null)
                                {
                                    List<Relationship> connectionRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                                          connectionEntity.getGUID(),
                                                                                                                          ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                                                                          AssetMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                                                          AssetMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                                                          startFrom,
                                                                                                                          queryPageSize,
                                                                                                                          methodName);

                                    if (connectionRelationships != null)
                                    {
                                        for (Relationship connectionRelationship : connectionRelationships)
                                        {
                                            if (connectionRelationship != null)
                                            {
                                                Asset asset = this.getValidatedVisibleAsset(userId,
                                                                                            supportedZones,
                                                                                            connectionRelationship,
                                                                                            serviceName,
                                                                                            methodName);

                                                if ((asset != null) && assets.size() <= queryPageSize)
                                                {
                                                    assets.add(asset);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * If there are more endpoints to process, and the number of assets returned has not reached the max page size then
             * it is possible to receive more endpoints in an attempt to receive as many assets as possible.
             */
            if ((endpoints.size() < queryPageSize) || (assets.size() == queryPageSize))
            {
                moreResultsAvailable = false;
            }
            else
            {
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
        }

        if (assets.isEmpty())
        {
            return null;
        }
        else
        {
            return assets;
        }
    }



    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
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
                                        int      startFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return(this.getGUIDs(this.findAssets(userId, searchString, startFrom, pageSize, methodName)));
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
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
    public List<Asset>  findAssets(String   userId,
                                   String   searchString,
                                   int      startFrom,
                                   int      pageSize,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   searchParameter = "searchString";

        invalidParameterHandler.validateSearchString(searchString, searchParameter, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        AssetBuilder builder = new AssetBuilder(searchString,
                                                searchString,
                                                searchString,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        List<Asset>  results = new ArrayList<>();
        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        while (moreResultsAvailable && (queryPageSize == 0 || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                       builder.getSearchInstanceProperties(methodName),
                                                                                       AssetMapper.ASSET_TYPE_GUID,
                                                                                       startNextQueryFrom,
                                                                                       queryPageSize,
                                                                                       methodName);

            if (retrievedEntities != null)
            {
                results = this.getAssetList(userId,
                                            retrievedEntities,
                                            results,
                                            queryPageSize,
                                            methodName);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
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
     * Return the list of assets that can be retrieved from the entities that came from the repository.
     *
     * @param userId calling user
     * @param retrievedEntities entities from repository
     * @param currentAssetList currently extracted assets
     * @param maxPageSize maximum number of assets that can be returned in total.
     * @param methodName calling method
     * @return accumulated list of assets
     */
    private List<Asset>   getAssetList(String             userId,
                                       List<EntityDetail> retrievedEntities,
                                       List<Asset>        currentAssetList,
                                       int                maxPageSize,
                                       String             methodName)
    {
        List<Asset>  results = currentAssetList;

        if (results == null)
        {
            results = new ArrayList<>();
        }

        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {
                if ((entity != null) && (results.size() < maxPageSize))
                {
                    AssetConverter  converter = new AssetConverter(entity, null, repositoryHelper, serviceName);
                    Asset           asset = converter.getAssetBean();

                    try
                    {
                        results.add(validatedVisibleAsset(userId,
                                                          this.getSupportedZones(userId, serviceName),
                                                          "asset.getGUID()",
                                                          asset,
                                                          serviceName,
                                                          methodName));
                    }
                    catch (Throwable error)
                    {
                        /*
                         * ignore invisible asset
                         */
                    }
                }
            }
        }

        return results;
    }


    /**
     * This verifies that the asset exists and the caller has authority to attach to it.
     *
     * @param userId calling user
     * @param assetGUID asset's unique identity
     * @param supportedZones supported zones from caller
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid or null
     * @throws PropertyServerException the repository is not available or has a logic problem
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    private void verifyAttachment(String       userId,
                                  String       assetGUID,
                                  List<String> supportedZones,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName, guidParameter);

        Asset asset = getValidatedVisibleAsset(userId, supportedZones, assetGUID, serviceName, methodName);

        securityVerifier.validateUserForAssetFeedback(userId, asset);
    }


    /**
     * This verifies that the asset exists and the caller has authority to attach to it.
     *
     * @param userId calling user
     * @param assetGUID asset's unique identity
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid or null
     * @throws PropertyServerException the repository is not available or has a logic problem
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    private void verifyAttachment(String     userId,
                                  String     assetGUID,
                                  String     methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        this.verifyAttachment(userId, assetGUID, this.getSupportedZones(userId, serviceName), methodName);
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
     * Return the comments attached to an anchor entity.
     *
     * @param userId     calling user
     * @param supportedZones  list of zones if different from the default set in the handler
     * @param assetGUID identifier for the asset that this comment is chained from
     * @param anchorGUID identifier for the entity that the feedback is attached to - ie this asset or a
     *                   comment chained off of this asset
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Comment>  getAssetComments(String        userId,
                                           List<String>  supportedZones,
                                           String        assetGUID,
                                           String        anchorGUID,
                                           int           startingFrom,
                                           int           pageSize,
                                           String        methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, supportedZones, methodName);

        return commentHandler.getComments(userId,
                                          anchorGUID,
                                          AssetMapper.ASSET_TYPE_NAME,
                                          startingFrom,
                                          pageSize,
                                          methodName);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique identifier for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName    calling method
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToAsset(String      userId,
                                    String      assetGUID,
                                    CommentType commentType,
                                    String      commentText,
                                    boolean     isPublic,
                                    String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        return commentHandler.addCommentToAnchor(userId,
                                                 assetGUID,
                                                 AssetMapper.ASSET_TYPE_NAME,
                                                 commentType,
                                                 commentText,
                                                 isPublic,
                                                 methodName);
    }

    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param assetGUID    unique identifier for the anchor entity
     * @param commentGUID   unique identifier for the comment to change.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateAssetComment(String      userId,
                                     String      assetGUID,
                                     String      commentGUID,
                                     CommentType commentType,
                                     String      commentText,
                                     boolean     isPublic,
                                     String      methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        commentHandler.updateComment(userId,
                                     assetGUID,
                                     AssetMapper.ASSET_TYPE_NAME,
                                     commentGUID,
                                     commentType,
                                     commentText,
                                     isPublic,
                                     methodName);
    }


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param assetGUID    unique identifier for the referenceable.
     * @param attachmentGUID unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName    calling method
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String      userId,
                                  String      assetGUID,
                                  String      attachmentGUID,
                                  CommentType commentType,
                                  String      commentText,
                                  boolean     isPublic,
                                  String      methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        return commentHandler.addCommentReplyToAnchor(userId,
                                                      assetGUID,
                                                      AssetMapper.ASSET_TYPE_NAME,
                                                      attachmentGUID,
                                                      commentType,
                                                      commentText,
                                                      isPublic,
                                                      methodName);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param commentGUID  unique identifier for the comment object.
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeAssetComment(String     userId,
                                   String     assetGUID,
                                   String     commentGUID,
                                   String     methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        commentHandler.removeCommentFromAnchor(userId,
                                               assetGUID,
                                               AssetMapper.ASSET_TYPE_NAME,
                                               commentGUID,
                                               methodName);
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
     * Adds a tag (either private of public) to an asset.
     *
     * @param userId           userId of user making request.
     * @param assetGUID       unique id for the anchor.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTagToAsset(String  userId,
                              String  assetGUID,
                              String  tagGUID,
                              boolean isPublic,
                              String  methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        informalTagHandler.addTagToAnchor(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, tagGUID, isPublic, methodName);
    }

    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeTagFromAsset(String userId,
                                   String assetGUID,
                                   String tagGUID,
                                   String methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        informalTagHandler.removeTagFromAnchor(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, tagGUID, methodName);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetsByTag(String userId,
                                       String tagGUID,
                                       int    startFrom,
                                       int    pageSize,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        // todo
        return null;
    }

    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetsByMeaning(String userId,
                                           String termGUID,
                                           int    startFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        // todo
        return null;
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
     * Adds a "Like" to the asset.  If the user has already attached a like then the original one
     * is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset where the like is to be attached.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addLikeToAsset(String     userId,
                                 String     assetGUID,
                                 boolean    isPublic,
                                 String     methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        likeHandler.addLikeToReferenceable(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, isPublic, methodName);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param assetGUID unique identifier for the asset where the like is attached.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeLikeFromAsset(String     userId,
                                      String     assetGUID,
                                      String     methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        likeHandler.removeLikeFromReferenceable(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName);
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
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToAsset(String     userId,
                                 String     assetGUID,
                                 StarRating starRating,
                                 String     review,
                                 boolean    isPublic,
                                 String     methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String ratingParameter = "starRating";

        invalidParameterHandler.validateEnum(starRating, ratingParameter, methodName);

        verifyAttachment(userId, assetGUID, methodName);

        ratingHandler.addRatingToReferenceable(userId,
                                               assetGUID,
                                               AssetMapper.ASSET_TYPE_NAME,
                                               starRating,
                                               review,
                                               isPublic,
                                               methodName);
    }


    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset where the rating is attached.
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromAsset(String     userId,
                                      String     assetGUID,
                                      String     methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        verifyAttachment(userId, assetGUID, methodName);

        ratingHandler.removeRatingFromReferencable(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName);
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
        List<RelatedAsset>  relatedAssets = this.getRelatedAssets(userId,
                                                                  this.getSupportedZones(userId, serviceName),
                                                                  anchorGUID,
                                                                  0,
                                                                  invalidParameterHandler.getMaxPagingSize(),
                                                                  serviceName,
                                                                  methodName);

        if (relatedAssets == null)
        {
            return 0;
        }
        else
        {
            return relatedAssets.size();
        }
    }


    /**
     * Return all the assets attached to an anchor asset.
     *
     * @param userId     calling user
     * @param supportedZones override the default supported zones.
     * @param anchorGUID identifier for the asset that the related assets are attached to
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param serviceName calling service
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<RelatedAsset>  getRelatedAssets(String       userId,
                                                List<String> supportedZones,
                                                String       anchorGUID,
                                                int          startFrom,
                                                int          pageSize,
                                                String       serviceName,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getRelatedAssets(userId,
                                     supportedZones,
                                     anchorGUID,
                                     null,
                                     "all",
                                     startFrom,
                                     pageSize,
                                     serviceName,
                                     methodName);
    }


    /**
     * Return the assets attached to an anchor asset by the specified relationship type.  If all related assets
     * are required then specify null for the relationship type GUID and name.
     *
     * @param userId     calling user
     * @param supportedZones override the default supported zones.
     * @param anchorGUID identifier for the asset that the related assets are attached to
     * @param relationshipTypeGUID unique identifier for relationship type
     * @param relationshipTypeName unique name for relationship type
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param serviceName calling service
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<RelatedAsset>  getRelatedAssets(String       userId,
                                                List<String> supportedZones,
                                                String       anchorGUID,
                                                String       relationshipTypeGUID,
                                                String       relationshipTypeName,
                                                int          startFrom,
                                                int          pageSize,
                                                String       serviceName,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String relationshipTypeDisplayName = "all";

        if (relationshipTypeName != null)
        {
            relationshipTypeDisplayName = relationshipTypeName;
        }

        List<Relationship>  assetRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                          anchorGUID,
                                                                                          AssetMapper.ASSET_TYPE_NAME,
                                                                                          relationshipTypeGUID,
                                                                                          relationshipTypeDisplayName,
                                                                                          startFrom,
                                                                                          queryPageSize,
                                                                                          methodName);

        if (assetRelationships != null)
        {
            List<RelatedAsset>  relatedAssets = new ArrayList<>();

            for (Relationship  relationship : assetRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy relatedEntityProxy = repositoryHandler.getOtherEnd(anchorGUID, relationship);

                    if (relatedEntityProxy != null)
                    {
                        if (repositoryHelper.isTypeOf(serviceName,
                                                      relatedEntityProxy.getType().getTypeDefName(),
                                                      AssetMapper.ASSET_TYPE_NAME))
                        {
                            RelatedAsset relatedAsset = new RelatedAsset();

                            relatedAsset.setTypeName(relatedEntityProxy.getType().getTypeDefName());
                            relatedAsset.setAttributeName(repositoryHelper.getOtherEndName(serviceName, anchorGUID, relationship));
                            try
                            {
                                relatedAsset.setRelatedAsset(this.getValidatedVisibleAsset(userId,
                                                                                           supportedZones,
                                                                                           relatedEntityProxy.getGUID(),
                                                                                           serviceName,
                                                                                           methodName));
                            }
                            catch (UserNotAuthorizedException  notVisible)
                            {
                                /*
                                 * Ignore the asset that this user is not supposed to see.
                                 */
                            }

                            relatedAssets.add(relatedAsset);
                        }
                    }
                }
            }

            if (relatedAssets.isEmpty())
            {
                return null;
            }
            else
            {
                return relatedAssets;
            }
        }

        return null;
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
        this.getValidatedVisibleAsset(userId, supportedZones,  anchorGUID, serviceName, methodName);

        return schemaTypeHandler.getSchemaTypeForAsset(userId, anchorGUID, methodName);
    }
}
