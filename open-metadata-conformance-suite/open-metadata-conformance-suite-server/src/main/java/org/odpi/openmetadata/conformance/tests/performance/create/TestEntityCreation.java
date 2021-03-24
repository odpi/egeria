/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.create;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.Map;


/**
 * Test performance of entity creation operations.
 */
public class TestEntityCreation extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-creation-performance";
    private static final String TEST_CASE_NAME = "Repository entity creation performance test case";

    private static final String A_ADD_ENTITY     = TEST_CASE_ID + "-addEntity";
    private static final String A_ADD_ENTITY_MSG = "Repository performs creation of instances of type: ";

    private static final String A_SAVE_ENTITY_RC     = TEST_CASE_ID + "-saveEntityReferenceCopy";
    private static final String A_SAVE_ENTITY_RC_MSG = "Repository performs creation of reference copies of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;



    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityCreation(PerformanceWorkPad workPad,
                              EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_CREATION.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
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
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        int numInstances = super.getInstancesPerType();

        addEntities(metadataCollection, numInstances);
        // TODO: addExternalEntities(metadataCollection, numInstances);
        // TODO: addEntityProxy(metadataCollection, repositoryHelper, numInstances);
        saveEntityReferenceCopies(metadataCollection, repositoryHelper, numInstances);

        super.setSuccessMessage("Entity creation performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to add a number of new entities.
     * @param metadataCollection through which to call addEntity
     * @param numInstances of times to call addEntity
     * @throws Exception on any errors
     */
    private void addEntities(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {

        InstanceProperties instProps = null;
        try {

            for (int i = 0; i < numInstances; i++)
            {
                instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef, i);

                long start = System.nanoTime();
                EntityDetail result = metadataCollection.addEntity(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        instProps,
                        null,
                        null);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                performanceWorkPad.incrementEntitiesCreated(1);

                assertCondition(result != null,
                        A_ADD_ENTITY,
                        A_ADD_ENTITY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_CREATION.getProfileId(),
                        null,
                        "addEntity",
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_ADD_ENTITY,
                    A_ADD_ENTITY_MSG + testTypeName,
                    PerformanceProfile.ENTITY_CREATION.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String methodName = "addEntity";
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialClassifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to add a number of new reference copy entities.
     * @param metadataCollection through which to call saveEntityReferenceCopy
     * @param repositoryHelper used to quickly construct proper instances
     * @param numInstances of times to call saveEntityReferenceCopy
     * @throws Exception on any errors
     */
    private void saveEntityReferenceCopies(OMRSMetadataCollection metadataCollection,
                                           OMRSRepositoryHelper repositoryHelper,
                                           int numInstances) throws Exception
    {

        InstanceProperties instProps = null;
        try {

            for (int i = 0; i < numInstances; i++)
            {
                instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef, i);
                EntityDetail rc = repositoryHelper.getSkeletonEntity(testCaseId,
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        performanceWorkPad.getLocalServerUserId(),
                        entityDef.getName());
                rc.setProperties(instProps);

                long start = System.nanoTime();
                metadataCollection.saveEntityReferenceCopy(workPad.getLocalServerUserId(), rc);
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                performanceWorkPad.incrementEntitiesCreated(1);

                assertCondition(true,
                        A_SAVE_ENTITY_RC,
                        A_SAVE_ENTITY_RC_MSG + testTypeName,
                        PerformanceProfile.ENTITY_CREATION.getProfileId(),
                        null,
                        "saveEntityReferenceCopy",
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_SAVE_ENTITY_RC,
                    A_SAVE_ENTITY_RC_MSG + testTypeName,
                    PerformanceProfile.ENTITY_CREATION.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String methodName = "saveEntityReferenceCopy";
            String operationDescription = "add an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
            parameters.put("initialClassifications", "null");
            parameters.put("initialStatus", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
