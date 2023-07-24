/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSDynamicTypeMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

/**
 * The InMemoryOMRSMetadataCollection represents a metadata repository that supports an in-memory repository.
 * Requests to this metadata collection work with the hashmaps used to manage metadata types and instances.
 */
public class InMemoryOMRSMetadataCollection extends OMRSDynamicTypeMetadataCollectionBase
{
    private final InMemoryOMRSMetadataStore  repositoryStore;


    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of the repository - used for logging.
     * @param repositoryHelper class used to build type definitions and instances.
     * @param repositoryValidator class used to validate type definitions and instances.
     * @param metadataCollectionId unique Identifier of the metadata collection id.
     */
    protected InMemoryOMRSMetadataCollection(InMemoryOMRSRepositoryConnector parentConnector,
                                             String                          repositoryName,
                                             OMRSRepositoryHelper            repositoryHelper,
                                             OMRSRepositoryValidator         repositoryValidator,
                                             String                          metadataCollectionId)
    {
        /*
         * The metadata collection id is the unique identifier for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        /*
         * Set up the repository name in the repository store
         */
        this.repositoryStore = new InMemoryOMRSMetadataStore(repositoryName, repositoryHelper, metadataCollectionId);
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns a boolean indicating if the entity is stored in the metadata collection.
     * Notice that entities in DELETED state are returned by this call.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return entity details if the entity is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail isEntityKnown(String     userId,
                                      String     guid) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        return repositoryStore.getEntity(guid);
    }


    /**
     * Return the header and classifications for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntitySummary getEntitySummary(String     userId,
                                          String     guid) throws InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  EntityNotKnownException,
                                                                  UserNotAuthorizedException
    {
        final String  methodName = "getEntitySummary";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        EntitySummary entity = repositoryStore.getEntitySummary(guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String     userId,
                                        String     guid) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityProxyOnlyException,
                                                                UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        EntityDetail  entity = this.isEntityKnown(userId, guid);
        if (entity == null)
        {
            EntityProxy entityProxy = repositoryStore.getEntityProxy(guid);

            if (entityProxy != null)
            {
                reportEntityProxyOnly(guid, guidParameterName, methodName);
            }
        }

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }


    /**
     * Return a historical version of an entity - includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid or date is null or date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  EntityDetail getEntityDetail(String     userId,
                                         String     guid,
                                         Date       asOfTime) throws InvalidParameterException,
                                                                     RepositoryErrorException,
                                                                     EntityNotKnownException,
                                                                     EntityProxyOnlyException,
                                                                     UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Perform operation
         */
        EntityDetail  entity = repositoryStore.timeWarpEntityStore(asOfTime).get(guid);
        if (entity == null)
        {
            EntityProxy  entityProxy = repositoryStore.getEntityProxy(guid);

            if (entityProxy != null)
            {
                reportEntityProxyOnly(guid, guidParameterName, methodName);
            }
        }

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize) throws InvalidParameterException,
                                                                                                    TypeErrorException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    PropertyErrorException,
                                                                                                    PagingErrorException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipsForEntity";

        /*
         * Validate parameters
         */
        super.getRelationshipsForEntityParameterValidation(userId,
                                                           entityGUID,
                                                           relationshipTypeGUID,
                                                           fromRelationshipElement,
                                                           limitResultsByStatus,
                                                           asOfTime,
                                                           sequencingProperty,
                                                           sequencingOrder,
                                                           pageSize);

        /*
         * Perform operation
         */
        EntitySummary  entity = this.getEntitySummary(userId, entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        List<Relationship> entityRelationships = new ArrayList<>();

        Map<String, Relationship>   relationshipStore = repositoryStore.timeWarpRelationshipStore(asOfTime);

        for (Relationship  storedRelationship : relationshipStore.values())
        {
            if (storedRelationship != null)
            {
                if (storedRelationship.getStatus() != InstanceStatus.DELETED)
                {
                    repositoryValidator.validRelationship(repositoryName, storedRelationship);

                    if (repositoryHelper.relatedEntity(repositoryName,
                                                       entityGUID,
                                                       storedRelationship))
                    {
                        if (relationshipTypeGUID == null)
                        {
                            entityRelationships.add(storedRelationship);
                        }
                        else if (relationshipTypeGUID.equals(storedRelationship.getType().getTypeDefGUID()))
                        {
                            entityRelationships.add(storedRelationship);
                        }
                    }
                }
            }
        }

        if (entityRelationships.isEmpty())
        {
            return null;
        }

        return repositoryHelper.formatRelationshipResults(entityRelationships,
                                                          fromRelationshipElement,
                                                          sequencingProperty,
                                                          sequencingOrder,
                                                          pageSize);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                     String                    entityTypeGUID,
                                                     InstanceProperties        matchProperties,
                                                     MatchCriteria             matchCriteria,
                                                     int                       fromEntityElement,
                                                     List<InstanceStatus>      limitResultsByStatus,
                                                     List<String>              limitResultsByClassification,
                                                     Date                      asOfTime,
                                                     String                    sequencingProperty,
                                                     SequencingOrder           sequencingOrder,
                                                     int                       pageSize) throws InvalidParameterException,
                                                                                                RepositoryErrorException,
                                                                                                TypeErrorException,
                                                                                                PropertyErrorException,
                                                                                                PagingErrorException,
                                                                                                UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyParameterValidation(userId,
                                                        entityTypeGUID,
                                                        matchProperties,
                                                        matchCriteria,
                                                        fromEntityElement,
                                                        limitResultsByStatus,
                                                        limitResultsByClassification,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        pageSize);

        /*
         * Perform operation
         *
         * This is a brute force implementation of locating in entity since it iterates through all
         * the stored entities.
         */
        List<EntityDetail>         foundEntities = new ArrayList<>();
        Map<String, EntityDetail>  entityStore = repositoryStore.timeWarpEntityStore(asOfTime);

        for (EntityDetail  entity : entityStore.values())
        {
            if (entity != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, entityTypeGUID, entity)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity)) &&
                    (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity)) &&
                    (repositoryValidator.verifyMatchingInstancePropertyValues(matchProperties,
                                                                              entity,
                                                                              entity.getProperties(),
                                                                              matchCriteria
                                                                              )))
                {
                    foundEntities.add(entity);
                }
            }
        }

        return repositoryHelper.formatEntityResults(foundEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
    }


    /**
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity property conditions to match.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> findEntities(String                    userId,
                                           String                    entityTypeGUID,
                                           List<String>              entitySubtypeGUIDs,
                                           SearchProperties          matchProperties,
                                           int                       fromEntityElement,
                                           List<InstanceStatus>      limitResultsByStatus,
                                           SearchClassifications     matchClassifications,
                                           Date                      asOfTime,
                                           String                    sequencingProperty,
                                           SequencingOrder           sequencingOrder,
                                           int                       pageSize) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      PagingErrorException,
                                                                                      UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.findEntitiesParameterValidation(userId,
                                              entityTypeGUID,
                                              entitySubtypeGUIDs,
                                              matchProperties,
                                              fromEntityElement,
                                              limitResultsByStatus,
                                              matchClassifications,
                                              asOfTime,
                                              sequencingProperty,
                                              sequencingOrder,
                                              pageSize);

        /*
         * Perform operation
         *
         * This is a brute force implementation of locating in entity since it iterates through all
         * the stored entities.
         */
        List<EntityDetail>         foundEntities = new ArrayList<>();
        Map<String, EntityDetail>  entityStore = repositoryStore.timeWarpEntityStore(asOfTime);

        for (EntityDetail  entity : entityStore.values())
        {
            if (entity != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, entityTypeGUID, entitySubtypeGUIDs, entity)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity)) &&
                    (repositoryValidator.verifyMatchingClassifications(matchClassifications, entity)) &&
                    (repositoryValidator.verifyMatchingInstancePropertyValues(matchProperties, entity, entity.getProperties())))
                {
                    foundEntities.add(entity);
                }
            }
        }

        return repositoryHelper.formatEntityResults(foundEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
    }


    /**
     * Return a list of entities that have the requested type of classifications attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null means any type of entity
     *                       (but could be slow so not recommended).
     * @param classificationName name of the classification, note a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search (where any String
     *                                      property's value should be defined as a Java regular expression, even if it
     *                                      should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<EntityDetail> findEntitiesByClassification(String                    userId,
                                                            String                    entityTypeGUID,
                                                            String                    classificationName,
                                                            InstanceProperties        matchClassificationProperties,
                                                            MatchCriteria             matchCriteria,
                                                            int                       fromEntityElement,
                                                            List<InstanceStatus>      limitResultsByStatus,
                                                            Date                      asOfTime,
                                                            String                    sequencingProperty,
                                                            SequencingOrder           sequencingOrder,
                                                            int                       pageSize) throws InvalidParameterException,
                                                                                                       TypeErrorException,
                                                                                                       RepositoryErrorException,
                                                                                                       ClassificationErrorException,
                                                                                                       PropertyErrorException,
                                                                                                       PagingErrorException,
                                                                                                       UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.findEntitiesByClassificationParameterValidation(userId,
                                                              entityTypeGUID,
                                                              classificationName,
                                                              matchClassificationProperties,
                                                              matchCriteria,
                                                              fromEntityElement,
                                                              limitResultsByStatus,
                                                              asOfTime,
                                                              sequencingProperty,
                                                              sequencingOrder,
                                                              pageSize);

        /*
         * Perform operation
         *
         * This is a brute force implementation of locating in entity since it iterates through all
         * the stored entities.
         */
        Map<String, EntityDetail>   entityStore = repositoryStore.timeWarpEntityStore(asOfTime);
        List<EntityDetail>          foundEntities = new ArrayList<>();

        List<String>                classificationList = new ArrayList<>();
        classificationList.add(classificationName);

        for (EntityDetail  entity : entityStore.values())
        {
            if (entity != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, entityTypeGUID, entity)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity)) &&
                    (repositoryValidator.verifyEntityIsClassified(classificationList, entity)))
                {
                    List<Classification>   entityClassifications = entity.getClassifications();

                    if (entityClassifications != null)
                    {
                        /*
                         * Locate the matching classification and validate its properties
                         */
                        for (Classification entityClassification : entityClassifications)
                        {
                            if (entityClassification != null)
                            {
                                if (classificationName.equals(entityClassification.getName()))
                                {
                                    if (repositoryValidator.verifyMatchingInstancePropertyValues(
                                            matchClassificationProperties,
                                            entityClassification,
                                            entityClassification.getProperties(),
                                            matchCriteria
                                            ))

                                    {
                                        foundEntities.add(entity);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return repositoryHelper.formatEntityResults(foundEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId  unique identifier for requesting user.
     * @param entityTypeGUID  GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within entity instances of the specified type(s).
     *                       This parameter must not be null.
     * @param fromEntityElement  the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus  By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification  List of classifications that must be present on all returned entities.
     * @param asOfTime  Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty  String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder  Enum defining how the results should be ordered.
     * @param pageSize  the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria - null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
                                                                                                  TypeErrorException,
                                                                                                  RepositoryErrorException,
                                                                                                  PropertyErrorException,
                                                                                                  PagingErrorException,
                                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";

        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyValueParameterValidation(userId,
                                                             entityTypeGUID,
                                                             searchCriteria,
                                                             fromEntityElement,
                                                             limitResultsByStatus,
                                                             limitResultsByClassification,
                                                             asOfTime,
                                                             sequencingProperty,
                                                             sequencingOrder,
                                                             pageSize);

        /*
         * Process operation
         *
         * This is a brute force implementation of locating in entity since it iterates through all
         * the stored entities.
         */
        List<EntityDetail>   foundEntities = new ArrayList<>();

        for (EntityDetail  entity : repositoryStore.timeWarpEntityStore(asOfTime).values())
        {
            if (entity != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, entityTypeGUID, entity)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity)) &&
                    (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity)) &&
                    (repositoryValidator.verifyInstancePropertiesMatchSearchCriteria(repositoryName,
                                                                                    entity.getProperties(),
                                                                                    searchCriteria,
                                                                                    methodName)))
                {
                    foundEntities.add(entity);
                }
            }
        }

        return repositoryHelper.formatEntityResults(foundEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
    }



    /**
     * Returns a relationship indicating if the relationship is stored in the metadata collection.
     * Notice that relationships in DELETED state are returned by this call.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique identifier for the relationship.
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship  isRelationshipKnown(String     userId,
                                             String     guid) throws InvalidParameterException,
                                                                     RepositoryErrorException,
                                                                     UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        return repositoryStore.getRelationship(guid);
    }


    /**
     * Return a requested relationship.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               RelationshipNotKnownException,
                                                               UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        Relationship  relationship = repositoryStore.getRelationship(guid);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);

        return relationship;
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or data is for a future time
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  Relationship getRelationship(String    userId,
                                         String    guid,
                                         Date      asOfTime) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    RelationshipNotKnownException,
                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Perform operation
         */
        Relationship  relationship = repositoryStore.timeWarpRelationshipStore(asOfTime).get(guid);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);

        return relationship;
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<Relationship>} of each historical version of the relationship within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    RelationshipNotKnownException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipHistory";

        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Perform operation
         */
        boolean oldestFirst = (sequencingOrder == HistorySequencingOrder.FORWARDS);
        List<Relationship> relationshipHistory = repositoryStore.getRelationshipHistory(guid, fromTime, toTime, oldestFirst);

        if (relationshipHistory == null)
        {
            repositoryValidator.validateRelationshipFromStore(repositoryName, guid, null, methodName);
        }
        else
        {
            if (relationshipHistory.isEmpty())
            {
                return null;
            }

            if (startFromElement > relationshipHistory.size())
            {
                return null;
            }

            if ((pageSize == 0) || (pageSize >= relationshipHistory.size()))
            {
                return new ArrayList<>(relationshipHistory.subList(startFromElement, relationshipHistory.size()));
            }
            else
            {
                return new ArrayList<>(relationshipHistory.subList(startFromElement, pageSize));
            }
        }

        return  null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationships(String                    userId,
                                                 String                    relationshipTypeGUID,
                                                 List<String>              relationshipSubtypeGUIDs,
                                                 SearchProperties          matchProperties,
                                                 int                       fromRelationshipElement,
                                                 List<InstanceStatus>      limitResultsByStatus,
                                                 Date                      asOfTime,
                                                 String                    sequencingProperty,
                                                 SequencingOrder           sequencingOrder,
                                                 int                       pageSize) throws InvalidParameterException,
                                                                                            TypeErrorException,
                                                                                            RepositoryErrorException,
                                                                                            PropertyErrorException,
                                                                                            PagingErrorException,
                                                                                            FunctionNotSupportedException,
                                                                                            UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.findRelationshipsParameterValidation(userId,
                                                   relationshipTypeGUID,
                                                   relationshipSubtypeGUIDs,
                                                   matchProperties,
                                                   fromRelationshipElement,
                                                   limitResultsByStatus,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);


        /*
         * Perform operation
         *
         * This is a brute force implementation of locating a relationship since it iterates through all
         * the stored entities.
         */
        List<Relationship>         foundRelationships = new ArrayList<>();
        Map<String, Relationship>  relationshipStore = repositoryStore.timeWarpRelationshipStore(asOfTime);

        for (Relationship  relationship : relationshipStore.values())
        {
            if (relationship != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, relationshipTypeGUID, relationshipSubtypeGUIDs, relationship)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)) &&
                    (repositoryValidator.verifyMatchingInstancePropertyValues(matchProperties, relationship, relationship.getProperties())))
                {
                    foundRelationships.add(relationship);
                }
            }
        }

        return repositoryHelper.formatRelationshipResults(foundRelationships,
                fromRelationshipElement,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be received as a series of pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties Optional list of relationship properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize) throws InvalidParameterException,
                                                                                                      TypeErrorException,
                                                                                                      RepositoryErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      PagingErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyParameterValidation(userId,
                                                             relationshipTypeGUID,
                                                             matchProperties,
                                                             matchCriteria,
                                                             fromRelationshipElement,
                                                             limitResultsByStatus,
                                                             asOfTime,
                                                             sequencingProperty,
                                                             sequencingOrder,
                                                             pageSize);
        

        /*
         * Perform operation
         *
         * This is a brute force implementation of locating a relationship since it iterates through all
         * the stored entities.
         */
        List<Relationship>         foundRelationships = new ArrayList<>();
        Map<String, Relationship>  relationshipStore = repositoryStore.timeWarpRelationshipStore(asOfTime);

        for (Relationship  relationship : relationshipStore.values())
        {
            if (relationship != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, relationshipTypeGUID, relationship)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)) &&
                    (repositoryValidator.verifyMatchingInstancePropertyValues(matchProperties,
                                                                              relationship,
                                                                              relationship.getProperties(),
                                                                              matchCriteria)))
                {
                    foundRelationships.add(relationship);
                }
            }
        }

        return repositoryHelper.formatRelationshipResults(foundRelationships,
                                                          fromRelationshipElement,
                                                          sequencingProperty,
                                                          sequencingOrder,
                                                          pageSize);
    }


    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the relationship instances of the specified type(s).
     *                       This parameter must not be null.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize) throws InvalidParameterException,
                                                                                                           TypeErrorException,
                                                                                                           RepositoryErrorException,
                                                                                                           PropertyErrorException,
                                                                                                           PagingErrorException,
                                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByPropertyValue";


        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyValueParameterValidation(userId,
                                                                  relationshipTypeGUID,
                                                                  searchCriteria,
                                                                  fromRelationshipElement,
                                                                  limitResultsByStatus,
                                                                  asOfTime,
                                                                  sequencingProperty,
                                                                  sequencingOrder,
                                                                  pageSize);

        /*
         * Perform operation
         *
         * This is a brute force implementation of locating a relationship since it iterates through all
         * the stored relationships.
         */
        List<Relationship>  foundRelationships = new ArrayList<>();

        for (Relationship  relationship : repositoryStore.timeWarpRelationshipStore(asOfTime).values())
        {
            if (relationship != null)
            {
                if ((repositoryValidator.verifyInstanceType(repositoryName, relationshipTypeGUID, relationship)) &&
                    (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)) &&
                    (repositoryValidator.verifyInstancePropertiesMatchSearchCriteria(repositoryName,
                                                                                     relationship.getProperties(),
                                                                                     searchCriteria,
                                                                                     methodName)))
                {
                    foundRelationships.add(relationship);
                }
            }
        }

        return repositoryHelper.formatRelationshipResults(foundRelationships,
                                         fromRelationshipElement,
                                         sequencingProperty,
                                         sequencingOrder,
                                         pageSize);
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param entityTypeGUIDs list of entity types to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs list of relationship types to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException one or more of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  InstanceGraph getEntityNeighborhood(String               userId,
                                                String               entityGUID,
                                                List<String>         entityTypeGUIDs,
                                                List<String>         relationshipTypeGUIDs,
                                                List<InstanceStatus> limitResultsByStatus,
                                                List<String>         limitResultsByClassification,
                                                Date                 asOfTime,
                                                int                  level) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                                  = "getEntityNeighborhood";
        final String entityGUIDParameterName                     = "entityGUID";
        final String entityTypeGUIDParameterName                 = "entityTypeGUIDs";
        final String relationshipTypeGUIDParameterName           = "relationshipTypeGUIDs";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";
        final String asOfTimeParameter                           = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        if (entityTypeGUIDs != null)
        {
            for (String guid : entityTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null)
        {
            for (String guid : relationshipTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
            }
        }

        if (limitResultsByClassification != null)
        {
            for (String classificationName : limitResultsByClassification)
            {
                repositoryValidator.validateClassificationName(repositoryName,
                                                               limitedResultsByClassificationParameterName,
                                                               classificationName,
                                                               methodName);
            }
        }

        /*
         * Time warp the stores
         */
        Map<String, EntityDetail>   entityStore = repositoryStore.timeWarpEntityStore(asOfTime);
        Map<String, Relationship>   relationshipStore = repositoryStore.timeWarpRelationshipStore(asOfTime);

        InMemoryEntityNeighbourhood inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,
                                                                                                  repositoryName,
                                                                                                  repositoryValidator,
                                                                                                  entityStore,
                                                                                                  relationshipStore,
                                                                                                  entityGUID,
                                                                                                  entityTypeGUIDs,
                                                                                                  relationshipTypeGUIDs,
                                                                                                  limitResultsByStatus,
                                                                                                  limitResultsByClassification,
                                                                                                  level);


        return inMemoryEntityNeighbourhood.createInstanceGraph();
    }


    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity - null means no properties.
     * @param initialClassifications initial list of classifications for the new entity - null means no classifications.
     * @param initialStatus initial status - typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addEntity(String                     userId,
                                  String                     entityTypeGUID,
                                  InstanceProperties         initialProperties,
                                  List<Classification>       initialClassifications,
                                  InstanceStatus             initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   ClassificationErrorException,
                                                                                   StatusNotSupportedException,
                                                                                   UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException
    {
        final String methodName = "addEntity";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addEntityParameterValidation(userId,
                                                             entityTypeGUID,
                                                             initialProperties,
                                                             initialClassifications,
                                                             initialStatus,
                                                             methodName);

        /*
         * Validation complete - ok to create new instance
         */
        EntityDetail   newEntity = repositoryHelper.getNewEntity(repositoryName,
                                                                 metadataCollectionId,
                                                                 metadataCollectionName,
                                                                 InstanceProvenanceType.LOCAL_COHORT,
                                                                 userId,
                                                                 typeDef.getName(),
                                                                 initialProperties,
                                                                 initialClassifications);
        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            newEntity.setStatus(initialStatus);
        }

        return repositoryStore.createEntityInStore(newEntity);
    }


    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new entity is assigned a new GUID and put
     * in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addExternalEntity(String                userId,
                                          String                entityTypeGUID,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          InstanceProperties    initialProperties,
                                          List<Classification>  initialClassifications,
                                          InstanceStatus        initialStatus) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      ClassificationErrorException,
                                                                                      StatusNotSupportedException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName = "addExternalEntity";

        TypeDef typeDef = super.addExternalEntityParameterValidation(userId,
                                                                     entityTypeGUID,
                                                                     externalSourceGUID,
                                                                     initialProperties,
                                                                     initialClassifications,
                                                                     initialStatus,
                                                                     methodName);

        /*
         * Validation complete - ok to create new instance
         */
        EntityDetail   newEntity = repositoryHelper.getNewEntity(repositoryName,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 InstanceProvenanceType.EXTERNAL_SOURCE,
                                                                 userId,
                                                                 typeDef.getName(),
                                                                 initialProperties,
                                                                 initialClassifications);

        newEntity.setMetadataCollectionName(externalSourceName);
        newEntity.setReplicatedBy(metadataCollectionId);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            newEntity.setStatus(initialStatus);
        }

        return repositoryStore.createEntityInStore(newEntity);
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws InvalidParameterException the entity proxy is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                UserNotAuthorizedException,
                                                                FunctionNotSupportedException
    {
        /*
         * Validate parameters
         */
        super.addEntityProxyParameterValidation(userId, entityProxy);

        /*
         * Validation complete
         */
        repositoryStore.addEntityProxyToStore(entityProxy);
    }


    /**
     * Update the status for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetail showing the current entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              StatusNotSupportedException,
                                                                              UserNotAuthorizedException,
                                                                              FunctionNotSupportedException
    {
        final String  methodName               = "updateEntityStatus";
        final String  statusParameterName      = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        TypeDef typeDef = super.getTypeDefForInstance(entity, methodName);

        repositoryValidator.validateNewStatus(repositoryName, statusParameterName, newStatus, typeDef, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setStatus(newStatus);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(entityGUID);
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws InvalidParameterException,
                                                                                       RepositoryErrorException,
                                                                                       EntityNotKnownException,
                                                                                       PropertyErrorException,
                                                                                       UserNotAuthorizedException,
                                                                                       FunctionNotSupportedException
    {
        final String  methodName = "updateEntityProperties";
        final String  propertiesParameterName  = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        TypeDef typeDef = super.getTypeDefForInstance(entity, methodName);

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                                                         propertiesParameterName,
                                                         typeDef,
                                                         properties,
                                                         methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setProperties(properties);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(entityGUID);
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    EntityNotKnownException,
                                                                    UserNotAuthorizedException,
                                                                    FunctionNotSupportedException
    {
        final String  methodName = "undoEntityUpdate";
        final String  parameterName = "entityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, entityGUID, parameterName, methodName);

        /*
         * Validation complete - ok to restore entity
         */
        EntityDetail currentEntity  = repositoryStore.getEntity(entityGUID);
        EntityDetail restoredEntity = repositoryStore.retrievePreviousVersionOfEntity(entityGUID);

        if (restoredEntity != null)
        {
            restoredEntity = repositoryHelper.incrementVersion(userId, currentEntity, restoredEntity);

            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, restoredEntity, methodName);
            repositoryValidator.validateEntityIsNotDeleted(repositoryName, restoredEntity, methodName);

            repositoryStore.addEntityToStore(restoredEntity);

            return repositoryStore.getEntity(entityGUID);
        }
        else
        {
            return null;
        }
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All homed relationships to the entity are also soft-deleted and will no longer be usable, while any
     * reference copy relationships to the entity will be purged (and will no longer be accessible in this repository).
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete() call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use; however,
     * this will not restore any of the relationships that were soft-deleted as part of the original deleteEntity() call.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
     * @return deleted entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail   deleteEntity(String    userId,
                                       String    typeDefGUID,
                                       String    typeDefName,
                                       String    obsoleteEntityGUID) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            UserNotAuthorizedException,
                                                                            FunctionNotSupportedException
    {
        final String methodName    = "deleteEntity";
        final String parameterName = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteEntityGUID, parameterName, methodName);

        /*
         * Locate Entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(obsoleteEntityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, obsoleteEntityGUID, entity, methodName);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          entity,
                                                          methodName);

        repositoryValidator.validateInstanceStatusForDelete(repositoryName, entity, methodName);

        /*
         * Locate/delete relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                                                                              obsoleteEntityGUID,
                                                                              null,
                                                                              0,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              10000);


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        InstanceType type = relationship.getType();
                        if (type != null)
                        {
                            if (metadataCollectionId.equals(relationship.getMetadataCollectionId()))
                            {
                                this.deleteRelationship(userId,
                                                        type.getTypeDefGUID(),
                                                        type.getTypeDefName(),
                                                        relationship.getGUID());
                            }
                            else
                            {
                                repositoryStore.removeRelationshipFromStore(relationship);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception  error)
        {
            // nothing to do - keep going
        }


        /*
         * The deleteEntity() call is a soft-delete that updates the status DELETED.
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setStatusOnDelete(entity.getStatus());
        updatedEntity.setStatus(InstanceStatus.DELETED);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(obsoleteEntityGUID);
    }


    /**
     * Permanently removes a deleted entity from the metadata collection. All relationships to the entity -- both homed
     * and reference copies -- will also be purged to maintain referential integrity within the repository. This request
     * can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is not in DELETED status and so can not be purged
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityNotDeletedException,
                                                                UserNotAuthorizedException,
                                                                FunctionNotSupportedException
    {
        final String methodName    = "purgeEntity";
        final String parameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedEntityGUID, parameterName, methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(deletedEntityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          entity,
                                                          methodName);

        repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);


        /*
         * Locate/purge relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                                                                              deletedEntityGUID,
                                                                              null,
                                                                              0,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              10000);


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        repositoryStore.removeRelationshipFromStore(relationship);
                    }
                }
            }
        }
        catch (Exception  error)
        {
            // nothing to do - keep going
        }

        /*
         * Validation is complete - ok to remove the entity
         */
        repositoryStore.purgeEntityFromStore(deletedEntityGUID);
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          EntityNotDeletedException,
                                                                          UserNotAuthorizedException,
                                                                          FunctionNotSupportedException
    {
        final String  methodName              = "restoreEntity";
        final String  parameterName           = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, parameterName, methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(deletedEntityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);
        repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);

        /*
         * Validation is complete.  It is ok to restore the entity.
         */

        EntityDetail restoredEntity = new EntityDetail(entity);

        restoredEntity.setStatus(entity.getStatusOnDelete());
        restoredEntity.setStatusOnDelete(null);

        restoredEntity = repositoryHelper.incrementVersion(userId, entity, restoredEntity);

        repositoryStore.updateEntityInStore(restoredEntity);

        return repositoryStore.getEntity(deletedEntityGUID);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName                  = "classifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName,
                                                   classificationParameterName,
                                                   classificationName,
                                                   entityType.getTypeDefName(),
                                                   methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                                                                 classificationName,
                                                                 propertiesParameterName,
                                                                 classificationProperties,
                                                                 methodName);

            /*
             * Validation complete - build the new classification
             */
            newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                      null,
                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                      userId,
                                                                      classificationName,
                                                                      entityType.getTypeDefName(),
                                                                      ClassificationOrigin.ASSIGNED,
                                                                      null,
                                                                      classificationProperties);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(repositoryName,
                                                                                                                        classificationName,
                                                                                                                        entityType.getTypeDefName()),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Validation complete - ok to update entity
         */

        EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                entity,
                                                                                newClassification,
                                                                                methodName);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(entityGUID);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName                  = "classifyEntity (EntityProxy)";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        String entityGUID = entityProxy.getGUID();
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = repositoryStore.getEntity(entityGUID);
        if (entity == null)
        {
            repositoryStore.addEntityProxyToStore(entityProxy);
            entity = repositoryStore.getEntityProxy(entityGUID);
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName,
                                                   classificationParameterName,
                                                   classificationName,
                                                   entityType.getTypeDefName(),
                                                   methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                                                                 classificationName,
                                                                 propertiesParameterName,
                                                                 classificationProperties,
                                                                 methodName);

            /*
             * Validation complete - build the new classification
             */
            newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                      null,
                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                      userId,
                                                                      classificationName,
                                                                      entityType.getTypeDefName(),
                                                                      ClassificationOrigin.ASSIGNED,
                                                                      null,
                                                                      classificationProperties);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(repositoryName,
                                                                                                                        classificationName,
                                                                                                                        entityType.getTypeDefName()),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Validation complete - ok to update entity
         */
        if (entity instanceof EntityDetail)
        {
            EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                    (EntityDetail) entity,
                                                                                    newClassification,
                                                                                    methodName);
            repositoryStore.updateEntityInStore(updatedEntity);
        }
        else
        {
            EntityProxy updatedProxy = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                  (EntityProxy) entity,
                                                                                  newClassification,
                                                                                  methodName);
            repositoryStore.addEntityProxyToStore(updatedProxy);
        }

        return newClassification;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     * @return EntitySummary showing the resulting entity header and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public   EntityDetail classifyEntity(String               userId,
                                         String               entityGUID,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed)";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName,
                                                   classificationParameterName,
                                                   classificationName,
                                                   entityType.getTypeDefName(),
                                                   methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                                                                 classificationName,
                                                                 propertiesParameterName,
                                                                 classificationProperties,
                                                                 methodName);

            /*
             * Validation complete - build the new classification
             */
            if (externalSourceGUID == null)
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                          null,
                                                                          InstanceProvenanceType.LOCAL_COHORT,
                                                                          userId,
                                                                          classificationName,
                                                                          entityType.getTypeDefName(),
                                                                          classificationOrigin,
                                                                          classificationOriginGUID,
                                                                          classificationProperties);
            }
            else
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                          externalSourceGUID,
                                                                          externalSourceName,
                                                                          InstanceProvenanceType.EXTERNAL_SOURCE,
                                                                          userId,
                                                                          classificationName,
                                                                          entityType.getTypeDefName(),
                                                                          classificationOrigin,
                                                                          classificationOriginGUID,
                                                                          classificationProperties);
                newClassification.setMetadataCollectionName(externalSourceName);
                newClassification.setReplicatedBy(metadataCollectionId);
            }
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(repositoryName,
                                                                                                                        classificationName,
                                                                                                                        entityType.getTypeDefName()),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Validation complete - ok to update entity
         */
        EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                entity,
                                                                                newClassification,
                                                                                methodName);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(entityGUID);
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     * @return Classification showing the resulting entity header and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed - EntityProxy)";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        String entityGUID = entityProxy.getGUID();
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = repositoryStore.getEntity(entityGUID);
        if (entity == null)
        {
            repositoryStore.addEntityProxyToStore(entityProxy);
            entity = repositoryStore.getEntityProxy(entityGUID);
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName,
                                                   classificationParameterName,
                                                   classificationName,
                                                   entityType.getTypeDefName(),
                                                   methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                                                                 classificationName,
                                                                 propertiesParameterName,
                                                                 classificationProperties,
                                                                 methodName);

            /*
             * Validation complete - build the new classification
             */
            if (externalSourceGUID == null)
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                         null,
                                                                         InstanceProvenanceType.LOCAL_COHORT,
                                                                         userId,
                                                                         classificationName,
                                                                         entityType.getTypeDefName(),
                                                                         classificationOrigin,
                                                                         classificationOriginGUID,
                                                                         classificationProperties);
            }
            else
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                                                                          externalSourceGUID,
                                                                          externalSourceName,
                                                                          InstanceProvenanceType.EXTERNAL_SOURCE,
                                                                          userId,
                                                                          classificationName,
                                                                          entityType.getTypeDefName(),
                                                                          classificationOrigin,
                                                                          classificationOriginGUID,
                                                                          classificationProperties);
                newClassification.setMetadataCollectionName(externalSourceName);
                newClassification.setReplicatedBy(metadataCollectionId);
            }
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(repositoryName,
                                                                                                                        classificationName,
                                                                                                                        entityType.getTypeDefName()),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Validation complete - ok to update entity
         */
        if (entity instanceof EntityDetail)
        {
            EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                    (EntityDetail) entity,
                                                                                    newClassification,
                                                                                    methodName);
            repositoryStore.updateEntityInStore(updatedEntity);
        }
        else
        {
            EntityProxy updatedProxy = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                  (EntityProxy) entity,
                                                                                  newClassification,
                                                                                  methodName);
            repositoryStore.addEntityProxyToStore(updatedProxy);
        }

        return newClassification;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            ClassificationErrorException,
                                                                            UserNotAuthorizedException,
                                                                            FunctionNotSupportedException
    {
        final String  methodName = "declassifyEntity";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return repositoryStore.removeClassificationFromEntity(entity, classificationName);
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @return Classification that has been removed.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification declassifyEntity(String      userId,
                                           EntityProxy entityProxy,
                                           String      classificationName) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  EntityNotKnownException,
                                                                                  ClassificationErrorException,
                                                                                  UserNotAuthorizedException,
                                                                                  FunctionNotSupportedException
    {
        final String  methodName = "declassifyEntity (EntityProxy)";

        super.declassifyEntityParameterValidation(userId, entityProxy, classificationName, methodName);

        repositoryStore.addEntityProxyToStore(entityProxy);

        return repositoryStore.removeClassificationFromProxy(entityProxy, classificationName);
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException
    {
        final String  methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        super.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                     entity,
                                                                                     classificationName,
                                                                                     methodName);

        Classification  newClassification = new Classification(classification);

        newClassification.setProperties(properties);

        repositoryHelper.incrementVersion(userId, classification, newClassification);

        EntityDetail updatedEntity = repositoryHelper.updateClassificationInEntity(repositoryName,
                                                                                   userId,
                                                                                   entity,
                                                                                   newClassification,
                                                                                   methodName);

        repositoryStore.updateEntityInStore(updatedEntity);

        return repositoryStore.getEntity(entityGUID);
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification updateEntityClassification(String               userId,
                                                     EntityProxy          entityProxy,
                                                     String               classificationName,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName = "updateEntityClassification (EntityProxy)";

        /*
         * Validate parameters
         */
        super.classifyEntityParameterValidation(userId, entityProxy, classificationName, properties, methodName);
        String entityGUID = entityProxy.getGUID();

        /*
         * Save entity proxy
         */
        EntitySummary entity = repositoryStore.getEntity(entityGUID);
        if (entity == null)
        {
            repositoryStore.addEntityProxyToStore(entityProxy);
            entity = repositoryStore.getEntityProxy(entityGUID);
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                     entity,
                                                                                     classificationName,
                                                                                     methodName);

        Classification newClassification = new Classification(classification);
        newClassification.setProperties(properties);
        repositoryHelper.incrementVersion(userId, classification, newClassification);

        if( entity instanceof EntityDetail)
        {
            EntityDetail updatedEntity = repositoryHelper.updateClassificationInEntity(repositoryName,
                                                                                       userId,
                                                                                       (EntityDetail) entity,
                                                                                       newClassification,
                                                                                       methodName);
            repositoryStore.updateEntityInStore(updatedEntity);
        }
        else
        {
            EntityProxy updatedProxy = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                  (EntityProxy) entity,
                                                                                  newClassification,
                                                                                  methodName);
            repositoryStore.addEntityProxyToStore(updatedProxy);
        }

        return newClassification;
    }


    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<EntityDetail>} of each historical version of the entity detail within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> getEntityDetailHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    EntityProxyOnlyException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getEntityDetailHistory";

        /*
         * Validate parameters
         */
        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Perform operation
         */
        boolean oldestFirst = (sequencingOrder == HistorySequencingOrder.FORWARDS);
        List<EntityDetail> entityHistory = repositoryStore.getEntityHistory(guid, fromTime, toTime, oldestFirst);

        if (entityHistory == null)
        {
            repositoryValidator.validateEntityFromStore(repositoryName, guid, null, methodName);
        }
        else
        {
            if (entityHistory.isEmpty())
            {
                return null;
            }

            if (startFromElement > entityHistory.size())
            {
                return null;
            }

            if ((pageSize == 0) || (pageSize >= entityHistory.size()))
            {
                return new ArrayList<>(entityHistory.subList(startFromElement, entityHistory.size()));
            }
            else
            {
                return new ArrayList<>(entityHistory.subList(startFromElement, pageSize));
            }
        }

        return null;
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity - null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status - typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeErrorException,
                                                                                   PropertyErrorException,
                                                                                   EntityNotKnownException,
                                                                                   StatusNotSupportedException,
                                                                                   UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException
    {
        final String  methodName = "addRelationship";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addRelationshipParameterValidation(userId,
                                                                   relationshipTypeGUID,
                                                                   initialProperties,
                                                                   entityOneGUID,
                                                                   entityTwoGUID,
                                                                   initialStatus,
                                                                   methodName);


        /*
         * Validation complete - ok to create new instance
         */
        Relationship   relationship = repositoryHelper.getNewRelationship(repositoryName,
                                                                          metadataCollectionId,
                                                                          metadataCollectionName,
                                                                          InstanceProvenanceType.LOCAL_COHORT,
                                                                          userId,
                                                                          typeDef.getName(),
                                                                          initialProperties);
        /*
         * See if there is a proxy for entity 1
         */
        EntityProxy entityOneProxy = repositoryStore.getEntityProxy(entityOneGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityOneGUID, entityOneProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityOneProxy, methodName);

        /*
         * See if there is a proxy for entity 2
         */
        EntityProxy entityTwoProxy = repositoryStore.getEntityProxy(entityTwoGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityTwoGUID, entityTwoProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityTwoProxy, methodName);
        repositoryValidator.validateRelationshipEnds(repositoryName, entityOneProxy, entityTwoProxy, typeDef, methodName);

        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            relationship.setStatus(initialStatus);
        }

        return repositoryStore.createRelationshipInStore(relationship);
    }


    /**
     * Save a new relationship that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new relationship is assigned a new GUID and put
     * in the requested state.  The new relationship is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status; typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship addExternalRelationship(String               userId,
                                                String               relationshipTypeGUID,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                InstanceProperties   initialProperties,
                                                String               entityOneGUID,
                                                String               entityTwoGUID,
                                                InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           TypeErrorException,
                                                                                           PropertyErrorException,
                                                                                           EntityNotKnownException,
                                                                                           StatusNotSupportedException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException
    {
        final String methodName = "addExternalRelationship";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addRelationshipParameterValidation(userId,
                                                                   relationshipTypeGUID,
                                                                   initialProperties,
                                                                   entityOneGUID,
                                                                   entityTwoGUID,
                                                                   initialStatus,
                                                                   methodName);


        /*
         * Validation complete - ok to create new instance
         */
        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                                                                        externalSourceGUID,
                                                                        InstanceProvenanceType.EXTERNAL_SOURCE,
                                                                        userId,
                                                                        typeDef.getName(),
                                                                        initialProperties);

        relationship.setMetadataCollectionName(externalSourceName);
        relationship.setReplicatedBy(metadataCollectionId);

        /*
         * See if there is a proxy for entity 1
         */
        EntityProxy entityOneProxy = repositoryStore.getEntityProxy(entityOneGUID);

        /*
         * if not, see if there is an entity for end 1
         *
         */
        if (entityOneProxy == null)
        {
            EntityDetail entityOneDetail = repositoryStore.getEntity(entityOneGUID);
            entityOneProxy = repositoryHelper.getNewEntityProxy(repositoryName, entityOneDetail);
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityOneGUID, entityOneProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityOneProxy, methodName);

        /*
         * See if there is a proxy for entity 2
         */
        EntityProxy entityTwoProxy = repositoryStore.getEntityProxy(entityTwoGUID);

        /*
         * If not, see if there is an entity for end 2
         */
        if (entityTwoProxy == null)
        {
            EntityDetail entityTwoDetail = repositoryStore.getEntity(entityTwoGUID);
            entityTwoProxy = repositoryHelper.getNewEntityProxy(repositoryName, entityTwoDetail);
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityTwoGUID, entityTwoProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityTwoProxy, methodName);
        repositoryValidator.validateRelationshipEnds(repositoryName, entityOneProxy, entityTwoProxy, typeDef,
                                                     methodName);

        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            relationship.setStatus(initialStatus);
        }

        repositoryStore.createRelationshipInStore(relationship);

        return relationship;
    }


    /**
     * Update the status of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return Resulting relationship structure with the new status set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    StatusNotSupportedException,
                                                                                    UserNotAuthorizedException,
                                                                                    FunctionNotSupportedException
    {
        final String  methodName          = "updateRelationshipStatus";
        final String  statusParameterName = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);

        repositoryValidator.validateInstanceType(repositoryName, relationship);

        TypeDef typeDef = super.getTypeDefForInstance(relationship, methodName);

        repositoryValidator.validateNewStatus(repositoryName,
                                              statusParameterName,
                                              newStatus,
                                              typeDef,
                                              methodName);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatus(newStatus);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(updatedRelationship);

        return repositoryStore.getRelationship(relationshipGUID);
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             RelationshipNotKnownException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName = "updateRelationshipProperties";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, methodName);

        /*
         * Locate relationship
         */

        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, relationshipGUID, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);


        repositoryValidator.validateInstanceType(repositoryName, relationship);

        TypeDef typeDef = super.getTypeDefForInstance(relationship, methodName);

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                                                         propertiesParameterName,
                                                         typeDef,
                                                         properties,
                                                         methodName);

        /*
         * Validation complete - ok to make changes
         */

        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setProperties(properties);
        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(updatedRelationship);

        return repositoryStore.getRelationship(relationshipGUID);
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the new current header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                RelationshipNotKnownException,
                                                                                UserNotAuthorizedException,
                                                                                FunctionNotSupportedException
    {
        final String  methodName = "undoRelationshipUpdate";
        final String  parameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, relationshipGUID, parameterName, methodName);

        /*
         * Restore previous version
         */
        Relationship currentRelationship = repositoryStore.getRelationship(relationshipGUID);
        Relationship restoredRelationship = repositoryStore.retrievePreviousVersionOfRelationship(relationshipGUID);
        restoredRelationship = repositoryHelper.incrementVersion(userId, currentRelationship, restoredRelationship);

        repositoryStore.updateRelationshipInStore(restoredRelationship);

        repositoryValidator.validateRelationshipFromStore(repositoryName, relationshipGUID, restoredRelationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, restoredRelationship, methodName);

        return repositoryStore.getRelationship(relationshipGUID);
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED and, it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return deleted relationship
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship deleteRelationship(String    userId,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    obsoleteRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName = "deleteRelationship";
        final String  parameterName = "obsoleteRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, obsoleteRelationshipGUID);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          relationship,
                                                          methodName);

        /*
         * A deleteRelationship() call is a soft-delete that updates the status to "DELETED".
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatusOnDelete(relationship.getStatus());
        updatedRelationship.setStatus(InstanceStatus.DELETED);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(updatedRelationship);

        return repositoryStore.getRelationship(obsoleteRelationshipGUID);
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            RelationshipNotKnownException,
                                                                            RelationshipNotDeletedException,
                                                                            UserNotAuthorizedException,
                                                                            FunctionNotSupportedException
    {
        final String  methodName    = "purgeRelationship";
        final String  parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               deletedRelationshipGUID,
                                               parameterName,
                                               methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = repositoryStore.getRelationship(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          relationship,
                                                          methodName);

        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);


        /*
         * Validation is complete - ok to remove the relationship
         */
        repositoryStore.purgeRelationshipFromStore(deletedRelationshipGUID);
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      RelationshipNotDeletedException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName    = "restoreRelationship";
        final String  parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = repositoryStore.getRelationship(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);

        /*
         * Validation is complete.  It is ok to restore the relationship.
         */
        Relationship restoredRelationship = new Relationship(relationship);

        restoredRelationship.setStatus(relationship.getStatusOnDelete());
        restoredRelationship.setStatusOnDelete(null);

        restoredRelationship = repositoryHelper.incrementVersion(userId, relationship, restoredRelationship);

        repositoryStore.updateRelationshipInStore(restoredRelationship);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, restoredRelationship, methodName);

        return repositoryStore.getRelationship(deletedRelationshipGUID);
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity - used to verify the entity identity.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @return entity - new values for this entity, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID) throws InvalidParameterException,
                                                                          RepositoryErrorException,
                                                                          EntityNotKnownException,
                                                                          UserNotAuthorizedException,
                                                                          FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyEntity";
        final String  instanceParameterName = "entityGUID";
        final String  newInstanceParameterName = "newEntityGUID";

        /*
         * Validate parameters
         */
        this.reIdentifyInstanceParameterValidation(userId,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   entityGUID,
                                                   instanceParameterName,
                                                   newEntityGUID,
                                                   newInstanceParameterName,
                                                   methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   deletedEntity = new EntityDetail(entity);
        deletedEntity.setStatusOnDelete(entity.getStatus());
        deletedEntity.setStatus(InstanceStatus.DELETED);
        deletedEntity = repositoryHelper.incrementVersion(userId, entity, deletedEntity);

        EntityDetail   updatedEntity = new EntityDetail(entity);
        updatedEntity.setGUID(newEntityGUID);
        updatedEntity.setReIdentifiedFromGUID(entityGUID);
        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        EntityProxy deletedEntityProxy = repositoryHelper.getNewEntityProxy(repositoryName, deletedEntity);
        EntityProxy newEntityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);

        /*
         * Locate/re-point relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                                                                              entityGUID,
                                                                              null,
                                                                              0,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              10000);


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (relationship.getEntityOneProxy().getGUID().equals(entityGUID))
                        {
                            relationship.setEntityOneProxy(newEntityProxy);
                        }
                        else if (relationship.getEntityTwoProxy().getGUID().equals(entityGUID))
                        {
                            relationship.setEntityTwoProxy(newEntityProxy);
                        }
                        repositoryStore.updateRelationshipInStore(relationship);
                    }
                }
            }
        }
        catch (Exception  error)
        {
            // nothing to do - keep going
        }

        repositoryStore.updateEntityInStore(deletedEntity);
        repositoryStore.createEntityInStore(updatedEntity);

        /*
         * The repository store maintains an entity proxy for use with relationships.
         */
        repositoryStore.addEntityProxyToStore(deletedEntityProxy);
        repositoryStore.addEntityProxyToStore(newEntityProxy);

        return updatedEntity;
    }


    /**
     * Change the type of existing entity.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity - used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity - new values for this entity, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws ClassificationErrorException the entity's classifications are not valid for the new type.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeErrorException,
                                                                              PropertyErrorException,
                                                                              ClassificationErrorException,
                                                                              EntityNotKnownException,
                                                                              UserNotAuthorizedException,
                                                                              FunctionNotSupportedException
    {
        final String  methodName = "reTypeEntity";
        final String  entityParameterName = "entityGUID";
        final String  currentTypeDefParameterName = "currentTypeDefSummary";
        final String  newTypeDefParameterName = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                TypeDefCategory.ENTITY_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName,
                                                 entity,
                                                 currentTypeDefParameterName,
                                                 currentTypeDefParameterName,
                                                 currentTypeDefSummary.getGUID(),
                                                 currentTypeDefSummary.getName());

        repositoryValidator.validatePropertiesForType(repositoryName,
                                                      newTypeDefParameterName,
                                                      newTypeDefSummary,
                                                      entity.getProperties(),
                                                      methodName);

        repositoryValidator.validateClassificationList(repositoryName,
                                                       entityParameterName,
                                                       entity.getClassifications(),
                                                       newTypeDefSummary.getName(),
                                                       methodName);




        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedEntity.setType(newInstanceType);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        repositoryStore.updateEntityInStore(entity);

        /*
         * The repository store maintains an entity proxy for use with relationships.
         */
        EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);

        repositoryStore.addEntityProxyToStore(entityProxy);

        return updatedEntity;
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefGUID the guid of the TypeDef for the entity - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity - used to verify the entity identity.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return entity - new values for this entity, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          EntityNotKnownException,
                                                                                          UserNotAuthorizedException,
                                                                                          FunctionNotSupportedException
    {
        final String methodName          = "reHomeEntity";
        final String entityParameterName = "entityGUID";

        /*
         * Validate parameters
         */
        super.reHomeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                typeDefGUID,
                                                typeDefName,
                                                homeMetadataCollectionId,
                                                newHomeMetadataCollectionId,
                                                methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = repositoryStore.getEntity(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

        repositoryValidator.validateEntityCanBeRehomed(repositoryName, metadataCollectionId, entity, methodName);


        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedEntity.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedEntity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        repositoryStore.updateEntityInStore(updatedEntity);

        /*
         * The repository store maintains an entity proxy for use with relationships.
         */
        EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);

        repositoryStore.addEntityProxyToStore(entityProxy);

        return updatedEntity;
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID  the new unique identifier for the relationship.
     * @return relationship - new values for this relationship, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      RelationshipNotKnownException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyRelationship";
        final String  instanceParameterName = "relationshipGUID";
        final String  newInstanceParameterName = "newRelationshipGUID";

        /*
         * Validate parameters
         */
        this.reIdentifyInstanceParameterValidation(userId,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   relationshipGUID,
                                                   instanceParameterName,
                                                   newRelationshipGUID,
                                                   newInstanceParameterName,
                                                   methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   deletedRelationship = new Relationship(relationship);
        deletedRelationship.setStatusOnDelete(relationship.getStatus());
        deletedRelationship.setStatus(InstanceStatus.DELETED);
        deletedRelationship = repositoryHelper.incrementVersion(userId, relationship, deletedRelationship);

        Relationship   updatedRelationship = new Relationship(relationship);
        updatedRelationship.setGUID(newRelationshipGUID);
        updatedRelationship.setReIdentifiedFromGUID(relationshipGUID);
        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(deletedRelationship);
        repositoryStore.createRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    /**
     * Change the type an existing relationship.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship - used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
     * @return relationship - new values for this relationship, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
                                                                                    RepositoryErrorException,
                                                                                    TypeErrorException,
                                                                                    PropertyErrorException,
                                                                                    RelationshipNotKnownException,
                                                                                    UserNotAuthorizedException,
                                                                                    FunctionNotSupportedException
    {
        final String methodName = "reTypeRelationship";
        final String relationshipParameterName = "relationshipGUID";
        final String currentTypeDefParameterName = "currentTypeDefSummary";
        final String newTypeDefParameterName = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                relationshipGUID,
                                                relationshipParameterName,
                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);
        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);

        repositoryValidator.validateInstanceType(repositoryName,
                                                 relationship,
                                                 currentTypeDefParameterName,
                                                 currentTypeDefParameterName,
                                                 currentTypeDefSummary.getGUID(),
                                                 currentTypeDefSummary.getName());


        repositoryValidator.validatePropertiesForType(repositoryName,
                                                      newTypeDefParameterName,
                                                      newTypeDefSummary,
                                                      relationship.getProperties(),
                                                      methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedRelationship.setType(newInstanceType);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID  the unique identifier for the relationship.
     * @param typeDefGUID  the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName  the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param homeMetadataCollectionId  the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId  unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return relationship - new values for this relationship, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          RelationshipNotKnownException,
                                                                                          UserNotAuthorizedException,
                                                                                          FunctionNotSupportedException
    {
        final String  methodName               = "reHomeRelationship";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeRehomed(repositoryName, metadataCollectionId, relationship, methodName);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedRelationship.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedRelationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        repositoryStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId  unique identifier for requesting server.
     * @param entity  details of the entity to save.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveEntityReferenceCopy(String         userId,
                                        EntityDetail   entity) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      TypeErrorException,
                                                                      PropertyErrorException,
                                                                      HomeEntityException,
                                                                      EntityConflictException,
                                                                      InvalidEntityException,
                                                                      UserNotAuthorizedException
    {
        final String  methodName = "saveEntityReferenceCopy";
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateReferenceInstanceHeader(repositoryName,
                                                            metadataCollectionId,
                                                            instanceParameterName,
                                                            entity,
                                                            auditLog,
                                                            methodName);

        repositoryStore.addEntityToStore(entity);
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 EntityNotKnownException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException
    {
        final String methodName = "getHomeClassifications";
        final String guidParameterName = "entityGUID";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);

        return repositoryStore.getHomeClassifications(entityGUID);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId  unique identifier for requesting server.
     * @param entityGUID  the unique identifier for the entity.
     * @param typeDefGUID  the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName  the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param homeMetadataCollectionId  identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntityReferenceCopy(String   userId,
                                         String   entityGUID,
                                         String   typeDefGUID,
                                         String   typeDefName,
                                         String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   EntityNotKnownException,
                                                                                   HomeEntityException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                = "purgeEntityReferenceCopy";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
                                                        entityGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        entityParameterName,
                                                        homeMetadataCollectionId,
                                                        homeParameterName,
                                                        methodName);

        /*
         * Remove entity
         */
        EntityDetail  entity = repositoryStore.getEntity(entityGUID);
        if (entity != null)
        {
            repositoryStore.purgeEntityFromStore(entityGUID);
        }
        else
        {
            super.reportEntityNotKnown(entityGUID, methodName);
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException
    {
        final String  methodName = "saveClassificationReferenceCopy";
        final String  classificationParameterName = "classification";
        final String  propertiesParameterName = "classification.getProperties()";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        /*
         * If the entity is a later version than the stored entity, it is updated.
         */
        repositoryStore.addEntityToStore(entity);

        EntityDetail retrievedEntity = repositoryStore.getEntity(entity.getGUID());

        if (retrievedEntity != null)
        {
            try
            {
                repositoryValidator.validateEntityFromStore(repositoryName, entity.getGUID(), retrievedEntity, methodName);
                repositoryValidator.validateEntityIsNotDeleted(repositoryName, retrievedEntity, methodName);

                repositoryValidator.validateInstanceType(repositoryName, entity);

                InstanceType entityType = entity.getType();

                repositoryValidator.validateClassification(repositoryName,
                                                           classificationParameterName,
                                                           classification.getName(),
                                                           entityType.getTypeDefName(),
                                                           methodName);

                repositoryValidator.validateClassificationProperties(repositoryName,
                                                                     classification.getName(),
                                                                     propertiesParameterName,
                                                                     classification.getProperties(),
                                                                     methodName);

                /*
                 * Validation complete - ok to update entity
                 */
                EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                        new EntityDetail(retrievedEntity),
                                                                                        classification,
                                                                                        methodName);

                repositoryStore.updateEntityInStore(updatedEntity);
            }
            catch (EntityNotKnownException  error)
            {
                // Ignore since the entity has been removed since the classification was added
            }
            catch (ClassificationErrorException error)
            {
                throw new TypeErrorException(error);
            }
        }
    }



    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entityProxy,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException
    {
        final String  methodName = "saveClassificationReferenceCopy(proxy)";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryStore.addEntityProxyToStore(entityProxy);
        repositoryStore.saveClassification(entityProxy.getGUID(), classification);
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityDetail   entity,
                                                  Classification classification) throws TypeErrorException,
                                                                                        RepositoryErrorException
    {
        final String methodName = "purgeClassificationReferenceCopy";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryStore.removeClassificationFromEntity(entity, classification.getName());
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityProxy    entityProxy,
                                                  Classification classification) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        PropertyErrorException,
                                                                                        EntityConflictException,
                                                                                        InvalidEntityException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        FunctionNotSupportedException
    {
        final String methodName = "purgeClassificationReferenceCopy (proxy)";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryStore.addEntityProxyToStore(entityProxy);
        repositoryStore.removeClassificationFromProxy(entityProxy, classification.getName());
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId  unique identifier for requesting server.
     * @param relationship  relationship to save.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveRelationshipReferenceCopy(String         userId,
                                              Relationship   relationship) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  TypeErrorException,
                                                                                  EntityNotKnownException,
                                                                                  PropertyErrorException,
                                                                                  HomeRelationshipException,
                                                                                  RelationshipConflictException,
                                                                                  InvalidRelationshipException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "saveRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        repositoryStore.addEntityProxyToStore(relationship.getEntityOneProxy());
        repositoryStore.addEntityProxyToStore(relationship.getEntityTwoProxy());
        repositoryStore.addRelationshipToStore(relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationshipReferenceCopy(String   userId,
                                               String   relationshipGUID,
                                               String   typeDefGUID,
                                               String   typeDefName,
                                               String   homeMetadataCollectionId) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         RelationshipNotKnownException,
                                                                                         HomeRelationshipException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName                = "purgeRelationshipReferenceCopy";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";


        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
                                                        relationshipGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        relationshipParameterName,
                                                        homeMetadataCollectionId,
                                                        homeParameterName,
                                                        methodName);

        /*
         * Purge relationship
         */
        repositoryStore.purgeRelationshipFromStore(relationshipGUID);
    }
}
