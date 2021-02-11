/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.UPDATE_TIME;


/**
 * The common handler provide common methods that is generic and reusable for other handlers.
 */
public class HandlerHelper {

    private static final String GUID_PARAMETER = "guid";
    private static final String ASSET_ZONE_MEMBERSHIP = "AssetZoneMembership";
    private static final String ZONE_MEMBERSHIP = "zoneMembership";

    private Set<String> lineageClassificationTypes;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public HandlerHelper(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler,
                         Set<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.lineageClassificationTypes = lineageClassificationTypes;
    }

    /**
     * Fetch the relationships of an entity based on the type of the relationship
     *
     * @param userId               the unique identifier for the user
     * @param entityGUID           the unique identifier of the entity for which the relationships are retrieved
     * @param relationshipTypeName the type of the relationships to be retrieved
     * @param entityTypeName       the type of the entity
     *
     * @return List of the relationships if found, empty list if not found
     *
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    List<Relationship> getRelationshipsByType(String userId, String entityGUID, String relationshipTypeName, String entityTypeName) throws
                                                                                                                                    OCFCheckedExceptionBase {

        final String methodName = "getRelationshipsByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, GUID_PARAMETER, methodName);

        String typeGuid = getTypeByName(userId, relationshipTypeName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId, entityGUID, entityTypeName, typeGuid,
                relationshipTypeName, methodName);

        if (relationships != null) {
            return relationships;
        }

        return Collections.emptyList();
    }

    /**
     * Retrieves guid for a specific type
     *
     * @param userId      String - userId of user making request.
     * @param typeDefName type of the Entity
     *
     * @return Guid of the type if found, null String if not found
     */
    String getTypeByName(String userId, String typeDefName) {
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
     *
     * @return the entity at the end
     *
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    EntityDetail getEntityAtTheEnd(String userId, String entityDetailGUID, Relationship relationship) throws OCFCheckedExceptionBase {
        String methodName = "getEntityAtTheEnd";

        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId, relationship.getEntityTwoProxy().getGUID(), GUID_PARAMETER,
                    relationship.getEntityTwoProxy().getType().getTypeDefName(), methodName);
        } else if (relationship.getEntityTwoProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId, relationship.getEntityOneProxy().getGUID(), GUID_PARAMETER,
                    relationship.getEntityOneProxy().getType().getTypeDefName(), methodName);
        }
        return null;
    }

    /**
     * Fetch the entity using the identifier and the type name
     *
     * @param userId           the user identifier
     * @param entityDetailGUID the entity identifier
     * @param entityTypeName   the entity type name
     *
     * @return the entity
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    public EntityDetail getEntityDetails(String userId, String entityDetailGUID, String entityTypeName) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException {
        String methodName = "getEntityDetails";

        return repositoryHandler.getEntityByGUID(userId, entityDetailGUID, GUID_PARAMETER, entityTypeName, methodName);
    }


    /**
     * Retrieves a list of entities based on the search criteria passed
     *
     * @param userId                 the user id
     * @param entityTypeName         the name of the entity type
     * @param searchProperties       searchProperties used in the filtering
     * @param findEntitiesParameters filtering used to reduce the scope of the search
     * @return a list with entities matching the supplied parameters;
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public List<EntityDetail> findEntitiesByType(String userId, String entityTypeName, SearchProperties searchProperties, FindEntitiesParameters findEntitiesParameters)
            throws UserNotAuthorizedException, PropertyServerException {
        final String methodName = "findEntitiesByType";
        String typeDefGUID = getTypeByName(userId, entityTypeName);
        return repositoryHandler.findEntities(userId, typeDefGUID, findEntitiesParameters.getEntitySubtypeGUIDs(),
                searchProperties, findEntitiesParameters.getLimitResultsByStatus(), findEntitiesParameters.getSearchClassifications(), null,
                findEntitiesParameters.getSequencingProperty(), findEntitiesParameters.getSequencingOrder(), 0, 0, methodName);
    }

    /**
     * Adds entities and relationships for the process Context structure
     *
     * @param userId       the user Id of user making request.
     * @param startEntity  parent entity of the relationship
     * @param relationship the relationship of the parent node
     * @param graph        the graph
     *
     * @return Entity which is the child of the relationship, null if there is no Entity
     *
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    EntityDetail buildGraphEdgeByRelationship(String userId, EntityDetail startEntity, Relationship relationship, AssetContext graph) throws
                                                                                                                                      OCFCheckedExceptionBase {

        Converter converter = new Converter(repositoryHelper);
        EntityDetail endEntity = getEntityAtTheEnd(userId, startEntity.getGUID(), relationship);

        if (endEntity == null) return null;

        LineageEntity startVertex;
        LineageEntity endVertex;

        if (startEntity.getGUID().equals(relationship.getEntityTwoProxy().getGUID())) {
            startVertex = converter.createLineageEntity(endEntity);
            endVertex = converter.createLineageEntity(startEntity);
        } else {
            startVertex = converter.createLineageEntity(startEntity);
            endVertex = converter.createLineageEntity(endEntity);
        }


        enhanceGraphContext(relationship, graph, startVertex, endVertex);
        return endEntity;

    }

    private void enhanceGraphContext(Relationship relationship, AssetContext graph, LineageEntity startVertex, LineageEntity endVertex) {

        GraphContext relationshipContext = new GraphContext(relationship.getType().getTypeDefName(), relationship.getGUID(), startVertex, endVertex);

        if (graph.getNeighbors().containsKey(relationshipContext.getRelationshipGuid())) {
            return;
        }
        for (GraphContext context : graph.getGraphContexts()) {
            if (relationshipContext.getRelationshipGuid().equals(context.getRelationshipGuid())) {
                return;
            }
        }

        graph.addVertex(startVertex);
        graph.addVertex(endVertex);
        graph.addGraphContext(relationshipContext);

    }

    /**
     * Fetch the zone membership property
     *
     * @param classifications asset properties
     *
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

    /**
     * Adds the classification context to the asset context.
     *
     * @param assetContext the context of the asset that is to be updated
     * @param entity       the entity with its classifications
     */
    public void addLineageClassificationToContext(EntityDetail entity, AssetContext assetContext) {
        List<Classification> classifications = filterLineageClassifications(entity.getClassifications());
        if (CollectionUtils.isNotEmpty(classifications)) {
            addClassificationsToGraphContext(classifications, assetContext, entity);
        }
    }

    /**
     * Extract the lineage classifications from the list of classifications assigned
     *
     * @param classifications the list of available classifications
     *
     * @return a list of lineage classifications
     */
    public List<Classification> filterLineageClassifications(List<Classification> classifications) {
        if (CollectionUtils.isNotEmpty(classifications)) {
            return classifications.stream()
                    .filter(classification -> classification.getType() != null)
                    .filter(classification -> lineageClassificationTypes.contains(classification.getType().getTypeDefName()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Add lineage classification to the graph context object
     *
     * @param classifications the list of classifications
     * @param assetContext    the asset context object
     * @param entityDetail    the entity object that is converted to lineage entity
     */
    private void addClassificationsToGraphContext(List<Classification> classifications, AssetContext assetContext, EntityDetail entityDetail) {
        Converter converter = new Converter(repositoryHelper);
        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);
        assetContext.addVertex(originalEntityVertex);

        String entityGUID = entityDetail.getGUID();
        for (Classification classification : classifications) {
            LineageEntity classificationVertex = getClassificationVertex(classification, entityGUID);
            assetContext.addVertex(classificationVertex);
            GraphContext graphContext = new GraphContext(CLASSIFICATION, classificationVertex.getGuid(),
                    originalEntityVertex, classificationVertex);
            assetContext.addGraphContext(graphContext);
        }

    }

    private LineageEntity getClassificationVertex(Classification classification, String entityGUID) {
        LineageEntity classificationVertex = new LineageEntity();

        String classificationGUID = classification.getName() + entityGUID;
        classificationVertex.setGuid(classificationGUID);
        copyClassificationProperties(classificationVertex, classification);

        return classificationVertex;
    }

    private void copyClassificationProperties(LineageEntity lineageEntity, Classification classification) {
        lineageEntity.setVersion(classification.getVersion());
        lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
        lineageEntity.setCreatedBy(classification.getCreatedBy());
        lineageEntity.setUpdatedBy(classification.getUpdatedBy());
        lineageEntity.setCreateTime(classification.getCreateTime());
        lineageEntity.setUpdateTime(classification.getUpdateTime());

        Converter converter = new Converter(repositoryHelper);
        lineageEntity.setProperties(converter.instancePropertiesToMap(classification.getProperties()));
    }

    /**
     * Creat the search body for find entities searching entities updated after the given time
     * @param time date in milliseconds after which the entities were updated
     * @return the search properties having the condition updateTime greater than the provided time
    * */
    public SearchProperties getSearchPropertiesAfterUpdateTime(Long time) {
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        primitivePropertyValue.setPrimitiveValue(time);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getGUID());

        PropertyCondition propertyCondition = new PropertyCondition();
        propertyCondition.setProperty(UPDATE_TIME);
        propertyCondition.setOperator(PropertyComparisonOperator.GT);
        propertyCondition.setValue(primitivePropertyValue);

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setConditions(Collections.singletonList(propertyCondition));
        return searchProperties;
    }

    /**
     * Builds the relationships context for an entity
     *
     * @param userId           the unique identifier for the user
     * @param entityGUID       the guid of the entity
     * @param relationships    the list of relationships for which the context is built
     *
     * @return a set of {@link GraphContext} containing the lineage context for the relationships
     *
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     */
    public RelationshipsContext buildContextForRelationships(String userId, String entityGUID, List<Relationship> relationships) throws
                                                                                                                                       UserNotAuthorizedException,
                                                                                                                                       PropertyServerException,
                                                                                                                                       InvalidParameterException {
        Set<GraphContext> lineageRelationships = new HashSet<>();

        Converter converter = new Converter(repositoryHelper);
        for (Relationship relationship : relationships) {

            EntityDetail startEntity = getEntityDetails(userId, relationship.getEntityOneProxy().getGUID(),
                    relationship.getEntityOneProxy().getType().getTypeDefName());

            EntityDetail endEntity = getEntityDetails(userId, relationship.getEntityTwoProxy().getGUID(),
                    relationship.getEntityTwoProxy().getType().getTypeDefName());

            if (endEntity == null) continue;

            LineageEntity startVertex = converter.createLineageEntity(startEntity);
            LineageEntity endVertex = converter.createLineageEntity(endEntity);

            lineageRelationships.add(new GraphContext(relationship.getType().getTypeDefName(), relationship.getGUID(), startVertex, endVertex));

        }

        return new RelationshipsContext(entityGUID, lineageRelationships);
    }

    /**
     * Builds the classification context for an entity
     *
     * @param entityDetail the entity for retrieving the classifications attached to it
     *
     * @return a set of {@link GraphContext} containing the lineage context for the classifications
     */
    public RelationshipsContext buildContextForLineageClassifications(EntityDetail entityDetail) {
        List<Classification> classifications = filterLineageClassifications(entityDetail.getClassifications());

        Converter converter = new Converter(repositoryHelper);
        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);

        String entityGUID = entityDetail.getGUID();
        return new RelationshipsContext(entityGUID, classifications
                .stream()
                .map(classification -> getClassificationVertex(classification, entityGUID))
                .map(classificationVertex -> new GraphContext(CLASSIFICATION, classificationVertex.getGuid(), originalEntityVertex,
                        classificationVertex)).collect(Collectors.toSet()));


    }
}