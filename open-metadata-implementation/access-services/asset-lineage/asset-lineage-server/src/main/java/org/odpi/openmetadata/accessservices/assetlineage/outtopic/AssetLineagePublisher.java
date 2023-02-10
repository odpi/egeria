/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipsEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageSyncEvent;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineagePublishSummary;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageSyncUpdateContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType.LINEAGE_SYNC_EVENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;

/**
 * AssetLineagePublisher is the connector responsible for publishing lineage context information about
 * new and changed assets.
 */
public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    private static final AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private final OpenMetadataTopicConnector outTopicConnector;
    private final String serverUserName;
    private final ProcessContextHandler processContextHandler;
    private final ClassificationHandler classificationHandler;
    private final GlossaryContextHandler glossaryHandler;
    private final AssetContextHandler assetContextHandler;
    private int batchSize;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     *
     * along with classes for testing and manipulating instances.
     *
     * @param outTopicConnector connection to the out topic
     * @param serverName        name of the user of the server instance
     * @param serverUserName    name of this server instance
     */
    public AssetLineagePublisher(OpenMetadataTopicConnector outTopicConnector, String serverName, String serverUserName,
                                 int batchSize) throws OCFCheckedExceptionBase {
        String methodName = "AssetLineagePublisher";

        this.outTopicConnector = outTopicConnector;
        this.serverUserName = serverUserName;
        this.batchSize = batchSize;
        this.processContextHandler = instanceHandler.getProcessHandler(serverUserName, serverName, methodName);
        this.classificationHandler = instanceHandler.getClassificationHandler(serverUserName, serverName, methodName);
        this.glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, methodName);
        this.assetContextHandler = instanceHandler.getAssetContextHandler(serverUserName, serverName, methodName);

    }

    /**
     * Takes the context for a Process and publishes the event to the output topic
     *
     * @param entityDetail entity to get context
     *
     * @return the Process context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    public Multimap<String, RelationshipsContext> publishProcessContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase,
                                                                                                          JsonProcessingException {
        Multimap<String, RelationshipsContext> processContext = processContextHandler.buildProcessContext(serverUserName, entityDetail);
        publishLineageRelationshipsEvents(processContext);

        return processContext;
    }

    /**
     * Build the context for a Glossary Term based on the glossary term GUID and publishes the event to the out topic
     *
     * @param glossaryTermGUID glossary term GUID to get context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    public void publishGlossaryContext(String glossaryTermGUID) throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = glossaryHandler.getGlossaryTermDetails(serverUserName, glossaryTermGUID);
        publishGlossaryContext(entityDetail);
    }

    /***
     * Build the context for a Glossary Term and publishes the event to the out topic
     *
     * @param entityDetail glossary term to get context

     * @return the Glossary Term context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    public Multimap<String, RelationshipsContext> publishGlossaryContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase,
                                                                                                           JsonProcessingException {
        Multimap<String, RelationshipsContext> glossaryTermContext = glossaryHandler.buildGlossaryTermContext(serverUserName, entityDetail);

        if (glossaryTermContext.isEmpty()) {
            log.info("Context not found for the entity {} ", entityDetail.getGUID());
        }

        publishGlossaryTermLineageRelationshipsEvents(glossaryTermContext);
        publishLineageSyncUpdateEvent(entityDetail.getGUID(), glossaryTermContext);

        return glossaryTermContext;
    }

    /**
     * Publishes a {@link LineageRelationshipsEvent} for each entry from the context map
     *
     * @param contextMap the context map to be published
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    private void publishLineageRelationshipsEvents(Multimap<String, RelationshipsContext> contextMap) throws JsonProcessingException,
                                                                                                             ConnectorCheckedException {
        for (String eventType : contextMap.keySet()) {
            for (RelationshipsContext relationshipsContext : contextMap.get(eventType)) {
                if (CollectionUtils.isNotEmpty(relationshipsContext.getRelationships())) {
                    LineageRelationshipsEvent event = new LineageRelationshipsEvent();

                    event.setRelationshipsContext(relationshipsContext);
                    event.setAssetLineageEventType(AssetLineageEventType.getByEventTypeName(eventType));

                    publishEvent(event);
                }
            }
        }
    }


    /**
     * Publishes events for the relationships of an entity based on the context map. The context is built in chunks of relationships configurable
     * by glossaryTermLineageEventsChunkSize.
     *
     * @param contextMap the context map to be published
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    private void publishGlossaryTermLineageRelationshipsEvents(Multimap<String, RelationshipsContext> contextMap) throws JsonProcessingException,
                                                                                                                         ConnectorCheckedException {
        for (String eventType : contextMap.keySet()) {
            for (RelationshipsContext relationshipsContext : contextMap.get(eventType)) {
                if (CollectionUtils.isNotEmpty(relationshipsContext.getRelationships())) {
                    int noOfRelationships = relationshipsContext.getRelationships().size();
                    int batchNumber = (noOfRelationships / batchSize) + 1;
                    Iterator<GraphContext> relationshipsIterator = relationshipsContext.getRelationships().iterator();

                    for (int i = 0; i < batchNumber; i++) {
                        Set<GraphContext> batch = new HashSet<>();
                        for (int j = 0; j < batchSize && relationshipsIterator.hasNext(); j++) {
                            batch.add(relationshipsIterator.next());
                        }
                        RelationshipsContext relationshipContextChunk = new RelationshipsContext();
                        relationshipContextChunk.setRelationships(batch);
                        relationshipContextChunk.setEntityGuid(relationshipsContext.getEntityGuid());
                        LineageRelationshipsEvent event = new LineageRelationshipsEvent();
                        event.setRelationshipsContext(relationshipContextChunk);
                        event.setAssetLineageEventType(AssetLineageEventType.getByEventTypeName(eventType));

                        publishEvent(event);
                    }
                }
            }
        }
    }

    /**
     * Publishes a LINEAGE_SYNC_EVENT which contains the GUID of the entity that was updated and all its neighbour entities' GUIDs
     *
     * @param entityGUID the GUID of the published entity
     * @param contextMap the context map that was published
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    private void publishLineageSyncUpdateEvent(String entityGUID, Multimap<String, RelationshipsContext> contextMap) throws JsonProcessingException,
                                                                                                                            ConnectorCheckedException {
        Set<String> neighbourGuids = new HashSet<>();
        for (String eventType : contextMap.keySet()) {
            for (RelationshipsContext relationshipsContext : contextMap.get(eventType)) {

                List<String> fromVertexGuids = relationshipsContext.getRelationships()
                        .stream()
                        .map(r -> r.getFromVertex().getGuid())
                        .filter(str -> !str.equals(entityGUID))
                        .collect(Collectors.toList());
                List<String> toVertexGuids = relationshipsContext.getRelationships()
                        .stream()
                        .map(r -> r.getToVertex().getGuid())
                        .filter(str -> !str.equals(entityGUID))
                        .collect(Collectors.toList());

                neighbourGuids.addAll(fromVertexGuids);
                neighbourGuids.addAll(toVertexGuids);
            }
        }
        LineageSyncUpdateContext lineageSyncUpdateContext = new LineageSyncUpdateContext();
        lineageSyncUpdateContext.setEntityGUID(entityGUID);
        lineageSyncUpdateContext.setNeighboursGUID(neighbourGuids);
        LineageSyncEvent event = new LineageSyncEvent();
        event.setSyncUpdateContext(lineageSyncUpdateContext);

        event.setAssetLineageEventType(LINEAGE_SYNC_EVENT);
        publishEvent(event);
    }

    /**
     * @param entityDetail          entity to get context
     * @param assetLineageEventType event type to get published
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    public void publishClassificationContext(EntityDetail entityDetail, AssetLineageEventType assetLineageEventType)
            throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, RelationshipsContext> classificationContext = classificationHandler.buildClassificationContext(entityDetail,
                assetLineageEventType);

        if (MapUtils.isEmpty(classificationContext)) {
            log.debug("Lineage classifications not found for the entity {} ", entityDetail.getGUID());
            return;
        }

        publishLineageRelationshipsEvents(Multimaps.forMap(classificationContext));
    }


    /**
     * Publishes a {@link LineageRelationshipEvent} containing a {@link LineageRelationship}
     *
     * @param lineageRelationship the LineageRelationship to be published
     * @param eventType           the type on the event
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishLineageRelationshipEvent(LineageRelationship lineageRelationship, AssetLineageEventType eventType) throws
                                                                                                                          ConnectorCheckedException,
                                                                                                                          JsonProcessingException {
        LineageRelationshipEvent event = new LineageRelationshipEvent();

        event.setLineageRelationship(lineageRelationship);
        event.setAssetLineageEventType(eventType);

        publishEvent(event);
    }


    /**
     * Publishes a {@link LineageRelationshipsEvent} containing a {@link LineageRelationship}
     *
     * @param assetContext the LineageRelationship to be published
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishAssetContextEvent(RelationshipsContext assetContext) throws ConnectorCheckedException, JsonProcessingException {
        LineageRelationshipsEvent event = new LineageRelationshipsEvent();

        event.setRelationshipsContext(assetContext);
        event.setAssetLineageEventType(AssetLineageEventType.ASSET_CONTEXT_EVENT);

        publishEvent(event);
    }

    /**
     * Output a new asset event.
     *
     * @param event event to send
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishEvent(AssetLineageEventHeader event) throws JsonProcessingException, ConnectorCheckedException {
        if (outTopicConnector == null)
            return;

        outTopicConnector.sendEvent(OBJECT_WRITER.writeValueAsString(event));

    }

    /**
     * Publish LineageSyncEvent that contains LineagePublishSummary details.
     *
     * @param summary details about lineage processing and publish activity completed by Asset Lineage OMAS.
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishLineageSummaryEvent(LineagePublishSummary summary) throws JsonProcessingException, ConnectorCheckedException {
        LineageSyncEvent event = new LineageSyncEvent();
        event.setPublishSummary(summary);
        event.setAssetLineageEventType(AssetLineageEventType.LINEAGE_SYNC_EVENT);
        publishEvent(event);
    }

    /**
     * Publish lineage entity event
     *
     * @param lineageEntity    - lineage entity
     * @param lineageEventType - lineage event type
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishLineageEntityEvent(LineageEntity lineageEntity, AssetLineageEventType lineageEventType) throws ConnectorCheckedException,
                                                                                                                      JsonProcessingException {
        LineageEntityEvent event = new LineageEntityEvent();

        event.setLineageEntity(lineageEntity);
        event.setAssetLineageEventType(lineageEventType);

        publishEvent(event);
    }

    /**
     * Publishes a {@link LineageRelationshipEvent} containing a {@link LineageRelationship}. For each end of the relationship it publishes a
     * {@link LineageRelationshipsEvent} containing the column context if available
     *
     * @param lineageRelationship the LineageRelationship to be published
     * @param eventType           the type on the event
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishDataFlowRelationshipEvent(LineageRelationship lineageRelationship, AssetLineageEventType eventType) throws
                                                                                                                                 OCFCheckedExceptionBase,
                                                                                                                                 JsonProcessingException {
        publishLineageRelationshipEvent(lineageRelationship, eventType);

        publishDataFlowContext(lineageRelationship.getSourceEntity());
        publishDataFlowContext(lineageRelationship.getTargetEntity());
    }

    /**
     * Publishes the context for an entity involved in a data flow. If the entity is of type column, it will publish the column context.
     * If the entity is of type asset, it will publish the asset context.
     *
     * @param lineageEntity the lineage entity
     *
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    private void publishDataFlowContext(LineageEntity lineageEntity) throws JsonProcessingException, OCFCheckedExceptionBase {
        publishLineageRelationshipsEvents(Multimaps.forMap(assetContextHandler.buildColumnContext(serverUserName, lineageEntity)));
        publishLineageRelationshipsEvents(Multimaps.forMap(assetContextHandler.buildAssetContext(serverUserName, lineageEntity)));

        Optional<LineageEntity> entityContext = assetContextHandler.buildEntityContext(serverUserName, lineageEntity.getGuid(),
                lineageEntity.getTypeDefName());
        if (entityContext.isPresent()) {
            publishLineageEntityEvent(entityContext.get(), AssetLineageEventType.UPDATE_ENTITY_EVENT);
        }

        log.debug("Asset Lineage OMAS published the context for entity with guid {} and type {}", lineageEntity.getGuid(),
                lineageEntity.getTypeDefName());
    }

    /**
     * Checks if the entity is eligible to be send in a lineage event.
     * The GlossaryTerm and GlossaryCategory entities are send out if has SemanticAssignment and/or TermCategorization relationships
     *
     * @param entityDetail entity to be checked
     *
     * @return true if the entity has the lineage relationships created
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException    problem accessing property server
     */
    public boolean isEntityEligibleForPublishing(EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String typeDefName = entityDetail.getType().getTypeDefName();
        if (typeDefName.equals(GLOSSARY_CATEGORY) || typeDefName.equals(GLOSSARY_TERM)) {
            return glossaryHandler.hasGlossaryTermLineageRelationships(serverUserName, entityDetail);
        }

        return true;
    }
}

