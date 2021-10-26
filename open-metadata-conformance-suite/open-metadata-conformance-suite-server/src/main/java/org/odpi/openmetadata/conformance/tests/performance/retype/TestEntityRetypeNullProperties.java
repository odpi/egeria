/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retype;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of entity retype operations.
 */
public class TestEntityRetypeNullProperties extends TestEntityRetype
{

    private static final String A_REMOVE_PROPERTIES     = TEST_CASE_ID + "-updateEntityProperties-remove";
    private static final String A_REMOVE_PROPERTIES_MSG = "Repository performs removal of all entity properties of instance of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @throws Exception on any initialization error
     */
    public TestEntityRetypeNullProperties(PerformanceWorkPad workPad,
                                          EntityDef          entityDef) throws Exception
    {
        super(workPad, entityDef, A_REMOVE_PROPERTIES, TEST_CASE_NAME + " - removing properties");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    @Override
    protected void run() throws Exception
    {
        nullPropertiesForEntities();
        super.setSuccessMessage("Entity retype property null performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to remove all properties of the entities provided, to allow them to be re-typed without property problems.
     * @throws Exception on any errors
     */
    private void nullPropertiesForEntities() throws Exception
    {

        final String methodName = "nullPropertiesForEntities";

        Set<String> guids = guidsByType.get(entityDef.getName());
        if (guids != null) {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            List<String> subTypeNames = repositoryHelper.getSubTypesOf(testCaseId, entityDef.getName());
            if (subTypeNames != null && !subTypeNames.isEmpty()) {
                String lastGuid = null;
                try {
                    for (String guid : guids) {
                        lastGuid = guid;
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(),
                                guid,
                                new InstanceProperties());
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(true,
                                A_REMOVE_PROPERTIES,
                                A_REMOVE_PROPERTIES_MSG + testTypeName,
                                PerformanceProfile.ENTITY_UPDATE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                } catch (Exception exc) {
                    String operationDescription = "remove properties of entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityGUID", lastGuid);
                    parameters.put("entityTypeGUID", entityDef.getGUID());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                    throw new Exception(msg, exc);
                }
            }
        }

    }

}
