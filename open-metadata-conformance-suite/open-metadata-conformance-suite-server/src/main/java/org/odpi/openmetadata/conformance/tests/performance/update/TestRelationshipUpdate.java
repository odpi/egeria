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
 * Test performance of relationship update operations.
 */
public class TestRelationshipUpdate extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-update-performance";
    private static final String TEST_CASE_NAME = "Repository relationship update performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationshipsByProperty";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType homed instances of type: ";

    private static final String A_UPDATE_PROPERTIES     = TEST_CASE_ID + "-updateRelationshipProperties";
    private static final String A_UPDATE_PROPERTIES_MSG = "Repository performs update of properties on instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipUpdate(PerformanceWorkPad workPad,
                                  RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_UPDATE.getProfileId());

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

        List<Relationship> relationshipsToUpdate = getRelationshipsToUpdate(metadataCollection, numInstances);
        // TODO: updateRelationshipStatus(metadataCollection, numInstances);
        updateRelationshipProperties(metadataCollection, relationshipsToUpdate);

        super.setSuccessMessage("Relationship update performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of relationships that are homed in the technology under test's repository.
     * @param metadataCollection through which to call findRelationshipsByProperty
     * @param numInstances number of instances to retrieve
     * @return list of instances homed in the technology under test's repository
     * @throws Exception on any errors
     */
    private List<Relationship> getRelationshipsToUpdate(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        final String methodName = "getRelationshipsToUpdate";
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef(testCaseId, relationshipDef, methodName);
        if (properties != null && !properties.isEmpty()) {
            InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                    null,
                    "metadataCollectionId",
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getTutMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            List<Relationship> relationshipsToUpdate = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
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
            assertCondition(relationshipsToUpdate != null,
                    A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null,
                    "findRelationshipsByProperty",
                    elapsedTime);
            return relationshipsToUpdate;
        }
        return null;
    }

    /**
     * Attempt to update a number of existing relationships.
     * @param metadataCollection through which to call updateRelationshipProperties
     * @param relationshipsToUpdate the relationships to update
     * @throws Exception on any errors
     */
    private void updateRelationshipProperties(OMRSMetadataCollection metadataCollection, List<Relationship> relationshipsToUpdate) throws Exception
    {

        final String methodName = "updateRelationshipProperties";

        if (relationshipsToUpdate != null) {
            InstanceProperties instProps = null;
            try {

                for (int i = 0; i < relationshipsToUpdate.size(); i++) {

                    instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), relationshipDef, i);
                    if (instProps != null) {
                        long start = System.nanoTime();
                        Relationship result = metadataCollection.updateRelationshipProperties(workPad.getLocalServerUserId(),
                                relationshipsToUpdate.get(i).getGUID(),
                                instProps);
                        long elapsedTime = (System.nanoTime() - start) / 1000000;
                        assertCondition(result != null,
                                A_UPDATE_PROPERTIES,
                                A_UPDATE_PROPERTIES_MSG + testTypeName,
                                PerformanceProfile.RELATIONSHIP_UPDATE.getProfileId(),
                                null,
                                methodName,
                                elapsedTime);
                    }
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_UPDATE_PROPERTIES,
                        A_UPDATE_PROPERTIES_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_UPDATE.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "update properties on relationship of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("properties", instProps != null ? instProps.toString() : "null");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
