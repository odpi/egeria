/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassificationHistoryAccumulator accumulates and validates historical versions of classifications received from a
 * collection of open metadata repositories.  It removes duplicates from the list to preserve one instance of each version.
 * This class may be called simultaneously from different threads, so it must be thread-safe.
 */
public class ClassificationHistoryAccumulator extends QueryInstanceAccumulatorBase
{
    /*
     * Map of version to classification retrieved from the repositories
     */
    private final Map<Long, Classification> accumulatedClassifications = new HashMap<>();



    /**
     * Construct an entity accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public ClassificationHistoryAccumulator(String                  localMetadataCollectionId,
                                            AuditLog                auditLog,
                                            OMRSRepositoryValidator repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Add a list of classifications to the accumulator. These are the historical versions of the classifications
     * from  particular repository.
     *
     * @param incomingClassifications list of retrieved classifications
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    public synchronized void saveClassifications(List<Classification> incomingClassifications,
                                                 String               metadataCollectionId)
    {
        if (incomingClassifications != null)
        {
            for (Classification classification : incomingClassifications)
            {
               this.addClassification(classification, metadataCollectionId);
            }

            /*
             * Record that this repository has returned results from the request.
             */
            super.setResultsReturned(metadataCollectionId, incomingClassifications.size());
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
     * Process a classification received from an open metadata repository.
     *
     * @param incomingClassification entity returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    private  void addClassification(Classification incomingClassification,
                                    String         metadataCollectionId)
    {
        if ((incomingClassification != null) && (metadataCollectionId != null))
        {
            String classificationName = incomingClassification.getName();

            /*
             * Remember that this metadata collection has already returned this entity.
             */

            accumulatedClassifications.put(incomingClassification.getVersion(), incomingClassification);

            if (metadataCollectionId.equals(localMetadataCollectionId))
            {
                super.captureLocalInstance(classificationName);
            }
        }
    }


    /**
     * Return the accumulated classifications to the caller.
     *
     * @param returnDeletedClassifications should classifications in deleted status be returned?
     * @return null or list of classifications
     */
    List<Classification> getClassifications(boolean returnDeletedClassifications)
    {
        if (accumulatedClassifications.isEmpty())
        {
            return null;
        }
        else if (returnDeletedClassifications)
        {
            return new ArrayList<>(accumulatedClassifications.values());
        }
        else
        {
            List<Classification> activeClassifications = new ArrayList<>();

            for (Classification accumulatedClassification : accumulatedClassifications.values())
            {
                if (accumulatedClassification.getStatus() != InstanceStatus.DELETED)
                {
                    activeClassifications.add(accumulatedClassification);
                }
            }

            return activeClassifications;
        }
    }


    /**
     * Extract the results - this will be a unique list of entities selected from the instances
     * supplied to this accumulator.  It should be called once all the executors have completed processing
     * their request(s).
     *
     * @param oldestFirst ordering of results
     * @param metadataCollection enterprise metadata collection
     * @return list of entities
     */
    public synchronized List<Classification> getResults(boolean                 oldestFirst,
                                                        OMRSMetadataCollection  metadataCollection)
    {
        if ((accumulatedClassifications == null) || (accumulatedClassifications.isEmpty()))
        {
            return null;
        }
        else
        {
            List<Classification>  historyResults = new ArrayList<>();

            if (metadataCollection != null)
            {
                for (Long version : accumulatedClassifications.keySet())
                {
                    if (oldestFirst)
                    {
                        /*
                         * Add to the front
                         */
                        historyResults.add(0, accumulatedClassifications.get(version));
                    }
                    else
                    {
                        /*
                         * Add to the back
                         */
                        historyResults.add(accumulatedClassifications.get(version));
                    }
                }
            }

            if (historyResults.isEmpty())
            {
                return null;
            }
            else
            {
                return historyResults;
            }
        }
    }
}
