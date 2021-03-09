/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

/**
 * The process context handler provides methods to build lineage context from processes.
 */
public class ProcessContextHandler {

    private static final Logger log = LoggerFactory.getLogger(ProcessContextHandler.class);

    private final AssetContextHandler assetContextHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final List<String> supportedZones;
    private final HandlerHelper handlerHelper;

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
    }

    /**
     * Retrieves the full context for a Process.
     * This context contains the full description for the Port Aliases, Port Implementations, SchemaTypes and Tabular Columns related to the process.
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

    /**
     * Adds lineage context for the tabular column. It adds the lineage mappings for the column and the column context for all the technical assets
     * that have lineage mappings to it.
     *
     * @param userId      userId of user making request.
     * @param context     the context to be updated
     * @param columnGUID  the column GUID
     * @param typeDefName the column type name
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
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
}

