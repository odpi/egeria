/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retype;

import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Test performance of entity retype operations.
 */
public class TestEntityRetypeSuper extends TestEntityRetype
{

    private static final String A_RETYPE_SUPER     = TEST_CASE_ID + "-reTypeEntity-toSupertype";
    private static final String A_RETYPE_SUPER_MSG = "Repository performs retyping of homed instances to supertype of type: ";


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @throws Exception on any initialization error
     */
    public TestEntityRetypeSuper(PerformanceWorkPad workPad,
                                 EntityDef          entityDef) throws Exception
    {
        super(workPad, entityDef, A_RETYPE_SUPER, TEST_CASE_NAME + " - to supertype");
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    @Override
    protected void run() throws Exception
    {
        reTypeEntitiesSuper();
        super.setSuccessMessage("Entity retype to supertype performance tests complete for: " + testTypeName);
    }


    /**
     * Attempt to retype the entities provided to its supertype.
     * @throws Exception on any errors
     */
    private void reTypeEntitiesSuper() throws Exception
    {

        final String methodName = "reTypeEntitySuper";

        Set<String> guids = guidsByType.get(entityDef.getName());
        if (guids != null) {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            TypeDefLink superType = entityDef.getSuperType();
            if (superType != null) {
                String superTypeName = superType.getName();
                TypeDefSummary targetType = repositoryHelper.getTypeDefByName(testCaseId, superTypeName);
                String lastGuid = null;
                try {
                    for (String guid : guids) {
                        lastGuid = guid;
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                guid,
                                targetType,
                                entityDef);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(true,
                                A_RETYPE_SUPER,
                                A_RETYPE_SUPER_MSG + testTypeName,
                                PerformanceProfile.ENTITY_RETYPE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                } catch (Exception exc) {
                    String operationDescription = "retype entity to supertype from type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("entityGUID", lastGuid);
                    parameters.put("currentTypeDefSummary", entityDef.getName());
                    parameters.put("newTypeDefSummary", superTypeName);
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc);
                    throw new Exception(msg, exc);
                }
            }
        }

    }

}
