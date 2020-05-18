/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.metadataelements.*;
import org.odpi.openmetadata.accessservices.dataplatform.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.dataplatform.metadataelements.ElementOrigin;
import org.odpi.openmetadata.accessservices.dataplatform.metadataelements.ElementType;
import org.odpi.openmetadata.accessservices.dataplatform.properties.*;
import org.odpi.openmetadata.accessservices.dataplatform.properties.Classification;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.dataplatform.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.dataplatform.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.RelationalDataHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
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

            String databaseGUID = handler.createDatabase(userId,
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
                                                         databaseProperties.getTypeName(),
                                                         databaseProperties.getExtendedProperties(),
                                                         methodName);

            if (databaseProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseGUID,
                                                         databaseProperties.getQualifiedName(),
                                                         databaseProperties.getVendorProperties(),
                                                         methodName);
            }

            response.setGUID(databaseGUID);
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
     * @param templateProperties properties that override the template
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
                                                   TemplateProperties templateProperties)
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
                                                                templateProperties.getQualifiedName(),
                                                                templateProperties.getDisplayName(),
                                                                templateProperties.getDescription(),
                                                                methodName));
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
                                   databaseProperties.getTypeName(),
                                   databaseProperties.getExtendedProperties(),
                                   methodName);

            if (databaseProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseGUID,
                                                         databaseProperties.getQualifiedName(),
                                                         databaseProperties.getVendorProperties(),
                                                         methodName);
            }
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

            handler.publishDatabase(userId, integratorGUID, integratorName, databaseGUID, methodName);
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

            handler.withdrawDatabase(userId, integratorGUID, integratorName, databaseGUID, methodName);
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

            handler.removeDatabase(userId, integratorGUID, integratorName, databaseGUID, qualifiedName, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseAssets = handler.findDatabases(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabasesFromAssets(databaseAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseAssets = handler.getDatabasesByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabasesFromAssets(databaseAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseAssets = handler.getDatabasesByDaemon(userId, integratorGUID, integratorName, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabasesFromAssets(databaseAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            Asset databaseAsset = handler.getDatabaseByGUID(userId, guid, methodName);

            response.setElement(this.getDatabaseFromAsset(databaseAsset, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseSchemaGUID = handler.createDatabaseSchema(userId,
                                                                     integratorGUID,
                                                                     integratorName,
                                                                     databaseGUID,
                                                                     databaseSchemaProperties.getQualifiedName(),
                                                                     databaseSchemaProperties.getDisplayName(),
                                                                     databaseSchemaProperties.getDescription(),
                                                                     databaseSchemaProperties.getOwner(),
                                                                     this.getOwnerType(databaseSchemaProperties.getOwnerCategory()),
                                                                     databaseSchemaProperties.getZoneMembership(),
                                                                     databaseSchemaProperties.getOrigin(),
                                                                     databaseSchemaProperties.getLatestChange(),
                                                                     databaseSchemaProperties.getAdditionalProperties(),
                                                                     databaseSchemaProperties.getTypeName(),
                                                                     databaseSchemaProperties.getExtendedProperties(),
                                                                     methodName);

            if (databaseSchemaProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseSchemaGUID,
                                                         databaseSchemaProperties.getQualifiedName(),
                                                         databaseSchemaProperties.getVendorProperties(),
                                                         methodName);
            }

            response.setGUID(databaseSchemaGUID);
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
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database schema or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseSchemaFromTemplate(String             serverName,
                                                         String             userId,
                                                         String             integratorGUID,
                                                         String             integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseSchemaFromTemplate(userId,
                                                                      integratorGUID,
                                                                      integratorName,
                                                                      templateGUID,
                                                                      databaseGUID,
                                                                      templateProperties.getQualifiedName(),
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseSchema(userId,
                                         integratorGUID,
                                         integratorName,
                                         databaseSchemaGUID,
                                         databaseSchemaProperties.getQualifiedName(),
                                         databaseSchemaProperties.getDisplayName(),
                                         databaseSchemaProperties.getDescription(),
                                         databaseSchemaProperties.getOwner(),
                                         this.getOwnerType(databaseSchemaProperties.getOwnerCategory()),
                                         databaseSchemaProperties.getZoneMembership(),
                                         databaseSchemaProperties.getOrigin(),
                                         databaseSchemaProperties.getLatestChange(),
                                         databaseSchemaProperties.getAdditionalProperties(),
                                         databaseSchemaProperties.getTypeName(),
                                         databaseSchemaProperties.getExtendedProperties(),
                                         methodName);

            if (databaseSchemaProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseSchemaGUID,
                                                         databaseSchemaProperties.getQualifiedName(),
                                                         databaseSchemaProperties.getVendorProperties(),
                                                         methodName);
            }
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

            handler.publishDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID, methodName);
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

            handler.withdrawDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID, methodName);
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

            handler.removeDatabaseSchema(userId, integratorGUID, integratorName, databaseSchemaGUID, qualifiedName, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseSchemaAssets = handler.findDatabaseSchemas(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseSchemasFromAssets(databaseSchemaAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseSchemaAssets = handler.getSchemasForDatabase(userId, databaseGUID, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseSchemasFromAssets(databaseSchemaAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<Asset> databaseSchemaAssets = handler.getDatabaseSchemasByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseSchemasFromAssets(databaseSchemaAssets, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            Asset databaseSchemaAsset = handler.getDatabaseSchemaByGUID(userId, guid, methodName);

            response.setElement(this.getDatabaseSchemaFromAsset(databaseSchemaAsset, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseTableGUID = handler.createDatabaseTable(userId,
                                                                   integratorGUID,
                                                                   integratorName,
                                                                   databaseSchemaGUID,
                                                                   databaseTableProperties.getQualifiedName(),
                                                                   databaseTableProperties.getDisplayName(),
                                                                   databaseTableProperties.getDescription(),
                                                                   databaseTableProperties.isDeprecated(),
                                                                   databaseTableProperties.getAliases(),
                                                                   databaseTableProperties.getAdditionalProperties(),
                                                                   databaseTableProperties.getTypeName(),
                                                                   databaseTableProperties.getExtendedProperties(),
                                                                   methodName);

            if (databaseTableProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseTableGUID,
                                                         databaseTableProperties.getQualifiedName(),
                                                         databaseTableProperties.getVendorProperties(),
                                                         methodName);
            }

            response.setGUID(databaseTableGUID);
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
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new database table or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseTableFromTemplate(String             serverName,
                                                        String             userId,
                                                        String             integratorGUID,
                                                        String             integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseTableFromTemplate(userId,
                                                                integratorGUID,
                                                                integratorName,
                                                                templateGUID,
                                                                databaseSchemaGUID,
                                                                templateProperties.getQualifiedName(),
                                                                templateProperties.getDisplayName(),
                                                                templateProperties.getDescription(),
                                                                methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseTable(userId,
                                        integratorGUID,
                                        integratorName,
                                        databaseTableGUID,
                                        databaseTableProperties.getQualifiedName(),
                                        databaseTableProperties.getDisplayName(),
                                        databaseTableProperties.getDescription(),
                                        databaseTableProperties.isDeprecated(),
                                        databaseTableProperties.getAliases(),
                                        databaseTableProperties.getAdditionalProperties(),
                                        databaseTableProperties.getTypeName(),
                                        databaseTableProperties.getExtendedProperties(),
                                        methodName);

            if (databaseTableProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseTableGUID,
                                                         databaseTableProperties.getQualifiedName(),
                                                         databaseTableProperties.getVendorProperties(),
                                                         methodName);
            }
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

            handler.removeDatabaseTable(userId, integratorGUID, integratorName, databaseTableGUID, qualifiedName, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseTableAttributes = handler.findDatabaseTables(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseTablesFromAttributes(databaseTableAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseTableAttributes = handler.getTablesForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseTablesFromAttributes(databaseTableAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseTableAttributes = handler.getDatabaseTablesByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseTablesFromAttributes(databaseTableAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            SchemaAttribute databaseTableAttribute = handler.getDatabaseTableByGUID(userId, guid, methodName);

            response.setElement(this.getDatabaseTableFromAttribute(databaseTableAttribute, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseViewGUID = handler.createDatabaseView(userId,
                                                                 integratorGUID,
                                                                 integratorName,
                                                                 databaseSchemaGUID,
                                                                 databaseViewProperties.getQualifiedName(),
                                                                 databaseViewProperties.getDisplayName(),
                                                                 databaseViewProperties.getDescription(),
                                                                 databaseViewProperties.isDeprecated(),
                                                                 databaseViewProperties.getAliases(),
                                                                 databaseViewProperties.getExpression(),
                                                                 databaseViewProperties.getAdditionalProperties(),
                                                                 databaseViewProperties.getTypeName(),
                                                                 databaseViewProperties.getExtendedProperties(),
                                                                 methodName);

            if (databaseViewProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseViewGUID,
                                                         databaseViewProperties.getQualifiedName(),
                                                         databaseViewProperties.getVendorProperties(),
                                                         methodName);
            }

            response.setGUID(databaseViewGUID);
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
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database view or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseViewFromTemplate(String             serverName,
                                                       String             userId,
                                                       String             integratorGUID,
                                                       String             integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseViewFromTemplate(userId,
                                                                     integratorGUID,
                                                                     integratorName,
                                                                     templateGUID,
                                                                     databaseSchemaGUID,
                                                                     templateProperties.getQualifiedName(),
                                                                     templateProperties.getDisplayName(),
                                                                     templateProperties.getDescription(),
                                                                     methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseView(userId,
                                       integratorGUID,
                                       integratorName,
                                       databaseViewGUID,
                                       databaseViewProperties.getQualifiedName(),
                                       databaseViewProperties.getDisplayName(),
                                       databaseViewProperties.getDescription(),
                                       databaseViewProperties.isDeprecated(),
                                       databaseViewProperties.getAliases(),
                                       databaseViewProperties.getExpression(),
                                       databaseViewProperties.getAdditionalProperties(),
                                       databaseViewProperties.getTypeName(),
                                       databaseViewProperties.getExtendedProperties(),
                                       methodName);

            if (databaseViewProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseViewGUID,
                                                         databaseViewProperties.getQualifiedName(),
                                                         databaseViewProperties.getVendorProperties(),
                                                         methodName);
            }
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

            handler.removeDatabaseView(userId, integratorGUID, integratorName, databaseViewGUID, qualifiedName, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseViewAttributes = handler.findDatabaseViews(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseViewsFromAttributes(databaseViewAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseViewAttributes = handler.getViewsForDatabaseSchema(userId, databaseSchemaGUID, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseViewsFromAttributes(databaseViewAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseViewAttributes = handler.getDatabaseViewsByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseViewsFromAttributes(databaseViewAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            SchemaAttribute databaseViewAttribute = handler.getDatabaseViewByGUID(userId, guid, methodName);

            response.setElement(this.getDatabaseViewFromAttribute(databaseViewAttribute, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            String databaseColumnGUID = handler.createDatabaseColumn(userId,
                                                                     integratorGUID,
                                                                     integratorName,
                                                                     databaseTableGUID,
                                                                     databaseColumnProperties.getQualifiedName(),
                                                                     databaseColumnProperties.getDisplayName(),
                                                                     databaseColumnProperties.getDescription(),
                                                                     databaseColumnProperties.getDataType(),
                                                                     databaseColumnProperties.getDefaultValue(),
                                                                     databaseColumnProperties.getFormula(),
                                                                     this.getOCFDerivedSchemaQueries(databaseColumnProperties.getQueries()),
                                                                     databaseColumnProperties.isDeprecated(),
                                                                     databaseColumnProperties.getElementPosition(),
                                                                     databaseColumnProperties.getMinCardinality(),
                                                                     databaseColumnProperties.getMaxCardinality(),
                                                                     databaseColumnProperties.isAllowsDuplicateValues(),
                                                                     databaseColumnProperties.isOrderedValues(),
                                                                     databaseColumnProperties.getDefaultValueOverride(),
                                                                     this.getOCFSortOrder(databaseColumnProperties.getSortOrder()),
                                                                     databaseColumnProperties.getMinimumLength(),
                                                                     databaseColumnProperties.getLength(),
                                                                     databaseColumnProperties.getSignificantDigits(),
                                                                     databaseColumnProperties.isNullable(),
                                                                     databaseColumnProperties.getNativeJavaClass(),
                                                                     databaseColumnProperties.getAliases(),
                                                                     databaseColumnProperties.getAdditionalProperties(),
                                                                     databaseColumnProperties.getTypeName(),
                                                                     databaseColumnProperties.getExtendedProperties(),
                                                                     methodName);

            if (databaseColumnProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseColumnGUID,
                                                         databaseColumnProperties.getQualifiedName(),
                                                         databaseColumnProperties.getVendorProperties(),
                                                         methodName);            }

            response.setGUID(databaseColumnGUID);
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
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the database column
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDatabaseColumnFromTemplate(String             serverName,
                                                         String             userId,
                                                         String             integratorGUID,
                                                         String             integratorName,
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            response.setGUID(handler.createDatabaseColumnFromTemplate(userId,
                                                                      integratorGUID,
                                                                      integratorName,
                                                                      templateGUID,
                                                                      databaseTableGUID,
                                                                      templateProperties.getQualifiedName(),
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.updateDatabaseColumn(userId,
                                         integratorGUID,
                                         integratorName,
                                         databaseColumnGUID,
                                         databaseColumnProperties.getQualifiedName(),
                                         databaseColumnProperties.getDisplayName(),
                                         databaseColumnProperties.getDescription(),
                                         databaseColumnProperties.getDataType(),
                                         databaseColumnProperties.getDefaultValue(),
                                         databaseColumnProperties.getFormula(),
                                         this.getOCFDerivedSchemaQueries(databaseColumnProperties.getQueries()),
                                         databaseColumnProperties.isDeprecated(),
                                         databaseColumnProperties.getElementPosition(),
                                         databaseColumnProperties.getMinCardinality(),
                                         databaseColumnProperties.getMaxCardinality(),
                                         databaseColumnProperties.isAllowsDuplicateValues(),
                                         databaseColumnProperties.isOrderedValues(),
                                         databaseColumnProperties.getDefaultValueOverride(),
                                         this.getOCFSortOrder(databaseColumnProperties.getSortOrder()),
                                         databaseColumnProperties.getMinimumLength(),
                                         databaseColumnProperties.getLength(),
                                         databaseColumnProperties.getSignificantDigits(),
                                         databaseColumnProperties.isNullable(),
                                         databaseColumnProperties.getNativeJavaClass(),
                                         databaseColumnProperties.getAliases(),
                                         databaseColumnProperties.getAdditionalProperties(),
                                         databaseColumnProperties.getTypeName(),
                                         databaseColumnProperties.getExtendedProperties(),
                                         methodName);

            if (databaseColumnProperties.getVendorProperties() != null)
            {
                ReferenceableHandler referenceableHandler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

                referenceableHandler.setVendorProperties(userId,
                                                         databaseColumnGUID,
                                                         databaseColumnProperties.getQualifiedName(),
                                                         databaseColumnProperties.getVendorProperties(),
                                                         methodName);
            }
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

            handler.removeDatabaseColumn(userId, integratorGUID, integratorName, databaseColumnGUID, qualifiedName, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseColumnAttributes = handler.findDatabaseColumns(userId, searchString, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseColumnsFromAttributes(databaseColumnAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseColumnAttributes = handler.getColumnsForDatabaseTable(userId, databaseTableGUID, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseColumnsFromAttributes(databaseColumnAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            List<SchemaAttribute> databaseColumnAttributes = handler.getDatabaseColumnsByName(userId, name, startFrom, pageSize, methodName);

            response.setElementList(this.getDatabaseColumnsFromAttributes(databaseColumnAttributes, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            SchemaAttribute schemaAttribute = handler.getDatabaseColumnByGUID(userId, guid, methodName);

            response.setElement(this.getDatabaseColumnFromAttribute(schemaAttribute, userId, serverName, methodName));
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.setPrimaryKeyOnColumn(userId,
                                          integratorGUID,
                                          integratorName,
                                          databaseColumnGUID,
                                          databasePrimaryKeyProperties.getName(),
                                          this.getOCFKeyPattern(databasePrimaryKeyProperties.getKeyPattern()),
                                          methodName);
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

            handler.removePrimaryKeyFromColumn(userId, integratorGUID, integratorName, databaseColumnGUID, methodName);
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

            RelationalDataHandler handler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

            handler.addForeignKeyRelationship(userId,
                                              integratorGUID,
                                              integratorName,
                                              primaryKeyColumnGUID,
                                              foreignKeyColumnGUID,
                                              databaseForeignKeyProperties.getName(),
                                              databaseForeignKeyProperties.getDescription(),
                                              databaseForeignKeyProperties.getConfidence(),
                                              databaseForeignKeyProperties.getSteward(),
                                              databaseForeignKeyProperties.getSource(),
                                              methodName);
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

            handler.removeForeignKeyRelationship(userId, integratorGUID, integratorName, primaryKeyColumnGUID, foreignKeyColumnGUID, methodName);
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
     * Retrieve the requested classification from the retrieved list, copying the other classifications into a new list.
     * This is used when an element that is stored as a classification is actually returned as a first class object.
     *
     * @param requestedClassificationName name of the desired classification
     * @param retrievedClassifications list of classification retrieved from the repository
     * @return requested classification or null if it is not present
     */
    private Classification extractClassification(String               requestedClassificationName,
                                                 List<Classification> retrievedClassifications)
    {
        Classification       requestedClassification = null;

        if (retrievedClassifications != null)
        {
            for (Classification retrievedClassification : retrievedClassifications)
            {
                if (retrievedClassification != null)
                {
                    if (requestedClassificationName.equals(retrievedClassification.getClassificationName()))
                    {
                        requestedClassification = retrievedClassification;
                    }
                }
            }
        }

        return requestedClassification;
    }


    /**
     * Remove the requested classification from the retrieved list.
     * This is used when an element that is stored as a classification is actually returned as a first class object.
     *
     * @param requestedClassificationName name of the desired classification
     * @param retrievedClassifications list of classification retrieved from the repository
     * @return requested classification or null if it is not present
     */
    private List<Classification> removeClassification(String               requestedClassificationName,
                                                      List<Classification> retrievedClassifications)
    {
        List<Classification>     newClassifications = null;

        if (retrievedClassifications != null)
        {
            newClassifications = new ArrayList<>();

            for (Classification retrievedClassification : retrievedClassifications)
            {
                if (retrievedClassification != null)
                {
                    if (! requestedClassificationName.equals(retrievedClassification.getClassificationName()))
                    {
                        newClassifications.add(retrievedClassification);
                    }
                }
            }

            if (newClassifications.isEmpty())
            {
                newClassifications = null;
            }
        }

        return newClassifications;
    }


    /**
     * Retrieve the list of Data Platform OMAS database elements from a list of OCF Asset Beans.
     *
     * @param assets OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DatabaseElement> getDatabasesFromAssets(List<Asset> assets,
                                                         String      userId,
                                                         String      serverName,
                                                         String      methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        List<DatabaseElement> databaseElements = null;

        if (assets != null)
        {
            databaseElements = new ArrayList<>();

            for (Asset asset: assets)
            {
                databaseElements.add(this.getDatabaseFromAsset(asset, userId, serverName, methodName));
            }
        }

        return databaseElements;
    }


    /**
     * Retrieve the Data Platform OMAS database element from an OCF Bean.
     *
     * @param asset OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DatabaseElement getDatabaseFromAsset(Asset asset,
                                                 String userId,
                                                 String serverName,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
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

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    asset.getGUID(),
                                                                    asset.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Platform OMAS database schema elements from a list of OCF Asset Beans.
     *
     * @param assets OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database schema metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DatabaseSchemaElement> getDatabaseSchemasFromAssets(List<Asset> assets,
                                                                     String      userId,
                                                                     String      serverName,
                                                                     String      methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        List<DatabaseSchemaElement> databaseSchemaElements = null;

        if (assets != null)
        {
            databaseSchemaElements = new ArrayList<>();

            for (Asset asset: assets)
            {
                databaseSchemaElements.add(this.getDatabaseSchemaFromAsset(asset, userId, serverName, methodName));
            }
        }

        return databaseSchemaElements;
    }


    /**
     * Retrieve the Data Platform OMAS database schema element from an OCF Bean.
     *
     * @param asset OCF Asset bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database schema metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DatabaseSchemaElement getDatabaseSchemaFromAsset(Asset  asset,
                                                             String userId,
                                                             String serverName,
                                                             String methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        DatabaseSchemaElement element = null;

        if (asset != null)
        {
            element = new DatabaseSchemaElement();

            element.setElementHeader(this.getElementHeaderFromBean(asset, serverName));
            this.setAssetProperties(element, asset);

            element.setExtendedProperties(asset.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    asset.getGUID(),
                                                                    asset.getQualifiedName(),
                                                                    methodName));
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
     * Retrieve the list of Data Platform OMAS database table elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database table metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DatabaseTableElement> getDatabaseTablesFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                                       String                userId,
                                                                       String                serverName,
                                                                       String                methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        List<DatabaseTableElement> databaseTableElements = null;

        if (schemaAttributes != null)
        {
            databaseTableElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseTableElements.add(this.getDatabaseTableFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseTableElements;
    }


    /**
     * Retrieve the Data Platform OMAS database table element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database table metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DatabaseTableElement getDatabaseTableFromAttribute(SchemaAttribute schemaAttribute,
                                                               String          userId,
                                                               String          serverName,
                                                               String          methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        DatabaseTableElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseTableElement();

            element.setElementHeader(this.getElementHeaderFromBean(schemaAttribute, serverName));
            this.setSchemaAttributeProperties(element, schemaAttribute);

            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Platform OMAS database table elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database view metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DatabaseViewElement> getDatabaseViewsFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                                     String                userId,
                                                                     String                serverName,
                                                                     String                methodName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        List<DatabaseViewElement> databaseViewElements = null;

        if (schemaAttributes != null)
        {
            databaseViewElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseViewElements.add(this.getDatabaseViewFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseViewElements;
    }


    /**
     * Retrieve the Data Platform OMAS database table element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database view metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DatabaseViewElement getDatabaseViewFromAttribute(SchemaAttribute schemaAttribute,
                                                             String          userId,
                                                             String          serverName,
                                                             String          methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        DatabaseViewElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseViewElement();

            ElementHeader elementHeader = this.getElementHeaderFromBean(schemaAttribute, serverName);

            Classification relationalViewClassification = this.extractClassification(SchemaElementMapper.RELATIONAL_VIEW_CLASSIFICATION_TYPE_NAME,
                                                                                     elementHeader.getClassifications());

            if (relationalViewClassification != null)
            {
                elementHeader.setClassifications(this.removeClassification(SchemaElementMapper.RELATIONAL_VIEW_CLASSIFICATION_TYPE_NAME,
                                                                           elementHeader.getClassifications()));

                Map<String, Object>  properties = relationalViewClassification.getClassificationProperties();
                if (properties != null)
                {
                    Object expressionObject = properties.get(SchemaElementMapper.EXPRESSION_PROPERTY_NAME);
                    if (expressionObject != null)
                    {
                        element.setExpression(expressionObject.toString());
                    }
                }
            }

            element.setElementHeader(elementHeader);

            this.setSchemaAttributeProperties(element, schemaAttribute);
            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of Data Platform OMAS database column elements from a list of OCF SchemaAttribute Beans.
     *
     * @param schemaAttributes OCF SchemaAttribute beans
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database column metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DatabaseColumnElement> getDatabaseColumnsFromAttributes(List<SchemaAttribute> schemaAttributes,
                                                                         String                userId,
                                                                         String                serverName,
                                                                         String                methodName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        List<DatabaseColumnElement> databaseColumnElements = null;

        if (schemaAttributes != null)
        {
            databaseColumnElements = new ArrayList<>();

            for (SchemaAttribute schemaAttribute: schemaAttributes)
            {
                databaseColumnElements.add(this.getDatabaseColumnFromAttribute(schemaAttribute, userId, serverName, methodName));
            }
        }

        return databaseColumnElements;
    }


    /**
     * Retrieve the Data Platform OMAS database column element from an OCF Bean.
     *
     * @param schemaAttribute OCF SchemaAttribute bean
     * @param userId calling user
     * @param serverName local server name
     * @param methodName calling method
     *
     * @return database column metadata element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DatabaseColumnElement getDatabaseColumnFromAttribute(SchemaAttribute schemaAttribute,
                                                                 String          userId,
                                                                 String          serverName,
                                                                 String          methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        DatabaseColumnElement element = null;

        if (schemaAttribute != null)
        {
            element = new DatabaseColumnElement();

            if (schemaAttribute instanceof DerivedSchemaAttribute)
            {
                element.setFormula(((DerivedSchemaAttribute) schemaAttribute).getFormula());
                element.setQueries(this.getDatabaseSchemaQueries(((DerivedSchemaAttribute) schemaAttribute).getQueries()));
            }
            else
            {
                SchemaType attributeType = schemaAttribute.getAttributeType();

                if (attributeType != null)
                {
                    if (attributeType instanceof PrimitiveSchemaType)
                    {
                        PrimitiveSchemaType primitiveSchemaType = (PrimitiveSchemaType)attributeType;

                        element.setDataType(primitiveSchemaType.getDataType());
                        element.setDefaultValue(primitiveSchemaType.getDefaultValue());
                    }
                }
            }

            ElementHeader elementHeader = this.getElementHeaderFromBean(schemaAttribute, serverName);

            this.setSchemaAttributeProperties(element, schemaAttribute);

            element.setExtendedProperties(schemaAttribute.getExtendedProperties());

            Classification primaryKeyClassification = this.extractClassification(SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                                                 elementHeader.getClassifications());

            if (primaryKeyClassification != null)
            {
                elementHeader.setClassifications(this.removeClassification(SchemaElementMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                                            elementHeader.getClassifications()));

                DatabasePrimaryKeyProperties primaryKeyProperties = new DatabasePrimaryKeyProperties();

                Map<String, Object> classificationProperties = primaryKeyClassification.getClassificationProperties();

                if (classificationProperties != null)
                {
                    Object primaryKeyName = classificationProperties.get(SchemaElementMapper.PRIMARY_KEY_NAME_PROPERTY_NAME);

                    if (primaryKeyName != null)
                    {
                        primaryKeyProperties.setName(primaryKeyName.toString());
                    }

                    Object primaryKeyPattern = classificationProperties.get(SchemaElementMapper.PRIMARY_KEY_PATTERN_PROPERTY_NAME);

                    if (primaryKeyPattern != null)
                    {
                        primaryKeyProperties.setKeyPattern(this.getKeyPattern((org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern) primaryKeyPattern));
                    }
                }

                element.setPrimaryKeyProperties(primaryKeyProperties);
            }

            element.setElementHeader(elementHeader);

            List<SchemaAttributeRelationship>  relationships = schemaAttribute.getAttributeRelationships();
            if (relationships != null)
            {
                for (SchemaAttributeRelationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (SchemaElementMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME.equals(relationship.getLinkType()))
                        {
                            element.setReferencedColumnGUID(relationship.getLinkedAttributeGUID());
                            element.setReferencedColumnQualifiedName(relationship.getLinkedAttributeName());

                            DatabaseForeignKeyProperties foreignKeyProperties = new DatabaseForeignKeyProperties();

                            Map<String, Object> linkProperties = relationship.getLinkProperties();
                            if (linkProperties != null)
                            {
                                Object propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_NAME_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setName(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setDescription(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setConfidence(Integer.getInteger(propertyValue.toString()));
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_STEWARD_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setSteward(propertyValue.toString());
                                }

                                propertyValue = linkProperties.get(SchemaElementMapper.FOREIGN_KEY_SOURCE_PROPERTY_NAME);

                                if (propertyValue != null)
                                {
                                    foreignKeyProperties.setSource(propertyValue.toString());
                                }
                            }

                            element.setForeignKeyProperties(foreignKeyProperties);
                        }
                    }
                }
            }

            ReferenceableHandler handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            element.setVendorProperties(handler.getVendorProperties(userId,
                                                                    schemaAttribute.getGUID(),
                                                                    schemaAttribute.getQualifiedName(),
                                                                    methodName));
        }

        return element;
    }


    /**
     * Set up the common properties for a schema attribute.
     *
     * @param schemaAttributePropertiesBean target Data Platform OMAS bean
     * @param schemaAttributeBean OCF bean
     */
    private void setSchemaAttributeProperties(SchemaAttributeProperties   schemaAttributePropertiesBean,
                                              SchemaAttribute             schemaAttributeBean)
    {
        if (schemaAttributePropertiesBean != null)
        {
            setSchemaElementProperties(schemaAttributePropertiesBean, schemaAttributeBean);

            schemaAttributePropertiesBean.setElementPosition(schemaAttributeBean.getElementPosition());
            schemaAttributePropertiesBean.setMinCardinality(schemaAttributeBean.getMinCardinality());
            schemaAttributePropertiesBean.setMaxCardinality(schemaAttributeBean.getMaxCardinality());
            schemaAttributePropertiesBean.setAllowsDuplicateValues(schemaAttributeBean.isAllowsDuplicateValues());
            schemaAttributePropertiesBean.setOrderedValues(schemaAttributeBean.isOrderedValues());
            schemaAttributePropertiesBean.setDefaultValueOverride(schemaAttributeBean.getDefaultValueOverride());
            schemaAttributePropertiesBean.setMinimumLength(schemaAttributeBean.getMinimumLength());
            schemaAttributePropertiesBean.setLength(schemaAttributeBean.getLength());
            schemaAttributePropertiesBean.setSignificantDigits(schemaAttributeBean.getSignificantDigits());
            schemaAttributePropertiesBean.setNullable(schemaAttributeBean.isNullable());
            schemaAttributePropertiesBean.setNativeJavaClass(schemaAttributeBean.getNativeJavaClass());
            schemaAttributePropertiesBean.setAliases(schemaAttributeBean.getAliases());
            schemaAttributePropertiesBean.setSortOrder(this.getDataItemSortOrder(schemaAttributeBean.getSortOrder()));
        }
    }


    /**
     * Set up the common properties for a schema element.
     *
     * @param schemaElementPropertiesBean target Data Platform OMAS bean
     * @param schemaElementBean OCF bean
     */
    private void setSchemaElementProperties(SchemaElementProperties   schemaElementPropertiesBean,
                                            SchemaElement             schemaElementBean)
    {
        if (schemaElementPropertiesBean != null)
        {
            setReferenceableProperties(schemaElementPropertiesBean, schemaElementBean);

            schemaElementPropertiesBean.setDisplayName(schemaElementBean.getDisplayName());
            schemaElementPropertiesBean.setDescription(schemaElementBean.getDescription());
            schemaElementPropertiesBean.setDeprecated(schemaElementBean.isDeprecated());

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
    private List<String>  getMeaningGUIDs(List<Meaning>   ocfMeanings)
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

            elementHeader.setMeanings(this.getMeaningGUIDs(bean.getMeanings()));
            elementHeader.setClassifications(this.getDataPlatformClassifications(bean.getClassifications()));
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


    /**
     * Return a Data Platform OMAS DataItemSourceOrder from an OCF DataItemSourceOrder.
     *
     * @param ocfSortOrder enum from OCF
     * @return Data Platform OMAS version
     */
    private DataItemSortOrder getDataItemSortOrder(org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder ocfSortOrder)
    {
        if (ocfSortOrder != null)
        {
            switch (ocfSortOrder)
            {
                case ASCENDING:
                    return DataItemSortOrder.ASCENDING;

                case DESCENDING:
                    return DataItemSortOrder.DESCENDING;

                case UNSORTED:
                    return DataItemSortOrder.UNSORTED;
            }
        }

        return DataItemSortOrder.UNKNOWN;
    }


    /**
     * Return a OCF DataItemSourceOrder from a Data Platform OMAS DataItemSourceOrder.
     *
     * @param dataItemSortOrder enum from Data Platform OMAS
     * @return Data Platform OMAS version
     */
    private org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder getOCFSortOrder(DataItemSortOrder dataItemSortOrder)
    {
        if (dataItemSortOrder != null)
        {
            switch (dataItemSortOrder)
            {
                case ASCENDING:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.ASCENDING;

                case DESCENDING:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.DESCENDING;

                case UNSORTED:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.UNSORTED;
            }
        }

        return org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder.UNKNOWN;
    }


    /**
     * Return a list of OCF SchemaImplementationQuery from a list of Data Platform OMAS DatabaseQueryProperties.
     *
     * @param databaseQueries bean from Data Platform OMAS
     * @return Data Platform OMAS version
     */
    private List<SchemaImplementationQuery> getOCFDerivedSchemaQueries(List<DatabaseQueryProperties> databaseQueries)
    {
        List<SchemaImplementationQuery> derivedSchemaQueries = null;

        if (databaseQueries != null)
        {
            derivedSchemaQueries = new ArrayList<>();

            for (DatabaseQueryProperties queryProperties : databaseQueries)
            {
                if (queryProperties != null)
                {
                    SchemaImplementationQuery ocfQueryProperties = new SchemaImplementationQuery();

                    ocfQueryProperties.setQueryId(queryProperties.getQueryId());
                    ocfQueryProperties.setQuery(queryProperties.getQuery());
                    ocfQueryProperties.setQueryTargetGUID(queryProperties.getQueryTargetGUID());

                    derivedSchemaQueries.add(ocfQueryProperties);
                }
            }

            if (derivedSchemaQueries.isEmpty())
            {
                derivedSchemaQueries = null;
            }
        }

        return derivedSchemaQueries;
    }


    /**
     * Return a list of Data Platform OMAS DatabaseQueryProperties from a list of OCF SchemaImplementationQuery.
     *
     * @param derivedSchemaQueries bean from Data Platform OMAS
     * @return Data Platform OMAS version
     */
    private List<DatabaseQueryProperties> getDatabaseSchemaQueries(List<SchemaImplementationQuery> derivedSchemaQueries)
    {
        List<DatabaseQueryProperties> databaseQueryPropertiesList = null;

        if (derivedSchemaQueries != null)
        {
            databaseQueryPropertiesList = new ArrayList<>();

            for (SchemaImplementationQuery derivedSchemaQueryProperties : derivedSchemaQueries)
            {
                if (derivedSchemaQueryProperties != null)
                {
                    DatabaseQueryProperties databaseQueryProperties = new DatabaseQueryProperties();

                    databaseQueryProperties.setQueryId(derivedSchemaQueryProperties.getQueryId());
                    databaseQueryProperties.setQuery(derivedSchemaQueryProperties.getQuery());
                    databaseQueryProperties.setQueryTargetGUID(derivedSchemaQueryProperties.getQueryTargetGUID());

                    databaseQueryPropertiesList.add(databaseQueryProperties);
                }
            }

            if (databaseQueryPropertiesList.isEmpty())
            {
                databaseQueryPropertiesList = null;
            }
        }

        return databaseQueryPropertiesList;
    }


    /**
     * Return a Data Platform OMAS KeyPattern from an OCF KeyPattern.
     *
     * @param ocfKeyPattern enum from OCF
     * @return Data Platform OMAS version
     */
    private KeyPattern getKeyPattern(org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern ocfKeyPattern)
    {
        if (ocfKeyPattern != null)
        {
            switch (ocfKeyPattern)
            {
                case LOCAL_KEY:
                    return KeyPattern.LOCAL_KEY;

                case RECYCLED_KEY:
                    return KeyPattern.RECYCLED_KEY;

                case NATURAL_KEY:
                    return KeyPattern.NATURAL_KEY;

                case MIRROR_KEY:
                    return KeyPattern.MIRROR_KEY;

                case AGGREGATE_KEY:
                    return KeyPattern.AGGREGATE_KEY;

                case CALLERS_KEY:
                    return KeyPattern.CALLERS_KEY;

                case STABLE_KEY:
                    return KeyPattern.STABLE_KEY;
            }
        }

        return KeyPattern.OTHER;
    }


    /**
     * Return an OCF KeyPattern from a Data Platform OMAS KeyPattern.
     *
     * @param keyPattern enum from OCF
     * @return Data Platform OMAS version
     */
    private org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern getOCFKeyPattern(KeyPattern keyPattern)
    {
        if (keyPattern != null)
        {
            switch (keyPattern)
            {
                case LOCAL_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.LOCAL_KEY;

                case RECYCLED_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.RECYCLED_KEY;

                case NATURAL_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.NATURAL_KEY;

                case MIRROR_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.MIRROR_KEY;

                case AGGREGATE_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.AGGREGATE_KEY;

                case CALLERS_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.CALLERS_KEY;

                case STABLE_KEY:
                    return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.STABLE_KEY;
            }
        }

        return org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern.OTHER;
    }

}
