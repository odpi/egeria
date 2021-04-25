/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.DatabaseManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * DatabaseManagerClient is the client for managing resources from a relational database server.
 */
public class DatabaseManagerClient implements DatabaseManagerInterface
{
    private final String databaseManagerGUIDParameterName = "databaseManagerGUID";
    private final String databaseManagerNameParameterName = "databaseManagerName";
    private final String editURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/database-managers/{2}/{3}/databases";
    private final String retrieveURLTemplatePrefix   = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/databases";
    private final String governanceURLTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/databases";

    private String   serverName;               /* Initialized in constructor */
    private String   serverPlatformURLRoot;    /* Initialized in constructor */
    private AuditLog auditLog = null;          /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private DataManagerRESTClient   restClient;               /* Initialized in constructor */

    private static NullRequestBody nullRequestBody   = new NullRequestBody();


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
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);
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
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot);
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
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DatabaseManagerClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 DataManagerRESTClient restClient,
                                 int                   maxPageSize,
                                 AuditLog              auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
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
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);
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
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateObject(databaseProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(databaseProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix;

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  databaseProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName);

        return restResult.getGUID();
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
        final String methodName                  = "createDatabaseFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/from-template/{4}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to update
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
                               DatabaseProperties databaseProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                  = "updateDatabase";
        final String elementGUIDParameterName    = "databaseGUID";
        final String propertiesParameterName     = "databaseProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(databaseProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + governanceURLTemplatePrefix + "/{4}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + governanceURLTemplatePrefix + "databases/{4}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseGUID);
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabase(String userId,
                               String databaseManagerGUID,
                               String databaseManagerName,
                               String databaseGUID,
                               String qualifiedName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "removeDatabase";
        final String databaseManagerGUIDParameterName = "databaseManagerGUID";
        final String databaseManagerNameParameterName = "databaseManagerName";
        final String elementGUIDParameterName    = "databaseGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/{4}/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseGUID,
                                        qualifiedName);
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/by-search-string/{2}?startFrom={3}&pageSize={4}";

        DatabasesResponse restResult = restClient.callDatabasesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           searchString,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/by-name/{2}?startFrom={3}&pageSize={4}";

        DatabasesResponse restResult = restClient.callDatabasesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           name,
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "?startFrom={4}&pageSize={5}";

        DatabasesResponse restResult = restClient.callDatabasesGetRESTCall(methodName,
                                                                           urlTemplate,
                                                                           serverName,
                                                                           userId,
                                                                           databaseManagerGUID,
                                                                           databaseManagerName,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/{2}";

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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseSchemaProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/{4}/schemas";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  databaseSchemaProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseGUID);

        return restResult.getGUID();
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
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/{4}/schemas/from-template/{5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to update
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
                                     DatabaseSchemaProperties databaseSchemaProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName               = "updateDatabaseSchema";
        final String elementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName  = "databaseProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseSchemaProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseSchemaProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseSchemaGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + governanceURLTemplatePrefix + "/schemas/{4}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseSchemaGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + governanceURLTemplatePrefix + "/schemas/{4}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseSchemaGUID);
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseSchema(String userId,
                                     String databaseManagerGUID,
                                     String databaseManagerName,
                                     String databaseSchemaGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "removeDatabaseSchema";
        final String elementGUIDParameterName    = "databaseSchemaGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseSchemaGUID,
                                        qualifiedName);
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/by-search-string/{2}?startFrom={3}&pageSize={4}";

        DatabaseSchemasResponse restResult = restClient.callDatabaseSchemasGetRESTCall(methodName,
                                                                                       urlTemplate,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/{2}/schemas?startFrom={3}&pageSize={4}";

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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/by-name/{2}?startFrom={3}&pageSize={4}";

        DatabaseSchemasResponse restResult = restClient.callDatabaseSchemasGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       name,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/{2}";

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
     * Create a new metadata element to represent a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
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
                                      String                  databaseSchemaGUID,
                                      DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                     = "createDatabaseTable";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName        = "databaseTableProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseTableProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}/tables";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  databaseTableProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseSchemaGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
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
    @Override
    public String createDatabaseTableFromTemplate(String             userId,
                                                  String             databaseManagerGUID,
                                                  String             databaseManagerName,
                                                  String             templateGUID,
                                                  String             databaseSchemaGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                     = "createDatabaseTableFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}/tables/from-template/{5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseSchemaGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the database table to update
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
                                    DatabaseTableProperties databaseTableProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName               = "updateDatabaseTable";
        final String elementGUIDParameterName = "databaseTableGUID";
        final String propertiesParameterName  = "databaseTableProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseTableProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseTableProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseTableGUID);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseTable(String userId,
                                    String databaseManagerGUID,
                                    String databaseManagerName,
                                    String databaseTableGUID,
                                    String qualifiedName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "removeDatabaseTable";
        final String elementGUIDParameterName    = "databaseTableGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/{4}/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseTableGUID,
                                        qualifiedName);
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/by-search-string/{2}?startFrom={3}&pageSize={4}";

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     searchString,
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
        final String methodName                     = "getTablesForDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/{2}/tables?startFrom={3}&pageSize={4}";

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     databaseSchemaGUID,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/by-name/{2}?startFrom={3}&pageSize={4}";

        DatabaseTablesResponse restResult = restClient.callDatabaseTablesGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     name,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/{2}";

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
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
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
                                     String                 databaseSchemaGUID,
                                     DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "createDatabaseView";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName        = "databaseViewProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseViewProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}/tables/views";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  databaseViewProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseSchemaGUID);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
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
    @Override
    public String createDatabaseViewFromTemplate(String             userId,
                                                 String             databaseManagerGUID,
                                                 String             databaseManagerName,
                                                 String             templateGUID,
                                                 String             databaseSchemaGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                     = "createDatabaseViewFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/{4}/tables/views/from-template/{5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseSchemaGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the database view to update
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
                                   DatabaseViewProperties databaseViewProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName               = "updateDatabaseView";
        final String elementGUIDParameterName = "databaseViewGUID";
        final String propertiesParameterName  = "databaseViewProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseViewProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/views/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseViewProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseViewGUID);
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseView(String userId,
                                   String databaseManagerGUID,
                                   String databaseManagerName,
                                   String databaseViewGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                  = "removeDatabaseView";
        final String elementGUIDParameterName    = "databaseViewGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseViewGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/views/{4}/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseViewGUID,
                                        qualifiedName);
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/views/by-search-string/{2}?startFrom={3}&pageSize={4}";

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   searchString,
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
        final String methodName                     = "getViewsForDatabaseSchema";
        final String parentElementGUIDParameterName = "databaseSchemaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseSchemaGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/{2}/tables/views?startFrom={3}&pageSize={4}";

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   databaseSchemaGUID,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/views/by-name/{2}?startFrom={3}&pageSize={4}";

        DatabaseViewsResponse restResult = restClient.callDatabaseViewsGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   name,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/views/{2}";

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
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseColumnProperties, propertiesParameterName, methodName);

        if (databaseColumnProperties.getExternalTypeGUID() == null)
        {
            invalidParameterHandler.validateName(databaseColumnProperties.getDataType(), dataTypeParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/{4}/columns";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  databaseColumnProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseTableGUID);

        return restResult.getGUID();
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
        final String methodName                     = "createDatabaseColumnFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "databaseTableGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseTableGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/{4}/columns/from-template/{5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  databaseManagerGUID,
                                                                  databaseManagerName,
                                                                  databaseTableGUID,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to update
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
                                     DatabaseColumnProperties databaseColumnProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName               = "updateDatabaseColumn";
        final String elementGUIDParameterName = "databaseColumnGUID";
        final String propertiesParameterName  = "databaseColumnProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseColumnProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseColumnProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseColumnGUID);
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDatabaseColumn(String userId,
                                     String databaseManagerGUID,
                                     String databaseManagerName,
                                     String databaseColumnGUID,
                                     String qualifiedName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "removeDatabaseColumn";
        final String elementGUIDParameterName    = "databaseColumnGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseColumnGUID,
                                        qualifiedName);
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/columns/by-search-string/{2}?startFrom={3}&pageSize={4}";

        DatabaseColumnsResponse restResult = restClient.callDatabaseColumnsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       searchString,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/{2}/columns?startFrom={3}&pageSize={4}";

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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/columns/by-name/{2}?startFrom={3}&pageSize={4}";

        DatabaseColumnsResponse restResult = restClient.callDatabaseColumnsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       name,
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

        final String urlTemplate = serverPlatformURLRoot + retrieveURLTemplatePrefix + "/schemas/tables/columns/{2}";

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
     * in this column and it can be used to uniquely identify the column.
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
        final String methodName                     = "setPrimaryKeyOnColumn";
        final String parentElementGUIDParameterName = "databaseColumnGUID";
        final String propertiesParameterName        = "databasePrimaryKeyProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databasePrimaryKeyProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}/primary-key";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databasePrimaryKeyProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseColumnGUID);
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
        final String methodName                     = "removePrimaryKeyFromColumn";
        final String parentElementGUIDParameterName = "databaseColumnGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(databaseColumnGUID, parentElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}/primary-key/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseColumnGUID);
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
        final String propertiesParameterName         = "databaseForeignKeyProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(databaseForeignKeyProperties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}/foreign-key/{5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        databaseForeignKeyProperties,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        foreignKeyColumnGUID,
                                        primaryKeyColumnGUID);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(databaseManagerGUID, databaseManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerName, databaseManagerNameParameterName, methodName);
        invalidParameterHandler.validateGUID(primaryKeyColumnGUID, primaryElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyColumnGUID, foreignElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + editURLTemplatePrefix + "/schemas/tables/columns/{4}/foreign-key/{5}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        foreignKeyColumnGUID,
                                        primaryKeyColumnGUID);
    }
}
