/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.CountAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;

/**
 * CountEntitiesExecutor is the executor for the countEntities request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 * It sums the counts returned by each repository in the cohort; it does not deduplicate reference copies of the
 * same entity held by more than one repository, since doing so would require fetching every matching entity.
 */
public class CountEntitiesExecutor extends PageableRepositoryExecutorBase
{
    private final SearchProperties      matchProperties;
    private final SearchClassifications matchClassifications;
    private final List<String>          instanceSubtypeGUIDs;
    private final CountAccumulator      accumulator;

    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity properties to match (contains wildcards).
     * @param fromEntityElement not used - the count is not affected by paging.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty not used - the count is not affected by sequencing.
     * @param sequencingOrder not used - the count is not affected by sequencing.
     * @param pageSize not used - the count is not affected by paging.
     * @param localMetadataCollectionId unique identifier for the local repository - may be null if no local repository
     * @param auditLog logging destination
     * @param repositoryValidator validator for resulting relationships
     * @param methodName calling method
     */
    public CountEntitiesExecutor(String                  userId,
                                 String                  entityTypeGUID,
                                 List<String>            entitySubtypeGUIDs,
                                 SearchProperties        matchProperties,
                                 int                     fromEntityElement,
                                 List<InstanceStatus>    limitResultsByStatus,
                                 SearchClassifications   matchClassifications,
                                 Date                    asOfTime,
                                 String                  sequencingProperty,
                                 SequencingOrder         sequencingOrder,
                                 int                     pageSize,
                                 String                  localMetadataCollectionId,
                                 AuditLog                auditLog,
                                 OMRSRepositoryValidator repositoryValidator,
                                 String                  methodName)
    {
        this(userId,
             entityTypeGUID,
             entitySubtypeGUIDs,
             matchProperties,
             fromEntityElement,
             limitResultsByStatus,
             matchClassifications,
             asOfTime,
             sequencingProperty,
             sequencingOrder,
             pageSize,
             new CountAccumulator(localMetadataCollectionId, auditLog, repositoryValidator),
             methodName);
    }


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity properties to match (contains wildcards).
     * @param fromEntityElement not used - the count is not affected by paging.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty not used - the count is not affected by sequencing.
     * @param sequencingOrder not used - the count is not affected by sequencing.
     * @param pageSize not used - the count is not affected by paging.
     * @param accumulator location for results and returned exceptions
     * @param methodName calling method
     */
    private CountEntitiesExecutor(String                userId,
                                  String                entityTypeGUID,
                                  List<String>          entitySubtypeGUIDs,
                                  SearchProperties      matchProperties,
                                  int                   fromEntityElement,
                                  List<InstanceStatus>  limitResultsByStatus,
                                  SearchClassifications matchClassifications,
                                  Date                  asOfTime,
                                  String                sequencingProperty,
                                  SequencingOrder       sequencingOrder,
                                  int                   pageSize,
                                  CountAccumulator      accumulator,
                                  String                methodName)
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

        this.matchProperties = matchProperties;
        this.matchClassifications = matchClassifications;
        this.instanceSubtypeGUIDs = entitySubtypeGUIDs;
        this.accumulator = accumulator;
    }


    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     * This is used when setting up the parallel execution of the work.  Each clone executes
     * the calls to a single open metadata repository.
     *
     * @return clone of this executor
     */
    public CloneableRepositoryExecutor getClone()
    {
        return new CountEntitiesExecutor(userId,
                                         instanceTypeGUID,
                                         instanceSubtypeGUIDs,
                                         matchProperties,
                                         startingElement,
                                         limitResultsByStatus,
                                         matchClassifications,
                                         asOfTime,
                                         sequencingProperty,
                                         sequencingOrder,
                                         pageSize,
                                         accumulator,
                                         methodName);
    }


    /**
     * Perform the required action for the supplied repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        try
        {
            /*
             * Issue the request
             */
            long result = metadataCollection.countEntities(userId,
                                                            instanceTypeGUID,
                                                            instanceSubtypeGUIDs,
                                                            matchProperties,
                                                            startingElement,
                                                            limitResultsByStatus,
                                                            matchClassifications,
                                                            asOfTime,
                                                            sequencingProperty,
                                                            sequencingOrder,
                                                            pageSize);

            accumulator.addCount(result, metadataCollectionId);
        }
        catch (FunctionNotSupportedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (TypeErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PropertyErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (PagingErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }


    /**
     * Return the results of the combined requests - the sum of the counts returned by all repositories that
     * responded successfully.
     *
     * @return count of entities matching the supplied criteria.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support this optional method.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public long getResults() throws InvalidParameterException,
                                    TypeErrorException,
                                    RepositoryErrorException,
                                    PropertyErrorException,
                                    PagingErrorException,
                                    FunctionNotSupportedException,
                                    UserNotAuthorizedException
    {
        if (accumulator.resultsReturned())
        {
            return accumulator.getResults();
        }

        handleCommonPagingRequestExceptions();

        return 0L;
    }
}
