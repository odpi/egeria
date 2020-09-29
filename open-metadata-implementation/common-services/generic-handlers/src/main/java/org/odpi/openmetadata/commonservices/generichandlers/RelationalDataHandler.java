/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * RelationalDataHandler manages the assets and schemas for relational data.
 */
public class RelationalDataHandler
{
    private String                          serviceName;
    private String                          serverName;
    private OMRSRepositoryHelper            repositoryHelper;
    private RepositoryHandler               repositoryHandler;
    private InvalidParameterHandler         invalidParameterHandler;
    private AssetHandler                    assetHandler;
    private SchemaTypeHandler               schemaTypeHandler;
    private SoftwareServerCapabilityHandler softwareServerCapabilityHandler;


    /**
     * Construct the relational data handler with information needed to work with assets, schemas,
     * software server capability and connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param assetHandler handler for managing assets
     * @param schemaTypeHandler handler for schema elements
     * @param softwareServerCapabilityHandler handler for file systems
     */
    public RelationalDataHandler(String                          serviceName,
                                 String                          serverName,
                                 InvalidParameterHandler         invalidParameterHandler,
                                 RepositoryHandler               repositoryHandler,
                                 OMRSRepositoryHelper            repositoryHelper,
                                 AssetHandler                    assetHandler,
                                 SchemaTypeHandler               schemaTypeHandler,
                                 SoftwareServerCapabilityHandler softwareServerCapabilityHandler)
    {
        this.serviceName                     = serviceName;
        this.serverName                      = serverName;
        this.invalidParameterHandler         = invalidParameterHandler;
        this.repositoryHandler               = repositoryHandler;
        this.repositoryHelper                = repositoryHelper;

        this.assetHandler = assetHandler;
        this.softwareServerCapabilityHandler = softwareServerCapabilityHandler;
        this.schemaTypeHandler = schemaTypeHandler;
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database that is owned by an external element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                 String               externalSourceGUID,
                                 String               externalSourceName,
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
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        softwareServerCapabilityHandler.verifyExternalSourceIdentity(userId, externalSourceGUID, externalSourceName, methodName);

        String assetTypeName = AssetMapper.DATABASE_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

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

        /*
         * This call will set up the default zones and give ownership of the asset to the calling user.
         */
        return assetHandler.addAsset(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     qualifiedName,
                                     displayName,
                                     description,
                                     owner,
                                     ownerType,
                                     zoneMembership,
                                     origin,
                                     latestChange,
                                     additionalProperties,
                                     assetTypeName,
                                     assetExtendedProperties,
                                     methodName);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                             String               externalSourceGUID,
                                             String               externalSourceName,
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
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return assetHandler.addAssetFromTemplate(userId,
                                                 templateGUID,
                                                 AssetMapper.DATABASE_TYPE_NAME,
                                                 qualifiedName,
                                                 displayName,
                                                 description,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 methodName);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                               String               externalSourceGUID,
                               String               externalSourceName,
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

        assetHandler.updateAsset(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 databaseGUID,
                                 qualifiedName,
                                 displayName,
                                 description,
                                 owner,
                                 ownerType,
                                 zoneMembership,
                                 origin,
                                 latestChange,
                                 additionalProperties,
                                 assetTypeName,
                                 assetExtendedProperties,
                                 methodName);
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
                                String databaseGUID,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        assetHandler.publishAsset(userId, databaseGUID, methodName);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabase(String userId,
                                 String databaseGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        assetHandler.withdrawAsset(userId, databaseGUID, methodName);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabase(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
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
         * If the externalSourceGUID is set, the instance belongs to an external metadata collection.
         * If the externalSourceGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (externalSourceGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }
        assetHandler.removeAsset(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 databaseGUID,
                                 qualifiedName,
                                 elementGUIDParameterName,
                                 expectedElementOrigin,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
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
                                              String externalSourceGUID,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return assetHandler.getAssetsByMetadataCollectionId(userId,
                                                            AssetMapper.DATABASE_TYPE_GUID,
                                                            externalSourceGUID,
                                                            startFrom,
                                                            pageSize,
                                                            methodName);
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

        /*
         * This call checks type of entity, zones and security.
         */
        return assetHandler.validateUserForAssetRead(userId, guid, AssetMapper.DATABASE_TYPE_NAME, methodName);
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                       String               externalSourceGUID,
                                       String               externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        softwareServerCapabilityHandler.verifyExternalSourceIdentity(userId, externalSourceGUID, externalSourceName, methodName);

        String assetTypeName = AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        /*
         * This call will set up the default zones and give ownership of the asset to the calling user.
         */
        String databaseSchemaGUID = assetHandler.addAsset(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          qualifiedName,
                                                          displayName,
                                                          description,
                                                          owner,
                                                          ownerType,
                                                          zoneMembership,
                                                          origin,
                                                          latestChange,
                                                          additionalProperties,
                                                          assetTypeName,
                                                          extendedProperties,
                                                          methodName);

        /*
         * This relationship links the database to the database schema.
         */
        repositoryHandler.createRelationship(userId,
                                             AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                             externalSourceGUID,
                                             externalSourceName,
                                             databaseGUID,
                                             databaseSchemaGUID,
                                             null,
                                             methodName);

        return databaseSchemaGUID;
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                                   String externalSourceGUID,
                                                   String externalSourceName,
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
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String databaseSchemaGUID = assetHandler.addAssetFromTemplate(userId,
                                                                      templateGUID,
                                                                      AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                                      qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      externalSourceGUID,
                                                                      externalSourceName,
                                                                      methodName);

        /*
         * This relationship links the database to the database schema.
         */
        repositoryHandler.createRelationship(userId,
                                             AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                             externalSourceGUID,
                                             externalSourceName,
                                             databaseGUID,
                                             databaseSchemaGUID,
                                             null,
                                             methodName);

        return databaseSchemaGUID;
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                     String              externalSourceGUID,
                                     String              externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String assetTypeName = AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        assetHandler.updateAsset(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 databaseSchemaGUID,
                                 qualifiedName,
                                 displayName,
                                 description,
                                 owner,
                                 ownerType,
                                 zoneMembership,
                                 origin,
                                 latestChange,
                                 additionalProperties,
                                 assetTypeName,
                                 extendedProperties,
                                 methodName);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabaseSchema(String userId,
                                      String databaseSchemaGUID,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        assetHandler.publishAsset(userId, databaseSchemaGUID, methodName);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabaseSchema(String userId,
                                       String databaseSchemaGUID,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String elementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        assetHandler.withdrawAsset(userId, databaseSchemaGUID, methodName);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseSchema(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String databaseSchemaGUID,
                                     String qualifiedName,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the externalSourceGUID is set, the instance belongs to an external metadata collection.
         * If the externalSourceGUID is not set then the element is a metadata collection in the local cohort.
         */
        ElementOrigin expectedElementOrigin = ElementOrigin.EXTERNAL_SOURCE;
        if (externalSourceGUID == null)
        {
            expectedElementOrigin = ElementOrigin.LOCAL_COHORT;
        }

        assetHandler.removeAsset(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 databaseSchemaGUID,
                                 qualifiedName,
                                 elementGUIDParameterName,
                                 expectedElementOrigin,
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
     * Return the list of (deployed database) schemas associated with a database.
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
                                                                        databaseGUID,
                                                                        AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                                                        AssetMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                        startFrom,
                                                                        pageSize,
                                                                        methodName);

        /*
         * The RelatedAsset is a wrapper around the asset we need to return.
         */
        List<Asset> assets = null;

        if (schemaAssets != null)
        {
            assets = new ArrayList<>();

            for (RelatedAsset relatedAsset : schemaAssets)
            {
                if (relatedAsset != null)
                {
                    if (relatedAsset.getRelatedAsset() != null)
                    {
                        assets.add(relatedAsset.getRelatedAsset());
                    }
                }
            }

            if (assets.isEmpty())
            {
                assets = null;
            }
        }

        return assets;
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
        return assetHandler.getAssetsByName(userId, AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_GUID, name, startFrom, pageSize, methodName);
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

        /*
         * This call checks type of entity, zones and security.
         */
        return assetHandler.validateUserForAssetRead(userId, guid, AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, methodName);
    }


    /* ==========================================================================
     * A database schema may contain multiple database tables and database views.
     */

    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller - if null a local element is created
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located
     * @param methodName calling method

     * @return properties of the anchor schema type for the database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private SchemaType getDatabaseSchemaSchemaType(String          userId,
                                                   String          externalSourceGUID,
                                                   String          externalSourceName,
                                                   String          databaseSchemaGUID,
                                                   String          methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        /*
         * It is not possible to update the attachments to assets that are outside of the supported zones or the user
         * does not have access to.  This call checks type of entity, zones and security.
         */
        Asset databaseSchemaAsset = assetHandler.validateUserForAssetRead(userId, databaseSchemaGUID, serviceName, methodName);

        if (databaseSchemaAsset == null)
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        databaseSchemaGUID,
                                                        AssetMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
            /* unreachable */
            return null;
        }

        /*
         * If the deployed database schema (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        SchemaType databaseSchemaType = schemaTypeHandler.getSchemaTypeForAsset(userId, databaseSchemaAsset.getGUID(), methodName);
        if (databaseSchemaType == null)
        {
            databaseSchemaType = schemaTypeHandler.getEmptyComplexSchemaType(SchemaElementMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME,
                                                                             null,
                                                                             externalSourceGUID,
                                                                             externalSourceName,
                                                                             methodName);

            databaseSchemaType.setQualifiedName("SchemaOf:" + databaseSchemaAsset.getQualifiedName());
            databaseSchemaType.setAnchorGUID(databaseSchemaAsset.getGUID());

            String databaseSchemaTypeGUID = assetHandler.saveAssociatedSchemaType(userId,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  databaseSchemaGUID,
                                                                                  databaseSchemaType,
                                                                                  null,
                                                                                  methodName);

            databaseSchemaType.setGUID(databaseSchemaTypeGUID);
        }

        return databaseSchemaType;
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located
     * @param qualifiedName unique name for the database table
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
                                      String               externalSourceGUID,
                                      String               externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * A database table is represented as a schemaAttribute of type RelationalTable (or a subtype).
         * Check that the type name requested is valid.
         */
        String attributeTypeName = SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME;
        String attributeTypeId   = SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID;

        if (typeName != null)
        {
            attributeTypeName = typeName;
            attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                         SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        }

        /*
         * If the deployed database schema (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        SchemaType databaseSchemaType = this.getDatabaseSchemaSchemaType(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         databaseSchemaGUID,
                                                                         methodName);

        if (databaseSchemaType != null)
        {
            /*
             * The schema type that connects the database schema asset to the tables has been created/retrieved.
             * Now work out the position of the new table in the database schema type.  This is used to set the element position.
             * Since this value begins with 0 as the first element, the table count is this table's position.
             */
            int tableCount = schemaTypeHandler.countSchemaAttributes(userId,
                                                                     databaseSchemaType.getGUID(),
                                                                     parentElementGUIDParameterName,
                                                                     methodName);

            /*
             * Load up the builder objects for processing by the schemaTypeHandler.  The builders manage the properties
             * of the metadata elements that make up the database table, and the schemaTypeHandler manages the elements themselves.
             */
            SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                       displayName,
                                                                                       description,
                                                                                       tableCount,
                                                                                       1,
                                                                                       1,
                                                                                       isDeprecated,
                                                                                       null,
                                                                                       true,
                                                                                       false,
                                                                                       null,
                                                                                       0,
                                                                                       0,
                                                                                       0,
                                                                                       false,
                                                                                       null,
                                                                                       aliases,
                                                                                       additionalProperties,
                                                                                       null,
                                                                                       attributeTypeName,
                                                                                       attributeTypeId,
                                                                                       extendedProperties,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       serverName);

            SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(qualifiedName + ":tableType",
                                                                        SchemaElementMapper.RELATIONAL_TABLE_TYPE_TYPE_NAME,
                                                                        SchemaElementMapper.RELATIONAL_TABLE_TYPE_TYPE_GUID,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);


            /*
             * Now create the table itself along with its schema type.  It also links the resulting table to the database schema type.
             * The returned value is the guid of the table.
             */
            return schemaTypeHandler.addSchemaAttribute(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        databaseSchemaGUID,
                                                        databaseSchemaType.getGUID(),
                                                        schemaAttributeBuilder,
                                                        schemaTypeBuilder,
                                                        methodName);
        }

        /*
         * Not reachable because any failures result in exceptions.
         */
        return null;
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller - if null a local element is created
     * @param externalSourceName unique name of software server capability representing the caller
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
                                                  String               externalSourceGUID,
                                                  String               externalSourceName,
                                                  String               templateGUID,
                                                  String               databaseSchemaGUID,
                                                  String               qualifiedName,
                                                  String               displayName,
                                                  String               description,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String guidParameterName = "databaseSchemaGUID";
        final String templateParameterName = "templateGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateGUID(databaseSchemaGUID, guidParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the deployed database schema (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        SchemaType databaseSchemaType = this.getDatabaseSchemaSchemaType(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         databaseSchemaGUID,
                                                                         methodName);

        if (databaseSchemaType != null)
        {
            return schemaTypeHandler.addSchemaAttributeFromTemplate(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    templateGUID,
                                                                    SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                    databaseSchemaType.getGUID(),
                                                                    databaseSchemaGUID,
                                                                    qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                    String               externalSourceGUID,
                                    String               externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String expectedTypeName = SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME;
        if (typeName != null)
        {
            expectedTypeName = typeName;
        }

        /*
         * Retrieve the current schema attribute for the database table. An exception is thrown if the guid is invalid
         */
        SchemaAttribute   tableSchemaAttribute = schemaTypeHandler.getSchemaAttribute(userId,
                                                                                      databaseTableGUID,
                                                                                      expectedTypeName,
                                                                                      null,
                                                                                      SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                      methodName);

        if (tableSchemaAttribute != null)
        {
            tableSchemaAttribute.setQualifiedName(qualifiedName);
            tableSchemaAttribute.setDisplayName(displayName);
            tableSchemaAttribute.setDescription(description);
            tableSchemaAttribute.setDeprecated(isDeprecated);
            tableSchemaAttribute.setAliases(aliases);
            tableSchemaAttribute.setAdditionalProperties(additionalProperties);
            tableSchemaAttribute.setExtendedProperties(extendedProperties);

            schemaTypeHandler.updateSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    databaseTableGUID,
                                                    tableSchemaAttribute);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        databaseTableGUID,
                                                        expectedTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseTable(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String databaseTableGUID,
                                    String qualifiedName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseTableGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        schemaTypeHandler.removeSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                databaseTableGUID,
                                                elementGUIDParameterName,
                                                SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                qualifiedName,
                                                qualifiedNameParameterName,
                                                methodName);
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

        return schemaTypeHandler.findSchemaAttributes(userId,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                      null,
                                                      SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                      searchString,
                                                      startFrom,
                                                      pageSize,
                                                      methodName);
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

        SchemaType databaseSchemaType = assetHandler.getSchemaType(userId, databaseSchemaGUID, methodName);

        if (databaseSchemaType != null)
        {
            return schemaTypeHandler.getSchemaAttributesForComplexSchemaType(userId,
                                                                             databaseSchemaType.getGUID(),
                                                                             null,
                                                                             SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                             null,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName);
        }

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

        return schemaTypeHandler.getSchemaAttributesByName(userId,
                                                           SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                           SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                           name,
                                                           null,
                                                           SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                           null,
                                                           startFrom,
                                                           pageSize,
                                                           methodName);
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

        return schemaTypeHandler.getSchemaAttribute(userId,
                                                    guid,
                                                    SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                    null,
                                                    SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                    methodName);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                     String               externalSourceGUID,
                                     String               externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * A database view is represented as a schemaAttribute of type RelationalTable (or a subtype).
         * Check that the type name requested is valid.
         */
        String attributeTypeName = SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME;
        String attributeTypeId   = SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID;

        if (typeName != null)
        {
            attributeTypeName = typeName;
            attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                         SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        }

        /*
         * If the deployed database schema (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        SchemaType databaseSchemaType = this.getDatabaseSchemaSchemaType(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         databaseSchemaGUID,
                                                                         methodName);

        if (databaseSchemaType != null)
        {
            /*
             * The schema type that connects the database schema asset to the tables has been created/retrieved.
             * Now work out the position of the new table in the database schema type.  This is used to set the element position.
             * Since this value begins with 0 as the first element, the table count is this table's position.
             */
            int tableCount = schemaTypeHandler.countSchemaAttributes(userId,
                                                                     databaseSchemaType.getGUID(),
                                                                     parentElementGUIDParameterName,
                                                                     methodName);

            /*
             * Load up the builder objects for processing by the schemaTypeHandler.  The builders manage the properties
             * of the metadata elements that make up the database table, and the schemaTypeHandler manages the elements themselves.
             */
            SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                       displayName,
                                                                                       description,
                                                                                       tableCount,
                                                                                       1,
                                                                                       1,
                                                                                       isDeprecated,
                                                                                       null,
                                                                                       true,
                                                                                       false,
                                                                                       null,
                                                                                       0,
                                                                                       0,
                                                                                       0,
                                                                                       false,
                                                                                       null,
                                                                                       aliases,
                                                                                       additionalProperties,
                                                                                       databaseSchemaGUID,
                                                                                       attributeTypeName,
                                                                                       attributeTypeId,
                                                                                       extendedProperties,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       serverName);

            SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(qualifiedName + ":tableType",
                                                                        SchemaElementMapper.RELATIONAL_TABLE_TYPE_TYPE_NAME,
                                                                        SchemaElementMapper.RELATIONAL_TABLE_TYPE_TYPE_GUID,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

            schemaTypeBuilder.setDerivedProperties(expression, null);

            /*
             * Now create the table itself along with its schema type.  It also links the resulting table to the database schema type.
             * The returned value is the guid of the table.
             */
            return schemaTypeHandler.addSchemaAttribute(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        databaseSchemaGUID,
                                                        databaseSchemaType.getGUID(),
                                                        schemaAttributeBuilder,
                                                        schemaTypeBuilder,
                                                        methodName);
        }

        /*
         * Not reachable because any failures result in exceptions.
         */
        return null;
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                                 String               externalSourceGUID,
                                                 String               externalSourceName,
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
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * If the deployed database schema (which is an asset) is new, it will not have a schema type attached.
         * However, if there are other tables already attached, the schema type will be there too.
         */
        SchemaType databaseSchemaType = this.getDatabaseSchemaSchemaType(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         databaseSchemaGUID,
                                                                         methodName);

        if (databaseSchemaType != null)
        {
            return schemaTypeHandler.addSchemaAttributeFromTemplate(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    templateGUID,
                                                                    SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                    databaseSchemaType.getGUID(),
                                                                    databaseSchemaGUID,
                                                                    qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                   String               externalSourceGUID,
                                   String               externalSourceName,
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
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String  expectedTypeName = SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME;
        if (typeName != null)
        {
            expectedTypeName = typeName;
        }

        /*
         * Retrieve the current schema attribute for the database view. An exception is thrown if the guid is invalid, points to
         * an entity of the wrong type, or one without the CalculatedValue classification on its schema type.
         */
        SchemaAttribute   viewSchemaAttribute = schemaTypeHandler.getSchemaAttribute(userId,
                                                                                     databaseViewGUID,
                                                                                     expectedTypeName,
                                                                                     SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                     null,
                                                                                     methodName);

        if (viewSchemaAttribute != null)
        {
            viewSchemaAttribute.setQualifiedName(qualifiedName);
            viewSchemaAttribute.setDisplayName(displayName);
            viewSchemaAttribute.setDescription(description);
            viewSchemaAttribute.setDeprecated(isDeprecated);
            viewSchemaAttribute.setAliases(aliases);
            viewSchemaAttribute.setAdditionalProperties(additionalProperties);
            viewSchemaAttribute.setExtendedProperties(extendedProperties);

            SchemaType schemaType = viewSchemaAttribute.getAttributeType();
            if (schemaType != null)
            {
                schemaType.setFormula(expression);
            }
            else
            {
                String metadataCollectionId   = "<Unknown>";
                String metadataCollectionName = "<Unknown>";

                if (viewSchemaAttribute.getType() != null)
                {
                    metadataCollectionId   = viewSchemaAttribute.getType().getElementMetadataCollectionId();
                    metadataCollectionName = viewSchemaAttribute.getType().getElementMetadataCollectionName();
                }
                throw new PropertyServerException(OCFErrorCode.UNKNOWN_SCHEMA_TYPE.getMessageDefinition(methodName,
                                                                                                        expectedTypeName,
                                                                                                        metadataCollectionId,
                                                                                                        metadataCollectionName),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            schemaTypeHandler.updateSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    databaseViewGUID,
                                                    viewSchemaAttribute);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        databaseViewGUID,
                                                        expectedTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseView(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String databaseViewGUID,
                                   String qualifiedName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseViewGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        schemaTypeHandler.removeSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                databaseViewGUID,
                                                elementGUIDParameterName,
                                                SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                qualifiedName,
                                                qualifiedNameParameterName,
                                                methodName);
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

        return schemaTypeHandler.findSchemaAttributes(userId,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                      SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                      null,
                                                      searchString,
                                                      startFrom,
                                                      pageSize,
                                                      methodName);
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

        SchemaType databaseSchemaType = assetHandler.getSchemaType(userId, databaseSchemaGUID, methodName);

        if (databaseSchemaType != null)
        {
            return schemaTypeHandler.getSchemaAttributesForComplexSchemaType(userId,
                                                                             databaseSchemaType.getGUID(),
                                                                             parentElementGUIDParameterName,
                                                                             SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                             null,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName);
        }

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

        return schemaTypeHandler.getSchemaAttributesByName(userId,
                                                           SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                           SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                           name,
                                                           SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                           null,
                                                           null,
                                                           startFrom,
                                                           pageSize,
                                                           methodName);
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

        return schemaTypeHandler.getSchemaAttribute(userId,
                                                    guid,
                                                    SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                    SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                    null,
                                                    methodName);
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */

    /**
     * Retrieve the unique identifier of the database table type.  This is the entity that the database column is connected to.
     * This will also validate that the database table is connected to a valid, visible column that can be updated.
     *
     * @param databaseTable bean for the database table (ie the schema attribute)
     * @param methodName calling method
     *
     * @return unique identifier of the database table type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     */
    private String getDatabaseTableTypeGUID(SchemaAttribute databaseTable,
                                            String          methodName) throws InvalidParameterException
    {
        final String databaseTableTypeParameterName = "databaseTableTypeGUID";

        String databaseTableTypeGUID = null;

        if (databaseTable != null)
        {
            if (databaseTable.getAttributeType() != null)
            {
                databaseTableTypeGUID = databaseTable.getAttributeType().getGUID();
            }
            else if (databaseTable.getExternalAttributeType() != null)
            {
                databaseTableTypeGUID = databaseTable.getExternalAttributeType().getLinkedSchemaTypeGUID();
            }
        }

        invalidParameterHandler.validateGUID(databaseTableTypeGUID, databaseTableTypeParameterName, methodName);

        return databaseTableTypeGUID;
    }


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                       String                             externalSourceGUID,
                                       String                             externalSourceName,
                                       String                             databaseTableGUID,
                                       String                             qualifiedName,
                                       String                             displayName,
                                       String                             description,
                                       String                             dataType,
                                       String                             defaultValue,
                                       String                             formula,
                                       List<DerivedSchemaTypeQueryTarget> queries,
                                       boolean                            isDeprecated,
                                       int                                elementPosition,
                                       int                                minCardinality,
                                       int                                maxCardinality,
                                       boolean                            allowsDuplicateValues,
                                       boolean                            orderedValues,
                                       String                             defaultValueOverride,
                                       EnumPropertyValue                  sortOrder,
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
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        /*
         * A database column is represented as a schemaAttribute of type RelationalColumn (or a subtype).
         * Check that the type name requested is valid.
         */
        String attributeTypeName = SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME;
        String attributeTypeId   = SchemaElementMapper.RELATIONAL_COLUMN_TYPE_GUID;

        if (typeName != null)
        {
            attributeTypeName = typeName;
            attributeTypeId   = invalidParameterHandler.validateTypeName(typeName,
                                                                         SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                                         serviceName,
                                                                         methodName,
                                                                         repositoryHelper);
        }

        SchemaAttribute databaseTable = schemaTypeHandler.getSchemaAttribute(userId,
                                                                             databaseTableGUID,
                                                                             SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                             null,
                                                                             null,
                                                                             methodName);

        String databaseTableTypeGUID = getDatabaseTableTypeGUID(databaseTable, methodName);


        /*
         * Load up the builder objects for processing by the schemaTypeHandler.  The builders manage the properties
         * of the metadata elements that make up the database table, and the schemaTypeHandler manages the elements themselves.
         */
        SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(qualifiedName,
                                                                                   displayName,
                                                                                   description,
                                                                                   elementPosition,
                                                                                   minCardinality,
                                                                                   maxCardinality,
                                                                                   isDeprecated,
                                                                                   defaultValueOverride,
                                                                                   allowsDuplicateValues,
                                                                                   orderedValues,
                                                                                   sortOrder,
                                                                                   minimumLength,
                                                                                   length,
                                                                                   significantDigits,
                                                                                   isNullable,
                                                                                   nativeJavaClass,
                                                                                   aliases,
                                                                                   additionalProperties,
                                                                                   databaseTable.getAnchorGUID(),
                                                                                   attributeTypeName,
                                                                                   attributeTypeId,
                                                                                   extendedProperties,
                                                                                   repositoryHelper,
                                                                                   serviceName,
                                                                                   serverName);

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(qualifiedName + ":ColumnType",
                                                                    SchemaElementMapper.RELATIONAL_COLUMN_TYPE_TYPE_NAME,
                                                                    SchemaElementMapper.RELATIONAL_COLUMN_TYPE_TYPE_GUID,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        schemaTypeBuilder.setDerivedProperties(formula, queries);
        schemaTypeBuilder.setDataType(dataType);
        schemaTypeBuilder.setDefaultValue(defaultValue);

        /*
         * Now create the table itself along with its schema type.  It also links the resulting table to the database schema type.
         * The returned value is the guid of the table.
         */
        return schemaTypeHandler.addSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    databaseTable.getAnchorGUID(),
                                                    databaseTableTypeGUID,
                                                    schemaAttributeBuilder,
                                                    schemaTypeBuilder,
                                                    methodName);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                                   String               externalSourceGUID,
                                                   String               externalSourceName,
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
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        SchemaAttribute databaseTable = schemaTypeHandler.getSchemaAttribute(userId,
                                                                             databaseTableGUID,
                                                                             SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                             null,
                                                                             null,
                                                                             methodName);

        String databaseTableTypeGUID = getDatabaseTableTypeGUID(databaseTable, methodName);

        return schemaTypeHandler.addSchemaAttributeFromTemplate(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                templateGUID,
                                                                SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                                databaseTableTypeGUID,
                                                                databaseTable.getAnchorGUID(),
                                                                qualifiedName,
                                                                displayName,
                                                                description,
                                                                methodName);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
    public void updateDatabaseColumn(String                             userId,
                                     String                             externalSourceGUID,
                                     String                             externalSourceName,
                                     String                             databaseColumnGUID,
                                     String                             qualifiedName,
                                     String                             displayName,
                                     String                             description,
                                     String                             dataType,
                                     String                             defaultValue,
                                     String                             formula,
                                     List<DerivedSchemaTypeQueryTarget> queries,
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
        final String elementGUIDParameterName   = "databaseColumnGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String  expectedTypeName = SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME;
        if (typeName != null)
        {
            expectedTypeName = typeName;
        }

        /*
         * Retrieve the current schema attribute for the database column. An exception is thrown if the guid is invalid, points to
         * an entity of the wrong type, or one with the CalculatedValue classification on its schema type.
         */
        SchemaAttribute   columnSchemaAttribute = schemaTypeHandler.getSchemaAttribute(userId,
                                                                                     databaseColumnGUID,
                                                                                     expectedTypeName,
                                                                                     null,
                                                                                     SchemaElementMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                     methodName);

        if (columnSchemaAttribute != null)
        {
            columnSchemaAttribute.setQualifiedName(qualifiedName);
            columnSchemaAttribute.setDisplayName(displayName);
            columnSchemaAttribute.setDescription(description);
            columnSchemaAttribute.setDeprecated(isDeprecated);
            columnSchemaAttribute.setElementPosition(elementPosition);
            columnSchemaAttribute.setMinCardinality(minCardinality);
            columnSchemaAttribute.setMaxCardinality(maxCardinality);
            columnSchemaAttribute.setAllowsDuplicateValues(allowsDuplicateValues);
            columnSchemaAttribute.setOrderedValues(orderedValues);
            columnSchemaAttribute.setDefaultValueOverride(defaultValueOverride);
            columnSchemaAttribute.setSortOrder(sortOrder);
            columnSchemaAttribute.setMinimumLength(minimumLength);
            columnSchemaAttribute.setLength(length);
            columnSchemaAttribute.setSignificantDigits(significantDigits);
            columnSchemaAttribute.setNullable(isNullable);
            columnSchemaAttribute.setNativeJavaClass(nativeJavaClass);
            columnSchemaAttribute.setAliases(aliases);
            columnSchemaAttribute.setAdditionalProperties(additionalProperties);
            columnSchemaAttribute.setExtendedProperties(extendedProperties);

            SchemaType schemaType = columnSchemaAttribute.getAttributeType();
            if (schemaType != null)
            {
                schemaType.setFormula(formula);
                schemaType.setQueries(queries);

                Map<String, Object>  extendedPropertiesForType = new HashMap<>();

                extendedPropertiesForType.put(SchemaElementMapper.DATA_TYPE_PROPERTY_NAME, dataType);
                extendedPropertiesForType.put(SchemaElementMapper.DEFAULT_VALUE_PROPERTY_NAME, defaultValue);

                schemaType.setExtendedProperties(extendedPropertiesForType);
            }
            else
            {
                String metadataCollectionId   = "<Unknown>";
                String metadataCollectionName = "<Unknown>";

                if (columnSchemaAttribute.getType() != null)
                {
                    metadataCollectionId   = columnSchemaAttribute.getType().getElementMetadataCollectionId();
                    metadataCollectionName = columnSchemaAttribute.getType().getElementMetadataCollectionName();
                }
                throw new PropertyServerException(OCFErrorCode.UNKNOWN_SCHEMA_TYPE.getMessageDefinition(methodName,
                                                                                                        expectedTypeName,
                                                                                                        metadataCollectionId,
                                                                                                        metadataCollectionName),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            schemaTypeHandler.updateSchemaAttribute(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    databaseColumnGUID,
                                                    columnSchemaAttribute);
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        databaseColumnGUID,
                                                        expectedTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseColumn(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String databaseColumnGUID,
                                     String qualifiedName,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String elementGUIDParameterName    = "databaseColumnGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        schemaTypeHandler.removeSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                databaseColumnGUID,
                                                elementGUIDParameterName,
                                                SchemaElementMapper.RELATIONAL_COLUMN_TYPE_GUID,
                                                SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                qualifiedName,
                                                qualifiedNameParameterName,
                                                methodName);
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

        return schemaTypeHandler.findSchemaAttributes(userId,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                      SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                      null,
                                                      null,
                                                      searchString,
                                                      startFrom,
                                                      pageSize,
                                                      methodName);
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

        SchemaAttribute databaseTable = schemaTypeHandler.getSchemaAttribute(userId,
                                                                             databaseTableGUID,
                                                                             SchemaElementMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                             null,
                                                                             null,
                                                                             methodName);

        if (databaseTable != null)
        {
            if (databaseTable.getAttributeType() != null)
            {
                return schemaTypeHandler.getSchemaAttributesForComplexSchemaType(userId,
                                                                                 databaseTable.getAttributeType().getGUID(),
                                                                                 null,
                                                                                 null,
                                                                                 null,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 methodName);
            }
        }

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

        return schemaTypeHandler.getSchemaAttributesByName(userId,
                                                           SchemaElementMapper.RELATIONAL_COLUMN_TYPE_GUID,
                                                           SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                           name,
                                                           null,
                                                           null,
                                                           null,
                                                           startFrom,
                                                           pageSize,
                                                           methodName);
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

        return schemaTypeHandler.getSchemaAttribute(userId,
                                                    guid,
                                                    SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                    null,
                                                    null,
                                                    methodName);
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column and it can be used to uniquely identify the column.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                      String     externalSourceGUID,
                                      String     externalSourceName,
                                      String     databaseColumnGUID,
                                      String     name,
                                      KeyPattern keyPattern,
                                      String     methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     SchemaElementMapper.PRIMARY_KEY_NAME_PROPERTY_NAME,
                                                                                     name,
                                                                                     methodName);

        if (keyPattern != null)
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    SchemaElementMapper.PRIMARY_KEY_PATTERN_PROPERTY_NAME,
                                                                    keyPattern.getOrdinal(),
                                                                    keyPattern.getName(),
                                                                    keyPattern.getDescription(),
                                                                    methodName);
        }

        schemaTypeHandler.addSchemaAttributeClassification(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                           SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                           databaseColumnGUID,
                                                           parentElementGUIDParameterName,
                                                           SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                           properties,
                                                           methodName);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePrimaryKeyFromColumn(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String databaseColumnGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);

        schemaTypeHandler.removeSchemaAttributeClassification(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                              SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                              databaseColumnGUID,
                                                              parentElementGUIDParameterName,
                                                              SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                              methodName);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
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
                                          String externalSourceGUID,
                                          String externalSourceName,
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
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     SchemaElementMapper.FOREIGN_KEY_NAME_PROPERTY_NAME,
                                                                                     name,
                                                                                     methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  SchemaElementMapper.FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME,
                                                               confidence,
                                                               methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  SchemaElementMapper.FOREIGN_KEY_STEWARD_PROPERTY_NAME,
                                                                  steward,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  SchemaElementMapper.FOREIGN_KEY_SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);

        schemaTypeHandler.addSchemaAttributeRelationship(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         SchemaElementMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                         primaryKeyColumnGUID,
                                                         primaryElementGUIDParameterName,
                                                         SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                         foreignKeyColumnGUID,
                                                         properties,
                                                         methodName);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForeignKeyRelationship(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String primaryKeyColumnGUID,
                                             String foreignKeyColumnGUID,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);

        schemaTypeHandler.removeSchemaAttributeRelationship(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            SchemaElementMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                            SchemaElementMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                            primaryKeyColumnGUID,
                                                            primaryElementGUIDParameterName,
                                                            SchemaElementMapper.RELATIONAL_COLUMN_TYPE_NAME,
                                                            foreignKeyColumnGUID,
                                                            methodName);
    }
}
