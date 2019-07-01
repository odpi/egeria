/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;


/**
 * GetEntitySummaryExecutor provides the executor for the getEntitySummary method.
 */
public class GetEntitySummaryExecutor extends RepositoryExecutorBase
{
    private String                 entityGUID;
    private EntitySummary          retrievedEntity = null;
    private MaintenanceAccumulator accumulator     = new MaintenanceAccumulator();



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the entity.
     * @param methodName calling method
     */
    public GetEntitySummaryExecutor(String               userId,
                                    String               entityGUID,
                                    String               methodName)
    {
        super(userId, methodName);

        this.entityGUID = entityGUID;
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
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        boolean result = false;

        try
        {
            /*
             * Issue the request and return if it succeeds
             */
            retrievedEntity = metadataCollection.getEntitySummary(userId, entityGUID);

            if (retrievedEntity != null)
            {
                result = true;
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
        catch (Throwable error)
        {
            accumulator.captureGenericException(error);
        }

        return result;
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntitySummary getEntitySummary() throws InvalidParameterException,
                                                    RepositoryErrorException,
                                                    EntityNotKnownException,
                                                    UserNotAuthorizedException
    {
        if (retrievedEntity != null)
        {
            return retrievedEntity;
        }

        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedInvalidParameterException();

        return null;
    }
}
