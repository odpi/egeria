/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

/**
 * ClassifyEntityProxyExecutor provides the executor for the classifyEntityProxy method.
 * This is a tricky request because the entity has been deleted so it is
 * not retrievable until restored.  Also, this is an optional function
 * so the repository where the entity has its home may not support restore.
 *
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class ClassifyEntityProxyExecutor extends RepositoryExecutorBase {

    private final MaintenanceAccumulator accumulator;
    private final String                 entityProxyGUID;
    private final String                 classificationName;
    private final InstanceProperties     classificationProperties;

    private EntityProxy                  updatedEntityProxy;

    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityProxyGUID unique identifier (guid) for the entity
     * @param classificationName String name for the classification
     * @param classificationProperties list of properties to set in the classification
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public ClassifyEntityProxyExecutor(String               userId,
                                       String               entityProxyGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties,
                                       AuditLog             auditLog,
                                       String               methodName)
    {
        super(userId, methodName);

        this.accumulator              = new MaintenanceAccumulator(auditLog);
        this.entityProxyGUID          = entityProxyGUID;
        this.classificationName       = classificationName;
        this.classificationProperties = classificationProperties;
    }


    /**
     * Perform the required action for the supplied repository.
     * Classify requests occur in the first repository that accepts the call.
     * Some repositories may produce exceptions. These exceptions are saved and will be returned if
     * there are no positive results from any repository.
     *
     * @param metadataCollectionId unique identifier for the metadata collection for the repository
     * @param metadataCollection metadata collection object for the repository
     *
     * @return boolean true means that the required results have been achieved
     */
    @Override
    public boolean issueRequestToRepository(String                 metadataCollectionId,
                                            OMRSMetadataCollection metadataCollection)
    {
        boolean result = false;

        try
        {
            updatedEntityProxy = metadataCollection
                    .classifyEntityProxy(userId, entityProxyGUID, classificationName, classificationProperties);
            result = true;
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (FunctionNotSupportedException error)
        {
            accumulator.captureException(error);
        }
        catch (EntityNotKnownException error)
        {
            accumulator.captureException(error);
        }
        catch (ClassificationErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (PropertyErrorException error)
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
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @return EntityDetail showing the updated entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public EntityProxy getUpdatedEntityProxy() throws InvalidParameterException,
                                                  RepositoryErrorException,
                                                  EntityNotKnownException,
                                                  ClassificationErrorException,
                                                  PropertyErrorException,
                                                  UserNotAuthorizedException,
                                                  FunctionNotSupportedException
    {
        if (updatedEntityProxy != null)
        {
            return updatedEntityProxy;
        }

        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(methodName);
        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedFunctionNotSupportedException();
        accumulator.throwCapturedClassificationErrorException();
        accumulator.throwCapturedPropertyErrorException();

        return null;
    }
}
