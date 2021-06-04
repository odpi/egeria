/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.restore;

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
 * Test performance of relationship restore operations.
 */
public class TestRelationshipRestore extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-restore-performance";
    private static final String TEST_CASE_NAME = "Repository relationship restore performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_RESTORE     = TEST_CASE_ID + "-restoreRelationship";
    private static final String A_RESTORE_MSG = "Repository performs restore of deleted instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipRestore(PerformanceWorkPad workPad,
                                   RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_RESTORE.getProfileId());

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
        restoreRelationships(metadataCollection, relationshipsToDelete);

        super.setSuccessMessage("Relationship restore performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of relationships that are deleted.
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs of deleted relationships
     * @throws Exception on any errors
     */
    private Set<String> getKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        List<InstanceStatus> deleted = new ArrayList<>();
        deleted.add(InstanceStatus.DELETED);
        long start = System.nanoTime();
        List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                null,
                null,
                0,
                deleted,
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
     * Attempt to restore a number of deleted relationships.
     * @param metadataCollection through which to call restoreRelationship
     * @param keys GUIDs of relationships to restore
     * @throws Exception on any errors
     */
    private void restoreRelationships(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "restoreRelationship";

        try {

            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.restoreRelationship(workPad.getLocalServerUserId(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(result != null,
                        A_RESTORE,
                        A_RESTORE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_RESTORE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_RESTORE,
                    A_RESTORE_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_RESTORE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "restore deleted relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
