/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.GenericStub;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.ClockService;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ANCHOR_GUID;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.EVENT_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_TYPE_OPTION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_FILE_COLUMN;

/**
 * The Asset Context Handler provides methods to build graph context for schema elements.
 */
public class AssetContextHandler {

    private final OpenMetadataAPIGenericHandler<GenericStub> genericHandler;
    private final HandlerHelper handlerHelper;
    private final List<String> supportedZones;
    private final ClockService clockService;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param genericHandler    handler for calling the repository services
     * @param handlerHelper     helper handler
     * @param supportedZones    configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     * @param clockService      clock service
     */
    public AssetContextHandler(OpenMetadataAPIGenericHandler<GenericStub> genericHandler, HandlerHelper handlerHelper,
                               List<String> supportedZones, ClockService clockService) {
        this.genericHandler = genericHandler;
        this.handlerHelper = handlerHelper;
        this.supportedZones = supportedZones;
        this.clockService = clockService;
    }

    /**
     * Builds the context for a schema element without the asset context.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, RelationshipsContext> buildSchemaElementContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        final String methodName = "buildSchemaElementContext";
        handlerHelper.validateAsset(entityDetail, methodName, supportedZones);

        Map<String, RelationshipsContext> context = new HashMap<>();
        final String typeDefName = entityDetail.getType().getTypeDefName();
        Set<GraphContext> columnContext = new HashSet<>();
        switch (typeDefName) {
            case TABULAR_COLUMN:
                if (!isInternalTabularColumn(userId, entityDetail)) {
                    columnContext = buildTabularColumnContext(userId, entityDetail);
                }
                break;
            case TABULAR_FILE_COLUMN:
                columnContext = buildTabularColumnContext(userId, entityDetail);
                break;
            case RELATIONAL_COLUMN:
                columnContext = buildRelationalColumnContext(userId, entityDetail);
                break;
            case EVENT_SCHEMA_ATTRIBUTE:
                columnContext = buildEventSchemaAttributeContext(userId, entityDetail);
                break;
            default:
               return context;
        }

        context.put(AssetLineageEventType.COLUMN_CONTEXT_EVENT.getEventTypeName(), new RelationshipsContext(entityDetail.getGUID(), columnContext));
        return context;
    }

    /**
     * Builds the asset context for a schema element.
     *
     * @param userId        the unique identifier for the user
     * @param lineageEntity the entity for which the context is build
     *
     * @return the asset context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, RelationshipsContext> buildAssetContext(String userId, LineageEntity lineageEntity)
            throws OCFCheckedExceptionBase {

        Map<String, RelationshipsContext> context = new HashMap<>();

        EntityDetail asset = handlerHelper.getEntityDetails(userId, lineageEntity.getGuid(), lineageEntity.getTypeDefName());
        context.put(AssetLineageEventType.ASSET_CONTEXT_EVENT.getEventTypeName(), buildAssetContext(userId, asset));
        return context;
    }

    /**
     * Builds the asset context for a schema element.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the asset context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public RelationshipsContext buildAssetContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        final String methodName = "buildAssetContext";
        handlerHelper.validateAsset(entityDetail, methodName, supportedZones);
        RelationshipsContext context = new RelationshipsContext();

        if (handlerHelper.isDataStore(userId, entityDetail)) {
            context = buildDataFileContext(userId, entityDetail);
        }

        if (handlerHelper.isTable(userId, entityDetail)) {
            context = buildRelationalTableContext(userId, entityDetail);
        }

        if (handlerHelper.isTopic(userId, entityDetail)) {
            context = buildTopicContext(userId, entityDetail);
        }

        return context;
    }

    /**
     * Builds the column context for a schema element
     *
     * @param userId calling user
     * @param lineageEntity column as lineage entity
     *
     * @return column context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, RelationshipsContext> buildColumnContext(String userId, LineageEntity lineageEntity)
            throws OCFCheckedExceptionBase {
        if (!handlerHelper.isSchemaAttribute(userId, lineageEntity.getTypeDefName())) {
            return new HashMap<>();
        }
        EntityDetail entityDetail = handlerHelper.getEntityDetails(userId, lineageEntity.getGuid(), SCHEMA_ATTRIBUTE);

        return buildSchemaElementContext(userId, entityDetail);
    }

    /**
     * Returns the asset entity context in lineage format
     *
     * @param userId      the unique identifier for the user
     * @param guid        the guid of the entity for which the context is build
     * @param typeDefName the type def name of the entity for which the context is build
     *
     * @return the asset entity context in lineage format
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Optional<LineageEntity> buildEntityContext(String userId, String guid, String typeDefName) throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = handlerHelper.getEntityDetails(userId, guid, typeDefName);
        return Optional.of(handlerHelper.getLineageEntity(entityDetail));
    }

    /**
     * Validates that an entity is internal to DataEngine OMAS
     *
     * @param userId        the unique identifier for the user
     * @param tabularColumn the column to validate
     *
     * @return true if it's internal, false otherwise
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private boolean isInternalTabularColumn(String userId, EntityDetail tabularColumn) throws OCFCheckedExceptionBase {
        String methodName = "isInternalTabularColumn";

        Optional<Relationship> relationship = handlerHelper.getUniqueRelationshipByType(userId, tabularColumn.getGUID(), ATTRIBUTE_FOR_SCHEMA,
                tabularColumn.getType().getTypeDefName());
        if (relationship.isEmpty()) {
            return false;
        }

        EntityDetail schemaType = handlerHelper.getEntityAtTheEnd(userId, tabularColumn.getGUID(), relationship.get());
        Optional<Classification> anchorGUIDClassification = getAnchorsClassification(schemaType);
        if (anchorGUIDClassification.isEmpty()) {
            return false;
        }
        Optional<String> anchorGUID = getAnchorGUID(anchorGUIDClassification.get());
        if (anchorGUID.isEmpty()) {
            return false;
        }

        return genericHandler.isEntityATypeOf(userId, anchorGUID.get(), ANCHOR_GUID, PORT_IMPLEMENTATION, true,
                false, clockService.getNow(), methodName);
    }

    /**
     * Retrieves the anchorGUID property form a classification
     *
     * @param classification the classification
     *
     * @return the anchorGUID property or an empty optional
     */
    private Optional<String> getAnchorGUID(Classification classification) {
        InstancePropertyValue anchorGUIDProperty = classification.getProperties().getPropertyValue(ANCHOR_GUID);
        if (anchorGUIDProperty == null) {
            return Optional.empty();
        }
        return Optional.of(anchorGUIDProperty.valueAsString());
    }

    /**
     * Retrieves the Anchors classification from an entity
     *
     * @param entityDetail the entity to check for the classification
     *
     * @return the Anchors classification or an empty Optional if missing
     */
    private Optional<Classification> getAnchorsClassification(EntityDetail entityDetail) {
        List<Classification> classifications = entityDetail.getClassifications();
        if (CollectionUtils.isEmpty(classifications)) {
            return Optional.empty();
        }
        for (Classification classification : classifications) {
            if ("Anchors".equalsIgnoreCase(classification.getName()))
                return Optional.of(classification);
        }
        return Optional.empty();
    }

    /**
     * Builds the relational table context for a relational column.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the relational table context of the relational column
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private RelationshipsContext buildRelationalTableContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> context = new HashSet<>();

        EntityDetail schemaType = handlerHelper.addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, context);

        EntityDetail deployedSchemaType = handlerHelper.addContextForRelationships(userId, schemaType, ASSET_SCHEMA_TYPE, context);

        EntityDetail database = handlerHelper.addContextForRelationships(userId, deployedSchemaType, DATA_CONTENT_FOR_DATA_SET, context);

        if (database != null) {
            addConnectionToAssetContext(userId, database, context);
        }

        return new RelationshipsContext(entityDetail.getGUID(), context);
    }

    /**
     * Adds the connection to asset context for an asset.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     * @param context      the context to be updated
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addConnectionToAssetContext(String userId, EntityDetail entityDetail, Set<GraphContext> context) throws OCFCheckedExceptionBase {
        EntityDetail connection = handlerHelper.addContextForRelationships(userId, entityDetail, CONNECTION_TO_ASSET, context);

        handlerHelper.addContextForRelationships(userId, connection, CONNECTION_ENDPOINT, context);
    }

    /**
     * Builds the data file context for a tabular column.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the data file context of the tabular column
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private RelationshipsContext buildDataFileContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> context = new HashSet<>();

        addConnectionToAssetContext(userId, entityDetail, context);

        EntityDetail fileFolder = handlerHelper.addContextForRelationships(userId, entityDetail, NESTED_FILE, context);

        addContextForFileFolder(userId, fileFolder, context);

        return new RelationshipsContext(entityDetail.getGUID(), context);
    }

    /**
     * Adds the file folder context for a data file.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     * @param context      the context to be updated
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private void addContextForFileFolder(String userId, EntityDetail entityDetail, Set<GraphContext> context) throws OCFCheckedExceptionBase {

        if (entityDetail == null) {
            return;
        }

        EntityDetail fileFolder = handlerHelper.addContextForRelationships(userId, entityDetail, FOLDER_HIERARCHY, context);

        if (fileFolder != null) {
            //recursively build the nested folder structure
            addContextForFileFolder(userId, fileFolder, context);
        } else {
            // build the context for the Connection
            addConnectionToAssetContext(userId, entityDetail, context);
        }
    }


    /**
     * Builds the column context for a RelationalColumn
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the column context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Set<GraphContext> buildRelationalColumnContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> columnContext = new HashSet<>();
        handlerHelper.addContextForRelationships(userId, entityDetail, NESTED_SCHEMA_ATTRIBUTE, columnContext);
        return columnContext;
    }

    /**
     * Builds the column context for a TabularColumn or TabularFileColumn
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the column context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Set<GraphContext> buildTabularColumnContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> columnContext = new HashSet<>();
        EntityDetail schemaType = handlerHelper.addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, columnContext);
        handlerHelper.addContextForRelationships(userId, schemaType, ASSET_SCHEMA_TYPE, columnContext);
        return columnContext;
    }

    /**
     * Builds the topic context
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the topic context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private RelationshipsContext buildTopicContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> context = new HashSet<>();

        handlerHelper.addContextForRelationships(userId, entityDetail, ASSET_SCHEMA_TYPE, context);

        return new RelationshipsContext(entityDetail.getGUID(), context);
    }

    /**
     * Builds the event schema attribute context
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the topic context
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private Set<GraphContext> buildEventSchemaAttributeContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        Set<GraphContext> columnContext = new HashSet<>();

        EntityDetail eventType = handlerHelper.addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, columnContext);
        EntityDetail eventTypeList = handlerHelper.addContextForRelationships(userId, eventType, SCHEMA_TYPE_OPTION, columnContext);

        handlerHelper.addContextForRelationships(userId, eventTypeList, ASSET_SCHEMA_TYPE, columnContext);
        return columnContext;
    }
}
