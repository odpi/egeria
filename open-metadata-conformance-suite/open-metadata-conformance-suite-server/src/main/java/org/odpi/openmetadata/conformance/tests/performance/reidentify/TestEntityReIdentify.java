/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.reidentify;

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
 * Test performance of entity re-identify operations.
 */
public class TestEntityReIdentify extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-re-identify-performance";
    private static final String TEST_CASE_NAME = "Repository entity re-identify performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_RE_IDENTIFY     = TEST_CASE_ID + "-reIdentifyEntity";
    private static final String A_RE_IDENTIFY_MSG = "Repository performs re-identification of homed instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityReIdentify(PerformanceWorkPad workPad,
                                EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_RE_IDENTIFY.getProfileId());

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

        Set<String> keysToReIdentify = getEntityKeys(metadataCollection, numInstances);
        reIdentifyEntities(metadataCollection, keysToReIdentify);

        super.setSuccessMessage("Entity re-identify performance tests complete for: " + testTypeName);
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
     * Attempt to re-identify the entities provided to use a new GUID.
     * @param metadataCollection through which to call reIdentifyEntity
     * @param keys GUIDs of instances to re-identify
     * @throws Exception on any errors
     */
    private void reIdentifyEntities(OMRSMetadataCollection metadataCollection,
                                    Set<String> keys) throws Exception
    {

        final String methodName = "reIdentifyEntity";

        if (keys != null) {
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    EntityDetail result = metadataCollection.reIdentifyEntity(workPad.getLocalServerUserId(),
                            entityDef.getGUID(),
                            entityDef.getName(),
                            guid,
                            UUID.randomUUID().toString());
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_RE_IDENTIFY,
                            A_RE_IDENTIFY_MSG + testTypeName,
                            PerformanceProfile.ENTITY_RE_IDENTIFY.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (Exception exc) {
                String operationDescription = "re-identify entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeDefGUID", entityDef.getGUID());
                parameters.put("typeDefName", entityDef.getName());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
