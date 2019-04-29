/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.List;


/**
 * BasicHandler issues common calls to the open metadata repository to retrieve and store metadata.  It converts the
 * repository service exceptions into access service exceptions.
 */
class BasicHandler
{
    private ErrorHandler           errorHandler;
    private OMRSMetadataCollection metadataCollection;


    /**
     * Construct the basic handler with information needed to call the repository services and report any error.
     *
     * @param errorHandler  generates error messages and exceptions
     * @param metadataCollection  access to the repository content.
     */
    BasicHandler(ErrorHandler            errorHandler,
                 OMRSMetadataCollection  metadataCollection)
    {
       this.errorHandler = errorHandler;
       this.metadataCollection = metadataCollection;
    }


    /**
     * Validate that the supplied GUID is for a real entity.
     *
     * @param userId  user making the request.
     * @param guid  unique identifier of the entity.
     * @param entityTypeName expected type of asset.
     * @param methodName  name of method called.
     *
     * @throws InvalidParameterException entity not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    EntitySummary validateEntityGUID(String                  userId,
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
     * Create a new entity in the open metadata repository.
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
    String  createEntity(String                  userId,
                         String                  entityTypeGUID,
                         String                  entityTypeName,
                         InstanceProperties      properties,
                         String                  methodName) throws UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        try
        {
            EntityDetail newEntity = metadataCollection.addEntity(userId,
                                                                  entityTypeGUID,
                                                                  properties,
                                                                  null,
                                                                  InstanceStatus.ACTIVE);

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
     * Update an existing entity in the open metadata repository.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param properties properties for the entity
     * @param methodName name of calling method
     *
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void    updateEntity(String                  userId,
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
     * Remove an entity from the open metadata repository.
     *
     * @param userId calling user
     * @param entityTypeGUID type of entity to create
     * @param entityTypeName name of the entity's type
     * @param validatingProperties properties that should be in the entity if we have the correct one.
     * @param methodName name of calling method
     *
     * @return unique identifier of new entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  deleteEntity(String                  userId,
                         String                  obsoleteEntityGUID,
                         String                  entityTypeGUID,
                         String                  entityTypeName,
                         InstanceProperties      validatingProperties,
                         String                  methodName) throws UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String guidParameterName = "obsoleteEntityGUID";

        try
        {
            EntitySummary obsoleteEntity = this.validateEntityGUID(userId,
                                                                   obsoleteEntityGUID,
                                                                   entityTypeName,
                                                                   methodName,
                                                                   guidParameterName);

            if (obsoleteEntity != null)
            {
                return obsoleteEntity.getGUID();
            }

            errorHandler.handleNoEntity(entityTypeGUID,
                                        entityTypeName,
                                        validatingProperties,
                                        methodName);
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
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
     * Remove an entity from the repository if it is no longer connected to any other entity.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of the entity
     * @param entityTypeGUID unique identifier for the entity's type
     * @param entityTypeName name of the entity's type
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing the property server
     */
    void deleteEntityOnLastUse(String                  userId,
                               String                  entityGUID,
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
                                                                                            entityGUID,
                                                                                            null,
                                                                                            0,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            5);

            if ((relationships == null) || (relationships.isEmpty()))
            {
                metadataCollection.deleteEntity(userId, entityTypeGUID,entityTypeName, entityGUID);
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, entityGUID, entityTypeName, methodName, guidParameterName);
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
     * Return the entity a the other end of the requested relationship type.  The assumption is that this is a 0..1
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
    EntityDetail getEntityForRelationshipType(String                 userId,
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

            errorHandler.handleNoRelationship(anchorEntityGUID,
                                              anchorEntityTypeName,
                                              relationshipTypeName,
                                              methodName);
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
    EntityDetail   getEntityByGUID(String                 userId,
                                   String                 guid,
                                   String                 guidParameterName,
                                   String                 entityTypeName,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        try
        {
            return metadataCollection.getEntityDetail(userId, guid);
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
     * Return the requested entity by name.
     *
     * @param userId calling userId
     * @param nameProperties list of name properties to search on.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    List<EntityDetail>  getEntityByName(String                 userId,
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
    EntityDetail  getUniqueEntityByName(String                 userId,
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
     * Return the requested entity by name.
     *
     * @param userId calling userId
     * @param entityTypeGUID type of entity required
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of returned entities
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    List<EntityDetail>  getEntityByType(String                 userId,
                                        String                 entityTypeGUID,
                                        int                    startingFrom,
                                        int                    maximumResults,
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
                                                             maximumResults);
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
    List<Relationship> getRelationshipsByType(String                 userId,
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
    Relationship getRelationshipBetweenEntities(String                 userId,
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
    List<Relationship> getPagedRelationshipsByType(String                 userId,
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
    Relationship getUniqueRelationshipByType(String                 userId,
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

            errorHandler.handleNoRelationship(anchorEntityGUID,
                                              anchorEntityTypeName,
                                              relationshipTypeName,
                                              methodName);
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
    void createRelationship(String                  userId,
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
     * Delete a relationship between two entities.
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
    void deleteRelationship(String                  userId,
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
    void deleteRelationshipBetweenEntities(String                  userId,
                                           String                  relationshipTypeGUID,
                                           String                  relationshipTypeName,
                                           String                  entity1GUID,
                                           String                  entity1TypeName,
                                           String                  entity2GUID,
                                           String                  methodName) throws UserNotAuthorizedException,
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
            this.deleteRelationship(userId,
                                    relationshipTypeGUID,
                                    relationshipTypeName,
                                    relationship.getGUID(),
                                    methodName);
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
    void updateUniqueRelationshipByType(String                 userId,
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
    void removeUniqueRelationshipByType(String                 userId,
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
            this.deleteRelationship(userId,
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
            this.deleteRelationship(userId,
                                    relationshipTypeGUID,
                                    relationshipTypeName,
                                    relationship.getGUID(),
                                    methodName);

            return null;
        }
    }
}
