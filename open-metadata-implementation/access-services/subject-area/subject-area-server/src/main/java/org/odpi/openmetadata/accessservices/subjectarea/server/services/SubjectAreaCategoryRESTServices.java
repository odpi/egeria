/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SubjectArea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategoryReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryCategoryToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectAreaDefinition Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaCategoryRESTServices  extends SubjectAreaRESTServices{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaCategoryRESTServices.class);

    private static final String className = SubjectAreaCategoryRESTServices.class.getName();


    /**
     * Default constructor
     */
    public SubjectAreaCategoryRESTServices() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Category. There is specialization of a Category that can also be created using this operation.
     * To create this specialization, you should specify a nodeType other than Category in the supplied category.
     * <p>
     * Valid nodeTypes for this request are:
     * <ul>
     *     <li>SubjectAreaDefinition to create a Category that represents a subject area </li>
     *     <li>Category to create a category that is not a subject area</li>
     * </ul>
     *
     * A category is always associated with a glossary so there needs to be a named existing glossary name supplied with this request.
     *
     * A category may have a category parent, the create looks at the Category parent guid property, if this is null then
     * the category is a top level category in the glossary; if this is not null then this needs to be a valid
     * guid of a Category.
     *
     * The name of the requested category needs to be unique within its sibling categories i.e. categories with the same category parent.
     *
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param suppliedCategory category to create
     * @return response, when successful contains the created category.
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> ClassificationException              Error processing a classification</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> StatusNotSupportedException          A status value is not supported</li>
     * </ul>
     */

    public SubjectAreaOMASAPIResponse createCategory(String userid, Category suppliedCategory)  {
        final String methodName = "createCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + userid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className,methodName,userid);
            InputValidator.validateNodeType(className,methodName,suppliedCategory.getNodeType(), NodeType.Category,NodeType.SubjectAreaDefinition);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        GlossaryCategory glossaryCategory = null;
        Glossary associatedGlossary = null;
        String suppliedCategoryParentGuid = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        if (response ==null) {
            service.setOMRSAPIHelper(this.oMRSAPIHelper);
            SubjectAreaCategoryRESTServices categoryRESTServices = new SubjectAreaCategoryRESTServices();
            categoryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

            if (suppliedCategory.getParentCategory() != null) {
                //store the parent category guid
                suppliedCategoryParentGuid = suppliedCategory.getParentCategory().getGuid();
            }
            try {
                glossaryCategory = CategoryMapper.mapCategoryToOMRSBean(suppliedCategory);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        // need to check we have a name
        final String suppliedCategoryName = suppliedCategory.getName();
        if (response ==null && (suppliedCategoryName == null || suppliedCategoryName.equals(""))) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }


        // should we remove this restriction?
        if (response ==null && (suppliedCategory.getProjects()!=null && !suppliedCategory.getProjects().isEmpty())  ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_PROJECTS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName, suppliedCategoryName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }

        GlossarySummary suppliedGlossary =suppliedCategory.getGlossary();
        if (response==null) {

            SubjectAreaOMASAPIResponse glossaryResponse = RestValidator.validateGlossarySummaryDuringCreation(methodName, suppliedGlossary, glossaryRESTServices, userid);
            if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary)) {
                // store the associated glossary
                associatedGlossary = ((GlossaryResponse)glossaryResponse).getGlossary();
            } else {
                // error
                response = glossaryResponse;
            }
        }
        GlossaryCategory newGlossaryCategory = null;
        String categoryGuid=null;

        if (response==null) {
                response = checkSiblingCategoryNames(userid, methodName, suppliedCategoryName, suppliedCategoryParentGuid, SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_FAILED_CATEGORY_NAME_ALREADY_USED);
        }
        if (response==null) {
            try {
                newGlossaryCategory = service.createGlossaryCategory(userid, glossaryCategory);
                categoryGuid=newGlossaryCategory.getSystemAttributes().getGUID();
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (ClassificationException e) {
                response = OMASExceptionToResponse.convertClassificationException(e);
            } catch (StatusNotSupportedException e) {
                response = OMASExceptionToResponse.convertStatusNotsupportedException(e);
            }
        }
        if (response==null) {
            // Knit the Category to the supplied glossary
            String glossaryGuid = associatedGlossary.getSystemAttributes().getGUID();
            CategoryAnchor categoryAnchor = new CategoryAnchor();
            categoryAnchor.setEntity1Guid(glossaryGuid);
            categoryAnchor.setEntity2Guid(categoryGuid);

            try {
                service.createCategoryAnchorRelationship(userid, categoryAnchor);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (StatusNotSupportedException e) {
                response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
            }
        }
        if (response==null && suppliedCategoryParentGuid!=null) {
            // Knit the Category to the supplied parent
            CategoryHierarchyLink categoryHierarchyLink = new CategoryHierarchyLink();
            categoryHierarchyLink.setEntity1Guid(suppliedCategoryParentGuid);
            categoryHierarchyLink.setEntity2Guid(categoryGuid);

            try {
                service.createCategoryHierarchyLinkRelationship(userid, categoryHierarchyLink);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (StatusNotSupportedException e) {
                response = OMASExceptionToResponse.convertStatusNotSupportedException(e);
            }
        }
        if  (response==null && suppliedCategory.getNodeType()== NodeType.SubjectAreaDefinition) {
            try
            {
                List<Classification> classifications = new ArrayList<>();
                classifications.add(new SubjectArea());
                service.addGlossaryCategoryClassifications(userid,categoryGuid,classifications);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (ClassificationException e)
            {
                response = OMASExceptionToResponse.convertClassificationException(e);
            } catch (UnrecognizedGUIDException e)
            {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
        }

        if (response==null) {
            // We could perform other relationship creation here. I suggest not - and we encourage users to use relationship creation API
            response = getCategory(userid, categoryGuid);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userid="+userid +", response="+response );
        }
        return response;
    }

    
    /**
     * Get a Category
     * @param userid unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the category to get. This could be a guid for a SubjectAreaDefintion, which is type of category
     * @return response which when successful contains the category with the requested guid
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised
     * <li> FunctionNotSupportedException        Function not supported
     * </ul>
     */

    public SubjectAreaOMASAPIResponse getCategory( String userid, String guid)  {
        final String methodName = "getCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + userid + ",guid="+guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        GlossaryCategory glossaryCategory = null;
        try {
            InputValidator.validateUserIdNotNull(className,methodName,userid);
            InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        NodeType nodeType = NodeType.Category;
        if (response==null) {
            try {
                glossaryCategory = subjectAreaOmasREST.getGlossaryCategoryById(userid, guid);
                Category gotCategory = CategoryMapper.mapOMRSBeantoCategory(glossaryCategory);
                List<Classification> classifications = new ArrayList();
                // set the GlossaryCategory classifications into the Node
                if (glossaryCategory.getClassifications()!=null) {
                    for (Classification classification : glossaryCategory.getClassifications())
                    {
                        if (classification.getClassificationName().equals("SubjectArea"))
                        {
                            nodeType = NodeType.SubjectAreaDefinition;
                        } else
                        {
                            classifications.add(classification);
                        }
                    }
                    if (classifications.size()>0)
                    {
                        gotCategory.setClassifications(classifications);
                    } else {
                        // ensure there are no classifications
                        gotCategory.setClassifications(null);
                    }

                }
                Set<Line> categoryRelationships = subjectAreaOmasREST.getGlossaryCategoryRelationships(userid, guid);
                GlossaryCategoryReferences glossaryCategoryReferences = new GlossaryCategoryReferences(guid, categoryRelationships);
                if (response == null) {
                    // set icon
                    Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryCategoryReferences.getRelatedMediaReferences();
                    Set<IconSummary> icons = SubjectAreaUtils.getIconSummaries(userid, relatedMediaReferenceSet);
                    if (icons != null) {
                        gotCategory.setIcons(icons);
                    }
                    AnchorReference anchorReference = glossaryCategoryReferences.getAnchorReference();
                    if (anchorReference != null) {

                        //get the glossary - we need this for the name and qualified name
                        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary glossary = subjectAreaOmasREST.getGlossaryById(userid, anchorReference.getRelatedEndGuid());
                        // set glossary summary
                        GlossarySummary glossarySummary = new GlossarySummary();
                        glossarySummary.setName(glossary.getDisplayName());
                        glossarySummary.setQualifiedName(glossary.getQualifiedName());
                        glossarySummary.setGuid(anchorReference.getRelatedEndGuid());
                        glossarySummary.setRelationshipguid(anchorReference.getRelationshipGuid());
                        glossarySummary.setRelationshipType(anchorReference.getRelationship_Type());
                        gotCategory.setGlossary(glossarySummary);
                    }
                    if (nodeType == NodeType.Category)
                    {
                        response = new CategoryResponse(gotCategory);
                    } else {
                        SubjectAreaDefinition subjectAreaDefintion =new SubjectAreaDefinition(gotCategory);
                        response = new SubjectAreaDefinitionResponse(subjectAreaDefintion);
                    }
                }
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (FunctionNotSupportedException e) {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userid="+userid+", Response="+ response );
        }
        return response;
    }

    /**
     * Update a Category
     * <p>
     * Status is not updated using this call.
     *
     * @param userid           userid under which the request is performed
     * @param guid             guid of the category to update
     * @param suppliedCategory     category to be updated
     * @param isReplace flag to indicate that this update is a replace. When not set only the supplied (non null) fields are updated.
     * @return a response which when successful contains the updated category
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse updateCategory(String userid, String guid, Category suppliedCategory, boolean isReplace) {
        final String methodName = "updateCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + userid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className,methodName,userid);
            InputValidator.validateNodeType(className,methodName,suppliedCategory.getNodeType(), NodeType.Category,NodeType.SubjectAreaDefinition);
            InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response ==null) {

        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getCategory(userid, guid);
        if (response.getResponseCategory().equals(ResponseCategory.Category)) {
            org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category originalCategory = ((CategoryResponse) response).getCategory();
            if (originalCategory.getSystemAttributes() != null) {
                Status status = originalCategory.getSystemAttributes().getStatus();
                SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
                if (deleteCheckResponse != null) {
                    response = deleteCheckResponse;
                }
            }
            if (suppliedCategory.getSystemAttributes() != null) {
                Status status = suppliedCategory.getSystemAttributes().getStatus();
                SubjectAreaOMASAPIResponse deleteCheckResponse = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED);
                if (deleteCheckResponse != null) {
                    response = deleteCheckResponse;
                }
            }
            if (response != null) {
                Category updateCategory = originalCategory;
                if (isReplace) {
                    // copy over attributes
                    updateCategory.setName(suppliedCategory.getName());
                    updateCategory.setQualifiedName(suppliedCategory.getQualifiedName());
                    updateCategory.setDescription(suppliedCategory.getDescription());
                    updateCategory.setAdditionalProperties(suppliedCategory.getAdditionalProperties());
                    //TODO handle governance classifications and other classifications
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
                    //TODO handle governance classifications and other classifications
                }
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory generatedCategory = null;
                try {
                    generatedCategory = CategoryMapper.mapCategoryToOMRSBean(updateCategory);
                    org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory updatedGeneratedCategory = null;
                    try {
                        updatedGeneratedCategory = service.updateGlossaryCategory(userid, generatedCategory);
                    } catch (MetadataServerUncontactableException e) {
                        response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                    } catch (UserNotAuthorizedException e) {
                        response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                    } catch (UnrecognizedGUIDException e) {
                        response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                    }
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category updatedCategory = CategoryMapper.mapOMRSBeantoCategory(updatedGeneratedCategory);
                    if (suppliedCategory.getNodeType() == NodeType.SubjectAreaDefinition) {
                        response = new SubjectAreaDefinitionResponse(new SubjectAreaDefinition(updatedCategory));
                    } else
                    {
                        response = new CategoryResponse(updatedCategory);
                    }
                } catch (InvalidParameterException e) {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                }
            }
        }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userid=" + userid + ",response=" + response);
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
     * @param userid  userid under which the request is performed
     * @param guid    guid of the category to be deleted.
     * @param isPurge true indicates a hard delete, false is a soft delete.
     * @return a void response
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised</li>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> FunctionNotSupportedException        Function not supported this indicates that a soft delete was issued but the repository does not support it.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service. There is a problem retrieving properties from the metadata repository.</li>
     * <li> EntityNotDeletedException            a soft delete was issued but the category was not deleted.</li>
     * <li> GUIDNotPurgedException               a hard delete was issued but the category was not purged</li>
     * </ul>
     */
    public SubjectAreaOMASAPIResponse deleteCategory(String userid, String guid, Boolean isPurge) {
        final String methodName = "deleteCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid=" + userid + ", guid=" + guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            InputValidator.validateUserIdNotNull(className,methodName,userid);
            InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response ==null) {
            SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
            service.setOMRSAPIHelper(oMRSAPIHelper);
            if (isPurge) {
                try {
                    service.purgeGlossaryCategoryByGuid(userid, guid);
                    response = new VoidResponse();
                } catch (MetadataServerUncontactableException e) {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e) {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                } catch (InvalidParameterException e) {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } catch (UnrecognizedGUIDException e) {
                    response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                } catch (EntityNotDeletedException e) {
                    response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
                } catch (GUIDNotPurgedException e) {
                    response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
                }

            } else {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory deletedGeneratedCategory = null;
                try {
                    // TODO check whether we have any terms
                    EntityDetail entityDetail = service.deleteGlossaryCategoryByGuid(userid, guid);
                    deletedGeneratedCategory = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategoryMapper.mapOmrsEntityDetailToGlossaryCategory(entityDetail);
                    NodeType nodeType= NodeType.Category;

                    if (deletedGeneratedCategory.getClassifications() !=null)
                    {
                        List<Classification> classifications = new ArrayList();
                        for (Classification classification : deletedGeneratedCategory.getClassifications())
                        {
                            if (classification.getClassificationName().equals("SubjectArea"))
                            {
                                nodeType = NodeType.SubjectAreaDefinition;
                            } else
                            {
                                classifications.add(classification);
                            }
                        }
                        if (classifications.size() > 0)
                        {
                            deletedGeneratedCategory.setClassifications(classifications);
                        } else
                        {
                            // ensure there are no classifications
                            deletedGeneratedCategory.setClassifications(null);
                        }
                    }
                    org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category deletedCategory = CategoryMapper.mapOMRSBeantoCategory(deletedGeneratedCategory);
                    if (nodeType == NodeType.SubjectAreaDefinition) {
                        response = new SubjectAreaDefinitionResponse(new SubjectAreaDefinition(deletedCategory));
                    } else{
                        response = new CategoryResponse(deletedCategory);
                    }
                } catch (MetadataServerUncontactableException e) {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e) {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                } catch (FunctionNotSupportedException e) {
                    response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
                } catch (InvalidParameterException e) {
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } catch (UnrecognizedGUIDException e) {
                    response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userid=" + userid);
        }
        return response;
    }

    /**
     * Check that the requested name is not already used in a sibling category. Category children name should be unique under a parent.
     * @param userid user idendifier
     * @param methodName method name
     * @param suppliedCategoryName non-null category name
     * @param suppliedCategoryParentGuid parent category guid. Do nothing if null.
     * @param errorCode error code to use if there is an existing sibling categorey with the same name.
     * @return null if successful , otehrwise a response containing the error
     */
    public SubjectAreaOMASAPIResponse checkSiblingCategoryNames(String userid, String methodName, String suppliedCategoryName, String suppliedCategoryParentGuid, SubjectAreaErrorCode errorCode) {
        SubjectAreaOMASAPIResponse response = null;
        // Check that the requested name is not already used in a sibling category. Category children name should be unique under a parent.
        List<Relationship> omrsRelationships = null;
        if (suppliedCategoryParentGuid !=null) {
            try {
                omrsRelationships = oMRSAPIHelper.callGetRelationshipsForEntity(userid, suppliedCategoryParentGuid, SubjectAreaGlossaryRESTServices.CATEGORY_HIERARCHY_LINK_GUID, 0, null, null, null, null, 0);

                if (omrsRelationships != null) {
                    for (Relationship omrsRelationship : omrsRelationships) {

                        if (omrsRelationship.getType().getTypeDefName().equals("CategoryHierarchyLink")) {
                            CategoryHierarchyLink link = new CategoryHierarchyLink(omrsRelationship);
                            if (link.getEntity1Guid().equals(suppliedCategoryParentGuid)) {
                                // this means that omrsRelationships are such that the supplied Parentid is entity 1's guid.

                                EntityDetail siblingEntityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userid, link.getEntity2Guid());
                                InstanceProperties siblingProperties = siblingEntityDetail.getProperties();
                                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) siblingProperties.getPropertyValue("displayName");

                                if (primitivePropertyValue != null) {
                                    String siblingCategoryName = (String) primitivePropertyValue.getPrimitiveValue();
                                    if (siblingCategoryName.equals(suppliedCategoryName)) {
                                        // Error category name already in use by a sibling.
                                        String errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(className,
                                                methodName, suppliedCategoryName);
                                        log.error(errorMessage);
                                        InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                className,
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
                                        response = new InvalidParameterExceptionResponse(e);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (FunctionNotSupportedException e) {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
        }

        return response;
    }
}
