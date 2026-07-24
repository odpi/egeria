/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * CountAccumulator accumulates counts received from a collection of open metadata repositories by summing them.
 * Unlike the entity/relationship accumulators, it does not attempt to deduplicate reference copies of the same
 * instance held by more than one repository in the cohort - doing so would require fetching every matching
 * instance, which defeats the purpose of an efficient count.  This class may be called simultaneously from
 * different threads, so it must be thread-safe.
 */
public class CountAccumulator extends QueryInstanceAccumulatorBase
{
    private long count = 0L;


    /**
     * Construct a count accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public CountAccumulator(String                  localMetadataCollectionId,
                            AuditLog                auditLog,
                            OMRSRepositoryValidator repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Add the count received from an open metadata repository to the running total.
     *
     * @param resultCount count returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    public synchronized void addCount(long   resultCount,
                                      String metadataCollectionId)
    {
        count += resultCount;

        /*
         * Record that this repository has returned results from the request.
         */
        super.setResultsReturned(metadataCollectionId, (int) Math.min(resultCount, Integer.MAX_VALUE));
    }


    /**
     * Extract the results - the sum of the counts returned by all repositories that responded successfully.
     * It should be called once all the executors have completed processing their request(s).
     *
     * @return total count
     */
    public synchronized long getResults()
    {
        return count;
    }
}
