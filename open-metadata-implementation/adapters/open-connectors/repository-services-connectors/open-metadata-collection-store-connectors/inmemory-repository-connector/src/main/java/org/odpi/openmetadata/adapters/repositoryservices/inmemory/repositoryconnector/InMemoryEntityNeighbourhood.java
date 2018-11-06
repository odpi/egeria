/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.util.*;

/**
 * In memory entity neighbourhood processing to return the entities and relationships that radiate out from the supplied entity GUID.
 * The results are scoped both the instance type guids, classifications, status and the level.
 */
public class InMemoryEntityNeighbourhood
{
    private OMRSRepositoryValidator repositoryValidator = null;
    private Map<String, EntityDetail> entityStore = null;
    private Map<String, Relationship> relationshipStore = null;
    private String rootEntityGUID = null;
    private List<String> entityTypeGUIDs = null;
    private List<String> relationshipTypeGUIDs = null;
    private List<InstanceStatus> limitResultsByStatus = null;
    private List<String> limitResultsByClassification = null;
    private int level = 0;
    private Set<String> graphEntities = new HashSet<>();
    private Set<String> graphRelationships = new HashSet<>();
    private Map<String, Set<String>> entityToRelationships = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> relationshipToEntities = new HashMap<String, Set<String>>();

    /**
     * Constructor
     *
     * @param repositoryValidator          repository validator
     * @param entityStore                  entity store
     * @param relationshipStore            relationship store
     * @param rootEntityGUID               the starting point of the query.
     * @param entityTypeGUIDs              list of entity types to include in the query results.  Null means include
     *                                     all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs        list of relationship types to include in the query results.  Null means include
     *                                     all entities found, irrespective of their type.
     * @param limitResultsByStatus         By default, relationships in all statuses are returned.  However, it is possible
     *                                     to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                     status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param level                        the number of the relationships out from the starting entity that the query will traverse to
     *                                     gather results.
     */
    public InMemoryEntityNeighbourhood(OMRSRepositoryValidator repositoryValidator, Map<String, EntityDetail> entityStore, Map<String, Relationship> relationshipStore, String rootEntityGUID, List<String> entityTypeGUIDs, List<String> relationshipTypeGUIDs, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, int level)
    {
        this.repositoryValidator = repositoryValidator;
        this.entityStore = entityStore;
        this.relationshipStore = relationshipStore;
        this.rootEntityGUID = rootEntityGUID;
        this.entityTypeGUIDs = entityTypeGUIDs;
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
        this.limitResultsByStatus = limitResultsByStatus;
        this.limitResultsByClassification = limitResultsByClassification;
        // limit the level to 100 in case the algorithm gets into a circularity - hoefully this is sufficiently high for in memory demo use cases.
        if (level < 1 || level > 100)
        {
            level = 100;
        }
        this.level = level;
        initializeMaps();
    }

    /**
     * Initialize maps that help us traverse between entities and relationships using their guids
     */
    private void initializeMaps()
    {
        for (Relationship relationship : relationshipStore.values())
        {
            String relationshipGuid = relationship.getGUID();
            String relationshipEnd1Guid = getEnd1EntityGUID(relationship);
            String relationshipEnd2Guid = getEnd2EntityGUID(relationship);
            Set<String> endsGuids = new HashSet<>();
            endsGuids.add(relationshipEnd1Guid);
            endsGuids.add(relationshipEnd2Guid);
            relationshipToEntities.put(relationshipGuid, endsGuids);
            Set<String> relationshipGuids = null;
            // process end1
            if (entityToRelationships.containsKey(relationshipEnd1Guid))
            {
                relationshipGuids = entityToRelationships.get(relationshipEnd1Guid);
                if (relationshipGuids == null)
                {
                    relationshipGuids = new HashSet<>();
                }
            } else
            {
                relationshipGuids = new HashSet<>();
            }
            relationshipGuids.add(relationshipGuid);
            entityToRelationships.put(relationshipEnd1Guid, relationshipGuids);
            // process end2
            if (entityToRelationships.containsKey(relationshipEnd2Guid))
            {
                relationshipGuids = entityToRelationships.get(relationshipEnd2Guid);
                if (relationshipGuids == null)
                {
                    relationshipGuids = new HashSet<>();
                }
            } else
            {
                relationshipGuids = new HashSet<>();
            }
            relationshipGuids.add(relationshipGuid);
            entityToRelationships.put(relationshipEnd2Guid, relationshipGuids);
        }
    }

    /**
     * Verify that the supplied relationship and the 2 entities that enclose it are valid , by checking the scoping conditions
     *
     * @param relationship relationship to verify
     * @return true if valid otherwise false
     */
    private boolean verifyRelationshipForEntityNeighbourhood(Relationship relationship)
    {
        boolean valid = false;
        boolean validEntity1 = false;
        boolean validEntity2 = false;
        boolean validRelationship = false;
        if (relationship != null)
        {
            String relationshipEnd1Guid = getEnd1EntityGUID(relationship);
            String relationshipEnd2Guid = getEnd2EntityGUID(relationship);
            EntityDetail entity1 = entityStore.get(relationshipEnd1Guid);
            EntityDetail entity2 = entityStore.get(relationshipEnd2Guid);
            if (relationshipTypeGUIDs != null)
            {
                for (String relationshipTypeGUID : relationshipTypeGUIDs)
                {
                    if (repositoryValidator.verifyInstanceType(relationshipTypeGUID, relationship))
                    {
                        validRelationship = true;
                    }
                }
            } else
            {
                validRelationship = true;
            }
            if (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship))
            {
                if (limitResultsByClassification == null || limitResultsByClassification != null && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity1) && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity2))))
                {
                    if (entityTypeGUIDs != null)
                    {
                        for (String typeGUID1 : entityTypeGUIDs)
                        {
                            if (repositoryValidator.verifyInstanceType(typeGUID1, entity1))
                            {
                                // valid type

                                if (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity1))
                                {
                                    validEntity1 = true;
                                }
                            }
                            for (String typeGUID2 : entityTypeGUIDs)
                            {
                                if (repositoryValidator.verifyInstanceType(typeGUID2, entity1))
                                {
                                    // valid type

                                    if (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity2))
                                    {
                                        validEntity2 = true;
                                    }
                                }
                            }

                        }
                    } else
                    {
                        validEntity1 = true;
                        validEntity2 = true;
                    }
                }
            }
        }

        if (validEntity1 && validEntity2 && validRelationship)
        {
            valid = true;
        }
        return valid;
    }

    /**
     * Create the instance graph
     *
     * @return InstanceGraph  the instance graph that contains the entities and relationships that radiate out from the supplied entity GUID.
     */
    public InstanceGraph createInstanceGraph()
    {
        Set<String> entities = new HashSet<>();
        Set<String> visitedEntities = new HashSet<>();
        Set<String> visitedRelationships = new HashSet<>();
        entities.add(rootEntityGUID);
        this.createGraph(entities, visitedEntities, visitedRelationships, 0);
        List entityList = new ArrayList();
        // add the root entity so the returned graph is consistent.
        List relationshipList = new ArrayList();
        EntityDetail rootEntity = (entityStore.get(rootEntityGUID));
        entityList.add(rootEntity);
        for (String entityGuid : this.graphEntities)
        {
            if (!entityGuid.equals(rootEntityGUID))
            {
                entityList.add(this.entityStore.get(entityGuid));
            }
        }
        for (String relationshipGuid : this.graphRelationships)
        {
            relationshipList.add(this.relationshipStore.get(relationshipGuid));
        }
        return new InstanceGraph(entityList, relationshipList);
    }

    /**
     * CreateGraph populates maps with entities and relationships that are required in the instance graph. This is a recursive method that is called for each level that is required.
     *
     * @param entities             the entities on which we need to find the relationships out to the radiated level out.
     * @param visitedEntities      the entities that have already been visited (seen)
     * @param visitedRelationships the relationship that have already been visited (seen)
     * @param currentLevel         the current level
     */
    private void createGraph(Set<String> entities, Set visitedEntities, Set visitedRelationships, int currentLevel)
    {
        Set<String> nextEntitySet = new HashSet<>();
        for (String entityGuid : entities)
        {
            Set<String> relationships = this.entityToRelationships.get(entityGuid);
            if (relationships != null)
            {
                for (String relationshipGuid : relationships)
                {
                    Relationship relationship = this.relationshipStore.get(relationshipGuid);
                    // check to see if we have already visited this relationship
                    if (!visitedRelationships.contains(relationshipGuid))
                    {
                        if (verifyRelationshipForEntityNeighbourhood(relationship))
                        {
                            // valid relationship and entities
                            graphEntities.add(entityGuid);
                            final String end1Guid = getEnd1EntityGUID(relationship);
                            final String end2Guid = getEnd2EntityGUID(relationship);
                            graphRelationships.add(relationshipGuid);
                            // add the entities - one end will already be there so will be replaced.
                            graphEntities.add(end1Guid);
                            graphEntities.add(end2Guid);
                            // if we have not see the other end then we need to traverse to it.
                            if (end1Guid.equals(entityGuid) && !visitedEntities.contains(end2Guid))
                            {
                                nextEntitySet.add(end2Guid);
                            }
                            if (end2Guid.equals(entityGuid) && !visitedEntities.contains(end1Guid))
                            {
                                nextEntitySet.add(end1Guid);
                            }
                            visitedEntities.add(end1Guid);
                            visitedEntities.add(end2Guid);
                            visitedRelationships.add(relationshipGuid);
                        }
                    }
                }
            }
        }
        int nextLevel = currentLevel + 1;
        if (nextLevel < this.level && nextEntitySet.size() > 0)
        {
            // recurse
            createGraph(nextEntitySet, visitedEntities, visitedRelationships, nextLevel);
        }
    }

    /**
     * Return the guid of an entity linked to end 1 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    private String getEnd1EntityGUID(Relationship relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                if (entityProxy.getGUID() != null)
                {
                    return entityProxy.getGUID();
                }
            }
        }
        return null;
    }

    /**
     * Return the guid of an entity linked to end 2 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    private String getEnd2EntityGUID(Relationship relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                if (entityProxy.getGUID() != null)
                {
                    return entityProxy.getGUID();
                }
            }
        }
        return null;
    }
}