/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.graph;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of graph query operations.
 */
public class TestGraphQueries extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-graph-query-performance";
    private static final String TEST_CASE_NAME = "Repository graph query performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType instances of type: ";

    private static final String A_GET_RELATIONSHIPS     = TEST_CASE_ID + "-getRelationshipsForEntity";
    private static final String A_GET_RELATIONSHIPS_MSG = "Repository performs retrieval of relationships for instances of type: ";

    private static final String A_GET_NEIGHBORHOOD1     = TEST_CASE_ID + "-getEntityNeighborhood-1";
    private static final String A_GET_NEIGHBORHOOD1_MSG = "Repository performs retrieval of neighborhood (level 1) for instances of type: ";

    private static final String A_GET_NEIGHBORHOOD2     = TEST_CASE_ID + "-getEntityNeighborhood-2";
    private static final String A_GET_NEIGHBORHOOD2_MSG = "Repository performs retrieval of neighborhood (level 2) for instances of type: ";

    private static final String A_GET_NEIGHBORHOOD3     = TEST_CASE_ID + "-getEntityNeighborhood-3";
    private static final String A_GET_NEIGHBORHOOD3_MSG = "Repository performs retrieval of neighborhood (level 3) for instances of type: ";

    private static final String A_GET_RELATED     = TEST_CASE_ID + "-getRelatedEntities";
    private static final String A_GET_RELATED_MSG = "Repository performs retrieval of related entities for instances of type: ";

    private static final String A_GET_LINKING     = TEST_CASE_ID + "-getLinkingEntities";
    private static final String A_GET_LINKING_MSG = "Repository performs retrieval of linking entities for instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestGraphQueries(PerformanceWorkPad workPad,
                            EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.GRAPH_QUERIES.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                TEST_CASE_ID,
                TEST_CASE_NAME);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();
        int numInstances = super.getInstancesPerType();

        Set<String> keys = getEntityKeys(metadataCollection, numInstances);

        getRelationshipsForEntity(metadataCollection, keys);
        getEntityNeighborhood(metadataCollection, keys);
        Map<String, List<String>> results = getRelatedEntities(metadataCollection, keys);
        if (results != null) {
            getLinkingEntities(metadataCollection, results);
        }

        super.setSuccessMessage("Graph query performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of entity GUIDs for this type.
     *
     * @param metadataCollection through which to call findEntities
     * @param numInstances of entities to retrieve
     * @return a set of entity GUIDs
     * @throws Exception on any errors
     */
    private Set<String> getEntityKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        Set<String> keys = new HashSet<>();
        try {

            long start = System.nanoTime();
            List<EntityDetail> entitiesToRetrieve = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                    entityDef.getGUID(),
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(entitiesToRetrieve != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityDef.getName(),
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);
            if (entitiesToRetrieve != null) {
                keys = entitiesToRetrieve.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null);
        }
        return keys;
    }

    /**
     * Attempt to retrieve a number of entities' relationships.
     * @param metadataCollection through which to call getRelationshipsForEntity
     * @param keys instance GUIDs on which to call getRelationshipsForEntity
     * @throws Exception on any errors
     */
    private void getRelationshipsForEntity(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getRelationshipsForEntity";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                List<Relationship> relationships = metadataCollection.getRelationshipsForEntity(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        0,
                        null,
                        null,
                        null,
                        null,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (relationships != null) {
                    assertCondition(true,
                            A_GET_RELATIONSHIPS,
                            A_GET_RELATIONSHIPS_MSG + testTypeName,
                            PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_RELATIONSHIPS,
                    A_GET_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve relationships of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to retrieve a number of entities' neighborhoods.
     * @param metadataCollection through which to call getEntityNeighborhood
     * @param keys instance GUIDs on which to call getEntityNeighborhood
     * @throws Exception on any errors
     */
    private void getEntityNeighborhood(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getEntityNeighborhood";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                InstanceGraph graph = metadataCollection.getEntityNeighborhood(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (graph != null) {
                    assertCondition(true,
                            A_GET_NEIGHBORHOOD1,
                            A_GET_NEIGHBORHOOD1_MSG + testTypeName,
                            PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
                start = System.nanoTime();
                graph = metadataCollection.getEntityNeighborhood(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        null,
                        null,
                        null,
                        null,
                        2);
                elapsedTime = (System.nanoTime() - start) / 1000000;
                if (graph != null) {
                    assertCondition(true,
                            A_GET_NEIGHBORHOOD2,
                            A_GET_NEIGHBORHOOD2_MSG + testTypeName,
                            PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
                start = System.nanoTime();
                graph = metadataCollection.getEntityNeighborhood(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        null,
                        null,
                        null,
                        null,
                        3);
                elapsedTime = (System.nanoTime() - start) / 1000000;
                if (graph != null) {
                    assertCondition(true,
                            A_GET_NEIGHBORHOOD3,
                            A_GET_NEIGHBORHOOD3_MSG + testTypeName,
                            PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_NEIGHBORHOOD1,
                    A_GET_NEIGHBORHOOD1_MSG + testTypeName,
                    PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve neighborhood of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to retrieve a number of entities' related entities.
     * @param metadataCollection through which to call getRelatedEntities
     * @param keys instance GUIDs on which to call getRelatedEntities
     * @return a map of starting entity GUIDs to a list of related entity GUIDs
     * @throws Exception on any errors
     */
    private Map<String, List<String>> getRelatedEntities(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getRelatedEntities";

        Map<String, List<String>> map = new HashMap<>();

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                List<EntityDetail> related = metadataCollection.getRelatedEntities(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        0,
                        null,
                        null,
                        null,
                        null,
                        null,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (related != null) {
                    assertCondition(true,
                            A_GET_RELATED,
                            A_GET_RELATED_MSG + testTypeName,
                            PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                    map.put(guid, related.stream().map(EntityDetail::getGUID).collect(Collectors.toList()));
                }
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_RELATED,
                    A_GET_RELATED_MSG + testTypeName,
                    PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                    null);
            return null;
        } catch (Exception exc) {
            String operationDescription = "retrieve related entities of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

        return map;

    }

    /**
     * Attempt to retrieve a number of entities' linking objects.
     * @param metadataCollection through which to call getLinkingEntities
     * @param keys instance GUIDs on which to call getLinkingEntities
     * @throws Exception on any errors
     */
    private void getLinkingEntities(OMRSMetadataCollection metadataCollection, Map<String, List<String>> keys) throws Exception
    {

        final String methodName = "getLinkingEntities";

        try {
            for (Map.Entry<String, List<String>> entry : keys.entrySet()) {
                String startingGUID = entry.getKey();
                List<String> allTargetGUIDs = entry.getValue();
                if (allTargetGUIDs != null) {
                    List<String> targetGUIDs = allTargetGUIDs;
                    if (allTargetGUIDs.size() > 3) {
                        // If there are more than 3 target GUIDs, then select just 3 for us to use:
                        // the first, the middle, and the last
                        targetGUIDs = new ArrayList<>();
                        int last = allTargetGUIDs.size() - 1;
                        int middle = allTargetGUIDs.size() / 2;
                        targetGUIDs.add(allTargetGUIDs.get(0));
                        targetGUIDs.add(allTargetGUIDs.get(middle));
                        targetGUIDs.add(allTargetGUIDs.get(last));
                    }
                    for (String targetGUID : targetGUIDs) {
                        long start = System.nanoTime();
                        InstanceGraph graph = metadataCollection.getLinkingEntities(workPad.getLocalServerUserId(),
                                startingGUID,
                                targetGUID,
                                null,
                                null);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(graph != null,
                                A_GET_LINKING,
                                A_GET_LINKING_MSG + testTypeName,
                                PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                }
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_LINKING,
                    A_GET_LINKING_MSG + testTypeName,
                    PerformanceProfile.GRAPH_QUERIES.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve linking entities of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
