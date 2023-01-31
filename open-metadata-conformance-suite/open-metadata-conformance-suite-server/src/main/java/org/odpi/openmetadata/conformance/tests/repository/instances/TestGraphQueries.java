/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.text.MessageFormat;
import java.util.*;


/**
 * Test that the repository under test can create graphs from the supported entity and relationship types
 * and that the graph queries are correctly supported.
 */

public class TestGraphQueries extends RepositoryConformanceTestCase {

    private static final String testCaseId = "repository-graph-queries";
    private static final String testCaseName = "Repository graph query test case";

    private static final String assertion0 = testCaseId + "-00";
    private static final String assertionMsg0 = " relationship type matches the known type.";

    private static final String assertion1 = testCaseId + "-01";
    private static final String assertionMsg1 = " graph query returned a result.";

    private static final String assertion2 = testCaseId + "-02";
    private static final String assertionMsg2 = " graph query returned the expected number of entities.";

    private static final String assertion3 = testCaseId + "-03";
    private static final String assertionMsg3 = " graph query returned all the expected entities.";

    private static final String assertion4 = testCaseId + "-04";
    private static final String assertionMsg4 = " graph query returned the expected number of relationships.";

    private static final String assertion5 = testCaseId + "-05";
    private static final String assertionMsg5 = " graph query returned all the expected relationships.";

    private static final String assertion6 = testCaseId + "-06";
    private static final String assertionMsg6 = " graph query returned the expected number of related entities.";

    private static final String assertion7 = testCaseId + "-07";
    private static final String assertionMsg7 = " graph query returned all the expected related entities.";

    private static final String assertion8 = testCaseId + "-08";
    private static final String assertionMsg8 = " getLinkedEntities found {0}/{1} expected entities using parameters: {2}";

    private static final String assertion9 = testCaseId + "-09";
    private static final String assertionMsg9 = " graph query returned all the expected entities.";

    private static final String assertion10 = testCaseId + "-10";
    private static final String assertionMsg10 = " graph query returned the expected number of relationships.";

    private static final String assertion11 = testCaseId + "-11";
    private static final String assertionMsg11 = " graph query returned all the expected relationships.";

    private static final String assertion12 = testCaseId + "-12";
    private static final String assertionMsg12 = " relationship end types are supported by repository ";

    private static final String assertion13 = testCaseId + "-13";
    private static final String assertionMsg13 = " repository supports the creation of instances ";

    private static final String assertion14 = testCaseId + "-14";
    private static final String assertionMsg14 = " repository supports getEntityNeighborhood ";

    private static final String assertion15 = testCaseId + "-15";
    private static final String assertionMsg15 = " repository supports getRelatedEntities ";

    private static final String assertion16 = testCaseId + "-16";
    private static final String assertionMsg16 = " repository supports getLinkingEntities ";


    private final String                      testTypeName;
    private final RepositoryConformanceWorkPad workPad;
    private OMRSMetadataCollection      metadataCollection;
    private Map<String,EntityDef>       entityDefs       = null;
    private Map<String,RelationshipDef> relationshipDefs = null;
    private Set<String>                 relationshipTypeNames = null;
    private Set<String>                 entityTypeNames = null;

    private int edgeCount = 0;
    private int nodeCount = 0;
    private final int maxDepth = 3;
    private final int maxFanout = 3;

    /*
     * The edgeToNodeMap is a map of relationshipGUID to a pair of entityGUIDs, ordered as end1 then end2
     */
    private final Map<String, List<String>> edgeToNodesMap = new HashMap<>();

    /*
     * The nodeToEdgesMap is a map of entityGUID to a pair of lists of relationshipGUIDs, ordered as end1 then end2
     */
    private final Map<String, List<List<String>>> nodeToEdgesMap = new HashMap<>();

    /*
     * Connectivity Map for convenient path finding traversal
     */
    private Map<String,Map<String,String>> connMap = new HashMap<>();




    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDefs types of relationships
     * @param entityDefs types of entities
     */
    public TestGraphQueries(RepositoryConformanceWorkPad workPad, List<RelationshipDef> relationshipDefs, Map<String,EntityDef> entityDefs)
    {

        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
        this.workPad = workPad;
        this.entityDefs       = entityDefs;
        this.relationshipDefs = new HashMap<>();
        for (RelationshipDef relDef : relationshipDefs)
        {
            this.relationshipDefs.put(relDef.getName(),relDef);
        }

        this.entityTypeNames       = workPad.getEntityTypeNames();
        this.relationshipTypeNames = workPad.getRelationshipTypeNames();

        this.testTypeName = this.updateTestIdByType(null, testCaseId, testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        this.metadataCollection = super.getMetadataCollection();

        /*
         * Construct a graph from types that the repository supports.
         */


        try
        {
            this.constructGraph();
        }
        catch (FunctionNotSupportedException exception)
        {

            /*
             * If running against a read-only repository/connector that cannot add
             * entities or relationships catch FunctionNotSupportedException and give up the test.
             *
             * In this case indicate that instance creation is not supported....
             */
            super.addNotSupportedAssertion(assertion13,
                                           assertionMsg13,
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
                                           RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

            return;
        }


        /*
         * Perform graph queries.
         */
        this.getEntityNeighborhood();
        this.getRelatedEntities();
        this.getLinkingEntities();

        /*
         * Destroy graph instances
         */
        this.destroyGraph();

        super.setSuccessMessage("Graph queries can be performed");
    }


    private void traverse(String curE, String tgtE, List<String> curP, List<List<String>> discoveredPaths ) {

        Map<String,String> routes = connMap.get(curE);
        Set<String> routeKeys = routes.keySet();
        for (String routeKey : routeKeys)
        {
            if (curP == null || !curP.contains(routeKey))
            {
                String remoteEntity = routes.get(routeKey);
                List<String> curPex = new ArrayList<>();
                if (curP != null)
                {
                    curPex.addAll(curP);
                }
                curPex.add(routeKey);
                if (remoteEntity.equals(tgtE))
                {
                    /* Arrived!  - record path and pop */
                    discoveredPaths.add(curPex);
                    return;
                }
                else
                {
                    traverse(remoteEntity, tgtE, curPex, discoveredPaths);
                }
            }
        }
    }

    /*
     * Explore the graph from the specified entity outwards (i.e. not returning on the arrivalEdge whose GUID
     * is specified). If remainingDepth is positive it is used as a limit to traversal depth. If it is negative
     * there is no limit enforced.
     */
    private List<List<String>> exploreFromEntity(String entityGUID, String arrivalEdgeGUID, int remainingDepth)
    {

        List<String> discoveredEntityGUIDs = new ArrayList<>();
        List<String> discoveredRelationshipGUIDs = new ArrayList<>();

        if (remainingDepth != 0)
        {

            List<List<String>> inOutEdges = nodeToEdgesMap.get(entityGUID);
            List<String> bothEdges = new ArrayList<>();
            bothEdges.addAll(inOutEdges.get(0));
            bothEdges.addAll(inOutEdges.get(1));
            if (!(bothEdges.isEmpty()))
            {
                for (String edgeGUID : bothEdges)
                {
                    if (!edgeGUID.equals(arrivalEdgeGUID))
                    {
                        /* For each untraversed edge from this entity, explore */
                        List<String> end1end2 = this.edgeToNodesMap.get(edgeGUID);
                        for (String entGUID : end1end2)
                        {
                            if (!(entGUID.equals(entityGUID)))
                            {
                                /* Explore from the other entity */
                                List<List<String>> newGUIDs = this.exploreFromEntity(entGUID, edgeGUID, remainingDepth>0?(remainingDepth - 1):remainingDepth);
                                discoveredEntityGUIDs.addAll(newGUIDs.get(0));
                                discoveredRelationshipGUIDs.addAll(newGUIDs.get(1));
                            }
                        }
                        /* Add the traversed edgeGUID */
                        discoveredRelationshipGUIDs.add(edgeGUID);
                    }
                }
            }
        }
        discoveredEntityGUIDs.add(entityGUID);

        List<List<String>> discoveredGUIDs = new ArrayList<>();
        discoveredGUIDs.add(discoveredEntityGUIDs);
        discoveredGUIDs.add(discoveredRelationshipGUIDs);
        return discoveredGUIDs;
    }



    private void constructGraph() throws Exception
    {

        /*
         * Clear the usage maps and counts
         */
        edgeCount = 0;
        nodeCount = 0;


        /*
         * Iterate over the supported relationship types and use them and the entity types at their ends.
         * The graph is built by placing an initial dumb-bell and then, from one end, recursing to max_depth and at each level iterating to max-fanout.
         * It is OK to reuse a type multiple times, but we try to avoid it by working modulo through the available types.
         */

        /*
         * Add the first edge to the graph
         */

        Iterator<String> relationshipTypeNameIterator = relationshipTypeNames.iterator();

        if (relationshipTypeNameIterator.hasNext())
        {

            String relationshipTypeName = relationshipTypeNameIterator.next();

            /*
             * Construct a full edge (i.e. it has both ends)
             */

            List<String> newGUIDs = this.addFullEdgeToGraph(relationshipTypeName);

            String relationshipGUID = newGUIDs.get(0);
            String end1GUID = newGUIDs.get(1);
            String end2GUID = newGUIDs.get(2);
            List<String> endGUIDs = new ArrayList<>();
            endGUIDs.add(end1GUID);
            endGUIDs.add(end2GUID);
            this.edgeToNodesMap.put(relationshipGUID, endGUIDs);

            /*
             * Bump graph counters
             */
            this.edgeCount++;
            this.nodeCount = this.nodeCount + 2;

            /*
             * Update end1's outbound and inbound edge lists
             */
            List<List<String>> end1pairedEdgeLists = this.nodeToEdgesMap.get(end1GUID);
            if (end1pairedEdgeLists == null)
            {
                /*
                 * First time for this node, so create the pair of lists
                 */
                List<String> end1RelsList = new ArrayList<>();
                List<String> end2RelsList = new ArrayList<>();
                end1pairedEdgeLists = new ArrayList<>();
                end1pairedEdgeLists.add(end1RelsList);
                end1pairedEdgeLists.add(end2RelsList);
                this.nodeToEdgesMap.put(end1GUID, end1pairedEdgeLists);
            }
            List<String> end1Rels = end1pairedEdgeLists.get(0);
            end1Rels.add(relationshipGUID);

            /*
             * Update end2's outbound and inbound edge lists
             */
            List<List<String>> end2pairedEdgeLists = this.nodeToEdgesMap.get(end2GUID);
            if (end2pairedEdgeLists == null) {
                /*
                 * First time for this node, so create the pair of lists
                 */
                List<String> end1RelsList = new ArrayList<>();
                List<String> end2RelsList = new ArrayList<>();
                end2pairedEdgeLists = new ArrayList<>();
                end2pairedEdgeLists.add(end1RelsList);
                end2pairedEdgeLists.add(end2RelsList);
                this.nodeToEdgesMap.put(end2GUID, end2pairedEdgeLists);
            }
            List<String> end2Rels = end2pairedEdgeLists.get(1);
            end2Rels.add(relationshipGUID);

            /*
             * For end2 (arbitrary choice) of the above edge extend the graph with 'fanout' edges
             *
             * Find the type of end2 and look up what types of relationship it supports.
             * Iterate up to fanout times.
             * Each iteration, pick a relationship type (preferably one that is not already used but if none are vacant reuse).
             * Create a relationship and far end (only) and hook the relationship onto the current (end2) entity.
             */

            if (this.maxDepth > 1)
            {
                this.extendGraph(workPad.getLocalServerUserId(), end2GUID, 1);
            }
        }
    }

    private void destroyGraph() throws Exception
    {

        /*
         * Clean up the instances created for the graph query tests.
         * This is easiest if we use the keys from the nodeToEdgesMap to delete the nodes (entities). This will clean up the relationships.
         */

        Set<String> nodeKeys = nodeToEdgesMap.keySet();
        for (String nodeKey : nodeKeys)
        {

            /*
             * Need to get the entity so that we can note its type, needed for delete.
             */

            try
            {
                EntityDetail entityToDelete = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), nodeKey);

                try
                {
                    metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                                                    entityToDelete.getType().getTypeDefGUID(),
                                                    entityToDelete.getType().getTypeDefName(),
                                                    nodeKey);

                }
                catch (FunctionNotSupportedException exc)
                {
                    /* NO OP - continue with purge */
                }

                metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                               entityToDelete.getType().getTypeDefGUID(),
                                               entityToDelete.getType().getTypeDefName(),
                                               nodeKey);
            }
            catch (Exception exception)
            {
                /*
                 * Rethrow the exception - this will cause a failure of the testcase, which is desirable.
                 */
                throw exception;
            }
        }


    }

    private void extendGraph(String userId, String entityGUID, int currentDepth) throws Exception
    {

        /*
         * Retrieve the entity, its typeName and hence the types of relationship we can use...
         */
        EntityDetail connectToEntity = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(), entityGUID);
        String connectToEntityTypeName = connectToEntity.getType().getTypeDefName();
        List<List<String>> possibleRelTypeNames = workPad.getEntityRelationshipTypes(connectToEntityTypeName);

        int fanout = 0;
        int endChoice = 1;
        while (fanout < maxFanout)
        {
            /*
             * Find an available relationship type if possible, if not reuse types.
             * Alternate between using this entity as end1 or end2 of the relationship to be created.
             */

            if (endChoice == 1)
            {
                /* Use as end1 */
                List<String> end1PossibleTypeNames = possibleRelTypeNames.get(0);
                int end1Index = fanout % 2;
                int relTypeIndex = end1Index % end1PossibleTypeNames.size();
                String relTypeName = end1PossibleTypeNames.get(relTypeIndex);

                /*
                 * Add a relationship of the given type
                 */

                List<String> extGUIDs = addPartialEdgeToGraph(relTypeName, connectToEntity.getGUID(), 1);

                String relationshipGUID = extGUIDs.get(0);
                String end1GUID = extGUIDs.get(1);
                String end2GUID = extGUIDs.get(2);
                List<String> endGUIDs = new ArrayList<>();
                endGUIDs.add(end1GUID);
                endGUIDs.add(end2GUID);

                this.edgeToNodesMap.put(relationshipGUID, endGUIDs);

                /*
                 * Update end1's outbound and inbound edge lists
                 */
                List<List<String>> end1pairedEdgeLists = this.nodeToEdgesMap.get(end1GUID);
                if (end1pairedEdgeLists == null)
                {
                    /*
                     * First time for this node, so create the pair of lists
                     */
                    List<String> end1RelsList = new ArrayList<>();
                    List<String> end2RelsList = new ArrayList<>();
                    end1pairedEdgeLists = new ArrayList<>();
                    end1pairedEdgeLists.add(end1RelsList);
                    end1pairedEdgeLists.add(end2RelsList);
                    this.nodeToEdgesMap.put(end1GUID, end1pairedEdgeLists);
                }
                List<String> end1Rels = end1pairedEdgeLists.get(0);
                end1Rels.add(relationshipGUID);

                /*
                 * Update end2's outbound and inbound edge lists
                 */
                List<List<String>> end2pairedEdgeLists = this.nodeToEdgesMap.get(end2GUID);
                if (end2pairedEdgeLists == null)
                {
                    /*
                     * First time for this node, so create the pair of lists
                     */
                    List<String> end1RelsList = new ArrayList<>();
                    List<String> end2RelsList = new ArrayList<>();
                    end2pairedEdgeLists = new ArrayList<>();
                    end2pairedEdgeLists.add(end1RelsList);
                    end2pairedEdgeLists.add(end2RelsList);
                    this.nodeToEdgesMap.put(end2GUID, end2pairedEdgeLists);
                }
                List<String> end2Rels = end2pairedEdgeLists.get(1);
                end2Rels.add(relationshipGUID);

                this.edgeCount++;
                this.nodeCount++;


                currentDepth++;

                if (this.maxDepth > currentDepth)
                {
                    this.extendGraph(userId, end2GUID, currentDepth);
                }
            }
            else
            {
                /* Use as end2 */

                List<String> end2PossibleTypeNames = possibleRelTypeNames.get(1);
                int end2Index = (int) (fanout / 2.0);
                int relTypeIndex = end2Index % end2PossibleTypeNames.size();
                String relTypeName = end2PossibleTypeNames.get(relTypeIndex);

                /*
                 * Add a relationship of the given type
                 */
                List<String> extGUIDs = addPartialEdgeToGraph(relTypeName, connectToEntity.getGUID(), 2);

                String relationshipGUID = extGUIDs.get(0);
                String end1GUID = extGUIDs.get(1);
                String end2GUID = extGUIDs.get(2);
                List<String> endGUIDs = new ArrayList<>();
                endGUIDs.add(end1GUID);
                endGUIDs.add(end2GUID);

                this.edgeToNodesMap.put(relationshipGUID, endGUIDs);

                /*
                 * Update end1's outbound and inbound edge lists
                 */
                List<List<String>> end1pairedEdgeLists = this.nodeToEdgesMap.get(end1GUID);
                if (end1pairedEdgeLists == null)
                {
                    /*
                     * First time for this node, so create the pair of lists
                     */
                    List<String> end1RelsList = new ArrayList<>();
                    List<String> end2RelsList = new ArrayList<>();
                    end1pairedEdgeLists = new ArrayList<>();
                    end1pairedEdgeLists.add(end1RelsList);
                    end1pairedEdgeLists.add(end2RelsList);
                    this.nodeToEdgesMap.put(end1GUID, end1pairedEdgeLists);
                }
                List<String> end1Rels = end1pairedEdgeLists.get(0);
                end1Rels.add(relationshipGUID);

                /*
                 * Update end2's outbound and inbound edge lists
                 */
                List<List<String>> end2pairedEdgeLists = this.nodeToEdgesMap.get(end2GUID);
                if (end2pairedEdgeLists == null)
                {
                    /*
                     * First time for this node, so create the pair of lists
                     */
                    List<String> end1RelsList = new ArrayList<>();
                    List<String> end2RelsList = new ArrayList<>();
                    end2pairedEdgeLists = new ArrayList<>();
                    end2pairedEdgeLists.add(end1RelsList);
                    end2pairedEdgeLists.add(end2RelsList);
                    this.nodeToEdgesMap.put(end2GUID, end2pairedEdgeLists);
                }
                List<String> end2Rels = end2pairedEdgeLists.get(1);
                end2Rels.add(relationshipGUID);

                this.edgeCount++;
                this.nodeCount++;

                currentDepth++;

                if (this.maxDepth > currentDepth)
                {
                    this.extendGraph(userId, end1GUID, currentDepth);
                }
            }

            /*
             * Bump the fanout counter and flip the end choice
             */

            fanout++;
            endChoice = (endChoice == 1) ? 2 : 1;

        }
    }


    /*
     * Add an edge to the graph and return (in strict order) the GUIDs of the relationship, end1 and end2.
     */
    private List<String> addFullEdgeToGraph(String relationshipTypeName) throws Exception
    {


        /* Use the relationship type name to get the known relationship type from the repository helper.
         * Then get the defined end types and work down the supertype hierarchy to collate the list of possible
         * end types, and finally filter this list (for each end) by the types supported by the repostiory.
         */

        /*
         * Check that the relationship type matches the known type from the repository helper
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null)
        {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        RelationshipDef knownRelationshipDef = null;
        if (repositoryHelper != null)
        {
            knownRelationshipDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipTypeName);
        }
        RelationshipDef repositoryRelationshipDef = relationshipDefs.get(relationshipTypeName);
        verifyCondition((repositoryRelationshipDef.equals(knownRelationshipDef)),
                        assertion0,
                        testTypeName + assertionMsg0,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        /*
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all the entity inheritance hierarchy - it may only
         * support a subset of entity types. So although the relationship type may have end definitions
         * each specifying a given entity type - the repository may only support certain sub-types of the
         * specified type. This is OK, and the testcase needs to only try to use entity types that are
         * supported by the repository being tested. To do this it needs to start with the specified
         * end type, e.g. Referenceable, and walk down the hierarchy looking for each subtype that
         * is supported by the repository (i.e. is in the entityDefs map). The test is run for
         * each combination of end1Type and end2Type - but only for types that are within the
         * active set for this repository.
         */

        String end1DefName = null;
        if (knownRelationshipDef != null) {
            end1DefName = knownRelationshipDef.getEndDef1().getEntityType().getName();
        }
        List<String> end1DefTypeNames = new ArrayList<>();
        end1DefTypeNames.add(end1DefName);
        if (this.workPad.getEntitySubTypes(end1DefName) != null)
        {
            end1DefTypeNames.addAll(this.workPad.getEntitySubTypes(end1DefName));
        }


        String end2DefName = knownRelationshipDef.getEndDef2().getEntityType().getName();
        List<String> end2DefTypeNames = new ArrayList<>();
        end2DefTypeNames.add(end2DefName);
        if (this.workPad.getEntitySubTypes(end2DefName) != null)
        {
            end2DefTypeNames.addAll(this.workPad.getEntitySubTypes(end2DefName));
        }

        /*
         * Filter the possible types to only include types that are supported by the repository
         */

        List<String> end1SupportedTypeNames = new ArrayList<>();
        for (String end1TypeName : end1DefTypeNames)
        {
            if (entityDefs.get(end1TypeName) != null)
                end1SupportedTypeNames.add(end1TypeName);
        }

        List<String> end2SupportedTypeNames = new ArrayList<>();
        for (String end2TypeName : end2DefTypeNames)
        {
            if (entityDefs.get(end2TypeName) != null)
                end2SupportedTypeNames.add(end2TypeName);
        }

        /*
         * Check that neither list is empty
         */
        if (end1SupportedTypeNames.isEmpty() || end2SupportedTypeNames.isEmpty())
        {
            /*
             * There are no supported types for at least one of the ends - the repository cannot test this relationship type.
             */
            assertCondition((false),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }



        /*
         * The test does not iterate over all possible end types - but it must select an end type that is supported by the repository,
         * so it uses the first type in the supported list for each end.
         */

        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);
        EntityDef end1Type = entityDefs.get(end1TypeName);
        EntityDetail end1 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
        EntityDef end2Type = entityDefs.get(end2TypeName);
        EntityDetail end2 = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);

        Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                          knownRelationshipDef.getGUID(),
                                                                          super.getPropertiesForInstance(knownRelationshipDef.getPropertiesDefinition()),
                                                                          end1.getGUID(),
                                                                          end2.getGUID(),
                                                                          null);

        List<String> guids = new ArrayList<>();
        guids.add(newRelationship.getGUID());
        guids.add(end1.getGUID());
        guids.add(end2.getGUID());
        return guids;
    }


    /*
     * Add an edge to the graph and return (in strict order) the GUIDs of the relationship, end1 and end2.
     */
    private List<String> addPartialEdgeToGraph(String relationshipTypeName, String existingEntityGUID, int existingEntityEnd) throws Exception
    {

        /* Use the relationship type name to get the known relationship type from the repository helper.
         * Then get the defined end types and work down the supertype hierarchy to collate the list of possible
         * end types, and finally filter this list (for each end) by the types supported by the repostiory.
         */

        /*
         * Check that the relationship type matches the known type from the repository helper
         */
        OMRSRepositoryConnector cohortRepositoryConnector = null;
        OMRSRepositoryHelper repositoryHelper = null;
        if (workPad != null)
        {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }

        RelationshipDef knownRelationshipDef = null;
        if (repositoryHelper != null)
        {
            knownRelationshipDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipTypeName);
        }
        RelationshipDef repositoryRelationshipDef = relationshipDefs.get(relationshipTypeName);
        verifyCondition((repositoryRelationshipDef.equals(knownRelationshipDef)),
                        assertion0,
                        testTypeName + assertionMsg0,
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getProfileId(),
                        RepositoryConformanceProfileRequirement.CONSISTENT_TYPES.getRequirementId());

        RelationshipDef relationshipDef = knownRelationshipDef;

        /*
         * In this testcase the repository is believed to support the relationship type defined by
         * relationshipDef - but may not support all the entity inheritance hierarchy - it may only
         * support a subset of entity types. So although the relationship type may have end definitions
         * each specifying a given entity type - the repository may only support certain sub-types of the
         * specified type. This is OK, and the testcase needs to only try to use entity types that are
         * supported by the repository being tested. To do this it needs to start with the specified
         * end type, e.g. Referenceable, and walk down the hierarchy looking for each subtype that
         * is supported by the repository (i.e. is in the entityDefs map). The test is run for
         * each combination of end1Type and end2Type - but only for types that are within the
         * active set for this repository.
         */

        String end1DefName = relationshipDef.getEndDef1().getEntityType().getName();
        List<String> end1DefTypeNames = new ArrayList<>();
        end1DefTypeNames.add(end1DefName);
        if (this.workPad.getEntitySubTypes(end1DefName) != null)
        {
            end1DefTypeNames.addAll(this.workPad.getEntitySubTypes(end1DefName));
        }


        String end2DefName = relationshipDef.getEndDef2().getEntityType().getName();
        List<String> end2DefTypeNames = new ArrayList<>();
        end2DefTypeNames.add(end2DefName);
        if (this.workPad.getEntitySubTypes(end2DefName) != null)
        {
            end2DefTypeNames.addAll(this.workPad.getEntitySubTypes(end2DefName));
        }
        /*
         * Filter the possible types to only include types that are supported by the repository
         */

        List<String> end1SupportedTypeNames = new ArrayList<>();
        for (String end1TypeName : end1DefTypeNames)
        {
            if (entityDefs.get(end1TypeName) != null)
            {
                end1SupportedTypeNames.add(end1TypeName);
            }
        }

        List<String> end2SupportedTypeNames = new ArrayList<>();
        for (String end2TypeName : end2DefTypeNames)
        {
            if (entityDefs.get(end2TypeName) != null)
            {
                end2SupportedTypeNames.add(end2TypeName);
            }
        }

        /*
         * Check that neither list is empty
         */
        if (end1SupportedTypeNames.isEmpty() || end2SupportedTypeNames.isEmpty()) {

            /*
             * There are no supported types for at least one of the ends - the repository cannot test this relationship type.
             */
            assertCondition((false),
                            assertion12,
                            testTypeName + assertionMsg12,
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getProfileId(),
                            RepositoryConformanceProfileRequirement.RELATIONSHIP_LIFECYCLE.getRequirementId());
        }

        /*
         * The test does not iterate over all possible end types - but it must select an end type that is supported by the repository,
         * so it uses the first type in the supported list for each end.
         */

        String end1TypeName = end1SupportedTypeNames.get(0);
        String end2TypeName = end2SupportedTypeNames.get(0);

        String end1GUID;
        String end2GUID;

        if (existingEntityEnd == 1)
        {
            /* Add an entity to end2 */
            EntityDef     end2Type = entityDefs.get(end2TypeName);
            EntityDetail  end2     = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end2Type);
            end1GUID = existingEntityGUID;
            end2GUID = end2.getGUID();
        }
        else
        {
            /* existing entity is end 2 */
            /* Add an entity to end1 */
            EntityDef     end1Type = entityDefs.get(end1TypeName);
            EntityDetail  end1     = this.addEntityToRepository(workPad.getLocalServerUserId(), metadataCollection, end1Type);
            end1GUID = end1.getGUID();
            end2GUID = existingEntityGUID;
        }


        Relationship newRelationship = metadataCollection.addRelationship(workPad.getLocalServerUserId(),
                                                                          relationshipDef.getGUID(),
                                                                          super.getPropertiesForInstance(relationshipDef.getPropertiesDefinition()),
                                                                          end1GUID,
                                                                          end2GUID,
                                                                          null);

        List<String> guids= new ArrayList<>();
        guids.add(newRelationship.getGUID());
        guids.add(end1GUID);
        guids.add(end2GUID);

        return guids;
    }

    private void getEntityNeighborhood() throws Exception
    {
        /*
         * getEntityNeighborhood()
         *
         * For each entity in the graph, request the neighborhood to different levels
         * There is no type filtering on these queries.
         */
        Set<String> entityGUIDs = this.nodeToEdgesMap.keySet();
        for (String entityGUID : entityGUIDs)
        {

            for (int level = 0; level < 4; level++)
            {
                InstanceGraph instGraph = null;

                long start;
                long elapsedTime;
                try
                {

                    start = System.currentTimeMillis();
                    instGraph = metadataCollection.getEntityNeighborhood(workPad.getLocalServerUserId(),
                                                                         entityGUID,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         level);
                    elapsedTime = System.currentTimeMillis() - start;
                }
                catch (FunctionNotSupportedException exception)
                {
                    super.addNotSupportedAssertion(assertion14,
                                                   assertionMsg14,
                                                   RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
                    return;
                }
                catch (Exception exc)
                {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "getEntityNeighborhood";
                    String operationDescription = "get the neighborhood of an entity with GUID " + entityGUID;
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityTypeGUIDs", "null");
                    parameters.put("relationshipTypeGUIDs", "null");
                    parameters.put("limitResultsByStatus", "null");
                    parameters.put("limitResultsByClassification", "null");
                    parameters.put("asOfTime", "null");
                    parameters.put("level", Integer.toString(level));
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);

                }


                assertCondition((true),
                                assertion14,
                                testTypeName + assertionMsg14,
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId(),
                                "getEntityNeighborhood",
                                elapsedTime);


                /*
                 * Formulate the expected result
                 */


                /* Add the current entity and then navigate each of its edges recursively amalgamating what you discover down that edge */

                List<List<String>> subgraph = this.exploreFromEntity(entityGUID, null, level);
                List<String> expectedEntityGUIDs = new ArrayList<>(subgraph.get(0));
                List<String> expectedRelationshipGUIDs = new ArrayList<>(subgraph.get(1));


                assertCondition((instGraph != null),
                                assertion1,
                                testTypeName + assertionMsg1,
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());


                /* Check entities */

                /* Always expect to get at least one entity - the root of the query */
                if (instGraph != null)
                {
                    assertCondition((instGraph.getEntities() != null && !(instGraph.getEntities().isEmpty()) && instGraph.getEntities().size() == expectedEntityGUIDs.size()),
                                    assertion2,
                                    testTypeName + assertionMsg2,
                                    RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                    RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
                }

                List<String> returnedEntityGUIDs = new ArrayList<>();
                if (instGraph != null && instGraph.getEntities() != null)
                {
                    for (EntityDetail entity : instGraph.getEntities())
                    {
                        if (entity != null)
                        {
                            returnedEntityGUIDs.add(entity.getGUID());
                        }
                    }
                }

                assertCondition((returnedEntityGUIDs.containsAll(expectedEntityGUIDs)),
                                assertion3,
                                testTypeName + assertionMsg3,
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());


                /* Check relationships */

                /* Don't always expect to get at least one relationship */
                if (expectedRelationshipGUIDs.isEmpty())
                {

                    if (instGraph != null)
                    {
                        assertCondition((instGraph.getRelationships() == null),
                                        assertion4,
                                        testTypeName + assertionMsg4,
                                        RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
                    }
                }
                else
                {
                    if (instGraph != null) {
                        assertCondition((instGraph.getRelationships().size() == expectedRelationshipGUIDs.size()),
                                        assertion4,
                                        testTypeName + assertionMsg4,
                                        RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                        RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
                    }

                    List<String> returnedRelationshipGUIDs = new ArrayList<>();
                    if (instGraph.getRelationships() != null)
                    {
                        for (Relationship relationship : instGraph.getRelationships()) {
                            if (relationship != null)
                            {
                                returnedRelationshipGUIDs.add(relationship.getGUID());
                            }
                        }
                    }

                    assertCondition((returnedRelationshipGUIDs.containsAll(expectedRelationshipGUIDs)),
                                    assertion5,
                                    testTypeName + assertionMsg5,
                                    RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getProfileId(),
                                    RepositoryConformanceProfileRequirement.ENTITY_NEIGHBORHOOD.getRequirementId());
                }
            }
        }
    }


    private void getRelatedEntities() throws Exception
    {

        /*
         * getRelatedEntities()
         *
         * For each entity in the graph, request the connected entities.
         * With no type filtering this should always return the entire graph (since our test data is connected).
         * There is no type filtering on these queries.
         */

        Set<String> entityGUIDs = this.nodeToEdgesMap.keySet();

        for (String entityGUID : entityGUIDs)
        {
            List<EntityDetail> relatedEntities = null;

            long start;
            long elapsedTime;
            try
            {

                start = System.currentTimeMillis();
                relatedEntities = metadataCollection.getRelatedEntities(workPad.getLocalServerUserId(),
                                                                        entityGUID,
                                                                        null,
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0);
                elapsedTime = System.currentTimeMillis() - start;

            }
            catch (FunctionNotSupportedException exception)
            {
                super.addNotSupportedAssertion(assertion15,
                                               assertionMsg15,
                                               RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getProfileId(),
                                               RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getRequirementId());

                return;

            }
            catch (Exception exc)
            {
                /*
                 * We are not expecting any other exceptions from this method call. Log and fail the test.
                 */

                String methodName = "getRelatedEntities";
                String operationDescription = "get the entities related to an entity with GUID " + entityGUID;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("entityTypeGUIDs", "null");
                parameters.put("fromEntityElement", Integer.toString(0));
                parameters.put("limitResultsByStatus", "null");
                parameters.put("limitResultsByClassification", "null");
                parameters.put("asOfTime", "null");
                parameters.put("sequencingProperty", "null");
                parameters.put("sequencingOrder", "null");
                parameters.put("pageSize", Integer.toString(0));
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                throw new Exception(msg, exc);
            }


            assertCondition((true),
                            assertion15,
                            testTypeName + assertionMsg15,
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getRequirementId(),
                            "getRelatedEntities",
                            elapsedTime);


            /*
             * Formulate the expected result
             */


            /* Add the current entity and then navigate each of its edges recursively amalgamating what you discover down that edge */
            //List<String> expectedRelationshipGUIDs = new ArrayList<>();

            List<List<String>> subgraph = this.exploreFromEntity(entityGUID, null, -1);
            List<String> expectedEntityGUIDs = new ArrayList<>(subgraph.get(0));
            //expectedRelationshipGUIDs.addAll(subgraph.get(1));



            /* Check entities */

            /* Always expect to get at least one entity - the root of the query */
            assertCondition((relatedEntities != null && !(relatedEntities.isEmpty()) && relatedEntities.size() == expectedEntityGUIDs.size()),
                            assertion6,
                            testTypeName + assertionMsg6,
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getRequirementId());

            List<String> returnedEntityGUIDs = new ArrayList<>();
            if (relatedEntities != null) {
                for (EntityDetail entity : relatedEntities) {
                    if (entity != null) {
                        returnedEntityGUIDs.add(entity.getGUID());
                    }
                }
            }
            assertCondition((returnedEntityGUIDs.containsAll(expectedEntityGUIDs)),
                            assertion7,
                            testTypeName + assertionMsg7,
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.CONNECTED_ENTITIES.getRequirementId());


        }
    }

    private void getLinkingEntities() throws Exception
    {

        /*
         * getLinkingEntities()
         * For each entity find the paths to each other entity, if they are connected.
         */

        /*
         * Formulate the expected result from the test graph.
         */

        /*
         * Construct a connectivity map from each entity...
         */
        Set<String> entityGUIDs = this.nodeToEdgesMap.keySet();
        Iterator<String> entityGUIDIterator = entityGUIDs.iterator();
        /* Form Map from entityGUID to Map of edgeGUID to remote entity GUID */
        this.connMap = new HashMap<>();

        while (entityGUIDIterator.hasNext())
        {
            String entityGUID = entityGUIDIterator.next();
            List<List<String>> edgeGUIDs = this.nodeToEdgesMap.get(entityGUID);

            Map<String, String> routeMap = new HashMap<>();
            this.connMap.put(entityGUID, routeMap);

            /* process the end1 ends */
            if (edgeGUIDs.get(0) != null)
            {
                for (String edgeGUID : edgeGUIDs.get(0))
                {
                    String otherEntityGUID = this.edgeToNodesMap.get(edgeGUID).get(1);
                    routeMap.put(edgeGUID, otherEntityGUID);
                }
            }

            /* process the end2 ends */
            if (edgeGUIDs.get(1) != null)
            {
                for (String edgeGUID : edgeGUIDs.get(1))
                {
                    String otherEntityGUID = this.edgeToNodesMap.get(edgeGUID).get(0);
                    routeMap.put(edgeGUID, otherEntityGUID);
                }
            }
        }

        /*
         * Traverse the connMap to find the available paths, from A to B
         */

        for (String entityAGUID : entityGUIDs)
        {

            for (String entityBGUID : entityGUIDs)
            {

                /*
                 * Calculate the expected result
                 */

                List<String> expectedEntityGUIDs = new ArrayList<>();
                List<String> expectedRelationshipGUIDs = new ArrayList<>();

                if (entityBGUID.equals(entityAGUID))
                {
                    /*
                     * There will be no paths - but we expect to get the (one) entity back.
                     */
                    expectedEntityGUIDs.add(entityAGUID);
                }
                else
                {
                    /*
                     * For the pair of entities A & B find all paths through the test graph (if any exist)
                     */

                    List<List<String>> pathsAB = new ArrayList<>();

                    traverse(entityAGUID, entityBGUID, null, pathsAB);

                    if (!pathsAB.isEmpty())
                    {

                        /* There is at least one path */
                        for (List<String> thisPath : pathsAB)
                        {

                            for (String thisEdge : thisPath)
                            {

                                if (!expectedRelationshipGUIDs.contains(thisEdge))
                                {
                                    expectedRelationshipGUIDs.add(thisEdge);
                                    String entity1GUID = this.edgeToNodesMap.get(thisEdge).get(0);
                                    if (!expectedEntityGUIDs.contains(entity1GUID))
                                        expectedEntityGUIDs.add(entity1GUID);
                                    String entity2GUID = this.edgeToNodesMap.get(thisEdge).get(1);
                                    if (!expectedEntityGUIDs.contains(entity2GUID))
                                        expectedEntityGUIDs.add(entity2GUID);
                                }
                            }
                        }
                    }
                }


                InstanceGraph instanceGraph = null;

                long start;
                long elapsedTime;
                try
                {

                    start = System.currentTimeMillis();
                    instanceGraph = metadataCollection.getLinkingEntities(workPad.getLocalServerUserId(),
                                                                          entityAGUID,
                                                                          entityBGUID,
                                                                          null,
                                                                          null);
                    elapsedTime = System.currentTimeMillis() - start;
                }
                catch (FunctionNotSupportedException exception)
                {

                    super.addNotSupportedAssertion(assertion16,
                                                   assertionMsg16,
                                                   RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                                   RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId());

                    return;
                }
                catch (Exception exc)
                {
                    /*
                     * We are not expecting any other exceptions from this method call. Log and fail the test.
                     */

                    String methodName = "getLinkingEntities";
                    String operationDescription = "get the entities that connect an entity with GUID " + entityAGUID + " to an entity witrh GUID " + entityBGUID;
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("limitResultsByStatus", "null");
                    parameters.put("asOfTime", "null");
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());

                    throw new Exception(msg, exc);

                }


                assertCondition((true),
                                assertion16,
                                testTypeName + assertionMsg16,
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId(),
                                "getLinkingEntities",
                                elapsedTime);


                /* Check results */
                List<EntityDetail> returnedEntities = instanceGraph.getEntities();
                List<Relationship> returnedRelationships = instanceGraph.getRelationships();



                /*
                 * We expect to get back the following entities:
                 * In the trivial case where start == finish, expect to get the one entity and no relationships
                 * In the non-trivial case where start!=finish and there are no paths expect to get no entities (or relationships)
                 * In the non-trivial case where start!=finish and there are paths expect to get entities and relationships.
                 */


                /* Check entities */
                int resultCount = returnedEntities == null ? 0 : returnedEntities.size();
                int expectedEntityCount = expectedEntityGUIDs.size();
                Map<String, String> parameters = getParameters(entityAGUID, entityBGUID);
                String assertionMessage = MessageFormat.format(assertionMsg8, resultCount, expectedEntityCount, parameters);

                assertCondition(((!expectedEntityGUIDs.isEmpty() && returnedEntities != null
                                          && !(returnedEntities.isEmpty())
                                          && returnedEntities.size() == expectedEntityGUIDs.size())
                                         || expectedEntityGUIDs.isEmpty() && returnedEntities == null),
                                assertion8,
                                assertionMessage,
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId());

                /* Extract the GUIDs so they are easier to check */

                List<String> returnedEntityGUIDs = new ArrayList<>();
                if (returnedEntities != null)
                {
                    for (EntityDetail entity : returnedEntities)
                    {
                        if (entity != null)
                        {
                            returnedEntityGUIDs.add(entity.getGUID());
                        }
                    }
                }
                assertCondition((returnedEntityGUIDs.containsAll(expectedEntityGUIDs)),
                                assertion9,
                                testTypeName + assertionMsg9,
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId());

                /* Check relationships */

                assertCondition(((!expectedRelationshipGUIDs.isEmpty() && returnedRelationships != null
                                          && !(returnedRelationships.isEmpty())
                                          && returnedRelationships.size() == expectedRelationshipGUIDs.size())
                                         || expectedRelationshipGUIDs.isEmpty() && returnedRelationships == null),
                                assertion10,
                                testTypeName + assertionMsg10,
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId());

                /* Extract the GUIDs so they are easier to check */

                List<String> returnedRelationshipGUIDs = new ArrayList<>();
                if (returnedRelationships != null)
                {
                    for (Relationship relationship : returnedRelationships)
                    {
                        if (relationship != null)
                        {
                            returnedRelationshipGUIDs.add(relationship.getGUID());
                        }
                    }
                }
                assertCondition((returnedRelationshipGUIDs.containsAll(expectedRelationshipGUIDs)),
                                assertion11,
                                testTypeName + assertionMsg11,
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.LINKED_ENTITIES.getRequirementId());


            }
        }
    }

    private Map<String, String> getParameters(String startEntityGUID, String endEntityGUID)
    {
        Map<String,String> parameters = new TreeMap<>();
        parameters.put("startEntityGUID", startEntityGUID);
        parameters.put("endEntityGUID", endEntityGUID);
        parameters.put("limitResultsByStatus", "null");
        parameters.put("asOfTime", "null");
        return parameters;
    }

}
