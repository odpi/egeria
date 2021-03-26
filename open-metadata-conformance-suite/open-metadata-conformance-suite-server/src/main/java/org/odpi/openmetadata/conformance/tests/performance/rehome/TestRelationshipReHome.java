/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.rehome;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of relationship re-home operations.
 */
public class TestRelationshipReHome extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-re-home-performance";
    private static final String TEST_CASE_NAME = "Repository relationship re-home performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    private static final String A_RE_HOME     = TEST_CASE_ID + "-reHomeRelationship";
    private static final String A_RE_HOME_MSG = "Repository performs re-homing of reference copies of instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipReHome(PerformanceWorkPad workPad,
                                  RelationshipDef          relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_RE_HOME.getProfileId());

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
        String metadataCollectionName = getRepositoryConnector().getMetadataCollectionName();
        int numInstances = super.getInstancesPerType();

        // Only re-home half of the instances, so that we can test purging of the other half
        Set<String> keysToReHome = getRelationshipKeys(metadataCollection, numInstances / 2);
        reHomeRelationships(metadataCollection, metadataCollectionName, keysToReHome);

        super.setSuccessMessage("Relationship re-home performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of relationship GUIDs for this type.
     *
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances of entities to re-home
     * @return a set of relationship GUIDs to re-home
     * @throws Exception on any errors
     */
    private Set<String> getRelationshipKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getRelationshipKeys";
        Set<String> keys = new HashSet<>();
        try {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                    null,
                    "metadataCollectionId",
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getReferenceCopyMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            List<Relationship> relationshipsToReHome = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    byMetadataCollectionId,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(relationshipsToReHome != null,
                    A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null,
                    "findRelationshipsByProperty",
                    elapsedTime);
            if (relationshipsToReHome != null) {
                keys = relationshipsToReHome.stream().map(Relationship::getGUID).collect(Collectors.toSet());
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
     * Attempt to re-home the relationships provided to the technology under test's repository.
     * @param metadataCollection through which to call reHomeRelationship
     * @param metadataCollectionName name of the technology under test's metadata collection
     * @param keys GUIDs of instances to re-home
     * @throws Exception on any errors
     */
    private void reHomeRelationships(OMRSMetadataCollection metadataCollection,
                                    String metadataCollectionName,
                                    Set<String> keys) throws Exception
    {

        final String methodName = "reHomeRelationship";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.reHomeRelationship(workPad.getLocalServerUserId(),
                        guid,
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        performanceWorkPad.getTutMetadataCollectionId(),
                        metadataCollectionName);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(result != null,
                        A_RE_HOME,
                        A_RE_HOME_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_RE_HOME.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (Exception exc) {
            String operationDescription = "re-home relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", relationshipDef.getGUID());
            parameters.put("typeDefName", relationshipDef.getName());
            parameters.put("homeMetadataCollectionId", performanceWorkPad.getReferenceCopyMetadataCollectionId());
            parameters.put("newHomeMetadataCollectionId", performanceWorkPad.getTutMetadataCollectionId());
            parameters.put("newHomeMetadataCollectionName", metadataCollectionName);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
