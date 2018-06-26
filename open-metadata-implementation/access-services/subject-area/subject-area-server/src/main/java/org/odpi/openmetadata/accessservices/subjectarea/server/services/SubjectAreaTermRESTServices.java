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
import org.odpi.openmetadata.accessservices.subjectarea.common.Status;
import org.odpi.openmetadata.accessservices.subjectarea.common.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.common.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ObjectIdentifier.ObjectIdentifier;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineAttribute.SpineAttribute;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.SpineObject.SpineObject;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTermReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Referenceable.Referenceable;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossary.AnchorReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToGlossaryCategory.CategoriesReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.GlossaryTermToReferenceable.AssignedElementsReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaOmasREST;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.NodeUtils;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.term.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("access-services/subject-area")
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
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "users/{userId}/terms")
    public Term createTerm(@PathVariable String userId, Term suppliedTerm) throws Exception {
        final String methodName = "createTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        GlossaryTerm glossaryTerm = null;
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaGlossaryRESTServices glossaryRESTServices =new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);

        org.odpi.openmetadata.accessservices.subjectarea.server.properties.glossary.Glossary associatedGlossary =null;
        glossaryTerm = TermMapper.mapTermToGlossaryTerm(suppliedTerm);
        // need to check we have a name
        final String suppliedTermName = suppliedTerm.getName();
        if (suppliedTermName == null || suppliedTermName.equals("")) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
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
        // TODO consider removing this restriction
        if (suppliedTerm.getCategories()!=null && !suppliedTerm.getCategories().isEmpty()  ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_CATEGORIES;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName, suppliedTermName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        // TODO consider removing this restriction
        if (suppliedTerm.getProjects()!=null && !suppliedTerm.getProjects().isEmpty()  ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_PROJECTS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName, suppliedTermName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        // We do not allow terms to be associated with assets in this OMAS
        // there is no setAssets on Term - but in theory a Term returned from a get could be fed into a create - which should be rejected.
        if (suppliedTerm.getAssets()!=null && !suppliedTerm.getAssets().isEmpty()  ) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_ASSETS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName,suppliedTermName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        String suppliedGlossaryName =suppliedTerm.getGlossaryName();

        if (suppliedGlossaryName==null || suppliedGlossaryName.equals("")) {
            // error glossary is mandatory
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_GLOSSARY;
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

        } else {
          associatedGlossary = glossaryRESTServices.getGlossaryByName(userId,suppliedGlossaryName);
          if (associatedGlossary ==null) {
              // glossary not found
              SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITH_NON_EXISTANT_GLOSSARY;
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
        }


        GlossaryTerm newGlossaryTerm = null;
        newGlossaryTerm = service.createGlossaryTerm(userId,glossaryTerm);
        // set the classifications
        String termGuid = newGlossaryTerm.getSystemAttributes().getGUID();

        List<Classification> classifications = new ArrayList<>();
        if (suppliedTerm.isSpineObject()) {
            classifications.add(new SpineObject());
        }
        if (suppliedTerm.isSpineAttribute()) {
            classifications.add(new SpineAttribute());
        }
        if (suppliedTerm.isObjectIdentifier()) {
            classifications.add(new ObjectIdentifier());
        }
        // governance levels
        List<GovernanceLevel> governanceLevels = suppliedTerm.getGovernanceLevels();
        if (governanceLevels!=null) {
            NodeUtils.addGovernanceLevelsToClassifications(classifications, governanceLevels);
        }
        // retention
        Retention retention = suppliedTerm.getRetention();
        if (retention!=null) {
            classifications.add(retention);
        }
        try {
            service.addGlossaryTermClassifications(userId,termGuid,classifications);
        } catch(Exception e) {
            // if it failed to create the classifications then we should delete the created term
            service.deleteGlossaryTermByGuid(userId,termGuid);
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_FAILED_ADDING_CLASSIFICATIONS;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName,suppliedTermName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        // Knit the Term to the supplied glossary
        TermAnchor termAnchor = new TermAnchor();
        termAnchor.setEntity1Guid(SubjectAreaGlossaryRESTServices.GLOSSARY_TYPE_GUID);
        termAnchor.setEntity2Guid(termGuid);
        try {
            service.createTermAnchorRelationship(userId, termAnchor);
        } catch(Exception e) {
            // if it failed to create the relationship then we should delete the created term
            service.deleteGlossaryTermByGuid(userId,termGuid);
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_FAILED_KNITTING_TO_GLOSSARY;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName,suppliedTermName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        // We could perform other relationship creation here. I suggest not - and we encourage users to use relationship creation API

        Term newTerm = getTermByGuid(userId,termGuid);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", created Node="+ newTerm );
        }
        return newTerm;
    }

    /**
     * Get a Term
     * @param userId
     * @param guid
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/terms/{guid}")
    public Term getTermByGuid(@PathVariable String userId, String guid) throws Exception {
        final String methodName = "getTermByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOmasREST subjectAreaOmasREST = new SubjectAreaOmasREST() ;
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        GlossaryTerm glossaryTerm = subjectAreaOmasREST.getGlossaryTermById(userId,guid);
        Term gotTerm = TermMapper.mapGlossaryTermtoTerm(glossaryTerm);
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
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Node="+ gotTerm );
        }
        return gotTerm;
    }

    /**
     * Update a Term's name
     * @param userId
     * @param guid
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/terms/{guid}/name/{name}")
    public Term updateTermName(@PathVariable String userId,@PathVariable String guid,@PathVariable String name) throws Exception {
        final String methodName = "updateTermName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Term term = getTermByGuid(userId,guid);
        Status status = term.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
        term.setName(name);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm glossaryTerm = TermMapper.mapTermToGlossaryTerm(term);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId,guid,glossaryTerm);

        Term updatedTerm =TermMapper.mapGlossaryTermtoTerm(updatedGlossaryTerm);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedTerm );
        }
        return updatedTerm;
    }
    /**
     * Update a Term's description
     * @param userId
     * @param guid
     * @param description
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/terms/{guid}/name/{name}")
    public Term updateTermDescription(@PathVariable String userId,@PathVariable String guid,@PathVariable String description) throws Exception {
        final String methodName = "updateTermDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Term term = getTermByGuid(userId,guid);
        Status status = term.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
        term.setDescription(description);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm glossaryTerm = TermMapper.mapTermToGlossaryTerm(term);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId,guid,glossaryTerm);

        Term updatedTerm =TermMapper.mapGlossaryTermtoTerm(updatedGlossaryTerm);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedTerm );
        }
        return updatedTerm;
    }
    /**
     * Update a Term's qualifiedName
     * @param userId
     * @param guid
     * @param qualifiedName
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/terms/{guid}/qualified-name/{qualifiedName}")
    public Term updateTermQualifiedName(@PathVariable String userId,@PathVariable String guid,@PathVariable String qualifiedName) throws Exception {
        final String methodName = "updateTermQualifiedName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Term term = getTermByGuid(userId,guid);
        Status status = term.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
        term.setQualifiedName(qualifiedName);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm glossaryTerm = TermMapper.mapTermToGlossaryTerm(term);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId,guid,glossaryTerm);

        Term updatedTerm =TermMapper.mapGlossaryTermtoTerm(updatedGlossaryTerm);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedTerm );
        }
        return updatedTerm;
    }
    /**
     * Update a Term's summary
     * @param userId
     * @param guid
     * @param summary
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/terms/{guid}/summary/{summary}")
    public Term updateTermSummary(@PathVariable String userId,@PathVariable String guid,@PathVariable String summary) throws Exception {
        final String methodName = "updateTermSummary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Term term = getTermByGuid(userId,guid);
        Status status = term.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.TERM_UPDATE_FAILED_ON_DELETED_TERM);
        term.setSummary(summary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm glossaryTerm = TermMapper.mapTermToGlossaryTerm(term);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm updatedGlossaryTerm = service.updateGlossaryTerm(userId,guid,glossaryTerm);

        Term updatedTerm =TermMapper.mapGlossaryTermtoTerm(updatedGlossaryTerm);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedTerm );
        }
        return updatedTerm;
    }
    @RequestMapping(method = RequestMethod.DELETE, path = "users/{userId}/terms/{guid}")
    public void deleteTerm(@PathVariable String userId, @PathVariable String guid, @RequestParam boolean isPurge) throws Exception {
        final String methodName = "deleteTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid="+guid );
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(oMRSAPIHelper);
        if (isPurge) {
            service.purgeGlossaryTermByGuid(userId, guid);
        } else {
            service.deleteGlossaryTermByGuid(userId, guid);
            //TODO catch expect saying soft delete not supported
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId);
        }
    }
}
