/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database.DatabaseStore;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database.QueryBuilder;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers.ClassificationMapper;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers.EntityMapper;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers.RelationshipMapper;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryTable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

/**
 * PostgresOMRSMetadataStore provides the PostgreSQL store for the PostgreSQL RepositoryConnector.
 */
class PostgresOMRSMetadataStore
{
    private final String                repositoryName;
    private final OMRSRepositoryHelper  repositoryHelper;
    private final String                localMetadataCollectionId;
    private final boolean               isReadOnly;
    private final Date                  defaultAsOfTime;
    private final JDBCResourceConnector jdbcResourceConnector;



    /**
     * Constructor to initialize store.
     *
     * @param repositoryName name of this repository
     * @param repositoryHelper helper
     * @param localMetadataCollectionId metadata collection Id for this repository
     * @param isReadOnly should the repository run in readOnly mode?
     * @param defaultAsOfTime what is the default value for asOfTime?
     * @param jdbcResourceConnector connector to access the database
     */
    PostgresOMRSMetadataStore(String                repositoryName,
                              OMRSRepositoryHelper  repositoryHelper,
                              String                localMetadataCollectionId,
                              boolean               isReadOnly,
                              Date                  defaultAsOfTime,
                              JDBCResourceConnector jdbcResourceConnector)
    {
        this.repositoryName = repositoryName;
        this.repositoryHelper = repositoryHelper;
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.isReadOnly = isReadOnly;
        this.defaultAsOfTime = defaultAsOfTime;
        this.jdbcResourceConnector = jdbcResourceConnector;
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @return entity object
     * @throws RepositoryErrorException problem forming entity
     * @throws  EntityProxyOnlyException not a full entity
     */
    EntityDetail  getEntity(String guid) throws RepositoryErrorException,
                                                EntityProxyOnlyException
    {
        return this.getEntity(guid, null);
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return entity object
     * @throws RepositoryErrorException problem forming entity proxy
     * @throws  EntityProxyOnlyException not a full entity
     */
    EntityDetail  getEntity(String guid,
                            Date   asOfTime) throws RepositoryErrorException,
                                                    EntityProxyOnlyException
    {
        final String methodName = "getEntity";
        final String guidParameterName = "guid";

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        EntityMapper storedEntity = databaseStore.getEntityFromStore(guid, getAsOfTime(asOfTime));
        databaseStore.disconnect();

        if (storedEntity != null)
        {
            if (storedEntity.isProxy())
            {
                throw new EntityProxyOnlyException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(guid,
                                                                                                        repositoryName,
                                                                                                        guidParameterName,
                                                                                                        methodName),
                                                   this.getClass().getName(),
                                                   methodName);
            }

            return storedEntity.getEntityDetail();
        }

        return null;
    }


    /**
     * Insert the defaultAsOfTime if the caller has passed null.
     *
     * @param suppliedAsOfTime value from caller
     * @return date
     */
    private Date getAsOfTime(Date suppliedAsOfTime)
    {
        if (suppliedAsOfTime == null)
        {
            return defaultAsOfTime;
        }

        return suppliedAsOfTime;
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @return entity object
     * @throws RepositoryErrorException problem forming entity proxy
     */
    EntitySummary  getEntitySummary(String guid) throws RepositoryErrorException
    {
        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        EntityMapper storedEntity = databaseStore.getEntityFromStore(guid, getAsOfTime(null));
        databaseStore.disconnect();

        if (storedEntity != null)
        {
            if (storedEntity.getEntityDetail() != null)
            {
                return storedEntity.getEntityDetail();
            }
            else
            {
                return storedEntity.getEntityProxy();
            }
        }

        return null;
    }


    /**
     * Return the entity proxy identified by the guid.
     *
     * @param guid - unique identifier
     * @return entity proxy object
     * @throws RepositoryErrorException problem forming entity proxy
     */
    EntityProxy  getEntityProxy(String guid, Date asOfTime) throws RepositoryErrorException
    {
        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        EntityMapper storedEntity = databaseStore.getEntityFromStore(guid, getAsOfTime(asOfTime));
        databaseStore.disconnect();

        if (storedEntity != null)
        {
            return storedEntity.getEntityProxy();
        }

        return null;
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<EntityDetail> findEntitiesByPropertyValue(String                entityTypeGUID,
                                                   String                searchCriteria,
                                                   int                   fromEntityElement,
                                                   List<InstanceStatus>  limitResultsByStatus,
                                                   List<String>          limitResultsByClassification,
                                                   Date                  asOfTime,
                                                   String                sequencingProperty,
                                                   SequencingOrder       sequencingOrder,
                                                   int                   pageSize) throws RepositoryErrorException
    {
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        QueryBuilder entityQueryBuilder = new QueryBuilder(RepositoryTable.ENTITY.getTableName(),
                                                           RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                           repositoryHelper,
                                                           repositoryName);

        entityQueryBuilder.setTypeGUID(entityTypeGUID, entityTypeGUIDParameterName);
        entityQueryBuilder.setSearchString(searchCriteria);
        entityQueryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        entityQueryBuilder.setAsOfTime(asOfTime);
        entityQueryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        entityQueryBuilder.setPaging(fromEntityElement, pageSize);

        QueryBuilder classificationQueryBuilder = null;

        if ((limitResultsByClassification != null) && (! limitResultsByClassification.isEmpty()))
        {
            classificationQueryBuilder = new QueryBuilder(RepositoryTable.CLASSIFICATION.getTableName(),
                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                          repositoryHelper,
                                                          repositoryName);

            classificationQueryBuilder.setLimitResultsByClassification(limitResultsByClassification);
            classificationQueryBuilder.setAsOfTime(asOfTime);
        }

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> entityMappers = databaseStore.retrieveEntitiesByProperties(entityQueryBuilder,
                                                                                      classificationQueryBuilder,
                                                                                      asOfTime);
        databaseStore.disconnect();

        return this.getEntitiesFromMappers(entityMappers);
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<EntityDetail> findEntitiesByProperty(String                    entityTypeGUID,
                                              InstanceProperties        matchProperties,
                                              MatchCriteria             matchCriteria,
                                              int                       fromEntityElement,
                                              List<InstanceStatus>      limitResultsByStatus,
                                              List<String>              limitResultsByClassification,
                                              Date                      asOfTime,
                                              String                    sequencingProperty,
                                              SequencingOrder           sequencingOrder,
                                              int                       pageSize) throws RepositoryErrorException
    {
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        QueryBuilder entityQueryBuilder = new QueryBuilder(RepositoryTable.ENTITY.getTableName(),
                                                           RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                           repositoryHelper,
                                                           repositoryName);

        entityQueryBuilder.setTypeGUID(entityTypeGUID, entityTypeGUIDParameterName);
        entityQueryBuilder.setMatchProperties(matchProperties,
                                              matchCriteria);
        entityQueryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        entityQueryBuilder.setAsOfTime(asOfTime);
        entityQueryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        entityQueryBuilder.setPaging(fromEntityElement, pageSize);

        QueryBuilder classificationQueryBuilder = null;

        if ((limitResultsByClassification != null) && (! limitResultsByClassification.isEmpty()))
        {
            classificationQueryBuilder = new QueryBuilder(RepositoryTable.CLASSIFICATION.getTableName(),
                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                          repositoryHelper,
                                                          repositoryName);

            classificationQueryBuilder.setLimitResultsByClassification(limitResultsByClassification);
            classificationQueryBuilder.setAsOfTime(asOfTime);
        }

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> entityMappers = databaseStore.retrieveEntitiesByProperties(entityQueryBuilder,
                                                                                      classificationQueryBuilder,
                                                                                      asOfTime);

        return this.getEntitiesFromMappers(entityMappers);
    }


    /**
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<EntityDetail> findEntities(String                    entityTypeGUID,
                                    List<String>              entitySubtypeGUIDs,
                                    SearchProperties          matchProperties,
                                    int                       fromEntityElement,
                                    List<InstanceStatus>      limitResultsByStatus,
                                    SearchClassifications     matchClassifications,
                                    Date                      asOfTime,
                                    String                    sequencingProperty,
                                    SequencingOrder           sequencingOrder,
                                    int                       pageSize) throws RepositoryErrorException
    {
        final String entityTypeGUIDParameterName = "entityTypeGUID";
        final String entitySubtypeGUIDsParameterName = "entitySubtypeGUIDs";

        QueryBuilder entityQueryBuilder = new QueryBuilder(RepositoryTable.ENTITY.getTableName(),
                                                           RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                           repositoryHelper,
                                                           repositoryName);
        QueryBuilder classificationQueryBuilder = null;

        entityQueryBuilder.setTypeGUID(entityTypeGUID, entityTypeGUIDParameterName, entitySubtypeGUIDs, entitySubtypeGUIDsParameterName);
        entityQueryBuilder.setSearchProperties(matchProperties);
        entityQueryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        entityQueryBuilder.setAsOfTime(asOfTime);
        entityQueryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        entityQueryBuilder.setPaging(fromEntityElement, pageSize);

        if (matchClassifications != null)
        {
            classificationQueryBuilder = new QueryBuilder(RepositoryTable.CLASSIFICATION.getTableName(),
                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                          repositoryHelper,
                                                          repositoryName);

            classificationQueryBuilder.setSearchClassifications(matchClassifications);
            classificationQueryBuilder.setAsOfTime(asOfTime);
        }

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> entityMappers = databaseStore.retrieveEntitiesByProperties(entityQueryBuilder,
                                                                                      classificationQueryBuilder,
                                                                                      asOfTime);

        return this.getEntitiesFromMappers(entityMappers);
    }


    /**
     * Return a list of entities that have the requested type of classifications attached.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<EntityDetail> findEntitiesByClassification(String                    entityTypeGUID,
                                                    String                    classificationName,
                                                    InstanceProperties        matchClassificationProperties,
                                                    MatchCriteria             matchCriteria,
                                                    int                       fromEntityElement,
                                                    List<InstanceStatus>      limitResultsByStatus,
                                                    Date                      asOfTime,
                                                    String                    sequencingProperty,
                                                    SequencingOrder           sequencingOrder,
                                                    int                       pageSize) throws RepositoryErrorException
    {
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        QueryBuilder entityQueryBuilder = new QueryBuilder(RepositoryTable.ENTITY.getTableName(),
                                                           RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                           repositoryHelper,
                                                           repositoryName);
        QueryBuilder classificationQueryBuilder = new QueryBuilder(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                   RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                   repositoryHelper,
                                                                   repositoryName);

        entityQueryBuilder.setTypeGUID(entityTypeGUID, entityTypeGUIDParameterName);
        entityQueryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        entityQueryBuilder.setAsOfTime(asOfTime);
        entityQueryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        entityQueryBuilder.setPaging(fromEntityElement, pageSize);

        classificationQueryBuilder.setLimitResultsByClassification(Collections.singletonList(classificationName));
        classificationQueryBuilder.setMatchProperties(matchClassificationProperties, matchCriteria);
        classificationQueryBuilder.setAsOfTime(asOfTime);

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> entityMappers = databaseStore.retrieveEntitiesByProperties(entityQueryBuilder,
                                                                                      classificationQueryBuilder,
                                                                                      asOfTime);
        databaseStore.disconnect();

        return this.getEntitiesFromMappers(entityMappers);
    }


    /**
     * Convert a list of entity mappers into a list of entity.
     *
     * @param entityMappers mappers containing information retrieved from the database (or not)
     * @return list of entity details or null
     * @throws RepositoryErrorException problem mapping results from the database
     */
    private List<EntityDetail> getEntitiesFromMappers(List<EntityMapper> entityMappers) throws RepositoryErrorException
    {
        if (entityMappers != null)
        {
            List<EntityDetail>  entityDetails = new ArrayList<>();

            for (EntityMapper entityMapper : entityMappers)
            {
                if (entityMapper != null)
                {
                    EntityDetail entityDetail = entityMapper.getEntityDetail();

                    /*
                     * Ignore entity proxies.
                     */
                    if (entityDetail != null)
                    {
                        entityDetails.add(entityDetail);
                    }
                }
            }

            if (! entityDetails.isEmpty())
            {
                return entityDetails;
            }
        }

        return null;
    }


    /**
     * Return the relationship identified by the guid.
     *
     * @param guid - unique identifier for the relationship
     * @return relationship object
     * @throws RepositoryErrorException problem forming entity proxy
     */
    Relationship getRelationship(String guid) throws RepositoryErrorException
    {
        DatabaseStore      databaseStore      = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        RelationshipMapper storedRelationship = databaseStore.getRelationshipFromStore(guid, getAsOfTime(null));
        databaseStore.disconnect();

        if (storedRelationship != null)
        {
            return storedRelationship.getRelationship();
        }

        return null;
    }


    /**
     * Return the relationship identified by the guid.
     *
     * @param guid - unique identifier for the relationship
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return relationship object
     * @throws RepositoryErrorException problem forming entity proxy
     */
    Relationship  getRelationship(String guid,
                                  Date   asOfTime) throws RepositoryErrorException
    {
        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        RelationshipMapper storedRelationship = databaseStore.getRelationshipFromStore(guid, asOfTime);
        databaseStore.disconnect();

        if (storedRelationship != null)
        {
            return storedRelationship.getRelationship();
        }

        return null;
    }


    /**
     * Return the relationships for a specific entity.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    List<Relationship> getRelationshipsForEntity(String                     entityGUID,
                                                 String                     relationshipTypeGUID,
                                                 int                        fromRelationshipElement,
                                                 List<InstanceStatus>       limitResultsByStatus,
                                                 Date                       asOfTime,
                                                 String                     sequencingProperty,
                                                 SequencingOrder            sequencingOrder,
                                                 int                        pageSize) throws RepositoryErrorException
    {
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";

        QueryBuilder queryBuilder = new QueryBuilder(RepositoryTable.RELATIONSHIP.getTableName(),
                                                     RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                     repositoryHelper,
                                                     repositoryName);

        queryBuilder.setTypeGUID(relationshipTypeGUID, relationshipTypeGUIDParameterName);
        queryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        queryBuilder.setAsOfTime(asOfTime);
        queryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        queryBuilder.setPaging(fromRelationshipElement, pageSize);
        queryBuilder.setRelationshipEndGUID(entityGUID);

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> storedRelationships = databaseStore.retrieveRelationships(queryBuilder, getAsOfTime(asOfTime));
        databaseStore.disconnect();

        return getRelationshipsFromMappers(storedRelationships);
    }


    /**
     * Convert a list of relationship mappers into a list of relationships.
     *
     * @param relationshipMappers mappers containing information retrieved from the database (or not)
     * @return list of relationships or null
     * @throws RepositoryErrorException problem mapping results from the database
     */
    private List<Relationship> getRelationshipsFromMappers(List<RelationshipMapper> relationshipMappers) throws RepositoryErrorException
    {
        if (relationshipMappers != null)
        {
            List<Relationship>  relationships = new ArrayList<>();

            for (RelationshipMapper relationshipMapper : relationshipMappers)
            {
                if (relationshipMapper != null)
                {
                    relationships.add(relationshipMapper.getRelationship());
                }
            }

            if (! relationships.isEmpty())
            {
                return relationships;
            }
        }

        return null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<Relationship> findRelationships(String                    relationshipTypeGUID,
                                         List<String>              relationshipSubtypeGUIDs,
                                         SearchProperties          matchProperties,
                                         int                       fromRelationshipElement,
                                         List<InstanceStatus>      limitResultsByStatus,
                                         Date                      asOfTime,
                                         String                    sequencingProperty,
                                         SequencingOrder           sequencingOrder,
                                         int                       pageSize) throws RepositoryErrorException
    {
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";
        final String relationshipSubtypeGUIDsParameterName = "relationshipSubtypeGUIDs";

        QueryBuilder queryBuilder = new QueryBuilder(RepositoryTable.RELATIONSHIP.getTableName(),
                                                     RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                     repositoryHelper,
                                                     repositoryName);

        queryBuilder.setTypeGUID(relationshipTypeGUID, relationshipTypeGUIDParameterName, relationshipSubtypeGUIDs, relationshipSubtypeGUIDsParameterName);
        queryBuilder.setSearchProperties(matchProperties);
        queryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        queryBuilder.setAsOfTime(asOfTime);
        queryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        queryBuilder.setPaging(fromRelationshipElement, pageSize);

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> storedRelationships = databaseStore.retrieveRelationshipsByProperties(queryBuilder, asOfTime);
        databaseStore.disconnect();

        return getRelationshipsFromMappers(storedRelationships);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be received as a series of pages.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    List<Relationship> findRelationshipsByProperty(String                    relationshipTypeGUID,
                                                   InstanceProperties        matchProperties,
                                                   MatchCriteria             matchCriteria,
                                                   int                       fromRelationshipElement,
                                                   List<InstanceStatus>      limitResultsByStatus,
                                                   Date                      asOfTime,
                                                   String                    sequencingProperty,
                                                   SequencingOrder           sequencingOrder,
                                                   int                       pageSize) throws RepositoryErrorException
    {
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";

        QueryBuilder queryBuilder = new QueryBuilder(RepositoryTable.RELATIONSHIP.getTableName(),
                                                     RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                     repositoryHelper,
                                                     repositoryName);

        queryBuilder.setTypeGUID(relationshipTypeGUID, relationshipTypeGUIDParameterName);
        queryBuilder.setMatchProperties(matchProperties,
                                        matchCriteria);
        queryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        queryBuilder.setAsOfTime(asOfTime);
        queryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        queryBuilder.setPaging(fromRelationshipElement, pageSize);

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> storedRelationships = databaseStore.retrieveRelationshipsByProperties(queryBuilder, asOfTime);
        databaseStore.disconnect();

        return getRelationshipsFromMappers(storedRelationships);
    }


    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    List<Relationship> findRelationshipsByPropertyValue(String                    relationshipTypeGUID,
                                                        String                    searchCriteria,
                                                        int                       fromRelationshipElement,
                                                        List<InstanceStatus>      limitResultsByStatus,
                                                        Date                      asOfTime,
                                                        String                    sequencingProperty,
                                                        SequencingOrder           sequencingOrder,
                                                        int                       pageSize) throws RepositoryErrorException
    {
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";

        QueryBuilder queryBuilder = new QueryBuilder(RepositoryTable.RELATIONSHIP.getTableName(),
                                                     RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                     repositoryHelper,
                                                     repositoryName);

        queryBuilder.setTypeGUID(relationshipTypeGUID, relationshipTypeGUIDParameterName);
        queryBuilder.setSearchString(searchCriteria);
        queryBuilder.setLimitResultsByStatus(limitResultsByStatus);
        queryBuilder.setAsOfTime(asOfTime);
        queryBuilder.setSequencingOrder(sequencingOrder, sequencingProperty);
        queryBuilder.setPaging(fromRelationshipElement, pageSize);

        DatabaseStore  databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> storedRelationships = databaseStore.retrieveRelationshipsByProperties(queryBuilder, asOfTime);
        databaseStore.disconnect();

        return getRelationshipsFromMappers(storedRelationships);
    }


    /**
     * Save an entity to the entity store.
     *
     * @param entityDetail - entity object to add
     * @throws RepositoryErrorException unable to create proxy
     */
    EntityDetail addEntityToStore(EntityDetail entityDetail) throws RepositoryErrorException
    {
        final String methodName = "addEntityToStore";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);

        EntityMapper storedEntity = databaseStore.getEntityForUpdate(entityDetail.getGUID());

        if (storedEntity == null)
        {
            databaseStore.addEntityToStore(new EntityMapper(entityDetail, repositoryHelper, repositoryName));
            databaseStore.disconnect();
            return entityDetail;
        }
        else if (entityDetail.getVersion() > storedEntity.getEntityDetail().getVersion())
        {
            databaseStore.updatePreviousEntityVersionEndTime(storedEntity, databaseStore.getVersionEndDate(entityDetail.getUpdateTime()));

            databaseStore.addEntityToStore(new EntityMapper(entityDetail, repositoryHelper, repositoryName));
            databaseStore.disconnect();
            return entityDetail;
        }

        return storedEntity.getEntityDetail();
    }


    /**
     * Save an entity proxy to the entity store.
     *
     * @param entityProxy - entity proxy object to add
     * @throws RepositoryErrorException problem forming entity proxy
     */
    void addEntityProxyToStore(EntityProxy entityProxy) throws RepositoryErrorException
    {
        final String methodName = "addEntityProxyToStore";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        databaseStore.addEntityProxyToStore(new EntityMapper(entityProxy, repositoryHelper, repositoryName));
        databaseStore.disconnect();
    }


    /**
     * Save a relationship to the relationship store.
     *
     * @param relationship - relationship to add
     * @return relationship
     * @throws RepositoryErrorException problem communicating with the database
     */
    Relationship addRelationshipToStore(Relationship relationship) throws RepositoryErrorException
    {
        final String methodName = "addRelationshipToStore";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);

        RelationshipMapper storedRelationship = databaseStore.getRelationshipForUpdate(relationship.getGUID());

        if (storedRelationship == null)
        {
            databaseStore.addRelationshipToStore(new RelationshipMapper(relationship, repositoryHelper, repositoryName));
            databaseStore.disconnect();
            return relationship;
        }
        else if (relationship.getVersion() > storedRelationship.getRelationship().getVersion())
        {
            databaseStore.updatePreviousRelationshipVersionEndTime(storedRelationship, databaseStore.getVersionEndDate(relationship.getUpdateTime()));
            databaseStore.addRelationshipToStore(new RelationshipMapper(relationship, repositoryHelper, repositoryName));
            databaseStore.disconnect();
            return relationship;
        }

        return storedRelationship.getRelationship();
    }


    /**
     * Maintain a classification within the entity.
     *
     * @param entityGUID unique identifier of entity
     * @param classification classification to update
     * @throws RepositoryErrorException problem communicating with the database
     */
    void saveClassification(String          entityGUID,
                            Classification  classification) throws RepositoryErrorException
    {
        final String methodName = "saveClassification";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);

        ClassificationMapper storedClassification = databaseStore.getClassificationForUpdate(entityGUID, classification.getName());

        if (storedClassification == null)
        {
            databaseStore.saveClassification(new ClassificationMapper(entityGUID, classification, repositoryHelper, repositoryName));
        }
        else if (classification.getVersion() > storedClassification.getClassification().getVersion())
        {
            databaseStore.updatePreviousClassificationVersionEndTime(storedClassification, databaseStore.getVersionEndDate(classification.getUpdateTime()));
            databaseStore.saveClassification(new ClassificationMapper(entityGUID, classification, repositoryHelper, repositoryName));
        }

        databaseStore.disconnect();
    }


    /**
     * Retrieve the previous version of a Relationship.  This is the first instance of this element that
     * appears in the history.
     *
     * @param currentRelationship - unique identifier for the required element
     * @return - previous version of this relationship - or null if not found
     * @throws RepositoryErrorException problem communicating with the database
     */
    Relationship retrievePreviousVersionOfRelationship(Relationship currentRelationship) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> storedRelationships = databaseStore.getRelationshipHistoryFromStore(currentRelationship.getGUID(), null, null, true);
        databaseStore.disconnect();

        if ((storedRelationships != null) && (storedRelationships.size() > 1))
        {
            RelationshipMapper relationshipMapper = storedRelationships.get(storedRelationships.size() - 2);

            if (relationshipMapper != null)
            {
                return relationshipMapper.getRelationship();
            }
        }

        return null;
    }


    /**
     * Retrieve the previous version of an Entity from the history store and restore it in the entity store.
     * This is the first instance of this element that appears in the history.
     *
     * @param currentEntity - required element
     * @return - previous version of this Entity - or null if not found
     * @throws RepositoryErrorException problem communicating with the database
     */
    EntityDetail retrievePreviousVersionOfEntity(EntityDetail  currentEntity) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> storedEntities = databaseStore.getEntityHistoryFromStore(currentEntity.getGUID(),
                                                                                    null,
                                                                                    null,
                                                                                    true);
        databaseStore.disconnect();

        if ((storedEntities != null) && (storedEntities.size() > 1))
        {
            EntityMapper entityMapper = storedEntities.get(storedEntities.size() - 2);

            if (entityMapper != null)
            {
                /*
                 * This should be null if only the entity proxy was previously known.
                 */
                try
                {
                    return entityMapper.getEntityDetail();
                }
                catch (RepositoryErrorException onlyAProxy)
                {
                    return null;
                }
            }
        }

        return null;
    }


    /**
     * Return the list of home classifications for an entity.
     *
     * @param guid unique identifier of the entity
     * @return list of classifications or null
     * @throws RepositoryErrorException problem communicating with the database
     */
    List<Classification> getHomeClassifications(String guid) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<ClassificationMapper> classificationMappers = databaseStore.getHomeClassifications(guid, localMetadataCollectionId, getAsOfTime(null));
        databaseStore.disconnect();

        if (classificationMappers != null)
        {
            List<Classification> classifications = new ArrayList<>();

            for (ClassificationMapper classificationMapper : classificationMappers)
            {
                if (classificationMapper != null)
                {
                    classifications.add(classificationMapper.getClassification());
                }
            }

            return classifications;
        }

        return null;
    }


    /**
     * Return the versions of the instance that where active between the "from" and "to" times.
     *
     * @param guid unique identifier of the instance
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     * @throws RepositoryErrorException problem communicating with the database
     */
    List<EntityDetail> getEntityHistory(String  guid,
                                        Date    fromTime,
                                        Date    toTime,
                                        boolean oldestFirst) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<EntityMapper> entityMappers = databaseStore.getEntityHistoryFromStore(guid, fromTime, toTime, oldestFirst);
        databaseStore.disconnect();

        if (entityMappers != null)
        {
            List<EntityDetail> historyResults = new ArrayList<>();

            for (EntityMapper entityMapper : entityMappers)
            {
                if (entityMapper != null)
                {
                    if (! entityMapper.isProxy())
                    {
                        historyResults.add(entityMapper.getEntityDetail());
                    }
                }
            }

            return historyResults;
        }

        return null;
    }


    /**
     * Return the versions of the instance that where active between the "from" and "to" times.
     *
     * @param guid unique identifier of the instance
     * @param classificationName name of the classification within entity
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     * @throws RepositoryErrorException problem communicating with the database
     */
    List<Classification> getClassificationHistory(String  guid,
                                                  String  classificationName,
                                                  Date    fromTime,
                                                  Date    toTime,
                                                  boolean oldestFirst) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<ClassificationMapper> classificationMappers = databaseStore.getClassificationHistoryFromStore(guid, classificationName, fromTime, toTime, oldestFirst);
        databaseStore.disconnect();

        if (classificationMappers != null)
        {
            List<Classification> historyResults = new ArrayList<>();

            for (ClassificationMapper classificationMapper : classificationMappers)
            {
                if (classificationMapper != null)
                {
                    historyResults.add(classificationMapper.getClassification());
                }
            }

            return historyResults;
        }

        return null;
    }


    /**
     * Return the versions of the instance that where active between the "from" and "to" times.
     *
     * @param guid unique identifier of the instance
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     * @throws RepositoryErrorException problem communicating with the database
     */
    List<Relationship> getRelationshipHistory(String  guid,
                                              Date    fromTime,
                                              Date    toTime,
                                              boolean oldestFirst) throws RepositoryErrorException
    {
        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        List<RelationshipMapper> relationshipMappers = databaseStore.getRelationshipHistoryFromStore(guid, fromTime, toTime, oldestFirst);
        databaseStore.disconnect();

        if (relationshipMappers != null)
        {
            List<Relationship> historyResults = new ArrayList<>();

            for (RelationshipMapper relationshipMapper : relationshipMappers)
            {
                if (relationshipMapper != null)
                {
                    historyResults.add(relationshipMapper.getRelationship());
                }
            }

            return historyResults;
        }

        return null;
    }


    /**
     * Remove a classification within the entity.  This is used with reference copies.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of classification to remove
     */
    void removeClassificationFromEntity(String entityGUID,
                                        String classificationName) throws RepositoryErrorException
    {
        final String  methodName = "removeClassificationFromEntity";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        databaseStore.purgeClassification(entityGUID, classificationName);
        databaseStore.disconnect();
    }


    /**
     * Remove all record of an entity - including its history.
     *
     * @param guid - entity to remove
     * @throws RepositoryErrorException problem communicating with the database
     */
    void purgeEntityFromStore(String guid) throws RepositoryErrorException
    {
        final String  methodName = "purgeEntityFromStore";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        databaseStore.purgeEntity(guid);
        databaseStore.disconnect();
    }


    /**
     * Remove a reference relationship from the active store and add it to the history store.
     *
     * @param guid - relationship to remove
     * @throws RepositoryErrorException problem communicating with the database
     */
    void purgeRelationshipFromStore(String guid) throws RepositoryErrorException
    {
        final String  methodName = "purgeRelationshipFromStore";

        if (isReadOnly)
        {
            throw new RepositoryErrorException(PostgresErrorCode.READ_ONLY_MODE.getMessageDefinition(repositoryName), this.getClass().getName(), methodName);
        }

        DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector, repositoryName, repositoryHelper);
        databaseStore.purgeRelationship(guid);
        databaseStore.disconnect();
    }
}
