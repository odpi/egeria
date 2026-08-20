/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.projectmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ProjectHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindProjectClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The ProjectManagerRESTServices provides the implementation of the Project Manager Open Metadata View Service (OMVS).
 */

public class ProjectManagerRESTServices extends TokenController
{
    private static final ProjectManagerInstanceHandler instanceHandler = new ProjectManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ProjectManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ProjectManagerRESTServices()
    {
    }

    /* =====================================================================================================================
     * Project Management methods
     */

    /**
     * Returns the list of projects that are linked to the supplied element.
     *
     * @param serverName     name of called server
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the projects hang off of
     * @param requestBody filter response by project status - if null, any value will do
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getLinkedProjects(String            serverName,
                                                              String            parentGUID,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getLinkedProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getLinkedProjects(userId, parentGUID, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getLinkedProjects(userId, parentGUID, null, null));
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
     * Returns the list of actors that are linked from the project.
     *
     * @param serverName     name of called server
     * @param projectGUID     unique identifier of the project
     * @param requestBody    filter response by team role
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getProjectTeam(String            serverName,
                                                           String            projectGUID,
                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProjectMembers(userId, projectGUID, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getProjectMembers(userId, projectGUID, null, null));
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
     * Returns the list of projects with a particular classification.
     *
     * @param serverName         name of called server
     * @param requestBody        name of the classification - if null, all projects are returned
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getClassifiedProjects(String            serverName,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getClassifiedProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getClassifiedProjects(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getClassifiedProjects(userId, null,null));
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
     * Returns the list of projects matching the search string - this is coded as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findProjects(String                  serverName,
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findProjects(userId,
                                                          requestBody.getSearchString(),
                                                          requestBody));
            }
            else
            {
                response.setElements(handler.findProjects(userId, null, null));
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
     * Returns the list of projects with a particular name.
     *
     * @param serverName    name of called server
     * @param requestBody      name of the projects to return - match is full text match in qualifiedName or name
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getProjectsByName(String            serverName,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getProjectsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProjectsByName(userId,
                                                               requestBody.getFilter(),
                                                               requestBody));
            }
            else
            {
                response.setElements(handler.getProjectsByName(userId,
                                                               null,
                                                               null));
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
     * Returns the list of projects matching the project classification properties.
     *
     * @param serverName    name of called server
     * @param requestBody      classification properties of the projects to return
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getProjectsByClassificationProperties(String                              serverName,
                                                                                  FindProjectClassificationProperties requestBody)
    {
        final String methodName = "getProjectsByClassificationProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProjectsByClassificationProperties(userId,
                                                                                   requestBody.getApproach(),
                                                                                   requestBody.getManagementStyle(),
                                                                                   requestBody.getResultsUsage(),
                                                                                   requestBody));
            }
            else
            {
                response.setElements(handler.getClassifiedProjects(userId,
                                                                   OpenMetadataType.PROJECT_CLASSIFICATION_CLASSIFICATION.typeName,
                                                                   null));
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
     * Return the properties of a specific project.
     *
     * @param serverName         name of called server
     * @param projectGUID unique identifier of the required project
     * @param requestBody properties to control the query
     *
     * @return project properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProject(String         serverName,
                                                      String         projectGUID,
                                                      GetRequestBody requestBody)
    {
        final String methodName = "getProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            response.setElement(handler.getProjectByGUID(userId, projectGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Returns the graph of related projects and resources starting with a supplied project guid.
     *
     * @param serverName         name of called server
     * @param projectGUID     unique identifier of the starting project
     * @param requestBody properties to control the query
     *
     * @return a graph of projects or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProjectGraph(String             serverName,
                                                           String             projectGUID,
                                                           ResultsRequestBody requestBody)

    {
        final String methodName = "getProjectGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            response.setElement(handler.getProjectInContext(userId, projectGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Returns the graph of managed projects and resources starting with a supplied project guid.
     *
     * @param serverName         name of called server
     * @param projectGUID     unique identifier of the starting project
     * @param requestBody properties to control the query
     *
     * @return a graph of projects or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProjectHierarchy(String             serverName,
                                                               String             projectGUID,
                                                               ResultsRequestBody requestBody)

    {
        final String methodName = "getProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            response.setElement(handler.getProjectHierarchy(userId, projectGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new generic project.
     *
     * @param serverName                 name of called server.
     * @param optionalClassificationName name of project classification
     * @param requestBody             properties for the project.
     *
     * @return unique identifier of the newly created Project
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createProject(String                serverName,
                                      String                optionalClassificationName,
                                      NewElementRequestBody requestBody)
    {
        final String methodName = "createProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof ProjectProperties properties)
                {
                    response.setGUID(handler.createProject(userId,
                                                           requestBody,
                                                           optionalClassificationName,
                                                           requestBody.getInitialClassifications(),
                                                           properties,
                                                           requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new project with the Task classification.  Used to identify the top of a
     * project hierarchy.
     *
     * @param serverName                 name of called server.
     * @param projectGUID             unique identifier of the project
     * @param requestBody             properties for the project.
     *
     * @return unique identifier of the newly created Project
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createTaskForProject(String            serverName,
                                             String            projectGUID,
                                             NewAttachmentRequestBody requestBody)
    {
        final String methodName = "createTaskForProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ProjectProperties properties))
            {
                ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                NewElementOptions newElementOptions = new NewElementOptions(requestBody);

                if (projectGUID != null)
                {
                    newElementOptions.setAnchorGUID(projectGUID);
                    newElementOptions.setIsOwnAnchor(false);
                    newElementOptions.setParentGUID(projectGUID);
                    newElementOptions.setParentAtEnd1(true);
                    newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName);
                }

                response.setGUID(handler.createProject(userId,
                                                       newElementOptions,
                                                       OpenMetadataType.TASK_CLASSIFICATION.typeName,
                                                       null,
                                                       properties,
                                                       null));
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
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new project.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createProjectFromTemplate(String              serverName,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createProjectFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                response.setGUID(handler.createProjectFromTemplate(userId,
                                                                   requestBody,
                                                                   requestBody.getTemplateGUID(),
                                                                   requestBody.getReplacementProperties(),
                                                                   requestBody.getReplacementClassifications(),
                                                                   requestBody.getPlaceholderPropertyValues(),
                                                                   requestBody.getParentRelationshipProperties()));
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
     * Update the properties of a project.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project (returned from create)
     * @param requestBody     properties for the project.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateProject(String                   serverName,
                                         String                   projectGUID,
                                         UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof  ProjectProperties projectProperties)
                {
                    response.setFlag(handler.updateProject(userId, projectGUID, requestBody, projectProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a project.  It is detected from all parent elements.  If members are anchored to the project
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project.
     * @param requestBody delete request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteProject(String                   serverName,
                                      String                   projectGUID,
                                      DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.deleteProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add an actor to a project.
     *
     * @param serverName               name of called server.
     * @param projectGUID       unique identifier of the project.
     * @param requestBody properties describing the membership characteristics.
     * @param actorGUID          unique identifier of the actor.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addToProjectTeam(String                     serverName,
                                         String                     projectGUID,
                                         String                     actorGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addToProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties projectTeamProperties)
                {
                    handler.setupProjectTeam(userId, projectGUID, actorGUID, requestBody, projectTeamProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupProjectTeam(userId, projectGUID, actorGUID, requestBody, null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setupProjectTeam(userId, projectGUID, actorGUID, null, null);
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
     * Remove a ProjectTeam relationship between a project and an actor.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project.
     * @param actorGUID    unique identifier of the element.
     * @param requestBody  null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeFromProjectTeam(String                        serverName,
                                              String                        projectGUID,
                                              String                        actorGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeFromProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectTeam(userId, projectGUID, actorGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a project dependency relationship between two projects.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectDependency(String                     serverName,
                                               String                     projectGUID,
                                               String                     dependsOnProjectGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupProjectDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectDependencyProperties projectManagementProperties)
                {
                    handler.setupProjectDependency(userId,
                                                       projectGUID,
                                                       dependsOnProjectGUID,
                                                       requestBody,
                                                       projectManagementProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupProjectDependency(userId,
                                                       projectGUID,
                                                       dependsOnProjectGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectDependencyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setupProjectDependency(userId,
                                               projectGUID,
                                               dependsOnProjectGUID,
                                               null,
                                               null);
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
     * Remove a project dependency relationship between two projects.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param dependsOnProjectGUID unique identifier of the project it depends on
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectDependency(String                        serverName,
                                               String                        projectGUID,
                                               String                        dependsOnProjectGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "clearProjectDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectDependency(userId, projectGUID, dependsOnProjectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a project hierarchy relationship between two projects.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it manages
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectHierarchy(String                    serverName,
                                              String                     projectGUID,
                                              String                     managedProjectGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectHierarchyProperties projectHierarchyProperties)
                {
                    handler.setupProjectHierarchy(userId,
                                                       projectGUID,
                                                       managedProjectGUID,
                                                       requestBody,
                                                       projectHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupProjectHierarchy(userId,
                                                       projectGUID,
                                                       managedProjectGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setupProjectHierarchy(userId,
                                                   projectGUID,
                                                   managedProjectGUID,
                                                   null,
                                                   null);
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
     * Remove a project hierarchy relationship between two projects.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param managedProjectGUID unique identifier of the project it depends on
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectHierarchy(String                        serverName,
                                              String                        projectGUID,
                                              String                        managedProjectGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "clearProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectHierarchy(userId, projectGUID, managedProjectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Add the ProjectClassification classification for a project.
     *
     * @param serverName  name of the server instance to connect to
     * @param projectGUID unique identifier of the project to classify/reclassify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException the full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse addProjectClassification(String                       serverName,
                                                 String                       projectGUID,
                                                 NewClassificationRequestBody requestBody)
    {
        final String methodName = "addProjectClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectClassificationProperties properties)
                {
                    ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                    handler.addProjectClassification(userId, projectGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectClassificationProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the ProjectClassification classification from a project.
     *
     * @param serverName  name of the server instance to connect to
     * @param projectGUID unique identifier of the project to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException the full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearProjectClassification(String                          serverName,
                                                   String                          projectGUID,
                                                   DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearProjectClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectClassification(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Project classifications
     */

    /**
     * Classify a project to say that it is a campaign - a long running activity made up of many projects.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsCampaign(String                       serverName,
                                             String                       projectGUID,
                                             NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsCampaign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsCampaign(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CampaignProperties properties)
            {
                handler.setProjectAsCampaign(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsCampaign(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CampaignProperties.class.getName(), methodName);
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
     * Remove the campaign designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsCampaign(String                          serverName,
                                               String                          projectGUID,
                                               DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsCampaign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsCampaign(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is a task within a larger project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsTask(String                       serverName,
                                         String                       projectGUID,
                                         NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsTask";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsTask(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof TaskProperties properties)
            {
                handler.setProjectAsTask(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsTask(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(TaskProperties.class.getName(), methodName);
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
     * Remove the task designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsTask(String                          serverName,
                                           String                          projectGUID,
                                           DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsTask";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsTask(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is a personal project used to organize an individual's work.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsPersonalProject(String                       serverName,
                                                    String                       projectGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsPersonalProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsPersonalProject(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof PersonalProjectProperties properties)
            {
                handler.setProjectAsPersonalProject(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsPersonalProject(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(PersonalProjectProperties.class.getName(), methodName);
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
     * Remove the personal project designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsPersonalProject(String                          serverName,
                                                      String                          projectGUID,
                                                      DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsPersonalProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsPersonalProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is a study project that is investigating a topic.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsStudyProject(String                       serverName,
                                                 String                       projectGUID,
                                                 NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsStudyProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsStudyProject(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof StudyProjectProperties properties)
            {
                handler.setProjectAsStudyProject(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsStudyProject(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(StudyProjectProperties.class.getName(), methodName);
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
     * Remove the study project designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsStudyProject(String                          serverName,
                                                   String                          projectGUID,
                                                   DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsStudyProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsStudyProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is an experiment that is testing a hypothesis.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsExperiment(String                       serverName,
                                               String                       projectGUID,
                                               NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsExperiment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsExperiment(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ExperimentProperties properties)
            {
                handler.setProjectAsExperiment(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsExperiment(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ExperimentProperties.class.getName(), methodName);
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
     * Remove the experiment designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsExperiment(String                          serverName,
                                                 String                          projectGUID,
                                                 DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsExperiment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsExperiment(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is managing the development of a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsGlossaryProject(String                       serverName,
                                                    String                       projectGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsGlossaryProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsGlossaryProject(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof GlossaryProjectProperties properties)
            {
                handler.setProjectAsGlossaryProject(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsGlossaryProject(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(GlossaryProjectProperties.class.getName(), methodName);
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
     * Remove the glossary project designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsGlossaryProject(String                          serverName,
                                                      String                          projectGUID,
                                                      DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsGlossaryProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsGlossaryProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to say that it is part of the governance program.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectAsGovernanceProject(String                       serverName,
                                                      String                       projectGUID,
                                                      NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectAsGovernanceProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectAsGovernanceProject(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof GovernanceProjectProperties properties)
            {
                handler.setProjectAsGovernanceProject(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectAsGovernanceProject(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(GovernanceProjectProperties.class.getName(), methodName);
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
     * Remove the governance project designation from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectAsGovernanceProject(String                          serverName,
                                                        String                          projectGUID,
                                                        DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectAsGovernanceProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectAsGovernanceProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify a project to describe the kind of project that it is.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setProjectKind(String                       serverName,
                                       String                       projectGUID,
                                       NewClassificationRequestBody requestBody)
    {
        final String methodName = "setProjectKind";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setProjectKind(userId, projectGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ProjectKindProperties properties)
            {
                handler.setProjectKind(userId, projectGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setProjectKind(userId, projectGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ProjectKindProperties.class.getName(), methodName);
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
     * Remove the project kind classification from a project.
     *
     * @param serverName name of the server to route the request to
     * @param projectGUID unique identifier of the project
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectKind(String                          serverName,
                                         String                          projectGUID,
                                         DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearProjectKind";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.clearProjectKind(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
