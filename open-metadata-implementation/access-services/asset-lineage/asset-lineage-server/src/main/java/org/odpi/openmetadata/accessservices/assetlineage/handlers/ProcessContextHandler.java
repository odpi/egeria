/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.COLLECTION_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_PORT;

/**
 * The process context handler provides methods to build lineage context from processes.
 */
public class ProcessContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessContextHandler.class);

    private final AssetContextHandler assetContextHandler;
    private final List<String> supportedZones;
    private final HandlerHelper handlerHelper;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param assetContextHandler the asset context handler
     * @param handlerHelper       the helper handler
     * @param supportedZones      configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public ProcessContextHandler(AssetContextHandler assetContextHandler, HandlerHelper handlerHelper, List<String> supportedZones) {
        this.handlerHelper = handlerHelper;
        this.assetContextHandler = assetContextHandler;
        this.supportedZones = supportedZones;
    }

    /**
     * Retrieves the full context for a Process.
     * This context contains the full description for the Port Implementations, SchemaTypes and Tabular Columns related to the process.
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
        handlerHelper.validateAsset(process, methodName, supportedZones);

        String processGUID = process.getGUID();

        Multimap<String, RelationshipsContext> context = ArrayListMultimap.create();

        List<Relationship> collection = handlerHelper.getRelationshipsByType(userId, processGUID, COLLECTION_MEMBERSHIP, PROCESS);
        if (!CollectionUtils.isEmpty(collection)) {
            RelationshipsContext collectionMembershipContext = handlerHelper.buildContextForRelationships(userId, processGUID, collection);
            for (Relationship collectionMembership : collection) {
                EntityDetail collectionEntity = handlerHelper.getEntityAtTheEnd(userId, processGUID, collectionMembership);
                handlerHelper
                        .addContextForRelationships(userId, collectionEntity, COLLECTION_MEMBERSHIP, collectionMembershipContext.getRelationships());
            }
            context.put(AssetLineageEventType.PROCESS_CONTEXT_EVENT.getEventTypeName(), collectionMembershipContext);
        }

        List<Relationship> processPorts = handlerHelper.getRelationshipsByType(userId, processGUID, PROCESS_PORT, PROCESS);
        if (CollectionUtils.isEmpty(processPorts)) {
            log.debug("No relationship ProcessPort has been found for the Process with guid {}", processGUID);
        } else {
            RelationshipsContext relationshipsContext = handlerHelper.buildContextForRelationships(userId, processGUID, processPorts);

            for (Relationship processPort : processPorts) {
                EntityDetail port = handlerHelper.getEntityAtTheEnd(userId, processGUID, processPort);
                addContextForPort(userId, port, relationshipsContext.getRelationships());
            }

            context.put(AssetLineageEventType.PROCESS_CONTEXT_EVENT.getEventTypeName(), relationshipsContext);

            Set<LineageEntity> tabularColumns = relationshipsContext.getRelationships().stream()
                    .filter(relationship -> ATTRIBUTE_FOR_SCHEMA.equalsIgnoreCase(relationship.getRelationshipType()))
                    .map(GraphContext::getToVertex).collect(Collectors.toSet());

            for (LineageEntity tabularColumn : tabularColumns) {
                addContextForColumn(userId, context, tabularColumn.getGuid(), tabularColumn.getTypeDefName(),
                        DATA_FLOW, AssetLineageEventType.DATA_FLOWS_EVENT);
                addContextForColumn(userId, context, tabularColumn.getGuid(), tabularColumn.getTypeDefName(),
                        LINEAGE_MAPPING, AssetLineageEventType.LINEAGE_MAPPINGS_EVENT);
            }
        }
        return context;
    }

    /**
     * Adds context for the tabular column. It adds the specified relationships for the column and the column context for
     * all the technical assets that have specified relationships to it.
     *
     * @param userId                  userId of user making request.
     * @param context                 the context to be updated
     * @param columnGUID              the column GUID
     * @param typeDefName             the column type name
     * @param relationshipTypeDefName the relationship type name
     * @param assetLineageEventType   the asset lineage event type
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addContextForColumn(String userId, Multimap<String, RelationshipsContext> context, String columnGUID,
                                    String typeDefName, String relationshipTypeDefName, AssetLineageEventType assetLineageEventType)
            throws OCFCheckedExceptionBase {
        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, columnGUID, relationshipTypeDefName, typeDefName);

        context.put(assetLineageEventType.getEventTypeName(),
                handlerHelper.buildContextForRelationships(userId, columnGUID, relationships));

        for (Relationship relationship : relationships) {
            context.putAll(Multimaps.forMap(assetContextHandler.buildSchemaElementContext(userId, handlerHelper.getEntityAtTheEnd(userId,
                    columnGUID, relationship))));
        }
    }

    /**
     * Adds the port context. If it's a Port Implementation it will add the context for the internal columns as well
     *
     * @param userId               userId of user making request.
     * @param port                 the port entity for which the context is being updated
     * @param relationshipsContext the context to be updated
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addContextForPort(String userId, EntityDetail port, Set<GraphContext> relationshipsContext) throws OCFCheckedExceptionBase {
        String portType = port.getType().getTypeDefName();

        if (PORT_IMPLEMENTATION.equals(portType)) {
            EntityDetail tabularSchemaType = handlerHelper.addContextForRelationships(userId, port, PORT_SCHEMA, relationshipsContext);

            EntityDetail tabularColumn = handlerHelper.addContextForRelationships(userId, tabularSchemaType, ATTRIBUTE_FOR_SCHEMA,
                    relationshipsContext);

            if (tabularColumn == null) {
                log.warn("No entity TabularColumn has been found for the PortImplementation with guid {}", port.getGUID());
            }
        }
    }
}

