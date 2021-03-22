/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.delete;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Test performance of relationship delete operations.
 */
public class TestRelationshipDelete extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-delete-performance";
    private static final String TEST_CASE_NAME = "Repository relationship delete performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteRelationship";
    private static final String A_DELETE_MSG = "Repository performs delete of instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipDelete(PerformanceWorkPad workPad,
                                  RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_DELETE.getProfileId());

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

        Set<String> relationshipsToDelete = getKeys(metadataCollection, numInstances);
        deleteRelationships(metadataCollection, relationshipsToDelete);

        super.setSuccessMessage("Relationship delete performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of relationships that are homed in the technology under test's repository.
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs of relationships to delete
     * @throws Exception on any errors
     */
    private Set<String> getKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getKeys";
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                null,
                "metadataCollectionId",
                repositoryHelper.getExactMatchRegex(performanceWorkPad.getTutMetadataCollectionId()),
                methodName);
        long start = System.nanoTime();
        List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
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
        assertCondition(relationships != null,
                A_FIND_RELATIONSHIPS,
                A_FIND_RELATIONSHIPS_MSG + testTypeName,
                PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                null,
                "findRelationshipsByProperty",
                elapsedTime);
        return relationships == null ? null : relationships.stream().map(Relationship::getGUID).collect(Collectors.toSet());
    }

    /**
     * Attempt to delete a number of existing relationships.
     * @param metadataCollection through which to call deleteRelationship
     * @param keys GUIDs of relationships to delete
     * @throws Exception on any errors
     */
    private void deleteRelationships(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "deleteRelationship";

        try {

            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                            relationshipDef.getGUID(),
                            relationshipDef.getName(),
                            guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(result != null,
                        A_DELETE,
                        A_DELETE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_DELETE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_DELETE,
                    A_DELETE_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_DELETE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "delete relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
