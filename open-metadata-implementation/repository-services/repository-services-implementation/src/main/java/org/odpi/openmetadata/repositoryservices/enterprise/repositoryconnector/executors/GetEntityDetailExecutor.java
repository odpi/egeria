/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.EntityDetailAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;

/**
 * GetEntityDetailExecutor provides the executor for the isEntityKnown and getEntityDetail methods.
 * The Entity is received from metadata repositories until the entity from the home repository is retrieved.
 * The classifications are saved from each retrieval and the latest version of the entity.
 *
 * When the home repository's version is retrieved, it switches to phase two where calls to getHomeClassifications are
 * made to the remaining repositories.  This is to pick up any stray classifications.
 *
 * The two phases are used to take advantage of an optimization in the OMRSRESTRepositoryConnector which remembers if a
 * remote repository does not support a call.
 * The getHomeClassifications methods are only supported by repositories that support the ability to home classifications on
 * entities from other repositories.  This means that the phase two calls will only go remote if the remote repository
 * supports the getHomeClassifications method.
 */
public class GetEntityDetailExecutor extends GetEntityExecutor
{
    private boolean      allExceptions = true;
    private Date         asOfTime      = null;

    private final EntityDetailAccumulator accumulator;


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the entity.
     * @param allExceptions is the a isEntityKnown or getEntityDetail request.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetEntityDetailExecutor(String   userId,
                                   String   entityGUID,
                                   boolean  allExceptions,
                                   AuditLog auditLog,
                                   String   methodName)
    {
        this(userId, entityGUID, allExceptions, new EntityDetailAccumulator(auditLog), methodName);
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the entity.
     * @param allExceptions is the a isEntityKnown or getEntityDetail request.
     * @param accumulator to use
     * @param methodName calling method
     */
    private GetEntityDetailExecutor(String                  userId,
                                    String                  entityGUID,
                                    boolean                 allExceptions,
                                    EntityDetailAccumulator accumulator,
                                    String                  methodName)
    {
        super(userId, entityGUID, accumulator, methodName);

        this.allExceptions = allExceptions;
        this.accumulator = accumulator;
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the  entity.
     * @param asOfTime is this a historical query.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetEntityDetailExecutor(String   userId,
                                   String   entityGUID,
                                   Date     asOfTime,
                                   AuditLog auditLog,
                                   String   methodName)
    {
        this(userId, entityGUID, asOfTime, new EntityDetailAccumulator(auditLog), methodName);
    }



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the  entity.
     * @param asOfTime is this a historical query.
     * @param accumulator to use
     * @param methodName calling method
     */
    private GetEntityDetailExecutor(String                  userId,
                                    String                  entityGUID,
                                    Date                    asOfTime,
                                    EntityDetailAccumulator accumulator,
                                    String                  methodName)
    {
        super(userId, entityGUID, accumulator, methodName);

        this.asOfTime = asOfTime;
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
             * Mark that this metadata collection has been visited.  It does not matter if the request fails or not since
             * it immediately calls for the home classifications if EntityProxyOnlyException is returned.  This means all information
             * from the repository is gathered in one go.
             */
            accumulator.addContributingMetadataCollection(metadataCollectionId);

            /*
             * Issue the request and return if it succeeds.
             */
            if (accumulator.isInPhaseOne()) /* retrieving entity */
            {
                EntityDetail retrievedEntity;
                if (asOfTime == null)
                {
                    if (allExceptions)
                    {
                        retrievedEntity = metadataCollection.getEntityDetail(userId, entityGUID);
                    }
                    else
                    {
                        retrievedEntity = metadataCollection.isEntityKnown(userId, entityGUID);
                    }
                }
                else
                {
                    retrievedEntity = metadataCollection.getEntityDetail(userId, entityGUID, asOfTime);
                }

                if (retrievedEntity != null)
                {
                    /*
                     * The classifications from every retrieved entity are also harvested.
                     */
                    accumulator.addEntity(retrievedEntity, metadataCollectionId);
                }
                else /* retrieving additional classifications */
                {
                    getHomeClassifications(metadataCollection);
                }
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
        catch (EntityProxyOnlyException error)
        {
            accumulator.captureException(error);
            getHomeClassifications(metadataCollection);
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
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail isEntityKnown() throws InvalidParameterException,
                                               RepositoryErrorException,
                                               UserNotAuthorizedException
    {
        EntityDetail result = accumulator.getResult();
        if (result != null)
        {
            return result;
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
    public EntityDetail getEntityDetail() throws InvalidParameterException,
                                                 RepositoryErrorException,
                                                 EntityNotKnownException,
                                                 EntityProxyOnlyException,
                                                 UserNotAuthorizedException
    {
        EntityDetail entity = this.isEntityKnown();

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
    public EntityDetail getEntityDetailHistory() throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        EntityNotKnownException,
                                                        EntityProxyOnlyException,
                                                        FunctionNotSupportedException,
                                                        UserNotAuthorizedException
    {
        EntityDetail entity = this.getEntityDetail();

        if (entity != null)
        {
            return entity;
        }

        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
