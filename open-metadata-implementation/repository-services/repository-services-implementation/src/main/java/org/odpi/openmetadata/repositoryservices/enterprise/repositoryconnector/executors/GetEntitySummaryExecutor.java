/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.EntitySummaryAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;



/**
 * GetEntitySummaryExecutor provides the executor for the getEntitySummary method.
 */
public class GetEntitySummaryExecutor extends GetEntityExecutor
{
    private final EntitySummaryAccumulator  accumulator;


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityGUID unique identifier (guid) for the entity
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetEntitySummaryExecutor(String   userId,
                                    String   entityGUID,
                                    AuditLog auditLog,
                                    String   methodName)
    {
        this(userId, entityGUID, new EntitySummaryAccumulator(auditLog), methodName);
    }



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityGUID unique identifier (guid) for the entity
     * @param accumulator to use
     * @param methodName calling method
     */
    private GetEntitySummaryExecutor(String                   userId,
                                     String                   entityGUID,
                                     EntitySummaryAccumulator accumulator,
                                     String                   methodName)
    {
        super(userId, entityGUID, accumulator, methodName);

        this.accumulator = accumulator;
    }


    /**
     * Perform the required action for the supplied repository.
     * Create requests occur in the first repository that accepts the call.
     * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
     * there are no positive results from any repository.
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
             * Issue the request and return if it succeeds
             */
            EntitySummary retrievedEntity = metadataCollection.getEntitySummary(userId, entityGUID);

            if (retrievedEntity != null)
            {
                /*
                 * The classifications from every retrieved entity are harvested.
                 */
                accumulator.addEntity(retrievedEntity, metadataCollectionId);
            }
            else /* retrieving additional classifications */
            {
                getHomeClassifications(metadataCollection);
            }
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (EntityNotKnownException error)
        {
            accumulator.captureException(error);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }


    /**
     * Return the entity and accumulated classifications.  The returned entity summary may be from a full entity object or an entity proxy.
     *
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntitySummary getEntitySummary() throws InvalidParameterException,
                                                   RepositoryErrorException,
                                                   EntityNotKnownException,
                                                   UserNotAuthorizedException
    {
        EntitySummary latestEntity = accumulator.getResult();

        if (latestEntity != null)
        {
            return latestEntity;
        }

        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedInvalidParameterException();

        return null;
    }
}
