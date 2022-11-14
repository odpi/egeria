/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.handlers.SubjectAreaProjectHandler;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides Project authoring interfaces for subject area experts.
 */

public class SubjectAreaProjectRESTServices extends SubjectAreaRESTServicesInstance {
    private static final String className = SubjectAreaProjectRESTServices.class.getName();
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaProjectRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SubjectAreaProjectRESTServices() {}

    /**
     * Create a Project.
     *
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     *
     * Projects that are created using this call will be ProjectProjects.
     * <p>

     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return response, when successful contains the created Project.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li>ClassificationException              Error processing a classification.</li>
     * <li>StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> createProject(String serverName, String userId, Project suppliedProject) {
        final String methodName = "createProject";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response= new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            response = handler.createProject(userId,suppliedProject);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get a Project by guid.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @return response which when successful contains the Project with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> getProjectByGuid(String serverName, String userId, String guid) {
        final String methodName = "getProjectByGuid";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response= new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            response = handler.getProjectByGuid(userId, guid);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Find Project
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Project property values. If not specified then all projects are returned.
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param asOfTime the projects returned as they were at this time. null indicates at the current time.
     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> findProject(String serverName,
                                                           String userId,
                                                           String searchCriteria,
                                                           boolean exactValue,
                                                           boolean ignoreCase,
                                                           Date asOfTime,
                                                           Integer startingFrom,
                                                           Integer pageSize,
                                                           SequencingOrder sequencingOrder,
                                                           String sequencingProperty) {

        final String methodName = "findProject";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response= new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setSearchCriteria(searchCriteria);
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);
            response = handler.findProject(userId,findRequest, exactValue,ignoreCase);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Get Project relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Project guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Relationship> getProjectRelationships(String serverName,
                                                                            String userId,
                                                                            String guid,
                                                                            Date asOfTime,
                                                                            Integer startingFrom,
                                                                            Integer pageSize,
                                                                            SequencingOrder sequencingOrder,
                                                                            String sequencingProperty) {
        String methodName = "getProjectRelationships";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Relationship> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            FindRequest findRequest = new FindRequest();
            findRequest.setAsOfTime(asOfTime);
            findRequest.setStartingFrom(startingFrom);
            findRequest.setPageSize(pageSize);
            findRequest.setSequencingOrder(sequencingOrder);
            findRequest.setSequencingProperty(sequencingProperty);
            response = handler.getProjectRelationships(userId, guid, findRequest);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

//    /**
//     * Get the terms in this project.
//     *
//     * @param serverName     serverName under which this request is performed, this is used in multi tenanting to identify the tenant
//     * @param userId         unique identifier for requesting user, under which the request is performed
//     * @param guid           guid of the Project
//     * @param startingFrom  the starting element number for this set of results.  This is used when retrieving elements
//     *                 beyond the first page of results. Zero means the results start from the first element.
//     * @param pageSize the maximum number of elements that can be returned on this request.
//     * @return a response which when successful contains the Project Terms
//     * when not successful the following Exception responses can occur
//     * <ul>
//     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
//     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
//     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
//     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
//     * </ul>
//     */
//    public SubjectAreaOMASAPIResponse<Term> getProjectTerms(String serverName,
//                                                            String userId,
//                                                            String guid,
//                                                            Integer startingFrom,
//                                                            Integer pageSize) {
//        String methodName = "getProjectTerms";
//        if (log.isDebugEnabled()) {
//            log.debug("==> Method: " + methodName + ",userId=" + userId );
//        }
//        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
//        AuditLog auditLog = null;
//        try {
//            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
//            SubjectAreaProjectHandler projectHandler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
//            response = projectHandler.getProjectTerms(userId, guid, instanceHandler.getSubjectAreaTermHandler(userId, serverName, methodName), startingFrom, pageSize);
//        } catch (OCFCheckedExceptionBase e) {
//            response.setExceptionInfo(e, className);
//        } catch (Exception exception) {
//            response = getResponseForException(exception, auditLog, className, methodName);
//        }
//        if (log.isDebugEnabled()) {
//            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
//        }
//
//        return response;
//    }

    /**
     * Update a Project
     * <p>
     * If the caller has chosen to incorporate the Project name in their Project Terms or Categories qualified name, renaming the Project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the Project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the Project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param guid             guid of the Project to update
     * @param suppliedProject Project to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse<Project> updateProject(String serverName, String userId, String guid, Project suppliedProject, boolean isReplace) {
        final String methodName = "updateProject";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            response = handler.updateProject(userId, guid, suppliedProject,isReplace);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Delete a Project instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the Project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the Project will not exist after the operation.
     * when not successful the following Exceptions can occur
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the Project to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the Project was not deleted.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> deleteProject(String serverName, String userId, String guid) {
        final String methodName = "deleteProject";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            response = handler.deleteProject(userId, guid);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Restore a Project
     *
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
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
    public SubjectAreaOMASAPIResponse<Project> restoreProject(String serverName, String userId, String guid) {
        final String methodName = "restoreProject";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SubjectAreaOMASAPIResponse<Project> response= new SubjectAreaOMASAPIResponse<>();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SubjectAreaProjectHandler handler = instanceHandler.getSubjectAreaProjectHandler(userId, serverName, methodName);
            response = handler.restoreProject(userId, guid);
        } catch (OCFCheckedExceptionBase e) {
            response.setExceptionInfo(e, className);
        } catch (Exception exception) {
            response = getResponseForException(exception, auditLog, className, methodName);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
