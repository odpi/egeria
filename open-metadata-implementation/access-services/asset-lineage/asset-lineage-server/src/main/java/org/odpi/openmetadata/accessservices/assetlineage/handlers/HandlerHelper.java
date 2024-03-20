/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.model.GenericStub;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageTypesValidator;
import org.odpi.openmetadata.accessservices.assetlineage.util.ClockService;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_STORE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TOPIC;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.UPDATE_TIME;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ZONE_MEMBERSHIP;


/**
 * The common handler provide common methods that is generic and reusable for other handlers.
 */
public class HandlerHelper {

    private static final String GUID_PARAMETER = "guid";

    private final OpenMetadataAPIGenericHandler<GenericStub> genericHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    private final Converter converter;
    private final AssetLineageTypesValidator assetLineageTypesValidator;
    private final ClockService clockService;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler    handler for invalid parameters
     * @param repositoryHelper           helper used by the converters
     * @param genericHandler             handler for calling the repository services
     * @param converter                  converter used for creating entities in Lineage Warehouse format
     * @param assetLineageTypesValidator service for validating types
     * @param clockService               clock service
     */
    public HandlerHelper(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                         OpenMetadataAPIGenericHandler<GenericStub> genericHandler, Converter converter,
                         AssetLineageTypesValidator assetLineageTypesValidator, ClockService clockService) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.genericHandler = genericHandler;
        this.assetLineageTypesValidator = assetLineageTypesValidator;
        this.converter = converter;
        this.clockService = clockService;
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

        String relationshipTypeGUID = getTypeGUID(userId, relationshipTypeName);

        List<Relationship> relationships = genericHandler.getAttachmentLinks(userId, entityGUID, GUID_PARAMETER,
                entityTypeName, relationshipTypeGUID, relationshipTypeName, null, null, 0,
                true, false, 0, 0, clockService.getNow(), methodName);

        if (CollectionUtils.isEmpty(relationships)) {
            return Collections.emptyList();
        }

        return relationships.stream().filter(relationship -> relationship.getEntityOneProxy() != null && relationship.getEntityTwoProxy() != null)
                .collect(Collectors.toList());
    }

    /**
     * Return the relationship of the requested type connected to the starting entity.
     * The assumption is that this is a 0..1 relationship so one relationship (or an empty Optional) is returned.
     * If lots of relationships are found then the PropertyServerException is thrown.
     *
     * @param userId               the unique identifier for the user
     * @param entityGUID           the unique identifier of the entity for which the relationships are retrieved
     * @param relationshipTypeName the type of the relationships to be retrieved
     * @param entityTypeName       the type of the entity
     *
     * @return Optional containing the relationship if found, empty optional if not found
     *
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    Optional<Relationship> getUniqueRelationshipByType(String userId, String entityGUID, String relationshipTypeName, String entityTypeName) throws
                                                                                                                                             OCFCheckedExceptionBase {
        final String methodName = "getUniqueRelationshipsByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, GUID_PARAMETER, methodName);

        String relationshipTypeGuid = getTypeGUID(userId, relationshipTypeName);
        return Optional.ofNullable(
                genericHandler.getUniqueAttachmentLink(userId, entityGUID, GUID_PARAMETER, entityTypeName,
                        relationshipTypeGuid, relationshipTypeName, null, null, 0,
                        true, false, clockService.getNow(), methodName)
        );
    }

    /**
     * Retrieves guid for a specific type
     *
     * @param userId      String - userId of user making request.
     * @param typeDefName type of the Entity
     *
     * @return Guid of the type if found, null String if not found
     */
    String getTypeGUID(String userId, String typeDefName) {
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
            return genericHandler.getEntityFromRepository(userId, relationship.getEntityTwoProxy().getGUID(), GUID_PARAMETER,
                    relationship.getEntityTwoProxy().getType().getTypeDefName(), null,
                    null, true, false, clockService.getNow(), methodName);
        } else if (relationship.getEntityTwoProxy().getGUID().equals(entityDetailGUID)) {
            return genericHandler.getEntityFromRepository(userId, relationship.getEntityOneProxy().getGUID(), GUID_PARAMETER,
                    relationship.getEntityOneProxy().getType().getTypeDefName(), null,
                    null, true, false, clockService.getNow(), methodName);
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

        return genericHandler.getEntityFromRepository(userId, entityDetailGUID, GUID_PARAMETER, entityTypeName,
                null, null, true, false,
                clockService.getNow(), methodName);
    }


    /**
     * Retrieves a list of entities based on the search criteria passed
     *
     * @param userId                 the user id
     * @param entityTypeName         the name of the entity type
     * @param searchProperties       searchProperties used in the filtering
     * @param findEntitiesParameters filtering used to reduce the scope of the search
     *
     * @return Optional container for collection of EntityDetails (if any) matching the supplied parameters.
     *
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    something went wrong with the REST call stack.
     */
    public Optional<List<EntityDetail>> findEntitiesByType(String userId, String entityTypeName, SearchProperties searchProperties,
                                                           FindEntitiesParameters findEntitiesParameters)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        List<EntityDetail> allEntities = new ArrayList<>();
        int startingFrom = 0;
        int pageSize = invalidParameterHandler.getMaxPagingSize();
        while (addPagedEntities(userId, entityTypeName, searchProperties, findEntitiesParameters, allEntities, startingFrom, pageSize)) {
            startingFrom += pageSize;
        }

        return Optional.of(allEntities);
    }

    private boolean addPagedEntities(String userId, String entityTypeName, SearchProperties searchProperties,
                                     FindEntitiesParameters findEntitiesParameters, List<EntityDetail> allEntities, int startingFrom, int pageSize)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "addPagedEntities";
        List<EntityDetail> pagedEntities = genericHandler.findEntities(userId, entityTypeName, findEntitiesParameters.getEntitySubtypeGUIDs(),
                searchProperties, findEntitiesParameters.getLimitResultsByStatus(), findEntitiesParameters.getSearchClassifications(), null,
                findEntitiesParameters.getSequencingProperty(), findEntitiesParameters.getSequencingOrder(),
                true, false, startingFrom, pageSize, clockService.getNow(), methodName);
        if (pagedEntities == null) {
            return false;
        }
        allEntities.addAll(pagedEntities);
        return pagedEntities.size() == pageSize;
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
                .filter(classification -> classification.getName().equals(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP)).findFirst();

        if (assetZoneMembership.isPresent()) {
            List<String> zoneMembership = repositoryHelper.getStringArrayProperty(AssetLineageConstants.ASSET_LINEAGE_OMAS, ZONE_MEMBERSHIP,
                    assetZoneMembership.get().getProperties(), methodName);

            if (CollectionUtils.isNotEmpty(zoneMembership)) {
                return zoneMembership;
            }
        }

        return Collections.emptyList();
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

        lineageEntity.setProperties(converter.instancePropertiesToMap(classification.getProperties()));
    }

    /**
     * Create the search body for find entities searching entities updated after the given time
     *
     * @param time date in milliseconds after which the entities were updated
     *
     * @return the search properties having the condition updateTime greater than the provided time
     */
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
     * @param userId        the unique identifier for the user
     * @param entityGUID    the guid of the entity
     * @param relationships the list of relationships for which the context is built
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
        List<Classification> classifications = assetLineageTypesValidator.filterLineageClassifications(entityDetail.getClassifications());

        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);

        String entityGUID = entityDetail.getGUID();
        return new RelationshipsContext(entityGUID, classifications
                .stream()
                .map(classification -> getClassificationVertex(classification, entityGUID))
                .map(classificationVertex -> new GraphContext(CLASSIFICATION, classificationVertex.getGuid(), originalEntityVertex,
                        classificationVertex)).collect(Collectors.toSet()));
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
    protected EntityDetail addContextForRelationships(String userId, EntityDetail startEntity, String relationshipTypeName,
                                                      Set<GraphContext> context) throws OCFCheckedExceptionBase {
        if (startEntity == null) {
            return null;
        }

        context.addAll(buildContextForLineageClassifications(startEntity).getRelationships());

        List<Relationship> relationships = getRelationshipsByType(userId, startEntity.getGUID(), relationshipTypeName,
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

        context.addAll(buildContextForRelationships(userId, startEntity.getGUID(), relationships).getRelationships());

        return getEntityAtTheEnd(userId, startEntity.getGUID(), relationships.get(0));
    }


    /**
     * Validate asset's GUID and it being in the specific supported zones.
     *
     * @param entityDetail   the entity detail
     * @param methodName     the method name
     * @param supportedZones the supported zones
     *
     * @throws InvalidParameterException the invalid parameter exception
     */
    public void validateAsset(EntityDetail entityDetail, String methodName, List<String> supportedZones) throws InvalidParameterException {
        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        invalidParameterHandler.validateAssetInSupportedZone(entityDetail.getGUID(), GUID_PARAMETER,
                getAssetZoneMembership(entityDetail.getClassifications()), supportedZones, ASSET_LINEAGE_OMAS, methodName);
    }

    /**
     * Return the entity detail in open lineage format
     *
     * @param entityDetail the entity detail
     *
     * @return the entity detail in open lineage format
     */
    public LineageEntity getLineageEntity(EntityDetail entityDetail) {
        return converter.createLineageEntity(entityDetail);
    }

    /**
     * Verifies if the entity is of type DataStore or subtype
     *
     * @param serviceName  the service name
     * @param entityDetail the entity detail
     *
     * @return true if the entity is of type Asset or subtype, false otherwise
     */
    public boolean isDataStore(String serviceName, EntityDetail entityDetail) {
        return repositoryHelper.isTypeOf(serviceName, entityDetail.getType().getTypeDefName(), DATA_STORE);
    }

    /**
     * Verifies if the entity is of type RelationalTable or subtype
     *
     * @param serviceName  the service name
     * @param entityDetail the entity detail
     *
     * @return true if the entity is of type RelationalTable or subtype, false otherwise
     */
    public boolean isTable(String serviceName, EntityDetail entityDetail) {
        return repositoryHelper.isTypeOf(serviceName, entityDetail.getType().getTypeDefName(), RELATIONAL_TABLE);
    }

    /**
     * Verifies if the entity is of type SchemaAttribute or subtype
     *
     * @param serviceName the service name
     * @param typeName    type of the entity
     *
     * @return true if the entity is of type TabularColumn or subtype, false otherwise
     */
    public boolean isSchemaAttribute(String serviceName, String typeName) {
        return repositoryHelper.isTypeOf(serviceName, typeName, SCHEMA_ATTRIBUTE);
    }

    /**
     * Verifies if the entity is of type Topic or subtype
     *
     * @param serviceName  the service name
     * @param entityDetail the entity detail
     *
     * @return true if the entity is of type RelationalTable or subtype, false otherwise
     */
    public boolean isTopic(String serviceName, EntityDetail entityDetail) {
        return repositoryHelper.isTypeOf(serviceName, entityDetail.getType().getTypeDefName(), TOPIC);
    }
}
