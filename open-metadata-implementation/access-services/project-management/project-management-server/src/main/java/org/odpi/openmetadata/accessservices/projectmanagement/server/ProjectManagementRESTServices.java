/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server;


import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectElement;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.AssignmentScopeProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectTeamProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ResourceListProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.StakeholderProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ActorProfileListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.PersonRoleListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ProjectHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * The ProjectManagementRESTServices provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class ProjectManagementRESTServices
{
    private static final ProjectManagementInstanceHandler instanceHandler = new ProjectManagementInstanceHandler();

    private final RESTExceptionHandler  restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(ProjectManagementRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ProjectManagementRESTServices()
    {
    }


    /**
     * Create a new metadata element to represent a project.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createProject(String                   serverName,
                                      String                   userId,
                                      ReferenceableRequestBody requestBody)
    {
        final String methodName = "createProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectProperties properties)
                {
                    String projectGUID = handler.createProject(userId,
                                                               requestBody.getExternalSourceGUID(),
                                                               requestBody.getExternalSourceName(),
                                                               properties.getQualifiedName(),
                                                               properties.getIdentifier(),
                                                               properties.getName(),
                                                               properties.getDescription(),
                                                               properties.getStartDate(),
                                                               properties.getPlannedEndDate(),
                                                               properties.getProjectPhase(),
                                                               properties.getProjectHealth(),
                                                               properties.getProjectStatus(),
                                                               properties.getPriority(),
                                                               properties.getAdditionalProperties(),
                                                               properties.getTypeName(),
                                                               properties.getExtendedProperties(),
                                                               false,
                                                               false,
                                                               null,
                                                               properties.getEffectiveFrom(),
                                                               properties.getEffectiveTo(),
                                                               new Date(),
                                                               methodName);

                    if (projectGUID != null)
                    {
                        handler.setVendorProperties(userId,
                                                    projectGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }

                    response.setGUID(projectGUID);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a project.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateProject(String                   serverName,
                                      String                   userId,
                                      String                   projectGUID,
                                      boolean                  isMergeUpdate,
                                      ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateProject";
        final String projectGUIDParameterName = "projectGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectProperties properties)
                {
                    handler.updateProject(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          projectGUID,
                                          projectGUIDParameterName,
                                          properties.getQualifiedName(),
                                          properties.getIdentifier(),
                                          properties.getName(),
                                          properties.getDescription(),
                                          properties.getStartDate(),
                                          properties.getPlannedEndDate(),
                                          properties.getProjectPhase(),
                                          properties.getProjectHealth(),
                                          properties.getProjectStatus(),
                                          properties.getPriority(),
                                          properties.getAdditionalProperties(),
                                          properties.getTypeName(),
                                          properties.getExtendedProperties(),
                                          properties.getEffectiveFrom(),
                                          properties.getEffectiveTo(),
                                          isMergeUpdate,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);

                    if ((! isMergeUpdate) || (properties.getVendorProperties() != null))
                    {
                        handler.setVendorProperties(userId,
                                                    projectGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a ProjectTeam relationship between a project and  an actor profile (typically a team).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the actor
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectTeam(String                  serverName,
                                         String                  userId,
                                         String                  projectGUID,
                                         String                  projectRoleGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName                   = "setupProjectTeam";
        final String projectGUIDParameterName     = "projectGUID";
        final String projectRoleGUIDParameterName = "projectRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProjectTeamProperties properties)
                {
                    handler.addActorToProject(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             properties.getTeamRole(),
                                             properties.getEffectiveFrom(),
                                             properties.getEffectiveTo(),
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addActorToProject(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             null,
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProjectTeamProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addActorToProject(userId,
                                          null,
                                          null,
                                          projectGUID,
                                          projectGUIDParameterName,
                                          projectRoleGUID,
                                          projectRoleGUIDParameterName,
                                          null,
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a ProjectTeam relationship between a project and an actor profile (typically a team).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param actorProfileGUID unique identifier of the actor
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectTeam(String                    serverName,
                                         String                    userId,
                                         String                    projectGUID,
                                         String                    actorProfileGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName                    = "clearProjectTeam";
        final String projectGUIDParameterName      = "projectGUID";
        final String actorProfileGUIDParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeActorFromProject(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               projectGUID,
                                               projectGUIDParameterName,
                                               actorProfileGUID,
                                               actorProfileGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                handler.removeActorFromProject(userId,
                                               null,
                                               null,
                                               projectGUID,
                                               projectGUIDParameterName,
                                               actorProfileGUID,
                                               actorProfileGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between a project and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProjectManagementRole(String                  serverName,
                                                   String                  userId,
                                                   String                  projectGUID,
                                                   String                  projectRoleGUID,
                                                   RelationshipRequestBody requestBody)
    {
        final String methodName                   = "setupProjectManagementRole";
        final String projectGUIDParameterName     = "projectGUID";
        final String projectRoleGUIDParameterName = "projectRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    ProjectTeamProperties properties = (ProjectTeamProperties) requestBody.getProperties();

                    handler.addProjectManager(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             properties.getEffectiveFrom(),
                                             properties.getEffectiveTo(),
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else
                {
                    handler.addProjectManager(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
            }
            else
            {
                handler.addProjectManager(userId,
                                          null,
                                          null,
                                          projectGUID,
                                          projectGUIDParameterName,
                                          projectRoleGUID,
                                          projectRoleGUIDParameterName,
                                          null,
                                          null,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a project and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProjectManagementRole(String                    serverName,
                                                   String                    userId,
                                                   String                    projectGUID,
                                                   String                    projectRoleGUID,
                                                   ExternalSourceRequestBody requestBody)
    {
        final String methodName                     = "clearProjectManagementRole";
        final String projectGUIDParameterName       = "projectGUID";
        final String projectRoleGUIDParameterName   = "projectRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeProjectManager(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                handler.removeProjectManager(userId,
                                             null,
                                             null,
                                             projectGUID,
                                             projectGUIDParameterName,
                                             projectRoleGUID,
                                             projectRoleGUIDParameterName,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a project.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeProject(String                    serverName,
                                      String                    userId,
                                      String                    projectGUID,
                                      ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeProject";
        final String projectGUIDParameterName = "projectGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeProject(userId,
                                      requestBody.getExternalSourceGUID(),
                                      requestBody.getExternalSourceName(),
                                      projectGUID,
                                      projectGUIDParameterName,
                                      false,
                                      false,
                                      new Date(),
                                      methodName);
            }
            else
            {
                handler.removeProject(userId,
                                      null,
                                      null,
                                      projectGUID,
                                      projectGUIDParameterName,
                                      false,
                                      false,
                                      new Date(),
                                      methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of project metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProjectListResponse findProjects(String                  serverName,
                                            String                  userId,
                                            SearchStringRequestBody requestBody,
                                            int                     startFrom,
                                            int                     pageSize)
    {
        final String methodName = "findProjects";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProjectListResponse response = new ProjectListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                List<ProjectElement> projects = handler.findProjects(userId,
                                                                     requestBody.getSearchString(),
                                                                     searchStringParameterName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName);

                response.setElements(setUpVendorProperties(userId, projects, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of project metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProjectListResponse getProjectsByName(String          serverName,
                                                 String          userId,
                                                 NameRequestBody requestBody,
                                                 int             startFrom,
                                                 int             pageSize)
    {
        final String methodName = "getProjectsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProjectListResponse response = new ProjectListResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

                List<ProjectElement> projects = handler.getProjectsByName(userId,
                                                                          requestBody.getName(),
                                                                          nameParameterName,
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

                response.setElements(setUpVendorProperties(userId, projects, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return information about the project management roles linked to a project.
     *
     * @param serverName called server
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRoleListResponse getProjectManagementRoles(String          serverName,
                                                            String          userId,
                                                            String          projectGUID,
                                                            int             startFrom,
                                                            int             pageSize)
    {
        final String methodName         = "getProjectManagementRoles";
        final String guidParameterName  = "projectGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRoleListResponse response = new PersonRoleListResponse();
        AuditLog               auditLog = null;

        try
        {
            PersonRoleHandler<PersonRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getProjectManagerRoles(userId,
                                                                projectGUID,
                                                                guidParameterName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Return information about the actors linked to a project.
     *
     * @param serverName called server
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileListResponse getProjectActors(String          serverName,
                                                     String          userId,
                                                     String          projectGUID,
                                                     int             startFrom,
                                                     int             pageSize)
    {
        final String methodName         = "getProjectActors";
        final String guidParameterName  = "projectGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileListResponse response = new ActorProfileListResponse();
        AuditLog                 auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getActorsForProject(userId,
                                                             projectGUID,
                                                             guidParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of project metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProjectListResponse getProjects(String          serverName,
                                           String          userId,
                                           int             startFrom,
                                           int             pageSize)
    {
        final String methodName = "getProjects";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProjectListResponse response = new ProjectListResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            List<ProjectElement> projects = handler.getProjects(userId,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);

            response.setElements(setUpVendorProperties(userId, projects, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the project metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProjectResponse getProjectByGUID(String serverName,
                                            String userId,
                                            String guid)
    {
        final String methodName = "getProjectByGUID";
        final String guidParameterName = "projectGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProjectResponse response = new ProjectResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProjectHandler<ProjectElement> handler = instanceHandler.getProjectHandler(userId, serverName, methodName);

            ProjectElement project = handler.getProjectByGUID(userId,
                                                              guid,
                                                              guidParameterName,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName);

            response.setElement(setUpVendorProperties(userId, project, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ProjectElement> setUpVendorProperties(String                              userId,
                                                       List<ProjectElement>             retrievedResults,
                                                       ProjectHandler<ProjectElement> handler,
                                                       String                             methodName) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (ProjectElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ProjectElement setUpVendorProperties(String                         userId,
                                                 ProjectElement                 element,
                                                 ProjectHandler<ProjectElement> handler,
                                                 String                         methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            ProjectProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }


    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupMoreInformation(String                  serverName,
                                             String                  userId,
                                             String                  elementGUID,
                                             String                  detailGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               requestBody.getProperties().getEffectiveFrom(),
                                               requestBody.getProperties().getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearMoreInformation(String                    serverName,
                                             String                    userId,
                                             String                    elementGUID,
                                             String                    detailGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeMoreInformation(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.removeMoreInformation(userId,
                                              null,
                                              null,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedElementListResponse getMoreInformation(String serverName,
                                                         String userId,
                                                         String elementGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getMoreInformation";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getMoreInformation(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getDescriptiveElements(String serverName,
                                                              String userId,
                                                              String detailGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getDescriptiveElements";
        final String guidPropertyName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getDescriptiveElements(userId,
                                                                detailGUID,
                                                                guidPropertyName,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupStakeholder(String                  serverName,
                                         String                  userId,
                                         String                  elementGUID,
                                         String                  stakeholderGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName                   = "setupStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof StakeholderProperties properties)
                {
                    handler.addStakeholder(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           elementGUID,
                                           elementGUIDParameterName,
                                           stakeholderGUID,
                                           stakeholderGUIDParameterName,
                                           properties.getStakeholderRole(),
                                           properties.getEffectiveFrom(),
                                           properties.getEffectiveTo(),
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addStakeholder(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               stakeholderGUID,
                                               stakeholderGUIDParameterName,
                                               null,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StakeholderProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearStakeholder(String                    serverName,
                                         String                    userId,
                                         String                    elementGUID,
                                         String                    stakeholderGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName                   = "clearStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeStakeholder(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
            else
            {
                handler.removeStakeholder(userId,
                                          null,
                                          null,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholders(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getStakeholders";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getStakeholders(userId,
                                                         elementGUID,
                                                         guidPropertyName,
                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholderCommissionedElements(String serverName,
                                                                          String userId,
                                                                          String stakeholderGUID,
                                                                          int    startFrom,
                                                                          int    pageSize)
    {
        final String methodName        = "getStakeholderCommissionedElements";
        final String guidPropertyName  = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getCommissionedByStakeholder(userId,
                                                                      stakeholderGUID,
                                                                      guidPropertyName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create an "AssignmentScope" relationship between an element and its scope.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param scopeGUID unique identifier of the scope
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAssignmentScope(String                  serverName,
                                             String                  userId,
                                             String                  elementGUID,
                                             String                  scopeGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.addAssignmentScope(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               scopeGUID,
                                               scopeGUIDParameterName,
                                               properties.getAssignmentType(),
                                               properties.getDescription(),
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addAssignmentScope(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               scopeGUID,
                                               scopeGUIDParameterName,
                                               null,
                                               null,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param scopeGUID unique identifier of the scope
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssignmentScope(String                    serverName,
                                             String                    userId,
                                             String                    elementGUID,
                                             String                    scopeGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeAssignmentScope(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              elementGUID,
                                              elementGUIDParameterName,
                                              scopeGUID,
                                              scopeGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.removeAssignmentScope(userId,
                                              null,
                                              null,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              scopeGUID,
                                              scopeGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the assigned scopes linked by the "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getAssignedScopes(String serverName,
                                                         String userId,
                                                         String elementGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getAssignedScopes";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getAssignmentScope(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the assigned actors linked by the "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param scopeGUID unique identifier of the scope
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getAssignedActors(String serverName,
                                                         String userId,
                                                         String scopeGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getAssignedActors";
        final String guidPropertyName  = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getAssignedActors(userId,
                                                           scopeGUID,
                                                           guidPropertyName,
                                                           OpenMetadataType.REFERENCEABLE.typeName,
                                                           OpenMetadataType.REFERENCEABLE.typeName,
                                                           startFrom,
                                                           pageSize,
                                                           false,
                                                           false,
                                                           new Date(),
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupResource(String                  serverName,
                                      String                  userId,
                                      String                  elementGUID,
                                      String                  resourceGUID,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName                = "setupResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties properties)
                {
                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   properties.getResourceUse(),
                                                   properties.getResourceUseDescription(),
                                                   properties.getResourceUseProperties(),
                                                   properties.getWatchResource(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   null,
                                                   null,
                                                   null,
                                                   false,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearResource(String                    serverName,
                                      String                    userId,
                                      String                    elementGUID,
                                      String                    resourceGUID,
                                      ExternalSourceRequestBody requestBody)
    {
        final String methodName                = "clearResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeResourceListMember(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                handler.removeResourceListMember(userId,
                                                 null,
                                                 null,
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getResourceList(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getResourceList";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getResourceList(userId,
                                                         elementGUID,
                                                         guidPropertyName,
                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getSupportedByResource(String serverName,
                                                              String userId,
                                                              String resourceGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getSupportedByResource";
        final String guidPropertyName  = "resourceGUID";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getSupportedByResource(userId,
                                                                resourceGUID,
                                                                guidPropertyName,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}