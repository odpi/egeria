/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceengine.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceCompletionInterface;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The GovernanceEngineRESTServices provides the server-side implementation of the Governance Engine Open Metadata
 * View Service (OMVS).  It is for governance engines that run outside of Egeria's engine hosts.  They pick up
 * the engine actions that are approved to run on them, run them, and report their progress and completion.
 * Egeria then coordinates the engine actions, for example by starting the next steps of a governance action process.
 * These are the same calls that Egeria's engine hosts make (see GovernanceCompletionInterface).
 */
public class GovernanceEngineRESTServices extends TokenController
{
    private static final GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceEngineRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public GovernanceEngineRESTServices()
    {
    }


    /**
     * Retrieve the engine actions that are approved to run on a governance engine and that no engine has
     * claimed yet.  The caller then claims the ones it is going to run.
     *
     * @param serverName           name of server instance to route request to
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom            starting from position
     * @param pageSize             maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getApprovedEngineActions(String serverName,
                                                                 String governanceEngineGUID,
                                                                 int    startFrom,
                                                                 int    pageSize)
    {
        final String methodName = "getApprovedEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                     auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

            response.setElements(handler.getApprovedEngineActions(userId, governanceEngineGUID, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Request that execution of an engine action is allocated to the caller.  This is only permitted if
     * no other caller has claimed it.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse claimEngineAction(String serverName,
                                          String engineActionGUID)
    {
        final String methodName = "claimEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

            handler.claimEngineAction(userId, engineActionGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName           name of server instance to route request to
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom            starting from position
     * @param pageSize             maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveClaimedEngineActions(String serverName,
                                                                      String governanceEngineGUID,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getActiveClaimedEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                     auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

            response.setElements(handler.getActiveClaimedEngineActions(userId, governanceEngineGUID, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody      new status enum
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse updateEngineActionStatus(String                        serverName,
                                                 String                        engineActionGUID,
                                                 EngineActionStatusRequestBody requestBody)
    {
        final String methodName = "updateEngineActionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

                handler.updateEngineActionStatus(userId, engineActionGUID, requestBody.getStatus());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the status of a specific action target.  By default, these values are derived from
     * the values for the engine action.  However, if the governance service has to process
     * many target elements, then setting the status on each individual target will show the progress of the
     * governance service.
     *
     * @param serverName  name of server instance to route request to
     * @param requestBody the action target and its new status
     *
     * @return void or
     *  InvalidParameterException the action target GUID is not recognized.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse updateActionTargetStatus(String                        serverName,
                                                 ActionTargetStatusRequestBody requestBody)
    {
        final String methodName = "updateActionTargetStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

                handler.updateActionTargetStatus(userId,
                                                 requestBody.getActionTargetGUID(),
                                                 requestBody.getStatus(),
                                                 requestBody.getStartDate(),
                                                 requestBody.getCompletionDate(),
                                                 requestBody.getCompletionMessage());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Declare that all the processing for the engine action is finished and the status of the work.
     * If the engine action is part of a governance action process, the output guards determine which
     * steps of the process run next.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID unique identifier of the associated engine action
     * @param requestBody      completion status, output guards, new action targets and completion message
     *
     * @return void or
     *  InvalidParameterException the completion status is null.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse recordCompletionStatus(String                      serverName,
                                               String                      engineActionGUID,
                                               CompletionStatusRequestBody requestBody)
    {
        final String methodName = "recordCompletionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceCompletionInterface handler = instanceHandler.getGovernanceCompletionClient(userId, serverName, methodName);

                handler.recordCompletionStatus(userId,
                                               engineActionGUID,
                                               requestBody.getRequestParameters(),
                                               requestBody.getStatus(),
                                               requestBody.getOutputGuards(),
                                               requestBody.getNewActionTargets(),
                                               requestBody.getCompletionMessage());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
