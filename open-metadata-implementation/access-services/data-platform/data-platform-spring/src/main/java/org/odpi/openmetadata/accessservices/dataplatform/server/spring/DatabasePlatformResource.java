/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.dataplatform.properties.*;
import org.odpi.openmetadata.accessservices.dataplatform.rest.*;
import org.odpi.openmetadata.accessservices.dataplatform.server.DatabasePlatformRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import org.springframework.web.bind.annotation.*;


/**
 * DatabasePlatformRESTServices is the server-side implementation of the Data Platform OMAS's
 * support for relational databases.  It matches the DatabasePlatformClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}")

@Tag(name="Data Platform OMAS",
        description="The Data Platform OMAS provides APIs for tools and applications wishing to manage metadata relating to data platforms.",
        externalDocs=@ExternalDocumentation(description="Data Platform Open Metadata Access Service (OMAS)",
                url="https://egeria.odpi.org/open-metadata-implementation/access-services/data-platform/"))

public class DatabasePlatformResource
{
    private DatabasePlatformRESTServices restAPI = new DatabasePlatformRESTServices();

    /**
     * Default constructor
     */
    public DatabasePlatformResource()
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
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases")

    public GUIDResponse createDatabase(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @PathVariable String             integratorGUID,
                                       @PathVariable String             integratorName,
                                       @RequestBody  DatabaseProperties databaseProperties)
    {
        return restAPI.createDatabase(serverName, userId, integratorGUID, integratorName, databaseProperties);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/from-template/{templateGUID}")

    public GUIDResponse createDatabaseFromTemplate(@PathVariable String             serverName,
                                                   @PathVariable String             userId,
                                                   @PathVariable String             integratorGUID,
                                                   @PathVariable String             integratorName,
                                                   @PathVariable String             templateGUID,
                                                   @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseFromTemplate(serverName, userId, integratorGUID, integratorName, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}")

    public VoidResponse updateDatabase(@PathVariable String             serverName,
                                       @PathVariable String             userId,
                                       @PathVariable String             integratorGUID,
                                       @PathVariable String             integratorName,
                                       @PathVariable String             databaseGUID,
                                       @RequestBody  DatabaseProperties databaseProperties)
    {
        return restAPI.updateDatabase(serverName, userId, integratorGUID, integratorName, databaseGUID, databaseProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}/publish")

    public VoidResponse publishDatabase(@PathVariable                  String          serverName,
                                        @PathVariable                  String          userId,
                                        @PathVariable                  String          integratorGUID,
                                        @PathVariable                  String          integratorName,
                                        @PathVariable                  String          databaseGUID,
                                        @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishDatabase(serverName, userId, integratorGUID, integratorName, databaseGUID, nullRequestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}/withdraw")

    public VoidResponse withdrawDatabase(@PathVariable                  String          serverName,
                                         @PathVariable                  String          userId,
                                         @PathVariable                  String          integratorGUID,
                                         @PathVariable                  String          integratorName,
                                         @PathVariable                  String          databaseGUID,
                                         @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, integratorGUID, integratorName, databaseGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabase(@PathVariable                  String          serverName,
                                       @PathVariable                  String          userId,
                                       @PathVariable                  String          integratorGUID,
                                       @PathVariable                  String          integratorName,
                                       @PathVariable                  String          databaseGUID,
                                       @PathVariable                  String          qualifiedName,
                                       @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabase(serverName, userId, integratorGUID, integratorName, databaseGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/by-search-string/{searchString}")

    public DatabasesResponse findDatabases(@PathVariable String serverName,
                                           @PathVariable String userId,
                                           @PathVariable String searchString,
                                           @RequestParam int    startFrom,
                                           @RequestParam int    pageSize)
    {
        return restAPI.findDatabases(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/by-name/{name}")

    public DatabasesResponse   getDatabasesByName(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String name,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.getDatabasesByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/for-integrator/{integratorGUID}/{integratorName}")

    public DatabasesResponse   getDatabasesByDaemon(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String integratorGUID,
                                                    @PathVariable String integratorName,
                                                    @RequestParam int    startFrom,
                                                    @RequestParam int    pageSize)
    {
        return restAPI.getDatabasesByDaemon(serverName, userId, integratorGUID, integratorName, startFrom, pageSize);
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
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}/schemas")

    public GUIDResponse createDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   integratorGUID,
                                             @PathVariable String                   integratorName,
                                             @PathVariable String                   databaseGUID,
                                             @RequestBody  DatabaseSchemaProperties databaseSchemaProperties)
    {
        return restAPI.createDatabaseSchema(serverName, userId, integratorGUID, integratorName, databaseGUID, databaseSchemaProperties);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/{databaseGUID}/schemas/from-template/{templateGUID}")

    public GUIDResponse createDatabaseSchemaFromTemplate(@PathVariable String             serverName,
                                                         @PathVariable String             userId,
                                                         @PathVariable String             integratorGUID,
                                                         @PathVariable String             integratorName,
                                                         @PathVariable String             templateGUID,
                                                         @PathVariable String             databaseGUID,
                                                         @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseSchemaFromTemplate(serverName, userId, integratorGUID, integratorName, templateGUID, databaseGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}")

    public VoidResponse updateDatabaseSchema(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   integratorGUID,
                                             @PathVariable String                   integratorName,
                                             @PathVariable String                   databaseSchemaGUID,
                                             @RequestBody  DatabaseSchemaProperties databaseSchemaProperties)
    {
        return restAPI.updateDatabaseSchema(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, databaseSchemaProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Platform OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/publish")

    public VoidResponse publishDatabaseSchema(@PathVariable                  String          serverName,
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          integratorGUID,
                                              @PathVariable                  String          integratorName,
                                              @PathVariable                  String          databaseSchemaGUID,
                                              @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishDatabaseSchema(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, nullRequestBody);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Platform OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/withdraw")

    public VoidResponse withdrawDatabaseSchema(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          integratorGUID,
                                               @PathVariable                  String          integratorName,
                                               @PathVariable                  String          databaseSchemaGUID,
                                               @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawDatabase(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseSchema(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          integratorGUID,
                                             @PathVariable                  String          integratorName,
                                             @PathVariable                  String          databaseSchemaGUID,
                                             @PathVariable                  String          qualifiedName,
                                             @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseSchema(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/by-search-string/{searchString}")

    public DatabaseSchemasResponse   findDatabaseSchemas(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String searchString,
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.findDatabaseSchemas(serverName, userId, searchString, startFrom, pageSize);
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
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/by-name/{name}")

    public DatabaseSchemasResponse   getDatabaseSchemasByName(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String name,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getDatabaseSchemasByName(serverName, userId, name, startFrom, pageSize);
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
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables")

    public GUIDResponse createDatabaseTable(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  integratorGUID,
                                            @PathVariable String                  integratorName,
                                            @PathVariable String                  databaseSchemaGUID,
                                            @RequestBody  DatabaseTableProperties databaseTableProperties)
    {
        return restAPI.createDatabaseTable(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, databaseTableProperties);
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables/from-template/{templateGUID}")

    public GUIDResponse createDatabaseTableFromTemplate(@PathVariable String             serverName,
                                                        @PathVariable String             userId,
                                                        @PathVariable String             integratorGUID,
                                                        @PathVariable String             integratorName,
                                                        @PathVariable String             templateGUID,
                                                        @PathVariable String             databaseSchemaGUID,
                                                        @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseTableFromTemplate(serverName, userId, integratorGUID, integratorName, templateGUID, databaseSchemaGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table to update
     * @param databaseTableProperties new properties for the database table
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables/{databaseTableGUID}")

    public VoidResponse updateDatabaseTable(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @PathVariable String                  integratorGUID,
                                            @PathVariable String                  integratorName,
                                            @PathVariable String                  databaseTableGUID,
                                            @RequestBody  DatabaseTableProperties databaseTableProperties)
    {
        return restAPI.updateDatabaseTable(serverName, userId, integratorGUID, integratorName, databaseTableGUID, databaseTableProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables/{databaseTableGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseTable(@PathVariable                  String          serverName,
                                            @PathVariable                  String          userId,
                                            @PathVariable                  String          integratorGUID,
                                            @PathVariable                  String          integratorName,
                                            @PathVariable                  String          databaseTableGUID,
                                            @PathVariable                  String          qualifiedName,
                                            @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseTable(serverName, userId, integratorGUID, integratorName, databaseTableGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/by-search-string/{searchString}")

    public DatabaseTablesResponse   findDatabaseTables(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String searchString,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.findDatabaseTables(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database tables associated with a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/{databaseSchemaGUID}/tables")

    public DatabaseTablesResponse    getTablesForDatabaseSchema(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String databaseSchemaGUID,
                                                                @RequestParam int    startFrom,
                                                                @RequestParam int    pageSize)
    {
        return restAPI.getTablesForDatabaseSchema(serverName, userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database table metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/by-name/{name}")

    public DatabaseTablesResponse   getDatabaseTablesByName(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String name,
                                                            @RequestParam int    startFrom,
                                                            @RequestParam int    pageSize)
    {
        return restAPI.getDatabaseTablesByName(serverName, userId, name, startFrom, pageSize);
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
    @GetMapping(path = "/databases/schemas/tables/{guid}")

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
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables/views")

    public GUIDResponse createDatabaseView(@PathVariable String                 serverName,
                                           @PathVariable String                 userId,
                                           @PathVariable String                 integratorGUID,
                                           @PathVariable String                 integratorName,
                                           @PathVariable String                 databaseSchemaGUID,
                                           @RequestBody  DatabaseViewProperties databaseViewProperties)
    {
        return restAPI.createDatabaseView(serverName, userId, integratorGUID, integratorName, databaseSchemaGUID, databaseViewProperties);
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/{databaseSchemaGUID}/tables/views/from-template/{templateGUID}")

    public GUIDResponse createDatabaseViewFromTemplate(@PathVariable String             serverName,
                                                       @PathVariable String             userId,
                                                       @PathVariable String             integratorGUID,
                                                       @PathVariable String             integratorName,
                                                       @PathVariable String             templateGUID,
                                                       @PathVariable String             databaseSchemaGUID,
                                                       @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseViewFromTemplate(serverName, userId, integratorGUID, integratorName, templateGUID, databaseSchemaGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the database view to update
     * @param databaseViewProperties properties for the new database view
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/views/{databaseViewGUID}")

    public VoidResponse updateDatabaseView(@PathVariable String                 serverName,
                                           @PathVariable String                 userId,
                                           @PathVariable String                 integratorGUID,
                                           @PathVariable String                 integratorName,
                                           @PathVariable String                 databaseViewGUID,
                                           @RequestBody  DatabaseViewProperties databaseViewProperties)
    {
        return restAPI.updateDatabaseView(serverName, userId, integratorGUID, integratorName, databaseViewGUID, databaseViewProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/views/{databaseViewGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseView(@PathVariable                  String          serverName,
                                           @PathVariable                  String          userId,
                                           @PathVariable                  String          integratorGUID,
                                           @PathVariable                  String          integratorName,
                                           @PathVariable                  String          databaseViewGUID,
                                           @PathVariable                  String          qualifiedName,
                                           @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseView(serverName, userId, integratorGUID, integratorName, databaseViewGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/views/by-search-string/{searchString}")

    public DatabaseViewsResponse   findDatabaseViews(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String searchString,
                                                     @RequestParam int    startFrom,
                                                     @RequestParam int    pageSize)
    {
        return restAPI.findDatabaseViews(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database views associated with a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseSchemaGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/{databaseSchemaGUID}/tables/views")

    public DatabaseViewsResponse    getViewsForDatabaseSchema(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String databaseSchemaGUID,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getViewsForDatabaseSchema(serverName, userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database view metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/views/by-name/{name}")

    public DatabaseViewsResponse   getDatabaseViewsByName(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String name,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getDatabaseViewsByName(serverName, userId, name, startFrom, pageSize);
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
    @GetMapping(path = "/databases/schemas/tables/views/{guid}")

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
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/{databaseTableGUID}/columns")

    public GUIDResponse createDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   integratorGUID,
                                             @PathVariable String                   integratorName,
                                             @PathVariable String                   databaseTableGUID,
                                             @RequestBody  DatabaseColumnProperties databaseColumnProperties)
    {
        return restAPI.createDatabaseColumn(serverName, userId, integratorGUID, integratorName, databaseTableGUID, databaseColumnProperties);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/{databaseTableGUID}/columns/from-template/{templateGUID}")

    public GUIDResponse createDatabaseColumnFromTemplate(@PathVariable String             serverName,
                                                         @PathVariable String             userId,
                                                         @PathVariable String             integratorGUID,
                                                         @PathVariable String             integratorName,
                                                         @PathVariable String             templateGUID,
                                                         @PathVariable String             databaseTableGUID,
                                                         @RequestBody  TemplateProperties templateProperties)
    {
        return restAPI.createDatabaseColumnFromTemplate(serverName, userId, integratorGUID, integratorName, templateGUID, databaseTableGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{databaseColumnGUID}")

    public VoidResponse updateDatabaseColumn(@PathVariable String                   serverName,
                                             @PathVariable String                   userId,
                                             @PathVariable String                   integratorGUID,
                                             @PathVariable String                   integratorName,
                                             @PathVariable String                   databaseColumnGUID,
                                             @RequestBody  DatabaseColumnProperties databaseColumnProperties)
    {
        return restAPI.updateDatabaseColumn(serverName, userId, integratorGUID, integratorName, databaseColumnGUID, databaseColumnProperties);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{databaseColumnGUID}/{qualifiedName}/delete")

    public VoidResponse removeDatabaseColumn(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          integratorGUID,
                                             @PathVariable                  String          integratorName,
                                             @PathVariable                  String          databaseColumnGUID,
                                             @PathVariable                  String          qualifiedName,
                                             @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeDatabaseColumn(serverName, userId, integratorGUID, integratorName, databaseColumnGUID, qualifiedName, nullRequestBody);
    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/columns/by-search-string/{searchString}")

    public DatabaseColumnsResponse   findDatabaseColumns(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String searchString,
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.findDatabaseColumns(serverName, userId, searchString, startFrom, pageSize);
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
    @GetMapping(path = "/databases/schemas/tables/{databaseTableGUID}/columns")

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
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/databases/schemas/tables/columns/by-name/{name}")

    public DatabaseColumnsResponse   getDatabaseColumnsByName(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String name,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getDatabaseColumnsByName(serverName, userId, name, startFrom, pageSize);
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
    @GetMapping(path = "/databases/schemas/tables/columns/{guid}")

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
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{databaseColumnGUID}/primary-key")

    public VoidResponse setPrimaryKeyOnColumn(@PathVariable String                       serverName,
                                              @PathVariable String                       userId,
                                              @PathVariable String                       integratorGUID,
                                              @PathVariable String                       integratorName,
                                              @PathVariable String                       databaseColumnGUID,
                                              @RequestBody  DatabasePrimaryKeyProperties databasePrimaryKeyProperties)
    {
        return restAPI.setPrimaryKeyOnColumn(serverName, userId, integratorGUID, integratorName, databaseColumnGUID, databasePrimaryKeyProperties);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{databaseColumnGUID}/primary-key/delete")

    public VoidResponse removePrimaryKeyFromColumn(@PathVariable                  String          serverName,
                                                   @PathVariable                  String          userId,
                                                   @PathVariable                  String          integratorGUID,
                                                   @PathVariable                  String          integratorName,
                                                   @PathVariable                  String          databaseColumnGUID,
                                                   @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removePrimaryKeyFromColumn(serverName, userId, integratorGUID, integratorName, databaseColumnGUID, nullRequestBody);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}")

    public VoidResponse addForeignKeyRelationship(@PathVariable String                       serverName,
                                                  @PathVariable String                       userId,
                                                  @PathVariable String                       integratorGUID,
                                                  @PathVariable String                       integratorName,
                                                  @PathVariable String                       primaryKeyColumnGUID,
                                                  @PathVariable String                       foreignKeyColumnGUID,
                                                  @RequestBody  DatabaseForeignKeyProperties databaseForeignKeyProperties)
    {
        return restAPI.addForeignKeyRelationship(serverName, userId, integratorGUID, integratorName, primaryKeyColumnGUID, foreignKeyColumnGUID, databaseForeignKeyProperties);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/integrators/{integratorGUID}/{integratorName}/databases/schemas/tables/columns/{foreignKeyColumnGUID}/foreign-key/{primaryKeyColumnGUID}/delete")

    public VoidResponse removeForeignKeyRelationship(@PathVariable                  String          serverName,
                                                     @PathVariable                  String          userId,
                                                     @PathVariable                  String          integratorGUID,
                                                     @PathVariable                  String          integratorName,
                                                     @PathVariable                  String          primaryKeyColumnGUID,
                                                     @PathVariable                  String          foreignKeyColumnGUID,
                                                     @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.removeForeignKeyRelationship(serverName, userId, integratorGUID, integratorName, primaryKeyColumnGUID, foreignKeyColumnGUID, nullRequestBody);
    }
}
