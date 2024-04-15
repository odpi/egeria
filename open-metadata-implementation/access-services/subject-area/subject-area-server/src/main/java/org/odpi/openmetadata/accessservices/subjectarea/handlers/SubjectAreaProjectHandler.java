/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.ProjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.commonservices.generichandlers.*;

import java.util.*;


/**
 * SubjectAreaProjectHandler manages Project objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class SubjectAreaProjectHandler extends SubjectAreaHandler {
    private static final Class<?> clazz = SubjectAreaProjectHandler.class;
    private static final String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);

    /**
     * Construct the Subject Area Project Handler
     * needed to operate within a single server instance.
     *
     * @param genericHandler    generic handler
     * @param maxPageSize       maximum page size
     */
    public SubjectAreaProjectHandler(OpenMetadataAPIGenericHandler genericHandler, int maxPageSize) {
        super(genericHandler, maxPageSize);
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
    public SubjectAreaOMASAPIResponse<Project> createProject(String userId, Project suppliedProject) {
        final String methodName = "createProject";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();

        try {
            InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);
            String suppliedProjectName = suppliedProject.getName();
            // need to check we have a name
            if (suppliedProjectName == null || suppliedProjectName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_PROJECT_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "Name", null);
            } else {
                setUniqueQualifiedNameIfBlank(suppliedProject);

                Date effectiveFrom = null;
                Date effectiveTo = null;

                if (suppliedProject.getEffectiveFromTime() != null) {
                    effectiveFrom = new Date(suppliedProject.getEffectiveFromTime());
                }
                if (suppliedProject.getEffectiveToTime() != null) {
                    effectiveTo = new Date(suppliedProject.getEffectiveToTime());
                }

                ProjectBuilder builder = new ProjectBuilder(suppliedProject.getQualifiedName(),
                                                            suppliedProject.getIdentifier(),
                                                            suppliedProject.getName(),
                                                            suppliedProject.getDescription(),
                                                            suppliedProject.getStartDate(),
                                                            suppliedProject.getPlannedEndDate(),
                                                            null,
                                                            null,
                                                            suppliedProject.getStatus(),
                                                            null,
                                                            OpenMetadataType.PROJECT.typeGUID,
                                                            OpenMetadataType.PROJECT.typeName,
                                                            null,
                                                            genericHandler.getRepositoryHelper(),
                                                            genericHandler.getServiceName(),
                                                            genericHandler.getServerName());

                builder.setEffectivityDates(effectiveFrom, effectiveTo);

                String entityDetailGuid = genericHandler.createBeanInRepository(userId,
                                                                                null,
                                                                                null,
                                                                                OpenMetadataType.PROJECT.typeGUID,
                                                                                OpenMetadataType.PROJECT.typeName,
                                                                                builder,
                                                                                null,
                                                                                methodName);
                if (entityDetailGuid != null) {
                    response = getProjectByGuid(userId, entityDetailGuid);
                }
            }
        } catch (InvalidParameterException | SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
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
    public SubjectAreaOMASAPIResponse<Project> getProjectByGuid(String userId, String guid) {
        final String methodName = "getProjectByGuid";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();

        try {
            EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                               guid,
                                                                               "guid",
                                                                               OpenMetadataType.PROJECT.typeName,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               null,
                                                                               methodName);

                ProjectMapper projectMapper = mappersFactory.get(ProjectMapper.class);
                Project project = projectMapper.map(entityDetail);
                response.addResult(project);

        } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Find Project
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @param exactValue a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @return A list of Projects meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a find was issued but the repository does not implement find functionality in some way.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> findProject(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) {

        final String methodName = "findProject";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();

        try {
            List<Project> foundProjects = findNodes(userId, OpenMetadataType.PROJECT.typeName, OpenMetadataType.PROJECT.typeGUID, findRequest, exactValue, ignoreCase, ProjectMapper.class, methodName);
            if (foundProjects != null) {
                response.addAllResults(foundProjects);
            } else {
                return response;
            }
        } catch (PropertyServerException | UserNotAuthorizedException |InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Get Project relationships
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid of the project to get
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Project userId
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse<Relationship> getProjectRelationships(String userId, String guid, FindRequest findRequest) {
        String methodName = "getProjectRelationships";
        return getAllRelationshipsForEntity(methodName, userId, guid, findRequest, OpenMetadataType.PROJECT.typeName );
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
    public SubjectAreaOMASAPIResponse<Project> updateProject(String userId, String guid, Project suppliedProject, boolean isReplace) {
        final String methodName = "updateProject";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();

        try {
            InputValidator.validateNodeType(className, methodName, suppliedProject.getNodeType(), NodeType.Project, NodeType.GlossaryProject);

            response = getProjectByGuid(userId, guid);
            if (response.head().isPresent()) {
//

                    Project storedProject = response.head().get();
                    ProjectMapper projectMapper = mappersFactory.get(ProjectMapper.class);

                    EntityDetail suppliedEntity = projectMapper.map(suppliedProject);
                    EntityDetail storedEntity = projectMapper.map(storedProject);
                    genericHandler.updateBeanInRepository(userId,
                                                          null,
                                                          null,
                                                          guid,
                                                          "guid",
                                                          OpenMetadataType.PROJECT.typeGUID,
                                                          OpenMetadataType.PROJECT.typeName,
                                                          false,
                                                          false,
                                                          suppliedEntity.getProperties(),
                                                          !isReplace,
                                                          null,
                                                          methodName);
                    setNodeEffectivity(userId,
                                       suppliedProject,
                                       methodName,
                                       guid,
                                       OpenMetadataType.PROJECT.typeGUID,
                                       OpenMetadataType.PROJECT.typeName);
                response = getProjectByGuid(userId, guid);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Delete a Project instance
     * <p>
     * The deletion of a project is only allowed if there is no project content (i.e. no terms or categories).
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional.
     * <p>
     * A soft delete means that the project instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the project will not exist after the operation.
     *
     * @param userId  unique identifier for requesting user, under which the request is performed
     * @param guid    guid of the project to be deleted.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the project was not deleted.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Project> deleteProject(String userId, String guid) {
        final String methodName = "deleteProject";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        boolean issueDelete = false;        try {
            if (genericHandler.isBeanIsolated(userId,
                                              guid,
                                              OpenMetadataType.PROJECT.typeName,
                                              false,
                                              false,
                                              null,
                                              methodName)) {

                issueDelete = true;
            } else {
                throw new EntityNotDeletedException(SubjectAreaErrorCode.PROJECT_CONTENT_PREVENTED_DELETE.getMessageDefinition(guid),
                                                    className,
                                                    methodName,
                                                    guid);
            }
            if (issueDelete) {
                genericHandler.deleteBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      "guid",
                                                      OpenMetadataType.PROJECT.typeGUID,    // true for sub types
                                                      OpenMetadataType.PROJECT.typeName,    // true for sub types
                                                      null,
                                                      null,
                                                      false,
                                                      false,
                                                      null,
                                                      methodName);
            }

        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | PropertyServerException | InvalidParameterException e) {
            response.setExceptionInfo(e, className);
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
    public SubjectAreaOMASAPIResponse<Project> restoreProject(String userId, String guid) {
        final String methodName = "restoreProject";
        SubjectAreaOMASAPIResponse<Project> response = new SubjectAreaOMASAPIResponse<>();
        try {
            genericHandler.getRepositoryHandler().restoreEntity(userId,
                                                                null,
                                                                null,
                                                                guid,
                                                                methodName);
            response = getProjectByGuid(userId, guid);
        } catch (UserNotAuthorizedException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    // The following method is not used - there is some commented out code to use as a basis to implement this method.

//    /**
//     * Get the terms in this project.
//     *
//     * @param userId                  unique identifier for requesting user, under which the request is performed
//     * @param guid                    guid of the Project
//     * @param termHandler             Term handler
//     * @param startingFrom            the starting element number for this set of results.  This is used when retrieving elements
//     *                                beyond the first page of results. Zero means the results start from the first element.
//     * @param pageSize                the maximum number of elements that can be returned on this request.
//     * @return a response which when successful contains the Project terms
//     * when not successful the following Exception responses can occur
//     * <ul>
//     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
//     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
//     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
//     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
//     * </ul>
//     */
//    public SubjectAreaOMASAPIResponse<Node> getProjectContent(String userId, String guid, SubjectAreaTermHandler termHandler, Integer startingFrom, Integer pageSize) {
//        final String methodName = "getProjectContent";
//        SubjectAreaOMASAPIResponse<Node> response = new SubjectAreaOMASAPIResponse<>();
//
//        if (pageSize == null) {
//            pageSize = maxPageSize;
//        }
//        if (startingFrom == null) {
//            startingFrom = 0;
//        }
//        SubjectAreaOMASAPIResponse<Project> thisTermResponse = getProjectByGuid(userId, guid);
//        if (thisTermResponse.getRelatedHTTPCode() == 200) {
//            try {
//
//                List<EntityDetail> entities = genericHandler.getAttachedFilteredEntities(userId,
//                                                                                         guid,
//                                                                                         "guid",
//                                                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
//                                                                                         OpenMetadataType.PROJECT_SCOPE_RELATIONSHIP_TYPE_NAME,
//                                                                                         OpenMetadataType.PROJECT_SCOPE_RELATIONSHIP_TYPE_GUID ,
//                                                                                         2,      // get the content
//                                                                                         null,
//                                                                                         "", // no search criteria
//                                                                                         startingFrom,
//                                                                                         false,
//                                                                                         false,
//                                                                                         pageSize,
//                                                                                         methodName);
//
//                Set<Node> nodes = new HashSet<>();
//
//
//                for (EntityDetail entity : entities) {
//                    String typeName = entity.getType().getTypeDefName();
//                    String serviceName = genericHandler.getServiceName();
//                    if (genericHandler.getRepositoryHelper().isTypeOf(serviceName, typeName, OpenMetadataType.GLOSSARY_TERM_TYPE_NAME)) {
//                        SubjectAreaOMASAPIResponse<Term> termResponse = termHandler.getTermByGuid(userId, entity.getGUID());
//                        if (termResponse.getRelatedHTTPCode() == 200) {
//                            nodes.add(termResponse.results().get(0));
//                        } else {
//                            response = termResponse;
//                            break;
//                        }
//                    } else  if (genericHandler.getRepositoryHelper().isTypeOf(serviceName, typeName, OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME)) {
//                        SubjectAreaOMASAPIResponse<Category> categoryResponse = categoryHandler.getCategoryByGuid(userId, entity.getGUID());
//                        if (categoryResponse.getRelatedHTTPCode() == 200) {
//                            nodes.add(categoryResponse.results().get(0));
//                        } else {
//                            response = categoryResponse;
//                            break;
//                        }
//                    } else  if (genericHandler.getRepositoryHelper().isTypeOf(serviceName, typeName, OpenMetadataType.GLOSSARY_TYPE_NAME)) {
//                        SubjectAreaOMASAPIResponse<Glossary> glossaryResponse = glossaryHandler.getCategoryByGuid(userId, entity.getGUID());
//                        if (glossaryResponse.getRelatedHTTPCode() == 200) {
//                            nodes.add(glossaryResponse.results().get(0));
//                        } else {
//                            response = glossaryResponse;
//                            break;
//                        }
//
//
//                    }
//                }
//                if (response.getRelatedHTTPCode() == 200) {
//                    response.addAllResults(nodes);
//                }
//
//            } catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
//                response.setExceptionInfo(e, className);
//            }
//        }
//
//        return response;
//
//    }
}
