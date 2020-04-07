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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
            errorHandler.handleRepositoryError(error, methodName);
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
        try
        {
            EntitySummary entity = metadataCollection.getEntitySummary(userId, guid);

            errorHandler.validateInstanceType(userId,
                                             entity,
                                             guidParameterName,
                                             entityTypeName,
                                             methodName);

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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
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
                                InstanceProperties      properties,
                                List<Classification>    initialClassifications,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createEntity(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 properties,
                                 initialClassifications,
                                 InstanceStatus.ACTIVE,
                                 methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the specified instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
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
                                InstanceProperties      properties,
                                InstanceStatus          instanceStatus,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
       return this.createEntity(userId,
                                entityTypeGUID,
                                entityTypeName,
                                properties,
                                null,
                                instanceStatus,
                                methodName);
    }


    /**
     * Create a new entity in the open metadata repository with the specified instance status.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
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
                                InstanceProperties      properties,
                                List<Classification>    initialClassifications,
                                InstanceStatus          instanceStatus,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        try
        {
            EntityDetail newEntity = metadataCollection.addEntity(userId,
                                                                  entityTypeGUID,
                                                                  properties,
                                                                  initialClassifications,
                                                                  instanceStatus);

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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }

    /**
     * Create a new entity from an external source in the open metadata repository with the ACTIVE instance status.
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
    public String  createExternalEntity(String                  userId,
                                        String                  entityTypeGUID,
                                        String                  entityTypeName,
                                        String                  externalSourceGUID,
                                        String                  externalSourceName,
                                        InstanceProperties      properties,
                                        String                  methodName) throws UserNotAuthorizedException,
            PropertyServerException
    {
        return this.createExternalEntity(userId,
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
     * Create a new entity from an external source in the open metadata repository with the ACTIVE instance status.
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
    public String  createExternalEntity(String                  userId,
                                        String                  entityTypeGUID,
                                        String                  entityTypeName,
                                        String                  externalSourceGUID,
                                        String                  externalSourceName,
                                        InstanceProperties      properties,
                                        List<Classification>    initialClassifications,
                                        String                  methodName) throws UserNotAuthorizedException,
            PropertyServerException
    {
        return this.createExternalEntity(userId,
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
        return this.createExternalEntity(userId,
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
     * Create a new entity from an external source in the open metadata repository with the specified instance status.
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
    public String  createExternalEntity(String                  userId,
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
        try
        {
            EntityDetail newEntity = metadataCollection.addExternalEntity(userId,
                                                        entityTypeGUID,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        properties,
                                                        initialClassifications,
                                                        instanceStatus);

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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }

    /**
     * Remove an entity attached to an anchor. There should be only one instance
     * of this relationship.
     *
     * @param userId calling user
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param anchorEntityTypeName name of anchor entity's type
     * @param anchorRelationshipTypeGUID unique identifier for the relationship's type
     * @param anchorRelationshipTypeName unique name for the relationship's type
     * @param anchorRelationshipProperties properties from relationship
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
    public String addUniqueAttachedEntityToAnchor(String                  userId,
                                                  String                  anchorEntityGUID,
                                                  String                  anchorEntityTypeName,
                                                  String                  anchorRelationshipTypeGUID,
                                                  String                  anchorRelationshipTypeName,
                                                  InstanceProperties      anchorRelationshipProperties,
                                                  String                  attachedEntityTypeGUID,
                                                  String                  attachedEntityTypeName,
                                                  InstanceProperties      attachedEntityProperties,
                                                  String                  methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String guidParameterName = "anchorEntityGUID";

        this.validateEntityGUID(userId,
                                anchorEntityGUID,
                                anchorEntityTypeName,
                                methodName,
                                guidParameterName);

        String  attachedEntityGUID = this.createEntity(userId,
                                                       attachedEntityTypeGUID,
                                                       attachedEntityTypeName,
                                                       attachedEntityProperties,
                                                       methodName);

        if (attachedEntityGUID != null)
        {
            this.createRelationship(userId,
                                    anchorRelationshipTypeGUID,
                                    anchorEntityGUID,
                                    attachedEntityGUID,
                                    anchorRelationshipProperties,
                                    methodName);
        }

        return attachedEntityGUID;
    }


    /**
     * Update an existing entity in the open metadata repository.
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
    public void    updateEntity(String                  userId,
                                String                  entityGUID,
                                String                  entityTypeGUID,
                                String                  entityTypeName,
                                InstanceProperties      properties,
                                String                  methodName) throws UserNotAuthorizedException,
                                                                           PropertyServerException
    {
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
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Update an existing entity status in the open metadata repository.
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
    public void    updateEntityStatus(String                  userId,
                                      String                  entityGUID,
                                      String                  entityTypeGUID,
                                      String                  entityTypeName,
                                      InstanceStatus          instanceStatus,
                                      String                  methodName) throws UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
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
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Add a new classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param properties properties for the classification
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    classifyEntity(String                  userId,
                                  String                  entityGUID,
                                  String                  classificationTypeGUID,
                                  String                  classificationTypeName,
                                  InstanceProperties      properties,
                                  String                  methodName) throws UserNotAuthorizedException,
                                                                             PropertyServerException
    {
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
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Update the properties of an existing classification to an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param properties properties for the classification
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    reclassifyEntity(String                  userId,
                                    String                  entityGUID,
                                    String                  classificationTypeGUID,
                                    String                  classificationTypeName,
                                    InstanceProperties      properties,
                                    String                  methodName) throws UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        try
        {
            EntityDetail newEntity = metadataCollection.updateEntityClassification(userId,
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
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Remove an existing classification from an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to update
     * @param classificationTypeGUID type of classification to create
     * @param classificationTypeName name of the classification's type
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void    declassifyEntity(String                  userId,
                                    String                  entityGUID,
                                    String                  classificationTypeGUID,
                                    String                  classificationTypeName,
                                    String                  methodName) throws UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        try
        {
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
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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

                this.removeIsolatedEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Remove an entity from the repository if it is no longer connected to any other entity.
     *
     * @param userId calling user
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
    public void removeEntityOnLastUse(String                  userId,
                                      String                  obsoleteEntityGUID,
                                      String                  guidParameterName,
                                      String                  entityTypeGUID,
                                      String                  entityTypeName,
                                      String                  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
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
                this.removeIsolatedEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
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
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
        {
            this.purgeEntity(userId, entityTypeGUID,entityTypeName, obsoleteEntityGUID, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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
    public void removeIsolatedEntity(String           userId,
                                     String           obsoleteEntityGUID,
                                     String           entityTypeGUID,
                                     String           entityTypeName,
                                     String           methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        // Todo - validate that the entity is in fact isolated.
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
            errorHandler.handleRepositoryError(error, methodName);
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
    private void  purgeEntity(String           userId,
                              String           obsoleteEntityGUID,
                              String           entityTypeGUID,
                              String           entityTypeName,
                              String           methodName) throws UserNotAuthorizedException,
                                                                  PropertyServerException
    {
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
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Remove an entity attached to an anchor. There should be only one instance
     * of this relationship.
     *
     * @param userId calling user
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param anchorEntityTypeName name of anchor entity's type
     * @param anchorRelationshipTypeGUID unique identifier for the relationship's type
     * @param anchorRelationshipTypeName unique name for the relationship's type
     * @param attachedEntityTypeGUID unique identifier for the attached entity's type
     * @param attachedEntityTypeName name of the attached entity's type
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeUniqueEntityTypeFromAnchor(String                  userId,
                                                 String                  anchorEntityGUID,
                                                 String                  anchorEntityTypeName,
                                                 String                  anchorRelationshipTypeGUID,
                                                 String                  anchorRelationshipTypeName,
                                                 String                  attachedEntityTypeGUID,
                                                 String                  attachedEntityTypeName,
                                                 String                  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        Relationship relationship = getUniqueRelationshipByType(userId,
                                                                anchorEntityGUID,
                                                                anchorEntityTypeName,
                                                                anchorRelationshipTypeGUID,
                                                                anchorRelationshipTypeName,
                                                                methodName);

        EntityDetail entity = getEntityForRelationshipType(userId,
                                                           anchorEntityGUID,
                                                           anchorEntityTypeName,
                                                           anchorRelationshipTypeGUID,
                                                           anchorRelationshipTypeName,
                                                           methodName);
        if (relationship != null)
        {
            this.removeRelationship(userId,
                                    anchorRelationshipTypeGUID,
                                    anchorRelationshipTypeName,
                                    relationship.getGUID(),
                                    methodName);
        }

        if (entity != null)
        {
            String attachedEntityGUID = entity.getGUID();

            try
            {
                metadataCollection.deleteEntity(userId,
                                                attachedEntityTypeGUID,
                                                attachedEntityTypeName,
                                                attachedEntityGUID);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
            {
                final String guidParameterName = "attachedEntityGUID";

                errorHandler.handleUnknownEntity(error,
                                                 anchorEntityGUID,
                                                 attachedEntityTypeName,
                                                 methodName,
                                                 guidParameterName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
            {
                this.purgeEntity(userId,
                                 attachedEntityTypeGUID,
                                 attachedEntityTypeGUID,
                                 attachedEntityGUID,
                                 methodName);
            }
            catch (Throwable error)
            {
                errorHandler.handleRepositoryError(error, methodName);
            }
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
                        errorHandler.validateInstanceType(userId, entity, "<null>", entityTypeName, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
                                                             String                 anchorEntityGUID,
                                                             String                 anchorEntityTypeName,
                                                             String                 relationshipTypeGUID,
                                                             String                 relationshipTypeName,
                                                             int                    startingFrom,
                                                             int                    pageSize,
                                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
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
                    EntityProxy requiredEnd = relationship.getEntityOneProxy();
                    if (anchorEntityGUID.equals(requiredEnd.getGUID()))
                    {
                        requiredEnd = relationship.getEntityTwoProxy();
                    }

                    results.add(metadataCollection.getEntityDetail(userId, requiredEnd.getGUID()));
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
            errorHandler.handleRepositoryError(error, methodName);
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
                                                               int startingFrom,
                                                               int pageSize,
                                                               String methodName) throws    UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
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
            errorHandler.handleRepositoryError(error, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
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
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachedEntityTypeGUID  identifier for the relationship to follow
     * @param attachedEntityTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getAttachedEntityFromUser(String  userId,
                                                  String  anchorEntityGUID,
                                                  String  anchorEntityTypeName,
                                                  String  relationshipTypeGUID,
                                                  String  relationshipTypeName,
                                                  String  attachedEntityTypeGUID,// todo error message
                                                  String  attachedEntityTypeName,// todo error message
                                                  String  methodName) throws UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        try
        {
            boolean      moreResultsAvailable = true;
            int          startNextQueryFrom = 0;

            while (moreResultsAvailable)
            {
                List<EntityDetail> retrievedEntities = this.getEntitiesForRelationshipType(userId, anchorEntityGUID, anchorEntityTypeName, relationshipTypeGUID, relationshipTypeName, startNextQueryFrom, maxPageSize, methodName);

                if (retrievedEntities != null)
                {
                    moreResultsAvailable = (retrievedEntities.size() == maxPageSize);
                    startNextQueryFrom   = startNextQueryFrom + maxPageSize;

                    for (EntityDetail entity : retrievedEntities)
                    {
                        if (entity != null)
                        {
                            if ((userId.equals(entity.getCreatedBy()) || (userId.equals(entity.getUpdatedBy())) || ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                            {
                                return entity;
                            }
                        }
                    }
                }
                else
                {
                    moreResultsAvailable = false;

                    if (log.isDebugEnabled())
                    {
                        log.debug("No entities of type " + attachedEntityTypeName + " found for " + anchorEntityTypeName + " entity " + anchorEntityGUID);
                    }
                }
            }
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type that were created or
     * edited by the requesting user.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
     */
    public List<EntityDetail> getAttachedEntitiesFromUser(String  userId,
                                                          String  anchorEntityGUID,
                                                          String  anchorEntityTypeName,
                                                          String  relationshipTypeGUID,
                                                          String  relationshipTypeName,
                                                          String  attachedEntityTypeGUID,// todo error message
                                                          String  attachedEntityTypeName,// todo error message
                                                          int     startingFrom,
                                                          int     pageSize,
                                                          String  methodName) throws UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        List<EntityDetail> results = new ArrayList<>();

        try
        {
            boolean      moreResultsAvailable = true;
            int          startNextQueryFrom = startingFrom;

            while (moreResultsAvailable && (results.size() < pageSize))
            {
                List<EntityDetail> retrievedEntities = this.getEntitiesForRelationshipType(userId, anchorEntityGUID, anchorEntityTypeName, relationshipTypeGUID, relationshipTypeName, startNextQueryFrom, pageSize, methodName);

                if (retrievedEntities != null)
                {
                    moreResultsAvailable = (retrievedEntities.size() == pageSize);
                    startNextQueryFrom = startNextQueryFrom + pageSize;

                    for (EntityDetail entity : retrievedEntities)
                    {
                        if (entity != null)
                        {
                            if ((userId.equals(entity.getCreatedBy()) || (userId.equals(entity.getUpdatedBy())) || ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                            {
                                results.add(entity);
                            }
                        }
                    }
                }
                else
                {
                    moreResultsAvailable = false;

                    if (log.isDebugEnabled())
                    {
                        log.debug("No entities of type " + attachedEntityTypeName + " found for " + anchorEntityTypeName + " entity " + anchorEntityGUID);
                    }
                }
            }
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
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
     * anchor entity.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     * @return list of entity proxies
     */
    public List<EntityProxy>  getRelatedEntityProxies(String                 userId,
                                                      String                 anchorEntityGUID,
                                                      String                 anchorEntityTypeName,
                                                      String                 relationshipTypeGUID,
                                                      String                 relationshipTypeName,
                                                      int                    startingFrom,
                                                      int                    pageSize,
                                                      String                 methodName) throws UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                       anchorEntityGUID,
                                                                       anchorEntityTypeName,
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
                    EntityProxy relatedEntityProxy = this.getOtherEnd(anchorEntityGUID, relationship);

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
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param relationship relationship to another entity
     * @return proxy to the other entity.
     */
    public  EntityProxy  getOtherEnd(String                 anchorEntityGUID,
                                     Relationship           relationship)
    {
        if (relationship != null)
        {
            // todo this is a good place to add validation that the entity proxies are ok

            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                if (anchorEntityGUID.equals(entityProxy.getGUID()))
                {
                    entityProxy = relationship.getEntityTwoProxy();
                }
            }

            return entityProxy;
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
    public EntityDetail getEntityForRelationship(String                 userId,
                                                 EntityProxy            requiredEnd,
                                                 String                 methodName) throws UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }




    /**
     * Return the entity at the other end of the requested relationship type.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param methodName  name of calling method
     * @return retrieved entity or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getEntityForRelationshipType(String                 userId,
                                                     String                 anchorEntityGUID,
                                                     String                 anchorEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            anchorEntityGUID,
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
                    if (anchorEntityGUID.equals(requiredEnd.getGUID()))
                    {
                        requiredEnd = relationship.getEntityTwoProxy();
                    }

                    return metadataCollection.getEntityDetail(userId, requiredEnd.getGUID());
                }
                else if (relationships.size() > 1)
                {
                    errorHandler.handleAmbiguousRelationships(anchorEntityGUID,
                                                              anchorEntityTypeName,
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
            errorHandler.handleRepositoryError(error, methodName);
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
        try
        {
            EntityDetail entity = metadataCollection.getEntityDetail(userId, guid);

            errorHandler.validateInstanceType(userId, entity, guidParameterName, entityTypeName, methodName);

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
            errorHandler.handleRepositoryError(error, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
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
            errorHandler.handleRepositoryError(error, methodName);
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

            if ((returnedEntities == null) || returnedEntities.isEmpty())
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
            errorHandler.handleRepositoryError(error, methodName);
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
        try
        {
            return metadataCollection.findEntitiesByProperty(userId,
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
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     * The list is expected to be small.
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
    public List<Relationship> getRelationshipsByType(String                 userId,
                                                     String                 anchorEntityGUID,
                                                     String                 anchorEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return this.getRelationshipsByType(userId,
                                           anchorEntityGUID,
                                           anchorEntityTypeName,
                                           relationshipTypeGUID,
                                           relationshipTypeName,
                                           0,
                                           maxPageSize,
                                           methodName);
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     * The list is expected to be small.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
                                                     String                 anchorEntityGUID,
                                                     String                 anchorEntityTypeName,
                                                     String                 relationshipTypeGUID,
                                                     String                 relationshipTypeName,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
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

            if ((relationships == null) || (relationships.isEmpty()))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No relationships of type " + relationshipTypeName +
                              " found for " + anchorEntityTypeName + " entity " + anchorEntityGUID);
                }

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
            errorHandler.handleRepositoryError(error, methodName);
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
        try
        {
            List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                            anchorEntityGUID,
                                                                                            relationshipTypeGUID,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            100);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                errorHandler.handleNoRelationship(anchorEntityGUID,
                                                  anchorEntityTypeName,
                                                  relationshipTypeName,
                                                  methodName);
            }

            return relationships;
        }
        catch (PropertyServerException  error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }



    /**
     * Count the number of relationships of a specific type attached to an anchor entity.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
                                                String                 anchorEntityGUID,
                                                String                 anchorEntityTypeName,
                                                String                 relationshipTypeGUID,
                                                String                 relationshipTypeName,
                                                String                 methodName) throws PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                       anchorEntityGUID,
                                                                       anchorEntityTypeName,
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
     * Return the list of relationships of the requested type connected to the anchor entity.
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
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getRelationshipBetweenEntities(String                 userId,
                                                       String                 entity1GUID,
                                                       String                 entity1TypeName,
                                                       String                 entity2GUID,
                                                       String                 relationshipTypeGUID,
                                                       String                 relationshipTypeName,
                                                       String                 methodName) throws UserNotAuthorizedException,
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
            for (Relationship  relationship : entity1Relationships)
            {
                if (relationship != null)
                {
                    EntityProxy  entity2Proxy = relationship.getEntityTwoProxy();

                    if (entity2Proxy != null)
                    {
                        if (entity2GUID.equals(entity2Proxy.getGUID()))
                        {
                            return relationship;
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
    public List<Relationship> getPagedRelationshipsByType(String                 userId,
                                                          String                 anchorEntityGUID,
                                                          String                 anchorEntityTypeName,
                                                          String                 relationshipTypeGUID,
                                                          String                 relationshipTypeName,
                                                          int                    startingFrom,
                                                          int                    maximumResults,
                                                          String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
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
                                                                                            maximumResults);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                errorHandler.handleNoRelationship(anchorEntityGUID,
                                                  anchorEntityTypeName,
                                                  relationshipTypeName,
                                                  methodName);
            }

            return relationships;
        }
        catch (PropertyServerException  error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the anchor entity.
     * The assumption is that this is a 0..1 relationship so one relationship (or null) is returned.
     * If lots of relationships are found then the PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
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
                                                    String                 anchorEntityGUID,
                                                    String                 anchorEntityTypeName,
                                                    String                 relationshipTypeGUID,
                                                    String                 relationshipTypeName,
                                                    String                 methodName) throws UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        try
        {
            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           anchorEntityGUID,
                                                                           anchorEntityTypeName,
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
                    errorHandler.handleAmbiguousRelationships(anchorEntityGUID,
                                                              anchorEntityTypeName,
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
            errorHandler.handleRepositoryError(error, methodName);
        }

        return null;
    }


    /**
     * Create a relationship between two entities.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param end1GUID entity to store at end 1
     * @param end2GUID entity to store at end 2
     * @param relationshipProperties properties for the relationship
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void createRelationship(String                  userId,
                                   String                  relationshipTypeGUID,
                                   String                  end1GUID,
                                   String                  end2GUID,
                                   InstanceProperties      relationshipProperties,
                                   String                  methodName) throws UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        try
        {
            metadataCollection.addRelationship(userId,
                                               relationshipTypeGUID,
                                               relationshipProperties,
                                               end1GUID,
                                               end2GUID,
                                               InstanceStatus.ACTIVE);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Create a relationship between two entities.
     *
     * @param userId calling user
     * @param end1TypeName unique name of the end 1's type
     * @param end1GUID entity to store at end 1
     * @param end2GUID entity to store at end 2
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param relationshipTypeName unique name of the relationship's type
     * @param relationshipProperties properties for the relationship
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void ensureRelationship(String                  userId,
                                   String                  end1TypeName,
                                   String                  end1GUID,
                                   String                  end2GUID,
                                   String                  relationshipTypeGUID,
                                   String                  relationshipTypeName,
                                   InstanceProperties      relationshipProperties,
                                   String                  methodName) throws UserNotAuthorizedException,
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
           this.createRelationship(userId, relationshipTypeGUID, end1GUID, end2GUID, relationshipProperties, methodName);
       }
       else
       {
           if ((relationshipProperties != null) || (relationship.getProperties() != null))
           {
               /*
                * This ensures the properties are identical.  It may create unnecessary update events if the properties have not changed.
                * This could therefore be made more efficient if additional validation of whether the properties have changed is made.
                */
               // TODO make more efficient by avoiding unnecessary updates?
               this.updateRelationshipProperties(userId, relationship.getGUID(), relationshipProperties, methodName);
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
        try
        {
            metadataCollection.addExternalRelationship(userId,
                                                       relationshipTypeGUID,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       relationshipProperties,
                                                       end1GUID,
                                                       end2GUID,
                                                       InstanceStatus.ACTIVE);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
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
    public void removeRelationship(String                  userId,
                                   String                  relationshipTypeGUID,
                                   String                  relationshipTypeName,
                                   String                  relationshipGUID,
                                   String                  methodName) throws UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        try
        {
            metadataCollection.deleteRelationship(userId,
                                                  relationshipTypeGUID,
                                                  relationshipTypeName,
                                                  relationshipGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException  error)
        {
            this.purgeRelationship(userId, relationshipTypeGUID, relationshipTypeName, relationshipGUID, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Purge a relationship between two entities.
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
    public void purgeRelationship(String                  userId,
                                  String                  relationshipTypeGUID,
                                  String                  relationshipTypeName,
                                  String                  relationshipGUID,
                                  String                  methodName) throws UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        try
        {
            metadataCollection.purgeRelationship(userId,
                                                 relationshipTypeGUID,
                                                 relationshipTypeName,
                                                 relationshipGUID);

            auditLog.logMessage(methodName,
                                RepositoryHandlerAuditCode.RELATIONSHIP_PURGED.getMessageDefinition(relationshipGUID,
                                                                                                    relationshipTypeName,
                                                                                                    relationshipTypeGUID,
                                                                                                    methodName,
                                                                                                    metadataCollection.getMetadataCollectionId(userId)));
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Remove all relationships of a certain type anchored at a particular entity.
     *
     * @param userId calling user
     * @param anchorEntityGUID identifier of starting entity
     * @param anchorEntityTypeName type of entity
     * @param relationshipTypeGUID unique identifier of the relationship type
     * @param relationshipTypeName unique name of the relationship type
     * @param methodName calling method
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeAllRelationshipsOfType(String                 userId,
                                             String                 anchorEntityGUID,
                                             String                 anchorEntityTypeName,
                                             String                 relationshipTypeGUID,
                                             String                 relationshipTypeName,
                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        try
        {
            boolean moreToReceive = true;

            while (moreToReceive)
            {
                /*
                 * Because removing relationships, the startingFrom value is always 0 because retrieving what is left.
                 */
                List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                               anchorEntityGUID,
                                                                               anchorEntityTypeName,
                                                                               relationshipTypeGUID,
                                                                               relationshipTypeName,
                                                                               0,
                                                                               maxPageSize,
                                                                               methodName);

                if ((relationships != null) && (! relationships.isEmpty()))
                {
                    for (Relationship relationship : relationships)
                    {
                        if (relationship != null)
                        {
                            this.removeRelationship(userId,
                                                    relationshipTypeGUID,
                                                    relationshipTypeName,
                                                    relationship.getGUID(),
                                                    methodName);
                        }
                    }
                }
                else
                {
                    moreToReceive = false;
                }
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName);
        }

    }


    /**
     * Delete a relationship between two entities.
     *
     * @param userId calling user
     * @param relationshipTypeGUID unique identifier of the type of relationship to delete
     * @param relationshipTypeName name of the type of relationship to delete
     * @param entity1GUID unique identifier of the entity at end 1 of the relationship to delete
     * @param entity1TypeName type name of the entity at end 1 of the relationship to delete
     * @param entity2GUID unique identifier of the entity at end 1 of the relationship to delete
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeRelationshipBetweenEntities(String   userId,
                                                  String   relationshipTypeGUID,
                                                  String   relationshipTypeName,
                                                  String   entity1GUID,
                                                  String   entity1TypeName,
                                                  String   entity2GUID,
                                                  String   methodName) throws UserNotAuthorizedException,
                                                                              PropertyServerException
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
                                    relationshipTypeGUID,
                                    relationshipTypeName,
                                    relationship.getGUID(),
                                    methodName);
        }
    }


    /**
     * Update the properties in the requested relationship.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship.
     * @param relationshipProperties new properties for relationship
     * @param methodName name of calling method.
     *
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateRelationshipProperties(String                 userId,
                                             String                 relationshipGUID,
                                             InstanceProperties     relationshipProperties,
                                             String                 methodName) throws UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
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
            errorHandler.handleRepositoryError(error, methodName);
        }
    }


    /**
     * Ensure that the unique relationship between two entities is established.  It is possible that there is
     * already a relationship of this type between either of the entities and another and so that needs to be
     * removed first.
     *
     * @param userId calling user
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
    public void updateUniqueRelationshipByType(String                 userId,
                                               String                 end1GUID,
                                               String                 end1TypeName,
                                               String                 end2GUID,
                                               String                 end2TypeName,
                                               String                 relationshipTypeGUID,
                                               String                 relationshipTypeName,
                                               String                 methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        Relationship  existingRelationshipForEntity1 = this.getUniqueRelationshipByType(userId,
                                                                                        end1GUID,
                                                                                        end1TypeName,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        existingRelationshipForEntity1 = this.removeIncompatibleRelationship(userId,
                                                                             existingRelationshipForEntity1,
                                                                             end1GUID,
                                                                             end2GUID,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             methodName);

        Relationship  existingRelationshipForEntity2 = this.getUniqueRelationshipByType(userId,
                                                                                        end2GUID,
                                                                                        end2TypeName,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        methodName);

        existingRelationshipForEntity2 = this.removeIncompatibleRelationship(userId,
                                                                             existingRelationshipForEntity2,
                                                                             end1GUID,
                                                                             end2GUID,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             methodName);

        if ((existingRelationshipForEntity1 == null) && (existingRelationshipForEntity2 == null))
        {
            this.createRelationship(userId,
                                    relationshipTypeGUID,
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
     * @param entityGUID unique identity of the anchor entity.
     * @param entityTypeName type name of entity
     * @param relationshipTypeGUID unique identifier of the relationship's type
     * @param relationshipTypeName name of the relationship's type
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException there is a problem communicating with the repository.
     */
    public void removeUniqueRelationshipByType(String                 userId,
                                               String                 entityGUID,
                                               String                 entityTypeName,
                                               String                 relationshipTypeGUID,
                                               String                 relationshipTypeName,
                                               String                 methodName) throws UserNotAuthorizedException,
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
                                    relationshipTypeGUID,
                                    relationshipTypeName,
                                    obsoleteRelationship.getGUID(),
                                    methodName);
        }
    }


    /**
     * Validate that the supplied relationship is actually connected to the two entities who's unique
     * identifiers (guids) are supplied.
     *
     * @param userId calling user
     * @param relationship relationship to validate
     * @param end1GUID unique identifier of the entity for end 1 of the relationship.
     * @param end2GUID unique identifier of the entity for end 2 of the relationship.
     * @param relationshipTypeGUID unique identifier of the type of relationship to create.
     * @param relationshipTypeName name of the type of relationship to create.
     * @param methodName name of calling method.
     * @return valid relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException there is a problem communicating with the repository.
     */
    private Relationship removeIncompatibleRelationship(String                 userId,
                                                        Relationship           relationship,
                                                        String                 end1GUID,
                                                        String                 end2GUID,
                                                        String                 relationshipTypeGUID,
                                                        String                 relationshipTypeName,
                                                        String                 methodName) throws UserNotAuthorizedException,
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
                                    relationshipTypeGUID,
                                    relationshipTypeName,
                                    relationship.getGUID(),
                                    methodName);

            return null;
        }
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
