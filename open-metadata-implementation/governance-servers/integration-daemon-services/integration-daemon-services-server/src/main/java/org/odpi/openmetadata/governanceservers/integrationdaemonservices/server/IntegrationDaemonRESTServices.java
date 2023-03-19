/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.*;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * IntegrationDaemonRESTServices provides the external service implementation for an integration service
 * and integration group.
 * The IntegrationDaemonRESTServices locates the correct integration service/group instance within the correct
 * integration daemon instance and delegates the request.
 */
public class IntegrationDaemonRESTServices
{
    private final static IntegrationDaemonInstanceHandler instanceHandler = new IntegrationDaemonInstanceHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(IntegrationDaemonRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public PropertiesResponse getConfigurationProperties(String serverName,
                                                         String userId,
                                                         String serviceURLMarker,
                                                         String connectorName)
    {
        final String methodName = "getConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PropertiesResponse response = new PropertiesResponse();
        AuditLog           auditLog = null;

        try
        {
            IntegrationServiceHandler handler = instanceHandler.getIntegrationServiceHandler(userId, serverName, serviceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setProperties(handler.getConfigurationProperties(userId, connectorName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param serviceURLMarker integration service identifier
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse updateConfigurationProperties(String                               serverName,
                                                       String                               userId,
                                                       String                               serviceURLMarker,
                                                       ConnectorConfigPropertiesRequestBody requestBody)
    {
        final String methodName = "updateConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationServiceHandler handler = instanceHandler.getIntegrationServiceHandler(userId, serverName, serviceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateConfigurationProperties(userId,
                                                      requestBody.getConnectorName(),
                                                      requestBody.getMergeUpdate(),
                                                      requestBody.getConfigurationProperties());
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
     * Request that the integration daemon refresh all the connectors in all the integration services
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @SuppressWarnings(value = "unused")
    public  VoidResponse refreshAllServices(String          serverName,
                                            String          userId,
                                            NullRequestBody requestBody)
    {
        final String methodName = "refreshAllServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<IntegrationServiceHandler> handlers = instanceHandler.getAllIntegrationServiceHandlers(userId, serverName, methodName);

            if (handlers != null)
            {
                for (IntegrationServiceHandler handler : handlers)
                {
                    if (handler != null)
                    {
                        handler.refreshService(null);
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
     * Request that the integration service refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * integration daemon is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param serviceURLMarker unique name of the integration service
     * @param requestBody name of a specific connector to refresh - if null all connectors are refreshed
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse refreshService(String          serverName,
                                        String          userId,
                                        String          serviceURLMarker,
                                        NameRequestBody requestBody)
    {
        final String methodName = "refreshService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationServiceHandler handler = instanceHandler.getIntegrationServiceHandler(userId, serverName, serviceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            String connectorName = null;

            if (requestBody != null)
            {
                connectorName = requestBody.getName();
            }

            handler.refreshService(connectorName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request that the integration service shutdown and recreate its integration connectors.
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param serviceURLMarker unique name of the integration service
     * @param requestBody name of a specific connector to refresh - if null all connectors are restarted.
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse restartService(String          serverName,
                                        String          userId,
                                        String          serviceURLMarker,
                                        NameRequestBody requestBody)
    {
        final String methodName = "restartService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationServiceHandler handler = instanceHandler.getIntegrationServiceHandler(userId, serverName, serviceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            String connectorName = null;

            if (requestBody != null)
            {
                connectorName = requestBody.getName();
            }

            handler.restartService(connectorName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of each of the integration services' and integration groups' status.
     *
     * @param serverName integration daemon name
     * @param userId calling user
     * @return list of statuses - on for each assigned integration services or group
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    public IntegrationDaemonStatusResponse getIntegrationDaemonStatus(String   serverName,
                                                                      String   userId)
    {
        final String methodName = "getIntegrationDaemonStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationDaemonStatusResponse response = new IntegrationDaemonStatusResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<IntegrationServiceHandler> integrationServiceHandlers = instanceHandler.getAllIntegrationServiceHandlers(userId, serverName, methodName);

            IntegrationDaemonStatus integrationDaemonStatus = new IntegrationDaemonStatus();

            List<IntegrationServiceSummary> integrationServiceSummaries = new ArrayList<>();

            if (integrationServiceHandlers != null)
            {
                for (IntegrationServiceHandler handler : integrationServiceHandlers)
                {
                    if (handler != null)
                    {
                        integrationServiceSummaries.add(handler.getIntegrationServiceSummary());
                    }
                }
            }

            if (! integrationServiceSummaries.isEmpty())
            {
                integrationDaemonStatus.setIntegrationServiceSummaries(integrationServiceSummaries);
            }

            integrationDaemonStatus.setIntegrationGroupSummaries(instanceHandler.getIntegrationGroupSummaries(userId, serverName, methodName));

            response.setIntegrationDaemonStatus(integrationDaemonStatus);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of each of the integration services' status.
     *
     * @param serverName integration daemon name
     * @param userId calling user
     * @return list of statuses - on for each assigned integration services
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration daemon.
     */
    public IntegrationServiceSummaryResponse getIntegrationServicesSummaries(String   serverName,
                                                                             String   userId)
    {
        final String methodName = "getIntegrationServicesSummaries";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationServiceSummaryResponse response = new IntegrationServiceSummaryResponse();
        AuditLog                        auditLog = null;

        try
        {
            List<IntegrationServiceHandler> integrationServiceHandlers = instanceHandler.getAllIntegrationServiceHandlers(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<IntegrationServiceSummary> integrationServiceSummaries = new ArrayList<>();

            if (integrationServiceHandlers != null)
            {
                for (IntegrationServiceHandler handler : integrationServiceHandlers)
                {
                    if (handler != null)
                    {
                        integrationServiceSummaries.add(handler.getIntegrationServiceSummary());
                    }
                }
            }

            if (! integrationServiceSummaries.isEmpty())
            {
                response.setIntegrationServiceSummaries(integrationServiceSummaries);
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
     * Request that the integration group refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server.
     * @param integrationGroupName unique name of the integration group.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    public  VoidResponse refreshConfig(String serverName,
                                       String userId,
                                       String integrationGroupName)
    {
        final String        methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            instanceHandler.refreshConfig(userId, serverName, integrationGroupName, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of the requested engine's status.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param integrationGroupName qualifiedName of the requested integration group
     *
     * @return list of integration group summaries or
     *  InvalidParameterException no available instance for the requested server
     *  UserNotAuthorizedException user does not have access to the requested server
     *  PropertyServerException the service name is not known - indicating a logic error
     */
    public IntegrationGroupSummaryResponse getIntegrationGroupSummary(String serverName,
                                                                      String userId,
                                                                      String integrationGroupName)
    {
        final String methodName = "getIntegrationGroupSummary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupSummaryResponse response = new IntegrationGroupSummaryResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setIntegrationGroupSummary(instanceHandler.getIntegrationGroupSummary(userId, serverName, integrationGroupName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return a summary of each of the integration groups' status for all running engine services.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @return list of statuses - on for each assigned integration groups or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    public IntegrationGroupSummariesResponse getIntegrationGroupSummaries(String   serverName,
                                                                          String   userId)
    {
        final String methodName = "getIntegrationGroupSummaries";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupSummariesResponse response = new IntegrationGroupSummariesResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setIntegrationGroupSummaries(instanceHandler.getIntegrationGroupSummaries(userId, serverName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
