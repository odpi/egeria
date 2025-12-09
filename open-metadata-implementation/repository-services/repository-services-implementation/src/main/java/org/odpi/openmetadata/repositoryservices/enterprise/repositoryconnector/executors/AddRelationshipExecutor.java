/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators.MaintenanceAccumulator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AddRelationshipExecutor provides the executor for the addEntity and addExternalEntity methods.
 */
public class AddRelationshipExecutor extends RepositoryExecutorBase
{
    private final MaintenanceAccumulator accumulator;

    private final String             relationshipTypeGUID;
    private final InstanceProperties initialProperties;
    private final EntityProxy        entityOneProxy;
    private final EntityProxy        entityTwoProxy;
    private final InstanceStatus     initialStatus;

    private String             externalSourceGUID = null;
    private String             externalSourceName = null;
    private Relationship       newRelationship    = null;

    private static final Logger log = LoggerFactory.getLogger(AddRelationshipExecutor.class);


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user
     * @param relationshipTypeGUID unique identifier (guid) for the new entity's type
     * @param initialProperties initial list of properties for the new relationship - null means no properties
     * @param entityOneProxy   the unique identifier of one of the entities that the relationship is connecting together
     * @param entityTwoProxy   the unique identifier of the other entity that the relationship is connecting together
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public AddRelationshipExecutor(String             userId,
                                   String             relationshipTypeGUID,
                                   InstanceProperties initialProperties,
                                   EntityProxy        entityOneProxy,
                                   EntityProxy        entityTwoProxy,
                                   InstanceStatus     initialStatus,
                                   AuditLog           auditLog,
                                   String             methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);

        this.relationshipTypeGUID = relationshipTypeGUID;
        this.initialProperties = initialProperties;
        this.entityOneProxy = entityOneProxy;
        this.entityTwoProxy = entityTwoProxy;
        this.initialStatus = initialStatus;
    }


    /**
     * Constructor takes the parameters for the request.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneProxy   the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoProxy   the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public AddRelationshipExecutor(String             userId,
                                   String             relationshipTypeGUID,
                                   String             externalSourceGUID,
                                   String             externalSourceName,
                                   InstanceProperties initialProperties,
                                   EntityProxy        entityOneProxy,
                                   EntityProxy        entityTwoProxy,
                                   InstanceStatus     initialStatus,
                                   AuditLog           auditLog,
                                   String             methodName)
    {
        super(userId, methodName);

        this.accumulator = new MaintenanceAccumulator(auditLog);

        this.relationshipTypeGUID = relationshipTypeGUID;
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
        this.initialProperties = initialProperties;
        this.entityOneProxy = entityOneProxy;
        this.entityTwoProxy = entityTwoProxy;
        this.initialStatus = initialStatus;
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

            String  entityOneGUID = entityOneProxy.getGUID();
            String  entityTwoGUID = entityTwoProxy.getGUID();

            if ((this.ensureEntityEndKnown(userId, entityOneGUID, entityOneProxy, metadataCollection)) &&
                (this.ensureEntityEndKnown(userId, entityTwoGUID, entityTwoProxy, metadataCollection)))
            {
                if (externalSourceGUID == null)
                {
                    newRelationship = metadataCollection.addRelationship(userId,
                                                                         relationshipTypeGUID,
                                                                         initialProperties,
                                                                         entityOneGUID,
                                                                         entityTwoGUID,
                                                                         initialStatus);
                }
                else
                {
                    newRelationship = metadataCollection.addExternalRelationship(userId,
                                                                                 relationshipTypeGUID,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 initialProperties,
                                                                                 entityOneGUID,
                                                                                 entityTwoGUID,
                                                                                 initialStatus);
                }
            }

            if (newRelationship != null)
            {
                result = true;
            }

        }
        catch (TypeErrorException error)
        {
            accumulator.captureException(error);
        }
        catch (EntityNotKnownException error)
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
        catch (FunctionNotSupportedException error)
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
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException   one of the parameters is invalid or null.
     * @throws RepositoryErrorException    there is a problem communicating with the metadata repository where
     *                                     the metadata collection is stored.
     * @throws TypeErrorException          the requested type is not known, or not supported in the metadata repository
     *                                     hosting the metadata collection.
     * @throws PropertyErrorException      one or more of the requested properties are not defined, or have different
     *                                     characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException     one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    public Relationship getNewRelationship() throws InvalidParameterException,
                                                    RepositoryErrorException,
                                                    TypeErrorException,
                                                    PropertyErrorException,
                                                    EntityNotKnownException,
                                                    StatusNotSupportedException,
                                                    FunctionNotSupportedException,
                                                    UserNotAuthorizedException
    {
        if (newRelationship != null)
        {
            return newRelationship;
        }

        accumulator.throwCapturedEntityNotKnownException();
        accumulator.throwCapturedInvalidParameterException();
        accumulator.throwCapturedRepositoryErrorException();
        accumulator.throwCapturedStatusNotSupportedException();
        accumulator.throwCapturedTypeErrorException();
        accumulator.throwCapturedPropertyErrorException();
        accumulator.throwCapturedUserNotAuthorizedException();
        accumulator.throwCapturedFunctionNotSupportedException();
        accumulator.throwCapturedGenericException(methodName);

        return null;
    }


    /**
     * Validate that the metadata collection supports the ends of the new relationship (and add if can).
     *
     * @param userId calling user
     * @param entityGUID guid of the entity.
     * @param entityProxy proxy to add if missing.
     * @param metadataCollection current repository.
     * @return boolean - true if entity is known.
     */
    private boolean ensureEntityEndKnown(String                 userId,
                                         String                 entityGUID,
                                         EntityProxy entityProxy,
                                         OMRSMetadataCollection metadataCollection)
    {
        try
        {
            metadataCollection.getEntitySummary(userId, entityGUID);
            return true;
        }
        catch (EntityNotKnownException error)
        {
            try
            {
                metadataCollection.addEntityProxy(userId, entityProxy);
                return true;
            }
            catch (Exception proxyError)
            {
                log.debug("Error from adding proxy: " + proxyError.getMessage());
            }
        }
        catch (Exception error)
        {
            log.debug("Error from querying entity: " + error.getMessage());
        }

        return false;
    }
}
