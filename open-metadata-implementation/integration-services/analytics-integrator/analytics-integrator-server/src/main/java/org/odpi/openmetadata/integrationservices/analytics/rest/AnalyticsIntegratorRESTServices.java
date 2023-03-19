/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.analytics.rest;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonInstanceHandler;
import org.odpi.openmetadata.integrationservices.analytics.api.AnalyticsIntegratorAPI;
import org.odpi.openmetadata.integrationservices.analytics.connector.AnalyticsIntegratorConnector;
import org.odpi.openmetadata.integrationservices.analytics.contextmanager.AnalyticsIntegratorContextManager;
import org.slf4j.LoggerFactory;


/**
 * AnalyticsIntegratorRESTServices provides the ability to validate that a connector will run successfully in this integration service.
 * It is a convenience method for configuration tools.
 */
public class AnalyticsIntegratorRESTServices
{
    private static final IntegrationDaemonInstanceHandler instanceHandler = new IntegrationDaemonInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AnalyticsIntegratorRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * The constructor registers this service with the integration daemon.  It is called with the spring module is
     * picked up by the component scan and instantiated.
     */
    public AnalyticsIntegratorRESTServices()
    {
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.ANALYTICS_INTEGRATOR_OMIS,
                                                              AnalyticsIntegratorContextManager.class.getName(),
                                                              AnalyticsIntegratorAPI.class.getName());
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
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
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
                                                                                AnalyticsIntegratorConnector.class,
                                                                                IntegrationServiceDescription.ANALYTICS_INTEGRATOR_OMIS.getIntegrationServiceFullName());

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
}
