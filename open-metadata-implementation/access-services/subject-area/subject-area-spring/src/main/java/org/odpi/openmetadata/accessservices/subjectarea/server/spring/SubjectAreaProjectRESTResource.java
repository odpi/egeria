/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaProjectRESTServices;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides Project authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/subject-area")
@Tag(name = "Subject Area OMAS", description = "The Subject Area OMAS supports subject matter experts who are documenting their knowledge about a particular subject. This includes glossary terms, reference data, validation rules.", externalDocs = @ExternalDocumentation(description = "Subject Area Open Metadata Access Service (OMAS)", url = "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"))
public class SubjectAreaProjectRESTResource {
    private final SubjectAreaProjectRESTServices restAPI = new SubjectAreaProjectRESTServices();

    /**
     * Default constructor
     */
    public SubjectAreaProjectRESTResource() { }

    /**
     * Create a Project.
     * <p>
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     * <p>
     * Projects that are created using this call will be GlossaryProjects.
     * <p>
     *
     * @param serverName      serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return response, when successful contains the created Project.
     * when not successful the following Exception responses can occur
     * UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * InvalidParameterException            one of the parameters is null or invalid.
     * UnrecognizedGUIDException            the supplied guid was not recognised
     * ClassificationException              Error processing a classification
     * StatusNotSupportedException          A status value is not supported
     */
    @PostMapping(path = "/users/{userId}/projects")
    public SubjectAreaOMASAPIResponse<Project> createProject(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @RequestBody Project suppliedProject) {
        return restAPI.createProject(serverName, userId, suppliedProject);
    }

    /**
     * Get a Project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     userId under which the request is performed
     * @param guid       guid of the Project to get
     * @return response which when successful contains the Project with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException  not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> UnrecognizedGUIDException the supplied guid was not recognised</li>
     * <li> FunctionNotSupportedException   Function not supported</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/projects/{guid}")
    public SubjectAreaOMASAPIResponse<Project> getProject(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String guid) {
        return restAPI.getProjectByGuid(serverName, userId, guid);
    }

    /**
     * Find Project
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Project property values. If not specified then all projects are returned.
     * @param asOfTime           the projects returned as they were at this time. null indicates at the current time.
     * @param startingFrom       the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/projects")
    public SubjectAreaOMASAPIResponse<Project> findProject(@PathVariable String serverName, @PathVariable String userId,
                                                             @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                                                             @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                             @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                             @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                            ) {
        return restAPI.findProject(serverName, userId, searchCriteria, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get Project relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the Project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */


    @GetMapping(path = "/users/{userId}/projects/{guid}/relationships")
    public SubjectAreaOMASAPIResponse<Relationship> getProjectRelationships(@PathVariable String serverName, @PathVariable String userId,
                                                                            @PathVariable String guid,
                                                                            @RequestParam(value = "asOfTime", required = false) Date asOfTime,
                                                                            @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                            @RequestParam(value = "sequencingOrder", required = false) SequencingOrder sequencingOrder,
                                                                            @RequestParam(value = "sequencingProperty", required = false) String sequencingProperty
                                                                           ) {
        return restAPI.getProjectRelationships(serverName, userId, guid, asOfTime, startingFrom, pageSize, sequencingOrder, sequencingProperty);
    }

    /**
     * Get the terms in this project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     * @return a response which when successful contains the Project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @GetMapping(path = "/users/{userId}/projects/{guid}/terms")
    public SubjectAreaOMASAPIResponse<Term> getProjectTerms(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String guid,
                                                            @RequestParam(value = "startingFrom", required = false, defaultValue = "0") Integer startingFrom,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return restAPI.getProjectTerms(serverName, userId, guid, startingFrom, pageSize);
    }

    /**
     * Update a Project
     * <p>
     * If the caller has chosen to incorporate the Project name in their Project Terms or Categories qualified name, renaming the Project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the Project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the Project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Project to update
     * @param Project    Project to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated Project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    @PutMapping(path = "/users/{userId}/projects/{guid}")
    public SubjectAreaOMASAPIResponse<Project> updateProject(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String guid,
                                                             @RequestBody Project Project,
                                                             @RequestParam(value = "isReplace", required = false, defaultValue = "false") Boolean isReplace
    ) {
        return restAPI.updateProject(serverName, userId, guid, Project, isReplace);
    }

    /**
     * Delete a Project instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the Project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the Project will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Project to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the Project was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the Project was not purged</li>
     * </ul>
     */
    @DeleteMapping(path = "/users/{userId}/projects/{guid}")
    public SubjectAreaOMASAPIResponse<Project> deleteProject(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String guid,
                                                             @RequestParam(value = "isPurge", required = false, defaultValue = "false") Boolean isPurge
    ) {
        return restAPI.deleteProject(serverName, userId, guid, isPurge);
    }

    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the Project to restore
     * @return response which when successful contains the restored Project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    @PostMapping(path = "/users/{userId}/projects/{guid}")
    public SubjectAreaOMASAPIResponse<Project> restoreProject(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String guid
    ) {
        return restAPI.restoreProject(serverName, userId, guid);
    }
}