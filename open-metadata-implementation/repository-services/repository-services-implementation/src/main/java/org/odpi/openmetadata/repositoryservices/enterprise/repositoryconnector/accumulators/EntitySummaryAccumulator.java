/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;

import java.util.ArrayList;
import java.util.List;


/**
 * ClassificationAccumulator accumulates and validates an entity received from a collection of open metadata
 * repositories.  It removes duplicates from the list by choosing the latest entity details object.
 *
 * This class may be called simultaneously from different threads, so it must be thread-safe.
 */
public class EntitySummaryAccumulator extends ClassificationAccumulator
{
    private volatile EntitySummary currentSavedEntity = null;


    /**
     * Construct an entity accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param auditLog audit log provides destination for log messages
     */
    public EntitySummaryAccumulator(AuditLog  auditLog)
    {
        super(auditLog);
    }


    /**
     * Provide an entity received from an open metadata repository.
     *
     * @param incomingEntity entity returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    public synchronized void addEntity(EntitySummary incomingEntity,
                                       String        metadataCollectionId)
    {
        if ((incomingEntity != null) && (incomingEntity.getGUID() != null))
        {
            if (metadataCollectionId.equals(incomingEntity.getMetadataCollectionId()))
            {
                /*
                 * The home repository is found - assume it is the latest version - moving to phase two
                 */
                currentSavedEntity = incomingEntity;
                super.setInPhaseOne(false);
            }
            else if (currentSavedEntity == null)
            {
                currentSavedEntity = incomingEntity;
            }
            else
            {
                /*
                 * This incoming instance is ignored if we already have a later version.
                 */
                if (! super.currentInstanceIsBest(currentSavedEntity, incomingEntity))
                {
                    currentSavedEntity = incomingEntity;
                }
            }

            /*
             * Now consider the classifications.  Each received entity may have a different set of classifications attached and
             * the enterprise connector should create an accumulated list of the best ones.
             */
            super.saveClassifications(incomingEntity.getClassifications());
        }
    }


    /**
     * Return the list of GUIDs for the entities returned from the query.
     *
     * @return null or list of GUIDs
     */
    public List<String> getResultsForAugmentation()
    {
        if (currentSavedEntity != null)
        {
            List<String> resultsArray = new ArrayList<>();
            resultsArray.add(currentSavedEntity.getGUID());

            return resultsArray;
        }

        return null;
    }


    /**
     * Extract the resulting entity and accumulated classifications.  It should be called once all the executors have completed processing
     * their request(s).
     *
     * @return list of entities
     */
    public  EntitySummary getResult()
    {
        if (currentSavedEntity != null)
        {
            currentSavedEntity.setClassifications(super.getClassifications());

            return currentSavedEntity;
        }

        return null;
    }
}
