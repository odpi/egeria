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
 * Test performance of entity restore operations.
 */
public class TestEntityRestore extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-restore-performance";
    private static final String TEST_CASE_NAME = "Repository entity restore performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_RESTORE     = TEST_CASE_ID + "-restoreEntity";
    private static final String A_RESTORE_MSG = "Repository performs restore of deleted instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityRestore(PerformanceWorkPad workPad,
                             EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_RESTORE.getProfileId());

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
        int numInstances = super.getInstancesPerType();

        Set<String> keys = getKeys(metadataCollection, numInstances);
        restoreEntities(metadataCollection, keys);

        super.setSuccessMessage("Entity restore performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of entities that are deleted.
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs that are deleted
     * @throws Exception on any errors
     */
    private Set<String> getKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        List<InstanceStatus> deleted = new ArrayList<>();
        deleted.add(InstanceStatus.DELETED);
        long start = System.nanoTime();
        List<EntityDetail> entities = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                null,
                null,
                0,
                deleted,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;
        if (entities != null) {
            assertCondition(true,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntitiesByProperty",
                    elapsedTime);
            return entities.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * Attempt to restore a number of deleted entities.
     * @param metadataCollection through which to call restoreEntity
     * @param keys GUIDs of entities to restore
     * @throws Exception on any errors
     */
    private void restoreEntities(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "restoreEntity";

        if (keys != null) {
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    EntityDetail result = metadataCollection.restoreEntity(workPad.getLocalServerUserId(),
                            guid);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_RESTORE,
                            A_RESTORE_MSG + testTypeName,
                            PerformanceProfile.ENTITY_RESTORE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_RESTORE,
                        A_RESTORE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_RESTORE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "restore deleted entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
