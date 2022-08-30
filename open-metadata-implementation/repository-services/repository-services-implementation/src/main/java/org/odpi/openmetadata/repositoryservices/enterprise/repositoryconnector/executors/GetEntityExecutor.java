/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.ClassificationAccumulator;

import java.util.List;


/**
 * GetEntityExecutor provides the common base executor for the getEntityXXX methods.  It is focused on managing the classifications.
 * Its subtypes manage the entity summary or entity detail.
 */
public abstract class GetEntityExecutor extends RepositoryExecutorBase
{
    private   final ClassificationAccumulator accumulator;

    protected String                    entityGUID;


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityGUID unique identifier (guid) for the entity
     * @param accumulator for the request
     * @param methodName calling method
     */
    public GetEntityExecutor(String                    userId,
                             String                    entityGUID,
                             ClassificationAccumulator accumulator,
                             String                    methodName)
    {
        super(userId, methodName);

        this.accumulator = accumulator;
        this.entityGUID = entityGUID;
    }


    /**
     * Return the results that need further augmentation.
     *
     * @return null (if no augmentation of results is required) or a list containing the guids of the results.
     */
    public List<String> getResultsForAugmentation()
    {
        return accumulator.getResultsForAugmentation();
    }


    /**
     * Perform the required action to augment a result by calling a specific repository.
     *
     * @param resultGUID unique identifier of result.
     * @param metadataCollectionId identifier for the metadata collection
     * @param metadataCollection metadata collection object for the repository
     */
    public void augmentResultFromRepository(String                 resultGUID,
                                            String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        List<String> contributingMetadataCollections = accumulator.getContributingMetadataCollections();

        if ((contributingMetadataCollections == null) || (! contributingMetadataCollections.contains(metadataCollectionId)))
        {
            getHomeClassifications(metadataCollection);
        }
    }


    /**
     * Retrieve the home classifications from the repository.
     *
     * @param metadataCollection repository to issue request to
     */
    protected void getHomeClassifications(OMRSMetadataCollection metadataCollection)
    {
        try
        {
            List<Classification> homeClassifications = metadataCollection.getHomeClassifications(userId, entityGUID);

            accumulator.saveClassifications(homeClassifications);
        }
        catch (Exception error)
        {
            // ignore exceptions because the returned exceptions come from the retrieval of the entity.
        }
    }
}
