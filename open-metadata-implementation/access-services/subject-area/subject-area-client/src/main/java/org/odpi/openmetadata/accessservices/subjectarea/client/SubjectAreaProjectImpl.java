/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides project authoring interface for subject area experts.
 */
public class SubjectAreaProjectImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaProject {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaProjectImpl.class);

    private static final String className = SubjectAreaProjectImpl.class.getName();

    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "projects";


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaProjectImpl(String omasServerURL, String serverName) {
        super(omasServerURL, serverName);
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
     * @return the created project.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws ClassificationException              Error processing a classification
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Project createProject(String userId, Project suppliedProject) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "createProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String url = this.omasServerURL + String.format(BASE_URL, serverName, userId);
        InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
        suppliedProject.setNodeType(NodeType.GlossaryProject);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedProject);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePost(className, methodName, requestBody, url);

        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowClassificationException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Project project = DetectUtils.detectAndReturnProject(className, methodName, restResponse);
        // that the returned nodeType matches the requested one
        if (suppliedProject.getNodeType() != null && !suppliedProject.getNodeType().equals(project.getNodeType())) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.UNEXPECTED_NODETYPE;
            throw new InvalidParameterException(
                    errorCode.getMessageDefinition(),
                    className,
                    methodName,
                    "Node type",
                    project.getNodeType().name()
            );
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    /**
     * Get a project by guid.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the project to get
     * @return the requested project.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Project getProjectByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "getProjectByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        Project project = DetectUtils.detectAndReturnProject(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    /**
     * Get Project relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the project to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Project guid
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public List<Line> getProjectRelationships(String userId, String guid,
                                              Date asOfTime,
                                              int offset,
                                              int pageSize,
                                              SequencingOrder sequencingOrder,
                                              String sequencingProperty) throws
                                                                         UserNotAuthorizedException,
                                                                         InvalidParameterException,
                                                                         FunctionNotSupportedException,
                                                                         UnexpectedResponseException,
                                                                         MetadataServerUncontactableException {
        final String methodName = "getProjectRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        List<Line> relationships = getRelationships(BASE_URL, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relationships;
    }

    /**
     * Get the terms in this project.
     *
     * @param userId   unique identifier for requesting user, under which the request is performed
     * @param guid     guid of the Project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @return the terms that are in the requested Project
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public List<Term> getProjectTerms(
            String userId,
            String guid,
            Date asOfTime
                                     ) throws InvalidParameterException, UserNotAuthorizedException, FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException {
        final String methodName = "getProjectTerms";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s/terms";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        List<Term> terms = DetectUtils.detectAndReturnTerms(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return terms;
    }

    /**
     * Find Project
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Project properties. If not specified then all projects are returned.
     * @param asOfTime           the projects returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Projects meeting the search Criteria
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public List<Project> findProject(String userId,
                                     String searchCriteria,
                                     Date asOfTime,
                                     int offset,
                                     int pageSize,
                                     SequencingOrder sequencingOrder,
                                     String sequencingProperty) throws
                                                                MetadataServerUncontactableException,
                                                                UserNotAuthorizedException,
                                                                InvalidParameterException,
                                                                FunctionNotSupportedException,
                                                                UnexpectedResponseException {

        final String methodName = "findProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        final String urlTemplate = this.omasServerURL + BASE_URL;
        String url = String.format(urlTemplate, serverName, userId);

        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils.addCharacterToQuery(queryStringSB);
        queryStringSB.append("sequencingOrder=" + sequencingOrder);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime=" + asOfTime);
        }
        if (searchCriteria != null) {
            // encode the string

            encodeQueryProperty("searchCriteria", searchCriteria, methodName, queryStringSB);
        }
        if (offset != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("offset=" + offset);
        }
        if (pageSize != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("pageSize=" + pageSize);
        }
        if (sequencingProperty != null) {
            // encode the string
            encodeQueryProperty("sequencingProperty", sequencingProperty, methodName, queryStringSB);
        }
        if (queryStringSB.length() > 0) {
            url = url + queryStringSB.toString();
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        List<Project> projects = DetectUtils.detectAndReturnProjects(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return projects;
    }

    /**
     * Replace a Project. This means to override all the existing attributes with the supplied attributes.
     * <p>
     * If the caller has chosen to incorporate the project name in their Project Terms or Categories qualified name, renaming the project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param userId          userId under which the request is performed
     * @param guid            guid if the project to update
     * @param suppliedProject project to be updated
     * @return replaced project
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Project replaceProject(String userId, String guid, Project suppliedProject) throws
                                                                                       UnexpectedResponseException,
                                                                                       UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       MetadataServerUncontactableException {
        final String methodName = "replaceProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Project project = updateProject(userId, guid, suppliedProject, true);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    /**
     * Update a Project. This means to update the project with any non-null attributes from the supplied project.
     * <p>
     * If the caller has chosen to incorporate the project name in their Project Terms or Categories qualified name, renaming the project will cause those
     * qualified names to mismatch the Project name.
     * If the caller has chosen to incorporate the project qualifiedName in their Project Terms or Categories qualified name, changing the qualified name of the project will cause those
     * qualified names to mismatch the Project name.
     * Status is not updated using this call.
     *
     * @param userId          userId under which the request is performed
     * @param guid            guid if the project to update
     * @param suppliedProject project to be updated
     * @return a response which when successful contains the updated project
     * when not successful the following Exceptions can occur
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Project updateProject(String userId, String guid, Project suppliedProject) throws UnexpectedResponseException,
                                                                                             UserNotAuthorizedException,
                                                                                             InvalidParameterException,
                                                                                             MetadataServerUncontactableException {
        final String methodName = "updateProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        Project project = updateProject(userId, guid, suppliedProject, false);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;

    }

    /**
     * Delete a Project instance
     * <p>
     * The deletion of a project is only allowed if there is no project content (i.e. no terms or categories).
     * <p>
     * A delete (also known as a soft delete) means that the project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the project to be deleted.
     * @return the deleted project
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotDeletedException            a delete was issued but the project was not deleted.
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public Project deleteProject(String userId, String guid) throws InvalidParameterException,
                                                                    MetadataServerUncontactableException,
                                                                    UserNotAuthorizedException,
                                                                    UnrecognizedGUIDException,
                                                                    FunctionNotSupportedException,
                                                                    UnexpectedResponseException,
                                                                    EntityNotDeletedException {
        final String methodName = "deleteProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=false";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        DetectUtils.detectAndThrowEntityNotDeletedException(restResponse);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    /**
     * Purge a Project instance
     * <p>
     * The purge of a project is only allowed if there is no project content (i.e. no terms or categories).
     * <p>
     * A purge means that the project will not exist after the operation.
     *
     * @param userId userId under which the request is performed
     * @param guid   guid of the project to be deleted.
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws EntityNotPurgedException             a hard delete was issued but the project was not purged
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */

    public void purgeProject(String userId, String guid) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                MetadataServerUncontactableException,
                                                                UnrecognizedGUIDException,
                                                                EntityNotPurgedException,
                                                                UnexpectedResponseException,
                                                                FunctionNotSupportedException {
        final String methodName = "purgeProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isPurge=true";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueDelete(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowEntityNotPurgedException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
    }

    /**
     * Update Project.
     * <p>
     * If the caller has chosen to incorporate the project name in their Project Terms qualified name, renaming the project will cause those
     * qualified names to mismatch the Project name.
     *
     * @param userId          userId under which the request is performed
     * @param guid            guid of the project to update
     * @param suppliedProject Project to be updated
     * @param isReplace       flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return the updated project.
     * <p>
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid
     *                                              <p>
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    private Project updateProject(String userId, String guid, Project suppliedProject, boolean isReplace) throws
                                                                                                          UserNotAuthorizedException,
                                                                                                          InvalidParameterException,
                                                                                                          MetadataServerUncontactableException,
                                                                                                          UnexpectedResponseException {
        final String methodName = "updateProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s?isReplace=%b";
        String url = String.format(urlTemplate, serverName, userId, guid, isReplace);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(suppliedProject);
        } catch (JsonProcessingException error) {
            RestCaller.throwJsonParseError(className, methodName, error);
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePut(className, methodName, requestBody, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid if the project to restore
     * @return the restored project
     * @throws UnrecognizedGUIDException            the supplied guid was not recognised
     * @throws UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.
     *                                              Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException          an unexpected response was returned from the server
     */
    public Project restoreProject(String userId, String guid) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     MetadataServerUncontactableException,
                                                                     UnrecognizedGUIDException,
                                                                     FunctionNotSupportedException,
                                                                     UnexpectedResponseException {
        final String methodName = "restoreProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

        final String urlTemplate = this.omasServerURL + BASE_URL + "/%s";
        String url = String.format(urlTemplate, serverName, userId, guid);

        SubjectAreaOMASAPIResponse restResponse = RestCaller.issuePostNoBody(className, methodName, url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowUnrecognizedGUIDException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }
}
