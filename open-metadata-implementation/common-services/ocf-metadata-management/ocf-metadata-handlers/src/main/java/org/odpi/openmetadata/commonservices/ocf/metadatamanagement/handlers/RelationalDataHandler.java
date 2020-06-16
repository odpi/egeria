/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RelationalDataHandler manages the assets, connections and schemas for relational data.
 */
public class RelationalDataHandler
{
    private final String integratorGUIDParameterName = "integratorGUID";
    private final String integratorNameParameterName = "integratorName";

    private String                          serviceName;
    private String                          serverName;
    private List<String>                    defaultZones;
    private List<String>                    supportedZones;
    private List<String>                    publishZones;
    private OMRSRepositoryHelper            repositoryHelper;
    private RepositoryHandler               repositoryHandler;
    private InvalidParameterHandler         invalidParameterHandler;
    private AssetHandler                    assetHandler;
    private ConnectionHandler               connectionHandler;
    private ConnectorTypeHandler            connectorTypeHandler;
    private EndpointHandler                 endpointHandler;
    private GlossaryTermHandler             glossaryTermHandler;
    private SchemaTypeHandler               schemaTypeHandler;
    private SoftwareServerCapabilityHandler softwareServerCapabilityHandler;


    /**
     * Construct the relational data handler with information needed to work with assets, schemas, software server capability and connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param defaultZones list of default zones
     * @param supportedZones list of supported zones
     * @param publishZones list of publish zones
     * @param assetHandler handler for managing assets
     * @param connectionHandler handler for managing connections
     * @param schemaTypeHandler handler for schema elements
     * @param softwareServerCapabilityHandler handler for file systems
     * @param glossaryTermHandler handler for glossary terms
     */
    public RelationalDataHandler(String                          serviceName,
                                 String                          serverName,
                                 InvalidParameterHandler         invalidParameterHandler,
                                 RepositoryHandler               repositoryHandler,
                                 OMRSRepositoryHelper            repositoryHelper,
                                 List<String>                    defaultZones,
                                 List<String>                    supportedZones,
                                 List<String>                    publishZones,
                                 AssetHandler                    assetHandler,
                                 ConnectionHandler               connectionHandler,
                                 SchemaTypeHandler               schemaTypeHandler,
                                 SoftwareServerCapabilityHandler softwareServerCapabilityHandler,
                                 GlossaryTermHandler             glossaryTermHandler)
    {
        this.serviceName                     = serviceName;
        this.serverName                      = serverName;
        this.defaultZones                    = defaultZones;
        this.supportedZones                  = supportedZones;
        this.publishZones                    = publishZones;
        this.invalidParameterHandler         = invalidParameterHandler;
        this.repositoryHandler               = repositoryHandler;
        this.repositoryHelper                = repositoryHelper;

        /*
         * The asset handler and connection handler are supplied from caller since they have security verifiers
         * set inside them.  This other handlers are stateless and can be set up here.
         * However we pass schemaTypeHandler and glossaryTermHandler to remover the need to
         * understand lastAttachmentHandler.
         */

        this.assetHandler = assetHandler;
        this.connectionHandler = connectionHandler;
        this.softwareServerCapabilityHandler = softwareServerCapabilityHandler;

        this.endpointHandler = new EndpointHandler(serviceName,
                                                   serverName,
                                                   invalidParameterHandler,
                                                   repositoryHandler,
                                                   repositoryHelper);

        this.connectorTypeHandler = new ConnectorTypeHandler(serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper);

        this.schemaTypeHandler = schemaTypeHandler;

        this.glossaryTermHandler = glossaryTermHandler;
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database that is owned by an external element.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param qualifiedName unique name for this database
     * @param displayName the stored display name property for the database
     * @param description the stored description property associated with the database
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database - null means use the default zones set for this service
     * @param origin the properties that characterize where this database is from
     * @param latestChange latest change string for the database
     * @param createTime the time that the database was created
     * @param modifiedTime the last known time the data store was modified
     * @param encodingType the name of the encoding style used in the database
     * @param encodingLanguage the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabase(String               userId,
                                 String               integratorGUID,
                                 String               integratorName,
                                 String               qualifiedName,
                                 String               displayName,
                                 String               description,
                                 String               owner,
                                 OwnerType            ownerType,
                                 List<String>         zoneMembership,
                                 Map<String, String>  origin,
                                 String               latestChange,
                                 Date                 createTime,
                                 Date                 modifiedTime,
                                 String               encodingType,
                                 String               encodingLanguage,
                                 String               encodingDescription,
                                 String               databaseType,
                                 String               databaseVersion,
                                 String               databaseInstance,
                                 String               databaseImportedFrom,
                                 Map<String, String>  additionalProperties,
                                 String               typeName,
                                 Map<String, Object>  extendedProperties,
                                 String               methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        softwareServerCapabilityHandler.verifyIntegratorIdentity(userId, integratorGUID, integratorName, methodName);

        String assetTypeName = AssetMapper.DATABASE_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        Asset asset;
        if (integratorGUID == null)
        {
            asset = assetHandler.createEmptyAsset(assetTypeName, methodName);
        }
        else
        {
            asset = assetHandler.createEmptyExternalAsset(assetTypeName,
                                                          ElementOrigin.EXTERNAL_SOURCE,
                                                          integratorGUID,
                                                          integratorName,
                                                          methodName);
        }

        fillDatabaseAsset(asset,
                          qualifiedName,
                          displayName,
                          description,
                          owner,
                          ownerType,
                          zoneMembership,
                          origin,
                          latestChange,
                          createTime,
                          modifiedTime,
                          encodingType,
                          encodingLanguage,
                          encodingDescription,
                          databaseType,
                          databaseVersion,
                          databaseInstance,
                          databaseImportedFrom,
                          additionalProperties,
                          extendedProperties);

        /*
         * This call will set up the default zones and give ownership of the asset to the calling user.
         */
        return assetHandler.addExternalAsset(userId,
                                             asset,
                                             null,
                                             null,
                                             null,
                                             integratorGUID,
                                             integratorName,
                                             methodName);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for this database - must not be null
     * @param displayName the stored display name property for the database - if null, the value from the template is used
     * @param description the stored description property associated with the database - if null, the value from the template is used.
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseFromTemplate(String               userId,
                                             String               integratorGUID,
                                             String               integratorName,
                                             String               templateGUID,
                                             String               qualifiedName,
                                             String               displayName,
                                             String               description,
                                             String               methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return assetHandler.addExternalAssetFromTemplate(userId,
                                                         templateGUID,
                                                         AssetMapper.DATABASE_TYPE_NAME,
                                                         qualifiedName,
                                                         displayName,
                                                         description,
                                                         integratorGUID,
                                                         integratorName,
                                                         methodName);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to update
     * @param qualifiedName unique name for this database
     * @param displayName the stored display name property for the database
     * @param description the stored description property associated with the database
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database - null means use the default zones set for this service
     * @param origin the properties that characterize where this database is from
     * @param latestChange latest change string for the database
     * @param createTime the time that the database was created
     * @param modifiedTime the last known time the data store was modified
     * @param encodingType the name of the encoding style used in the database
     * @param encodingLanguage the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabase(String               userId,
                               String               integratorGUID,
                               String               integratorName,
                               String               databaseGUID,
                               String               qualifiedName,
                               String               displayName,
                               String               description,
                               String               owner,
                               OwnerType            ownerType,
                               List<String>         zoneMembership,
                               Map<String, String>  origin,
                               String               latestChange,
                               Date                 createTime,
                               Date                 modifiedTime,
                               String               encodingType,
                               String               encodingLanguage,
                               String               encodingDescription,
                               String               databaseType,
                               String               databaseVersion,
                               String               databaseInstance,
                               String               databaseImportedFrom,
                               Map<String, String>  additionalProperties,
                               String               typeName,
                               Map<String, Object>  extendedProperties,
                               String               methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String assetTypeName = AssetMapper.DATABASE_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        AssetConverter converter = assetHandler.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                           databaseGUID,
                                                                                           elementGUIDParameterName,
                                                                                           assetTypeName,
                                                                                           methodName);

        if (converter != null)
        {
            Asset originalAsset = converter.getAssetBean();

            /*
             * If the asset is not from one of the supported zones then it is effectively invisible.
             * An exception is thrown as if the GUID is not recognized.
             */
            invalidParameterHandler.validateAssetInSupportedZone(databaseGUID,
                                                                 elementGUIDParameterName,
                                                                 originalAsset.getZoneMembership(),
                                                                 supportedZones,
                                                                 serviceName,
                                                                 methodName);

            /*
             * If the integratorGUID is set, the instance belongs to an external metadata collection.
             * If the integratorGUID is not set then the element is a metadata collection in the local cohort.
             */
            ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
            if (integratorGUID == null)
            {
                expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
            }
            invalidParameterHandler.validateInstanceProvenanceForUpdate(databaseGUID,
                                                                        elementGUIDParameterName,
                                                                        originalAsset,
                                                                        expectedElementOrigin,
                                                                        integratorGUID,
                                                                        integratorName,
                                                                        serviceName,
                                                                        methodName);



            Asset updatedAsset = new Asset(originalAsset);

            fillDatabaseAsset(updatedAsset,
                              qualifiedName,
                              displayName,
                              description,
                              owner,
                              ownerType,
                              zoneMembership,
                              origin,
                              latestChange,
                              createTime,
                              modifiedTime,
                              encodingType,
                              encodingLanguage,
                              encodingDescription,
                              databaseType,
                              databaseVersion,
                              databaseInstance,
                              databaseImportedFrom,
                              additionalProperties,
                              extendedProperties);

            assetHandler.updateAsset(userId,
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
     * Add the supplied database properties to the asset.
     *
     * @param asset asset to fill
     * @param qualifiedName unique name for this database
     * @param displayName the stored display name property for the database
     * @param description the stored description property associated with the database
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database - null means use the default zones set for this service
     * @param origin the properties that characterize where this database is from
     * @param latestChange latest change string for the database
     * @param createTime the time that the database was created
     * @param modifiedTime the last known time the data store was modified
     * @param encodingType the name of the encoding style used in the database
     * @param encodingLanguage the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param extendedProperties properties from any subtype
     */
    private void fillDatabaseAsset(Asset                asset,
                                   String               qualifiedName,
                                   String               displayName,
                                   String               description,
                                   String               owner,
                                   OwnerType            ownerType,
                                   List<String>         zoneMembership,
                                   Map<String, String>  origin,
                                   String               latestChange,
                                   Date                 createTime,
                                   Date                 modifiedTime,
                                   String               encodingType,
                                   String               encodingLanguage,
                                   String               encodingDescription,
                                   String               databaseType,
                                   String               databaseVersion,
                                   String               databaseInstance,
                                   String               databaseImportedFrom,
                                   Map<String, String>  additionalProperties,
                                   Map<String, Object>  extendedProperties)
    {

        asset.setQualifiedName(qualifiedName);
        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setOwnerType(ownerType);
        asset.setZoneMembership(zoneMembership);
        asset.setOrigin(origin);
        asset.setLatestChange(latestChange);
        asset.setAdditionalProperties(additionalProperties);

        Map<String, Object> assetExtendedProperties = new HashMap<>();
        if (extendedProperties != null)
        {
            assetExtendedProperties.putAll(extendedProperties);
        }

        assetExtendedProperties.put(AssetMapper.CREATE_TIME_PROPERTY_NAME, createTime);
        assetExtendedProperties.put(AssetMapper.MODIFIED_TIME_PROPERTY_NAME, modifiedTime);
        assetExtendedProperties.put(AssetMapper.ENCODING_TYPE_PROPERTY_NAME, encodingType);
        assetExtendedProperties.put(AssetMapper.ENCODING_LANGUAGE_PROPERTY_NAME, encodingLanguage);
        assetExtendedProperties.put(AssetMapper.ENCODING_DESCRIPTION_PROPERTY_NAME, encodingDescription);
        assetExtendedProperties.put(AssetMapper.DATABASE_TYPE_PROPERTY_NAME, databaseType);
        assetExtendedProperties.put(AssetMapper.DATABASE_VERSION_PROPERTY_NAME, databaseVersion);
        assetExtendedProperties.put(AssetMapper.DATABASE_INSTANCE_PROPERTY_NAME, databaseInstance);
        assetExtendedProperties.put(AssetMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME, databaseImportedFrom);

        asset.setExtendedProperties(assetExtendedProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabase(String userId,
                                String integratorGUID,
                                String integratorName,
                                String databaseGUID,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        /*
         * If the integratorGUID is set, the instance belongs to an external metadata collection.
         * If the integratorGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (integratorGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }

        assetHandler.updateAssetZones(userId,
                                      databaseGUID,
                                      elementGUIDParameterName,
                                      expectedElementOrigin,
                                      integratorGUID,
                                      integratorName,
                                      publishZones,
                                      methodName);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabase(String userId,
                                 String integratorGUID,
                                 String integratorName,
                                 String databaseGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        /*
         * If the integratorGUID is set, the instance belongs to an external metadata collection.
         * If the integratorGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (integratorGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }

        assetHandler.updateAssetZones(userId,
                                      databaseGUID,
                                      elementGUIDParameterName,
                                      expectedElementOrigin,
                                      integratorGUID,
                                      integratorName,
                                      defaultZones,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabase(String userId,
                               String integratorGUID,
                               String integratorName,
                               String databaseGUID,
                               String qualifiedName,
                               String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the integratorGUID is set, the instance belongs to an external metadata collection.
         * If the integratorGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (integratorGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }
        assetHandler.removeAsset(userId,
                                 databaseGUID,
                                 qualifiedName,
                                 elementGUIDParameterName,
                                 expectedElementOrigin,
                                 integratorGUID,
                                 integratorName,
                                 methodName);
    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<Asset> findDatabases(String userId,
                                     String searchString,
                                     int    startFrom,
                                     int    pageSize,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return assetHandler.findAssets(userId,
                                       AssetMapper.DATABASE_TYPE_GUID,
                                       searchString,
                                       startFrom,
                                       pageSize,
                                       methodName);
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<Asset>   getDatabasesByName(String userId,
                                            String name,
                                            int    startFrom,
                                            int    pageSize,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return assetHandler.getAssetsByName(userId,
                                            AssetMapper.DATABASE_TYPE_GUID,
                                            name,
                                            startFrom,
                                            pageSize,
                                            methodName);
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
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
    public List<Asset>   getDatabasesByDaemon(String userId,
                                              String integratorGUID,
                                              String integratorName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        // todo
        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the database metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Asset getDatabaseByGUID(String userId,
                                   String guid,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        return assetHandler.getValidatedVisibleAsset(userId, supportedZones, guid, serviceName, methodName);
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param qualifiedName unique name for this database schema
     * @param displayName the stored display name property for the database schema
     * @param description the stored description property associated with the database schema
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database schema - null means use the default zones set for this service
     * @param origin the properties that characterize where this database schema is from
     * @param latestChange latest change string for the database schema
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchema(String               userId,
                                       String               integratorGUID,
                                       String               integratorName,
                                       String               databaseGUID,
                                       String               qualifiedName,
                                       String               displayName,
                                       String               description,
                                       String               owner,
                                       OwnerType            ownerType,
                                       List<String>         zoneMembership,
                                       Map<String, String>  origin,
                                       String               latestChange,
                                       Map<String, String>  additionalProperties,
                                       String               typeName,
                                       Map<String, Object>  extendedProperties,
                                       String               methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        softwareServerCapabilityHandler.verifyIntegratorIdentity(userId, integratorGUID, integratorName, methodName);

        String assetTypeName = AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        Asset asset;
        if (integratorGUID == null)
        {
            asset = assetHandler.createEmptyAsset(assetTypeName, methodName);
        }
        else
        {
            asset = assetHandler.createEmptyExternalAsset(assetTypeName,
                                                          ElementOrigin.EXTERNAL_SOURCE,
                                                          integratorGUID,
                                                          integratorName,
                                                          methodName);
        }

        fillDatabaseSchemaAsset(asset,
                                qualifiedName,
                                displayName,
                                description,
                                owner,
                                ownerType,
                                zoneMembership,
                                origin,
                                latestChange,
                                additionalProperties,
                                extendedProperties);

        ComplexSchemaType schemaType = new ComplexSchemaType();
        schemaType.setQualifiedName(qualifiedName + "_schemaType");
        schemaType.setDisplayName(displayName);

        /*
         * This call will set up the default zones and give ownership of the asset to the calling user.
         */
        String databaseSchemaGUID = assetHandler.addExternalAsset(userId,
                                                                  asset,
                                                                  schemaType,
                                                                  null,
                                                                  null,
                                                                  integratorGUID,
                                                                  integratorName,
                                                                  methodName);

        /*
         * This relationship links the database to the database schema.
         */
        repositoryHandler.createRelationship(userId,
                                             AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                             databaseGUID,
                                             databaseSchemaGUID,
                                             null,
                                             methodName);

        return databaseSchemaGUID;
    }


    /**
     * Add the supplied database properties to the asset.
     *
     * @param asset asset to fill
     * @param qualifiedName unique name for this database
     * @param displayName the stored display name property for the database
     * @param description the stored description property associated with the database
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database - null means use the default zones set for this service
     * @param origin the properties that characterize where this database is from
     * @param latestChange latest change string for the database

     * @param additionalProperties any arbitrary properties not part of the type system
     * @param extendedProperties properties from any subtype
     */
    private void fillDatabaseSchemaAsset(Asset                asset,
                                         String               qualifiedName,
                                         String               displayName,
                                         String               description,
                                         String               owner,
                                         OwnerType            ownerType,
                                         List<String>         zoneMembership,
                                         Map<String, String>  origin,
                                         String               latestChange,
                                         Map<String, String>  additionalProperties,
                                         Map<String, Object>  extendedProperties)
    {
        asset.setQualifiedName(qualifiedName);
        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setOwnerType(ownerType);
        asset.setZoneMembership(zoneMembership);
        asset.setOrigin(origin);
        asset.setLatestChange(latestChange);
        asset.setAdditionalProperties(additionalProperties);
        asset.setExtendedProperties(extendedProperties);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database schema
     * @param description the stored description property associated with the database schema
     * @param methodName calling method
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchemaFromTemplate(String userId,
                                                   String integratorGUID,
                                                   String integratorName,
                                                   String templateGUID,
                                                   String databaseGUID,
                                                   String qualifiedName,
                                                   String displayName,
                                                   String description,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return assetHandler.addExternalAssetFromTemplate(userId,
                                                         templateGUID,
                                                         AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                         qualifiedName,
                                                         displayName,
                                                         description,
                                                         integratorGUID,
                                                         integratorName,
                                                         methodName);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database schema
     * @param description the stored description property associated with the database schema
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database schema - null means use the default zones set for this service
     * @param origin the properties that characterize where this database schema is from
     * @param latestChange latest change string for the database schema
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseSchema(String              userId,
                                     String              integratorGUID,
                                     String              integratorName,
                                     String              databaseSchemaGUID,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              owner,
                                     OwnerType           ownerType,
                                     List<String>        zoneMembership,
                                     Map<String, String> origin,
                                     String              latestChange,
                                     Map<String, String> additionalProperties,
                                     String              typeName,
                                     Map<String, Object> extendedProperties,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String assetTypeName = AssetMapper.DATABASE_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        AssetConverter converter = assetHandler.retrieveAssetConverterFromRepositoryByGUID(userId,
                                                                                           databaseSchemaGUID,
                                                                                           elementGUIDParameterName,
                                                                                           assetTypeName,
                                                                                           methodName);

        if (converter != null)
        {
            Asset originalAsset = converter.getAssetBean();

            invalidParameterHandler.validateAssetInSupportedZone(databaseSchemaGUID,
                                                                 elementGUIDParameterName,
                                                                 originalAsset.getZoneMembership(),
                                                                 supportedZones,
                                                                 serviceName,
                                                                 methodName);

            Asset updatedAsset = new Asset(originalAsset);

            fillDatabaseSchemaAsset(updatedAsset,
                                    qualifiedName,
                                    displayName,
                                    description,
                                    owner,
                                    ownerType,
                                    zoneMembership,
                                    origin,
                                    latestChange,
                                    additionalProperties,
                                    extendedProperties);

            assetHandler.updateAsset(userId,
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
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabaseSchema(String userId,
                                      String integratorGUID,
                                      String integratorName,
                                      String databaseSchemaGUID,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        assetHandler.updateAssetZones(userId, databaseSchemaGUID, publishZones, methodName);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabaseSchema(String userId,
                                       String integratorGUID,
                                       String integratorName,
                                       String databaseSchemaGUID,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String elementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        assetHandler.updateAssetZones(userId, databaseSchemaGUID, publishZones, methodName);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseSchema(String userId,
                                     String integratorGUID,
                                     String integratorName,
                                     String databaseSchemaGUID,
                                     String qualifiedName,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the integratorGUID is set, the instance belongs to an external metadata collection.
         * If the integratorGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (integratorGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }

        assetHandler.removeAsset(userId,
                                 databaseSchemaGUID,
                                 qualifiedName,
                                 elementGUIDParameterName,
                                 expectedElementOrigin,
                                 integratorGUID,
                                 integratorName,
                                 methodName);
    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<Asset>   findDatabaseSchemas(String userId,
                                             String searchString,
                                             int    startFrom,
                                             int    pageSize,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return assetHandler.findAssets(userId,
                                       AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_GUID,
                                       searchString,
                                       startFrom,
                                       pageSize,
                                       methodName);
    }


    /**
     * Return the list of schemas associated with a database.
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the database to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of metadata elements describing the schemas associated with the requested database
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Asset>   getSchemasForDatabase(String userId,
                                               String databaseGUID,
                                               int    startFrom,
                                               int    pageSize,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);

        List<RelatedAsset> schemaAssets = assetHandler.getRelatedAssets(userId,
                                                                        supportedZones,
                                                                        databaseGUID,
                                                                        AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                                                        AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                        startFrom,
                                                                        pageSize,
                                                                        serviceName,
                                                                        methodName);

        return null;
    }


    /**
     * Retrieve the list of database schema metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<Asset>   getDatabaseSchemasByName(String userId,
                                                  String name,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the database schema metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Asset getDatabaseSchemaByGUID(String userId,
                                         String guid,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        Asset asset = assetHandler.getValidatedVisibleAsset(userId, supportedZones, guid, serviceName, methodName);

        this.validateAssetType(guid,
                               methodName,
                               asset,
                               AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);

        return asset;
    }


    /* ==========================================================================
     * A database schema may contain multiple database tables and database views.
     */

    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method

     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTable(String               userId,
                                      String               integratorGUID,
                                      String               integratorName,
                                      String               databaseSchemaGUID,
                                      String               qualifiedName,
                                      String               displayName,
                                      String               description,
                                      boolean              isDeprecated,
                                      List<String>         aliases,
                                      Map<String, String>  additionalProperties,
                                      String               typeName,
                                      Map<String, Object>  extendedProperties,
                                      String               methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param methodName calling method
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTableFromTemplate(String               userId,
                                                  String               integratorGUID,
                                                  String               integratorName,
                                                  String               templateGUID,
                                                  String               databaseSchemaGUID,
                                                  String               qualifiedName,
                                                  String               displayName,
                                                  String               description,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table to update
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseTable(String               userId,
                                    String               integratorGUID,
                                    String               integratorName,
                                    String               databaseTableGUID,
                                    String               qualifiedName,
                                    String               displayName,
                                    String               description,
                                    boolean              isDeprecated,
                                    List<String>         aliases,
                                    Map<String, String>  additionalProperties,
                                    String               typeName,
                                    Map<String, Object>  extendedProperties,
                                    String               methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String elementGUIDParameterName   = "databaseTableGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseTable(String userId,
                                    String integratorGUID,
                                    String integratorName,
                                    String databaseTableGUID,
                                    String qualifiedName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseTableGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<SchemaAttribute>   findDatabaseTables(String userId,
                                                      String searchString,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of database tables associated with a database schema.
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttribute>    getTablesForDatabaseSchema(String userId,
                                                               String databaseSchemaGUID,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               String methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of database table metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<SchemaAttribute>   getDatabaseTablesByName(String userId,
                                                           String name,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the database table metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseTableByGUID(String userId,
                                                  String guid,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param expression the code that generates the value for this view.
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseView(String               userId,
                                     String               integratorGUID,
                                     String               integratorName,
                                     String               databaseSchemaGUID,
                                     String               qualifiedName,
                                     String               displayName,
                                     String               description,
                                     boolean              isDeprecated,
                                     List<String>         aliases,
                                     String               expression,
                                     Map<String, String>  additionalProperties,
                                     String               typeName,
                                     Map<String, Object>  extendedProperties,
                                     String               methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseViewFromTemplate(String               userId,
                                                 String               integratorGUID,
                                                 String               integratorName,
                                                 String               templateGUID,
                                                 String               databaseSchemaGUID,
                                                 String               qualifiedName,
                                                 String               displayName,
                                                 String               description,
                                                 String               methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the database view to update
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param expression the code that generates the value for this view.
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseView(String               userId,
                                   String               integratorGUID,
                                   String               integratorName,
                                   String               databaseViewGUID,
                                   String               qualifiedName,
                                   String               displayName,
                                   String               description,
                                   boolean              isDeprecated,
                                   List<String>         aliases,
                                   String               expression,
                                   Map<String, String>  additionalProperties,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties,
                                   String               methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String elementGUIDParameterName   = "databaseViewGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseView(String userId,
                                   String integratorGUID,
                                   String integratorName,
                                   String databaseViewGUID,
                                   String qualifiedName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseViewGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<SchemaAttribute>   findDatabaseViews(String userId,
                                                     String searchString,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of database views associated with a database schema.
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttribute>    getViewsForDatabaseSchema(String userId,
                                                              String databaseSchemaGUID,
                                                              int    startFrom,
                                                              int    pageSize,
                                                              String methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of database view metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<SchemaAttribute>   getDatabaseViewsByName(String userId,
                                                          String name,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the database view metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseViewByGUID(String userId,
                                                 String guid,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param dataType data type name - for stored values
     * @param defaultValue string containing default value - for stored values
     * @param formula String formula - for derived values
     * @param queries list of queries and their target element
     * @param isDeprecated is this table deprecated?
     * @param elementPosition the position of this column in its parent table.
     * @param minCardinality minimum number of repeating instances allowed for this column - typically 1
     * @param maxCardinality the maximum number of repeating instances allowed for this column - typically 1
     * @param allowsDuplicateValues  whether the same value can be used by more than one instance of this attribute
     * @param orderedValues whether the attribute instances are arranged in an order
     * @param sortOrder the order that the attribute instances are arranged in - if any
     * @param minimumLength the minimum length of the data
     * @param length the length of the data field
     * @param significantDigits number of significant digits to the right of decimal point
     * @param isNullable whether the field is nullable or not
     * @param nativeJavaClass equivalent Java class implementation
     * @param defaultValueOverride default value for this column
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumn(String                             userId,
                                       String                             integratorGUID,
                                       String                             integratorName,
                                       String                             databaseTableGUID,
                                       String                             qualifiedName,
                                       String                             displayName,
                                       String                             description,
                                       String                             dataType,
                                       String                             defaultValue,
                                       String                             formula,
                                       List<SchemaImplementationQuery>    queries,
                                       boolean                            isDeprecated,
                                       int                                elementPosition,
                                       int                                minCardinality,
                                       int                                maxCardinality,
                                       boolean                            allowsDuplicateValues,
                                       boolean                            orderedValues,
                                       String                             defaultValueOverride,
                                       DataItemSortOrder                  sortOrder,
                                       int                                minimumLength,
                                       int                                length,
                                       int                                significantDigits,
                                       boolean                            isNullable,
                                       String                             nativeJavaClass,
                                       List<String>                       aliases,
                                       Map<String, String>                additionalProperties,
                                       String                             typeName,
                                       Map<String, Object>                extendedProperties,
                                       String                             methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table

     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumnFromTemplate(String               userId,
                                                   String               integratorGUID,
                                                   String               integratorName,
                                                   String               templateGUID,
                                                   String               databaseTableGUID,
                                                   String               qualifiedName,
                                                   String               displayName,
                                                   String               description,
                                                   String               methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param dataType data type name - for stored values
     * @param defaultValue string containing default value - for stored values
     * @param formula String formula - for derived values
     * @param queries list of queries and their target element
     * @param isDeprecated is this table deprecated?
     * @param elementPosition the position of this column in its parent table.
     * @param minCardinality minimum number of repeating instances allowed for this column - typically 1
     * @param maxCardinality the maximum number of repeating instances allowed for this column - typically 1
     * @param allowsDuplicateValues  whether the same value can be used by more than one instance of this attribute
     * @param orderedValues whether the attribute instances are arranged in an order
     * @param sortOrder the order that the attribute instances are arranged in - if any
     * @param minimumLength the minimum length of the data
     * @param length the length of the data field
     * @param significantDigits number of significant digits to the right of decimal point
     * @param isNullable whether the field is nullable or not
     * @param nativeJavaClass equivalent Java class implementation
     * @param defaultValueOverride default value for this column
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseColumn(String                          userId,
                                     String                          integratorGUID,
                                     String                          integratorName,
                                     String                          databaseColumnGUID,
                                     String                          qualifiedName,
                                     String                          displayName,
                                     String                          description,
                                     String                          dataType,
                                     String                          defaultValue,
                                     String                          formula,
                                     List<SchemaImplementationQuery> queries,
                                     boolean                         isDeprecated,
                                     int                             elementPosition,
                                     int                             minCardinality,
                                     int                             maxCardinality,
                                     boolean                         allowsDuplicateValues,
                                     boolean                         orderedValues,
                                     String                          defaultValueOverride,
                                     DataItemSortOrder               sortOrder,
                                     int                             minimumLength,
                                     int                             length,
                                     int                             significantDigits,
                                     boolean                         isNullable,
                                     String                          nativeJavaClass,
                                     List<String>                    aliases,
                                     Map<String, String>             additionalProperties,
                                     String                          typeName,
                                     Map<String, Object>             extendedProperties,
                                     String                          methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String elementGUIDParameterName   = "databaseColumnGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Update the metadata element representing a database derived column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param qualifiedName unique name for the database schema
     * @param displayName the stored display name property for the database table
     * @param description the stored description property associated with the database table
     * @param isDeprecated is this table deprecated?
     * @param elementPosition the position of this column in its parent table.
     * @param minCardinality minimum number of repeating instances allowed for this column - typically 1
     * @param maxCardinality the maximum number of repeating instances allowed for this column - typically 1
     * @param allowsDuplicateValues  whether the same value can be used by more than one instance of this attribute
     * @param orderedValues whether the attribute instances are arranged in an order
     * @param sortOrder the order that the attribute instances are arranged in - if any
     * @param minimumLength the minimum length of the data
     * @param length the length of the data field
     * @param significantDigits number of significant digits to the right of decimal point
     * @param isNullable whether the field is nullable or not
     * @param nativeJavaClass equivalent Java class implementation
     * @param defaultValueOverride default value for this column
     * @param aliases a list of alternative names for the attribute
     * @param expression the code that generates the value for this view.
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseDerivedColumn(String               userId,
                                            String               integratorGUID,
                                            String               integratorName,
                                            String               databaseColumnGUID,
                                            String               qualifiedName,
                                            String               displayName,
                                            String               description,
                                            boolean              isDeprecated,
                                            int                  elementPosition,
                                            int                  minCardinality,
                                            int                  maxCardinality,
                                            boolean              allowsDuplicateValues,
                                            boolean              orderedValues,
                                            String               defaultValueOverride,
                                            DataItemSortOrder    sortOrder,
                                            int                  minimumLength,
                                            int                  length,
                                            int                  significantDigits,
                                            boolean              isNullable,
                                            String               nativeJavaClass,
                                            List<String>         aliases,
                                            String               expression,
                                            Map<String, String>  additionalProperties,
                                            String               typeName,
                                            Map<String, Object>  extendedProperties,
                                            String               methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String elementGUIDParameterName   = "databaseColumnGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseColumn(String userId,
                                     String integratorGUID,
                                     String integratorName,
                                     String databaseColumnGUID,
                                     String qualifiedName,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseColumnGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<SchemaAttribute>   findDatabaseColumns(String userId,
                                                       String searchString,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of column for a database table (or view)
     *
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the database table of interest
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
    public List<SchemaAttribute>    getColumnsForDatabaseTable(String userId,
                                                               String databaseTableGUID,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               String methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseTableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the list of database column metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<SchemaAttribute>   getDatabaseColumnsByName(String userId,
                                                            String name,
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

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /**
     * Retrieve the database column metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseColumnByGUID(String userId,
                                                   String guid,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

        return null;
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column and it can be used to uniquely identify the column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param name name of primary key
     * @param keyPattern type of lifecycle and scope
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setPrimaryKeyOnColumn(String     userId,
                                      String     integratorGUID,
                                      String     integratorName,
                                      String     databaseColumnGUID,
                                      String     name,
                                      KeyPattern keyPattern,
                                      String     methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePrimaryKeyFromColumn(String userId,
                                           String integratorGUID,
                                           String integratorName,
                                           String databaseColumnGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param name the display name for UIs and reports
     * @param description description of the foreign key
     * @param confidence the level of confidence that the foreign key is correct.  This is a value between 0 and 100
     * @param steward the name of the steward who assigned the foreign key (or approved the discovered value)
     * @param source the id of the source of the knowledge of the foreign key
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addForeignKeyRelationship(String userId,
                                          String integratorGUID,
                                          String integratorName,
                                          String primaryKeyColumnGUID,
                                          String foreignKeyColumnGUID,
                                          String name,
                                          String description,
                                          int    confidence,
                                          String steward,
                                          String source,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForeignKeyRelationship(String userId,
                                             String integratorGUID,
                                             String integratorName,
                                             String primaryKeyColumnGUID,
                                             String foreignKeyColumnGUID,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);

        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);

    }


    /**
     * Throw an exception if the supplied guid returned an instance of the wrong type
     *
     * @param guid  unique identifier of instance
     * @param methodName  name of the method making the call.
     * @param assets  retrieved instances
     * @param expectedType  type the instance should be
     *
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    private void validateAssetListType(String       guid,
                                       String       methodName,
                                       List<Asset>  assets,
                                       String       expectedType) throws InvalidParameterException
    {
        if (assets != null)
        {
            for (Asset asset : assets)
            {
                this.validateAssetType(guid, methodName, asset, expectedType);
            }
        }
    }


    /**
     * Throw an exception if the supplied guid returned an instance of the wrong type
     *
     * @param guid  unique identifier of instance
     * @param methodName  name of the method making the call.
     * @param asset  retrieved instance
     * @param expectedType  type the instance should be
     *
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    private void validateAssetType(String guid,
                                   String methodName,
                                   Asset  asset,
                                   String expectedType) throws InvalidParameterException
    {
        if (asset != null)
        {
            if (asset.getType() != null)
            {
                String actualType = asset.getType().getElementTypeName();

                if (!repositoryHelper.isTypeOf(serviceName, actualType, expectedType))
                {
                    invalidParameterHandler.handleWrongTypeForGUIDException(guid,
                                                                            methodName,
                                                                            actualType,
                                                                            expectedType);
                }
            }
        }
    }


    /**
     * Throw an exception if the supplied guid returned an instance of the wrong type
     *
     * @param guid  unique identifier of instance
     * @param methodName  name of the method making the call.
     * @param schemaAttributes  retrieved instances
     * @param expectedType  type the instance should be
     *
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    private void validateSchemaAttributeListType(String                guid,
                                                 String                methodName,
                                                 List<SchemaAttribute> schemaAttributes,
                                                 String                expectedType) throws InvalidParameterException
    {
        if (schemaAttributes != null)
        {
            for (SchemaAttribute schemaAttribute : schemaAttributes)
            {
                this.validateSchemaAttributeType(guid, methodName, schemaAttribute, expectedType);
            }
        }
    }


    /**
     * Throw an exception if the supplied guid returned an instance of the wrong type
     *
     * @param guid  unique identifier of instance
     * @param methodName  name of the method making the call.
     * @param schemaAttribute  retrieved instance
     * @param expectedType  type the instance should be
     *
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    private void validateSchemaAttributeType(String          guid,
                                             String          methodName,
                                             SchemaAttribute schemaAttribute,
                                             String          expectedType) throws InvalidParameterException
    {
        if (schemaAttribute != null)
        {
            if (schemaAttribute.getType() != null)
            {
                String actualType = schemaAttribute.getType().getElementTypeName();

                if (!repositoryHelper.isTypeOf(serviceName, actualType, expectedType))
                {
                    invalidParameterHandler.handleWrongTypeForGUIDException(guid,
                                                                            methodName,
                                                                            actualType,
                                                                            expectedType);
                }
            }
        }
    }
}
