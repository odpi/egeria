/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.services;


import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedExceptionBase;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * The SubjectAreaViewProjectRESTServices provides the org.odpi.openmetadata.viewservices.subjectarea.services implementation of the SubjectArea Open Metadata
 * View Service (OMVS). This interface provides view project authoring interfaces for subject area experts.
 */

public class SubjectAreaViewProjectRESTServices extends BaseSubjectAreaView {

    private static String className = SubjectAreaViewProjectRESTServices.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);

    /**
     * Default constructor
     */
    public SubjectAreaViewProjectRESTServices() {

    }

    /**
     * Create a Project. There are specializations of projects that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Project in the supplied project.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalProject to create a canonical project </li>
     * <li>TaxonomyAndCanonicalProject to create a project that is both a taxonomy and a canonical glosary </li>
     * <li>Project to create a project that is not a taxonomy or a canonical project</li>
     * </ul>
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param suppliedProject Project to create
     * @return response, when successful contains the created project.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse createProject(String serverName, String userId, Project suppliedProject) {
        SubjectAreaOMASAPIResponse response;

        try {
            Project project = instanceHandler.createProject(serverName, userId, suppliedProject);
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProject(project);
            return projectResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Get a project.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the project to get
     * @return response which when successful contains the project with the requested guid
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

    public SubjectAreaOMASAPIResponse getProject(String serverName, String userId, String guid) {
        SubjectAreaOMASAPIResponse response;

        try {
            Project project = instanceHandler.getProjectByGuid(serverName, userId, guid);
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProject(project);
            response = projectResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }

        return response;
    }

    /**
     * Find Project
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Project property values .
     * @param asOfTime           the projects returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findProject(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {
            if (offset == null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }

            List<Project> projects = instanceHandler.findProject(
                    serverName,
                    userId,
                    searchCriteria,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
            ProjectsResponse projectsResponse = new ProjectsResponse();
            projectsResponse.setProjects(projects);
            response = projectsResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Get Project relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the project to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getProjectRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer offset,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


    ) {

        SubjectAreaOMASAPIResponse response;

        try {
            List<Line> lines = instanceHandler.getProjectRelationships(
                    serverName,
                    userId,
                    guid,
                    asOfTime,
                    offset,
                    pageSize,
                    sequencingOrder,
                    sequencingProperty);
            LinesResponse linesResponse = new LinesResponse();
            linesResponse.setLines(lines);
            response = linesResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;

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
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the project to update
     * @param project            project to update
     * @param isReplace          flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse updateProject(
            String serverName,
            String userId,
            String guid,
            Project project,
            Boolean isReplace
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {

            Project updatedProject;
            if (isReplace == null) {
                isReplace = false;
            }
            if (isReplace) {
                updatedProject = instanceHandler.replaceProject(serverName, userId, guid, project);
            } else {
                updatedProject = instanceHandler.updateProject(serverName, userId, guid, project);
            }
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProject(updatedProject);
            response = projectResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Delete a Project instance
     * <p>
     * The deletion of a project is only allowed if there is no project content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the project will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the project to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the project was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the project was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteProject(
            String serverName,
            String userId,
            String guid,
            Boolean isPurge
    ) {
        SubjectAreaOMASAPIResponse response = null;

        try {
            if (isPurge == null) {
                // default to soft delete if isPurge is not specified.
                isPurge = false;
            }

            if (isPurge) {
                instanceHandler.purgeProject(serverName, userId, guid);
                response = new VoidResponse();
            } else {
                Project project = instanceHandler.deleteProject(serverName, userId, guid);
                ProjectResponse projectResponse = new ProjectResponse();
                projectResponse.setProject(project);
                response = projectResponse;
            }
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }

    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the project to restore
     * @return response which when successful contains the restored project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse restoreProject(
            String serverName,
            String userId,
            String guid) {
        SubjectAreaOMASAPIResponse response;

        try {
            Project project = instanceHandler.restoreProject(serverName, userId, guid);
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProject(project);
            response = projectResponse;
        } catch (SubjectAreaCheckedExceptionBase e) {
            response = DetectUtils.getResponseFromException(e);
        }
        return response;
    }
}
