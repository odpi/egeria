/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.engineservices.governanceaction.rest.EngineSummaryListResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;

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
     * Return information about the governance engines running in this Governance Action Open Metadata Engine Service (OMES).
     *
     * @param serverName name of the engine host server
     * @param userId identifier of calling user
     *
     * @return list of engine summaries or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceActionEngineException there was a problem detected by the governance action engine.
     */
    public EngineSummaryListResponse getLocalEngines(String serverName,
                                                     String userId)
    {
        final String        methodName = "getLocalEngines";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineSummaryListResponse response = new EngineSummaryListResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceActionEngineHandler handler = instanceHandler.getGovernanceActionEngineHandler(userId,
                                                                                                     serverName,
                                                                                                     null,
                                                                                                     methodName);
            response.setEngineSummaries(handler.getLocalEngines());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
