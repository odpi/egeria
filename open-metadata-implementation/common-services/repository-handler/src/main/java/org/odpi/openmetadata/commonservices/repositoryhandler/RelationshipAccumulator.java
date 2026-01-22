/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accumulates relationships from multiple retrieval requests.
 */
public class RelationshipAccumulator
{
    private final OMRSRepositoryHelper   repositoryHelper;
    private final RepositoryHandler      repositoryHandler;
    private final RepositoryErrorHandler errorHandler;
    private final boolean                forDuplicateProcessing;
    private final Date                   effectiveTime;
    private final AuditLog               auditLog;
    private final String                 methodName;

    private Map<String, List<Relationship>> relationshipMap = null;

    private static final Logger log = LoggerFactory.getLogger(RelationshipAccumulator.class);


    /**
     * Create the relationship accumulator.
     *
     * @param repositoryHelper methods for manipulating OMRS elements
     * @param repositoryHandler this repository handler
     * @param errorHandler       generates error messages and exceptions
     * @param forDuplicateProcessing is this a duplicate processing request
     * @param effectiveTime what is the effective time needed by the caller
     * @param methodName calling method
     */
    RelationshipAccumulator(OMRSRepositoryHelper   repositoryHelper,
                            RepositoryHandler      repositoryHandler,
                            RepositoryErrorHandler errorHandler,
                            boolean                forDuplicateProcessing,
                            Date                   effectiveTime,
                            AuditLog               auditLog,
                            String                 methodName)
    {
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.effectiveTime = effectiveTime;
        this.forDuplicateProcessing = forDuplicateProcessing;
        this.auditLog = auditLog;
        this.methodName = methodName;
    }

    /**
     * Add relationships that have just been retrieved for a specific entity. Only relationships with an appropriate effective time are saved.
     * The end of the relationship that is the receiving end is updated to the starting entity.
     * Note this method should only be called if
     *
     * @param startingEntity this is an entity proxy for the starting entity.  It will be added to the appropriate end of each relationship
     * @param receivingEntityGUID this is the guid of the entity used to retrieve the relationships
     * @param retrievedRelationships list of relationships from the repositories.
     */
    void addRelationships(EntityProxy        startingEntity,
                          String             receivingEntityGUID,
                          List<Relationship> retrievedRelationships)
    {
        if (retrievedRelationships != null)
        {
            if (receivingEntityGUID.equals(startingEntity.getGUID()))
            {
                /*
                 * Retrieving based on the requested entity - rather than a peer duplicate.
                 */
                this.addRelationships(retrievedRelationships);
            }
            else
            {
                List<Relationship> modifiedRelationships = new ArrayList<>();

                for (Relationship retrievedRelationship : retrievedRelationships)
                {
                    if (retrievedRelationship != null)
                    {
                        log.debug("Processing relationship " + retrievedRelationship.getGUID() + " of type " + retrievedRelationship.getType().getTypeDefName());

                        Relationship modifiedRelationship = new Relationship(retrievedRelationship);

                        if (receivingEntityGUID.equals(modifiedRelationship.getEntityOneProxy().getGUID()))
                        {
                            log.debug("Updating other end (1) from " + modifiedRelationship.getEntityOneProxy().getGUID() + " to " + startingEntity.getGUID());
                            modifiedRelationship.setEntityOneProxy(startingEntity);
                        }

                        if (receivingEntityGUID.equals(modifiedRelationship.getEntityTwoProxy().getGUID()))
                        {
                            log.debug("Updating other end (2) from " + modifiedRelationship.getEntityTwoProxy().getGUID() + " to " + startingEntity.getGUID());
                            modifiedRelationship.setEntityTwoProxy(startingEntity);
                        }

                        modifiedRelationships.add(modifiedRelationship);
                    }
                }

                this.addRelationships(modifiedRelationships);
            }
        }
    }


    /**
     * Add relationships that have just been retrieved. Only relationships with an appropriate effective time are saved.
     * This method is used when relationships are being retrieved independent of an entity starting end,
     *
     * @param retrievedRelationships list of relationships from the repositories.
     */
    void addRelationships(List<Relationship> retrievedRelationships)
    {
        if (retrievedRelationships != null)
        {
            if (relationshipMap == null)
            {
                relationshipMap = new HashMap<>();
            }

            /*
             * Sort relationships by type into the relationship map - removing those that are not effective at this time.
             */
            for (Relationship relationship : retrievedRelationships)
            {
                if (relationship != null)
                {
                    if (repositoryHandler.isCorrectEffectiveTime(relationship.getProperties(), effectiveTime))
                    {
                        List<Relationship> similarRelationships = relationshipMap.get(relationship.getType().getTypeDefName());

                        if (similarRelationships == null)
                        {
                            similarRelationships = new ArrayList<>();
                        }

                        similarRelationships.add(relationship);

                        relationshipMap.put(relationship.getType().getTypeDefName(), similarRelationships);
                    }
                }
            }

            /*
             * The map contains list of relationships sorted by type.
             */

            if (log.isDebugEnabled())
            {
                log.debug("There are " + relationshipMap.size() + " different types of relationships after filtering for effective time " + effectiveTime);
            }
        }
        else if (log.isDebugEnabled())
        {
            log.debug("No relationships returned");
        }
    }



    /**
     * Return the list of filtered relationships.
     *
     * @param startingEntityGUID this is the guid of the entity used to retrieve the relationships
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @return list of relationships.  Null means no relationships were retrieved from the repositories; Empty list means all retrieved relationships
     * were filtered out and can retrieve again.
     */
    List<Relationship> getRelationships(String startingEntityGUID,
                                        int    attachmentEntityEnd)
    {
        if (relationshipMap != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (String relationshipTypeName : relationshipMap.keySet())
            {
                if (relationshipTypeName != null)
                {
                    List<Relationship> similarRelationships = relationshipMap.get(relationshipTypeName);

                    if (forDuplicateProcessing)
                    {
                        results.addAll(similarRelationships);
                    }
                    else if (similarRelationships != null)
                    {
                        log.debug(similarRelationships.size() + " relationships of type " + relationshipTypeName);
                        List<Relationship> filteredRelationships = this.filterOutDuplicateRelationships(relationshipTypeName, similarRelationships, startingEntityGUID, attachmentEntityEnd);

                        if (filteredRelationships != null)
                        {
                            log.debug(filteredRelationships.size() + " filtered relationships of type " + relationshipTypeName);
                            results.addAll(filteredRelationships);
                        }
                    }
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the list of filtered relationships.
     *
     * @return list of relationships.  Null means no relationships were retrieved from the repositories; Empty list means all retrieved relationships
     * were filtered out and can retrieve again.
     */
    List<Relationship> getRelationships()
    {
        return getRelationships( null, 0);
    }


    /**
     * Scan a list of relationships and remove any duplicate instances.
     *
     * @param initialRelationships initial list
     * @return deduplicated list
     */
    private List<Relationship> removeDuplicateRelationshipInstances(List<Relationship> initialRelationships)
    {
        Map<String, Relationship> usedRelationshipGUIDs = new HashMap<>();

        for (Relationship retrievedRelationship : initialRelationships)
        {
            Relationship duplicateRelationship = usedRelationshipGUIDs.get(retrievedRelationship.getGUID());

            if ((duplicateRelationship == null) || (errorHandler.validateIsLatestUpdate(duplicateRelationship, retrievedRelationship)))
            {
                /*
                 * Replacing the relationship with the previous one because it is newer.
                 */
                usedRelationshipGUIDs.put(retrievedRelationship.getGUID(), retrievedRelationship);
            }
            else
            {
                log.debug("Skipping duplicate for relationship " + retrievedRelationship.getGUID());
            }
        }

        return new ArrayList<>(usedRelationshipGUIDs.values());
    }



    /**
     * Return the relationships that should be returned to the caller.   Null means no relationships retrieved.  Empty list means all relationships
     * filtered out.
     * This method needs to perform multiple passes on the relationships.  The first pass removes older versions of the same relationship instance.
     * If the relationship is multiLink or there are no entities supplied in duplicate cluster, the deduplicated list is returned.
     * UniLink entities do not allow two relationships between the same two elements.  If there is a duplicate cluster then these entities need to
     * be treated as if they are one.  Relationships of the same type that link any of these entities (from the same end) to the same remote element
     * need to be filtered out.
     * The final phase it to filter out relationships that violate the cardinality of the relationship
     *
     * @param relationshipTypeName type of relationship
     * @param retrievedRelationships list of relationships retrieved from the repository
     * @param startingEntityGUID unique identifier of starting entity
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @return list of filtered relationships
     */
    private List<Relationship> filterOutDuplicateRelationships(String             relationshipTypeName,
                                                               List<Relationship> retrievedRelationships,
                                                               String             startingEntityGUID,
                                                               int                attachmentEntityEnd)
    {
        if ((retrievedRelationships != null) && (! retrievedRelationships.isEmpty()))
        {
            log.debug(retrievedRelationships.size() + " relationships received of type " + relationshipTypeName);

            /*
             * First remove the duplicated relationships.  These occur when the same consolidated entity is requested multiple times because
             * the peers are being retrieved.  In general, the find methods try to remove these duplicates before the relationship are retrieved
             * but not all retrieves follow the same path so this is a catchall piece of logic.
             */
            List<Relationship> deDuplicatedRelationships = removeDuplicateRelationshipInstances(retrievedRelationships);

            log.debug(deDuplicatedRelationships.size() + " deduplicated relationships received of type " + relationshipTypeName);

            /*
             * Switching to focus on entities.
             */
            if (startingEntityGUID != null)
            {
                /*
                 * Need to understand the settings in the RelationshipDef
                 */
                TypeDef typeDef = repositoryHelper.getTypeDefByName(methodName, relationshipTypeName);

                if (typeDef instanceof RelationshipDef relationshipDef)
                {
                    if (relationshipDef.getMultiLink())
                    {
                        /*
                         * Nothing more can be done for multiLinks
                         */
                        log.debug(deDuplicatedRelationships.size() + " multi-link relationships received of type " + relationshipTypeName);
                        return deDuplicatedRelationships;
                    }

                    /*
                     * This map links the combination of the GUIDs from end 1 and end 2 to the relationship.
                     * Looking for the same relationship between the same entities.
                     */
                    Map<String, Relationship> uniLinkMap = new HashMap<>();

                    for (Relationship deDuplicatedRelationship : deDuplicatedRelationships)
                    {
                        Relationship existingMappedRelationship = uniLinkMap.get(deDuplicatedRelationship.getEntityOneProxy().getGUID() + deDuplicatedRelationship.getEntityTwoProxy().getGUID());

                        if ((existingMappedRelationship == null) || (errorHandler.validateIsLatestUpdate(existingMappedRelationship, deDuplicatedRelationship)))
                        {
                            uniLinkMap.put(deDuplicatedRelationship.getEntityOneProxy().getGUID() + deDuplicatedRelationship.getEntityTwoProxy().getGUID(), deDuplicatedRelationship);
                        }
                    }

                    List<Relationship> validUniLinkRelationships = new ArrayList<>(uniLinkMap.values());

                    /*
                     * Are relationships many to many so can return the results of the UniLink filtering
                     */
                    if ((relationshipDef.getEndDef1().getAttributeCardinality() == RelationshipEndCardinality.ANY_NUMBER) &&
                                (relationshipDef.getEndDef2().getAttributeCardinality() == RelationshipEndCardinality.ANY_NUMBER))
                    {
                        log.debug(validUniLinkRelationships.size() + " valid uni-link relationships received of type " + relationshipTypeName);

                        if (validUniLinkRelationships.size() != retrievedRelationships.size())
                        {
                            logDeDuplicationResults(retrievedRelationships, validUniLinkRelationships);
                        }

                        return validUniLinkRelationships;
                    }

                    /*
                     * Considering situations where there are multiple results for relationship where only one element is allowed.
                     */
                    Map<String, Relationship> usedEntityGUIDs = new HashMap<>();
                    List<Relationship> validEnd1Relationships;

                    /*
                     * Search for multiple results at end 1
                     */
                    if (relationshipDef.getEndDef1().getAttributeCardinality() == RelationshipEndCardinality.AT_MOST_ONE)
                    {
                        /*
                         * If attachment end = 2 the UniLink work removed all the duplicates from end 1 since all values received were based on this entity.
                         */
                        if (attachmentEntityEnd != 2)
                        {
                            for (Relationship relationship : validUniLinkRelationships)
                            {
                                Relationship matchingRelationship = usedEntityGUIDs.get(relationship.getEntityTwoProxy().getGUID());

                                if (matchingRelationship == null)
                                {
                                    /*
                                     * Relationship with new end 1.
                                     */
                                    log.debug("New Proxy end 2:  " + relationship.getGUID());
                                    usedEntityGUIDs.put(relationship.getEntityTwoProxy().getGUID(), relationship);
                                }
                                else if (errorHandler.validateIsLatestUpdate(matchingRelationship, relationship))
                                {
                                    /*
                                     * Replacing the relationship with the previous one because it is newer.
                                     */
                                    log.debug("Matching Proxy end 2: Replacing relationship " + matchingRelationship.getGUID() + " with " + relationship.getGUID());
                                    usedEntityGUIDs.put(relationship.getEntityTwoProxy().getGUID(), relationship);
                                }
                            }

                            validEnd1Relationships = new ArrayList<>(usedEntityGUIDs.values());

                            /*
                             * Is end1 independent of end2?
                             */
                            if (! relationshipDef.getEndDef1().getAttributeName().equals(relationshipDef.getEndDef2().getAttributeName()))
                            {
                                /*
                                 * The attribute names are different at either end of the relationship.  This means the ends have a particular
                                 * direction, and we can process each end independently.
                                 */
                                if (! usedEntityGUIDs.isEmpty())
                                {
                                    log.debug("Independent ends for type " + relationshipTypeName);
                                    usedEntityGUIDs = new HashMap<>();
                                }
                            }
                        }
                        else
                        {
                            log.debug("Skipping attachment end 1 validation");
                            validEnd1Relationships = validUniLinkRelationships;
                        }
                    }
                    else
                    {
                        log.debug("Skipping end 1 validation");
                        validEnd1Relationships = validUniLinkRelationships;
                    }

                    log.debug(validEnd1Relationships.size() + " valid end1 relationships received of type " + relationshipTypeName);

                    List<Relationship> validCardinalityRelationships = new ArrayList<>();

                    /*
                     * Search for duplicates at end 2
                     */
                    if (relationshipDef.getEndDef2().getAttributeCardinality() == RelationshipEndCardinality.AT_MOST_ONE)
                    {
                        if (attachmentEntityEnd != 1)
                        {
                            for (Relationship relationship : validEnd1Relationships)
                            {
                                if (relationship != null)
                                {
                                    Relationship duplicateRelationship = usedEntityGUIDs.get(relationship.getEntityOneProxy().getGUID());

                                    if (duplicateRelationship == null)
                                    {
                                        /*
                                         * Replacing the existing relationship with the previous one because it is newer.
                                         */
                                        log.debug("New proxy end 2: " + relationship.getGUID());
                                        usedEntityGUIDs.put(relationship.getEntityOneProxy().getGUID(), relationship);
                                    }
                                    else if  (errorHandler.validateIsLatestUpdate(duplicateRelationship, relationship))
                                    {
                                        /*
                                         * Replacing the existing relationship with the previous one because it is newer.
                                         */
                                        log.debug("Matching Proxy end 1: Replacing relationship " + duplicateRelationship.getGUID() + " with " + relationship.getGUID());
                                        usedEntityGUIDs.put(relationship.getEntityOneProxy().getGUID(), relationship);
                                    }
                                }
                            }

                            /*
                             * Add filtered relationships to the results.
                             */
                            if (! usedEntityGUIDs.isEmpty())
                            {
                                log.debug("Valid relationships");

                                validCardinalityRelationships.addAll(usedEntityGUIDs.values());
                            }
                            else
                            {
                                log.debug("No valid relationships");
                            }
                        }
                        else
                        {
                            log.debug("Skipping attachment end 2 validation");
                            validCardinalityRelationships = validEnd1Relationships;
                        }
                    }
                    else
                    {
                        log.debug("Skipping end 2 validation");
                        validCardinalityRelationships = validEnd1Relationships;
                    }

                    log.debug(validCardinalityRelationships.size() + " valid cardinality relationships received of type " + relationshipTypeName);

                    if (validCardinalityRelationships.size() != retrievedRelationships.size())
                    {
                        logDeDuplicationResults(retrievedRelationships, validCardinalityRelationships);
                    }

                    /*
                     * One final removal of duplicates since we may have processed both ends.
                     */
                    return removeDuplicateRelationshipInstances(validCardinalityRelationships);
                }
                else
                {
                    /*
                     * This is a logic error
                     */
                    log.error("Typedef " + relationshipTypeName + " is not a relationship");
                }
            }
            else
            {
                /*
                 * This request was not focused on a specific entity and so there is no reasonable way to deduplicate the relationships.
                 */
                log.debug(deDuplicatedRelationships.size() + " unfiltered deduplicated relationships received of type " + relationshipTypeName);
                return deDuplicatedRelationships;
            }
        }

        return null;
    }


    /**
     * Log the effect of the relationship deduplication.
     *
     * @param retrievedRelationships relationships retrieved from the repository
     * @param resultingRelationships relationships being returned.
     */
    private void logDeDuplicationResults(List<Relationship>  retrievedRelationships,
                                         List<Relationship>  resultingRelationships)
    {
        List<String> originalRelationships = new ArrayList<>();
        List<String> deDupedRelationships = new ArrayList<>();

        for (Relationship relationship : resultingRelationships)
        {
            if (relationship != null)
            {
                deDupedRelationships.add(relationship.getGUID());
            }
        }

        for (Relationship relationship : retrievedRelationships)
        {
            if (relationship != null)
            {
                originalRelationships.add(relationship.getGUID() + "[" + relationship.getCreatedBy() + "," + relationship.getType().getTypeDefName() + "]");
            }
        }

        auditLog.logMessage(methodName,
                            RepositoryHandlerAuditCode.RELATION_DEDUP_SUMMARY.getMessageDefinition(originalRelationships.toString(),
                                                                                                   deDupedRelationships.toString()));
    }
}

