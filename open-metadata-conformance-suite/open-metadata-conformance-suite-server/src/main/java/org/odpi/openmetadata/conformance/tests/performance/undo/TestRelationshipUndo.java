/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.undo;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of relationship undo operations.
 */
public class TestRelationshipUndo extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-undo-performance";
    private static final String TEST_CASE_NAME = "Repository relationship undo performance test case";

    private static final String A_FIND_RELATIONSHIPS        = TEST_CASE_ID + "-findRelationships";
    private static final String A_FIND_RELATIONSHIPS_MSG    = "Repository performs search for unordered first instancesPerType homed instances, with a version greater than 1, of type: ";

    private static final String A_UNDO     = TEST_CASE_ID + "-undoRelationshipUpdate";
    private static final String A_UNDO_MSG = "Repository performs undo of last update to instance of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipUndo(PerformanceWorkPad workPad,
                                RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_UNDO.getProfileId());

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

        undoRelationshipUpdate(metadataCollection, numInstances);

        super.setSuccessMessage("Relationship undo performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to undo a number of relationships' updates.
     * @param metadataCollection through which to call undoRelationshipUpdate
     * @param numInstances of times to call undoRelationshipUpdate
     * @throws Exception on any errors
     */
    private void undoRelationshipUpdate(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {

        final String methodName = "undoRelationshipUpdate";

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        List<PropertyCondition> conditions = new ArrayList<>();
        PropertyCondition byVersion = new PropertyCondition();
        byVersion.setProperty("version");
        byVersion.setOperator(PropertyComparisonOperator.GT);
        PrimitivePropertyValue ppv = new PrimitivePropertyValue();
        ppv.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
        ppv.setPrimitiveValue(1);
        byVersion.setValue(ppv);
        conditions.add(byVersion);
        PropertyCondition byMetadataCollectionId = new PropertyCondition();
        byMetadataCollectionId.setProperty("metadataCollectionId");
        byMetadataCollectionId.setOperator(PropertyComparisonOperator.EQ);
        PrimitivePropertyValue ppvMetadataCollection = new PrimitivePropertyValue();
        ppvMetadataCollection.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        ppvMetadataCollection.setPrimitiveValue(performanceWorkPad.getTutMetadataCollectionId());
        byMetadataCollectionId.setValue(ppvMetadataCollection);
        conditions.add(byMetadataCollectionId);
        searchProperties.setConditions(conditions);

        long start = System.nanoTime();
        List<Relationship> relationshipsToUndo = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                relationshipDef.getGUID(),
                null,
                searchProperties,
                0,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;

        if (relationshipsToUndo != null) {

            assertCondition(true,
                    A_FIND_RELATIONSHIPS,
                    A_FIND_RELATIONSHIPS_MSG + relationshipDef.getName(),
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null,
                    "findRelationships",
                    elapsedTime);

            try {
                for (Relationship relationship : relationshipsToUndo) {

                    start = System.nanoTime();
                    Relationship result = metadataCollection.undoRelationshipUpdate(workPad.getLocalServerUserId(),
                            relationship.getGUID());
                    elapsedTime = (System.nanoTime() - start) / 1000000;

                    assertCondition(result != null,
                            A_UNDO,
                            A_UNDO_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_UNDO.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_UNDO,
                        A_UNDO_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_UNDO.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "undo a relationship update of type: " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
