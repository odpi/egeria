/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.search;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of entity search operations by classification.
 */
public class TestClassificationSearch extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-classification-search-performance";
    private static final String TEST_CASE_NAME = "Repository classification search performance test case";

    private static final String A_CLASSIFICATION_ALONE     = TEST_CASE_ID + "-findEntitiesByClassification-alone";
    private static final String A_CLASSIFICATION_ALONE_MSG = "Repository performs search by classification alone (no properties), sorting by most recent creation date, of first page of instances for classification: ";

    private static final String A_FIND_BY_PROPERTY_ONE     = TEST_CASE_ID + "-findEntitiesByClassification-one";
    private static final String A_FIND_BY_PROPERTY_ONE_MSG = "Repository performs search by a single classification property value, sorting by oldest creation date, of first page of instances for classification: ";

    private static final String A_FIND_BY_PROPERTY_ALL     = TEST_CASE_ID + "-findEntitiesByClassification-all";
    private static final String A_FIND_BY_PROPERTY_ALL_MSG = "Repository performs search for both of two classification property values, sorting by most recent update date, of first page of instances for classification: ";

    private static final String A_FIND_BY_PROPERTY_ANY     = TEST_CASE_ID + "-findEntitiesByClassification-any";
    private static final String A_FIND_BY_PROPERTY_ANY_MSG = "Repository performs search for either of two classification property values, sorting by oldest update date, of first page of instances for classification: ";

    private static final String A_FIND_BY_PROPERTY_NONE     = TEST_CASE_ID + "-findEntitiesByClassification-none";
    private static final String A_FIND_BY_PROPERTY_NONE_MSG = "Repository performs search for neither of two classification property values, sorting by entity GUID, of first page of instances for classification: ";

    private final ClassificationDef   classificationDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param classificationDef type of valid classifications
     */
    public TestClassificationSearch(PerformanceWorkPad workPad,
                                    ClassificationDef  classificationDef)
    {
        super(workPad, PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId());

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

        findEntitiesByClassification(metadataCollection, repositoryHelper);

        super.setSuccessMessage("Classification search performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to find entities based on matching a classification.
     *
     * @param metadataCollection through which to run findEntitiesByClassification
     * @param repositoryHelper utilities for introspecting properties
     * @throws Exception on any error
     */
    private void findEntitiesByClassification(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper) throws Exception
    {

        final String methodName = "findEntitiesByClassification";

        // Take the first two properties...
        PrimitivePropertyValue oneMatch = null;
        String oneName = null;
        PrimitivePropertyValue twoMatch = null;
        String twoName = null;
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef("performance-workbench", classificationDef, methodName);
        for (TypeDefAttribute property : properties) {
            if (property.getAttributeType().getCategory().equals(AttributeTypeDefCategory.PRIMITIVE)) {
                PrimitiveDef attribute = (PrimitiveDef) property.getAttributeType();
                String propertyNameToSearch = property.getAttributeName();
                PrimitivePropertyValue candidate = getPrimitivePropertyValue(propertyNameToSearch, attribute, property.isUnique(), 0);
                if (attribute.getPrimitiveDefCategory().equals(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)) {
                    candidate.setPrimitiveValue(repositoryHelper.getExactMatchRegex(candidate.valueAsString()));
                }
                if (oneMatch == null) {
                    oneMatch = candidate;
                    oneName = propertyNameToSearch;
                } else {
                    twoMatch = candidate;
                    twoName = propertyNameToSearch;
                }
            }
            if (oneMatch != null && twoMatch != null)
                break;
        }

        Map<String, InstancePropertyValue> one = new HashMap<>();
        one.put(oneName, oneMatch);
        InstanceProperties oneProperty = new InstanceProperties();
        oneProperty.setInstanceProperties(one);

        Map<String, InstancePropertyValue> two = new HashMap<>();
        two.put(oneName, oneMatch);
        two.put(twoName, twoMatch);
        InstanceProperties twoProperties = new InstanceProperties();
        twoProperties.setInstanceProperties(two);

        // Run an initial search based simply on the classification being present (no properties matching)
        try {
            long start = System.nanoTime();
            List<EntityDetail> results = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                    null,
                    classificationDef.getName(),
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    SequencingOrder.CREATION_DATE_RECENT,
                    performanceWorkPad.getMaxSearchResults());
            long elapsedTime = (System.nanoTime() - start) / 1000000;
            if (results != null && !results.isEmpty()) {
                assertCondition(true,
                        A_CLASSIFICATION_ALONE,
                        A_CLASSIFICATION_ALONE_MSG + testTypeName,
                        PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }
        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_CLASSIFICATION_ALONE,
                    A_CLASSIFICATION_ALONE_MSG + testTypeName,
                    PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String operationDescription = "search entities with classification " + classificationDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", classificationDef.getGUID());
            parameters.put("matchProperties", "null");
            parameters.put("matchCriteria", "null");
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

        // There must be at least one property to attempt to match against to proceed with this test
        if (oneMatch != null) {
            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                        null,
                        classificationDef.getName(),
                        oneProperty,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.CREATION_DATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ONE,
                            A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                            PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ONE,
                        A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                        PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search entities with classification " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("matchProperties", oneProperty.toString());
                parameters.put("matchCriteria", "ALL");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

        // There must be at least two properties to attempt to match against to proceed with these tests
        if (twoMatch != null) {
            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                        null,
                        classificationDef.getName(),
                        twoProperties,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.LAST_UPDATE_RECENT,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ALL,
                            A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                            PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ALL,
                        A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                        PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search entities with classification " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ALL");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                        null,
                        classificationDef.getName(),
                        twoProperties,
                        MatchCriteria.ANY,
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.LAST_UPDATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ANY,
                            A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                            PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ANY,
                        A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                        PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search entities with classification " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ANY");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByClassification(workPad.getLocalServerUserId(),
                        null,
                        classificationDef.getName(),
                        twoProperties,
                        MatchCriteria.NONE,
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.GUID,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_NONE,
                            A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                            PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_NONE,
                        A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                        PerformanceProfile.CLASSIFICATION_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search entities with classification " + classificationDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", classificationDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "NONE");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

        }

    }

}
