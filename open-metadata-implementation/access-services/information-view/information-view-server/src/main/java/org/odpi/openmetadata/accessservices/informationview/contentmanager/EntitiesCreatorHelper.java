/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
     * @param metadataCollectionId unique identifier for the metadata collection used for adding entities
     * @param userName name of the user performing the add operation
     * @param typeName             of the entity type def
     * @param instanceProperties   specific to the entity
     * @return the new entity added to the metadata collection
     * @throws Exception
     */
    private EntityDetail addEntity(String metadataCollectionId,
                                   String userName,
                                   String typeName,
                                   InstanceProperties instanceProperties) throws Exception {
        EntityDetail entity;
        try {
            entity = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonEntity("",
                    metadataCollectionId,
                    InstanceProvenanceType.LOCAL_COHORT,
                    userName,
                    typeName);
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
                    auditCode.getFormattedErrorMessage(typeName),
                    "entity of type{" + typeName + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new Exception(e);
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
     * @throws Exception
     */
    private Relationship addRelationship(String metadataCollectionId,
                                         String typeName,
                                         InstanceProperties initialProperties,
                                         String entityOneGUID,
                                         String entityTwoGUID) throws Exception {

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
                    auditCode.getFormattedErrorMessage(typeName),
                    "relationship of type{" + typeName + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            throw new Exception(e);
        }

    }

    /**
     * Returns the entity of the specified type retrieved based on qualified name
     *
     * @param typeName     name of the type def for the entity to be retrieved
     * @param qualifiedName qualified name property of the entity to be retrieved
     * @return the existing entity with the given qualified name or null if it doesn't exist
     * @throws Exception
     */
    public EntityDetail getEntity(String typeName, String qualifiedName) throws Exception {
        InstanceProperties matchProperties = buildMatchingInstanceProperties(Constants.QUALIFIED_NAME, qualifiedName);
        TypeDef typeDef = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, typeName);
        List<EntityDetail> existingEntities;
        try {
            existingEntities = enterpriseConnector.getMetadataCollection()
                    .findEntitiesByProperty(Constants.USER_ID,
                            typeDef.getGUID(),
                            matchProperties,
                            MatchCriteria.ALL,
                            0,
                            Arrays.asList(InstanceStatus.ACTIVE),
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
                    auditCode.getFormattedErrorMessage(qualifiedName),
                    "entity with properties{" + matchProperties + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            throw new Exception(e);
        }

    }

    /**
     *Returns the entity filtered out from entities list based on qualified name
     *
     * @param existingEntities the list of entities to search in
     * @param qualifiedName qualified name based on which the entity is retrieved
     * @return the entity that has the specified qualified name
     */
    private EntityDetail checkEntities(List<EntityDetail> existingEntities, String qualifiedName) {
        if (existingEntities != null)
            return existingEntities.stream().filter(e -> qualifiedName.equals(EntityPropertiesUtils.getStringValueForProperty(e.getProperties(), Constants.QUALIFIED_NAME))).findFirst().get();
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
                                         String guid2) throws Exception {
        List<Relationship> relationships;
        try {
            String relationshipTypeGuid = enterpriseConnector.getMetadataCollection()
                    .getTypeDefByName(Constants.USER_ID, relationshipType)
                    .getGUID();
            relationships = enterpriseConnector.getMetadataCollection()
                    .getRelationshipsForEntity(Constants.USER_ID,
                            guid2,
                            relationshipTypeGuid,
                            0,
                            Arrays.asList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            0);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            auditLog.logException("getRelationship",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(relationshipType),
                    "relationship with type" + relationshipType + " between {" + guid1 + ", " + guid2 + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new Exception(e);
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

    /**
     * Returns true if the provided relationship is between the 2 specified entities
     *
     * @param relationship - the relationship instance to be checked
     * @param guid1        is the guid of one end
     * @param guid2        is the guid of the second end
     * @return
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
     * @throws Exception
     */
    public EntityDetail addEntity(String typeName,
                                  String qualifiedName,
                                  InstanceProperties properties) throws Exception {
        EntityDetail entityDetail;

        entityDetail = getEntity(typeName, qualifiedName);
        if (entityDetail == null) {
            entityDetail = addEntity("", Constants.USER_ID, typeName, properties);
            log.info("Entity with qualified name {} added", qualifiedName);
            log.info("Entity: {}", entityDetail);
        } else {
            log.info("Entity with qualified name {} already exists", qualifiedName);
            log.info("Entity: {}", entityDetail);
        }


        return entityDetail;
    }

    /**
     *  Returns the relationship of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param relationshipType is the relationship type name
     * @param guid1            first end of the relationship
     * @param guid2            second end of the relationship
     * @param source           name of the relationship creator
     * @param properties       specific to the relationship type
     * @return the existing relationship with the given qualified name or the newly created relationship with the given qualified name
     * @throws Exception
     */
    public Relationship addRelationship(String relationshipType,
                                        String guid1,
                                        String guid2,
                                        String source,
                                        InstanceProperties properties) throws Exception {
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
     * @param key - name of the property
     * @param value - value of the property
     * @return properties with the given key - value pair
     */
    private InstanceProperties buildMatchingInstanceProperties(String key, String value) {
        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties.setProperty(key,
                EntityPropertiesUtils.createPrimitiveStringPropertyValue(value));
        return instanceProperties;
    }


}
