/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ANCHOR_GUID;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.AVRO_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CSV_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DOCUMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.JSON_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.KEYSTORE_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LOG_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.MEDIA_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TABULAR_COLUMN;

/**
 * The Asset Context Handler provides methods to build graph context for schema elements.
 */
public class AssetContextHandler {

    private final RepositoryHandler repositoryHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final HandlerHelper handlerHelper;
    private final List<String> supportedZones;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler    handler for invalid parameters
     * @param repositoryHelper           helper used by the converters
     * @param repositoryHandler          handler for calling the repository services
     * @param supportedZones             configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     * @param lineageClassificationTypes lineage classification list
     */
    public AssetContextHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                               RepositoryHandler repositoryHandler, List<String> supportedZones, Set<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
        this.supportedZones = supportedZones;
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
        Set<GraphContext> columnContext = new HashSet<>();

        final String typeDefName = entityDetail.getType().getTypeDefName();
        switch (typeDefName) {
            case TABULAR_COLUMN:
                if (!isInternalTabularColumn(userId, entityDetail)) {
                    EntityDetail schemaType = handlerHelper.addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, columnContext);

                    handlerHelper.addContextForRelationships(userId, schemaType, ASSET_SCHEMA_TYPE, columnContext);

                    context.put(AssetLineageEventType.COLUMN_CONTEXT_EVENT.getEventTypeName(), new RelationshipsContext(entityDetail.getGUID(),
                            columnContext));
                }
                break;
            case RELATIONAL_COLUMN:
                handlerHelper.addContextForRelationships(userId, entityDetail, NESTED_SCHEMA_ATTRIBUTE, columnContext);

                context.put(AssetLineageEventType.COLUMN_CONTEXT_EVENT.getEventTypeName(), new RelationshipsContext(entityDetail.getGUID(),
                        columnContext));
                break;
        }

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

        final String typeDefName = entityDetail.getType().getTypeDefName();
        switch (typeDefName) {
            case AVRO_FILE:
            case CSV_FILE:
            case JSON_FILE:
            case KEYSTORE_FILE:
            case LOG_FILE:
            case MEDIA_FILE:
            case DOCUMENT:
            case DATA_FILE:
                context = buildDataFileContext(userId, entityDetail);
                break;

            case RELATIONAL_TABLE:
                context = buildRelationalTableContext(userId, entityDetail);
                break;
        }

        return context;
    }

    /**
     * Builds the column context for a schema element
     *
     * @param guid the unique identifier of the column
     *
     * @return the columnn context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    public Map<String, RelationshipsContext> buildColumnContext(String userId, String guid) throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = handlerHelper.getEntityDetails(userId, guid, TABULAR_COLUMN);

        return buildSchemaElementContext(userId, entityDetail);
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
                TABULAR_COLUMN);
        if (!relationship.isPresent()) {
            return false;
        }

        EntityDetail schemaType = handlerHelper.getEntityAtTheEnd(userId, tabularColumn.getGUID(), relationship.get());
        Optional<Classification> anchorGUIDClassification = getAnchorsClassification(schemaType);
        if (!anchorGUIDClassification.isPresent()) {
            return false;
        }
        Optional<String> anchorGUID = getAnchorGUID(anchorGUIDClassification.get());
        if (!anchorGUID.isPresent()) {
            return false;
        }

        return repositoryHandler.isEntityATypeOf(userId, anchorGUID.get(), ANCHOR_GUID, PORT_IMPLEMENTATION, methodName);
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
}
