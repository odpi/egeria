/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSDynamicTypeMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;

import java.io.IOException;
import java.util.*;

/**
 * Provides the OMRSMetadataCollection implementation for XTDB.
 */
public class XTDBOMRSMetadataCollection extends OMRSDynamicTypeMetadataCollectionBase {

    private static final Logger log = LoggerFactory.getLogger(XTDBOMRSMetadataCollection.class);

    private final XTDBOMRSRepositoryConnector xtdbRepositoryConnector;

    /**
     * Default constructor
     *
     * @param parentConnector      connector that this metadata collection supports.
     *                             The connector has the information to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper that provides methods to repository connectors and repository event mappers
     *                             to build valid type definitions, entities and relationships.
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters
     * @param metadataCollectionId unique identifier for the repository
     * @param auditLog             logging destination
     */
    public XTDBOMRSMetadataCollection(XTDBOMRSRepositoryConnector parentConnector,
                                      String repositoryName,
                                      OMRSRepositoryHelper repositoryHelper,
                                      OMRSRepositoryValidator repositoryValidator,
                                      String metadataCollectionId,
                                      AuditLog auditLog) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
        log.debug("Constructing XTDBOMRSMetadataCollection with name: {}", repositoryName);
        parentConnector.setRepositoryName(repositoryName);
        this.xtdbRepositoryConnector = parentConnector;
        setAuditLog(auditLog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTypeDef(String userId,
                           TypeDef newTypeDef) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException {
        final String methodName = "addTypeDef";
        super.addTypeDef(userId, newTypeDef);
        try {
            TypeDefCache.addTypeDef(newTypeDef);
        } catch (InvalidParameterException e) {
            throw new InvalidTypeDefException(XTDBErrorCode.INVALID_TYPEDEF.getMessageDefinition(newTypeDef.getName()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAttributeTypeDef(String userId,
                                    AttributeTypeDef newAttributeTypeDef) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException {
        super.addAttributeTypeDef(userId, newAttributeTypeDef);
        TypeDefCache.addAttributeTypeDef(newAttributeTypeDef);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeDef updateTypeDef(String userId,
                                 TypeDefPatch typeDefPatch) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            PatchErrorException {
        final String methodName = "updateTypeDef";
        TypeDef updated = super.updateTypeDef(userId, typeDefPatch);
        try {
            TypeDefCache.addTypeDef(updated);
        } catch (InvalidParameterException e) {
            throw new PatchErrorException(XTDBErrorCode.INVALID_TYPEDEF.getMessageDefinition(updated.getName()),
                                          this.getClass().getName(), methodName, e);
        }
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTypeDef(String userId,
                              String obsoleteTypeDefGUID,
                              String obsoleteTypeDefName) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            TypeDefInUseException,
            UserNotAuthorizedException {
        super.deleteTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
        TypeDefCache.removeTypeDef(obsoleteTypeDefGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAttributeTypeDef(String userId,
                                       String obsoleteTypeDefGUID,
                                       String obsoleteTypeDefName) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            TypeDefInUseException,
            UserNotAuthorizedException {
        super.deleteAttributeTypeDef(userId, obsoleteTypeDefGUID, obsoleteTypeDefName);
        TypeDefCache.removeAttributeTypeDef(obsoleteTypeDefGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeDef reIdentifyTypeDef(String userId,
                                     String originalTypeDefGUID,
                                     String originalTypeDefName,
                                     String newTypeDefGUID,
                                     String newTypeDefName) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException {
        TypeDef updated = super.reIdentifyTypeDef(userId, originalTypeDefGUID, originalTypeDefName, newTypeDefGUID, newTypeDefName);
        TypeDefCache.addTypeDef(updated);
        TypeDefCache.removeTypeDef(originalTypeDefGUID);
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AttributeTypeDef reIdentifyAttributeTypeDef(String userId,
                                                       String originalAttributeTypeDefGUID,
                                                       String originalAttributeTypeDefName,
                                                       String newAttributeTypeDefGUID,
                                                       String newAttributeTypeDefName) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException {
        AttributeTypeDef updated = super.reIdentifyAttributeTypeDef(userId, originalAttributeTypeDefGUID, originalAttributeTypeDefName, newAttributeTypeDefGUID, newAttributeTypeDefName);
        TypeDefCache.addAttributeTypeDef(updated);
        TypeDefCache.removeAttributeTypeDef(originalAttributeTypeDefGUID);
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail isEntityKnown(String userId,
                                      String guid) throws
            InvalidParameterException,
            RepositoryErrorException {
        final String methodName = "isEntityKnown";
        this.getInstanceParameterValidation(userId, guid, methodName);
        try {
            EntityDetail entity = new GetEntity(xtdbRepositoryConnector, guid, null).asDetail();
            repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
            return entity;
        } catch (EntityNotKnownException e) {
            log.info("Entity with GUID {} does not exist in the repository.", guid);
        } catch (EntityProxyOnlyException e) {
            log.info("Entity with GUID {} exists, but is only a proxy in the repository.", guid);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntitySummary getEntitySummary(String userId,
                                          String guid) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        final String methodName = "getEntitySummary";
        super.getInstanceParameterValidation(userId, guid, methodName);
        EntitySummary summary = new GetEntity(xtdbRepositoryConnector, guid, null).asSummary();
        repositoryValidator.validateEntityFromStore(repositoryName, guid, summary, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, summary, methodName);
        return summary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException {
        return getEntityDetail(userId, guid, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid,
                                        Date asOfTime) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException {
        final String methodName = "getEntityDetail";
        super.getInstanceParameterValidation(userId, guid, methodName);
        EntityDetail entity = new GetEntity(xtdbRepositoryConnector, guid, asOfTime).asDetail();
        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDetail> getEntityDetailHistory(String userId,
                                                     String guid,
                                                     Date fromTime,
                                                     Date toTime,
                                                     int startFromElement,
                                                     int pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        final String methodName = "getEntityDetailHistory";
        super.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);
        return new GetEntityHistory(xtdbRepositoryConnector, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String userId,
                                                        String entityGUID,
                                                        String relationshipTypeGUID,
                                                        int fromRelationshipElement,
                                                        List<InstanceStatus> limitResultsByStatus,
                                                        Date asOfTime,
                                                        String sequencingProperty,
                                                        SequencingOrder sequencingOrder,
                                                        int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PagingErrorException,
            UserNotAuthorizedException {
        final String methodName = "getRelationshipsForEntity";
        super.getRelationshipsForEntityParameterValidation(userId, entityGUID, relationshipTypeGUID, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        List<Relationship> entityRelationships;
        IXtdb xtdbAPI = xtdbRepositoryConnector.getXtdbAPI();
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            EntitySummary entity = GetEntity.summaryByGuid(xtdbRepositoryConnector, db, entityGUID);
            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
            repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);
            entityRelationships = new GetRelationshipsForEntity(xtdbRepositoryConnector,
                    entityGUID,
                    relationshipTypeGUID,
                    fromRelationshipElement,
                    limitResultsByStatus,
                    db,
                    sequencingProperty,
                    sequencingOrder,
                    pageSize,
                    userId).getResults();
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), this.getClass().getName(), e);
        }
        return entityRelationships == null || entityRelationships.isEmpty() ? null : entityRelationships;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDetail> findEntitiesByProperty(String userId,
                                                     String entityTypeGUID,
                                                     InstanceProperties matchProperties,
                                                     MatchCriteria matchCriteria,
                                                     int fromEntityElement,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     List<String> limitResultsByClassification,
                                                     Date asOfTime,
                                                     String sequencingProperty,
                                                     SequencingOrder sequencingOrder,
                                                     int pageSize) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {

        super.findEntitiesByPropertyParameterValidation(userId, entityTypeGUID, matchProperties, matchCriteria, fromEntityElement, limitResultsByStatus, limitResultsByClassification, asOfTime, sequencingProperty, sequencingOrder, pageSize);

        SearchProperties searchProperties = repositoryHelper.getSearchPropertiesFromInstanceProperties(repositoryName, matchProperties, matchCriteria);
        SearchClassifications searchClassifications = repositoryHelper.getSearchClassificationsFromList(limitResultsByClassification);

        return findEntities(userId,
                entityTypeGUID,
                null,
                searchProperties,
                fromEntityElement,
                limitResultsByStatus,
                searchClassifications,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDetail> findEntities(String userId,
                                           String entityTypeGUID,
                                           List<String> entitySubtypeGUIDs,
                                           SearchProperties matchProperties,
                                           int fromEntityElement,
                                           List<InstanceStatus> limitResultsByStatus,
                                           SearchClassifications matchClassifications,
                                           Date asOfTime,
                                           String sequencingProperty,
                                           SequencingOrder sequencingOrder,
                                           int pageSize) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PagingErrorException {
        this.findEntitiesParameterValidation(userId, entityTypeGUID, entitySubtypeGUIDs, matchProperties, fromEntityElement, limitResultsByStatus, matchClassifications, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        return new FindEntities(xtdbRepositoryConnector,
                entityTypeGUID,
                entitySubtypeGUIDs,
                matchProperties,
                fromEntityElement,
                limitResultsByStatus,
                matchClassifications,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId).getResults();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDetail> findEntitiesByClassification(String userId,
                                                           String entityTypeGUID,
                                                           String classificationName,
                                                           InstanceProperties matchClassificationProperties,
                                                           MatchCriteria matchCriteria,
                                                           int fromEntityElement,
                                                           List<InstanceStatus> limitResultsByStatus,
                                                           Date asOfTime,
                                                           String sequencingProperty,
                                                           SequencingOrder sequencingOrder,
                                                           int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            ClassificationErrorException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {

        super.findEntitiesByClassificationParameterValidation(userId, entityTypeGUID, classificationName, matchClassificationProperties, matchCriteria, fromEntityElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);

        SearchProperties searchProperties = repositoryHelper.getSearchPropertiesFromInstanceProperties(repositoryName, matchClassificationProperties, matchCriteria);
        SearchClassifications searchClassifications = new SearchClassifications();
        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition condition = new ClassificationCondition();
        condition.setName(classificationName);
        condition.setMatchProperties(searchProperties);
        classificationConditions.add(condition);
        searchClassifications.setConditions(classificationConditions);

        // Since matchCriteria passed are embedded with properties, the overall matchCriteria should be all (?)
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return findEntities(userId,
                entityTypeGUID,
                null,
                null,
                fromEntityElement,
                limitResultsByStatus,
                searchClassifications,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityDetail> findEntitiesByPropertyValue(String userId,
                                                          String entityTypeGUID,
                                                          String searchCriteria,
                                                          int fromEntityElement,
                                                          List<InstanceStatus> limitResultsByStatus,
                                                          List<String> limitResultsByClassification,
                                                          Date asOfTime,
                                                          String sequencingProperty,
                                                          SequencingOrder sequencingOrder,
                                                          int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {
        super.findEntitiesByPropertyValueParameterValidation(userId, entityTypeGUID, searchCriteria, fromEntityElement, limitResultsByStatus, limitResultsByClassification, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        SearchClassifications searchClassifications = repositoryHelper.getSearchClassificationsFromList(limitResultsByClassification);
        return new FindEntitiesByPropertyValue(xtdbRepositoryConnector,
                entityTypeGUID,
                searchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                searchClassifications,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId).getResults();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship isRelationshipKnown(String userId,
                                            String guid) throws
            InvalidParameterException,
            RepositoryErrorException {
        final String methodName = "isRelationshipKnown";
        super.getInstanceParameterValidation(userId, guid, methodName);
        return new GetRelationship(xtdbRepositoryConnector, guid, null).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship getRelationship(String userId,
                                        String guid) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {
        final String methodName = "getRelationship";
        this.getInstanceParameterValidation(userId, guid, methodName);
        return getAndValidateRelationship(guid, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship getRelationship(String userId,
                                        String guid,
                                        Date asOfTime) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {

        final String methodName = "getRelationship";
        this.getInstanceParameterValidation(userId, guid, asOfTime, methodName);
        return getAndValidateRelationship(guid, asOfTime);
    }

    private Relationship getAndValidateRelationship(String guid,
                                                    Date asOfTime) throws
            RepositoryErrorException,
            RelationshipNotKnownException {
        final String methodName = "getAndValidateRelationship";
        Relationship relationship = new GetRelationship(xtdbRepositoryConnector, guid, asOfTime).execute();
        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);
        return relationship;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relationship> getRelationshipHistory(String userId,
                                                     String guid,
                                                     Date fromTime,
                                                     Date toTime,
                                                     int startFromElement,
                                                     int pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {
        final String methodName = "getRelationshipHistory";
        super.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);
        return new GetRelationshipHistory(xtdbRepositoryConnector, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relationship> findRelationships(String userId,
                                                String relationshipTypeGUID,
                                                List<String> relationshipSubtypeGUIDs,
                                                SearchProperties matchProperties,
                                                int fromRelationshipElement,
                                                List<InstanceStatus> limitResultsByStatus,
                                                Date asOfTime,
                                                String sequencingProperty,
                                                SequencingOrder sequencingOrder,
                                                int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PagingErrorException {
        super.findRelationshipsParameterValidation(userId, relationshipTypeGUID, relationshipSubtypeGUIDs, matchProperties, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        return new FindRelationships(xtdbRepositoryConnector,
                relationshipTypeGUID,
                relationshipSubtypeGUIDs,
                matchProperties,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId).getResults();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relationship> findRelationshipsByProperty(String userId,
                                                          String relationshipTypeGUID,
                                                          InstanceProperties matchProperties,
                                                          MatchCriteria matchCriteria,
                                                          int fromRelationshipElement,
                                                          List<InstanceStatus> limitResultsByStatus,
                                                          Date asOfTime,
                                                          String sequencingProperty,
                                                          SequencingOrder sequencingOrder,
                                                          int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {
        super.findRelationshipsByPropertyParameterValidation(userId, relationshipTypeGUID, matchProperties, matchCriteria, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        SearchProperties searchProperties = repositoryHelper.getSearchPropertiesFromInstanceProperties(repositoryName, matchProperties, matchCriteria);
        return findRelationships(userId,
                relationshipTypeGUID,
                null,
                searchProperties,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Relationship> findRelationshipsByPropertyValue(String userId,
                                                               String relationshipTypeGUID,
                                                               String searchCriteria,
                                                               int fromRelationshipElement,
                                                               List<InstanceStatus> limitResultsByStatus,
                                                               Date asOfTime,
                                                               String sequencingProperty,
                                                               SequencingOrder sequencingOrder,
                                                               int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {
        super.findRelationshipsByPropertyValueParameterValidation(userId, relationshipTypeGUID, searchCriteria, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
        return new FindRelationshipsByPropertyValue(xtdbRepositoryConnector,
                relationshipTypeGUID,
                searchCriteria,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId).getResults();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstanceGraph getLinkingEntities(String userId,
                                            String startEntityGUID,
                                            String endEntityGUID,
                                            List<InstanceStatus> limitResultsByStatus,
                                            Date asOfTime) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException {

        final String methodName = "getLinkingEntities";

        this.getLinkingEntitiesParameterValidation(userId,
                startEntityGUID,
                endEntityGUID,
                limitResultsByStatus,
                asOfTime);

        // If the two GUIDs are equal, just return the one entity and nothing else (no relationships)
        if (startEntityGUID.equals(endEntityGUID)) {
            try {
                InstanceGraph one = new InstanceGraph();
                List<EntityDetail> list = new ArrayList<>();
                EntityDetail entity = new GetEntity(xtdbRepositoryConnector, startEntityGUID, asOfTime).asDetail();
                list.add(entity);
                one.setEntities(list);
                return one;
            } catch (EntityProxyOnlyException e) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(
                        startEntityGUID, repositoryName), this.getClass().getName(), methodName, e);
            }
        }

        // Otherwise, actually do the traversals...
        return new GetLinkingEntities(xtdbRepositoryConnector,
                startEntityGUID,
                endEntityGUID,
                limitResultsByStatus,
                asOfTime).execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstanceGraph getEntityNeighborhood(String userId,
                                               String entityGUID,
                                               List<String> entityTypeGUIDs,
                                               List<String> relationshipTypeGUIDs,
                                               List<InstanceStatus> limitResultsByStatus,
                                               List<String> limitResultsByClassification,
                                               Date asOfTime,
                                               int level) throws
            InvalidParameterException,
            EntityNotKnownException,
            RepositoryErrorException,
            TypeErrorException {

        final String methodName = "getEntityNeighborhood";
        final String entityTypeGUIDParameterName = "entityTypeGUIDs";
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUIDs";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";
        final String asOfTimeParameter = "asOfTime";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, Constants.ENTITY_GUID, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        if (entityTypeGUIDs != null) {
            for (String guid : entityTypeGUIDs) {
                repositoryValidator.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null) {
            for (String guid : relationshipTypeGUIDs) {
                repositoryValidator.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
            }
        }

        if (limitResultsByClassification != null) {
            for (String classificationName : limitResultsByClassification) {
                repositoryValidator.validateClassificationName(repositoryName,
                        limitedResultsByClassificationParameterName,
                        classificationName,
                        methodName);
            }
        }

        return new GetEntityNeighborhood(xtdbRepositoryConnector,
                entityGUID,
                entityTypeGUIDs,
                relationshipTypeGUIDs,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                level).execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  List<EntityDetail> getRelatedEntities(String userId,
                                                  String startEntityGUID,
                                                  List<String> entityTypeGUIDs,
                                                  int fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String> limitResultsByClassification,
                                                  Date asOfTime,
                                                  String sequencingProperty,
                                                  SequencingOrder sequencingOrder,
                                                  int pageSize) throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException {

        this.getRelatedEntitiesParameterValidation(userId,
                startEntityGUID,
                entityTypeGUIDs,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

        final String methodName = "getRelatedEntities";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        if (limitResultsByClassification != null) {
            for (String classificationName : limitResultsByClassification) {
                repositoryValidator.validateClassificationName(repositoryName,
                        limitedResultsByClassificationParameterName,
                        classificationName,
                        methodName);
            }
        }

        // Retrieve ALL (full depth) neighborhood from the starting entity (not retrieving any relationships)
        InstanceGraph adjacent = new GetRelatedEntities(xtdbRepositoryConnector,
                startEntityGUID,
                entityTypeGUIDs,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime).execute();

        if (adjacent != null) {
            // ... and then simply limit the entity results according to the sequencing and paging parameters
            return repositoryHelper.formatEntityResults(adjacent.getEntities(), fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail addEntity(String userId,
                                  String entityTypeGUID,
                                  InstanceProperties initialProperties,
                                  List<Classification> initialClassifications,
                                  InstanceStatus initialStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            StatusNotSupportedException {

        final String methodName = "addEntity";

        // Note that these validations should not confirm required or unique properties: that is the responsibility
        // of the layer above (OMAS)

        TypeDef typeDef = super.addEntityParameterValidation(userId, entityTypeGUID, initialProperties, initialClassifications, initialStatus, methodName);
        EntityDetail newEntity = repositoryHelper.getNewEntity(repositoryName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                null,
                userId,
                typeDef.getName(),
                initialProperties,
                initialClassifications);
        newEntity.setMetadataCollectionName(metadataCollectionName);

        if (initialStatus != null) {
            newEntity.setStatus(initialStatus);
        }

        return addEntity(newEntity);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail addExternalEntity(String userId,
                                          String entityTypeGUID,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          InstanceProperties initialProperties,
                                          List<Classification> initialClassifications,
                                          InstanceStatus initialStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException {

        final String methodName = "addExternalEntity";

        TypeDef typeDef = super.addExternalEntityParameterValidation(userId, entityTypeGUID, externalSourceGUID, initialProperties, initialClassifications, initialStatus, methodName);
        EntityDetail newEntity = repositoryHelper.getNewEntity(repositoryName,
                externalSourceGUID,
                InstanceProvenanceType.EXTERNAL_SOURCE,
                metadataCollectionId,
                userId,
                typeDef.getName(),
                initialProperties,
                initialClassifications);
        newEntity.setMetadataCollectionName(externalSourceName);

        if (initialStatus != null) {
            newEntity.setStatus(initialStatus);
        }

        return addEntity(newEntity);

    }

    private EntityDetail addEntity(EntityDetail entity) throws
            RepositoryErrorException {
        return AddEntity.transact(xtdbRepositoryConnector, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntityProxy(String userId,
                               EntityProxy entityProxy) throws
            InvalidParameterException,
            RepositoryErrorException {
        super.addEntityProxyParameterValidation(userId, entityProxy);
        AddEntityProxy.transact(xtdbRepositoryConnector, entityProxy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail updateEntityStatus(String userId,
                                           String entityGUID,
                                           InstanceStatus newStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            StatusNotSupportedException {
        this.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, "updateEntityStatus");
        return UpdateEntityStatus.transact(xtdbRepositoryConnector, userId, entityGUID, newStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail updateEntityProperties(String userId,
                                               String entityGUID,
                                               InstanceProperties properties) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException {
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, "updateEntityProperties");
        return UpdateEntityProperties.transact(xtdbRepositoryConnector, userId, entityGUID, properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail undoEntityUpdate(String userId,
                                         String entityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        super.manageInstanceParameterValidation(userId, entityGUID, Constants.ENTITY_GUID, "undoEntityUpdate");
        return UndoEntityUpdate.transact(xtdbRepositoryConnector, userId, entityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail deleteEntity(String userId,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String obsoleteEntityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        super.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteEntityGUID, "obsoleteEntityGUID", "deleteEntity");
        return DeleteEntity.transact(xtdbRepositoryConnector, userId, obsoleteEntityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeEntity(String userId,
                            String typeDefGUID,
                            String typeDefName,
                            String deletedEntityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException {
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedEntityGUID, "deletedEntityGUID", "purgeEntity");
        PurgeEntity.transactWithValidation(xtdbRepositoryConnector, deletedEntityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail restoreEntity(String userId,
                                      String deletedEntityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException {
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, "deletedEntityGUID", "restoreEntity");
        return RestoreEntity.transact(xtdbRepositoryConnector, userId, deletedEntityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail classifyEntity(String userId,
                                       String entityGUID,
                                       String classificationName,
                                       InstanceProperties classificationProperties) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException {

        return classifyEntity(userId,
                entityGUID,
                classificationName,
                null,
                null,
                null,
                null,
                classificationProperties);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Classification classifyEntity(String userId,
                                         EntityProxy entityProxy,
                                         String classificationName,
                                         InstanceProperties classificationProperties) throws
            InvalidParameterException,
            RepositoryErrorException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException,
            FunctionNotSupportedException {
        return classifyEntity(userId,
                entityProxy,
                classificationName,
                null,
                null,
                null,
                null,
                classificationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail classifyEntity(String userId,
                                       String entityGUID,
                                       String classificationName,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       ClassificationOrigin classificationOrigin,
                                       String classificationOriginGUID,
                                       InstanceProperties classificationProperties) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException {

        final String methodName = "classifyEntity";
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, Constants.ENTITY_GUID, entityGUID, methodName);
        return ClassifyEntityDetail.transact(xtdbRepositoryConnector,
                userId,
                entityGUID,
                classificationName,
                externalSourceGUID,
                externalSourceName,
                classificationOrigin,
                classificationOriginGUID,
                classificationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Classification classifyEntity(String userId,
                                         EntityProxy entityProxy,
                                         String classificationName,
                                         String externalSourceGUID,
                                         String externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String classificationOriginGUID,
                                         InstanceProperties classificationProperties) throws
            InvalidParameterException,
            RepositoryErrorException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException,
            FunctionNotSupportedException {

        final String methodName = "classifyEntityProxy (detailed)";
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, Constants.ENTITY_GUID, entityProxy == null ? null : entityProxy.getGUID(), methodName);
        return ClassifyEntityProxy.transact(xtdbRepositoryConnector,
                userId,
                entityProxy,
                classificationName,
                externalSourceGUID,
                externalSourceName,
                classificationOrigin,
                classificationOriginGUID,
                classificationProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail declassifyEntity(String userId,
                                         String entityGUID,
                                         String classificationName) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException {
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName, "declassifyEntity");
        return DeclassifyEntityDetail.transact(xtdbRepositoryConnector, entityGUID, classificationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Classification declassifyEntity(String userId,
                                           EntityProxy entityProxy,
                                           String classificationName) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException {
        super.declassifyEntityParameterValidation(userId, entityProxy, classificationName, "declassifyEntityProxy");
        return DeclassifyEntityProxy.transact(xtdbRepositoryConnector, entityProxy, classificationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail updateEntityClassification(String userId,
                                                   String entityGUID,
                                                   String classificationName,
                                                   InstanceProperties properties) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException {
        super.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, "updateEntityClassification");
        return UpdateEntityDetailClassification.transact(xtdbRepositoryConnector, userId, entityGUID, classificationName, properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Classification updateEntityClassification(String userId,
                                                     EntityProxy entityProxy,
                                                     String classificationName,
                                                     InstanceProperties properties) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException,
            FunctionNotSupportedException {
        super.classifyEntityParameterValidation(userId, entityProxy, classificationName, properties, "updateEntityProxyClassification");
        return UpdateEntityProxyClassification.transact(xtdbRepositoryConnector, userId, entityProxy, classificationName, properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship addRelationship(String userId,
                                        String relationshipTypeGUID,
                                        InstanceProperties initialProperties,
                                        String entityOneGUID,
                                        String entityTwoGUID,
                                        InstanceStatus initialStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException {

        TypeDef typeDef = super.addRelationshipParameterValidation(userId, relationshipTypeGUID, initialProperties, entityOneGUID, entityTwoGUID, initialStatus, "addRelationship");

        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                userId,
                typeDef.getName(),
                initialProperties);
        if (initialStatus != null) {
            relationship.setStatus(initialStatus);
        }

        return addRelationship(relationship, entityOneGUID, entityTwoGUID);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship addExternalRelationship(String userId,
                                                String relationshipTypeGUID,
                                                String externalSourceGUID,
                                                String externalSourceName,
                                                InstanceProperties initialProperties,
                                                String entityOneGUID,
                                                String entityTwoGUID,
                                                InstanceStatus initialStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException {

        TypeDef typeDef = super.addRelationshipParameterValidation(userId, relationshipTypeGUID, initialProperties, entityOneGUID, entityTwoGUID, initialStatus, "addExternalRelationship");

        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                externalSourceGUID,
                InstanceProvenanceType.EXTERNAL_SOURCE,
                userId,
                typeDef.getName(),
                initialProperties);

        relationship.setMetadataCollectionName(externalSourceName);
        relationship.setReplicatedBy(metadataCollectionId);
        if (initialStatus != null) {
            relationship.setStatus(initialStatus);
        }

        return addRelationship(relationship, entityOneGUID, entityTwoGUID);

    }

    private Relationship addRelationship(Relationship relationship,
                                         String entityOneGUID,
                                         String entityTwoGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        return AddRelationship.transact(xtdbRepositoryConnector, relationship, entityOneGUID, entityTwoGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship updateRelationshipStatus(String userId,
                                                 String relationshipGUID,
                                                 InstanceStatus newStatus) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            StatusNotSupportedException {
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, "updateRelationshipStatus");
        return UpdateRelationshipStatus.transact(xtdbRepositoryConnector, userId, relationshipGUID, newStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship updateRelationshipProperties(String userId,
                                                     String relationshipGUID,
                                                     InstanceProperties properties) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            PropertyErrorException {
        this.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, "updateRelationshipProperties");
        return UpdateRelationshipProperties.transact(xtdbRepositoryConnector, userId, relationshipGUID, properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship undoRelationshipUpdate(String userId,
                                               String relationshipGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {
        this.manageInstanceParameterValidation(userId, relationshipGUID, Constants.RELATIONSHIP_GUID, "undoRelationshipUpdate");
        return UndoRelationshipUpdate.transact(xtdbRepositoryConnector, userId, relationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship deleteRelationship(String userId,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String obsoleteRelationshipGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID, "obsoleteRelationshipGUID", "deleteRelationship");
        return DeleteRelationship.transact(xtdbRepositoryConnector, userId, obsoleteRelationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeRelationship(String userId,
                                  String typeDefGUID,
                                  String typeDefName,
                                  String deletedRelationshipGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException {
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedRelationshipGUID, "deletedRelationshipGUID", "purgeRelationship");
        PurgeRelationship.transactWithValidation(xtdbRepositoryConnector, deletedRelationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship restoreRelationship(String userId,
                                            String deletedRelationshipGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException {
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, "deletedRelationshipGUID", "restoreRelationship");
        return RestoreRelationship.transact(xtdbRepositoryConnector, userId, deletedRelationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail reIdentifyEntity(String userId,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String entityGUID,
                                         String newEntityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException {
        this.reIdentifyInstanceParameterValidation(userId, typeDefGUID, typeDefName, entityGUID, Constants.ENTITY_GUID, newEntityGUID, "newEntityGUID", "reIdentifyEntity");
        return ReIdentifyEntity.transact(xtdbRepositoryConnector, userId, entityGUID, newEntityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail reTypeEntity(String userId,
                                     String entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            EntityNotKnownException {
        super.reTypeInstanceParameterValidation(userId, entityGUID, Constants.ENTITY_GUID, TypeDefCategory.ENTITY_DEF, currentTypeDefSummary, newTypeDefSummary, "reTypeEntity");
        return ReTypeEntity.transact(xtdbRepositoryConnector, userId, entityGUID, newTypeDefSummary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDetail reHomeEntity(String userId,
                                     String entityGUID,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String homeMetadataCollectionId,
                                     String newHomeMetadataCollectionId,
                                     String newHomeMetadataCollectionName) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        super.reHomeInstanceParameterValidation(userId, entityGUID, Constants.ENTITY_GUID, typeDefGUID, typeDefName, homeMetadataCollectionId, newHomeMetadataCollectionId, "reHomeEntity");
        return ReHomeEntity.transact(xtdbRepositoryConnector, userId, entityGUID, newHomeMetadataCollectionId, newHomeMetadataCollectionName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship reIdentifyRelationship(String userId,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String relationshipGUID,
                                               String newRelationshipGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException {
        this.reIdentifyInstanceParameterValidation(userId, typeDefGUID, typeDefName, relationshipGUID, Constants.RELATIONSHIP_GUID, newRelationshipGUID, "newRelationshipGUID", "reIdentifyRelationship");
        return ReIdentifyRelationship.transact(xtdbRepositoryConnector, userId, relationshipGUID, newRelationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship reTypeRelationship(String userId,
                                           String relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            RelationshipNotKnownException {
        super.reTypeInstanceParameterValidation(userId, relationshipGUID, Constants.RELATIONSHIP_GUID, TypeDefCategory.RELATIONSHIP_DEF, currentTypeDefSummary, newTypeDefSummary, "reTypeRelationship");
        return ReTypeRelationship.transact(xtdbRepositoryConnector, userId, relationshipGUID, newTypeDefSummary);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship reHomeRelationship(String userId,
                                           String relationshipGUID,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String homeMetadataCollectionId,
                                           String newHomeMetadataCollectionId,
                                           String newHomeMetadataCollectionName) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException {
        final String methodName = "reHomeRelationship";
        final String guidParameterName = "typeDefGUID";
        final String nameParameterName = "typeDefName";
        final String newHomeParameterName = "newHomeMetadataCollectionId";
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, Constants.RELATIONSHIP_GUID, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName, guidParameterName, nameParameterName, typeDefGUID, typeDefName, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, Constants.HOME_METADATA_COLLECTION_ID, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);
        return ReHomeRelationship.transact(xtdbRepositoryConnector, userId, relationshipGUID, newHomeMetadataCollectionId, newHomeMetadataCollectionName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveEntityReferenceCopy(String userId,
                                        EntityDetail entity) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityConflictException,
            HomeEntityException {
        final String methodName = "saveEntityReferenceCopy";
        final String instanceParameterName = "entity";
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateReferenceInstanceHeader(repositoryName, metadataCollectionId, instanceParameterName, entity, methodName);
        SaveEntityReferenceCopy.transact(xtdbRepositoryConnector, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {
        return getHomeClassifications(userId, entityGUID, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date asOfTime) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException {

        final String methodName = "getHomeClassifications";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, Constants.ENTITY_GUID, entityGUID, methodName);

        EntitySummary retrievedEntity = new GetEntity(xtdbRepositoryConnector, entityGUID, asOfTime).asSummary();
        List<Classification> homeClassifications = new ArrayList<>();

        if (retrievedEntity != null) {
            List<Classification> retrievedClassifications = retrievedEntity.getClassifications();
            if (retrievedClassifications != null) {
                for (Classification retrievedClassification : retrievedClassifications) {
                    if (retrievedClassification != null) {
                        if (metadataCollectionId.equals(retrievedClassification.getMetadataCollectionId())) {
                            homeClassifications.add(retrievedClassification);
                        }
                    }
                }
            }
        } else {
            super.reportEntityNotKnown(entityGUID, methodName);
        }

        return homeClassifications.isEmpty() ? null : homeClassifications;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeEntityReferenceCopy(String userId,
                                         String entityGUID,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String homeMetadataCollectionId) throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException {
        final String methodName = "purgeEntityReferenceCopy";
        this.manageReferenceInstanceParameterValidation(userId, entityGUID, typeDefGUID, typeDefName, Constants.ENTITY_GUID, homeMetadataCollectionId, Constants.HOME_METADATA_COLLECTION_ID, methodName);
        PurgeEntity.transactWithoutValidation(xtdbRepositoryConnector, entityGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveClassificationReferenceCopy(String userId,
                                                EntityDetail entity,
                                                Classification classification) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            EntityConflictException,
            PropertyErrorException {
        SaveClassificationReferenceCopyEntityDetail.transact(xtdbRepositoryConnector, entity, classification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveClassificationReferenceCopy(String userId,
                                                EntityProxy entity,
                                                Classification classification) throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            EntityConflictException,
            InvalidEntityException,
            PropertyErrorException {
        SaveClassificationReferenceCopyEntityProxy.transact(xtdbRepositoryConnector, entity, classification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeClassificationReferenceCopy(String userId,
                                                 EntityDetail entity,
                                                 Classification classification) throws
            EntityConflictException,
            RepositoryErrorException {
        PurgeClassificationReferenceCopyEntityDetail.transact(xtdbRepositoryConnector, entity, classification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeClassificationReferenceCopy(String userId,
                                                 EntityProxy entity,
                                                 Classification classification) throws
            InvalidParameterException,
            TypeErrorException,
            PropertyErrorException,
            EntityConflictException,
            InvalidEntityException,
            RepositoryErrorException {
        PurgeClassificationReferenceCopyEntityProxy.transact(xtdbRepositoryConnector, entity, classification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveRelationshipReferenceCopy(String userId,
                                              Relationship relationship) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipConflictException,
            HomeRelationshipException,
            UserNotAuthorizedException {
        super.referenceInstanceParameterValidation(userId, relationship, "relationship", "saveRelationshipReferenceCopy");
        SaveRelationshipReferenceCopy.transact(xtdbRepositoryConnector, relationship);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeRelationshipReferenceCopy(String userId,
                                               String relationshipGUID,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String homeMetadataCollectionId) throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException {
        final String methodName = "purgeRelationshipReferenceCopy";
        this.manageReferenceInstanceParameterValidation(userId, relationshipGUID, typeDefGUID, typeDefName, Constants.RELATIONSHIP_GUID, homeMetadataCollectionId, Constants.HOME_METADATA_COLLECTION_ID, methodName);
        PurgeRelationship.transactWithoutValidation(xtdbRepositoryConnector, relationshipGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityProxy getEntityProxy(String userId,
                                         String entityGUID,
                                         String methodName) throws
            EntityNotKnownException,
            RepositoryErrorException {
        EntityProxy proxy = new GetEntity(xtdbRepositoryConnector, entityGUID, null).asProxy();
        if (proxy == null) {
            reportEntityNotKnown(entityGUID, methodName);
        }
        return proxy;
    }

}
