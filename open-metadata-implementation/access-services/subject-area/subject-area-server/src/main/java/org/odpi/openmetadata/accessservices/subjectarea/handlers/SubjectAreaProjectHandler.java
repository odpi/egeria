/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.ProjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaProjectRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * SubjectAreaProjectHandler manages Project objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaProjectHandler extends SubjectAreaHandler {
    private static final Class clazz = SubjectAreaProjectHandler.class;
    private static final String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);

    /**
     * Construct the Subject Area Project Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param oMRSAPIHelper           OMRS API helper
     */
    public SubjectAreaProjectHandler(String serviceName,
                                     String serverName,
                                     InvalidParameterHandler invalidParameterHandler,
                                     OMRSRepositoryHelper repositoryHelper,
                                     RepositoryHandler repositoryHandler,
                                     OMRSAPIHelper oMRSAPIHelper) {
        super(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler, oMRSAPIHelper);
    }

    /**
     * Take an entityDetail response and map it to the appropriate Project orientated response
     *
     * @param response entityDetailResponse
     * @return Project response containing the appropriate Project object.
     */
    @Override
    protected SubjectAreaOMASAPIResponse getResponse(SubjectAreaOMASAPIResponse response) {
        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
        ProjectMapper projectMapper = new ProjectMapper(oMRSAPIHelper);

        try {
            Project project = projectMapper.mapEntityDetailToNode(entityDetail);
            if (project.getNodeType() == NodeType.GlossaryProject) {
                GlossaryProject glossaryProject = (GlossaryProject) project;
                response = new ProjectResponse(glossaryProject);
            } else {
                response = new ProjectResponse(project);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
    }

    /**
     * Create a Project. There are specializations of projects that can also be created using this operation.
     * To create a specialization, you should specify a nodeType other than Project in the supplied project.
     * <p>
     * Projects with the same name can be confusing. Best practise is to createProjects that have unique names.
     * This Create call does not police that project names are unique. So it is possible to create Projects with the same name as each other.
     *
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>Taxonomy to create a Taxonomy </li>
     * <li>CanonicalProject to create a canonical project </li>
     * <li>TaxonomyAndCanonicalProject to create a project that is both a taxonomy and a canonical project </li>
     * <li>Project to create a project that is not a taxonomy or a canonical project</li>
     * </ul>
     *
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param suppliedProject Project to create
     * @return response, when successful contains the created project.
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
    public SubjectAreaOMASAPIResponse createProject(String userId, Project suppliedProject) {
        final String methodName = "createProject";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
            String suppliedProjectName = suppliedProject.getName();
            // need to check we have a name
            if (suppliedProjectName == null || suppliedProjectName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_PROJECT_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition,
                                                    className,
                                                    methodName,
                                                    "Name",
                                                    null);

            } else {
                ProjectMapper projectMapper = new ProjectMapper(oMRSAPIHelper);
                EntityDetail projectEntityDetail = projectMapper.mapNodeToEntityDetail(suppliedProject);
                response = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, projectEntityDetail);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
                    response = getProjectByGuid(userId, entityDetail.getGUID());
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

    /**
     * Get a project by guid.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the project to get
     * @return response which when successful contains the project with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getProjectByGuid(String userId, String guid) {
        final String methodName = "getProjectByGuid";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = oMRSAPIHelper.callOMRSGetEntityByGuid(methodName, userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                response = getResponse(response);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

    /**
     * Find Project
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Project property values. If not specified then all projects are returned.
     * @param asOfTime           the projects returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findProject(String userId,
                                                  String searchCriteria,
                                                  Date asOfTime,
                                                  Integer offset,
                                                  Integer pageSize,
                                                  org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                  String sequencingProperty) {

        final String methodName = "findProject";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        /*
         * If no search criteria is supplied then we return all projects, this should not be too many.
         */
        if (searchCriteria == null) {
            response = oMRSAPIHelper.getEntitiesByType(oMRSAPIHelper, methodName, userId, "Project", asOfTime, offset, pageSize);
        } else {
            response = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, "Project", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
        }
        if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetails)) {
            EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
            List<EntityDetail> entityDetails = entityDetailsResponse.getEntityDetails();

            List<Project> projects = new ArrayList<>();
            if (entityDetails == null) {
                response = new ProjectsResponse(projects);
            } else {
                for (EntityDetail entityDetail : entityDetails) {
                    // call the getProject so that the ProjectSummary and other parts are populated.
                    response = getProjectByGuid(userId, entityDetail.getGUID());
                    if (response.getResponseCategory() == ResponseCategory.Project) {
                        ProjectResponse projectResponse = (ProjectResponse) response;
                        Project project = projectResponse.getProject();
                        projects.add(project);
                    } else {
                        break;
                    }
                }
                if (response.getResponseCategory() == ResponseCategory.Project) {
                    response = new ProjectsResponse(projects);
                }
            }
        }
        return response;
    }

    /**
     * Get Project relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the project to get
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is not limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Project userId
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getProjectRelationships(String userId, String guid,
                                                              Date asOfTime,
                                                              Integer offset,
                                                              Integer pageSize,
                                                              org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                              String sequencingProperty
                                                             ) {
        String methodName = "getProjectRelationships";
        return getRelationshipsFromGuid(methodName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
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
     * @param userId          unique identifier for requesting user, under which the request is performed
     * @param guid            guid of the project to update
     * @param suppliedProject project to be updated
     * @param isReplace       flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
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
    public SubjectAreaOMASAPIResponse updateProject(String userId, String guid, Project suppliedProject, boolean isReplace) {
        final String methodName = "updateProject";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        try {

            InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

            response = getProjectByGuid(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Project)) {
                Project originalProject = ((ProjectResponse) response).getProject();
                Project updateProject = originalProject;
                if (isReplace) {
                    // copy over attributes
                    updateProject.setName(suppliedProject.getName());
                    updateProject.setQualifiedName(suppliedProject.getQualifiedName());
                    updateProject.setDescription(suppliedProject.getDescription());
                    updateProject.setStartDate(suppliedProject.getStartDate());
                    updateProject.setPlannedEndDate(suppliedProject.getPlannedEndDate());
                    updateProject.setStatus(suppliedProject.getStatus());
                    updateProject.setAdditionalProperties(suppliedProject.getAdditionalProperties());
                } else {
                    // copy over attributes if specified
                    if (suppliedProject.getName() != null) {
                        updateProject.setName(suppliedProject.getName());
                    }
                    if (suppliedProject.getQualifiedName() != null) {
                        updateProject.setQualifiedName(suppliedProject.getQualifiedName());
                    }
                    if (suppliedProject.getDescription() != null) {
                        updateProject.setDescription(suppliedProject.getDescription());
                    }
                    if (suppliedProject.getStartDate() != null) {
                        updateProject.setStartDate(suppliedProject.getStartDate());
                    }
                    if (suppliedProject.getPlannedEndDate() != null) {
                        updateProject.setPlannedEndDate(suppliedProject.getPlannedEndDate());
                    }
                    if (suppliedProject.getStatus() != null) {
                        updateProject.setStatus(suppliedProject.getStatus());
                    }
                    if (suppliedProject.getAdditionalProperties() != null) {
                        updateProject.setAdditionalProperties(suppliedProject.getAdditionalProperties());
                    }
                }
                Date termFromTime = suppliedProject.getEffectiveFromTime();
                Date termToTime = suppliedProject.getEffectiveToTime();
                updateProject.setEffectiveFromTime(termFromTime);
                updateProject.setEffectiveToTime(termToTime);
                ProjectMapper projectMapper = new ProjectMapper(oMRSAPIHelper);
                EntityDetail entityDetail = projectMapper.mapNodeToEntityDetail(updateProject);
                String projectGuid = entityDetail.getGUID();
                response = oMRSAPIHelper.callOMRSUpdateEntityProperties(methodName, userId, entityDetail);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    response = getProjectByGuid(userId, projectGuid);
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
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
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the project to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the project was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the project was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteProject(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteProject";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // do not delete if there is project content (terms or categories)
            // look for all project content that is not deleted.
            List<InstanceStatus> statusList = new ArrayList<>();
            statusList.add(InstanceStatus.ACTIVE);
            OMRSRepositoryHelper repositoryHelper = this.oMRSAPIHelper.getOMRSRepositoryHelper();
            //TODO get source properly
            String source = oMRSAPIHelper.getServiceName();
            String projectTypeDefName = "Project";
            String projectTypeDefGuid = repositoryHelper.getTypeDefByName(source, projectTypeDefName).getGUID();

            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, projectTypeDefName, projectTypeDefGuid, guid);
                response = new VoidResponse();
            } else {
                String termAnchorGuid = repositoryHelper.getTypeDefByName(methodName, "TermAnchor").getGUID();
                String categoryAnchorGuid = repositoryHelper.getTypeDefByName(methodName, "TermAnchor").getGUID();

                // if this is a not a purge then attempt to get terms and categories, as we should not delete if there are any
                response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, termAnchorGuid, 0, statusList, null, null, null, 1);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                    RelationshipsResponse termAnchorRelationshipsResponse = (RelationshipsResponse) response;
                    List<Relationship> termRelationships = termAnchorRelationshipsResponse.getRelationships();
                    response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, categoryAnchorGuid, 0, statusList, null, null, null, 1);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                        RelationshipsResponse categoryAnchorRelationshipsResponse = (RelationshipsResponse) response;
                        List<Relationship> categoryRelationships = categoryAnchorRelationshipsResponse.getRelationships();
                        if (((termRelationships == null) || termRelationships.isEmpty()) && (categoryRelationships == null || categoryRelationships.isEmpty())) {
                            response = oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, projectTypeDefName, projectTypeDefGuid, guid);
                            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                                response = getResponse(response);
                            }
                        } else {
                            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE.getMessageDefinition();
                            response = new EntityNotDeletedExceptionResponse(
                                    new EntityNotDeletedException(messageDefinition,
                                                                  className,
                                                                  methodName,
                                                                  guid)
                            );
                        }
                    }
                }
            }

        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }


    /**
     * Restore a Project
     * <p>
     * Restore allows the deleted Project to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the project to restore
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
    public SubjectAreaOMASAPIResponse restoreProject(String userId, String guid) {
        final String methodName = "restoreProject";
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaProjectRESTServices projectRESTServices = new SubjectAreaProjectRESTServices();
        projectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                response = getProjectByGuid(userId, guid);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        return response;
    }

    /**
     * Get the terms in this project.
     *
     * @param subjectAreaGraphHandler graph handler
     * @param userId                  unique identifier for requesting user, under which the request is performed
     * @param guid                    guid of the Project to get
     * @param asOfTime                the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @return a response which when successful contains the Project terms
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse getProjectTerms(
            SubjectAreaGraphHandler subjectAreaGraphHandler,
            String userId,
            String guid,
            Date asOfTime
                                                     ) {


        // find all nodes, project and its subclass GlossaryProject, Term and its subclass Activity
        String nodeFilter = (new HashSet<>(Arrays.asList(NodeType.Project, NodeType.Term, NodeType.GlossaryProject, NodeType.Activity))).toString();
        // filter on the project scope relationship
        String lineFilter = (new HashSet<>(Arrays.asList(LineType.ProjectScope))).toString();
        SubjectAreaOMASAPIResponse response = subjectAreaGraphHandler.getGraph(
                userId,
                guid,
                asOfTime,
                nodeFilter,
                lineFilter,
                null, 1);
        List<Term> terms = null;
        if (response.getResponseCategory() == ResponseCategory.Graph) {
            GraphResponse graphResponse = (GraphResponse) response;
            Graph graph = graphResponse.getGraph();
            if (graph != null) {
                Set<Node> nodes = graph.getNodes();
                if (nodes != null) {
                    for (Node node : nodes) {
                        if (node.getNodeType() == NodeType.Term) {
                            if (terms == null) {
                                terms = new ArrayList();
                            }
                            terms.add((Term) node);
                        }
                    }
                }
            }
            response = new TermsResponse(terms);
        }
        return response;
    }


}
