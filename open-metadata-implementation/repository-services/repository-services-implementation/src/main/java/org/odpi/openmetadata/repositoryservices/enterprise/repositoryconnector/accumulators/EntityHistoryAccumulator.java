/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EntityHistoryAccumulator accumulates and validates historical versions of entities received from a collection of open metadata
 * repositories.  It removes duplicates from the list but preserves one instance of each version.
 * This class may be called simultaneously from different threads, so it must be thread-safe.
 */
public class EntityHistoryAccumulator extends EntitiesAccumulator
{
    /*
     * Map of version to entity detail retrieved from the repositories
     */
    private final Map<Long, EntityDetail>           accumulatedEntities = new HashMap<>();
    private final Map<String, List<Classification>> accumulatedClassifications = new HashMap<>();
    private final Map<String, List<String>>         accumulatedEntitySources   = new HashMap<>();

    private String entityGUID = null;


    /**
     * Construct an entity accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public EntityHistoryAccumulator(String                  localMetadataCollectionId,
                                    AuditLog                auditLog,
                                    OMRSRepositoryValidator repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Provide an entity received from an open metadata repository.
     *
     * @param incomingEntity entity returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    private  void addEntity(EntityDetail incomingEntity,
                            String       metadataCollectionId)
    {
        if ((incomingEntity != null) && (incomingEntity.getGUID() != null) && (metadataCollectionId != null))
        {
            entityGUID = incomingEntity.getGUID();

            /*
             * Remember that this metadata collection has already returned this entity.
             */
            List<String> contributingMetadataCollections = accumulatedEntitySources.get(entityGUID);

            if (contributingMetadataCollections == null)
            {
                contributingMetadataCollections = new ArrayList<>();
            }

            contributingMetadataCollections.add(metadataCollectionId);

            accumulatedEntitySources.put(entityGUID, contributingMetadataCollections);

            accumulatedEntities.put(incomingEntity.getVersion(), incomingEntity);

            if (metadataCollectionId.equals(localMetadataCollectionId))
            {
                super.captureLocalInstance(entityGUID);
            }

            /*
             * Now consider the classifications.  Each received entity may have a different set of classifications attached and
             * the enterprise connector should create an accumulated list of the best ones.
             */
            this.saveClassifications(entityGUID, incomingEntity.getClassifications());
        }
    }


    /**
     * Add a list of entities to the accumulator. This method is included to save the executors from coding this
     * loop to process each entity.
     *
     * @param entities list of retrieved entities
     * @param metadataCollectionId source metadata collection
     */
    @Override
    public synchronized void addEntities(List<EntityDetail>   entities,
                                         String               metadataCollectionId)
    {
        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                entityGUID = entity.getGUID();

                this.addEntity(entity, metadataCollectionId);
            }

            /*
             * Record that this repository has returned results from the request.
             */
            super.setResultsReturned(metadataCollectionId, entities.size());
        }
        else
        {
            /*
             * Even though results were not found it was still a successful request.
             */
            super.setResultsReturned(metadataCollectionId, 0);
        }
    }


    /**
     * Add a list of classifications to the accumulator. These are extracted from the retrieved entities, or they are separately
     * retrieved from metadata collections that only have an entity proxy with
     * classifications connected to it that are uniquely stored in the metadata collection.
     *
     * @param entityGUID unique identifier
     * @param incomingClassifications list of retrieved classifications
     */
    @Override
    public synchronized void saveClassifications(String               entityGUID,
                                                 List<Classification> incomingClassifications)
    {

        /*
         * Each received entity may have a different set of classifications attached and
         * the enterprise connector should create an accumulated list of the best ones.
         */
        List<Classification> currentSavedClassifications = accumulatedClassifications.get(entityGUID);

        if (currentSavedClassifications == null)
        {
            /*
             * No classifications currently saved for this entity
             */
            if (incomingClassifications != null)
            {
                accumulatedClassifications.put(entityGUID, incomingClassifications);
            }
        }
        else if (incomingClassifications != null)
        {
            /*
             * Need to merge the two lists
             */
            Map<String, Classification> entityClassificationsMap = new HashMap<>();

            /*
             * Add the current classifications to the map.
             */
            for (Classification existingClassification : currentSavedClassifications)
            {
                if (existingClassification != null)
                {
                    entityClassificationsMap.put(existingClassification.getName(), existingClassification);
                }
            }

            /*
             * Add the incoming classifications if they are a later version.
             */
            for (Classification newClassification : incomingClassifications)
            {
                if (newClassification != null)
                {
                    Classification existingClassification = entityClassificationsMap.get(newClassification.getName());

                    /*
                     * Ignore older versions of the classification
                     */
                    if ((existingClassification == null) || (existingClassification.getVersion() < newClassification.getVersion()))
                    {
                        entityClassificationsMap.put(newClassification.getName(), newClassification);
                    }
                }
            }

            /*
             * Save the merged list.
             */
            if (! entityClassificationsMap.isEmpty())
            {
                accumulatedClassifications.put(entityGUID, new ArrayList<>(entityClassificationsMap.values()));
            }
        }
    }


    /**
     * Return the list of metadata collection ids for the metadata collections that retrieved this entity.
     *
     * @param entityGUID unique identifier for entity of interest
     * @return null or list of metadata collection ids
     */
    @Override
    public List<String> getContributingMetadataCollections(String entityGUID)
    {
        return accumulatedEntitySources.get(entityGUID);
    }


    /**
     * Return the list of GUIDs for the entities returned from the query.
     *
     * @return null or list of GUIDs
     */
    @Override
    public List<String> getResultsForAugmentation()
    {
        if ((! accumulatedEntities.isEmpty() && (entityGUID != null)))
        {
            List<String> guids = new ArrayList<>();

            guids.add(entityGUID);

            return guids;
        }

        return null;
    }


    /**
     * Extract the results - this will be a unique list of entities selected from the instances
     * supplied to this accumulator.  It should be called once all the executors have completed processing
     * their request(s).
     *
     * @param oldestFirst ordering of results
     * @param repositoryConnector enterprise connector
     * @param metadataCollection enterprise metadata collection
     * @return list of entities
     */
    public synchronized List<EntityDetail> getResults(boolean                           oldestFirst,
                                                      EnterpriseOMRSRepositoryConnector repositoryConnector,
                                                      OMRSMetadataCollection            metadataCollection)
    {
        if ((accumulatedEntities == null) || (accumulatedEntities.isEmpty()))
        {
            return null;
        }
        else
        {
            this.makeRefreshRecommendations(repositoryConnector);

            List<EntityDetail>  results = new ArrayList<>();

            if (metadataCollection != null)
            {
                for (Long version : accumulatedEntities.keySet())
                {
                    EntityDetail resultEntity = new EntityDetail(accumulatedEntities.get(version));

                    resultEntity.setClassifications(accumulatedClassifications.get(resultEntity.getGUID()));

                    if (oldestFirst)
                    {
                        /*
                         * Add to the front
                         */
                        results.add(0, resultEntity);
                    }
                    else
                    {
                        /*
                         * Add to the back
                         */
                        results.add(resultEntity);
                    }
                }
            }

            if (results.isEmpty())
            {
                return null;
            }
            else
            {
                return results;
            }
        }
    }


    /**
     * Return the list of entities that where retrieved from other repositories and not stored in the local repository.
     * The local repository may use this list to send out refresh requests on the OMRS Topic.  If the rules allow, and
     * the remote repository response with a refresh response, this entity could be replicated into the local
     * repository.
     *
     * The value of this processing is that the entity is of interest to the local users so having a local copy could
     * reduce the access time for the entity.
     *
     * This call should be made once all processing has stopped.
     *
     * @param repositoryConnector enterprise connector
     */
    private void makeRefreshRecommendations(EnterpriseOMRSRepositoryConnector repositoryConnector)
    {
        /*
         * Either no local repository or nothing accumulated so nothing to return
         */
        if ((localMetadataCollectionId == null) || (accumulatedEntities.isEmpty()))
        {
            return;
        }

        /*
         * Ignore all entities that came from the local repository
         */
        for (EntityDetail accumulatedEntity : accumulatedEntities.values())
        {
            if (accumulatedEntity != null)
            {
                String  entityGUID = accumulatedEntity.getGUID();

                if (entityGUID != null)
                {
                    if (super.notLocal(entityGUID))
                    {
                        repositoryConnector.requestRefreshOfEntity(accumulatedEntity);
                    }
                }
            }
        }
    }
}
