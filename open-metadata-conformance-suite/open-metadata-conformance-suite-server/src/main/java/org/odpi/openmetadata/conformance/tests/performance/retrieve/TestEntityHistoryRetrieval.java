/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retrieve;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of entity history retrieval operations.
 */
public class TestEntityHistoryRetrieval extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-history-retrieval-performance";
    private static final String TEST_CASE_NAME = "Repository entity history retrieval performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs historical search for unordered first instancesPerType instances of type: ";

    private static final String A_GET_HISTORY     = TEST_CASE_ID + "-getEntityDetail";
    private static final String A_GET_HISTORY_MSG = "Repository performs retrieval of historical instance of type: ";

    private static final String A_GET_FULL_HISTORY     = TEST_CASE_ID + "-getEntityDetailHistory";
    private static final String A_GET_FULL_HISTORY_MSG = "Repository performs retrieval of full history of instance of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;
    private final Date                asOfTime;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @param asOfTime at which to search history
     */
    public TestEntityHistoryRetrieval(PerformanceWorkPad workPad,
                                      EntityDef          entityDef,
                                      Date               asOfTime)
    {
        super(workPad, PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                TEST_CASE_ID,
                TEST_CASE_NAME);

        this.asOfTime = asOfTime;
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

        Set<String> keysToRetrieve = getEntityKeys(metadataCollection, numInstances);
        getEntityDetail(metadataCollection, keysToRetrieve);
        getEntityDetailHistory(metadataCollection, keysToRetrieve);

        super.setSuccessMessage("Entity history retrieval performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of entity GUIDs for this type.
     *
     * @param metadataCollection through which to call findEntities
     * @param numInstances of entities to retrieve
     * @return a set of entity GUIDs to retrieve
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
                    asOfTime,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(entitiesToRetrieve != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityDef.getName(),
                    PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);
            if (entitiesToRetrieve != null) {
                keys = entitiesToRetrieve.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                    null);
        }
        return keys;
    }

    /**
     * Retrieve a number of instances.
     * @param metadataCollection through which to call getEntityDetail
     * @param keys GUIDs of instances to retrieve
     * @throws Exception on any errors
     */
    private void getEntityDetail(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getEntityDetail";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                EntitySummary result = metadataCollection.getEntityDetail(workPad.getLocalServerUserId(),
                        guid,
                        asOfTime);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_HISTORY,
                        A_GET_HISTORY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_HISTORY,
                    A_GET_HISTORY_MSG + testTypeName,
                    PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve historical instance of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("asOfTime", asOfTime.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Retrieve the full history of a number of instances.
     * @param metadataCollection through which to call getEntityDetailHistory
     * @param keys GUIDs of instances to retrieve
     * @throws Exception on any errors
     */
    private void getEntityDetailHistory(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getEntityDetailHistory";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                List<EntityDetail> result = metadataCollection.getEntityDetailHistory(workPad.getLocalServerUserId(),
                        guid,
                        null,
                        null,
                        0,
                        performanceWorkPad.getMaxPageSize(),
                        HistorySequencingOrder.BACKWARDS);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_FULL_HISTORY,
                        A_GET_FULL_HISTORY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_FULL_HISTORY,
                    A_GET_FULL_HISTORY_MSG + testTypeName,
                    PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve full history of instance of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
