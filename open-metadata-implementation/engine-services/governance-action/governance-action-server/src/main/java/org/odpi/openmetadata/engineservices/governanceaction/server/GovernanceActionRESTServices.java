/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.engineservices.governanceaction.rest.ProviderReportResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionService;
import org.slf4j.LoggerFactory;


/**
 * GovernanceActionRESTServices provides the external service implementation for a governance action engine services.
 * Each method contains the engine host server name and the governance action engine identifier (guid).
 * The GovernanceActionRESTServices locates the correct governance action engine instance within the correct
 * engine host server instance and delegates the request.
 */
public class GovernanceActionRESTServices
{
    private static GovernanceActionInstanceHandler instanceHandler = new GovernanceActionInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceActionRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


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
    public ProviderReportResponse validateConnector(String serverName,
                                                    String userId,
                                                    String connectorProviderClassName)
    {
        final String methodName = "validateConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProviderReportResponse response = new ProviderReportResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setProviderReport(instanceHandler.validateGovernanceActionConnector(connectorProviderClassName,
                                                                                         GovernanceActionService.class,
                                                                                         EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceFullName()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
