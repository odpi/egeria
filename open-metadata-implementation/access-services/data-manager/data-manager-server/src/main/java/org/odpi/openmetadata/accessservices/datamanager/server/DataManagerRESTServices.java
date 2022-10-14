/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The DataManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of data managers.
 */
public class DataManagerRESTServices
{
    private static final DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DataManagerRESTServices.class),
                                                                                        instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataManagerRESTServices()
    {
    }


    /**
     * Return the connection object for the Data Manager OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public ConnectionResponse getOutTopicConnection(String serverName,
                                                    String userId,
                                                    String callerId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Files live on a file system.  This method creates a top level capability for a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse   createFileSystemInCatalog(String                serverName,
                                                    String                userId,
                                                    FileSystemRequestBody requestBody)
    {
        final String methodName = "createFileSystemInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                response.setGUID(handler.createFileSystem(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          requestBody.getQualifiedName(),
                                                          requestBody.getDisplayName(),
                                                          requestBody.getDescription(),
                                                          requestBody.getTypeDescription(),
                                                          requestBody.getVersion(),
                                                          requestBody.getPatchLevel(),
                                                          requestBody.getSource(),
                                                          requestBody.getFormat(),
                                                          requestBody.getEncryption(),
                                                          requestBody.getAdditionalProperties(),
                                                          requestBody.getVendorProperties(),
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName));
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
     * Files live on a file system.  This method creates a top level capability for a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse  createFileManagerInCatalog(String                 serverName,
                                                    String                 userId,
                                                    FileManagerRequestBody requestBody)
    {
        final String methodName = "createFileManagerInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                response.setGUID(handler.createSoftwareCapability(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  OpenMetadataAPIMapper.DATA_MANAGER_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.FILE_MANAGER_CLASSIFICATION_TYPE_NAME,
                                                                  requestBody.getQualifiedName(),
                                                                  requestBody.getDisplayName(),
                                                                  requestBody.getDescription(),
                                                                  requestBody.getTypeDescription(),
                                                                  requestBody.getVersion(),
                                                                  requestBody.getPatchLevel(),
                                                                  requestBody.getSource(),
                                                                  requestBody.getAdditionalProperties(),
                                                                  requestBody.getExtendedProperties(),
                                                                  requestBody.getVendorProperties(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
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
     * Create the software server capability for the database manager (DBMS).
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the database manager
     *
     * @return unique identifier of the software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createDatabaseManagerInCatalog(String                     serverName,
                                                       String                     userId,
                                                       DatabaseManagerRequestBody requestBody)
    {
        final String methodName = "createDatabaseManagerInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);
            response.setGUID(handler.createSoftwareCapability(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              OpenMetadataAPIMapper.DATABASE_MANAGER_TYPE_NAME,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getTypeDescription(),
                                                              requestBody.getVersion(),
                                                              requestBody.getPatchLevel(),
                                                              requestBody.getSource(),
                                                              requestBody.getAdditionalProperties(),
                                                              requestBody.getExtendedProperties(),
                                                              requestBody.getVendorProperties(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
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
     * Create the Software server capability for the API Manager.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the API manager
     *
     * @return unique identifier of the software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createAPIManagerInCatalog(String                serverName,
                                                  String                userId,
                                                  APIManagerRequestBody requestBody)
    {
        final String methodName = "createAPIManagerInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);
            response.setGUID(handler.createSoftwareCapability(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              OpenMetadataAPIMapper.API_MANAGER_TYPE_NAME,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getTypeDescription(),
                                                              requestBody.getVersion(),
                                                              requestBody.getPatchLevel(),
                                                              requestBody.getSource(),
                                                              requestBody.getAdditionalProperties(),
                                                              requestBody.getExtendedProperties(),
                                                              requestBody.getVendorProperties(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
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
     * Create the Software server capability for the Event Broker.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the Event Broker
     *
     * @return unique identifier of the software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createEventBrokerInCatalog(String                 serverName,
                                                   String                 userId,
                                                   EventBrokerRequestBody requestBody)
    {
        final String methodName = "createEventBrokerInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);
            response.setGUID(handler.createSoftwareCapability(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              OpenMetadataAPIMapper.EVENT_BROKER_TYPE_NAME,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getTypeDescription(),
                                                              requestBody.getVersion(),
                                                              requestBody.getPatchLevel(),
                                                              requestBody.getSource(),
                                                              requestBody.getAdditionalProperties(),
                                                              requestBody.getExtendedProperties(),
                                                              requestBody.getVendorProperties(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
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
     * Create the Software server capability for an Application.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the Application
     *
     * @return unique identifier of the software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createApplicationInCatalog(String                 serverName,
                                                   String                 userId,
                                                   ApplicationRequestBody requestBody)
    {
        final String methodName = "createApplicationInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);
            response.setGUID(handler.createSoftwareCapability(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              OpenMetadataAPIMapper.APPLICATION_TYPE_NAME,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getTypeDescription(),
                                                              requestBody.getVersion(),
                                                              requestBody.getPatchLevel(),
                                                              requestBody.getSource(),
                                                              requestBody.getAdditionalProperties(),
                                                              requestBody.getExtendedProperties(),
                                                              requestBody.getVendorProperties(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
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
     * Create the Software server capability for a data processing engine such as a reporting engine.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody description of the data processing engine
     *
     * @return unique identifier of the software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createDataProcessingEngineInCatalog(String                          serverName,
                                                            String                          userId,
                                                            DataProcessingEngineRequestBody requestBody)
    {
        final String methodName = "createDataProcessingEngineInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);
            response.setGUID(handler.createSoftwareCapability(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              OpenMetadataAPIMapper.ENGINE_TYPE_NAME,
                                                              null,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getTypeDescription(),
                                                              requestBody.getVersion(),
                                                              requestBody.getPatchLevel(),
                                                              requestBody.getSource(),
                                                              requestBody.getAdditionalProperties(),
                                                              requestBody.getExtendedProperties(),
                                                              requestBody.getVendorProperties(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              new Date(),
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
     * Retrieve the unique identifier of the integration daemon service.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse  getMetadataSourceGUID(String serverName,
                                               String userId,
                                               NameRequestBody requestBody)
    {
        final String methodName = "getMetadataSourceGUID";
        final String parameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

                response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                    OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                    requestBody.getName(),
                                                                    parameterName,
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
}
