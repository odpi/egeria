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
 * Test performance of relationship purge operations.
 */
public abstract class TestRelationshipPurge extends OpenMetadataPerformanceTestCase
{

    protected static final String TEST_CASE_ID   = "repository-relationship-purge-performance";
    protected static final String TEST_CASE_NAME = "Repository relationship purge performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType deleted instances of type: ";

    private static final String A_FIND_RELATIONSHIPS_RC     = TEST_CASE_ID + "-findRelationshipsByProperty-rc";
    private static final String A_FIND_RELATIONSHIPS_RC_MSG = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    protected final RelationshipDef     relationshipDef;
    protected String                    testTypeName;

    protected static Map<String, Set<String>> guidsByType = new HashMap<>();
    protected static Map<String, Set<String>> guidsByTypeRC = new HashMap<>();
    protected static OMRSMetadataCollection metadataCollection = null;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     * @param testCaseId unique ID for the test case
     * @param testCaseName name for the test case
     * @throws Exception on any initialization error
     */
    public TestRelationshipPurge(PerformanceWorkPad workPad,
                                 RelationshipDef    relationshipDef,
                                 String             testCaseId,
                                 String             testCaseName) throws Exception
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_PURGE.getProfileId());

        this.relationshipDef = relationshipDef;

        this.testTypeName = this.updateTestIdByType(relationshipDef.getName(),
                testCaseId,
                testCaseName);

        String typeDefName = relationshipDef.getName();
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


}
