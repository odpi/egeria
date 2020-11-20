/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.database.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import java.util.List;


/**
 * DatabaseIntegratorContext is the context for managing resources from a relational database server.
 */
public class DatabaseIntegratorContext
{
    private DatabaseManagerClient  client;
    private DataManagerEventClient eventClient;
    private String                 userId;
    private String                 databaseManagerGUID;
    private String                 databaseManagerName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param client client to map request to
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param databaseManagerGUID unique identifier of the software server capability for the database manager
     * @param databaseManagerName unique name of the software server capability for the database manager
     */
    public DatabaseIntegratorContext(DatabaseManagerClient  client,
                                     DataManagerEventClient eventClient,
                                     String                 userId,
                                     String                 databaseManagerGUID,
                                     String                 databaseManagerName)
    {
        this.client              = client;
        this.eventClient         = eventClient;
        this.userId              = userId;
        this.databaseManagerGUID = databaseManagerGUID;
        this.databaseManagerName = databaseManagerName;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database.
     *
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabase(DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.createDatabase(userId, databaseManagerGUID, databaseManagerName, databaseProperties);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseFromTemplate(String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return client.createDatabaseFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabase(String             databaseGUID,
                               DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        client.updateDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID, databaseProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param databaseGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabase(String databaseGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        client.publishDatabase(userId, databaseGUID);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param databaseGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabase(String databaseGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        client.withdrawDatabase(userId, databaseGUID);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabase(String databaseGUID,
                               String qualifiedName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        client.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID, qualifiedName);
    }


    /**
     * Retrieve the list of database metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<DatabaseElement> findDatabases(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return client.findDatabases(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<DatabaseElement>   getDatabasesByName(String name,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return client.getDatabasesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DatabaseElement>   getMyDatabases(int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return client.getDatabasesForDatabaseManager(userId, databaseManagerGUID, databaseManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the database metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseElement getDatabaseByGUID(String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return client.getDatabaseByGUID(userId, guid);
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseSchema(String                   databaseGUID,
                                       DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return client.createDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseGUID, databaseSchemaProperties);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
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
    public String createDatabaseSchemaFromTemplate(String             templateGUID,
                                                   String             databaseGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return client.createDatabaseSchemaFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseSchema(String                   databaseSchemaGUID,
                                     DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        client.updateDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, databaseSchemaProperties);
    }


    /**
     * Update the zones for the database asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDatabaseSchema(String databaseSchemaGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        client.publishDatabaseSchema(userId, databaseSchemaGUID);
    }


    /**
     * Update the zones for the database asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the database is first created).
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDatabaseSchema(String databaseSchemaGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        client.withdrawDatabaseSchema(userId, databaseSchemaGUID);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseSchema(String databaseSchemaGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        client.removeDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, qualifiedName);
    }


    /**
     * Retrieve the list of database schema metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<DatabaseSchemaElement>   findDatabaseSchemas(String searchString,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.findDatabaseSchemas(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a database.
     *
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
    public List<DatabaseSchemaElement>   getSchemasForDatabase(String databaseGUID,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return client.getSchemasForDatabase(userId, databaseGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database schema metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<DatabaseSchemaElement>   getDatabaseSchemasByName(String name,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getDatabaseSchemasByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database schema metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseSchemaElement getDatabaseSchemaByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return client.getDatabaseSchemaByGUID(userId, guid);
    }


    /* ==========================================================================
     * A database schema may contain multiple database tables and database views.
     */

    /**
     * Create a new metadata element to represent a database table.
     *
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTable(String                  databaseSchemaGUID,
                                      DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return client.createDatabaseTable(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, databaseTableProperties);
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseTableFromTemplate(String             templateGUID,
                                                  String             databaseSchemaGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return client.createDatabaseTableFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseSchemaGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the database table to update
     * @param databaseTableProperties new properties for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseTable(String                  databaseTableGUID,
                                    DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        client.updateDatabaseTable(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, databaseTableProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseTable(String databaseTableGUID,
                                    String qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        client.removeDatabaseTable(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, qualifiedName);
    }


    /**
     * Retrieve the list of database table metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<DatabaseTableElement>   findDatabaseTables(String searchString,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return client.findDatabaseTables(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database tables associated with a database schema.
     *
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
    public List<DatabaseTableElement>    getTablesForDatabaseSchema(String databaseSchemaGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return client.getTablesForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database table metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<DatabaseTableElement>   getDatabaseTablesByName(String name,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return client.getDatabaseTablesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database table metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseTableElement getDatabaseTableByGUID(String guid) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return client.getDatabaseTableByGUID(userId, guid);
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseView(String                 databaseSchemaGUID,
                                     DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return client.createDatabaseView(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, databaseViewProperties);
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseViewFromTemplate(String             templateGUID,
                                                 String             databaseSchemaGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return client.createDatabaseViewFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseSchemaGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the database view to update
     * @param databaseViewProperties properties for the new database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseView(String                 databaseViewGUID,
                                   DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        client.updateDatabaseView(userId, databaseManagerGUID, databaseManagerName, databaseViewGUID, databaseViewProperties);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseView(String databaseViewGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        client.removeDatabaseView(userId, databaseManagerGUID, databaseManagerName, databaseViewGUID, qualifiedName);
    }


    /**
     * Retrieve the list of database view metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<DatabaseViewElement>   findDatabaseViews(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.findDatabaseViews(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database views associated with a database schema.
     *
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
    public List<DatabaseViewElement>    getViewsForDatabaseSchema(String databaseSchemaGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getViewsForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database view metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<DatabaseViewElement>   getDatabaseViewsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return client.getDatabaseViewsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database view metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseViewElement getDatabaseViewByGUID(String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.getDatabaseViewByGUID(userId, guid);
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDatabaseColumn(String                   databaseTableGUID,
                                       DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return client.createDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, databaseColumnProperties);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
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
    public String createDatabaseColumnFromTemplate(String             templateGUID,
                                                   String             databaseTableGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return client.createDatabaseColumnFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, databaseTableGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDatabaseColumn(String                   databaseColumnGUID,
                                     DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        client.updateDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, databaseColumnProperties);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDatabaseColumn(String databaseColumnGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        client.removeDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, qualifiedName);
    }


    /**
     * Retrieve the list of database column metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<DatabaseColumnElement>   findDatabaseColumns(String searchString,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
       return client.findDatabaseColumns(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of column for a database table (or view)
     *
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
    public List<DatabaseColumnElement>    getColumnsForDatabaseTable(String databaseTableGUID,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return client.getColumnsForDatabaseTable(userId, databaseTableGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of database column metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<DatabaseColumnElement>   getDatabaseColumnsByName(String name,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getDatabaseColumnsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the database column metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseColumnElement getDatabaseColumnByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return client.getDatabaseColumnByGUID(userId, guid);
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column and it can be used to uniquely identify the column.
     *
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setPrimaryKeyOnColumn(String                       databaseColumnGUID,
                                      DatabasePrimaryKeyProperties databasePrimaryKeyProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        client.setPrimaryKeyOnColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, databasePrimaryKeyProperties);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param databaseColumnGUID unique identifier if the primary key column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePrimaryKeyFromColumn(String databaseColumnGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        client.removePrimaryKeyFromColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addForeignKeyRelationship(String                       primaryKeyColumnGUID,
                                          String                       foreignKeyColumnGUID,
                                          DatabaseForeignKeyProperties databaseForeignKeyProperties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        client.addForeignKeyRelationship(userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, foreignKeyColumnGUID, databaseForeignKeyProperties);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForeignKeyRelationship(String  primaryKeyColumnGUID,
                                             String  foreignKeyColumnGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        client.removeForeignKeyRelationship(userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, foreignKeyColumnGUID);
    }
}
