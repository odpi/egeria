/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.EntityDetailsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.GlossarySummaryResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.CategoryAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.CategoryHierarchyLinkMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaCategoryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.TypeGuids;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The SubjectAreaRESTServicesInstance provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaCategoryHandler extends SubjectAreaHandler {

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaCategoryHandler.class);

    private static final String className = SubjectAreaCategoryHandler.class.getName();


    /**
     * Construct the Subject Area Category Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param oMRSAPIHelper           omrs API helper
     * @param errorHandler            handler for repository service errors
     */
    public SubjectAreaCategoryHandler(String serviceName,
                                      String serverName,
                                      InvalidParameterHandler invalidParameterHandler,
                                      OMRSRepositoryHelper repositoryHelper,
                                      RepositoryHandler repositoryHandler,
                                      OMRSAPIHelper oMRSAPIHelper,
                                      RepositoryErrorHandler errorHandler) {
        super(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler, oMRSAPIHelper, errorHandler);
    }

    /**
     * Take an entityDetail response and map it to the appropriate Term orientated response
     * @param response entityDetailResponse
     * @return Term response containing the appropriate Term object.
     */
    @Override
    protected SubjectAreaOMASAPIResponse getResponse( SubjectAreaOMASAPIResponse response) {
        EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
        EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
        CategoryMapper categoryMapper = new CategoryMapper(oMRSAPIHelper);

        try {
            Category category= categoryMapper.mapEntityDetailToNode(entityDetail);
            if (category.getNodeType()==NodeType.SubjectAreaDefinition) {
                SubjectAreaDefinition subjectArea = (SubjectAreaDefinition)category;
                response = new CategoryResponse(subjectArea);
            } else {
                response = new CategoryResponse(category);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
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
     * @param glossaryHandler  glossary handler
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

    public SubjectAreaOMASAPIResponse createCategory( SubjectAreaGlossaryHandler glossaryHandler, String userId, Category suppliedCategory) {
        final String methodName = "createCategory";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);
            SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
            String suppliedCategoryParentGuid = null;
            if (suppliedCategory.getParentCategory() != null) {
                //store the parent category guid
                suppliedCategoryParentGuid = suppliedCategory.getParentCategory().getGuid();
            }

            // need to check we have a name
            final String suppliedCategoryName = suppliedCategory.getName();
            if (suppliedCategoryName == null || suppliedCategoryName.equals("")) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
            }
            GlossarySummary suppliedGlossary = suppliedCategory.getGlossary();
            SubjectAreaOMASAPIResponse glossaryResponse = validateGlossarySummaryDuringCreation(glossaryHandler, userId, methodName, suppliedGlossary);
            if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary)) {
                // the glossary that was supplied is valid.
                EntityDetail entityDetail = new CategoryMapper(oMRSAPIHelper).mapNodeToEntityDetail(suppliedCategory);

                response = oMRSAPIHelper.callOMRSAddEntity(methodName, userId, entityDetail);
                if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    entityDetail = entityDetailResponse.getEntityDetail();
                    String categoryGuid = entityDetail.getGUID();
                    // Knit the Category to the supplied glossary
                    // get the associated glossary
                    Glossary associatedGlossary = ((GlossaryResponse) glossaryResponse).getGlossary();
                    String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
                    CategoryAnchor categoryAnchor = new CategoryAnchor();
                    categoryAnchor.setGlossaryGuid(glossaryGuid);
                    categoryAnchor.setCategoryGuid(categoryGuid);
                    Relationship categoryAnchorRelationship = new CategoryAnchorMapper(oMRSAPIHelper).mapLineToRelationship(categoryAnchor);
                    response = oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, categoryAnchorRelationship);
                    if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                        if (suppliedCategoryParentGuid != null) {
                            // Knit the Category to the supplied parent
                            CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
                            categoryHierarchyLink.setSuperCategoryGuid(suppliedCategoryParentGuid);
                            categoryHierarchyLink.setSubCategoryGuid(categoryGuid);
                            Relationship relationship = new CategoryHierarchyLinkMapper(oMRSAPIHelper).mapLineToRelationship(categoryHierarchyLink);
                            response = oMRSAPIHelper.callOMRSAddRelationship(methodName, userId, relationship);
                        }
                        if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                            response = getCategory(userId, categoryGuid);
                        }
                    }
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }


        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Get a Category
     *
    
     * @param userId     unique identifier for requesting user, under which the request is performed
     * @param guid       guid of the category to get. This could be a guid for a SubjectAreaDefintion, which is type of category
     * @return response which when successful contains the category with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getCategory(String userId, String guid) {
        final String methodName = "getCategory";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = oMRSAPIHelper.callOMRSGetEntityByGuid(methodName, userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                EntityDetail gotEntityDetail = entityDetailResponse.getEntityDetail();
                CategoryMapper categoryMapper = new CategoryMapper(oMRSAPIHelper);
                Category gotCategory = (Category) categoryMapper.mapEntityDetailToNode(gotEntityDetail);
                String anchorTypeGuid = TypeGuids.getCategoryAnchorTypeGuid();
                response = oMRSAPIHelper.callGetRelationshipsForEntity(methodName, userId, guid, anchorTypeGuid, 0, null, null, null, 0);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsRelationships)) {
                    RelationshipsResponse relationshipsResponse = (RelationshipsResponse) response;
                    List<Relationship> glossaryRelationships = relationshipsResponse.getRelationships();
                    if (glossaryRelationships.iterator().hasNext()) {
                        Relationship glossaryRelationship = glossaryRelationships.iterator().next();
                        CategoryAnchor categoryAnchor = (CategoryAnchor) new CategoryAnchorMapper(oMRSAPIHelper).mapRelationshipToLine(glossaryRelationship);
                        response = SubjectAreaUtils.getGlossarySummaryForCategory(methodName, userId, oMRSAPIHelper, categoryAnchor);
                        if (response.getResponseCategory().equals(ResponseCategory.GlossarySummary)) {
                            GlossarySummaryResponse glossarySummaryResponse = (GlossarySummaryResponse) response;
                            GlossarySummary glossarySummary = glossarySummaryResponse.getGlossarySummary();
                            gotCategory.setGlossary(glossarySummary);
                            response = createCategoryResponse(gotCategory);
                        }
                    } else {
                        // return the Category without a Glossary summary as we have not got one.
                        response = createCategoryResponse(gotCategory);
                    }
                }
            }

        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }

    protected SubjectAreaOMASAPIResponse createCategoryResponse(Category gotCategory) {
        SubjectAreaOMASAPIResponse response;
        if (gotCategory.getNodeType() == NodeType.SubjectAreaDefinition) {
            SubjectAreaDefinition subjectAreaDefinition = (SubjectAreaDefinition) gotCategory;
            response = new SubjectAreaDefinitionResponse(subjectAreaDefinition);
        } else {
            response = new CategoryResponse(gotCategory);
        }
        return response;
    }

    /**
     * Find Category
     *
     * @param userId             unique identifier for requesting user, under which the request is performed
     * @param searchCriteria     String expression matching Category property values (this does not include the CategorySummary content). When not specified, all terms are returned.
     * @param asOfTime           the relationships returned as they were at this time. null indicates at the current time.
     * @param offset             the starting element number for this set of results.  This is used when retrieving elements
     *                           beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize           the maximum number of elements that can be returned on this request.
     *                           0 means there is no limit to the page size
     * @param sequencingOrder    the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return A list of Glossaries meeting the search Criteria
     *
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     *
     * </ul>
     */
    public SubjectAreaOMASAPIResponse findCategory(String userId,
                                                   String searchCriteria,
                                                   Date asOfTime,
                                                   Integer offset,
                                                   Integer pageSize,
                                                   org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                   String sequencingProperty) {

        final String methodName = "findCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        response = this.oMRSAPIHelper.findEntitiesByPropertyValue(methodName, userId, "GlossaryCategory", searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty, methodName);
        if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetails) {
            EntityDetailsResponse entityDetailsResponse = (EntityDetailsResponse) response;
            List<EntityDetail> entitydetails = entityDetailsResponse.getEntityDetails();
            List<Category> categories = new ArrayList<>();
            if (entitydetails == null) {
                response = new CategoriesResponse(categories);
            } else {
                for (EntityDetail entityDetail : entitydetails) {
                    // call the getCategory so that the GlossarySummary and other parts are populated.
                    response = getCategory(userId, entityDetail.getGUID());
                    if (response.getResponseCategory() == ResponseCategory.Category) {
                        CategoryResponse categoryResponse = (CategoryResponse) response;
                        Category category = categoryResponse.getCategory();
                        categories.add(category);
                    } else {
                        break;
                    }
                }
                if (response.getResponseCategory() == ResponseCategory.Category) {
                    response = new CategoriesResponse(categories);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /**
     * Get Category relationships
     *
    
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested Category guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException the requesting user is not authorized to issue this request.</li>
     * <li> InvalidParameterException one of the parameters is null or invalid.</li>
     * <li> FunctionNotSupportedException   Function not supported.</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getCategoryRelationships(String userId, String guid,
                                                               Date asOfTime,
                                                               Integer offset,
                                                               Integer pageSize,
                                                               org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                               String sequencingProperty
                                                              ) {
        String restAPIName = "getCategoryRelationships";
        return getRelationshipsFromGuid(restAPIName, userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
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
    public SubjectAreaOMASAPIResponse updateCategory(String userId, String guid, Category suppliedCategory, boolean isReplace) {
        final String methodName = "updateCategory";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateUserIdNotNull(className, methodName, userId);
            InputValidator.validateNodeType(className, methodName, suppliedCategory.getNodeType(), NodeType.Category, NodeType.SubjectAreaDefinition);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = getCategory(userId, guid);
            if (response.getResponseCategory().equals(ResponseCategory.Category)) {
                Category originalCategory = ((CategoryResponse) response).getCategory();
                if (originalCategory.getSystemAttributes() != null) {
                    Status status = originalCategory.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                }
                if (suppliedCategory.getSystemAttributes() != null) {
                    Status status = suppliedCategory.getSystemAttributes().getStatus();
                    SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                }
                Category updateCategory = originalCategory;
                if (isReplace) {
                    // copy over attributes
                    updateCategory.setName(suppliedCategory.getName());
                    updateCategory.setQualifiedName(suppliedCategory.getQualifiedName());
                    updateCategory.setDescription(suppliedCategory.getDescription());
                    updateCategory.setAdditionalProperties(suppliedCategory.getAdditionalProperties());
                    //TODO handle classifications
                } else {
                    // copy over attributes if specified
                    if (suppliedCategory.getName() != null) {
                        updateCategory.setName(suppliedCategory.getName());
                    }
                    if (suppliedCategory.getQualifiedName() != null) {
                        updateCategory.setQualifiedName(suppliedCategory.getQualifiedName());
                    }
                    if (suppliedCategory.getDescription() != null) {
                        updateCategory.setDescription(suppliedCategory.getDescription());
                    }

                    if (suppliedCategory.getAdditionalProperties() != null) {
                        updateCategory.setAdditionalProperties(suppliedCategory.getAdditionalProperties());
                    }
                    //TODO handle classifications
                }
                Date termFromTime = suppliedCategory.getEffectiveFromTime();
                Date termToTime = suppliedCategory.getEffectiveToTime();
                updateCategory.setEffectiveFromTime(termFromTime);
                updateCategory.setEffectiveToTime(termToTime);
                CategoryMapper mapper = new CategoryMapper(oMRSAPIHelper);
                EntityDetail updateEntityDetail = mapper.mapNodeToEntityDetail(updateCategory);
                String categoryGuid = updateCategory.getSystemAttributes().getGUID();
                response = oMRSAPIHelper.callOMRSUpdateEntityProperties(methodName, userId, updateEntityDetail);
                if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                    response = getCategory(userId, categoryGuid);
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
     * <li> GUIDNotPurgedException               a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteCategory(String userId, String guid, Boolean isPurge) {
        final String methodName = "deleteCategory";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        OMRSRepositoryHelper repositoryHelper = this.oMRSAPIHelper.getOMRSRepositoryHelper();
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            String source = oMRSAPIHelper.getServiceName();
            String typeDefName = "GlossaryCategory";
            String typeDefGuid = repositoryHelper.getTypeDefByName(source, typeDefName).getGUID();
            if (isPurge) {
                response = oMRSAPIHelper.callOMRSPurgeEntity(methodName, userId, typeDefName, typeDefGuid, guid);
            } else {
                response = oMRSAPIHelper.callOMRSDeleteEntity(methodName, userId, typeDefName, typeDefGuid, guid);
                if (response.getResponseCategory().equals(ResponseCategory.OmrsEntityDetail)) {
                    EntityDetailResponse entityDetailResponse = (EntityDetailResponse) response;
                    EntityDetail entityDetail = entityDetailResponse.getEntityDetail();
                    CategoryMapper categoryMapper = new CategoryMapper(oMRSAPIHelper);
                    Category deletedCategory = (Category) categoryMapper.mapEntityDetailToNode(entityDetail);
                    response = createCategoryResponse(deletedCategory);
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
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
    public SubjectAreaOMASAPIResponse restoreCategory(String userId, String guid) {
        final String methodName = "restoreCategory";
        SubjectAreaOMASAPIResponse response = null;

        SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
        categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        try {
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            response = this.oMRSAPIHelper.callOMRSRestoreEntity(methodName, userId, guid);
            if (response.getResponseCategory() == ResponseCategory.OmrsEntityDetail) {
                response = getCategory(userId, guid);
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

}
