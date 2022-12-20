/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.DatabaseManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * DatabaseManagerClient is the client for managing resources from a relational database server.
 */
public class DatabaseManagerClient extends DataManagerBaseClient implements DatabaseManagerInterface
{
    private final String urlTemplatePrefix  = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/databases";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String serverName,
                                 String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 DataManagerRESTClient restClient,
                                 int                   maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * The database is the top level asset on a database server
     */


    /**
     * Create a new metadata element to represent a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabase(String             userId,
                                 String             databaseManagerGUID,
                                 String             databaseManagerName,
                                 DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "createDatabase";
        final String propertiesParameterName     = "databaseProperties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix;

        return super.createReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseFromTemplate(String             userId,
                                             String             databaseManagerGUID,
                                             String             databaseManagerName,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName  = "createDatabaseFromTemplate";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/from-template/{2}";

        return super.createReferenceableFromTemplate(userId, databaseManagerGUID, databaseManagerName, templateGUID, templateProperties, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDatabase(String             userId,
                               String             databaseManagerGUID,
                               String             databaseManagerName,
                               String             databaseGUID,
                               boolean            isMergeUpdate,
                               DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                  = "updateDatabase";
        final String elementGUIDParameterName    = "databaseGUID";
        final String propertiesParameterName     = "databaseProperties";
        final String urlTemplate                 = serverPlatformURLRoot + urlTemplatePrefix + "/{2}";

        super.updateReferenceable(userId,
                                  databaseManagerGUID,
                                  databaseManagerName,
                                  databaseGUID,
                                  elementGUIDParameterName,
                                  isMergeUpdate,
                                  databaseProperties,
                                  propertiesParameterName,
                                  urlTemplate,
                                  methodName);
    }


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
    @Override
    public void publishDatabase(String userId,
                                String databaseGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName               = "publishDatabase";
        final String elementGUIDParameterName = "databaseGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/publish";

        super.setReferenceableClassification(userId, null, null, databaseGUID, elementGUIDParameterName, null, urlTemplate, methodName);
    }


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
    @Override
    public void withdrawDatabase(String userId,
                                 String databaseGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName               = "withdrawDatabase";
        final String elementGUIDParameterName = "databaseGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "databases/{2}/withdraw";

        super.setReferenceableClassification(userId, null, null, databaseGUID, elementGUIDParameterName, null, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabase(String userId,
                               String databaseManagerGUID,
                               String databaseManagerName,
                               String databaseGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "removeDatabase";
        final String elementGUIDParameterName    = "databaseGUID";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseGUID, elementGUIDParameterName, urlTemplate, methodName);
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
    @Override
    public List<DatabaseElement> findDatabases(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DatabasesResponse restResult = restClient.callDatabasesPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseElement>   getDatabasesByName(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DatabasesResponse restResult = restClient.callDatabasesPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


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
    @Override
    public List<DatabaseElement>   getDatabasesForDatabaseManager(String userId,
                                                                  String databaseManagerGUID,
                                                                  String databaseManagerName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "getDatabasesForDatabaseManager";
        final String databaseManagerGUIDParameterName = "databaseManagerGUID";
        final String databaseManagerNameParameterName = "databaseManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-database-manager?startFrom={2}&pageSize={3}";

        ExternalSourceRequestBody requestBody = new ExternalSourceRequestBody();

        requestBody.setExternalSourceGUID(databaseManagerGUID);
        requestBody.setExternalSourceName(databaseManagerName);

        DatabasesResponse restResult = restClient.callDatabasesPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public DatabaseElement getDatabaseByGUID(String userId,
                                             String guid) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "getDatabaseByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}";

        DatabaseResponse restResult = restClient.callDatabaseGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         guid);

        return restResult.getElement();
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseSchema(String                   userId,
                                       String                   databaseManagerGUID,
                                       String                   databaseManagerName,
                                       String                   databaseGUID,
                                       DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                     = "createDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String propertiesParameterName        = "databaseSchemaProperties";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/schemas";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseGUID, parentElementGUIDParameterName, databaseSchemaProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
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
    @Override
    public String createDatabaseSchemaFromTemplate(String             userId,
                                                   String             databaseManagerGUID,
                                                   String             databaseManagerName,
                                                   String             templateGUID,
                                                   String             databaseGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                     = "createDatabaseSchemaFromTemplate";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/from-template/{2}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               databaseManagerGUID,
                                                               databaseManagerName,
                                                               databaseGUID,
                                                               parentElementGUIDParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               urlTemplate,
                                                               methodName);
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDatabaseSchema(String                   userId,
                                     String                   databaseManagerGUID,
                                     String                   databaseManagerName,
                                     String                   databaseSchemaGUID,
                                     boolean                  isMergeUpdate,
                                     DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName               = "updateDatabaseSchema";
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName  = "databaseProperties";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/{2}";

        super.updateReferenceable(userId,
                                  databaseManagerGUID,
                                  databaseManagerName,
                                  databaseSchemaGUID,
                                  elementGUIDParameterName,
                                  isMergeUpdate,
                                  databaseSchemaProperties,
                                  propertiesParameterName,
                                  urlTemplate,
                                  methodName);
    }


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
    @Override
    public void publishDatabaseSchema(String userId,
                                      String databaseSchemaGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                  = "publishDatabaseSchema";
        final String elementGUIDParameterName    = "databaseSchemaGUID";
        final String urlTemplate                 = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/{2}/publish";

        super.setReferenceableClassification(userId, null, null, databaseSchemaGUID, elementGUIDParameterName, null, urlTemplate, methodName);
    }


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
    @Override
    public void withdrawDatabaseSchema(String userId,
                                       String databaseSchemaGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName               = "withdrawDatabase";
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/{2}/withdraw";

        super.setReferenceableClassification(userId, null, null, databaseSchemaGUID, elementGUIDParameterName, null, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseSchema(String userId,
                                     String databaseManagerGUID,
                                     String databaseManagerName,
                                     String databaseSchemaGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName               = "removeDatabaseSchema";
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/{2}/delete";

        super.removeReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, elementGUIDParameterName, urlTemplate, methodName);
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
    @Override
    public List<DatabaseSchemaElement>   findDatabaseSchemas(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DatabaseSchemasResponse restResult = restClient.callDatabaseSchemasPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       searchString,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseSchemaElement>   getSchemasForDatabase(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/schemas?startFrom={3}&pageSize={4}";

        DatabaseSchemasResponse restResult = restClient.callDatabaseSchemasGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       databaseGUID,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseSchemaElement>   getDatabaseSchemasByName(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DatabaseSchemasResponse restResult = restClient.callDatabaseSchemasPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public DatabaseSchemaElement getDatabaseSchemaByGUID(String userId,
                                                         String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName        = "getDatabaseSchemaByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/schemas/{2}";

        DatabaseSchemaResponse restResult = restClient.callDatabaseSchemaGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     guid);

        return restResult.getElement();
    }


    /* ==========================================================================
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
    public String createDatabaseSchemaType(String               userId,
                                           String               databaseManagerGUID,
                                           String               databaseManagerName,
                                           String               qualifiedName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                 = "createDatabaseSchemaType";
        final String qualifiedNameParameterName = "qualifiedName";
        final String urlTemplate                = serverPlatformURLRoot + urlTemplatePrefix + "/schema-type";

        DatabaseSchemaTypeProperties databaseSchemaTypeProperties = new DatabaseSchemaTypeProperties();

        databaseSchemaTypeProperties.setQualifiedName(qualifiedName);

        return super.createReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeProperties, qualifiedNameParameterName, urlTemplate, methodName);
    }



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
    public  void attachSchemaTypeToDatabaseAsset(String  userId,
                                                 String  databaseManagerGUID,
                                                 String  databaseManagerName,
                                                 String  databaseAssetGUID,
                                                 String  schemaTypeGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName                     = "attachSchemaTypeToDatabaseAsset";
        final String databaseAssetGUIDParameterName = "databaseAssetGUID";
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/schema-type/{3}";

        super.setupRelationship(userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, databaseAssetGUIDParameterName, null, schemaTypeGUID, schemaTypeGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseTable(String                  userId,
                                      String                  databaseManagerGUID,
                                      String                  databaseManagerName,
                                      String                  databaseAssetGUID,
                                      DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                     = "createDatabaseTable";
        final String parentElementGUIDParameterName = "databaseAssetGUID";
        final String propertiesParameterName        = "databaseTableProperties";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/tables";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, parentElementGUIDParameterName, databaseTableProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseAssetGUID unique identifier of the database or database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseTableFromTemplate(String             userId,
                                                  String             databaseManagerGUID,
                                                  String             databaseManagerName,
                                                  String             templateGUID,
                                                  String             databaseAssetGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                     = "createDatabaseTableFromTemplate";
        final String parentElementGUIDParameterName = "databaseAssetGUID";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/tables/from-template/{2}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               databaseManagerGUID,
                                                               databaseManagerName,
                                                               databaseAssetGUID,
                                                               parentElementGUIDParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               urlTemplate,
                                                               methodName);
    }


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
    public String createDatabaseTableForSchemaType(String                  userId,
                                                   String                  databaseManagerGUID,
                                                   String                  databaseManagerName,
                                                   String                  databaseSchemaTypeGUID,
                                                   DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName                     = "createDatabaseTableForSchemaType";
        final String parentElementGUIDParameterName = "databaseSchemaTypeGUID";
        final String propertiesParameterName        = "databaseTableProperties";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/schema-type/tables";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeGUID, parentElementGUIDParameterName, databaseTableProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the database table to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseTableProperties new properties for the database table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDatabaseTable(String                  userId,
                                    String                  databaseManagerGUID,
                                    String                  databaseManagerName,
                                    String                  databaseTableGUID,
                                    boolean                 isMergeUpdate,
                                    DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName               = "updateDatabaseTable";
        final String elementGUIDParameterName = "databaseTableGUID";
        final String propertiesParameterName  = "databaseTableProperties";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/{2}";

        super.updateReferenceable(userId,
                                  databaseManagerGUID,
                                  databaseManagerName,
                                  databaseTableGUID,
                                  elementGUIDParameterName,
                                  isMergeUpdate,
                                  databaseTableProperties,
                                  propertiesParameterName,
                                  urlTemplate,
                                  methodName);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseTable(String userId,
                                    String databaseManagerGUID,
                                    String databaseManagerName,
                                    String databaseTableGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName               = "removeDatabaseTable";
        final String elementGUIDParameterName = "databaseTableGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/{2}/delete";

        super.removeReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, elementGUIDParameterName, urlTemplate, methodName);
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
    @Override
    public List<DatabaseTableElement>   findDatabaseTables(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseTableElement>    getTablesForDatabaseSchema(String userId,
                                                                    String databaseSchemaGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return this.getTablesForDatabaseAsset(userId, databaseSchemaGUID, startFrom, pageSize);
    }


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
    @Override
    public List<DatabaseTableElement>    getTablesForDatabaseAsset(String userId,
                                                                   String databaseAssetGUID,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "getTablesForDatabaseAsset";
        final String parentElementGUIDParameterName = "databaseAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseAssetGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/tables?startFrom={3}&pageSize={4}";

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     databaseAssetGUID,
                                                                                     startFrom,
                                                                                     validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseTableElement>   getDatabaseTablesByName(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public DatabaseTableElement getDatabaseTableByGUID(String userId,
                                                       String guid) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "getDatabaseTableByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/{2}";

        DatabaseTableResponse restResult = restClient.callDatabaseTableGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   guid);

        return restResult.getElement();
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseAssetGUID unique identifier of the database or database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseView(String                 userId,
                                     String                 databaseManagerGUID,
                                     String                 databaseManagerName,
                                     String                 databaseAssetGUID,
                                     DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "createDatabaseView";
        final String parentElementGUIDParameterName = "databaseAssetGUID";
        final String propertiesParameterName        = "databaseViewProperties";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseAssetGUID, parentElementGUIDParameterName, databaseViewProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
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
    @Override
    public String createDatabaseViewFromTemplate(String             userId,
                                                 String             databaseManagerGUID,
                                                 String             databaseManagerName,
                                                 String             templateGUID,
                                                 String             databaseAssetGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                     = "createDatabaseViewFromTemplate";
        final String parentElementGUIDParameterName = "databaseAssetGUID";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/from-template/{2}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               databaseManagerGUID,
                                                               databaseManagerName,
                                                               databaseAssetGUID,
                                                               parentElementGUIDParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               urlTemplate,
                                                               methodName);
    }


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
    public String createDatabaseViewForSchemaType(String                 userId,
                                                  String                 databaseManagerGUID,
                                                  String                 databaseManagerName,
                                                  String                 databaseSchemaTypeGUID,
                                                  DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName                     = "createDatabaseViewForSchemaType";
        final String parentElementGUIDParameterName = "databaseSchemaTypeGUID";
        final String propertiesParameterName        = "databaseViewProperties";
        final String urlTemplate                    = serverPlatformURLRoot + urlTemplatePrefix + "/schema-type/tables/views";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseSchemaTypeGUID, parentElementGUIDParameterName, databaseViewProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the database view to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseViewProperties properties for the new database view
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDatabaseView(String                 userId,
                                   String                 databaseManagerGUID,
                                   String                 databaseManagerName,
                                   String                 databaseViewGUID,
                                   boolean                isMergeUpdate,
                                   DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName               = "updateDatabaseView";
        final String elementGUIDParameterName = "databaseViewGUID";
        final String propertiesParameterName  = "databaseViewProperties";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/{2}";

        super.updateReferenceable(userId,
                                  databaseManagerGUID,
                                  databaseManagerName,
                                  databaseViewGUID,
                                  elementGUIDParameterName,
                                  isMergeUpdate,
                                  databaseViewProperties,
                                  propertiesParameterName,
                                  urlTemplate,
                                  methodName);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseView(String userId,
                                   String databaseManagerGUID,
                                   String databaseManagerName,
                                   String databaseViewGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName               = "removeDatabaseView";
        final String elementGUIDParameterName = "databaseViewGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/{2}/delete";

        super.removeReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseViewGUID, elementGUIDParameterName, urlTemplate, methodName);
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
    @Override
    public List<DatabaseViewElement>   findDatabaseViews(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseViewElement>    getViewsForDatabaseSchema(String userId,
                                                                  String databaseSchemaGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return this.getViewsForDatabaseAsset(userId, databaseSchemaGUID, startFrom, pageSize);
    }


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
    @Override
    public List<DatabaseViewElement> getViewsForDatabaseAsset(String userId,
                                                              String databaseAssetGUID,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                     = "getViewsForDatabaseAsset";
        final String parentElementGUIDParameterName = "databaseAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseAssetGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/tables/views?startFrom={3}&pageSize={4}";

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   databaseAssetGUID,
                                                                                   startFrom,
                                                                                   validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseViewElement>   getDatabaseViewsByName(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    requestBody,
                                                                                    serverName,
                                                                                    userId,
                                                                                    startFrom,
                                                                                    validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public DatabaseViewElement getDatabaseViewByGUID(String userId,
                                                     String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getDatabaseViewByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/views/{2}";

        DatabaseViewResponse restResult = restClient.callDatabaseViewGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 guid);

        return restResult.getElement();
    }


    /* ==============================================================================================
     * Database tables and views have columns.  They are either directly stored or derived from other
     * values.
     */


    /**
     * Create a new metadata element to represent a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDatabaseColumn(String                   userId,
                                       String                   databaseManagerGUID,
                                       String                   databaseManagerName,
                                       String                   databaseTableGUID,
                                       DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                     = "createDatabaseColumn";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String propertiesParameterName        = "databaseColumnProperties";
        final String dataTypeParameterName          = "databaseColumnProperties.dataType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseColumnProperties, propertiesParameterName, methodName);

        if (databaseColumnProperties.getExternalTypeGUID() == null)
        {
            invalidParameterHandler.validateName(databaseColumnProperties.getDataType(), dataTypeParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns";

        return super.createReferenceableWithParent(userId, databaseManagerGUID, databaseManagerName, databaseTableGUID, parentElementGUIDParameterName, databaseColumnProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
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
    @Override
    public String createDatabaseColumnFromTemplate(String             userId,
                                                   String             databaseManagerGUID,
                                                   String             databaseManagerName,
                                                   String             templateGUID,
                                                   String             databaseTableGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName  = "createDatabaseColumnFromTemplate";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/from-template/{2}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               databaseManagerGUID,
                                                               databaseManagerName,
                                                               databaseTableGUID,
                                                               parentElementGUIDParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               urlTemplate,
                                                               methodName);
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDatabaseColumn(String                   userId,
                                     String                   databaseManagerGUID,
                                     String                   databaseManagerName,
                                     String                   databaseColumnGUID,
                                     boolean                  isMergeUpdate,
                                     DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName               = "updateDatabaseColumn";
        final String elementGUIDParameterName = "databaseColumnGUID";
        final String propertiesParameterName  = "databaseColumnProperties";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}";

        super.updateReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, elementGUIDParameterName, isMergeUpdate, databaseColumnProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseColumn(String userId,
                                     String databaseManagerGUID,
                                     String databaseManagerName,
                                     String databaseColumnGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName                = "removeDatabaseColumn";
        final String elementGUIDParameterName  = "databaseColumnGUID";
        final String urlTemplate               = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}/delete";

        super.removeReferenceable(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, elementGUIDParameterName, urlTemplate, methodName);
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
    @Override
    public List<DatabaseColumnElement>   findDatabaseColumns(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DatabaseColumnsResponse restResult = restClient.callDatabaseColumnsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseColumnElement>    getColumnsForDatabaseTable(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/{2}/columns?startFrom={3}&pageSize={4}";

        DatabaseColumnsResponse restResult = restClient.callDatabaseColumnsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       databaseTableGUID,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public List<DatabaseColumnElement>   getDatabaseColumnsByName(String userId,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DatabaseColumnsResponse restResult = restClient.callDatabaseColumnsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize);

        return restResult.getElementList();
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
    @Override
    public DatabaseColumnElement getDatabaseColumnByGUID(String userId,
                                                         String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName        = "getDatabaseColumnByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}";

        DatabaseColumnResponse restResult = restClient.callDatabaseColumnGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     guid);

        return restResult.getElement();
    }


    /* ==================================================================================
     * Database columns can be decorated with additional information about their content.
     */

    /**
     * Classify a column in a database table as the primary key.  This means each row has a different value
     * in this column, and it can be used to uniquely identify the column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setPrimaryKeyOnColumn(String                       userId,
                                      String                       databaseManagerGUID,
                                      String                       databaseManagerName,
                                      String                       databaseColumnGUID,
                                      DatabasePrimaryKeyProperties databasePrimaryKeyProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName               = "setPrimaryKeyOnColumn";
        final String elementGUIDParameterName = "databaseColumnGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}/primary-key";

        setReferenceableClassification(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, elementGUIDParameterName, databasePrimaryKeyProperties, urlTemplate, methodName);
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removePrimaryKeyFromColumn(String                       userId,
                                           String                       databaseManagerGUID,
                                           String                       databaseManagerName,
                                           String                       databaseColumnGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName               = "removePrimaryKeyFromColumn";
        final String elementGUIDParameterName = "databaseColumnGUID";
        final String urlTemplate              = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}/primary-key/delete";

        super.removeReferenceableClassification(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, elementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addForeignKeyRelationship(String                       userId,
                                          String                       databaseManagerGUID,
                                          String                       databaseManagerName,
                                          String                       primaryKeyColumnGUID,
                                          String                       foreignKeyColumnGUID,
                                          DatabaseForeignKeyProperties databaseForeignKeyProperties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName                      = "addForeignKeyRelationship";
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";
        final String urlTemplate                     = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}/foreign-key/{3}";

        super.setupRelationship(userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, primaryElementGUIDParameterName, databaseForeignKeyProperties, foreignKeyColumnGUID, foreignElementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeForeignKeyRelationship(String                       userId,
                                             String                       databaseManagerGUID,
                                             String                       databaseManagerName,
                                             String                       primaryKeyColumnGUID,
                                             String                       foreignKeyColumnGUID) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                      = "removeForeignKeyRelationship";
        final String primaryElementGUIDParameterName = "primaryKeyColumnGUID";
        final String foreignElementGUIDParameterName = "foreignKeyColumnGUID";
        final String urlTemplate                     = serverPlatformURLRoot + urlTemplatePrefix + "/tables/columns/{2}/foreign-key/{3}/delete";

        super.clearRelationship(userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, primaryElementGUIDParameterName, foreignKeyColumnGUID, foreignElementGUIDParameterName, urlTemplate, methodName);
    }
}
