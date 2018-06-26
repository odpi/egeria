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
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryReferences;
import org.odpi.openmetadata.accessservices.subjectarea.generated.references.ReferenceableToRelatedMedia.RelatedMediaReference;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaOmasREST;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.glossary.GlossaryMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.GovernanceLevel;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.node.NodeUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
public class SubjectAreaGlossaryRESTServices  extends SubjectAreaRESTServices{
    // these guids need to match the archive types. I did not want to introduce a dependancy on the archive types to get them.
    public static final String GLOSSARY_TYPE_GUID = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String TERM_ANCHOR_RELATIONSHIP_GUID = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    public static final String CATEGORY_ANCHOR_RELATIONSHIP_GUID = "c628938e-815e-47db-8d1c-59bb2e84e028";
    public static final String CATEGORY_HIERARCHY_LINK_GUID = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";

    static private String accessServiceName = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaGlossaryRESTServices.class);

    private static final String className = SubjectAreaGlossaryRESTServices.class.getName();

    /**
     * Default constructor
     */
    public SubjectAreaGlossaryRESTServices() {
        //SubjectAreaRESTServices registers this omas.
    }

    /**
     * Create a Glossary
     * @param userId
     * @param suppliedGlossary
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "users/{userId}/glossaries")
    public Glossary createGlossary(@PathVariable String userId, Glossary suppliedGlossary) throws Exception {
        final String methodName = "createGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }

        SubjectAreaGlossaryRESTServices glossaryRESTServices =new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(oMRSAPIHelper);

        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(suppliedGlossary);

        // need to check we have a name
        final String suppliedGlossaryName = suppliedGlossary.getName();
        if (suppliedGlossaryName == null || suppliedGlossaryName.equals("")) {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CREATE_WITHOUT_NAME;
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
            Glossary associatedGlossary = glossaryRESTServices.getGlossaryByName(userId,suppliedGlossaryName);
            if (associatedGlossary !=null) {
                // glossary found
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CREATE_FAILED_NAME_ALREADY_EXISTS;
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
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary newGeneratedGlossary = service.createGlossary(userId,generatedGlossary);
        // set the classifications
        String glossaryGuid = newGeneratedGlossary.getSystemAttributes().getGUID();

        List<Classification> classifications = new ArrayList<>();

        // governance levels
        if (null !=suppliedGlossary.getGovernanceLevels()) {
            List<GovernanceLevel> governanceLevels = suppliedGlossary.getGovernanceLevels();
            NodeUtils.addGovernanceLevelsToClassifications(classifications, governanceLevels);
        }

        // retention
        Retention retention = suppliedGlossary.getRetention();
        if (retention!=null) {
            classifications.add(retention);
        }
        try {
            service.addGlossaryClassifications(userId,glossaryGuid,classifications);
        } catch(Exception e) {
            // if it failed to create the relationship then we should delete the created term
            service.deleteGlossaryByGuid(userId,glossaryGuid);
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CREATE_FAILED_ADDING_CLASSIFICATIONS;
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
        Glossary newGlossary = getGlossaryByGuid(userId,glossaryGuid);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", created Glossary="+ newGlossary );
        }
        return newGlossary;
    }

    /**
     * Get a Glossary
     * @param userId
     * @param guid
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/glossaries/{guid}")
    public Glossary getGlossaryByGuid(@PathVariable String userId, String guid) throws Exception {
        final String methodName = "getGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid="+guid);
        }
        SubjectAreaOmasREST subjectAreaOmasREST = new SubjectAreaOmasREST() ;
        subjectAreaOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = subjectAreaOmasREST.getGlossaryById(userId,guid);
        Glossary gotGlossary = GlossaryMapper.mapGeneratedGlossarytoGlossary(generatedGlossary);
        List<Classification> classifications = generatedGlossary.getClassifications();
        // set the org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary classifications into the Node
        gotGlossary.setClassifications(classifications);

        List<Line> glossaryRelationships = subjectAreaOmasREST.getGlossaryRelationships(userId,guid);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryReferences generatedGlossaryReferences = new GlossaryReferences(guid, glossaryRelationships);
        // set icon
        Set<RelatedMediaReference> relatedMediaReferenceSet = generatedGlossaryReferences.getRelatedMediaReferences();
        String iconUrl = "";
                //SubjectAreaUtils.getIcon(userId, subjectAreaOmasREST, relatedMediaReferenceSet);

        if (iconUrl!=null) {
            gotGlossary.setIcon(iconUrl);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Node="+ gotGlossary );
        }
        return gotGlossary;
    }
    /**
     * Update a Glossary's name.
     *
     * If the caller has chosen to incorporate the glossary name in their Glossary Terms qualified name, renaming the glossary will cause those
     * qualified names to mismatch the Glossary name.
     *
     * @param userId
     * @param guid
     * @param name - new name for the glossary
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/glossaries/{guid}/name/{name}")
    public Glossary updateGlossaryName(@PathVariable String userId,@PathVariable String guid,@PathVariable String name) throws Exception {
        final String methodName = "updateGlossaryName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Glossary glossary = getGlossaryByGuid(userId,guid);
        Status status = glossary.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
        glossary.setName(name);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(glossary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = service.updateGlossary(userId,guid,generatedGlossary);

        Glossary updatedGlossary =GlossaryMapper.mapGeneratedGlossarytoGlossary(updatedGeneratedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedGlossary );
        }
        return updatedGlossary;
    }
    /**
     * Update a Glossary's description
     * @param userId
     * @param guid
     * @param description
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/glossaries/{guid}/description/{description}")
    public Glossary updateGlossaryDescription(@PathVariable String userId,@PathVariable String guid,@PathVariable String description) throws Exception {
        final String methodName = "updateGlossaryDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Glossary glossary = getGlossaryByGuid(userId,guid);
        Status status = glossary.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
        glossary.setDescription(description);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(glossary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = service.updateGlossary(userId,guid,generatedGlossary);

        Glossary updatedGlossary =GlossaryMapper.mapGeneratedGlossarytoGlossary(updatedGeneratedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedGlossary );
        }
        return updatedGlossary;
    }
    /**
     * Update a Glossary's qualifiedName
     * @param userId
     * @param guid
     * @param qualifiedName
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/glossaries/{guid}/qualified-name/{qualifiedName}")
    public Glossary updateGlossaryQualifiedName(@PathVariable String userId,@PathVariable String guid,@PathVariable String qualifiedName) throws Exception {
        final String methodName = "updateGlossaryDescription";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Glossary glossary = getGlossaryByGuid(userId,guid);
        Status status = glossary.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
        glossary.setQualifiedName(qualifiedName);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(glossary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = service.updateGlossary(userId,guid,generatedGlossary);

        Glossary updatedGlossary =GlossaryMapper.mapGeneratedGlossarytoGlossary(updatedGeneratedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedGlossary );
        }
        return updatedGlossary;
    }
    /**
     * Update a Glossary's usage
     * @param userId
     * @param guid
     * @param usage
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/glossaries/{guid}/usage/{usage}")
    public Glossary updateGlossaryUsage(@PathVariable String userId,@PathVariable String guid,@PathVariable String usage) throws Exception {
        final String methodName = "updateGlossaryUsage";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Glossary glossary = getGlossaryByGuid(userId,guid);
        Status status = glossary.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
        glossary.setUsage(usage);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(glossary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = service.updateGlossary(userId,guid,generatedGlossary);

        Glossary updatedGlossary =GlossaryMapper.mapGeneratedGlossarytoGlossary(updatedGeneratedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedGlossary );
        }
        return updatedGlossary;
    }
    /**
     * Update a Glossary's usage
     * @param userId
     * @param guid
     * @param language
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "users/{userId}/glossaries/{guid}/language/{language}")
    public Glossary updateGlossaryLanguage(@PathVariable String userId,@PathVariable String guid,@PathVariable String language) throws Exception {
        final String methodName = "updateGlossaryLanguage";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId);
        }
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(this.oMRSAPIHelper);
        Glossary glossary = getGlossaryByGuid(userId,guid);
        Status status = glossary.getSystemAttributes().getStatus();
        SubjectAreaUtils.checkStatusNotDeleted(status,SubjectAreaErrorCode.GLOSSARY_UPDATE_FAILED_ON_DELETED_GLOSSARY);
        glossary.setLanguage(language);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary generatedGlossary = GlossaryMapper.mapGlossaryToGeneratedGlossary(glossary);
        org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary updatedGeneratedGlossary = service.updateGlossary(userId,guid,generatedGlossary);

        Glossary updatedGlossary =GlossaryMapper.mapGeneratedGlossarytoGlossary(updatedGeneratedGlossary);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", updated Glossary="+ updatedGlossary );
        }
        return updatedGlossary;
    }

    /**
     * Get a Glossary by name
     * @param userId
     * @param name
     * @return
     * @throws InvalidParameterException
     * @throws UserNotAuthorizedException
     * @throws PropertyServerException
     * @throws PagingException
     * @throws FunctionNotSupportedException
     * @throws TypeException
     */

    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/glossaries/by-name/{name}")
    public Glossary getGlossaryByName(@PathVariable String userId, String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, PagingException, FunctionNotSupportedException, TypeException {
        final String methodName = "getGlossaryByName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",name="+name);
        }
        RestValidator.validateUserIdNotNull(className,methodName,userId);

        MatchCriteria matchCriteria = MatchCriteria.ALL;
        // guid for the Glossary Type.
        String entityTypeGUID= GLOSSARY_TYPE_GUID;
        InstanceProperties matchProperties= new InstanceProperties();
        PrimitivePropertyValue nameValue = new PrimitivePropertyValue();
        nameValue.setPrimitiveValue(name);
        nameValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        matchProperties.setProperty("displayName", nameValue);
        List<EntityDetail> omrsEntityDetails = oMRSAPIHelper.callFindEntitiesByProperty(
                userId,
                entityTypeGUID,
                matchProperties,
                matchCriteria,
                0,
                null,
                null,
                null,
                null,
                null,
                0

        );
        Glossary gotGlossary = null;
        if (omrsEntityDetails.size() >0 ) {
             org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.Glossary gotGeneratedGlossary = org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Glossary.GlossaryMapper.mapOmrsEntityDetailToGlossary(omrsEntityDetails.get(0));
             gotGlossary = GlossaryMapper.mapGeneratedGlossarytoGlossary(gotGeneratedGlossary);
        }
        //If there are more than one then pick the first one. We could be more sophisticated with this in the future maybe identify a primary icon.

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+", Node="+ gotGlossary );
        }
        return gotGlossary;
    }

    /**
     * Delete a Glossary instance
     *
     * The deletion of a glossary is only allowed if there is no glossary content (i.e. no terms or categories).
     *
     * If we attempt to delete the glossary and soft deletes are not supported then we should tell the user to issue a purge.
     *
     * @param userId
     * @param entityGUID
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "users/{userId}/glossaries/{entityGUID}")
    public void deleteGlossary(@PathVariable String userId, @PathVariable String entityGUID,@RequestParam boolean isPurge)  throws Exception {
        final String methodName = "deleteGlossary";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", entityGUID="+entityGUID );
        }
        //do not delete if there is glossary content (terms or categories)
        // look for all glossary content that is not deleted.
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        statusList.add(InstanceStatus.DRAFT);
        statusList.add(InstanceStatus.PROPOSED);
        statusList.add(InstanceStatus.PREPARED);
        statusList.add(InstanceStatus.UNKNOWN);

        List<Relationship> terms = oMRSAPIHelper.callGetRelationshipsForEntity(userId,entityGUID,TERM_ANCHOR_RELATIONSHIP_GUID,0,statusList,null,null,null,1);
        List<Relationship> categories = oMRSAPIHelper.callGetRelationshipsForEntity(userId,entityGUID,CATEGORY_ANCHOR_RELATIONSHIP_GUID,0,statusList,null,null,null,1);
        SubjectAreaOmasREST service = new SubjectAreaOmasREST();
        service.setOMRSAPIHelper(oMRSAPIHelper);
        if (terms.isEmpty() && categories.isEmpty()) {
            if (isPurge) {
                service.deleteGlossaryByGuid(userId,entityGUID);
                // TODO catch the exception saying that soft delete is not supported.
            } else {
                service.purgeGlossaryByGuid(userId,entityGUID);
            }
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_CONTENT_PREVENTED_DELETE;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName,entityGUID);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId);
        }
    }
}
