/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.rest;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonInstanceHandler;
import org.odpi.openmetadata.integrationservices.lineage.api.LineageIntegratorAPI;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorOMISConnector;
import org.odpi.openmetadata.integrationservices.lineage.contextmanager.LineageIntegratorContextManager;
import org.slf4j.LoggerFactory;


/**
 * LineageIntegratorRESTServices provides the ability to validate that a connector will run successfully in this integration service.
 * It is a convenience method for configuration tools.
 */
public class LineageIntegratorRESTServices
{
    private static final IntegrationDaemonInstanceHandler instanceHandler = new IntegrationDaemonInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LineageIntegratorRESTServices.class),
                                                                            instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * The constructor registers this service with the integration daemon.  It is called with the spring module is
     * picked up by the component scan and instantiated.
     */
    public LineageIntegratorRESTServices()
    {
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS,
                                                              LineageIntegratorContextManager.class.getName(),
                                                              LineageIntegratorOMISConnector.class.getName());
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type or
     *
     *  InvalidParameterException the connector provider class name is not a valid connector for this service;
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReportResponse validateConnector(String serverName,
                                                     String userId,
                                                     String connectorProviderClassName)
    {
        final String methodName = "validateConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorReportResponse response = new ConnectorReportResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorReport connectorReport = instanceHandler.validateConnector(connectorProviderClassName,
                                                                                LineageIntegratorConnector.class,
                                                                                IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS.getIntegrationServiceFullName());

            if (connectorReport != null)
            {
                response.setConnectorReport(connectorReport);
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
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param event open lineage event to publish.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid;
     *  UserNotAuthorizedException the caller is not authorized to call the service or
     *  PropertyServerException there is a problem processing the request
     */
    public VoidResponse publishOpenLineageEvent(String serverName,
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

            LineageIntegratorContextManager contextManager =
                    (LineageIntegratorContextManager)instanceHandler.getIntegrationServiceContextManager(userId,
                                                                                                         serverName,
                                                                                                         IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS.getIntegrationServiceURLMarker(),
                                                                                                         methodName);
            contextManager.publishOpenLineageRunEvent(event);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
