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
 * Test performance of entity undo operations.
 */
public class TestEntityUndo extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-undo-performance";
    private static final String TEST_CASE_NAME = "Repository entity undo performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType homed instances, with a version greater than 1, of type: ";

    private static final String A_UNDO     = TEST_CASE_ID + "-undoEntityUpdate";
    private static final String A_UNDO_MSG = "Repository performs undo of last update to instance of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestEntityUndo(PerformanceWorkPad workPad,
                          EntityDef          entityDef)
    {
        super(workPad, PerformanceProfile.ENTITY_UNDO.getProfileId());

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

        undoEntityUpdate(metadataCollection, numInstances);

        super.setSuccessMessage("Entity undo performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to undo a number of entities' updates.
     * @param metadataCollection through which to call undoEntityUpdate
     * @param numInstances of times to call undoEntityUpdate
     * @throws Exception on any errors
     */
    private void undoEntityUpdate(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {

        final String methodName = "undoEntityUpdate";

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
        List<EntityDetail> entitiesToUndo = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                null,
                searchProperties,
                0,
                null,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;

        if (entitiesToUndo != null) {

            assertCondition(true,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityDef.getName(),
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);

            try {
                for (EntityDetail entityDetail : entitiesToUndo) {

                    start = System.nanoTime();
                    EntityDetail result = metadataCollection.undoEntityUpdate(workPad.getLocalServerUserId(),
                            entityDetail.getGUID());
                    elapsedTime = (System.nanoTime() - start) / 1000000;

                    assertCondition(result != null,
                            A_UNDO,
                            A_UNDO_MSG + testTypeName,
                            PerformanceProfile.ENTITY_UNDO.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_UNDO,
                        A_UNDO_MSG + testTypeName,
                        PerformanceProfile.ENTITY_UNDO.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "undo an entity update of type: " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
