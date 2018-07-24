/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategoryReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryCategoryToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
//import org.odpi.openmetadata.accessservices.subjectarea.server.properties.category.Category;
//import org.odpi.openmetadata.accessservices.subjectarea.server.properties.category.CategoryMapper;
//import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
//import org.odpi.openmetadata.accessservices.subjectarea.server.responses.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
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
     * Create a Category
     *
     * A category is always associated with a glossary so there needs to be a named existing glossary name supplied with this request.
     *
     * A category may have a category parent, the create looks at the Category parent guid property, if this is null then
     * the category is a top level category in the glossary; if this is not null then this needs to be a valid
     * guid of a Category.
     *
     * The name of the requested category needs to be unique within its sibling categories i.e. categories with the same category parent.
     *
     * @param userId
     * @param suppliedCategory
     * @return created Category
     * @
     */

    public SubjectAreaOMASAPIResponse createCategory(String userId, Category suppliedCategory)  {
        final String methodName = "createCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaGlossaryRESTServices glossaryRESTServices =new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category newCategory =null;
        Glossary associatedGlossary =null;

        GlossaryCategory glossaryCategory = null;
        final String suppliedGlossaryName = suppliedCategory.getGlossaryName();
        final String suppliedCategoryParentGuid = suppliedCategory.getParentCategoryGuid();
        final String suppliedCategoryName = suppliedCategory.getName();

        try {
            glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(suppliedCategory);
        } catch (InvalidParameterException e) {
           response= OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response ==null) {
            // need to check we have a name

            if (suppliedCategoryName == null || suppliedCategoryName.equals("")) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME;
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
                response = new InvalidParameterExceptionResponse(e);
            }
        }

        if (response ==null) {

            if (suppliedGlossaryName == null) {
                // error glossary is mandatory
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_GLOSSARY;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                InvalidParameterException e =  new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = new InvalidParameterExceptionResponse(e);
            }
        }
        if (response ==null) {
            // check that the requested glossary exists
            response = glossaryRESTServices.getGlossaryByName(userId, suppliedGlossaryName);
            if (associatedGlossary == null) {
                // glossary not found
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName, suppliedGlossaryName);
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
        // null category parent guid means that a top level category is being created.
        // if not null, check that the requested parent Category exists using the guid (the parent category name should not be used as it is not unique).

        if (suppliedCategoryParentGuid != null) {
            GlossaryCategory parent = null;
            try {
                parent = service.getGlossaryCategoryById(userId, suppliedCategoryParentGuid);
            } catch (PropertyServerException e) {
               response=OMASExceptionToResponse.convertPropertyServerException(e);
            } catch (UserNotAuthorizedException e) {
               response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
               response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
            if (parent == null) {
                // could not find the category
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_PARENT;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName, suppliedCategoryParentGuid);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = new InvalidParameterExceptionResponse(e);
            } else {
                // Do not allow a deleted category to be specified as the guid here.
                if (parent.getSystemAttributes().getStatus() == Status.DELETED) {
                    // do not knit to a deleted category.
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_PARENT;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName, suppliedCategoryParentGuid);
                    log.error(errorMessage);
                    InvalidParameterException e =  new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = new InvalidParameterExceptionResponse(e);
                }
            }
        }


        checkSiblingCategoryNames(userId, methodName, suppliedCategoryName, suppliedCategoryParentGuid, SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_FAILED_CATEGORY_NAME_ALREADY_USED);

        GlossaryCategory newGlossaryCategory = null;
        try {
            newGlossaryCategory = newGlossaryCategory = service.createGlossaryCategory(userId, glossaryCategory);
        } catch (PropertyServerException e) {
           response=OMASExceptionToResponse.convertPropertyServerException(e);
        } catch (UserNotAuthorizedException e) {
           response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
           response=OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (ClassificationException e) {
           response=OMASExceptionToResponse.convertClassificationException(e);
        } catch (StatusNotSupportedException e) {
           response=OMASExceptionToResponse.convertStatusNotsupportedException(e);
        }
        String categoryGuid = newGlossaryCategory.getSystemAttributes().getGUID();
        // Knit the Category to the supplied glossary
        CategoryAnchor categoryAnchor = new CategoryAnchor();
        categoryAnchor.setEntity1Guid(SubjectAreaGlossaryRESTServices.GLOSSARY_TYPE_GUID);
        categoryAnchor.setEntity2Guid(categoryGuid);

        try {
            service.createCategoryAnchorRelationship(userId, categoryAnchor);
        } catch (PropertyServerException e) {
           response=OMASExceptionToResponse.convertPropertyServerException(e);
        } catch (UserNotAuthorizedException e) {
           response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (UnrecognizedGUIDException e) {
           response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (InvalidParameterException e) {
           response=OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        // We could allow other relationship creation here. I suggest not - encourage user to use relationship creation API
        if (response == null) {
            response = getCategory(userId, categoryGuid);
        }


        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Check that the requested name is not already used in a sibling category. Category children name should be unique under a parent.
     * @param userId
     * @param methodName
     * @param suppliedCategoryName
     * @param suppliedCategoryParentGuid
     * @param errorCode
     * @throws UnrecognizedGUIDException
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws PropertyServerException
     */
    public SubjectAreaOMASAPIResponse checkSiblingCategoryNames(String userId, String methodName, String suppliedCategoryName, String suppliedCategoryParentGuid, SubjectAreaErrorCode errorCode) {
        SubjectAreaOMASAPIResponse response = null;
        // look for all status that are not deleted or unknown.
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        statusList.add(InstanceStatus.DRAFT);
        statusList.add(InstanceStatus.PREPARED);
        statusList.add(InstanceStatus.PROPOSED);

        // Check that the requested name is not already used in a sibling category. Category children name should be unique under a parent.
        List<Relationship> omrsRelationships = null;
        try {
            omrsRelationships = oMRSAPIHelper.callGetRelationshipsForEntity(userId,suppliedCategoryParentGuid, SubjectAreaGlossaryRESTServices.CATEGORY_HIERARCHY_LINK_GUID,0,statusList,null,null,null,0);
        } catch (PropertyServerException e) {
           response=OMASExceptionToResponse.convertPropertyServerException(e);
        } catch (UserNotAuthorizedException e) {
           response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
           response=OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (FunctionNotSupportedException e) {
           response=OMASExceptionToResponse.convertFunctionNotSupportedException(e);
        } catch (UnrecognizedGUIDException e) {
           response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        for (Relationship omrsRelationship:omrsRelationships) {
            CategoryHierarchyLink link = new CategoryHierarchyLink(omrsRelationship);
            if (link.getEntity1Guid().equals(suppliedCategoryParentGuid)) {
                // this means that omrsRelationships are such that the supplied Parentid is entity 1's guid.

                String siblingCategoryName = link.getEntity2Name();
                // TODO what is the label in the omrsRelationship? Do we need it?

                if (siblingCategoryName.equals(suppliedCategoryName))  {
                    // Error category name already in use by a sibling.
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName,suppliedCategoryName);
                    log.error(errorMessage);
                    InvalidParameterException e= new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = new InvalidParameterExceptionResponse(e);
                }
            }
        }
        return response;
    }

    /**
     * Get a Category
     * @param userId
     * @param guid
     * @return
     * @
     */
    public SubjectAreaOMASAPIResponse getCategory(String userId, String guid)  {
        final String methodName = "getCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        GlossaryCategory glossaryCategory = null;
        Category gotCategory =null;
        GlossaryCategoryReferences glossaryCategoryReferences = null;
        try {
            glossaryCategory = subjectAreaOmasREST.getGlossaryCategoryById(userId,guid);
            gotCategory = CategoryMapper.mapOMRSBeantoCategory(glossaryCategory);
            response = new CategoryResponse(gotCategory);
        } catch (PropertyServerException e) {
           response=OMASExceptionToResponse.convertPropertyServerException(e);
        } catch (UserNotAuthorizedException e) {
           response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
           response=OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e) {
           response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }

        List<Classification> classifications = glossaryCategory.getClassifications();
        // set the GlossaryCategory classifications into the Node
        gotCategory.setClassifications(classifications);
        if (response.getResponseCategory().equals(ResponseCategory.Category)) {
            List<Line> termRelationships = null;
            try {
                termRelationships = subjectAreaOmasREST.getGlossaryCategoryRelationships(userId, guid);
            } catch (PropertyServerException e) {
               response=OMASExceptionToResponse.convertPropertyServerException(e);
            } catch (UserNotAuthorizedException e) {
               response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (FunctionNotSupportedException e) {
               response=OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            } catch (UnrecognizedGUIDException e) {
               response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }

            try {
                glossaryCategoryReferences = new GlossaryCategoryReferences(guid, termRelationships);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (response.getResponseCategory().equals(ResponseCategory.Category)) {
            // set icon
            Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryCategoryReferences.getRelatedMediaReferences();

            String iconUrl = "";
//                SubjectAreaUtils.getIcon(userId, subjectAreaOmasREST, relatedMediaReferenceSet);
            if (iconUrl != null) {
                gotCategory.setIcon(iconUrl);
            }

            AnchorReference anchorReference = glossaryCategoryReferences.getAnchorReference();
            if (anchorReference != null) {
                String glossaryName = anchorReference.getGlossary().getDisplayName();
                gotCategory.setGlossaryName(glossaryName);
            }
        }
        gotCategory = CategoryMapper.mapOMRSBeantoCategory(glossaryCategory);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Category="+ gotCategory );
        }
        return response;
    }
    /**
     * Update a Category's name
     * @param userId
     * @param guid
     * @param suppliedCategoryName
     * @return updated Category
     * @
     */
    public SubjectAreaOMASAPIResponse updateCategoryName(String userId,String guid,String suppliedCategoryName)  {
        final String methodName = "updateCategoryName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category category = null;

        SubjectAreaOMASAPIResponse categoryResponse = getCategory(userId,guid);
        if (categoryResponse.equals(ResponseCategory.Category)) {
            category = ((CategoryResponse)response).getCategory();
        } else {
            response = categoryResponse;
        }
        if (response ==null) {
            Status status = category.getSystemAttributes().getStatus();
            SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);

            String suppliedCategoryParentGuid = category.getParentCategoryGuid();
            response = checkSiblingCategoryNames(userId, methodName, suppliedCategoryName, suppliedCategoryParentGuid, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED);
            category.setName(suppliedCategoryName);
        }

        if (response ==null) {
            try {
                GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
                GlossaryCategory updatedGlossaryCategory = service.updateGlossaryCategory(userId, glossaryCategory);
                Category updatedCategory = CategoryMapper.mapOMRSBeantoCategory(updatedGlossaryCategory);
                response = new CategoryResponse(updatedCategory);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
               response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
               response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (PropertyServerException e) {
               response=OMASExceptionToResponse.convertPropertyServerException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }

    /**
     * Update a Category's description
     * @param userId
     * @param guid
     * @param description
     * @return
     * @
     */
    public SubjectAreaOMASAPIResponse updateCategoryDescription(String userId,String guid,String description)  {
        final String methodName = "updateCategoryDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category category = null;

        SubjectAreaOMASAPIResponse categoryResponse = getCategory(userId,guid);
        if (categoryResponse.equals(ResponseCategory.Category)) {
            category = ((CategoryResponse)response).getCategory();
        } else {
            response = categoryResponse;
        }
        if (response ==null) {
            Status status = category.getSystemAttributes().getStatus();
            SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);

            String suppliedCategoryParentGuid = category.getParentCategoryGuid();
            response = checkSiblingCategoryNames(userId, methodName, category.getName(), suppliedCategoryParentGuid, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED);
            category.setDescription(description);
        }

        if (response ==null) {
            try {
                GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
                GlossaryCategory updatedGlossaryCategory = service.updateGlossaryCategory(userId, glossaryCategory);
                Category updatedCategory = CategoryMapper.mapOMRSBeantoCategory(updatedGlossaryCategory);
                response = new CategoryResponse(updatedCategory);
            } catch (InvalidParameterException e) {
                response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (PropertyServerException e) {
                response=OMASExceptionToResponse.convertPropertyServerException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }
    /**
     * Update a Category's qualifiedName
     * @param userId
     * @param guid
     * @param qualifiedName
     * @return
     * @
     */
    public SubjectAreaOMASAPIResponse updateCategoryQualifiedName(String userId,String guid,String qualifiedName)  {
        final String methodName = "updateCategoryQualifiedName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category category = null;

        SubjectAreaOMASAPIResponse categoryResponse = getCategory(userId,guid);
        if (categoryResponse.equals(ResponseCategory.Category)) {
            category = ((CategoryResponse)response).getCategory();
        } else {
            response = categoryResponse;
        }
        if (response ==null) {
            Status status = category.getSystemAttributes().getStatus();
            SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);

            String suppliedCategoryParentGuid = category.getParentCategoryGuid();
            response = checkSiblingCategoryNames(userId, methodName, category.getName(), suppliedCategoryParentGuid, SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED);
            category.setQualifiedName(qualifiedName);
        }

        if (response ==null) {
            try {
                GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
                GlossaryCategory updatedGlossaryCategory = service.updateGlossaryCategory(userId, glossaryCategory);
                Category updatedCategory = CategoryMapper.mapOMRSBeantoCategory(updatedGlossaryCategory);
                response = new CategoryResponse(updatedCategory);
            } catch (InvalidParameterException e) {
                response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (PropertyServerException e) {
                response=OMASExceptionToResponse.convertPropertyServerException(e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse deleteCategory(String userId, String guid, boolean isPurge)  {
        final String methodName = "deleteCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid="+guid );
        }
        SubjectAreaOMASAPIResponse response = null;
        // Not worth rejecting deletes on a deleted Category
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(oMRSAPIHelper);
        if (isPurge) {
            try {
                service.purgeGlossaryCategoryByGuid(userId, guid);
                response = new VoidResponse();
            } catch (PropertyServerException e) {
               response=OMASExceptionToResponse.convertPropertyServerException(e);
            } catch (UserNotAuthorizedException e) {
               response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
               response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (EntityNotDeletedException e) {
               response=OMASExceptionToResponse.convertEntityNotDeletedException(e);
            } catch (GUIDNotPurgedException e) {
               response=OMASExceptionToResponse.convertGUIDNotPurgedException(e);
            }
        } else {
            try {
                service.deleteGlossaryCategoryByGuid(userId, guid);
                response = new VoidResponse();
            } catch (PropertyServerException e) {
               response=OMASExceptionToResponse.convertPropertyServerException(e);
            } catch (UserNotAuthorizedException e) {
               response=OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (FunctionNotSupportedException e) {
               response=OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            } catch (InvalidParameterException e) {
               response=OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
               response=OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId);
        }
        return response;
    }
}
