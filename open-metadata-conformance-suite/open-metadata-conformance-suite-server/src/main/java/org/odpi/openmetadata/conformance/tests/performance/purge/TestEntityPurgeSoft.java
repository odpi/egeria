/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.purge;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of entity purge operations.
 */
public class TestEntityPurgeSoft extends TestEntityPurge
{

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteEntity";
    private static final String A_DELETE_MSG = "Repository delete of instances of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @throws Exception on any initialization error
     */
    public TestEntityPurgeSoft(PerformanceWorkPad workPad,
                               EntityDef          entityDef) throws Exception
    {
        super(workPad, entityDef, A_DELETE, TEST_CASE_NAME + " - soft-delete");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        deleteEntities();
        super.setSuccessMessage("Entity purge performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to delete a number of existing entities.
     * @throws Exception on any errors
     */
    private void deleteEntities() throws Exception
    {

        final String methodName = "deleteEntity";

        Set<String> guids = guidsByType.get(entityDef.getName());
        if (guids != null) {
            String lastGuid = null;
            try {
                for (String guid : guids) {
                    lastGuid = guid;
                    long start = System.nanoTime();
                    EntityDetail result = metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                            entityDef.getGUID(),
                            entityDef.getName(),
                            guid);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_DELETE,
                            A_DELETE_MSG + testTypeName,
                            PerformanceProfile.ENTITY_DELETE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_DELETE,
                        A_DELETE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_DELETE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "delete entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                parameters.put("obsoleteEntityGUID", lastGuid);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                throw new Exception(msg, exc);
            }
        }

    }


}
