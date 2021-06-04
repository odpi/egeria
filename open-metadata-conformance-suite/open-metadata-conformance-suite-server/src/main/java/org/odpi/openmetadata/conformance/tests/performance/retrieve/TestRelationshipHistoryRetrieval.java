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
 * Test performance of relationship retrieval operations.
 */
public class TestRelationshipHistoryRetrieval extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-history-retrieval-performance";
    private static final String TEST_CASE_NAME = "Repository relationship history retrieval performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationships";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs historical search for unordered first instancesPerType instances of type: ";

    private static final String A_GET_HISTORY     = TEST_CASE_ID + "-getRelationship";
    private static final String A_GET_HISTORY_MSG = "Repository performs retrieval of historical instance of type: ";

    private static final String A_GET_FULL_HISTORY     = TEST_CASE_ID + "-getRelationshipHistory";
    private static final String A_GET_FULL_HISTORY_MSG = "Repository performs retrieval of full history of instance of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;
    private final Date                asOfTime;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     * @param asOfTime at which to search history
     */
    public TestRelationshipHistoryRetrieval(PerformanceWorkPad workPad,
                                            RelationshipDef    relationshipDef,
                                            Date               asOfTime)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileId());

        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
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

        Set<String> keysToRetrieve = getRelationshipKeys(metadataCollection, numInstances);
        getRelationship(metadataCollection, keysToRetrieve);
        getRelationshipHistory(metadataCollection, keysToRetrieve);

        super.setSuccessMessage("Relationship history retrieval performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of relationship GUIDs for this type.
     *
     * @param metadataCollection through which to call findRelationships
     * @param numInstances of relationships to retrieve
     * @return a set of relationship GUIDs to retrieve
     * @throws Exception on any errors
     */
    private Set<String> getRelationshipKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        Set<String> keys = new HashSet<>();
        try {

            long start = System.nanoTime();
            List<Relationship> relationshipsToRetrieve = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    null,
                    null,
                    0,
                    null,
                    asOfTime,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(relationshipsToRetrieve != null,
                    A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + relationshipDef.getName(),
                    PerformanceProfile.RELATIONSHIP_HISTORY_SEARCH.getProfileId(),
                    null,
                    "findRelationships",
                    elapsedTime);
            if (relationshipsToRetrieve != null) {
                keys = relationshipsToRetrieve.stream().map(Relationship::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_HISTORY_SEARCH.getProfileId(),
                    null);
        }
        return keys;
    }

    /**
     * Retrieve a number of instances.
     * @param metadataCollection through which to call getRelationship
     * @param keys GUIDs of instances to retrieve
     * @throws Exception on any errors
     */
    private void getRelationship(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getRelationship";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.getRelationship(workPad.getLocalServerUserId(),
                        guid,
                        asOfTime);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_HISTORY,
                        A_GET_HISTORY_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_HISTORY,
                    A_GET_HISTORY_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String operationDescription = "retrieve historical instance of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            parameters.put("asOfTime", asOfTime.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Retrieve the full history of a number of instances.
     * @param metadataCollection through which to call getRelationshipHistory
     * @param keys GUIDs of instances to retrieve
     * @throws Exception on any errors
     */
    private void getRelationshipHistory(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getRelationshipHistory";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                List<Relationship> result = metadataCollection.getRelationshipHistory(workPad.getLocalServerUserId(),
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
                        PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_GET_FULL_HISTORY,
                    A_GET_FULL_HISTORY_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "retrieve full history of instance of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
