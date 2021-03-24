/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.util.Date;
import java.util.List;

/**
 * The GlossaryAuthorViewProjectRESTServices provides the org.odpi.openmetadata.viewservices.glossaryauthor.services implementation of the Glossary Author Open Metadata
 * View Service (OMVS). This interface provides view project authoring interfaces for subject area experts.
 */

public class GlossaryAuthorViewProjectRESTServices extends BaseGlossaryAuthorView {
    private static String className = GlossaryAuthorViewProjectRESTServices.class.getName();

    /**
     * Default constructor
     */
    public GlossaryAuthorViewProjectRESTServices() {

    }

    /**
     * Create a Project.
     *
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     *
     * Projects that are created using this call will be GlossaryProjects.
     * <p>
     *
     * @param serverName name of the local UI server.
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return the created Project.
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Project> createProject(String serverName, String userId, Project suppliedProject) {
        final String methodName = "createProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Project createdProject = clients.projects().create(userId, suppliedProject);
            response.addResult(createdProject);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Get a project.
     *
     * @param serverName name of the local UI server.
     * @param userId     user identifier
     * @param guid       guid of the project to get
     * @return response which when successful contains the project with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Project> getProject(String serverName, String userId, String guid) {
        final String methodName = "getProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Project obtainedProject = clients.projects().getByGUID(userId, guid);
            response.addResult(obtainedProject);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Project
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param searchCriteria     String expression matching Project property values .
     * @param asOfTime           the glossaries returned as they were at this time. null indicates at the current time.
     * @param startingFrom             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> findProject(
            String serverName,
            String userId,
            Date asOfTime,
            String searchCriteria,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty
    ) {
        final String methodName = "findProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (startingFrom == null) {
                startingFrom = 0;
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);
            SubjectAreaConfigClient client = instanceHandler.getSubjectAreaConfigClient(serverName, userId, methodName);
            Config subjectAreaConfig = client.getConfig(userId);
            List<Project> projects = clients.projects().find(userId, findRequest, subjectAreaConfig.getMaxPageSize());
            response.addAllResults(projects);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get Project relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid               guid of the project to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom          the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return a response which when successful contains the project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getProjectRelationships(
            String serverName,
            String userId,
            String guid,
            Date asOfTime,
            Integer startingFrom,
            Integer pageSize,
            SequencingOrder sequencingOrder,
            String sequencingProperty


                                                                           ) {
        final String methodName = "getProjectRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            if (startingFrom == null) {
                startingFrom = 0;
            }
            if (pageSize == null) {
                pageSize = invalidParameterHandler.getMaxPagingSize();
            }
            invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);

            List<Relationship> relationships =  clients.projects().getRelationships(userId, guid, findRequest);
            response.addAllResults(relationships);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update a Project
     * <p>
     * Status is not updated using this call.
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param guid       guid of the project to update
     * @param project   project to update
     * @param isReplace  flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated project
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Project> updateProject(
            String serverName,
            String userId,
            String guid,
            Project project,
            boolean isReplace
    ) {
        final String methodName = "updateProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Project updatedProject;

            if (isReplace) {
                updatedProject = clients.projects().replace(userId, guid, project);
            } else {
                updatedProject = clients.projects().update(userId, guid, project);
            }
            response.addResult(updatedProject);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> deleteProject(
            String serverName,
            String userId,
            String guid,
            boolean isPurge
    ) {

        final String methodName = "deleteProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            if (isPurge) {
                clients.projects().purge(userId, guid);
            } else {
                clients.projects().delete(userId, guid);
            }
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> restoreProject(
            String serverName,
            String userId,
            String guid) {
        final String methodName = "restoreProject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;

        // should not be called without a supplied project - the calling layer should not allow this.
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaNodeClients clients = instanceHandler.getSubjectAreaNodeClients(serverName, userId, methodName);
            Project project = clients.projects().restore(userId, guid);
            response.addResult(project);
        }  catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
