/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.DatabaseManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import org.springframework.web.bind.annotation.*;


/**
 * DatabaseManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for relational databases.  It matches the DatabaseManagerClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Data Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class DatabaseManagerResource
{
    private final DatabaseManagerRESTServices restAPI = new DatabaseManagerRESTServices();

    /**
     * Default constructor
     */
    public DatabaseManagerResource()
    {
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases")

    public GUIDResponse createDatabase(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @PathVariable String             databaseManagerGUID,
                                       @PathVariable String             databaseManagerName,
                                       @RequestBody  DatabaseProperties databaseProperties)
    {
        return restAPI.createDatabase(serverName, userId, databaseManagerGUID, databaseManagerName, databaseProperties);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/from-template/{templateGUID}")

    public GUIDResponse createDatabaseFromTemplate(@PathVariable String             serverName,
                                                   @PathVariable String             userId,
                                                   @PathVariable String             databaseManagerGUID,
                                                   @PathVariable String             databaseManagerName,
                                                   @PathVariable String             templateGUID,
                                                   @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseFromTemplate(serverName, userId, databaseManagerGUID, databaseManagerName, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/{databaseGUID}")

    public VoidResponse updateDatabase(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @PathVariable String             databaseManagerGUID,
                                       @PathVariable String             databaseManagerName,
                                       @PathVariable String             databaseGUID,
                                       @RequestBody  DatabaseProperties databaseProperties)
    {
        return restAPI.updateDatabase(serverName, userId, databaseManagerGUID, databaseManagerName, databaseGUID, databaseProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}/publish")

    public VoidResponse publishDatabase(@PathVariable                  String          serverName,
                                        @PathVariable                  String          userId,
                                        @PathVariable                  String          databaseGUID,
                                        @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishDatabase(serverName, userId, databaseGUID, nullRequestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}/withdraw")

    public VoidResponse withdrawDatabase(@PathVariable                  String          serverName,
                                         @PathVariable                  String          userId,
                                         @PathVariable                  String          databaseGUID,
                                         @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, databaseGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/{databaseGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabase(@PathVariable                  String          serverName,
                                       @PathVariable                  String          userId,
                                       @PathVariable                  String          databaseManagerGUID,
                                       @PathVariable                  String          databaseManagerName,
                                       @PathVariable                  String          databaseGUID,
                                       @PathVariable                  String          qualifiedName,
                                       @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabase(serverName, userId, databaseManagerGUID, databaseManagerName, databaseGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/by-search-string")

    public DatabasesResponse findDatabases(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  SearchStringRequestBody requestBody,
                                           @RequestParam int                     startFrom,
                                           @RequestParam int                     pageSize)
    {
        return restAPI.findDatabases(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/by-name")

    public DatabasesResponse   getDatabasesByName(@PathVariable String          serverName,
                                                  @PathVariable String          userId,
                                                  @RequestBody  NameRequestBody requestBody,
                                                  @RequestParam int             startFrom,
                                                  @RequestParam int             pageSize)
    {
        return restAPI.getDatabasesByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of databases created by this database manager.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases")

    public DatabasesResponse   getDatabasesForDatabaseManager(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String databaseManagerGUID,
                                                              @PathVariable String databaseManagerName,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getDatabasesForDatabaseManager(serverName, userId, databaseManagerGUID, databaseManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the database metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/{guid}")

    public DatabaseResponse getDatabaseByGUID(@PathVariable String serverName,
                                              @PathVariable String userId,
                                              @PathVariable String guid)
    {
        return restAPI.getDatabaseByGUID(serverName, userId, guid);
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/{databaseGUID}/schemas")

    public GUIDResponse createDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseManagerGUID,
                                             @PathVariable String                   databaseManagerName,
                                             @PathVariable String                   databaseGUID,
                                             @RequestBody  DatabaseSchemaProperties databaseSchemaProperties)
    {
        return restAPI.createDatabaseSchema(serverName, userId, databaseManagerGUID, databaseManagerName, databaseGUID, databaseSchemaProperties);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/{databaseGUID}/schemas/from-template/{templateGUID}")

    public GUIDResponse createDatabaseSchemaFromTemplate(@PathVariable String             serverName,
                                                         @PathVariable String             userId,
                                                         @PathVariable String             databaseManagerGUID,
                                                         @PathVariable String             databaseManagerName,
                                                         @PathVariable String             templateGUID,
                                                         @PathVariable String             databaseGUID,
                                                         @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseSchemaFromTemplate(serverName, userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/schemas/{databaseSchemaGUID}")

    public VoidResponse updateDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseManagerGUID,
                                             @PathVariable String                   databaseManagerName,
                                             @PathVariable String                   databaseSchemaGUID,
                                             @RequestBody  DatabaseSchemaProperties databaseSchemaProperties)
    {
        return restAPI.updateDatabaseSchema(serverName, userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, databaseSchemaProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}/publish")

    public VoidResponse publishDatabaseSchema(@PathVariable                  String          serverName,
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          databaseSchemaGUID,
                                              @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishDatabaseSchema(serverName, userId, databaseSchemaGUID, nullRequestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}/withdraw")

    public VoidResponse withdrawDatabaseSchema(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          databaseSchemaGUID,
                                               @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, databaseSchemaGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/schemas/{databaseSchemaGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseSchema(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          databaseManagerGUID,
                                             @PathVariable                  String          databaseManagerName,
                                             @PathVariable                  String          databaseSchemaGUID,
                                             @PathVariable                  String          qualifiedName,
                                             @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseSchema(serverName, userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/by-search-string")

    public DatabaseSchemasResponse   findDatabaseSchemas(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @RequestBody  SearchStringRequestBody requestBody,
                                                         @RequestParam int                     startFrom,
                                                         @RequestParam int                     pageSize)
    {
        return restAPI.findDatabaseSchemas(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the database to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested database or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/{databaseGUID}/schemas")

    public DatabaseSchemasResponse   getSchemasForDatabase(@PathVariable String serverName,
                                                           @PathVariable String userId,
                                                           @PathVariable String databaseGUID,
                                                           @RequestParam int    startFrom,
                                                           @RequestParam int    pageSize)
    {
        return restAPI.getSchemasForDatabase(serverName, userId, databaseGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database schema metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/by-name")

    public DatabaseSchemasResponse   getDatabaseSchemasByName(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @RequestBody  NameRequestBody requestBody,
                                                              @RequestParam int             startFrom,
                                                              @RequestParam int             pageSize)
    {
        return restAPI.getDatabaseSchemasByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the database schema metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/{guid}")

    public DatabaseSchemaResponse getDatabaseSchemaByGUID(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid)
    {
        return restAPI.getDatabaseSchemaByGUID(serverName, userId, guid);
    }


    /* ==========================================================================
     * A database schema may contain multiple database tables and database views.
     */


    /**
     * Create a database top-level schema type used to attach tables and views to the database/database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID guid of the software server capability entity that represented the external source - null for local
     * @param databaseManagerName name of the software server capability entity that represented the external source - null for local
     * @param requestBody qualified name of the schema type - suggest "SchemaOf:" + asset's qualified name
     * @return unique identifier of the database schema type or
     *  InvalidParameterException the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException problem accessing the property server
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/schema-type")

    public GUIDResponse createDatabaseSchemaType(@PathVariable String          serverName,
                                                 @PathVariable String          userId,
                                                 @PathVariable String          databaseManagerGUID,
                                                 @PathVariable String          databaseManagerName,
                                                 @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.createDatabaseSchemaType(serverName, userId, databaseManagerGUID, databaseManagerName, requestBody);
    }


    /**
     * Link the schema type and asset.  This is called from outside the AssetHandler.  The databaseAssetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID guid of the software server capability entity that represented the external source - null for local
     * @param databaseManagerName name of the software server capability entity that represented the external source - null for local
     * @param databaseAssetGUID unique identifier of the asset to connect the schema to
     * @param schemaTypeGUID identifier for schema Type object
     * @param requestBody null request body
     * @return void or
     *  InvalidParameterException the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException problem accessing the property server
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/{databaseAssetGUID}/schema-type/{schemaTypeGUID}")

    public  VoidResponse attachSchemaTypeToDatabaseAsset(@PathVariable String          serverName,
                                                         @PathVariable String          userId,
                                                         @PathVariable String          databaseManagerGUID,
                                                         @PathVariable String          databaseManagerName,
                                                         @PathVariable String          databaseAssetGUID,
                                                         @PathVariable String          schemaTypeGUID,
                                                         @RequestBody(required = false)
                                                                       NullRequestBody requestBody)
    {
        return restAPI.attachSchemaTypeToDatabaseAsset(serverName, userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/{databaseAssetGUID}/tables")

    public GUIDResponse createDatabaseTable(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  databaseManagerGUID,
                                            @PathVariable String                  databaseManagerName,
                                            @PathVariable String                  databaseAssetGUID,
                                            @RequestBody  DatabaseTableProperties databaseTableProperties)
    {
        return restAPI.createDatabaseTable(serverName, userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, databaseTableProperties);
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/{databaseAssetGUID}/tables/from-template/{templateGUID}")

    public GUIDResponse createDatabaseTableFromTemplate(@PathVariable String             serverName,
                                                        @PathVariable String             userId,
                                                        @PathVariable String             databaseManagerGUID,
                                                        @PathVariable String             databaseManagerName,
                                                        @PathVariable String             templateGUID,
                                                        @PathVariable String             databaseAssetGUID,
                                                        @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseTableFromTemplate(serverName, userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseAssetGUID, templateProperties);
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaTypeGUID unique identifier of the database or database schema where the database table is located
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/schema-type/{databaseSchemaTypeGUID}/tables")

    public GUIDResponse createDatabaseTableForSchemaType(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @PathVariable String                  databaseManagerGUID,
                                                         @PathVariable String                  databaseManagerName,
                                                         @PathVariable String                  databaseSchemaTypeGUID,
                                                         @RequestBody  DatabaseTableProperties databaseTableProperties)
    {
        return restAPI.createDatabaseTableForSchemaType(serverName, userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeGUID, databaseTableProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseTableGUID unique identifier of the database table to update
     * @param databaseTableProperties new properties for the database table
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/{databaseTableGUID}")

    public VoidResponse updateDatabaseTable(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  databaseManagerGUID,
                                            @PathVariable String                  databaseManagerName,
                                            @PathVariable String                  databaseTableGUID,
                                            @RequestBody  DatabaseTableProperties databaseTableProperties)
    {
        return restAPI.updateDatabaseTable(serverName, userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, databaseTableProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/{databaseTableGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseTable(@PathVariable                  String          serverName,
                                            @PathVariable                  String          userId,
                                            @PathVariable                  String          databaseManagerGUID,
                                            @PathVariable                  String          databaseManagerName,
                                            @PathVariable                  String          databaseTableGUID,
                                            @PathVariable                  String          qualifiedName,
                                            @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseTable(serverName, userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/by-search-string")

    public DatabaseTablesResponse   findDatabaseTables(@PathVariable String                  serverName,
                                                       @PathVariable String                  userId,
                                                       @RequestBody  SearchStringRequestBody requestBody,
                                                       @RequestParam int                     startFrom,
                                                       @RequestParam int                     pageSize)
    {
        return restAPI.findDatabaseTables(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database tables associated with a database or database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/assets/{databaseAssetGUID}/tables")

    public DatabaseTablesResponse getTablesForDatabaseAsset(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String databaseAssetGUID,
                                                            @RequestParam int    startFrom,
                                                            @RequestParam int    pageSize)
    {
        return restAPI.getTablesForDatabaseAsset(serverName, userId, databaseAssetGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database table metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/by-name")

    public DatabaseTablesResponse   getDatabaseTablesByName(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @RequestBody  NameRequestBody requestBody,
                                                            @RequestParam int             startFrom,
                                                            @RequestParam int             pageSize)
    {
        return restAPI.getDatabaseTablesByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the database table metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/tables/{guid}")

    public DatabaseTableResponse getDatabaseTableByGUID(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String guid)
    {
        return restAPI.getDatabaseTableByGUID(serverName, userId, guid);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/{databaseAssetGUID}/tables/views")

    public GUIDResponse createDatabaseView(@PathVariable String                 serverName,
                                           @PathVariable String                 userId,
                                           @PathVariable String                 databaseManagerGUID,
                                           @PathVariable String                 databaseManagerName,
                                           @PathVariable String                 databaseAssetGUID,
                                           @RequestBody  DatabaseViewProperties databaseViewProperties)
    {
        return restAPI.createDatabaseView(serverName, userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, databaseViewProperties);
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/{databaseAssetGUID}/tables/views/from-template/{templateGUID}")

    public GUIDResponse createDatabaseViewFromTemplate(@PathVariable String             serverName,
                                                       @PathVariable String             userId,
                                                       @PathVariable String             databaseManagerGUID,
                                                       @PathVariable String             databaseManagerName,
                                                       @PathVariable String             templateGUID,
                                                       @PathVariable String             databaseAssetGUID,
                                                       @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseViewFromTemplate(serverName, userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseAssetGUID, templateProperties);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaTypeGUID unique identifier of the schema type where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/assets/schema-type/{databaseSchemaTypeGUID}/tables/views")

    public GUIDResponse createDatabaseViewForSchemaType(@PathVariable String                 serverName,
                                                        @PathVariable String                 userId,
                                                        @PathVariable String                 databaseManagerGUID,
                                                        @PathVariable String                 databaseManagerName,
                                                        @PathVariable String                 databaseSchemaTypeGUID,
                                                        @RequestBody  DatabaseViewProperties databaseViewProperties)
    {
        return restAPI.createDatabaseViewForSchemaType(serverName, userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeGUID, databaseViewProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseViewGUID unique identifier of the database view to update
     * @param databaseViewProperties properties for the new database view
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/views/{databaseViewGUID}")

    public VoidResponse updateDatabaseView(@PathVariable String                 serverName,
                                           @PathVariable String                 userId,
                                           @PathVariable String                 databaseManagerGUID,
                                           @PathVariable String                 databaseManagerName,
                                           @PathVariable String                 databaseViewGUID,
                                           @RequestBody  DatabaseViewProperties databaseViewProperties)
    {
        return restAPI.updateDatabaseView(serverName, userId, databaseManagerGUID, databaseManagerName, databaseViewGUID, databaseViewProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/views/{databaseViewGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseView(@PathVariable                  String          serverName,
                                           @PathVariable                  String          userId,
                                           @PathVariable                  String          databaseManagerGUID,
                                           @PathVariable                  String          databaseManagerName,
                                           @PathVariable                  String          databaseViewGUID,
                                           @PathVariable                  String          qualifiedName,
                                           @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseView(serverName, userId, databaseManagerGUID, databaseManagerName, databaseViewGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views/by-search-string")

    public DatabaseViewsResponse   findDatabaseViews(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestBody  SearchStringRequestBody requestBody,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize)
    {
        return restAPI.findDatabaseViews(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database views associated with a database or database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the database or database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/assets/{databaseAssetGUID}/tables/views")

    public DatabaseViewsResponse getViewsForDatabaseAsset(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String databaseAssetGUID,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getViewsForDatabaseAsset(serverName, userId, databaseAssetGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database view metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views/by-name")

    public DatabaseViewsResponse   getDatabaseViewsByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestBody  NameRequestBody requestBody,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize)
    {
        return restAPI.getDatabaseViewsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the database view metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/tables/views/{guid}")

    public DatabaseViewResponse getDatabaseViewByGUID(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String guid)
    {
        return restAPI.getDatabaseViewByGUID(serverName, userId, guid);
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/{databaseTableGUID}/columns")

    public GUIDResponse createDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseManagerGUID,
                                             @PathVariable String                   databaseManagerName,
                                             @PathVariable String                   databaseTableGUID,
                                             @RequestBody  DatabaseColumnProperties databaseColumnProperties)
    {
        return restAPI.createDatabaseColumn(serverName, userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, databaseColumnProperties);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/{databaseTableGUID}/columns/from-template/{templateGUID}")

    public GUIDResponse createDatabaseColumnFromTemplate(@PathVariable String             serverName,
                                                         @PathVariable String             userId,
                                                         @PathVariable String             databaseManagerGUID,
                                                         @PathVariable String             databaseManagerName,
                                                         @PathVariable String             templateGUID,
                                                         @PathVariable String             databaseTableGUID,
                                                         @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseColumnFromTemplate(serverName, userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseTableGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{databaseColumnGUID}")

    public VoidResponse updateDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseManagerGUID,
                                             @PathVariable String                   databaseManagerName,
                                             @PathVariable String                   databaseColumnGUID,
                                             @RequestBody  DatabaseColumnProperties databaseColumnProperties)
    {
        return restAPI.updateDatabaseColumn(serverName, userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, databaseColumnProperties);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{databaseColumnGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseColumn(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          databaseManagerGUID,
                                             @PathVariable                  String          databaseManagerName,
                                             @PathVariable                  String          databaseColumnGUID,
                                             @PathVariable                  String          qualifiedName,
                                             @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseColumn(serverName, userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/by-search-string")

    public DatabaseColumnsResponse   findDatabaseColumns(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @RequestBody  SearchStringRequestBody requestBody,
                                                         @RequestParam int                     startFrom,
                                                         @RequestParam int                     pageSize)
    {
        return restAPI.findDatabaseColumns(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of column for a database table (or view)
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the database table of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/tables/{databaseTableGUID}/columns")

    public DatabaseColumnsResponse    getColumnsForDatabaseTable(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @PathVariable String databaseTableGUID,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getColumnsForDatabaseTable(serverName, userId, databaseTableGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database column metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/by-name")

    public DatabaseColumnsResponse   getDatabaseColumnsByName(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @RequestBody  NameRequestBody requestBody,
                                                              @RequestParam int             startFrom,
                                                              @RequestParam int             pageSize)
    {
        return restAPI.getDatabaseColumnsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the database column metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/tables/columns/{guid}")

    public DatabaseColumnResponse getDatabaseColumnByGUID(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid)
    {
        return restAPI.getDatabaseColumnByGUID(serverName, userId, guid);
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column and it can be used to uniquely identify the column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{databaseColumnGUID}/primary-key")

    public VoidResponse setPrimaryKeyOnColumn(@PathVariable String                       serverName,
                                              @PathVariable String                       userId,
                                              @PathVariable String                       databaseManagerGUID,
                                              @PathVariable String                       databaseManagerName,
                                              @PathVariable String                       databaseColumnGUID,
                                              @RequestBody  DatabasePrimaryKeyProperties databasePrimaryKeyProperties)
    {
        return restAPI.setPrimaryKeyOnColumn(serverName, userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, databasePrimaryKeyProperties);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{databaseColumnGUID}/primary-key/delete")

    public VoidResponse removePrimaryKeyFromColumn(@PathVariable                  String          serverName,
                                                   @PathVariable                  String          userId,
                                                   @PathVariable                  String          databaseManagerGUID,
                                                   @PathVariable                  String          databaseManagerName,
                                                   @PathVariable                  String          databaseColumnGUID,
                                                   @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removePrimaryKeyFromColumn(serverName, userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, nullRequestBody);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}")

    public VoidResponse addForeignKeyRelationship(@PathVariable String                       serverName,
                                                  @PathVariable String                       userId,
                                                  @PathVariable String                       databaseManagerGUID,
                                                  @PathVariable String                       databaseManagerName,
                                                  @PathVariable String                       primaryKeyColumnGUID,
                                                  @PathVariable String                       foreignKeyColumnGUID,
                                                  @RequestBody  DatabaseForeignKeyProperties databaseForeignKeyProperties)
    {
        return restAPI.addForeignKeyRelationship(serverName, userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, foreignKeyColumnGUID, databaseForeignKeyProperties);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the owning DBMS
     * @param databaseManagerName unique name of software server capability representing the owning DBMS
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/database-managers/{databaseManagerGUID}/{databaseManagerName}/databases/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}/delete")

    public VoidResponse removeForeignKeyRelationship(@PathVariable                  String          serverName,
                                                     @PathVariable                  String          userId,
                                                     @PathVariable                  String          databaseManagerGUID,
                                                     @PathVariable                  String          databaseManagerName,
                                                     @PathVariable                  String          primaryKeyColumnGUID,
                                                     @PathVariable                  String          foreignKeyColumnGUID,
                                                     @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeForeignKeyRelationship(serverName, userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, foreignKeyColumnGUID, nullRequestBody);
    }
}
