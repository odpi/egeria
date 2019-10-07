/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.common;

import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
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
//    private ErrorHandler errorHandler=null;
    private String serviceName="Subject Area OMAS";
    private String serverName = null;
    private OMRSRepositoryHelper omrsRepositoryHelper  = null;

    public OMRSMetadataCollection getOMRSMetadataCollection()  {
        //validateInitialization();
        return oMRSMetadataCollection;
    }
    public OMRSRepositoryHelper getOMRSRepositoryHelper() {
        return omrsRepositoryHelper;
    }

    /**
     * Set the OMRS repository connector
     * @param connector connector cannot be null
     * @throws RepositoryErrorException repository error
     */
    public void setOMRSRepositoryConnector(OMRSRepositoryConnector connector) throws RepositoryErrorException {
        //TODO pass the API name down the call stack
        String restAPIName ="";
        String methodName = "setOMRSRepositoryConnector";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + "connector="+connector);
        }

            this.oMRSMetadataCollection = connector.getMetadataCollection();
            this.omrsRepositoryHelper = connector.getRepositoryHelper();
        

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
    }

    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     */
//    private void validateInitialization()  {
//        String restAPIName= "";
//        if (oMRSMetadataCollection == null) {
//            if (this.omrsRepositoryHelper == null) {
//                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
//                String errorMessage = errorCode.getErrorMessageId()
//                        + errorCode.getFormattedErrorMessage(restAPIName);
//
//                throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
//                        this.getClass().getName(),
//                        restAPIName,
//                        errorMessage,
//                        errorCode.getSystemAction(),
//                        errorCode.getUserAction());
//            }
//        }
//    }

    public OMRSAPIHelper() {
    }
    // types
    public TypeDefGallery callGetAllTypes(String userId) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException


    {
        String methodName = "callGetAllTypes";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        TypeDefGallery typeDefGallery = getOMRSMetadataCollection().getAllTypes(userId);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return typeDefGallery;
    }

    public TypeDef callGetTypeDefByName(String userId, String typeName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException {
        String methodName = "callGetTypeDefByName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
             //TODO cascade
        String restAPIName= "";


        TypeDef typeDef= getOMRSMetadataCollection().getTypeDefByName(userId,typeName);

        return typeDef;
    }

    // entity CRUD
    public EntityDetail callOMRSAddEntity(String userId, EntityDetail entityDetail) throws ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        String methodName = "callOMRSAddEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  addedEntityDetail=null;


        InstanceProperties instanceProperties = entityDetail.getProperties();

            addedEntityDetail=getOMRSMetadataCollection().addEntity(userId, entityDetail.getType().getTypeDefGUID(), instanceProperties, entityDetail.getClassifications(), InstanceStatus.ACTIVE);

        return addedEntityDetail;
    }
    public EntityDetail callOMRSGetEntityByGuid(String userId, String entityGUID) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {

        String methodName = "callOMRSGetEntityByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  gotEntityDetail = getOMRSMetadataCollection().getEntityDetail(userId, entityGUID);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return gotEntityDetail;


    }




    public List<EntityDetail> callFindEntitiesByPropertyValue(String               userId,
                                                         String                    entityTypeGUID,
                                                         String                    searchCriteria ,
                                                         int                       fromEntityElement,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         List<String>              limitResultsByClassification,
                                                         Date                      asOfTime,
                                                         String                    sequencingProperty,
                                                         SequencingOrder sequencingOrder,
                                                         int                       pageSize) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        String methodName = "callFindEntitiesByPropertyValue";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }

        //TODO cascade
        String restAPIName= "";

        List<EntityDetail>  foundEntities = null;



            foundEntities = getOMRSMetadataCollection().findEntitiesByPropertyValue(userId,
                    entityTypeGUID,
                    searchCriteria,
                    fromEntityElement,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,pageSize);

        return foundEntities;
    }
    public EntityDetail callOMRSUpdateEntity(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        String methodName = "callOMRSUpdateEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail updatedEntity = null;

        InstanceProperties instanceProperties = entityDetail.getProperties();

            updatedEntity = getOMRSMetadataCollection().updateEntityProperties(userId, entityDetail.getGUID(), instanceProperties);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return updatedEntity ;
    }
    public  EntityDetail callOMRSDeleteEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, FunctionNotSupportedException, EntityNotKnownException {
        String methodName = "callOMRSDeleteEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        EntityDetail deletedEntity = getOMRSMetadataCollection().deleteEntity(userId,typeDefGuid, typeDefName, obsoleteGuid);


        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return deletedEntity;
    }
    public void callOMRSPurgeEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws UserNotAuthorizedException, EntityNotKnownException, EntityNotDeletedException, InvalidParameterException, RepositoryErrorException, FunctionNotSupportedException {
        String methodName = "callOMRSPurgeEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

            getOMRSMetadataCollection().purgeEntity(userId, typeDefGuid, typeDefName,  obsoleteGuid);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }
    public EntityDetail callOMRSRestoreEntity(String userId,String guid) throws UserNotAuthorizedException, EntityNotKnownException, EntityNotDeletedException, InvalidParameterException, RepositoryErrorException, FunctionNotSupportedException {
        // restore the Entity
        String methodName = "callOMRSRestoreEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from omas
        String restAPIName = methodName;
        EntityDetail restoredEntity = getOMRSMetadataCollection().restoreEntity(userId, guid);

        return restoredEntity;
    }

    // entity classification
    public EntityDetail callOMRSClassifyEntity(String userId,
                                               String entityGUID,
                                               String classificationName,
                                               InstanceProperties instanceProperties
    ) throws ClassificationErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        String methodName = "callOMRSClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity =  getOMRSMetadataCollection().classifyEntity(userId, entityGUID, classificationName, instanceProperties);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return entity;
    }

    public EntityDetail callOMRSDeClassifyEntity(String userId,
                                                 String entityGUID,
                                                 String classificationName
    ) throws ClassificationErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException {

        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity = null;

            entity = getOMRSMetadataCollection().declassifyEntity(userId, entityGUID, classificationName);

        return entity;
    }


    // relationship CRUD
    public Relationship callOMRSAddRelationship(String userId, Relationship relationship) throws StatusNotSupportedException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        Relationship addedRelationship = getOMRSMetadataCollection().addRelationship(userId,
                    relationship.getType().getTypeDefGUID(),
                    relationship.getProperties(),
                    relationship.getEntityOneProxy().getGUID(),
                    relationship.getEntityTwoProxy().getGUID(),
                    InstanceStatus.ACTIVE);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return addedRelationship;
    }

    public Relationship callOMRSGetRelationshipByGuid(String userId, String relationshipGUID) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, RelationshipNotKnownException {
        String methodName = "callOMRSGetRelationshipByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade from OMAS
        String restAPIName= methodName;
        Relationship relationship = getOMRSMetadataCollection().getRelationship(userId,relationshipGUID);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationship;
    }
    public Relationship callOMRSUpdateRelationship(String userId, Relationship relationship) throws StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, PropertyErrorException {
        String methodName = "callOMRSUpdateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from OMAS
        String restAPIName = methodName;
        Relationship updatedRelationship = getOMRSMetadataCollection().updateRelationshipProperties(userId,
                    relationship.getGUID(),
                    relationship.getProperties());
            if ( relationship.getStatus() !=null && updatedRelationship !=null &&
                    !relationship.getStatus().equals(updatedRelationship.getStatus())) {
                updatedRelationship = getOMRSMetadataCollection().updateRelationshipStatus(userId,
                        relationship.getGUID(),
                        relationship.getStatus());
            }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return updatedRelationship;

    }
    public Relationship callOMRSDeleteRelationship(String userId, String typeGuid, String typeName,String guid) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, FunctionNotSupportedException, RelationshipNotKnownException {
        // delete the relationship
        String methodName = "callOMRSDeleteRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from omas
        String restAPIName = methodName;
        Relationship deletedRelationship = getOMRSMetadataCollection().deleteRelationship(userId, typeGuid, typeName, guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return deletedRelationship;
    }
    public Relationship callOMRSRestoreRelationship(String userId,String guid) throws RelationshipNotDeletedException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException {
        // restore the relationship
        String methodName = "callOMRSRestoreRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from omas
        String restAPIName = methodName;
        Relationship restoredRelationship = getOMRSMetadataCollection().restoreRelationship(userId, guid);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return restoredRelationship;
    }
    public void callOMRSPurgeRelationship(String userId, String typeGuid, String typeName,String guid) throws RelationshipNotDeletedException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException {

        // delete the relationship
        String methodName = "callOMRSPurgeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade from OMAS
        String restAPIName = methodName;
        getOMRSMetadataCollection().purgeRelationship(userId, typeGuid, typeName, guid);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }
    public List<Relationship> callGetRelationshipsForEntity(String                     userId,
                                                            String                     entityGUID,
                                                            String                     relationshipTypeGuid,
                                                            int                        fromRelationshipElement,
                                                            Date                       asOfTime,
                                                            String                     sequencingProperty,
                                                            SequencingOrder            sequencingOrder,
                                                            int                        pageSize
                                                            ) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        List<Relationship> relationships =  getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,
                   relationshipTypeGuid,
                    fromRelationshipElement,
                    statusList,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,
                    pageSize
            );

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
                                                            int                        pageSize) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        List<Relationship> relationships=  getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,relationshipTypeGUID,fromRelationshipElement,limitResultsByStatus,asOfTime,
                    sequencingProperty,sequencingOrder,pageSize);

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationships;
    }



    // type
    public String callGetTypeGuid(String userId, String typeName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException {
        String methodName = "callgetTypeGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        String typeDefGuid = null;

            TypeDef typeDef = getOMRSMetadataCollection().getTypeDefByName(userId, typeName);
            typeDefGuid = typeDef.getGUID().toString();


        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return typeDefGuid;
    }
    public InstanceGraph callGetEntityNeighbourhood(String userId, String entityGUID,
                                                    List<String> entityTypeGUIDs,
                                                    List<String> relationshipTypeGUIDs,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    List<String> limitResultsByClassification,
                                                    Date asOfTime,
                                                    int level) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        String methodName = "callgetEntityNeighborhood";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = methodName;
        InstanceGraph instanceGraph  = getOMRSMetadataCollection().getEntityNeighborhood(userId,
                    entityGUID,
                    entityTypeGUIDs,
                    relationshipTypeGUIDs,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    level) ;

        return instanceGraph;
    }


    public static List<EntityDetail> findEntitiesByType(OMRSAPIHelper oMRSAPIHelper, String serverName, String userId, String type, String searchCriteria, Date asOfTime, Integer offset, Integer pageSize, SequencingOrder sequencingOrder, String sequencingProperty, String methodName) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
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
        OpenMetadataTypesArchiveAccessor archiveAccessor = OpenMetadataTypesArchiveAccessor.getInstance();
        TypeDef                          typeDef         =archiveAccessor.getEntityDefByName(type);
        String                           entityTypeGUID  = typeDef.getGUID();
        return oMRSAPIHelper.callFindEntitiesByPropertyValue(
                userId,
                entityTypeGUID,
                searchCriteria,
                offset.intValue(),
                null,       // TODO limit by status ?
                null,  // TODO limit by classification ?
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }
}