/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.retype;

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
 * Test performance of entity retype operations.
 */
public class TestEntityRetype extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-retype-performance";
    private static final String TEST_CASE_NAME = "Repository entity retype performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_REMOVE_PROPERTIES     = TEST_CASE_ID + "-updateEntityProperties-remove";
    private static final String A_REMOVE_PROPERTIES_MSG = "Repository performs removal of all entity properties of instance of type: ";

    private static final String A_RETYPE_SUB     = TEST_CASE_ID + "-reTypeEntity-toSubtype";
    private static final String A_RETYPE_SUB_MSG = "Repository performs retyping of homed instances to each subtype of type: ";

    private static final String A_RETYPE_SUPER     = TEST_CASE_ID + "-reTypeEntity-toSupertype";
    private static final String A_RETYPE_SUPER_MSG = "Repository performs retyping of homed instances to supertype of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityRetype(PerformanceWorkPad workPad,
                            EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_RETYPE.getProfileId());

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

        Set<String> keys = getEntityKeys(metadataCollection, numInstances);
        reTypeEntities(metadataCollection, keys);

        super.setSuccessMessage("Entity retype performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of entity GUIDs for this type.
     *
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances of entities to re-identify
     * @return a set of entity GUIDs to re-identify
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
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getTutMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            List<EntityDetail> entities = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
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
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null);
        }
        return null;
    }

    /**
     * Attempt to retype the entities provided to one of its subtypes, and then back again to its original type.
     * @param metadataCollection through which to call reTypeEntity
     * @param keys GUIDs of instances to retype
     * @throws Exception on any errors
     */
    private void reTypeEntities(OMRSMetadataCollection metadataCollection,
                                Set<String> keys) throws Exception
    {

        final String methodName = "reTypeEntity";

        if (keys != null) {
            OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
            List<String> subTypeNames = repositoryHelper.getSubTypesOf(testCaseId, entityDef.getName());
            if (subTypeNames != null && !subTypeNames.isEmpty()) {
                String subTypeName = subTypeNames.get(0);
                TypeDefSummary targetType = repositoryHelper.getTypeDefByName(testCaseId, subTypeName);
                try {
                    for (String guid : keys) {
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.updateEntityProperties(workPad.getLocalServerUserId(),
                                guid,
                                new InstanceProperties());
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(result != null,
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
                    parameters.put("entityTypeGUID", entityDef.getGUID());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                    throw new Exception(msg, exc);
                }
                try {
                    for (String guid : keys) {
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                guid,
                                entityDef,
                                targetType);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(result != null,
                                A_RETYPE_SUB,
                                A_RETYPE_SUB_MSG + testTypeName,
                                PerformanceProfile.ENTITY_RETYPE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                } catch (Exception exc) {
                    String operationDescription = "retype entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("currentTypeDefSummary", entityDef.getName());
                    parameters.put("newTypeDefSummary", subTypeName);
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                    throw new Exception(msg, exc);
                }
                try {
                    for (String guid : keys) {
                        long start = System.nanoTime();
                        EntityDetail result = metadataCollection.reTypeEntity(workPad.getLocalServerUserId(),
                                guid,
                                targetType,
                                entityDef);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(result != null,
                                A_RETYPE_SUPER,
                                A_RETYPE_SUPER_MSG + testTypeName,
                                PerformanceProfile.ENTITY_RETYPE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                } catch (Exception exc) {
                    String operationDescription = "retype entity of type " + entityDef.getName();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("currentTypeDefSummary", subTypeName);
                    parameters.put("newTypeDefSummary", entityDef.getName());
                    String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                    throw new Exception(msg, exc);
                }
            }
        }

    }

}
