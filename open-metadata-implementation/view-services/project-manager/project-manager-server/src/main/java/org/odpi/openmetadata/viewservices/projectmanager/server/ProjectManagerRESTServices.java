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
     * Returns the list of projects that are linked off of the supplied element.
     *
     * @param serverName     name of called server
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the projects hang off of
     * @param requestBody filter response by project status - if null, any value will do
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getLinkedProjects(String            serverName,
                                                              String            parentGUID,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getLinkedProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Returns the list of actors that are linked off of the project.
     *
     * @param serverName     name of called server
     * @param projectGUID     unique identifier of the project
     * @param requestBody    filter response by team role
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getProjectTeam(String            serverName,
                                                           String            projectGUID,
                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getClassifiedProjects(String            serverName,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getClassifiedProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findProjects(String                  serverName,
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getProjectsByName(String            serverName,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getProjectsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProject(String         serverName,
                                                      String         projectGUID,
                                                      GetRequestBody requestBody)
    {
        final String methodName = "getProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProjectGraph(String             serverName,
                                                           String             projectGUID,
                                                           ResultsRequestBody requestBody)

    {
        final String methodName = "getProjectGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getProjectHierarchy(String             serverName,
                                                               String             projectGUID,
                                                               ResultsRequestBody requestBody)

    {
        final String methodName = "getProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createProject(String                serverName,
                                      String                optionalClassificationName,
                                      NewElementRequestBody requestBody)
    {
        final String methodName = "createProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new project with the Task classification.  Used to identify the top of a
     * project hierarchy.
     *
     * @param serverName                 name of called server.
     * @param projectGUID             unique identifier of the project
     * @param properties             properties for the project.
     *
     * @return unique identifier of the newly created Project
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createTaskForProject(String            serverName,
                                             String            projectGUID,
                                             ProjectProperties properties)
    {
        final String methodName = "createTaskForProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (properties != null)
            {
                ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                NewElementOptions newElementOptions = new NewElementOptions();

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createProjectFromTemplate(String              serverName,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createProjectFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the properties of a project.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project (returned from create)
     * @param requestBody     properties for the project.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateProject(String                   serverName,
                                        String                   projectGUID,
                                        UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

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
                    handler.updateProject(userId, projectGUID, requestBody, projectProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateProject(userId, projectGUID, requestBody, null);
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteProject(String                   serverName,
                                      String                   projectGUID,
                                      DeleteRequestBody requestBody)
    {
        final String methodName = "deleteProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            handler.removeProject(userId, projectGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addToProjectTeam(String                  serverName,
                                         String                  projectGUID,
                                         String                  actorGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addToProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                    handler.setupProjectTeam(userId, projectGUID, actorGUID, null, projectTeamProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupProjectTeam(userId, projectGUID, actorGUID, null, null);
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeFromProjectTeam(String                   serverName,
                                              String                   projectGUID,
                                              String                   actorGUID,
                                              DeleteRequestBody requestBody)
    {
        final String methodName = "removeFromProjectTeam";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectDependency(String                  serverName,
                                               String                  projectGUID,
                                               String                  dependsOnProjectGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupProjectDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectDependency(String                   serverName,
                                               String                   projectGUID,
                                               String                   dependsOnProjectGUID,
                                               DeleteRequestBody requestBody)
    {
        final String methodName = "clearProjectDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectHierarchy(String                  serverName,
                                              String                  projectGUID,
                                              String                  managedProjectGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectHierarchy(String                   serverName,
                                              String                   projectGUID,
                                              String                   managedProjectGUID,
                                              DeleteRequestBody requestBody)
    {
        final String methodName = "clearProjectHierarchy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
