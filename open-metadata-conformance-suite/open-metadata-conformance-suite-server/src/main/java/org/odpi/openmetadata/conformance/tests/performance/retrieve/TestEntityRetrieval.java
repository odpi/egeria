/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retrieve;

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
 * Test performance of entity retrieval operations.
 */
public class TestEntityRetrieval extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-retrieval-performance";
    private static final String TEST_CASE_NAME = "Repository entity retrieval performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType instances, with a version greater than 1, of type: ";

    private static final String A_IS_KNOWN     = TEST_CASE_ID + "-isEntityKnown";
    private static final String A_IS_KNOWN_MSG = "Repository performs check of existence of instances of type: ";

    private static final String A_GET_SUMMARY     = TEST_CASE_ID + "-getEntitySummary";
    private static final String A_GET_SUMMARY_MSG = "Repository performs retrieval of summary entity of type: ";

    private static final String A_GET_INSTANCE     = TEST_CASE_ID + "-getEntityDetail";
    private static final String A_GET_INSTANCE_MSG = "Repository performs retrieval of instance of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityRetrieval(PerformanceWorkPad workPad,
                               EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_RETRIEVAL.getProfileId());

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

        Set<String> keysToRetrieve = getEntityKeys(metadataCollection, numInstances);
        isEntityKnown(metadataCollection, keysToRetrieve);
        getEntitySummary(metadataCollection, keysToRetrieve);
        getEntityDetail(metadataCollection, keysToRetrieve);

        super.setSuccessMessage("Entity retrieval performance tests complete for: " + testTypeName);
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
     * Check the existence of a number of entities.
     * @param metadataCollection through which to call isEntityKnown
     * @param keys GUIDs of instances to check
     * @throws Exception on any errors
     */
    private void isEntityKnown(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "isEntityKnown";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                EntityDetail result = metadataCollection.isEntityKnown(workPad.getLocalServerUserId(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_IS_KNOWN,
                        A_IS_KNOWN_MSG + testTypeName,
                        PerformanceProfile.ENTITY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "check existence of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Retrieve the summary of a number of instances.
     * @param metadataCollection through which to call getEntitySummary
     * @param keys GUIDs of instances to retrieve
     * @throws Exception on any errors
     */
    private void getEntitySummary(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "getEntitySummary";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                EntitySummary result = metadataCollection.getEntitySummary(workPad.getLocalServerUserId(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_SUMMARY,
                        A_GET_SUMMARY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "retrieve summary of entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

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
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_INSTANCE,
                        A_GET_INSTANCE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "retrieve instance of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
