/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EntityAccumulator accumulates and validates entities received from a collection of open metadata
 * repositories.  It removes duplicates from the list by choosing the latest entity details object.
 *
 * This class may be called simultaneously from many different threads so it must be thread-safe.
 */
public class EntityAccumulator extends QueryInstanceAccumulatorBase
{
    private volatile Map<String, EntityDetail> accumulatedEntities = new HashMap<>();


    /**
     * Construct a entity accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection Id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public EntityAccumulator(String                  localMetadataCollectionId,
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
    private  void addEntity(EntityDetail   incomingEntity,
                            String         metadataCollectionId)
    {
        if ((incomingEntity != null) && (incomingEntity.getGUID() != null) && (metadataCollectionId != null))
        {
            String        entityGUID = incomingEntity.getGUID();
            EntityDetail  currentSavedEntity = accumulatedEntities.get(entityGUID);

            /*
             * This incoming instance is ignored if we already have a later version.
             */
            if (super.currentInstanceIsBest(currentSavedEntity, incomingEntity))
            {
                return;
            }

            /*
             * Since the incoming instance is better than what we have saved then save it.
             */
            accumulatedEntities.put(entityGUID, incomingEntity);

            if (metadataCollectionId.equals(localMetadataCollectionId))
            {
                super.captureLocalInstance(entityGUID);
            }
        }
    }


    /**
     * Add a list of entities to the accumulator. This method is included to save the executors from coding this
     * loop to process each entity.
     *
     * @param entities list of retrieved entities
     * @param metadataCollectionId source metadata collection
     */
    public synchronized void addEntities(List<EntityDetail>   entities,
                                         String               metadataCollectionId)
    {
        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
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
     * Extract the results - this will the a unique list of entities selected from the instances
     * supplied to this accumulator.  It should be called once all of the executors have completed processing
     * their request(s).
     *
     * @param repositoryConnector enterprise connector
     * @return list of entities
     */
    public synchronized List<EntityDetail>  getResults(EnterpriseOMRSRepositoryConnector repositoryConnector)
    {
        if (accumulatedEntities.isEmpty())
        {
            return null;
        }
        else
        {
            this.makeRefreshRecommendations(repositoryConnector);
            return new ArrayList<>(accumulatedEntities.values());
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
    private  void  makeRefreshRecommendations(EnterpriseOMRSRepositoryConnector repositoryConnector)
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
