/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummariesResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummaryResponse;
import org.slf4j.LoggerFactory;


/**
 * AssetAnalysisRESTServices provides the external service implementation for a governance engine.
 * Each method contains the governance server name and the governance engine identifier (guid).
 * The AssetAnalysisRESTServices locates the correct governance engine instance within the correct
 * governance server instance and delegates the request.
 */
public class EngineHostRESTServices
{
    private static final EngineHostInstanceHandler instanceHandler = new EngineHostInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(EngineHostRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the governance server.
     * @param governanceEngineName unique name of the governance engine.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  GovernanceEngineException there was a problem detected by the governance engine.
     */
    public  VoidResponse refreshConfig(String serverName,
                                       String userId,
                                       String governanceEngineName)
    {
        final String        methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            instanceHandler.refreshConfig(userId, serverName, governanceEngineName, methodName);
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
     * @param governanceEngineName qualifiedName of the requested governance engine
     *
     * @return list of governance engine summaries or
     *  InvalidParameterException no available instance for the requested server
     *  UserNotAuthorizedException user does not have access to the requested server
     *  PropertyServerException the service name is not known - indicating a logic error
     */
    public GovernanceEngineSummaryResponse getGovernanceEngineSummary(String serverName,
                                                                      String userId,
                                                                      String governanceEngineName)
    {
        final String methodName = "getGovernanceEngineSummary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineSummaryResponse response = new GovernanceEngineSummaryResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGovernanceEngineSummary(instanceHandler.getGovernanceEngineSummary(userId, serverName, governanceEngineName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the description and status of each governance engine assigned to a specific Open Metadata Engine Service (OMES).
     *
     * @param serverName governance server name
     * @param userId calling user
     * @param serviceURLMarker URL marker of the engine service
     * @return list of statuses - on for each assigned governance engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    public GovernanceEngineSummariesResponse getGovernanceEngineSummaries(String serverName,
                                                                          String userId,
                                                                          String serviceURLMarker)
    {
        final String methodName = "getGovernanceEngineSummaries";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineSummariesResponse response = new GovernanceEngineSummariesResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGovernanceEngineSummaries(instanceHandler.getGovernanceEngineSummaries(userId, serverName, serviceURLMarker, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of each of the governance engines' status for all running engine services.
     *
     * @param serverName engine host server name
     * @param userId calling user
     * @return list of statuses - on for each assigned governance engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    public GovernanceEngineSummariesResponse getGovernanceEngineSummaries(String   serverName,
                                                                          String   userId)
    {
        final String methodName = "getGovernanceEngineSummaries";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineSummariesResponse response = new GovernanceEngineSummariesResponse();
        AuditLog                          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGovernanceEngineSummaries(instanceHandler.getGovernanceEngineSummaries(userId, serverName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
