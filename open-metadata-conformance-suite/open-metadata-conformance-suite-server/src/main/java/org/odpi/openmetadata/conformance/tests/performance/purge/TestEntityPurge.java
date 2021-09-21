/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.purge;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of entity purge operations.
 */
public abstract class TestEntityPurge extends OpenMetadataPerformanceTestCase
{

    protected static final String TEST_CASE_ID   = "repository-entity-purge-performance";
    protected static final String TEST_CASE_NAME = "Repository entity purge performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_FIND_ENTITIES_RC     = TEST_CASE_ID + "-findEntitiesByProperty-rc";
    private static final String A_FIND_ENTITIES_RC_MSG = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    protected final EntityDef           entityDef;
    protected String                    testTypeName;

    protected static Map<String, Set<String>> guidsByType = new HashMap<>();
    protected static Map<String, Set<String>> guidsByTypeRC = new HashMap<>();
    protected static OMRSMetadataCollection metadataCollection = null;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @param testCaseId unique ID for the test case
     * @param testCaseName name for the test case
     * @throws Exception on any initialization error
     */
    public TestEntityPurge(PerformanceWorkPad workPad,
                           EntityDef          entityDef,
                           String             testCaseId,
                           String             testCaseName) throws Exception
    {
        super(workPad, PerformanceProfile.ENTITY_PURGE.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                testCaseId,
                testCaseName);

        String typeDefName = entityDef.getName();
        int numInstances = super.getInstancesPerType();
        if (metadataCollection == null)
        {
            metadataCollection = super.getMetadataCollection();
        }
        if (guidsByType.get(typeDefName) == null)
        {
            guidsByType.put(typeDefName, getKeys(metadataCollection, numInstances));
        }
        if (guidsByTypeRC.get(typeDefName) == null)
        {
            guidsByTypeRC.put(typeDefName, getReferenceCopyKeys(metadataCollection, numInstances));
        }
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected abstract void run() throws Exception;


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
        return null;
    }

    /**
     * Retrieve a list of reference copy entities.
     * @param metadataCollection through which to call findEntitiesByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs that are reference copies
     * @throws Exception on any errors
     */
    private Set<String> getReferenceCopyKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getReferenceCopyKeys";
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                null,
                "metadataCollectionId",
                repositoryHelper.getExactMatchRegex(performanceWorkPad.getReferenceCopyMetadataCollectionId()),
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
                    A_FIND_ENTITIES_RC,
                    A_FIND_ENTITIES_RC_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntitiesByProperty",
                    elapsedTime);
            return entities.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
        }
        return null;
    }


}
