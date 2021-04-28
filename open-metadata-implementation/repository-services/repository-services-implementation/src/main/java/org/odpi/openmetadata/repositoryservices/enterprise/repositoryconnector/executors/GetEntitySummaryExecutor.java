/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
    protected MaintenanceAccumulator      accumulator;
    protected String                      entityGUID;
    protected Map<String, Classification> allClassifications = new HashMap<>();

    protected boolean                     inPhaseOne         = true;


    private EntitySummary latestEntity = null;


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
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);

        this.entityGUID = entityGUID;
    }


    /**
     * Save the best classifications from all of the repositories.
     *
     * @param retrievedClassifications classifications from a repository
     */
    protected void saveClassifications(List<Classification> retrievedClassifications)
    {
        if (retrievedClassifications != null)
        {
            for (Classification entityClassification : retrievedClassifications)
            {
                if (entityClassification != null)
                {
                    Classification existingClassification = allClassifications.get(entityClassification.getName());

                    /*
                     * Ignore older versions of the classification
                     */
                    if ((existingClassification == null) ||
                                (existingClassification.getVersion() < entityClassification.getVersion()))
                    {
                        allClassifications.put(entityClassification.getName(), entityClassification);
                    }
                }
            }
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

            saveClassifications(homeClassifications);
        }
        catch (Exception error)
        {
            // ignore exceptions because the returned exceptions come from the retrieval of the entity.
        }
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
            if (inPhaseOne) /* retrieving entity */
            {
                EntitySummary retrievedEntity = metadataCollection.getEntitySummary(userId, entityGUID);

                if (retrievedEntity != null)
                {
                    /*
                     * The classifications from every retrieved entity are harvested.
                     */
                    saveClassifications(retrievedEntity.getClassifications());

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
            accumulator.captureGenericException(methodName,
                                                metadataCollectionId,
                                                error);
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
    public EntitySummary getEntitySummary() throws InvalidParameterException,
                                                   RepositoryErrorException,
                                                   EntityNotKnownException,
                                                   UserNotAuthorizedException
    {
        if (latestEntity != null)
        {
            if (allClassifications.isEmpty())
            {
                latestEntity.setClassifications(null);
            }
            else
            {
                latestEntity.setClassifications(new ArrayList<>(allClassifications.values()));
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
