/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retype;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of entity retype operations.
 */
public class TestEntityRetypeSub extends TestEntityRetype
{

    private static final String A_RETYPE_SUB     = TEST_CASE_ID + "-reTypeEntity-toSubtype";
    private static final String A_RETYPE_SUB_MSG = "Repository performs retyping of homed instances to each subtype of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @throws Exception on any initialization error
     */
    public TestEntityRetypeSub(PerformanceWorkPad workPad,
                               EntityDef          entityDef) throws Exception
    {
        super(workPad, entityDef, A_RETYPE_SUB, TEST_CASE_NAME + " - to subtype");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    @Override
    protected void run() throws Exception
    {
        reTypeEntitiesSub();
        super.setSuccessMessage("Entity retype to subtype performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to retype the entities provided to one of its subtypes.
     * @throws Exception on any errors
     */
    private void reTypeEntitiesSub() throws Exception
    {

        final String methodName = "reTypeEntitySub";

        Set<String> guids = guidsByType.get(entityDef.getName());
        if (guids != null) {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            List<String> subTypeNames = repositoryHelper.getSubTypesOf(testCaseId, entityDef.getName());
            if (subTypeNames != null && !subTypeNames.isEmpty()) {
                String subTypeName = subTypeNames.get(0);
                TypeDefSummary targetType = repositoryHelper.getTypeDefByName(testCaseId, subTypeName);
                String lastGuid = null;
                try {
                    for (String guid : guids) {
                        lastGuid = guid;
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                guid,
                                entityDef,
                                targetType);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(true,
                                A_RETYPE_SUB,
                                A_RETYPE_SUB_MSG + testTypeName,
                                PerformanceProfile.ENTITY_RETYPE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                } catch (Exception exc) {
                    String operationDescription = "retype entity to subtype from type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityGUID", lastGuid);
                    parameters.put("currentTypeDefSummary", entityDef.getName());
                    parameters.put("newTypeDefSummary", subTypeName);
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                    throw new Exception(msg, exc);
                }
            }
        }

    }

}
