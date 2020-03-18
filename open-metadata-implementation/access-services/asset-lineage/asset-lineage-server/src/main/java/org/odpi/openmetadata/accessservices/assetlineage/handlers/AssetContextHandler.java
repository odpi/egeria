/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.*;

/**
 * The Asset Context handler provides methods to build graph context for assets that has been created.
 */
public class AssetContextHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetContextHandler.class);

    private final RepositoryHandler repositoryHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final HandlerHelper handlerHelper;
    private final List<String> supportedZones;

    private AssetContext graph;

    /**
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param supportedZones          configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public AssetContextHandler(InvalidParameterHandler invalidParameterHandler,
                               OMRSRepositoryHelper repositoryHelper,
                               RepositoryHandler repositoryHandler,
                               List<String> supportedZones) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler);
        this.supportedZones = supportedZones;
    }


    /**
     * Gets asset context.
     *
     * @param userId the user id
     * @param guid   the guid
     * @param type   the type
     * @return the asset context
     */
    public AssetContext getAssetContext(String userId, String guid, String type) throws OCFCheckedExceptionBase {
        final String methodName = "getAssetContext";

        graph = new AssetContext();

        invalidParameterHandler.validateGUID(guid, GUID_PARAMETER, methodName);

        Optional<EntityDetail> entityDetail = getEntityDetails(userId, guid, type);
        if (!entityDetail.isPresent()) {
            log.error("Something is wrong in the OMRS Connector when a specific operation is performed in the metadata collection." +
                    " Entity not found with guid {}", guid);

            throw new AssetLineageException(ENTITY_NOT_FOUND.getHTTPErrorCode(),
                    this.getClass().getName(),
                    "Retrieving Entity",
                    ENTITY_NOT_FOUND.getErrorMessage(),
                    ENTITY_NOT_FOUND.getSystemAction(),
                    ENTITY_NOT_FOUND.getUserAction());
        }

        invalidParameterHandler.validateAssetInSupportedZone(guid,
                GUID_PARAMETER,
                handlerHelper.getAssetZoneMembership(entityDetail.get().getClassifications()),
                supportedZones,
                ASSET_LINEAGE_OMAS,
                methodName);

        buildAssetContext(userId, entityDetail.get());
        return graph;


    }

    private Optional<EntityDetail> getEntityDetails(String userId, String guid, String type) throws OCFCheckedExceptionBase {
        final String methodName = "getEntityDetails";
        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER, type, methodName));
    }


    private void buildAssetContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        final String typeDefName = entityDetail.getType().getTypeDefName();

        if (typeDefName.equals(RELATIONAL_TABLE) || typeDefName.equals(DATA_FILE)) {
            addContextForSchemaAttributeType(userId, entityDetail, typeDefName);
        }

        List<EntityDetail> tableTypeEntities = buildGraphByRelationshipType(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, typeDefName, false);

        if (tableTypeEntities.isEmpty()) {
            tableTypeEntities = buildGraphByRelationshipType(userId, entityDetail, NESTED_SCHEMA_ATTRIBUTE, typeDefName, false);
        }
        for (EntityDetail schemaTypeEntity : tableTypeEntities) {
            if (isComplexSchemaType(userId, schemaTypeEntity.getType().getTypeDefName())) {
                setAssetDetails(userId, schemaTypeEntity);
            } else {
                buildAssetContext(userId, tableTypeEntities.stream().findFirst().get());
            }
        }
    }

    private List<EntityDetail> buildGraphByRelationshipType(String userId, EntityDetail startEntity,
                                                            String relationshipType, String typeDefName, boolean changeDirection) throws OCFCheckedExceptionBase {
        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, typeDefName);

        if (startEntity.getType().getTypeDefName().equals(FILE_FOLDER)) {
            relationships = relationships.stream().filter(relationship ->
                    relationship.getEntityTwoProxy().getGUID().equals(startEntity.getGUID())).collect(Collectors.toList());
        }

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {

            EntityDetail endEntity = handlerHelper.buildGraphEdgeByRelationship(userId, startEntity, relationship, graph, changeDirection);
            if (endEntity == null) return Collections.emptyList();

            entityDetails.add(endEntity);
        }
        return entityDetails;
    }

    private void setAssetDetails(String userId, EntityDetail startEntity) throws OCFCheckedExceptionBase {
        List<EntityDetail> assetEntity = buildGraphByRelationshipType(userId, startEntity, ASSET_SCHEMA_TYPE, startEntity.getType().getTypeDefName(), false);
        Optional<EntityDetail> first = assetEntity.stream().findFirst();
        if (first.isPresent()) {
            buildAsset(userId, first.get());

        }
    }

    private void buildAsset(String userId, EntityDetail dataSet) throws OCFCheckedExceptionBase {
        final String typeDefName = dataSet.getType().getTypeDefName();
        List<EntityDetail> entityDetails;
        if (typeDefName.equals(DATA_FILE)) {
            entityDetails = buildGraphByRelationshipType(userId, dataSet, NESTED_FILE, typeDefName, false);
        } else {
            entityDetails = buildGraphByRelationshipType(userId, dataSet, DATA_CONTENT_FOR_DATA_SET, typeDefName, false);
        }

        if (CollectionUtils.isEmpty(entityDetails)) {
            return;
        }
        addContextForEndpoints(userId, entityDetails.toArray(new EntityDetail[0]));
    }


    private void addContextForEndpoints(String userId, EntityDetail... entityDetails) throws OCFCheckedExceptionBase {
        for (EntityDetail entityDetail : entityDetails) {
            if (entityDetail != null) {
                if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
                    addContextForConnections(userId, entityDetail);
                } else {
                    addContextFolderHierarchy(userId, entityDetail);
                }
            }
        }
    }

    private void addContextForConnections(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail, CONNECTION_TO_ASSET, DATABASE, false);

        if (!connections.isEmpty()) {
            for (EntityDetail entity : connections) {
                buildGraphByRelationshipType(userId, entity, CONNECTION_ENDPOINT, CONNECTION, false);
            }
        }
    }

    private void addContextFolderHierarchy(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail,
                CONNECTION_TO_ASSET, entityDetail.getType().getTypeDefName(), false);

        Optional<EntityDetail> connection = connections.stream().findFirst();
        if (connection.isPresent()) {
            buildGraphByRelationshipType(userId, entityDetail, CONNECTION_ENDPOINT, CONNECTION, false);
        }

        Optional<EntityDetail> nestedFolder = buildGraphByRelationshipType(userId, entityDetail, FOLDER_HIERARCHY, FILE_FOLDER, false)
                .stream()
                .findFirst();

        if (nestedFolder.isPresent()) {
            addContextFolderHierarchy(userId, nestedFolder.get());
        }
    }

    private void addContextForSchemaAttributeType(String userId, EntityDetail entityDetail, String typeDefName) throws OCFCheckedExceptionBase {
        List<EntityDetail> schemaAttributeTypes = buildGraphByRelationshipType(userId, entityDetail, ASSET_SCHEMA_TYPE, typeDefName, true);

        if (schemaAttributeTypes.isEmpty()) {
            addColumns(userId, NESTED_SCHEMA_ATTRIBUTE, typeDefName, entityDetail);
        }

        addColumns(userId, ATTRIBUTE_FOR_SCHEMA, typeDefName, schemaAttributeTypes.toArray(new EntityDetail[0]));
    }

    private void addColumns(String userId, String relationshipType, String typeDefName, EntityDetail... entities) throws OCFCheckedExceptionBase {
        for (EntityDetail entityDetail : entities) {
            buildGraphByRelationshipType(userId, entityDetail, relationshipType, typeDefName, true);
        }
    }

    private boolean isComplexSchemaType(String userId, String typeDefName) throws RepositoryErrorException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {
        TypeDefGallery allTypes = repositoryHandler.getMetadataCollection().getAllTypes(userId);
        return allTypes.getTypeDefs().stream().anyMatch(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE));
    }
}
