/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
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
     * @param localMetadataCollectionId collection Id of local repository - null means no local repository
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
     * Return a flag indicating whether the incoming element should be preferred over the current saved element.
     *
     * @param currentSavedElement element returned from previous request
     * @param incomingElement element just returned
     * @return boolean flag - true means the incoming element should be discarded
     */
    boolean currentInstanceIsBest(InstanceAuditHeader currentSavedElement,
                                  InstanceAuditHeader incomingElement)
    {
        if (currentSavedElement != null)
        {
            return (incomingElement.getVersion() <= currentSavedElement.getVersion());
        }

        return false;
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
