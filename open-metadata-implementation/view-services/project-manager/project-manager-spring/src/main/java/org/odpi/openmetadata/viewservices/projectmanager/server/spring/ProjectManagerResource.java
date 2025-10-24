/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.projectmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.viewservices.projectmanager.server.ProjectManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ProjectManagerResource provides the Spring API endpoints of the Project Manager Open Metadata View Service (OMVS).
= */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/project-manager")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Project Manager OMVS",
     description="Maintain and explore the contents of nested projects. These projects can be used organize activity aiming to achieve a specific goal.",
     externalDocs=@ExternalDocumentation(description="Further Information",
             url="https://egeria-project.org/services/omvs/project-manager/overview/"))

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
     * @param requestBody    filter response by project status - if null, any value will do
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{parentGUID}/projects")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLinkedProjects",
            description="Returns the list of projects that are linked off of the supplied element. Any relationship will do.  The request body is optional, but if supplied acts as a filter on project status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementsResponse getLinkedProjects(@PathVariable String            serverName,
                                                              @PathVariable String            parentGUID,
                                                              @RequestBody(required = false)
                                                                  FilterRequestBody requestBody)
    {
        return restAPI.getLinkedProjects(serverName, parentGUID, requestBody);
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
    @PostMapping(path = "/projects/by-classifications")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getClassifiedProjects",
            description="Returns the list of projects with a particular classification.  The name of the classification is supplied in the request body.  Examples of these classifications include StudyProject, PersonalProjectProperties, Campaign or Task.  There is also GlossaryProject and GovernanceProject.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementsResponse getClassifiedProjects(@PathVariable String            serverName,
                                                                  @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getClassifiedProjects(serverName, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/team")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProjectTeam",
            description="Returns the list of actors that are linked off of the project.  " +
                    "This includes the project managers. The optional request body allows a teamRole to be " +
                    "specified as a filter.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementsResponse getProjectTeam(@PathVariable String            serverName,
                                                           @PathVariable String            projectGUID,
                                                           @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getProjectTeam(serverName, projectGUID, requestBody);
    }


    /**
     * Returns the list of projects matching the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findProjects",
            description="Returns the list of projects matching the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementsResponse findProjects(@PathVariable String            serverName,
                                                         @RequestBody  (required = false)
                                                         SearchStringRequestBody requestBody)
    {
        return restAPI.findProjects(serverName, requestBody);
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
    @PostMapping(path = "/projects/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProjectsByName",
            description="Returns the list of projects with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementsResponse getProjectsByName(@PathVariable String            serverName,
                                                              @RequestBody(required = false)
                                                              FilterRequestBody requestBody)
    {
        return restAPI.getProjectsByName(serverName, requestBody);
    }


    /**
     * Return the properties of a specific project.
     *
     * @param serverName         name of called server
     * @param projectGUID unique identifier of the required project
     * @param requestBody optional properties to control the query
     *
     * @return project properties
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProjectByGUID",
            description="Return the properties of a specific project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public OpenMetadataRootElementResponse getProject(@PathVariable String serverName,
                                                      @PathVariable String projectGUID,
                                                      @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getProject(serverName, projectGUID, requestBody);
    }



    /**
     * Returns the graph of related projects and resources starting with a supplied project guid.
     *
     * @param serverName         name of called server
     * @param projectGUID     unique identifier of the starting project
     * @param requestBody optional properties to control the query
     *
     * @return a graph of projects or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/graph")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProjectGraph",
            description="Returns the graph of related projects and resources starting with a supplied project guid.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))
    public OpenMetadataRootElementResponse getProjectGraph(@PathVariable String serverName,
                                                           @PathVariable String projectGUID,
                                                           @RequestBody(required = false)
                                                               ResultsRequestBody requestBody)
    {
        return restAPI.getProjectGraph(serverName, projectGUID, requestBody);
    }



    /**
     * Returns the hierarchy of managed projects and resources starting with a supplied project guid.
     *
     * @param serverName         name of called server
     * @param projectGUID     unique identifier of the starting project
     * @param requestBody optional properties to control the query
     *
     * @return a graph of projects or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/hierarchy")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProjectHierarchy",
            description="Returns the hierarchy of managed projects and resources starting with a supplied project guid.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))
    public OpenMetadataRootElementResponse getProjectHierarchy(@PathVariable String serverName,
                                                               @PathVariable String projectGUID,
                                                               @RequestBody(required = false)
                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getProjectHierarchy(serverName, projectGUID, requestBody);
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createProject",
            description="Create a new generic project with an optional classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public GUIDResponse createProject(@PathVariable String                serverName,
                                      @RequestParam(required = false)
                                                    String                classificationName,
                                      @RequestBody  NewElementRequestBody requestBody)
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
    @SecurityRequirement(name = "BearerAuthorization")

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
    @SecurityRequirement(name = "BearerAuthorization")

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
     * @param requestBody     properties for the project.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateProject",
            description="Update the properties of a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse updateProject(@PathVariable String            serverName,
                                      @PathVariable String            projectGUID,
                                      @RequestBody(required = false)
                                                    UpdateElementRequestBody requestBody)
    {
        return restAPI.updateProject(serverName, projectGUID, requestBody);
    }


    /**
     * Delete a project.  It is detected from all parent elements.  If members are anchored to the project
     * then they are also deleted.
     *
     * @param serverName         name of called server.
     * @param projectGUID unique identifier of the project.
     * @param requestBody  null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/projects/{projectGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteProject",
            description="Delete a project.  It is detected from all parent elements.  If members are anchored to the project then they are also deleted.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse deleteProject(@PathVariable String          serverName,
                                      @PathVariable String          projectGUID,
                                      @RequestBody(required = false)
                                          DeleteElementRequestBody requestBody)
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addToProjectTeam",
            description="Add an actor to a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse addToProjectTeam(@PathVariable String                serverName,
                                         @PathVariable String                projectGUID,
                                         @PathVariable String                actorGUID,
                                         @RequestBody(required = false)
                                             NewRelationshipRequestBody requestBody)
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeFromProjectTeam",
            description="Remove an actor from a project.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse removeFromProjectTeam(@PathVariable String          serverName,
                                              @PathVariable String          projectGUID,
                                              @PathVariable String          actorGUID,
                                              @RequestBody(required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeFromProjectTeam(serverName, projectGUID, actorGUID, requestBody);
    }


    /**
     * Create a project hierarchy relationship between two projects.
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
    @PostMapping(path = "/projects/{projectGUID}/project-hierarchies/{managedProjectGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupProjectHierarchy",
            description="Create a project hierarchy relationship between two projects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse setupProjectHierarchy(@PathVariable String          serverName,
                                              @PathVariable String          projectGUID,
                                              @PathVariable String          managedProjectGUID,
                                              @RequestBody(required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupProjectHierarchy(serverName, projectGUID, managedProjectGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/project-hierarchies/{managedProjectGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearProjectHierarchy",
            description="Remove a project hierarchy relationship between two projects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse clearProjectHierarchy(@PathVariable String          serverName,
                                              @PathVariable String          projectGUID,
                                              @PathVariable String          managedProjectGUID,
                                              @RequestBody(required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.clearProjectHierarchy(serverName, projectGUID, managedProjectGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/project-dependencies/{dependsOnProjectGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupProjectDependency",
            description="Create a project dependency relationship between two projects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse setupProjectDependency(@PathVariable String          serverName,
                                                   @PathVariable String          projectGUID,
                                                   @PathVariable String          dependsOnProjectGUID,
                                                   @RequestBody(required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupProjectDependency(serverName, projectGUID, dependsOnProjectGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/project-dependencies/{dependsOnProjectGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearProjectDependency",
            description="Remove a project dependency relationship between two projects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/project"))

    public VoidResponse clearProjectDependency(@PathVariable String          serverName,
                                               @PathVariable String          projectGUID,
                                               @PathVariable String          dependsOnProjectGUID,
                                               @RequestBody(required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.clearProjectDependency(serverName, projectGUID, dependsOnProjectGUID, requestBody);
    }
}

