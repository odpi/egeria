/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.*;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.VoidResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.handlers.ErrorHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a facade around the OMRS API. It transforms the OMRS Exceptions into OMAS exceptions
 */
public class OMRSAPIHelper {

    // logging
    private static final Logger log = LoggerFactory.getLogger(OMRSAPIHelper.class);
    private static final String className = OMRSAPIHelper.class.getName();

    private OMRSMetadataCollection oMRSMetadataCollection=null;
    private ErrorHandler errorHandler=null;
    final private String serviceName;
    private String serverName = null;
    private OMRSRepositoryHelper omrsRepositoryHelper  = null;

    public OMRSMetadataCollection getOMRSMetadataCollection(String restAPIName) throws MetadataServerUncontactableException {
        validateInitialization(restAPIName);
        return oMRSMetadataCollection;
    }
    public OMRSRepositoryHelper getOMRSRepositoryHelper() {
        return omrsRepositoryHelper;
    }

    public void setOMRSMetadataCollection(OMRSMetadataCollection oMRSMetadataCollection) {
        this.oMRSMetadataCollection = oMRSMetadataCollection;
    }

    public void setOmrsRepositoryHelper(OMRSRepositoryHelper omrsRepositoryHelper) {
        this.omrsRepositoryHelper = omrsRepositoryHelper;
    }
    //    /**
//     * Set the OMRS repository connector
//     * @param connector connector cannot be null
//     * @param restAPIName rest API name
//     * @throws MetadataServerUncontactableException Metadata server not contactable
//     */
    public void setOMRSRepositoryConnector(OMRSRepositoryConnector connector, String restAPIName) throws MetadataServerUncontactableException {
        String methodName = "setOMRSRepositoryConnector";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + "connector="+connector);
        }

        try {
            this.oMRSMetadataCollection = connector.getMetadataCollection();
            this.omrsRepositoryHelper = connector.getRepositoryHelper();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(e.getMessage(),
                    restAPIName,
                    serviceName,
                    serverName);
            log.error(errorMessage);
            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    restAPIName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
    }

    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     * @throws MetadataServerUncontactableException not initialized
     * @param restAPIName
     */
    private void validateInitialization(String restAPIName) throws MetadataServerUncontactableException {
       
        if (oMRSMetadataCollection == null) {
            if (this.omrsRepositoryHelper == null) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(restAPIName);

                throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        restAPIName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }
    }

    public OMRSAPIHelper(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Get the service name - ths is used for logging
     * @return service name
     */
    public String getServiceName() {
        return this.serviceName;
    }

    // entity CRUD
    public  SubjectAreaOMASAPIResponse callOMRSAddEntity(String restAPIName, String userId , EntityDetail entityDetail) {
        String methodName = "callOMRSAddEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            EntityDetail addedEntityDetail=getOMRSMetadataCollection(restAPIName).addEntity(userId, entityDetail.getType().getTypeDefGUID(), instanceProperties, entityDetail.getClassifications(), InstanceStatus.ACTIVE);
            response = new EntityDetailResponse(addedEntityDetail);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
           response =  this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
           response =  this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse callOMRSGetEntityByGuid(String restAPIName, String userId , String entityGUID) {

        String methodName = "callOMRSGetEntityByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       

        try {
            EntityDetail gotEntityDetail=  getOMRSMetadataCollection(restAPIName).getEntityDetail(userId, entityGUID);
            response = new EntityDetailResponse(gotEntityDetail);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e) {
           response =  this.errorHandler.handleEntityProxyOnlyException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }


    public SubjectAreaOMASAPIResponse callFindEntitiesByPropertyValue(String restAPIName, String     userId,
                                                         String                    entityTypeGUID,
                                                         String                    searchCriteria ,
                                                         int                       fromEntityElement,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         List<String>              limitResultsByClassification,
                                                         Date                      asOfTime,
                                                         String                    sequencingProperty,
                                                         SequencingOrder sequencingOrder,
                                                         int                       pageSize) {

        String methodName = "callFindEntitiesByPropertyValue";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       

        try {
            List<EntityDetail>  foundEntities = getOMRSMetadataCollection(restAPIName).findEntitiesByPropertyValue(userId,
                    entityTypeGUID,
                    searchCriteria,
                    fromEntityElement,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,pageSize);
            response = new EntityDetailsResponse(foundEntities);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
           response =  this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }

    public SubjectAreaOMASAPIResponse callGetEntitiesByType(String restAPIName, String     userId,
                                                            String                    entityTypeGUID,
                                                            Date                      asOfTime,
                                                            int                       fromEntityElement,
                                                            int                       pageSize) {

        String methodName = "callGetEntitiesByType";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;

        try {
            List<EntityDetail>  foundEntities = getOMRSMetadataCollection(restAPIName).findEntitiesByProperty(userId,
                    entityTypeGUID,
                    null,
                    null,
                    fromEntityElement,
                    null,
                    null,
                    asOfTime,
                    null,
                    null,
                    pageSize
                   );
            response = new EntityDetailsResponse(foundEntities);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            response =  this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }


    public  SubjectAreaOMASAPIResponse callOMRSUpdateEntityProperties(String restAPIName, String userId , EntityDetail entityDetail) {
        String methodName = "callOMRSUpdateEntityProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       

        EntityDetail updatedEntity = null;

        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            updatedEntity = getOMRSMetadataCollection(restAPIName).updateEntityProperties(userId, entityDetail.getGUID(), instanceProperties);
            response = new EntityDetailResponse(updatedEntity);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityDetail.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public  SubjectAreaOMASAPIResponse callOMRSDeleteEntity(String restAPIName, String userId , String typeDefName, String typeDefGuid, String obsoleteGuid) {
        String methodName = "callOMRSDeleteEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        
       
        SubjectAreaOMASAPIResponse response = null;
        try {
            EntityDetail deletedEntity   = getOMRSMetadataCollection(restAPIName).deleteEntity(userId,typeDefGuid, typeDefName, obsoleteGuid);
            response = new EntityDetailResponse(deletedEntity);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse  callOMRSPurgeEntity(String restAPIName, String userId , String typeDefName, String typeDefGuid, String obsoleteGuid) {
        String methodName = "callOMRSPurgeEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       
        try {
            getOMRSMetadataCollection(restAPIName).purgeEntity(userId, typeDefGuid, typeDefName,  obsoleteGuid);
            response = new VoidResponse();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e) {
           response =  this.errorHandler.handleEntityNotPurgedException(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public  SubjectAreaOMASAPIResponse callOMRSRestoreEntity(String restAPIName, String userId ,String guid)
    {
        // restore the Entity
        String methodName = "callOMRSRestoreEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            EntityDetail restoredEntity =getOMRSMetadataCollection(restAPIName).restoreEntity(userId, guid);
            response = new EntityDetailResponse(restoredEntity);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (EntityNotDeletedException e)
        {
           response =  this.errorHandler.handleEntityNotDeletedException(guid,
                    restAPIName,
                    serverName,
                    serviceName
            );
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }

    // entity classification
    public  SubjectAreaOMASAPIResponse callOMRSClassifyEntity(String restAPIName, String userId ,
                                               String entityGUID,
                                               String classificationName,
                                               InstanceProperties instanceProperties
    )  {
        String methodName = "callOMRSClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       

        try {
            EntityDetail entity = getOMRSMetadataCollection(restAPIName).classifyEntity(userId, entityGUID, classificationName, instanceProperties);
            response = new EntityDetailResponse(entity);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
           response =  this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

          } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }

    public  SubjectAreaOMASAPIResponse callOMRSDeClassifyEntity(String restAPIName, String userId ,
                                                 String entityGUID,
                                                 String classificationName
    )  {

        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        
       

        try {
            EntityDetail entity = getOMRSMetadataCollection(restAPIName).declassifyEntity(userId, entityGUID, classificationName);
            response = new EntityDetailResponse(entity);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
           response =  this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }


    // relationship CRUD
    public  SubjectAreaOMASAPIResponse callOMRSAddRelationship(String restAPIName, String userId , Relationship relationship) {
        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response =null;
        
       

        try {
            Relationship addedRelationship =getOMRSMetadataCollection(restAPIName).addRelationship(userId,
                    relationship.getType().getTypeDefGUID(),
                    relationship.getProperties(),
                    relationship.getEntityOneProxy().getGUID(),
                    relationship.getEntityTwoProxy().getGUID(),
                    InstanceStatus.ACTIVE);
            response = new RelationshipResponse(addedRelationship);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {

           response = response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
           response =  this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            //TODO this is the wrong guid. We should pass the entity guid that is not found. But that is not yet in the the Excpetion we get

           response =  this.errorHandler.handleEntityNotKnownError(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }

    public  SubjectAreaOMASAPIResponse callOMRSGetRelationshipByGuid(String restAPIName, String userId , String relationshipGUID)
            {
        String methodName = "callOMRSGetRelationshipByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            Relationship relationship = getOMRSMetadataCollection(restAPIName).getRelationship(userId,relationshipGUID);
            response = new RelationshipResponse(relationship);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
           response =  this.errorHandler.handleRelationshipNotKnownException(relationshipGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public  SubjectAreaOMASAPIResponse callOMRSUpdateRelationship(String restAPIName, String userId , Relationship relationship)
            {
        String methodName = "callOMRSUpdateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        Relationship updatedRelationship = null;
        // update the relationship properties
        try {
            updatedRelationship = getOMRSMetadataCollection(restAPIName).updateRelationshipProperties(userId,
                    relationship.getGUID(),
                    relationship.getProperties());
            if ( relationship.getStatus() !=null && updatedRelationship !=null &&
                    !relationship.getStatus().equals(updatedRelationship.getStatus())) {
                updatedRelationship = getOMRSMetadataCollection(restAPIName).updateRelationshipStatus(userId,
                        relationship.getGUID(),
                        relationship.getStatus());
            }
            response = new RelationshipResponse(updatedRelationship);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
           response =  this.errorHandler.handleRelationshipNotKnownException(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
           response =  this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return response;

    }
    public SubjectAreaOMASAPIResponse callOMRSDeleteRelationship(String restAPIName, String userId , String typeGuid, String typeName,String guid) {
        // delete the relationship
        String methodName = "callOMRSDeleteRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            Relationship deletedRelationship =getOMRSMetadataCollection(restAPIName).deleteRelationship(userId, typeGuid, typeName, guid);
            response = new RelationshipResponse(deletedRelationship);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
           response =  this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse callOMRSRestoreRelationship(String restAPIName, String userId ,String guid) {
        // restore the relationship
        String methodName = "callOMRSRestoreRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        Relationship restoredRelationship = null;
        try {
            restoredRelationship =getOMRSMetadataCollection(restAPIName).restoreRelationship(userId, guid);
            response = new RelationshipResponse(restoredRelationship);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
           response =  this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RelationshipNotDeletedException e)
        {
           response =  this.errorHandler.handleRelationshipNotDeletedException(guid,
                    restAPIName,
                    serverName,
                    serviceName
                    );
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse callOMRSPurgeRelationship(String restAPIName, String userId , String typeGuid, String typeName,String guid)
    {

        // delete the relationship
        String methodName = "callOMRSPurgeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        try {
            getOMRSMetadataCollection(restAPIName).purgeRelationship(userId, typeGuid, typeName, guid);
            response = new VoidResponse();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
           response =  this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException e) {
           response =  this.errorHandler.handleRelationshipNotPurgedException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }
    public SubjectAreaOMASAPIResponse callGetRelationshipsForEntity(String restAPIName,
                                                                    String                   userId,
                                                            String                     entityGUID,
                                                            String                     relationshipTypeGuid,
                                                            int                        fromRelationshipElement,
                                                            Date                       asOfTime,
                                                            String                     sequencingProperty,
                                                            SequencingOrder            sequencingOrder,
                                                            int                        pageSize)
           {

        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        

        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        List<Relationship> relationships = null;
        try {
            relationships = getOMRSMetadataCollection(restAPIName).getRelationshipsForEntity(userId,
                    entityGUID,
                   relationshipTypeGuid,
                    fromRelationshipElement,
                    statusList,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,
                    pageSize
            );
            response = new RelationshipsResponse(relationships);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
           response =  this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return response;
    }

    public SubjectAreaOMASAPIResponse callGetRelationshipsForEntity(String restAPIName, 
                                                                    String         userId,
                                                            String                     entityGUID,
                                                            String                     relationshipTypeGUID,
                                                            int                        fromRelationshipElement,
                                                            List<InstanceStatus>       limitResultsByStatus,
                                                            Date                       asOfTime,
                                                            String                     sequencingProperty,
                                                            SequencingOrder            sequencingOrder,
                                                            int                        pageSize)  {
        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        

        try {
            List<Relationship>  relationships =  getOMRSMetadataCollection(restAPIName).getRelationshipsForEntity(userId,
                    entityGUID,relationshipTypeGUID,fromRelationshipElement,limitResultsByStatus,asOfTime,
                    sequencingProperty,sequencingOrder,pageSize);
            response = new RelationshipsResponse(relationships);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
           response =  this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
           response =  this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return response;
    }



    public SubjectAreaOMASAPIResponse  callGetEntityNeighbourhood(String restAPIName, String userId , String entityGUID,
                                                    List<String> entityTypeGUIDs,
                                                    List<String> relationshipTypeGUIDs,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    List<String> limitResultsByClassification,
                                                    Date asOfTime,
                                                    int level)
    {
        String methodName = "callgetEntityNeighborhood";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        SubjectAreaOMASAPIResponse response = null;
        


        try {
            InstanceGraph instanceGraph  = getOMRSMetadataCollection(restAPIName).getEntityNeighborhood(userId,
                    entityGUID,
                    entityTypeGUIDs,
                    relationshipTypeGUIDs,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    level) ;
            return new InstanceGraphResponse(instanceGraph);
        } catch (UserNotAuthorizedException e)
        {
           response =  this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
           response =  this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e)
        {
            // check to see if the method is not implemented. in this case do not error.
            String method_not_implemented_msg_id = OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition().getMessageId();
            if (!e.getReportedErrorMessage().startsWith(method_not_implemented_msg_id)) {
               response =  this.errorHandler.handleRepositoryError(e, restAPIName, serverName, serviceName);
            }
        } catch (TypeErrorException e)
        {
           response =  this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (EntityNotKnownException e)
        {
           response =  this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (InvalidParameterException e)
        {
           response =  this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (PropertyErrorException e)
        {
           response =  this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (FunctionNotSupportedException e)
        {
           response =  this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        return response;
    }


    public SubjectAreaOMASAPIResponse findEntitiesByPropertyValue(String restAPIName, String userId, String typeName, String searchCriteria, Date asOfTime, Integer offset, Integer pageSize, org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder, String sequencingProperty, String methodName) {
                  // if offset or pagesize were not supplied then default them, so they can be converted to primitives.
        if (offset == null) {
            offset = new Integer(0);
        }
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
        if (sequencingProperty !=null) {
            try {
                sequencingProperty = URLDecoder.decode(sequencingProperty,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO error
            }
        }
        if (searchCriteria !=null) {
            try {
                searchCriteria = URLDecoder.decode(searchCriteria,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO error
            }
        }

        SequencingOrder omrsSequencingOrder =  SubjectAreaUtils.convertOMASToOMRSSequencingOrder(sequencingOrder);
        TypeDef typeDef = this.omrsRepositoryHelper.getTypeDefByName("findEntitiesByPropertyValue",typeName);
        String entityTypeGUID = typeDef.getGUID();
        return  this.callFindEntitiesByPropertyValue(
                restAPIName,
                userId,
                entityTypeGUID,
                searchCriteria,
                offset.intValue(),
                null,       // TODO limit by status ?
                null,  // TODO limit by classification ?
                asOfTime,
                sequencingProperty,
                omrsSequencingOrder,
                pageSize);
    }
    public SubjectAreaOMASAPIResponse getEntitiesByType(OMRSAPIHelper oMRSAPIHelper,
                                                               String restAPIName,
                                                               String userId,
                                                               String typeName,
                                                               Date asOfTime,
                                                               Integer offset,
                                                               Integer pageSize
                                                               ) {
        if (offset == null) {
            offset = new Integer(0);
        }
        if (pageSize == null) {
            pageSize = new Integer(0);
        }
      
        TypeDef typeDef = this.omrsRepositoryHelper.getTypeDefByName("getEntitiesByType",typeName);
        String entityTypeGUID = typeDef.getGUID();
        return oMRSAPIHelper.callGetEntitiesByType(
                restAPIName,
                userId,
                entityTypeGUID,
                asOfTime,
                offset,
                pageSize);
    }
}