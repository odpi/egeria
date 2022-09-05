/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.RelationshipDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;

/**
 * DataEngineCommonHandler manages objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineCommonHandler {
    private final String serviceName;
    private final String serverName;
    private final OpenMetadataAPIGenericHandler<Referenceable> genericHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;

    private static final Logger log = LoggerFactory.getLogger(DataEngineCommonHandler.class);

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param genericHandler                manages calls to the repository services
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     */
    public DataEngineCommonHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                   OpenMetadataAPIGenericHandler<Referenceable> genericHandler, OMRSRepositoryHelper repositoryHelper,
                                   DataEngineRegistrationHandler dataEngineRegistrationHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.genericHandler = genericHandler;
        this.repositoryHelper = repositoryHelper;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
    }

    /**
     * Build an EntityDetail object  based on the instance properties on an entity bean
     *
     * @param entityGUID         unique identifier of entity to update
     * @param instanceProperties the properties of the entity
     *
     * @return an EntityDetail object containing the entity properties
     */
    protected EntityDetail buildEntityDetail(String entityGUID, InstanceProperties instanceProperties) {
        EntityDetail entityDetail = new EntityDetail();

        entityDetail.setGUID(entityGUID);
        entityDetail.setProperties(instanceProperties);

        return entityDetail;
    }

    /**
     * Build an Relationship  object  based on the instance properties of a relationship
     *
     * @param entityGUID         unique identifier of entity to update
     * @param instanceProperties the properties of the relationship
     *
     * @return an Relationship object containing the entity properties
     */
    protected Relationship buildRelationship(String entityGUID, InstanceProperties instanceProperties) {
        Relationship relationship = new Relationship();

        relationship.setGUID(entityGUID);
        relationship.setProperties(instanceProperties);

        return relationship;
    }

    /**
     * Find out if the entity is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId         the name of the calling user
     * @param qualifiedName  the qualifiedName name of the entity to be searched
     * @param entityTypeName the type name of the entity
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findEntity(String userId, String qualifiedName, String entityTypeName) throws UserNotAuthorizedException,
                                                                                                                PropertyServerException,
                                                                                                                InvalidParameterException {
        final String methodName = "findEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);

        EntityDetail retrievedEntity = genericHandler.getEntityByValue(userId, qualifiedName, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, null, methodName);

        String guid = null;
        if(retrievedEntity != null) {
            guid = retrievedEntity.getGUID();
        }
        log.trace("Searching for entity with qualifiedName: {}. Result is {}", qualifiedName, guid);

        return Optional.ofNullable(retrievedEntity);
    }

    /**
     * Fetch the entity using the identifier and the type name. It uses the unique identifier to retrieve the entity
     *
     * @param userId           the user identifier
     * @param entityDetailGUID the entity unique identifier
     * @param entityTypeName   the entity type name
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    public Optional<EntityDetail> getEntityDetails(String userId, String entityDetailGUID, String entityTypeName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityDetailGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        EntityDetail retrievedEntity = genericHandler.getEntityFromRepository(userId, entityDetailGUID,
                CommonMapper.GUID_PROPERTY_NAME, entityTypeName, null, null,
                false, false, null, null, methodName);

        return Optional.ofNullable(retrievedEntity);
    }

    /**
     * Create or updates an external relationship between two entities. Verifies that the relationship is not present before creating it. If the
     * relationship is present, verifies the instanceProperties for the relationship to be updated.
     *
     * @param userId                 the name of the calling user
     * @param firstGUID              the unique identifier of the entity at first end
     * @param secondGUID             the unique identifier of the entity at second end
     * @param relationshipTypeName   type name for the relationship to create
     * @param firstEntityTypeName    type name for the entity at first end
     * @param secondEntityTypeName   type name for the entity at the second end
     * @param externalSourceName     the unique name of the external source
     * @param relationshipProperties the properties for the relationship
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected void upsertExternalRelationship(String userId, String firstGUID, String secondGUID, String relationshipTypeName,
                                              String firstEntityTypeName, String secondEntityTypeName, String externalSourceName,
                                              InstanceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException {

        final String methodName = "upsertExternalRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(firstGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(secondGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        Optional<Relationship> relationship = findRelationship(userId, firstGUID, secondGUID, firstEntityTypeName,
                secondEntityTypeName, relationshipTypeName);
        if (relationship.isEmpty()) {

            TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, relationshipTypeName);

            genericHandler.linkElementToElement(userId, externalSourceGUID, externalSourceName, firstGUID,
                    CommonMapper.GUID_PROPERTY_NAME, firstEntityTypeName, secondGUID, CommonMapper.GUID_PROPERTY_NAME,
                    secondEntityTypeName, false, false, null,
                    relationshipTypeDef.getGUID(), relationshipTypeName, relationshipProperties, null, null, null, methodName);
        } else {
            Relationship originalRelationship = relationship.get();
            String relationshipGUID = originalRelationship.getGUID();

            RelationshipDifferences relationshipDifferences = repositoryHelper.getRelationshipDifferences(originalRelationship,
                    buildRelationship(relationshipGUID, relationshipProperties), true);

            if (relationshipDifferences.hasInstancePropertiesDifferences()) {
                genericHandler.updateRelationshipProperties(userId, externalSourceGUID, externalSourceName, relationshipGUID,
                                                            GUID_PROPERTY_NAME, originalRelationship.getType().getTypeDefName(), true,
                                                            relationshipProperties, false, false, null, methodName);
            }
        }
    }

    /**
     * Find out if the relationship is already stored in the repository.
     * It will search for relationships that have the source firstGUID and target secondGUID
     *
     * @param userId               the name of the calling user
     * @param firstGUID            the unique identifier of the entity at first end
     * @param secondGUID           the unique identifier of the entity at second end
     * @param firstEntityTypeName  type name for the entity at first end
     * @param secondEntityTypeName type name for the entity at second end
     * @param relationshipTypeName type name for the relationship to create
     *
     * @return The found relationship or an empty Optional
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected Optional<Relationship> findRelationship(String userId, String firstGUID, String secondGUID, String firstEntityTypeName,
                                                      String secondEntityTypeName, String relationshipTypeName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException {
        final String methodName = "findRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(firstGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(secondGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, relationshipTypeName);
        Relationship relationshipBetweenEntities = genericHandler.getUniqueAttachmentLink(userId, firstGUID,
                 CommonMapper.GUID_PROPERTY_NAME, firstEntityTypeName, relationshipTypeDef.getGUID(),
                relationshipTypeDef.getName(), secondGUID, secondEntityTypeName, 0,
                false, false, null, methodName);

        if (relationshipBetweenEntities == null) {
            return Optional.empty();
        }

        if (firstGUID.equalsIgnoreCase(relationshipBetweenEntities.getEntityOneProxy().getGUID())
                && secondGUID.equalsIgnoreCase(relationshipBetweenEntities.getEntityTwoProxy().getGUID())) {
            return Optional.of(relationshipBetweenEntities);
        }

        return Optional.empty();
    }

    /**
     * Remove entity
     *
     * @param userId             the name of the calling user
     * @param entityGUID         the unique identifier of the port to be removed
     * @param entityTypeName     the type name of the entity
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected void removeEntity(String userId, String entityGUID, String entityTypeName, String externalSourceName) throws InvalidParameterException,
                                                                                                                           PropertyServerException,
                                                                                                                           UserNotAuthorizedException {
        final String methodName = "removeEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, entityTypeName);
        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngine(userId, externalSourceName);

        genericHandler.deleteBeanInRepository(userId, externalSourceGUID, externalSourceName, entityGUID, GUID_PROPERTY_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), null, null, false, false, null, methodName);
    }

    /**
     * Return the owner type ordinal
     *
     * @param ownerType OwnerType enum
     *
     * @return DataItemSortOrder enum ordinal
     */
    protected int getOwnerTypeOrdinal(OwnerType ownerType) {
        int ownerTypeOrdinal = OwnerType.USER_ID.getOpenTypeOrdinal();

        if (ownerType != null) {
            ownerTypeOrdinal = ownerType.getOpenTypeOrdinal();
        }
        return ownerTypeOrdinal;
    }

    /**
     * Return the ordinal for the order that the column is arranged in
     *
     * @param column the column to
     *
     * @return DataItemSortOrder enum ordinal
     */
    protected int getSortOrder(Attribute column) {
        int sortOrder = DataItemSortOrder.UNKNOWN.getOpenTypeOrdinal();
        if (column.getSortOrder() != null) {
            sortOrder = column.getSortOrder().getOpenTypeOrdinal();
        }
        return sortOrder;
    }

    public void throwInvalidParameterException(DataEngineErrorCode errorCode, String methodName, String... params) throws
                                                                                                                      InvalidParameterException {

        throw new InvalidParameterException(errorCode.getMessageDefinition(params), this.getClass().getName(), methodName, "qualifiedName");
    }

    public void throwEntityNotDeletedException(DataEngineErrorCode errorCode, String methodName, String... params) throws EntityNotDeletedException {

        throw new EntityNotDeletedException(errorCode.getMessageDefinition(params), this.getClass().getName(), methodName);
    }

    /**
     * Return the set of entities at the other end of the requested relationship type.
     *
     * @param userId                    the name of the calling user
     * @param guid                      starting entity's GUID
     * @param relationshipTypeName      type name for the relationship to follow
     * @param resultingElementTypeName  resulting entity's type name
     * @param entityTypeName            starting entity's type name
     *
     * @return retrieved entities or empty set
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected Set<EntityDetail> getEntitiesForRelationship(String userId, String guid, String relationshipTypeName,
            String resultingElementTypeName, String entityTypeName) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        final String methodName = "getEntitiesForRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, relationshipTypeName);

        List<EntityDetail> entities = genericHandler.getAttachedEntities(userId, guid, CommonMapper.GUID_PROPERTY_NAME,
                entityTypeName, relationshipTypeDef.getGUID(), relationshipTypeName, resultingElementTypeName,
                null, null, 0, false, false, 0, invalidParameterHandler.getMaxPagingSize(), null, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().collect(Collectors.toSet());
    }

    /**
     * Return the entity at the other end of the requested relationship type.
     *
     * @param userId                  the name of the calling user
     * @param entityGUID              the unique identifier of the starting entity
     * @param relationshipTypeName    the relationship type name
     * @param entityTypeName          the entity of the starting end type name
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected Optional<EntityDetail> getEntityForRelationship(String userId, String entityGUID, String relationshipTypeName,
                                                              String entityTypeName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException {
        final String methodName = "getEntityForRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, relationshipTypeName);
        EntityDetail entity = genericHandler.getAttachedEntity(userId, entityGUID, GUID_PROPERTY_NAME,
                entityTypeName, relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), null,
                false, false, null, methodName);
        return Optional.ofNullable(entity);
    }

    protected void validateDeleteSemantic(DeleteSemantic deleteSemantic, String methodName) throws FunctionNotSupportedException {
        if (deleteSemantic != DeleteSemantic.SOFT) {
            throw new FunctionNotSupportedException(OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName, this.getClass().getName(),
                    serverName), this.getClass().getName(), methodName);
        }
    }
}
