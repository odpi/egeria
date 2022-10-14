/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.*;

/**
 * InMemoryOMRSMetadataStore provides the in memory stores for the InMemoryRepositoryConnector
 */
class InMemoryOMRSMetadataStore
{
    private String                                 repositoryName           = null;
    private volatile Map<String, EntityDetail>     entityStore              = new HashMap<>();
    private volatile Map<String, EntityProxy>      entityProxyStore         = new HashMap<>();
    private volatile List<EntityDetail>            entityHistoryStore       = new ArrayList<>();
    private volatile Map<String, Relationship>     relationshipStore        = new HashMap<>();
    private volatile List<Relationship>            relationshipHistoryStore = new ArrayList<>();


    /**
     * Default constructor
     */
    InMemoryOMRSMetadataStore()
    {
    }


    /**
     * Set up the name of the repository for logging.
     *
     * @param repositoryName - String name
     */
    protected void  setRepositoryName(String    repositoryName)
    {
        this.repositoryName = repositoryName;
    }


    /**
     * Return a list of entities from the store that are at the latest level.
     *
     * @return list of EntityDetail objects
     */
    synchronized List<EntityDetail>   getEntities()
    {
        return new ArrayList<>(entityStore.values());
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @return entity object
     */
    synchronized EntityDetail  getEntity(String   guid)
    {
        return entityStore.get(guid);
    }


    /**
     * Return the entity proxy identified by the guid.
     *
     * @param guid - unique identifier
     * @return entity proxy object
     */
    synchronized EntityProxy  getEntityProxy(String   guid)
    {
        return entityProxyStore.get(guid);
    }


    /**
     * Return an entity store that contains entities as they were at the time supplied in the asOfTime
     * parameter
     *
     * @param asOfTime - time for the store (or null means now)
     * @return entity store for the requested time
     */
    synchronized Map<String, EntityDetail>  timeWarpEntityStore(Date         asOfTime)
    {
        if (asOfTime == null)
        {
            return new HashMap<>(entityStore);
        }

        Map<String, EntityDetail>  timeWarpedEntityStore = new HashMap<>();

        /*
         * First step through the current relationship store and extract all the relationships that were
         * last updated before the asOfTime.
         */
        for (EntityDetail  entity : entityStore.values())
        {
            if (entity != null)
            {
                if (entity.getUpdateTime() != null)
                {
                    String entityGUID = entity.getGUID();

                    if (entityGUID != null)
                    {
                        if (! entity.getUpdateTime().after(asOfTime))
                        {
                            timeWarpedEntityStore.put(entityGUID, entity);
                        }
                    }
                }
                else if (entity.getCreateTime() != null)
                {
                    if (! entity.getCreateTime().after(asOfTime))
                    {
                        timeWarpedEntityStore.put(entity.getGUID(), entity);
                    }
                }
            }
        }

        /*
         * Now step through the history store picking up the versions of other entities that were active
         * at the time of the asOfTime.
         */
        for (EntityDetail oldEntity : entityHistoryStore)
        {
            if (oldEntity != null)
            {
                String entityGUID = oldEntity.getGUID();

                if (oldEntity.getUpdateTime() != null)
                {
                    if (! oldEntity.getUpdateTime().after(asOfTime))
                    {
                        EntityDetail newerEntity = timeWarpedEntityStore.put(entityGUID, oldEntity);

                        if (newerEntity != null)
                        {
                            timeWarpedEntityStore.put(entityGUID, newerEntity);
                        }
                        break;
                    }
                }
                else if (oldEntity.getCreateTime() != null)
                {
                    if (! oldEntity.getCreateTime().after(asOfTime))
                    {
                        timeWarpedEntityStore.put(entityGUID, oldEntity);
                        break;
                    }
                }
            }
        }

        return timeWarpedEntityStore;
    }


    /**
     * Return the list of relationships at their current level.
     *
     * @return list of relationships
     */
    synchronized List<Relationship>   getRelationships()
    {
        return new ArrayList<>(relationshipStore.values());
    }


    /**
     * Return the relationship identified by the guid.
     *
     * @param guid - unique identifier for the relationship
     * @return relationship object
     */
    protected synchronized Relationship  getRelationship(String   guid)
    {
        return relationshipStore.get(guid);
    }

    /**
     * Return a relationship store that contains relationships as they were at the time supplied in the asOfTime
     * parameter
     *
     * @param asOfTime - time for the store (or null means now)
     * @return relationship store for the requested time
     */
    synchronized Map<String, Relationship>  timeWarpRelationshipStore(Date         asOfTime)
    {
        if (asOfTime == null)
        {
            return new HashMap<>(relationshipStore);
        }

        Map<String, Relationship>  timeWarpedRelationshipStore = new HashMap<>();


        /*
         * First step through the current relationship store and extract all the relationships that were
         * last updated before the asOfTime.
         */
        for (Relationship  relationship : relationshipStore.values())
        {
            if (relationship != null)
            {
                if (relationship.getUpdateTime() != null)
                {
                    String relationshipGUID = relationship.getGUID();

                    if (relationshipGUID != null)
                    {
                        if (! relationship.getUpdateTime().after(asOfTime))
                        {
                            timeWarpedRelationshipStore.put(relationshipGUID, relationship);
                        }
                    }
                }
                else if (relationship.getCreateTime() != null)
                {
                    if (! relationship.getCreateTime().after(asOfTime))
                    {
                        timeWarpedRelationshipStore.put(relationship.getGUID(), relationship);
                    }
                }
            }
        }

        /*
         * Now step through the history store picking up the versions of other relationships that were active
         * at the time of the asOfTime.
         */
        for (Relationship oldRelationship : relationshipHistoryStore)
        {
            if (oldRelationship != null)
            {
                String relationshipGUID = oldRelationship.getGUID();

                if (oldRelationship.getUpdateTime() != null)
                {
                    if (! oldRelationship.getUpdateTime().after(asOfTime))
                    {
                        Relationship newerRelationship = timeWarpedRelationshipStore.put(relationshipGUID, oldRelationship);

                        if (newerRelationship != null)
                        {
                            timeWarpedRelationshipStore.put(relationshipGUID, newerRelationship);
                        }
                        break;
                    }
                }
                else if (oldRelationship.getCreateTime() != null)
                {
                    if (! oldRelationship.getCreateTime().after(asOfTime))
                    {
                        timeWarpedRelationshipStore.put(relationshipGUID, oldRelationship);
                        break;
                    }
                }
            }
        }

        return timeWarpedRelationshipStore;
    }

    /**
     * Create a new entity in the entity store.
     *
     * @param entity - new version of the entity
     * @return entity with potentially updated GUID
     */
    synchronized EntityDetail createEntityInStore(EntityDetail    entity)
    {
        /*
         * There is a small chance the randomly generated GUID will clash with an existing relationship.
         * If this happens a new GUID is generated for the relationship and the process repeats.
         */
        EntityDetail existingEntity = entityStore.put(entity.getGUID(), entity);

        while (existingEntity != null)
        {
            entityStore.put(entity.getGUID(), existingEntity);
            entity.setGUID(UUID.randomUUID().toString());
            existingEntity = entityStore.put(entity.getGUID(), entity);
        }

        return entity;
    }


    /**
     * Create a new relationship in the relationship store.
     *
     * @param relationship - new version of the relationship
     * @return relationship with potentially updated GUID
     */
    synchronized Relationship createRelationshipInStore(Relationship    relationship)
    {
        /*
         * There is a small chance the randomly generated GUID will clash with an existing relationship.
         * If this happens a new GUID is generated for the relationship and the process repeats.
         */
        Relationship existingRelationship = relationshipStore.put(relationship.getGUID(), relationship);

        while (existingRelationship != null)
        {
            relationshipStore.put(relationship.getGUID(), existingRelationship);
            relationship.setGUID(UUID.randomUUID().toString());
            existingRelationship = relationshipStore.put(relationship.getGUID(), relationship);
        }

        return relationship;
    }


    /**
     * Save an entity proxy to the entity store.
     *
     * @param entityProxy - entity proxy object to add
     */
    synchronized void addEntityProxyToStore(EntityProxy    entityProxy)
    {
        entityProxyStore.put(entityProxy.getGUID(), entityProxy);
    }


    /**
     * Maintain a history of entities as they are stored into the entity store to ensure old version can be restored.
     * The history is maintained with the latest changes first in the list.
     *
     * @param entity - new version of the entity
     */
    synchronized void updateEntityInStore(EntityDetail entity)
    {
        EntityDetail oldEntity = entityStore.put(entity.getGUID(), entity);

        if (oldEntity != null)
        {
            entityHistoryStore.add(0, oldEntity);
        }
    }


    /**
     * Update an entity proxy in the proxy store.
     *
     * @param entityProxy - entity proxy object to add
     */
    synchronized void updateEntityProxyInStore(EntityProxy entityProxy)
    {
        entityProxyStore.put(entityProxy.getGUID(), entityProxy);
    }


    /**
     * Maintain a history of relationships as they are stored into the relationship store to ensure old version
     * can be restored.  The history is maintained with the latest changes first in the list.
     *
     * @param relationship - new version of the relationship
     */
    synchronized void updateRelationshipInStore(Relationship    relationship)
    {
        Relationship    oldRelationship = relationshipStore.put(relationship.getGUID(), relationship);

        if (oldRelationship != null)
        {
            relationshipHistoryStore.add(0, oldRelationship);
        }
    }


    /**
     * Save a reference copy of an entity to the active store.  Reference copies are not maintained in the
     * history store.
     *
     * @param entity - object to save
     */
    synchronized void saveReferenceEntityToStore(EntityDetail    entity)
    {
        entityStore.put(entity.getGUID(), entity);
    }


    /**
     * Save a reference copy of a relationship to the active store.  Reference copies are not maintained in the
     * history store.
     *
     * @param relationship - object to save
     */
    synchronized void saveReferenceRelationshipToStore(Relationship    relationship)
    {
        relationshipStore.put(relationship.getGUID(), relationship);
    }


    /**
     * Retrieve the previous version of a Relationship.  This is the first instance of this element that
     * appears in the history.
     *
     * @param guid - unique identifier for the required element
     * @return - previous version of this relationship - or null if not found
     */
    synchronized Relationship retrievePreviousVersionOfRelationship(String   guid)
    {
        if (guid != null)
        {
            Relationship  currentVersionOfRelationship = relationshipStore.get(guid);

            long versionNumber = 0;

            if (currentVersionOfRelationship != null)
            {
                versionNumber = currentVersionOfRelationship.getVersion() + 1;
            }


            for (Relationship relationship : relationshipHistoryStore)
            {
                if (relationship != null)
                {
                    if (guid.equals(relationship.getGUID()))
                    {
                        if (versionNumber == 0)
                        {
                            versionNumber = relationship.getVersion() + 1;
                        }
                        /*
                         * Clone the head (most recent) version in the history, set its version number to the next version
                         * and insert the new clone into the current store (under key GUID). Also, take the 'current version'
                         * (as was at start of method) and shunt that into the history. Do not remove anything from the history.
                         * Remember also to set the updateTime to NOW - otherwise the historical copy will appear to have been
                         * updated longer ago than was really the case.
                         */
                        Relationship newRelationship = new Relationship(relationship);
                        newRelationship.setVersion(versionNumber);
                        Date restoreTime = new Date();
                        newRelationship.setUpdateTime(restoreTime);
                        relationshipStore.put(guid, newRelationship);
                        relationshipHistoryStore.add(0, currentVersionOfRelationship);
                        return newRelationship;

                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the previous version of an Entity from the history store and restore it in the entity store.
     * This is the first instance of this element that appears in the history.
     *
     * @param guid - unique identifier for the required element
     * @return - previous version of this Entity - or null if not found
     */
    synchronized EntityDetail retrievePreviousVersionOfEntity(String   guid)
    {
        if (guid != null)
        {
            EntityDetail  currentVersionOfEntity = entityStore.get(guid);

            long versionNumber = 0;

            if (currentVersionOfEntity != null)
            {
                versionNumber = currentVersionOfEntity.getVersion() + 1;
            }

            for (EntityDetail entity : entityHistoryStore)
            {
                if (entity != null)
                {
                    if (guid.equals(entity.getGUID()))
                    {
                        if (versionNumber == 0)
                        {
                            versionNumber = entity.getVersion() + 1;
                        }

                        /*
                         * Clone the head (most recent) version in the history, set its version number to the next version
                         * and insert the new clone into the current store (under key GUID). Also, take the 'current version'
                         * (as was at start of method) and shunt that into the history. Do not remove anything from the history.
                         * Remember also to set the updateTime to NOW - otherwise the historical copy will appear to have been
                         * updated longer ago than was really the case.
                         *
                         */
                        EntityDetail newEntity = new EntityDetail(entity);
                        newEntity.setVersion(versionNumber);
                        Date restoreTime = new Date();
                        newEntity.setUpdateTime(restoreTime);
                        entityStore.put(guid, newEntity);
                        entityHistoryStore.add(0, currentVersionOfEntity);
                        return newEntity;

                    }
                }
            }
        }

        return null;
    }


    /**
     * Remove an entity from the active store and add it to the history store.
     *
     * @param entity - entity to remove
     */
    synchronized void removeEntityFromStore(EntityDetail     entity)
    {
        String entityGUID = entity.getGUID();
        entityStore.remove(entityGUID);
        List<EntityDetail> purgedHistory = new ArrayList<>();
        for (EntityDetail history : entityHistoryStore)
        {
            if (history != null && !entityGUID.equals(history.getGUID()))
            {
                purgedHistory.add(history);
            }
        }
        entityHistoryStore = purgedHistory;
    }


    /**
     * Remove a reference entity from the active store and add it to the history store.
     *
     * @param guid - entity to remove
     */
    synchronized void removeReferenceEntityFromStore(String     guid)
    {
        EntityDetail entity = entityStore.remove(guid);

        if (entity != null)
        {
            List<EntityDetail> purgedHistory = new ArrayList<>();
            for (EntityDetail history : entityHistoryStore)
            {
                if (history != null && !guid.equals(history.getGUID()))
                {
                    purgedHistory.add(history);
                }
            }
            entityHistoryStore = purgedHistory;
        }
    }


    /**
     * Remove an entity from the active store and add it to the history store.
     *
     * @param guid - entity proxy to remove
     */
    synchronized void removeEntityProxyFromStore(String     guid)
    {
        entityProxyStore.remove(guid);
    }


    /**
     * Remove a relationship from the active store and add it to the history store.
     *
     * @param relationship - relationship to remove
     */
    synchronized void removeRelationshipFromStore(Relationship     relationship)
    {
        String relationshipGUID = relationship.getGUID();
        relationshipStore.remove(relationshipGUID);
        List<Relationship> purgedHistory = new ArrayList<>();
        for (Relationship history : relationshipHistoryStore)
        {
            if (history != null && !relationshipGUID.equals(history.getGUID()))
            {
                purgedHistory.add(history);
            }
        }
        relationshipHistoryStore = purgedHistory;
    }


    /**
     * Remove a reference relationship from the active store and add it to the history store.
     *
     * @param guid - relationship to remove
     */
    synchronized void removeReferenceRelationshipFromStore(String     guid)
    {
        Relationship  relationship = relationshipStore.remove(guid);

        if (relationship != null)
        {
            List<Relationship> purgedHistory = new ArrayList<>();
            for (Relationship history : relationshipHistoryStore)
            {
                if (history != null && !guid.equals(history.getGUID()))
                {
                    purgedHistory.add(history);
                }
            }
            relationshipHistoryStore = purgedHistory;
        }
    }

}
