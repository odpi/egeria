/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.projectmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectTeamProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.projectmanager.rest.*;
import org.odpi.openmetadata.viewservices.projectmanager.server.ProjectManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ProjectManagerResource provides the Spring API endpoints of the Project Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/project-manager")

@Tag(name="API: Project Manager OMVS",
     description="Maintain and explore the contents of nested projects. These projects can be used to represent digital products, or projects of resources for a particular project or team. They can be used to organize assets and other resources into logical groups.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/project-manager/overview/"))

public class ProjectManagerResource
{

    private final ProjectManagerRESTServices restAPI = new ProjectManagerRESTServices();


    /**
     * Default constructor
     */
    public ProjectManagerResource()
    {
    }

    /* =====================================================================================================================
     * Retrieving Projects: https://egeria-project.org/concepts/project
     */

    /**
     * Returns the list of projects that are linked off of the supplied element. Any relationship will do.
     *
     * @param serverName     name of called server
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the projects hang off of
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     * @param requestBody    filter response by project status - if null, any value will do
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/projects")
    @Operation(summary="getLinkedProjects",
            description="Returns the list of projects that are linked off of the supplied element. Any relationship will do.  The request body is optional, but if supplied acts as a filter on project status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectListResponse getLinkedProjects(@PathVariable String            serverName,
                                                 @PathVariable String            parentGUID,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                               int               startFrom,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                               int               pageSize,
                                                 @RequestBody(required = false)
                                                               FilterRequestBody requestBody)
    {
        return restAPI.getLinkedProjects(serverName, parentGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of projects with a particular classification.
     *
     * @param serverName         name of called server
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     * @param requestBody        name of the classification - if null, all projects are returned
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/by-classifications")
    @Operation(summary="getClassifiedProjects",
            description="Returns the list of projects with a particular classification.  The name of the classification is supplied in the request body.  Examples of these classifications include StudyProject, PersonalProject, Campaign or Task.  There is also GlossaryProject and GovernanceProject.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectListResponse getClassifiedProjects(@PathVariable String            serverName,
                                                     @RequestParam(required = false, defaultValue = "0")
                                                     int               startFrom,
                                                     @RequestParam(required = false, defaultValue = "0")
                                                     int               pageSize,
                                                     @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getClassifiedProjects(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of actors that are linked off of the project.
     *
     * @param serverName     name of called server
     * @param projectGUID     unique identifier of the project
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     * @param requestBody    filter response by team role
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{projectGUID}/projects/team")
    @Operation(summary="getProjectTeam",
            description="Returns the list of actors that are linked off of the project.  " +
                    "This includes the project managers. The optional request body allows a teamRole to be " +
                    "specified as a filter.  To filter out the project managers, " +
                    "specify ProjectManagement as the team role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectMemberListResponse getProjectTeam(@PathVariable String            serverName,
                                                    @PathVariable String            projectGUID,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                                  int               startFrom,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                                  int               pageSize,
                                                    @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getProjectTeam(serverName, projectGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of projects matching the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/by-search-string")
    @Operation(summary="findProjects",
            description="Returns the list of projects matching the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectListResponse findProjects(@PathVariable String            serverName,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean           startsWith,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean           endsWith,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean           ignoreCase,
                                            @RequestParam (required = false, defaultValue = "0")
                                                          int               startFrom,
                                            @RequestParam (required = false, defaultValue = "0")
                                                          int               pageSize,
                                            @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.findProjects(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Returns the list of projects with a particular name.
     *
     * @param serverName    name of called server
     * @param requestBody      name of the projects to return - match is full text match in qualifiedName or name
     * @param startFrom index of the list to start from (0 for start)
     * @param pageSize  maximum number of elements to return
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/by-name")
    @Operation(summary="getProjectsByName",
            description="Returns the list of projects with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectListResponse getProjectsByName(@PathVariable String            serverName,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                               int               startFrom,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                               int               pageSize,
                                                 @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getProjectsByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific project.
     *
     * @param serverName         name of called server
     * @param projectGUID unique identifier of the required project
     *
     * @return project properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/projects/{projectGUID}")
    @Operation(summary="getProjectByGUID",
            description="Return the properties of a specific project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public ProjectResponse getProject(@PathVariable String serverName,
                                      @PathVariable String projectGUID)
    {
        return restAPI.getProject(serverName, projectGUID);
    }


    /**
     * Create a new generic project.
     *
     * @param serverName                 name of called server.
     * @param classificationName name of project classification
     * @param requestBody             properties for the project.
     *
     * @return unique identifier of the newly created Project
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects")
    @Operation(summary="createProject",
            description="Create a new generic project with an optional classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public GUIDResponse createProject(@PathVariable String                serverName,
                                      @RequestParam(required = false)
                                                    String                classificationName,
                                      @RequestBody  NewProjectRequestBody requestBody)
    {
        return restAPI.createProject(serverName, classificationName, requestBody);
    }


    /**
     * Create a new task linked to the supplied project.  Used to identify a discrete piece of work within the project.
     *
     * @param serverName              name of called server
     * @param projectGUID             unique identifier of the project
     * @param requestBody             properties for the project
     *
     * @return unique identifier of the newly created Project
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/task")
    @Operation(summary="createTaskForProject",
            description="Create a new task linked to the supplied project.  Used to identify a discrete piece of work within the project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public GUIDResponse createTaskProject(@PathVariable String            serverName,
                                          @PathVariable String            projectGUID,
                                          @RequestBody  ProjectProperties requestBody)
    {
        return restAPI.createTaskForProject(serverName, projectGUID, requestBody);
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
    @PostMapping(path = "/projects/from-template")
    @Operation(summary="createProjectFromTemplate",
            description="Create a new metadata element to represent a project using an existing metadata element as a template." +
                    " The template defines additional classifications and relationships that should be added to the new project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public GUIDResponse createProjectFromTemplate(@PathVariable String              serverName,
                                                  @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createProjectFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a project.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the project.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/update")
    @Operation(summary="updateProject",
            description="Update the properties of a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse updateProject(@PathVariable String            serverName,
                                      @PathVariable String            projectGUID,
                                      @RequestParam boolean           replaceAllProperties,
                                      @RequestBody  ProjectProperties requestBody)
    {
        return restAPI.updateProject(serverName, projectGUID, replaceAllProperties, requestBody);
    }


    /**
     * Delete a project.  It is detected from all parent elements.  If members are anchored to the project
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/delete")
    @Operation(summary="deleteProject",
            description="Delete a project.  It is detected from all parent elements.  If members are anchored to the project then they are also deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse deleteProject(@PathVariable String          serverName,
                                      @PathVariable String          projectGUID,
                                      @RequestBody(required = false)
                                                    NullRequestBody requestBody)
    {
        return restAPI.deleteProject(serverName, projectGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/members/{actorGUID}/attach")
    @Operation(summary="addToProjectTeam",
            description="Add an actor to a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse addToProjectTeam(@PathVariable String                serverName,
                                         @PathVariable String                projectGUID,
                                         @PathVariable String                actorGUID,
                                         @RequestBody(required = false)
                                                       ProjectTeamProperties requestBody)
    {
        return restAPI.addToProjectTeam(serverName, projectGUID, actorGUID, requestBody);
    }


    /**
     * Remove an actor from a project.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project.
     * @param actorGUID    unique identifier of the actor.
     * @param requestBody  null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem updating information in the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/members/{actorGUID}/detach")
    @Operation(summary="removeFromProjectTeam",
            description="Remove an actor from a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse removeFromProjectTeam(@PathVariable String          serverName,
                                              @PathVariable String          projectGUID,
                                              @PathVariable String          actorGUID,
                                              @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeFromProjectTeam(serverName, projectGUID, actorGUID, requestBody);
    }


    /**
     * Create a ProjectManagement relationship between a project and a person role to show that anyone appointed to the role is a member of the project.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/{projectGUID}/project-management-roles/{projectRoleGUID}/attach")
    @Operation(summary="setupProjectManagementRole",
            description="Create a ProjectManagement relationship between a project and a person role to show that anyone appointed to the role is a member of the project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse setupProjectManagementRole(@PathVariable String          serverName,
                                                   @PathVariable String          projectGUID,
                                                   @PathVariable String          projectRoleGUID,
                                                   @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.setupProjectManagementRole(serverName, projectGUID, projectRoleGUID, requestBody);
    }


    /**
     * Remove a ProjectManagement relationship between a project and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/{projectGUID}/project-management-roles/{projectRoleGUID}/detach")
    @Operation(summary="clearProjectManagementRole",
            description="Remove a ProjectManagement relationship between a project and a person role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse clearProjectManagementRole(@PathVariable String          serverName,
                                                   @PathVariable String          projectGUID,
                                                   @PathVariable String          projectRoleGUID,
                                                   @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.clearProjectManagementRole(serverName, projectGUID, projectRoleGUID, requestBody);
    }
}

