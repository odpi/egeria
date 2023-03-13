/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;

import java.util.List;

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


    /**
     * Return the results that need further augmentation.
     *
     * @return null (if no augmentation of results is required) or a list containing the guids of the results.
     */
    default List<String> getResultsForAugmentation()
    {
        return null;
    }


    /**
     * Perform the required action to augment a result.
     *
     * @param resultGUID unique identifier of result.
     * @param metadataCollectionId identifier for the metadata collection
     * @param metadataCollection metadata collection object for the repository
     */
    default void augmentResultFromRepository(String                 resultGUID,
                                             String                 metadataCollectionId,
                                             OMRSMetadataCollection metadataCollection)
    {
    }
}
