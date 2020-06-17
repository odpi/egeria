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
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse2;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
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
     */
    public SubjectAreaCategoryHandler(OMRSAPIHelper oMRSAPIHelper) {
        super(oMRSAPIHelper);
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
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse2<Category> createCategory(String userId, Category suppliedCategory) {
        final String methodName = "createCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();
        String createdCategoryGuid = null;

        try {
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);
            // need to check we have a name
            final String suppliedCategoryName = suppliedCategory.getName();
            if (suppliedCategoryName == null || suppliedCategoryName.equals("")) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME.getMessageDefinition();
                throw new InvalidParameterException(messageDefinition, className, methodName, "Name", null);
            } else {
                CategoryMapper categoryMapper = mappersFactory.get(CategoryMapper.class);
                EntityDetail categoryEntityDetail = categoryMapper.map(suppliedCategory);
                GlossarySummary suppliedGlossary = suppliedCategory.getGlossary();

                String glossaryGuid = validateGlossarySummaryDuringCreation(userId, methodName, suppliedGlossary);
                createdCategoryGuid = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, categoryEntityDetail);
                if (createdCategoryGuid != null) {
                    CategoryAnchor categoryAnchor = new CategoryAnchor();
                    categoryAnchor.setGlossaryGuid(glossaryGuid);
                    categoryAnchor.setCategoryGuid(createdCategoryGuid);
                    CategoryAnchorMapper anchorMapper = mappersFactory.get(CategoryAnchorMapper.class);
                    Relationship relationship = anchorMapper.map(categoryAnchor);
                    oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                }

                if (suppliedCategory.getParentCategory() != null && suppliedCategory.getParentCategory().getGuid() != null) {
                    String parentCategoryGuid = suppliedCategory.getParentCategory().getGuid();
                    CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
                    categoryHierarchyLink.setSuperCategoryGuid(parentCategoryGuid);
                    categoryHierarchyLink.setSubCategoryGuid(createdCategoryGuid);
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
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata repository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * </ul>
     */

    public SubjectAreaOMASAPIResponse2<Category> getCategoryByGuid(String userId, String guid) {
        final String methodName = "getCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();

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
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     *
     * </ul>
     */
    public SubjectAreaOMASAPIResponse2<Category> findCategory(String userId, FindRequest findRequest) {
        final String methodName = "findCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();

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
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse2<Line> getCategoryRelationships(String userId, String guid, FindRequest findRequest) {
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse2<Category> updateCategory(String userId, String guid, Category suppliedCategory, boolean isReplace) {
        final String methodName = "updateCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();
        try {
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);

            response = getCategoryByGuid(userId, guid);
            if (response.getHead() != null) {
                Category currentCategory = (Category) response.getHead();
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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the category was not deleted.</li>
     * <li> EntityNotPurgedException               a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse2<Category> deleteCategory(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();

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
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse2<Category> restoreCategory(String userId, String guid) {
        final String methodName = "restoreCategory";
        SubjectAreaOMASAPIResponse2<Category> response = new SubjectAreaOMASAPIResponse2<>();

        try {
            this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            response = getCategoryByGuid(userId, guid);
        } catch (SubjectAreaCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            response.setExceptionInfo(e, className);
        }

        return response;
    }
}