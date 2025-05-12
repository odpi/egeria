/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.*;

import java.util.List;

/**
 * DatabaseManagerInterface defines the client side interface for the Data Manager OMAS that is
 * relevant for relational database assets.   It provides the ability to
 * define and maintain the metadata about a database and the schemas (tables and columns) it contains.
 */
public interface DatabaseManagerInterface
{

    /*
     * The database is the top level asset in a database server
     */


    /**
     * Create a new metadata element to represent a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabase(String             userId,
                          String             databaseManagerGUID,
                          String             databaseManagerName,
                          DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseFromTemplate(String             userId,
                                      String             databaseManagerGUID,
                                      String             databaseManagerName,
                                      String             templateGUID,
                                      TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDatabase(String             userId,
                        String             databaseManagerGUID,
                        String             databaseManagerName,
                        String             databaseGUID,
                        boolean            isMergeUpdate,
                        DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishDatabase(String userId,
                         String databaseGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException;


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawDatabase(String userId,
                          String databaseGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param cascadedDelete can the operation remove nested schemas. tables and columns (default false)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDatabase(String  userId,
                        String  databaseManagerGUID,
                        String  databaseManagerName,
                        String  databaseGUID,
                        boolean cascadedDelete) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException;


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
    List<DatabaseElement>   findDatabases(String userId,
                                          String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


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
    List<DatabaseElement>   getDatabasesByName(String userId,
                                               String name,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the database manager (DBMS)
     * @param databaseManagerName unique name of software server capability representing the database manager (DBMS)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DatabaseElement>   getDatabasesForDatabaseManager(String userId,
                                                           String databaseManagerGUID,
                                                           String databaseManagerName,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


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
    DatabaseElement getDatabaseByGUID(String userId,
                                      String guid) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /*
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseSchema(String                   userId,
                                String                   databaseManagerGUID,
                                String                   databaseManagerName,
                                String                   databaseGUID,
                                DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseSchemaFromTemplate(String             userId,
                                            String             databaseManagerGUID,
                                            String             databaseManagerName,
                                            String             templateGUID,
                                            String             databaseGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDatabaseSchema(String                   userId,
                              String                   databaseManagerGUID,
                              String                   databaseManagerName,
                              String                   databaseSchemaGUID,
                              boolean                  isMergeUpdate,
                              DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishDatabaseSchema(String userId,
                               String databaseSchemaGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawDatabaseSchema(String userId,
                                String databaseSchemaGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param cascadedDelete can the operation remove nested schemas. tables and columns (default false)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDatabaseSchema(String  userId,
                              String  databaseManagerGUID,
                              String  databaseManagerName,
                              String  databaseSchemaGUID,
                              boolean cascadedDelete) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


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
    List<DatabaseSchemaElement>   findDatabaseSchemas(String userId,
                                                      String searchString,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


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
    List<DatabaseSchemaElement>   getSchemasForDatabase(String userId,
                                                        String databaseGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


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
    List<DatabaseSchemaElement>   getDatabaseSchemasByName(String userId,
                                                           String name,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


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
    DatabaseSchemaElement getDatabaseSchemaByGUID(String userId,
                                                  String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;

    /*
     * A database schema may contain multiple database tables and database views.
     */


    /**
     * Create a database top-level schema type used to attach tables and views to the database/database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID guid of the software server capability entity that represented the external source - null for local
     * @param databaseManagerName name of the software server capability entity that represented the external source - null for local
     * @param qualifiedName qualified name ofr the schema type - suggest "SchemaOf:" + asset's qualified name
     * @return unique identifier of the database schema type
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String createDatabaseSchemaType(String userId,
                                    String databaseManagerGUID,
                                    String databaseManagerName,
                                    String qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;



    /**
     * Link the schema type and asset.  This is called from outside AssetHandler.  The databaseAssetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param databaseManagerGUID guid of the software server capability entity that represented the external source - null for local
     * @param databaseManagerName name of the software server capability entity that represented the external source - null for local
     * @param databaseAssetGUID unique identifier of the asset to connect the schema to
     * @param schemaTypeGUID identifier for schema Type object
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    void attachSchemaTypeToDatabaseAsset(String  userId,
                                         String  databaseManagerGUID,
                                         String  databaseManagerName,
                                         String  databaseAssetGUID,
                                         String  schemaTypeGUID) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseAssetGUID unique identifier of the database or schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseTable(String                  userId,
                               String                  databaseManagerGUID,
                               String                  databaseManagerName,
                               String                  databaseAssetGUID,
                               DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseTableFromTemplate(String             userId,
                                           String             databaseManagerGUID,
                                           String             databaseManagerName,
                                           String             templateGUID,
                                           String             databaseAssetGUID,
                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaTypeGUID unique identifier of the database or database schema where the database table is located
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseTableForSchemaType(String                  userId,
                                            String                  databaseManagerGUID,
                                            String                  databaseManagerName,
                                            String                  databaseSchemaTypeGUID,
                                            DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseTableProperties new properties for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDatabaseTable(String                  userId,
                             String                  databaseManagerGUID,
                             String                  databaseManagerName,
                             String                  databaseTableGUID,
                             boolean                 isMergeUpdate,
                             DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param cascadedDelete can the operation remove nested schemas. tables and columns (default false)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDatabaseTable(String  userId,
                             String  databaseManagerGUID,
                             String  databaseManagerName,
                             String  databaseTableGUID,
                             boolean cascadedDelete) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


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
    List<DatabaseTableElement>   findDatabaseTables(String userId,
                                                    String searchString,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


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
    @Deprecated
    List<DatabaseTableElement>    getTablesForDatabaseSchema(String userId,
                                                             String databaseSchemaGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Retrieve the list of database tables associated with a database or database schema.
     *
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DatabaseTableElement>    getTablesForDatabaseAsset(String userId,
                                                            String databaseAssetGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


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
    List<DatabaseTableElement>   getDatabaseTablesByName(String userId,
                                                         String name,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


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
    DatabaseTableElement getDatabaseTableByGUID(String userId,
                                                String guid) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseView(String                 userId,
                              String                 databaseManagerGUID,
                              String                 databaseManagerName,
                              String                 databaseAssetGUID,
                              DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;



    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseViewFromTemplate(String             userId,
                                          String             databaseManagerGUID,
                                          String             databaseManagerName,
                                          String             templateGUID,
                                          String             databaseAssetGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;



    /**
     * Create a new metadata element to represent a database view.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaTypeGUID unique identifier of the schema type where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseViewForSchemaType(String                 userId,
                                           String                 databaseManagerGUID,
                                           String                 databaseManagerName,
                                           String                 databaseSchemaTypeGUID,
                                           DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the database view to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseViewProperties properties for the new database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDatabaseView(String                 userId,
                            String                 databaseManagerGUID,
                            String                 databaseManagerName,
                            String                 databaseViewGUID,
                            boolean                isMergeUpdate,
                            DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDatabaseView(String  userId,
                            String  databaseManagerGUID,
                            String  databaseManagerName,
                            String  databaseViewGUID,
                            boolean cascadedDelete) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


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
    List<DatabaseViewElement>   findDatabaseViews(String userId,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


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
    @Deprecated
    List<DatabaseViewElement>    getViewsForDatabaseSchema(String userId,
                                                           String databaseSchemaGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Retrieve the list of database views associated with a database or database schema.
     *
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DatabaseViewElement>    getViewsForDatabaseAsset(String userId,
                                                          String databaseAssetGUID,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


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
    List<DatabaseViewElement>   getDatabaseViewsByName(String userId,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


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
    DatabaseViewElement getDatabaseViewByGUID(String userId,
                                              String guid) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;

    /*
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the new database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseColumn(String                   userId,
                                String                   databaseManagerGUID,
                                String                   databaseManagerName,
                                String                   databaseTableGUID,
                                DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;



    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDatabaseColumnFromTemplate(String             userId,
                                            String             databaseManagerGUID,
                                            String             databaseManagerName,
                                            String             templateGUID,
                                            String             databaseTableGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Update the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDatabaseColumn(String                   userId,
                              String                   databaseManagerGUID,
                              String                   databaseManagerName,
                              String                   databaseColumnGUID,
                              boolean                  isMergeUpdate,
                              DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDatabaseColumn(String userId,
                              String databaseManagerGUID,
                              String databaseManagerName,
                              String databaseColumnGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


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
    List<DatabaseColumnElement>   findDatabaseColumns(String userId,
                                                      String searchString,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


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
    List<DatabaseColumnElement>    getColumnsForDatabaseTable(String userId,
                                                              String databaseTableGUID,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


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
    List<DatabaseColumnElement>   getDatabaseColumnsByName(String userId,
                                                           String name,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


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
    DatabaseColumnElement getDatabaseColumnByGUID(String userId,
                                                  String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /*
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column, and it can be used to uniquely identify the column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setPrimaryKeyOnColumn(String                       userId,
                               String                       databaseManagerGUID,
                               String                       databaseManagerName,
                               String                       databaseColumnGUID,
                               DatabasePrimaryKeyProperties databasePrimaryKeyProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removePrimaryKeyFromColumn(String                       userId,
                                    String                       databaseManagerGUID,
                                    String                       databaseManagerName,
                                    String                       databaseColumnGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addForeignKeyRelationship(String                       userId,
                                   String                       databaseManagerGUID,
                                   String                       databaseManagerName,
                                   String                       primaryKeyColumnGUID,
                                   String                       foreignKeyColumnGUID,
                                   DatabaseForeignKeyProperties databaseForeignKeyProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the caller
     * @param databaseManagerName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeForeignKeyRelationship(String                       userId,
                                      String                       databaseManagerGUID,
                                      String                       databaseManagerName,
                                      String                       primaryKeyColumnGUID,
                                      String                       foreignKeyColumnGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;
}
