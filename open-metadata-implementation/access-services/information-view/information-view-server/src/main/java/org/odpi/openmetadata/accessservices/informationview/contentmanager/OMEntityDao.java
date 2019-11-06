/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.reports.ReportBasicOperation;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildAddEntityRelationship;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildAddRelationshipException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildDeleteRelationshipException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildEntityNotFoundException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildIllegalUpdateEntityException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildRetrieveEntityException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildRetrieveRelationshipException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildUpdateEntityException;
import static org.odpi.openmetadata.accessservices.informationview.utils.Constants.PAGE_SIZE;

public class OMEntityDao {

    private static final Logger log = LoggerFactory.getLogger(OMEntityDao.class);
    private final OMRSRepositoryConnector enterpriseConnector;
    private final OMRSAuditLog auditLog;
    private List<String> supportedZones;

    public OMEntityDao(OMRSRepositoryConnector enterpriseConnector, List<String> supportedZones, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
        this.supportedZones = supportedZones;
    }

    /**
     * Returns the newly created entity with the specified properties
     *
     * @param metadataCollectionId unique identifier for the metadata collection used for adding entities
     * @param userName             name of the user performing the add operation
     * @param typeName             of the entity type def
     * @param instanceProperties   specific to the entity
     * @param zoneRestricted
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
                                   boolean zoneRestricted) throws ClassificationErrorException, StatusNotSupportedException,
                                                                  UserNotAuthorizedException, InvalidParameterException,
                                                                  RepositoryErrorException, PropertyErrorException,
                                                                  TypeErrorException, FunctionNotSupportedException {
        EntityDetail entity;
            entity = enterpriseConnector.getRepositoryHelper().getSkeletonEntity(Constants.INFORMATION_VIEW_OMAS_NAME,
                                                                                metadataCollectionId,
                                                                                InstanceProvenanceType.LOCAL_COHORT,
                                                                                userName,
                                                                                typeName);
            entity.setClassifications(classifications);
            if(zoneRestricted && supportedZones != null && !supportedZones.isEmpty()){
                instanceProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, instanceProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "addEntity");
            }
            return enterpriseConnector.getMetadataCollection().addEntity(userName,
                                                                        entity.getType().getTypeDefGUID(),
                                                                        instanceProperties,
                                                                        entity.getClassifications(),
                                                                        entity.getStatus());
    }

    /**
     * Returns the newly created relationship between 2 entities with the specified properties
     *
     * @param metadataCollectionId unique identifier for the metadata collection used for adding relationships
     * @param typeName             name of the relationship type def
     * @param instanceProperties    properties for the relationship
     * @param entityOneGUID        giud of the first end of the relationship
     * @param entityTwoGUID        giud of the second end of the relationship
     * @return the created relationship
     */
    private Relationship addRelationship(String metadataCollectionId,
                                         String typeName,
                                         InstanceProperties instanceProperties,
                                         String entityOneGUID,
                                         String entityTwoGUID) {

        Relationship relationship = null;
        try {
            relationship = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonRelationship(Constants.INFORMATION_VIEW_OMAS_NAME,
                            metadataCollectionId,
                            InstanceProvenanceType.LOCAL_COHORT,
                            Constants.INFORMATION_VIEW_USER_ID,
                            typeName);
            return enterpriseConnector.getMetadataCollection()
                    .addRelationship(Constants.INFORMATION_VIEW_USER_ID,
                            relationship.getType().getTypeDefGUID(),
                            instanceProperties,
                            entityOneGUID,
                            entityTwoGUID,
                            InstanceStatus.ACTIVE);
        } catch (StatusNotSupportedException | UserNotAuthorizedException | EntityNotKnownException | InvalidParameterException | RepositoryErrorException | PropertyErrorException | TypeErrorException | FunctionNotSupportedException e) {
            throw buildAddRelationshipException(typeName, e.getMessage(), this.getClass().getName());
        }
    }

    /**
     * Returns the entity of the specified type retrieved based on qualified name
     *
     * @param typeName      name of the type def for the entity to be retrieved
     * @param qualifiedName qualified name property of the entity to be retrieved
     * @param zoneRestricted
     * @return the existing entity with the given qualified name or null if it doesn't exist
     */
    public EntityDetail getEntity(String typeName, String qualifiedName, boolean zoneRestricted) {
        Map<String, String>  properties = new HashMap<>();
        // GDW - need to convert qualifiedName to exactMatchRegex
        String qualifiedNameRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(qualifiedName);
        properties.put(Constants.QUALIFIED_NAME, qualifiedNameRegex);
        InstanceProperties matchProperties = buildMatchingInstanceProperties(properties, zoneRestricted);
        List<EntityDetail> existingEntities;
        existingEntities = findEntities(matchProperties, typeName, Constants.START_FROM, PAGE_SIZE);
        return checkEntities(existingEntities, qualifiedName);
    }

    public List<EntityDetail> findEntities(InstanceProperties matchProperties, String typeName, int fromElement, int pageSize) {
        // GDW the matchProperties passed to this method should have already converted any exact match string
        // using the getExactMatchRegex repository helper method
        OMRSRepositoryHelper repositoryHelper = enterpriseConnector.getRepositoryHelper();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID, typeName);
        List<EntityDetail> existingEntities;
        try {
            log.debug("Retrieving entities of type {} with properties {}", typeDef.getName(),  matchProperties);
            existingEntities = enterpriseConnector.getMetadataCollection().findEntitiesByProperty(Constants.INFORMATION_VIEW_USER_ID,
                                                                                                    typeDef.getGUID(),
                                                                                                    matchProperties,
                                                                                                    MatchCriteria.ALL,
                                                                                                    fromElement,
                                                                                                    Collections.singletonList(InstanceStatus.ACTIVE),
                                                                                                    null,
                                                                                                    null,
                                                                                                    null,
                                                                                                    SequencingOrder.ANY,
                                                                                                    pageSize);
        } catch (InvalidParameterException | PropertyErrorException | TypeErrorException | FunctionNotSupportedException | UserNotAuthorizedException | RepositoryErrorException | PagingErrorException e) {
            String keys = String.join(",", matchProperties.getInstanceProperties().keySet());
            String values = matchProperties.getInstanceProperties().values().stream().map(InstancePropertyValue::valueAsString).collect(Collectors.joining(","));
            throw buildRetrieveEntityException(keys, values, e, this.getClass().getName());
        }
        return existingEntities;
    }


    public EntityDetail getEntityByGuid(String guid)  {
        EntityDetail entity = null;
        try {
            entity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID, guid);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            throw buildRetrieveEntityException(Constants.GUID, guid, e, this.getClass().getName());
        }
        if(entity == null ) {
            throw buildEntityNotFoundException(Constants.GUID, guid, null, this.getClass().getName());
        }

        return entity;
    }

    /**
     * Returns the entity filtered out from entities list based on qualified name
     *
     * @param existingEntities the list of entities to search in
     * @param qualifiedName    qualified name based on which the entity is retrieved
     * @return the entity that has the specified qualified name
     */
    private EntityDetail checkEntities(List<EntityDetail> existingEntities, String qualifiedName) {
        if (!CollectionUtils.isEmpty(existingEntities))
            return existingEntities.stream().filter(e -> qualifiedName.equals(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, e.getProperties(), "checkEntities"))).findFirst().orElse(null);
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
    private Relationship checkRelationshipExists(String relationshipType,
                                         String guid1,
                                         String guid2) {
        List<Relationship> relationships = getRelationships(relationshipType, guid2);
        if (!CollectionUtils.isEmpty(relationships)){
            return relationships.stream().filter(relationship -> relationship.getType().getTypeDefName().equals(relationshipType)
                    && checkRelationshipEnds(relationship, guid1, guid2)).findFirst().orElse(null);
        }
        return null;
    }

    public List<Relationship> getRelationships(String relationshipType, String guid) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving relationships of type {} for entity {}", relationshipType, guid);
        }
        try {
            String relationshipTypeGuid = enterpriseConnector.getRepositoryHelper()
                                                            .getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID, relationshipType)
                                                            .getGUID();

            return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID,
                                                                                        guid,
                                                                                        relationshipTypeGuid,
                                                                                        Constants.START_FROM,
                                                                                        Collections.singletonList(InstanceStatus.ACTIVE),
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        PAGE_SIZE);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw buildRetrieveRelationshipException(guid, relationshipType, e, this.getClass().getName());
        }
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
                                  InstanceProperties properties,
                                  boolean zoneRestricted) throws InvalidParameterException, PropertyErrorException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException {
        return addEntity(typeName, qualifiedName, properties, null, zoneRestricted);
    }


    /**
     * Returns the entity of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param typeName        is the entity type
     * @param qualifiedName   - qualified name property of the entity, unique for the same entity type
     * @param properties      specific to the entity type
     * @param classifications classifications to be added to entity
     * @param zoneRestricted
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
                                  List<Classification> classifications,
                                  boolean zoneRestricted) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeErrorException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException {

        OMEntityWrapper wrapper = createOrUpdateEntity(typeName,
                                                        qualifiedName,
                                                        properties,
                                                        classifications,
                                                        false,
                                                        zoneRestricted);
        return wrapper != null ? wrapper.getEntityDetail() : null;
    }

    public OMEntityWrapper createOrUpdateEntity(String typeName,
                                                String qualifiedName,
                                                InstanceProperties properties,
                                                List<Classification> classifications,
                                                boolean update,
                                                boolean zoneRestricted) throws UserNotAuthorizedException, FunctionNotSupportedException,
                                                                               InvalidParameterException, RepositoryErrorException,
                                                                               PropertyErrorException, TypeErrorException,
                                                                               PagingErrorException, ClassificationErrorException,
                                                                               StatusNotSupportedException, EntityNotKnownException {
        EntityDetail entityDetail;
        OMEntityWrapper wrapper;
        entityDetail = getEntity(typeName, qualifiedName, zoneRestricted);
        if (entityDetail == null) {
            entityDetail = addEntity("", Constants.INFORMATION_VIEW_USER_ID, typeName, properties, classifications, zoneRestricted);
            log.debug("Entity with qualified name {} added", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.NEW);
        } else {
            log.debug("Entity with qualified name {} already exists", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            if (update && !EntityPropertiesUtils.matchExactlyInstanceProperties(entityDetail.getProperties(), properties)) {//TODO should add validation
                log.debug("Updating entity with qualified name {} ", qualifiedName);
                entityDetail = updateEntity(entityDetail, Constants.INFORMATION_VIEW_USER_ID, properties, zoneRestricted);
                wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.UPDATED);
            }
            else{
                wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.EXISTING);
            }

        }

        return wrapper;
    }

    public OMEntityWrapper createOrUpdateExternalEntity(String userId,
                                                        String typeName,
                                                        String qualifiedName,
                                                        String externalSourceGuid,
                                                        String externalSourceName,
                                                        InstanceProperties properties,
                                                        List<Classification> classifications,
                                                        boolean update,
                                                        boolean zoneRestricted) {
        EntityDetail entityDetail;
        OMEntityWrapper wrapper;
        entityDetail = getEntity(typeName, qualifiedName, zoneRestricted);
        if (entityDetail == null) {
            entityDetail = addExternalEntity(userId, typeName, qualifiedName, externalSourceGuid, externalSourceName, properties, classifications, zoneRestricted);
            log.debug("Entity with qualified name {} added", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.NEW);
        } else {
            log.debug("Entity with qualified name {} already exists", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            if (update && !EntityPropertiesUtils.matchExactlyInstanceProperties(entityDetail.getProperties(), properties)) {//TODO should add validation
                log.debug("Updating entity with qualified name {} ", qualifiedName);
                if(!externalSourceGuid.equals(entityDetail.getMetadataCollectionId())){
                    throw buildIllegalUpdateEntityException(null, this.getClass().getName());
                }
                entityDetail = updateEntity(entityDetail, userId, properties,  zoneRestricted);
                wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.UPDATED);
            }
            else{
                wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.EXISTING);
            }

        }

        return wrapper;
    }



    public EntityDetail addExternalEntity(String userId,
                                          String typeName,
                                          String qualifiedName,
                                          String externalSourceGuid,
                                          String externalSourceName,
                                          InstanceProperties properties,
                                          List<Classification> classifications,
                                          boolean zoneRestricted) {
        TypeDef type = enterpriseConnector.getRepositoryHelper().getTypeDefByName(Constants.INFORMATION_VIEW_OMAS_NAME, typeName);
        if(zoneRestricted && supportedZones != null && !supportedZones.isEmpty()){
            properties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, properties, Constants.ZONE_MEMBERSHIP, supportedZones, "addEntity");
        }
        properties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, properties, Constants.QUALIFIED_NAME, qualifiedName, "addEntity");

        try {
            return enterpriseConnector.getMetadataCollection().addExternalEntity(userId,
                                                                                type.getGUID(),
                                                                                externalSourceGuid,
                                                                                externalSourceName,
                                                                                properties,
                                                                                classifications,
                                                                                InstanceStatus.ACTIVE);
        } catch (InvalidParameterException | RepositoryErrorException | TypeErrorException | PropertyErrorException | ClassificationErrorException | StatusNotSupportedException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            throw buildAddEntityRelationship(typeName, e, ReportBasicOperation.class.getName());
        }
    }




    private EntityDetail updateEntity(EntityDetail entityDetail, String userId, InstanceProperties instanceProperties, boolean zoneRestricted)  {
        //TODO add validation to new instance properties
        if(zoneRestricted){
            instanceProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, instanceProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "addEntity");
        }
        try {
            entityDetail = enterpriseConnector.getMetadataCollection().updateEntityProperties(userId, entityDetail.getGUID(), instanceProperties);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | UserNotAuthorizedException | FunctionNotSupportedException e) {
            throw buildUpdateEntityException(entityDetail.getGUID(), e, this.getClass().getName());
        }
        return entityDetail;
    }

    /**
     * Returns the relationship of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param relationshipType is the relationship type name
     * @param guid1            first end of the relationship
     * @param guid2            second end of the relationship
     * @param instanceProperties       specific to the relationship type
     * @return the existing relationship with the given qualified name or the newly created relationship with the given qualified name
     */
    public Relationship addRelationship(String relationshipType,
                                        String guid1,
                                        String guid2,
                                        InstanceProperties instanceProperties) {
        Relationship relationship = checkRelationshipExists(relationshipType, guid1, guid2);
        if (relationship == null) {
            relationship = addRelationship("", relationshipType, instanceProperties, guid1, guid2);
            log.debug("Relationship {} added between: {} and {}", relationshipType, guid1, guid2);
            if(log.isDebugEnabled()) {
                log.debug("Relationship: {}", relationship);
            }
        } else {
            log.debug("Relationship {} already exists between: {} and {}", relationshipType, guid1, guid2);
        }

        return relationship;
    }


    public Relationship addExternalRelationship(String userId,
                                                String relationshipType,
                                                String externalSourceGuid,
                                                String externalSourceName,
                                                String guid1,
                                                String guid2,
                                                InstanceProperties instanceProperties) {
         Relationship relationship = checkRelationshipExists(relationshipType, guid1, guid2);
        if (relationship == null) {
            relationship = addExternalRelationship(userId,"", relationshipType, externalSourceGuid, externalSourceName, instanceProperties, guid1, guid2 );
            log.debug("Relationship {} added between: {} and {}", relationshipType, guid1, guid2);
            if(log.isDebugEnabled()) {
                log.debug("Relationship: {}", relationship);
            }
        } else {
            log.debug("Relationship {} already exists between: {} and {}", relationshipType, guid1, guid2);
        }

        return relationship;
    }

    private Relationship addExternalRelationship(String userId,
                                                 String metadataCollectionId,
                                                 String typeName,
                                                 String registrationGuid,
                                                 String registrationQualifiedName,
                                                 InstanceProperties instanceProperties,
                                                 String entityOneGUID,
                                                 String entityTwoGUID) {

        Relationship relationship = null;
        try {
            relationship = enterpriseConnector.getRepositoryHelper().getSkeletonRelationship(Constants.INFORMATION_VIEW_OMAS_NAME,
                                                                                            metadataCollectionId,
                                                                                            InstanceProvenanceType.LOCAL_COHORT,
                                                                                            Constants.INFORMATION_VIEW_USER_ID,
                                                                                            typeName);
            return enterpriseConnector.getMetadataCollection().addExternalRelationship(userId,
                                                                                        relationship.getType().getTypeDefGUID(),
                                                                                        registrationGuid,
                                                                                        registrationQualifiedName,
                                                                                        instanceProperties,
                                                                                        entityOneGUID,
                                                                                        entityTwoGUID,
                                                                                        InstanceStatus.ACTIVE);
        } catch (UserNotAuthorizedException | StatusNotSupportedException | RepositoryErrorException | TypeErrorException | EntityNotKnownException | InvalidParameterException | PropertyErrorException | FunctionNotSupportedException e) {
            throw buildAddRelationshipException(typeName, e.getMessage(), this.getClass().getName());
        }
    }


    /**
     * Returns the properties object for the given pair of key - value that can be used for retrieving
     *
     * @param properties - all properties to use for matching
     * @param zoneRestricted
     * @return properties with the given key - value pair
     */
    public InstanceProperties buildMatchingInstanceProperties(Map<String, String> properties, boolean zoneRestricted) {
        InstanceProperties instanceProperties = new InstanceProperties();
        if(properties != null && properties.size() > 0) {
            for(Map.Entry<String, String> entry :  properties.entrySet()){
                instanceProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, instanceProperties, entry.getKey(), entry.getValue(), "throw buildMatchingInstanceProperties");
            }
        }
        if(zoneRestricted && supportedZones != null && !supportedZones.isEmpty()){
            instanceProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, instanceProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "throw buildMatchingInstanceProperties");
        }

        return instanceProperties;
    }

    public void purgeRelationships(List<Relationship> relationship) {
        Optional.ofNullable(relationship).map(Collection::stream)
                .orElseGet(Stream::empty)
                .forEach(this::purgeRelationship);
    }
    public void purgeRelationship(Relationship relationship) {
        if (relationship == null || relationship.getGUID() == null || relationship.getType() == null) {
            log.debug("Nothing will be purged, invalid relationship passed as argument: {}", relationship);
        } else {
            log.debug("Purge relationship with guid {}", relationship.getGUID());
            try {
                enterpriseConnector.getMetadataCollection().deleteRelationship(Constants.INFORMATION_VIEW_USER_ID, relationship.getType().getTypeDefGUID(), relationship.getType().getTypeDefName(), relationship.getGUID());
                enterpriseConnector.getMetadataCollection().purgeRelationship(Constants.INFORMATION_VIEW_USER_ID, relationship.getType().getTypeDefGUID(), relationship.getType().getTypeDefName(), relationship.getGUID());
            }  catch (InvalidParameterException | RepositoryErrorException | RelationshipNotDeletedException | UserNotAuthorizedException | FunctionNotSupportedException | RelationshipNotKnownException e) {
                throw buildDeleteRelationshipException(relationship, e, this.getClass().getName());
            }
        }
    }

    public void purgeEntity(EntitySummary entitySummary) throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException, EntityNotKnownException, EntityNotDeletedException, FunctionNotSupportedException {
        if (entitySummary == null || entitySummary.getGUID() == null || entitySummary.getType() == null) {
            log.debug("Nothing will be purged, invalid entity passed as argument: {}", entitySummary);
        } else {
            log.debug("Purge entity with guid {}", entitySummary.getGUID());
            enterpriseConnector.getMetadataCollection().deleteEntity(Constants.INFORMATION_VIEW_USER_ID, entitySummary.getType().getTypeDefGUID(), entitySummary.getType().getTypeDefName(), entitySummary.getGUID());
            enterpriseConnector.getMetadataCollection().purgeEntity(Constants.INFORMATION_VIEW_USER_ID, entitySummary.getType().getTypeDefGUID(), entitySummary.getType().getTypeDefName(), entitySummary.getGUID());
        }
    }

    /**
     * Return list of guids of entities linked through the mentioned relationship
     * @param allEntitiesGuids
     * @param relationshipType
     * @param relationshipEndFunction
     * @return list of related entities
     */
    public List<EntityDetail> getRelatedEntities(List<String> allEntitiesGuids, String relationshipType, Function<Relationship, String> relationshipEndFunction) {
        List<Relationship> relatedEntities = allEntitiesGuids.stream().flatMap(e -> Optional.ofNullable(getRelationships(relationshipType, e))
                                                                                                        .map(Collection::stream)
                                                                                                        .orElseGet(Stream::empty)).collect(Collectors.toList());
        return getEntityDetails(relatedEntities, relationshipEndFunction);
    }



    public List<EntityDetail> getEntityDetails(List<Relationship> relationships, Function<Relationship, String> relationshipEndFunction ) {
        Set<String> allLinkedEntitiesGuids = Optional.ofNullable(relationships)
                                                    .map(Collection::stream)
                                                    .orElseGet(Stream::empty)
                                                    .map(relationshipEndFunction)
                                                    .collect(Collectors.toSet());
        return allLinkedEntitiesGuids.stream().map(this::getEntityByGuid).collect(Collectors.toList());
    }
}
