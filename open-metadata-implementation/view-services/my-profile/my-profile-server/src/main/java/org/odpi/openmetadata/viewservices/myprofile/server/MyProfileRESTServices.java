/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ToDoActionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonalProfileUniverse;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonalProfileProperties;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    public PersonalProfileResponse getMyProfile(String serverName)
    {
        final String methodName = "getMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PersonalProfileResponse response = new PersonalProfileResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OrganizationManagement client = instanceHandler.getOrganizationManagementClient(userId, serverName, methodName);

            ActorProfileElement actorProfileElement = client.getActorProfileByUserId(userId, userId);

            if (actorProfileElement != null)
            {
                PersonalProfileUniverse   personalProfileUniverse = new PersonalProfileUniverse();
                PersonalProfileProperties profileProperties      = new PersonalProfileProperties();
                ActorProfileProperties    actorProfileProperties = actorProfileElement.getProfileProperties();
                Map<String, Object>       extendedProperties     = actorProfileProperties.getExtendedProperties();

                profileProperties.setQualifiedName(actorProfileProperties.getQualifiedName());
                profileProperties.setKnownName(actorProfileProperties.getKnownName());
                profileProperties.setDescription(actorProfileProperties.getDescription());
                profileProperties.setPronouns(getExtendedProperty(extendedProperties,"pronouns"));
                profileProperties.setTitle(getExtendedProperty(extendedProperties,"title"));
                profileProperties.setInitials(getExtendedProperty(extendedProperties,"initials"));
                profileProperties.setGivenNames(getExtendedProperty(extendedProperties,"givenNames"));
                profileProperties.setSurname(getExtendedProperty(extendedProperties,"surname"));
                profileProperties.setFullName(getExtendedProperty(extendedProperties,"fullName"));
                profileProperties.setPreferredLanguage(getExtendedProperty(extendedProperties,"preferredLanguage"));
                profileProperties.setJobTitle(getExtendedProperty(extendedProperties,"jobTitle"));
                profileProperties.setEmployeeNumber(getExtendedProperty(extendedProperties,"employeeNumber"));
                profileProperties.setEmployeeType(getExtendedProperty(extendedProperties,"employeeType"));
                profileProperties.setIsPublic(getExtendedBooleanProperty(extendedProperties,"isPublic"));
                profileProperties.setAdditionalProperties(actorProfileProperties.getAdditionalProperties());
                profileProperties.setEffectiveFrom(actorProfileProperties.getEffectiveFrom());
                profileProperties.setEffectiveTo(actorProfileProperties.getEffectiveTo());

                personalProfileUniverse.setElementHeader(actorProfileElement.getElementHeader());
                personalProfileUniverse.setProfileProperties(profileProperties);
                personalProfileUniverse.setContributionRecord(actorProfileElement.getContributionRecord());
                personalProfileUniverse.setContactMethods(actorProfileElement.getContactMethods());
                personalProfileUniverse.setUserIdentities(actorProfileElement.getUserIdentities());
                personalProfileUniverse.setPeers(actorProfileElement.getPeers());

                if (actorProfileElement.getPersonRoles() != null)
                {
                    List<ActorRoleElement> personRoles = new ArrayList<>();

                    for (ElementStub personRoleStub : actorProfileElement.getPersonRoles())
                    {
                        ActorRoleElement actorRoleElement = client.getPersonRoleByGUID(userId,
                                                                                       personRoleStub.getGUID());

                        if (actorRoleElement != null)
                        {
                            personRoles.add(actorRoleElement);
                        }
                    }

                    if (! personRoles.isEmpty())
                    {
                        personalProfileUniverse.setRoles(personRoles);
                    }
                }

                response.setPersonalProfile(personalProfileUniverse);
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
    public GUIDResponse createToDo(String          serverName,
                                   ToDoRequestBody requestBody)
    {
        final String methodName = "createToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createToDo(userId,
                                                    requestBody.getOriginatorGUID(),
                                                    requestBody.getActionSponsorGUID(),
                                                    requestBody.getAssignToActorGUID(),
                                                    requestBody.getNewActionTargetProperties(),
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
     * @param toDoGUID unique identifier of the to do
     * @param isMergeUpdate should the toDoProperties overlay the existing stored properties or replace them
     * @param toDoProperties properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse updateToDo(String         serverName,
                                   String         toDoGUID,
                                   boolean        isMergeUpdate,
                                   ToDoProperties toDoProperties)
    {
        final String methodName = "updateToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            handler.updateToDo(userId, toDoGUID, isMergeUpdate, toDoProperties);
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
     * @param isMergeUpdate should the actionTargetProperties overlay the existing stored properties or replace them
     * @param actionTargetProperties properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public VoidResponse updateActionTargetProperties(String                 serverName,
                                                     String                 actionTargetGUID,
                                                     boolean                isMergeUpdate,
                                                     ToDoActionTargetProperties actionTargetProperties)
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
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            handler.updateActionTargetProperties(userId, actionTargetGUID, isMergeUpdate, actionTargetProperties);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Assign a "To Do" to a new actor.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse reassignToDo(String          serverName,
                                     String          toDoGUID,
                                     String          actorGUID,
                                     NullRequestBody requestBody)
    {
        final String methodName = "reassignToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            handler.reassignToDo(userId, toDoGUID, actorGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete an existing to do.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteToDo(String          serverName,
                                   String          toDoGUID,
                                   NullRequestBody requestBody)
    {
        final String methodName = "deleteToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            handler.deleteToDo(userId, toDoGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a "To Do" by unique identifier.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     *
     * @return to do bean or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDoResponse getToDo(String serverName,
                                String toDoGUID)
    {
        final String methodName = "getToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDoResponse response = new ToDoResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            response.setElement(handler.getToDo(userId, toDoGUID));
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
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDosResponse getActionsForActionTarget(String                serverName,
                                                   String                elementGUID,
                                                   int                   startFrom,
                                                   int                   pageSize,
                                                   ToDoStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForActionTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDosResponse response = new ToDosResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       requestBody.getToDoStatus(),
                                                                       startFrom,
                                                                       pageSize));
            }
            else
            {
                response.setElements(handler.getActionsForActionTarget(userId,
                                                                       elementGUID,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize));
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
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDosResponse getActionsForSponsor(String                serverName,
                                              String                elementGUID,
                                              int                   startFrom,
                                              int                   pageSize,
                                              ToDoStatusRequestBody requestBody)
    {
        final String methodName = "getActionsForSponsor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDosResponse response = new ToDosResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  requestBody.getToDoStatus(),
                                                                  startFrom,
                                                                  pageSize));
            }
            else
            {
                response.setElements(handler.getActionsForSponsor(userId,
                                                                  elementGUID,
                                                                  null,
                                                                  startFrom,
                                                                  pageSize));
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
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDosResponse getAssignedActions(String                serverName,
                                            String                actorGUID,
                                            int                   startFrom,
                                            int                   pageSize,
                                            ToDoStatusRequestBody requestBody)
    {
        final String methodName = "getAssignedActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDosResponse response = new ToDosResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                requestBody.getToDoStatus(),
                                                                startFrom,
                                                                pageSize));
            }
            else
            {
                response.setElements(handler.getAssignedActions(userId,
                                                                actorGUID,
                                                                null,
                                                                startFrom,
                                                                pageSize));
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
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDosResponse findToDos(String                 serverName,
                                   int                    startFrom,
                                   int                    pageSize,
                                   boolean                startsWith,
                                   boolean                endsWith,
                                   boolean                ignoreCase,
                                   ToDoStatusSearchString requestBody)
    {
        final String methodName = "findToDos";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDosResponse response = new ToDosResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findToDos(userId,
                                                       instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                       requestBody.getToDoStatus(),
                                                       startFrom,
                                                       pageSize));
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
     * Retrieve the "To Dos" that match the type name and status.
     *
     * @param serverName name of the server instances for this request
     * @param toDoType   type to search for
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDosResponse getToDosByType(String                serverName,
                                        String                toDoType,
                                        int                   startFrom,
                                        int                   pageSize,
                                        ToDoStatusRequestBody requestBody)
    {
        final String methodName = "getToDosByType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ToDosResponse response = new ToDosResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ToDoActionHandler handler = instanceHandler.getToDoActionManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getToDosByType(userId,
                                                            toDoType,
                                                            requestBody.getToDoStatus(),
                                                            startFrom,
                                                            pageSize));
            }
            else
            {
                response.setElements(handler.getToDosByType(userId,
                                                            toDoType,
                                                            null,
                                                            startFrom,
                                                            pageSize));
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
     * Extract the named property from the extended properties.
     *
     * @param extendedProperties extended properties from the repositories
     * @param propertyName name of property
     * @return property
     */
    private String getExtendedProperty(Map<String, Object> extendedProperties,
                                       String              propertyName)
    {
        if (extendedProperties != null)
        {
            Object propertyValue = extendedProperties.get(propertyName);

            if (propertyValue != null)
            {
                return propertyValue.toString();
            }
        }

        return null;
    }


    /**
     * Extract the named property from the extended properties.
     *
     * @param extendedProperties extended properties from the repositories
     * @param propertyName name of property
     * @return property
     */
    private boolean getExtendedBooleanProperty(Map<String, Object> extendedProperties,
                                               String              propertyName)
    {
        if (extendedProperties != null)
        {
            Object propertyValue = extendedProperties.get(propertyName);

            if (propertyValue instanceof Boolean booleanValue)
            {
                return booleanValue;
            }
        }

        return true;
    }
}
