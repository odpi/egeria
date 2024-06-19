/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerPlatformListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerPlatformResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareServerResponse;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.OMAGServerConnectorBase;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.PlatformReportResponse;
import org.odpi.openmetadata.viewservices.runtimemanager.rest.ServerReportResponse;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The RuntimeManagerRESTServices provides the server-side implementation of the Runtime Manager Open Metadata
 * View Service (OMVS).
 */
public class RuntimeManagerRESTServices extends TokenController
{
    private static final RuntimeManagerInstanceHandler instanceHandler = new RuntimeManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(RuntimeManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public RuntimeManagerRESTServices()
    {
    }


    /**
     * Returns the list of platforms with a particular qualifiedName or name.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerPlatformListResponse getPlatformsByName(String            serverName,
                                                                 int               startFrom,
                                                                 int               pageSize,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformListResponse response = new SoftwareServerPlatformListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSoftwareServerPlatformsByName(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
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
     * Returns the list of platforms with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerPlatformListResponse getPlatformsByDeployedImplType(String            serverName,
                                                                             int               startFrom,
                                                                             int               pageSize,
                                                                             FilterRequestBody requestBody)
    {
        final String methodName = "getPlatformsByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformListResponse response = new SoftwareServerPlatformListResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSoftwareServerPlatformsByDeployedImplType(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
            }
            else
            {
                response.setElementList(handler.getSoftwareServerPlatformsByDeployedImplType(userId, null, new Date(), startFrom, pageSize));
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
     * Returns details about the platform's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerPlatformResponse getPlatformByGUID(String                        serverName,
                                                            String                        platformGUID,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPlatformByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerPlatformResponse response = new SoftwareServerPlatformResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PlatformManagerClient handler = instanceHandler.getPlatformManagerClient(userId, serverName, methodName);

            response.setElement(handler.getSoftwareServerPlatformByGUID(userId, platformGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns details about the running platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PlatformReportResponse getPlatformReport(String serverName,
                                                    String platformGUID)
    {
        final String methodName = "getPlatformReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PlatformReportResponse response = new PlatformReportResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, platformGUID);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                omagServerPlatformConnector.start();
                response.setElement(omagServerPlatformConnector.getPlatformReport());
                omagServerPlatformConnector.disconnect();
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
     * Returns the list of platforms with a particular qualifiedName or name.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerListResponse getServersByName(String            serverName,
                                                       int               startFrom,
                                                       int               pageSize,
                                                       FilterRequestBody requestBody)
    {
        final String methodName = "getServersByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerListResponse response = new SoftwareServerListResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSoftwareServersByName(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
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
     * Returns the list of servers with a particular deployed implementation type.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody qualified name or display name of the platform
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerListResponse getServersByDeployedImplType(String            serverName,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "getServersByDeployedImplType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerListResponse response = new SoftwareServerListResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSoftwareServersByDeployedImplType(userId, requestBody.getFilter(), requestBody.getEffectiveTime(), startFrom, pageSize));
            }
            else
            {
                response.setElementList(handler.getSoftwareServersByDeployedImplType(userId, null, new Date(), startFrom, pageSize));
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
     * Returns details about the server's catalog entry (asset).
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the platform
     * @param requestBody effective time
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerResponse getServerByGUID(String                        serverName,
                                                  String                        serverGUID,
                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getServerByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SoftwareServerResponse response = new SoftwareServerResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ServerManagerClient handler = instanceHandler.getServerManagerClient(userId, serverName, methodName);

            response.setElement(handler.getSoftwareServerByGUID(userId, serverGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns details about the running server.
     *
     * @param serverName  name of called server
     * @param serverGUID unique identifier of the platform
     * @return a list of platforms
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ServerReportResponse getServerReport(String serverName,
                                                String serverGUID)
    {
        final String methodName = "getServerReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerReportResponse response = new ServerReportResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient handler = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = handler.getConnectorForAsset(userId, serverGUID);

            if (connector instanceof OMAGServerConnectorBase omagServerConnector)
            {
                omagServerConnector.start();
                response.setElement(omagServerConnector.getServerReport());
                omagServerConnector.disconnect();
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
