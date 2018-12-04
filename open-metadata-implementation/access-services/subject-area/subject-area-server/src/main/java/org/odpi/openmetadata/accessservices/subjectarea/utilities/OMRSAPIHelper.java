/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.StatusNotSupportedException;
import org.odpi.openmetadata.accessservices.subjectarea.server.handlers.ErrorHandler;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private String serviceName="Subject Area OMAS";
    private String serverName = null;
    private OMRSRepositoryConnector omrsConnector = null;

    public OMRSMetadataCollection getOMRSMetadataCollection() throws MetadataServerUncontactableException {
        validateInitialization();
        return oMRSMetadataCollection;
    }
    public OMRSRepositoryHelper getOMRSRepositoryHelper() {
        return omrsConnector.getRepositoryHelper();
    }
    public void setOMRSRepositoryConnector(OMRSRepositoryConnector connector) throws MetadataServerUncontactableException {
        //TODO pass the API name down the call stack
        String restAPIName ="";
        String methodName = "setOMRSRepositoryConnector";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + "connector="+connector);
        }

        if (connector==null ) {
            // TODO error
        }

        try {
            this.oMRSMetadataCollection = connector.getMetadataCollection();
        } catch ( org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.METADATA_SERVER_UNCONTACTABLE_ERROR;
            String                 errorMessage = errorCode.getErrorMessageId()
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
     */
    private void validateInitialization() throws MetadataServerUncontactableException {
        String restAPIName= "";
        if (oMRSMetadataCollection == null) {
            if (this.omrsConnector == null) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(restAPIName);

                throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        restAPIName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {
                this.setOMRSRepositoryConnector(this.omrsConnector);
            }
        }
    }

    public OMRSAPIHelper() {
    }
    // types
    public TypeDefGallery callGetAllTypes(String userId)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            MetadataServerUncontactableException

    {
        String methodName = "callGetAllTypes";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        TypeDefGallery typeDefGallery=null;

        try {
            typeDefGallery= getOMRSMetadataCollection().getAllTypes(userId);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return typeDefGallery;
    }

    public TypeDef callGetTypeDefByName(String userId, String typeName)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            MetadataServerUncontactableException {
        String methodName = "callGetTypeDefByName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        TypeDef typeDef =null;
        //TODO cascade
        String restAPIName= "";

        try {
            typeDef= getOMRSMetadataCollection().getTypeDefByName(userId,typeName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException e) {
            this.errorHandler.handleTypeDefNotKnownException(typeName,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return typeDef;
    }

    // entity CRUD
    public EntityDetail callOMRSAddEntity(String userId, EntityDetail entityDetail)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            ClassificationException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.StatusNotSupportedException,
            MetadataServerUncontactableException {
        String methodName = "callOMRSAddEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  addedEntityDetail=null;


        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            addedEntityDetail=getOMRSMetadataCollection().addEntity(userId, entityDetail.getType().getTypeDefGUID(), instanceProperties, entityDetail.getClassifications(), InstanceStatus.ACTIVE);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
            this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return addedEntityDetail;
    }
    public EntityDetail callOMRSGetEntityByGuid(String userId, String entityGUID)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {

        String methodName = "callOMRSGetEntityByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  gotEntityDetail=null;

        try {
            gotEntityDetail=  getOMRSMetadataCollection().getEntityDetail(userId, entityGUID);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e) {
            this.errorHandler.handleEntityProxyOnlyException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return gotEntityDetail;


    }

    public List<EntityDetail> callFindEntitiesByProperty(String             userId,
                                                         String                    entityTypeGUID,
                                                         InstanceProperties        matchProperties,
                                                         MatchCriteria matchCriteria,
                                                         int                       fromEntityElement,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         List<String>              limitResultsByClassification,
                                                         Date                      asOfTime,
                                                         String                    sequencingProperty,
                                                         SequencingOrder sequencingOrder,
                                                         int                       pageSize)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            MetadataServerUncontactableException {

        String methodName = "callFindEntitiesByProperty";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }

        //TODO cascade
        String restAPIName= "";

        List<EntityDetail>  foundEntities = null;


        try {
            foundEntities =  getOMRSMetadataCollection().findEntitiesByProperty(userId,
                    entityTypeGUID,
                    matchProperties,
                    matchCriteria,
                    fromEntityElement,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,pageSize);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return foundEntities;
    }

    public EntityDetail callOMRSUpdateEntity(String userId, EntityDetail entityDetail) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        String methodName = "callOMRSUpdateEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail updatedEntity = null;

        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            updatedEntity = getOMRSMetadataCollection().updateEntityProperties(userId, entityDetail.getGUID(), instanceProperties);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityDetail.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return updatedEntity ;
    }
    public  EntityDetail callOMRSDeleteEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        String methodName = "callOMRSDeleteEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        EntityDetail deletedEntity =null;

        try {
            deletedEntity   = getOMRSMetadataCollection().deleteEntity(userId,typeDefGuid, typeDefName, obsoleteGuid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return deletedEntity;
    }
    public void callOMRSPurgeEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, MetadataServerUncontactableException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,  UnrecognizedGUIDException, GUIDNotPurgedException {
        String methodName = "callOMRSPurgeEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        try {
            getOMRSMetadataCollection().purgeEntity(userId, typeDefGuid, typeDefName,  obsoleteGuid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e) {
            this.errorHandler.handleEntityNotDeletedException(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }

    // entity classification
    public EntityDetail callOMRSClassifyEntity(String userId,
                                               String entityGUID,
                                               String classificationName,
                                               InstanceProperties instanceProperties
    ) throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            ClassificationException, UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        String methodName = "callOMRSClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity = null;
        try {
            entity = getOMRSMetadataCollection().classifyEntity(userId, entityGUID, classificationName, instanceProperties);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return entity;
    }

    public EntityDetail callOMRSDeClassifyEntity(String userId,
                                                 String entityGUID,
                                                 String classificationName
    ) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            ClassificationException, UnrecognizedGUIDException,
            MetadataServerUncontactableException {

        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity = null;
        try {
            entity = getOMRSMetadataCollection().declassifyEntity(userId, entityGUID, classificationName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return entity;
    }


    // relationship CRUD
    public Relationship callOMRSAddRelationship(String userId, Relationship relationship) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException,
            StatusNotSupportedException {
        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        Relationship addedRelationship =null;
        try {
            addedRelationship =getOMRSMetadataCollection().addRelationship(userId,
                    relationship.getType().getTypeDefGUID(),
                    relationship.getProperties(),
                    relationship.getEntityOneProxy().getGUID(),
                    relationship.getEntityTwoProxy().getGUID(),
                    InstanceStatus.ACTIVE);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
            this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            //TODO this is the wrong guid. We should pass the entity guid that is not found. But that is not yet in the the Excpetion we get

            this.errorHandler.handleEntityNotKnownError(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return addedRelationship;
    }

    public Relationship callOMRSGetRelationshipByGuid(String userId, String relationshipGUID)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        String methodName = "callOMRSGetRelationshipByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade from OMAS
        String restAPIName= methodName;
        Relationship relationship =null;
        try {
            relationship = getOMRSMetadataCollection().getRelationship(userId,relationshipGUID);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(relationshipGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationship;
    }
    public Relationship callOMRSUpdateRelationship(String userId, Relationship relationship)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            MetadataServerUncontactableException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.StatusNotSupportedException,
            UnrecognizedGUIDException {
        String methodName = "callOMRSUpdateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from OMAS
        String restAPIName = methodName;
        Relationship updatedRelationship = null;
        // update the relationship properties
        try {
            updatedRelationship = getOMRSMetadataCollection().updateRelationshipProperties(userId,
                    relationship.getGUID(),
                    relationship.getProperties());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        // update the status if we have one and it is different
        if ( relationship.getStatus() !=null &&
             !relationship.getStatus().equals(updatedRelationship.getStatus())) {
            try {
                updatedRelationship = getOMRSMetadataCollection().updateRelationshipStatus(userId,
                        relationship.getGUID(),
                        relationship.getStatus());
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
                this.errorHandler.handleInvalidParameterException(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
                this.errorHandler.handleRepositoryError(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
                this.errorHandler.handleUnauthorizedUser(userId,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
                this.errorHandler.handleStatusNotSupportedException(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
                this.errorHandler.handleRelationshipNotKnownException(relationship.getGUID(),
                        restAPIName,
                        serverName,
                        serviceName);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return updatedRelationship;

    }
    public Relationship callOMRSDeleteRelationship(String userId, String typeGuid, String typeName,String guid)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        // delete the relationship
        String methodName = "callOMRSDeleteRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from omas
        String restAPIName = methodName;
        Relationship deletedRelationship = null;
        try {
            deletedRelationship =getOMRSMetadataCollection().deleteRelationship(userId, typeGuid, typeName, guid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return deletedRelationship;
    }
    public void callOMRSPurgeRelationship(String userId, String typeGuid, String typeName,String guid)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            GUIDNotPurgedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {

        // delete the relationship
        String methodName = "callOMRSPurgeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from OMAS
        String restAPIName = methodName;
        Relationship updatedRelationship = null;
        try {
            getOMRSMetadataCollection().purgeRelationship(userId, typeGuid, typeName, guid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException e) {
            this.errorHandler.handleRelationshipNotDeletedException(e,
                    restAPIName,
                    serverName,
                    serviceName,guid);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }
    public List<Relationship> callGetRelationshipsForEntity(String                     userId,
                                                            String                     entityGUID)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {

        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        Relationship updatedRelationship = null;
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        List<Relationship> relationships = null;
        try {
            relationships = getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,
                    null,
                    0,
                    statusList,
                    null,
                    null,
                    null,
                    0
            );
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return relationships;
    }

    public List<Relationship> callGetRelationshipsForEntity(String           userId,
                                                            String                     entityGUID,
                                                            String                     relationshipTypeGUID,
                                                            int                        fromRelationshipElement,
                                                            List<InstanceStatus>       limitResultsByStatus,
                                                            Date                       asOfTime,
                                                            String                     sequencingProperty,
                                                            SequencingOrder            sequencingOrder,
                                                            int                        pageSize) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException,
            UnrecognizedGUIDException,
            MetadataServerUncontactableException {
        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        List<Relationship> relationships= null;
        try {
            relationships =  getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,relationshipTypeGUID,fromRelationshipElement,limitResultsByStatus,asOfTime,
                    sequencingProperty,sequencingOrder,pageSize);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationships;
    }

    // type
    public String callGetTypeGuid(String userId, String typeName) throws
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException,
            org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,
            MetadataServerUncontactableException {
        String methodName = "callgetTypeGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        TypeDef typeDef = null;
        try {
            typeDef = getOMRSMetadataCollection().getTypeDefByName(userId, typeName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (TypeDefNotKnownException e) {
            this.errorHandler.handleTypeDefNotKnownException(typeName,
                    restAPIName,
                    serverName,
                    serviceName);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return typeDef.getGUID().toString();
    }
    public InstanceGraph callGetEntityNeighbourhood(String userId, String entityGUID, List<String> entityTypeGUIDs,
                                                    List<String> relationshipTypeGUIDs,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    List<String> limitResultsByClassification,
                                                    Date asOfTime,
                                                    int level) throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, MetadataServerUncontactableException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, UnrecognizedGUIDException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException
    {
        String methodName = "callgetEntityNeighborhood";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        InstanceGraph instanceGraph = null;
        try {
            instanceGraph  = getOMRSMetadataCollection().getEntityNeighborhood(userId,
                    entityGUID,
                    entityTypeGUIDs,
                    relationshipTypeGUIDs,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    level) ;
        } catch (UserNotAuthorizedException e)
        {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (MetadataServerUncontactableException e)
        {
            this.errorHandler.handleMetadataServerUnContactable(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e)
        {
            // check to see if the method is not implemented. in this case do not error.
            String method_not_implemented_msg_id = OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getErrorMessageId();
            if (!e.getErrorMessage().startsWith(method_not_implemented_msg_id)) {
                this.errorHandler.handleRepositoryError(e, restAPIName, serverName, serviceName);
            }
        } catch (TypeErrorException e)
        {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (EntityNotKnownException e)
        {
            this.errorHandler.handleEntityNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (InvalidParameterException e)
        {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (PropertyErrorException e)
        {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (FunctionNotSupportedException e)
        {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        return instanceGraph;
    }
}