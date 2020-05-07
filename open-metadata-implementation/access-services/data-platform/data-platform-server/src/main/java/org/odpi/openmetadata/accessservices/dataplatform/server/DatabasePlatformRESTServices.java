/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.metadataelements.*;
import org.odpi.openmetadata.accessservices.dataplatform.properties.*;
import org.odpi.openmetadata.accessservices.dataplatform.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * DatabasePlatformRESTServices is the server-side implementation of the Data Platform OMAS's
 * support for relational databases.  It matches the DatabasePlatformClient.
 */
public class DatabasePlatformRESTServices
{
    private static DataPlatformInstanceHandler instanceHandler = new DataPlatformInstanceHandler();

    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DatabasePlatformRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DatabasePlatformRESTServices()
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
    public GUIDResponse createDatabase(String             serverName,
                                       String             userId,
                                       String             integratorGUID,
                                       String             integratorName,
                                       DatabaseProperties databaseProperties)
    {
        final String methodName = "createDatabase";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabase(userId,
                                                    integratorGUID,
                                                    integratorName,
                                                    databaseProperties.getQualifiedName(),
                                                    databaseProperties.getDisplayName(),
                                                    databaseProperties.getDescription(),
                                                    databaseProperties.getOwner(),
                                                    this.getOwnerType(databaseProperties.getOwnerCategory()),
                                                    databaseProperties.getZoneMembership(),
                                                    databaseProperties.getOrigin(),
                                                    databaseProperties.getLatestChange(),
                                                    databaseProperties.getCreateTime(),
                                                    databaseProperties.getModifiedTime(),
                                                    databaseProperties.getEncodingType(),
                                                    databaseProperties.getEncodingLanguage(),
                                                    databaseProperties.getEncodingDescription(),
                                                    databaseProperties.getDatabaseType(),
                                                    databaseProperties.getDatabaseVersion(),
                                                    databaseProperties.getDatabaseInstance(),
                                                    databaseProperties.getDatabaseImportedFrom(),
                                                    databaseProperties.getAdditionalProperties(),
                                                    databaseProperties.getVendorProperties(),
                                                    databaseProperties.getMeanings(),
                                                    this.getOCFClassifications(databaseProperties.getClassifications()),
                                                    databaseProperties.getTypeName(),
                                                    databaseProperties.getExtendedProperties()));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseProperties properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseFromTemplate(String             serverName,
                                                   String             userId,
                                                   String             integratorGUID,
                                                   String             integratorName,
                                                   String             templateGUID,
                                                   DatabaseProperties databaseProperties)
    {
        final String methodName = "createDatabaseFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseFromTemplate(userId,
                                                                integratorGUID,
                                                                integratorName,
                                                                templateGUID,
                                                                databaseProperties.getQualifiedName(),
                                                                databaseProperties.getDisplayName(),
                                                                databaseProperties.getDescription(),
                                                                databaseProperties.getOwner(),
                                                                this.getOwnerType(databaseProperties.getOwnerCategory()),
                                                                databaseProperties.getZoneMembership(),
                                                                databaseProperties.getOrigin(),
                                                                databaseProperties.getLatestChange(),
                                                                databaseProperties.getCreateTime(),
                                                                databaseProperties.getModifiedTime(),
                                                                databaseProperties.getEncodingType(),
                                                                databaseProperties.getEncodingLanguage(),
                                                                databaseProperties.getEncodingDescription(),
                                                                databaseProperties.getDatabaseType(),
                                                                databaseProperties.getDatabaseVersion(),
                                                                databaseProperties.getDatabaseInstance(),
                                                                databaseProperties.getDatabaseImportedFrom(),
                                                                databaseProperties.getAdditionalProperties(),
                                                                databaseProperties.getVendorProperties(),
                                                                databaseProperties.getMeanings(),
                                                                this.getOCFClassifications(databaseProperties.getClassifications()),
                                                                databaseProperties.getTypeName(),
                                                                databaseProperties.getExtendedProperties()));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDatabase(String             serverName,
                                       String             userId,
                                       String             integratorGUID,
                                       String             integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabase(userId,
                                   integratorGUID,
                                   integratorName,
                                   databaseGUID,
                                   databaseProperties.getQualifiedName(),
                                   databaseProperties.getDisplayName(),
                                   databaseProperties.getDescription(),
                                   databaseProperties.getOwner(),
                                   this.getOwnerType(databaseProperties.getOwnerCategory()),
                                   databaseProperties.getZoneMembership(),
                                   databaseProperties.getOrigin(),
                                   databaseProperties.getLatestChange(),
                                   databaseProperties.getCreateTime(),
                                   databaseProperties.getModifiedTime(),
                                   databaseProperties.getEncodingType(),
                                   databaseProperties.getEncodingLanguage(),
                                   databaseProperties.getEncodingDescription(),
                                   databaseProperties.getDatabaseType(),
                                   databaseProperties.getDatabaseVersion(),
                                   databaseProperties.getDatabaseInstance(),
                                   databaseProperties.getDatabaseImportedFrom(),
                                   databaseProperties.getAdditionalProperties(),
                                   databaseProperties.getVendorProperties(),
                                   databaseProperties.getMeanings(),
                                   this.getOCFClassifications(databaseProperties.getClassifications()),
                                   databaseProperties.getTypeName(),
                                   databaseProperties.getExtendedProperties());
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabase(String          serverName,
                                        String          userId,
                                        String          integratorGUID,
                                        String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.publishDatabase(userId, integratorGUID, integratorName, databaseGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabase(String          serverName,
                                         String          userId,
                                         String          integratorGUID,
                                         String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.withdrawDatabase(userId, integratorGUID, integratorName, databaseGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabase(String          serverName,
                                       String          userId,
                                       String          integratorGUID,
                                       String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabase(userId, integratorGUID, integratorName, databaseGUID, qualifiedName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DatabasesResponse   getDatabasesByDaemon(String serverName,
                                                    String userId,
                                                    String integratorGUID,
                                                    String integratorName,
                                                    int    startFrom,
                                                    int    pageSize)
    {
        final String methodName = "getDatabasesByDaemon";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DatabasesResponse response = new DatabasesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public GUIDResponse createDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             String                   integratorGUID,
                                             String                   integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseSchemaProperties properties about the database schema
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseSchemaFromTemplate(String                   serverName,
                                                         String                   userId,
                                                         String                   integratorGUID,
                                                         String                   integratorName,
                                                         String                   templateGUID,
                                                         String                   databaseGUID,
                                                         DatabaseSchemaProperties databaseSchemaProperties)
    {
        final String methodName = "createDatabaseSchemaFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDatabaseSchema(String                   serverName,
                                             String                   userId,
                                             String                   integratorGUID,
                                             String                   integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishDatabaseSchema(String          serverName,
                                              String          userId,
                                              String          integratorGUID,
                                              String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.publishDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawDatabaseSchema(String          serverName,
                                               String          userId,
                                               String          integratorGUID,
                                               String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.withdrawDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseSchema(String          serverName,
                                             String          userId,
                                             String          integratorGUID,
                                             String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID, qualifiedName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public GUIDResponse createDatabaseTable(String                  serverName,
                                            String                  userId,
                                            String                  integratorGUID,
                                            String                  integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseTableProperties properties about the database table
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseTableFromTemplate(String                  serverName,
                                                        String                  userId,
                                                        String                  integratorGUID,
                                                        String                  integratorName,
                                                        String                  templateGUID,
                                                        String                  databaseSchemaGUID,
                                                        DatabaseTableProperties databaseTableProperties)
    {
        final String methodName = "createDatabaseTableFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDatabaseTable(String                  serverName,
                                            String                  userId,
                                            String                  integratorGUID,
                                            String                  integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseTable(String          serverName,
                                            String          userId,
                                            String          integratorGUID,
                                            String          integratorName,
                                            String          databaseTableGUID,
                                            String          qualifiedName,
                                            NullRequestBody nullRequestBody)
    {
        final String methodName = "removeDatabaseTable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseTable(userId, integratorGUID, integratorName, databaseTableGUID, qualifiedName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createDatabaseView(String                 serverName,
                                           String                 userId,
                                           String                 integratorGUID,
                                           String                 integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseViewProperties properties for the new view
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseViewFromTemplate(String                  serverName,
                                                       String                  userId,
                                                       String                  integratorGUID,
                                                       String                  integratorName,
                                                       String                  templateGUID,
                                                       String                  databaseSchemaGUID,
                                                       DatabaseTableProperties databaseViewProperties)
    {
        final String methodName = "createDatabaseViewFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDatabaseView(String                 serverName,
                                           String                 userId,
                                           String                 integratorGUID,
                                           String                 integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseView(String          serverName,
                                           String          userId,
                                           String          integratorGUID,
                                           String          integratorName,
                                           String          databaseViewGUID,
                                           String          qualifiedName,
                                           NullRequestBody nullRequestBody)
    {
        final String methodName = "removeDatabaseView";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseView(userId, integratorGUID, integratorName, databaseViewGUID, qualifiedName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public GUIDResponse createDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             String                   integratorGUID,
                                             String                   integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseColumnFromTemplate(String                   serverName,
                                                         String                   userId,
                                                         String                   integratorGUID,
                                                         String                   integratorName,
                                                         String                   templateGUID,
                                                         String                   databaseTableGUID,
                                                         DatabaseColumnProperties databaseColumnProperties)
    {
        final String methodName = "createDatabaseColumnFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database derived column.
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
    public GUIDResponse createDatabaseDerivedColumn(String                          serverName,
                                                    String                          userId,
                                                    String                          integratorGUID,
                                                    String                          integratorName,
                                                    String                          databaseTableGUID,
                                                    DatabaseDerivedColumnProperties databaseColumnProperties)
    {
        final String methodName = "createDatabaseDerivedColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a database derived column using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param databaseTableGUID unique identifier of the database table where this column is located
     * @param databaseColumnProperties properties for the new column
     *
     * @return unique identifier of the new metadata element for the database column or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseDerivedColumnFromTemplate(String                          serverName,
                                                                String                          userId,
                                                                String                          integratorGUID,
                                                                String                          integratorName,
                                                                String                          templateGUID,
                                                                String                          databaseTableGUID,
                                                                DatabaseDerivedColumnProperties databaseColumnProperties)
    {
        final String methodName = "createDatabaseDerivedColumnFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a link to the data value that is used to derive a database column.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param integratorGUID unique identifier of software server capability representing the caller
     * @param integratorName unique name of software server capability representing the caller
     * @param databaseColumnGUID unique identifier of the derived column
     * @param databaseQueryProperties properties for the query
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse addQueryTargetToDerivedColumn(String                  serverName,
                                                      String                  userId,
                                                      String                  integratorGUID,
                                                      String                  integratorName,
                                                      String                  databaseColumnGUID,
                                                      DatabaseQueryProperties databaseQueryProperties)
    {
        final String methodName = "addQueryTargetToDerivedColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDatabaseColumn(String                   serverName,
                                             String                   userId,
                                             String                   integratorGUID,
                                             String                   integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a database derived column.
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
    public VoidResponse updateDatabaseDerivedColumn(String                          serverName,
                                                    String                          userId,
                                                    String                          integratorGUID,
                                                    String                          integratorName,
                                                    String                          databaseColumnGUID,
                                                    DatabaseDerivedColumnProperties databaseColumnProperties)
    {
        final String methodName = "updateDatabaseDerivedColumn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeDatabaseColumn(String          serverName,
                                             String          userId,
                                             String          integratorGUID,
                                             String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeDatabaseColumn(userId, integratorGUID, integratorName, databaseColumnGUID, qualifiedName);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public VoidResponse setPrimaryKeyOnColumn(String                       serverName,
                                              String                       userId,
                                              String                       integratorGUID,
                                              String                       integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removePrimaryKeyFromColumn(String          serverName,
                                                   String          userId,
                                                   String          integratorGUID,
                                                   String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removePrimaryKeyFromColumn(userId, integratorGUID, integratorName, databaseColumnGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
    public VoidResponse addForeignKeyRelationship(String                       serverName,
                                                  String                       userId,
                                                  String                       integratorGUID,
                                                  String                       integratorName,
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
            // todo
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeForeignKeyRelationship(String          serverName,
                                                     String          userId,
                                                     String          integratorGUID,
                                                     String          integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.removeForeignKeyRelationship(userId, integratorGUID, integratorName, primaryKeyColumnGUID, foreignKeyColumnGUID);
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Perform conversion between types.
     *
     * @param ownerCategory data platform version of enum
     * @return OCF version of enum
     */
    private OwnerType getOwnerType(OwnerCategory ownerCategory)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (ownerCategory == OwnerCategory.USER_ID)
        {
            ownerType = OwnerType.USER_ID;
        }
        else if (ownerCategory == OwnerCategory.PROFILE_ID)
        {
            ownerType = OwnerType.PROFILE_ID;
        }

        return ownerType;
    }


    /**
     * Perform conversion between types.
     *
     * @param ownerType OCF version of enum
     * @return data platform version of enum
     */
    private OwnerCategory getOwnerCategory(OwnerType ownerType)
    {
        OwnerCategory ownerCategory = OwnerCategory.OTHER;

        if (ownerType == OwnerType.USER_ID)
        {
            ownerCategory = OwnerCategory.USER_ID;
        }
        else if (ownerType == OwnerType.PROFILE_ID)
        {
            ownerCategory = OwnerCategory.PROFILE_ID;
        }

        return ownerCategory;
    }


    /**
     * Perform conversion between types.
     *
     * @param dataPlatformClassifications classifications used by the data platform OMAS
     * @return classifications used by the OCF
     */
    private List<org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification> getOCFClassifications(List<Classification> dataPlatformClassifications)
    {
        List<org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification> ocfClassifications = null;

        if (dataPlatformClassifications != null)
        {
            ocfClassifications = new ArrayList<>();

            for (Classification dataPlatformClassification : dataPlatformClassifications)
            {
                if (dataPlatformClassification != null)
                {
                    org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification =
                            this.getOCFClassification(dataPlatformClassification);

                    if (ocfClassification != null)
                    {
                        ocfClassifications.add(ocfClassification);
                    }
                }
            }

            if (ocfClassifications.isEmpty())
            {
                ocfClassifications = null;
            }
        }

        return ocfClassifications;
    }


    /**
     * Perform conversion between types.
     *
     * @param dataPlatformClassification classification used by the data platform OMAS
     * @return classification used by the OCF
     */
    private org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification getOCFClassification(Classification dataPlatformClassification)
    {
        if (dataPlatformClassification != null)
        {
            org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification = new org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification();

            ocfClassification.setClassificationName(dataPlatformClassification.getClassificationName());
            ocfClassification.setClassificationProperties(dataPlatformClassification.getClassificationProperties());

            return ocfClassification;
        }

        return null;
    }


    /**
     * Perform conversion between types.
     *
     * @param ocfClassifications classifications used by the data platform OMAS
     * @return classifications used by the OCF
     */
    private List<Classification> getDataPlatformClassifications(List<org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification> ocfClassifications)
    {
        List<Classification> dataPlatformClassifications = null;

        if (ocfClassifications != null)
        {
            dataPlatformClassifications = new ArrayList<>();

            for (org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification : ocfClassifications)
            {
                if (ocfClassification != null)
                {
                    Classification dataPlatformClassification = this.getDataPlatformClassification(ocfClassification);

                    if (dataPlatformClassification != null)
                    {
                        dataPlatformClassifications.add(dataPlatformClassification);
                    }
                }
            }

            if (dataPlatformClassifications.isEmpty())
            {
                dataPlatformClassifications = null;
            }
        }

        return dataPlatformClassifications;
    }


    /**
     * Perform conversion between types.
     *
     * @param ocfClassification classification used by the OCF
     * @return classification used by the data platform OMAS
     */
    private Classification getDataPlatformClassification(org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification ocfClassification)
    {
        if (ocfClassification != null)
        {
            Classification dataPlatformClassification = new Classification();

            dataPlatformClassification.setClassificationName(ocfClassification.getClassificationName());
            dataPlatformClassification.setClassificationProperties(ocfClassification.getClassificationProperties());

            return dataPlatformClassification;
        }

        return null;
    }


    /**
     * Retrieve the list of Data Platform OMAS database elements from a list of OCF Asset Beans.
     *
     * @param assets OCF Asset bean
     * @param serverName local server name
     * @return database metadata element
     */
    private List<DatabaseElement> getDatabasesFromAssets(List<Asset> assets, String serverName)
    {
        List<DatabaseElement> databaseElements = null;

        if (assets != null)
        {
            databaseElements = new ArrayList<>();

            for (Asset asset: assets)
            {
                databaseElements.add(this.getDatabaseFromAsset(asset, serverName));
            }
        }

        return databaseElements;
    }


    /**
     * Retrieve the Data Platform OMAS database element from an OCF Bean.
     *
     * @param asset OCF Asset bean
     * @param serverName local server name
     * @return database metadata element
     */
    private DatabaseElement getDatabaseFromAsset(Asset asset, String serverName)
    {
        DatabaseElement element = null;

        if (asset != null)
        {
            element = new DatabaseElement();

            element.setElementHeader(this.getElementHeaderFromBean(asset, serverName));
            this.setAssetProperties(element, asset);

            Map<String, Object>  extendedProperties = asset.getExtendedProperties();
            if (extendedProperties != null)
            {
                if (extendedProperties.get(AssetMapper.CREATE_TIME_PROPERTY_NAME) != null)
                {
                    element.setCreateTime((Date)extendedProperties.get(AssetMapper.CREATE_TIME_PROPERTY_NAME));
                }
                if (extendedProperties.get(AssetMapper.MODIFIED_TIME_PROPERTY_NAME) != null)
                {
                    element.setModifiedTime((Date)extendedProperties.get(AssetMapper.MODIFIED_TIME_PROPERTY_NAME));
                }
                if (extendedProperties.get(AssetMapper.ENCODING_TYPE_PROPERTY_NAME) != null)
                {
                    element.setEncodingType(extendedProperties.get(AssetMapper.ENCODING_TYPE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.ENCODING_LANGUAGE_PROPERTY_NAME) != null)
                {
                    element.setEncodingLanguage(extendedProperties.get(AssetMapper.ENCODING_LANGUAGE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.ENCODING_DESCRIPTION_PROPERTY_NAME) != null)
                {
                    element.setEncodingDescription(extendedProperties.get(AssetMapper.ENCODING_DESCRIPTION_PROPERTY_NAME).toString());
                }

                if (extendedProperties.get(AssetMapper.DATABASE_TYPE_PROPERTY_NAME) != null)
                {
                    element.setDatabaseType(extendedProperties.get(AssetMapper.DATABASE_TYPE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_VERSION_PROPERTY_NAME) != null)
                {
                    element.setDatabaseVersion(extendedProperties.get(AssetMapper.DATABASE_VERSION_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_INSTANCE_PROPERTY_NAME) != null)
                {
                    element.setDatabaseInstance(extendedProperties.get(AssetMapper.DATABASE_INSTANCE_PROPERTY_NAME).toString());
                }
                if (extendedProperties.get(AssetMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME) != null)
                {
                    element.setDatabaseImportedFrom(extendedProperties.get(AssetMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME).toString());
                }
            }
        }

        return element;
    }


    /**
     * Set up the common properties for an asset.
     *
     * @param assetPropertiesBean target Data Platform OMAS bean
     * @param assetBean OCF bean
     */
    private void setAssetProperties(AssetProperties   assetPropertiesBean,
                                    Asset             assetBean)
    {
        if (assetPropertiesBean != null)
        {
            setReferenceableProperties(assetPropertiesBean, assetBean);

            assetPropertiesBean.setDisplayName(assetBean.getDisplayName());
            assetPropertiesBean.setDescription(assetBean.getDescription());
            assetPropertiesBean.setOwner(assetBean.getOwner());
            assetPropertiesBean.setOwnerCategory(this.getOwnerCategory(assetBean.getOwnerType()));
            assetPropertiesBean.setZoneMembership(assetBean.getZoneMembership());
            assetPropertiesBean.setOrigin(assetBean.getOrigin());
            assetPropertiesBean.setLatestChange(assetBean.getLatestChange());
        }
    }


    /**
     * Set up the common properties for a referencable (ignoring extendedProperties and vendor properties).
     *
     * @param referenceablePropertiesBean Data Platform OMAS bean
     * @param referenceableBean OCF bean
     */
    private void setReferenceableProperties(ReferenceableProperties   referenceablePropertiesBean,
                                            Referenceable             referenceableBean)
    {
        if (referenceableBean != null)
        {
            referenceablePropertiesBean.setQualifiedName(referenceableBean.getQualifiedName());
            referenceablePropertiesBean.setAdditionalProperties(referenceableBean.getAdditionalProperties());
            referenceablePropertiesBean.setMeanings(this.getMeaningGUIDs(referenceableBean.getMeanings()));
            referenceablePropertiesBean.setClassifications(this.getDataPlatformClassifications(referenceableBean.getClassifications()));

            if (referenceableBean.getType() != null)
            {
                referenceablePropertiesBean.setTypeName(referenceableBean.getType().getElementTypeName());
            }
        }
    }


    /**
     * Return the list of unique identifiers for the glossary terms linked to the metadata element.
     *
     * @param ocfMeanings OCF bean representing the meanings
     * @return list of GUIDs
     */
    List<String>  getMeaningGUIDs(List<Meaning>   ocfMeanings)
    {
        List<String> meaningGUIDs = null;

        if ((ocfMeanings != null) && (!ocfMeanings.isEmpty()))
        {
            meaningGUIDs = new ArrayList<>();

            for (Meaning ocfMeaning : ocfMeanings)
            {
                if (ocfMeaning != null)
                {
                    meaningGUIDs.add(ocfMeaning.getGUID());
                }
            }
        }

        return meaningGUIDs;
    }

    /**
     * Create the metadata element header for the metadata element just extracted from the metadata repository.
     *
     * @param bean metadata from repository
     * @param serverName local server name
     * @return metadata element header
     */
    private ElementHeader getElementHeaderFromBean(Referenceable   bean,
                                                   String          serverName)
    {
        ElementHeader elementHeader = null;

        if (bean != null)
        {
            elementHeader = new ElementHeader();

            elementHeader.setGUID(bean.getGUID());
            elementHeader.setOrigin(getElementOriginFromBean(bean, serverName));
            elementHeader.setType(this.getElementTypeFromBean(bean));
        }

        return elementHeader;
    }


    /**
     * Set up the Data Platform ElementOrigin from the OCF bean.
     *
     * @param bean OCF bean
     * @param serverName local server name
     * @return Data Platform OMAS version
     */
    private ElementOrigin getElementOriginFromBean(Referenceable   bean,
                                                   String          serverName)
    {
        ElementOrigin elementOrigin = null;

        if (bean != null)
        {
            elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);

            if (bean.getType() != null)
            {
                elementOrigin.setOriginCategory(this.getElementOriginCategory(bean.getType().getElementOrigin()));
                elementOrigin.setHomeMetadataCollectionId(bean.getType().getElementHomeMetadataCollectionId());
                elementOrigin.setHomeMetadataCollectionName(bean.getType().getElementHomeMetadataCollectionName());
                elementOrigin.setLicense(bean.getType().getElementLicense());
            }
        }

        return elementOrigin;
    }


    /**
     * Return a Data Platform OMAS ElementOriginCategory from an OCF ElementOrigin.
     *
     * @param ocfElementOrigin enum from OCF
     * @return Data Platform OMAS version
     */
    private ElementOriginCategory getElementOriginCategory(org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin ocfElementOrigin)
    {
        if (ocfElementOrigin != null)
        {
            switch (ocfElementOrigin)
            {
                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;
            }
        }

        return ElementOriginCategory.UNKNOWN;
    }


    /**
     * Create a Data Platform OMAS Element Type from an OCF bean.
     *
     * @param bean OCF bean
     * @return new element type
     */
    private ElementType getElementTypeFromBean(Referenceable  bean)
    {
        ElementType elementType = null;

        if ((bean != null) && (bean.getType() != null))
        {
            elementType = new ElementType();

            elementType.setTypeId(bean.getType().getElementTypeId());
            elementType.setTypeName(bean.getType().getElementTypeName());
            elementType.setTypeDescription(bean.getType().getElementTypeDescription());
            elementType.setTypeVersion(bean.getType().getElementTypeVersion());
            elementType.setSuperTypeNames(bean.getType().getElementSuperTypeNames());
        }

        return elementType;
    }
}
