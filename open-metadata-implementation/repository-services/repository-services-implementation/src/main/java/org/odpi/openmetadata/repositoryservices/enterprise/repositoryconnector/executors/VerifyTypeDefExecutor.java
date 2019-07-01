/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;


/**
 * VerifyTypeDefExecutor provides the executor for the verifyTypeDef method.
 * This method returns confirmation that a specific type is supported somewhere in the cohort.
 */
public class VerifyTypeDefExecutor extends RepositoryExecutorBase
{
    private TypeDef                typeDef;
    private boolean                result      = false;
    private boolean                resultSet   = false;
    private MaintenanceAccumulator accumulator = new MaintenanceAccumulator();


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef  TypeDef structure describing the TypeDef to test.
     * @param methodName calling method
     */
    public VerifyTypeDefExecutor(String  userId,
                                 TypeDef typeDef,
                                 String  methodName)
    {
        super(userId, methodName);

        this.typeDef = typeDef;
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
            result = metadataCollection.verifyTypeDef(userId, typeDef);
            resultSet = true;
        }
        catch (TypeDefNotSupportedException error)
        {
            accumulator.captureException(error);
        }
        catch (InvalidTypeDefException error)
        {
            accumulator.captureException(error);
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (TypeDefConflictException error)
        {
            accumulator.captureException(error);
            resultSet = true;
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

        return resultSet;
    }


    /**
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public boolean getResult() throws InvalidParameterException,
                                      RepositoryErrorException,
                                      TypeDefNotSupportedException,
                                      TypeDefConflictException,
                                      InvalidTypeDefException,
                                      UserNotAuthorizedException
    {
        accumulator.throwCapturedTypeDefConflictException();

        if (resultSet)
        {
            return result;
        }

        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedThrowableException(super.methodName);
        accumulator.throwCapturedTypeDefNotSupportedException();
        accumulator.throwCapturedInvalidTypeDefException();
        accumulator.throwCapturedInvalidParameterException();

        return false;
    }
}
