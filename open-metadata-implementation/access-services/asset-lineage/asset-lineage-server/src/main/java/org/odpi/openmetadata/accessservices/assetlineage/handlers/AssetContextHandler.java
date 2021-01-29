/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;
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
     * @param userId         the unique identifier for the user
     * @param entityTypeName the name of the entity type
     *
     * @return the existing list of glossary terms available in the repository
     *
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public List<EntityDetail> getEntitiesByTypeName(String userId, String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        final String methodName = "getEntitiesByTypeName";

        String typeDefGUID = handlerHelper.getTypeByName(userId, entityTypeName);

        return repositoryHandler.getEntitiesByType(userId, typeDefGUID, 0, 0, methodName);
    }

    /**
     * @param userId         the unique identifier for the user
     * @param guid           the guid of the entity
     * @param entityTypeName the name of the entity type
     *
     * @return the existing list of glossary terms available in the repository
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    public EntityDetail getEntityByTypeAndGuid(String userId, String guid, String entityTypeName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException {
        return handlerHelper.getEntityDetails(userId, guid, entityTypeName);
    }

    /**
     * Builds the context for a schema element.
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

        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        invalidParameterHandler.validateAssetInSupportedZone(entityDetail.getGUID(), GUID_PARAMETER,
                handlerHelper.getAssetZoneMembership(entityDetail.getClassifications()), supportedZones, ASSET_LINEAGE_OMAS, methodName);

        Map<String, RelationshipsContext> context = new HashMap<>();
        Set<GraphContext> columnContext = new HashSet<>();

        context.put(AssetLineageEventType.LINEAGE_MAPPINGS_EVENT.getEventTypeName(), buildLineageMappingsContext(userId, entityDetail));
        final String typeDefName = entityDetail.getType().getTypeDefName();
        switch (typeDefName) {
            case TABULAR_COLUMN:
                EntityDetail schemaType = addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, columnContext);

                EntityDetail dataFile = addContextForRelationships(userId, schemaType, ASSET_SCHEMA_TYPE, columnContext);

                context.put(AssetLineageEventType.COLUMN_CONTEXT_EVENT.getEventTypeName(), new RelationshipsContext(entityDetail.getGUID(),
                        columnContext));

                if (dataFile != null) {
                    context.put(AssetLineageEventType.ASSET_CONTEXT_EVENT.getEventTypeName(), buildDataFileContext(userId, dataFile));
                }
                break;

            case RELATIONAL_COLUMN:
                EntityDetail relationalTable = addContextForRelationships(userId, entityDetail, NESTED_SCHEMA_ATTRIBUTE, columnContext);

                context.put(AssetLineageEventType.COLUMN_CONTEXT_EVENT.getEventTypeName(), new RelationshipsContext(entityDetail.getGUID(),
                        columnContext));

                if (relationalTable != null) {
                    context.put(AssetLineageEventType.ASSET_CONTEXT_EVENT.getEventTypeName(), buildRelationalTableContext(userId, relationalTable));
                }
                break;
        }

        return context;
    }

    /**
     * Builds the lineage mappings context for a schema element.
     *
     * @param userId       the unique identifier for the user
     * @param entityDetail the entity for which the context is build
     *
     * @return the lineage mappings context of the schema element
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private RelationshipsContext buildLineageMappingsContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, entityDetail.getGUID(), LINEAGE_MAPPING,
                entityDetail.getType().getTypeDefName());

        return handlerHelper.buildContextForRelationships(userId, entityDetail.getGUID(), relationships);
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

        EntityDetail schemaType = addContextForRelationships(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, context);

        EntityDetail deployedSchemaType = addContextForRelationships(userId, schemaType, ASSET_SCHEMA_TYPE, context);

        EntityDetail database = addContextForRelationships(userId, deployedSchemaType, DATA_CONTENT_FOR_DATA_SET, context);

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
        EntityDetail connection = addContextForRelationships(userId, entityDetail, CONNECTION_TO_ASSET, context);

        addContextForRelationships(userId, connection, CONNECTION_ENDPOINT, context);
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

        EntityDetail fileFolder = addContextForRelationships(userId, entityDetail, NESTED_FILE, context);

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

        EntityDetail fileFolder = addContextForRelationships(userId, entityDetail, FOLDER_HIERARCHY, context);

        if (fileFolder != null) {
            //recursively build the nested folder structure
            addContextForFileFolder(userId, fileFolder, context);
        } else {
            // build the context for the Connection
            addConnectionToAssetContext(userId, entityDetail, context);
        }
    }

    /**
     * Adds the relationships context for an entity, based on the relationship type.
     *
     * @param userId               the unique identifier for the user
     * @param startEntity          the start entity for the relationships
     * @param relationshipTypeName the type of the relationship for which the context is built
     * @param context              the context to be updated
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     */
    private EntityDetail addContextForRelationships(String userId, EntityDetail startEntity, String relationshipTypeName,
                                                    Set<GraphContext> context) throws OCFCheckedExceptionBase {
        if (startEntity == null) {
            return null;
        }

        context.addAll(handlerHelper.buildContextForLineageClassifications(startEntity).getRelationships());

        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, startEntity.getGUID(), relationshipTypeName,
                startEntity.getType().getTypeDefName());
        if (CollectionUtils.isEmpty(relationships)) {
            return null;
        }

        if (startEntity.getType().getTypeDefName().equals(FILE_FOLDER)) {
            relationships = relationships.stream().filter(relationship ->
                    relationship.getEntityTwoProxy().getGUID().equals(startEntity.getGUID())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(relationships)) {
                return null;
            }
        }

        context.addAll(handlerHelper.buildContextForRelationships(userId, startEntity.getGUID(), relationships).getRelationships());

        return handlerHelper.getEntityAtTheEnd(userId, startEntity.getGUID(), relationships.get(0));
    }
}
