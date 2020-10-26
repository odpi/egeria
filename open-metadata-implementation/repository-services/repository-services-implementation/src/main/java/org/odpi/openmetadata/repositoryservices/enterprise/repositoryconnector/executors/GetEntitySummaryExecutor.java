/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GetEntitySummaryExecutor provides the executor for the getEntitySummary method.
 */
public class GetEntitySummaryExecutor extends RepositoryExecutorBase
{
    private String                      entityGUID;

    private boolean                     inPhaseOne          = true;
    private EntitySummary               latestEntity        = null;
    private Map<String, Classification> homeClassifications = new HashMap<>();

    private MaintenanceAccumulator      accumulator         = new MaintenanceAccumulator();



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
        try
        {
            /*
             * Issue the request and return if it succeeds
             */
            if (inPhaseOne) /* retrieving entity */
            {
                EntitySummary retrievedEntity = metadataCollection.getEntitySummary(userId, entityGUID);

                if (retrievedEntity != null)
                {
                    /*
                     * The classifications from every retrieved entity are harvested.
                     */
                    if (retrievedEntity.getClassifications() != null)
                    {
                        for (Classification entityClassification : retrievedEntity.getClassifications())
                        {
                            if (entityClassification != null)
                            {
                                /*
                                 * Only home classifications are saved.
                                 */
                                if (metadataCollectionId.equals(entityClassification.getMetadataCollectionId()))
                                {
                                    homeClassifications.put(entityClassification.getName(), entityClassification);
                                }
                            }
                        }
                    }

                    if (metadataCollectionId.equals(retrievedEntity.getMetadataCollectionId()))
                    {
                        /*
                         * The home repository is found - assume it is the latest version - moving to phase two
                         */
                        latestEntity = retrievedEntity;
                        inPhaseOne = false;
                    }
                    else if (latestEntity == null)
                    {
                        latestEntity = retrievedEntity;
                    }
                    else
                    {
                        if (retrievedEntity.getVersion() > latestEntity.getVersion())
                        {
                            latestEntity = retrievedEntity;
                        }
                    }
                }
            }
            else /* retrieving additional classifications */
            {
                List<Classification> homeClassifications;

                homeClassifications = metadataCollection.getHomeClassifications(userId, entityGUID);

                /*
                 * Home classifications override any matching classifications stored in the entity.
                 */
                if (homeClassifications != null)
                {
                    for (Classification homeClassification : homeClassifications)
                    {
                        if (homeClassification != null)
                        {
                            this.homeClassifications.put(homeClassification.getName(), homeClassification);
                        }
                    }
                }
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

        return false;
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
        if (latestEntity != null)
        {
            if (homeClassifications.isEmpty())
            {
                latestEntity.setClassifications(null);
            }
            else
            {
                latestEntity.setClassifications(new ArrayList<>(homeClassifications.values()));
            }

            return latestEntity;
        }

        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedInvalidParameterException();

        return null;
    }
}
