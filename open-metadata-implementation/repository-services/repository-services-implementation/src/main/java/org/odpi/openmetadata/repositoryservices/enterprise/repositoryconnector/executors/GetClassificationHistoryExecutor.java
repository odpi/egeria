/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.ClassificationHistoryAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;

/**
 * GetEntityDetailHistoryExecutor is the executor for the findEntitiesByPropertyValue request.
 * This request can be issued in parallel - the call to each request potentially running in a different thread.
 */
public class GetClassificationHistoryExecutor extends PageableRepositoryExecutorBase
{
    private final String                 guid;
    private final String                 classificationName;
    private final Date                   fromTime;
    private final Date                   toTime;
    private final HistorySequencingOrder historySequencingOrder;

    private final ClassificationHistoryAccumulator historyAccumulator;


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of classification to retrieve
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param localMetadataCollectionId unique identifier for the local repository - may be null if no local repository
     * @param auditLog logging destination
     * @param repositoryValidator validator for resulting relationships
     * @param methodName calling method
     */
    public GetClassificationHistoryExecutor(String                  userId,
                                            String                  guid,
                                            String                  classificationName,
                                            Date                    fromTime,
                                            Date                    toTime,
                                            int                     startFromElement,
                                            int                     pageSize,
                                            HistorySequencingOrder  sequencingOrder,
                                            String                  localMetadataCollectionId,
                                            AuditLog                auditLog,
                                            OMRSRepositoryValidator repositoryValidator,
                                            String                  methodName)
    {
        this(userId,
             guid,
             classificationName,
             fromTime,
             toTime,
             startFromElement,
             pageSize,
             sequencingOrder,
             new ClassificationHistoryAccumulator(localMetadataCollectionId, auditLog, repositoryValidator),
             methodName);
    }


    /**
     * Create the executor.  The parameters provide the parameters for issuing the requests and
     * combining the results.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of classification to retrieve
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param accumulator location for results and returned exceptions
     * @param methodName calling method
     */
    private GetClassificationHistoryExecutor(String                           userId,
                                             String                           guid,
                                             String                           classificationName,
                                             Date                             fromTime,
                                             Date                             toTime,
                                             int                              startFromElement,
                                             int                              pageSize,
                                             HistorySequencingOrder           sequencingOrder,
                                             ClassificationHistoryAccumulator accumulator,
                                             String                           methodName)
    {
        super(userId,
              null,
              startFromElement,
              null,
              null,
              null,
              pageSize,
              null,
              accumulator,
              methodName);

        this.guid = guid;
        this.classificationName = classificationName;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.historySequencingOrder = sequencingOrder;
        this.historyAccumulator = accumulator;
    }


    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     * This is used when setting up the parallel execution of the work.  Each clone executes
     * the calls to a single open metadata repository.
     *
     * @return clone of this executor
     */
    @Override
    public CloneableRepositoryExecutor getClone()
    {
        return new GetClassificationHistoryExecutor(userId,
                                                    guid,
                                                    classificationName,
                                                    fromTime,
                                                    toTime,
                                                    startingElement,
                                                    pageSize,
                                                    historySequencingOrder,
                                                    historyAccumulator,
                                                    methodName);
    }


    /**
     * Perform the required action for the supplied repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     * @return boolean true means that the required results have been achieved
     */
    @Override
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        try
        {
            /*
             * Issue the request
             */
            List<Classification> results = metadataCollection.getClassificationHistory(userId,
                                                                                       guid,
                                                                                       classificationName,
                                                                                       fromTime,
                                                                                       toTime,
                                                                                       startingElement,
                                                                                       pageSize,
                                                                                       historySequencingOrder);

            historyAccumulator.saveClassifications(results, metadataCollectionId);
        }
        catch (InvalidParameterException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (FunctionNotSupportedException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (RepositoryErrorException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (UserNotAuthorizedException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (EntityNotKnownException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (EntityProxyOnlyException error)
        {
            historyAccumulator.captureException(metadataCollectionId, error);
        }
        catch (Exception error)
        {
            historyAccumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }


    /**
     * Return the results or exception.
     *
     * @param metadataCollection enterprise metadata collection
     *
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     */
    public List<Classification> getHistoryResults(OMRSMetadataCollection metadataCollection) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    EntityProxyOnlyException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        if (historyAccumulator.resultsReturned())
        {
            if (historySequencingOrder == HistorySequencingOrder.BACKWARDS)
            {
                return historyAccumulator.getResults(false, metadataCollection);
            }
            else
            {
                return historyAccumulator.getResults(true, metadataCollection);
            }
        }

        historyAccumulator.throwCapturedUserNotAuthorizedException();
        historyAccumulator.throwCapturedRepositoryErrorException();
        historyAccumulator.throwCapturedEntityNotKnownException();
        historyAccumulator.throwCapturedEntityProxyOnlyException();
        historyAccumulator.throwCapturedGenericException(methodName);
        historyAccumulator.throwCapturedInvalidParameterException();
        historyAccumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
