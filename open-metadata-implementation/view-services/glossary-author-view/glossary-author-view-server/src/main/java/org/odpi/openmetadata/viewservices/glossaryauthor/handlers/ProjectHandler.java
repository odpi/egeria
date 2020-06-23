/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.client.entities.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The project handler is initialised with a SubjectAreaProject, that contains the server the call should be sent to.
 * The handler exposes methods for project functionality for the glossary author view
 */
public class ProjectHandler {
    private SubjectAreaProject subjectAreaProject;

    /**
     * Constructor for the ProjectHandler
     *
     * @param subjectAreaProject The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for projects. This is the same as the
     *                           The SubjectAreaDefinition Open Metadata View Service (OMVS) API for projects.
     */
    public ProjectHandler(SubjectAreaProject subjectAreaProject) {
        this.subjectAreaProject = subjectAreaProject;
    }

    /**
     * Create a Project.
     * <p>
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     * <p>
     * Projects that are created using this call will be GlossaryProjects.
     * <p>
     *
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return the created Project.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Project createProject(String userId, Project suppliedProject) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return subjectAreaProject.project().create(userId, suppliedProject);
    }

    /**
     * Get a Project by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the Project to get
     * @return the requested Project.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Project getProjectByGuid(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().getByGUID(userId, guid);
    }

    /**
     * Get Project relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the Project to get
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Project guid
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public List<Line> getProjectRelationships(String userId, String guid, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().getRelationships(userId, guid, findRequest);
    }

    /**
     * Find Project
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Projects meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public List<Project> findProject(String userId, FindRequest findRequest) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().find(userId, findRequest);
    }

    /**
     * Replace a Project. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId          userId under which the request is performed
     * @param guid            guid of the Project to update
     * @param suppliedProject Project to be updated
     * @return replaced Project
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Project replaceProject(String userId, String guid, Project suppliedProject) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().replace(userId, guid, suppliedProject);
    }

    /**
     * Update a Project. This means to update the Project with any non-null attributes from the supplied Project.
     * <p>
     * Status is not updated using this call.
     *
     * @param userId          userId under which the request is performed
     * @param guid            guid of the Project to update
     * @param suppliedProject Project to be updated
     * @return a response which when successful contains the updated Project
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Project updateProject(String userId, String guid, Project suppliedProject) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().update(userId, guid, suppliedProject);
    }

    /**
     * Delete a Project instance
     * <p>
     * The deletion of a Project is only allowed if there is no Project content
     * <p>
     * A delete (also known as a soft delete) means that the Project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the Project to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public void deleteProject(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaProject.project().delete(userId, guid);
    }

    /**
     * Purge a Project instance
     * <p>
     * The purge of a Project is only allowed if there is no Project content.
     * <p>
     * A purge means that the Project will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the Project to be deleted.
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              Property Server Exception
     */
    public void purgeProject(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        subjectAreaProject.project().purge(userId, guid);
    }

    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to restore
     * @return the restored Project
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws PropertyServerException              property server exception
     */
    public Project restoreProject(String userId, String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return subjectAreaProject.project().restore(userId, guid);
    }
}