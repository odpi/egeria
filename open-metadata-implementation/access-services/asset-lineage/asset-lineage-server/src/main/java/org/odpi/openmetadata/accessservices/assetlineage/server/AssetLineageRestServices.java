/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.HandlerHelper;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineagePublishSummary;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;

/**
 * The AssetLineageRESTServices provides the server-side implementation of the Asset Lineage Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageRestServices.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private AtomicBoolean publishLineageTaskActive = new AtomicBoolean(false);

    private static final String PROCESS_STARTED = "PROCESS_STARTED";
    private static final String PROCESS_COMPLETED = "PROCESS_COMPLETED";
    private static final String ENTITY_CONTEXT_PUBLISHED = "ENTITY_CONTEXT_PUBLISHED";
    private static final String ENTITY_CONTEXT_ERROR = "ENTITY_CONTEXT_ERROR";


    /**
     * Default constructor
     */
    public AssetLineageRestServices() {
        instanceHandler.registerAccessService();
    }


    /**
     * //TODO: To be renamed to publishLineage()
     *
     * publishLineage interface is used to request lineage related metadata to be sent out on-demand for scenarios such as initial load of metadata or pull based changes.
     * The method provides non-blocking like, async interface for executing time/resource intensive tasks in background thread (i.e. building lineage context for collection of entities).
     * It is implemented using concurrent processing APIs based on {@link java.util.concurrent.CompletableFuture}
     *
     * How it works:
     * Based on the findEntitiesParameters provided, scans the cohort for the given type and finds list of matching entities.
     * Providing updatedAfter filed in findEntitiesParameters will retrieve only the entities that were changed from that point in time.
     * Once entities are found, it spins up *asynchronous* background task that will build the lineage context and publish respective output as events on the asset-lineage output topic.
     * Note that only single background task can be active at given time (per server instance controlled by `publishLineageTaskActive`). All subsequent requests will result in empty response until the task if finished.
     * At the end lineage sync event is published to notify the external systems sending summary of the work completed.
     *
     * @param serverName             name of server instance to call
     * @param userId                 the name of the calling user
     * @param entityType             the type of the entity to search for
     * @param findEntitiesParameters filtering used to reduce the scope of the search
     *
     * @return a collection of unique identifiers (guids) of the available entities that will produce lineage events.
     *  OR empty collection in case there is noting to be processed or the background task is already busy (active).
     */
    public GUIDListResponse publishEntities(String serverName, String userId, String entityType,
                                            FindEntitiesParameters findEntitiesParameters) {

        String methodName = "publishEntities";
        GUIDListResponse response = new GUIDListResponse();

        try {

            HandlerHelper handlerHelper = instanceHandler.getHandlerHelper(userId, serverName, methodName);
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SearchProperties searchProperties = handlerHelper.getSearchPropertiesAfterUpdateTime(findEntitiesParameters.getUpdatedAfter());
            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);

            if (!publishLineageTaskActive.get()) {

                Long cutOffTime = System.currentTimeMillis();
                Optional<List<EntityDetail>> entitiesByTypeName = handlerHelper.findEntitiesByType(userId, entityType, searchProperties, findEntitiesParameters);

                if (!entitiesByTypeName.isPresent()) {
                    return response;
                }

                response.setGUIDs(entitiesByTypeName.get().stream().map(InstanceHeader::getGUID).collect(Collectors.toList()));

                CompletableFuture.supplyAsync(buildAndPublishLineageContext(auditLog,publisher,entitiesByTypeName, entityType))
                        .thenAccept((result) ->
                            result.ifPresent(publishedItems -> {
                                sendLineagePublishSummary(publishedItems, cutOffTime, publisher, auditLog);
                                publishLineageTaskActive.set(false);
                            })
                        );
            }


        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    /**
     * Supplier of lineage processing result for input entities
     *
     * @param auditLog instance of auditLog logging interface
     * @param publisher instance of the asset-lineage topic publisher
     * @param entities entity detail collection to be processed
     * @return Supplier object to be used for async processing
     */
    private Supplier<Optional<List<String>>> buildAndPublishLineageContext(AuditLog auditLog, AssetLineagePublisher publisher, Optional<List<EntityDetail>> entities, String entityType) {
      return () -> {
          String methodName = "buildAndPublishLineageContext";
          Optional<List<String>> result = Optional.empty();
          publishLineageTaskActive.set(true);
          if(entities.isPresent()) {
              auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition(PROCESS_STARTED, entityType, String.valueOf(entities.get().size())));
              result = Optional.of(entities.get().parallelStream().map(entityDetail -> publishEntityContext(publisher, entityDetail, auditLog)).collect(Collectors.toList()));
              CollectionUtils.filter(result.get(), PredicateUtils.notNullPredicate());
              auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition(PROCESS_COMPLETED, entityType, String.valueOf(result.get().size())));
          }
          return result;
      };
    }

    /**
     * Find the entity the element with the provided guid and the given type
     * Publish the context for the entity on the AL OMAS out Topic
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param entityType the type of the entity to search for
     * @param guid       the guid of the searched entity
     *
     * @return the unique identifier (guid) of the entity that was processed
     */
    public GUIDListResponse publishEntity(String serverName, String userId, String entityType, String guid) {
        GUIDListResponse response = new GUIDListResponse();

        String methodName = "publishEntity";

        try {
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            HandlerHelper handlerHelper = instanceHandler.getHandlerHelper(userId, serverName, methodName);
            EntityDetail entity = handlerHelper.getEntityDetails(userId, guid, entityType);
            if (entity == null) {
                auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("ENTITY_NOT_FOUND", entityType, guid));
                return response;
            }

            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("ENTITY_FOUND", entityType, guid));
            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);

            String publishedEntitiesContext = publishEntityContext(publisher, entity, auditLog);
            response.setGUIDs(Collections.singletonList(publishedEntitiesContext));

        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }
        return response;
    }

    /**
     * Returns GUID that was published on the Out Topic
     *
     * @param entityDetail the entity for which the event is published
     * @param publisher    Asset Lineage publisher
     *
     * @return the GUID published on the Asset Lineage Out Topic
     */
    private String publishEntityContext(AssetLineagePublisher publisher, EntityDetail entityDetail, AuditLog auditLog) {
        String methodName = "publishEntityContext";

        try {
            String result = publishContext(entityDetail, publisher);
            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition(ENTITY_CONTEXT_PUBLISHED,
                    entityDetail.getType().getTypeDefName(), entityDetail.getGUID()));
            return result;
        } catch (Exception e) {
            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition(ENTITY_CONTEXT_ERROR,
                    entityDetail.getType().getTypeDefName(), entityDetail.getGUID()));
            auditLog.logException(methodName, AssetLineageAuditCode.ENTITY_ERROR.getMessageDefinition(entityDetail.getType().getTypeDefName(),
                    entityDetail.getGUID()), e);
        }
        return null;
    }

    /**
     * Build entity's context and publish it on Out Topic
     *
     * @param entityDetail - the entity based on which we want to build the context
     * @param publisher    Asset Lineage publisher
     *
     * @return the entity GUID if the context is not empty
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    private String publishContext(EntityDetail entityDetail, AssetLineagePublisher publisher) throws OCFCheckedExceptionBase,
                                                                                                     JsonProcessingException {
        String typeName = entityDetail.getType().getTypeDefName();
        Multimap<String, RelationshipsContext> context = ArrayListMultimap.create();
        switch (typeName) {
            case GLOSSARY_TERM: {
                context = publisher.publishGlossaryContext(entityDetail);
                break;
            }
            case PROCESS: {
                context = publisher.publishProcessContext(entityDetail);
                break;
            }
            default:
                log.error("Unsupported typeName {} for entity with guid {}. The context can not be published",
                        typeName, entityDetail.getGUID());
                break;
        }

        if (!context.isEmpty()) {
            return entityDetail.getGUID();
        }

        return null;
    }

    /**
     * Publish asset context on the Out Topic. Provides a list with asset context entities' GUIDs.
     *
     * @param serverName the server name
     * @param userId     the user id
     * @param entityType the entity type
     * @param guid       the GUID of the entity for which the context is published
     * @return a list with asset context entities' GUIDs
     */
    public GUIDListResponse publishAssetContext(String serverName, String userId, String entityType, String guid) {
        String methodName = "publishAssetContext";
        GUIDListResponse response = new GUIDListResponse();

        try {
            AssetContextHandler assetContextHandler = instanceHandler.getAssetContextHandler(userId, serverName, methodName);
            HandlerHelper handlerHelper = instanceHandler.getHandlerHelper(userId, serverName, methodName);
            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EntityDetail entity = handlerHelper.getEntityDetails(userId, guid, entityType);
            RelationshipsContext assetContext = assetContextHandler.buildAssetContext(userId, entity);
            publisher.publishAssetContextEvent(assetContext);

            auditLog.logMessage(methodName, AssetLineageAuditCode.ASSET_CONTEXT_INFO.getMessageDefinition(guid));
            List<String> guids = getEntityGUIDsFromAssetContext(assetContext);
            response.setGUIDs(guids);

        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (OCFCheckedExceptionBase | JsonProcessingException e) {
            restExceptionHandler.captureThrowable(response, e, methodName);
        }

        return response;
    }

    /**
     *
     * Publishes summary for completed publishLineage processing sequence.
     *
     * @param publishedGUIDs list of guids published as result of the processing.
     * @param lineageTimestamp the point in time for which lineage process was executed
     * @param publisher instance of the asset-lineage topic publisher
     * @param auditLog instance of auditLog logging interface
     */
    private void sendLineagePublishSummary(List<String> publishedGUIDs, Long lineageTimestamp, AssetLineagePublisher publisher, AuditLog auditLog) {
        String methodName = "sendLineagePublishSummary";
        try {
            LineagePublishSummary publishSummary = new LineagePublishSummary();
            publishSummary.setItems(publishedGUIDs);
            publishSummary.setLineageTimestamp(lineageTimestamp);
            publisher.publishLineageSummaryEvent(publishSummary);
        } catch (JsonProcessingException | ConnectorCheckedException e) {
            auditLog.logException(methodName, AssetLineageAuditCode.PUBLISH_EVENT_ERROR.getMessageDefinition(),e);
        }
    }

    private List<String> getEntityGUIDsFromAssetContext(RelationshipsContext assetContext) {
        Set<String> guids = new HashSet<>();
        assetContext.getRelationships()
                .forEach(relationship ->  {
                    guids.add(relationship.getFromVertex().getGuid());
                    guids.add(relationship.getToVertex().getGuid());
                });
        return new ArrayList<>(guids);
    }

}
