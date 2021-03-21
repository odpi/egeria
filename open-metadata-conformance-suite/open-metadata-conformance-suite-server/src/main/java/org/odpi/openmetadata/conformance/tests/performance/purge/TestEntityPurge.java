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
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Test performance of entity purge operations.
 */
public class TestEntityPurge extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-purge-performance";
    private static final String TEST_CASE_NAME = "Repository entity purge performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_FIND_ENTITIES_RC     = TEST_CASE_ID + "-findEntitiesByProperty-rc";
    private static final String A_FIND_ENTITIES_RC_MSG = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteEntity";
    private static final String A_DELETE_MSG = "Repository delete of instances of type: ";

    private static final String A_PURGE     = TEST_CASE_ID + "-purgeEntity";
    private static final String A_PURGE_MSG = "Repository purge of instances of type: ";

    private static final String A_PURGE_RC     = TEST_CASE_ID + "-purgeEntityReferenceCopy";
    private static final String A_PURGE_RC_MSG = "Repository purge of reference copy instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityPurge(PerformanceWorkPad workPad,
                           EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_PURGE.getProfileId());

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
        purgeEntities(metadataCollection, keys);
        Set<String> rcKeys = getReferenceCopyKeys(metadataCollection, numInstances);
        purgeEntityReferenceCopies(metadataCollection, rcKeys);

        super.setSuccessMessage("Entity purge performance tests complete for: " + testTypeName);
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

    /**
     * Attempt to purge a number of deleted entities.
     * @param metadataCollection through which to call purgeEntity
     * @param keys GUIDs of entities to purge
     * @throws Exception on any errors
     */
    private void purgeEntities(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "purgeEntity";

        if (keys != null) {
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                            entityDef.getGUID(),
                            entityDef.getName(),
                            guid);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(true,
                            A_PURGE,
                            A_PURGE_MSG + testTypeName,
                            PerformanceProfile.ENTITY_PURGE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_PURGE,
                        A_PURGE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_PURGE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "purge entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

    /**
     * Attempt to purge a number of reference copy entities.
     * @param metadataCollection through which to call purgeEntityReferenceCopy
     * @param keys GUIDs of entities to purge
     * @throws Exception on any errors
     */
    private void purgeEntityReferenceCopies(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "purgeEntityReferenceCopy";

        if (keys != null) {
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    metadataCollection.purgeEntityReferenceCopy(workPad.getLocalServerUserId(),
                            guid,
                            entityDef.getGUID(),
                            entityDef.getName(),
                            performanceWorkPad.getReferenceCopyMetadataCollectionId());
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(true,
                            A_PURGE_RC,
                            A_PURGE_RC_MSG + testTypeName,
                            PerformanceProfile.ENTITY_PURGE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_PURGE_RC,
                        A_PURGE_RC_MSG + testTypeName,
                        PerformanceProfile.ENTITY_PURGE.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "purge reference copy entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
