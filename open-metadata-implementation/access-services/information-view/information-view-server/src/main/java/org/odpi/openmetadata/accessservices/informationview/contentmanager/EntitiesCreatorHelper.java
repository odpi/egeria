/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntitiesCreatorHelper {

    private static final Logger log = LoggerFactory.getLogger(EntitiesCreatorHelper.class);
    private static final Integer PAGE_SIZE = 0;
    private OMRSRepositoryConnector enterpriseConnector;
    private OMRSAuditLog auditLog;

    public EntitiesCreatorHelper(OMRSRepositoryConnector enterpriseConnector, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
    }

    /**
     * Returns the newly created entity with the specified properties
     *
     * @param sourceName
     * @param metadataCollectionId unique identifier for the metadata collection used for adding entities
     * @param userName             name of the user performing the add operation
     * @param typeName             of the entity type def
     * @param instanceProperties   specific to the entity
     * @return the new entity added to the metadata collection
     * @throws ClassificationErrorException
     * @throws StatusNotSupportedException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     */
    private EntityDetail addEntity(String metadataCollectionId,
                                   String userName,
                                   String typeName,
                                   InstanceProperties instanceProperties,
                                   List<Classification> classifications,
                                   String sourceName) throws ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        EntityDetail entity;
        try {
            entity = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonEntity(sourceName,
                            metadataCollectionId,
                            InstanceProvenanceType.LOCAL_COHORT,
                            userName,
                            typeName);
            entity.setClassifications(classifications);
            return enterpriseConnector.getMetadataCollection()
                    .addEntity(userName,
                            entity.getType().getTypeDefGUID(),
                            instanceProperties,
                            entity.getClassifications(),
                            entity.getStatus());
        } catch (Exception e) {

            InformationViewErrorCode auditCode = InformationViewErrorCode.ADD_ENTITY_EXCEPTION;
            auditLog.logException("addEntity",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(typeName, e.getMessage()),
                    "entity of type{" + typeName + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw e;
        }
    }

    /**
     * Returns the newly created relationship between 2 entities with the specified properties
     *
     * @param metadataCollectionId unique identifier for the metadata collection used for adding relationships
     * @param typeName             name of the relationship type def
     * @param initialProperties    properties for the relationship
     * @param entityOneGUID        giud of the first end of the relationship
     * @param entityTwoGUID        giud of the second end of the relationship
     * @return the created relationship
     * @throws StatusNotSupportedException
     * @throws UserNotAuthorizedException
     * @throws EntityNotKnownException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     */
    private Relationship addRelationship(String metadataCollectionId,
                                         String typeName,
                                         InstanceProperties initialProperties,
                                         String entityOneGUID,
                                         String entityTwoGUID) throws StatusNotSupportedException, UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {

        Relationship relationship;
        try {
            relationship = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonRelationship("",
                            metadataCollectionId,
                            InstanceProvenanceType.LOCAL_COHORT,
                            Constants.USER_ID,
                            typeName);
            return enterpriseConnector.getMetadataCollection()
                    .addRelationship(Constants.USER_ID,
                            relationship.getType().getTypeDefGUID(),
                            initialProperties,
                            entityOneGUID,
                            entityTwoGUID,
                            InstanceStatus.ACTIVE);
        } catch (Exception e) {

            InformationViewErrorCode auditCode = InformationViewErrorCode.ADD_RELATIONSHIP_EXCEPTION;
            auditLog.logException("addRelationship",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(typeName, e.getMessage()),
                    "relationship of type{" + typeName + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            throw e;
        }

    }

    /**
     * Returns the entity of the specified type retrieved based on qualified name
     *
     * @param typeName      name of the type def for the entity to be retrieved
     * @param qualifiedName qualified name property of the entity to be retrieved
     * @return the existing entity with the given qualified name or null if it doesn't exist
     */
    public EntityDetail getEntity(String typeName, String qualifiedName)  {
        InstanceProperties matchProperties = buildMatchingInstanceProperties(Constants.QUALIFIED_NAME, qualifiedName);
        TypeDef typeDef = enterpriseConnector.getRepositoryHelper().getTypeDefByName(Constants.USER_ID, typeName);
        List<EntityDetail> existingEntities;
        try {
            existingEntities = enterpriseConnector.getMetadataCollection()
                    .findEntitiesByProperty(Constants.USER_ID,
                            typeDef.getGUID(),
                            matchProperties,
                            MatchCriteria.ALL,
                            0,
                            Collections.singletonList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            SequencingOrder.ANY,
                            PAGE_SIZE);
            return checkEntities(existingEntities, qualifiedName);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
            auditLog.logException("getEntity",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage("qualifiedName: " + qualifiedName, e.getMessage()),
                    "entity with properties{" + matchProperties + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            return null;
        }

    }


    public EntityDetail getEntityByGuid(String guid) throws Exception {
        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guid);

    }

    /**
     * Returns the entity filtered out from entities list based on qualified name
     *
     * @param existingEntities the list of entities to search in
     * @param qualifiedName    qualified name based on which the entity is retrieved
     * @return the entity that has the specified qualified name
     */
    private EntityDetail checkEntities(List<EntityDetail> existingEntities, String qualifiedName) {
        if (existingEntities != null && !existingEntities.isEmpty())
            return existingEntities.stream().filter(e -> qualifiedName.equals(EntityPropertiesUtils.getStringValueForProperty(e.getProperties(), Constants.QUALIFIED_NAME))).findFirst().orElse(null);
        return null;
    }

    /**
     * Returns the relationship of the given type between the 2 entities
     *
     * @param relationshipType is the name of the relationship type
     * @param guid1            is guid of first end of relationship
     * @param guid2            is guid of the second end f relationship
     * @return the relationship of the given type between the two entities; null if it doesn't exist
     */
    private Relationship getRelationship(String relationshipType,
                                         String guid1,
                                         String guid2)  {
        List<Relationship> relationships;
        try {
            relationships = getRelationships(relationshipType, guid2);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            auditLog.logException("getRelationship",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(relationshipType, e.getMessage()),
                    "relationship with type" + relationshipType + " between {" + guid1 + ", " + guid2 + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            return null;
        }
        if (relationships != null && !relationships.isEmpty())
            for (Relationship relationship : relationships) {
                if (relationship.getType().getTypeDefName().equals(relationshipType) && checkRelationshipEnds(relationship,
                        guid1,
                        guid2))
                    return relationship;
            }
        return null;
    }

    public List<Relationship> getRelationships(String relationshipType, String guid2)  {
        List<Relationship> relationships = new ArrayList<>();
        String relationshipTypeGuid = enterpriseConnector.getRepositoryHelper()
                .getTypeDefByName(Constants.USER_ID, relationshipType)
                .getGUID();
        try {
            relationships = enterpriseConnector.getMetadataCollection()
                    .getRelationshipsForEntity(Constants.USER_ID,
                            guid2,
                            relationshipTypeGuid,
                            0,
                            Collections.singletonList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            0);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            auditLog.logException("getRelationships",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(relationshipType, e.getMessage()),
                    "relationship with type" + relationshipType + " gor {" + guid2 + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
        return relationships;
    }

    /**
     * Returns true if the provided relationship is between the 2 specified entities
     *
     * @param relationship - the relationship instance to be checked
     * @param guid1        is the guid of one end
     * @param guid2        is the guid of the second end
     * @return boolean
     */
    private boolean checkRelationshipEnds(Relationship relationship, String guid1, String guid2) {
        String end1Guid = relationship.getEntityOneProxy().getGUID();
        String end2Guid = relationship.getEntityTwoProxy().getGUID();
        return (end1Guid.equals(guid1) && end2Guid.equals(guid2)) || (end1Guid.equals(guid2) && end2Guid.equals(guid1));
    }

    /**
     * Returns the entity of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param typeName      is the entity type
     * @param qualifiedName - qualified name property of the entity, unique for the same entity type
     * @param properties    specific to the entity type
     * @return the existing entity with the given qualified name or the newly created entity with the given qualified name
     * @throws InvalidParameterException
     * @throws PropertyErrorException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     */
    public EntityDetail addEntity(String typeName,
                                  String qualifiedName,
                                  InstanceProperties properties) throws InvalidParameterException, PropertyErrorException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {
        return addEntity(typeName, qualifiedName, properties, null);
    }

    /**
     * Returns the entity of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param typeName        is the entity type
     * @param qualifiedName   - qualified name property of the entity, unique for the same entity type
     * @param properties      specific to the entity type
     * @param classifications classifications to be added to entity
     * @return the existing entity with the given qualified name or the newly created entity with the given qualified name
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     */
    public EntityDetail addEntity(String typeName,
                                  String qualifiedName,
                                  InstanceProperties properties,
                                  List<Classification> classifications) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeErrorException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException {

        EntityDetailWrapper wrapper = createOrUpdateEntity(typeName,
                qualifiedName,
                properties,
                classifications,
                false);
        return wrapper != null ? wrapper.getEntityDetail() : null;
    }

    public EntityDetailWrapper createOrUpdateEntity(String typeName,
                                                    String qualifiedName,
                                                    InstanceProperties properties,
                                                    List<Classification> classifications,
                                                    boolean update) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, ClassificationErrorException, StatusNotSupportedException, EntityNotKnownException {
        EntityDetail entityDetail;
        EntityDetailWrapper wrapper;
        entityDetail = getEntity(typeName, qualifiedName);
        if (entityDetail == null) {
            entityDetail = addEntity("", Constants.USER_ID, typeName, properties, classifications, Constants.INFORMATION_VIEW_OMAS_NAME);
            log.info("Entity with qualified name {} added", qualifiedName);
            log.info("Entity: {}", entityDetail);
            wrapper = new EntityDetailWrapper(entityDetail, EntityDetailWrapper.EntityStatus.NEW);
        } else {
            log.info("Entity with qualified name {} already exists", qualifiedName);
            log.info("Entity: {}", entityDetail);
            if (update && !EntityPropertiesUtils.matchExactlyInstanceProperties(entityDetail.getProperties(), properties)) {//TODO should add validation
                log.info("Updating entity with qualified name {} ", qualifiedName);
                entityDetail = updateEntity(entityDetail, Constants.USER_ID, properties);
            }
            wrapper = new EntityDetailWrapper(entityDetail, EntityDetailWrapper.EntityStatus.UPDATED);
        }

        return wrapper;
    }

    private EntityDetail updateEntity(EntityDetail entityDetail, String userId, InstanceProperties properties) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, EntityNotKnownException, PropertyErrorException {
        //TODO add validation to new instance properties
        entityDetail = enterpriseConnector.getMetadataCollection().updateEntityProperties(userId, entityDetail.getGUID(), properties);
        return entityDetail;
    }

    /**
     * Returns the relationship of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param relationshipType is the relationship type name
     * @param guid1            first end of the relationship
     * @param guid2            second end of the relationship
     * @param source           name of the relationship creator
     * @param properties       specific to the relationship type
     * @return the existing relationship with the given qualified name or the newly created relationship with the given qualified name
     * @throws InvalidParameterException
     * @throws TypeErrorException
     * @throws TypeDefNotKnownException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws StatusNotSupportedException
     */
    public Relationship addRelationship(String relationshipType,
                                        String guid1,
                                        String guid2,
                                        String source,
                                        InstanceProperties properties) throws InvalidParameterException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, StatusNotSupportedException {
        Relationship relationship;

        relationship = getRelationship(relationshipType, guid1, guid2);
        if (relationship == null) {
            relationship = addRelationship("", relationshipType, properties, guid1, guid2);
            log.info("Relationship {} added between: {} and {}", relationshipType, guid1, guid2);
            log.info("Relationship: {}", relationship);
        } else {
            log.info("Relationship {} already exists between: {} and {}", relationshipType, guid1, guid2);
            log.info("Relationship: {}", relationship);
        }

        return relationship;
    }


    /**
     * Returns the properties object for the given pair of key - value that can be used for retrieving
     *
     * @param key   - name of the property
     * @param value - value of the property
     * @return properties with the given key - value pair
     */
    private InstanceProperties buildMatchingInstanceProperties(String key, String value) {
        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setProperty(key,
                EntityPropertiesUtils.createPrimitiveStringPropertyValue(value));
        return instanceProperties;
    }

    public Classification buildClassification(String classificationTypeName, String entityTypeName, InstanceProperties classificationProperties) throws TypeErrorException {
        try {
            Classification classification = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonClassification("",
                            Constants.USER_ID,
                            classificationTypeName,
                            entityTypeName);
            classification.setProperties(classificationProperties);
            return classification;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            InformationViewErrorCode auditCode = InformationViewErrorCode.ADD_CLASSIFICATION;
            auditLog.logException("getClassification",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(classificationTypeName, entityTypeName, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            throw e;
        }
    }

    public void purgeRelationship(Relationship relationship) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, RelationshipNotDeletedException, RelationshipNotKnownException, FunctionNotSupportedException {
        if (relationship == null || relationship.getGUID() == null || relationship.getType() == null) {
            log.info("Nothing will be purged, invalid relationship passed as argument: {}", relationship);
        } else {
            log.info("Purge relationship with guid {}", relationship.getGUID());
            enterpriseConnector.getMetadataCollection().deleteRelationship(Constants.USER_ID, relationship.getType().getTypeDefGUID(), relationship.getType().getTypeDefName(), relationship.getGUID());
            enterpriseConnector.getMetadataCollection().purgeRelationship(Constants.USER_ID, relationship.getType().getTypeDefGUID(), relationship.getType().getTypeDefName(), relationship.getGUID());
        }
    }

    public void purgeEntity(EntitySummary entitySummary) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, EntityNotKnownException, EntityNotDeletedException, FunctionNotSupportedException {
        if (entitySummary == null || entitySummary.getGUID() == null || entitySummary.getType() == null) {
            log.info("Nothing will be purged, invalid entity passed as argument: {}", entitySummary);
        } else {
            log.info("Purge entity with guid {}", entitySummary.getGUID());
            enterpriseConnector.getMetadataCollection().deleteEntity(Constants.USER_ID, entitySummary.getType().getTypeDefGUID(), entitySummary.getType().getTypeDefName(), entitySummary.getGUID());
            enterpriseConnector.getMetadataCollection().purgeEntity(Constants.USER_ID, entitySummary.getType().getTypeDefGUID(), entitySummary.getType().getTypeDefName(), entitySummary.getGUID());
        }
    }
}
