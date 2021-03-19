/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.classify;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Test performance of entity declassification operations.
 */
public class TestEntityDeclassification extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-declassification-performance";
    private static final String TEST_CASE_NAME = "Repository entity declassification performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntitiesByClassification";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType instances with classification: ";

    private static final String A_DECLASSIFY     = TEST_CASE_ID + "-classifyEntity";
    private static final String A_DECLASSIFY_MSG = "Repository performs declassification of instances with: ";

    private final ClassificationDef   classificationDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param classificationDef type of valid classifications
     */
    public TestEntityDeclassification(PerformanceWorkPad workPad,
                                      ClassificationDef  classificationDef)
    {
        super(workPad, PerformanceProfile.ENTITY_DECLASSIFY.getProfileId());

        this.classificationDef = classificationDef;

        this.testTypeName = this.updateTestIdByType(classificationDef.getName(),
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

        Set<String> keys = getEntityKeys(metadataCollection, numInstances);
        declassifyEntities(metadataCollection, keys);

        super.setSuccessMessage("Entity declassification performance tests complete for: " + testTypeName);
    }

    /**
     * Retrieve a list of entity GUIDs that have this classification.
     * @param metadataCollection through which to call findEntitiesByClassification
     * @param numInstances number of instances to retrieve
     * @return set of entity GUIDs that have this classification
     * @throws Exception on any errors
     */
    private Set<String> getEntityKeys(OMRSMetadataCollection metadataCollection, int numInstances) throws Exception
    {
        long start = System.nanoTime();
        List<EntityDetail> entitiesWithClassification = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                null,
                classificationDef.getName(),
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                numInstances);
        long elapsedTime = (System.nanoTime() - start) / 1000000;
        if (entitiesWithClassification != null) {
            assertCondition(true,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + testTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntitiesByClassification",
                    elapsedTime);
            return entitiesWithClassification.stream().map(EntityDetail::getGUID).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * Attempt to declassify a number of entities.
     * @param metadataCollection through which to call declassifyEntity
     * @param keys GUIDs of entities with this classification
     * @throws Exception on any errors
     */
    private void declassifyEntities(OMRSMetadataCollection metadataCollection, Set<String> keys) throws Exception
    {

        final String methodName = "declassifyEntity";
        try {

            for (String guid : keys) {
                long start = System.nanoTime();
                EntityDetail result = metadataCollection.declassifyEntity(workPad.getLocalServerUserId(),
                        guid,
                        classificationDef.getName());
                long elapsedTime = (System.nanoTime() - start) / 1000000;

                assertCondition(result != null,
                        A_DECLASSIFY,
                        A_DECLASSIFY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_DECLASSIFY.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_DECLASSIFY,
                    A_DECLASSIFY_MSG + testTypeName,
                    PerformanceProfile.ENTITY_DECLASSIFY.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String operationDescription = "declassify an entity with " + classificationDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", classificationDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

}
