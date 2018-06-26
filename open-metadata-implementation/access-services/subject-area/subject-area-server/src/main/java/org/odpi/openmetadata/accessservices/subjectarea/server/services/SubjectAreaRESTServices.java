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


import org.odpi.openmetadata.accessservices.subjectarea.admin.SubjectAreaAdmin;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.validators.RestValidator;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.subjectarea.common.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */
@RestController
@RequestMapping("access-services/subject-area")
public class SubjectAreaRESTServices {
    static private String accessServiceName = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServices.class);

    private static final String className = SubjectAreaRESTServices.class.getName();

    // The OMRSAPIHelper allows the junits to mock out the omrs layer.
    protected OMRSAPIHelper oMRSAPIHelper = new OMRSAPIHelper();

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper) {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }


    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName   - name of this access service
     * @param repositoryConnector - OMRS Repository Connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
     */
    static public void setRepositoryConnector(String accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector) {
        SubjectAreaRESTServices.accessServiceName = accessServiceName;
        SubjectAreaRESTServices.repositoryConnector = repositoryConnector;
    }

    /**
     * Default constructor
     */
    public SubjectAreaRESTServices() {
        AccessServiceDescription myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                SubjectAreaAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }
    @RequestMapping(method = RequestMethod.GET, path = "users/{userId}/Node/guid/{guid}/possibleClassifications")
    public Set<String> getPossibleClassificationsForTerm(String userId, String guid) throws ClassificationErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, TypeDefNotKnownException, ConnectionCheckedException, ConnectorCheckedException, EntityProxyOnlyException, EntityNotKnownException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, UnrecognizedGUIDException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException {
        final String methodName = "getPossibleClassificationsForTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        RestValidator.validateUserIdNotNull(className,methodName,userId);
        RestValidator.validateGUIDNotNull(className,methodName,guid,"guid");

        Set<String> possibleClassifications = new HashSet<>();
        EntityDetail entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
        String typeName = entityDetail.getType().getTypeDefName();
        if (SubjectAreaUtils.isTerm(typeName)) {
            possibleClassifications = getPossibleClassificationsForEntity(userId, typeName);
        }  else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.WRONG_TYPE_FOR_ENTITY_GUID;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    guid,
                    "GlossaryTerm",
                    typeName);

            throw new org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        return possibleClassifications;
    }

    // TODO CRUDS for Node
    // TODO CRUDS for Category
    // TODO CRUDS for Glossary
    // TODO get possible classifications for term
    private Set<String> getPossibleClassificationsForEntity(String userId, String typeName) throws ClassificationErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, TypeDefNotKnownException, ConnectionCheckedException, ConnectorCheckedException, EntityProxyOnlyException, EntityNotKnownException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException {
        final String methodName = "getPossibleClassificationsForEntityType";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
        }
        Set<String> classifications = new HashSet<>();
        TypeDef typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
        if (typeDef.getCategory().getTypeCode() == (TypeDefCategory.ENTITY_DEF.getTypeCode())) {
            EntityDef entityDef = (EntityDef) typeDef;
            Set<EntityDef> entityDefs = new HashSet<>();
            entityDefs.add(entityDef);
            //find the entityDef super types
            while (entityDef.getSuperType() != null) {
                entityDef = (EntityDef) entityDef.getSuperType();
                entityDefs.add(entityDef);
            }

            TypeDefGallery gallery = oMRSAPIHelper.callGetAllTypes(userId);
            for (TypeDef def : gallery.getTypeDefs()) {
                if (def.getCategory().getTypeCode() == (TypeDefCategory.CLASSIFICATION_DEF.getTypeCode())) {
                    ClassificationDef classificationDef = (ClassificationDef) def;
                    // getValidEntityDefs() should account of classification inheritance hierarchies.
                    for (TypeDefLink validTypeDefLink : classificationDef.getValidEntityDefs()) {
                        for (EntityDef loopEntityDef : entityDefs) {
                            if (validTypeDefLink.getName().equals(loopEntityDef.getName())) {
                                classifications.add(classificationDef.getName());
                            }
                        }
                    }
                }
            }
        } else {
            // TODO error
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + "classifications=" + classifications);
        }
        return classifications;
    }

    public Set<String> getPossibleRelationshipsForTerm(String userId, String guid) throws ClassificationErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, TypeDefNotKnownException, ConnectionCheckedException, ConnectorCheckedException, EntityProxyOnlyException, EntityNotKnownException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, UnrecognizedGUIDException {
        final String methodName = "getPossibleRelationshipsForTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        Set<String> possibleRelationships = new HashSet<>();
        EntityDetail entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
        String typeName = entityDetail.getType().getTypeDefName();
        //TODO handle Exceptions properly i,e, change the method signature to now throw these internal Exceptions
        if (SubjectAreaUtils.isTerm(typeName)) {
           possibleRelationships = this.getPossibleRelationshipsForEntity(userId, typeName);
        }
        // TODO process Exception properly
        return possibleRelationships;

    }

    private Set<String> getPossibleRelationshipsForEntity(String userId, String typeName) throws ClassificationErrorException, StatusNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, TypeDefNotKnownException, ConnectionCheckedException, ConnectorCheckedException, EntityProxyOnlyException, EntityNotKnownException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException {
        final String methodName = "getPossibleRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
        }

        Set<String> relationships = new HashSet<>();
        TypeDef typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
        if (typeDef.getCategory().getTypeCode() == (TypeDefCategory.ENTITY_DEF.getTypeCode())) {
            EntityDef entityDef = (EntityDef) typeDef;
            TypeDefGallery gallery = oMRSAPIHelper.callGetAllTypes(userId);
            for (TypeDef def : gallery.getTypeDefs()) {
                if (def.getCategory().getTypeCode() == (TypeDefCategory.RELATIONSHIP_DEF.getTypeCode())) {
                    RelationshipDef relationshipDef = (RelationshipDef) def;
                    String end1Name = relationshipDef.getEndDef1().getEntityType().getName();
                    String end2Name = relationshipDef.getEndDef2().getEntityType().getName();
                    if (end1Name.equals(typeName) || end2Name.equals(typeName)) {
                        // Consider whether the relationshipDef guid might be more useful then the name.
                        relationships.add(relationshipDef.getName());
                    }
                }
            }
        } else {
            // TODO error
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + "relationships=" + relationships);
        }
        return relationships;
    }
    // TODO get graph
}
