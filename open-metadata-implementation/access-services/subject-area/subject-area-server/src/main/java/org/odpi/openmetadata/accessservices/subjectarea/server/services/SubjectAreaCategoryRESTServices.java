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


import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.common.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.common.Status;
import org.odpi.openmetadata.accessservices.subjectarea.common.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategoryReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryCategoryToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryHierarchyLink.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaOmasREST;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.category.CategoryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.NodeUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("access-services/subject-area")
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
     * A category is always associated with a glossary - so there needs to be a named existing glossary name supplied with this request.
     *
     * A category may have a category parent, the create looks at the Category parent guid property, if this is null then
     * the category is a top level category in the glossary; if this is not null then this needs to be a valid
     * guid of a Glossary Category.
     *
     * The name of the requested category needs to be unique within its sibling categories i.e. categories with the same category parent.
     *
     * @param userId
     * @param suppliedCategory
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "users/{userId}/categorys")
    public Category createCategory(@PathVariable String userId, Category suppliedCategory) throws Exception {
        final String methodName = "createCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaGlossaryRESTServices glossaryRESTServices =new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category newCategory =null;
        org.odpi.openmetadata.accessservices.subjectarea.server.properties.glossary.Glossary associatedGlossary =null;

        GlossaryCategory glossaryCategory = null;
        try {
            glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(suppliedCategory);

            // need to check we have a name
            final String suppliedCategoryName = suppliedCategory.getName();
            if (suppliedCategoryName == null || suppliedCategoryName.equals("")) {
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_NAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

            String suppliedGlossaryName =suppliedCategory.getGlossaryName();
            String suppliedCategoryParentGuid = suppliedCategory.getParentCategoryGuid();

            if (suppliedGlossaryName==null) {
                // error glossary is mandatory
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITHOUT_GLOSSARY;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            }

            // check that the requested glossary exists
            associatedGlossary = glossaryRESTServices.getGlossaryByName(userId,suppliedGlossaryName);
            if (associatedGlossary ==null) {
                // glossary not found
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName,suppliedGlossaryName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
            // null category parent guid means that a top level category is being created.
            // if not null, check that the requested parent Category exists using the guid (the parent category name should not be used as it is not unique).

            if (suppliedCategoryParentGuid!=null) {
                GlossaryCategory parent = service.getGlossaryCategoryById(userId, suppliedCategoryParentGuid);
                if (parent == null) {
                    // could not find the category
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_PARENT;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName, suppliedCategoryParentGuid);
                    log.error(errorMessage);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                } else {
                    // Do not allow a deleted category to be specified as the guid here.
                    if (parent.getSystemAttributes().getStatus() == Status.DELETED) {
                        // do not knit to a deleted category.
                        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_PARENT;
                        String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(className,
                                methodName, suppliedCategoryParentGuid);
                        log.error(errorMessage);
                        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                className,
                                methodName,
                                errorMessage,
                                errorCode.getSystemAction(),
                                errorCode.getUserAction());
                    }
                }
            }



            checkSiblingCategoryNames(userId, methodName, suppliedCategoryName, suppliedCategoryParentGuid,SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_FAILED_CATEGORY_NAME_ALREADY_USED);

            GlossaryCategory newGlossaryCategory= newGlossaryCategory = service.createGlossaryCategory(userId,glossaryCategory);

            // set the classifications
            String categoryGuid = newGlossaryCategory.getSystemAttributes().getGUID();

            List<Classification> classifications = new ArrayList<>();

            // governance levels
            List<GovernanceLevel> governanceLevels = suppliedCategory.getGovernanceLevels();
            if (governanceLevels!=null) {
                NodeUtils.addGovernanceLevelsToClassifications(classifications, governanceLevels);
            }
            // retention
            Retention retention = suppliedCategory.getRetention();
            if (retention!=null) {
                classifications.add(retention);
            }
            try {
                service.addGlossaryCategoryClassifications(userId, categoryGuid, classifications);
            } catch (Exception e) {
                // if it failed to create the classifications then we should delete the created category
                service.deleteGlossaryTermByGuid(userId,categoryGuid);
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_FAILED_ADDING_CLASSIFICATIONS;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(className,
                        methodName,suppliedCategoryName);
                log.error(errorMessage);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            }
            // Knit the Category to the supplied glossary
            CategoryAnchor categoryAnchor = new CategoryAnchor();
            categoryAnchor.setEntity1Guid(SubjectAreaGlossaryRESTServices.GLOSSARY_TYPE_GUID);
            categoryAnchor.setEntity2Guid(categoryGuid);

            try {
                service.createCategoryAnchorRelationship(userId, categoryAnchor);
            } catch (PropertyServerException e) {
                e.printStackTrace();
            } catch (UserNotAuthorizedException e) {
                e.printStackTrace();
            } catch (TypeException e) {
                e.printStackTrace();
            } catch (UnrecognizedGUIDException e) {
                e.printStackTrace();
            }
//        } catch (Exception e) {
//            // if it failed to create the classifications then we should delete the created category
//            service.deleteGlossaryTermByGuid(userId,categoryGuid);
//            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_FAILED_KNITTING_TO_GLOSSARY;
//            String errorMessage = errorCode.getErrorMessageId()
//                    + errorCode.getFormattedErrorMessage(className,
//                    methodName,suppliedCategoryName);
//            log.error(errorMessage);
//            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
//                    className,
//                    methodName,
//                    errorMessage,
//                    errorCode.getSystemAction(),
//                    errorCode.getUserAction());
//        }
            // We could allow other relationship creation here. I suggest not - encourage user to use relationship creation API

            newCategory = getCategory(userId,categoryGuid);

        } catch (InvalidParameterException eee) {

        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", created Category="+ newCategory );
        }
        return newCategory;
    }

    public void checkSiblingCategoryNames(@PathVariable String userId, String methodName, String suppliedCategoryName, String suppliedCategoryParentGuid, SubjectAreaErrorCode errorCode) throws UnrecognizedGUIDException, UserNotAuthorizedException, FunctionNotSupportedException, TypeException, InvalidParameterException, PagingException, PropertyServerException {

        // look for all status that are not deleted or unknown.
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        statusList.add(InstanceStatus.DRAFT);
        statusList.add(InstanceStatus.PREPARED);
        statusList.add(InstanceStatus.PROPOSED);

        // Check that the requested name is not already used in a sibling category. Category children name should be unique under a parent.
        List<Relationship> omrsRelationships = oMRSAPIHelper.callGetRelationshipsForEntity(userId,suppliedCategoryParentGuid, SubjectAreaGlossaryRESTServices.CATEGORY_HIERARCHY_LINK_GUID,0,statusList,null,null,null,0);

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
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }
            }
        }
    }

    /**
     * Get a Category
     * @param userId
     * @param guid
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/categories/{guid}")
    public Category getCategory(@PathVariable String userId, String guid) throws Exception {
        final String methodName = "getCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOmasREST subjectAreaOmasREST = new SubjectAreaOmasREST() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        GlossaryCategory glossaryCategory = subjectAreaOmasREST.getGlossaryCategoryById(userId,guid);
        Category gotCategory = CategoryMapper.mapGlossaryCategorytoCategory(glossaryCategory);
        List<Classification> classifications = glossaryCategory.getClassifications();
        // set the GlossaryCategory classifications into the Node
        gotCategory.setClassifications(classifications);

        List<Line> termRelationships = subjectAreaOmasREST.getGlossaryCategoryRelationships(userId,guid);
        GlossaryCategoryReferences glossaryCategoryReferences = new GlossaryCategoryReferences(guid, termRelationships);
        // set icon
        Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryCategoryReferences.getRelatedMediaReferences();

        String iconUrl ="";
//                SubjectAreaUtils.getIcon(userId, subjectAreaOmasREST, relatedMediaReferenceSet);
        if (iconUrl!=null) {
            gotCategory.setIcon(iconUrl);
        }

        AnchorReference anchorReference = glossaryCategoryReferences.getAnchorReference();
        if (anchorReference!=null ) {
            String glossaryName = anchorReference.getGlossary().getDisplayName();
            gotCategory.setGlossaryName(glossaryName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Category="+ gotCategory );
        }
        return gotCategory;
    }
    /**
     * Update a Category's name
     * @param userId
     * @param guid
     * @param suppliedCategoryName
     * @return updated Category
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/categories/{guid}/name/{name}")
    public Category updateCategoryName(@PathVariable String userId,@PathVariable String guid,@PathVariable String suppliedCategoryName) throws Exception {
        final String methodName = "updateCategoryName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);

        Category category = getCategory(userId,guid);

        Status status = category.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);

        String suppliedCategoryParentGuid = category.getParentCategoryGuid();
        checkSiblingCategoryNames(userId, methodName, suppliedCategoryName, suppliedCategoryParentGuid,SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED);
        category.setName(suppliedCategoryName);
        GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
        GlossaryCategory updatedGlossaryCategory=service.updateGlossaryCategory(userId,guid,glossaryCategory);
        Category updatedCategory = CategoryMapper.mapGlossaryCategorytoCategory(updatedGlossaryCategory);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Node="+ updatedCategory );
        }
        return updatedCategory;
    }

    /**
     * Update a Category's description
     * @param userId
     * @param guid
     * @param description
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/categories/{guid}/description/{description}")
    public Category updateCategoryDescription(@PathVariable String userId,@PathVariable String guid,@PathVariable String description) throws Exception {
        final String methodName = "updateCategoryDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category category = getCategory(userId,guid);
        Status status = category.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);
        category.setDescription(description);
        GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
        GlossaryCategory updatedGlossaryCategory = service.updateGlossaryCategory(userId,guid,glossaryCategory);

        Category updatedCategory = CategoryMapper.mapGlossaryCategorytoCategory(updatedGlossaryCategory);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Node="+ updatedCategory );
        }
        return updatedCategory;
    }
    /**
     * Update a Category's qualifiedName
     * @param userId
     * @param guid
     * @param qualifiedName
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/categories/{guid}/qualified-name/{qualifiedName}")
    public Category updateCategoryQualifiedName(@PathVariable String userId,@PathVariable String guid,@PathVariable String qualifiedName) throws Exception {
        final String methodName = "updateCategoryQualifiedName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Category category = getCategory(userId,guid);
        Status status = category.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_ON_DELETED_CATEGORY);
        category.setQualifiedName(qualifiedName);
        GlossaryCategory glossaryCategory = CategoryMapper.mapCategoryToGlossaryCategory(category);
        GlossaryCategory updatedGlossaryCategory = service.updateGlossaryCategory(userId,guid,glossaryCategory);

        Category updatedCategory = CategoryMapper.mapGlossaryCategorytoCategory(updatedGlossaryCategory);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Category="+ updatedCategory );
        }
        return updatedCategory;
    }
    @RequestMapping(method = RequestMethod.DELETE, path = "users/{userId}/categories/{guid}")
    public void deleteCategory(@PathVariable String userId, @PathVariable String guid, @RequestParam boolean isPurge) throws Exception {
        final String methodName = "deleteCategory";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid="+guid );
        }
        // Not worth rejecting deletes on a deleted Category
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(oMRSAPIHelper);
        if (isPurge) {
            service.purgeGlossaryCategoryByGuid(userId, guid);
        } else {
            service.deleteGlossaryCategoryByGuid(userId, guid);
            //TODO catch expect saying soft delete not supported
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId);
        }
    }
}
