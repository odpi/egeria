/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ActorProfileListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.PersonRoleListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ProjectResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.projectmanagement.server.ProjectManagementRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The ProjectManagementResource provides the server-side implementation of the Project Management Open Metadata
 * Assess Service (OMAS).
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/project-management/users/{userId}")

@Tag(name="Project Management OMAS", description="The Project Management OMAS provides APIs and events for tools and applications that support project leaders - particularly those who are leading governance projects." +
        "\n", externalDocs=@ExternalDocumentation(description="Project Management Open Metadata Access Service (OMAS)",url="https://egeria-project.org/services/omas/project-management/overview/"))

public class ProjectManagementResource
{
    private ProjectManagementRESTServices restAPI = new ProjectManagementRESTServices();

    /**
     * Default constructor
     */
    public ProjectManagementResource()
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
    @PostMapping(path = "/projects")

    public GUIDResponse createProject(@PathVariable String                   serverName,
                                      @PathVariable String                   userId,
                                      @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createProject(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a project using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/from-template/{templateGUID}")

    public GUIDResponse createProjectFromTemplate(@PathVariable String              serverName,
                                                  @PathVariable String              userId,
                                                  @PathVariable String              templateGUID,
                                                  @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createProjectFromTemplate(serverName, userId, templateGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}")

    public VoidResponse updateProject(@PathVariable String                   serverName,
                                      @PathVariable String                   userId,
                                      @PathVariable String                   projectGUID,
                                      @RequestParam boolean                  isMergeUpdate,
                                      @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateProject(serverName, userId, projectGUID, isMergeUpdate, requestBody);
    }


    /**
     * Create a membership relationship between a project and a person role to show that anyone appointed to the role is a member of the project.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/{projectGUID}/project-management-roles/{projectRoleGUID}")

    public VoidResponse setupProjectManagementRole(@PathVariable String                  serverName,
                                                   @PathVariable String                  userId,
                                                   @PathVariable String                  projectGUID,
                                                   @PathVariable String                  projectRoleGUID,
                                                   @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupProjectManagementRole(serverName, userId, projectGUID, projectRoleGUID, requestBody);
    }


    /**
     * Remove a membership relationship between a project and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param projectRoleGUID unique identifier of the person role
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/{projectGUID}/project-management-roles/{projectRoleGUID}/delete")

    public VoidResponse clearProjectManagementRole(@PathVariable String                    serverName,
                                                   @PathVariable String                    userId,
                                                   @PathVariable String                    projectGUID,
                                                   @PathVariable String                    projectRoleGUID,
                                                   @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearProjectManagementRole(serverName, userId, projectGUID, projectRoleGUID, requestBody);
    }


    /**
     * Create a project team relationship between a project and a person role to show that anyone appointed to the role is a member of the project.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param projectGUID unique identifier of the project
     * @param actorProfileGUID unique identifier of the actor
     * @param requestBody external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/projects/{projectGUID}/project-teams/{actorProfileGUID}")

    public VoidResponse setupProjectTeam(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  projectGUID,
                                         @PathVariable String                  actorProfileGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupProjectTeam(serverName, userId, projectGUID, actorProfileGUID, requestBody);
    }


    /**
     * Remove a relationship between a project and a person role.
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
    @PostMapping(path = "/projects/{projectGUID}/project-teams/{actorProfileGUID}/delete")

    public VoidResponse clearProjectTeam(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    projectGUID,
                                         @PathVariable String                    actorProfileGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearProjectTeam(serverName, userId, projectGUID, actorProfileGUID, requestBody);
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
    @PostMapping(path = "/projects/{projectGUID}/delete")

    public VoidResponse removeProject(@PathVariable String                    serverName,
                                        @PathVariable String                    userId,
                                        @PathVariable String                    projectGUID,
                                        @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeProject(serverName, userId, projectGUID, requestBody);
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
    @PostMapping(path = "/projects/by-search-string")

    public ProjectListResponse findProjects(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @RequestBody  SearchStringRequestBody requestBody,
                                            @RequestParam int                     startFrom,
                                            @RequestParam int                     pageSize)
    {
        return restAPI.findProjects(serverName, userId, requestBody, startFrom, pageSize);
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
    @PostMapping(path = "/projects/by-name")

    public ProjectListResponse   getProjectsByName(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @RequestBody NameRequestBody requestBody,
                                                        @RequestParam int             startFrom,
                                                        @RequestParam int             pageSize)
    {
        return restAPI.getProjectsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return information about a person role connected to the named project.
     *
     * @param serverName called server
     * @param userId calling user
     * @param projectGUID unique identifier for the project
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/project-managers/by-project/{projectGUID}")

    public PersonRoleListResponse getProjectManagementRoles(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @PathVariable String          projectGUID,
                                                            @RequestParam int             startFrom,
                                                            @RequestParam int             pageSize)
    {
        return restAPI.getProjectManagementRoles(serverName, userId, projectGUID, startFrom, pageSize);
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
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/project-actors/by-project/{projectGUID}")

    public ActorProfileListResponse getProjectActors(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          projectGUID,
                                                     @RequestParam int             startFrom,
                                                     @RequestParam int             pageSize)
    {
        return restAPI.getProjectActors(serverName, userId, projectGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of project metadata elements.
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
    @GetMapping(path = "/projects")

    public ProjectListResponse   getProjectsByName(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @RequestParam int             startFrom,
                                                        @RequestParam int             pageSize)
    {
        return restAPI.getProjects(serverName, userId, startFrom, pageSize);
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
    @GetMapping(path = "/projects/{guid}")

    public ProjectResponse getProjectByGUID(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String guid)
    {
        return restAPI.getProjectByGUID(serverName, userId, guid);
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
    @PostMapping(path = "/related-elements/{elementGUID}/more-information/{detailGUID}")

    public VoidResponse setupMoreInformation(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  elementGUID,
                                             @PathVariable String                  detailGUID,
                                             @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupMoreInformation(serverName, userId, elementGUID, detailGUID, requestBody);
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
    @PostMapping(path = "/related-elements/{elementGUID}/more-information/{detailGUID}/delete")

    public VoidResponse clearMoreInformation(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    elementGUID,
                                             @PathVariable String                    detailGUID,
                                             @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearMoreInformation(serverName, userId, elementGUID, detailGUID, requestBody);
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
    @GetMapping(path = "/related-elements/more-information/by-descriptive-element/{elementGUID}")

    public  RelatedElementListResponse getMoreInformation(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String elementGUID,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getMoreInformation(serverName, userId, elementGUID, startFrom, pageSize);
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
    @GetMapping(path = "/related-elements/more-information/by-detail-element/{detailGUID}")

    public  RelatedElementListResponse getDescriptiveElements(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String detailGUID,
                                                              @RequestParam int    startFrom,
                                                              @RequestParam int    pageSize)
    {
        return restAPI.getDescriptiveElements(serverName, userId, detailGUID, startFrom, pageSize);
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
    @PostMapping(path = "/related-elements/{elementGUID}/stakeholders/{stakeholderGUID}")

    public VoidResponse setupStakeholder(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  elementGUID,
                                         @PathVariable String                  stakeholderGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupStakeholder(serverName, userId, elementGUID, stakeholderGUID, requestBody);
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
    @PostMapping(path = "/related-elements/{elementGUID}/stakeholders/{stakeholderGUID}/delete")

    public VoidResponse clearStakeholder(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    elementGUID,
                                         @PathVariable String                    stakeholderGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearStakeholder(serverName, userId, elementGUID, stakeholderGUID, requestBody);
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
    @GetMapping(path = "/related-elements/stakeholders/by-commissioned-element/{elementGUID}")

    public  RelatedElementListResponse getStakeholders(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int   startFrom,
                                                       @RequestParam int   pageSize)
    {
        return restAPI.getStakeholders(serverName, userId, elementGUID, startFrom, pageSize);
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
    @GetMapping(path = "/related-elements/stakeholders/by-stakeholder/{stakeholderGUID}")

    public  RelatedElementListResponse getStakeholderCommissionedElements(@PathVariable String serverName,
                                                                          @PathVariable String userId,
                                                                          @PathVariable String stakeholderGUID,
                                                                          @RequestParam int   startFrom,
                                                                          @RequestParam int   pageSize)
    {
        return restAPI.getStakeholderCommissionedElements(serverName, userId, stakeholderGUID, startFrom, pageSize);
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
    @PostMapping(path = "/related-elements/{elementGUID}/assignment-scopes/{scopeGUID}")

    public VoidResponse setupAssignmentScope(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  elementGUID,
                                             @PathVariable String                  scopeGUID,
                                             @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupAssignmentScope(serverName, userId, elementGUID, scopeGUID, requestBody);
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
    @PostMapping(path = "/related-elements/{elementGUID}/assignment-scopes/{scopeGUID}/delete")

    public VoidResponse clearAssignmentScope(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    elementGUID,
                                             @PathVariable String                    scopeGUID,
                                             @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearAssignmentScope(serverName, userId, elementGUID, scopeGUID, requestBody);
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
    @GetMapping(path = "/related-elements/assignment-scopes/by-assigned-actor/{elementGUID}")

    public  RelatedElementListResponse getAssignedScopes(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String elementGUID,
                                                         @RequestParam int   startFrom,
                                                         @RequestParam int   pageSize)
    {
        return restAPI.getAssignedScopes(serverName, userId, elementGUID, startFrom, pageSize);
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
    @GetMapping(path = "/related-elements/assignment-scopes/by-assigned-scope/{scopeGUID}")

    public  RelatedElementListResponse getAssignedActors(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String scopeGUID,
                                                         @RequestParam int   startFrom,
                                                         @RequestParam int   pageSize)
    {
        return restAPI.getAssignedActors(serverName, userId, scopeGUID, startFrom, pageSize);
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
    @PostMapping(path = "/related-elements/{elementGUID}/resource-list/{resourceGUID}")

    public VoidResponse setupResource(@PathVariable String                  serverName,
                                      @PathVariable String                  userId,
                                      @PathVariable String                  elementGUID,
                                      @PathVariable String                  resourceGUID,
                                      @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupResource(serverName, userId, elementGUID, resourceGUID, requestBody);
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
    @PostMapping(path = "/related-elements/{elementGUID}/resource-list/{resourceGUID}/delete")

    public VoidResponse clearResource(@PathVariable String                    serverName,
                                      @PathVariable String                    userId,
                                      @PathVariable String                    elementGUID,
                                      @PathVariable String                    resourceGUID,
                                      @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearResource(serverName, userId, elementGUID, resourceGUID, requestBody);
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
    @GetMapping(path = "/related-elements/resource-list/by-assignee/{elementGUID}")

    public  RelatedElementListResponse getResourceList(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String elementGUID,
                                                       @RequestParam int   startFrom,
                                                       @RequestParam int   pageSize)
    {
        return restAPI.getResourceList(serverName, userId, elementGUID, startFrom, pageSize);
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
    @GetMapping(path = "/related-elements/resource-list/by-resource/{resourceGUID}")

    public RelatedElementListResponse getSupportedByResource(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String resourceGUID,
                                                             @RequestParam int   startFrom,
                                                             @RequestParam int   pageSize)
    {
        return restAPI.getSupportedByResource(serverName, userId, resourceGUID, startFrom, pageSize);
    }
}
