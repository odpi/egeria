/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * The common handler provide common methods that is generic and reusable for other handlers.
 */
public class HandlerHelper {

    private static final String GUID_PARAMETER = "guid";
    private static final String ASSET_ZONE_MEMBERSHIP = "AssetZoneMembership";
    private static final String ZONE_MEMBERSHIP = "zoneMembership";

    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public HandlerHelper(InvalidParameterHandler invalidParameterHandler,
                         OMRSRepositoryHelper repositoryHelper,
                         RepositoryHandler repositoryHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Query about the entity in the repositories based on the Guid
     *
     * @param userId   String - userId of user making request.
     * @param guid     guid of the asset we need to retrieve from a repository
     * @param typeName the name of the Open Metadata type for getting details
     * @return optional with entity details if found, empty optional if not found
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    Optional<EntityDetail> getEntityDetails(String userId, String guid, String typeName) throws OCFCheckedExceptionBase {

        String methodName = "getEntityDetails";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, GUID_PARAMETER, methodName);

        return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId, guid, GUID_PARAMETER, typeName, methodName));
    }

    /**
     * Query about the relationships of an entity based on the type of the relationship
     *
     * @param userId               String - userId of user making request.
     * @param assetGuid            guid of the asset we need to retrieve the relationships
     * @param relationshipTypeName the type of the relationship
     * @param entityTypeName       the type of the entity
     * @return List of the relationships if found, empty list if not found
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    List<Relationship> getRelationshipsByType(String userId, String assetGuid,
                                              String relationshipTypeName, String entityTypeName) throws OCFCheckedExceptionBase {

        final String methodName = "getRelationshipsByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGuid, GUID_PARAMETER, methodName);

        String typeGuid = getTypeName(userId, relationshipTypeName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                assetGuid,
                entityTypeName,
                typeGuid,
                relationshipTypeName,
                methodName);

        if (relationships != null) {
            return relationships;
        }

        return new ArrayList<>();
    }

    /**
     * Retrieves guid for a specific type
     *
     * @param userId      String - userId of user making request.
     * @param typeDefName type of the Entity
     * @return Guid of the type if found, null String if not found
     */
    String getTypeName(String userId, String typeDefName) {
        final TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }


    /**
     * Gets entity at the end.
     *
     * @param userId           the user id
     * @param entityDetailGUID the entity detail guid
     * @param relationship     the relationship
     * @return the entity at the end
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    private EntityDetail getEntityAtTheEnd(String userId, String entityDetailGUID, Relationship relationship) throws OCFCheckedExceptionBase {

        String methodName = "getEntityAtTheEnd";

        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId,
                    relationship.getEntityTwoProxy().getGUID(),
                    GUID_PARAMETER,
                    relationship.getEntityTwoProxy().getType().getTypeDefName(), methodName);
        } else {
            return repositoryHandler.getEntityByGUID(userId,
                    relationship.getEntityOneProxy().getGUID(),
                    GUID_PARAMETER,
                    relationship.getEntityOneProxy().getType().getTypeDefName(), methodName);
        }
    }

    /**
     * Adds entities and relationships for the process Context structure
     *
     * @param userId       String - userId of user making request.
     * @param startEntity  parent entity of the relationship
     * @param relationship the relationship of the parent node
     * @param graph        the graph
     * @return Entity which is the child of the relationship, null if there is no Entity
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    EntityDetail buildGraphEdgeByRelationship(String userId, EntityDetail startEntity,
                                              Relationship relationship, AssetContext graph, boolean changeDirection) throws OCFCheckedExceptionBase {

        Converter converter = new Converter();
        EntityDetail endEntity = getEntityAtTheEnd(userId, startEntity.getGUID(), relationship);

        if (endEntity == null) return null;

        LineageEntity startVertex;
        LineageEntity endVertex;
        if (changeDirection) {
            startVertex = converter.createLineageEntity(endEntity);
            endVertex = converter.createLineageEntity(startEntity);
        } else {
            startVertex = converter.createLineageEntity(startEntity);
            endVertex = converter.createLineageEntity(endEntity);
        }


        GraphContext graphContext = new GraphContext(relationship.getType().getTypeDefName(), relationship.getGUID(), startVertex, endVertex);

        if (graph.getGraphContexts().stream().noneMatch(e -> e.getRelationshipGuid().equals(graphContext.getRelationshipGuid()))) {
            graph.addVertex(startVertex);
            graph.addVertex(endVertex);
            graph.addGraphContext(graphContext);
        }

        return endEntity;
    }

    /**
     * Fetch the zone membership property
     *
     * @param classifications asset properties
     * @return the list that contains the zone membership
     */
    List<String> getAssetZoneMembership(List<Classification> classifications) {
        String methodName = "getAssetZoneMembership";
        if (CollectionUtils.isEmpty(classifications)) {
            return Collections.emptyList();
        }

        Optional<Classification> assetZoneMembership = classifications.stream()
                .filter(classification -> classification.getName().equals(ASSET_ZONE_MEMBERSHIP)).findFirst();

        if (assetZoneMembership.isPresent()) {
            List<String> zoneMembership = repositoryHelper.getStringArrayProperty(AssetLineageConstants.ASSET_LINEAGE_OMAS, ZONE_MEMBERSHIP,
                    assetZoneMembership.get().getProperties(), methodName);

            if (CollectionUtils.isNotEmpty(zoneMembership)) {
                return zoneMembership;
            }
        }

        return Collections.emptyList();
    }
}
