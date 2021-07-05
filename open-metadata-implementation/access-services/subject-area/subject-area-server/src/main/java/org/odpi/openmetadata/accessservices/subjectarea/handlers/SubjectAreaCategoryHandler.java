/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.CategoryAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.CategoryHierarchyLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaCategoryHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaCategoryHandler.class.getName();


    /**
     * Construct the Subject Area Category Handler
     * needed to operate within a single server instance.
     *
     * @param oMRSAPIHelper omrs API helper
     * @param maxPageSize   maximum page size
     */
    public SubjectAreaCategoryHandler(OMRSAPIHelper oMRSAPIHelper, int maxPageSize) {
        super(oMRSAPIHelper, maxPageSize);
    }

    /**
     * Create a Category. There is specialization of a Category that can also be created using this operation.
     * To create this specialization, you should specify a nodeType other than Category in the supplied category.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     * <li>SubjectAreaDefinition to create a Category that represents a subject area </li>
     * <li>Category to create a category that is not a subject area</li>
     * </ul>
     * <p>
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryCategory concatinated with the the guid.
     *
     * <p>
     * Failure to create the Categories classifications, link to its glossary or its icon, results in the create failing and the category being deleted
     *
     * @param userId           unique identifier for requesting user, under which the request is performed
     * @param suppliedCategory category to create
     * @return response, when successful contains the created category.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> createCategory(String userId, Category suppliedCategory) {
        final String methodName = "createCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        String createdCategoryGuid = null;

        try {
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);
            // need to check we have a name
            final String suppliedCategoryName = suppliedCategory.getName();
            if (suppliedCategoryName == null || suppliedCategoryName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "Name", null);
            } else {
                setUniqueQualifiedNameIfBlank(suppliedCategory);
                GlossarySummary suppliedGlossary = suppliedCategory.getGlossary();
                String glossaryGuid = validateGlossarySummaryDuringCreation(userId, methodName, suppliedGlossary);
                GlossaryCategoryBuilder builder = new GlossaryCategoryBuilder(suppliedCategory.getQualifiedName(),
                                              suppliedCategory.getName(),
                                              suppliedCategory.getDescription(),
                                              genericHandler.getRepositoryHelper(),
                                              genericHandler.getServiceName(),
                                              genericHandler.getServerName());
                createdCategoryGuid = genericHandler.createBeanInRepository(userId,
                                                                            null,
                                                                            null,
                                                                            OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                                                            OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                            null,
                                                                            null,
                                                                            builder,
                                                                            methodName);

                if (createdCategoryGuid != null) {

                    // set effectivity dates if required
                    setEffectivity(userId,
                                   suppliedCategory,
                                   methodName,
                                   createdCategoryGuid,
                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME);

                    CategoryAnchor categoryAnchor = new CategoryAnchor();
                    categoryAnchor.getEnd1().setNodeGuid(glossaryGuid);
                    categoryAnchor.getEnd2().setNodeGuid(createdCategoryGuid);
                    // we expect that the created category has a from time of now or the supplied value.
                    // set the relationship from value to the same
//                    categoryAnchor.setEffectiveFromTime(instanceProperties.getEffectiveFromTime().getTime());
//                    if (instanceProperties.getEffectiveToTime() != null) {
//                        categoryAnchor.setEffectiveToTime(instanceProperties.getEffectiveToTime().getTime());
//                    }
                    CategoryAnchorMapper anchorMapper = mappersFactory.get(CategoryAnchorMapper.class);
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship = anchorMapper.map(categoryAnchor);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                }

                if (suppliedCategory.getParentCategory() != null && suppliedCategory.getParentCategory().getGuid() != null) {
                    String parentCategoryGuid = suppliedCategory.getParentCategory().getGuid();
                    CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
                    categoryHierarchyLink.getEnd1().setNodeGuid(parentCategoryGuid);
                    categoryHierarchyLink.getEnd2().setNodeGuid(createdCategoryGuid);
                    // we expect that the created category has a from time of now or the supplied value.
                    // set the relationship from value to the same
//                    categoryHierarchyLink.setEffectiveFromTime(instanceProperties.getEffectiveFromTime().getTime());
//                    if (instanceProperties.getEffectiveToTime() != null) {
//                        categoryHierarchyLink.setEffectiveToTime(instanceProperties.getEffectiveToTime().getTime());
//                    }
                    CategoryHierarchyLinkMapper hierarchyMapper = mappersFactory.get(CategoryHierarchyLinkMapper.class);
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship = hierarchyMapper.map(categoryHierarchyLink);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                }
                response = getCategoryByGuid(userId, createdCategoryGuid);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            //if the entity is created, but subsequently an error occurred while creating the relationship
            if (createdCategoryGuid != null) {
                deleteCategory(userId, createdCategoryGuid, false);
                deleteCategory(userId, createdCategoryGuid, true);
            }
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Get a Category
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get. This could be a guid for a SubjectAreaDefinition, which is a type of category
     * @return response which when successful contains the category with the requested guid
     * n not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> getCategoryByGuid(String userId, String guid) {
        final String methodName = "getCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        try {
            EntityDetail entityDetail = genericHandler.getEntityFromRepository(userId,
                                                                               guid,
                                                                               "guid",
                                                                               OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               null,
                                                                               methodName);
                CategoryMapper categoryMapper = mappersFactory.get(CategoryMapper.class);
                Category category = categoryMapper.map(entityDetail);
                setGlossary(userId, category, methodName);
                setParentCategory(userId, category, methodName);
                response.addResult(category);

        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException | SubjectAreaCheckedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Find Category
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param findRequest {@link FindRequest}
     * @param exactValue  a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase  a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @return A list of Categories meeting the search Criteria
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> findCategory(String userId, FindRequest findRequest, boolean exactValue, boolean ignoreCase) {
        final String methodName = "findCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        try {
            List<Category> foundCategories = findNodes(userId, OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME, OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,  findRequest, exactValue, ignoreCase, CategoryMapper.class, methodName);

            if (foundCategories != null) {
                for (Category category : foundCategories) {
                    setGlossary(userId, category, methodName);
                    setParentCategory(userId, category, methodName);
                    response.addResult(category);
                }
            }
        } catch (UserNotAuthorizedException | SubjectAreaCheckedException | InvalidParameterException | PropertyServerException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    private void setGlossary(String userId, Category category, String methodName) throws SubjectAreaCheckedException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         InvalidParameterException {
        final String guid = category.getSystemAttributes().getGUID();
        List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships = oMRSAPIHelper.getRelationshipsByType(userId, guid, CATEGORY_TYPE_NAME, CATEGORY_ANCHOR_RELATIONSHIP_NAME, methodName);
        if (CollectionUtils.isNotEmpty(relationships)) {
            for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship : relationships) {
                CategoryAnchorMapper categotyAnchorMapper = mappersFactory.get(CategoryAnchorMapper.class);
                CategoryAnchor termAnchor = categotyAnchorMapper.map(relationship);
                GlossarySummary glossarySummary = getGlossarySummary(methodName, userId, termAnchor);
                if (glossarySummary != null) {
                    category.setGlossary(glossarySummary);
                    break;
                }
            }
        }
        // return the Term without a Glossary summary as we have not got one.
    }


    private void setParentCategory(String userId, Category category, String methodName) throws SubjectAreaCheckedException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException {
        final String currentCategoryGuid = category.getSystemAttributes().getGUID();
        List<EntityDetail> foundEntities = oMRSAPIHelper.callGetEntitiesForRelationshipEnd2(
                methodName, userId, currentCategoryGuid, CATEGORY_TYPE_NAME, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME);
        if (CollectionUtils.isNotEmpty(foundEntities)) {
            for (EntityDetail entity : foundEntities) {
                String entityGUID = entity.getGUID();
                List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships = oMRSAPIHelper.getRelationshipsByType(
                        userId, entityGUID, CATEGORY_TYPE_NAME, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, methodName);
                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship relationship : relationships) {
                    String parentGuid = relationship.getEntityOneProxy().getGUID();
                    String childGuid = relationship.getEntityTwoProxy().getGUID();
                    if (entityGUID.equals(parentGuid) && currentCategoryGuid.equals(childGuid)) {
                        CategoryHierarchyLinkMapper hierarchyMapper = mappersFactory.get(CategoryHierarchyLinkMapper.class);
                        CategoryMapper categoryMapper = mappersFactory.get(CategoryMapper.class);
                        CategoryHierarchyLink link = hierarchyMapper.map(relationship);
                        Category parentCategory = categoryMapper.map(entity);
                        category.setParentCategory(SubjectAreaUtils.extractCategorySummaryFromCategory(parentCategory, link));
                    }
                }
            }
        }
    }

    /**
     * Get Category relationships
     *
     * @param userId      unique identifier for requesting user, under which the request is performed
     * @param guid        guid
     * @param findRequest {@link FindRequest}
     * @return the relationships associated with the requested Category guid
     * <p>
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Relationship> getCategoryRelationships(String userId, String guid, FindRequest findRequest) {
        String restAPIName = "getCategoryRelationships";
        return getAllRelationshipsForEntity(restAPIName, userId, guid, findRequest);
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param userId           userId under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory category to be updated
     * @param isReplace        flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> updateCategory(String userId, String guid, Category suppliedCategory, boolean isReplace) {
        final String methodName = "updateCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        try {
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);

            response = getCategoryByGuid(userId, guid);
            if (response.head().isPresent()) {
                Category currentCategory = response.head().get();
                checkReadOnly(methodName, currentCategory, "update");
                if (isReplace)
                    // copy over attributes
                    replaceAttributes(currentCategory, suppliedCategory);
                else
                    updateAttributes(currentCategory, suppliedCategory);

                Long categoryFromTime = suppliedCategory.getEffectiveFromTime();
                Long categoryToTime = suppliedCategory.getEffectiveToTime();
                currentCategory.setEffectiveFromTime(categoryFromTime);
                currentCategory.setEffectiveToTime(categoryToTime);

                CategoryMapper mapper = mappersFactory.get(CategoryMapper.class);
                EntityDetail entityDetail = mapper.map(currentCategory);
                oMRSAPIHelper.callOMRSUpdateEntity(methodName, userId, entityDetail);
                response = getCategoryByGuid(userId, guid);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    private void replaceAttributes(Category currentCategory, Category newCategory) {
        currentCategory.setName(newCategory.getName());
        currentCategory.setQualifiedName(newCategory.getQualifiedName());
        currentCategory.setDescription(newCategory.getDescription());
        currentCategory.setAdditionalProperties(newCategory.getAdditionalProperties());
        //TODO handle classifications
    }

    private void updateAttributes(Category currentCategory, Category newTerm) {
        // copy over attributes if specified
        if (newTerm.getName() != null) {
            currentCategory.setName(newTerm.getName());
        }
        if (newTerm.getQualifiedName() != null) {
            currentCategory.setQualifiedName(newTerm.getQualifiedName());
        }
        if (newTerm.getDescription() != null) {
            currentCategory.setDescription(newTerm.getDescription());
        }

        if (newTerm.getAdditionalProperties() != null) {
            currentCategory.setAdditionalProperties(newTerm.getAdditionalProperties());
        }
        //TODO handle classifications
    }

    /**
     * Delete a Category or SubjectAreaDefinition instance
     * <p>
     * There are 2 types of deletion, a soft delete and a hard delete (also known as a purge). All repositories support hard deletes. Soft deletes support
     * is optional. Soft delete is the default.
     * <p>
     * A soft delete means that the category instance will exist in a deleted state in the repository after the delete operation. This means
     * that it is possible to undo the delete.
     * A hard delete means that the category will not exist after the operation.
     * when not successful the following Exception responses can occur
     *
     * @param userId  userId under which the request is performed
     * @param guid    guid of the category to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * n not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * <li> EntityNotDeletedException            a soft delete was issued but the category was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> deleteCategory(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        try {
            response = getCategoryByGuid(userId, guid);
            if (response.head().isPresent()) {
                Category categoryToBeDeleted = response.head().get();
                checkReadOnly(methodName, categoryToBeDeleted, "delete");
            }
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, CATEGORY_TYPE_NAME, guid);
            } else {
                genericHandler.deleteBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      "guid",
                                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                      null,
                                                      methodName);
            }
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Restore a Category or a SubjectAreaDefinition
     * <p>
     * Restore allows the deleted Category to be made active again. Restore allows deletes to be undone. Hard deletes are not stored in the repository so cannot be restored.
     *
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to restore
     * @return response which when successful contains the restored category
     * n not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> restoreCategory(String userId, String guid) {
        final String methodName = "restoreCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        try {
            this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            response = getCategoryByGuid(userId, guid);
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }

    /**
     * Get the terms that are categorized by this Category
     *
     * @param userId         unique identifier for requesting user, under which the request is performed
     * @param guid           guid of the category to get terms
     * @param searchCriteria String expression to match the categorized Term property values.
     * @param exactValue     a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase     a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param startingFrom   the starting element number for this set of results.  This is used when retrieving elements
     * @param termHandler    term handler
     * @param startingFrom   initial position in the stored list.
     * @param pageSize       maximum number of definitions to return on this call.
     * @return A list of terms categorized by this Category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Term> getCategorizedTerms(String userId, String guid, String searchCriteria, boolean exactValue, boolean ignoreCase, SubjectAreaTermHandler termHandler, Integer startingFrom, Integer pageSize) {
        final String methodName = "getCategorizedTerms";
        SubjectAreaOMASAPIResponse<Term> response = new SubjectAreaOMASAPIResponse<>();
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        if (startingFrom == null) {
            startingFrom = 0;
        }
        // this is used to track getting all the pages of all the data from the start
        // We use this to issue calls to get all the data so we can then apply the filter.
        Integer unfilteredStartingFrom = 0;

        SubjectAreaOMASAPIResponse<Category> thisCategoryResponse = getCategoryByGuid(userId, guid);
        if (thisCategoryResponse.getRelatedHTTPCode() == 200) {
            List<Term> filteredTerms = new ArrayList<>();
            boolean continueGettingTerms = true;
            while (continueGettingTerms) {
                SubjectAreaOMASAPIResponse<Term> childTermsResponse = getRelatedNodesForEnd1(methodName, userId, guid, TERM_CATEGORIZATION_RELATIONSHIP_NAME, TermMapper.class, unfilteredStartingFrom, pageSize);
                if (childTermsResponse.results() != null && childTermsResponse.results().size() > 0) {
                   // we now have results. We need to filter those results then apply the requested startingFrom

                    for (Term term : childTermsResponse.results()) {
                        // this term to the results if it matches the search criteria and we have not already got a page of results to return
                        if (termMatchSearchCriteria(term, searchCriteria, exactValue, ignoreCase) ) {
                            filteredTerms.add(term);
                        }
                    }
                }

                if (childTermsResponse.results().size() < pageSize || filteredTerms.size() >= startingFrom + pageSize) {
                    // we have a page to return or the last get returned less than a page.
                    continueGettingTerms = false;
                } else {
                    // issue another call to get another page of terms
                    unfilteredStartingFrom = unfilteredStartingFrom + pageSize;
                }
            }
            // we have a list of filteredTerms , we need to slice out the page we need to return to the user based on their requested startingFrom

            if (filteredTerms.size() > startingFrom) {
                // we have something to return
                int endingAt = startingFrom + pageSize;
                if (filteredTerms.size() < startingFrom + pageSize) {
                    // if there is not a page worth - then calculate the ending index of the list.
                    endingAt = filteredTerms.size();
                }
                response.addAllResults(filteredTerms.subList(startingFrom, endingAt));
            }

        }

        return response;
    }

    /**
     * Get this Category's child Categories. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param userId         unique identifier for requesting user, under which the request is performed
     * @param guid           guid of the parent category
     * @param searchCriteria String expression matching child Category property values.
     * @param exactValue     a boolean, which when set means that only exact matches will be returned, otherwise matches that start with the search criteria will be returned.
     * @param ignoreCase     a boolean, which when set means that case will be ignored, if not set that case will be respected
     * @param startingFrom   the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize       the maximum number of elements that can be returned on this request.
     * @return A list of child categories filtered by the search criteria if one is supplied.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     **/
    public SubjectAreaOMASAPIResponse<Category> getCategoryChildren(String userId, String guid, String searchCriteria, boolean exactValue, boolean ignoreCase, Integer startingFrom, Integer pageSize) {
        final String methodName = "getCategoryChildren";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        if (startingFrom == null) {
            startingFrom = 0;
        }

        SubjectAreaOMASAPIResponse<Category> thisCategoryResponse = getCategoryByGuid(userId, guid);
        if (thisCategoryResponse.getRelatedHTTPCode() == 200) {
            String parentCategoryGuid = thisCategoryResponse.results().get(0).getSystemAttributes().getGUID();

            List<Category> categoriesToReturn = new ArrayList<>();
            // we have more to get, bump up the startingFrom and get the next page
            boolean continueGettingCategories = true;
            while (continueGettingCategories) {
                SubjectAreaOMASAPIResponse<Category> relatedCategoriesResponse = getRelatedNodesForEnd1(methodName, userId, guid, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, CategoryMapper.class, startingFrom, pageSize);
                if (relatedCategoriesResponse.results() != null && relatedCategoriesResponse.results().size() > 0) {
                    for (Category relatedCategory : relatedCategoriesResponse.results()) {
                        // this category to the results if it is not the parent category and it matches the search criteria
                        if (categoryMatchSearchCriteria(relatedCategory, searchCriteria, exactValue, ignoreCase) &&
                                !parentCategoryGuid.equals(relatedCategory.getSystemAttributes().getGUID()) &&
                                categoriesToReturn.size() < pageSize) {
                            categoriesToReturn.add(relatedCategory);
                        }
                    }
                }
                if (relatedCategoriesResponse.results().size() < pageSize || categoriesToReturn.size() == pageSize) {
                    // we have a page to return or the last get returned less than a page.
                    continueGettingCategories = false;
                } else {
                    // issue another call to get another page of terms
                    startingFrom = startingFrom + pageSize;
                }
            }
            response.addAllResults(categoriesToReturn);
        }

        return response;
    }

}