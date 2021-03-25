/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.update;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of entity update operations.
 */
public class TestEntityUpdate extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-update-performance";
    private static final String TEST_CASE_NAME = "Repository entity update performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_UPDATE_PROPERTIES     = TEST_CASE_ID + "-updateEntityProperties";
    private static final String A_UPDATE_PROPERTIES_MSG = "Repository performs update of properties on instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityUpdate(PerformanceWorkPad workPad,
                            EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_UPDATE.getProfileId());

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

        List<EntityDetail> entitiesToUpdate = getEntitiesToUpdate(metadataCollection, numInstances);
        // TODO: updateEntityStatus(metadataCollection, numInstances);
        updateEntityProperties(metadataCollection, entitiesToUpdate);

        super.setSuccessMessage("Entity update performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of entities that are homed in the technology under test's repository.
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances number of instances to retrieve
     * @return list of instances homed in the technology under test's repository
     * @throws Exception on any errors
     */
    private List<EntityDetail> getEntitiesToUpdate(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getEntitiesToUpdate";
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef(testCaseId, entityDef, methodName);
        if (properties != null && !properties.isEmpty()) {
            InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                    null,
                    "metadataCollectionId",
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getTutMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            List<EntityDetail> entitiesToUpdate = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
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
            assertCondition(entitiesToUpdate != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntitiesByProperty",
                    elapsedTime);
            return entitiesToUpdate;
        }
        return null;
    }

    /**
     * Attempt to update a number of entities' properties.
     * @param metadataCollection through which to call updateEntityProperties
     * @param entitiesToUpdate the entities to update
     * @throws Exception on any errors
     */
    private void updateEntityProperties(OMRSMetadataCollection metadataCollection, List<EntityDetail> entitiesToUpdate) throws Exception
    {

        final String methodName = "updateEntityProperties";

        if (entitiesToUpdate != null) {
            InstanceProperties instProps = null;
            try {

                for (int i = 0; i < entitiesToUpdate.size(); i++) {
                    instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), entityDef, i);

                    if (instProps != null) {
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(),
                                entitiesToUpdate.get(i).getGUID(),
                                instProps);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;

                        assertCondition(result != null,
                                A_UPDATE_PROPERTIES,
                                A_UPDATE_PROPERTIES_MSG + testTypeName,
                                PerformanceProfile.ENTITY_UPDATE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_UPDATE_PROPERTIES,
                        A_UPDATE_PROPERTIES_MSG + testTypeName,
                        PerformanceProfile.ENTITY_UPDATE.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "update properties of entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("properties", instProps != null ? instProps.toString() : "null");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
