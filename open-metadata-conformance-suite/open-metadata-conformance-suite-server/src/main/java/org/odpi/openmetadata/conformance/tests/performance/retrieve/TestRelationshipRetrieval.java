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
 * Test performance of relationship retrieval operations.
 */
public class TestRelationshipRetrieval extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-retrieval-performance";
    private static final String TEST_CASE_NAME = "Repository relationship retrieval performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationships";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType instances, with a version greater than 1, of type: ";

    private static final String A_IS_KNOWN     = TEST_CASE_ID + "-isRelationshipKnown";
    private static final String A_IS_KNOWN_MSG = "Repository performs check of existence of instances of type: ";

    private static final String A_GET_INSTANCE     = TEST_CASE_ID + "-getRelationship";
    private static final String A_GET_INSTANCE_MSG = "Repository performs retrieval of instance of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipRetrieval(PerformanceWorkPad workPad,
                                     RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_RETRIEVAL.getProfileId());

        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
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

        Set<String> keysToRetrieve = getRelationshipKeys(metadataCollection, numInstances);
        isRelationshipKnown(metadataCollection, keysToRetrieve);
        getRelationship(metadataCollection, keysToRetrieve);

        super.setSuccessMessage("Relationship retrieval performance tests complete for: " + testTypeName);
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
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(relationshipsToRetrieve != null,
                    A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + relationshipDef.getName(),
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null,
                    "findRelationships",
                    elapsedTime);
            if (relationshipsToRetrieve != null) {
                keys = relationshipsToRetrieve.stream().map(Relationship::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null);
        }
        return keys;
    }

    /**
     * Check the existence of a number of relationships.
     * @param metadataCollection through which to call isRelationshipKnown
     * @param keys GUIDs of instances to check
     * @throws Exception on any errors
     */
    private void isRelationshipKnown(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "isRelationshipKnown";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.isRelationshipKnown(workPad.getLocalServerUserId(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_IS_KNOWN,
                        A_IS_KNOWN_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "check existence of relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

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
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_GET_INSTANCE,
                        A_GET_INSTANCE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_RETRIEVAL.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "retrieve instance of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
