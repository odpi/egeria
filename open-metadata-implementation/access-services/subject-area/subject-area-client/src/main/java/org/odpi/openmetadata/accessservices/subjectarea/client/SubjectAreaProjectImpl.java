/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the Subject Area OMAS.
 * This interface provides project authoring interface for subject area experts.
 */
public class SubjectAreaProjectImpl extends SubjectAreaBaseImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaProject {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaProjectImpl.class);

    private static final String className = SubjectAreaProjectImpl.class.getName();

    private static final String BASE_URL = SubjectAreaImpl.SUBJECT_AREA_BASE_URL + "projects";


    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    public SubjectAreaProjectImpl(String serverName, String serverPlatformURLRoot) throws
                                                                                    org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    @Override
    public Project createProject(String userId, Project suppliedProject) throws MetadataServerUncontactableException, InvalidParameterException, UserNotAuthorizedException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, PropertyServerException {
        final String methodName = "createProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        final String urlTemplate = BASE_URL;
        SubjectAreaOMASAPIResponse response = postRESTCall(userId, methodName, urlTemplate, suppliedProject);
        Project project = DetectUtils.detectAndReturnProject(className, methodName, response);
        // that the returned nodeType matches the requested one
        if (suppliedProject.getNodeType() != null && !suppliedProject.getNodeType().equals(project.getNodeType())) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.UNEXPECTED_NODETYPE;
            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition();
            String propertyName = "NodeType";
            String propertyValue = suppliedProject.getNodeType().name();
            messageDefinition.setMessageParameters(propertyName,propertyValue);
            throw new InvalidParameterException(
                    messageDefinition,
                    className,
                    methodName,
                    propertyName,
                    propertyValue);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }
    @Override
    public Project getProjectByGuid(String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, UnexpectedResponseException, PropertyServerException {
        final String methodName = "getCategoryByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        Project project = DetectUtils.detectAndReturnProject(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    @Override
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
                                                                         MetadataServerUncontactableException,
                                                                         PropertyServerException {
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

    @Override
    public List<Term> getProjectTerms(
            String userId,
            String guid,
            Date asOfTime
                                     ) throws InvalidParameterException,
                                              UserNotAuthorizedException,
                                              FunctionNotSupportedException,
                                              UnexpectedResponseException,
                                              MetadataServerUncontactableException,
                                              PropertyServerException {
        final String methodName = "getProjectTerms";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s/terms";
        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        List<Term> terms = DetectUtils.detectAndReturnTerms(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return terms;
    }

    @Override
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
                                                                UnexpectedResponseException,
                                                                PropertyServerException {

        final String methodName = "findProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = findRESTCall(userId,
                                                           methodName,
                                                           BASE_URL,
                                                           searchCriteria,
                                                           asOfTime,
                                                           offset,
                                                           pageSize,
                                                           sequencingOrder,
                                                           sequencingProperty);
        List<Project> projects = DetectUtils.detectAndReturnProjects(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return projects;
    }

    @Override
    public Project replaceProject(String userId, String guid, Project suppliedProject) throws
                                                                                       UnexpectedResponseException,
                                                                                       UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       MetadataServerUncontactableException,
                                                                                       PropertyServerException,
                                                                                       FunctionNotSupportedException {
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

    @Override
    public Project updateProject(String userId, String guid, Project suppliedProject) throws UnexpectedResponseException,
                                                                                             UserNotAuthorizedException,
                                                                                             InvalidParameterException,
                                                                                             MetadataServerUncontactableException,
                                                                                             PropertyServerException,
                                                                                             FunctionNotSupportedException {
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

    @Override
    public Project deleteProject(String userId, String guid) throws InvalidParameterException,
                                                                    MetadataServerUncontactableException,
                                                                    UserNotAuthorizedException,
                                                                    UnrecognizedGUIDException,
                                                                    FunctionNotSupportedException,
                                                                    UnexpectedResponseException,
                                                                    EntityNotDeletedException,
                                                                    PropertyServerException {
        final String methodName = "deleteProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
         final String urlTemplate = BASE_URL + "/%s?isPurge=false";
        SubjectAreaOMASAPIResponse response = deleteEntityRESTCall(userId, guid, methodName, urlTemplate);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }

    @Override
    public void purgeProject(String userId, String guid) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                MetadataServerUncontactableException,
                                                                UnrecognizedGUIDException,
                                                                EntityNotPurgedException,
                                                                UnexpectedResponseException,
                                                                FunctionNotSupportedException, PropertyServerException {
        final String methodName = "purgeProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }


        final String urlTemplate =  BASE_URL + "/%s?isPurge=true";
        purgeEntityRESTCall(userId, guid, methodName, urlTemplate);
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
                                                                                                          UnexpectedResponseException,
                                                                                                          PropertyServerException, FunctionNotSupportedException {
        final String methodName = "updateProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s?isReplace=%b";
        SubjectAreaOMASAPIResponse response = putRESTCall(userId, guid, isReplace, methodName, urlTemplate, suppliedProject);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, response);
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
                                                                     UnexpectedResponseException, PropertyServerException {
        final String methodName = "restoreProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        final String urlTemplate = BASE_URL + "/%s";
        SubjectAreaOMASAPIResponse response = restoreRESTCall(userId, guid, methodName, urlTemplate);

        Project project = DetectUtils.detectAndReturnProject(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return project;
    }
}
