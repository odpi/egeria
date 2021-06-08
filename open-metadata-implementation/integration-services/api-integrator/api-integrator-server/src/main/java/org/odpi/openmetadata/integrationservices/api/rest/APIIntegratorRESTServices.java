/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.api.rest;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonInstanceHandler;
import org.odpi.openmetadata.integrationservices.api.connector.APIIntegratorConnector;
import org.odpi.openmetadata.integrationservices.api.contextmanager.APIIntegratorContextManager;
import org.slf4j.LoggerFactory;


/**
 * APIIntegratorRESTServices provides the ability to validate that a connector will run successfully in this integration service.
 * It is a convenience method for configuration tools.
 */
public class APIIntegratorRESTServices
{
    private static IntegrationDaemonInstanceHandler instanceHandler = new IntegrationDaemonInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(APIIntegratorRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * The constructor registers this service with the integration daemon.  It is called with the spring module is
     * picked up by the component scan and instantiated.
     */
    public APIIntegratorRESTServices()
    {
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.API_INTEGRATOR_OMIS,
                                                              APIIntegratorContextManager.class.getName());
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
    public ConnectorTypeResponse validateConnector(String serverName,
                                                   String userId,
                                                   String connectorProviderClassName)
    {
        final String methodName = "validateConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setConnectorType(instanceHandler.validateConnector(connectorProviderClassName,
                                                                        APIIntegratorConnector.class,
                                                                        IntegrationServiceDescription.API_INTEGRATOR_OMIS.getIntegrationServiceFullName()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
