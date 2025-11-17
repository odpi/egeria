/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RepositoryHandler issues common calls to the open metadata repository to retrieve and store metadata.  It converts the
 * repository service exceptions into access service exceptions.  It is also responsible for validating provenance
 * and ensuring elements are only returned if they have appropriate effectivity dates.  If no effectivity date is passed as a parameter,
 * then the time is assumed to be now. If a null effectivity date is supplied then it is set to "any".
 */
public class RepositoryHandler
{
    private static final String consolidatedDuplicateLinkName = "ConsolidatedDuplicateLink";
    private static final String peerDuplicateLink             = "PeerDuplicateLink";

    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryErrorHandler  errorHandler;
    private final OMRSRepositoryHelper    repositoryHelper;
    private final OMRSMetadataCollection  metadataCollection;
    private final int                     maxPageSize;
    private final AuditLog                auditLog;

    private static final Logger log = LoggerFactory.getLogger(RepositoryHandler.class);


    /**
     * Construct the basic handler with information needed to call the repository services and report any error.
     *
     * @param auditLog           logging destination
     * @param repositoryHelper   helper class for manipulating OMRS objects
     * @param errorHandler       generates error messages and exceptions
     * @param metadataCollection access to the repository content.
     * @param maxPageSize        maximum number of instances that can be returned on a single call
     */
    public RepositoryHandler(AuditLog               auditLog,
                             OMRSRepositoryHelper   repositoryHelper,
                             RepositoryErrorHandler errorHandler,
                             OMRSMetadataCollection metadataCollection,
                             int                    maxPageSize)
    {
        this.auditLog = auditLog;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.metadataCollection = metadataCollection;
        this.maxPageSize = maxPageSize;
        this.invalidParameterHandler = new InvalidParameterHandler();

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Return a flag to indicate whether the effectivity dates in the properties of an element indicate that the element is not
     * effective at the supplied effectiveTime.  If a null effectiveTime is supplied then it is assumed to be "any".
     *
     * @param properties    properties from the element
     * @param effectiveTime time to measure effectivity against
     *
     * @return boolean - true = element is effective; false = element not effective
     */
    boolean isCorrectEffectiveTime(InstanceProperties properties,
                                   Date               effectiveTime)
    {
        if (effectiveTime == null)
        {
            return true;
        }

        if (properties == null)
        {
            return true;
        }

        if ((properties.getEffectiveFromTime() != null) && (effectiveTime.before(properties.getEffectiveFromTime())))
        {
            return false;
        }

        return ! ((properties.getEffectiveToTime() != null) && (effectiveTime.after(properties.getEffectiveToTime())));
    }


    /**
     * Validate that the supplied GUID is for a real entity and map exceptions if not.
     *
     * @param userId            user making the request.
     * @param guid              unique identifier of the entity.
     * @param guidParameterName name of parameter that passed the guid
     * @param entityTypeName    expected type of asset.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param methodName        name of method called.
     *
     * @return retrieved entity
     *
     * @throws InvalidParameterException  entity not known/not effective
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail validateEntityGUID(String userId,
                                           String guid,
                                           String guidParameterName,
                                           String entityTypeName,
                                           Date   asOfTime,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String localMethodName = "validateEntityGUID";

        try
        {
            EntityDetail entity;

            if (asOfTime == null)
            {
                entity = metadataCollection.getEntityDetail(userId, guid);
            }
            else
            {
                entity = metadataCollection.getEntityDetail(userId, guid, asOfTime);
            }

            if (entity != null)
            {
                errorHandler.validateInstanceType(entity, entityTypeName, methodName, localMethodName);
            }
            
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
        catch (InvalidParameterException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Validate an entity retrieved from the repository is suitable for the requester.
     * There are three considerations: (1) Are the effectivity dates in the entity's properties indicating that this entity
     * is effective at this time? (2) If the entity is a memento (ie it has the Memento classification attached) then it should only be
     * returned if the request is for lineage. (3) If the entity is a known duplicate (ie it has the KnownDuplicate classification attached)
     * and this request is not for duplicate processing then retrieve and combine the duplicate entities.
     *
     * @param userId calling user
     * @param entity retrieved entity
     * @param entityTypeName unique name for type of entity
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime   time when the examined elements must be effective
     * @param methodName calling method
     * @return entity to return to the caller - or null to mean the retrieved entity is not appropriate for the caller
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the repository services
     */
    public EntityDetail validateRetrievedEntity(String               userId,
                                                EntityDetail         entity,
                                                String               entityTypeName,
                                                List<InstanceStatus> limitResultsByStatus,
                                                Date                 asOfTime,
                                                SequencingOrder      sequencingOrder,
                                                String               sequencingPropertyName,
                                                boolean              forLineage,
                                                boolean              forDuplicateProcessing,
                                                Date                 effectiveTime,
                                                String               methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (entity == null)
        {
            log.debug("no supplied entity");
            return null;
        }

        DuplicateEntityIterator duplicateEntityIterator = new DuplicateEntityIterator(this,
                                                                                      errorHandler,
                                                                                      invalidParameterHandler,
                                                                                      userId,
                                                                                      entity,
                                                                                      entityTypeName,
                                                                                      limitResultsByStatus,
                                                                                      asOfTime,
                                                                                      sequencingOrder,
                                                                                      sequencingPropertyName,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);

        /*
         * This may be the original entity, the consolidated entity or null if the entity should be ignored.
         * The subsequent call to duplicateEntityIterator.morePeersToReceive() will only return true if
         * there is no consolidated entity and the entity has peer duplicates.
         */
        EntityDetail resultingEntity = duplicateEntityIterator.getNextPeer();

        if ((resultingEntity != null) && (duplicateEntityIterator.morePeersToReceive()))
        {
            log.debug("Deduping");

            /*
             * Consolidating the classification across the peer entities.
             */
            resultingEntity = new EntityDetail(resultingEntity);

            Map<String, Classification> classificationMap = new HashMap<>();

            /*
             * Begin by filling the classification map with the classifications from the original entity
             */
            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        classificationMap.put(classification.getName(), classification);
                    }
                }
            }

            /*
             * The aim of this process is to take the latest classification of the peers
             */
            while (duplicateEntityIterator.morePeersToReceive())
            {
                EntityDetail peerEntity = duplicateEntityIterator.getNextPeer();

                if (peerEntity != null)
                {
                    /*
                     * Use the latest entity
                     */
                    resultingEntity = getLatest(resultingEntity, peerEntity);

                    if (peerEntity.getClassifications() != null)
                    {
                        for (Classification peerClassification : peerEntity.getClassifications())
                        {
                            if (peerClassification != null)
                            {
                                Classification existingClassification = classificationMap.get(peerClassification.getName());

                                if (existingClassification == null)
                                {
                                    classificationMap.put(peerClassification.getName(), peerClassification);
                                }
                                else if (errorHandler.validateIsLatestUpdate(existingClassification, peerClassification))
                                {
                                    classificationMap.put(peerClassification.getName(), peerClassification);
                                }
                            }
                        }
                    }
                }

                resultingEntity.setClassifications(new ArrayList<>(classificationMap.values()));
            }
        }

        if (log.isDebugEnabled())
        {
            if (resultingEntity == null)
            {
                log.debug("no resulting entity");
            }
            else
            {
                log.debug("Resulting entity: " + resultingEntity.getGUID());
            }
        }

        return resultingEntity;
    }


    /**
     * Validate a relationship retrieved from the repository is suitable for the requester.
     * There are two considerations: (1) Are the effectivity dates in the relationship's properties indicating that this relationship
     * is effective at this time? (2) is the type of the relationship correct.
     *
     * @param relationship retrieved relationship
     * @param relationshipTypeName unique name for type of relationship
     * @param effectiveTime   time when the examined elements must be effective
     * @param methodName calling method
     * @return relationship to return to the caller - or null to mean the retrieved relationship is not appropriate for the caller
     */
    public Relationship validateRetrievedRelationship(Relationship relationship,
                                                      String       relationshipTypeName,
                                                      Date         effectiveTime,
                                                      String       methodName)
    {
        if (relationship == null)
        {
            log.debug("no supplied relationship");
            return null;
        }

        if ((isCorrectEffectiveTime(relationship.getProperties(), effectiveTime) &&
                (errorHandler.isInstanceATypeOf(relationship, relationshipTypeName, methodName))))
        {
            return relationship;
        }

        log.debug("invalid relationship");
        return null;
    }



    /**
     * Use the dates in the entities and return the one that was last updated most recently.
     * If the dates are equal, then compare GUIDs in order to always retrieve the same entity (no matter
     * which of the entities is the current and which is the new one).
     *
     * @param currentEntity current entity
     * @param newEntity next peer entity
     * @return latest values
     */
    private EntityDetail getLatest(EntityDetail currentEntity, EntityDetail newEntity)
    {
        Date currentLastUpdate = currentEntity.getUpdateTime();

        if (currentLastUpdate == null)
        {
            currentLastUpdate = currentEntity.getCreateTime();
        }

        Date newLastUpdate = newEntity.getUpdateTime();

        if (newLastUpdate == null)
        {
            newLastUpdate = newEntity.getCreateTime();
        }

        if(currentLastUpdate.equals(newLastUpdate))
        {
            if(currentEntity.getGUID().compareTo(newEntity.getGUID()) >= 0)
            {
                return currentEntity;
            }
            else
            {
                return newEntity;
            }
        }

        if (currentLastUpdate.before(newLastUpdate))
        {
            return new EntityDetail(newEntity);
        }

        return currentEntity;
    }


    /**
     * Filter entity results that do not match the caller's criteria.  If all entities are filtered out, an empty list is
     * returned to show that the caller can issue another retrieve if more elements are needed.
     *
     * @param userId calling user
     * @param retrievedEntities list of entities retrieved from the repositories
     * @param expectedEntityTypeName type name to validate
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime  effective time to match against
     * @param methodName calling method
     *
     * @return matching entities
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException unexpected security error
     * @throws PropertyServerException logic error
     */
    private List<EntityDetail> validateEntities(String               userId,
                                                List<EntityDetail>   retrievedEntities,
                                                String               expectedEntityTypeName,
                                                List<InstanceStatus> limitResultsByStatus,
                                                Date                 asOfTime,
                                                SequencingOrder      sequencingOrder,
                                                String               sequencingPropertyName,
                                                boolean              forLineage,
                                                boolean              forDuplicateProcessing,
                                                Date                 effectiveTime,
                                                String               methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        Set<String> acceptedGUIDs = new HashSet<>();

        if (retrievedEntities != null)
        {
            List<EntityDetail> results = new ArrayList<>();

            for (EntityDetail entity : retrievedEntities)
            {
                if (entity != null)
                {
                    if (! acceptedGUIDs.contains(entity.getGUID()))
                    {
                        /*
                         * Only validate an entity once.
                         */
                        acceptedGUIDs.add(entity.getGUID());

                        EntityDetail validatedEntity = this.validateRetrievedEntity(userId,
                                                                                    entity,
                                                                                    expectedEntityTypeName,
                                                                                    limitResultsByStatus,
                                                                                    asOfTime,
                                                                                    sequencingOrder,
                                                                                    sequencingPropertyName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

                        if (validatedEntity != null)
                        {
                            results.add(validatedEntity);
                        }
                        else
                        {
                            log.debug("Skipping entity since unavailable for some reason");
                        }
                    }
                    else
                    {
                        log.debug("Skipping entity since already retrieved - because using consolidated entities");
                    }
                }
            }

            if (log.isDebugEnabled())
            {
                log.debug(results.size() + " entities returned");
            }

            return results;
        }

        if (log.isDebugEnabled())
        {
            log.debug("No entities returned");
        }

        return null;
    }



    /**
     * Filter relationship results that do not match the caller's criteria.  If all entities are filtered out, an empty list is
     * returned to show that the caller can issue another retrieve if more elements are needed.
     *
     * @param retrievedRelationships list of entities retrieved from the repositories
     * @param expectedRelationshipTypeName type name to validate
     * @param effectiveTime  effective time to match against
     * @param methodName calling method
     *
     * @return matching relationships
     */
    private List<Relationship> validateRelationships(List<Relationship> retrievedRelationships,
                                                     String             expectedRelationshipTypeName,
                                                     Date               effectiveTime,
                                                     String             methodName)
    {
        Set<String> acceptedGUIDs = new HashSet<>();

        if (retrievedRelationships != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (Relationship relationship : retrievedRelationships)
            {
                if (relationship != null)
                {
                    Relationship validatedRelationship = this.validateRetrievedRelationship(relationship,
                                                                                            expectedRelationshipTypeName,
                                                                                            effectiveTime,
                                                                                            methodName);

                    if (validatedRelationship != null)
                    {
                        if (! acceptedGUIDs.contains(validatedRelationship.getGUID()))
                        {
                            acceptedGUIDs.add(validatedRelationship.getGUID());
                            results.add(validatedRelationship);
                        }
                        else
                        {
                            log.debug("Skipping relationship since already retrieved - because using consolidated entities");
                        }
                    }
                    else
                    {
                        log.debug("Skipping relationship since unavailable for some reason");
                    }
                }
            }

            if (log.isDebugEnabled())
            {
                log.debug(results.size() + " entities returned");
            }

            return results;
        }

        if (log.isDebugEnabled())
        {
            log.debug("No entities returned");
        }

        return null;
    }


    /**
     * Validate that the supplied GUID is for a real entity that is effective.  Return null if not.
     *
     * @param userId         user making the request.
     * @param guid           unique identifier of the entity.
     * @param guidParameterName name of parameter passing the GUID
     * @param entityTypeName expected type of asset.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime  time when the element should be effective
     * @param methodName     name of method called.
     *
     * @return retrieved entity
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail isEntityKnown(String  userId,
                                      String  guid,
                                      String  guidParameterName,
                                      String  entityTypeName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String localMethodName = "isEntityKnown";

        try
        {
            return this.getEntityByGUID(userId,
                                        guid,
                                        guidParameterName,
                                        entityTypeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
        catch (InvalidParameterException error)
        {
            return null;
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Create a new entity in the open metadata repository with the specified instance status. The setting of externalSourceGUID determines
     * whether a local or a remote entity is created.
     *
     * @param userId             calling user
     * @param entityTypeGUID     type of entity to create
     * @param entityTypeName     name of the entity's type
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param properties         properties for the entity
     * @param instanceStatus     initial status (needs to be valid for type)
     * @param methodName         name of calling method
     *
     * @return unique identifier of new entity
     *
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createEntity(String             userId,
                               String             entityTypeGUID,
                               String             entityTypeName,
                               String             externalSourceGUID,
                               String             externalSourceName,
                               InstanceProperties properties,
                               InstanceStatus     instanceStatus,
                               String             methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
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
     * @param userId                 calling user
     * @param entityTypeGUID         type of entity to create
     * @param entityTypeName         name of the entity's type
     * @param externalSourceGUID     unique identifier (guid) for the external source.
     * @param externalSourceName     unique name for the external source.
     * @param properties             properties for the entity
     * @param initialClassifications list of classifications for the first version of this entity.
     * @param instanceStatus         initial status (needs to be valid for type)
     * @param methodName             name of calling method
     *
     * @return unique identifier of new entity
     *
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createEntity(String               userId,
                               String               entityTypeGUID,
                               String               entityTypeName,
                               String               externalSourceGUID,
                               String               externalSourceName,
                               InstanceProperties   properties,
                               List<Classification> initialClassifications,
                               InstanceStatus       instanceStatus,
                               String               methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String localMethodName = "createEntity";
        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";
        final String newPropertiesParameterName = "newProperties";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

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
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
        {
            errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID         unique identifier of entity to update
     * @param entityGUIDParameterName parameter supplying entityGUID
     * @param entityTypeGUID     type of entity to create
     * @param entityTypeName     name of the entity's type
     * @param updateProperties   properties for the entity
     * @param forLineage         is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime      the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         name of calling method
     *
     * @return updated entity
     *
     * @throws InvalidParameterException  problem with the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail updateEntityProperties(String             userId,
                                               String             externalSourceGUID,
                                               String             externalSourceName,
                                               String             entityGUID,
                                               String             entityGUIDParameterName,
                                               String             entityTypeGUID,
                                               String             entityTypeName,
                                               InstanceProperties updateProperties,
                                               boolean            forLineage,
                                               boolean            forDuplicateProcessing,
                                               Date               effectiveTime,
                                               String             methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        EntityDetail originalEntity = this.getEntityByGUID(userId,
                                                           entityGUID,
                                                           entityGUIDParameterName,
                                                           entityTypeName,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        return updateEntityProperties(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      originalEntity.getGUID(),
                                      originalEntity,
                                      entityTypeGUID,
                                      entityTypeName,
                                      updateProperties,
                                      methodName);
    }


    /**
     * Update the properties of an existing entity in the open metadata repository.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID         unique identifier of entity to update
     * @param originalEntity     entity retrieved from repository - must not be null
     * @param entityTypeGUID     type of entity to create
     * @param entityTypeName     name of the entity's type
     * @param newProperties      properties for the entity
     * @param methodName         name of calling method
     *
     * @return updated entity
     *
     * @throws InvalidParameterException  problem with the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail updateEntityProperties(String             userId,
                                               String             externalSourceGUID,
                                               String             externalSourceName,
                                               String             entityGUID,
                                               EntityDetail       originalEntity,
                                               String             entityTypeGUID,
                                               String             entityTypeName,
                                               InstanceProperties newProperties,
                                               String             methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName   = "updateEntityProperties";
        final String guidParameterName = "guid";
        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";
        final String newPropertiesParameterName = "newProperties";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        if (originalEntity != null)
        {
            errorHandler.validateProvenance(userId,
                                            originalEntity,
                                            originalEntity.getGUID(),
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
                    errorHandler.handleNoEntity(entityTypeGUID, entityTypeName, newProperties, methodName);
                }

                return newEntity;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
            {
                errorHandler.handleUnknownEntity(error, entityGUID, entityTypeName, methodName, guidParameterName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
            {
                errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
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

        return null;
    }


    /**
     * Extract the classification type from the classification header.
     *
     * @param classification incoming classification
     * @return type def unique identifier or null
     */
    private String getClassificationTypeGUID(Classification classification)
    {
        InstanceType type = classification.getType();

        if (type != null)
        {
            return type.getTypeDefGUID();
        }

        return null;
    }


    /**
     * Update the classifications for an entity.
     *
     * @param userId                        calling user
     * @param externalSourceGUID            unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName            unique name for the external source.
     * @param entityGUID                    entity to update
     * @param existingEntityClassifications existing classifications retrieved from the repository
     * @param classifications               new/updated classifications for the entity
     * @param forDuplicateProcessing        the query is for duplicate processing and so must not deduplicate
     * @param methodName                    name of calling method
     *
     * @throws InvalidParameterException  problem with the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void updateEntityClassifications(String               userId,
                                             String               externalSourceGUID,
                                             String               externalSourceName,
                                             String               entityGUID,
                                             String               entityGUIDParameterName,
                                             String               entityTypeName,
                                             List<Classification> existingEntityClassifications,
                                             List<Classification> classifications,
                                             boolean              forLineage,
                                             boolean              forDuplicateProcessing,
                                             Date                 effectiveTime,
                                             String               methodName) throws InvalidParameterException,
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
                     * All the classifications are new
                     */
                    for (Classification newClassification : classifications)
                    {
                        if (newClassification != null)
                        {
                            this.classifyEntity(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entityGUID,
                                                null,
                                                entityGUIDParameterName,
                                                entityTypeName,
                                                getClassificationTypeGUID(newClassification),
                                                newClassification.getName(),
                                                newClassification.getClassificationOrigin(),
                                                newClassification.getClassificationOriginGUID(),
                                                newClassification.getProperties(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
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
                 * All the classifications are deleted
                 */
                for (Classification obsoleteClassification : existingEntityClassifications)
                {
                    if (obsoleteClassification != null)
                    {
                        this.declassifyEntity(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              entityGUID,
                                              null,
                                              entityGUIDParameterName,
                                              entityTypeName,
                                              getClassificationTypeGUID(obsoleteClassification),
                                              obsoleteClassification.getName(),
                                              obsoleteClassification,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
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
                                                entityGUIDParameterName,
                                                entityTypeName,
                                                getClassificationTypeGUID(classification),
                                                classification.getName(),
                                                classification.getClassificationOrigin(),
                                                classification.getClassificationOriginGUID(),
                                                classification.getProperties(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
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
                                                          entityGUIDParameterName,
                                                          entityTypeName,
                                                          getClassificationTypeGUID(classification),
                                                          classification.getName(),
                                                          matchingEntityClassification,
                                                          null, // clears all properties
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);
                                }
                            }
                            else if (! classification.getProperties().equals(matchingEntityClassification.getProperties()))
                            {
                                this.reclassifyEntity(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      entityGUID,
                                                      entityGUIDParameterName,
                                                      entityTypeName,
                                                      getClassificationTypeGUID(classification),
                                                      classification.getName(),
                                                      matchingEntityClassification,
                                                      classification.getProperties(),
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
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
                                                  entityGUIDParameterName,
                                                  entityTypeName,
                                                  getClassificationTypeGUID(classification),
                                                  classification.getName(),
                                                  entityClassificationMap.get((entityClassificationName)),
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
                        }
                    }
                }
            }
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Retrieve a specific classification for an entity. Null is returned if the entity is not classified
     * in this way.
     *
     * @param userId             calling user
     * @param entityGUID         unique identity of the entity - if this entity is not known then an exception occurs
     * @param classificationName name of the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         calling method
     *
     * @return located classification or null if not found
     *
     * @throws InvalidParameterException  invalid parameter (probably the guid)
     * @throws UserNotAuthorizedException calling user does not have appropriate permissions
     * @throws PropertyServerException    internal error
     */
    private Classification getClassificationForEntity(String  userId,
                                                      String  entityGUID,
                                                      String  entityGUIDParameterName,
                                                      String  entityTypeName,
                                                      String  classificationName,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        /*
         * This call verifies that the entity is usable by the caller.
         */
        EntityDetail entity = this.getEntityByGUID(userId,
                                                   entityGUID,
                                                   entityGUIDParameterName,
                                                   entityTypeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
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
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID         unique identifier entity to update
     * @param entityGUIDParameterName parameter supplying entityGUID
     * @param entityTypeGUID     type of entity to create
     * @param entityTypeName     name of the entity's type
     * @param properties         properties for the entity
     * @param classifications    classifications for entity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         name of calling method
     *
     * @return returned entity containing the update
     *
     * @throws InvalidParameterException  problem with the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail updateEntity(String               userId,
                                     String               externalSourceGUID,
                                     String               externalSourceName,
                                     String               entityGUID,
                                     String               entityGUIDParameterName,
                                     String               entityTypeGUID,
                                     String               entityTypeName,
                                     InstanceProperties   properties,
                                     List<Classification> classifications,
                                     boolean              forLineage,
                                     boolean              forDuplicateProcessing,
                                     Date                 effectiveTime,
                                     String               methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        EntityDetail entity = this.updateEntityProperties(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          entityGUID,
                                                          entityGUIDParameterName,
                                                          entityTypeGUID,
                                                          entityTypeName,
                                                          properties,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        this.updateEntityClassifications(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         entity.getGUID(),
                                         entityGUIDParameterName,
                                         entityTypeName,
                                         entity.getClassifications(),
                                         classifications,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

        return entity;
    }


    /**
     * Update an existing entity in the open metadata repository. The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instance's
     * metadata collection identifiers.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityHeader       unique identifier of entity to update
     * @param entityTypeGUID     type of entity to create
     * @param entityTypeName     name of the entity's type
     * @param properties         properties for the entity
     * @param methodName         name of calling method
     *
     * @throws InvalidParameterException  problem with the properties
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateEntityProperties(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       InstanceHeader     entityHeader,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       InstanceProperties properties,
                                       String             methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String localMethodName = "updateEntityProperties";
        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";
        final String newPropertiesParameterName = "newProperties";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);
        try
        {
            errorHandler.validateProvenance(userId,
                                            entityHeader,
                                            entityHeader.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            EntityDetail newEntity = metadataCollection.updateEntityProperties(userId, entityHeader.getGUID(), properties);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID, entityTypeName, properties, methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
        {
            errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Undo the last update to the requested entity.
     *
     * @param userId             unique identifier for requesting user.
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param updatedEntityGUID  String unique identifier (guid) for the entity.
     * @param methodName         name of calling method
     * @return recovered entity
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail undoEntityUpdate(String userId,
                                         String externalSourceGUID,
                                         String externalSourceName,
                                         String updatedEntityGUID,
                                         String methodName) throws UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String localMethodName = "undoEntityUpdate";

        try
        {
            EntityDetail entity = metadataCollection.undoEntityUpdate(userId, updatedEntityGUID);

            if (entity != null)
            {
                errorHandler.validateProvenance(userId,
                                                entity,
                                                updatedEntityGUID,
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);
            }

            return entity;
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
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

        return null;
    }



    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId             unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startingFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param methodName         name of calling method
     * @return list of versions of an entity
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException bad guid
     */
    public List<EntityDetail> getEntityDetailHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException,
                                                                                               InvalidParameterException
    {
        final String localMethodName = "getEntityDetailHistory";
        final String guidParameterName = "entityGUID";

        try
        {
            return metadataCollection.getEntityDetailHistory(userId,
                                                             guid,
                                                             fromTime,
                                                             toTime,
                                                             startingFrom,
                                                             pageSize,
                                                             sequencingOrder);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, guid, null, methodName, guidParameterName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return all historical versions of an entity's classification within the bounds of the provided timestamps.
     * To retrieve all historical versions of an entity's classification, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId             unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of the classification
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startingFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param methodName         name of calling method
     * @return list of versions of an entity
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException bad guid
     */
    public List<Classification> getClassificationHistory(String                 userId,
                                                         String                 guid,
                                                         String                 classificationName,
                                                         Date                   fromTime,
                                                         Date                   toTime,
                                                         int                    startingFrom,
                                                         int                    pageSize,
                                                         HistorySequencingOrder sequencingOrder,
                                                         String                 methodName) throws UserNotAuthorizedException,
                                                                                                   PropertyServerException,
                                                                                                   InvalidParameterException
    {
        final String localMethodName = "getClassificationHistory";
        final String guidParameterName = "entityGUID";

        try
        {
            return metadataCollection.getClassificationHistory(userId,
                                                               guid,
                                                               classificationName,
                                                               fromTime,
                                                               toTime,
                                                               startingFrom,
                                                               pageSize,
                                                               sequencingOrder);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownEntity(error, guid, null, methodName, guidParameterName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }



    /**
     * Update an existing entity status in the open metadata repository.  The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instance's
     * metadata collection identifiers.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param entityGUID         unique identifier of entity to update
     * @param entity             current values of entity
     * @param entityTypeGUID     type of entity to update
     * @param entityTypeName     name of the entity's type
     * @param instanceStatus     initial status (needs to be valid for type)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName         name of calling method
     *
     * @throws InvalidParameterException  problem with the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateEntityStatus(String         userId,
                                   String         externalSourceGUID,
                                   String         externalSourceName,
                                   String         entityGUID,
                                   EntityDetail   entity,
                                   String         entityTypeGUID,
                                   String         entityTypeName,
                                   InstanceStatus instanceStatus,
                                   boolean        forLineage,
                                   boolean        forDuplicateProcessing,
                                   Date           effectiveTime,
                                   String         methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String guidParameterName = "entityGUID";
        final String localMethodName   = "updateEntityStatus";
        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        EntityDetail retrievedEntity = entity;

        if (retrievedEntity == null)
        {
            retrievedEntity = this.getEntityByGUID(userId,
                                                   entityGUID,
                                                   guidParameterName,
                                                   entityTypeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
        }

        try
        {
            errorHandler.validateProvenance(userId,
                                            retrievedEntity,
                                            retrievedEntity.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            EntityDetail newEntity = metadataCollection.updateEntityStatus(userId, entityGUID, instanceStatus);

            if (newEntity == null)
            {
                errorHandler.handleNoEntity(entityTypeGUID, entityTypeName, null, methodName);
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
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


    /**
     * Add a new classification to an existing entity in the open metadata repository.
     *
     * @param userId                   calling user
     * @param externalSourceGUID       unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName       unique name for the external source
     * @param entityGUID               unique identifier of entity to update
     * @param entityDetail             retrieved entity (maybe null)
     * @param entityGUIDParameterName  parameter supplying entityGUID
     * @param entityTypeName           type of entity
     * @param classificationTypeGUID   type of classification to create
     * @param classificationTypeName   name of the classification's type
     * @param classificationOrigin     is this classification assigned or propagated?
     * @param classificationOriginGUID which entity did a propagated classification originate from?
     * @param properties               properties for the classification
     * @param forLineage               the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing   the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime            the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName               name of calling method
     *
     * @return updated entity
     *
     * @throws InvalidParameterException  problem with the properties
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail classifyEntity(String               userId,
                                       String               externalSourceGUID,
                                       String               externalSourceName,
                                       String               entityGUID,
                                       EntityDetail         entityDetail,
                                       String               entityGUIDParameterName,
                                       String               entityTypeName,
                                       String               classificationTypeGUID,
                                       String               classificationTypeName,
                                       ClassificationOrigin classificationOrigin,
                                       String               classificationOriginGUID,
                                       InstanceProperties   properties,
                                       boolean              forLineage,
                                       boolean              forDuplicateProcessing,
                                       Date                 effectiveTime,
                                       String               methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String localMethodName = "classifyEntity";

        final String typeGUIDParameterName = "classificationTypeGUID";
        final String typeNameParameterName = "classificationTypeName";
        final String newPropertiesParameterName = "newProperties";

        errorHandler.validateTypeIdentifiers(classificationTypeGUID,
                                             typeGUIDParameterName,
                                             classificationTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);
        try
        {
            if (entityDetail == null)
            {
                entityDetail = this.getEntityByGUID(userId, entityGUID, entityGUIDParameterName, entityTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
            }

            // create a proxy representation to allow classification of entities incoming from other metadata collections
            EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(userId, entityDetail);

            Classification newClassification = metadataCollection.classifyEntity(userId,
                                                                                 entityProxy,
                                                                                 classificationTypeName,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 classificationOrigin,
                                                                                 classificationOriginGUID,
                                                                                 properties);


            if (newClassification == null)
            {
                errorHandler.handleNoEntityForClassification(entityGUID,
                                                             classificationTypeGUID,
                                                             classificationTypeName,
                                                             properties,
                                                             methodName);
            }
            else
            {
                // update the entity's classifications list with the one we just added
                List<Classification> classifications = entityDetail.getClassifications() == null
                                                            ? new ArrayList<>()
                                                            : entityDetail.getClassifications();
                classifications.add(newClassification);
                entityDetail.setClassifications(classifications);

                return entityDetail;
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException error)
        {
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
        {
            errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
        }
        catch (ClassificationErrorException error)
        {
            /*
             * There was a problem adding the classification.  It probably means that another thread/server
             * is simultaneously adding the same classification to the same element.
             * The example that caused this code to be added was the attachment of Anchors classifications
             * to new entities being processed by multiple event listener threads from the OMASs.
             */
            try
            {
                entityDetail = this.getEntityByGUID(userId,
                                                    entityGUID,
                                                    entityGUIDParameterName,
                                                    entityTypeName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

                if (entityDetail.getClassifications() != null)
                {
                    for (Classification classification : entityDetail.getClassifications())
                    {
                        if (classificationTypeName.equals(classification.getName()))
                        {
                            /*
                             * The same classification is already present on the entity.
                             * This means two threads were updating the same classification on the same entity
                             * at the same time.
                             */
                            if (classification.getProperties() == null)
                            {
                                if (properties == null)
                                {
                                    /*
                                     * No concern as the updates are identical.
                                     */
                                    return entityDetail;
                                }
                            }
                            else
                            {
                                if (classification.getProperties().equals(properties))
                                {
                                    /*
                                     * No concern as the updates are identical.
                                     */
                                    return entityDetail;
                                }
                            }

                            /*
                             * Retry the classification - there may have been a timing error, with two threads
                             * trying to classify it at the same time
                             */
                            try
                            {
                                auditLog.logMessage(methodName, RepositoryHandlerAuditCode.CLASSIFICATION_RETRY.getMessageDefinition(classificationTypeName,
                                                                                                                                     entityDetail.getGUID(),
                                                                                                                                     error.getClass().getName(),
                                                                                                                                     error.getMessage()));

                                EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(userId, entityDetail);

                                Classification newClassification = metadataCollection.updateEntityClassification(userId,
                                                                                                                 entityProxy,
                                                                                                                 classificationTypeName,
                                                                                                                 properties);

                                // update the entity's classifications list with the one we just added
                                List<Classification> classifications = entityDetail.getClassifications() == null
                                        ? new ArrayList<>()
                                        : entityDetail.getClassifications();
                                classifications.add(newClassification);
                                entityDetail.setClassifications(classifications);

                                return entityDetail;
                            }
                            catch (Exception reclassificationError)
                            {
                                /*
                                 * The change to the classification is incompatible with the conflicting
                                 * change, so the appropriate exception is returned to the caller.
                                 */
                                errorHandler.handleRepositoryError(reclassificationError, methodName, localMethodName);
                            }
                        }
                    }

                    errorHandler.handleRepositoryError(error, methodName, localMethodName);
                }
                else
                {
                    errorHandler.handleRepositoryError(error, methodName, localMethodName);
                }
            }
            catch (Exception nestedError)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName);
            }
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Update the properties of an existing classification to an existing entity in the open metadata repository.
     *
     * @param userId                       calling user
     * @param externalSourceGUID           unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName           unique name for the external source.
     * @param entityGUID                   unique identifier of entity to update
     * @param entityGUIDParameterName      parameter supplying entityGUID
     * @param entityTypeName               type of entity
     * @param classificationTypeGUID       type of classification to create
     * @param classificationTypeName       name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param newProperties                properties for the classification
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime                the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                   name of calling method
     *
     * @throws InvalidParameterException  invalid parameters passed - probably GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void reclassifyEntity(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              entityGUID,
                                 String              entityGUIDParameterName,
                                 String              entityTypeName,
                                 String              classificationTypeGUID,
                                 String              classificationTypeName,
                                 InstanceAuditHeader existingClassificationHeader,
                                 InstanceProperties  newProperties,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String localMethodName = "reclassifyEntity";

        final String typeGUIDParameterName = "classificationTypeGUID";
        final String typeNameParameterName = "classificationTypeName";
        final String newPropertiesParameterName = "newProperties";

        errorHandler.validateTypeIdentifiers(classificationTypeGUID,
                                             typeGUIDParameterName,
                                             classificationTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        /*
         * The audit header is supplied if the caller has already looked up the entity/classification
         */
        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId,
                                                          entityGUID,
                                                          entityGUIDParameterName,
                                                          entityTypeName,
                                                          classificationTypeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);
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

                EntityDetail entityDetail = this.getEntityByGUID(userId,
                                                                 entityGUID,
                                                                 entityGUIDParameterName,
                                                                 entityTypeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

                EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(userId, entityDetail);

                Classification newClassification = metadataCollection.updateEntityClassification(userId,
                                                                                                 entityProxy,
                                                                                                 classificationTypeName,
                                                                                                 newProperties);

                if (newClassification == null)
                {
                    errorHandler.handleNoEntityForClassification(entityGUID,
                                                                 classificationTypeGUID,
                                                                 classificationTypeName,
                                                                 newProperties,
                                                                 methodName);
                }
            }
            catch (UserNotAuthorizedException | PropertyServerException error)
            {
                /*
                 * This comes from validateProvenance.  The call to validate provenance is in the try...catch
                 * in case the caller has passed bad parameters.
                 */
                throw error;
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
            {
                errorHandler.handleUnauthorizedUser(userId, methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException error)
            {
                errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
            }
            catch (Exception error)
            {
                errorHandler.handleRepositoryError(error, methodName, localMethodName + "(" + classificationTypeName + ")");
            }
        }
        else /* should be a classify method call */
        {
            this.classifyEntity(userId,
                                externalSourceGUID,
                                externalSourceName,
                                entityGUID,
                                null,
                                entityGUIDParameterName,
                                entityTypeName,
                                classificationTypeGUID,
                                classificationTypeName,
                                ClassificationOrigin.ASSIGNED,
                                null,
                                newProperties,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
        }
    }


    /**
     * Remove an existing classification from an existing entity in the open metadata repository.
     *
     * @param userId                       calling user
     * @param externalSourceGUID           unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName           unique name for the external source.
     * @param entityGUID                   unique identifier of entity to update
     * @param entity                       full entity bean
     * @param entityGUIDParameterName      parameter name that passed the entityGUID
     * @param entityTypeName               type of entity
     * @param classificationTypeGUID       type of classification to create
     * @param classificationTypeName       name of the classification's type
     * @param existingClassificationHeader current value of classification
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime                the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                   name of calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid = probably the GUID
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void declassifyEntity(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              entityGUID,
                                 EntityDetail        entity,
                                 String              entityGUIDParameterName,
                                 String              entityTypeName,
                                 String              classificationTypeGUID,
                                 String              classificationTypeName,
                                 InstanceAuditHeader existingClassificationHeader,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String localMethodName = "declassifyEntity";

        final String typeGUIDParameterName = "classificationTypeGUID";
        final String typeNameParameterName = "classificationTypeName";

        errorHandler.validateTypeIdentifiers(classificationTypeGUID,
                                             typeGUIDParameterName,
                                             classificationTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        InstanceAuditHeader auditHeader = existingClassificationHeader;

        if (auditHeader == null)
        {
            auditHeader = this.getClassificationForEntity(userId,
                                                          entityGUID,
                                                          entityGUIDParameterName,
                                                          entityTypeName,
                                                          classificationTypeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);
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

                EntityDetail entityDetail = entity;
                if (entityDetail == null)
                {
                    entityDetail = this.getEntityByGUID(userId,
                                                        entityGUID,
                                                        entityGUIDParameterName,
                                                        entityTypeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
                }

                // create a proxy representation to allow declassification of entities incoming from other metadata collections
                EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(userId, entityDetail);

                Classification removedClassification = metadataCollection.declassifyEntity(userId, entityProxy, classificationTypeName);

                if (removedClassification == null)
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
                 * This comes from validateProvenance.  The call to validate provenance is in the try...catch
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
     * Remove an entity from the open metadata repository if the validating properties match. The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instance's
     * metadata collection identifiers.
     *
     * @param userId                          calling user
     * @param externalSourceGUID              unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName              unique name for the external source.
     * @param obsoleteEntityGUID              unique identifier of the entity
     * @param obsoleteEntityGUIDParameterName name for unique identifier of the entity
     * @param entityTypeGUID                  type of entity to delete
     * @param entityTypeName                  name of the entity's type
     * @param validatingPropertyName          name of property that should be in the entity if we have the correct one.
     * @param validatingProperty              value of property that should be in the entity if we have the correct one.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime                the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                      name of calling method
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException  mismatch on properties
     */
    public void removeEntity(String               userId,
                             String               externalSourceGUID,
                             String               externalSourceName,
                             String               obsoleteEntityGUID,
                             String               obsoleteEntityGUIDParameterName,
                             String               entityTypeGUID,
                             String               entityTypeName,
                             String               validatingPropertyName,
                             String               validatingProperty,
                             List<InstanceStatus> limitResultsByStatus,
                             Date                 asOfTime,
                             SequencingOrder      sequencingOrder,
                             String               sequencingPropertyName,
                             boolean              forLineage,
                             boolean              forDuplicateProcessing,
                             Date                 effectiveTime,
                             String               methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String localMethodName = "removeEntity";

        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        try
        {
            EntityDetail obsoleteEntity = this.getEntityByGUID(userId,
                                                               obsoleteEntityGUID,
                                                               obsoleteEntityGUIDParameterName,
                                                               entityTypeName,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

            if (obsoleteEntity != null)
            {
                errorHandler.validateProvenance(userId,
                                                obsoleteEntity,
                                                obsoleteEntity.getGUID(),
                                                externalSourceGUID,
                                                externalSourceName,
                                                methodName);

                errorHandler.validateProperties(obsoleteEntity.getGUID(),
                                                validatingPropertyName,
                                                validatingProperty,
                                                obsoleteEntity.getProperties(),
                                                methodName);

                this.isolateAndRemoveEntity(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            obsoleteEntity.getGUID(),
                                            entityTypeGUID,
                                            entityTypeName,
                                            limitResultsByStatus,
                                            asOfTime,
                                            sequencingOrder,
                                            sequencingPropertyName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            methodName);
            }
        }
        catch (UserNotAuthorizedException | PropertyServerException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Remove an entity from the open metadata repository after checking that it is not connected to
     * anything else.  The repository handler helps to ensure that all relationships are deleted explicitly
     * ensuring the events are created and making it easier for third party repositories to keep track of
     * changes rather than have to implement the implied deletes from the logical graph.
     *
     * @param userId             calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID     type of entity to delete
     * @param entityTypeName     name of the entity's type
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage         the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param methodName         name of calling method
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void isolateAndRemoveEntity(String               userId,
                                        String               externalSourceGUID,
                                        String               externalSourceName,
                                        String               obsoleteEntityGUID,
                                        String               entityTypeGUID,
                                        String               entityTypeName,
                                        List<InstanceStatus> limitResultsByStatus,
                                        Date                 asOfTime,
                                        SequencingOrder      sequencingOrder,
                                        String               sequencingPropertyName,
                                        boolean              forLineage,
                                        boolean              forDuplicateProcessing,
                                        String               methodName) throws UserNotAuthorizedException,
                                                                                PropertyServerException,
                                                                                InvalidParameterException
    {
        final String localMethodName = "isolateAndRemoveEntity";

        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        this.removeAllRelationshipsOfType(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          obsoleteEntityGUID,
                                          entityTypeName,
                                          null,
                                          null,
                                          limitResultsByStatus,
                                          asOfTime,
                                          sequencingOrder,
                                          sequencingPropertyName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          null,
                                          methodName);

        auditLog.logMessage(methodName, RepositoryHandlerAuditCode.ENTITY_DELETED.getMessageDefinition(obsoleteEntityGUID, entityTypeName, entityTypeGUID, methodName));

        try
        {
            metadataCollection.deleteEntity(userId, entityTypeGUID, entityTypeName, obsoleteEntityGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException error)
        {
            this.purgeEntity(userId, obsoleteEntityGUID, entityTypeGUID, entityTypeName, methodName);
        }
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Purge an entity stored in a repository that does not support delete.
     *
     * @param userId             calling user
     * @param obsoleteEntityGUID unique identifier of the entity
     * @param entityTypeGUID     type of entity to delete
     * @param entityTypeName     name of the entity's type
     * @param methodName         name of calling method
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void purgeEntity(String userId,
                            String obsoleteEntityGUID,
                            String entityTypeGUID,
                            String entityTypeName,
                            String methodName) throws UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String localMethodName = "purgeEntity";

        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

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
        catch (Exception error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId             unique identifier for requesting user.
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param deletedEntityGUID  String unique identifier (guid) for the entity.
     * @param methodName         name of calling method
     *
     * @return restored entity
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail restoreEntity(String userId,
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

            return entity;
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
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

        return null;
    }


    /**
     * Return the list of entities of the requested type.
     *
     * @param userId         user making the request
     * @param entityTypeGUID identifier for the entity's type
     * @param entityTypeName name for the entity's type
     * @param limitResultsByStatus only return elements that have the requested status (null means all statuses
     * @param limitResultsByClassification only return elements that have the requested classification(s)
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom   initial position in the stored list.
     * @param pageSize       maximum number of definitions to return on this call.
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     name of calling method
     *
     * @return retrieved entities or null
     *
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForType(String               userId,
                                                 String               entityTypeGUID,
                                                 String               entityTypeName,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 List<String>         limitResultsByClassification,
                                                 Date                 asOfTime,
                                                 SequencingOrder      sequencingOrder,
                                                 String               sequencingPropertyName,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 int                  startingFrom,
                                                 int                  pageSize,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "getEntitiesForType";

        final String typeGUIDParameterName = "entityTypeGUID";
        final String typeNameParameterName = "entityTypeName";

        errorHandler.validateTypeIdentifiers(entityTypeGUID,
                                             typeGUIDParameterName,
                                             entityTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        try
        {
            List<EntityDetail> retrievedEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                             entityTypeGUID,
                                                                                             null,
                                                                                             null,
                                                                                             startingFrom,
                                                                                             limitResultsByStatus,
                                                                                             limitResultsByClassification,
                                                                                             asOfTime,
                                                                                             sequencingPropertyName,
                                                                                             sequencingOrder,
                                                                                             pageSize);


            /*
             * Results will be empty only if all retrieved entities are filtered out - this means the caller should retrieve again to get
             * more results. (Null means no more to receive.)
             */
            if (retrievedEntities != null)
            {
                return this.validateEntities(userId,
                                             retrievedEntities,
                                             entityTypeName,
                                             limitResultsByStatus,
                                             asOfTime,
                                             sequencingOrder,
                                             sequencingPropertyName,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (log.isDebugEnabled())
            {
                log.debug("No entities of type {}", entityTypeGUID);
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
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getEntityForRelationshipType(String userId,
                                                     String startingEntityGUID,
                                                     String startingEntityTypeName,
                                                     String relationshipTypeGUID,
                                                     String relationshipTypeName,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final  String startingEntityGUIDParameterName= "startingEntityGUID";

        Date effectiveTime = new Date();

        EntityDetail startingEntity = this.getEntityByGUID(userId,
                                                           startingEntityGUID,
                                                           startingEntityGUIDParameterName,
                                                           startingEntityTypeName,
                                                           false,
                                                           false,
                                                           effectiveTime,
                                                           methodName);

        return getEntityForRelationshipType(userId,
                                            startingEntity,
                                            startingEntityTypeName,
                                            relationshipTypeGUID,
                                            relationshipTypeName,
                                            null,
                                            0,
                                            null,
                                            0,
                                            null,
                                            null,
                                            SequencingOrder.CREATION_DATE_RECENT,
                                            null,
                                            false,
                                            false,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the entity at the other end of the requested relationship type.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param startingEntity starting entity
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param statusPropertyName name of the property to check that the status is acceptable
     * @param statusThreshold the value of status that the relationship property must be equal to or greater
     * @param returningEntityTypeName the type of the resulting entity
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @return retrieved entity or null
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getEntityForRelationshipType(String               userId,
                                                     EntityDetail         startingEntity,
                                                     String               startingEntityTypeName,
                                                     String               relationshipTypeGUID,
                                                     String               relationshipTypeName,
                                                     String               statusPropertyName,
                                                     int                  statusThreshold,
                                                     String               returningEntityTypeName,
                                                     int                  attachmentEntityEnd,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     Date                 asOfTime,
                                                     SequencingOrder      sequencingOrder,
                                                     String               sequencingPropertyName,
                                                     boolean              forLineage,
                                                     boolean              forDuplicateProcessing,
                                                     Date                 effectiveTime,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String localMethodName = "getEntityForRelationshipType";

        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        List<Relationship> filteredRelationships = this.getRelationshipsByType(userId, startingEntity, startingEntityTypeName, relationshipTypeGUID, relationshipTypeName, attachmentEntityEnd, limitResultsByStatus, asOfTime, sequencingOrder, sequencingPropertyName, forLineage, forDuplicateProcessing, 0, 0, effectiveTime, methodName);
        Relationship       resultingRelationship = null;

        if ((filteredRelationships != null) && (filteredRelationships.size() == 1))
        {
            resultingRelationship = filteredRelationships.get(0);

            if (! errorHandler.validateStatus(statusPropertyName, statusThreshold, resultingRelationship.getProperties(), methodName))
            {
                resultingRelationship = null;
            }
        }
        else if ((filteredRelationships != null) && (filteredRelationships.size() > 1))
        {
            errorHandler.handleAmbiguousRelationships(startingEntity.getGUID(),
                                                      startingEntityTypeName,
                                                      relationshipTypeName,
                                                      filteredRelationships,
                                                      methodName);
        }

        if (resultingRelationship != null)
        {
            EntityProxy requiredEnd = resultingRelationship.getEntityOneProxy();
            EntityProxy startingEnd = resultingRelationship.getEntityTwoProxy();

            if (startingEntity.getGUID().equals(requiredEnd.getGUID()))
            {
                requiredEnd = resultingRelationship.getEntityTwoProxy();
                startingEnd = resultingRelationship.getEntityOneProxy();
            }

            errorHandler.validateInstanceType(startingEnd, startingEntityTypeName, methodName, localMethodName);
            errorHandler.validateInstanceType(requiredEnd, returningEntityTypeName, methodName, localMethodName);

            final String guidParameterName = "requiredEnd.getGUID";
            return this.getEntityByGUID(userId,
                                        requiredEnd.getGUID(),
                                        guidParameterName,
                                        returningEntityTypeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type.  The returned entities will be from relationships
     * that have an effective date of now.
     *
     * @param userId                 user making the request
     * @param startingEntityGUID     starting entity's GUID
     * @param startingEntityTypeName starting entity's type name
     * @param relationshipTypeGUID   identifier for the relationship to follow
     * @param relationshipTypeName   type name for the relationship to follow
     * @param startingFrom           initial position in the stored list.
     * @param pageSize               maximum number of definitions to return on this call.
     * @param methodName             name of calling method
     *
     * @return retrieved entities or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public List<EntityDetail> getEntitiesForRelationshipType(String userId,
                                                             String startingEntityGUID,
                                                             String startingEntityTypeName,
                                                             String relationshipTypeGUID,
                                                             String relationshipTypeName,
                                                             int    startingFrom,
                                                             int    pageSize,
                                                             String methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return this.getEntitiesForRelationshipType(userId,
                                                   startingEntityGUID,
                                                   startingEntityTypeName,
                                                   relationshipTypeGUID,
                                                   relationshipTypeName,
                                                   0,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   startingFrom,
                                                   pageSize,
                                                   new Date(),
                                                   methodName);
    }


    /**
     * Return the list of entities at the other end of the requested relationship type.
     *
     * @param userId                 user making the request
     * @param startingEntityGUID     starting entity's GUID
     * @param startingEntityTypeName starting entity's type name
     * @param relationshipTypeGUID   identifier for the relationship to follow
     * @param relationshipTypeName   type name for the relationship to follow
     * @param attachmentEntityEnd    0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startingFrom           initial position in the stored list.
     * @param pageSize               maximum number of definitions to return on this call
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName             name of calling method
     *
     * @return retrieved entities or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<EntityDetail> getEntitiesForRelationshipType(String               userId,
                                                             String               startingEntityGUID,
                                                             String               startingEntityTypeName,
                                                             String               relationshipTypeGUID,
                                                             String               relationshipTypeName,
                                                             int                  attachmentEntityEnd,
                                                             List<InstanceStatus> limitResultsByStatus,
                                                             Date                 asOfTime,
                                                             SequencingOrder      sequencingOrder,
                                                             String               sequencingPropertyName,
                                                             boolean              forLineage,
                                                             boolean              forDuplicateProcessing,
                                                             int                  startingFrom,
                                                             int                  pageSize,
                                                             Date                 effectiveTime,
                                                             String               methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String localMethodName = "getEntitiesForRelationshipType";

        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        final String guidParameterName = "startingEntityGUID";

        EntityDetail startingEntity = this.getEntityByGUID(userId,
                                                           startingEntityGUID,
                                                           guidParameterName,
                                                           startingEntityTypeName,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        if (startingEntity != null)
        {
            List<Relationship> relationships = getRelationshipsByType(userId,
                                                                      startingEntity,
                                                                      startingEntityTypeName,
                                                                      relationshipTypeGUID,
                                                                      relationshipTypeName,
                                                                      attachmentEntityEnd,
                                                                      limitResultsByStatus,
                                                                      asOfTime,
                                                                      sequencingOrder,
                                                                      sequencingPropertyName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      startingFrom,
                                                                      pageSize,
                                                                      effectiveTime,
                                                                      methodName);

            if (relationships != null)
            {
                List<EntityDetail> results = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        EntityProxy requiredEnd = getOtherEnd(startingEntityGUID, startingEntityTypeName, relationship, attachmentEntityEnd, methodName);

                        EntityDetail entity = this.getEntityByGUID(userId,
                                                                   requiredEnd.getGUID(),
                                                                   guidParameterName,
                                                                   requiredEnd.getType().getTypeDefName(),
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

                        if (entity != null)
                        {
                            results.add(entity);
                        }
                    }
                }

                return this.validateEntities(userId,
                                             results,
                                             null,
                                             limitResultsByStatus,
                                             asOfTime,
                                             sequencingOrder,
                                             sequencingPropertyName,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
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

        return null;
    }


    /**
     * Filter out the relationships where the entity used to retrieve the relationships is at the wrong end.
     *
     * @param receivedRelationships list of relationships retrieved from the repositories
     * @param retrievingEntity entity used as a starting point in the query
     * @param attachmentEntityEnd which end should the receiving entity be in the relationship (0 is either end)
     * @param forDuplicateProcessing is this call part of duplicate processing?
     * @return filtered list
     */
    private List<Relationship> filterRelationshipsByEntityEnd(List<Relationship> receivedRelationships,
                                                              EntityDetail       retrievingEntity,
                                                              int                attachmentEntityEnd,
                                                              boolean            forDuplicateProcessing)
    {
        if (receivedRelationships == null)
        {
            log.debug("No relationships retrieved for entity " + retrievingEntity.getGUID());
            return null;
        }

        List<Relationship> notDeDupRelationships;

        if (forDuplicateProcessing)
        {
            notDeDupRelationships = receivedRelationships;
        }
        else
        {
            notDeDupRelationships = new ArrayList<>();

            for (Relationship receivedRelationship : receivedRelationships)
            {
                if ((receivedRelationship != null) && (! peerDuplicateLink.equals(receivedRelationship.getType().getTypeDefName())) && (! consolidatedDuplicateLinkName.equals(receivedRelationship.getType().getTypeDefName())))
                {
                    notDeDupRelationships.add(receivedRelationship);
                }
            }
        }

        if (attachmentEntityEnd == 1)
        {
            /*
             * Remove relationships where the retrieving entity is not at end 2.
             */
            List<Relationship> filteredRelationships = new ArrayList<>();

            for (Relationship retrievedRelationship : notDeDupRelationships)
            {
                if (retrievedRelationship != null)
                {
                    if (retrievingEntity.getGUID().equals(retrievedRelationship.getEntityTwoProxy().getGUID()))
                    {
                        filteredRelationships.add(retrievedRelationship);
                    }
                }
            }

            log.debug(filteredRelationships.size() + " relationships returned after filtering for attachmentEntityEnd=1 and entity " + retrievingEntity.getGUID());
            return filteredRelationships;
        }

        if (attachmentEntityEnd == 2)
        {
            /*
             * Remove relationships where the retrieving entity is not at end 1.
             */
            List<Relationship> filteredRelationships = new ArrayList<>();

            for (Relationship retrievedRelationship : notDeDupRelationships)
            {
                if (retrievedRelationship != null)
                {
                    if (retrievingEntity.getGUID().equals(retrievedRelationship.getEntityOneProxy().getGUID()))
                    {
                        filteredRelationships.add(retrievedRelationship);
                    }
                }
            }

            log.debug(filteredRelationships.size() + " relationships returned after filtering for attachmentEntityEnd=2 and entity " + retrievingEntity.getGUID());
            return filteredRelationships;
        }

        /*
         * Nothing to do because the attachment entity end is 0 - meaning that the receiving entity can be at either end.
         */
        log.debug(notDeDupRelationships.size() + " relationships returned after filtering for attachmentEntityEnd=0 and entity " + retrievingEntity.getGUID());

        return notDeDupRelationships;
    }


    /**
     * Return the entity proxy for the related entity.
     *
     * @param startingEntityGUID unique identifier of the starting entity
     * @param relationship relationship to another entity
     * @return proxy to the other entity.
     */
    public  EntityProxy  getOtherEnd(String       startingEntityGUID,
                                     Relationship relationship)
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
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param methodName calling method
     * @return proxy to the other entity.
     * @throws InvalidParameterException the type of the starting entity is incorrect
     */
    public  EntityProxy  getOtherEnd(String       startingEntityGUID,
                                     String       startingEntityTypeName,
                                     Relationship relationship,
                                     int          attachmentEntityEnd,
                                     String       methodName) throws InvalidParameterException
    {
        final String localMethodName = "getOtherEnd";

        if (relationship != null)
        {
            if (attachmentEntityEnd == 1)
            {
                errorHandler.validateInstanceType(relationship.getEntityTwoProxy(), startingEntityTypeName, methodName, localMethodName);

                return relationship.getEntityOneProxy();
            }
            else if (attachmentEntityEnd == 2)
            {
                errorHandler.validateInstanceType(relationship.getEntityOneProxy(), startingEntityTypeName, methodName, localMethodName);

                return relationship.getEntityTwoProxy();
            }
            else
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
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method name
     *
     * @return entity detail object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public EntityDetail   getEntityByGUID(String  userId,
                                          String  guid,
                                          String  guidParameterName,
                                          String  entityTypeName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return this.getEntityByGUID(userId, guid, guidParameterName, entityTypeName, forLineage, forDuplicateProcessing, null, effectiveTime, methodName);
    }


    /**
     * Return the requested entity, converting any errors from the repository services into the local
     * OMAS exceptions.
     *
     * @param userId calling user
     * @param guid unique identifier for the entity
     * @param guidParameterName name of the guid parameter for error handling
     * @param entityTypeName expected type of the entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method name
     *
     * @return entity detail object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public EntityDetail   getEntityByGUID(String  userId,
                                          String  guid,
                                          String  guidParameterName,
                                          String  entityTypeName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    asOfTime,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String localMethodName = "getEntityByGUID";

        EntityDetail entity = validateEntityGUID(userId, guid, guidParameterName, entityTypeName, asOfTime, methodName);

        if (entity != null)
        {
            log.debug("retrievedEntity=" + entity.getGUID());
        }
        else
        {
            log.debug("No retrievedEntity");
        }

        EntityDetail verifiedEntity = this.validateRetrievedEntity(userId,
                                                                   entity,
                                                                   entityTypeName,
                                                                   null,
                                                                   null,
                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        if (verifiedEntity != null)
        {
            log.debug("verifiedEntity=" + verifiedEntity.getGUID());
            return verifiedEntity;
        }
        else if (entity != null)
        {
            log.debug("No verifiedEntity");

            List<String> classificationNames = new ArrayList<>();

            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        classificationNames.add(classification.getName());
                    }
                }
            }

            String effectiveFromString = "<null>";
            String effectiveToString   = "<null>";
            String effectiveTimeString = "<null>";

            if (effectiveTime != null)
            {
                effectiveTimeString = effectiveTime + " (" + effectiveTime.getTime() + ")";
            }

            if (entity.getProperties() != null)
            {
                if (entity.getProperties().getEffectiveFromTime() != null)
                {
                    effectiveFromString = entity.getProperties().getEffectiveFromTime().toString() + " (" + entity.getProperties().getEffectiveFromTime().getTime() + ")";
                }
                if (entity.getProperties().getEffectiveToTime() != null)
                {
                    effectiveToString = entity.getProperties().getEffectiveToTime().toString() + " (" + entity.getProperties().getEffectiveToTime().getTime() + ")";
                }
            }

            InvalidParameterException error = new InvalidParameterException(RepositoryHandlerErrorCode.UNAVAILABLE_ENTITY.getMessageDefinition(entityTypeName,
                                                                                                                                               guid,
                                                                                                                                               localMethodName,
                                                                                                                                               methodName,
                                                                                                                                               userId,
                                                                                                                                               effectiveTimeString,
                                                                                                                                               effectiveFromString,
                                                                                                                                               effectiveToString,
                                                                                                                                               classificationNames.toString(),
                                                                                                                                               Boolean.toString(forLineage),
                                                                                                                                               Boolean.toString(forDuplicateProcessing)),
                                                                            this.getClass().getName(),
                                                                            methodName,
                                                                            guidParameterName);

            auditLog.logException(localMethodName,
                                  RepositoryHandlerAuditCode.UNAVAILABLE_ENTITY.getMessageDefinition(entityTypeName,
                                                                                                     guid,
                                                                                                     localMethodName,
                                                                                                     methodName,
                                                                                                     userId,
                                                                                                     effectiveTimeString,
                                                                                                     effectiveFromString,
                                                                                                     effectiveToString,
                                                                                                     classificationNames.toString(),
                                                                                                     Boolean.toString(forLineage),
                                                                                                     Boolean.toString(forDuplicateProcessing)),
                                  error);

            throw error;
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED.getMessageDefinition(entityTypeName, guid, localMethodName, methodName, userId),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Test whether an entity is of a particular type or not.
     *
     * @param userId calling user
     * @param guid unique identifier of the entity.
     * @param guidParameterName name of the parameter containing the guid.
     * @param entityTypeName name of the type to test for
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public boolean isEntityATypeOf(String userId,
                                   String guid,
                                   String guidParameterName,
                                   String entityTypeName,
                                   Date   effectiveTime,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String localMethodName = "isEntityATypeOf";

        try
        {
            EntityDetail entity = metadataCollection.getEntityDetail(userId, guid);

            if ((entity == null) || (! this.isCorrectEffectiveTime(entity.getProperties(), effectiveTime)))
            {
                errorHandler.handleUnknownEntity(null, guid, entityTypeName, methodName, guidParameterName);
            }

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
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return false;
    }


    /**
     * Return the requested entity by name.  The sequencing property name ensure that all elements are returned
     * in the same order to ensure none are lost in the paging process.
     *
     * @param userId calling userId
     * @param nameProperties list of name properties to search on
     * @param entityTypeGUID unique identifier of the entity's type
     * @param limitResultsByStatus only return elements that have the requested status (null means all statuses
     * @param limitResultsByClassification only return elements that have the requested classification(s)
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of returned entities - null means no more to retrieve; list (even if empty) means more to receive
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByName(String               userId,
                                                 InstanceProperties   nameProperties,
                                                 String               entityTypeGUID,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 List<String>         limitResultsByClassification,
                                                 Date                 asOfTime,
                                                 SequencingOrder      sequencingOrder,
                                                 String               sequencingPropertyName,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 int                  startingFrom,
                                                 int                  pageSize,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "getEntitiesByName";

        try
        {
            List<EntityDetail> retrievedEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                             entityTypeGUID,
                                                                                             nameProperties,
                                                                                             MatchCriteria.ANY,
                                                                                             startingFrom,
                                                                                             limitResultsByStatus,
                                                                                             limitResultsByClassification,
                                                                                             asOfTime,
                                                                                             sequencingPropertyName,
                                                                                             sequencingOrder,
                                                                                             pageSize);

            return this.validateEntities(userId,
                                         retrievedEntities,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         sequencingOrder,
                                         sequencingPropertyName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return the entities that match all supplied properties.  The sequencing order is important if the
     * caller is paging to ensure that all the results are returned.
     *
     * @param userId calling userId
     * @param propertyValue string value to search on - may be a RegEx
     * @param entityTypeGUID unique identifier of the entity's type
     * @param limitResultsByStatus only return elements that have the requested status (null means all statuses
     * @param limitResultsByClassification only return elements that have the requested classification(s)
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of returned entities - null means no more to retrieve; list (even if empty) means more to receive
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail>  getEntitiesByValue(String               userId,
                                                  String               propertyValue,
                                                  String               entityTypeGUID,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  SequencingOrder      sequencingOrder,
                                                  String               sequencingPropertyName,
                                                  boolean              forLineage,
                                                  boolean              forDuplicateProcessing,
                                                  int                  startingFrom,
                                                  int                  pageSize,
                                                  Date                 effectiveTime,
                                                  String               methodName) throws UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String localMethodName = "getEntitiesByValue";

        try
        {
            List<EntityDetail> retrievedEntities = metadataCollection.findEntitiesByPropertyValue(userId,
                                                                                                  entityTypeGUID,
                                                                                                  propertyValue,
                                                                                                  startingFrom,
                                                                                                  limitResultsByStatus,
                                                                                                  limitResultsByClassification,
                                                                                                  asOfTime,
                                                                                                  sequencingPropertyName,
                                                                                                  sequencingOrder,
                                                                                                  pageSize);

            return this.validateEntities(userId,
                                         retrievedEntities,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         sequencingOrder,
                                         sequencingPropertyName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                                Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of returned entities - null means no more to retrieve; list (even if empty) means more to receive
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<EntityDetail> getEntitiesByType(String               userId,
                                                String               entityTypeGUID,
                                                List<InstanceStatus> limitResultsByStatus,
                                                Date                 asOfTime,
                                                String               sequencingProperty,
                                                SequencingOrder      sequencingOrder,
                                                boolean              forLineage,
                                                boolean              forDuplicateProcessing,
                                                int                  startingFrom,
                                                int                  pageSize,
                                                Date                 effectiveTime,
                                                String               methodName) throws UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String localMethodName = "getEntitiesByType";

        try
        {
            List<EntityDetail> retrievedEntities = metadataCollection.findEntitiesByProperty(userId,
                                                                                             entityTypeGUID,
                                                                                             null,
                                                                                             null,
                                                                                             startingFrom,
                                                                                             limitResultsByStatus,
                                                                                             null,
                                                                                             asOfTime,
                                                                                             sequencingProperty,
                                                                                             sequencingOrder,
                                                                                             pageSize);

            return this.validateEntities(userId,
                                         retrievedEntities,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         sequencingOrder,
                                         sequencingProperty,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection; list (even if empty) means more to receive
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
                                           boolean               forLineage,
                                           boolean               forDuplicateProcessing,
                                           int                   startingFrom,
                                           int                   pageSize,
                                           Date                  effectiveTime,
                                           String                methodName) throws UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String localMethodName = "findEntities";

        try
        {
            List<EntityDetail> retrievedEntities = metadataCollection.findEntities(userId,
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

            return this.validateEntities(userId,
                                         retrievedEntities,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         sequencingOrder,
                                         sequencingProperty,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
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
                                                boolean               forDuplicateProcessing,
                                                int                   startingFrom,
                                                int                   pageSize,
                                                Date                  effectiveTime,
                                                String                methodName) throws UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String localMethodName = "findRelationships";

        try
        {
            List<Relationship> relationships = metadataCollection.findRelationships(userId,
                                                                                    relationshipTypeGUID,
                                                                                    relationshipSubtypeGUIDs,
                                                                                    searchProperties,
                                                                                    startingFrom,
                                                                                    limitResultsByStatus,
                                                                                    asOfTime,
                                                                                    sequencingProperty,
                                                                                    sequencingOrder,
                                                                                    pageSize);

            if (relationships != null)
            {
                RelationshipAccumulator accumulator = new RelationshipAccumulator(repositoryHelper,
                                                                                  this,
                                                                                  errorHandler,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  auditLog,
                                                                                  methodName);

                accumulator.addRelationships(relationships);
                return accumulator.getRelationships();
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

        return null;
    }


    /**
     * Return the current version of a requested relationship.
     *
     * @param userId  user making the request
     * @param relationshipGUID unique identifier for the relationship
     * @param relationshipParameterName parameter name supplying relationshipGUID
     * @param relationshipTypeName type name for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                              Date   effectiveTime,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String localMethodName = "getRelationshipByGUID";

        try
        {
            Relationship relationship = metadataCollection.getRelationship(userId, relationshipGUID);

            errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);

            if (isCorrectEffectiveTime(relationship.getProperties(), effectiveTime))
            {
                return relationship;
            }

            errorHandler.handleNotEffectiveElement(relationshipGUID,
                                                   relationshipTypeName,
                                                   relationship.getProperties(),
                                                   methodName,
                                                   relationshipParameterName,
                                                   effectiveTime);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException  error)
        {
            errorHandler.handleUnknownRelationship(error, relationshipGUID, relationshipTypeName, methodName, relationshipParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                              Date   asOfTime,
                                              Date   effectiveTime,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String localMethodName = "getRelationshipByGUID";

        try
        {
            Relationship relationship;

            if (asOfTime == null)
            {
                relationship = metadataCollection.getRelationship(userId, relationshipGUID);
            }
            else
            {
                relationship = metadataCollection.getRelationship(userId, relationshipGUID, asOfTime);
            }

            errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);

            if (isCorrectEffectiveTime(relationship.getProperties(), effectiveTime))
            {
                return relationship;
            }

            errorHandler.handleNotEffectiveElement(relationshipGUID,
                                                   relationshipTypeName,
                                                   relationship.getProperties(),
                                                   methodName,
                                                   relationshipParameterName,
                                                   effectiveTime);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException  error)
        {
            errorHandler.handleUnknownRelationship(error, relationshipGUID, relationshipTypeName, methodName, relationshipParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId             unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startingFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param methodName         name of calling method
     * @return list of versions of an entity
     *
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException bad guid
     */
    public List<Relationship> getRelationshipHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder,
                                                     String                 methodName) throws UserNotAuthorizedException,
                                                                                               PropertyServerException,
                                                                                               InvalidParameterException
    {
        final String localMethodName = "getRelationshipHistory";
        final String relationshipParameterName = "relationshipGUID";

        try
        {
            return metadataCollection.getRelationshipHistory(userId,
                                                             guid,
                                                             fromTime,
                                                             toTime,
                                                             startingFrom,
                                                             pageSize,
                                                             sequencingOrder);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException  error)
        {
            errorHandler.handleUnknownRelationship(error, guid, null, methodName, relationshipParameterName);
        }
        catch (Exception error)
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
     * @throws InvalidParameterException the GUID is invalid
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String userId,
                                                     String startingEntityGUID,
                                                     String startingEntityTypeName,
                                                     String relationshipTypeGUID,
                                                     String relationshipTypeName,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return this.getRelationshipsByType(userId,
                                           startingEntityGUID,
                                           startingEntityTypeName,
                                           relationshipTypeGUID,
                                           relationshipTypeName,
                                           0,
                                           null,
                                           null,
                                           null,
                                           null,
                                           false,
                                           false,
                                           0,
                                           maxPageSize,
                                           new Date(),
                                           methodName);
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
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this call part of duplicate processing?
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws InvalidParameterException the starting entity GUID is invalid
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String               userId,
                                                     String               startingEntityGUID,
                                                     String               startingEntityTypeName,
                                                     String               relationshipTypeGUID,
                                                     String               relationshipTypeName,
                                                     int                  attachmentEntityEnd,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     Date                 asOfTime,
                                                     SequencingOrder      sequencingOrder,
                                                     String               sequencingPropertyName,
                                                     boolean              forLineage,
                                                     boolean              forDuplicateProcessing,
                                                     int                  startingFrom,
                                                     int                  pageSize,
                                                     Date                 effectiveTime,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String guidParameterName = "startingEntityGUID";

        EntityDetail startingEntity = this.getEntityByGUID(userId,
                                                           startingEntityGUID,
                                                           guidParameterName,
                                                           startingEntityTypeName,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        if (startingEntity != null)
        {
            return getRelationshipsByType(userId,
                                          startingEntity,
                                          startingEntityTypeName,
                                          relationshipTypeGUID,
                                          relationshipTypeName,
                                          attachmentEntityEnd,
                                          limitResultsByStatus,
                                          asOfTime,
                                          sequencingOrder,
                                          sequencingPropertyName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          startingFrom,
                                          pageSize,
                                          effectiveTime,
                                          methodName);
        }

        return null;
    }


    /**
     * Return the list of relationships of the requested type connected to the starting entity.
     * The list is expected to be small.
     *
     * @param userId  user making the request
     * @param startingEntity  starting entity
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param callersSequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this call part of duplicate processing?
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsByType(String               userId,
                                                     EntityDetail         startingEntity,
                                                     String               startingEntityTypeName,
                                                     String               relationshipTypeGUID,
                                                     String               relationshipTypeName,
                                                     int                  attachmentEntityEnd,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     Date                 asOfTime,
                                                     SequencingOrder      callersSequencingOrder,
                                                     String               sequencingPropertyName,
                                                     boolean              forLineage,
                                                     boolean              forDuplicateProcessing,
                                                     int                  startingFrom,
                                                     int                  pageSize,
                                                     Date                 effectiveTime,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String localMethodName = "getRelationshipsByType";

        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        errorHandler.validateInstanceType(startingEntity, startingEntityTypeName, methodName, localMethodName);

        SequencingOrder sequencingOrder = SequencingOrder.CREATION_DATE_RECENT;

        if (callersSequencingOrder != null)
        {
            sequencingOrder = callersSequencingOrder;
        }

        if (! forDuplicateProcessing)
        {
            /*
             * The starting entity may be a duplicate, which means the retrieve needs to be made against
             * any peer duplicates - or the consolidated duplicate if there is one.
             */
            DuplicateEntityIterator duplicateEntityIterator = new DuplicateEntityIterator(this,
                                                                                          errorHandler,
                                                                                          invalidParameterHandler,
                                                                                          userId,
                                                                                          startingEntity,
                                                                                          startingEntityTypeName,
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          sequencingOrder,
                                                                                          sequencingPropertyName,
                                                                                          forLineage,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          methodName);

            /*
             * This may be the original entity, the consolidated entity or null if the entity should be ignored.
             * The subsequent call to duplicateEntityIterator.morePeersToReceive() will only return true if
             * there is no consolidated entity and the entity has peer duplicates.
             */
            if (duplicateEntityIterator.morePeersToReceive())
            {
                EntityProxy startingProxy = new EntityProxy(startingEntity);
                startingProxy.setUniqueProperties(repositoryHelper.getUniqueProperties(methodName, startingEntity.getType().getTypeDefName(), startingEntity.getProperties()));

                RelationshipAccumulator accumulator = new RelationshipAccumulator(repositoryHelper,
                                                                                  this,
                                                                                  errorHandler,
                                                                                  false,
                                                                                  effectiveTime,
                                                                                  auditLog,
                                                                                  methodName);

                do
                {
                    EntityDetail retrievingEntity = duplicateEntityIterator.getNextPeer();

                    try
                    {
                        List<Relationship> retrievedRelationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                                                 retrievingEntity.getGUID(),
                                                                                                                 relationshipTypeGUID,
                                                                                                                 startingFrom,
                                                                                                                 limitResultsByStatus,
                                                                                                                 asOfTime,
                                                                                                                 sequencingPropertyName,
                                                                                                                 sequencingOrder,
                                                                                                                 pageSize);

                        accumulator.addRelationships(startingProxy, retrievingEntity.getGUID(), filterRelationshipsByEntityEnd(retrievedRelationships, retrievingEntity, attachmentEntityEnd, forDuplicateProcessing));
                    }
                    catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
                    {
                        errorHandler.handleUnauthorizedUser(userId, methodName);
                    }
                    catch (Exception error)
                    {
                        errorHandler.handleRepositoryError(error, methodName, localMethodName);
                    }

                } while (duplicateEntityIterator.morePeersToReceive());

                return accumulator.getRelationships(startingEntity.getGUID(), attachmentEntityEnd);
            }
        }
        else
        {
            try
            {
                List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(userId,
                                                                                                startingEntity.getGUID(),
                                                                                                relationshipTypeGUID,
                                                                                                startingFrom,
                                                                                                limitResultsByStatus,
                                                                                                asOfTime,
                                                                                                sequencingPropertyName,
                                                                                                sequencingOrder,
                                                                                                pageSize);

                if ((relationships == null) || (relationships.isEmpty()))
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("No relationships of type " + relationshipTypeGUID + " found for entity " + startingEntity.getGUID());
                    }

                    return null;
                }

                /*
                 * Validate the types of the element returned.
                 */
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);

                        this.getOtherEnd(startingEntity.getGUID(), startingEntityTypeName, relationship, attachmentEntityEnd, methodName);
                    }
                }

                return filterRelationshipsByEntityEnd(relationships, startingEntity, attachmentEntityEnd, forDuplicateProcessing);
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

        return null;
    }



    /**
     * Return the requested relationships that match the requested type.
     *
     * @param userId calling userId
     * @param relationshipTypeGUID type of relationship required
     * @param relationshipTypeName type of relationship required
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param asOfTime Requests a historical query of the relationship.  Null means return the present values.
     * @param sequencingProperty String name of the relationship property that is to be used to sequence the results.
     *                                Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of returned relationship - null means no more to retrieve; list (even if empty) means more to receive
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public List<Relationship> getRelationshipsForType(String               userId,
                                                      String               relationshipTypeGUID,
                                                      String               relationshipTypeName,
                                                      List<InstanceStatus> limitResultsByStatus,
                                                      int                  startingFrom,
                                                      int                  pageSize,
                                                      Date                 asOfTime,
                                                      String               sequencingProperty,
                                                      SequencingOrder      sequencingOrder,
                                                      Date                 effectiveTime,
                                                      String               methodName) throws UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String localMethodName = "getRelationshipsForType";

        try
        {
            List<Relationship> retrievedRelationships = metadataCollection.findRelationshipsByProperty(userId,
                                                                                                       relationshipTypeGUID,
                                                                                                       null,
                                                                                                       null,
                                                                                                       startingFrom,
                                                                                                       limitResultsByStatus,
                                                                                                       asOfTime,
                                                                                                       sequencingProperty,
                                                                                                       sequencingOrder,
                                                                                                       pageSize);

            return validateRelationships(retrievedRelationships,
                                         relationshipTypeName,
                                         effectiveTime,
                                         methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
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
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws InvalidParameterException wrong type in entity 1
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsBetweenEntities(String               userId,
                                                              String               entity1GUID,
                                                              String               entity1TypeName,
                                                              String               entity2GUID,
                                                              String               relationshipTypeGUID,
                                                              String               relationshipTypeName,
                                                              int                  attachmentEntityEnd,
                                                              List<InstanceStatus> limitResultsByStatus,
                                                              Date                 asOfTime,
                                                              SequencingOrder      sequencingOrder,
                                                              String               sequencingPropertyName,
                                                              boolean              forLineage,
                                                              boolean              forDuplicateProcessing,
                                                              Date                 effectiveTime,
                                                              String               methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String localMethodName = "getRelationshipsBetweenEntities";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        List<Relationship>  entity1Relationships = this.getRelationshipsByType(userId,
                                                                               entity1GUID,
                                                                               entity1TypeName,
                                                                               relationshipTypeGUID,
                                                                               relationshipTypeName,
                                                                               attachmentEntityEnd,
                                                                               limitResultsByStatus,
                                                                               asOfTime,
                                                                               sequencingOrder,
                                                                               sequencingPropertyName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               0, 0,
                                                                               effectiveTime,
                                                                               methodName);

        if (entity1Relationships != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (Relationship  relationship : entity1Relationships)
            {
                if ((relationship != null) && (isCorrectEffectiveTime(relationship.getProperties(), effectiveTime)))
                {
                    EntityProxy  entity2Proxy = this.getOtherEnd(entity1GUID, entity1TypeName, relationship, attachmentEntityEnd, methodName);
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
     * Return the list of relationships of the requested type connecting the supplied entities with matching effectivity dates.
     *
     * @param userId  user making the request
     * @param entity1Entity  entity at end 1
     * @param entity1TypeName   entity 1's type name
     * @param entity2GUID  entity at end 2 GUID
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param exactMatchOnEffectivityDates do the effectivity dates have to match exactly (ie this is for a create/update request)
     *                                     or is it enough that the retrieved effectivity data are equal or inside the supplied time period
     *                                     (as in a retrieve).
     * @param methodName  name of calling method
     *
     * @return retrieved relationships or null
     *
     * @throws InvalidParameterException wrong type in entity 1; relationships found with incompatible effectivity dates
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public List<Relationship> getRelationshipsBetweenEntities(String               userId,
                                                              EntityDetail         entity1Entity,
                                                              String               entity1TypeName,
                                                              String               entity2GUID,
                                                              String               relationshipTypeGUID,
                                                              String               relationshipTypeName,
                                                              int                  attachmentEntityEnd,
                                                              List<InstanceStatus> limitResultsByStatus,
                                                              Date                 asOfTime,
                                                              SequencingOrder      sequencingOrder,
                                                              String               sequencingPropertyName,
                                                              boolean              forLineage,
                                                              boolean              forDuplicateProcessing,
                                                              Date                 effectiveFrom,
                                                              Date                 effectiveTo,
                                                              boolean              exactMatchOnEffectivityDates,
                                                              String               methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String localMethodName = "getRelationshipsBetweenEntities";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        long effectiveFromLong = Long.MIN_VALUE;
        long effectiveToLong   = Long.MAX_VALUE;

        String effectiveFromString = "<null>";
        String effectiveToString = "<null>";

        if (effectiveFrom != null)
        {
            effectiveFromLong = effectiveFrom.getTime();
            effectiveFromString = effectiveFrom.toString();
        }

        if (effectiveTo != null)
        {
            effectiveToLong = effectiveTo.getTime();
            effectiveToString = effectiveTo.toString();
        }

        /*
         * Retrieve all relationships of requested type connected to the starting entity.
         * EffectiveTime is null to ensure all relationships are considered.
         */
        List<Relationship>  entity1Relationships = this.getRelationshipsByType(userId,
                                                                               entity1Entity,
                                                                               entity1TypeName,
                                                                               relationshipTypeGUID,
                                                                               relationshipTypeName,
                                                                               attachmentEntityEnd,
                                                                               limitResultsByStatus,
                                                                               asOfTime,
                                                                               sequencingOrder,
                                                                               sequencingPropertyName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               0, 0,
                                                                               null,
                                                                               methodName);

        if (entity1Relationships != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (Relationship  relationship : entity1Relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entity2Proxy = this.getOtherEnd(entity1Entity.getGUID(), entity1TypeName, relationship, attachmentEntityEnd, methodName);

                    if (entity2Proxy != null)
                    {
                        if (entity2GUID.equals(entity2Proxy.getGUID()))
                        {
                            /*
                             * Both ends match - just need to test the effectivity dates.
                             */
                            if (relationship.getProperties() == null)
                            {
                                /*
                                 * No retrieved properties means no effective dates.
                                 */
                                if (((effectiveFrom == null) || (effectiveFrom.getTime() == Long.MIN_VALUE)) &&
                                    ((effectiveTo == null) || (effectiveTo.getTime() == Long.MAX_VALUE)))
                                {
                                    /*
                                     * Matching relationship because both the stored relationship and incoming request ask for
                                     * effective dates of "any".
                                     */
                                    log.debug("{} also has maximum effectivity dates", relationship.getGUID());

                                    results.add(relationship);
                                }
                                else if (exactMatchOnEffectivityDates)
                                {
                                    /*
                                     * This is an error.  The existing relationship claims the whole effective time period,
                                     * whereas the incoming request restricts the effectivity date.
                                     */
                                    log.error("{} has broader effectivity dates than requested", relationship.getGUID());

                                    throw new InvalidParameterException(RepositoryHandlerErrorCode.BROADER_EFFECTIVE_RELATIONSHIP.getMessageDefinition(relationshipTypeName,
                                                                                                                                                       relationship.getGUID(),
                                                                                                                                                       effectiveFromString,
                                                                                                                                                       effectiveToString),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        relationshipTypeName);
                                }
                                else
                                {
                                    /*
                                     * The retrieved effectivity dates are within the requested effectivity dates.
                                     */
                                    log.debug("{} has narrower effectivity dates", relationship.getGUID());

                                    results.add(relationship);
                                }
                            }
                            else
                            {
                                /*
                                 * Need to compare the effectivity dates
                                 */
                                long relationshipEffectiveFromLong = Long.MIN_VALUE;
                                long relationshipEffectiveToLong   = Long.MAX_VALUE;

                                String relationshipEffectiveFromString = "<null>";
                                String relationshipEffectiveToString = "<null>";

                                if (relationship.getProperties().getEffectiveFromTime() != null)
                                {
                                    relationshipEffectiveFromLong = relationship.getProperties().getEffectiveFromTime().getTime();
                                    relationshipEffectiveFromString = Long.toString(relationship.getProperties().getEffectiveFromTime().getTime());
                                }
                                if (relationship.getProperties().getEffectiveFromTime() != null)
                                {
                                    relationshipEffectiveToLong = relationship.getProperties().getEffectiveToTime().getTime();
                                    relationshipEffectiveToString = Long.toString(relationship.getProperties().getEffectiveToTime().getTime());

                                }

                                if ((relationshipEffectiveFromLong > effectiveToLong) || (relationshipEffectiveToLong < effectiveFromLong))
                                {
                                    /*
                                     * This relationship is outside the requested effectivity dates and so can be ignored.
                                     */
                                    log.debug("{} outside of requested effective dates and can be ignored", relationship.getGUID());
                                }
                                else if ((relationshipEffectiveFromLong == effectiveFromLong) && (relationshipEffectiveToLong == effectiveToLong))
                                {
                                    /*
                                     * This relationship has exactly matching effectivity dates and can be returned.
                                     */
                                    log.debug("{} has matching effective dates and can be returned", relationship.getGUID());

                                    results.add(relationship);
                                }
                                else if ((relationshipEffectiveFromLong > effectiveFromLong) && (relationshipEffectiveToLong < effectiveToLong))
                                {
                                    if (exactMatchOnEffectivityDates)
                                    {
                                        /*
                                         * The relationship's effectivity dates are within the requested effectivity dates but request
                                         * demands an exact match.
                                         */
                                        log.error("{} has narrower effectivity dates and exact match required", relationship.getGUID());

                                        throw new InvalidParameterException(RepositoryHandlerErrorCode.NARROWER_EFFECTIVE_RELATIONSHIP.getMessageDefinition(relationshipTypeName,
                                                                                                                                                            relationship.getGUID(),
                                                                                                                                                            relationshipEffectiveFromString,
                                                                                                                                                            relationshipEffectiveToString,
                                                                                                                                                            effectiveFromString,
                                                                                                                                                            effectiveToString),
                                                                            this.getClass().getName(),
                                                                            methodName,
                                                                            relationshipTypeName);
                                    }
                                    else
                                    {
                                        /*
                                         * The relationship's effectivity dates are within the requested effectivity dates and allowed.
                                         */
                                        log.debug("{} has narrower effectivity dates and so can return", relationship.getGUID());

                                        results.add(relationship);
                                    }
                                }
                                else
                                {
                                    /*
                                     * The relationship's effectivity dates are overlap the requested effectivity dates but request
                                     * demands an exact match.
                                     */
                                    log.error("{} has overlapping effectivity dates", relationship.getGUID());

                                    throw new InvalidParameterException(RepositoryHandlerErrorCode.OVERLAPPING_EFFECTIVE_RELATIONSHIPS.getMessageDefinition(relationshipTypeName,
                                                                                                                                                            relationship.getGUID(),
                                                                                                                                                            relationshipEffectiveFromString,
                                                                                                                                                            relationshipEffectiveToString,
                                                                                                                                                            effectiveFromString,
                                                                                                                                                            effectiveToString),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        relationshipTypeName);
                                }
                            }
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
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws InvalidParameterException wrong type in entity 1
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getRelationshipBetweenEntities(String               userId,
                                                       String               entity1GUID,
                                                       String               entity1TypeName,
                                                       String               entity2GUID,
                                                       String               relationshipTypeGUID,
                                                       String               relationshipTypeName,
                                                       List<InstanceStatus> limitResultsByStatus,
                                                       Date                 asOfTime,
                                                       SequencingOrder      sequencingOrder,
                                                       String               sequencingPropertyName,
                                                       boolean              forLineage,
                                                       boolean              forDuplicateProcessing,
                                                       Date                 effectiveTime,
                                                       String               methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String localMethodName = "getRelationshipBetweenEntities";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        List<Relationship>  entity1Relationships = this.getRelationshipsBetweenEntities(userId,
                                                                                        entity1GUID,
                                                                                        entity1TypeName,
                                                                                        entity2GUID,
                                                                                        relationshipTypeGUID,
                                                                                        relationshipTypeName,
                                                                                        2,
                                                                                        limitResultsByStatus,
                                                                                        asOfTime,
                                                                                        sequencingOrder,
                                                                                        sequencingPropertyName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
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
     * Return the relationship of the requested type connected to the starting entity and where the starting entity is the logical child.
     * The assumption is that this is a 0..1 relationship so the first matching relationship is returned (or null if there is none).
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param parentAtEnd1 boolean flag to indicate which end has the parent element
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage                   the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueParentRelationshipByType(String               userId,
                                                          String               startingEntityGUID,
                                                          String               startingEntityTypeName,
                                                          String               relationshipTypeGUID,
                                                          String               relationshipTypeName,
                                                          boolean              parentAtEnd1,
                                                          List<InstanceStatus> limitResultsByStatus,
                                                          Date                 asOfTime,
                                                          SequencingOrder      sequencingOrder,
                                                          String               sequencingPropertyName,
                                                          boolean              forLineage,
                                                          boolean              forDuplicateProcessing,
                                                          Date                 effectiveTime,
                                                          String               methodName) throws UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String localMethodName = "getUniqueParentRelationshipByType";

        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        try
        {
            int attachmentEntityEnd = 2;

            if (parentAtEnd1)
            {
                attachmentEntityEnd = 1;
            }
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(this,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           startingEntityGUID,
                                                                                           startingEntityTypeName,
                                                                                           relationshipTypeGUID,
                                                                                           relationshipTypeName,
                                                                                           attachmentEntityEnd,
                                                                                           limitResultsByStatus,
                                                                                           asOfTime,
                                                                                           sequencingOrder,
                                                                                           sequencingPropertyName,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           maxPageSize,
                                                                                           effectiveTime,
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();
                if (relationship != null)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("getUniqueParentRelationshipByType while (iterator.moreToReceive()");
                        log.debug("relationship.getGUID()=" +
                                          relationship.getGUID() +
                                          ", relationship.end1 guid=" +
                                          relationship.getEntityOneProxy().getGUID() +
                                          ",qualified name=" +
                                          relationship.getEntityOneProxy().getUniqueProperties().getInstanceProperties().get("qualifiedName") +
                                          "relationship.end2 guid " +
                                          relationship.getEntityTwoProxy().getGUID() +
                                          ",qualified name=" +
                                          relationship.getEntityTwoProxy().getUniqueProperties().getInstanceProperties().get("qualifiedName"));
                    }

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
                        if (log.isDebugEnabled())
                        {
                            log.debug("getUniqueParentRelationshipByType : returning relationship.getGUID()=" +
                                              relationship.getGUID() +
                                              "relationship.end1 guid=" +
                                              relationship.getEntityOneProxy().getGUID() +
                                              relationship.getEntityOneProxy().getUniqueProperties().getInstanceProperties().get("qualifiedName") +
                                              "relationship.end2 guid " +
                                              relationship.getEntityTwoProxy().getGUID() +
                                              relationship.getEntityTwoProxy().getUniqueProperties().getInstanceProperties().get("qualifiedName"));

                        }

                        return relationship;
                    }
                }
            }
        }
        catch (PropertyServerException | UserNotAuthorizedException error)
        {
            throw error;
        }
        catch (Exception   error)
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
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueRelationshipByType(String               userId,
                                                    String               startingEntityGUID,
                                                    String               startingEntityTypeName,
                                                    String               relationshipTypeGUID,
                                                    String               relationshipTypeName,
                                                    int                  attachmentEntityEnd,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    Date                 asOfTime,
                                                    SequencingOrder      sequencingOrder,
                                                    String               sequencingPropertyName,
                                                    boolean              forLineage,
                                                    boolean              forDuplicateProcessing,
                                                    Date                 effectiveTime,
                                                    String               methodName) throws UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String localMethodName = "getUniqueRelationshipByType";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        try
        {
            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           startingEntityGUID,
                                                                           startingEntityTypeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           attachmentEntityEnd,
                                                                           limitResultsByStatus,
                                                                           asOfTime,
                                                                           sequencingOrder,
                                                                           sequencingPropertyName,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           0, 0,
                                                                           effectiveTime,
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
        catch (Exception   error)
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
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     *
     * @return retrieved relationship or null
     *
     * @throws InvalidParameterException one of the guids is no longer available
     * @throws UserNotAuthorizedException security access problem
     * @throws PropertyServerException problem accessing the property server
     */
    public Relationship getUniqueRelationshipByType(String               userId,
                                                    String               startingEntityGUID,
                                                    String               startingEntityTypeName,
                                                    boolean              startAtEnd1,
                                                    String               relationshipTypeGUID,
                                                    String               relationshipTypeName,
                                                    List<InstanceStatus> limitResultsByStatus,
                                                    Date                 asOfTime,
                                                    SequencingOrder      sequencingOrder,
                                                    String               sequencingPropertyName,
                                                    boolean              forLineage,
                                                    boolean              forDuplicateProcessing,
                                                    Date                 effectiveTime,
                                                    String               methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String localMethodName = "getUniqueRelationshipByType";

        try
        {
            int attachmentEntityEnd = 1;
            if (startAtEnd1)
            {
                attachmentEntityEnd = 2;
            }

            List<Relationship> relationships = this.getRelationshipsByType(userId,
                                                                           startingEntityGUID,
                                                                           startingEntityTypeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           attachmentEntityEnd,
                                                                           limitResultsByStatus,
                                                                           asOfTime,
                                                                           sequencingOrder,
                                                                           sequencingPropertyName,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           0, 2,
                                                                           effectiveTime,
                                                                           methodName);

            if ((relationships != null) && (! relationships.isEmpty()))
            {
                if (relationships.size() == 1)
                {
                    return relationships.get(0);
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
        catch (PropertyServerException | UserNotAuthorizedException | InvalidParameterException error)
        {
            throw error;
        }
        catch (Exception   error)
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
     * @throws InvalidParameterException bad properties
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Relationship createRelationship(String             userId,
                                           String             relationshipTypeGUID,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             end1GUID,
                                           String             end2GUID,
                                           InstanceProperties relationshipProperties,
                                           String             methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String localMethodName = "createRelationship";
        final String newPropertiesParameterName = "newProperties";

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
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException  error)
        {
            errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName name of calling method
     *
     * @throws InvalidParameterException type of end 1 is not correct
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void ensureRelationship(String               userId,
                                   String               end1TypeName,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   String               end1GUID,
                                   String               end2GUID,
                                   String               relationshipTypeGUID,
                                   String               relationshipTypeName,
                                   InstanceProperties   relationshipProperties,
                                   List<InstanceStatus> limitResultsByStatus,
                                   Date                 asOfTime,
                                   SequencingOrder      sequencingOrder,
                                   String               sequencingPropertyName,
                                   boolean              forLineage,
                                   boolean              forDuplicateProcessing,
                                   Date                 effectiveTime,
                                   String               methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String localMethodName = "ensureRelationship";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        Relationship relationship = this.getRelationshipBetweenEntities(userId,
                                                                        end1GUID,
                                                                        end1TypeName,
                                                                        end2GUID,
                                                                        relationshipTypeGUID,
                                                                        relationshipTypeName,
                                                                        limitResultsByStatus,
                                                                        asOfTime,
                                                                        sequencingOrder,
                                                                        sequencingPropertyName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
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
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
     * The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instance's
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
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Delete a relationship between two entities.  If delete is not supported, purge is used.
     * The external source identifiers
     * are used to validate the provenance of the entity before the update.  If they are null,
     * only local cohort entities can be updated.  If they are not null, they need to match the instance's
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

        auditLog.logMessage(methodName, RepositoryHandlerAuditCode.RELATIONSHIP_DELETED.getMessageDefinition(relationship.getGUID(),
                                                                                                             relationship.getType().getTypeDefName(),
                                                                                                             relationship.getType().getTypeDefGUID(),
                                                                                                             relationship.getEntityOneProxy().getGUID(),
                                                                                                             relationship.getEntityOneProxy().getType().getTypeDefName(),
                                                                                                             relationship.getEntityOneProxy().getType().getTypeDefGUID(),
                                                                                                             relationship.getEntityTwoProxy().getGUID(),
                                                                                                             relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                             relationship.getEntityTwoProxy().getType().getTypeDefGUID(),
                                                                                                             methodName));

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
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException  error)
        {
            /* All ok since the relationship has already been deleted by another thread */
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException  error)
        {
            this.purgeRelationship(userId,
                                   relationship.getType().getTypeDefGUID(),
                                   relationship.getType().getTypeDefName(),
                                   relationship.getGUID(),
                                   methodName);
        }
        catch (Exception   error)
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
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

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
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }


    /**
     * Restore the requested relationship to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
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
            // check each end is active prior to attempting the restore

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
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
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
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeAllRelationshipsOfType(String               userId,
                                             String               externalSourceGUID,
                                             String               externalSourceName,
                                             String               startingEntityGUID,
                                             String               startingEntityTypeName,
                                             String               relationshipTypeGUID,
                                             String               relationshipTypeName,
                                             List<InstanceStatus> limitResultsByStatus,
                                             Date                 asOfTime,
                                             SequencingOrder      sequencingOrder,
                                             String               sequencingPropertyName,
                                             boolean              forLineage,
                                             boolean              forDuplicateProcessing,
                                             Date                 effectiveTime,
                                             String               methodName) throws UserNotAuthorizedException,
                                                                                     PropertyServerException,
                                                                                     InvalidParameterException
    {
        final String localMethodName = "removeAllRelationshipsOfType";
        final String typeGUIDParameterName = "relationshipTypeGUID";
        final String typeNameParameterName = "relationshipTypeName";

        errorHandler.validateTypeIdentifiers(relationshipTypeGUID,
                                             typeGUIDParameterName,
                                             relationshipTypeName,
                                             typeNameParameterName,
                                             methodName,
                                             localMethodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(this,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntityGUID,
                                                                                       startingEntityTypeName,
                                                                                       relationshipTypeGUID,
                                                                                       relationshipTypeName,
                                                                                       0,
                                                                                       limitResultsByStatus,
                                                                                       asOfTime,
                                                                                       sequencingOrder,
                                                                                       sequencingPropertyName,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       maxPageSize,
                                                                                       effectiveTime,
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
     * Update the properties in the requested relationship (if the proposed changes are different from the stored values).
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param relationship relationship to update.
     * @param relationshipProperties new properties for relationship
     * @param methodName name of calling method.
     * @return updated relationship
     *
     * @throws InvalidParameterException bad properties
     * @throws PropertyServerException there is a problem communicating with the repository.
     * @throws UserNotAuthorizedException security access problem
     */
    public Relationship updateRelationshipProperties(String             userId,
                                                     String             externalSourceGUID,
                                                     String             externalSourceName,
                                                     Relationship       relationship,
                                                     InstanceProperties relationshipProperties,
                                                     String             methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String localMethodName = "updateRelationshipProperties";
        final String newPropertiesParameterName = "newProperties";

        /*
         * This avoids any unnecessary updates
         */
        if ((relationship == null) ||
                ((relationship.getProperties() == null) && (relationshipProperties == null))  ||
                (((relationshipProperties != null) && (relationshipProperties.equals(relationship.getProperties())))))
        {
            return relationship;
        }

        try
        {
            errorHandler.validateProvenance(userId,
                                            relationship,
                                            relationship.getGUID(),
                                            externalSourceGUID,
                                            externalSourceName,
                                            methodName);

            return metadataCollection.updateRelationshipProperties(userId, relationship.getGUID(), relationshipProperties);
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * This comes from validateProvenance.  The call to validate provenance is in the try...catch
             * in case the caller has passed bad parameters.
             */
            throw error;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException  error)
        {
            errorHandler.handleUnsupportedProperty(error, methodName, newPropertiesParameterName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException  error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }

        return null;
    }


    /**
     * Update the properties in the requested relationship.  The relationship is retrieved first to validate the GUID and
     * then updated if necessary (ie if the proposed changes are different from the stored values).
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
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
        }
    }

    /**
     * Update the status in the requested relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
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
            Relationship relationship = this.getRelationshipByGUID(userId,
                                                                   relationshipGUID,
                                                                   relationshipParameterName,
                                                                   relationshipParameterName,
                                                                   null,
                                                                   methodName);

            errorHandler.validateInstanceType(relationship, relationshipTypeName, methodName, localMethodName);

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
        catch (Exception   error)
        {
            errorHandler.handleRepositoryError(error, methodName, localMethodName);
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
        catch (Exception   error)
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
