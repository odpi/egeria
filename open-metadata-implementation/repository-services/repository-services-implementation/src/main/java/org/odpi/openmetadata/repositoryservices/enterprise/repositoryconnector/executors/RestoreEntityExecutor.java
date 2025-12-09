/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * RestoreEntityExecutor provides the executor for the restoreEntity method.
 * This is a tricky request because the entity has been deleted so it is
 * not retrievable until restored.  Also, this is an optional function
 * so the repository where the entity has its home may not support restore.
 * <p>
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class RestoreEntityExecutor extends RepositoryExecutorBase
{
    private final MaintenanceAccumulator accumulator;
    private final String                 entityGUID;

    private EntityDetail           restoredEntity = null;



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityGUID unique identifier (guid) for the entity
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public RestoreEntityExecutor(String   userId,
                                 String   entityGUID,
                                 AuditLog auditLog,
                                 String   methodName)
    {
        super(userId, methodName);

        this.entityGUID = entityGUID;
        this.accumulator = new MaintenanceAccumulator(auditLog);

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
            restoredEntity = metadataCollection.restoreEntity(userId, entityGUID);
            result = true;
        }
        catch (FunctionNotSupportedException error)
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
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(error);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName,
                                                metadataCollectionId,
                                                error);
        }

        return result;
    }


    /**
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail getRestoredEntity() throws InvalidParameterException,
                                                   RepositoryErrorException,
                                                   EntityNotKnownException,
                                                   EntityNotDeletedException,
                                                   FunctionNotSupportedException,
                                                   UserNotAuthorizedException
    {
        if (restoredEntity != null)
        {
            return restoredEntity;
        }

        accumulator.throwCapturedEntityNotDeletedException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(methodName);
        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
