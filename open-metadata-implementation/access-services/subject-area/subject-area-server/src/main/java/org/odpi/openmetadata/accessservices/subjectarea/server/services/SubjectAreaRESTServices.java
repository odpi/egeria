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
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleClassificationsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleRelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The SubjectAreaRESTServices provides the org.odpi.openmetadata.accessservices.subjectarea.server-side implementation of the SubjectArea Open Metadata
 * Assess Service (OMAS).  This interface provides glossary authoring interfaces for subject area experts.
 */

public class SubjectAreaRESTServices {
    static private String accessServiceName = null;
    static  private OMRSRepositoryConnector repositoryConnector = null;

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
     * @param accessServiceName    name of this access service
     * @param repositoryConnector  OMRS Repository Connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
     */
    static public void setRepositoryConnector(String accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector) {
        SubjectAreaRESTServices.accessServiceName = accessServiceName;
        SubjectAreaRESTServices.repositoryConnector = repositoryConnector;
    }
    static public OMRSRepositoryConnector getRepositoryConnector() {
        return repositoryConnector;

    }

    /**
     * Default constructor
     */
    public SubjectAreaRESTServices() {
        boolean initialized = false ;
        List<AccessServiceRegistration> registeredServices = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();
        for (AccessServiceRegistration accessServiceRegistration:registeredServices) {
           if (AccessServiceDescription.SUBJECT_AREA_OMAS.getAccessServiceCode()== accessServiceRegistration.getAccessServiceCode()){
               initialized = true;
           }
        }
        if (!initialized) {
            AccessServiceDescription myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;
            AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                    myDescription.getAccessServiceName(),
                    myDescription.getAccessServiceDescription(),
                    myDescription.getAccessServiceWiki(),
                    AccessServiceOperationalStatus.ENABLED,
                    SubjectAreaAdmin.class.getName());
            OMAGAccessServiceRegistration.registerAccessService(myRegistration);
        }
    }
    public SubjectAreaOMASAPIResponse getPossibleClassificationsForTerm(String userId, String guid)  {
        final String methodName = "getPossibleClassificationsForTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }

        SubjectAreaOMASAPIResponse response=null;

        try {
            InputValidator.validateUserIdNotNull(className,methodName,userId);
            InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
        } catch (InvalidParameterException invalidParameterException) {
            response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
        }

        if (response==null) {

            EntityDetail entityDetail = null;
            try {
                entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
            } catch (MetadataServerUncontactableException e) {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e) {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e) {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e) {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
            String typeName = entityDetail.getType().getTypeDefName();

            if (response ==null) {
                if (SubjectAreaUtils.isTerm(typeName)) {
                    response = getPossibleClassificationsForEntity(userId, typeName);

                } else {
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPE_FOR_ENTITY_GUID;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(
                            guid,
                            "GlossaryTerm",
                            typeName);


                    InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                    response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        return response;
    }

    // TODO CRUDS for Node
    // TODO CRUDS for Category
    // TODO CRUDS for Glossary
    // TODO get possible classifications for term
    private SubjectAreaOMASAPIResponse getPossibleClassificationsForEntity(String userId, String typeName) {
        final String methodName = "getPossibleClassificationsForEntityType";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {

            Set<String> classifications = new HashSet<>();
            TypeDef typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);

            if (typeDef.getCategory().getName() == (TypeDefCategory.ENTITY_DEF.getName())) {
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
                    if (def.getCategory().getName() == (TypeDefCategory.CLASSIFICATION_DEF.getName())) {
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
                response = new PossibleClassificationsResponse(classifications);
            } else {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPENAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(
                        typeDef.getCategory().getName(),
                        typeName);
                InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
            }
        }catch (UserNotAuthorizedException e) {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (MetadataServerUncontactableException e) {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + ", response=" + response);
        }
        return response;
    }

    public SubjectAreaOMASAPIResponse getPossibleRelationshipsForTerm(String userId, String guid)  {
        final String methodName = "getPossibleRelationshipsForTerm";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        SubjectAreaOMASAPIResponse response =null;
        EntityDetail entityDetail = null;
        try {
            entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
        } catch (MetadataServerUncontactableException e) {
            response=  OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response=  OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response=  OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UnrecognizedGUIDException e) {
            response=  OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        }
        if (response ==null) {
            String typeName = entityDetail.getType().getTypeDefName();
            if (SubjectAreaUtils.isTerm(typeName)) {
                response = this.getPossibleRelationshipsForEntity(userId, typeName);
            }
        }
        return response;
    }

    private SubjectAreaOMASAPIResponse getPossibleRelationshipsForEntity(String userId, String typeName) {
        final String methodName = "getPossibleRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
        }
        SubjectAreaOMASAPIResponse response =null;
        Set<String> relationships = new HashSet<>();
        TypeDef typeDef = null;
        try {
            typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
        } catch (MetadataServerUncontactableException e) {
            response=  OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UserNotAuthorizedException e) {
            response=  OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (InvalidParameterException e) {
            response=  OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (response ==null) {
            if (typeDef.getCategory().getName() == (TypeDefCategory.ENTITY_DEF.getName())) {
                EntityDef entityDef = (EntityDef) typeDef;
                TypeDefGallery gallery = null;
                try {
                    gallery = oMRSAPIHelper.callGetAllTypes(userId);
                } catch (MetadataServerUncontactableException e) {
                    response=  OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e) {
                    response=  OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                }
                for (TypeDef def : gallery.getTypeDefs()) {
                    if (def.getCategory().getName() == (TypeDefCategory.RELATIONSHIP_DEF.getName())) {
                        RelationshipDef relationshipDef = (RelationshipDef) def;
                        String end1Name = relationshipDef.getEndDef1().getEntityType().getName();
                        String end2Name = relationshipDef.getEndDef2().getEntityType().getName();
                        if (end1Name.equals(typeName) || end2Name.equals(typeName)) {
                            // Consider whether the relationshipDef guid might be more useful then the name.
                            relationships.add(relationshipDef.getName());
                        }
                    }
                }
                response = new PossibleRelationshipsResponse(relationships);
            } else {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPENAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(
                        typeDef.getCategory().getName(),
                        typeName);
                InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        className,
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + "relationships=" + relationships);
        }
        return response;
    }
    // TODO get graph
}
