/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.delete;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of entity delete operations.
 */
public class TestEntityDelete extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-delete-performance";
    private static final String TEST_CASE_NAME = "Repository entity delete performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteEntity";
    private static final String A_DELETE_MSG = "Repository delete of instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityDelete(PerformanceWorkPad workPad,
                            EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_DELETE.getProfileId());

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
        deleteEntities(metadataCollection, keys);

        super.setSuccessMessage("Entity delete performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of entities that are homed in the technology under test's repository.
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs that are homed in the technology under test's repository
     * @throws Exception on any errors
     */
    private Set<String> getKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getKeys";
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                null,
                "metadataCollectionId",
                repositoryHelper.getExactMatchRegex(performanceWorkPad.getTutMetadataCollectionId()),
                methodName);
        List<InstanceStatus> initial = new ArrayList<>();
        initial.add(entityDef.getInitialStatus());
        long start = System.nanoTime();
        List<EntityDetail> entities = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                byMetadataCollectionId,
                MatchCriteria.ALL,
                0,
                initial,
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
     * Attempt to delete a number of existing entities.
     * @param metadataCollection through which to call deleteEntity
     * @param keys GUIDs of entities to delete
     * @throws Exception on any errors
     */
    private void deleteEntities(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "deleteEntity";

        if (keys != null) {
            try {
                for (String guid : keys) {
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
                parameters.put("typeGUID", entityDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
