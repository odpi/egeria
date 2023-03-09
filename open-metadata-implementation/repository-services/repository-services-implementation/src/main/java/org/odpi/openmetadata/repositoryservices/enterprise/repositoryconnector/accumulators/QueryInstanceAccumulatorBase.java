/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryAccumulatorBase acts as a base class to the accumulators that need to visit each repository and
 * combine results.
 */
public class QueryInstanceAccumulatorBase extends QueryAccumulatorBase
{
    protected List<String>            locallyStoredInstancesGUIDs = new ArrayList<>();

    /**
     * Construct a query accumulator.  This base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    QueryInstanceAccumulatorBase(String                  localMetadataCollectionId,
                                 AuditLog                auditLog,
                                 OMRSRepositoryValidator repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Save the list of GUIDs of instances that are stored locally.  These may be home or reference copies.
     *
     * @param locallyStoredInstancesGUID guid of an instance that is best of breed and stored locally
     */
    void captureLocalInstance(String   locallyStoredInstancesGUID)
    {
        this.locallyStoredInstancesGUIDs.add(locallyStoredInstancesGUID);
    }


    /**
     * Return a flag that indicates if the instance is stored in the repository or not.
     *
     * @param instanceGUID unique identifier (guid) of instance
     * @return boolean true if entity is NOT stored in local repository
     */
    boolean notLocal(String   instanceGUID)
    {
        return ! (locallyStoredInstancesGUIDs.contains(instanceGUID));
    }
}
