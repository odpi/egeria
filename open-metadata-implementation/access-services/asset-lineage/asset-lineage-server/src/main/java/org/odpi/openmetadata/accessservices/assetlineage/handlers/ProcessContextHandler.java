/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.SuperTypesRetriever;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.RELATIONSHIP_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_ALIAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_DELEGATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_PORT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.immutableProcessRelationshipsTypes;

/**
 * The process context handler provides methods to build lineage context from processes.
 */
public class ProcessContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessContextHandler.class);

    private final AssetContextHandler assetContextHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final List<String> supportedZones;
    private final HandlerHelper handlerHelper;
    private SuperTypesRetriever superTypesRetriever;

    private AssetContext graph;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param supportedZones          configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public ProcessContextHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler, AssetContextHandler assetContextHandler, List<String> supportedZones,
                                 Set<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
        this.assetContextHandler = assetContextHandler;
        this.supportedZones = supportedZones;
        this.superTypesRetriever = new SuperTypesRetriever(repositoryHelper);
    }

    /**
     * Retrieves the full context for a Process
     *
     * @param userId  userId of user making request.
     * @param process the process entity for which the context is built
     *
     * @return Map of the relationships between the Entities that are relevant to a Process
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Multimap<String, RelationshipsContext> buildProcessContext(String userId, EntityDetail process) throws OCFCheckedExceptionBase {

        final String methodName = "buildProcessContext";

        String processGUID = process.getGUID();
        invalidParameterHandler.validateAssetInSupportedZone(processGUID, GUID_PARAMETER,
                handlerHelper.getAssetZoneMembership(process.getClassifications()), supportedZones, ASSET_LINEAGE_OMAS, methodName);

        List<Relationship> processPorts = handlerHelper.getRelationshipsByType(userId, processGUID, PROCESS_PORT, PROCESS);
        if (CollectionUtils.isEmpty(processPorts)) {
            log.error("No relationships Process Port has been found for the entity with guid {}", processGUID);

            throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getMessageDefinition(), this.getClass().getName(), "Retrieving Relationship");
        }

        RelationshipsContext relationshipsContext = handlerHelper.buildContextForRelationships(userId, processGUID, processPorts);

        for (Relationship processPort : processPorts) {
            EntityDetail port = handlerHelper.getEntityAtTheEnd(userId, processGUID, processPort);
            addContextForPort(userId, port, relationshipsContext.getRelationships());
        }

        Multimap<String, RelationshipsContext> context = ArrayListMultimap.create();
        context.put(AssetLineageEventType.PROCESS_CONTEXT_EVENT.getEventTypeName(), relationshipsContext);

        Set<LineageEntity> tabularColumns = relationshipsContext.getRelationships().stream()
                .filter(relationship -> relationship.getRelationshipType().equalsIgnoreCase(ATTRIBUTE_FOR_SCHEMA))
                .map(GraphContext::getToVertex).collect(Collectors.toSet());

        for (LineageEntity tabularColumn : tabularColumns) {
            addLineageContextForColumn(userId, context, tabularColumn.getGuid(), tabularColumn.getTypeDefName());
        }

        return context;
    }

    private void addLineageContextForColumn(String userId, Multimap<String, RelationshipsContext> context, String columnGUID, String typeDefName) throws
                                                                                                                                            OCFCheckedExceptionBase {
        List<Relationship> lineageMappings = handlerHelper.getRelationshipsByType(userId, columnGUID, LINEAGE_MAPPING, typeDefName);

        context.put(AssetLineageEventType.LINEAGE_MAPPINGS_EVENT.getEventTypeName(),
                handlerHelper.buildContextForRelationships(userId, columnGUID, lineageMappings));

        for (Relationship lineageMapping : lineageMappings) {
            context.putAll(Multimaps.forMap(assetContextHandler.buildSchemaElementContext(userId, handlerHelper.getEntityAtTheEnd(userId,
                    columnGUID, lineageMapping))));
        }
    }

    private void addContextForPort(String userId, EntityDetail port, Set<GraphContext> relationshipsContext) throws OCFCheckedExceptionBase {
        String portType = port.getType().getTypeDefName();

        if (PORT_ALIAS.equals(portType)) {
            EntityDetail delegatedPort = handlerHelper.addContextForRelationships(userId, port, PORT_DELEGATION, relationshipsContext);

            addContextForPort(userId, delegatedPort, relationshipsContext);
        }
        if (PORT_IMPLEMENTATION.equals(portType)) {
            EntityDetail tabularSchemaType = handlerHelper.addContextForRelationships(userId, port, PORT_SCHEMA, relationshipsContext);

            EntityDetail tabularColumn = handlerHelper.addContextForRelationships(userId, tabularSchemaType, ATTRIBUTE_FOR_SCHEMA,
                    relationshipsContext);

            if (tabularColumn == null) {
                log.error("No entity TabularColumn has been found for the the TabularSchemaType with guid {}", tabularSchemaType.getGUID());

                throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getMessageDefinition(), this.getClass().getName(), "Retrieving Relationship");
            }
        }
    }

    /**
     * Check if the Port to Process relationships are created
     *
     * @param userId       userId of user making request
     * @param entityDetail the entity for which the relationships are retrieved
     *
     * @return the current graph context if the Process to Port relationships are available
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Map<String, Set<GraphContext>> checkIfAllRelationshipsExist(String userId,
                                                                        EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        boolean entitiesTillLastRelationshipExist = hasEntitiesLinkedWithProcessPort(userId, entityDetail);
        if (entitiesTillLastRelationshipExist) {
            return graph.getNeighbors();
        }

        log.error("Some relationships are missing for the entity with guid {}", entityDetail.getGUID());

        throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getMessageDefinition(),
                this.getClass().getName(),
                "Retrieving Relationships");
    }

    /**
     * Checks if the all the  Process relationships are created
     *
     * @param userId       userId of user making request
     * @param entityDetail the entity for which the relationships are retrieved
     *
     * @return true if all the process relationships are created
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private boolean hasEntitiesLinkedWithProcessPort(String userId,
                                                     EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        List<EntityDetail> entityDetails = getRelationshipsBetweenEntities(userId, entityDetail, PROCESS_PORT);

        if (entityDetails.isEmpty()) {
            log.error("No relationships Process Port has been found for the entity with guid {}", entityDetail.getGUID());

            throw new AssetLineageException(RELATIONSHIP_NOT_FOUND.getMessageDefinition(),
                    this.getClass().getName(),
                    "Retrieving Relationship");
        }

        return hasRelationshipBasedOnType(entityDetails, userId);
    }


    /**
     * Retrieves the relationships of an Entity
     *
     * @param userId           String - userId of user making request.
     * @param startEntity      the entity for which the relationships are retrieved
     * @param relationshipType type of the relationship
     *
     * @return List of entities that are on the other end of the relationship, empty list if none
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private List<EntityDetail> getRelationshipsBetweenEntities(String userId,
                                                               EntityDetail startEntity,
                                                               String relationshipType) throws OCFCheckedExceptionBase {
        if (startEntity == null) return Collections.emptyList();

        handlerHelper.addLineageClassificationToContext(startEntity, graph);
        String startEntityType = startEntity.getType().getTypeDefName();

        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, startEntityType);
        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {
            EntityDetail endEntity = handlerHelper.buildGraphEdgeByRelationship(userId, startEntity, relationship, graph);
            if (endEntity == null) {
                return Collections.emptyList();
            }

            addContextForTabularColumns(userId, endEntity);
            entityDetails.add(endEntity);
        }

        return entityDetails;
    }

    /**
     * Enhance the process context with Tabular Column context
     * Add the asset neighbors for Tabular Column
     *
     * @param userId String - userId of user making request.
     * @param entity details of the entity
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addContextForTabularColumns(String userId, EntityDetail entity) throws OCFCheckedExceptionBase {
        Set<String> superTypes = superTypesRetriever.getSuperTypes(userId, entity.getType().getTypeDefName());

        if (superTypes.contains(TABULAR_COLUMN)) {
            Map<String, RelationshipsContext> assetContext = assetContextHandler.buildSchemaElementContext(userId, entity);

            for (String eventType : assetContext.keySet()) {
                RelationshipsContext relationshipContext = assetContext.get(eventType);
                Set<GraphContext> relationships = relationshipContext.getRelationships();

                graph.getGraphContexts().addAll(relationships);
                mergeGraphNeighbors(eventType, relationships);
            }
        }
    }

    private void mergeGraphNeighbors(String k, Set<GraphContext> v) {
        if (graph.getNeighbors().containsKey(k)) {
            graph.getNeighbors().get(k).addAll(v);
        } else {
            graph.getNeighbors().put(k, v);
        }
    }

    /**
     * Creates the full context for a Process. There are two cases, a process can have a relationship to either
     * a Port Alias or to a Port Implementation. In case of Port Alias it should take the context until Port Implementation
     * entities otherwise it should take the context down to TabularColumn entities.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     *
     * @return boolean true if relationships exist otherwise false.
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private boolean hasRelationshipBasedOnType(List<EntityDetail> entityDetails,
                                               String userId) throws OCFCheckedExceptionBase {
        boolean relationshipsExist = false;
        if (checkIfEntityExistWithSpecificType(entityDetails, PORT_ALIAS))
            relationshipsExist = hasLineageRelationships(entityDetails, userId);

        if (checkIfEntityExistWithSpecificType(entityDetails, PORT_IMPLEMENTATION))
            relationshipsExist = hasTabularSchemaTypes(entityDetails, userId);

        return relationshipsExist;
    }

    /**
     * Returns true if the entities that are passed as an argument in the method have any lineage related relationships.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     *
     * @return boolean true if relationships exist otherwise false.
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private boolean hasLineageRelationships(List<EntityDetail> entityDetails,
                                            String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {
            result.addAll(getRelationshipsBetweenEntities(userId, entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName())));
        }
        return !result.isEmpty();
    }

    /**
     * Returns if there are any TabularSchemaTypes that are related to a Port Implementation Entity.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     *
     * @return boolean true if relationships exist otherwise false.
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private boolean hasTabularSchemaTypes(List<EntityDetail> entityDetails,
                                          String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail> tabularSchemaType = getRelationshipsBetweenEntities(userId,
                    entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()));
            Optional<EntityDetail> first = tabularSchemaType.stream().findFirst();
            result.add(first.orElse(null));
        }
        return hasSchemaAttributes(result, userId);
    }

    /**
     * Returns if the TabularColumns are part of a TabularSchemaType.
     *
     * @param entityDetails list of entities
     * @param userId        String - userId of user making request.
     *
     * @return boolean true if relationships exist otherwise false.
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private boolean hasSchemaAttributes(List<EntityDetail> entityDetails,
                                        String userId) throws OCFCheckedExceptionBase {
        List<EntityDetail> result = new ArrayList<>();
        for (EntityDetail entityDetail : entityDetails) {

            List<EntityDetail> newListOfEntityDetails = getRelationshipsBetweenEntities(userId,
                    entityDetail,
                    immutableProcessRelationshipsTypes.get(entityDetail.getType().getTypeDefName()));
            result.addAll(newListOfEntityDetails);
        }
        return hasLineageRelationships(result, userId);
    }

    /**
     * Check if the Type of the entity exists on Open Metadata Types.
     *
     * @param entityDetails list of entities
     * @param typeDefName   String - the type to be checked
     *
     * @return Boolean if type exists or not
     */
    private boolean checkIfEntityExistWithSpecificType(List<EntityDetail> entityDetails, String typeDefName) {
        return entityDetails.stream().anyMatch(entity -> entity.getType().getTypeDefName().equals(typeDefName));
    }
}

