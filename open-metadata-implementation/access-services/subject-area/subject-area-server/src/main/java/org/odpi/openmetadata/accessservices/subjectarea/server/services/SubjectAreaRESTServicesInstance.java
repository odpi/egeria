/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaInstanceHandler;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class);
    private static final String className = SubjectAreaRESTServicesInstance.class.getName();

    // The OMRSAPIHelper allows the junits to mock out the omrs layer.
    protected OMRSAPIHelper oMRSAPIHelper = null;
    static private String accessServiceName = null;
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance()
    {
    }

    /**
     * Setup the OMRSAPIHelper with the right instance based on the serverName
     *
     * @param serverName serverName, used to identify a server in a multitenant environment.
     * @return SubjectAreaOMASAPIResponse null if successful
     */
    protected SubjectAreaOMASAPIResponse initialiseOMRSAPIHelperForInstance(String serverName)
    {
        SubjectAreaOMASAPIResponse response = null;
        // this is set as a mock object for junits.
        if (oMRSAPIHelper ==null)
        {
            oMRSAPIHelper = new OMRSAPIHelper();
        }
        try
        {
            OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
            oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        }
        return response;
    }

/*
     * @param serverName  - name of the server that the request is for
     * @param userId             - String - userId of user making request.
     * @param classification - this may be the qualifiedName or displayName of the connection.
     * @return GovernanceClassificationDefinitionList or
     * InvalidParameterException - one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException - there is no connection defined for this name.
     * AmbiguousConnectionNameException - there is more than one connection defined for this name.
     * PropertyServerException - there is a problem retrieving information from the property (metadata) handlers.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.

public GovernanceClassificationDefListAPIResponse getGovernanceClassificationDefs(String serverName, String userId, List<String> classification
) {
    final String methodName = "getGovernanceClassificationDefs";

    if (log.isDebugEnabled()) {
        log.debug("Calling method: " + methodName);
    }

    // create API response
    GovernanceClassificationDefListAPIResponse response = new GovernanceClassificationDefListAPIResponse();

    // Invoke the right handler for this API request
    try {
        GovernanceClassificationDefHandler governanceClassificationDefHandler = new GovernanceClassificationDefHandler(instanceHandler.getAccessServiceName(serverName),
                instanceHandler.getRepositoryConnector(serverName));

        response.setClassificationDefsList(governanceClassificationDefHandler.getGovernanceClassificationDefs(userId, classification));
 */
    //
    //    public SubjectAreaOMASAPIResponse getPossibleClassificationsForTerm(String serverName,String userId, String guid)  {
    //        final String methodName = "getPossibleClassificationsForTerm";
    //        if (log.isDebugEnabled()) {
    //            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
    //        }
    //
    //        SubjectAreaOMASAPIResponse response=null;
    //
    //        try {
    //            InputValidator.validateUserIdNotNull(className,methodName,userId);
    //            InputValidator.validateGUIDNotNull(className,methodName,guid,"guid");
    //        } catch (InvalidParameterException invalidParameterException) {
    //            response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
    //        }
    //        EntityDetail entityDetail = null;
    //        if (response==null) {
    //            try
    //            {
    //                instanceHandler.getRepositoryConnector(serverName);
    //                entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
    //            } catch (MetadataServerUncontactableException e) {
    //                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
    //            } catch (UserNotAuthorizedException e) {
    //                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
    //            } catch (InvalidParameterException e) {
    //                response = OMASExceptionToResponse.convertInvalidParameterException(e);
    //            } catch (UnrecognizedGUIDException e) {
    //                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
    //            }
    //            String typeName = entityDetail.getType().getTypeDefName();
    //
    //            if (response ==null) {
    //                if (SubjectAreaUtils.isTerm(typeName)) {
    //                    response = getPossibleClassificationsForEntity(userId, typeName);
    //
    //                } else {
    //                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPE_FOR_ENTITY_GUID;
    //                    String errorMessage = errorCode.getErrorMessageId()
    //                            + errorCode.getFormattedErrorMessage(
    //                            guid,
    //                            "GlossaryTerm",
    //                            typeName);
    //
    //
    //                    InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(),
    //                            className,
    //                            methodName,
    //                            errorMessage,
    //                            errorCode.getSystemAction(),
    //                            errorCode.getUserAction());
    //                    response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
    //                }
    //            }
    //        }
    //
    //        if (log.isDebugEnabled()) {
    //            log.debug("<== Method: " + methodName + ",userId=" + userId + ", guid" + guid);
    //        }
    //        return response;
    //    }
    //
    //    // TODO get possible classifications for term
    //    private SubjectAreaOMASAPIResponse getPossibleClassificationsForEntity(String userId, String typeName) {
    //        final String methodName = "getPossibleClassificationsForEntityType";
    //        if (log.isDebugEnabled()) {
    //            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
    //        }
    //        SubjectAreaOMASAPIResponse response = null;
    //        try {
    //
    //            Set<String> classifications = new HashSet<>();
    //            TypeDef typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
    //
    //            if (typeDef.getCategory().getName() == (TypeDefCategory.ENTITY_DEF.getName())) {
    //                EntityDef entityDef = (EntityDef) typeDef;
    //                Set<EntityDef> entityDefs = new HashSet<>();
    //                entityDefs.add(entityDef);
    //                //find the entityDef super types
    //                while (entityDef.getSuperType() != null) {
    //                    entityDef = (EntityDef) entityDef.getSuperType();
    //                    entityDefs.add(entityDef);
    //                }
    //                TypeDefGallery gallery = oMRSAPIHelper.callGetAllTypes(userId);
    //                for (TypeDef def : gallery.getTypeDefs()) {
    //                    if (def.getCategory().getName() == (TypeDefCategory.CLASSIFICATION_DEF.getName())) {
    //                        ClassificationDef classificationDef = (ClassificationDef) def;
    //                        // getValidEntityDefs() should account of classification inheritance hierarchies.
    //                        for (TypeDefLink validTypeDefLink : classificationDef.getValidEntityDefs()) {
    //                            for (EntityDef loopEntityDef : entityDefs) {
    //                                if (validTypeDefLink.getName().equals(loopEntityDef.getName())) {
    //                                    classifications.add(classificationDef.getName());
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //                response = new PossibleClassificationsResponse(classifications);
    //            } else {
    //                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPENAME;
    //                String errorMessage = errorCode.getErrorMessageId()
    //                        + errorCode.getFormattedErrorMessage(
    //                        typeDef.getCategory().getName(),
    //                        typeName);
    //                InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(),
    //                        className,
    //                        methodName,
    //                        errorMessage,
    //                        errorCode.getSystemAction(),
    //                        errorCode.getUserAction());
    //                response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
    //            }
    //        }catch (UserNotAuthorizedException e) {
    //            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
    //        } catch (InvalidParameterException e) {
    //            response = OMASExceptionToResponse.convertInvalidParameterException(e);
    //        } catch (MetadataServerUncontactableException e) {
    //            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
    //        }
    //        if (log.isDebugEnabled()) {
    //            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + ", response=" + response);
    //        }
    //        return response;
    //    }

    public SubjectAreaOMASAPIResponse getPossibleRelationshipsForTerm(String serverName, String userId, String guid)
    {
        final String methodName = "getPossibleRelationshipsForTerm";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", guid" + guid);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initialiseOMRSAPIHelperForInstance(serverName);
        EntityDetail entityDetail = null;
        if (response == null)
        {
            try
            {
                entityDetail = oMRSAPIHelper.callOMRSGetEntityByGuid(userId, guid);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnrecognizedGUIDException e)
            {
                response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
            }
        }
        if (response == null)
        {
            String typeName = entityDetail.getType().getTypeDefName();
            if (SubjectAreaUtils.isTerm(typeName))
            {
                response = this.getPossibleRelationshipsForEntity(serverName, userId, typeName);
            }
        }
        return response;
    }

    private SubjectAreaOMASAPIResponse getPossibleRelationshipsForEntity(String serverName, String userId, String typeName)
    {
        final String methodName = "getPossibleRelationshipsForEntity";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ", typeName" + typeName);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initialiseOMRSAPIHelperForInstance(serverName);
        Set<String> relationships = new HashSet<>();
        TypeDef typeDef = null;
        if (response == null)
        {
            try
            {
                typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
            } catch (MetadataServerUncontactableException e)
            {
                response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
            } catch (UserNotAuthorizedException e)
            {
                response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (response == null)
        {
            if (typeDef.getCategory().getName() == (TypeDefCategory.ENTITY_DEF.getName()))
            {
                EntityDef entityDef = (EntityDef) typeDef;
                TypeDefGallery gallery = null;
                try
                {
                    gallery = oMRSAPIHelper.callGetAllTypes(userId);
                } catch (MetadataServerUncontactableException e)
                {
                    response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
                } catch (UserNotAuthorizedException e)
                {
                    response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
                }
                if (response == null)
                {
                    for (TypeDef def : gallery.getTypeDefs())
                    {
                        if (def.getCategory().getName() == (TypeDefCategory.RELATIONSHIP_DEF.getName()))
                        {
                            RelationshipDef relationshipDef = (RelationshipDef) def;
                            String end1Name = relationshipDef.getEndDef1().getEntityType().getName();
                            String end2Name = relationshipDef.getEndDef2().getEntityType().getName();
                            if (end1Name.equals(typeName) || end2Name.equals(typeName))
                            {
                                // Consider whether the relationshipDef guid might be more useful then the name.
                                relationships.add(relationshipDef.getName());
                            }
                        }
                    }
                    response = new PossibleRelationshipsResponse(relationships);
                }
            } else
            {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.WRONG_TYPENAME;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(typeDef.getCategory().getName(), typeName);
                InvalidParameterException invalidParameterException = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(invalidParameterException);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== Method: " + methodName + ",userId=" + userId + ", typeName=" + typeName + "relationships=" + relationships);
        }
        return response;
    }

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }
    // TODO get graph
}
