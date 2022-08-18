/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.List;

/**
 * AddEntityExecutor provides the executor for the addEntity and addExternalEntity methods.
 */
public class AddEntityExecutor extends RepositoryExecutorBase
{
    private final String                 entityTypeGUID;
    private final InstanceProperties     initialProperties;
    private final List<Classification>   initialClassifications;
    private final InstanceStatus         initialStatus;
    private final MaintenanceAccumulator accumulator;

    private String                 externalSourceGUID = null;
    private String                 externalSourceName = null;
    private EntityDetail           newEntity          = null;


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public AddEntityExecutor(String               userId,
                             String               entityTypeGUID,
                             InstanceProperties   initialProperties,
                             List<Classification> initialClassifications,
                             InstanceStatus       initialStatus,
                             AuditLog             auditLog,
                             String               methodName)
    {
        super(userId, methodName);

        this.entityTypeGUID = entityTypeGUID;
        this.initialProperties = initialProperties;
        this.initialClassifications = initialClassifications;
        this.initialStatus = initialStatus;
        this.accumulator = new MaintenanceAccumulator(auditLog);
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public AddEntityExecutor(String               userId,
                             String               entityTypeGUID,
                             String               externalSourceGUID,
                             String               externalSourceName,
                             InstanceProperties   initialProperties,
                             List<Classification> initialClassifications,
                             InstanceStatus       initialStatus,
                             AuditLog             auditLog,
                             String               methodName)
    {
        super(userId, methodName);

        this.entityTypeGUID = entityTypeGUID;
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
        this.initialProperties = initialProperties;
        this.initialClassifications = initialClassifications;
        this.initialStatus = initialStatus;
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
            if (externalSourceGUID == null)
            {
                newEntity = metadataCollection.addEntity(userId,
                                                         entityTypeGUID,
                                                         initialProperties,
                                                         initialClassifications,
                                                         initialStatus);
            }
            else
            {
                newEntity = metadataCollection.addExternalEntity(userId,
                                                                 entityTypeGUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 initialProperties,
                                                                 initialClassifications,
                                                                 initialStatus);
            }
            if (newEntity != null)
            {
                result = true;
            }
        }
        catch (InvalidParameterException error)
        {
            accumulator.captureException(error);
        }
        catch (ClassificationErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (TypeErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (StatusNotSupportedException error)
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
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public EntityDetail getNewEntity() throws InvalidParameterException,
                                              RepositoryErrorException,
                                              TypeErrorException,
                                              PropertyErrorException,
                                              ClassificationErrorException,
                                              StatusNotSupportedException,
                                              FunctionNotSupportedException,
                                              UserNotAuthorizedException
    {
        if (newEntity != null)
        {
            return newEntity;
        }

        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedGenericException(super.methodName);
        accumulator.throwCapturedTypeErrorException();
        accumulator.throwCapturedClassificationErrorException();
        accumulator.throwCapturedPropertyErrorException();
        accumulator.throwCapturedStatusNotSupportedException();
        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedFunctionNotSupportedException();

        return null;
    }
}
