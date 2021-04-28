/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;
import java.util.List;


/**
 * DatabaseManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for relational databases.  It matches the DatabaseManagerClient.
 */
public class DatabaseManagerRESTServices
{
    private static DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(DatabaseManagerRESTServices.class),
                                                                                   instanceHandler.getServiceName());

    private RESTExceptionHandler     restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DatabaseManagerRESTServices()
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
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabase(String             serverName,
                                       String             userId,
                                       String             databaseManagerGUID,
                                       String             databaseManagerName,
                                       DatabaseProperties databaseProperties)
    {
        final String methodName = "createDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

            if (databaseProperties.getOwnerCategory() != null)
            {
                ownerCategory = databaseProperties.getOwnerCategory().getOpenTypeOrdinal();
            }
            String databaseGUID = handler.createDatabase(userId,
                                                         databaseManagerGUID,
                                                         databaseManagerName,
                                                         databaseProperties.getQualifiedName(),
                                                         databaseProperties.getDisplayName(),
                                                         databaseProperties.getDescription(),
                                                         databaseProperties.getOwner(),
                                                         ownerCategory,
                                                         databaseProperties.getZoneMembership(),
                                                         databaseProperties.getOriginOrganizationGUID(),
                                                         databaseProperties.getOriginBusinessCapabilityGUID(),
                                                         databaseProperties.getOtherOriginValues(),
                                                         databaseProperties.getPathName(),
                                                         databaseProperties.getCreateTime(),
                                                         databaseProperties.getModifiedTime(),
                                                         databaseProperties.getEncodingType(),
                                                         databaseProperties.getEncodingLanguage(),
                                                         databaseProperties.getEncodingDescription(),
                                                         databaseProperties.getEncodingProperties(),
                                                         databaseProperties.getDatabaseType(),
                                                         databaseProperties.getDatabaseVersion(),
                                                         databaseProperties.getDatabaseInstance(),
                                                         databaseProperties.getDatabaseImportedFrom(),
                                                         databaseProperties.getAdditionalProperties(),
                                                         databaseProperties.getTypeName(),
                                                         databaseProperties.getExtendedProperties(),
                                                         databaseProperties.getVendorProperties(),
                                                         methodName);

            response.setGUID(databaseGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseFromTemplate(String             serverName,
                                                   String             userId,
                                                   String             databaseManagerGUID,
                                                   String             databaseManagerName,
                                                   String             templateGUID,
                                                   TemplateProperties templateProperties)
    {
        final String methodName = "createDatabaseFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseFromTemplate(userId,
                                                                databaseManagerGUID,
                                                                databaseManagerName,
                                                                templateGUID,
                                                                templateProperties.getQualifiedName(),
                                                                templateProperties.getDisplayName(),
                                                                templateProperties.getDescription(),
                                                                templateProperties.getNetworkAddress(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to update
     * @param databaseProperties new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDatabase(String             serverName,
                                       String             userId,
                                       String             databaseManagerGUID,
                                       String             databaseManagerName,
                                       String             databaseGUID,
                                       DatabaseProperties databaseProperties)
    {
        final String methodName = "updateDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

            if (databaseProperties.getOwnerCategory() != null)
            {
                ownerCategory = databaseProperties.getOwnerCategory().getOpenTypeOrdinal();
            }

            handler.updateDatabase(userId,
                                   databaseManagerGUID,
                                   databaseManagerName,
                                   databaseGUID,
                                   databaseProperties.getQualifiedName(),
                                   databaseProperties.getDisplayName(),
                                   databaseProperties.getDescription(),
                                   databaseProperties.getOwner(),
                                   ownerCategory,
                                   databaseProperties.getZoneMembership(),
                                   databaseProperties.getOriginOrganizationGUID(),
                                   databaseProperties.getOriginBusinessCapabilityGUID(),
                                   databaseProperties.getOtherOriginValues(),
                                   databaseProperties.getCreateTime(),
                                   databaseProperties.getModifiedTime(),
                                   databaseProperties.getEncodingType(),
                                   databaseProperties.getEncodingLanguage(),
                                   databaseProperties.getEncodingDescription(),
                                   databaseProperties.getEncodingProperties(),
                                   databaseProperties.getDatabaseType(),
                                   databaseProperties.getDatabaseVersion(),
                                   databaseProperties.getDatabaseInstance(),
                                   databaseProperties.getDatabaseImportedFrom(),
                                   databaseProperties.getAdditionalProperties(),
                                   databaseProperties.getTypeName(),
                                   databaseProperties.getExtendedProperties(),
                                   databaseProperties.getVendorProperties(),
                                   methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabase(String          serverName,
                                        String          userId,
                                        String          databaseGUID,
                                        NullRequestBody nullRequestBody)
    {
        final String methodName = "publishDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.publishDatabase(userId, databaseGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabase(String          serverName,
                                         String          userId,
                                         String          databaseGUID,
                                         NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.withdrawDatabase(userId, databaseGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabase(String          serverName,
                                       String          userId,
                                       String          databaseManagerGUID,
                                       String          databaseManagerName,
                                       String          databaseGUID,
                                       String          qualifiedName,
                                       NullRequestBody nullRequestBody)
    {
        final String methodName = "removeDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabase(userId, databaseManagerGUID, databaseManagerName, databaseGUID, qualifiedName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabasesResponse findDatabases(String serverName,
                                           String userId,
                                           String searchString,
                                           int    startFrom,
                                           int    pageSize)
    {
        final String methodName = "findDatabases";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseElement> databaseAssets = handler.findDatabases(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(databaseAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabasesResponse   getDatabasesByName(String serverName,
                                                  String userId,
                                                  String name,
                                                  int    startFrom,
                                                  int    pageSize)
    {
        final String methodName = "getDatabasesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseElement> databaseAssets = handler.getDatabasesByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(databaseAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of databases created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabasesResponse   getDatabasesForDatabaseManager(String serverName,
                                                              String userId,
                                                              String databaseManagerGUID,
                                                              String databaseManagerName,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "getDatabasesForDatabaseManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseElement> databaseAssets = handler.getDatabasesForDatabaseManager(userId,
                                                                                          databaseManagerGUID,
                                                                                          databaseManagerName,
                                                                                          startFrom,
                                                                                          pageSize,
                                                                                          methodName);

            response.setElementList(databaseAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseResponse getDatabaseByGUID(String serverName,
                                              String userId,
                                              String guid)
    {
        final String methodName = "getDatabaseByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseResponse response = new DatabaseResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            DatabaseElement databaseAsset = handler.getDatabaseByGUID(userId, guid, methodName);

            response.setElement(databaseAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ============================================================================
     * A database may host one or more database schemas depending on its capability
     */

    /**
     * Create a new metadata element to represent a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             String                   databaseManagerGUID,
                                             String                   databaseManagerName,
                                             String                   databaseGUID,
                                             DatabaseSchemaProperties databaseSchemaProperties)
    {
        final String methodName = "createDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

            if (databaseSchemaProperties.getOwnerCategory() != null)
            {
                ownerCategory = databaseSchemaProperties.getOwnerCategory().getOpenTypeOrdinal();
            }
            String databaseSchemaGUID = handler.createDatabaseSchema(userId,
                                                                     databaseManagerGUID,
                                                                     databaseManagerName,
                                                                     databaseGUID,
                                                                     databaseSchemaProperties.getQualifiedName(),
                                                                     databaseSchemaProperties.getDisplayName(),
                                                                     databaseSchemaProperties.getDescription(),
                                                                     databaseSchemaProperties.getOwner(),
                                                                     ownerCategory,
                                                                     databaseSchemaProperties.getZoneMembership(),
                                                                     databaseSchemaProperties.getOriginOrganizationGUID(),
                                                                     databaseSchemaProperties.getOriginBusinessCapabilityGUID(),
                                                                     databaseSchemaProperties.getOtherOriginValues(),
                                                                     databaseSchemaProperties.getAdditionalProperties(),
                                                                     databaseSchemaProperties.getTypeName(),
                                                                     databaseSchemaProperties.getExtendedProperties(),
                                                                     databaseSchemaProperties.getVendorProperties(),
                                                                     methodName);

            response.setGUID(databaseSchemaGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database schema using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseGUID unique identifier of the database where the schema is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseSchemaFromTemplate(String             serverName,
                                                         String             userId,
                                                         String             databaseManagerGUID,
                                                         String             databaseManagerName,
                                                         String             templateGUID,
                                                         String             databaseGUID,
                                                         TemplateProperties templateProperties)
    {
        final String methodName = "createDatabaseSchemaFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseSchemaFromTemplate(userId,
                                                                      databaseManagerGUID,
                                                                      databaseManagerName,
                                                                      templateGUID,
                                                                      databaseGUID,
                                                                      templateProperties.getQualifiedName(),
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to update
     * @param databaseSchemaProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             String                   databaseManagerGUID,
                                             String                   databaseManagerName,
                                             String                   databaseSchemaGUID,
                                             DatabaseSchemaProperties databaseSchemaProperties)
    {
        final String methodName = "updateDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

            if (databaseSchemaProperties.getOwnerCategory() != null)
            {
                ownerCategory = databaseSchemaProperties.getOwnerCategory().getOpenTypeOrdinal();
            }
            handler.updateDatabaseSchema(userId,
                                         databaseManagerGUID,
                                         databaseManagerName,
                                         databaseSchemaGUID,
                                         databaseSchemaProperties.getQualifiedName(),
                                         databaseSchemaProperties.getDisplayName(),
                                         databaseSchemaProperties.getDescription(),
                                         databaseSchemaProperties.getOwner(),
                                         ownerCategory,
                                         databaseSchemaProperties.getZoneMembership(),
                                         databaseSchemaProperties.getOriginOrganizationGUID(),
                                         databaseSchemaProperties.getOriginBusinessCapabilityGUID(),
                                         databaseSchemaProperties.getOtherOriginValues(),
                                         databaseSchemaProperties.getAdditionalProperties(),
                                         databaseSchemaProperties.getTypeName(),
                                         databaseSchemaProperties.getExtendedProperties(),
                                         databaseSchemaProperties.getVendorProperties(),
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabaseSchema(String          serverName,
                                              String          userId,
                                              String          databaseSchemaGUID,
                                              NullRequestBody nullRequestBody)
    {
        final String methodName = "publishDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.publishDatabaseSchema(userId, databaseSchemaGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabaseSchema(String          serverName,
                                               String          userId,
                                               String          databaseSchemaGUID,
                                               NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.withdrawDatabaseSchema(userId, databaseSchemaGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseSchema(String          serverName,
                                             String          userId,
                                             String          databaseManagerGUID,
                                             String          databaseManagerName,
                                             String          databaseSchemaGUID,
                                             String          qualifiedName,
                                             NullRequestBody nullRequestBody)
    {
        final String methodName = "removeDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseSchema(userId, databaseManagerGUID, databaseManagerName, databaseSchemaGUID, qualifiedName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseSchemasResponse   findDatabaseSchemas(String serverName,
                                                         String userId,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "findDatabaseSchemas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemasResponse response = new DatabaseSchemasResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseSchemaElement> databaseSchemaAssets = handler.findDatabaseSchemas(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(databaseSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseSchemasResponse   getSchemasForDatabase(String serverName,
                                                           String userId,
                                                           String databaseGUID,
                                                           int    startFrom,
                                                           int    pageSize)
    {
        final String methodName = "getSchemasForDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemasResponse response = new DatabaseSchemasResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseSchemaElement> databaseSchemaAssets = handler.getSchemasForDatabase(userId, databaseGUID, startFrom, pageSize, methodName);

            response.setElementList(databaseSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseSchemasResponse   getDatabaseSchemasByName(String serverName,
                                                              String userId,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "getDatabaseSchemasByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemasResponse response = new DatabaseSchemasResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseSchemaElement> databaseSchemaAssets = handler.getDatabaseSchemasByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(databaseSchemaAssets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseSchemaResponse getDatabaseSchemaByGUID(String serverName,
                                                          String userId,
                                                          String guid)
    {
        final String methodName = "getDatabaseSchemaByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemaResponse response = new DatabaseSchemaResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            DatabaseSchemaElement databaseSchemaAsset = handler.getDatabaseSchemaByGUID(userId, guid, methodName);

            response.setElement(databaseSchemaAsset);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ==========================================================================
     * A database schema may contain multiple database tables and database views.
     */

    /**
     * Create a new metadata element to represent a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the database schema ASSET where the database table is located.
     * @param databaseTableProperties properties for the database table
     *
     * @return unique identifier of the new metadata element for the database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseTable(String                  serverName,
                                            String                  userId,
                                            String                  databaseManagerGUID,
                                            String                  databaseManagerName,
                                            String                  databaseSchemaGUID,
                                            DatabaseTableProperties databaseTableProperties)
    {
        final String methodName = "createDatabaseTable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseTableGUID = handler.createDatabaseTable(userId,
                                                                   databaseManagerGUID,
                                                                   databaseManagerName,
                                                                   databaseSchemaGUID,
                                                                   databaseTableProperties.getQualifiedName(),
                                                                   databaseTableProperties.getDisplayName(),
                                                                   databaseTableProperties.getDescription(),
                                                                   databaseTableProperties.getIsDeprecated(),
                                                                   databaseTableProperties.getAliases(),
                                                                   databaseTableProperties.getAdditionalProperties(),
                                                                   databaseTableProperties.getTypeName(),
                                                                   databaseTableProperties.getExtendedProperties(),
                                                                   databaseTableProperties.getVendorProperties(),
                                                                   methodName);

            response.setGUID(databaseTableGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database table is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseTableFromTemplate(String             serverName,
                                                        String             userId,
                                                        String             databaseManagerGUID,
                                                        String             databaseManagerName,
                                                        String             templateGUID,
                                                        String             databaseSchemaGUID,
                                                        TemplateProperties templateProperties)
    {
        final String methodName = "createDatabaseTableFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseTableFromTemplate(userId,
                                                                databaseManagerGUID,
                                                                databaseManagerName,
                                                                templateGUID,
                                                                databaseSchemaGUID,
                                                                templateProperties.getQualifiedName(),
                                                                templateProperties.getDisplayName(),
                                                                templateProperties.getDescription(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the database table to update
     * @param databaseTableProperties new properties for the database table
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDatabaseTable(String                  serverName,
                                            String                  userId,
                                            String                  databaseManagerGUID,
                                            String                  databaseManagerName,
                                            String                  databaseTableGUID,
                                            DatabaseTableProperties databaseTableProperties)
    {
        final String methodName = "updateDatabaseTable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseTable(userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseTableGUID,
                                        databaseTableProperties.getQualifiedName(),
                                        databaseTableProperties.getDisplayName(),
                                        databaseTableProperties.getDescription(),
                                        databaseTableProperties.getIsDeprecated(),
                                        databaseTableProperties.getAliases(),
                                        databaseTableProperties.getAdditionalProperties(),
                                        databaseTableProperties.getTypeName(),
                                        databaseTableProperties.getExtendedProperties(),
                                        databaseTableProperties.getVendorProperties(),
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseTable(String          serverName,
                                            String          userId,
                                            String          databaseManagerGUID,
                                            String          databaseManagerName,
                                            String          databaseTableGUID,
                                            String          qualifiedName,
                                            NullRequestBody nullRequestBody)
    {
        final String methodName                  = "removeDatabaseTable";
        final String elementGUIDParameterName    = "databaseTableGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseTable(userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseTableGUID,
                                        elementGUIDParameterName,
                                        qualifiedName,
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseTablesResponse   findDatabaseTables(String serverName,
                                                       String userId,
                                                       String searchString,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName = "findDatabaseTables";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTablesResponse response = new DatabaseTablesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseTableElement> databaseTableAttributes = handler.findDatabaseTables(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(databaseTableAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseTablesResponse    getTablesForDatabaseSchema(String serverName,
                                                                String userId,
                                                                String databaseSchemaGUID,
                                                                int    startFrom,
                                                                int    pageSize)
    {
        final String methodName = "getTablesForDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTablesResponse response = new DatabaseTablesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseTableElement> databaseTableAttributes = handler.getTablesForDatabaseSchema(userId,
                                                                                                    databaseSchemaGUID,
                                                                                                    startFrom,
                                                                                                    pageSize,
                                                                                                    methodName);

            response.setElementList(databaseTableAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseTablesResponse   getDatabaseTablesByName(String serverName,
                                                            String userId,
                                                            String name,
                                                            int    startFrom,
                                                            int    pageSize)
    {
        final String methodName = "getDatabaseTablesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTablesResponse response = new DatabaseTablesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseTableElement> databaseTableAttributes = handler.getDatabaseTablesByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(databaseTableAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseTableResponse getDatabaseTableByGUID(String serverName,
                                                        String userId,
                                                        String guid)
    {
        final String methodName = "getDatabaseTableByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTableResponse response = new DatabaseTableResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            DatabaseTableElement databaseTableAttribute = handler.getDatabaseTableByGUID(userId, guid, methodName);

            response.setElement(databaseTableAttribute);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database view.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseView(String                 serverName,
                                           String                 userId,
                                           String                 databaseManagerGUID,
                                           String                 databaseManagerName,
                                           String                 databaseSchemaGUID,
                                           DatabaseViewProperties databaseViewProperties)
    {
        final String methodName = "createDatabaseView";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseViewGUID = handler.createDatabaseView(userId,
                                                                 databaseManagerGUID,
                                                                 databaseManagerName,
                                                                 databaseSchemaGUID,
                                                                 databaseViewProperties.getQualifiedName(),
                                                                 databaseViewProperties.getDisplayName(),
                                                                 databaseViewProperties.getDescription(),
                                                                 databaseViewProperties.getIsDeprecated(),
                                                                 databaseViewProperties.getAliases(),
                                                                 databaseViewProperties.getFormula(),
                                                                 databaseViewProperties.getAdditionalProperties(),
                                                                 databaseViewProperties.getTypeName(),
                                                                 databaseViewProperties.getExtendedProperties(),
                                                                 databaseViewProperties.getVendorProperties(),
                                                                 methodName);

            response.setGUID(databaseViewGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database view using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseSchemaGUID unique identifier of the database schema where the database view is located.
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseViewFromTemplate(String             serverName,
                                                       String             userId,
                                                       String             databaseManagerGUID,
                                                       String             databaseManagerName,
                                                       String             templateGUID,
                                                       String             databaseSchemaGUID,
                                                       TemplateProperties templateProperties)
    {
        final String methodName = "createDatabaseViewFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseViewFromTemplate(userId,
                                                                     databaseManagerGUID,
                                                                     databaseManagerName,
                                                                     templateGUID,
                                                                     databaseSchemaGUID,
                                                                     templateProperties.getQualifiedName(),
                                                                     templateProperties.getDisplayName(),
                                                                     templateProperties.getDescription(),
                                                                     methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the database view to update
     * @param databaseViewProperties properties for the new database view
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDatabaseView(String                 serverName,
                                           String                 userId,
                                           String                 databaseManagerGUID,
                                           String                 databaseManagerName,
                                           String                 databaseViewGUID,
                                           DatabaseViewProperties databaseViewProperties)
    {
        final String methodName = "updateDatabaseView";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseView(userId,
                                       databaseManagerGUID,
                                       databaseManagerName,
                                       databaseViewGUID,
                                       databaseViewProperties.getQualifiedName(),
                                       databaseViewProperties.getDisplayName(),
                                       databaseViewProperties.getDescription(),
                                       databaseViewProperties.getIsDeprecated(),
                                       databaseViewProperties.getAliases(),
                                       databaseViewProperties.getFormula(),
                                       databaseViewProperties.getAdditionalProperties(),
                                       databaseViewProperties.getTypeName(),
                                       databaseViewProperties.getExtendedProperties(),
                                       databaseViewProperties.getVendorProperties(),
                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseViewGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseView(String          serverName,
                                           String          userId,
                                           String          databaseManagerGUID,
                                           String          databaseManagerName,
                                           String          databaseViewGUID,
                                           String          qualifiedName,
                                           NullRequestBody nullRequestBody)
    {
        final String methodName                  = "removeDatabaseView";
        final String elementGUIDParameterName    = "databaseViewGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseTable(userId,
                                        databaseManagerGUID,
                                        databaseManagerName,
                                        databaseViewGUID,
                                        elementGUIDParameterName,
                                        qualifiedName,
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseViewsResponse   findDatabaseViews(String serverName,
                                                     String userId,
                                                     String searchString,
                                                     int    startFrom,
                                                     int    pageSize)
    {
        final String methodName = "findDatabaseViews";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewsResponse response = new DatabaseViewsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseViewElement> databaseViewAttributes = handler.findDatabaseViews(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(databaseViewAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseViewsResponse    getViewsForDatabaseSchema(String serverName,
                                                              String userId,
                                                              String databaseSchemaGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "getViewsForDatabaseSchema";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewsResponse response = new DatabaseViewsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseViewElement> databaseViewAttributes = handler.getViewsForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize, methodName);

            response.setElementList(databaseViewAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseViewsResponse   getDatabaseViewsByName(String serverName,
                                                          String userId,
                                                          String name,
                                                          int    startFrom,
                                                          int    pageSize)
    {
        final String methodName = "getDatabaseViewsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewsResponse response = new DatabaseViewsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseViewElement> databaseViewAttributes = handler.getDatabaseViewsByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(databaseViewAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseViewResponse getDatabaseViewByGUID(String serverName,
                                                      String userId,
                                                      String guid)
    {
        final String methodName = "getDatabaseViewByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewResponse response = new DatabaseViewResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            DatabaseViewElement databaseViewAttribute = handler.getDatabaseViewByGUID(userId, guid, methodName);

            response.setElement(databaseViewAttribute);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             String                   databaseManagerGUID,
                                             String                   databaseManagerName,
                                             String                   databaseTableGUID,
                                             DatabaseColumnProperties databaseColumnProperties)
    {
        final String methodName = "createDatabaseColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

            if (databaseColumnProperties.getSortOrder() != null)
            {
                sortOrder = databaseColumnProperties.getSortOrder().getOpenTypeOrdinal();
            }

            String databaseColumnGUID = handler.createDatabaseColumn(userId,
                                                                     databaseManagerGUID,
                                                                     databaseManagerName,
                                                                     databaseTableGUID,
                                                                     databaseColumnProperties.getQualifiedName(),
                                                                     databaseColumnProperties.getDisplayName(),
                                                                     databaseColumnProperties.getDescription(),
                                                                     databaseColumnProperties.getExternalTypeGUID(),
                                                                     databaseColumnProperties.getDataType(),
                                                                     databaseColumnProperties.getDefaultValue(),
                                                                     databaseColumnProperties.getFixedValue(),
                                                                     databaseColumnProperties.getValidValuesSetGUID(),
                                                                     databaseColumnProperties.getFormula(),
                                                                     databaseColumnProperties.getIsDeprecated(),
                                                                     databaseColumnProperties.getElementPosition(),
                                                                     databaseColumnProperties.getMinCardinality(),
                                                                     databaseColumnProperties.getMaxCardinality(),
                                                                     databaseColumnProperties.getAllowsDuplicateValues(),
                                                                     databaseColumnProperties.getOrderedValues(),
                                                                     databaseColumnProperties.getDefaultValueOverride(),
                                                                     sortOrder,
                                                                     databaseColumnProperties.getMinimumLength(),
                                                                     databaseColumnProperties.getLength(),
                                                                     databaseColumnProperties.getPrecision(),
                                                                     databaseColumnProperties.getIsNullable(),
                                                                     databaseColumnProperties.getNativeJavaClass(),
                                                                     databaseColumnProperties.getAliases(),
                                                                     databaseColumnProperties.getAdditionalProperties(),
                                                                     databaseColumnProperties.getTypeName(),
                                                                     databaseColumnProperties.getExtendedProperties(),
                                                                     databaseColumnProperties.getVendorProperties(),
                                                                     methodName);

            if ((databaseColumnGUID != null) && (databaseColumnProperties.getQueries() != null))
            {
                for (DatabaseQueryProperties queryProperties : databaseColumnProperties.getQueries())
                {
                    if (queryProperties != null)
                    {
                        handler.createDatabaseColumnQuery(userId,
                                                          databaseManagerGUID,
                                                          databaseManagerName,
                                                          databaseColumnGUID,
                                                          queryProperties.getQueryId(),
                                                          queryProperties.getQuery(),
                                                          queryProperties.getQueryTargetGUID(),
                                                          methodName);
                    }
                }
            }

            response.setGUID(databaseColumnGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database column using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseColumnFromTemplate(String             serverName,
                                                         String             userId,
                                                         String             databaseManagerGUID,
                                                         String             databaseManagerName,
                                                         String             templateGUID,
                                                         String             databaseTableGUID,
                                                         TemplateProperties templateProperties)
    {
        final String methodName = "createDatabaseColumnFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseColumnFromTemplate(userId,
                                                                      databaseManagerGUID,
                                                                      databaseManagerName,
                                                                      templateGUID,
                                                                      databaseTableGUID,
                                                                      templateProperties.getQualifiedName(),
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to update
     * @param databaseColumnProperties new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             String                   databaseManagerGUID,
                                             String                   databaseManagerName,
                                             String                   databaseColumnGUID,
                                             DatabaseColumnProperties databaseColumnProperties)
    {
        final String methodName = "updateDatabaseColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

            if (databaseColumnProperties.getSortOrder() != null)
            {
                sortOrder = databaseColumnProperties.getSortOrder().getOpenTypeOrdinal();
            }

            handler.updateDatabaseColumn(userId,
                                         databaseManagerGUID,
                                         databaseManagerName,
                                         databaseColumnGUID,
                                         databaseColumnProperties.getQualifiedName(),
                                         databaseColumnProperties.getDisplayName(),
                                         databaseColumnProperties.getDescription(),
                                         databaseColumnProperties.getExternalTypeGUID(),
                                         databaseColumnProperties.getDataType(),
                                         databaseColumnProperties.getDefaultValue(),
                                         databaseColumnProperties.getFixedValue(),
                                         databaseColumnProperties.getValidValuesSetGUID(),
                                         databaseColumnProperties.getFormula(),
                                         databaseColumnProperties.getIsDeprecated(),
                                         databaseColumnProperties.getElementPosition(),
                                         databaseColumnProperties.getMinCardinality(),
                                         databaseColumnProperties.getMaxCardinality(),
                                         databaseColumnProperties.getAllowsDuplicateValues(),
                                         databaseColumnProperties.getOrderedValues(),
                                         databaseColumnProperties.getDefaultValueOverride(),
                                         sortOrder,
                                         databaseColumnProperties.getMinimumLength(),
                                         databaseColumnProperties.getLength(),
                                         databaseColumnProperties.getPrecision(),
                                         databaseColumnProperties.getIsNullable(),
                                         databaseColumnProperties.getNativeJavaClass(),
                                         databaseColumnProperties.getAliases(),
                                         databaseColumnProperties.getAdditionalProperties(),
                                         databaseColumnProperties.getTypeName(),
                                         databaseColumnProperties.getExtendedProperties(),
                                         databaseColumnProperties.getVendorProperties(),
                                         methodName);

            if (databaseColumnProperties.getQueries() != null)
            {
                for (DatabaseQueryProperties queryProperties : databaseColumnProperties.getQueries())
                {
                    if (queryProperties != null)
                    {
                        handler.createDatabaseColumnQuery(userId,
                                                          databaseManagerGUID,
                                                          databaseManagerName,
                                                          databaseColumnGUID,
                                                          queryProperties.getQueryId(),
                                                          queryProperties.getQuery(),
                                                          queryProperties.getQueryTargetGUID(),
                                                          methodName);
                    }
                }
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseColumn(String          serverName,
                                             String          userId,
                                             String          databaseManagerGUID,
                                             String          databaseManagerName,
                                             String          databaseColumnGUID,
                                             String          qualifiedName,
                                             NullRequestBody nullRequestBody)
    {
        final String methodName = "removeDatabaseColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, qualifiedName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseColumnsResponse   findDatabaseColumns(String serverName,
                                                         String userId,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "findDatabaseColumns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnsResponse response = new DatabaseColumnsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseColumnElement> databaseColumnAttributes = handler.findDatabaseColumns(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(databaseColumnAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseColumnsResponse    getColumnsForDatabaseTable(String serverName,
                                                                 String userId,
                                                                 String databaseTableGUID,
                                                                 int    startFrom,
                                                                 int    pageSize)
    {
        final String methodName = "getColumnsForDatabaseTable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnsResponse response = new DatabaseColumnsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseColumnElement> databaseColumnAttributes = handler.getColumnsForDatabaseTable(userId,
                                                                                                      databaseTableGUID,
                                                                                                      startFrom,
                                                                                                      pageSize,
                                                                                                      methodName);

            response.setElementList(databaseColumnAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseColumnsResponse   getDatabaseColumnsByName(String serverName,
                                                              String userId,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "getDatabaseColumnsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnsResponse response = new DatabaseColumnsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<DatabaseColumnElement> databaseColumnAttributes = handler.getDatabaseColumnsByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(databaseColumnAttributes);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabaseColumnResponse getDatabaseColumnByGUID(String serverName,
                                                          String userId,
                                                          String guid)
    {
        final String methodName = "getDatabaseColumnByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnResponse response = new DatabaseColumnResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            DatabaseColumnElement schemaAttribute = handler.getDatabaseColumnByGUID(userId, guid, methodName);

            response.setElement(schemaAttribute);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param databasePrimaryKeyProperties properties to store
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setPrimaryKeyOnColumn(String                       serverName,
                                              String                       userId,
                                              String                       databaseManagerGUID,
                                              String                       databaseManagerName,
                                              String                       databaseColumnGUID,
                                              DatabasePrimaryKeyProperties databasePrimaryKeyProperties)
    {
        final String methodName = "setPrimaryKeyOnColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            int keyPattern = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();

            if (databasePrimaryKeyProperties.getKeyPattern() != null)
            {
                keyPattern = databasePrimaryKeyProperties.getKeyPattern().getOpenTypeOrdinal();
            }

            handler.setPrimaryKeyOnColumn(userId,
                                          databaseManagerGUID,
                                          databaseManagerName,
                                          databaseColumnGUID,
                                          databasePrimaryKeyProperties.getName(),
                                          keyPattern,
                                          methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the classification that this column is a primary key.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param databaseColumnGUID unique identifier if the primary key column
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removePrimaryKeyFromColumn(String          serverName,
                                                   String          userId,
                                                   String          databaseManagerGUID,
                                                   String          databaseManagerName,
                                                   String          databaseColumnGUID,
                                                   NullRequestBody nullRequestBody)
    {
        final String methodName = "removePrimaryKeyFromColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removePrimaryKeyFromColumn(userId, databaseManagerGUID, databaseManagerName, databaseColumnGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a foreign relationship between two columns.  One of the columns holds the primary key of the other
     * to form a link.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param primaryKeyColumnGUID unique identifier of the column containing the primary key
     * @param foreignKeyColumnGUID unique identifier of the column containing the primary key from the other table
     * @param databaseForeignKeyProperties properties about the foreign key relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse addForeignKeyRelationship(String                       serverName,
                                                  String                       userId,
                                                  String                       databaseManagerGUID,
                                                  String                       databaseManagerName,
                                                  String                       primaryKeyColumnGUID,
                                                  String                       foreignKeyColumnGUID,
                                                  DatabaseForeignKeyProperties databaseForeignKeyProperties)
    {
        final String methodName = "addForeignKeyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.addForeignKeyRelationship(userId,
                                              databaseManagerGUID,
                                              databaseManagerName,
                                              primaryKeyColumnGUID,
                                              foreignKeyColumnGUID,
                                              databaseForeignKeyProperties.getName(),
                                              databaseForeignKeyProperties.getDescription(),
                                              databaseForeignKeyProperties.getConfidence(),
                                              databaseForeignKeyProperties.getSteward(),
                                              databaseForeignKeyProperties.getSource(),
                                              methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the foreign key relationship for the requested columns.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseManagerGUID unique identifier of software server capability representing the DBMS
     * @param databaseManagerName unique name of software server capability representing the DBMS
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeForeignKeyRelationship(String          serverName,
                                                     String          userId,
                                                     String          databaseManagerGUID,
                                                     String          databaseManagerName,
                                                     String          primaryKeyColumnGUID,
                                                     String          foreignKeyColumnGUID,
                                                     NullRequestBody nullRequestBody)
    {
        final String methodName = "removeForeignKeyRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler<DatabaseElement,
                    DatabaseSchemaElement,
                    DatabaseTableElement,
                    DatabaseViewElement,
                    DatabaseColumnElement,
                    SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeForeignKeyRelationship(userId, databaseManagerGUID, databaseManagerName, primaryKeyColumnGUID, foreignKeyColumnGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
