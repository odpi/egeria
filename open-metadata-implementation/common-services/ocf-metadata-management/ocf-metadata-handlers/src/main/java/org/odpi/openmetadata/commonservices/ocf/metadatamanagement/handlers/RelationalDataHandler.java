/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RelationalDataHandler manages the assets, connections and schemas for relational data.
 */
public class RelationalDataHandler extends AttachmentHandlerBase
{
    private final String integratorGUIDParameterName = "integratorGUID";
    private final String integratorNameParameterName = "integratorName";

    private AssetHandler              assetHandler;
    private ConnectionHandler         connectionHandler;
    private ConnectorTypeHandler      connectorTypeHandler;
    private EndpointHandler           endpointHandler;
    private GlossaryTermHandler       glossaryTermHandler;
    private SchemaTypeHandler         schemaTypeHandler;


    /**
     * Construct the connection handler with information needed to work with Connection objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param assetHandler handler for managing assets
     * @param connectionHandler handler for managing connections
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public RelationalDataHandler(String                  serviceName,
                                 String                  serverName,
                                 InvalidParameterHandler invalidParameterHandler,
                                 RepositoryHandler       repositoryHandler,
                                 OMRSRepositoryHelper    repositoryHelper,
                                 AssetHandler            assetHandler,
                                 ConnectionHandler       connectionHandler,
                                 LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);

        /*
         * The asset handler and connection handler are supplied from caller since they have security verifiers
         * set inside them.  This other handlers are stateless and can be set up here.
         */
        this.assetHandler = assetHandler;
        this.connectionHandler = connectionHandler;

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

        this.schemaTypeHandler = new SchemaTypeHandler(serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper,
                                                       lastAttachmentHandler);

        this.glossaryTermHandler = new GlossaryTermHandler(serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper,
                                                       lastAttachmentHandler);
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
     * @param encoding the name of the encoding style used in the database
     * @param language the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database implementation
     * @param meanings the unique identifiers of the assigned meanings for this database
     * @param classifications the list of classifications associated with this database
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                 String               encoding,
                                 String               language,
                                 String               encodingDescription,
                                 String               databaseType,
                                 String               databaseVersion,
                                 String               databaseInstance,
                                 String               databaseImportedFrom,
                                 Map<String, String>  additionalProperties,
                                 Map<String, String>  vendorProperties,
                                 List<String>         meanings,
                                 List<Classification> classifications,
                                 String               typeName,
                                 Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                  = "createDatabase";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
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
     * @param encoding the name of the encoding style used in the database
     * @param language the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database implementation
     * @param meanings the unique identifiers of the assigned meanings for this database
     * @param classifications the list of classifications associated with this database
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                             String               owner,
                                             OwnerType            ownerType,
                                             List<String>         zoneMembership,
                                             Map<String, String>  origin,
                                             String               latestChange,
                                             Date                 createTime,
                                             Date                 modifiedTime,
                                             String               encoding,
                                             String               language,
                                             String               encodingDescription,
                                             String               databaseType,
                                             String               databaseVersion,
                                             String               databaseInstance,
                                             String               databaseImportedFrom,
                                             Map<String, String>  additionalProperties,
                                             Map<String, String>  vendorProperties,
                                             List<String>         meanings,
                                             List<Classification> classifications,
                                             String               typeName,
                                             Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                  = "createDatabaseFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
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
     * @param encoding the name of the encoding style used in the database
     * @param language the name of the natural language used for text strings within the database
     * @param encodingDescription the description of the encoding used in the database
     * @param databaseType a description of the database type
     * @param databaseVersion the version of the database - often this is related to the version of its schemas.
     * @param databaseInstance the name of this database instance - useful if the same schemas are deployed to multiple database instances
     * @param databaseImportedFrom the source (typically connection name) of the database information
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database implementation
     * @param meanings the unique identifiers of the assigned meanings for this database
     * @param classifications the list of classifications associated with this database
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                               String               encoding,
                               String               language,
                               String               encodingDescription,
                               String               databaseType,
                               String               databaseVersion,
                               String               databaseInstance,
                               String               databaseImportedFrom,
                               Map<String, String>  additionalProperties,
                               Map<String, String>  vendorProperties,
                               List<String>         meanings,
                               List<Classification> classifications,
                               String               typeName,
                               Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "updateDatabase";
        final String elementGUIDParameterName    = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabase(String userId,
                                String integratorGUID,
                                String integratorName,
                                String databaseGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName               = "publishDatabase";
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);


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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabase(String userId,
                                 String integratorGUID,
                                 String integratorName,
                                 String databaseGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName               = "withdrawDatabase";
        final String elementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);


    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabase(String userId,
                               String integratorGUID,
                               String integratorName,
                               String databaseGUID,
                               String qualifiedName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "removeDatabase";
        final String integratorGUIDParameterName = "integratorGUID";
        final String integratorNameParameterName = "integratorName";
        final String elementGUIDParameterName    = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName                = "findDatabases";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName        = "getDatabasesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getDatabasesByDaemon";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        return null;
    }


    /**
     * Retrieve the database metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Asset getDatabaseByGUID(String userId,
                                   String guid) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "getDatabaseByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        return null;
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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                       Map<String, String>  vendorProperties,
                                       List<String>         meanings,
                                       List<Classification> classifications,
                                       String               typeName,
                                       Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                     = "createDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
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
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database schema - null means use the default zones set for this service
     * @param origin the properties that characterize where this database schema is from
     * @param latestChange latest change string for the database schema
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchemaFromTemplate(String               userId,
                                                   String               integratorGUID,
                                                   String               integratorName,
                                                   String               templateGUID,
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
                                                   Map<String, String>  vendorProperties,
                                                   List<String>         meanings,
                                                   List<Classification> classifications,
                                                   String               typeName,
                                                   Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName                     = "createDatabaseSchemaFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param displayName the stored display name property for the database schema
     * @param description the stored description property associated with the database schema
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param zoneMembership governance zones for the database schema - null means use the default zones set for this service
     * @param origin the properties that characterize where this database schema is from
     * @param latestChange latest change string for the database schema
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseSchema(String               userId,
                                     String               integratorGUID,
                                     String               integratorName,
                                     String               databaseSchemaGUID,
                                     String               qualifiedName,
                                     String               displayName,
                                     String               description,
                                     String               owner,
                                     OwnerType            ownerType,
                                     List<String>         zoneMembership,
                                     Map<String, String>  origin,
                                     String               latestChange,
                                     Map<String, String>  additionalProperties,
                                     Map<String, String>  vendorProperties,
                                     List<String>         meanings,
                                     List<Classification> classifications,
                                     String               typeName,
                                     Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName               = "updateDatabaseSchema";
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabaseSchema(String userId,
                                      String integratorGUID,
                                      String integratorName,
                                      String databaseSchemaGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                  = "publishDatabaseSchema";
        final String elementGUIDParameterName    = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);


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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabaseSchema(String userId,
                                       String integratorGUID,
                                       String integratorName,
                                       String databaseSchemaGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName               = "withdrawDatabase";
        final String elementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);


    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseSchema(String userId,
                                     String integratorGUID,
                                     String integratorName,
                                     String databaseSchemaGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "removeDatabaseSchema";
        final String elementGUIDParameterName    = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                = "findDatabaseSchemas";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Return the list of schemas associated with a database.
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the database to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName                     = "getSchemasForDatabase";
        final String parentElementGUIDParameterName = "databaseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

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
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "getDatabaseSchemasByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the database schema metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Asset getDatabaseSchemaByGUID(String userId,
                                         String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName        = "getDatabaseSchemaByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        return null;
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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                      Map<String, String>  vendorProperties,
                                      List<String>         meanings,
                                      List<Classification> classifications,
                                      String               typeName,
                                      Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                     = "createDatabaseTable";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                                  boolean              isDeprecated,
                                                  List<String>         aliases,
                                                  Map<String, String>  additionalProperties,
                                                  Map<String, String>  vendorProperties,
                                                  List<String>         meanings,
                                                  List<Classification> classifications,
                                                  String               typeName,
                                                  Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                     = "createDatabaseTableFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                    Map<String, String>  vendorProperties,
                                    List<String>         meanings,
                                    List<Classification> classifications,
                                    String               typeName,
                                    Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                 = "updateDatabaseTable";
        final String elementGUIDParameterName   = "databaseTableGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseTable(String userId,
                                    String integratorGUID,
                                    String integratorName,
                                    String databaseTableGUID,
                                    String qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "removeDatabaseTable";
        final String elementGUIDParameterName    = "databaseTableGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                = "findDatabaseTables";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the list of database tables associated with a database schema.
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                     = "getTablesForDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

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
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName        = "getDatabaseTablesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the database table metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseTableByGUID(String userId,
                                                  String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName        = "getDatabaseTableByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                     Map<String, String>  vendorProperties,
                                     List<String>         meanings,
                                     List<Classification> classifications,
                                     String               typeName,
                                     Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "createDatabaseView";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param isDeprecated is this table deprecated?
     * @param aliases a list of alternative names for the attribute
     * @param expression the code that generates the value for this view.
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                                 boolean              isDeprecated,
                                                 List<String>         aliases,
                                                 String               expression,
                                                 Map<String, String>  additionalProperties,
                                                 Map<String, String>  vendorProperties,
                                                 List<String>         meanings,
                                                 List<Classification> classifications,
                                                 String               typeName,
                                                 Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                     = "createDatabaseViewFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                   Map<String, String>  vendorProperties,
                                   List<String>         meanings,
                                   List<Classification> classifications,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                 = "updateDatabaseView";
        final String elementGUIDParameterName   = "databaseViewGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseView(String userId,
                                   String integratorGUID,
                                   String integratorName,
                                   String databaseViewGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                  = "removeDatabaseView";
        final String elementGUIDParameterName    = "databaseViewGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                = "findDatabaseViews";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the list of database views associated with a database schema.
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                     = "getViewsForDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


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
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName        = "getDatabaseViewsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the database view metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseViewByGUID(String userId,
                                                 String guid) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName        = "getDatabaseViewByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumn(String               userId,
                                       String               integratorGUID,
                                       String               integratorName,
                                       String               databaseTableGUID,
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
                                       Map<String, String>  additionalProperties,
                                       Map<String, String>  vendorProperties,
                                       List<String>         meanings,
                                       List<Classification> classifications,
                                       String               typeName,
                                       Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                     = "createDatabaseColumn";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                                   Map<String, String>  additionalProperties,
                                                   Map<String, String>  vendorProperties,
                                                   List<String>         meanings,
                                                   List<Classification> classifications,
                                                   String               typeName,
                                                   Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName                     = "createDatabaseColumnFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database derived column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table where this column is located
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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseDerivedColumn(String               userId,
                                              String               integratorGUID,
                                              String               integratorName,
                                              String               databaseTableGUID,
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
                                              Map<String, String>  vendorProperties,
                                              List<String>         meanings,
                                              List<Classification> classifications,
                                              String               typeName,
                                              Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName                     = "createDatabaseDerivedColumn";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
    }


    /**
     * Create a new metadata element to represent a database derived column using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseDerivedColumnFromTemplate(String               userId,
                                                          String               integratorGUID,
                                                          String               integratorName,
                                                          String               templateGUID,
                                                          String               databaseTableGUID,
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
                                                          Map<String, String>  vendorProperties,
                                                          List<String>         meanings,
                                                          List<Classification> classifications,
                                                          String               typeName,
                                                          Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName                     = "createDatabaseDerivedColumnFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String qualifiedNameParameterName     = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        return null;
    }


    /**
     * Create a link to the data value that is used to derive a database column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the derived column
     * @param query properties for the query
     * @param queryTargetGUID the identity of the query target (a type of schema attribute - typically a database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addQueryTargetToDerivedColumn(String userId,
                                              String integratorGUID,
                                              String integratorName,
                                              String databaseColumnGUID,
                                              String query,
                                              String queryTargetGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                     = "addQueryTargetToDerivedColumn";
        final String parentElementGUIDParameterName = "databaseColumnGUID";
        final String queryParameterName             = "query";
        final String queryTargetGUIDParameterName   = "queryTargetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(query, queryParameterName, methodName);
        invalidParameterHandler.validateGUID(queryTargetGUID, queryTargetGUIDParameterName, methodName);


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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseColumn(String               userId,
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
                                     Map<String, String>  additionalProperties,
                                     Map<String, String>  vendorProperties,
                                     List<String>         meanings,
                                     List<Classification> classifications,
                                     String               typeName,
                                     Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                 = "updateDatabaseColumn";
        final String elementGUIDParameterName   = "databaseColumnGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

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
     * @param vendorProperties properties that are specific to the database schema implementation
     * @param meanings the unique identifiers of the assigned meanings for this database schema
     * @param classifications the list of classifications associated with this database schema
     * @param typeName name of the type that is a subtype of DeployedDatabaseSchema - or null to create standard type
     * @param extendedProperties properties from any subtype
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
                                            Map<String, String>  vendorProperties,
                                            List<String>         meanings,
                                            List<Classification> classifications,
                                            String               typeName,
                                            Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                 = "updateDatabaseDerivedColumn";
        final String elementGUIDParameterName   = "databaseColumnGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseColumn(String userId,
                                     String integratorGUID,
                                     String integratorName,
                                     String databaseColumnGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "removeDatabaseColumn";
        final String elementGUIDParameterName    = "databaseColumnGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);


    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                = "findDatabaseColumns";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the list of column for a database table (or view)
     *
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the database table of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                     = "getColumnsForDatabaseTable";
        final String parentElementGUIDParameterName = "databaseTableGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

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
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName        = "getDatabaseColumnsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        return null;
    }


    /**
     * Retrieve the database column metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttribute getDatabaseColumnByGUID(String userId,
                                                   String guid) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName        = "getDatabaseColumnByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

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
                                      KeyPattern keyPattern) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                     = "setPrimaryKeyOnColumn";
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);


    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePrimaryKeyFromColumn(String                       userId,
                                           String                       integratorGUID,
                                           String                       integratorName,
                                           String                       databaseColumnGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                     = "removePrimaryKeyFromColumn";
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);


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
                                          String source) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName                      = "addForeignKeyRelationship";
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);


    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForeignKeyRelationship(String                       userId,
                                             String                       integratorGUID,
                                             String                       integratorName,
                                             String                       primaryKeyColumnGUID,
                                             String                       foreignKeyColumnGUID) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                      = "removeForeignKeyRelationship";
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integratorGUID, integratorGUIDParameterName, methodName);
        invalidParameterHandler.validateName(integratorName, integratorNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);


    }
}
