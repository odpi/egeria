/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The MyProfileRESTServices provides the server-side implementation of the My Profile Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MyProfileRESTServices extends TokenController
{
    private static final MyProfileInstanceHandler instanceHandler = new MyProfileInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MyProfileRESTServices.class),
                                                                            instanceHandler.getServiceName());
    /**
     * Default constructor
     */
    public MyProfileRESTServices()
    {
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getMyProfile(String serverName)
    {
        final String methodName = "getMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElement(client.getActorProfileByUserId(userId, userId, null));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new to do action and link it to the supplied role and targets (if applicable).
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties of the to do action
     *
     * @return unique identifier of the to do or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public GUIDResponse createAction(String            serverName,
                                     ActionRequestBody requestBody)
    {
        final String methodName = "createAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createAction(userId,
                                                      requestBody.getOriginatorGUID(),
                                                      requestBody.getActionSponsorGUID(),
                                                      requestBody.getAssignToActorGUID(),
                                                      requestBody,
                                                      null,
                                                      requestBody.getNewActionTargets(),
                                                      requestBody.getProperties()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Update the properties associated with a "To Do".
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the to do
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse updateAction(String                   serverName,
                                     String                   actionGUID,
                                     UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ActionProperties actionProperties)
                {
                    handler.updateAsset(userId, actionGUID, requestBody, actionProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ToDoProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, UpdateElementRequestBody.class.getName());
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
     * Update the properties associated with an Action Target.
     *
     * @param serverName name of the server instances for this request
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse updateActionTargetProperties(String                        serverName,
                                                     String                        actionTargetGUID,
                                                     UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateActionTargetProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ActionTargetProperties toDoActionTargetProperties)
                {
                    handler.updateActionTargetProperties(userId, actionTargetGUID, requestBody, toDoActionTargetProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActionTargetProperties.class.getName(), methodName);
                }
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
     * Assign an action to a new actor.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse reassignAction(String                        serverName,
                                       String                        actionGUID,
                                       String                        actorGUID,
                                       UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "reassignAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.reassignAction(userId, actionGUID, actorGUID, requestBody, null);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete an existing action.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the to do
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse deleteAction(String            serverName,
                                     String            actionGUID,
                                     DeleteRequestBody requestBody)
    {
        final String methodName = "deleteAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.deleteAsset(userId, actionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a action by unique identifier.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the to do
     *
     * @return to do bean or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementResponse getActionByGUID(String serverName,
                                                           String actionGUID)
    {
        final String methodName = "getActionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            GetOptions getOptions = new GetOptions();

            getOptions.setMetadataElementTypeName(OpenMetadataType.ACTION.typeName);

            response.setElement(handler.getAssetByGUID(userId, actionGUID, null));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the "To Dos" that are chained off of an action target element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getActionsForActionTarget(String                    serverName,
                                                   String                    elementGUID,
                                                   ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       requestBody.getActivityStatus(),
                                                                       requestBody));
            }
            else
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       null,
                                                                       requestBody));
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
     * Retrieve the "To Dos" that are chained off of a sponsoring element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getActionsForSponsor(String                    serverName,
                                              String                    elementGUID,
                                              ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForSponsor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  requestBody.getActivityStatus(),
                                                                  requestBody));
            }
            else
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  null,
                                                                  requestBody));
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
     * Retrieve the "To Dos" for a particular actor.
     *
     * @param serverName name of the server instances for this request
     * @param actorGUID unique identifier of the role
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getAssignedActions(String                serverName,
                                                               String                actorGUID,
                                                               ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getAssignedActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                requestBody.getActivityStatus(),
                                                                requestBody));
            }
            else
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                null,
                                                                requestBody));
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
     * Retrieve the "To Dos" that match the search string.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse findActions(String                    serverName,
                                                        ActivityStatusSearchString requestBody)
    {
        final String methodName = "findToDos";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findProcesses(userId,
                                                           requestBody.getSearchString(),
                                                           requestBody.getActivityStatus(),
                                                           requestBody));
            }
            else
            {
                response.setElements(handler.findProcesses(userId,
                                                           null,
                                                           null,
                                                           null));
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
     * Retrieve the "To Dos" that match the category name and status.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public OpenMetadataRootElementsResponse getActionsByCategory(String                          serverName,
                                                                 ActivityStatusFilterRequestBody requestBody)
    {
        final String methodName = "getActionsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProcessesByCategory(userId,
                                                                  requestBody.getFilter(),
                                                                  requestBody.getActivityStatus(),
                                                                  requestBody));
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
}
