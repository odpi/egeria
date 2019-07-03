/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;

/**
 * GetEntityExecutor provides the executor for the isEntityKnown and getEntityDetail methods.
 */
public class GetEntityExecutor extends RepositoryExecutorBase
{
    private String                 entityGUID;
    private boolean                allExceptions   = true;
    private Date                   asOfTime        = null;
    private EntityDetail           retrievedEntity = null;
    private MaintenanceAccumulator accumulator     = new MaintenanceAccumulator();



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the entity.
     * @param allExceptions is the a isEntityKnown or getEntityDetail request.
     * @param methodName calling method
     */
    public GetEntityExecutor(String               userId,
                             String               entityGUID,
                             boolean              allExceptions,
                             String               methodName)
    {
        super(userId, methodName);

        this.entityGUID = entityGUID;
        this.allExceptions = allExceptions;
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the  entity.
     * @param asOfTime is this a historical query.
     * @param methodName calling method
     */
    public GetEntityExecutor(String               userId,
                             String               entityGUID,
                             Date                 asOfTime,
                             String               methodName)
    {
        super(userId, methodName);

        this.entityGUID = entityGUID;
        this.asOfTime = asOfTime;
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
            if (asOfTime == null)
            {
                if (allExceptions)
                {
                    retrievedEntity = metadataCollection.getEntityDetail(userId,
                                                                         entityGUID);
                }
                else
                {
                    retrievedEntity = metadataCollection.isEntityKnown(userId,
                                                                       entityGUID);
                }
            }
            else
            {
                retrievedEntity = metadataCollection.getEntityDetail(userId,
                                                                     entityGUID,
                                                                     asOfTime);
            }
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
        catch (EntityProxyOnlyException error)
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
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetail isEntityKnown() throws InvalidParameterException,
                                                RepositoryErrorException,
                                                UserNotAuthorizedException
    {
        if (retrievedEntity != null)
        {
            return retrievedEntity;
        }

        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedInvalidParameterException();

        return null;
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetail  getEntityDetail() throws InvalidParameterException,
                                                   RepositoryErrorException,
                                                   EntityNotKnownException,
                                                   EntityProxyOnlyException,
                                                   UserNotAuthorizedException
    {
        EntityDetail  entity = this.isEntityKnown();
        if (entity != null)
        {
            return entity;
        }

        accumulator.throwCapturedEntityProxyOnlyException();
        accumulator.throwCapturedEntityNotKnownException();

        return null;
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
     *
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public  EntityDetail  getEntityDetailHistory() throws InvalidParameterException,
                                                          RepositoryErrorException,
                                                          EntityNotKnownException,
                                                          EntityProxyOnlyException,
                                                          FunctionNotSupportedException,
                                                          UserNotAuthorizedException
    {
        EntityDetail  entity = this.getEntityDetail();

        if (entity != null)
        {
            return entity;
        }

        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
