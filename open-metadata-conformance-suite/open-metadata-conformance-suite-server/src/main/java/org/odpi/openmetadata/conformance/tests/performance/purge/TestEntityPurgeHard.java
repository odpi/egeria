/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.purge;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of entity purge operations.
 */
public class TestEntityPurgeHard extends TestEntityPurge
{

    private static final String A_PURGE     = TEST_CASE_ID + "-purgeEntity";
    private static final String A_PURGE_MSG = "Repository purge of instances of type: ";

    private static final String A_PURGE_RC     = TEST_CASE_ID + "-purgeEntityReferenceCopy";
    private static final String A_PURGE_RC_MSG = "Repository purge of reference copy instances of type: ";

    private static final Set<String> purgedGUIDs = new HashSet<>();

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @throws Exception on any initialization error
     */
    public TestEntityPurgeHard(PerformanceWorkPad workPad,
                               EntityDef          entityDef) throws Exception
    {
        super(workPad, entityDef, A_PURGE, TEST_CASE_NAME + " - hard-delete");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        purgeEntities();
        purgeEntityReferenceCopies();
        super.setSuccessMessage("Entity purge performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to purge a number of deleted entities.
     * @throws Exception on any errors
     */
    private void purgeEntities() throws Exception
    {

        final String methodName = "purgeEntity";

        Set<String> guids = guidsByType.get(entityDef.getName());
        if (guids != null) {
            String lastGuid = null;
            try {
                for (String guid : guids) {
                    if (!purgedGUIDs.contains(guid)) {
                        lastGuid = guid;
                        long start = System.nanoTime();
                        metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                                entityDef.getGUID(),
                                entityDef.getName(),
                                guid);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(true,
                                A_PURGE,
                                A_PURGE_MSG + testTypeName,
                                PerformanceProfile.ENTITY_PURGE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                        purgedGUIDs.add(guid);
                    }
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_PURGE,
                        A_PURGE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_PURGE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "purge entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                parameters.put("deletedEntityGUID", lastGuid);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                throw new Exception(msg, exc);
            }
        }

    }


    /**
     * Attempt to purge a number of reference copy entities.
     * @throws Exception on any errors
     */
    private void purgeEntityReferenceCopies() throws Exception
    {

        final String methodName = "purgeEntityReferenceCopy";

        Set<String> guidsRC = guidsByTypeRC.get(entityDef.getName());
        if (guidsRC != null) {
            String lastGuid = null;
            try {
                for (String guid : guidsRC) {
                    if (!purgedGUIDs.contains(guid)) {
                        lastGuid = guid;
                        long start = System.nanoTime();
                        metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(),
                                guid,
                                entityDef.getGUID(),
                                entityDef.getName(),
                                performanceWorkPad.getReferenceCopyMetadataCollectionId());
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(true,
                                A_PURGE_RC,
                                A_PURGE_RC_MSG + testTypeName,
                                PerformanceProfile.ENTITY_PURGE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                        purgedGUIDs.add(guid);
                    }
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_PURGE_RC,
                        A_PURGE_RC_MSG + testTypeName,
                        PerformanceProfile.ENTITY_PURGE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "purge reference copy entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                parameters.put("deletedEntityGUID", lastGuid);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                throw new Exception(msg, exc);
            }
        }

    }

}
