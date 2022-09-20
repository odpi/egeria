/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DuplicateEntityIterator retrieves the list of entities that need to be processed for a specific entity.
 * The first entity returned is the principle entity or its consolidated replacement.  After that are the peer duplicates.
 *
 * Note: this class is not thread-safe - use only within a single-threaded request.
 */
public class DuplicateEntityIterator
{
    private static final String consolidatedDuplicate         = "ConsolidatedDuplicate";
    private static final String consolidatedDuplicateLinkName = "ConsolidatedDuplicateLink";
    private static final String consolidatedDuplicateLinkGUID = "a1fabffd-d6ec-4b2d-bfe4-646f27c07c82";
    private static final String knownDuplicate                = "KnownDuplicate";
    private static final String peerDuplicateLink             = "PeerDuplicateLink";
    private static final String peerDuplicateLinkGUID         = "a94b2929-9e62-4b12-98ab-8ac45691e5bd";

    private static final String statusPropertyName            = "statusIdentifier";
    private static final int    statusThreshold               = 1;

    private static final String memento                       = "Memento";

    private static final Logger log = LoggerFactory.getLogger(DuplicateEntityIterator.class);

    private final RepositoryHandler       repositoryHandler;
    private final RepositoryErrorHandler  errorHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final String                  userId;
    private final String                  entityTypeName;
    private final String                  methodName;
    private final boolean                 forLineage;
    private final boolean                 forDuplicateProcessing;
    private final Date                    effectiveTime;

    private EntityDetail cachedEntity = null;

    private final Set<String>        processedPeerGUIDs = new HashSet<>();
    private final List<EntityDetail> unprocessedPeers   = new ArrayList<>();


    /**
     * Construct the duplicate entity iterator.
     *
     * @param repositoryHandler this repository handler
     * @param errorHandler       generates error messages and exceptions
     * @param invalidParameterHandler handles parameter errors
     * @param userId calling user
     * @param startingEntity entity to start processing on
     * @param entityTypeName type of entities that the iterator is working with
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this a duplicate processing request
     * @param effectiveTime what is the effective time needed by the caller
     * @param methodName calling method
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException broader security failure
     * @throws PropertyServerException logic error
     */
    public DuplicateEntityIterator(RepositoryHandler       repositoryHandler,
                                   RepositoryErrorHandler  errorHandler,
                                   InvalidParameterHandler invalidParameterHandler,
                                   String                  userId,
                                   EntityDetail            startingEntity,
                                   String                  entityTypeName,
                                   boolean                 forLineage,
                                   boolean                 forDuplicateProcessing,
                                   Date                    effectiveTime,
                                   String                  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.invalidParameterHandler = invalidParameterHandler;
        this.userId = userId;
        this.entityTypeName = entityTypeName;
        this.forLineage = forLineage;
        this.forDuplicateProcessing = forDuplicateProcessing;
        this.effectiveTime = effectiveTime;
        this.methodName = methodName;
        this.unprocessedPeers.add(startingEntity);

        fillCache();
    }


    /**
     * Initialize the iterator based on the callers specification and the content of the principle entity.
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException broader security failure
     * @throws PropertyServerException logic error
     */
    private void fillCache() throws InvalidParameterException,
                                    UserNotAuthorizedException,
                                    PropertyServerException
    {
        this.getNextEntity();

        while ((cachedEntity == null) && (! unprocessedPeers.isEmpty()))
        {
            this.getNextEntity();
        }
    }



    /**
     * Process the entity and add to cachedEntity.
     *
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException broader security failure
     * @throws PropertyServerException logic error
     */
    private void getNextEntity() throws InvalidParameterException,
                                        UserNotAuthorizedException,
                                        PropertyServerException
    {
        EntityDetail processingEntity = null;

        /*
         * Get the next (or first) peer to process.
         */
        if (! unprocessedPeers.isEmpty())
        {
            processingEntity = unprocessedPeers.remove(unprocessedPeers.size()-1);
        }

        while ((! unprocessedPeers.isEmpty()) && (this.processedPeerGUIDs.contains(processingEntity.getGUID())))
        {
            processingEntity = unprocessedPeers.remove(unprocessedPeers.size()-1);
        }

        if ((processingEntity != null) && (! this.processedPeerGUIDs.contains(processingEntity.getGUID())))
        {
            /*
             * Found a new entity to process
             */
            processedPeerGUIDs.add(processingEntity.getGUID());

            /*
             * Assume this is the one to return.
             */
            cachedEntity = processingEntity;

            if (! forDuplicateProcessing)
            {
                boolean deduplicationNeeded = false;

                if (processingEntity.getClassifications() != null)
                {
                    for (Classification classification : processingEntity.getClassifications())
                    {
                        if (classification != null)
                        {
                            /*
                             * Ignore any classification that is not active at this time.
                             */
                            if (repositoryHandler.isCorrectEffectiveTime(classification.getProperties(), effectiveTime))
                            {
                                if (knownDuplicate.equals(classification.getName()))
                                {
                                    log.debug("KnownDuplicate classification detected");
                                    deduplicationNeeded = true;
                                }
                            }
                        }
                    }
                }

                if (deduplicationNeeded)
                {
                    /*
                     * First look to see if there is a consolidated entity.  This will take precedence.  Notice that the type of the retrieved
                     * entity is used to validate the type of the consolidated entity.
                     */
                    EntityDetail consolidatedEntity = this.getConsolidatedEntity(userId,
                                                                                 processingEntity,
                                                                                 processingEntity.getType().getTypeDefName(),
                                                                                 forLineage,
                                                                                 effectiveTime,
                                                                                 methodName);

                    /*
                     * If a consolidated entity is returned, it must have an appropriate status before it can be used.
                     */
                    if ((consolidatedEntity != null) && (consolidatedEntity.getClassifications() != null))
                    {
                        log.debug("Consolidated entity returned: " + consolidatedEntity.getGUID());

                        /*
                         * Need to check the ConsolidatedDuplicate classification
                         */
                        for (Classification classification : consolidatedEntity.getClassifications())
                        {
                            if (classification != null)
                            {
                                if (consolidatedDuplicate.equals(classification.getName()))
                                {
                                    if (errorHandler.validateStatus(statusPropertyName, 1, classification.getProperties(), methodName))
                                    {
                                        log.debug("Valid consolidated entity: " + consolidatedEntity.getGUID() + " so making it the visible entity");

                                        cachedEntity = consolidatedEntity;
                                        deduplicationNeeded = false;
                                    }
                                    else
                                    {
                                        log.debug("Ignoring consolidated entity: " +  consolidatedEntity.getGUID() + " due to status setting");
                                    }
                                }
                            }
                        }
                    }
                }

                if (deduplicationNeeded)
                {
                    /*
                     * Retrieve the peers of this entity and save them for later processing.
                     */
                    RepositoryRelationshipsIterator peerIterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                                       invalidParameterHandler,
                                                                                                       userId,
                                                                                                       processingEntity,
                                                                                                       entityTypeName,
                                                                                                       peerDuplicateLinkGUID,
                                                                                                       peerDuplicateLink,
                                                                                                       0,
                                                                                                       true,
                                                                                                       true,
                                                                                                       0,
                                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                                       null,
                                                                                                       methodName);

                    while (peerIterator.moreToReceive())
                    {
                        try
                        {
                            Relationship relationship = peerIterator.getNext();

                            if (errorHandler.validateStatus(statusPropertyName, statusThreshold, relationship.getProperties(), methodName))
                            {
                                EntityProxy peerProxy = repositoryHandler.getOtherEnd(processingEntity.getGUID(), relationship);

                                if (! processedPeerGUIDs.contains(peerProxy.getGUID()))
                                {
                                    /*
                                     * It is important that all peers are included in the processing even if they are not valid to return to the caller.
                                     */
                                    final String guidParameterName = "peerProxy.getGUID()";

                                    /*
                                     * Filter out the peers that were already added to the list of unprocessed peers.
                                     */
                                    if(!unprocessedPeers.stream().map(InstanceHeader::getGUID).collect(Collectors.toList()).contains(peerProxy.getGUID()))
                                    {
                                        EntityDetail peerEntity = repositoryHandler.validateEntityGUID(userId,
                                                                                                       peerProxy.getGUID(),
                                                                                                       guidParameterName,
                                                                                                       entityTypeName,
                                                                                                       methodName);

                                        /*
                                        * Save the entity for later processing since it may link to peer entities that the current processing entity
                                        * does not know about.
                                        */
                                        unprocessedPeers.add(peerEntity);
                                    }
                                }
                            }
                        }
                        catch (Exception error)
                        {
                            log.debug("Ignored unreachable relationship/entity: " + error);
                        }
                    }
                }
            }

            if (! isEntityValidToReturn(this.cachedEntity))
            {
                this.cachedEntity = null;
            }
        }
        else
        {
            this.cachedEntity = null;
        }
    }


    /**
     * Validate if the entity matches the request parameters.
     * @param entity entity to test
     * @return flag to indicate whether it can be returned
     */
    private boolean isEntityValidToReturn(EntityDetail entity)
    {
        /*
         * If the processing element is effective at this time, it can be used.
         */
        if (repositoryHandler.isCorrectEffectiveTime(entity.getProperties(), effectiveTime))
        {
            log.debug("Entity" + entity.getGUID() + " is effective");

            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        /*
                         * Ignore any classification that is not active at this time.
                         */
                        if (repositoryHandler.isCorrectEffectiveTime(classification.getProperties(), effectiveTime))
                        {
                            if (memento.equals(classification.getName()))
                            {
                                /*
                                 * The Memento classification means that the element is logically deleted but kept active in the repository
                                 * to support the linkage needed for lineage.
                                 */
                                if (forLineage)
                                {
                                    log.debug("Lineage request - Ignoring Memento classification");
                                }
                                else
                                {
                                    log.debug("Memento classification detected");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            log.debug("Entity can be returned");
            return true;
        }
        else
        {
            log.debug("Ignoring entity due to effectivity dates");
            return false;
        }
    }


    /**
     * Is there a possibility of more peer entities to process?
     *
     * @return flag is not exhausted the possibilities
     */
    boolean morePeersToReceive()
    {
        return (this.cachedEntity != null);
    }


    /**
     * Retrieve the next peer entity in the deduplication
     *
     * @return entity or null
     * @throws InvalidParameterException bad parameters
     * @throws UserNotAuthorizedException broader security failure
     * @throws PropertyServerException logic error
     */
    EntityDetail getNextPeer() throws InvalidParameterException,
                                      UserNotAuthorizedException,
                                      PropertyServerException
    {
        if (cachedEntity != null)
        {
            EntityDetail nextPeer = cachedEntity;

            fillCache();

            return nextPeer;
        }

        return null;
    }


    /**
     * Return the entity at the other end of the requested relationship type.  The assumption is that this is a 0..1
     * relationship so one entity (or null) is returned.  If lots of relationships are found then the
     * PropertyServerException is thrown.
     *
     * @param userId  user making the request
     * @param startingEntity starting entity
     * @param startingEntityTypeName  starting entity's type name
     * @param forLineage the query is to support lineage retrieval
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @return retrieved entity or null
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    public EntityDetail getConsolidatedEntity(String       userId,
                                              EntityDetail startingEntity,
                                              String       startingEntityTypeName,
                                              boolean      forLineage,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return repositoryHandler.getEntityForRelationshipType(userId,
                                                              startingEntity,
                                                              startingEntityTypeName,
                                                              consolidatedDuplicateLinkGUID,
                                                              consolidatedDuplicateLinkName,
                                                              null,
                                                              0,
                                                              startingEntityTypeName,
                                                              2,
                                                              forLineage,
                                                              true,
                                                              effectiveTime,
                                                              methodName);
    }
}


