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


import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Asset;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.TermMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Referenceable.Referenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossaryCategory.CategoriesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToReferenceable.AssignedElementsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * TheSubjectAreaTermRESTServices provides the server-side implementation of the SubjectArea Open Metadata
 * Access Service (OMAS) for Terms.  This interface provides term authoring interfaces for subject area experts.
 */

public class SubjectAreaTermRESTServices extends SubjectAreaRESTServices{


    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermRESTServices.class);

    private static final String className = SubjectAreaTermRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaTermRESTServices() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Term
     *
     * The name needs to be specified - as this is the main identifier for the term. The name should be unique for canonical glossaries. This API does not police the uniqueness in this case.
     *
     * A name of an existing glossary needs to be specified in the glossaryName, as a term should not exist outside a glossary.
     *
     * The qualifiedName can be specified and will be honoured. If it is specified then the caller may wish to ensure that it is
     * unique. If this qualifiedName is not specified then one will be generated as GlossaryTerm concatinated with the the guid.
     *
     * Failure to create the Terms classifications, link to its glossary or its icon, results in the create failing and the term being deleted
     *
     * @param userId
     * @param suppliedTerm
     * @return
     *
     */
    public SubjectAreaOMASAPIResponse createTerm(String userId, Term suppliedTerm)  {
        final String methodName = "createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        GlossaryTerm glossaryTerm = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaGlossaryRESTServices glossaryRESTServices =new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        Glossary associatedGlossary =null;
        try {
            glossaryTerm = TermMapper.mapTermToOMRSBean(suppliedTerm);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        // need to check we have a name
        final String suppliedTermName = suppliedTerm.getName();
        if (response ==null && (suppliedTermName == null || suppliedTermName.equals(""))) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
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
        // TODO consider removing this restriction
        if (response ==null &&(suppliedTerm.getCategories()!=null && !suppliedTerm.getCategories().isEmpty() ) ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_CATEGORIES;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // TODO consider removing this restriction
        if (response ==null && (suppliedTerm.getProjects()!=null && !suppliedTerm.getProjects().isEmpty())  ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_PROJECTS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName, suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        // We do not allow terms to be associated with assets in this OMAS
        // there is no setAssets on Term - but in theory a Term returned from a get could be fed into a create - which should be rejected.
        if (response ==null && (suppliedTerm.getAssets()!=null && !suppliedTerm.getAssets().isEmpty() ) ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_ASSETS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName,suppliedTermName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);

        }
        String suppliedGlossaryName =suppliedTerm.getGlossaryName();
        if (response==null) {
            if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
                // error glossary is mandatory
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_GLOSSARY;
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

            } else {

                SubjectAreaOMASAPIResponse glossaryResponse = glossaryRESTServices.getGlossaryByName(userId, suppliedGlossaryName);
                if (glossaryResponse.getResponseCategory().equals(ResponseCategory.Category.Glossary)) {
                    GlossaryResponse typedGlossaryResponse = (GlossaryResponse)glossaryResponse;
                    associatedGlossary = typedGlossaryResponse.getGlossary();
                } else {
                    // glossary not found
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_NON_EXISTANT_GLOSSARY;
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
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                }
            }
        }
        GlossaryTerm newGlossaryTerm = null;
        List<Classification> classifications = new ArrayList<>();
        String termGuid=null;

        if (response==null) {
            try {
                newGlossaryTerm = service.createGlossaryTerm(userId, glossaryTerm);
                termGuid=newGlossaryTerm.getSystemAttributes().getGUID();
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
            // Knit the Term to the supplied glossary
            TermAnchor termAnchor = new TermAnchor();
            termAnchor.setEntity1Guid(SubjectAreaGlossaryRESTServices.GLOSSARY_TYPE_GUID);
            termAnchor.setEntity2Guid(termGuid);

            try {
                service.createTermAnchorRelationship(userId, termAnchor);
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
        if (response==null) {
            // We could perform other relationship creation here. I suggest not - and we encourage users to use relationship creation API
            response = getTermByGuid(userId, termGuid);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId +", response="+response );
        }
        return response;
    }

    /**
     * Get a Term
     * @param userId
     * @param guid
     * @return
     *
     */

    public SubjectAreaOMASAPIResponse getTermByGuid( String userId, String guid)  {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        GlossaryTerm glossaryTerm = null;
        try {
            glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId,guid);
            Term gotTerm = TermMapper.mapOMRSBeantoTerm(glossaryTerm);
            List<Classification> classifications = glossaryTerm.getClassifications();
            // set the GlossaryTerm classifications into the Node
            gotTerm.setClassifications(classifications);

            List<Line> termRelationships = subjectAreaOmasREST.getGlossaryTermRelationships(userId,guid);
            GlossaryTermReferences glossaryTermReferences = new GlossaryTermReferences(guid, termRelationships);
            // set icon
            Set<RelatedMediaReference> relatedMediaReferenceSet = glossaryTermReferences.getRelatedMediaReferences();
            String iconUrl = "";
            SubjectAreaUtils.getIcon(userId, relatedMediaReferenceSet);
            if (iconUrl!=null) {
                gotTerm.setIcon(iconUrl);
            }
            // set assets
            Set<Asset> assets = new HashSet();
            Set<AssignedElementsReference> assignedElementsReferences = glossaryTermReferences.getAssignedElementsReferences();
            if (assignedElementsReferences!=null && !assignedElementsReferences.isEmpty()) {
                for (AssignedElementsReference assignedElementsReference : assignedElementsReferences) {
                    Referenceable referenceable = assignedElementsReference.getReferenceable();
                    Asset asset = new Asset();
                    asset.setGuid(referenceable.getSystemAttributes().getGUID());
                    asset.setName(referenceable.getQualifiedName());
                    assets.add(asset);
                }
                gotTerm.setAssets(assets);
            }
            // set categories
            Set<String> categories = new HashSet();
            Set<CategoriesReference> categoriesReferences = glossaryTermReferences.getCategoriesReferences();
            if (categoriesReferences!=null && !categoriesReferences.isEmpty()) {
                for (CategoriesReference categoryReference : categoriesReferences) {
                    String categoryName = categoryReference.getGlossaryCategory().getDisplayName();
                    categories.add(categoryName);
                }
                gotTerm.setCategories(categories);
            }

            AnchorReference anchorReference = glossaryTermReferences.getAnchorReference();
            if (anchorReference!=null ) {
                String glossaryName = anchorReference.getGlossary().getDisplayName();
                gotTerm.setGlossaryName(glossaryName);
            }
            response = new TermResponse(gotTerm);
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


        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Response="+ response );
        }
        return response;
    }

    /**
     * Update a Term's name
     * @param userId
     * @param guid
     * @param name
     * @return
     *
     */

    public  SubjectAreaOMASAPIResponse updateTermName( String userId, String guid, String name)  {
        final String methodName = "updateTermName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getTermByGuid(userId,guid);

        if (response.getResponseCategory().equals(ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)response;
            Term term = termResponse.getTerm();

            Status status = term.getSystemAttributes().getStatus();
            response = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
            term.setName(name);
            GlossaryTerm glossaryTerm = null;
            try {
                glossaryTerm = TermMapper.mapTermToOMRSBean(term);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId, glossaryTerm);
                Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGlossaryTerm);
                response = new TermResponse(updatedTerm);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            }


        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }
    /**
     * Update a Term's description
     * @param userId
     * @param guid
     * @param description
     * @return
     *
     */
    public SubjectAreaOMASAPIResponse updateTermDescription( String userId, String guid, String description) {
        final String methodName = "updateTermDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getTermByGuid(userId,guid);

        if (response.getResponseCategory().equals(ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)response;
            Term term = termResponse.getTerm();

            Status status = term.getSystemAttributes().getStatus();
            response = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
            term.setDescription(description);
            GlossaryTerm glossaryTerm = null;
            try {
                glossaryTerm = TermMapper.mapTermToOMRSBean(term);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId, glossaryTerm);
                Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGlossaryTerm);
                response = new TermResponse(updatedTerm);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            }


        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }
    /**
     * Update a Term's qualifiedName
     * @param userId
     * @param guid
     * @param qualifiedName
     * @return
     *
     */

    public SubjectAreaOMASAPIResponse updateTermQualifiedName( String userId, String guid, String qualifiedName)  {
        final String methodName = "updateTermQualifiedName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getTermByGuid(userId,guid);

        if (response.getResponseCategory().equals(ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)response;
            Term term = termResponse.getTerm();

            Status status = term.getSystemAttributes().getStatus();
            response = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
            term.setQualifiedName(qualifiedName);
            GlossaryTerm glossaryTerm = null;
            try {
                glossaryTerm = TermMapper.mapTermToOMRSBean(term);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId, glossaryTerm);
                Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGlossaryTerm);
                response = new TermResponse(updatedTerm);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            }


        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }

    /**
     * Update a Term's summary
     * @param userId
     * @param guid
     * @param summary
     * @return response
     *
     */

    public SubjectAreaOMASAPIResponse updateTermSummary( String userId, String guid, String summary)  {
        final String methodName = "updateTermSummary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        response = getTermByGuid(userId,guid);

        if (response.getResponseCategory().equals(ResponseCategory.Term)) {
            TermResponse termResponse = (TermResponse)response;
            Term term = termResponse.getTerm();

            Status status = term.getSystemAttributes().getStatus();
            response = SubjectAreaUtils.checkStatusNotDeleted(status, SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
            term.setSummary(summary);
            GlossaryTerm glossaryTerm = null;
            try {
                glossaryTerm = TermMapper.mapTermToOMRSBean(term);
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId, glossaryTerm);
                Term updatedTerm = TermMapper.mapOMRSBeantoTerm(updatedGlossaryTerm);
                response = new TermResponse(updatedTerm);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            }


        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", response="+ response );
        }
        return response;
    }

    public SubjectAreaOMASAPIResponse deleteTerm( String userId,  String guid,  boolean isPurge)  {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid="+guid );
        }
        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaBeansToAccessOMRS service = new SubjectAreaBeansToAccessOMRS();
        service.setOMRSAPIHelper(oMRSAPIHelper);
        if (isPurge) {
            try {
                service.purgeGlossaryTermByGuid(userId, guid);
                response = new VoidResponse();
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (GUIDNotPurgedException e) {
                response = OMASExceptionToResponse.convertGUIDNotPurgedException(e);
            } catch (EntityNotDeletedException e) {
                response = OMASExceptionToResponse.convertEntityNotDeletedException(e);
            }
        } else {
            try {
                EntityDetail entityDetail  = service.deleteGlossaryTermByGuid(userId,guid);
                GlossaryTerm deletedGeneratedGlossaryTerm = GlossaryTermMapper.mapOmrsEntityDetailToGlossaryTerm(entityDetail);
                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term deletedTerm = TermMapper.mapOMRSBeantoTerm(deletedGeneratedGlossaryTerm);
                response = new TermResponse(deletedTerm);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (FunctionNotSupportedException e) {
                response = OMASExceptionToResponse.convertFunctionNotSupportedException(e);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId);
        }
        return response;
    }
}
