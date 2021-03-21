/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.classify;

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
import java.util.stream.Collectors;


/**
 * Test performance of entity classification operations.
 */
public class TestEntityClassification extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-classification-performance";
    private static final String TEST_CASE_NAME = "Repository entity classification performance test case";

    private static final String A_FIND_ENTITIES        = TEST_CASE_ID + "-findEntities";
    private static final String A_FIND_ENTITIES_MSG    = "Repository performs search for unordered first instancesPerType instances of type: ";

    private static final String A_FIND_RC_ENTITIES     = TEST_CASE_ID + "-findEntitiesByProperty";
    private static final String A_FIND_RC_ENTITIES_MSG = "Repository performs search for unordered first instancesPerType reference copy instances of type: ";

    private static final String A_CLASSIFY     = TEST_CASE_ID + "-classifyEntity";
    private static final String A_CLASSIFY_MSG = "Repository performs classification of instances with: ";

    private static final String A_SAVE_CLASSIFICATION_RC     = TEST_CASE_ID + "-saveClassificationReferenceCopy";
    private static final String A_SAVE_CLASSIFICATION_RC_MSG = "Repository performs reference copy classification of instances with: ";

    private final ClassificationDef   classificationDef;
    private final String              testTypeName;
    private String                    entityTypeName = null;
    private String                    entityTypeGUID = null;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param classificationDef type of valid classifications
     */
    public TestEntityClassification(PerformanceWorkPad workPad,
                                    ClassificationDef  classificationDef)
    {
        super(workPad, PerformanceProfile.ENTITY_CLASSIFICATION.getProfileId());

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
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();
        int numInstances = super.getInstancesPerType();

        classifyEntities(metadataCollection, repositoryHelper, numInstances);
        // TODO: classifyEntities(metadataCollection, repositoryHelper, numInstances); // from external source
        saveClassificationReferenceCopies(metadataCollection, repositoryHelper, numInstances);

        super.setSuccessMessage("Entity classification performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to classify a number of entities.
     * @param metadataCollection through which to call classifyEntity
     * @param repositoryHelper utilities for introspecting the repository's capabilities
     * @param numInstances of times to call classifyEntity
     * @throws Exception on any errors
     */
    private void classifyEntities(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper, int numInstances) throws Exception
    {

        List<TypeDefLink> validEntityDefs = classificationDef.getValidEntityDefs();
        List<TypeDef> knownTypeDefs = repositoryHelper.getKnownTypeDefs();
        List<String> knownTypeDefNames = knownTypeDefs.stream().map(TypeDef::getName).collect(Collectors.toList());

        List<EntityDetail> entitiesToClassify = null;
        if (validEntityDefs != null && !validEntityDefs.isEmpty()) {
            for (TypeDefLink typeDefLink : validEntityDefs) {
                String candidateTypeName = typeDefLink.getName();
                if (knownTypeDefNames.contains(candidateTypeName)) {
                    entityTypeName = candidateTypeName;
                    entityTypeGUID = typeDefLink.getGUID();
                }
            }
        }

        if (entityTypeName != null) {
            long start = System.nanoTime();
            entitiesToClassify = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                    entityTypeGUID,
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    numInstances);
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            assertCondition(entitiesToClassify != null,
                    A_FIND_ENTITIES,
                    A_FIND_ENTITIES_MSG + entityTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntities",
                    elapsedTime);
        }

        if (entitiesToClassify != null) {
            InstanceProperties instProps = null;
            try {

                for (int i = 0; i < entitiesToClassify.size(); i++) {

                    EntityDetail toClassify = entitiesToClassify.get(i);

                    instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), classificationDef, i);

                    long start = System.nanoTime();
                    EntityDetail result = metadataCollection.classifyEntity(workPad.getLocalServerUserId(),
                            toClassify.getGUID(),
                            classificationDef.getName(),
                            instProps);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;

                    assertCondition(result != null,
                            A_CLASSIFY,
                            A_CLASSIFY_MSG + testTypeName,
                            PerformanceProfile.ENTITY_CLASSIFICATION.getProfileId(),
                            null,
                            "classifyEntity",
                            elapsedTime);
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_CLASSIFY,
                        A_CLASSIFY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_CLASSIFICATION.getProfileId(),
                        null);
            } catch (Exception exc) {
                String methodName = "classifyEntity";
                String operationDescription = "classify an entity with " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

    /**
     * Attempt to add a number of new reference copy classifications.
     * @param metadataCollection through which to call saveClassificationReferenceCopy
     * @param repositoryHelper used to quickly construct proper instances
     * @param numInstances of times to call saveClassificationReferenceCopy
     * @throws Exception on any errors
     */
    private void saveClassificationReferenceCopies(OMRSMetadataCollection metadataCollection,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   int numInstances) throws Exception
    {

        final String methodName = "saveClassificationReferenceCopy";

        List<EntityDetail> entitiesToClassify = null;
        if (entityTypeName != null) {
            InstanceProperties byMetadataCollectionId = repositoryHelper.addStringPropertyToInstance(testCaseId,
                    null,
                    "metadataCollectionId",
                    repositoryHelper.getExactMatchRegex(performanceWorkPad.getReferenceCopyMetadataCollectionId()),
                    methodName);
            long start = System.nanoTime();
            entitiesToClassify = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                    entityTypeGUID,
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
            assertCondition(entitiesToClassify != null,
                    A_FIND_RC_ENTITIES,
                    A_FIND_RC_ENTITIES_MSG + entityTypeName,
                    PerformanceProfile.ENTITY_SEARCH.getProfileId(),
                    null,
                    "findEntitiesByProperty",
                    elapsedTime);
        }

        if (entitiesToClassify != null) {
            InstanceProperties instProps = null;
            try {

                for (int i = 0; i < entitiesToClassify.size(); i++) {
                    EntityDetail toClassify = entitiesToClassify.get(i);
                    instProps = super.getAllPropertiesForInstance(workPad.getLocalServerUserId(), classificationDef, i);
                    Classification classification = repositoryHelper.getNewClassification(testCaseId,
                            performanceWorkPad.getReferenceCopyMetadataCollectionId(),
                            InstanceProvenanceType.LOCAL_COHORT,
                            performanceWorkPad.getLocalServerUserId(),
                            classificationDef.getName(),
                            entityTypeName,
                            ClassificationOrigin.ASSIGNED,
                            null,
                            instProps);

                    long start = System.nanoTime();
                    metadataCollection.saveClassificationReferenceCopy(workPad.getLocalServerUserId(), toClassify, classification);
                    long elapsedTime = (System.nanoTime() - start) / 1000000;

                    assertCondition(true,
                            A_SAVE_CLASSIFICATION_RC,
                            A_SAVE_CLASSIFICATION_RC_MSG + testTypeName,
                            PerformanceProfile.ENTITY_CLASSIFICATION.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }

            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_SAVE_CLASSIFICATION_RC,
                        A_SAVE_CLASSIFICATION_RC_MSG + testTypeName,
                        PerformanceProfile.ENTITY_CLASSIFICATION.getProfileId(),
                        null);
            } catch (Exception exc) {
                String operationDescription = "classify a reference copy entity with " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("initialProperties", instProps != null ? instProps.toString() : "null");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

}
