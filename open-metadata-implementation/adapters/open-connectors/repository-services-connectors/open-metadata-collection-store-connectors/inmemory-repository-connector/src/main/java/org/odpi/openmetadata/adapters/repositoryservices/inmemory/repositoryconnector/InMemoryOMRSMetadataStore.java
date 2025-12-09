/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * InMemoryOMRSMetadataStore provides the in memory store for the InMemoryRepositoryConnector.
 */
class InMemoryOMRSMetadataStore
{
    private final String               repositoryName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final String               localMetadataCollectionId;

    private volatile Map<String, StoredEntity>       entityStore       = new HashMap<>();
    private volatile Map<String, StoredRelationship> relationshipStore = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(InMemoryOMRSMetadataStore.class);


    /**
     * Determine which classifications were active between the from and to date.
     *
     * @param fromTime starting time
     * @param toTime ending time
     * @param classifications instance version
     * @param versionEndTime time when this version was superseded
     * @return boolean flag - true means it is valid
     */
    static synchronized List<Classification> getClassificationsForInclusiveDate(String               classificationName,
                                                                                Date                 fromTime,
                                                                                Date                 toTime,
                                                                                List<Classification> classifications,
                                                                                Date                 versionEndTime)
    {
        if ((classifications != null) && (! classifications.isEmpty()))
        {
            List<Classification> results = new ArrayList<>();

            for (Classification classification : classifications)
            {
                if ((classification != null) && (classification.getName().equals(classificationName)))
                {
                    if (checkInclusiveDate(fromTime,
                                           toTime,
                                           classification,
                                           versionEndTime))
                    {
                        results.add(classification);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }



    /**
     * Determine is an instance was active between the from and to date.
     *
     * @param fromTime starting time
     * @param toTime ending time
     * @param instanceHeader instance version
     * @param versionEndTime time when this version was superseded
     * @return boolean flag - true means it is valid
     */
    static synchronized boolean checkInclusiveDate(Date                fromTime,
                                                   Date                toTime,
                                                   InstanceAuditHeader instanceHeader,
                                                   Date                versionEndTime)
    {
        Date versionStartTime = instanceHeader.getUpdateTime();

        if (versionStartTime == null)
        {
            versionStartTime = instanceHeader.getCreateTime();
        }

        if ((toTime != null) && (toTime.before(versionStartTime)))
        {
            return false;
        }

        if (versionEndTime == null)
        {
            return true;
        }

        if (fromTime == null)
        {
            return true;
        }

        return ! fromTime.after(versionEndTime);
    }


    /**
     * Constructor to initialize store.
     *
     * @param repositoryName name of this repository
     * @param repositoryHelper helper
     * @param localMetadataCollectionId identifier for this store's metadata collection
     */
    InMemoryOMRSMetadataStore(String               repositoryName,
                              OMRSRepositoryHelper repositoryHelper,
                              String               localMetadataCollectionId)
    {
        this.repositoryName = repositoryName;
        this.repositoryHelper = repositoryHelper;
        this.localMetadataCollectionId = localMetadataCollectionId;
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @return entity object
     */
    synchronized EntityDetail  getEntity(String guid)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            return storedEntity.getEntity();
        }

        return null;
    }


    /**
     * Return the entity identified by the guid.
     *
     * @param guid - unique identifier for the entity
     * @return entity object
     */
    synchronized EntitySummary  getEntitySummary(String guid)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            if (storedEntity.getEntity() != null)
            {
                return storedEntity.getEntity();
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
     */
    synchronized EntityProxy  getEntityProxy(String guid)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            return storedEntity.getEntityProxy();
        }

        return null;
    }


    /**
     * Return an entity store that contains entities as they were at the time supplied in the asOfTime
     * parameter
     *
     * @param asOfTime - time for the store (or null means now)
     * @return entity store for the requested time
     */
    synchronized Map<String, EntityDetail>  timeWarpEntityStore(Date asOfTime)
    {
        Map<String, EntityDetail>  timeWarpedEntityStore = new HashMap<>();

        for (StoredEntity storedEntity : entityStore.values())
        {
            EntityDetail entityDetail = storedEntity.getEntity(asOfTime);

            if (entityDetail != null)
            {
                timeWarpedEntityStore.put(entityDetail.getGUID(), entityDetail);
            }
        }

        return timeWarpedEntityStore;
    }


    /**
     * Return the relationship identified by the guid.
     *
     * @param guid - unique identifier for the relationship
     * @return relationship object
     */
    synchronized Relationship  getRelationship(String guid)
    {
        StoredRelationship storedRelationship = relationshipStore.get(guid);

        if (storedRelationship != null)
        {
            return storedRelationship.getRelationship();
        }

        return null;
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
        Map<String, Relationship>  timeWarpedRelationshipStore = new HashMap<>();

        for (StoredRelationship storedRelationship : relationshipStore.values())
        {
            Relationship relationship = storedRelationship.getRelationship(asOfTime);

            if (relationship != null)
            {
                timeWarpedRelationshipStore.put(relationship.getGUID(), relationship);
            }
        }

        return timeWarpedRelationshipStore;
    }


    /**
     * Create a new entity in the entity store.
     *
     * @param entity - new version of the entity
     * @return entity with potentially updated GUID
     * @throws RepositoryErrorException problem generating entity proxy - probably bad entity
     */
    synchronized EntityDetail createEntityInStore(EntityDetail entity) throws RepositoryErrorException
    {
        entity.setGUID(generateGUID(entity.getType().getTypeDefName(), entity.getGUID()));

        StoredEntity newStoredEntity = new StoredEntity(entity);

        /*
         * There is a small chance the randomly generated GUID will clash with an existing entity.
         * If this happens a new GUID is generated for the entity and the process repeats.
         */
        StoredEntity existingStoredEntity = entityStore.put(entity.getGUID(), newStoredEntity);

        while (existingStoredEntity != null)
        {
            entityStore.put(entity.getGUID(), existingStoredEntity);
            entity.setGUID(generateGUID(entity.getType().getTypeDefName(), UUID.randomUUID().toString()));
            newStoredEntity = new StoredEntity(entity);
            existingStoredEntity = entityStore.put(entity.getGUID(), newStoredEntity);
        }

        return entity;
    }


    /**
     * Create a GUID that includes the type name.  This is to help with debugging.
     *
     * @param typeName typeName of element.
     * @param guid random UUID.
     * @return composite GUID
     */
    private String generateGUID(String typeName,
                                String guid)
    {
        return typeName + "-" + guid;
    }


    /**
     * Create a new relationship in the relationship store.
     *
     * @param relationship - new version of the relationship
     * @return relationship with potentially updated GUID
     */
    synchronized Relationship createRelationshipInStore(Relationship relationship)
    {
        relationship.setGUID(generateGUID(relationship.getType().getTypeDefName(), relationship.getGUID()));

        StoredRelationship newStoredRelationship = new StoredRelationship(relationship);

        /*
         * There is a small chance the randomly generated GUID will clash with an existing relationship.
         * If this happens a new GUID is generated for the relationship and the process repeats.
         */
        StoredRelationship existingStoredRelationship = relationshipStore.put(relationship.getGUID(), newStoredRelationship);

        while (existingStoredRelationship != null)
        {
            relationshipStore.put(relationship.getGUID(), existingStoredRelationship);
            relationship.setGUID(generateGUID(relationship.getType().getTypeDefName(), UUID.randomUUID().toString()));
            newStoredRelationship = new StoredRelationship(relationship);
            existingStoredRelationship = relationshipStore.put(relationship.getGUID(), newStoredRelationship);
        }

        return relationship;
    }


    /**
     * Save an entity to the entity store.
     *
     * @param entityDetail - entity object to add
     * @throws RepositoryErrorException unable to create proxy
     */
    synchronized void addEntityToStore(EntityDetail entityDetail) throws RepositoryErrorException
    {
        StoredEntity storedEntity = entityStore.get(entityDetail.getGUID());

        if (storedEntity == null)
        {
            entityStore.put(entityDetail.getGUID(), new StoredEntity(entityDetail));
        }
        else
        {
            storedEntity.saveEntity(entityDetail);
        }
    }



    /**
     * Save an entity proxy to the entity store.
     *
     * @param entityProxy - entity proxy object to add
     */
    synchronized void addEntityProxyToStore(EntityProxy entityProxy)
    {
        StoredEntity storedEntity = entityStore.get(entityProxy.getGUID());

        if (storedEntity == null)
        {
            entityStore.put(entityProxy.getGUID(), new StoredEntity(entityProxy));
        }
        else
        {
            storedEntity.saveEntityProxy(entityProxy);
        }
    }


    /**
     * Save an entity proxy to the entity store.
     *
     * @param relationship - entity proxy object to add
     */
    synchronized void addRelationshipToStore(Relationship relationship)
    {
        StoredRelationship storedRelationship = relationshipStore.get(relationship.getGUID());

        if (storedRelationship == null)
        {
            relationshipStore.put(relationship.getGUID(), new StoredRelationship(relationship));
        }
        else
        {
            storedRelationship.saveRelationship(relationship);
        }
    }


    /**
     * Maintain a history of entities as they are stored into the entity store to ensure old version can be restored.
     * The history is maintained with the latest changes first in the list.
     *
     * @param entity - new version of the entity
     * @throws RepositoryErrorException problem generating entity proxy - probably bad entity
     */
    synchronized void updateEntityInStore(EntityDetail entity) throws RepositoryErrorException
    {
        StoredEntity storedEntity = entityStore.get(entity.getGUID());

        if (storedEntity == null)
        {
            entityStore.put(entity.getGUID(), new StoredEntity(entity));
        }
        else
        {
            storedEntity.saveEntity(entity);
        }
    }


    /**
     * Maintain a classification within the entity proxy.
     *
     * @param entityGUID unique identifier of entity
     * @param classification classification to update
     */
    synchronized void saveClassification(String          entityGUID,
                                         Classification  classification)
    {
        StoredEntity storedEntity = entityStore.get(entityGUID);

        if (storedEntity != null)
        {
           storedEntity.saveClassification(classification);
        }
    }


    /**
     * Maintain a classification within the entity.
     *
     * @param entityDetail entity
     * @param classificationName name of classification to remove
     */
    synchronized EntityDetail removeClassificationFromEntity(EntityDetail entityDetail,
                                                             String       classificationName)
    {
        StoredEntity storedEntity = entityStore.get(entityDetail.getGUID());

        if (storedEntity != null)
        {
            return storedEntity.removeClassificationFromEntity(classificationName);
        }

        return null;
    }


    /**
     * Maintain a classification within the entity proxy.
     *
     * @param entityProxy entity
     * @param classificationName name of classification to remove
     */
    synchronized Classification removeClassificationFromProxy(EntityProxy entityProxy,
                                                              String      classificationName)
    {
        StoredEntity storedEntity = entityStore.get(entityProxy.getGUID());

        if (storedEntity != null)
        {
            return storedEntity.removeClassificationFromEntityProxy(classificationName);
        }

        return null;
    }


    /**
     * Maintain a history of relationships as they are stored into the relationship store to ensure old version
     * can be restored.  The history is maintained with the latest changes first in the list.
     *
     * @param relationship - new version of the relationship
     */
    synchronized void updateRelationshipInStore(Relationship relationship)
    {
        StoredRelationship storedRelationship = relationshipStore.get(relationship.getGUID());

        if (storedRelationship == null)
        {
            relationshipStore.put(relationship.getGUID(), new StoredRelationship(relationship));
        }
        else
        {
            storedRelationship.saveRelationship(relationship);
        }
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
        StoredRelationship storedRelationship = relationshipStore.get(guid);

        if (storedRelationship != null)
        {
            return storedRelationship.retrievePreviousVersion();
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
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            return storedEntity.retrievePreviousVersion();
        }

        return null;
    }


    /**
     * Return the list of home classifications for an entity.
     *
     * @param guid unique identifier of the entity
     * @return list of classifications or null
     */
    synchronized List<Classification> getHomeClassifications(String guid)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            return storedEntity.getHomeClassifications();
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
     */
    synchronized List<EntityDetail> getEntityHistory(String  guid,
                                                     Date    fromTime,
                                                     Date    toTime,
                                                     boolean oldestFirst)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity == null)
        {
            return null;
        }

        return storedEntity.getEntityHistory(fromTime, toTime, oldestFirst);
    }


    /**
     * Return the versions of the instance that where active between the "from" and "to" times.
     *
     * @param guid unique identifier of the instance
     * @param classificationName name of the classification history to retrieve
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     */
    synchronized List<Classification> getClassificationHistory(String  guid,
                                                               String  classificationName,
                                                               Date    fromTime,
                                                               Date    toTime,
                                                               boolean oldestFirst)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity == null)
        {
            return null;
        }

        return storedEntity.getClassificationHistory(classificationName, fromTime, toTime, oldestFirst);
    }


    /**
     * Return the versions of the instance that where active between the "from" and "to" times.
     *
     * @param guid unique identifier of the instance
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     */
    synchronized  List<Relationship> getRelationshipHistory(String  guid,
                                                            Date    fromTime,
                                                            Date    toTime,
                                                            boolean oldestFirst)
    {
        StoredRelationship storedRelationship = relationshipStore.get(guid);

        if (storedRelationship == null)
        {
            return null;
        }

        return storedRelationship.getRelationshipHistory(fromTime, toTime, oldestFirst);
    }


    /**
     * Remove all record of an entity - including its history.
     *
     * @param guid - entity to remove
     */
    synchronized void purgeEntityFromStore(String guid)
    {
        StoredEntity storedEntity = entityStore.get(guid);

        if (storedEntity != null)
        {
            entityStore.remove(guid);
        }
    }


    /**
     * Remove a relationship from the active store and add it to the history store.
     * This occurs when an entity is deleted.
     *
     * @param relationship - relationship to remove
     */
    synchronized void removeRelationshipFromStore(Relationship     relationship)
    {
        StoredRelationship storedRelationship = relationshipStore.get(relationship.getGUID());

        if (storedRelationship == null)
        {
            storedRelationship = new StoredRelationship(relationship);

            relationshipStore.put(relationship.getGUID(), storedRelationship);
        }

        storedRelationship.purgeRelationship();
    }


    /**
     * Remove a reference relationship from the active store and add it to the history store.
     *
     * @param guid - relationship to remove
     */
    synchronized void purgeRelationshipFromStore(String guid)
    {
        StoredRelationship storedRelationship = relationshipStore.get(guid);

        if (storedRelationship != null)
        {
            relationshipStore.remove(guid);
        }
    }


    /**
     * Provides storage for an entity, its proxy and classifications.  It is proactively keeping the stored entity
     * and entity proxy up-to-date with the latest known classifications.
     */
    private class StoredEntity
    {
        private final Map<String, HomeClassification> homeClassifications = new HashMap<>();
        private final List<EntityDetail>              entityHistory       = new ArrayList<>();

        private EntityDetail entity = null;
        private EntityProxy  entityProxy = null;

        /**
         * Constructor for when the first element stored is an entity
         *
         * @param entity first version of the entity
         */
        StoredEntity(EntityDetail entity) throws RepositoryErrorException
        {
            saveEntity(entity);
        }


        /**
         * Constructor for when the first element stored is an entity proxy.
         *
         * @param entityProxy first version of the entity proxy
         */
        StoredEntity(EntityProxy entityProxy)
        {
            saveEntityProxy(entityProxy);
        }


        /**
         * Retrieve and save any classifications that belong to the local metadata collection.
         *
         * @param entitySummary header of either an entity or an entity proxy
         */
        private synchronized void saveHomeClassifications(EntitySummary entitySummary)
        {
            List<Classification>  entityClassifications = entitySummary.getClassifications();

            if (entityClassifications != null)
            {
                for (Classification classification : entityClassifications)
                {
                    saveHomeClassification(classification);
                }
            }
        }


        /**
         * If the classification is part of the home metadata collection, save it to home metadata collections.
         *
         * @param classification potential classification to save
         */
        private synchronized void saveHomeClassification(Classification classification)
        {
            if (classification != null)
            {
                if ((classification.getMetadataCollectionId() == null) || (classification.getMetadataCollectionId().equals(localMetadataCollectionId)))
                {
                    HomeClassification existingHomeClassification = homeClassifications.get(classification.getName());

                    if (existingHomeClassification != null)
                    {
                        existingHomeClassification.saveClassification(classification);
                    }
                    else
                    {
                        HomeClassification homeClassification = new HomeClassification(classification);

                        homeClassifications.put(classification.getName(), homeClassification);
                    }
                }
            }
        }


        /**
         * Ensure any home classifications are added to the entity/entity proxy.
         *
         * @param entitySummary entity/entity proxy
         */
        private synchronized void addHomeClassifications(EntitySummary entitySummary)
        {
            if (! homeClassifications.isEmpty())
            {
                if (entitySummary.getClassifications() == null)
                {
                    entitySummary.setClassifications(getHomeClassifications());
                }
                else
                {
                    /*
                     * Need to merge the two classification lists
                     */
                    Map<String, Classification> mergedList = new HashMap<>();

                    for (String homeClassificationName : homeClassifications.keySet())
                    {
                        Classification classification = homeClassifications.get(homeClassificationName).getHomeClassification();

                        if (classification != null)
                        {
                            mergedList.put(homeClassificationName, classification);
                        }
                    }

                    for (Classification entityClassification : entitySummary.getClassifications())
                    {
                        Classification existingClassification = mergedList.put(entityClassification.getName(), entityClassification);

                        if ((existingClassification != null) &&
                                    (existingClassification.getVersion() > entityClassification.getVersion()))
                        {
                            mergedList.put(entityClassification.getName(), existingClassification);
                        }
                    }

                    entitySummary.setClassifications(new ArrayList<>(mergedList.values()));
                }
            }
        }


        /**
         * Return the home classifications for this instance.
         *
         * @return list of classifications or null
         */
        synchronized List<Classification> getHomeClassifications()
        {
            if (! homeClassifications.isEmpty())
            {
                List<Classification> results = new ArrayList<>();

                for (String homeClassificationName : homeClassifications.keySet())
                {
                    Classification homeClassification = homeClassifications.get(homeClassificationName).getHomeClassification();
                    if (homeClassification != null)
                    {
                        results.add(homeClassification);
                    }
                }

                return results;
            }

            return null;
        }


        /**
         * Remove the classification from the entity
         *
         * @param classificationName classification to remove
         */
        synchronized EntityDetail removeClassificationFromEntity(String classificationName)
        {
            final String methodName = "removeClassificationFromEntity";

            HomeClassification homeClassification = homeClassifications.get(classificationName);

            if (homeClassification != null)
            {
                homeClassification.deleteClassification(null);
            }

            try
            {
                EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                                                                                             entity,
                                                                                             classificationName,
                                                                                             methodName);

                this.saveEntity(updatedEntity);
            }
            catch (Exception error)
            {
                // No action required
                log.info(error.toString());
            }

            return this.entity;
        }


        /**
         * Remove the classification from the entity
         *
         * @param classificationName classification to remove
         */
        synchronized Classification removeClassificationFromEntityProxy(String classificationName)
        {
            final String methodName = "removeClassificationFromEntityProxy";

            Classification removedClassification = null;

            HomeClassification homeClassification = homeClassifications.get(classificationName);

            if (homeClassification != null)
            {
                removedClassification = homeClassification.getHomeClassification();
            }

            if ((removedClassification == null) && (this.entity != null))
            {
                try
                {
                    removedClassification = repositoryHelper.getClassificationFromEntity(repositoryName, this.entity, classificationName, methodName);
                }
                catch (Exception error)
                {
                    // No action required
                    log.info(error.toString());
                }
            }

            if ((removedClassification == null) && (this.entityProxy != null))
            {
                try
                {
                    removedClassification = repositoryHelper.getClassificationFromEntity(repositoryName, this.entityProxy, classificationName, methodName);
                }
                catch (Exception error)
                {
                    // No action required
                    log.info(error.toString());
                }
            }

            if (homeClassification != null)
            {
                homeClassification.deleteClassification(removedClassification);
            }

            if (this.entity != null)
            {
                try
                {
                    EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                                                                                                this.entity,
                                                                                                classificationName,
                                                                                                methodName);
                    this.saveEntity(updatedEntity);
                }
                catch (Exception error)
                {
                    // No action required
                    log.info(error.toString());
                }
            }

            if (this.entityProxy != null)
            {
                try
                {
                    EntityProxy updatedProxy = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                                                                                               this.entityProxy,
                                                                                               classificationName,
                                                                                               methodName);
                    this.saveEntityProxy(updatedProxy);
                }
                catch (Exception error)
                {
                    // No action required
                    log.info(error.toString());
                }
            }

            return removedClassification;
        }


        /**
         * Return the entity identified by the guid.
         *
         * @param entity entity object
         * @throws RepositoryErrorException problem forming entity proxy
         */
        synchronized void saveEntity(EntityDetail entity) throws RepositoryErrorException
        {
            saveHomeClassifications(entity);

            /*
             * The test of the version is >= to ensure updates to classifications (that do not change the entity version) are stored.
             * The history contains the intermediate versions of the entity caused by classification changes.
             */
            if ((this.entity == null) || (entity.getVersion() >= this.entity.getVersion()))
            {
                if (this.entity != null)
                {
                    entityHistory.add(0, this.entity);
                }

                this.entity = new EntityDetail(entity);
            }

            addHomeClassifications(this.entity);

            this.entityProxy = repositoryHelper.getNewEntityProxy(repositoryName, this.entity);
        }


        /**
         * Save an entity proxy - this may come from a relationship - or from a classification
         *
         * @param entityProxy entity proxy
         */
        synchronized void saveEntityProxy(EntityProxy entityProxy)
        {
            /*
             * The proxy is saved if it is not older than the stored proxy.  Note the entity proxy may (temporarily)
             * be a later version than the entity.  However, the entity should catch up through replication
             * within the cohort (as long as events are flowing).
             */
            saveHomeClassifications(entityProxy);

            if ((this.entityProxy == null) || (entityProxy.getVersion() >= this.entityProxy.getVersion()))
            {
                this.entityProxy = new EntityProxy(entityProxy);
            }

            addHomeClassifications(this.entityProxy);

            if (this.entity != null)
            {
                addHomeClassifications(this.entity);
            }
        }


        /**
         * Save a classification to the entities (and HomeClassifications if needed).
         *
         * @param classification classification to save
         */
        synchronized void saveClassification(Classification classification)
        {
            final String methodName = "saveClassification";

            if (classification != null)
            {
                saveHomeClassification(classification);

                if (this.entity != null)
                {
                    repositoryHelper.addClassificationToEntity(repositoryName, this.entity, new Classification(classification), methodName);
                }

                if (this.entityProxy != null)
                {
                    repositoryHelper.addClassificationToEntity(repositoryName, this.entityProxy, new Classification(classification), methodName);
                }
            }
        }


        /**
         * Return the entity identified by the guid.
         *
         * @return entity object
         */
        synchronized EntityDetail  getEntity()
        {
            return entity;
        }


        /**
         * Return the entity proxy.
         *
         * @return entity proxy object
         */
        synchronized EntityProxy  getEntityProxy()
        {
            return entityProxy;
        }


        /**
         * Retrieve the version that was active in the repository at a particular time.
         *
         * @param asOfTime time to use on the query
         * @return selected instance
         */
        synchronized EntityDetail getEntity(Date asOfTime)
        {
            if (asOfTime == null)
            {
                return getEntity();
            }

            if (this.entity != null)
            {
                /*
                 * The requested time is before the element was created.
                 */
                if (asOfTime.before(entity.getCreateTime()))
                {
                    return null;
                }

                /*
                 * The element has never been updated so the initial version is still valid.
                 */
                if (entity.getUpdateTime() == null)
                {
                    return entity;
                }

                if ((asOfTime.equals(entity.getUpdateTime())) || (asOfTime.after(entity.getUpdateTime())))
                {
                    /*
                     * The asOfTime is within the window of when this instance is valid.
                     */
                    return entity;
                }
            }

            for (EntityDetail historicalEntity : entityHistory)
            {
                if (historicalEntity.getUpdateTime() == null)
                {
                    /*
                     * This is the first version of the instance.
                     */
                    return historicalEntity;
                }

                if ((asOfTime.equals(historicalEntity.getUpdateTime())) ||
                            (asOfTime.after(historicalEntity.getUpdateTime())))
                {
                    /*
                     * The asOfTime is within the window of when this instance was valid.
                     */
                    return historicalEntity;
                }
            }

            return null;
        }


        /**
         * Return the instances that match the history.
         *
         * @param fromTime starting time
         * @param toTime ending time
         * @param oldestFirst ordering of results
         * @return list of versions of this relationship
         */
        synchronized List<EntityDetail> getEntityHistory(Date    fromTime,
                                                         Date    toTime,
                                                         boolean oldestFirst)
        {
            List<EntityDetail> historyResults = new ArrayList<>();

            /*
             * Do not have a full entity
             */
            if (this.entity == null)
            {
                return null;
            }

            if ((toTime != null) && (toTime.before(this.entity.getCreateTime())))
            {
                /*
                 * The entity is known - but the query time is from before the instance existed.
                 */
                return null;
            }

            /*
             * The current version of the entity is in range.
             */
            if (checkInclusiveDate(fromTime, toTime, this.entity, null))
            {
                historyResults.add(this.entity);
            }

            if (! this.entityHistory.isEmpty())
            {
                /*
                 * The period when an instance is active is from its updateTime to the updateTime of the next element.
                 * The entityHistory has the latest version first.
                 */
                Date followingUpdateTime = this.entity.getUpdateTime();

                for (EntityDetail historicalInstance : this.entityHistory)
                {
                    if (checkInclusiveDate(fromTime, toTime, historicalInstance, followingUpdateTime))
                    {
                        if (oldestFirst)
                        {
                            /*
                             * Add to the front
                             */
                            historyResults.add(0, historicalInstance);
                        }
                        else
                        {
                            /*
                             * Add to the back
                             */
                            historyResults.add(historicalInstance);
                        }
                    }

                    followingUpdateTime = historicalInstance.getUpdateTime();
                }
            }

            return historyResults;
        }


        /**
         * Return the instances that match the history.
         *
         * @param classificationName name of the classification history to retrieve
         * @param fromTime starting time
         * @param toTime ending time
         * @param oldestFirst ordering of results
         * @return list of versions of this relationship
         */
        synchronized List<Classification> getClassificationHistory(String  classificationName,
                                                                   Date    fromTime,
                                                                   Date    toTime,
                                                                   boolean oldestFirst)
        {
            /*
             * Do not have a full entity
             */
            if (this.entity == null)
            {
                return null;
            }

            if ((toTime != null) && (toTime.before(this.entity.getCreateTime())))
            {
                /*
                 * The entity is known - but the query time is from before the instance existed.
                 */
                return null;
            }

            /*
             * The current version of the entity is in range.
             */
            Map<Long, Classification> historyMap = new HashMap<>();

            List<Classification> matches = getClassificationsForInclusiveDate(classificationName,
                                                                              fromTime,
                                                                              toTime,
                                                                              this.entity.getClassifications(),
                                                                              null);

            if (matches != null)
            {
                for (Classification match : matches)
                {
                    historyMap.put(match.getVersion(), match);
                }
            }

            if (! this.entityHistory.isEmpty())
            {
                /*
                 * The period when an instance is active is from its updateTime to the updateTime of the next element.
                 * The entityHistory has the latest version first.
                 */
                Date followingUpdateTime = this.entity.getUpdateTime();

                for (EntityDetail historicalInstance : this.entityHistory)
                {
                    matches = getClassificationsForInclusiveDate(classificationName,
                                                                 fromTime,
                                                                 toTime,
                                                                 historicalInstance.getClassifications(),
                                                                 followingUpdateTime);

                    if (matches != null)
                    {
                        for (Classification match : matches)
                        {
                            historyMap.put(match.getVersion(), match);
                        }
                    }

                    followingUpdateTime = historicalInstance.getUpdateTime();
                }
            }

            if (! historyMap.isEmpty())
            {
                List<Classification> historyResults = new ArrayList<>();

                for (Long version : historyMap.keySet())
                {
                    if (oldestFirst)
                    {
                        /*
                         * Add to the front
                         */
                        historyResults.add(historyMap.get(version));
                    }
                    else
                    {
                        /*
                         * Add to the back
                         */
                        historyResults.add(0, historyMap.get(version));
                    }
                }

                return historyResults;
            }

            return null;
        }


        /**
         * Retrieve the previous version of the instance.
         *
         * @return first element in the history
         */
        synchronized EntityDetail retrievePreviousVersion()
        {
            if (! entityHistory.isEmpty())
            {
                return entityHistory.get(0);
            }

            return null;
        }


        /**
         * Class used to store and manage a single home classification.
         */
        private static class HomeClassification
        {
            volatile Classification latestClassification;
            volatile long           deletedVersionNumber = 0;


            /**
             * Constructor always includes the first version of the classification.
             *
             * @param classification classification to save
             */
            HomeClassification(Classification classification)
            {
                this.latestClassification = new Classification(classification);
            }


            /**
             * Return the saved classification (if any)
             *
             * @return active classification or null if the classification has been deleted
             */
            synchronized Classification getHomeClassification()
            {
                return this.latestClassification;
            }


            /**
             * Save an update to the classification.
             *
             * @param classification latest version of the classification
             */
            synchronized void saveClassification(Classification classification)
            {
                if (this.latestClassification == null)
                {
                    if (classification.getVersion() > this.deletedVersionNumber)
                    {
                        this.latestClassification = classification;
                    }
                }
                else
                {
                    if (classification.getVersion() > this.latestClassification.getVersion())
                    {
                        this.latestClassification = classification;
                    }
                }
            }


            /**
             * Remove the classification.  Care is take to retain the deleted version number to
             * be able to distinguish between a late update request and a restore request.
             *
             * @param classification optional classification from the caller
             */
            synchronized void deleteClassification(Classification classification)
            {
                if (this.latestClassification != null)
                {
                    this.deletedVersionNumber = this.latestClassification.getVersion();
                    this.latestClassification = null;
                }

                if (classification != null)
                {
                    if (classification.getVersion() > this.deletedVersionNumber)
                    {
                        this.deletedVersionNumber = classification.getVersion();
                    }
                }
            }
        }
    }


    /**
     * Class to manage the storage of relationships.
     */
    private class StoredRelationship
    {
        private final List<Relationship> relationshipHistory = new ArrayList<>();

        private volatile Relationship relationship         = null;
        private volatile Date         unilateralDeleteTime = null;


        /**
         * StoredRelationship is constructed with a valid relationship.  It may not be version 1.
         *
         * @param relationship first relationship
         */
        StoredRelationship(Relationship relationship)
        {
            saveRelationship(relationship);
        }


        /**
         * Save the new instance in the store and move the current instance to the front of the history.
         *
         * @param relationship new instance
         */
        synchronized void saveRelationship(Relationship relationship)
        {
            if (this.relationship != null)
            {
                this.relationshipHistory.add(0, this.relationship);
            }

            this.relationship = refreshRelationshipProxies(relationship);
        }


        /**
         * Remove the current version of the instance.  The history is still in place.
         **/
        synchronized void purgeRelationship()
        {
            if (this.relationship != null)
            {
                this.relationshipHistory.add(this.relationship);
                this.unilateralDeleteTime = new Date();
            }

            this.relationship = null;
        }


        /**
         * Retrieve the current instance.
         *
         * @return selected instance
         */
        synchronized Relationship getRelationship()
        {
            if (relationship != null)
            {
                return refreshRelationshipProxies(this.relationship);
            }

            return relationship;
        }


        /**
         * Retrieve the version that was active in the repository at a particular time.
         *
         * @param asOfTime time to use on the query
         * @return selected instance
         */
        synchronized Relationship getRelationship(Date asOfTime)
        {
            if (asOfTime == null)
            {
                return getRelationship();
            }

            if (this.relationship != null)
            {
                /*
                 * The requested time is before the element was created.
                 */
                if (asOfTime.before(relationship.getCreateTime()))
                {
                    return null;
                }

                /*
                 * The element has never been updated so the initial version is still valid.
                 */
                if (relationship.getUpdateTime() == null)
                {
                    return relationship;
                }

                if ((asOfTime.equals(relationship.getUpdateTime())) || (asOfTime.after(relationship.getUpdateTime())))
                {
                    /*
                     * The asOfTime is within the window of when this instance is valid.
                     */
                    return relationship;
                }
            }

            for (Relationship historicalRelationship : relationshipHistory)
            {
                if (historicalRelationship.getUpdateTime() == null)
                {
                    /*
                     * This is the first version of the instance.
                     */
                    return historicalRelationship;
                }

                if ((asOfTime.equals(historicalRelationship.getUpdateTime())) || (asOfTime.after(historicalRelationship.getUpdateTime())))
                {
                    /*
                     * The asOfTime is within the window of when this instance was valid.
                     */
                    return historicalRelationship;
                }
            }

            return null;
        }


        /**
         * Return the instances that match the history.
         *
         * @param fromTime starting time
         * @param toTime ending time
         * @param oldestFirst ordering of results
         * @return list of versions of this relationship
         */
        synchronized List<Relationship> getRelationshipHistory(Date    fromTime,
                                                               Date    toTime,
                                                               boolean oldestFirst)
        {
            List<Relationship> historyResults = new ArrayList<>();
            Date               followingUpdateTime;

            /*
             * Do not have a relationship
             */
            if (this.relationship == null)
            {
                return null;
            }

            if ((toTime != null) && (toTime.before(this.relationship.getCreateTime())))
            {
                /*
                 * The relationship is known - but the query time is from before the instance existed.
                 */
                return null;
            }

            if (unilateralDeleteTime != null)
            {
                /*
                 * Unilateral delete set when a linked entity is deleted.
                 */
                if (fromTime.after(unilateralDeleteTime))
                {
                    /*
                     * The relationship has been purged before the "fromTime".
                     */
                    return null;
                }

                followingUpdateTime = unilateralDeleteTime;
            }
            else
            {
                if (checkInclusiveDate(fromTime, toTime, this.relationship, null))
                {
                    /*
                     * The current version of the relationship is in range.
                     */
                    historyResults.add(this.relationship);
                }

                followingUpdateTime = this.relationship.getUpdateTime();
            }

            if (! this.relationshipHistory.isEmpty())
            {
                /*
                 * The period when an instance is active is from its updateTime to the updateTime of the next element.
                 * The relationshipHistory has the latest version first.
                 */
                for (Relationship historicalInstance : this.relationshipHistory)
                {
                    if (checkInclusiveDate(fromTime, toTime, historicalInstance, followingUpdateTime))
                    {
                        if (oldestFirst)
                        {
                            /*
                             * Add to the front
                             */
                            historyResults.add(0, historicalInstance);
                        }
                        else
                        {
                            /*
                             * Add to the back
                             */
                            historyResults.add(historicalInstance);
                        }
                    }

                    followingUpdateTime = historicalInstance.getUpdateTime();
                }
            }

            return historyResults;
        }


        /**
         * Retrieve the previous version of the instance.
         *
         * @return first element in the history
         */
        synchronized Relationship retrievePreviousVersion()
        {
            if (! relationshipHistory.isEmpty())
            {
                return relationshipHistory.get(0);
            }

            return null;
        }


        /**
         * Ensure a returned relationship is a clone of a stored value and contains the latest proxies for its ends.
         *
         * @param storedRelationship relationship retrieved from one of the relationship stores.
         * @return a cloned relationship with the latest proxies.
         */
        private synchronized Relationship refreshRelationshipProxies(Relationship storedRelationship)
        {
            if (storedRelationship != null)
            {
                Relationship result = new Relationship(storedRelationship);

                StoredEntity storedEntity = entityStore.get(storedRelationship.getEntityOneProxy().getGUID());

                if (storedEntity != null)
                {
                    result.setEntityOneProxy(storedEntity.getEntityProxy());
                }

                storedEntity = entityStore.get(storedRelationship.getEntityTwoProxy().getGUID());

                if (storedEntity != null)
                {
                    result.setEntityTwoProxy(storedEntity.getEntityProxy());
                }

                return result;
            }

            return null;
        }
    }
}
