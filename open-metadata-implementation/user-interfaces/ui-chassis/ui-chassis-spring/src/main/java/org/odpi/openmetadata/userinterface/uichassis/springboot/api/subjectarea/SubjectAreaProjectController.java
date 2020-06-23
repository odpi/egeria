/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.subjectarea;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides project authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/api/subject-area/projects")
public class SubjectAreaProjectController extends SecureController {
    private static String className = SubjectAreaProjectController.class.getName();
    private final SubjectAreaProject subjectAreaProject;

    /**
     * Default constructor
     * @param subjectArea main client object for the Subject Area OMAS
     */
    public SubjectAreaProjectController(SubjectArea subjectArea) {
        this.subjectAreaProject = subjectArea.getSubjectAreaProject();
    }

    /**
     * Create a Project.
     *
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     *
     * Projects that are created using this call will be GlossaryProjects.
     * <p>
     * @param suppliedProject Project to create
     * @param request HttpServletRequest the servlet request
     * @return response, when successful contains the created project.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping()
    public SubjectAreaOMASAPIResponse<Project> createProject(@RequestBody Project suppliedProject, HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Project project = this.subjectAreaProject.project().create(userId, suppliedProject);
            response.addResult(project);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }

    /**
     * Get a project.
     * @param guid guid of the project to get
     * @param request HttpServletRequest the servlet request
     * @return response which when successful contains the project with the requested guid
     *  when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Project> getProject(@PathVariable String guid,HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Project project = this.subjectAreaProject.project().getByGUID(userId,guid);
            response.addResult(project);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Find Project
     *
     * @param searchCriteria String expression matching Project property values .
     * @param asOfTime the projects returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request HttpServletRequest the servlet request
     * @return A list of projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping()
    public  SubjectAreaOMASAPIResponse<Project> findProject(
                                                @RequestParam(value = "searchCriteria", required=false) String searchCriteria,
                                                @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                @RequestParam(value = "offset", required = false, defaultValue = PAGE_OFFSET_DEFAULT_VALUE) Integer offset,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize,
                                                @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                HttpServletRequest request
    )  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setOffset(offset);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Project> projects = this.subjectAreaProject.project().find(userId, findRequest);
            response.addAllResults(projects);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Get Project relationships
     *
     * @param guid   guid of the project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @param request HttpServletRequest the servlet request
     * @return a response which when successful contains the project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping( path = "/{guid}/relationships")
    public  SubjectAreaOMASAPIResponse<Line> getProjectRelationships(
                                                            @PathVariable String guid,
                                                            @RequestParam(value = "asOfTime", required=false) Date asOfTime,
                                                            @RequestParam(value = "offset", required = false, defaultValue = PAGE_OFFSET_DEFAULT_VALUE) Integer offset,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE_DEFAULT_VALUE) Integer pageSize,
                                                            @RequestParam(value = "sequencingOrder", required=false) SequencingOrder sequencingOrder,
                                                            @RequestParam(value = "SequencingProperty", required=false) String sequencingProperty,
                                                            HttpServletRequest request
    
    ) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Line> response = new SubjectAreaOMASAPIResponse<>();
        try {
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setOffset(offset);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Line> lines = this.subjectAreaProject.project().getRelationships(userId, guid, findRequest);
            response.addAllResults(lines);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;

    }

    /**
     * Update a Project
     * <p>
     * If the caller has chosen to incorporate the project name in their Project Terms or Categories qualified name, renaming the project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param guid             guid of the project to update
     * @param project         project to update
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @param request HttpServletRequest the servlet request
     * @return a response which when successful contains the updated project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PutMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Project> updateProject(
                                                      @PathVariable String guid,
                                                      @RequestBody Project project,
                                                      @RequestParam(value = "isReplace", required=false, defaultValue = "false") Boolean isReplace,
                                                      HttpServletRequest request) {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Project updatedProject;
            if (isReplace) {
                updatedProject = this.subjectAreaProject.project().replace(userId, guid, project);
            } else {
                updatedProject = this.subjectAreaProject.project().update(userId, guid, project);
            }
            response.addResult(updatedProject);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }

    /**
     * Delete a Project instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the project will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param guid    guid of the project to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @param request HttpServletRequest the servlet request
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @DeleteMapping( path = "/{guid}")
    public  SubjectAreaOMASAPIResponse<Project> deleteProject(@PathVariable String guid,
                                                     @RequestParam(value = "isPurge", required=false, defaultValue = "false") Boolean isPurge,
                                                     HttpServletRequest request)  {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            if (isPurge) {
                this.subjectAreaProject.project().purge(userId,guid);
            } else {
                this.subjectAreaProject.project().delete(userId,guid);
            }

        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
    /**
     * Restore a Project
     *
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     * @param guid       guid of the project to restore
     * @param request HttpServletRequest the servlet request
     * @return response which when successful contains the restored project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @PostMapping( path = "/{guid}")
    public SubjectAreaOMASAPIResponse<Project> restoreProject(@PathVariable String guid, HttpServletRequest request)
    {
        String userId = getUser(request);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            Project project = this.subjectAreaProject.project().restore(userId,guid);
            response.addResult(project);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return  response;
    }
}
