/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.GlossaryProject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.ProjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides Project authoring interfaces for subject area experts.
 */

public class SubjectAreaProjectRESTServices extends SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaProjectRESTServices.class);
    private static final String className = SubjectAreaProjectRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaProjectRESTServices() {
        super();
    }
    public SubjectAreaProjectRESTServices(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper =oMRSAPIHelper;
    }


    /**
     * Create a Project.
     *
     * Projects with the same name can be confusing. Best practise is to create projects that have unique names.
     * This Create call does not police that Project names are unique. So it is possible to create projects with the same name as each other.
     *
     * Projects that are created using this call will be GlossaryProjects.
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
    public SubjectAreaOMASAPIResponse createProject(String serverName, String userId, Project suppliedProject)
    {
        final String methodName = "createProject";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null) {
            SubjectAreaProjectRESTServices ProjectRESTServices = new SubjectAreaProjectRESTServices();
            ProjectRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
            try
            {
                InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
                final String suppliedProjectName = suppliedProject.getName();

                // need to check we have a name
                if (suppliedProjectName == null || suppliedProjectName.equals(""))
                {
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.PROJECT_CREATE_WITHOUT_NAME;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                    log.error(errorMessage);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                } else
                {
                    ProjectMapper ProjectMapper = new ProjectMapper(oMRSAPIHelper);
                    // ensure a glossaryProject is created
                    suppliedProject.setNodeType(NodeType.GlossaryProject);
                    EntityDetail projectEntityDetail = ProjectMapper.mapNodeToEntityDetail(suppliedProject);
                    response = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, projectEntityDetail);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                    {
                        EntityDetailResponse entityDetailResponse = (EntityDetailResponse)response;
                        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
                        response = getProjectByGuid(serverName,userId,entityDetail.getGUID());
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response =" + response);
        }
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
    public SubjectAreaOMASAPIResponse getProjectByGuid(String serverName, String userId, String guid) {
        final String methodName = "getProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response ==null) {
            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = oMRSAPIHelper.callOMRSGetEntityByGuid(methodName, userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                {
                    response = getResponse(response);
                }
            } catch(InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return response;
    }
    /**
     * Find Project
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param searchCriteria String expression matching Project property values. If not specified then all projects are returned.
     * @param asOfTime the projects returned as they were at this time. null indicates at the current time.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is no limit to the page size
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
    public  SubjectAreaOMASAPIResponse findProject(String serverName, String userId,
                                                   String searchCriteria,
                                                   Date asOfTime,
                                                   Integer offset,
                                                   Integer pageSize,
                                                   org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                   String sequencingProperty) {

        final String methodName = "findProject";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response ==null)
        {
            /*
             * If no search criteria is supplied then we return all projects, this should not be too many.
             */
            if (searchCriteria == null) {
                response = oMRSAPIHelper.getEntitiesByType(oMRSAPIHelper,methodName,userId,"Project",asOfTime, offset, pageSize);
            } else {
                response = oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, "Project", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
            }
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetails))
            {
                EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
                List<EntityDetail> entityDetails = entityDetailsResponse.getEntityDetails();

                List<Project> projects = new ArrayList<>();
                if (entityDetails == null) {
                    response = new ProjectsResponse(projects);
                } else {
                    for (EntityDetail entityDetail : entityDetails)
                    {
                        // call the getProject so that the ProjectSummary and other parts are populated.
                        response = getProjectByGuid(serverName, userId, entityDetail.getGUID());
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
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /*
     * Get Project relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
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

    public  SubjectAreaOMASAPIResponse getProjectRelationships(String serverName, String userId,String guid,
                                                               Date asOfTime,
                                                               Integer offset,
                                                               Integer pageSize,
                                                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                               String sequencingProperty
    ) {
        String methodName = "getProjectRelationships";
        return  getRelationshipsFromGuid(serverName, methodName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }
    /*
     * Get the terms in this project.
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the Project to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @return a response which when successful contains the Project relationships
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public  SubjectAreaOMASAPIResponse getProjectTerms(String serverName,
                                                       String userId,
                                                       String guid,
                                                       Date asOfTime
    ) {
        String methodName = "getProjectTerms";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaGraphRESTServices subjectAreaGraphRESTServices = new SubjectAreaGraphRESTServices(oMRSAPIHelper);
        // find all nodes, project and its subclass GlossaryProject, Term and its subclass Activity
        String nodeFilter =  (new HashSet<>(Arrays.asList(NodeType.Project,NodeType.Term,NodeType.GlossaryProject,NodeType.Activity))).toString();
        // filter on the project scope relationship
        String lineFilter=   (new HashSet<>(Arrays.asList(LineType.ProjectScope))).toString();
        SubjectAreaOMASAPIResponse response = subjectAreaGraphRESTServices.getGraph(serverName,userId,guid,asOfTime,nodeFilter,lineFilter,null,1);
        List<Term> terms = null;
        if (response.getResponseCategory() == ResponseCategory.Graph) {
            GraphResponse graphResponse = (GraphResponse)response;
            Graph graph =graphResponse.getGraph();
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
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;
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
    public SubjectAreaOMASAPIResponse updateProject(String serverName, String userId, String guid, Project suppliedProject, boolean isReplace) {
        final String methodName = "updateProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;

        // initialise omrs API helper with the right instance based on the server name
        response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            try
            {

                InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

                response = getProjectByGuid(serverName, userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.Project))
                {
                    Project originalProject = ((ProjectResponse) response).getProject();
                    Project updateProject = originalProject;
                    if (isReplace)
                    {
                        // copy over attributes
                        updateProject.setName(suppliedProject.getName());
                        updateProject.setQualifiedName(suppliedProject.getQualifiedName());
                        updateProject.setDescription(suppliedProject.getDescription());
                        updateProject.setStartDate(suppliedProject.getStartDate());
                        updateProject.setPlannedEndDate(suppliedProject.getPlannedEndDate());
                        updateProject.setStatus(suppliedProject.getStatus());
                        updateProject.setAdditionalProperties(suppliedProject.getAdditionalProperties());
                    } else
                    {
                        // copy over attributes if specified
                        if (suppliedProject.getName() != null)
                        {
                            updateProject.setName(suppliedProject.getName());
                        }
                        if (suppliedProject.getQualifiedName() != null)
                        {
                            updateProject.setQualifiedName(suppliedProject.getQualifiedName());
                        }
                        if (suppliedProject.getDescription() != null)
                        {
                            updateProject.setDescription(suppliedProject.getDescription());
                        }
                        if (suppliedProject.getStartDate() != null)
                        {
                            updateProject.setStartDate(suppliedProject.getStartDate());
                        }
                        if (suppliedProject.getPlannedEndDate() != null)
                        {
                            updateProject.setPlannedEndDate(suppliedProject.getPlannedEndDate());
                        }
                        if (suppliedProject.getStatus() != null)
                        {
                            updateProject.setStatus(suppliedProject.getStatus());
                        }
                        if (suppliedProject.getAdditionalProperties() != null)
                        {
                            updateProject.setAdditionalProperties(suppliedProject.getAdditionalProperties());
                        }
                    }
                    Date termFromTime = suppliedProject.getEffectiveFromTime();
                    Date termToTime = suppliedProject.getEffectiveToTime();
                    updateProject.setEffectiveFromTime(termFromTime);
                    updateProject.setEffectiveToTime(termToTime);
                    ProjectMapper ProjectMapper = new ProjectMapper(oMRSAPIHelper);
                    EntityDetail entityDetail = ProjectMapper.mapNodeToEntityDetail(updateProject);
                    String ProjectGuid = entityDetail.getGUID();
                    response = oMRSAPIHelper.callOMRSUpdateEntityProperties(methodName, userId, entityDetail);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                    {
                        response = getProjectByGuid(serverName,userId,ProjectGuid);
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }

        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",response=" + response);
        }
        return response;
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
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the Project to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the Project was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the Project was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteProject(String serverName, String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteProject";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid=" + guid);
        }

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");

                List<InstanceStatus> statusList = new ArrayList<>();
                statusList.add(InstanceStatus.ACTIVE);
                OMRSRepositoryHelper repositoryHelper = this.oMRSAPIHelper.getOMRSRepositoryHelper();
                String source = oMRSAPIHelper.getServiceName();
                String ProjectTypeDefName = "Project";
                String ProjectTypeDefGuid = repositoryHelper.getTypeDefByName(source, ProjectTypeDefName).getGUID();

                if (isPurge)
                {
                    oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, ProjectTypeDefName, ProjectTypeDefGuid, guid);
                    response = new VoidResponse();
                } else
                {
                    response = oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, ProjectTypeDefName, ProjectTypeDefGuid, guid);
                    if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                    {
                        response = getResponse(response);
                    }
                }

            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
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
    public SubjectAreaOMASAPIResponse restoreProject(String serverName, String userId, String guid)
    {
        final String methodName = "restoreProject";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }

        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response ==null)
        {
            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail))
                {
                    response = getProjectByGuid(serverName,userId,guid);
                }

            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Take an entityDetail response and map it to the appropriate Project orientated response
     * @param response entityDetailResponse
     * @return Project response containing the appropriate Project object.
     */
    protected SubjectAreaOMASAPIResponse getResponse( SubjectAreaOMASAPIResponse response) {
        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
        ProjectMapper ProjectMapper = new ProjectMapper(oMRSAPIHelper);

        try {
            Project project= ProjectMapper.mapEntityDetailToNode(entityDetail);
            if (project.getNodeType()==NodeType.GlossaryProject) {
                GlossaryProject glossaryProject = (GlossaryProject)project;
                response = new ProjectResponse(glossaryProject);
            } else {
                response = new ProjectResponse(project);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
    }
}
