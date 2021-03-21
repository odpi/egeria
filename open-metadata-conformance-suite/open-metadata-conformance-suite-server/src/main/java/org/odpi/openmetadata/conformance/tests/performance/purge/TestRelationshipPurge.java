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
 * Test performance of relationship purge operations.
 */
public class TestRelationshipPurge extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-purge-performance";
    private static final String TEST_CASE_NAME = "Repository relationship purge performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_FIND_RELATIONSHIPS_RC     = TEST_CASE_ID + "-findRelationshipsByProperty-rc";
    private static final String A_FIND_RELATIONSHIPS_RC_MSG = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    private static final String A_DELETE     = TEST_CASE_ID + "-deleteRelationship";
    private static final String A_DELETE_MSG = "Repository performs delete of instances of type: ";

    private static final String A_PURGE     = TEST_CASE_ID + "-purgeRelationship";
    private static final String A_PURGE_MSG = "Repository purge of instances of type: ";

    private static final String A_PURGE_RC     = TEST_CASE_ID + "-purgeRelationshipReferenceCopy";
    private static final String A_PURGE_RC_MSG = "Repository purge of reference copy instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipPurge(PerformanceWorkPad workPad,
                                 RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_PURGE.getProfileId());

        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
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
        deleteRelationships(metadataCollection, keys);
        purgeRelationships(metadataCollection, keys);
        Set<String> rcKeys = getReferenceCopyKeys(metadataCollection, numInstances);
        purgeRelationshipReferenceCopies(metadataCollection, rcKeys);

        super.setSuccessMessage("Relationship purge performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of relationships that are homed in the technology under test's repository.
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances number of instances to retrieve
     * @return set of GUIDs of relationships to delete
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
        List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                byMetadataCollectionId,
                MatchCriteria.ALL,
                0,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;
        assertCondition(relationships != null,
                A_FIND_RELATIONSHIPS,
                A_FIND_RELATIONSHIPS_MSG + testTypeName,
                PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                null,
                "findRelationshipsByProperty",
                elapsedTime);
        return relationships == null ? null : relationships.stream().map(Relationship::getGUID).collect(Collectors.toSet());
    }

    /**
     * Retrieve a list of reference copy relationships.
     * @param metadataCollection through which to call findRelationshipsByProperty
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
        List<Relationship> relationships = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                byMetadataCollectionId,
                MatchCriteria.ALL,
                0,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;
        assertCondition(relationships != null,
                A_FIND_RELATIONSHIPS_RC,
                A_FIND_RELATIONSHIPS_RC_MSG + testTypeName,
                PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                null,
                "findRelationshipsByProperty",
                elapsedTime);
        return relationships == null ? null : relationships.stream().map(Relationship::getGUID).collect(Collectors.toSet());
    }

    /**
     * Attempt to delete a number of existing relationships.
     * @param metadataCollection through which to call deleteRelationship
     * @param keys GUIDs of relationships to delete
     * @throws Exception on any errors
     */
    private void deleteRelationships(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "deleteRelationship";

        try {

            for (String guid : keys) {
                long start = System.nanoTime();
                Relationship result = metadataCollection.deleteRelationship(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(result != null,
                        A_DELETE,
                        A_DELETE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_DELETE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_DELETE,
                    A_DELETE_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_DELETE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "delete relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to purge a number of deleted relationships.
     * @param metadataCollection through which to call purgeRelationship
     * @param keys GUIDs of relationships to purge
     * @throws Exception on any errors
     */
    private void purgeRelationships(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "purgeRelationship";

        try {

            for (String guid : keys) {
                long start = System.nanoTime();
                metadataCollection.purgeRelationship(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        guid);
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(true,
                        A_PURGE,
                        A_PURGE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_PURGE,
                    A_PURGE_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "purge deleted relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to purge a number of reference copy relationships.
     * @param metadataCollection through which to call purgeRelationshipReferenceCopy
     * @param keys GUIDs of relationships to purge
     * @throws Exception on any errors
     */
    private void purgeRelationshipReferenceCopies(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "purgeRelationshipReferenceCopy";

        try {
            for (String guid : keys) {
                long start = System.nanoTime();
                metadataCollection.purgeRelationshipReferenceCopy(workPad.getLocalServerUserId(),
                        guid,
                        relationshipDef.getGUID(),
                        relationshipDef.getName(),
                        performanceWorkPad.getReferenceCopyMetadataCollectionId());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                assertCondition(true,
                        A_PURGE_RC,
                        A_PURGE_RC_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_PURGE_RC,
                    A_PURGE_RC_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_PURGE.getProfileId(),
                    null);
        } catch (Exception exc) {
            String operationDescription = "purge reference copy relationship of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
