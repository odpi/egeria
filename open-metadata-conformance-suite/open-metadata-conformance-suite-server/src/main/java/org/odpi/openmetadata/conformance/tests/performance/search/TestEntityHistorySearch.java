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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of entity search operations against history.
 */
public class TestEntityHistorySearch extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-entity-search-history-performance";
    private static final String TEST_CASE_NAME = "Repository entity search performance test case against history";

    private static final String A_FIND_ENTITIES_ALL     = TEST_CASE_ID + "-findEntities-all";
    private static final String A_FIND_ENTITIES_ALL_MSG = "Repository performs historical search of all instances, sorting by GUID and paging all results, of type: ";

    private static final String A_FIND_BY_TEXT_EXACT     = TEST_CASE_ID + "-findEntitiesByPropertyValue-exact";
    private static final String A_FIND_BY_TEXT_EXACT_MSG = "Repository performs historical search with an exact text value, sorting by oldest creation time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_START     = TEST_CASE_ID + "-findEntitiesByPropertyValue-start";
    private static final String A_FIND_BY_TEXT_START_MSG = "Repository performs historical search with a text value 'starts-with', sorting by newest creation time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_CONTAINS     = TEST_CASE_ID + "-findEntitiesByPropertyValue-contains";
    private static final String A_FIND_BY_TEXT_CONTAINS_MSG = "Repository performs historical search with a text value 'contains', sorting by oldest update time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_END       = TEST_CASE_ID + "-findEntitiesByPropertyValue-end";
    private static final String A_FIND_BY_TEXT_END_MSG   = "Repository performs historical search with a text value 'ends-with', sorting by newest update time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_REGEX     = TEST_CASE_ID + "-findEntitiesByPropertyValue-regex";
    private static final String A_FIND_BY_TEXT_REGEX_MSG = "Repository performs historical search with a regular expression text value, sorting by GUID, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ONE     = TEST_CASE_ID + "-findEntitiesByProperty-one";
    private static final String A_FIND_BY_PROPERTY_ONE_MSG = "Repository performs historical search by a single property value, sorting by that property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ALL     = TEST_CASE_ID + "-findEntitiesByProperty-all";
    private static final String A_FIND_BY_PROPERTY_ALL_MSG = "Repository performs historical search by two property values, sorting by the second property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ANY     = TEST_CASE_ID + "-findEntitiesByProperty-any";
    private static final String A_FIND_BY_PROPERTY_ANY_MSG = "Repository performs historical search by two property values, sorting by the second property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_NONE     = TEST_CASE_ID + "-findEntitiesByProperty-none";
    private static final String A_FIND_BY_PROPERTY_NONE_MSG = "Repository performs historical search by two property value, sorting by the first property, of first page of instances of type: ";

    private final EntityDef           entityDef;
    private final String              testTypeName;
    private final Date                asOfTime;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     * @param asOfTime at which to search history
     */
    public TestEntityHistorySearch(PerformanceWorkPad workPad,
                                   EntityDef          entityDef,
                                   Date               asOfTime)
    {
        super(workPad, PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId());

        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                TEST_CASE_ID,
                TEST_CASE_NAME);

        this.asOfTime = asOfTime;
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

        findEntities(metadataCollection);
        findEntitiesByPropertyValue(metadataCollection, repositoryHelper);
        findEntitiesByProperty(metadataCollection, repositoryHelper);

        super.setSuccessMessage("Entity historical search performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to find all entities based solely on the typeDef GUID.
     *
     * @param metadataCollection through which to run findEntities
     * @throws Exception on any error
     */
    private void findEntities(OMRSMetadataCollection metadataCollection) throws Exception
    {

        final String methodName = "findEntities";

        try {

            int fromElement = 0;
            long start = System.nanoTime();
            List<EntityDetail> results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                    entityDef.getGUID(),
                    null,
                    null,
                    fromElement,
                    null,
                    null,
                    asOfTime,
                    null,
                    SequencingOrder.GUID,
                    performanceWorkPad.getMaxSearchResults());
            long elapsedTime = (System.nanoTime() - start) / 1000000;

            int page = 1;
            if (results != null && !results.isEmpty()) {
                assertCondition(true,
                        A_FIND_ENTITIES_ALL + "-p" + page,
                        A_FIND_ENTITIES_ALL_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

            // Page the results to test paging performance + tally a running count of the total number of
            // instances in the environment (do this here rather than via create tests so that such counts also work
            // for repositories that do not support write operations)
            while (results != null && !results.isEmpty()) {
                fromElement = fromElement + results.size();
                start = System.nanoTime();
                results = metadataCollection.findEntities(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        null,
                        null,
                        fromElement,
                        null,
                        null,
                        null,
                        null,
                        SequencingOrder.GUID,
                        performanceWorkPad.getMaxSearchResults());
                elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    page += 1;
                    assertCondition(true,
                            A_FIND_ENTITIES_ALL + "-p" + page,
                            A_FIND_ENTITIES_ALL_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_ENTITIES_ALL,
                    A_FIND_ENTITIES_ALL + testTypeName,
                    PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String operationDescription = "search an entity of type " + entityDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", entityDef.getGUID());
            parameters.put("asOfTime", asOfTime.toString());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to find entities based on various textual value matches.
     *
     * @param metadataCollection through which to run findEntities
     * @param repositoryHelper utilities for introspecting properties and generating regular expressions
     * @throws Exception on any error
     */
    private void findEntitiesByPropertyValue(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper) throws Exception
    {

        final String methodName = "findEntitiesByPropertyValue";

        // Start by finding the first string-based property that exists in this type definition, and whose name is at
        // least 3 characters long
        String stringPropertyName = null;
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef("performance-workbench", entityDef, methodName);
        for (TypeDefAttribute property : properties) {
            if (property.getAttributeType().getCategory().equals(AttributeTypeDefCategory.PRIMITIVE)
                    && ((PrimitiveDef) property.getAttributeType()).getPrimitiveDefCategory().equals(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING) ) {
                if (property.getAttributeName().length() > 2) {
                    stringPropertyName = property.getAttributeName();
                    break;
                }
            }
        }

        // Create variations of this string to search against (can only run this method if there is at least a single
        // string property to search against)
        if (stringPropertyName != null) {

            String exact = stringPropertyName;
            String startsWith = stringPropertyName.substring(0, exact.length() - 1);
            String endsWith = stringPropertyName.substring(1);
            String contains = startsWith.substring(1);
            String regex = startsWith + ".*";

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        repositoryHelper.getExactMatchRegex(exact),
                        0,
                        null,
                        null,
                        asOfTime,
                        null,
                        SequencingOrder.CREATION_DATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_EXACT,
                            A_FIND_BY_TEXT_EXACT_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_EXACT,
                        A_FIND_BY_TEXT_EXACT_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getExactMatchRegex(exact));
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        repositoryHelper.getStartsWithRegex(startsWith),
                        0,
                        null,
                        null,
                        asOfTime,
                        null,
                        SequencingOrder.CREATION_DATE_RECENT,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_START,
                            A_FIND_BY_TEXT_START_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_START,
                        A_FIND_BY_TEXT_START_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getStartsWithRegex(startsWith));
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        repositoryHelper.getContainsRegex(contains),
                        0,
                        null,
                        null,
                        asOfTime,
                        null,
                        SequencingOrder.LAST_UPDATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_CONTAINS,
                            A_FIND_BY_TEXT_CONTAINS_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_CONTAINS,
                        A_FIND_BY_TEXT_CONTAINS_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getContainsRegex(contains));
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        repositoryHelper.getEndsWithRegex(endsWith),
                        0,
                        null,
                        null,
                        asOfTime,
                        null,
                        SequencingOrder.LAST_UPDATE_RECENT,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_END,
                            A_FIND_BY_TEXT_END_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_END,
                        A_FIND_BY_TEXT_END_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getEndsWithRegex(endsWith));
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByPropertyValue(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        regex,
                        0,
                        null,
                        null,
                        asOfTime,
                        null,
                        SequencingOrder.GUID,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_REGEX,
                            A_FIND_BY_TEXT_REGEX_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_REGEX,
                        A_FIND_BY_TEXT_REGEX_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("searchCriteria", regex);
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

    /**
     * Attempt to find entities based on matching a property's value.
     *
     * @param metadataCollection through which to run findEntities
     * @param repositoryHelper utilities for introspecting properties
     * @throws Exception on any error
     */
    private void findEntitiesByProperty(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper) throws Exception
    {

        final String methodName = "findEntitiesByProperty";

        // Try to find a non-string-based property (if any) that exists in this type definition, since the
        // findEntitiesByPropertyValue will already be doing fairly extensive testing of string-based properties
        PrimitivePropertyValue oneMatch = null;
        String oneName = null;
        PrimitivePropertyValue twoMatch = null;
        String twoName = null;
        PrimitivePropertyValue oneString = null;
        String oneStringName = null;
        PrimitivePropertyValue twoString = null;
        String twoStringName = null;
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef("performance-workbench", entityDef, methodName);
        for (TypeDefAttribute property : properties) {
            if (property.getAttributeType().getCategory().equals(AttributeTypeDefCategory.PRIMITIVE)) {
                PrimitiveDef attribute = (PrimitiveDef) property.getAttributeType();
                String propertyNameToSearch = property.getAttributeName();
                PrimitiveDefCategory propertyType = attribute.getPrimitiveDefCategory();
                PrimitivePropertyValue candidate = getPrimitivePropertyValue(propertyNameToSearch, attribute, property.isUnique(), 0);
                if (attribute.getPrimitiveDefCategory().equals(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)) {
                    candidate.setPrimitiveValue(repositoryHelper.getExactMatchRegex(candidate.valueAsString()));
                }
                if (!propertyType.equals(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)) {
                    if (oneMatch == null) {
                        oneMatch = candidate;
                        oneName = propertyNameToSearch;
                    } else {
                        twoMatch = candidate;
                        twoName = propertyNameToSearch;
                    }
                } else if (oneString == null) {
                    oneString = candidate;
                    oneStringName = propertyNameToSearch;
                } else if (twoString == null) {
                    twoString = candidate;
                    twoStringName = propertyNameToSearch;
                }
            }
            if (oneMatch != null && twoMatch != null) {
                break;
            }
        }

        // If we could not find two non-string properties, fallback to string properties
        if (oneMatch == null && oneString != null) {
            oneMatch = oneString;
            oneName = oneStringName;
        }
        if (twoMatch == null && twoString != null) {
            twoMatch = twoString;
            twoName = twoStringName;
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

        // There must be at least one property to attempt to match against to proceed with this test
        if (oneMatch != null) {
            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        oneProperty,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        asOfTime,
                        oneName,
                        SequencingOrder.PROPERTY_ASCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ONE,
                            A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ONE,
                        A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("matchProperties", oneProperty.toString());
                parameters.put("matchCriteria", "ALL");
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

        // There must be at least two properties to attempt to match against to proceed with these tests
        if (twoMatch != null) {
            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        twoProperties,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        asOfTime,
                        twoName,
                        SequencingOrder.PROPERTY_DESCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ALL,
                            A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ALL,
                        A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ALL");
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        twoProperties,
                        MatchCriteria.ANY,
                        0,
                        null,
                        null,
                        asOfTime,
                        twoName,
                        SequencingOrder.PROPERTY_ASCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ANY,
                            A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ANY,
                        A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ANY");
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<EntityDetail> results = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                        entityDef.getGUID(),
                        twoProperties,
                        MatchCriteria.NONE,
                        0,
                        null,
                        null,
                        asOfTime,
                        oneName,
                        SequencingOrder.PROPERTY_DESCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_NONE,
                            A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                            PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_NONE,
                        A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                        PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search an entity of type " + entityDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", entityDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "NONE");
                parameters.put("asOfTime", asOfTime.toString());
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

        }

    }

}
