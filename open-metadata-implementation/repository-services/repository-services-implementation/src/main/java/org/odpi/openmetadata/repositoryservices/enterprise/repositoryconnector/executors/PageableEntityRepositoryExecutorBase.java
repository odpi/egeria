/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.EntitiesAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * PageableEntityRepositoryExecutorBase is the executor base class for find requests that return entities.  It is responsible for the
 * second phase of retrieving a list of entities where repositories that have not returned an entity instance are called to see if they have
 * disconnected home classifications to add to these entities.
 */
public abstract class PageableEntityRepositoryExecutorBase extends PageableRepositoryExecutorBase
{
    EntitiesAccumulator accumulator;


    /**
     * Create the executor.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param accumulator location for results and returned exceptions
     * @param methodName calling method
     */
    PageableEntityRepositoryExecutorBase(String                  userId,
                                         String                  entityTypeGUID,
                                         int                     fromEntityElement,
                                         List<InstanceStatus>    limitResultsByStatus,
                                         Date                    asOfTime,
                                         String                  sequencingProperty,
                                         SequencingOrder         sequencingOrder,
                                         int                     pageSize,
                                         EntitiesAccumulator     accumulator,
                                         String                  methodName)
    {
        super(userId,
              entityTypeGUID,
              fromEntityElement,
              limitResultsByStatus,
              sequencingProperty,
              sequencingOrder,
              pageSize,
              asOfTime,
              accumulator,
              methodName);

        this.accumulator = accumulator;
    }


    /**
     * Return the results that need further augmentation.
     *
     * @return null (if no augmentation of results is required) or a list containing the guids of the results.
     */
    @Override
    public List<String> getResultsForAugmentation()
    {
        return accumulator.getResultsForAugmentation();
    }


    /**
     * Perform the required action to augment a result.
     *
     * @param resultGUID unique identifier of result.
     * @param metadataCollectionId identifier for the metadata collection
     * @param metadataCollection metadata collection object for the repository
     */
    @Override
    public void augmentResultFromRepository(String                 resultGUID,
                                            String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        List<String> contributingMetadataCollections = accumulator.getContributingMetadataCollections(resultGUID);

        if ((contributingMetadataCollections == null) || (! contributingMetadataCollections.contains(metadataCollectionId)))
        {
            /*
             * This repository did not return this entity.  It may have a classification attached to an entity proxy
             */
            try
            {
                List<Classification> homeClassifications = metadataCollection.getHomeClassifications(userId, resultGUID);

                accumulator.saveClassifications(resultGUID, homeClassifications);
            }
            catch (Exception error)
            {
                // ignore exceptions because the returned exceptions come from the retrieval of the entity.
            }
        }
    }


    /**
     * Return the results or exception.
     *
     * @param repositoryConnector enterprise connector
     * @param metadataCollection enterprise metadata collection
     *
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<EntityDetail> getResults(EnterpriseOMRSRepositoryConnector repositoryConnector,
                                         OMRSMetadataCollection            metadataCollection) throws InvalidParameterException,
                                                                                                      RepositoryErrorException,
                                                                                                      TypeErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      PagingErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      UserNotAuthorizedException
    {
        if (accumulator.resultsReturned())
        {
            return accumulator.getResults(repositoryConnector, metadataCollection);
        }

        handleCommonPagingRequestExceptions();

        return null;
    }
}
