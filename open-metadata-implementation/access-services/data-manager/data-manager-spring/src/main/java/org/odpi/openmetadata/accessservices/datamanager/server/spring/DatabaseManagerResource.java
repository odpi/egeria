/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.datamanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.server.DatabaseManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;

import org.springframework.web.bind.annotation.*;


/**
 * DatabaseManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for relational databases.  It matches the DatabaseManagerClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Metadata Access Server: Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Further Information",
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
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases")

    public GUIDResponse createDatabase(@PathVariable String                   serverName,
                                       @PathVariable String                   userId,
                                       @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabase(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/from-template/{templateGUID}")

    public GUIDResponse createDatabaseFromTemplate(@PathVariable String              serverName,
                                                   @PathVariable String              userId,
                                                   @PathVariable String              templateGUID,
                                                   @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDatabaseFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}")

    public VoidResponse updateDatabase(@PathVariable String                   serverName,
                                       @PathVariable String                   userId,
                                       @PathVariable String                   databaseGUID,
                                       @RequestParam(required = false)
                                                     boolean                  isMergeUpdate,
                                       @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateDatabase(serverName, userId, databaseGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to publish
     * @param requestBody classification request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}/publish")

    public VoidResponse publishDatabase(@PathVariable                  String                    serverName,
                                        @PathVariable                  String                    userId,
                                        @PathVariable                  String                    databaseGUID,
                                        @RequestBody(required = false) ClassificationRequestBody requestBody)
    {
        return restAPI.publishDatabase(serverName, userId, databaseGUID, requestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to withdraw
     * @param requestBody classification request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}/withdraw")

    public VoidResponse withdrawDatabase(@PathVariable                  String                    serverName,
                                         @PathVariable                  String                    userId,
                                         @PathVariable                  String                    databaseGUID,
                                         @RequestBody(required = false) ClassificationRequestBody requestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, databaseGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param requestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/{databaseGUID}/delete")

    public VoidResponse removeDatabase(@PathVariable                  String                    serverName,
                                       @PathVariable                  String                    userId,
                                       @PathVariable                  String                    databaseGUID,
                                       @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeDatabase(serverName, userId, databaseGUID, requestBody);
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
     * @param requestBody properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas")

    public GUIDResponse createDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseSchema(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/from-template/{templateGUID}")

    public GUIDResponse createDatabaseSchemaFromTemplate(@PathVariable String              serverName,
                                                         @PathVariable String              userId,
                                                         @PathVariable String              templateGUID,
                                                         @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDatabaseSchemaFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}")

    public VoidResponse updateDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseSchemaGUID,
                                             @RequestParam(required = false)
                                                           boolean                  isMergeUpdate,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateDatabaseSchema(serverName, userId, databaseSchemaGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     * @param requestBody classification request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}/publish")

    public VoidResponse publishDatabaseSchema(@PathVariable                  String                    serverName,
                                              @PathVariable                  String                    userId,
                                              @PathVariable                  String                    databaseSchemaGUID,
                                              @RequestBody(required = false) ClassificationRequestBody requestBody)
    {
        return restAPI.publishDatabaseSchema(serverName, userId, databaseSchemaGUID, requestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     * @param requestBody classification request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}/withdraw")

    public VoidResponse withdrawDatabaseSchema(@PathVariable                  String                    serverName,
                                               @PathVariable                  String                    userId,
                                               @PathVariable                  String                    databaseSchemaGUID,
                                               @RequestBody(required = false) ClassificationRequestBody requestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, databaseSchemaGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schemas/{databaseSchemaGUID}/delete")

    public VoidResponse removeDatabaseSchema(@PathVariable                  String                    serverName,
                                             @PathVariable                  String                    userId,
                                             @PathVariable                  String                    databaseSchemaGUID,
                                             @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeDatabaseSchema(serverName, userId, databaseSchemaGUID, requestBody);
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
     * @param requestBody qualified name of the schema type - suggest "SchemaOf:" + asset's qualified name
     * @return unique identifier of the database schema type or
     *  InvalidParameterException the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException problem accessing the property server
     */
    @PostMapping(path = "/databases/schema-type")

    public GUIDResponse createDatabaseSchemaType(@PathVariable String                   serverName,
                                                 @PathVariable String                   userId,
                                                 @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseSchemaType(serverName, userId, requestBody);
    }


    /**
     * Link the schema type and asset.  This is called from outside the AssetHandler.  The databaseAssetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the asset to connect the schema to
     * @param schemaTypeGUID identifier for schema Type object
     * @param requestBody null request body
     * @return void or
     *  InvalidParameterException the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException problem accessing the property server
     */
    @PostMapping(path = "/databases/{databaseAssetGUID}/schema-type/{schemaTypeGUID}")

    public  VoidResponse attachSchemaTypeToDatabaseAsset(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @PathVariable String                  databaseAssetGUID,
                                                         @PathVariable String                  schemaTypeGUID,
                                                         @RequestBody(required = false)
                                                                       RelationshipRequestBody requestBody)
    {
        return restAPI.attachSchemaTypeToDatabaseAsset(serverName, userId, databaseAssetGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables")

    public GUIDResponse createDatabaseTable(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseTable(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/from-template/{templateGUID}")

    public GUIDResponse createDatabaseTableFromTemplate(@PathVariable String              serverName,
                                                        @PathVariable String              userId,
                                                        @PathVariable String              templateGUID,
                                                        @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDatabaseTableFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param requestBody properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schema-type/tables")

    public GUIDResponse createDatabaseTableForSchemaType(@PathVariable String                   serverName,
                                                         @PathVariable String                   userId,
                                                         @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseTableForSchemaType(serverName, userId, requestBody);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the database table to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the database table
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/{databaseTableGUID}")

    public VoidResponse updateDatabaseTable(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   databaseTableGUID,
                                            @RequestParam(required = false)
                                                          boolean                  isMergeUpdate,
                                            @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateDatabaseTable(serverName, userId, databaseTableGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param requestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/{databaseTableGUID}/delete")

    public VoidResponse removeDatabaseTable(@PathVariable                  String                    serverName,
                                            @PathVariable                  String                    userId,
                                            @PathVariable                  String                    databaseTableGUID,
                                            @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeDatabaseTable(serverName, userId, databaseTableGUID, requestBody);
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
    @GetMapping(path = "/databases/{databaseAssetGUID}/tables")

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
     * @param requestBody properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views")

    public GUIDResponse createDatabaseView(@PathVariable String                   serverName,
                                           @PathVariable String                   userId,
                                           @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseView(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views/from-template/{templateGUID}")

    public GUIDResponse createDatabaseViewFromTemplate(@PathVariable String              serverName,
                                                       @PathVariable String              userId,
                                                       @PathVariable String              templateGUID,
                                                       @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDatabaseViewFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/schema-type/tables/views")

    public GUIDResponse createDatabaseViewForSchemaType(@PathVariable String                   serverName,
                                                        @PathVariable String                   userId,
                                                        @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseViewForSchemaType(serverName, userId, requestBody);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseViewGUID unique identifier of the database view to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody properties for the new database view
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views/{databaseViewGUID}")

    public VoidResponse updateDatabaseView(@PathVariable String                   serverName,
                                           @PathVariable String                   userId,
                                           @PathVariable String                   databaseViewGUID,
                                           @RequestParam(required = false)
                                                         boolean                  isMergeUpdate,
                                           @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateDatabaseView(serverName, userId, databaseViewGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/views/{databaseViewGUID}/delete")

    public VoidResponse removeDatabaseView(@PathVariable                  String                    serverName,
                                           @PathVariable                  String                    userId,
                                           @PathVariable                  String                    databaseViewGUID,
                                           @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeDatabaseView(serverName, userId, databaseViewGUID, requestBody);
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
    @GetMapping(path = "/databases/{databaseAssetGUID}/tables/views")

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
     * @param requestBody properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns")

    public GUIDResponse createDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createDatabaseColumn(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/from-template/{templateGUID}")

    public GUIDResponse createDatabaseColumnFromTemplate(@PathVariable String              serverName,
                                                         @PathVariable String              userId,
                                                         @PathVariable String              templateGUID,
                                                         @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDatabaseColumnFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{databaseColumnGUID}")

    public VoidResponse updateDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   databaseColumnGUID,
                                             @RequestParam(required = false)
                                                           boolean                  isMergeUpdate,
                                             @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateDatabaseColumn(serverName, userId, databaseColumnGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{databaseColumnGUID}/delete")

    public VoidResponse removeDatabaseColumn(@PathVariable                  String                    serverName,
                                             @PathVariable                  String                    userId,
                                             @PathVariable                  String                    databaseColumnGUID,
                                             @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeDatabaseColumn(serverName, userId, databaseColumnGUID, requestBody);
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
     * in this column, and it can be used to uniquely identify the column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param requestBody properties to store
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{databaseColumnGUID}/primary-key")

    public VoidResponse setPrimaryKeyOnColumn(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    databaseColumnGUID,
                                              @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setPrimaryKeyOnColumn(serverName, userId, databaseColumnGUID, requestBody);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{databaseColumnGUID}/primary-key/delete")

    public VoidResponse removePrimaryKeyFromColumn(@PathVariable                  String                    serverName,
                                                   @PathVariable                  String                    userId,
                                                   @PathVariable                  String                    databaseColumnGUID,
                                                   @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removePrimaryKeyFromColumn(serverName, userId, databaseColumnGUID, requestBody);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param requestBody properties about the foreign key relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}")

    public VoidResponse addForeignKeyRelationship(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @PathVariable String                  primaryKeyColumnGUID,
                                                  @PathVariable String                  foreignKeyColumnGUID,
                                                  @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.addForeignKeyRelationship(serverName, userId, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/databases/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}/delete")

    public VoidResponse removeForeignKeyRelationship(@PathVariable                  String          serverName,
                                                     @PathVariable                  String          userId,
                                                     @PathVariable                  String          primaryKeyColumnGUID,
                                                     @PathVariable                  String          foreignKeyColumnGUID,
                                                     @RequestBody(required = false) ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeForeignKeyRelationship(serverName, userId, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody);
    }
}
