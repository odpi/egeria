/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * RestoreEntityExecutor provides the executor for the restoreEntity method.
 * This is a tricky request because the entity has been deleted, so it is
 * not retrievable until restored.  Also, this is an optional function
 * so the repository where the entity has its home may not support restore.
 *
 * The only possible approach is to step through the repositories hoping that one
 * will respond positively.
 */
public class ClassifyEntityExecutor extends RepositoryExecutorBase
{
    private final MaintenanceAccumulator accumulator;
    private final String                 entityGUID;
    private final EntityProxy            entityProxy;
    private final String                 classificationName;
    private final String                 externalSourceGUID;
    private final String                 externalSourceName;
    private final ClassificationOrigin   classificationOrigin;
    private final String                 classificationOriginGUID;
    private final InstanceProperties     classificationProperties;

    private EntityDetail updatedEntity = null;
    private Classification addedClassification = null;


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param entityGUID unique identifier (guid) for the entity
     * @param entityProxy entity proxy to anchor classification
     * @param classificationName String name for the classification
     * @param externalSourceGUID unique identifier (guid) for the external source
     * @param externalSourceName unique name for the external source
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public ClassifyEntityExecutor(String               userId,
                                  String               entityGUID,
                                  EntityProxy          entityProxy,
                                  String               classificationName,
                                  String               externalSourceGUID,
                                  String               externalSourceName,
                                  ClassificationOrigin classificationOrigin,
                                  String               classificationOriginGUID,
                                  InstanceProperties   classificationProperties,
                                  AuditLog             auditLog,
                                  String               methodName)
    {
        super(userId, methodName);

        this.accumulator              = new MaintenanceAccumulator(auditLog);

        this.entityGUID               = entityGUID;
        this.entityProxy              = entityProxy;
        this.classificationName       = classificationName;
        this.externalSourceGUID       = externalSourceGUID;
        this.externalSourceName       = externalSourceName;
        this.classificationOrigin     = classificationOrigin;
        this.classificationOriginGUID = classificationOriginGUID;
        this.classificationProperties = classificationProperties;
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
             * Issue the request and return if it succeeds.  The simpler call is used if possible since it is an original method and
             * more repositories may support it.
             */
            if ((externalSourceGUID == null) && (classificationOrigin == ClassificationOrigin.ASSIGNED))
            {
                if (entityProxy == null)
                {
                    updatedEntity = metadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      classificationProperties);
                }
                else
                {
                    addedClassification = metadataCollection.classifyEntity(userId,
                                                                            entityProxy,
                                                                            classificationName,
                                                                            classificationProperties);
                }
            }
            else
            {
                if (entityProxy == null)
                {
                    updatedEntity = metadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      externalSourceGUID,
                                                                      externalSourceName,
                                                                      classificationOrigin,
                                                                      classificationOriginGUID,
                                                                      classificationProperties);
                }
                else
                {
                    addedClassification = metadataCollection.classifyEntity(userId,
                                                                            entityProxy,
                                                                            classificationName,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            classificationOrigin,
                                                                            classificationOriginGUID,
                                                                            classificationProperties);
                }
            }

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
     * @return EntityDetail showing the updated entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public EntityDetail getUpdatedEntity() throws InvalidParameterException,
                                                  RepositoryErrorException,
                                                  EntityNotKnownException,
                                                  ClassificationErrorException,
                                                  PropertyErrorException,
                                                  UserNotAuthorizedException,
                                                  FunctionNotSupportedException
    {
        if (updatedEntity != null)
        {
            return updatedEntity;
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

    /**
     * Return the result of the execution.  Hopefully this is a result - but may be an exception
     *
     * @return Classification newly added classification into the entity.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    public Classification getAddedClassification() throws InvalidParameterException,
                                                          RepositoryErrorException,
                                                          EntityNotKnownException,
                                                          ClassificationErrorException,
                                                          PropertyErrorException,
                                                          UserNotAuthorizedException,
                                                          FunctionNotSupportedException
    {
        if (addedClassification != null)
        {
            return addedClassification;
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
