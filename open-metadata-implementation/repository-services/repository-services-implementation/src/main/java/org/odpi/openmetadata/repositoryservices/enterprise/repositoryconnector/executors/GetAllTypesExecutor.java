/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.TypesAccumulator;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * GetAllTypesExecutor provides the executor for the getAllType executor
 */
public class GetAllTypesExecutor extends CloneableRepositoryExecutorBase
{
    private final TypesAccumulator accumulator;

    /**
     * Simple public constructor
     *
     * @param userId calling user
     * @param methodName calling method
     * @param localMetadataCollectionId metadata collection id for the local repository
     * @param auditLog logging destination
     * @param repositoryValidator validation methods
     */
    public GetAllTypesExecutor(String                  userId,
                               String                  methodName,
                               String                  localMetadataCollectionId,
                               AuditLog                auditLog,
                               OMRSRepositoryValidator repositoryValidator)
    {
        this(userId,
             methodName,
             new TypesAccumulator(localMetadataCollectionId, auditLog, repositoryValidator));
    }


    /**
     * Common constructor for an all types executor.
     *
     * @param userId calling user
     * @param methodName calling method
     * @param accumulator store for the results
     */
    private GetAllTypesExecutor(String                   userId,
                                String                   methodName,
                                TypesAccumulator accumulator)
    {
        super(userId, methodName, accumulator);

        this.accumulator = accumulator;
    }


    /**
     * Return a clone of this executor with the same command parameters and accumulator instance.
     *
     * @return clone of this executor
     */
    public CloneableRepositoryExecutor getClone()
    {
        return new GetAllTypesExecutor(userId, methodName, accumulator);
    }


    /**
     * Perform the required action for the supplied repository.
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
             * Issue the request
             */
            accumulator.addTypeDefGallery(metadataCollection.getAllTypes(userId),
                                          metadataCollectionId);
        }
        catch (RepositoryErrorException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (UserNotAuthorizedException error)
        {
            accumulator.captureException(metadataCollectionId, error);
        }
        catch (Exception error)
        {
            accumulator.captureGenericException(methodName, metadataCollectionId, error);
        }

        return false;
    }


    /**
     * Return the result of the combined requests
     *
     * @return TypeDefGallery  List of different categories of TypeDefs.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefGallery getResults() throws InvalidParameterException,
                                              RepositoryErrorException,
                                              UserNotAuthorizedException
    {
        TypeDefGallery results = accumulator.getResults();

        if (results != null)
        {
           return results;
        }

        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedGenericException(methodName);

        return null;
    }
}
