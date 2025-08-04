/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.ConnectorConfigPropertiesRequestBody;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.IntegrationDaemonStatusResponse;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.IntegrationGroupSummariesResponse;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.rest.IntegrationGroupSummaryResponse;
import org.slf4j.LoggerFactory;

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
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param event open lineage event to publish.
     */
    public void publishOpenLineageEvent(String serverName,
                                        String userId,
                                        String event)
    {
        final String methodName = "publishOpenLineageEvent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<IntegrationContextManager> contextManagers = instanceHandler.getIntegrationGroupContextManagers(userId,
                                                                                                                 serverName,
                                                                                                                 methodName);

            if (contextManagers != null)
            {
                for (IntegrationContextManager contextManager : contextManagers)
                {
                    if (contextManager != null)
                    {
                        contextManager.publishOpenLineageRunEvent(event);
                    }
                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
    }


    /**
     * Retrieve the configuration properties of the named connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     *
     * @return properties map or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public PropertiesResponse getConfigurationProperties(String serverName,
                                                         String userId,
                                                         String connectorName)
    {
        final String methodName = "getConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PropertiesResponse response = new PropertiesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setProperties(instanceHandler.getConfigurationProperties(userId,
                                                                              serverName,
                                                                              methodName,
                                                                              connectorName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the configuration properties of the connectors, or specific connector if a connector name is supplied.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    public  VoidResponse updateConfigurationProperties(String                               serverName,
                                                       String                               userId,
                                                       ConnectorConfigPropertiesRequestBody requestBody)
    {
        final String methodName = "updateConfigurationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId,
                                                   serverName,
                                                   methodName);

            if (requestBody != null)
            {
                instanceHandler.updateConfigurationProperties(userId,
                                                              serverName,
                                                              methodName,
                                                              requestBody.getConnectorName(),
                                                              requestBody.getMergeUpdate(),
                                                              requestBody.getConfigurationProperties());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the endpoint network address for a specific integration connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param requestBody name of a specific connector or null for all connectors and the properties to change
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */

    public  VoidResponse updateEndpointNetworkAddress(String            serverName,
                                                      String            userId,
                                                      String            connectorName,
                                                      StringRequestBody requestBody)
    {
        final String methodName = "updateEndpointNetworkAddress";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId,
                                                   serverName,
                                                   methodName);

            if (requestBody != null)
            {
                instanceHandler.updateEndpointNetworkAddress(userId,
                                                             serverName,
                                                             methodName,
                                                             connectorName,
                                                             requestBody.getString());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the connection for a specific integration connector.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param requestBody new connection object
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */

    public  VoidResponse updateConnectorConnection(String     serverName,
                                                   String     userId,
                                                   String     connectorName,
                                                   Connection requestBody)
    {
        final String methodName = "updateConnectorConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId,
                                                   serverName,
                                                   methodName);

            if (requestBody != null)
            {
                instanceHandler.updateConnectorConnection(userId,
                                                          serverName,
                                                          methodName,
                                                          connectorName,
                                                          requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request that the integration daemon refresh all the connectors in all the integration services and groups
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @SuppressWarnings(value = "unused")
    public  VoidResponse refreshConnectors(String          serverName,
                                           String          userId,
                                           NameRequestBody requestBody)
    {
        final String methodName = "refreshConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            String connectorName = null;

            if (requestBody != null)
            {
                connectorName = requestBody.getName();
            }

            instanceHandler.refreshConnector(userId, serverName, methodName, connectorName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request that the integration daemon restart all the connectors in all the integration services and groups
     *
     * @param serverName name of the integration daemon
     * @param userId identifier of calling user
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service.
     */
    @SuppressWarnings(value = "unused")
    public  VoidResponse restartConnectors(String          serverName,
                                           String          userId,
                                           NameRequestBody requestBody)
    {
        final String methodName = "restartConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            String connectorName = null;

            if (requestBody != null)
            {
                connectorName = requestBody.getName();
            }

            instanceHandler.restartConnector(userId, serverName, methodName, connectorName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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


            IntegrationDaemonStatus integrationDaemonStatus = new IntegrationDaemonStatus();

            integrationDaemonStatus.setIntegrationGroupSummaries(instanceHandler.getIntegrationGroupSummaries(userId, serverName, methodName));

            integrationDaemonStatus.setIntegrationConnectorReports(instanceHandler.getIntegrationConnectors(userId, serverName, methodName));

            response.setIntegrationDaemonStatus(integrationDaemonStatus);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  IntegrationGroupException there was a problem detected by the integration group.
     */
    public  VoidResponse refreshIntegrationGroupConfig(String serverName,
                                                       String userId,
                                                       String integrationGroupName)
    {
        final String        methodName = "refreshIntegrationGroupConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            instanceHandler.refreshIntegrationGroupConfig(userId, serverName, integrationGroupName, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
