/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassificationAccumulator accumulates and validates an entity received from a collection of open metadata
 * repositories.  It removes duplicates from the list by choosing the latest entity details object.
 *
 * This class may be called simultaneously from multiple threads, so it must be thread-safe.
 */
public abstract class ClassificationAccumulator extends MaintenanceAccumulator
{
    private final List<String>                contributingMetadataCollections = new ArrayList<>();
    private final Map<String, Classification> allClassifications              = new HashMap<>();
    private volatile boolean                  inPhaseOne                      = true;


    /**
     * Construct an entity accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param auditLog audit log provides destination for log messages
     */
    ClassificationAccumulator(AuditLog auditLog)
    {
        super(auditLog);
    }


    /**
     * Return the current state of the inPhaseOne flag.
     *
     * @return flag
     */
    public synchronized boolean isInPhaseOne()
    {
        return inPhaseOne;
    }


    /**
     * Update the state of the inPhaseOne flag.
     *
     * @param inPhaseOne new flag
     */
    synchronized void setInPhaseOne(boolean inPhaseOne)
    {
        this.inPhaseOne = inPhaseOne;
    }


    /**
     * Remember that a specific metadata collection has already been called.
     *
     * @param metadataCollectionId identifier of metadata collection
     */
    public synchronized void addContributingMetadataCollection(String metadataCollectionId)
    {
        contributingMetadataCollections.add(metadataCollectionId);
    }


    /**
     * Return the list of metadata collections that have been called.
     *
     * @return list of metadata collection ids
     */
    public synchronized List<String> getContributingMetadataCollections()
    {
        return contributingMetadataCollections;
    }


    /**
     * Return the list of GUIDs for the entities returned from the query.
     *
     * @return null or list of GUIDs
     */
    public abstract List<String> getResultsForAugmentation();


    /**
     * Save the best classifications from all the repositories.
     *
     * @param retrievedClassifications classifications from a repository
     */
    public void saveClassifications(List<Classification> retrievedClassifications)
    {
        if (retrievedClassifications != null)
        {
            for (Classification entityClassification : retrievedClassifications)
            {
                if (entityClassification != null)
                {
                    Classification existingClassification = allClassifications.get(entityClassification.getName());

                    /*
                     * Ignore older versions of the classification
                     */
                    if ((existingClassification == null) ||
                                (existingClassification.getVersion() < entityClassification.getVersion()))
                    {
                        allClassifications.put(entityClassification.getName(), entityClassification);
                    }
                }
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
        if (allClassifications.isEmpty())
        {
            return null;
        }
        else if (returnDeletedClassifications)
        {
            return new ArrayList<>(allClassifications.values());
        }
        else
        {
            List<Classification> activeClassifications = new ArrayList<>();

            for (Classification accumulatedClassification : allClassifications.values())
            {
                if (accumulatedClassification.getStatus() != InstanceStatus.DELETED)
                {
                    activeClassifications.add(accumulatedClassification);
                }
            }
            return activeClassifications;
        }
    }

}
