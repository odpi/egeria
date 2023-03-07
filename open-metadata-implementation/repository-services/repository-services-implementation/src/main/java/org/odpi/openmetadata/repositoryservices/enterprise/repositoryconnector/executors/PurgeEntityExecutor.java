/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * PurgeEntityExecutor provides the executor for the purgeEntity method.
 * This is a tricky request because the entity has been deleted, so it is
 * not retrievable until restored.
 *
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class PurgeEntityExecutor extends RepositoryExecutorBase
{
    private final MaintenanceAccumulator accumulator;
    private final String                 entityGUID;
    private final String                 typeDefGUID;
    private final String                 typeDefName;

    private boolean                entityDeleted = false;



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param typeDefGUID unique identifier of the type of the entity to purge
     * @param typeDefName unique name of the type of the entity to purge
     * @param entityGUID String unique identifier (guid) for the entity
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public PurgeEntityExecutor(String    userId,
                               String    typeDefGUID,
                               String    typeDefName,
                               String    entityGUID,
                               AuditLog  auditLog,
                               String    methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);
        this.entityGUID = entityGUID;
        this.typeDefGUID = typeDefGUID;
        this.typeDefName = typeDefName;
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
            metadataCollection.purgeEntity(userId, typeDefGUID, typeDefName, entityGUID);
            entityDeleted = true;
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (EntityNotKnownException error)
        {
            accumulator.captureException(error);
        }
        catch (EntityNotDeletedException error)
        {
            accumulator.captureException(error);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (FunctionNotSupportedException error)
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

        return entityDeleted;
    }


    /**
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is not in DELETED status and so can not be purged
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void getResult() throws InvalidParameterException,
                                   RepositoryErrorException,
                                   EntityNotKnownException,
                                   EntityNotDeletedException,
                                   FunctionNotSupportedException,
                                   UserNotAuthorizedException
    {
        if (entityDeleted)
        {
            return;
        }

        accumulator.throwCapturedEntityNotDeletedException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(methodName);
        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedFunctionNotSupportedException();
        accumulator.throwCapturedInvalidParameterException();
    }
}
