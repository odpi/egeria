/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;

/**
 * In memory entity neighbourhood processing to return the entities and relationships that radiate out from the supplied entity GUID.
 * The results are scoped both the instance type guids, classifications, status and the level.
 */
class InMemoryEntityNeighbourhood
{
    private OMRSRepositoryValidator   repositoryValidator;
    private OMRSRepositoryHelper      repositoryHelper;
    private String                    repositoryName;
    private Map<String, EntityDetail> entityStore;
    private Map<String, Relationship> relationshipStore;
    private String                    rootEntityGUID;
    private List<String>              entityTypeGUIDs;
    private List<String>              relationshipTypeGUIDs;
    private List<InstanceStatus>      limitResultsByStatus;
    private List<String>              limitResultsByClassification;
    private int                       level;
    private Set<String>               graphEntities          = new HashSet<>();
    private Set<String>               graphRelationships     = new HashSet<>();
    private Map<String, Set<String>>  entityToRelationships  = new HashMap<>();
    private Map<String, Set<String>>  relationshipToEntities = new HashMap<>();

    /**
     * Constructor
     *
     * @param repositoryHelper             helper methods when calling the repository connector
     * @param repositoryName               name of this repository
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
     */
    InMemoryEntityNeighbourhood(OMRSRepositoryHelper      repositoryHelper,
                                String                    repositoryName,
                                OMRSRepositoryValidator   repositoryValidator,
                                Map<String, EntityDetail> entityStore,
                                Map<String, Relationship> relationshipStore,
                                String                    rootEntityGUID,
                                List<String>              entityTypeGUIDs,
                                List<String>              relationshipTypeGUIDs,
                                List<InstanceStatus>      limitResultsByStatus,
                                List<String>              limitResultsByClassification,
                                int                       level)
    {
        this.repositoryHelper = repositoryHelper;
        this.repositoryName = repositoryName;
        this.repositoryValidator = repositoryValidator;
        this.entityStore = entityStore;
        this.relationshipStore = relationshipStore;
        this.rootEntityGUID = rootEntityGUID;
        this.entityTypeGUIDs = entityTypeGUIDs;
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
        this.limitResultsByStatus = limitResultsByStatus;
        this.limitResultsByClassification = limitResultsByClassification;
        /*
         * limit the level to 100 in case the algorithm gets into a circularity - hopefully this is sufficiently high for in memory demo use cases.
         */
        if (level < 0 || level > 100)
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
            String      relationshipGuid     = relationship.getGUID();
            String      relationshipEnd1Guid = getEnd1EntityGUID(relationship);
            String      relationshipEnd2Guid = getEnd2EntityGUID(relationship);
            Set<String> endsGuids            = new HashSet<>();

            endsGuids.add(relationshipEnd1Guid);
            endsGuids.add(relationshipEnd2Guid);
            relationshipToEntities.put(relationshipGuid, endsGuids);
            Set<String> relationshipGuids;

            /*
             * process end1
             */
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
            /*
             * process end2
             */
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
     * Verify that the supplied relationship and the 2 entities that enclose it are valid, by checking the scoping conditions
     *
     * @param relationship relationship to verify
     * @return true if valid otherwise false
     * @throws TypeErrorException type error
     */
    private boolean verifyRelationshipForEntityNeighbourhood(Relationship relationship) throws TypeErrorException
    {
        boolean valid = false;
        boolean validEntity1 = false;
        boolean validEntity2 = false;
        boolean validRelationship = false;

        if (!validateRelationshipAgainstEntityTypes(relationship))
        {
           return false;
        }

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
                    if (repositoryValidator.verifyInstanceType(repositoryName, relationshipTypeGUID, relationship))
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
                if (limitResultsByClassification == null ||
                    limitResultsByClassification != null &&
                    (includeEntityIfClassifiedAppropriately(limitResultsByClassification, entity1) &&
                    (includeEntityIfClassifiedAppropriately(limitResultsByClassification, entity2))))
                {
                    if (entityTypeGUIDs != null) {
                        if (graphEntities.contains(relationshipEnd1Guid)) {
                            validEntity1 = true;
                        }
                        else {
                            /*
                             * If the entity is already included there is no need to test the type of this end entity.
                             * By omitting the test there is no need to include the root entity type GUID in the
                             * entityTypeGUIDs filtering list. This is beneficial because, although it could be included
                             * and the relationship validation would work correctly, if the root type is a higher level (in
                             * hierarchy terms), inclusion of its type in the filter list will admit all other entities
                             * of that type or any of its subtypes. A finer-grain graph can be achieved by not
                             * including the root type and instead not validating the types of entities already visited
                             * and included in the graph.
                             */

                            for (String typeGUID1 : entityTypeGUIDs) {
                                if (repositoryValidator.verifyInstanceType(repositoryName, typeGUID1, entity1)) {
                                    /*
                                     * Valid type
                                     */

                                    if (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity1)) {
                                        validEntity1 = true;
                                    }
                                }
                            }
                        }

                        if (graphEntities.contains(relationshipEnd2Guid)) {
                            validEntity2 = true;
                        }
                        else {
                            /*
                             * If the entity is already included there is no need to test the type of this end entity.
                             * By omitting the test there is no need to include the root entity type GUID in the
                             * entityTypeGUIDs filtering list. This is beneficial because, although it could be included
                             * and the relationship validation would work correctly, if the root type is a higher level (in
                             * hierarchy terms) inclusion of its type in the filter list will admit all other entities
                             * of that type or any of its subtypes. A finer-grain graph can be achieved by not
                             * including the root type and instead not validating the types of entities already visited
                             * and included in the graph.
                             */

                            for (String typeGUID2 : entityTypeGUIDs) {
                                if (repositoryValidator.verifyInstanceType(repositoryName, typeGUID2, entity2)) {
                                    /*
                                     * Valid type
                                     */

                                    if (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity2)) {
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
     * Check whether the supplied entities have one or more of the required classifications.
     * The root entity is always always included, irrespective of whether it matches the classifications.
     * @param limitingClassifications classification names that limit the entity
     * @param entity entity to check for inclusion against the classification list.
     * @return whether to include this entity
     */
    private boolean includeEntityIfClassifiedAppropriately(List<String> limitingClassifications, EntityDetail entity)
        {
       boolean  includeEntity = true;

       if (!entity.getGUID().equals(rootEntityGUID))
       {
           // returns true if entity is classified appropriately
           includeEntity =repositoryValidator.verifyEntityIsClassified(limitingClassifications, entity);
       }
       return includeEntity;
    }

    /**
     * Validate the relationship proxy types against the entity types that are scoping the graph
     * @param relationship to validate
     * @return flag indicating whether the relationships proxy types are includes
     * @throws TypeErrorException Type error.
     */
    private boolean validateRelationshipAgainstEntityTypes(Relationship relationship) throws TypeErrorException
    {
        String methodName  ="validateRelationshipAgainstEntityTypes";
        boolean valid = false;
        /*
         * If we have entityType Guids specified then they will scope this relationship. So we need to check that the relationship
         * entity types of the ends are in scope
         *
         * This check is replaced for an entity that has already been added to the graph. This is slightly more efficient, but
         * more importantly it means the root entity is always included and entity type filters are only evaluated against the
         * other (non-root) neighbour entities.
         */

        boolean found1 = false;
        boolean found2 = false;

        if (entityTypeGUIDs !=null && !entityTypeGUIDs.isEmpty()) {

            String end1GUID = relationship.getEntityOneProxy().getGUID();
            String end2GUID = relationship.getEntityTwoProxy().getGUID();

            String actualTypeName1 = relationship.getEntityOneProxy().getType().getTypeDefName();
            String actualTypeName2 = relationship.getEntityTwoProxy().getType().getTypeDefName();
            /*
             * Need to go through each guid in entityTypeGUIDs and check whether the entity types of each proxy are a subtype of them or not
             */
            for (String entityTypeGUID:entityTypeGUIDs)
            {
                TypeDef entityTypeDef = repositoryHelper.getTypeDef(repositoryName,
                        "guid",
                        entityTypeGUID,
                        methodName);
                if (graphEntities.contains(end1GUID) || repositoryHelper.isTypeOf(methodName, actualTypeName1, entityTypeDef.getName()))
                {
                    found1 = true;
                }
                if (graphEntities.contains(end2GUID) || repositoryHelper.isTypeOf(methodName, actualTypeName2, entityTypeDef.getName()))
                {
                    found2 = true;
                }
            }

            if (found1 && found2)
            {
                valid = true;
            }
        } else {
            /*
             * No restrictions on relationship
             */
            valid = true;
        }
        return valid;
    }

    /**
     * Create the instance graph
     *
     * @return InstanceGraph  the instance graph that contains the entities and relationships that radiate out from the supplied entity GUID.
     * @throws TypeErrorException Type error.
     */
    InstanceGraph createInstanceGraph() throws TypeErrorException
    {
        Set<String> entities = new HashSet<>();
        Set<String> visitedEntities = new HashSet<>();
        Set<String> visitedRelationships = new HashSet<>();
        entities.add(rootEntityGUID);
        this.createGraph(entities, visitedEntities, visitedRelationships, 0);
        List<EntityDetail> entityList = new ArrayList<>();
        /*
         * add the root entity so the returned graph is consistent.
         */
        List<Relationship> relationshipList = new ArrayList<>();
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
     * @throws TypeErrorException Type error.
     */
    private void createGraph(Set<String> entities, Set<String> visitedEntities, Set<String> visitedRelationships, int currentLevel) throws TypeErrorException
    {

        Set<String> nextEntitySet = new HashSet<>();
        for (String entityGuid : entities)
        {
            if (currentLevel == 0) {
                graphEntities.add(entityGuid);
            }
            if (currentLevel < this.level) {
                Set<String> relationships = this.entityToRelationships.get(entityGuid);
                if (relationships != null) {
                    for (String relationshipGuid : relationships) {
                        Relationship relationship = this.relationshipStore.get(relationshipGuid);
                        /*
                         * Check to see if we have already visited this relationship
                         */
                        if (!visitedRelationships.contains(relationshipGuid)) {
                            if (verifyRelationshipForEntityNeighbourhood(relationship)) {
                                /*
                                 * valid relationship and entities
                                 */
                                graphEntities.add(entityGuid);
                                final String end1Guid = getEnd1EntityGUID(relationship);
                                final String end2Guid = getEnd2EntityGUID(relationship);
                                graphRelationships.add(relationshipGuid);
                                /*
                                 * add the entities - one end will already be there so will be replaced.
                                 */
                                graphEntities.add(end1Guid);
                                graphEntities.add(end2Guid);
                                /*
                                 * if we have not seen the other end then we need to traverse to it.
                                 */
                                if (end1Guid.equals(entityGuid) && !visitedEntities.contains(end2Guid)) {
                                    nextEntitySet.add(end2Guid);
                                }
                                if (end2Guid.equals(entityGuid) && !visitedEntities.contains(end1Guid)) {
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
        }
        int nextLevel = currentLevel + 1;
        if (nextLevel <= this.level && nextEntitySet.size() > 0)
        {
            /*
             * recurse
             */
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