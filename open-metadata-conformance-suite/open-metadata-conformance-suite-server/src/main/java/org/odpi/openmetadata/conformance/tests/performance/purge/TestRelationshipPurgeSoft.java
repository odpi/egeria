/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.purge;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of relationship purge operations.
 */
public class TestRelationshipPurgeSoft extends TestRelationshipPurge
{

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteRelationship";
    private static final String A_DELETE_MSG = "Repository performs delete of instances of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     * @throws Exception on any initialization error
     */
    public TestRelationshipPurgeSoft(PerformanceWorkPad workPad,
                                     RelationshipDef    relationshipDef) throws Exception
    {
        super(workPad, relationshipDef, A_DELETE, TEST_CASE_NAME + " - soft-delete");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    @Override
    protected void run() throws Exception
    {
        List<String> methodsToSkip = performanceWorkPad.getMethodsToSkip();
        if (!methodsToSkip.contains("purgeRelationship")) {
            deleteRelationships();
            super.setSuccessMessage("Relationship purge (soft) performance tests complete for: " + testTypeName);
        }
    }


    /**
     * Attempt to delete a number of existing relationships.
     * @throws Exception on any errors
     */
    private void deleteRelationships() throws Exception
    {

        final String methodName = "deleteRelationship";

        String lastGuid = null;
        try {

            Set<String> guids = guidsByType.get(relationshipDef.getName());
            for (String guid : guids) {
                lastGuid = guid;
                long start = System.nanoTime();
                Relationship result = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(true,
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
            parameters.put("typeDefGUID", relationshipDef.getGUID());
            parameters.put("typeDefName", relationshipDef.getName());
            parameters.put("obsoleteEntityGUID", lastGuid);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
            throw new Exception(msg, exc);
        }

    }


}
