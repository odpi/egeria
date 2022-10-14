/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

/**
 * InstanceGraphAccumulator provides the accumulator for
 * an instance graph - this is a collection of related
 * entities and relationships.
 */
public class InstanceGraphAccumulator extends QueryAccumulatorBase
{
    /**
     * Construct a query accumulator.  This base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    InstanceGraphAccumulator(String                            localMetadataCollectionId,
                             OMRSAuditLog                      auditLog,
                             OMRSRepositoryValidator           repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }
}
