/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;

public interface RepositoryExecutor
{
    /**
     * Perform the required action for the supplied repository.
     *
     * @param metadataCollectionId identifier for the metadata collection
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    boolean issueRequestToRepository(String                 metadataCollectionId,
                                     OMRSMetadataCollection metadataCollection);
}
