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
public abstract class TestEntityRetype extends OpenMetadataPerformanceTestCase
{

    protected static final String TEST_CASE_ID   = "repository-entity-retype-performance";
    protected static final String TEST_CASE_NAME = "Repository entity retype performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    protected final EntityDef           entityDef;
    protected String                    testTypeName;

    protected static OMRSMetadataCollection metadataCollection = null;
    protected static Map<String, Set<String>> guidsByType = new HashMap<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @param testCaseId unique ID for the test case
     * @param testCaseName name for the test case
     * @throws Exception on any initialization error
     */
    public TestEntityRetype(PerformanceWorkPad workPad,
                            EntityDef          entityDef,
                            String             testCaseId,
                            String             testCaseName) throws Exception
    {
        super(workPad, PerformanceProfile.ENTITY_RETYPE.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                testCaseId,
                testCaseName);

        String typeDefName = entityDef.getName();
        if (guidsByType.get(typeDefName) == null)
        {
            metadataCollection = super.getMetadataCollection();
            guidsByType.put(typeDefName, getEntityKeys(metadataCollection, super.getInstancesPerType()));
        }

    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected abstract void run() throws Exception;

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


}
