/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.COMPLEX_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATABASE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;

/**
 * The Asset Context handler provides methods to build graph context for assets that has been created.
 */
public class AssetContextHandler {

    private final RepositoryHandler repositoryHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final HandlerHelper handlerHelper;
    private final List<String> supportedZones;

    private AssetContext graph;

    /**
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param supportedZones          configurable list of zones that Asset Lineage is allowed to retrieve Assets from
     */
    public AssetContextHandler(InvalidParameterHandler invalidParameterHandler,
                               OMRSRepositoryHelper repositoryHelper,
                               RepositoryHandler repositoryHandler,
                               List<String> supportedZones,
                               List<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, repositoryHandler, lineageClassificationTypes);
        this.supportedZones = supportedZones;
    }

    /**
     * @param userId the user id
     * @return the existing list of glossary terms available in the repository
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public List<EntityDetail> getEntitiesByTypeName(String userId, String entityTypeName) throws UserNotAuthorizedException, PropertyServerException {
        final String methodName = "getEntitiesByTypeName";

        String typeDefGUID = handlerHelper.getTypeByName(userId, entityTypeName);

        return repositoryHandler.getEntitiesByType(userId, typeDefGUID, 0, 0, methodName);
    }

    /**
     * Gets asset context.
     *
     * @param userId       the user id
     * @param entityDetail the entity for which the context is build
     * @return the asset context
     */
    public AssetContext getAssetContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        final String methodName = "getAssetContext";
        graph = new AssetContext();

        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        invalidParameterHandler.validateAssetInSupportedZone(entityDetail.getGUID(),
                GUID_PARAMETER,
                handlerHelper.getAssetZoneMembership(entityDetail.getClassifications()),
                supportedZones,
                ASSET_LINEAGE_OMAS,
                methodName);

        buildAssetContext(userId, entityDetail);
        return graph;
    }

    private void buildAssetContext(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        final String typeDefName = entityDetail.getType().getTypeDefName();

        if (typeDefName.equals(RELATIONAL_TABLE) || typeDefName.equals(DATA_FILE)) {
            addContextForSchemaAttributeType(userId, entityDetail, typeDefName);
        }

        addLineageMappings(userId, entityDetail, typeDefName);

        List<EntityDetail> tableTypeEntities = buildGraphByRelationshipType(userId, entityDetail, ATTRIBUTE_FOR_SCHEMA, typeDefName);

        if (tableTypeEntities.isEmpty()) {
            tableTypeEntities = buildGraphByRelationshipType(userId, entityDetail, NESTED_SCHEMA_ATTRIBUTE, typeDefName);
        }
        for (EntityDetail schemaTypeEntity : tableTypeEntities) {
            if (isComplexSchemaType(userId, schemaTypeEntity.getType().getTypeDefName())) {
                setAssetDetails(userId, schemaTypeEntity);
            } else {
                Optional<EntityDetail> first = tableTypeEntities.stream().findFirst();
                if (first.isPresent()) {
                    buildAssetContext(userId, first.get());
                }
            }
        }
    }

    private void addLineageMappings(String userId, EntityDetail entityDetail, String typeDefName) throws OCFCheckedExceptionBase {
        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, entityDetail.getGUID(), LINEAGE_MAPPING, typeDefName);
        for (Relationship relationship : relationships) {
            handlerHelper.buildGraphEdgeByRelationship(userId, entityDetail, relationship, graph);
        }
    }

    private List<EntityDetail> buildGraphByRelationshipType(String userId, EntityDetail startEntity,
                                                            String relationshipType, String typeDefName) throws OCFCheckedExceptionBase {
        handlerHelper.addLineageClassificationToContext(startEntity, graph);

        List<Relationship> relationships = handlerHelper.getRelationshipsByType(userId, startEntity.getGUID(), relationshipType, typeDefName);

        if (startEntity.getType().getTypeDefName().equals(FILE_FOLDER)) {
            relationships = relationships.stream().filter(relationship ->
                    relationship.getEntityTwoProxy().getGUID().equals(startEntity.getGUID())).collect(Collectors.toList());
        }

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : relationships) {

            EntityDetail endEntity = handlerHelper.buildGraphEdgeByRelationship(userId, startEntity, relationship, graph);
            if (endEntity == null) return Collections.emptyList();

            entityDetails.add(endEntity);
        }
        return entityDetails;
    }

    private void setAssetDetails(String userId, EntityDetail startEntity) throws OCFCheckedExceptionBase {
        List<EntityDetail> assetEntity = buildGraphByRelationshipType(userId, startEntity, ASSET_SCHEMA_TYPE, startEntity.getType().getTypeDefName());
        Optional<EntityDetail> first = assetEntity.stream().findFirst();
        if (first.isPresent()) {
            buildAsset(userId, first.get());

        }
    }

    private void buildAsset(String userId, EntityDetail dataSet) throws OCFCheckedExceptionBase {
        final String typeDefName = dataSet.getType().getTypeDefName();

        String relationshipType = typeDefName.equals(DATA_FILE) ? NESTED_FILE : DATA_CONTENT_FOR_DATA_SET;
        List<EntityDetail> entityDetails = buildGraphByRelationshipType(userId, dataSet, relationshipType, typeDefName);
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

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail, CONNECTION_TO_ASSET, DATABASE);

        if (!connections.isEmpty()) {
            for (EntityDetail entity : connections) {
                buildGraphByRelationshipType(userId, entity, CONNECTION_ENDPOINT, CONNECTION);
            }
        }
    }

    private void addContextFolderHierarchy(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        List<EntityDetail> connections = buildGraphByRelationshipType(userId, entityDetail,
                CONNECTION_TO_ASSET, entityDetail.getType().getTypeDefName());

        Optional<EntityDetail> connection = connections.stream().findFirst();
        if (connection.isPresent()) {
            buildGraphByRelationshipType(userId, entityDetail, CONNECTION_ENDPOINT, CONNECTION);
        }

        Optional<EntityDetail> nestedFolder = buildGraphByRelationshipType(userId, entityDetail, FOLDER_HIERARCHY, FILE_FOLDER)
                .stream()
                .findFirst();

        if (nestedFolder.isPresent()) {
            addContextFolderHierarchy(userId, nestedFolder.get());
        }
    }

    private void addContextForSchemaAttributeType(String userId, EntityDetail entityDetail, String typeDefName) throws OCFCheckedExceptionBase {
        List<EntityDetail> schemaAttributeTypes = buildGraphByRelationshipType(userId, entityDetail, ASSET_SCHEMA_TYPE, typeDefName);

        if (schemaAttributeTypes.isEmpty()) {
            addColumns(userId, NESTED_SCHEMA_ATTRIBUTE, typeDefName, entityDetail);
        }

        addColumns(userId, ATTRIBUTE_FOR_SCHEMA, typeDefName, schemaAttributeTypes.toArray(new EntityDetail[0]));
    }

    private void addColumns(String userId, String relationshipType, String typeDefName, EntityDetail... entities) throws OCFCheckedExceptionBase {
        for (EntityDetail entityDetail : entities) {
            buildGraphByRelationshipType(userId, entityDetail, relationshipType, typeDefName);
        }
    }

    private boolean isComplexSchemaType(String userId, String typeDefName) throws RepositoryErrorException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {
        TypeDefGallery allTypes = repositoryHandler.getMetadataCollection().getAllTypes(userId);
        return allTypes.getTypeDefs().stream().anyMatch(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE));
    }
}
