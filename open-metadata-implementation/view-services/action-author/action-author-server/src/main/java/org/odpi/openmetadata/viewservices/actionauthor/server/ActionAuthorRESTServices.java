/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.actionauthor.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionExecutorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.NextGovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ActionAuthorRESTServices provides the implementation of the Action Author Open Metadata View Service (OMVS).
 */

public class ActionAuthorRESTServices extends TokenController
{
    private static final ActionAuthorInstanceHandler instanceHandler = new ActionAuthorInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ActionAuthorRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ActionAuthorRESTServices()
    {
    }



    /**
     * Link a governance action to the element it is to work on (action target).
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkTargetForGovernanceAction(String                     serverName,
                                                      String                     governanceActionGUID,
                                                      String                     elementGUID,
                                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkTargetForGovernanceAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TargetForGovernanceActionProperties targetForGovernanceActionProperties)
                {
                    handler.linkTargetForGovernanceAction(userId, governanceActionGUID, elementGUID, requestBody, targetForGovernanceActionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkTargetForGovernanceAction(userId, governanceActionGUID, elementGUID, requestBody, null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TargetForGovernanceActionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTargetForGovernanceAction(userId, governanceActionGUID, elementGUID, null,null);
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
     * Detach a governance action from the element it is to work on (action target).
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachTargetForGovernanceAction(String                        serverName,
                                                        String        governanceActionGUID,
                                                        String        elementGUID,
                                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachTargetForGovernanceAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachTargetForGovernanceAction(userId, governanceActionGUID, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a governance action type to the governance engine that it is to call.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkGovernanceActionExecutor(String                     serverName,
                                                     String                     governanceActionTypeGUID,
                                                     String                     governanceEngineGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkGovernanceActionExecutor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceActionExecutorProperties governanceActionExecutorProperties)
                {
                    handler.linkGovernanceActionExecutor(userId, governanceActionTypeGUID, governanceEngineGUID, requestBody, governanceActionExecutorProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceActionExecutorProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkGovernanceActionExecutor(userId, governanceActionTypeGUID, governanceEngineGUID, null,null);
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
     * Detach a governance action type from the governance engine that it is to call.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachGovernanceActionExecutor(String                        serverName,
                                                       String                        governanceActionTypeGUID,
                                                       String                        governanceEngineGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachGovernanceActionExecutor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachGovernanceActionExecutor(userId, governanceActionTypeGUID, governanceEngineGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */



    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setupFirstActionProcessStep(String                     serverName,
                                                    String                     processGUID,
                                                    String                     processStepGUID,
                                                    NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceActionProcessFlowProperties governanceActionProcessFlowProperties)
                {
                    handler.linkGovernanceActionProcessFlow(userId, processGUID, processStepGUID, requestBody, governanceActionProcessFlowProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkGovernanceActionProcessFlow(userId, processGUID, processStepGUID, requestBody, null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceActionProcessFlowProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkGovernanceActionProcessFlow(userId, processGUID, processStepGUID, null,null);
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
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param firstProcessStepGUID             unique identifier of the first step in the process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse removeFirstProcessStep(String                        serverName,
                                               String                        processGUID,
                                               String                        firstProcessStepGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachGovernanceActionProcessFlow(userId, processGUID, firstProcessStepGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param currentProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupNextActionProcessStep(String                     serverName,
                                                   String                     currentProcessStepGUID,
                                                   String                     nextProcessStepGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NextGovernanceActionProcessStepProperties properties)
                {
                    response.setGUID(handler.linkNextProcessStep(userId,
                                                                 currentProcessStepGUID,
                                                                 nextProcessStepGUID,
                                                                 requestBody,
                                                                 properties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NextGovernanceActionProcessStepProperties.class.getName(), methodName);
                }
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
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse updateNextActionProcessStep(String                        serverName,
                                                    String                        nextProcessStepLinkGUID,
                                                    UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NextGovernanceActionProcessStepProperties properties)
                {
                    handler.updateNextProcessStep(userId,
                                                  nextProcessStepLinkGUID,
                                                  requestBody,
                                                  properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NextGovernanceActionProcessStepProperties.class.getName(), methodName);
                }
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
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param actionLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeNextActionProcessStep(String                        serverName,
                                                    String                        actionLinkGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachNextProcessStep(userId, actionLinkGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
