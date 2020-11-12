/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

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
     * @param oMRSAPIHelper           omrs API helper
     * @param maxPageSize             maximum page size
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
                CategoryMapper categoryMapper = mappersFactory.get(CategoryMapper.class);
                EntityDetail categoryEntityDetail = categoryMapper.map(suppliedCategory);
                GlossarySummary suppliedGlossary = suppliedCategory.getGlossary();

                String glossaryGuid = validateGlossarySummaryDuringCreation(userId, methodName, suppliedGlossary);
                createdCategoryGuid = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, categoryEntityDetail);
                if (createdCategoryGuid != null) {
                    CategoryAnchor categoryAnchor = new CategoryAnchor();
                    categoryAnchor.getEnd1().setNodeGuid(glossaryGuid);
                    categoryAnchor.getEnd2().setNodeGuid(createdCategoryGuid);
                    CategoryAnchorMapper anchorMapper = mappersFactory.get(CategoryAnchorMapper.class);
                    Relationship relationship = anchorMapper.map(categoryAnchor);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                }

                if (suppliedCategory.getParentCategory() != null && suppliedCategory.getParentCategory().getGuid() != null) {
                    String parentCategoryGuid = suppliedCategory.getParentCategory().getGuid();
                    CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
                    categoryHierarchyLink.getEnd1().setNodeGuid(parentCategoryGuid);
                    categoryHierarchyLink.getEnd2().setNodeGuid(createdCategoryGuid);
                    CategoryHierarchyLinkMapper hierarchyMapper = mappersFactory.get(CategoryHierarchyLinkMapper.class);
                    Relationship relationship = hierarchyMapper.map(categoryHierarchyLink);
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

     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get. This could be a guid for a SubjectAreaDefinition, which is a type of category
     * @return response which when successful contains the category with the requested guid
     * when not successful the following Exception responses can occur
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
            Optional<EntityDetail> entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid, CATEGORY_TYPE_NAME, methodName);
            if (entityDetail.isPresent()) {
                CategoryMapper categoryMapper = mappersFactory.get(CategoryMapper.class);
                Category category = categoryMapper.map(entityDetail.get());
                setGlossary(userId, category, methodName);
                setParentCategory(userId, category, methodName);
                response.addResult(category);
            }
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException | SubjectAreaCheckedException e) {
            response.setExceptionInfo(e, className);
        }
        return response;
    }

    /**
     * Find Category
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param findRequest        {@link FindRequest}
     * @return A list of Glossaries meeting the search Criteria
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Category> findCategory(String userId, FindRequest findRequest) {
        final String methodName = "findCategory";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();

        try {
            List<Category> foundCategories = findEntities(userId, CATEGORY_TYPE_NAME, findRequest, CategoryMapper.class, methodName);

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
                                                                                         InvalidParameterException
    {
        final String guid = category.getSystemAttributes().getGUID();
        List<Relationship> relationships = oMRSAPIHelper.getRelationshipsByType(userId, guid, CATEGORY_TYPE_NAME, CATEGORY_ANCHOR_RELATIONSHIP_NAME, methodName);
        if (CollectionUtils.isNotEmpty(relationships)) {
            for (Relationship relationship : relationships) {
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
                                                                                         UserNotAuthorizedException
    {
        final String currentCategoryGuid = category.getSystemAttributes().getGUID();
        List<EntityDetail> foundEntities = oMRSAPIHelper.callGetEntitiesForRelationshipEnd2(
                methodName, userId, currentCategoryGuid, CATEGORY_TYPE_NAME, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME);
        if (CollectionUtils.isNotEmpty(foundEntities)) {
            for (EntityDetail entity : foundEntities) {
                String entityGUID = entity.getGUID();
                List<Relationship> relationships = oMRSAPIHelper.getRelationshipsByType(
                        userId, entityGUID, CATEGORY_TYPE_NAME, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, methodName);
                for (Relationship relationship : relationships) {
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
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param guid               guid
     * @param findRequest        {@link FindRequest}
     * @return the relationships associated with the requested Category guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse<Line> getCategoryRelationships(String userId, String guid, FindRequest findRequest) {
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
                if (isReplace)
                    // copy over attributes
                    replaceAttributes(currentCategory, suppliedCategory);
                else
                    updateAttributes(currentCategory, suppliedCategory);

                Date termFromTime = suppliedCategory.getEffectiveFromTime();
                Date termToTime = suppliedCategory.getEffectiveToTime();
                currentCategory.setEffectiveFromTime(termFromTime);
                currentCategory.setEffectiveToTime(termToTime);

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

     * @param userId     userId under which the request is performed
     * @param guid       guid of the category to be deleted.
     * @param isPurge    true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
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
            if (isPurge) {
                oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, CATEGORY_TYPE_NAME, guid);
            } else {
                oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, CATEGORY_TYPE_NAME, guid);
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

     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to restore
     * @return response which when successful contains the restored category
     * when not successful the following Exception responses can occur
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
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param termHandler term handler
     * @param startingFrom initial position in the stored list.
     * @param pageSize     maximum number of definitions to return on this call.
     * @return A list of terms is categorized by this Category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     * */
    public SubjectAreaOMASAPIResponse<Term> getCategorizedTerms(String userId, String guid, SubjectAreaTermHandler termHandler, Integer startingFrom, Integer pageSize) {
        final String methodName = "getTerms";
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        SubjectAreaOMASAPIResponse<Term>  response = getRelatedNodesForEnd1(methodName, userId, guid, TERM_CATEGORIZATION_RELATIONSHIP_NAME,  TermMapper.class, startingFrom, pageSize);
        List<Term> allTerms = new ArrayList<>();
        // the terms we get back from the mappers only map the parts from the entity. They do not set the glossary.
        if (response.getRelatedHTTPCode() == 200 && response.results() !=null && response.results().size() >0) {
            for (Term mappedCategory: response.results()) {
                SubjectAreaOMASAPIResponse<Term> termResponse = termHandler.getTermByGuid(userId, mappedCategory.getSystemAttributes().getGUID());
                if (termResponse.getRelatedHTTPCode() == 200) {
                    allTerms.add(termResponse.results().get(0));
                } else {
                    response = termResponse;
                    break;
                }
            }
        }
        if (response.getRelatedHTTPCode() == 200) {
            response = new SubjectAreaOMASAPIResponse<>();
            response.addAllResults(allTerms);
        }
        return response;
    }
    /**
     * Get this Category's child Categories. The server has a maximum page size defined, the number of Categories returned is limited by that maximum page size.
     *
     * @param userId       unique identifier for requesting user, under which the request is performed
     * @param guid         guid of the category to get terms
     * @param startingFrom the starting element number for this set of results.  This is used when retrieving elements
     * @param pageSize     the maximum number of elements that can be returned on this request.
     * @return A list of child categories
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> PropertyServerException              Property server exception. </li>
     * </ul>
     **/
    public SubjectAreaOMASAPIResponse<Category> getCategoryChildren(String userId, String guid, Integer startingFrom, Integer pageSize) {
        final String methodName = "getCategoryChildren";
        SubjectAreaOMASAPIResponse<Category> response = new SubjectAreaOMASAPIResponse<>();
        if (pageSize == null) {
            pageSize = maxPageSize;
        }
        SubjectAreaOMASAPIResponse<Category> thisCategoryResponse = getCategoryByGuid(userId, guid);
        if (thisCategoryResponse.getRelatedHTTPCode() == 200) {
            boolean foundParent = false;
            String parentCategoryGuid = thisCategoryResponse.results().get(0).getSystemAttributes().getGUID();
            SubjectAreaOMASAPIResponse<Category> relatedCategories = getRelatedNodesForEnd1(methodName, userId, guid, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME,  CategoryMapper.class, startingFrom, pageSize);
            if (relatedCategories != null && relatedCategories.results() != null && relatedCategories.results().size() >0 ) {
                for (Category relatedCategory: relatedCategories.results()) {
                    // only add related categories that are not the parent Category.
                    if (relatedCategory.getSystemAttributes().getGUID().equals(parentCategoryGuid)) {
                        foundParent = true;
                    } else {
                        response.addResult(relatedCategory);
                    }
                }

                // if we got back a page of categories and we found a parent which we removed from the results then we need to get another category, which cant be a parent as
                // a category can only have one parent.
                if (foundParent && relatedCategories.results().size() == maxPageSize ) {
                    SubjectAreaOMASAPIResponse<Category>  extraCategoryResponse = getRelatedNodesForEnd1(methodName, userId, guid, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME, CategoryMapper.class, maxPageSize, 1);
                    if (extraCategoryResponse.getRelatedHTTPCode() == 200 && extraCategoryResponse.results() != null && extraCategoryResponse.results().size() ==1 ) {
                        response.addResult(extraCategoryResponse.results().get(0));
                    }
                }
            }
        }

        return response;
    }
}