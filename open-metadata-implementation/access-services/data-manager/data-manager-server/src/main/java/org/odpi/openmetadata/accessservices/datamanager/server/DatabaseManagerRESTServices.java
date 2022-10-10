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
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * DatabaseManagerRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for relational databases.  It matches the DatabaseManagerClient.
 */
public class DatabaseManagerRESTServices
{
    private static final DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static final RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(DatabaseManagerRESTServices.class),
                                                                                         instanceHandler.getServiceName());

    private final RESTExceptionHandler     restExceptionHandler = new RESTExceptionHandler();

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
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabase(String                   serverName,
                                       String                   userId,
                                       ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseProperties)
                {
                    DatabaseProperties databaseProperties = (DatabaseProperties)requestBody.getProperties();

                    String name = databaseProperties.getName();

                    if (name == null)
                    {
                        name = databaseProperties.getDisplayName();
                    }
                    String databaseGUID = handler.createDatabase(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
                                                                 databaseProperties.getQualifiedName(),
                                                                 name,
                                                                 databaseProperties.getVersionIdentifier(),
                                                                 databaseProperties.getDescription(),
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
                                                                 databaseProperties.getEffectiveFrom(),
                                                                 databaseProperties.getEffectiveTo(),
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);

                    response.setGUID(databaseGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseFromTemplate(String              serverName,
                                                   String              userId,
                                                   String              templateGUID,
                                                   TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createDatabaseFromTemplate(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    templateGUID,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getVersionIdentifier(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getPathName(),
                                                                    requestBody.getNetworkAddress(),
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse updateDatabase(String                   serverName,
                                       String                   userId,
                                       String                   databaseGUID,
                                       boolean                  isMergeUpdate,
                                       ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseProperties)
                {
                    DatabaseProperties databaseProperties = (DatabaseProperties) requestBody.getProperties();

                    handler.updateDatabase(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           databaseGUID,
                                           databaseProperties.getQualifiedName(),
                                           databaseProperties.getDisplayName(),
                                           databaseProperties.getVersionIdentifier(),
                                           databaseProperties.getDescription(),
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
                                           databaseProperties.getEffectiveFrom(),
                                           databaseProperties.getEffectiveTo(),
                                           isMergeUpdate,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabase(String                    serverName,
                                        String                    userId,
                                        String                    databaseGUID,
                                        ClassificationRequestBody requestBody)
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

            handler.publishDatabase(userId,
                                    databaseGUID,
                                    false,
                                    false,
                                    new Date(),
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabase(String                    serverName,
                                         String                    userId,
                                         String                    databaseGUID,
                                         ClassificationRequestBody requestBody)
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

            handler.withdrawDatabase(userId,
                                     databaseGUID,
                                     false,
                                     false,
                                     new Date(),
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
     * Remove the metadata element representing a database.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeDatabase(String                    serverName,
                                       String                    userId,
                                       String                    databaseGUID,
                                       ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeDatabase(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       databaseGUID,
                                       null,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
            }
            else
            {
                handler.removeDatabase(userId,
                                       null,
                                       null,
                                       databaseGUID,
                                       null,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
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
    public DatabasesResponse findDatabases(String                  serverName,
                                           String                  userId,
                                           SearchStringRequestBody requestBody,
                                           int                     startFrom,
                                           int                     pageSize)
    {
        final String methodName = "findDatabases";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseElement> databaseAssets = handler.findDatabases(userId,
                                                                             requestBody.getSearchString(),
                                                                             startFrom,
                                                                             pageSize,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName);

                response.setElementList(databaseAssets);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public DatabasesResponse   getDatabasesByName(String          serverName,
                                                  String          userId,
                                                  NameRequestBody requestBody,
                                                  int             startFrom,
                                                  int             pageSize)
    {
        final String methodName = "getDatabasesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseElement> databaseAssets = handler.getDatabasesByName(userId,
                                                                                  requestBody.getName(),
                                                                                  startFrom,
                                                                                  pageSize,
                                                                                  false,
                                                                                  false,
                                                                                  new Date(),
                                                                                  methodName);

                response.setElementList(databaseAssets);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
                                                                                          false,
                                                                                          false,
                                                                                          new Date(),
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

            DatabaseElement databaseAsset = handler.getDatabaseByGUID(userId,
                                                                      guid,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);

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
     * @param requestBody properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseSchemaProperties)
                {
                    DatabaseSchemaProperties databaseSchemaProperties = (DatabaseSchemaProperties) requestBody.getProperties();

                    String databaseSchemaGUID = handler.createDatabaseSchema(userId,
                                                                             requestBody.getExternalSourceGUID(),
                                                                             requestBody.getExternalSourceName(),
                                                                             requestBody.getParentGUID(),
                                                                             databaseSchemaProperties.getQualifiedName(),
                                                                             databaseSchemaProperties.getDisplayName(),
                                                                             databaseSchemaProperties.getVersionIdentifier(),
                                                                             databaseSchemaProperties.getDescription(),
                                                                             databaseSchemaProperties.getAdditionalProperties(),
                                                                             databaseSchemaProperties.getTypeName(),
                                                                             databaseSchemaProperties.getExtendedProperties(),
                                                                             databaseSchemaProperties.getVendorProperties(),
                                                                             databaseSchemaProperties.getEffectiveFrom(),
                                                                             databaseSchemaProperties.getEffectiveTo(),
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName);

                    response.setGUID(databaseSchemaGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseSchemaProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseSchemaFromTemplate(String              serverName,
                                                         String              userId,
                                                         String              templateGUID,
                                                         TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createDatabaseSchemaFromTemplate(userId,
                                                                          requestBody.getExternalSourceGUID(),
                                                                          requestBody.getExternalSourceName(),
                                                                          templateGUID,
                                                                          requestBody.getParentGUID(),
                                                                          requestBody.getQualifiedName(),
                                                                          requestBody.getDisplayName(),
                                                                          requestBody.getVersionIdentifier(),
                                                                          requestBody.getDescription(),
                                                                          null,
                                                                          null,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse updateDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             String                   databaseSchemaGUID,
                                             boolean                  isMergeUpdate,
                                             ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseSchemaProperties)
                {
                    DatabaseSchemaProperties databaseSchemaProperties = (DatabaseSchemaProperties) requestBody.getProperties();

                    handler.updateDatabaseSchema(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 databaseSchemaGUID,
                                                 databaseSchemaProperties.getQualifiedName(),
                                                 databaseSchemaProperties.getDisplayName(),
                                                 databaseSchemaProperties.getVersionIdentifier(),
                                                 databaseSchemaProperties.getDescription(),
                                                 null,
                                                 0,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 databaseSchemaProperties.getAdditionalProperties(),
                                                 databaseSchemaProperties.getTypeName(),
                                                 databaseSchemaProperties.getExtendedProperties(),
                                                 databaseSchemaProperties.getVendorProperties(),
                                                 databaseSchemaProperties.getEffectiveFrom(),
                                                 databaseSchemaProperties.getEffectiveTo(),
                                                 isMergeUpdate,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseSchemaProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabaseSchema(String                    serverName,
                                              String                    userId,
                                              String                    databaseSchemaGUID,
                                              ClassificationRequestBody requestBody)
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

            handler.publishDatabaseSchema(userId,
                                          databaseSchemaGUID,
                                          false,
                                          false,
                                          new Date(),
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabaseSchema(String                    serverName,
                                               String                    userId,
                                               String                    databaseSchemaGUID,
                                               ClassificationRequestBody requestBody)
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

            handler.withdrawDatabaseSchema(userId,
                                           databaseSchemaGUID,
                                           false,
                                           false,
                                           new Date(),
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
    public VoidResponse removeDatabaseSchema(String                    serverName,
                                             String                    userId,
                                             String                    databaseSchemaGUID,
                                             ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeDatabaseSchema(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             databaseSchemaGUID,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                handler.removeDatabaseSchema(userId,
                                             null,
                                             null,
                                             databaseSchemaGUID,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
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
    public DatabaseSchemasResponse   findDatabaseSchemas(String                  serverName,
                                                         String                  userId,
                                                         SearchStringRequestBody requestBody,
                                                         int                     startFrom,
                                                         int                     pageSize)
    {
        final String methodName = "findDatabaseSchemas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemasResponse response = new DatabaseSchemasResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseSchemaElement> databaseSchemaAssets = handler.findDatabaseSchemas(userId,
                                                                                               requestBody.getSearchString(),
                                                                                               startFrom,
                                                                                               pageSize,
                                                                                               false,
                                                                                               false,
                                                                                               new Date(),
                                                                                               methodName);

                response.setElementList(databaseSchemaAssets);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            List<DatabaseSchemaElement> databaseSchemaAssets = handler.getSchemasForDatabase(userId,
                                                                                             databaseGUID,
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             false,
                                                                                             false,
                                                                                             new Date(),
                                                                                             methodName);

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
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseSchemasResponse   getDatabaseSchemasByName(String          serverName,
                                                              String          userId,
                                                              NameRequestBody requestBody,
                                                              int             startFrom,
                                                              int             pageSize)
    {
        final String methodName = "getDatabaseSchemasByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseSchemasResponse response = new DatabaseSchemasResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseSchemaElement> databaseSchemaAssets = handler.getDatabaseSchemasByName(userId,
                                                                                                    requestBody.getName(),
                                                                                                    startFrom,
                                                                                                    pageSize,
                                                                                                    false,
                                                                                                    false,
                                                                                                    new Date(),
                                                                                                    methodName);

                response.setElementList(databaseSchemaAssets);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            DatabaseSchemaElement databaseSchemaAsset = handler.getDatabaseSchemaByGUID(userId,
                                                                                        guid,
                                                                                        false,
                                                                                        false,
                                                                                        new Date(),
                                                                                        methodName);

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
    public GUIDResponse createDatabaseSchemaType(String                   serverName,
                                                 String                   userId,
                                                 ReferenceableRequestBody requestBody)
    {
        final String methodName = "createDatabaseSchemaType";

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
                                         SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId,
                                                                                                               serverName,
                                                                                                               methodName);
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseSchemaTypeProperties)
                {
                    DatabaseSchemaTypeProperties databaseSchemaTypeProperties = (DatabaseSchemaTypeProperties) requestBody.getProperties();

                    String databaseTableGUID = handler.createDatabaseSchemaType(userId,
                                                                                requestBody.getExternalSourceGUID(),
                                                                                requestBody.getExternalSourceName(),
                                                                                databaseSchemaTypeProperties.getQualifiedName(),
                                                                                databaseSchemaTypeProperties.getEffectiveFrom(),
                                                                                databaseSchemaTypeProperties.getEffectiveTo(),
                                                                                new Date(),
                                                                                methodName);

                    response.setGUID(databaseTableGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseSchemaTypeProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Link the schema type and asset.  This is called from outside AssetHandler.  The databaseAssetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the asset to connect the schema to
     * @param schemaTypeGUID identifier for schema Type object
     * @param requestBody external source request body
     * @return void or
     *  InvalidParameterException the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException problem accessing the property server
     */
    public  VoidResponse attachSchemaTypeToDatabaseAsset(String                  serverName,
                                                         String                  userId,
                                                         String                  databaseAssetGUID,
                                                         String                  schemaTypeGUID,
                                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "attachSchemaTypeToDatabaseAsset";

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

            if (requestBody != null)
            {
                handler.attachSchemaTypeToDatabaseAsset(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        databaseAssetGUID,
                                                        schemaTypeGUID,
                                                        null,
                                                        null,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
            }
            else
            {
                handler.attachSchemaTypeToDatabaseAsset(userId,
                                                        null,
                                                        null,
                                                        databaseAssetGUID,
                                                        schemaTypeGUID,
                                                        null,
                                                        null,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
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
    public GUIDResponse createDatabaseTable(String                   serverName,
                                            String                   userId,
                                            ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof  DatabaseTableProperties)
                {
                    DatabaseTableProperties databaseTableProperties = (DatabaseTableProperties) requestBody.getProperties();

                    String databaseTableGUID = handler.createDatabaseTable(userId,
                                                                           requestBody.getExternalSourceGUID(),
                                                                           requestBody.getExternalSourceName(),
                                                                           requestBody.getParentGUID(),
                                                                           databaseTableProperties.getQualifiedName(),
                                                                           databaseTableProperties.getDisplayName(),
                                                                           databaseTableProperties.getDescription(),
                                                                           databaseTableProperties.getIsDeprecated(),
                                                                           databaseTableProperties.getAliases(),
                                                                           databaseTableProperties.getAdditionalProperties(),
                                                                           databaseTableProperties.getTypeName(),
                                                                           databaseTableProperties.getExtendedProperties(),
                                                                           databaseTableProperties.getVendorProperties(),
                                                                           databaseTableProperties.getEffectiveFrom(),
                                                                           databaseTableProperties.getEffectiveTo(),
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);

                    response.setGUID(databaseTableGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseTableProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Create a new metadata element to represent a database table using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseTableFromTemplate(String              serverName,
                                                        String              userId,
                                                        String              templateGUID,
                                                        TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createDatabaseTableFromTemplate(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         templateGUID,
                                                                         requestBody.getParentGUID(),
                                                                         requestBody.getQualifiedName(),
                                                                         requestBody.getDisplayName(),
                                                                         requestBody.getDescription(),
                                                                         null,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseTableForSchemaType(String                   serverName,
                                                         String                   userId,
                                                         ReferenceableRequestBody requestBody)
    {
        final String methodName = "createDatabaseTableForSchemaType";

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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof  DatabaseTableProperties)
                {
                    DatabaseTableProperties databaseTableProperties = (DatabaseTableProperties) requestBody.getProperties();

                    String databaseTableGUID = handler.createDatabaseTableForSchemaType(userId,
                                                                                        requestBody.getExternalSourceGUID(),
                                                                                        requestBody.getExternalSourceName(),
                                                                                        requestBody.getParentGUID(),
                                                                                        databaseTableProperties.getQualifiedName(),
                                                                                        databaseTableProperties.getDisplayName(),
                                                                                        databaseTableProperties.getDescription(),
                                                                                        databaseTableProperties.getIsDeprecated(),
                                                                                        databaseTableProperties.getAliases(),
                                                                                        databaseTableProperties.getAdditionalProperties(),
                                                                                        databaseTableProperties.getTypeName(),
                                                                                        databaseTableProperties.getExtendedProperties(),
                                                                                        databaseTableProperties.getVendorProperties(),
                                                                                        databaseTableProperties.getEffectiveFrom(),
                                                                                        databaseTableProperties.getEffectiveTo(),
                                                                                        false,
                                                                                        false,
                                                                                        new Date(),
                                                                                        methodName);

                    response.setGUID(databaseTableGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseTableProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse updateDatabaseTable(String                   serverName,
                                            String                   userId,
                                            String                   databaseTableGUID,
                                            boolean                  isMergeUpdate,
                                            ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof  DatabaseTableProperties)
                {
                    DatabaseTableProperties databaseTableProperties = (DatabaseTableProperties) requestBody.getProperties();

                    handler.updateDatabaseTable(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
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
                                                databaseTableProperties.getEffectiveFrom(),
                                                databaseTableProperties.getEffectiveTo(),
                                                isMergeUpdate,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseTableProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove the metadata element representing a database table.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseTableGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeDatabaseTable(String                    serverName,
                                            String                    userId,
                                            String                    databaseTableGUID,
                                            ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeDatabaseTable(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            databaseTableGUID,
                                            elementGUIDParameterName,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                handler.removeDatabaseTable(userId,
                                            null,
                                            null,
                                            databaseTableGUID,
                                            elementGUIDParameterName,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
    public DatabaseTablesResponse   findDatabaseTables(String                  serverName,
                                                       String                  userId,
                                                       SearchStringRequestBody requestBody,
                                                       int                     startFrom,
                                                       int                     pageSize)
    {
        final String methodName = "findDatabaseTables";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTablesResponse response = new DatabaseTablesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseTableElement> databaseTableAttributes = handler.findDatabaseTables(userId,
                                                                                                requestBody.getSearchString(),
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                false,
                                                                                                false,
                                                                                                new Date(),
                                                                                                methodName);

                response.setElementList(databaseTableAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Retrieve the list of database tables associated with a database or database schema.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param databaseAssetGUID unique identifier of the database schema of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseTablesResponse getTablesForDatabaseAsset(String serverName,
                                                            String userId,
                                                            String databaseAssetGUID,
                                                            int    startFrom,
                                                            int    pageSize)
    {
        final String methodName = "getTablesForDatabaseAsset";

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

            List<DatabaseTableElement> databaseTableAttributes = handler.getTablesForDatabaseAsset(userId,
                                                                                                   databaseAssetGUID,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   null,
                                                                                                   null,
                                                                                                   false,
                                                                                                   false,
                                                                                                   new Date(),
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
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseTablesResponse   getDatabaseTablesByName(String          serverName,
                                                            String          userId,
                                                            NameRequestBody requestBody,
                                                            int             startFrom,
                                                            int             pageSize)
    {
        final String methodName = "getDatabaseTablesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseTablesResponse response = new DatabaseTablesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseTableElement> databaseTableAttributes = handler.getDatabaseTablesByName(userId,
                                                                                                     requestBody.getName(),
                                                                                                     startFrom,
                                                                                                     pageSize,
                                                                                                     false,
                                                                                                     false,
                                                                                                     new Date(),
                                                                                                     methodName);

                response.setElementList(databaseTableAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            DatabaseTableElement databaseTableAttribute = handler.getDatabaseTableByGUID(userId,
                                                                                         guid,
                                                                                         false,
                                                                                         false,
                                                                                         new Date(),
                                                                                         methodName);

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
     * @param requestBody properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseView(String                   serverName,
                                           String                   userId,
                                           ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseViewProperties)
                {
                    DatabaseViewProperties databaseViewProperties = (DatabaseViewProperties) requestBody.getProperties();

                    String databaseViewGUID = handler.createDatabaseView(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         requestBody.getParentGUID(),
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
                                                                         null,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                    response.setGUID(databaseViewGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseViewProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseViewFromTemplate(String              serverName,
                                                       String              userId,
                                                       String              templateGUID,
                                                       TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createDatabaseViewFromTemplate(userId,
                                                                        requestBody.getExternalSourceGUID(),
                                                                        requestBody.getExternalSourceName(),
                                                                        templateGUID,
                                                                        requestBody.getParentGUID(),
                                                                        requestBody.getQualifiedName(),
                                                                        requestBody.getDisplayName(),
                                                                        requestBody.getDescription(),
                                                                        null,
                                                                        null,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseViewForSchemaType(String                   serverName,
                                                        String                   userId,
                                                        ReferenceableRequestBody requestBody)
    {
        final String methodName = "createDatabaseViewForSchemaType";

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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseViewProperties)
                {
                    DatabaseViewProperties databaseViewProperties = (DatabaseViewProperties) requestBody.getProperties();


                    String databaseViewGUID = handler.createDatabaseViewForSchemaType(userId,
                                                                                      requestBody.getExternalSourceGUID(),
                                                                                      requestBody.getExternalSourceName(),
                                                                                      requestBody.getParentGUID(),
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
                                                                                      null,
                                                                                      null,
                                                                                      false,
                                                                                      false,
                                                                                      new Date(),
                                                                                      methodName);

                    response.setGUID(databaseViewGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseViewProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse updateDatabaseView(String                   serverName,
                                           String                   userId,
                                           String                   databaseViewGUID,
                                           boolean                  isMergeUpdate,
                                           ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseViewProperties)
                {
                    DatabaseViewProperties databaseViewProperties = (DatabaseViewProperties) requestBody.getProperties();

                    handler.updateDatabaseView(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
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
                                               databaseViewProperties.getEffectiveFrom(),
                                               databaseViewProperties.getEffectiveTo(),
                                               isMergeUpdate,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseViewProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse removeDatabaseView(String                    serverName,
                                           String                    userId,
                                           String                    databaseViewGUID,
                                           ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeDatabaseTable(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            databaseViewGUID,
                                            elementGUIDParameterName,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                handler.removeDatabaseTable(userId,
                                            null,
                                            null,
                                            databaseViewGUID,
                                            elementGUIDParameterName,
                                            null,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
    public DatabaseViewsResponse   findDatabaseViews(String                  serverName,
                                                     String                  userId,
                                                     SearchStringRequestBody requestBody,
                                                     int                     startFrom,
                                                     int                     pageSize)
    {
        final String methodName = "findDatabaseViews";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewsResponse response = new DatabaseViewsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseViewElement> databaseViewAttributes = handler.findDatabaseViews(userId,
                                                                                             requestBody.getSearchString(),
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             false,
                                                                                             false,
                                                                                             new Date(),
                                                                                             methodName);

                response.setElementList(databaseViewAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public DatabaseViewsResponse getViewsForDatabaseAsset(String serverName,
                                                          String userId,
                                                          String databaseAssetGUID,
                                                          int    startFrom,
                                                          int    pageSize)
    {
        final String methodName = "getViewsForDatabaseAsset";

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

            List<DatabaseViewElement> databaseViewAttributes = handler.getViewsForDatabaseAsset(userId,
                                                                                                databaseAssetGUID,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                null,
                                                                                                null,
                                                                                                false,
                                                                                                false,
                                                                                                new Date(),
                                                                                                methodName);

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
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseViewsResponse   getDatabaseViewsByName(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName = "getDatabaseViewsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseViewsResponse response = new DatabaseViewsResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseViewElement> databaseViewAttributes = handler.getDatabaseViewsByName(userId,
                                                                                                  requestBody.getName(),
                                                                                                  nameParameterName,
                                                                                                  startFrom,
                                                                                                  pageSize,
                                                                                                  false,
                                                                                                  false,
                                                                                                  new Date(),
                                                                                                  methodName);

                response.setElementList(databaseViewAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            DatabaseViewElement databaseViewAttribute = handler.getDatabaseViewByGUID(userId,
                                                                                      guid,
                                                                                      false,
                                                                                      false,
                                                                                      new Date(),
                                                                                      methodName);

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
     * @param requestBody properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseColumnProperties)
                {
                    DatabaseColumnProperties databaseColumnProperties = (DatabaseColumnProperties) requestBody.getProperties();

                    int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

                    if (databaseColumnProperties.getSortOrder() != null)
                    {
                        sortOrder = databaseColumnProperties.getSortOrder().getOpenTypeOrdinal();
                    }

                    String databaseColumnGUID = handler.createDatabaseColumn(userId,
                                                                             requestBody.getExternalSourceGUID(),
                                                                             requestBody.getExternalSourceName(),
                                                                             requestBody.getParentGUID(),
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
                                                                             databaseColumnProperties.getEffectiveFrom(),
                                                                             databaseColumnProperties.getEffectiveTo(),
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName);

                    if ((databaseColumnGUID != null) && (databaseColumnProperties.getQueries() != null))
                    {
                        for (DatabaseQueryProperties queryProperties : databaseColumnProperties.getQueries())
                        {
                            if (queryProperties != null)
                            {
                                handler.createDatabaseColumnQuery(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  databaseColumnGUID,
                                                                  queryProperties.getQueryId(),
                                                                  queryProperties.getQuery(),
                                                                  queryProperties.getQueryTargetGUID(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);
                            }
                        }
                    }

                    response.setGUID(databaseColumnGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseColumnProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public GUIDResponse createDatabaseColumnFromTemplate(String              serverName,
                                                         String              userId,
                                                         String              templateGUID,
                                                         TemplateRequestBody requestBody)
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

            if (requestBody != null)
            {
                response.setGUID(handler.createDatabaseColumnFromTemplate(userId,
                                                                          requestBody.getExternalSourceGUID(),
                                                                          requestBody.getExternalSourceName(),
                                                                          templateGUID,
                                                                          requestBody.getParentGUID(),
                                                                          requestBody.getQualifiedName(),
                                                                          requestBody.getDisplayName(),
                                                                          requestBody.getDescription(),
                                                                          null,
                                                                          null,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse updateDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             String                   databaseColumnGUID,
                                             boolean                  isMergeUpdate,
                                             ReferenceableRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseColumnProperties)
                {
                    DatabaseColumnProperties databaseColumnProperties = (DatabaseColumnProperties) requestBody.getProperties();

                    int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();

                    if (databaseColumnProperties.getSortOrder() != null)
                    {
                        sortOrder = databaseColumnProperties.getSortOrder().getOpenTypeOrdinal();
                    }

                    handler.updateDatabaseColumn(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
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
                                                 databaseColumnProperties.getEffectiveFrom(),
                                                 databaseColumnProperties.getEffectiveTo(),
                                                 isMergeUpdate,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);

                    if (databaseColumnProperties.getQueries() != null)
                    {
                        for (DatabaseQueryProperties queryProperties : databaseColumnProperties.getQueries())
                        {
                            if (queryProperties != null)
                            {
                                handler.createDatabaseColumnQuery(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  databaseColumnGUID,
                                                                  queryProperties.getQueryId(),
                                                                  queryProperties.getQuery(),
                                                                  queryProperties.getQueryTargetGUID(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);
                            }
                        }
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseColumnProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * @param databaseColumnGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeDatabaseColumn(String                    serverName,
                                             String                    userId,
                                             String                    databaseColumnGUID,
                                             ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeDatabaseColumn(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             databaseColumnGUID,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                handler.removeDatabaseColumn(userId,
                                             null,
                                             null,
                                             databaseColumnGUID,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
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
    public DatabaseColumnsResponse   findDatabaseColumns(String                  serverName,
                                                         String                  userId,
                                                         SearchStringRequestBody requestBody,
                                                         int                     startFrom,
                                                         int                     pageSize)
    {
        final String methodName = "findDatabaseColumns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnsResponse response = new DatabaseColumnsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseColumnElement> databaseColumnAttributes = handler.findDatabaseColumns(userId,
                                                                                                   requestBody.getSearchString(),
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   false,
                                                                                                   false,
                                                                                                   new Date(),
                                                                                                   methodName);

                response.setElementList(databaseColumnAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
                                                                                                      false,
                                                                                                      false,
                                                                                                      new Date(),
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
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DatabaseColumnsResponse   getDatabaseColumnsByName(String          serverName,
                                                              String          userId,
                                                              NameRequestBody requestBody,
                                                              int             startFrom,
                                                              int             pageSize)
    {
        final String methodName = "getDatabaseColumnsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabaseColumnsResponse response = new DatabaseColumnsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                RelationalDataHandler<DatabaseElement,
                                             DatabaseSchemaElement,
                                             DatabaseTableElement,
                                             DatabaseViewElement,
                                             DatabaseColumnElement,
                                             SchemaTypeElement> handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

                List<DatabaseColumnElement> databaseColumnAttributes = handler.getDatabaseColumnsByName(userId,
                                                                                                        requestBody.getName(),
                                                                                                        startFrom,
                                                                                                        pageSize,
                                                                                                        false,
                                                                                                        false,
                                                                                                        new Date(),
                                                                                                        methodName);

                response.setElementList(databaseColumnAttributes);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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

            DatabaseColumnElement schemaAttribute = handler.getDatabaseColumnByGUID(userId,
                                                                                    guid,
                                                                                    false,
                                                                                    false,
                                                                                    new Date(),
                                                                                    methodName);

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
    public VoidResponse setPrimaryKeyOnColumn(String                    serverName,
                                              String                    userId,
                                              String                    databaseColumnGUID,
                                              ClassificationRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabasePrimaryKeyProperties)
                {
                    DatabasePrimaryKeyProperties databasePrimaryKeyProperties = (DatabasePrimaryKeyProperties) requestBody.getProperties();

                    int keyPattern = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();

                    if (databasePrimaryKeyProperties.getKeyPattern() != null)
                    {
                        keyPattern = databasePrimaryKeyProperties.getKeyPattern().getOpenTypeOrdinal();
                    }

                    handler.setPrimaryKeyOnColumn(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  databaseColumnGUID,
                                                  databasePrimaryKeyProperties.getName(),
                                                  keyPattern,
                                                  databasePrimaryKeyProperties.getEffectiveFrom(),
                                                  databasePrimaryKeyProperties.getEffectiveTo(),
                                                  true,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabasePrimaryKeyProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
    public VoidResponse removePrimaryKeyFromColumn(String                    serverName,
                                                   String                    userId,
                                                   String                    databaseColumnGUID,
                                                   ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removePrimaryKeyFromColumn(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   databaseColumnGUID,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
            }
            else
            {
                handler.removePrimaryKeyFromColumn(userId,
                                                   null,
                                                   null,
                                                   databaseColumnGUID,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
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
    public VoidResponse addForeignKeyRelationship(String                  serverName,
                                                  String                  userId,
                                                  String                  primaryKeyColumnGUID,
                                                  String                  foreignKeyColumnGUID,
                                                  RelationshipRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DatabaseForeignKeyProperties)
                {
                    DatabaseForeignKeyProperties databaseForeignKeyProperties = (DatabaseForeignKeyProperties) requestBody.getProperties();

                    handler.addForeignKeyRelationship(userId,
                                                      requestBody.getExternalSourceGUID(),
                                                      requestBody.getExternalSourceName(),
                                                      primaryKeyColumnGUID,
                                                      foreignKeyColumnGUID,
                                                      databaseForeignKeyProperties.getName(),
                                                      databaseForeignKeyProperties.getDescription(),
                                                      databaseForeignKeyProperties.getConfidence(),
                                                      databaseForeignKeyProperties.getSteward(),
                                                      databaseForeignKeyProperties.getSource(),
                                                      databaseForeignKeyProperties.getEffectiveFrom(),
                                                      databaseForeignKeyProperties.getEffectiveTo(),
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DatabaseForeignKeyProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId ,methodName, serverName);
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
     * Remove the foreign key relationship for the requested columns.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param primaryKeyColumnGUID unique identifier of the column that is the linked primary key
     * @param foreignKeyColumnGUID unique identifier of the column the contains the primary key from another table
     * @param requestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeForeignKeyRelationship(String                    serverName,
                                                     String                    userId,
                                                     String                    primaryKeyColumnGUID,
                                                     String                    foreignKeyColumnGUID,
                                                     ExternalSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.removeForeignKeyRelationship(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     primaryKeyColumnGUID,
                                                     foreignKeyColumnGUID,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName);
            }
            else
            {
                handler.removeForeignKeyRelationship(userId,
                                                     null,
                                                     null,
                                                     primaryKeyColumnGUID,
                                                     foreignKeyColumnGUID,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
