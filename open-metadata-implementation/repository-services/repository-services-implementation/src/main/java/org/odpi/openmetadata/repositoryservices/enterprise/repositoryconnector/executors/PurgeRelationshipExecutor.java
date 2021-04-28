/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * PurgeRelationshipExecutor provides the executor for the purgeRelationship method.
 * This is a tricky request because the relationship has been deleted so it is
 * not retrievable until restored.
 *
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class PurgeRelationshipExecutor extends RepositoryExecutorBase
{
    private MaintenanceAccumulator accumulator;
    private String                 relationshipGUID;
    private String                 typeDefGUID;
    private String                 typeDefName;
    private boolean                relationshipDeleted = false;



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param typeDefGUID unique identifier of the type of the relationship to purge
     * @param typeDefName unique name of the type of the relationship to purge
     * @param relationshipGUID String unique identifier (guid) for the relationship
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public PurgeRelationshipExecutor(String    userId,
                                     String    typeDefGUID,
                                     String    typeDefName,
                                     String    relationshipGUID,
                                     AuditLog  auditLog,
                                     String    methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);
        this.relationshipGUID = relationshipGUID;
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
            metadataCollection.purgeRelationship(userId, typeDefGUID, typeDefName, relationshipGUID);
            relationshipDeleted = true;
        }
        catch (InvalidParameterException error)
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

        return relationshipDeleted;
    }


    /**
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public void getResult() throws InvalidParameterException,
                                   RepositoryErrorException,
                                   RelationshipNotKnownException,
                                   RelationshipNotDeletedException,
                                   FunctionNotSupportedException,
                                   UserNotAuthorizedException
    {
        if (relationshipDeleted)
        {
            return;
        }

        accumulator.throwCapturedRelationshipNotDeletedException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(methodName);
        accumulator.throwCapturedRelationshipNotKnownException();
        accumulator.throwCapturedFunctionNotSupportedException();
        accumulator.throwCapturedInvalidParameterException();
    }
}
