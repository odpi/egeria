/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * RestoreRelationshipExecutor provides the executor for the restoreRelationship method.
 * This is a tricky request because the relationship has been deleted so it is
 * not retrievable until restored.  Also, this is an optional function
 * so the repository where the relationship has its home may not support restore.
 *
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class RestoreRelationshipExecutor extends RepositoryExecutorBase
{
    private final MaintenanceAccumulator accumulator;
    private final String                 relationshipGUID;

    private Relationship           restoredRelationship = null;



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param relationshipGUID unique identifier (guid) for the relationship
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public RestoreRelationshipExecutor(String   userId,
                                       String   relationshipGUID,
                                       AuditLog auditLog,
                                       String   methodName)
    {
        super(userId, methodName);

        this.relationshipGUID = relationshipGUID;
        this.accumulator      = new MaintenanceAccumulator(auditLog);
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
        boolean result = false;

        try
        {
            /*
             * Issue the request and return if it succeeds
             */
            restoredRelationship = metadataCollection.restoreRelationship(userId, relationshipGUID);
            result = true;
        }
        catch (FunctionNotSupportedException error)
        {
            accumulator.captureException(error);
        }
        catch (RelationshipNotKnownException error)
        {
            accumulator.captureException(error);
        }
        catch (RelationshipNotDeletedException error)
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
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship getRestoredRelationship() throws InvalidParameterException,
                                                         RepositoryErrorException,
                                                         RelationshipNotKnownException,
                                                         RelationshipNotDeletedException,
                                                         FunctionNotSupportedException,
                                                         UserNotAuthorizedException
    {
        if (restoredRelationship != null)
        {
            return restoredRelationship;
        }

        accumulator.throwCapturedRelationshipNotDeletedException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(methodName);
        accumulator.throwCapturedRelationshipNotKnownException();
        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
