/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.rehome;

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
 * Test performance of entity re-home operations.
 */
public class TestEntityReHome extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-re-home-performance";
    private static final String TEST_CASE_NAME = "Repository entity re-home performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    private static final String A_RE_HOME     = TEST_CASE_ID + "-reHomeEntity";
    private static final String A_RE_HOME_MSG = "Repository performs re-homing of reference copies of instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityReHome(PerformanceWorkPad workPad,
                            EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_RE_HOME.getProfileId());

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
        String metadataCollectionName = getRepositoryConnector().getMetadataCollectionName();
        int numInstances = super.getInstancesPerType();

        // Only re-home half of the instances, so that we can test purging of the other half
        Set<String> keysToReHome = getEntityKeys(metadataCollection, numInstances / 2);
        reHomeEntities(metadataCollection, metadataCollectionName, keysToReHome);

        super.setSuccessMessage("Entity re-home performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of entity GUIDs for this type.
     *
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances of entities to re-home
     * @return a set of entity GUIDs to re-home
     * @throws Exception on any errors
     */
    private Set<String> getEntityKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getEntityKeys";
        try {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                    null,
                    "metadataCollectionId",
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getReferenceCopyMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            List<EntityDetail> entitiesToReHome = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                    entityDef.getGUID(),
                    byMetadataCollectionId,
                    MatchCriteria.ALL,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            if (entitiesToReHome != null) {
                assertCondition(true,
                        A_FIND_ENTITIES,
                        A_FIND_ENTITIES_MSG + testTypeName,
                        PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                        null,
                        "findEntitiesByProperty",
                        elapsedTime);
                return entitiesToReHome.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null);
        }
        return null;
    }

    /**
     * Attempt to re-home the entities provided to the technology under test's repository.
     * @param metadataCollection through which to call reHomeEntity
     * @param metadataCollectionName name of the technology under test's metadata collection
     * @param keys GUIDs of instances to re-home
     * @throws Exception on any errors
     */
    private void reHomeEntities(OMRSMetadataCollection metadataCollection,
                                String metadataCollectionName,
                                Set<String> keys) throws Exception
    {

        final String methodName = "reHomeEntity";

        if (keys != null) {
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    EntityDetail result = metadataCollection.reHomeEntity(workPad.getLocalServerUserId(),
                            guid,
                            entityDef.getGUID(),
                            entityDef.getName(),
                            performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                            performanceWorkPad.getTutMetadataCollectionId(),
                            metadataCollectionName);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_RE_HOME,
                            A_RE_HOME_MSG + testTypeName,
                            PerformanceProfile.ENTITY_RE_HOME.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (Exception exc) {
                String operationDescription = "re-home entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                parameters.put("homeMetadataCollectionId", performanceWorkPad.getReferenceCopyMetadataCollectionId());
                parameters.put("newHomeMetadataCollectionId", performanceWorkPad.getTutMetadataCollectionId());
                parameters.put("newHomeMetadataCollectionName", metadataCollectionName);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
