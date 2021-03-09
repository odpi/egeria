/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RepositoryHandler issues common calls to the open metadata repository to retrieve and store metadata.  It converts the
 * repository service exceptions into access service exceptions.
 */
public class RepositoryHandler
{
    private RepositoryErrorHandler errorHandler;
    private OMRSMetadataCollection metadataCollection;
    private int                    maxPageSize;
    private AuditLog               auditLog;

    private static final Logger log = LoggerFactory.getLogger(RepositoryHandler.class);


    /**
     * Construct the basic handler with information needed to call the repository services and report any error.
     *
     * @param auditLog logging destination
     * @param errorHandler  generates error messages and exceptions
     * @param metadataCollection  access to the repository content.
     * @param maxPageSize maximum number of instances that can be returned on a single call
     */
    public RepositoryHandler(AuditLog                auditLog,
                             RepositoryErrorHandler  errorHandler,
                             OMRSMetadataCollection  metadataCollection,
                             int                     maxPageSize)
    {
        this.auditLog = auditLog;
        this.errorHandler = errorHandler;
        this.metadataCollection = metadataCollection;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Validate that the supplied GUID is for a real entity and map exceptions if not
     *
     * @param userId  user making the request.
     * @param guid  unique identifier of the entity.
     * @param entityTypeName expected type of asset.
     * @param methodName  name of method called.
     * @param guidParameterName name of parameter that passed the guid
     * @return retrieved entity
     * @throws InvalidParameterException entity not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntitySummary  validateEntityGUID(String                  userId,
                                             String                  guid,
                                             String                  entityTypeName,
                                             String                  methodName,
                                             String                  guidParameterName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String localMethodName = "validateEntityGUID";

        try
        {
            return metadataCollection.getEntitySummary(userId, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error,
                                             guid,
                                             entityTypeName,
                                             methodName,
                                             guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Validate that the supplied GUID is for a real entity.  Return null if not
     *
     * @param userId  user making the request.
     * @param guid  unique identifier of the entity.
     * @param entityTypeName expected type of asset.
     * @param methodName  name of method called.
     * @param guidParameterName name of parameter that passed the guid
     * @return retrieved entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntitySummary  isEntityKnown(String                  userId,
                                        String                  guid,
                                        String                  entityTypeName,
                                        String                  methodName,
                                        String                  guidParameterName) throws UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String localMethodName = "isEntityKnown";

        try
        {
            EntitySummary entity = metadataCollection.getEntitySummary(userId, guid);

            errorHandler.validateInstanceType(entity, entityTypeName, methodName, localMethodName);

            return entity;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            return null;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Create a new entity from an external source in the open metadata repository with the specified instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties properties for the entity
     * @param instanceStatus initial status (needs to be valid for type)
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public String  createExternalEntity(String                  userId,
                                        String                  entityTypeGUID,
                                        String                  entityTypeName,
                                        String                  externalSourceGUID,
                                        String                  externalSourceName,
                                        InstanceProperties      properties,
                                        InstanceStatus          instanceStatus,
                                        String                  methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 externalSourceGUID,
                                 externalSourceName,
                                 properties,
                                 null,
                                 instanceStatus,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the ACTIVE instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public String  createEntity(String                  userId,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                InstanceProperties      properties,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 null,
                                 null,
                                 properties,
                                 null,
                                 InstanceStatus.ACTIVE,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the ACTIVE instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties properties for the entity
     * @param initialClassifications list of classifications to attach to the entity
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  createEntity(String                  userId,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                String                  externalSourceGUID,
                                String                  externalSourceName,
                                InstanceProperties      properties,
                                List<Classification>    initialClassifications,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 externalSourceGUID,
                                 externalSourceName,
                                 properties,
                                 initialClassifications,
                                 InstanceStatus.ACTIVE,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the ACTIVE instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties properties for the entity
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  createEntity(String                  userId,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                String                  externalSourceGUID,
                                String                  externalSourceName,
                                InstanceProperties      properties,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 externalSourceGUID,
                                 externalSourceName,
                                 properties,
                                 null,
                                 InstanceStatus.ACTIVE,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the specified instance status. The setting of externalSourceGUID determines
     * whether a local or a remote entity is created.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties properties for the entity
     * @param instanceStatus initial status (needs to be valid for type)
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  createEntity(String                  userId,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                String                  externalSourceGUID,
                                String                  externalSourceName,
                                InstanceProperties      properties,
                                InstanceStatus          instanceStatus,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 externalSourceGUID,
                                 externalSourceName,
                                 properties,
                                 null,
                                 instanceStatus,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the specified instance status.  The setting of externalSourceGUID determines
     * whether a local or a remote entity is created.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties properties for the entity
     * @param initialClassifications list of classifications for the first version of this entity.
     * @param instanceStatus initial status (needs to be valid for type)
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String  createEntity(String                  userId,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                String                  externalSourceGUID,
                                String                  externalSourceName,
                                InstanceProperties      properties,
                                List<Classification>    initialClassifications,
                                InstanceStatus          instanceStatus,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String localMethodName = "createEntity";

        try
        {
            EntityDetail newEntity;
            if (externalSourceGUID == null)
            {
                newEntity = metadataCollection.addEntity(userId,
                                                         entityTypeGUID,
                                                         properties,
                                                         initialClassifications,
                                                         instanceStatus);
            }
            else
            {
                newEntity = metadataCollection.addExternalEntity(userId,
                                                                 entityTypeGUID,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 properties,
                                                                 initialClassifications,
                                                                 instanceStatus);
            }

            if (newEntity != null)
            {
                return newEntity.getGUID();
            }

            errorHandler.handleNoEntity(entityTypeGUID,
                                        entityTypeName,
                                        properties,
                                        methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Remove an entity attached to an starting. There should be only one instance
     * of this relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param startingEntityGUID unique identifier of the starting entity
     * @param startingEntityTypeName name of starting entity's type
     * @param startingRelationshipTypeGUID unique identifier for the relationship's type
     * @param startingRelationshipTypeName unique name for the relationship's type
     * @param startingRelationshipProperties properties from relationship
     * @param attachedEntityTypeGUID unique identifier for the attached entity's type
     * @param attachedEntityTypeName name of the attached entity's type
     * @param attachedEntityProperties properties of entity
     * @param methodName name of calling method
     *
     * @return attached entity GUID
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing the property server
     */
    public String addUniqueAttachedEntityToElement(String                  userId,
                                                   String                  externalSourceGUID,
                                                   String                  externalSourceName,
                                                   String                  startingEntityGUID,
                                                   String                  startingEntityTypeName,
                                                   String                  startingRelationshipTypeGUID,
                                                   String                  startingRelationshipTypeName,
                                                   InstanceProperties      startingRelationshipProperties,
                                                   String                  attachedEntityTypeGUID,
                                                   String                  attachedEntityTypeName,
                                                   InstanceProperties      attachedEntityProperties,
                                                   String                  methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String guidParameterName = "elementEntityGUID";

        this.validateEntityGUID(userId,
                                startingEntityGUID,
                                startingEntityTypeName,
                                methodName,
                                guidParameterName);

        String  attachedEntityGUID = this.createEntity(userId,
                                                       attachedEntityTypeGUID,
                                                       attachedEntityTypeName,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       attachedEntityProperties,
                                                       methodName);

        if (attachedEntityGUID != null)
        {
            this.createRelationship(userId,
                                    startingRelationshipTypeGUID,
                                    externalSourceGUID,
                                    externalSourceName,
                                    startingEntityGUID,
                                    attachedEntityGUID,
                                    startingRelationshipProperties,
                                    methodName);
        }

        return attachedEntityGUID;
    }



    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param updateProperties properties for the entity
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail  updateEntityProperties(String                  userId,
                                                String                  entityGUID,
                                                String                  entityTypeGUID,
                                                String                  entityTypeName,
                                                InstanceProperties      updateProperties,
                                                String                  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String guidParameterName = "entityGUID";

        EntityDetail originalEntity = this.getEntityByGUID(userId,
                                                           entityGUID,
                                                           guidParameterName,
                                                           entityTypeName,
                                                           methodName);

        return updateEntityProperties(userId,
                                      entityGUID,
                                      originalEntity,
                                      entityTypeGUID,
                                      entityTypeName,
                                      updateProperties,
                                      methodName);
    }



    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param updateProperties properties for the entity
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail  updateEntityProperties(String                  userId,
                                                String                  externalSourceGUID,
                                                String                  externalSourceName,
                                                String                  entityGUID,
                                                String                  entityTypeGUID,
                                                String                  entityTypeName,
                                                InstanceProperties      updateProperties,
                                                String                  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String guidParameterName = "entityGUID";

        EntityDetail originalEntity = this.getEntityByGUID(userId,
                                                           entityGUID,
                                                           guidParameterName,
                                                           entityTypeName,
                                                           methodName);

        return updateEntityProperties(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      entityGUID,
                                      originalEntity,
                                      entityTypeGUID,
                                      entityTypeName,
                                      updateProperties,
                                      methodName);
    }


    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param originalEntity entity retrieved from repository
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param newProperties properties for the entity
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail  updateEntityProperties(String                  userId,
                                                String                  entityGUID,
                                                EntityDetail            originalEntity,
                                                String                  entityTypeGUID,
                                                String                  entityTypeName,
                                                InstanceProperties      newProperties,
                                                String                  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String localMethodName = "updateEntityProperties";

        if (originalEntity != null)
        {
            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (originalEntity.getProperties() == null))
            {
                return originalEntity;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(originalEntity.getProperties())))
            {
                return originalEntity;
            }

            try
            {
                EntityDetail newEntity = metadataCollection.updateEntityProperties(userId, entityGUID, newProperties);
                if (newEntity == null)
                {
                    errorHandler.handleNoEntity(entityTypeGUID,
                                                entityTypeName,
                                                newProperties,
                                                methodName);
                }

                return newEntity;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Throwable error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }

        return null;
    }


    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param originalEntity entity retrieved from repository
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param newProperties properties for the entity
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail  updateEntityProperties(String                  userId,
                                                String                  externalSourceGUID,
                                                String                  externalSourceName,
                                                String                  entityGUID,
                                                EntityDetail            originalEntity,
                                                String                  entityTypeGUID,
                                                String                  entityTypeName,
                                                InstanceProperties      newProperties,
                                                String                  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String localMethodName = "updateEntityProperties";

        if (originalEntity != null)
        {
            errorHandler.validateProvenance(userId,
                                            originalEntity,
                                            entityGUID,
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (originalEntity.getProperties() == null))
            {
                return originalEntity;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(originalEntity.getProperties())))
            {
                return originalEntity;
            }

            try
            {
                EntityDetail newEntity = metadataCollection.updateEntityProperties(userId, entityGUID, newProperties);
                if (newEntity == null)
                {
                    errorHandler.handleNoEntity(entityTypeGUID,
                                                entityTypeName,
                                                newProperties,
                                                methodName);
                }

                return newEntity;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Throwable error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }

        return null;
    }


    /**
     * Update just the specific list of classifications on an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param newClassifications classifications for the entity
     * @param methodName name of calling method
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateSelectedEntityClassifications(String                  userId,
                                                    String                  externalSourceGUID,
                                                    String                  externalSourceName,
                                                    String                  entityGUID,
                                                    String                  entityTypeGUID,
                                                    String                  entityTypeName,
                                                    List<Classification>    newClassifications,
                                                    String                  methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String guidParameterName = "entityGUID";

        /*
         * The current entity is needed to work out which classifications are new, updated or deleted
         */
        EntityDetail entity = this.getEntityByGUID(userId, entityGUID, guidParameterName, entityTypeName, methodName);

        if ((entity != null) && (newClassifications != null) && (! newClassifications.isEmpty()))
        {
            if ((entity.getClassifications() == null) || (entity.getClassifications().isEmpty()))
            {
                updateEntityClassifications(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            entityGUID,
                                            entity.getClassifications(),
                                            newClassifications,
                                            methodName);
            }
            else
            {
                Map<String, Classification> classificationMap = new HashMap<>();

                for (Classification classification : entity.getClassifications())
                {
                    if ((classification != null) && (classification.getName() != null))
                    {
                        classificationMap.put(classification.getName(), classification);
                    }
                }

                for (Classification classification : newClassifications)
                {
                    if ((classification != null) && (classification.getName() != null))
                    {
                        classificationMap.put(classification.getName(), classification);
                    }
                }

                List<Classification> fullClassificationList = new ArrayList<>(classificationMap.values());

                updateEntityClassifications(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            entityGUID,
                                            fullClassificationList,
                                            newClassifications,
                                            methodName);
            }
        }
    }


    /**
     * Update an existing entity in the open metadata repository.  Both the properties and the classifications are updated
     * to the supplied values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID entity to update
     * @param existingEntityClassifications existing classifications retrieved from the repository
     * @param classifications new/updated classifications for the entity
     * @param methodName name of calling method
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void updateEntityClassifications(String                  userId,
                                             String                  externalSourceGUID,
                                             String                  externalSourceName,
                                             String                  entityGUID,
                                             List<Classification>    existingEntityClassifications,
                                             List<Classification>    classifications,
                                             String                  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String localMethodName = "updateEntityClassifications";

        try
        {
            if ((existingEntityClassifications == null) || (existingEntityClassifications.isEmpty()))
            {
                if ((classifications != null) && (! classifications.isEmpty()))
                {
                    /*
                     * All of the classifications are new
                     */
                    for (Classification  newClassification : classifications)
                    {
                        if (newClassification != null)
                        {
                            this.classifyEntity(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entityGUID,
                                                null,
                                                newClassification.getName(),
                                                newClassification.getClassificationOrigin(),
                                                newClassification.getClassificationOriginGUID(),
                                                newClassification.getProperties(),
                                                methodName);
                        }
                    }
                }

                /*
                 * If both the existing and new classifications are null then nothing to do.
                 */
            }
            else if ((classifications == null) || (classifications.isEmpty()))
            {
                /*
                 * All of the classifications are deleted
                 */
                for (Classification  obsoleteClassification : existingEntityClassifications)
                {
                    if (obsoleteClassification != null)
                    {
                        this.declassifyEntity(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              entityGUID,
                                              null,
                                              obsoleteClassification.getName(),
                                              obsoleteClassification,
                                              methodName);
                    }
                }
            }
            else /* there are existing classifications as well as new ones */
            {
                Map<String, Classification> entityClassificationMap = new HashMap<>();

                for (Classification entityClassification : existingEntityClassifications)
                {
                    if ((entityClassification != null) && (entityClassification.getName() != null))
                    {
                        entityClassificationMap.put(entityClassification.getName(), entityClassification);
                    }
                }

                for (Classification classification : classifications)
                {
                    if ((classification != null) && (classification.getName() != null))
                    {
                        Classification matchingEntityClassification = entityClassificationMap.get(classification.getName());

                        if (matchingEntityClassification == null)
                        {
                            this.classifyEntity(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entityGUID,
                                                null,
                                                classification.getName(),
                                                classification.getClassificationOrigin(),
                                                classification.getClassificationOriginGUID(),
                                                classification.getProperties(),
                                                methodName);
                        }
                        else /* new and old match */
                        {
                            if (classification.getProperties() == null)
                            {
                                if (matchingEntityClassification.getProperties() != null)
                                {
                                    this.reclassifyEntity(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          entityGUID,
                                                          null,
                                                          classification.getName(),
                                                          matchingEntityClassification,
                                                          null, // clears all properties
                                                          methodName);
                                }
                            }
                            else if (!classification.getProperties().equals(matchingEntityClassification.getProperties()))
                            {
                                this.reclassifyEntity(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      entityGUID,
                                                      null,
                                                      classification.getName(),
                                                      matchingEntityClassification,
                                                      classification.getProperties(),
                                                      methodName);
                            }

                            entityClassificationMap.remove(classification.getName());
                        }
                    }

                    /*
                     * Whatever is left in the map needs to be removed
                     */
                    for (String entityClassificationName : entityClassificationMap.keySet())
                    {
                        if (entityClassificationName != null && classification != null)
                        {
                            this.declassifyEntity(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  entityGUID,
                                                  null,
                                                  classification.getName(),
                                                  entityClassificationMap.get((entityClassificationName)),
                                                  methodName);
                        }
                    }
                }
            }
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }

    /**
     * Update an existing entity in the open metadata repository.  Both the properties and the classifications are updated
     * to the supplied values.
     *
     * @param userId calling user
     * @param entityGUID entity to update
     * @param existingEntityClassifications existing classifications
     * @param classifications classifications for the entity
     * @param methodName name of calling method
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    private void updateEntityClassifications(String                  userId,
                                             String                  entityGUID,
                                             List<Classification>    existingEntityClassifications,
                                             List<Classification>    classifications,
                                             String                  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String localMethodName = "updateEntityClassifications";

        try
        {
            if ((existingEntityClassifications == null) || (existingEntityClassifications.isEmpty()))
            {
                if ((classifications != null) && (! classifications.isEmpty()))
                {
                    /*
                     * All of the classifications are new
                     */
                    for (Classification  newClassification : classifications)
                    {
                        if (newClassification != null)
                        {
                            this.classifyEntity(userId,
                                                entityGUID,
                                                newClassification.getName(),
                                                newClassification.getProperties(),
                                                methodName);
                        }
                    }
                }

                /*
                 * If both the existing and new classifications are null then nothing to do.
                 */
            }
            else if ((classifications == null) || (classifications.isEmpty()))
            {
                /*
                 * All of the classifications are deleted
                 */
                for (Classification  obsoleteClassification : existingEntityClassifications)
                {
                    if (obsoleteClassification != null)
                    {
                        this.declassifyEntity(userId,
                                              entityGUID,
                                              obsoleteClassification.getName(),
                                              obsoleteClassification,
                                              methodName);
                    }
                }
            }
            else /* there are existing classifications as well as new ones */
            {
                Map<String, Classification> entityClassificationMap = new HashMap<>();

                for (Classification entityClassification : existingEntityClassifications)
                {
                    if ((entityClassification != null) && (entityClassification.getName() != null))
                    {
                        entityClassificationMap.put(entityClassification.getName(), entityClassification);
                    }
                }

                for (Classification classification : classifications)
                {
                    if ((classification != null) && (classification.getName() != null))
                    {
                        Classification matchingEntityClassification = entityClassificationMap.get(classification.getName());

                        if (matchingEntityClassification == null)
                        {
                            this.classifyEntity(userId,
                                                entityGUID,
                                                classification.getName(),
                                                classification.getProperties(),
                                                methodName);
                        }
                        else /* new and old match */
                        {
                            if (classification.getProperties() == null)
                            {
                                if (matchingEntityClassification.getProperties() != null)
                                {
                                    this.reclassifyEntity(userId,
                                                          entityGUID,
                                                          classification.getName(),
                                                          matchingEntityClassification,
                                                          null, // clears all properties
                                                          methodName);
                                }
                            }
                            else if (!classification.getProperties().equals(matchingEntityClassification.getProperties()))
                            {
                                this.reclassifyEntity(userId,
                                                      entityGUID,
                                                      classification.getName(),
                                                      matchingEntityClassification,
                                                      classification.getProperties(),
                                                      methodName);
                            }

                            entityClassificationMap.remove(classification.getName());
                        }
                    }

                    /*
                     * Whatever is left in the map needs to be removed
                     */
                    for (String entityClassificationName : entityClassificationMap.keySet())
                    {
                        if (entityClassificationName != null && classification != null)
                        {
                            this.declassifyEntity(userId,
                                                  entityGUID,
                                                  classification.getName(),
                                                  entityClassificationMap.get(entityClassificationName),
                                                  methodName);
                        }
                    }
                }
            }
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Retrieve a specific classification for an entity. Null is returned if the entity is not classified
     * in this way.
     *
     * @param userId calling user
     * @param entityGUID unique identity of the entity - if this entity is not known then an exception occurs
     * @param classificationName name of the classification
     * @param methodName calling method
     * @return located classification or null if not found
     * @throws InvalidParameterException invalid parameter (probably the guid)
     * @throws UserNotAuthorizedException calling user does not have appropriate permissions
     * @throws PropertyServerException internal error
     */
    private Classification getClassificationForEntity(String userId,
                                                      String entityGUID,
                                                      String classificationName,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String entityGUIDParameterName = "entityGUID";

        EntityDetail entity = this.getEntityByGUID(userId,
                                                   entityGUID,
                                                   entityGUIDParameterName,
                                                   null, // any type allowed
                                                   methodName);


        if (entity != null)
        {
            if ((classificationName != null) && (entity.getClassifications() != null))
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        if (classificationName.equals(classification.getName()))
                        {
                            return classification;
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Update an existing entity in the open metadata repository.  Both the properties and the classifications are updated
     * to the supplied values.
     *
     * @param userId calling user
     * @param entityGUID unique identifier entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param classifications classifications for entity
     * @param methodName name of calling method
     *
     * @return returned entity containing the update
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail updateEntity(String                  userId,
                                     String                  entityGUID,
                                     String                  entityTypeGUID,
                                     String                  entityTypeName,
                                     InstanceProperties      properties,
                                     List<Classification>    classifications,
                                     String                  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        EntityDetail entity = this.updateEntityProperties(userId,
                                                          entityGUID,
                                                          entityTypeGUID,
                                                          entityTypeName,
                                                          properties,
                                                          methodName);

        this.updateEntityClassifications(userId,
                                         entity.getGUID(),
                                         entity.getClassifications(),
                                         classifications,
                                         methodName);

        return entity;
    }


    /**
     * Update an existing entity in the open metadata repository.  Both the properties and the classifications are updated
     * to the supplied values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param classifications classifications for entity
     * @param methodName name of calling method
     *
     * @return returned entity containing the update
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail updateEntity(String                  userId,
                                     String                  externalSourceGUID,
                                     String                  externalSourceName,
                                     String                  entityGUID,
                                     String                  entityTypeGUID,
                                     String                  entityTypeName,
                                     InstanceProperties      properties,
                                     List<Classification>    classifications,
                                     String                  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        EntityDetail entity = this.updateEntityProperties(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          entityGUID,
                                                          entityTypeGUID,
                                                          entityTypeName,
                                                          properties,
                                                          methodName);

        this.updateEntityClassifications(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         entity.getGUID(),
                                         entity.getClassifications(),
                                         classifications,
                                         methodName);

        return entity;
    }


    /**
     * Update an existing entity in the open metadata repository. The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instances
     * metadata collection identifiers.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityHeader unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateEntityProperties(String                  userId,
                                       String                  externalSourceGUID,
                                       String                  externalSourceName,
                                       InstanceHeader          entityHeader,
                                       String                  entityTypeGUID,
                                       String                  entityTypeName,
                                       InstanceProperties      properties,
                                       String                  methodName) throws UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String localMethodName = "updateEntityProperties";


        try
        {
            errorHandler.validateProvenance(userId,
                                            entityHeader,
                                            entityHeader.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            EntityDetail newEntity = metadataCollection.updateEntityProperties(userId,
                                                                               entityHeader.getGUID(),
                                                                               properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID,
                                            entityTypeName,
                                            properties,
                                            methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }



    /**
     * Update an existing entity status in the open metadata repository.
     * This method is deprecated because it is not possible to validate the metadata provenance without
     * the external source identifiers (if any)
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param instanceStatus initial status (needs to be valid for type)
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void    updateEntityStatus(String                  userId,
                                      String                  entityGUID,
                                      String                  entityTypeGUID,
                                      String                  entityTypeName,
                                      InstanceStatus          instanceStatus,
                                      String                  methodName) throws UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String localMethodName = "updateEntityStatus(deprecated)";

        try
        {
            EntityDetail newEntity = metadataCollection.updateEntityStatus(userId,
                                                                           entityGUID,
                                                                           instanceStatus);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID,
                                            entityTypeName,
                                            null,
                                            methodName);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update an existing entity status in the open metadata repository.  The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instances
     * metadata collection identifiers.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param instanceStatus initial status (needs to be valid for type)
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException problem with the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    updateEntityStatus(String                  userId,
                                      String                  externalSourceGUID,
                                      String                  externalSourceName,
                                      String                  entityGUID,
                                      String                  entityTypeGUID,
                                      String                  entityTypeName,
                                      InstanceStatus          instanceStatus,
                                      String                  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String guidParameterName = "entityGUID";
        final String localMethodName = "updateEntityStatus";

        EntityDetail entity = this.getEntityByGUID(userId, entityGUID, guidParameterName, entityTypeName, methodName);

        try
        {
            errorHandler.validateProvenance(userId,
                                            entity,
                                            entityGUID,
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            EntityDetail newEntity = metadataCollection.updateEntityStatus(userId,
                                                                           entityGUID,
                                                                           instanceStatus);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID,
                                            entityTypeName,
                                            null,
                                            methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update the properties of an existing entity in the open metadata repository.
     * This method is deprecated because it does not pass the external source identifiers and so the
     * metadata provenance can not be verified.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail    updateEntity(String                  userId,
                                        String                  entityGUID,
                                        String                  entityTypeGUID,
                                        String                  entityTypeName,
                                        InstanceProperties      properties,
                                        String                  methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String localMethodName = "updateEntity(deprecated)";

        try
        {
            EntityDetail newEntity = metadataCollection.updateEntityProperties(userId,
                                                                               entityGUID,
                                                                               properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID,
                                            entityTypeName,
                                            properties,
                                            methodName);
            }

            return newEntity;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Add a new classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationName name of the classification's type
     * @param properties properties for the classification
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail    classifyEntity(String                  userId,
                                          String                  entityGUID,
                                          String                  classificationName,
                                          InstanceProperties      properties,
                                          String                  methodName) throws UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName = "classifyEntity(deprecated)";

        try
        {
            EntityDetail newEntity = metadataCollection.classifyEntity(userId,
                                                                       entityGUID,
                                                                       classificationName,
                                                                       properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntityForClassification(entityGUID,
                                                             null,
                                                             classificationName,
                                                             properties,
                                                             methodName);
            }
            else
            {
                return newEntity;
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Add a new classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID identifier of the classification's type
     * @param classificationTypeName name of the classification's type
     * @param properties properties for the classification
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public EntityDetail    classifyEntity(String                  userId,
                                          String                  entityGUID,
                                          String                  classificationTypeGUID,
                                          String                  classificationTypeName,
                                          InstanceProperties      properties,
                                          String                  methodName) throws UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName = "classifyEntity(deprecated)";

        try
        {
            EntityDetail newEntity = metadataCollection.classifyEntity(userId,
                                                                       entityGUID,
                                                                       classificationTypeName,
                                                                       properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntityForClassification(entityGUID,
                                                             classificationTypeGUID,
                                                             classificationTypeName,
                                                             properties,
                                                             methodName);
            }
            else
            {
                return newEntity;
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Add a new classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param classificationOrigin is this classification assigned or propagated?
     * @param classificationOriginGUID which entity did a propagated classification originate from?
     * @param properties properties for the classification
     * @param methodName name of calling method
     * @return updated entity
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail    classifyEntity(String                  userId,
                                          String                  externalSourceGUID,
                                          String                  externalSourceName,
                                          String                  entityGUID,
                                          String                  classificationTypeGUID,
                                          String                  classificationTypeName,
                                          ClassificationOrigin    classificationOrigin,
                                          String                  classificationOriginGUID,
                                          InstanceProperties      properties,
                                          String                  methodName) throws UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName = "classifyEntity";

        try
        {
            EntityDetail newEntity = metadataCollection.classifyEntity(userId,
                                                                       entityGUID,
                                                                       classificationTypeName,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       classificationOrigin,
                                                                       classificationOriginGUID,
                                                                       properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntityForClassification(entityGUID,
                                                             classificationTypeGUID,
                                                             classificationTypeName,
                                                             properties,
                                                             methodName);
            }
            else
            {
                return newEntity;
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Update the properties of an existing classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeName name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param newProperties properties for the classification
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException invalid parameters passed - probably GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void    reclassifyEntity(String                  userId,
                                    String                  entityGUID,
                                    String                  classificationTypeName,
                                    InstanceAuditHeader     existingClassificationHeader,
                                    InstanceProperties      newProperties,
                                    String                  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String localMethodName = "reclassifyEntity";

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId, entityGUID, classificationTypeName, methodName);
        }

        /*
         * OK to reclassify.
         */
        if (auditHeader != null)
        {
            try
            {
                EntityDetail newEntity = metadataCollection.updateEntityClassification(userId,
                                                                                       entityGUID,
                                                                                       classificationTypeName,
                                                                                       newProperties);

                if (newEntity == null)
                {
                    errorHandler.handleNoEntityForClassification(entityGUID,
                                                                 null,
                                                                 classificationTypeName,
                                                                 newProperties,
                                                                 methodName);
                }
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Throwable error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }
        else /* should be a classify */
        {
            this.classifyEntity(userId,
                                entityGUID,
                                classificationTypeName,
                                newProperties,
                                methodName);
        }
    }


    /**
     * Update the properties of an existing classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param newProperties properties for the classification
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException invalid parameters passed - probably GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    reclassifyEntity(String                  userId,
                                    String                  externalSourceGUID,
                                    String                  externalSourceName,
                                    String                  entityGUID,
                                    String                  classificationTypeGUID,
                                    String                  classificationTypeName,
                                    InstanceAuditHeader     existingClassificationHeader,
                                    InstanceProperties      newProperties,
                                    String                  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String localMethodName = "reclassifyEntity";

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        /*
         * The audit header is supplied if the caller has already lloked up the entity/classification
         */
        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId, entityGUID, classificationTypeName, methodName);
        }

        if (auditHeader != null)
        {
            /*
             * OK to reclassify the classification is currently attached.
             */
            try
            {
                errorHandler.validateProvenance(userId,
                                                existingClassificationHeader,
                                                entityGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);

                EntityDetail newEntity = metadataCollection.updateEntityClassification(userId,
                                                                                       entityGUID,
                                                                                       classificationTypeName,
                                                                                       newProperties);

                if (newEntity == null)
                {
                    errorHandler.handleNoEntityForClassification(entityGUID,
                                                                 classificationTypeGUID,
                                                                 classificationTypeName,
                                                                 newProperties,
                                                                 methodName);
                }
            }
            catch (UserNotAuthorizedException error)
            {
                /*
                 * This comes from validateProvenance.  The call to validate provenance is in the try..catch
                 * in case the caller has passed bad parameters.
                 */
                throw error;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Throwable error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }
        else /* should be a classify */
        {
            this.classifyEntity(userId,
                                externalSourceGUID,
                                externalSourceName,
                                entityGUID,
                                classificationTypeGUID,
                                classificationTypeName,
                                ClassificationOrigin.ASSIGNED,
                                null,
                                newProperties,
                                methodName);
        }
    }


    /**
     * Remove an existing classification from an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeName name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid = probably the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void    declassifyEntity(String                  userId,
                                    String                  entityGUID,
                                    String                  classificationTypeName,
                                    InstanceAuditHeader     existingClassificationHeader,
                                    String                  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String localMethodName = "declassifyEntity";

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId, entityGUID, classificationTypeName, methodName);
        }

        /*
         * Nothing to do if the classification is already gone.
         */
        if (auditHeader != null)
        {
            try
            {
                EntityDetail newEntity = metadataCollection.declassifyEntity(userId, entityGUID, classificationTypeName);

                if (newEntity == null)
                {
                    errorHandler.handleNoEntityForClassification(entityGUID,
                                                                 null,
                                                                 classificationTypeName,
                                                                 null,
                                                                 methodName);
                }
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Exception error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }
    }


    /**
     * Remove an existing classification from an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid = probably the GUID
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    declassifyEntity(String                  userId,
                                    String                  externalSourceGUID,
                                    String                  externalSourceName,
                                    String                  entityGUID,
                                    String                  classificationTypeGUID,
                                    String                  classificationTypeName,
                                    InstanceAuditHeader     existingClassificationHeader,
                                    String                  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String localMethodName = "declassifyEntity";

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId, entityGUID, classificationTypeName, methodName);
        }

        /*
         * Nothing to do if the classification is already gone.
         */
        if (auditHeader != null)
        {
            try
            {
                errorHandler.validateProvenance(userId,
                                                auditHeader,
                                                entityGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);

                EntityDetail newEntity = metadataCollection.declassifyEntity(userId,
                                                                             entityGUID,
                                                                             classificationTypeName);

                if (newEntity == null)
                {
                    errorHandler.handleNoEntityForClassification(entityGUID,
                                                                 classificationTypeGUID,
                                                                 classificationTypeName,
                                                                 null,
                                                                 methodName);
                }
            }
            catch (UserNotAuthorizedException error)
            {
                /*
                 * This comes from validateProvenance.  The call to validate provenance is in the try..catch
                 * in case the caller has passed bad parameters.
                 */
                throw error;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (Exception error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }
    }


    /**
     * Remove an entity from the open metadata repository if the validating properties match.
     *
     * @param userId calling user
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID type of entity to delete
     * @param entityTypeName name of the entity's type
     * @param validatingPropertyName name of property that should be in the entity if we have the correct one.
     * @param validatingProperty value of property that should be in the entity if we have the correct one.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException mismatch on properties
     */
    @Deprecated
    public void removeEntity(String           userId,
                             String           obsoleteEntityGUID,
                             String           entityTypeGUID,
                             String           entityTypeName,
                             String           validatingPropertyName,
                             String           validatingProperty,
                             String           methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String guidParameterName = "obsoleteEntityGUID";
        final String localMethodName   = "removeEntity(deprecated)";

        try
        {
            EntityDetail obsoleteEntity = this.getEntityByGUID(userId,
                                                               obsoleteEntityGUID,
                                                               guidParameterName,
                                                               entityTypeName,
                                                               methodName);

            if (obsoleteEntity != null)
            {
                errorHandler.validateProperties(obsoleteEntityGUID,
                                                validatingPropertyName,
                                                validatingProperty,
                                                obsoleteEntity.getProperties(),
                                                methodName);

            }
        }
        catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        try
        {
            metadataCollection.deleteEntity(userId, entityTypeGUID, entityTypeName, obsoleteEntityGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
        {
            this.purgeEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove an entity from the open metadata repository if the validating properties match. The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instances
     * metadata collection identifiers.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param obsoleteEntityGUIDParameterName name for unique identifier of the entity
     * @param entityTypeGUID type of entity to delete
     * @param entityTypeName name of the entity's type
     * @param validatingPropertyName name of property that should be in the entity if we have the correct one.
     * @param validatingProperty value of property that should be in the entity if we have the correct one.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException mismatch on properties
     */
    public void removeEntity(String           userId,
                             String           externalSourceGUID,
                             String           externalSourceName,
                             String           obsoleteEntityGUID,
                             String           obsoleteEntityGUIDParameterName,
                             String           entityTypeGUID,
                             String           entityTypeName,
                             String           validatingPropertyName,
                             String           validatingProperty,
                             String           methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String localMethodName = "removeEntity";

        try
        {
            EntityDetail obsoleteEntity = this.getEntityByGUID(userId,
                                                               obsoleteEntityGUID,
                                                               obsoleteEntityGUIDParameterName,
                                                               entityTypeName,
                                                               methodName);

            if (obsoleteEntity != null)
            {
                errorHandler.validateProvenance(userId,
                                                obsoleteEntity,
                                                obsoleteEntityGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);

                errorHandler.validateProperties(obsoleteEntityGUID,
                                                validatingPropertyName,
                                                validatingProperty,
                                                obsoleteEntity.getProperties(),
                                                methodName);

                this.isolateAndRemoveEntity(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            obsoleteEntityGUID,
                                            entityTypeGUID,
                                            entityTypeName,
                                            methodName);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove an entity from the repository if it is no longer connected to any other entity.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param guidParameterName name of parameter that passed the entity guid
     * @param entityTypeGUID unique identifier for the entity's type
     * @param entityTypeName name of the entity's type
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeEntityOnLastUse(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String obsoleteEntityGUID,
                                      String guidParameterName,
                                      String entityTypeGUID,
                                      String entityTypeName,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String localMethodName = "removeEntityOnLastUse";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            obsoleteEntityGUID,
                                                                                            null,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            5);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                this.isolateAndRemoveEntity(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            obsoleteEntityGUID,
                                            entityTypeGUID,
                                            entityTypeName,
                                            methodName);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, obsoleteEntityGUID, entityTypeName, methodName, guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove an entity from the open metadata repository after checking that is is not connected to
     * anything else.  The repository handler helps to ensure that all relationships are deleted explicitly
     * ensuring the events are created and making it easier for third party repositories to keep track of
     * changes rather than have to implement the implied deletes from the logical graph.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID type of entity to delete
     * @param entityTypeName name of the entity's type
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void isolateAndRemoveEntity(String userId,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        String obsoleteEntityGUID,
                                        String entityTypeGUID,
                                        String entityTypeName,
                                        String methodName) throws UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String localMethodName = "isolateAndRemoveEntity";

        this.removeAllRelationshipsOfType(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          obsoleteEntityGUID,
                                          entityTypeName,
                                          null,
                                          null,
                                          methodName);

        try
        {
            metadataCollection.deleteEntity(userId, entityTypeGUID, entityTypeName, obsoleteEntityGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
        {
            this.purgeEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove an entity from the open metadata repository after checking that is is not connected to
     * anything else.  The repository handler helps to ensure that all relationships are deleted explicitly
     * ensuring the events are created and making it easier for third party repositories to keep track of
     * changes rather than have to implement the implied deletes from the logical graph.
     *
     * @param userId calling user
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID type of entity to delete
     * @param entityTypeName name of the entity's type
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void removeIsolatedEntity(String           userId,
                                     String           obsoleteEntityGUID,
                                     String           entityTypeGUID,
                                     String           entityTypeName,
                                     String           methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String localMethodName = "removeIsolatedEntity";

        // Todo - validate that the entity is in fact isolated.
        try
        {
            try
            {
                metadataCollection.deleteEntity(userId, entityTypeGUID, entityTypeName, obsoleteEntityGUID);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
            {
                this.purgeEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
            }
        }
        catch (Throwable error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Purge an entity stored in a repository that does not support delete.
     *
     * @param userId calling user
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID type of entity to delete
     * @param entityTypeName name of the entity's type
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  purgeEntity(String           userId,
                             String           obsoleteEntityGUID,
                             String           entityTypeGUID,
                             String           entityTypeName,
                             String           methodName) throws UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String localMethodName = "purgeEntity";

        try
        {
            metadataCollection.purgeEntity(userId, entityTypeGUID, entityTypeName, obsoleteEntityGUID);
            auditLog.logMessage(methodName,
                                RepositoryHandlerAuditCode.ENTITY_PURGED.getMessageDefinition(obsoleteEntityGUID,
                                                                                              entityTypeName,
                                                                                              entityTypeGUID,
                                                                                              methodName,
                                                                                              metadataCollection.getMetadataCollectionId(userId)));
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void  restoreEntity(String userId,
                               String deletedEntityGUID,
                               String methodName) throws UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String localMethodName = "restoreEntity(deprecated)";

        try
        {
            metadataCollection.restoreEntity(userId, deletedEntityGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  restoreEntity(String userId,
                               String externalSourceGUID,
                               String externalSourceName,
                               String deletedEntityGUID,
                               String methodName) throws UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String localMethodName = "restoreEntity";

        try
        {
            EntityDetail entity = metadataCollection.restoreEntity(userId, deletedEntityGUID);

            if (entity != null)
            {
                errorHandler.validateProvenance(userId,
                                                entity,
                                                deletedEntityGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Return the list of entities of the requested type.
     *
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the entity's type
     * @param entityTypeName  name for the entity's type
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForType(String                 userId,
                                                 String                 entityTypeGUID,
                                                 String                 entityTypeName,
                                                 int                    startingFrom,
                                                 int                    pageSize,
                                                 String                 methodName) throws UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String localMethodName = "getEntitiesForType";

        try
        {
            List<EntityDetail> results = metadataCollection.findEntitiesByProperty(userId,
                                                                                   entityTypeGUID,
                                                                                   null,
                                                                                   null,
                                                                                   startingFrom,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   pageSize);

            if (results == null)
            {
                return null;
            }
            else if (results.isEmpty())
            {
                return null;
            }
            else
            {
                for (EntityDetail  entity : results)
                {
                    if (entity != null)
                    {
                        errorHandler.validateInstanceType(entity, entityTypeName, methodName, localMethodName);
                    }
                }
                return results;
            }

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForRelationshipType(String                 userId,
                                                             String                 startingEntityGUID,
                                                             String                 startingEntityTypeName,
                                                             String                 relationshipTypeGUID,
                                                             String                 relationshipTypeName,
                                                             int                    startingFrom,
                                                             int                    pageSize,
                                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String localMethodName = "getEntitiesForRelationshipType";

        List<EntityDetail> results = new ArrayList<>();

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            startingEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            pageSize);

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    EntityProxy requiredEnd = getOtherEnd(startingEntityGUID, startingEntityTypeName, relationship, methodName);

                    results.add(this.getEntityForRelationship(userId, requiredEnd, methodName));
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No relationships of type " + relationshipTypeName +
                                      " found for " + startingEntityTypeName + " entity " + startingEntityGUID);
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }

    /**
     * Return the list of entities by the requested classification type.
     *
     * @param userId               user making the request
     * @param entityEntityTypeGUID starting entity's GUID
     * @param classificationName   type name for the classification to follow
     * @param startingFrom         initial position in the stored list.
     * @param pageSize             maximum number of definitions to return on this call.
     * @param methodName           name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForClassificationType(String userId,
                                                               String entityEntityTypeGUID,
                                                               String classificationName,
                                                               int    startingFrom,
                                                               int    pageSize,
                                                               String methodName) throws    UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String localMethodName = "getEntitiesForClassificationType";

        try
        {
            List<EntityDetail> entitiesByClassification = metadataCollection.findEntitiesByClassification(userId,
                                                                                                          entityEntityTypeGUID,
                                                                                                          classificationName,
                                                                                                          null,
                                                                                                          MatchCriteria.ALL,
                                                                                                          startingFrom,
                                                                                                          null,
                                                                                                          null,
                                                                                                          null,
                                                                                                           SequencingOrder.ANY,
                                                                                                           pageSize);

            if (entitiesByClassification != null)
            {
                return entitiesByClassification;
            }
            else
            {
                log.debug("No entities of type {} with classification {}.", entityEntityTypeGUID, classificationName);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return new ArrayList<>();
    }


    /**
     * Return the list of entities at the requested end of the requested relationship type.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param anchorAtEnd1 indicates that the match of the anchor entity must be at end 1 (otherwise it is at end two)
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForRelationshipEnd(String                 userId,
                                                            String                 anchorEntityGUID,
                                                            String                 anchorEntityTypeName,
                                                            boolean                anchorAtEnd1,
                                                            String                 relationshipTypeGUID,
                                                            String                 relationshipTypeName,
                                                            int                    startingFrom,
                                                            int                    pageSize,
                                                            String                 methodName) throws UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String localMethodName = "getEntitiesForRelationshipEnd";

        List<EntityDetail> results = new ArrayList<>();

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            anchorEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            pageSize);

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    EntityProxy anchorEndProxy = relationship.getEntityOneProxy();
                    EntityProxy requiredEndProxy = relationship.getEntityTwoProxy();

                    if (! anchorAtEnd1)
                    {
                        anchorEndProxy = relationship.getEntityTwoProxy();
                        requiredEndProxy = relationship.getEntityOneProxy();
                    }

                    if (anchorEntityGUID.equals(anchorEndProxy.getGUID()))
                    {
                        results.add(metadataCollection.getEntityDetail(userId, requiredEndProxy.getGUID()));
                    }
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No relationships of type " + relationshipTypeName +
                                      " found for " + anchorEntityTypeName + " entity " + anchorEntityGUID);
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of entities at the other end of the requested relationship type that were created or edited by
     * the requesting user.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachedEntityTypeGUID  identifier for the relationship to follow
     * @param attachedEntityTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws InvalidParameterException the entity at the other end is not of the expected type
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getAttachedEntityFromUser(String  userId,
                                                  String  startingEntityGUID,
                                                  String  startingEntityTypeName,
                                                  String  relationshipTypeGUID,
                                                  String  relationshipTypeName,
                                                  String  attachedEntityTypeGUID,
                                                  String  attachedEntityTypeName,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String localMethodName = "getAttachedEntityFromUser";

        RepositoryRelatedEntitiesIterator iterator = new RepositoryRelatedEntitiesIterator(this,
                                                                                           userId,
                                                                                           startingEntityGUID,
                                                                                           startingEntityTypeName,
                                                                                           relationshipTypeGUID,
                                                                                           relationshipTypeName,
                                                                                           0,
                                                                                           maxPageSize,
                                                                                           methodName);




        while (iterator.moreToReceive())
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                if ((userId.equals(entity.getCreatedBy()) || (userId.equals(entity.getUpdatedBy())) || ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                {
                    errorHandler.validateInstanceType(entity, attachedEntityTypeName, methodName, localMethodName);
                    return entity;
                }
            }
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type that were created or
     * edited by the requesting user.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachedEntityTypeGUID  identifier for the relationship to follow
     * @param attachedEntityTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException one of the parameters is in error
     */
    public List<EntityDetail> getAttachedEntitiesFromUser(String  userId,
                                                          String  startingEntityGUID,
                                                          String  startingEntityTypeName,
                                                          String  relationshipTypeGUID,
                                                          String  relationshipTypeName,
                                                          String  attachedEntityTypeGUID,
                                                          String  attachedEntityTypeName,
                                                          int     startingFrom,
                                                          int     pageSize,
                                                          String  methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName = "getAttachedEntitiesFromUser";

        List<EntityDetail> results = new ArrayList<>();

        RepositoryRelatedEntitiesIterator iterator = new RepositoryRelatedEntitiesIterator(this,
                                                                                           userId,
                                                                                           startingEntityGUID,
                                                                                           startingEntityTypeName,
                                                                                           relationshipTypeGUID,
                                                                                           relationshipTypeName,
                                                                                           startingFrom,
                                                                                           pageSize,
                                                                                           methodName);

        while ((iterator.moreToReceive() && ((pageSize == 0) || (results.size() < pageSize))))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                if ((userId.equals(entity.getCreatedBy()) || (userId.equals(entity.getUpdatedBy())) || ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                {
                    errorHandler.validateInstanceType(entity, attachedEntityTypeName, methodName, localMethodName);
                    results.add(entity);
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of entity proxies for the entities at the far end of the relationships linked to the
     * starting entity.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     * @throws InvalidParameterException the starting entity is not of the expected type
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     * @return list of entity proxies
     */
    public List<EntityProxy>  getRelatedEntityProxies(String                 userId,
                                                      String                 startingEntityGUID,
                                                      String                 startingEntityTypeName,
                                                      String                 relationshipTypeGUID,
                                                      String                 relationshipTypeName,
                                                      int                    startingFrom,
                                                      int                    pageSize,
                                                      String                 methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                       startingEntityGUID,
                                                                       startingEntityTypeName,
                                                                       relationshipTypeGUID,
                                                                       relationshipTypeName,
                                                                       startingFrom,
                                                                       pageSize,
                                                                       methodName);

        if (relationships != null)
        {
            List<EntityProxy>  entityProxies = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy relatedEntityProxy = this.getOtherEnd(startingEntityGUID, startingEntityTypeName, relationship, methodName);

                    if (relatedEntityProxy != null)
                    {
                        entityProxies.add(relatedEntityProxy);
                    }
                }
            }

            if (entityProxies.isEmpty())
            {
                return null;
            }
            else
            {
                return entityProxies;
            }
        }

        return null;
    }


    /**
     * Return the entity proxy for the related entity.
     *
     * @param startingEntityGUID unique identifier of the starting entity
     * @param relationship relationship to another entity
     * @return proxy to the other entity.
     */
    public  EntityProxy  getOtherEnd(String                 startingEntityGUID,
                                     Relationship           relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                if (startingEntityGUID.equals(entityProxy.getGUID()))
                {
                    entityProxy = relationship.getEntityTwoProxy();
                }
            }

            return entityProxy;
        }

        return null;
    }


    /**
     * Return the entity proxy for the related entity.
     *
     * @param startingEntityGUID unique identifier of the starting entity
     * @param startingEntityTypeName type of the entity
     * @param relationship relationship to another entity
     * @param methodName calling method
     * @return proxy to the other entity.
     * @throws InvalidParameterException the type of the starting entity is incorrect
     */
    public  EntityProxy  getOtherEnd(String       startingEntityGUID,
                                     String       startingEntityTypeName,
                                     Relationship relationship,
                                     String       methodName) throws InvalidParameterException
    {
        final String localMethodName = "getOtherEnd";

        if (relationship != null)
        {
            EntityProxy requiredEnd = relationship.getEntityOneProxy();
            EntityProxy startingEnd = relationship.getEntityTwoProxy();

            if (startingEntityGUID.equals(requiredEnd.getGUID()))
            {
                requiredEnd = relationship.getEntityTwoProxy();
                startingEnd = relationship.getEntityOneProxy();
            }

            errorHandler.validateInstanceType(startingEnd, startingEntityTypeName, methodName, localMethodName);

            return requiredEnd;
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type.
     *
     * @param userId  user making the request
     * @param requiredEnd  entityProxy from relationship
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    private EntityDetail getEntityForRelationship(String                 userId,
                                                  EntityProxy            requiredEnd,
                                                  String                 methodName) throws UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String localMethodName = "getEntityForRelationship";

        try
        {
            return metadataCollection.getEntityDetail(userId, requiredEnd.getGUID());
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the entity at the other end of the requested relationship type.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     * @return retrieved entity or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getEntityForRelationshipType(String                 userId,
                                                     String                 startingEntityGUID,
                                                     String                 startingEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String localMethodName = "getEntityForRelationshipType";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            startingEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            100);

            if (relationships != null)
            {
                if (relationships.size() == 1)
                {
                    Relationship  relationship = relationships.get(0);

                    EntityProxy requiredEnd = relationship.getEntityOneProxy();
                    EntityProxy startingEnd = relationship.getEntityTwoProxy();

                    if (startingEntityGUID.equals(requiredEnd.getGUID()))
                    {
                        requiredEnd = relationship.getEntityTwoProxy();
                        startingEnd = relationship.getEntityOneProxy();
                    }

                    errorHandler.validateInstanceType(startingEnd, startingEntityTypeName, methodName, localMethodName);

                    return this.getEntityForRelationship(userId, requiredEnd, methodName);
                }
                else if (relationships.size() > 1)
                {
                    errorHandler.handleAmbiguousRelationships(startingEntityGUID,
                                                              startingEntityTypeName,
                                                              relationshipTypeName,
                                                              relationships,
                                                              methodName);
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the requested entity, converting any errors from the repository services into the local
     * OMAS exceptions.
     *
     * @param userId calling user
     * @param guid unique identifier for the entity
     * @param guidParameterName name of the guid parameter for error handling
     * @param entityTypeName expected type of the entity
     * @param methodName calling method name
     *
     * @return entity detail object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public EntityDetail   getEntityByGUID(String                 userId,
                                          String                 guid,
                                          String                 guidParameterName,
                                          String                 entityTypeName,
                                          String                 methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String localMethodName = "getEntityByGUID";

        try
        {
            EntityDetail entity = metadataCollection.getEntityDetail(userId, guid);

            errorHandler.validateInstanceType(entity, entityTypeName, methodName, localMethodName);

            return entity;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, guid, entityTypeName, methodName, guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException error)
        {
            errorHandler.handleEntityProxy(error, guid, entityTypeName, methodName, guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Test whether an entity is of a particular type or not.
     *
     * @param userId calling user
     * @param guid unique identifier of the entity.
     * @param guidParameterName name of the parameter containing the guid.
     * @param entityTypeName name of the type to test for
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public boolean isEntityATypeOf(String                 userId,
                                   String                 guid,
                                   String                 guidParameterName,
                                   String                 entityTypeName,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String localMethodName = "isEntityATypeOf";

        try
        {
            EntityDetail entity = metadataCollection.getEntityDetail(userId, guid);

            return errorHandler.isInstanceATypeOf(entity, entityTypeName, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, guid, entityTypeName, methodName, guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException error)
        {
            errorHandler.handleEntityProxy(error, guid, entityTypeName, methodName, guidParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return false;
    }


    /**
     * Return the requested entity by name.
     *
     * @param userId calling userId
     * @param nameProperties list of name properties to search on.
     * @param entityTypeGUID unique identifier of the entity's type
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntityByName(String                 userId,
                                               InstanceProperties     nameProperties,
                                               String                 entityTypeGUID,
                                               String                 methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "getEntityByName";

        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             nameProperties,
                                                             MatchCriteria.ANY,
                                                             0,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             2);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the requested entity by name.
     *
     * @param userId calling userId
     * @param nameProperties list of name properties to search on.
     * @param entityTypeGUID unique identifier of the entity's type
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByName(String                 userId,
                                                 InstanceProperties     nameProperties,
                                                 String                 entityTypeGUID,
                                                 int                    startingFrom,
                                                 int                    pageSize,
                                                 String                 methodName) throws UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String localMethodName = "getEntitiesByName";

        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             nameProperties,
                                                             MatchCriteria.ANY,
                                                             startingFrom,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }



    /**
     * Return the entities that match all supplied properties.
     *
     * @param userId calling userId
     * @param properties list of name properties to search on.
     * @param entityTypeGUID unique identifier of the entity's type
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByAllProperties(String                 userId,
                                                          InstanceProperties     properties,
                                                          String                 entityTypeGUID,
                                                          int                    startingFrom,
                                                          int                    pageSize,
                                                          String                 methodName) throws UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String localMethodName = "getEntitiesByAllProperties";

        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             properties,
                                                             MatchCriteria.ALL,
                                                             startingFrom,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }

    /**
     * Return the entities that match all supplied properties.
     *
     * @param userId calling userId
     * @param properties list of name properties to search on.
     * @param entityTypeGUID unique identifier of the entity's type
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesWithoutPropertyValues(String                 userId,
                                                                InstanceProperties     properties,
                                                                String                 entityTypeGUID,
                                                                int                    startingFrom,
                                                                int                    pageSize,
                                                                String                 methodName) throws UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String localMethodName = "getEntitiesWithoutPropertyValues";

        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             properties,
                                                             MatchCriteria.NONE,
                                                             startingFrom,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the entities that match all supplied properties.
     *
     * @param userId calling userId
     * @param propertyValue string value to search on - may be a RegEx.
     * @param entityTypeGUID unique identifier of the entity's type
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByValue(String     userId,
                                                  String     propertyValue,
                                                  String     entityTypeGUID,
                                                  int        startingFrom,
                                                  int        pageSize,
                                                  String     methodName) throws UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String localMethodName = "getEntitiesByValue";

        try
        {
            return metadataCollection.findEntitiesByPropertyValue(userId,
                                                                  entityTypeGUID,
                                                                  propertyValue,
                                                                  startingFrom,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the entities that match all supplied properties.
     *
     * @param userId calling user
     * @param entityTypeGUID unique identifier of the entity's type
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                             within entity instances of the specified type(s).
     *                             This parameter must not be null.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param methodName calling method
     *
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                              Null means do not sequence on a property name (see SequencingOrder).
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByPropertyValue(String          userId,
                                                          String          entityTypeGUID,
                                                          String          searchCriteria,
                                                          int             startingFrom,
                                                          int             pageSize,
                                                          Date            asOfTime,
                                                          String          sequencingProperty,
                                                          SequencingOrder sequencingOrder,
                                                          String          methodName) throws UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String localMethodName = "getEntitiesByPropertyValue";
        try
        {
            return metadataCollection.findEntitiesByPropertyValue(userId,
                                                                  entityTypeGUID,
                                                                  searchCriteria,
                                                                  startingFrom,
                                                                  null,
                                                                  null,
                                                                  asOfTime,
                                                                  sequencingProperty,
                                                                  sequencingOrder,
                                                                  pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the requested entity by name.
     *
     * @param userId calling userId
     * @param nameValue property name being searched for
     * @param nameParameterName name of parameter that passed the name value
     * @param nameProperties list of name properties to search on
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public EntityDetail  getUniqueEntityByName(String                 userId,
                                               String                 nameValue,
                                               String                 nameParameterName,
                                               InstanceProperties     nameProperties,
                                               String                 entityTypeGUID,
                                               String                 entityTypeName,
                                               String                 methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "getUniqueEntityByName";

        try
        {
            List<EntityDetail> returnedEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                            entityTypeGUID,
                                                                                            nameProperties,
                                                                                            MatchCriteria.ANY,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            2);

            if ((returnedEntities == null) || (returnedEntities.isEmpty()))
            {
                return null;
            }
            else if (returnedEntities.size() == 1)
            {
                return returnedEntities.get(0);
            }
            else
            {
                errorHandler.handleAmbiguousEntityName(nameValue, nameParameterName, entityTypeName, returnedEntities, methodName);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the requested entities that match the requested type.
     *
     * @param userId calling userId
     * @param entityTypeGUID type of entity required
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail> getEntitiesByType(String                 userId,
                                                String                 entityTypeGUID,
                                                int                    startingFrom,
                                                int                    pageSize,
                                                String                 methodName) throws UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return getEntitiesByType(userId,
                                 entityTypeGUID,
                                 startingFrom,
                                 pageSize,
                                 null,
                                 null,
                                 null,
                                 methodName);
    }


    /**
     * Return the requested entities that match the requested type.
     *
     * @param userId calling userId
     * @param entityTypeGUID type of entity required
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                                Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail> getEntitiesByType(String          userId,
                                                String          entityTypeGUID,
                                                int             startingFrom,
                                                int             pageSize,
                                                Date            asOfTime,
                                                String          sequencingProperty,
                                                SequencingOrder sequencingOrder,
                                                String          methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String localMethodName = "getEntitiesByType";

        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
                                                             entityTypeGUID,
                                                             null,
                                                             null,
                                                             startingFrom,
                                                             null,
                                                             null,
                                                             asOfTime,
                                                             sequencingProperty,
                                                             sequencingOrder,
                                                             pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param searchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startingFrom the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail> findEntities(String                userId,
                                           String                entityTypeGUID,
                                           List<String>          entitySubtypeGUIDs,
                                           SearchProperties      searchProperties,
                                           List<InstanceStatus>  limitResultsByStatus,
                                           SearchClassifications searchClassifications,
                                           Date                  asOfTime,
                                           String                sequencingProperty,
                                           SequencingOrder       sequencingOrder,
                                           int                   startingFrom,
                                           int                   pageSize,
                                           String                methodName) throws UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String localMethodName = "findEntities";

        try
        {
            return metadataCollection.findEntities(userId,
                                                   entityTypeGUID,
                                                   entitySubtypeGUIDs,
                                                   searchProperties,
                                                   startingFrom,
                                                   limitResultsByStatus,
                                                   searchClassifications,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the relationshipTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startingFrom the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<Relationship> findRelationships(String                userId,
                                                String                relationshipTypeGUID,
                                                List<String>          relationshipSubtypeGUIDs,
                                                SearchProperties      searchProperties,
                                                List<InstanceStatus>  limitResultsByStatus,
                                                Date                  asOfTime,
                                                String                sequencingProperty,
                                                SequencingOrder       sequencingOrder,
                                                int                   startingFrom,
                                                int                   pageSize,
                                                String                methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "findRelationships";

        try
        {
            return metadataCollection.findRelationships(userId,
                                                        relationshipTypeGUID,
                                                        relationshipSubtypeGUIDs,
                                                        searchProperties,
                                                        startingFrom,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity.
     * The list is expected to be small.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String                 userId,
                                                     String                 startingEntityGUID,
                                                     String                 startingEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return this.getRelationshipsByType(userId,
                                           startingEntityGUID,
                                           startingEntityTypeName,
                                           relationshipTypeGUID,
                                           relationshipTypeName,
                                           0,
                                           maxPageSize,
                                           methodName);
    }


    /**
     * Return the current version of a requested relationship.
     *
     * @param userId  user making the request
     * @param relationshipGUID String unique identifier for the relationship.
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or exception
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    @Deprecated
    public Relationship getRelationshipByGUID(String                 userId,
                                              String                 relationshipGUID,
                                              String                 methodName) throws UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String localMethodName = "getRelationshipByGUID";

        try
        {
            return metadataCollection.getRelationship(userId, relationshipGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the current version of a requested relationship.
     *
     * @param userId  user making the request
     * @param relationshipGUID unique identifier for the relationship
     * @param relationshipParameterName parameter name supplying relationshipGUID
     * @param relationshipTypeName type name for the relationship
     *
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or exception
     *
     * @throws InvalidParameterException the GUID is invalid
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getRelationshipByGUID(String userId,
                                              String relationshipGUID,
                                              String relationshipParameterName,
                                              String relationshipTypeName,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String localMethodName = "getRelationshipByGUID";

        try
        {
            return metadataCollection.getRelationship(userId, relationshipGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException  error)
        {
            errorHandler.handleUnknownRelationship(error, relationshipGUID, relationshipTypeName, methodName, relationshipParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity.
     * The list is expected to be small.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String                 userId,
                                                     String                 startingEntityGUID,
                                                     String                 startingEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String localMethodName = "getRelationshipsByType";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            startingEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            pageSize);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No relationships of type " + relationshipTypeGUID +
                                      " found for entity " + startingEntityGUID);
                }

                return null;
            }

            List<Relationship>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);
                    this.getOtherEnd(startingEntityGUID, startingEntityTypeName, relationship, methodName);

                    results.add(relationship);
                }
            }

            if (results.isEmpty())
            {
                return null;
            }

            return results;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity.
     * The list is expected to be small.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String                 userId,
                                                     String                 startingEntityGUID,
                                                     String                 relationshipTypeGUID,
                                                     List<InstanceStatus>   limitResultsByStatus,
                                                     Date                   asOfTime,
                                                     String                 sequencingProperty,
                                                     SequencingOrder        sequencingOrder,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String localMethodName = "getRelationshipsByType";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            startingEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            limitResultsByStatus,
                                                                                            asOfTime,
                                                                                            sequencingProperty,
                                                                                            sequencingOrder,
                                                                                            pageSize);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No relationships of type " + relationshipTypeGUID +
                              " found for entity " + startingEntityGUID);
                }

                return null;
            }

            List<Relationship>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    errorHandler.validateInstanceType(relationship, relationshipTypeGUID, methodName, localMethodName);
                    this.getOtherEnd(startingEntityGUID, relationship);

                    results.add(relationship);
                }
            }

            if (results.isEmpty())
            {
                return null;
            }

            return results;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     * No relationships found results in an exception.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRequiredRelationshipsByType(String                 userId,
                                                             String                 anchorEntityGUID,
                                                             String                 anchorEntityTypeName,
                                                             String                 relationshipTypeGUID,
                                                             String                 relationshipTypeName,
                                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return this.getRequiredRelationshipsByType(userId,
                                                   anchorEntityGUID,
                                                   anchorEntityTypeName,
                                                   relationshipTypeGUID,
                                                   relationshipTypeName,
                                                   0,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   100,
                                                   methodName);
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     * No relationships found results in an exception.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRequiredRelationshipsByType(String                 userId,
                                                             String                 anchorEntityGUID,
                                                             String                 anchorEntityTypeName,
                                                             String                 relationshipTypeGUID,
                                                             String                 relationshipTypeName,
                                                             int                    startingFrom,
                                                             List<InstanceStatus>   limitResultsByStatus,
                                                             Date                   asOfTime,
                                                             String                 sequencingProperty,
                                                             SequencingOrder        sequencingOrder,
                                                             int                    pageSize,
                                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String localMethodName = "getRequiredRelationshipsByType";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            anchorEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            limitResultsByStatus,
                                                                                            asOfTime,
                                                                                            sequencingProperty,
                                                                                            sequencingOrder,
                                                                                            pageSize);

            if (relationships.isEmpty())
            {
                return null;
            }

            return relationships;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }



    /**
     * Count the number of relationships of a specific type attached to an starting entity.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return count of the number of relationships
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countAttachedRelationshipsByType(String                 userId,
                                                String                 startingEntityGUID,
                                                String                 startingEntityTypeName,
                                                String                 relationshipTypeGUID,
                                                String                 relationshipTypeName,
                                                String                 methodName) throws PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                       startingEntityGUID,
                                                                       startingEntityTypeName,
                                                                       relationshipTypeGUID,
                                                                       relationshipTypeName,
                                                                       methodName);

        int count = 0;

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    count ++;
                }
            }
        }

        return count;
    }


    /**
     * Return the list of relationships of the requested type connecting the supplied entities.
     *
     * @param userId  user making the request
     * @param entity1GUID  entity at end 1 GUID
     * @param entity1TypeName   entity 1's type name
     * @param entity2GUID  entity at end 2 GUID
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws InvalidParameterException wrong type in entity 1
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsBetweenEntities(String                 userId,
                                                              String                 entity1GUID,
                                                              String                 entity1TypeName,
                                                              String                 entity2GUID,
                                                              String                 relationshipTypeGUID,
                                                              String                 relationshipTypeName,
                                                              String                 methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        List<Relationship>  entity1Relationships = this.getRelationshipsByType(userId,
                                                                               entity1GUID,
                                                                               entity1TypeName,
                                                                               relationshipTypeGUID,
                                                                               relationshipTypeName,
                                                                               methodName);

        if (entity1Relationships != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (Relationship  relationship : entity1Relationships)
            {
                if (relationship != null)
                {
                    EntityProxy  entity2Proxy = this.getOtherEnd(entity1GUID, entity1TypeName, relationship, methodName);
                    if (entity2Proxy != null)
                    {
                        if (entity2GUID.equals(entity2Proxy.getGUID()))
                        {
                            results.add(relationship);
                        }
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }



    /**
     * Return the first found relationship of the requested type connecting the supplied entities.
     *
     * @param userId  user making the request
     * @param entity1GUID  entity at end 1 GUID
     * @param entity1TypeName   entity 1's type name
     * @param entity2GUID  entity at end 2 GUID
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws InvalidParameterException wrong type in entity 1
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getRelationshipBetweenEntities(String                 userId,
                                                       String                 entity1GUID,
                                                       String                 entity1TypeName,
                                                       String                 entity2GUID,
                                                       String                 relationshipTypeGUID,
                                                       String                 relationshipTypeName,
                                                       String                 methodName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        List<Relationship>  entity1Relationships = this.getRelationshipsBetweenEntities(userId,
                                                                                        entity1GUID,
                                                                                        entity1TypeName,
                                                                                        entity2GUID,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        if (entity1Relationships != null)
        {
            for (Relationship  relationship : entity1Relationships)
            {
                if (relationship != null)
                {
                    return relationship;
                }
            }
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity.  If there are no relationships
     * null is returned
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom results starting point
     * @param maximumResults page size
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getPagedRelationshipsByType(String userId,
                                                          String startingEntityGUID,
                                                          String startingEntityTypeName,
                                                          String relationshipTypeGUID,
                                                          String relationshipTypeName,
                                                          int    startingFrom,
                                                          int    maximumResults,
                                                          String methodName) throws UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String localMethodName = "getPagedRelationshipsByType";

        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            startingEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            startingFrom,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            maximumResults);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                return null;
            }

            return relationships;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the relationship of the requested type connected to the starting entity and where the starting entity is the logical child.
     * The assumption is that this is a 0..1 relationship so the first matching relationship is returned (or null if there is none).
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param parentAtEnd1 boolean flag to indicate which end has the parent element
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueParentRelationshipByType(String  userId,
                                                          String  startingEntityGUID,
                                                          String  startingEntityTypeName,
                                                          String  relationshipTypeGUID,
                                                          String  relationshipTypeName,
                                                          boolean parentAtEnd1,
                                                          String  methodName) throws UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String localMethodName = "getUniqueParentRelationshipByType";

        try
        {
            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           startingEntityGUID,
                                                                           startingEntityTypeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           methodName);

            if (relationships != null)
            {
                RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(this,
                                                                                               userId,
                                                                                               startingEntityGUID,
                                                                                               startingEntityTypeName,
                                                                                               relationshipTypeGUID,
                                                                                               relationshipTypeName,
                                                                                               0,
                                                                                               maxPageSize,
                                                                                               methodName);

                while (iterator.moreToReceive())
                {
                    Relationship relationship = iterator.getNext();

                    if (relationship != null)
                    {
                        EntityProxy parentEntity;

                        if (parentAtEnd1)
                        {
                            parentEntity = relationship.getEntityOneProxy();
                        }
                        else
                        {
                            parentEntity = relationship.getEntityTwoProxy();
                        }

                        if ((parentEntity != null) && (! startingEntityGUID.equals(parentEntity.getGUID())))
                        {
                            return relationship;
                        }
                    }
                }
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the relationship of the requested type connected to the starting entity.
     * The assumption is that this is a 0..1 relationship so one relationship (or null) is returned.
     * If lots of relationships are found then the PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueRelationshipByType(String                 userId,
                                                    String                 startingEntityGUID,
                                                    String                 startingEntityTypeName,
                                                    String                 relationshipTypeGUID,
                                                    String                 relationshipTypeName,
                                                    String                 methodName) throws UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String localMethodName = "getUniqueRelationshipByType";

        try
        {
            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           startingEntityGUID,
                                                                           startingEntityTypeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           methodName);

            if (relationships != null)
            {
                if (relationships.size() == 1)
                {
                    return relationships.get(0);
                }
                else if (relationships.size() > 1)
                {
                    errorHandler.handleAmbiguousRelationships(startingEntityGUID,
                                                              startingEntityTypeName,
                                                              relationshipTypeName,
                                                              relationships,
                                                              methodName);
                }
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity where the
     * starting entity is at the end indicated by the startAtEnd1 boolean parameter.
     * The assumption is that this is a 0..1 relationship so one relationship (or null) is returned.
     * If lots of relationships are found then the PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param startAtEnd1 is the starting entity at end 1 of the relationship
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueRelationshipByType(String                 userId,
                                                    String                 startingEntityGUID,
                                                    String                 startingEntityTypeName,
                                                    boolean                startAtEnd1,
                                                    String                 relationshipTypeGUID,
                                                    String                 relationshipTypeName,
                                                    String                 methodName) throws UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String localMethodName = "getUniqueRelationshipByType";

        try
        {
            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           startingEntityGUID,
                                                                           startingEntityTypeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           methodName);

            if (relationships != null)
            {
                Relationship  result = null;

                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        EntityProxy proxy;

                        if (startAtEnd1)
                        {
                            proxy = relationship.getEntityOneProxy();
                        }
                        else
                        {
                            proxy = relationship.getEntityTwoProxy();
                        }

                        if (proxy != null)
                        {
                            if (startingEntityGUID.equals(proxy.getGUID()))
                            {
                                if (result == null)
                                {
                                    /*
                                     * Although we have found the relationship requests, the loop continues to make
                                     * sure this is the only one.
                                     */
                                    result = relationship;
                                }
                                else
                                {
                                    errorHandler.handleAmbiguousRelationships(startingEntityGUID,
                                                                              startingEntityTypeName,
                                                                              relationshipTypeName,
                                                                              relationships,
                                                                              methodName);
                                }
                            }
                        }
                    }
                }

                return result;
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Create a relationship between two entities.  The value of external source GUID determines if it is local or remote.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param end1GUID entity to store at end 1
     * @param end2GUID entity to store at end 2
     * @param relationshipProperties properties for the relationship
     * @param methodName name of calling method
     * @return Relationship structure with the new header, requested entities and properties or null.
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Relationship createRelationship(String                  userId,
                                           String                  relationshipTypeGUID,
                                           String                  externalSourceGUID,
                                           String                  externalSourceName,
                                           String                  end1GUID,
                                           String                  end2GUID,
                                           InstanceProperties      relationshipProperties,
                                           String                  methodName) throws UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String localMethodName = "createRelationship";

        try
        {
            if (externalSourceGUID == null)
            {
                return metadataCollection.addRelationship(userId,
                                                          relationshipTypeGUID,
                                                          relationshipProperties,
                                                          end1GUID,
                                                          end2GUID,
                                                          InstanceStatus.ACTIVE);
            }
            else
            {
                return metadataCollection.addExternalRelationship(userId,
                                                                  relationshipTypeGUID,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  relationshipProperties,
                                                                  end1GUID,
                                                                  end2GUID,
                                                                  InstanceStatus.ACTIVE);
            }

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Ensure a relationship exists between two entities.  The setting of external source GUID determines if the relationship is external or not
     *
     * @param userId calling user
     * @param end1TypeName unique name of the end 1's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param end1GUID entity to store at end 1
     * @param end2GUID entity to store at end 2
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param relationshipTypeName unique name of the relationship's type
     * @param relationshipProperties properties for the relationship
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException type of end 1 is not correct
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void ensureRelationship(String                  userId,
                                   String                  end1TypeName,
                                   String                  externalSourceGUID,
                                   String                  externalSourceName,
                                   String                  end1GUID,
                                   String                  end2GUID,
                                   String                  relationshipTypeGUID,
                                   String                  relationshipTypeName,
                                   InstanceProperties      relationshipProperties,
                                   String                  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
       Relationship relationship = this.getRelationshipBetweenEntities(userId,
                                                                       end1GUID,
                                                                       end1TypeName,
                                                                       end2GUID,
                                                                       relationshipTypeGUID,
                                                                       relationshipTypeName,
                                                                       methodName);
       if (relationship == null)
       {
           this.createRelationship(userId, relationshipTypeGUID, externalSourceGUID, externalSourceName, end1GUID, end2GUID, relationshipProperties, methodName);
       }
       else
       {
           errorHandler.validateProvenance(userId,
                                           relationship,
                                           relationship.getGUID(),
                                           externalSourceGUID,
                                           externalSourceName,
                                           methodName);

           if ((relationshipProperties != null) || (relationship.getProperties() != null))
           {
               /*
                * This ensures the properties are identical.
                */
               this.updateRelationshipProperties(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 relationshipProperties,
                                                 methodName);
           }
       }
    }


    /**
     * Create a relationship from an external source between two entities.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param end1GUID entity to store at end 1
     * @param end2GUID entity to store at end 2
     * @param relationshipProperties properties for the relationship
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void createExternalRelationship(String                  userId,
                                           String                  relationshipTypeGUID,
                                           String                  externalSourceGUID,
                                           String                  externalSourceName,
                                           String                  end1GUID,
                                           String                  end2GUID,
                                           InstanceProperties      relationshipProperties,
                                           String                  methodName) throws UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        this.createRelationship(userId,
                                relationshipTypeGUID,
                                externalSourceGUID,
                                externalSourceName,
                                end1GUID,
                                end2GUID,
                                relationshipProperties,
                                methodName);
    }


    /**
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
     * This method is deprecated because it is not possible to verify the metadata provenance of the
     * relationship being deleted.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the type of relationship to delete
     * @param relationshipTypeName name of the type of relationship to delete
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void removeRelationship(String                  userId,
                                   String                  relationshipTypeGUID,
                                   String                  relationshipTypeName,
                                   String                  relationshipGUID,
                                   String                  methodName) throws UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String localMethodName = "removeRelationship";

        try
        {
            try
            {
                metadataCollection.deleteRelationship(userId,
                                                      relationshipTypeGUID,
                                                      relationshipTypeName,
                                                      relationshipGUID);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
            {
                this.purgeRelationship(userId, relationshipTypeGUID, relationshipTypeName, relationshipGUID, methodName);
                auditLog.logMessage(methodName,
                                    RepositoryHandlerAuditCode.RELATIONSHIP_PURGED.getMessageDefinition(relationshipGUID,
                                                                                                        relationshipTypeName,
                                                                                                        relationshipTypeGUID,
                                                                                                        methodName,
                                                                                                        metadataCollection.getMetadataCollectionId(userId)));
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
     * The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instances
     * metadata collection identifiers.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param relationshipTypeName name of the type of relationship to delete
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeRelationship(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String relationshipTypeName,
                                   String relationshipGUID,
                                   String methodName) throws UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String localMethodName = "removeRelationship";

        try
        {
            Relationship relationship = metadataCollection.getRelationship(userId, relationshipGUID);

            if (relationship != null)
            {
                errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);

                removeRelationship(userId, externalSourceGUID, externalSourceName, relationship, methodName);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
     * The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instances
     * metadata collection identifiers.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param relationship relationship to delete
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  void removeRelationship(String       userId,
                                    String       externalSourceGUID,
                                    String       externalSourceName,
                                    Relationship relationship,
                                    String       methodName) throws UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String localMethodName = "removeRelationship";

        try
        {
            errorHandler.validateProvenance(userId,
                                            relationship,
                                            relationship.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            metadataCollection.deleteRelationship(userId,
                                                  relationship.getType().getTypeDefGUID(),
                                                  relationship.getType().getTypeDefName(),
                                                  relationship.getGUID());
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException  error)
        {
            this.purgeRelationship(userId,
                                   relationship.getType().getTypeDefGUID(),
                                   relationship.getType().getTypeDefName(),
                                   relationship.getGUID(),
                                   methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Purge a relationship between two entities.  Used if delete fails.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the type of relationship to delete
     * @param relationshipTypeName name of the type of relationship to delete
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void purgeRelationship(String userId,
                                  String relationshipTypeGUID,
                                  String relationshipTypeName,
                                  String relationshipGUID,
                                  String methodName) throws UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String localMethodName = "purgeRelationship";

        try
        {
            metadataCollection.purgeRelationship(userId,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 relationshipGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested relationship to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void restoreRelationship(String userId,
                                    String deletedRelationshipGUID,
                                    String methodName) throws UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String localMethodName = "restoreRelationship(deprecated)";

        try
        {
            metadataCollection.restoreRelationship(userId, deletedRelationshipGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested relationship to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void restoreRelationship(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String deletedRelationshipGUID,
                                    String methodName) throws UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String localMethodName = "restoreRelationship";

        try
        {
            Relationship relationship = metadataCollection.restoreRelationship(userId, deletedRelationshipGUID);
            if (relationship != null)
            {
                errorHandler.validateProvenance(userId,
                                                relationship,
                                                deletedRelationshipGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove all relationships of a certain type starting at a particular entity.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param startingEntityGUID identifier of starting entity
     * @param startingEntityTypeName type of entity
     * @param relationshipTypeGUID unique identifier of the relationship type
     * @param relationshipTypeName unique name of the relationship type
     * @param methodName calling method
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeAllRelationshipsOfType(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String startingEntityGUID,
                                             String startingEntityTypeName,
                                             String relationshipTypeGUID,
                                             String relationshipTypeName,
                                             String methodName) throws UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(this,
                                                                                       userId,
                                                                                       startingEntityGUID,
                                                                                       startingEntityTypeName,
                                                                                       relationshipTypeGUID,
                                                                                       relationshipTypeName,
                                                                                       0,
                                                                                       maxPageSize,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                this.removeRelationship(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        relationship,
                                        methodName);
            }
        }
    }


    /**
     * Delete a relationship between two specific entities.  The relationship must have compatible provenance
     * to allow the update to proceed.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param relationshipTypeGUID unique identifier of the type of relationship to delete
     * @param relationshipTypeName name of the type of relationship to delete
     * @param entity1GUID unique identifier of the entity at end 1 of the relationship to delete
     * @param entity1TypeName type name of the entity at end 1 of the relationship to delete
     * @param entity2GUID unique identifier of the entity at end 1 of the relationship to delete
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException type of entity 1 is not correct
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeRelationshipBetweenEntities(String userId,
                                                  String externalSourceGUID,
                                                  String externalSourceName,
                                                  String relationshipTypeGUID,
                                                  String relationshipTypeName,
                                                  String entity1GUID,
                                                  String entity1TypeName,
                                                  String entity2GUID,
                                                  String methodName) throws UserNotAuthorizedException,
                                                                            PropertyServerException,
                                                                            InvalidParameterException
    {
        Relationship  relationship = this.getRelationshipBetweenEntities(userId,
                                                                         entity1GUID,
                                                                         entity1TypeName,
                                                                         entity2GUID,
                                                                         relationshipTypeGUID,
                                                                         relationshipTypeName,
                                                                         methodName);


        if (relationship != null)
        {
            this.removeRelationship(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    relationship,
                                    methodName);
        }
    }


    /**
     * Update the properties in the requested relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param relationship relationship to update.
     * @param relationshipProperties new properties for relationship
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateRelationshipProperties(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             Relationship       relationship,
                                             InstanceProperties relationshipProperties,
                                             String             methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String localMethodName = "updateRelationshipProperties";

        /*
         * This avoids any unnecessary updates
         */
        if ((relationship == null) ||
                ((relationship.getProperties() == null) && (relationshipProperties == null))  ||
                (((relationshipProperties != null) && (relationshipProperties.equals(relationship.getProperties())))))
        {
            return;
        }

        try
        {
            errorHandler.validateProvenance(userId,
                                            relationship,
                                            relationship.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            metadataCollection.updateRelationshipProperties(userId,
                                                            relationship.getGUID(),
                                                            relationshipProperties);
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try..catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update the properties in the requested relationship.  This method is deprecated because it is not
     * possible to verify the metadata provenance of the relationship.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship.
     * @param relationshipProperties new properties for relationship
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void updateRelationshipProperties(String                 userId,
                                             String                 relationshipGUID,
                                             InstanceProperties     relationshipProperties,
                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String localMethodName = "updateRelationshipProperties";

        try
        {
            metadataCollection.updateRelationshipProperties(userId,
                                                            relationshipGUID,
                                                            relationshipProperties);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update the properties in the requested relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param relationshipGUID unique identifier of the relationship.
     * @param relationshipProperties new properties for relationship
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */

    public void updateRelationshipProperties(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             relationshipGUID,
                                             InstanceProperties relationshipProperties,
                                             String             methodName) throws UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String localMethodName = "updateRelationshipProperties";

        try
        {
            Relationship relationship = metadataCollection.getRelationship(userId, relationshipGUID);

            if (relationship != null)
            {
                updateRelationshipProperties(userId, externalSourceGUID, externalSourceName, relationship, relationshipProperties, methodName);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update the status in the requested relationship.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship.
     * @param instanceStatus new InstanceStatus for the entity.
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void updateRelationshipStatus(String                 userId,
                                         String                 relationshipGUID,
                                         InstanceStatus         instanceStatus,
                                         String                 methodName) throws UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String localMethodName = "updateRelationshipStatus(deprecated)";

        try
        {
            metadataCollection.updateRelationshipStatus(userId, relationshipGUID, instanceStatus);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Update the status in the requested relationship.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship.
     * @param relationshipParameterName parameter name supplying relationshipGUID
     * @param relationshipTypeName type name for the relationship
     * @param instanceStatus new InstanceStatus for the entity.
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateRelationshipStatus(String         userId,
                                         String         externalSourceGUID,
                                         String         externalSourceName,
                                         String         relationshipGUID,
                                         String         relationshipParameterName,
                                         String         relationshipTypeName,
                                         InstanceStatus instanceStatus,
                                         String         methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String localMethodName = "updateRelationshipStatus";

        try
        {
            Relationship relationship = this.getRelationshipByGUID(userId, relationshipGUID, relationshipParameterName, relationshipParameterName, methodName);

            errorHandler.validateProvenance(userId,
                                            relationship,
                                            relationshipGUID,
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            metadataCollection.updateRelationshipStatus(userId, relationshipGUID, instanceStatus);
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            /*
             * These exceptions have already been correctly mapped.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }



    /**
     * Ensure that the unique relationship between two entities is established.  It is possible that there is
     * already a relationship of this type between either of the entities and another and so that needs to be
     * removed first.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param end1GUID unique identifier of the entity for end 1 of the relationship.
     * @param end1TypeName type of the entity for end 1
     * @param end2GUID unique identifier of the entity for end 2 of the relationship.
     * @param end2TypeName type of the entity for end 2
     * @param relationshipTypeGUID unique identifier of the type of relationship to create.
     * @param relationshipTypeName name of the type of relationship to create.
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateUniqueRelationshipByType(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String end1GUID,
                                               String end1TypeName,
                                               String end2GUID,
                                               String end2TypeName,
                                               String relationshipTypeGUID,
                                               String relationshipTypeName,
                                               String methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        Relationship  existingRelationshipForEntity1 = this.getUniqueRelationshipByType(userId,
                                                                                        end1GUID,
                                                                                        end1TypeName,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        existingRelationshipForEntity1 = this.removeIncompatibleRelationship(userId,
                                                                             externalSourceGUID,
                                                                             externalSourceName,
                                                                             existingRelationshipForEntity1,
                                                                             end1GUID,
                                                                             end2GUID,
                                                                             methodName);

        Relationship  existingRelationshipForEntity2 = this.getUniqueRelationshipByType(userId,
                                                                                        end2GUID,
                                                                                        end2TypeName,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        existingRelationshipForEntity2 = this.removeIncompatibleRelationship(userId,
                                                                             externalSourceGUID,
                                                                             externalSourceName,
                                                                             existingRelationshipForEntity2,
                                                                             end1GUID,
                                                                             end2GUID,
                                                                             methodName);

        if ((existingRelationshipForEntity1 == null) && (existingRelationshipForEntity2 == null))
        {
            this.createRelationship(userId,
                                    relationshipTypeGUID,
                                    externalSourceGUID,
                                    externalSourceName,
                                    end1GUID,
                                    end2GUID,
                                    null,
                                    methodName);
        }
    }


    /**
     * Remove the relationship connected to the supplied entity.  Due to the definition of the
     * relationship, only one is expected.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID unique identity of the starting entity.
     * @param entityTypeName type name of entity
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param relationshipTypeName name of the relationship's type
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException there is a problem communicating with the repository.
     */
    public void removeUniqueRelationshipByType(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String entityGUID,
                                               String entityTypeName,
                                               String relationshipTypeGUID,
                                               String relationshipTypeName,
                                               String methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        Relationship obsoleteRelationship = this.getUniqueRelationshipByType(userId,
                                                                             entityGUID,
                                                                             entityTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             methodName);

        if (obsoleteRelationship != null)
        {
            this.removeRelationship(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    obsoleteRelationship,
                                    methodName);
        }
    }


    /**
     * Validate that the supplied relationship is actually connected to the two entities who's unique
     * identifiers (guids) are supplied.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param relationship relationship to validate
     * @param end1GUID unique identifier of the entity for end 1 of the relationship.
     * @param end2GUID unique identifier of the entity for end 2 of the relationship.
     * @param methodName name of calling method.
     * @return valid relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException there is a problem communicating with the repository.
     */
    private Relationship removeIncompatibleRelationship(String       userId,
                                                        String       externalSourceGUID,
                                                        String       externalSourceName,
                                                        Relationship relationship,
                                                        String       end1GUID,
                                                        String       end2GUID,
                                                        String       methodName) throws UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (relationship == null)
        {
            return null;
        }

        EntityProxy end1Proxy = relationship.getEntityOneProxy();
        EntityProxy end2Proxy = relationship.getEntityTwoProxy();

        if ((end1GUID.equals(end1Proxy.getGUID())) && (end2GUID.equals(end2Proxy.getGUID())))
        {
            return relationship;
        }
        else
        {
            /*
             * The current relationship is between different entities.  It needs to be deleted.
             */
            this.removeRelationship(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    relationship,
                                    methodName);

            return null;
        }
    }

    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param entityTypeGUIDs list of entity types to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs list of relationship types to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @param methodName name of calling method.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships or null.
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public InstanceGraph getEntityNeighborhood(String               userId,
                                               String               entityGUID,
                                               List<String>         entityTypeGUIDs,
                                               List<String>         relationshipTypeGUIDs,
                                               List<InstanceStatus> limitResultsByStatus,
                                               List<String>         limitResultsByClassification,
                                               Date                 asOfTime,
                                               int                  level,
                                               String               methodName) throws UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String localMethodName = "getEntityNeighborhood";

        try
        {
            return metadataCollection.getEntityNeighborhood(userId,
                                                            entityGUID,
                                                            entityTypeGUIDs,
                                                            relationshipTypeGUIDs,
                                                            limitResultsByStatus,
                                                            limitResultsByClassification,
                                                            asOfTime,
                                                            level);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the metadata collection for the repository.  This is used by services that need function that is not
     * supported by this class.
     *
     * @return metadata collection for the repository
     */
    public OMRSMetadataCollection getMetadataCollection()
    {
        return metadataCollection;
    }
}
