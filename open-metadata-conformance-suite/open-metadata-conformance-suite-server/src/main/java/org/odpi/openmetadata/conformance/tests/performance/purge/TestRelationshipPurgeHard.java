/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.purge;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of relationship purge operations.
 */
public class TestRelationshipPurgeHard extends TestRelationshipPurge
{

    private static final String A_PURGE     = TEST_CASE_ID + "-purgeRelationship";
    private static final String A_PURGE_MSG = "Repository purge of instances of type: ";

    private static final String A_PURGE_RC     = TEST_CASE_ID + "-purgeRelationshipReferenceCopy";
    private static final String A_PURGE_RC_MSG = "Repository purge of reference copy instances of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     * @throws Exception on any initialization error
     */
    public TestRelationshipPurgeHard(PerformanceWorkPad workPad,
                                     RelationshipDef    relationshipDef) throws Exception
    {
        super(workPad, relationshipDef, A_PURGE, TEST_CASE_NAME + " - hard-delete");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        purgeRelationships();
        purgeRelationshipReferenceCopies();
        super.setSuccessMessage("Relationship purge performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to purge a number of deleted relationships.
     * @throws Exception on any errors
     */
    private void purgeRelationships() throws Exception
    {

        final String methodName = "purgeRelationship";

        String lastGuid = null;
        try {

            Set<String> guids = guidsByType.get(relationshipDef.getName());
            for (String guid : guids) {
                lastGuid = guid;
                long start = System.nanoTime();
                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(true,
                        A_PURGE,
                        A_PURGE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_PURGE,
                    A_PURGE_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "purge deleted relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", relationshipDef.getGUID());
            parameters.put("typeDefName", relationshipDef.getName());
            parameters.put("deletedRelationshipGUID", lastGuid);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
            throw new Exception(msg, exc);
        }

    }


    /**
     * Attempt to purge a number of reference copy relationships.
     * @throws Exception on any errors
     */
    private void purgeRelationshipReferenceCopies() throws Exception
    {

        final String methodName = "purgeRelationshipReferenceCopy";

        String lastGuid = null;
        try {
            Set<String> guidsRC = guidsByTypeRC.get(relationshipDef.getName());
            for (String guid : guidsRC) {
                lastGuid = guid;
                long start = System.nanoTime();
                metadataCollection.purgeRelationshipReferenceCopy(workPad.getLocalServerUserId(),
                        guid,
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        performanceWorkPad.getReferenceCopyMetadataCollectionId());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(true,
                        A_PURGE_RC,
                        A_PURGE_RC_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_PURGE_RC,
                    A_PURGE_RC_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "purge reference copy relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeDefGUID", relationshipDef.getGUID());
            parameters.put("typeDefName", relationshipDef.getName());
            parameters.put("deletedRelationshipGUID", lastGuid);
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
            throw new Exception(msg, exc);
        }

    }

}
