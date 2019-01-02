/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
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

import java.util.Date;
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

//    /**
//     * Setup the OMRSAPIHelper with the right instance based on the serverName
//     *
//     * @param serverName serverName, used to identify a server in a multitenant environment.
//     * @return SubjectAreaOMASAPIResponse null if successful
//     */
//    protected void initialiseOMRSAPIHelperForInstance(String serverName) throws MetadataServerUncontactableException
//    {
//        // this is set as a mock object for junits.
//        if (oMRSAPIHelper ==null)
//        {
//            oMRSAPIHelper = new OMRSAPIHelper();
//        }
//
//        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
//        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, String methodName) throws MetadataServerUncontactableException,
            InvalidParameterException
    {
        return initializeAPI(serverName,userId,null,null,methodName);
    }
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param from effective from date
     * @param to effective to Date
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, Date from, Date to, String methodName) throws MetadataServerUncontactableException, InvalidParameterException
    {
        // this is set as a mock object for junits.
        if (oMRSAPIHelper ==null)
        {
            oMRSAPIHelper = new OMRSAPIHelper();
        }
        // initialise omrs API helper with the right instance based on the server name
        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateEffectiveDate(className,methodName,to,from);
        return subjectAreaOmasREST;
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
        SubjectAreaOMASAPIResponse response = null;
        EntityDetail entityDetail = null;
        if (response == null)
        {
            try
            {
                // initialise omrs API helper with the right instance based on the server name
                SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
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
        SubjectAreaOMASAPIResponse response = null;
        Set<String> relationships = new HashSet<>();
        TypeDef typeDef = null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            typeDef = oMRSAPIHelper.callGetTypeDefByName(userId, typeName);
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
