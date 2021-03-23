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
 * Test performance of relationship retype operations.
 */
public class TestRelationshipRetype extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-retype-performance";
    private static final String TEST_CASE_NAME = "Repository relationship retype performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_REMOVE_PROPERTIES     = TEST_CASE_ID + "-updateRelationshipProperties-remove";
    private static final String A_REMOVE_PROPERTIES_MSG = "Repository performs removal of all relationship properties of instance of type: ";

    private static final String A_RETYPE_SUB     = TEST_CASE_ID + "-reTypeRelationship-toSubtype";
    private static final String A_RETYPE_SUB_MSG = "Repository performs retyping of homed instances to each subtype of type: ";

    private static final String A_RETYPE_SUPER     = TEST_CASE_ID + "-reTypeRelationship-toSupertype";
    private static final String A_RETYPE_SUPER_MSG = "Repository performs retyping of homed instances to supertype of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipRetype(PerformanceWorkPad workPad,
                                  RelationshipDef          relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_RETYPE.getProfileId());

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

        Set<String> keys = getRelationshipKeys(metadataCollection, numInstances);
        reTypeRelationships(metadataCollection, keys);

        super.setSuccessMessage("Relationship retype performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a set of relationship GUIDs for this type.
     *
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances of relationships to retype
     * @return a set of relationship GUIDs to retype
     * @throws Exception on any errors
     */
    private Set<String> getRelationshipKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getRelationshipKeys";
        Set<String> keys = new HashSet<>();
        try {
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
            if (relationships != null) {
                keys = relationships.stream().map(Relationship::getGUID).collect(Collectors.toSet());
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null);
        }
        return keys;
    }

    /**
     * Attempt to retype the relationships provided to one of its subtypes, and then back again to its original type.
     * @param metadataCollection through which to call reTypeRelationship
     * @param keys GUIDs of instances to retype
     * @throws Exception on any errors
     */
    private void reTypeRelationships(OMRSMetadataCollection metadataCollection,
                                     Set<String> keys) throws Exception
    {

        final String methodName = "reTypeRelationship";

        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        List<String> subTypeNames = repositoryHelper.getSubTypesOf(testCaseId, relationshipDef.getName());
        if (subTypeNames != null && !subTypeNames.isEmpty()) {
            String subTypeName = subTypeNames.get(0);
            TypeDefSummary targetType = repositoryHelper.getTypeDefByName(testCaseId, subTypeName);
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    Relationship result = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(),
                            guid,
                            new InstanceProperties());
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_REMOVE_PROPERTIES,
                            A_REMOVE_PROPERTIES_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_UPDATE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (Exception exc) {
                String operationDescription = "remove properties of relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("relationshipTypeGUID", relationshipDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    Relationship result = metadataCollection.reTypeRelationship(workPad.getLocalServerUserId(),
                            guid,
                            relationshipDef,
                            targetType);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_RETYPE_SUB,
                            A_RETYPE_SUB_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_RETYPE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (Exception exc) {
                String operationDescription = "retype relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("currentTypeDefSummary", relationshipDef.getName());
                parameters.put("newTypeDefSummary", subTypeName);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
            try {
                for (String guid : keys) {
                    long start = System.nanoTime();
                    Relationship result = metadataCollection.reTypeRelationship(workPad.getLocalServerUserId(),
                            guid,
                            targetType,
                            relationshipDef);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;
                    assertCondition(result != null,
                            A_RETYPE_SUPER,
                            A_RETYPE_SUPER_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_RETYPE.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (Exception exc) {
                String operationDescription = "retype relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("currentTypeDefSummary", subTypeName);
                parameters.put("newTypeDefSummary", relationshipDef.getName());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
