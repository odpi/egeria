/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;

/**
 * GetRelationshipExecutor provides the executor for the isRelationshipKnown and getRelationship methods.
 */
public class GetRelationshipExecutor extends RepositoryExecutorBase
{
    private MaintenanceAccumulator accumulator;
    private String                 relationshipGUID;
    private boolean                allExceptions         = true;
    private Date                   asOfTime              = null;
    private Relationship           retrievedRelationship = null;



    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param relationshipGUID unique identifier (guid) for the relationship
     * @param allExceptions is the a isRelationshipKnown or getRelationship request
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetRelationshipExecutor(String    userId,
                                   String    relationshipGUID,
                                   boolean   allExceptions,
                                   AuditLog auditLog,
                                   String    methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);

        this.relationshipGUID = relationshipGUID;
        this.allExceptions = allExceptions;
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID unique identifier (guid) for the new entity's type.
     * @param asOfTime is this a historical query.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public GetRelationshipExecutor(String    userId,
                                   String    relationshipGUID,
                                   Date      asOfTime,
                                   AuditLog  auditLog,
                                   String    methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);

        this.relationshipGUID = relationshipGUID;
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
            if (asOfTime == null)
            {
                if (allExceptions)
                {
                    retrievedRelationship = metadataCollection.getRelationship(userId,
                                                                               relationshipGUID);
                }
                else
                {
                    retrievedRelationship = metadataCollection.isRelationshipKnown(userId,
                                                                                   relationshipGUID);
                }
            }
            else
            {
                retrievedRelationship = metadataCollection.getRelationship(userId,
                                                                           relationshipGUID,
                                                                           asOfTime);
            }
            if (retrievedRelationship != null)
            {
                result = true;
            }
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (RelationshipNotKnownException error)
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

        return result;
    }


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship isRelationshipKnown() throws InvalidParameterException,
                                                     RepositoryErrorException,
                                                     UserNotAuthorizedException
    {
        if (retrievedRelationship != null)
        {
            return retrievedRelationship;
        }

        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedInvalidParameterException();

        return null;
    }


    /**
     * Return a requested relationship.
     *
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship getRelationship() throws InvalidParameterException,
                                                 RepositoryErrorException,
                                                 RelationshipNotKnownException,
                                                 UserNotAuthorizedException
    {
        Relationship relationship = this.isRelationshipKnown();

        if (relationship != null)
        {
            return relationship;
        }

        accumulator.throwCapturedRelationshipNotKnownException();

        return null;
    }


    /**
     * Return a historical version of a relationship.
     *
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship getRelationshipHistory() throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        RelationshipNotKnownException,
                                                        FunctionNotSupportedException,
                                                        UserNotAuthorizedException
    {
        Relationship relationship = this.getRelationship();

        if (relationship != null)
        {
            return relationship;
        }

        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
