/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.search;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test performance of relationship search operations.
 */
public class TestRelationshipSearch extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-relationship-search-performance";
    private static final String TEST_CASE_NAME = "Repository relationship search performance test case";

    private static final String A_FIND_RELATIONSHIPS_ALL     = TEST_CASE_ID + "-findRelationships-all";
    private static final String A_FIND_RELATIONSHIPS_ALL_MSG = "Repository performs search of all instances, sorting by GUID and paging all results, of type: ";

    private static final String A_FIND_BY_TEXT_EXACT     = TEST_CASE_ID + "-findRelationshipsByPropertyValue-exact";
    private static final String A_FIND_BY_TEXT_EXACT_MSG = "Repository performs search with an exact text value, sorting by oldest creation time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_START     = TEST_CASE_ID + "-findRelationshipsByPropertyValue-start";
    private static final String A_FIND_BY_TEXT_START_MSG = "Repository performs search with a text value 'starts-with', sorting by newest creation time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_CONTAINS     = TEST_CASE_ID + "-findRelationshipsByPropertyValue-contains";
    private static final String A_FIND_BY_TEXT_CONTAINS_MSG = "Repository performs search with a text value 'contains', sorting by oldest update time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_END       = TEST_CASE_ID + "-findRelationshipsByPropertyValue-end";
    private static final String A_FIND_BY_TEXT_END_MSG   = "Repository performs search with a text value 'ends-with', sorting by newest update time, of first page of instances of type: ";

    private static final String A_FIND_BY_TEXT_REGEX     = TEST_CASE_ID + "-findRelationshipsByPropertyValue-regex";
    private static final String A_FIND_BY_TEXT_REGEX_MSG = "Repository performs search with a regular expression text value, sorting by GUID, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ONE     = TEST_CASE_ID + "-findRelationshipsByProperty-one";
    private static final String A_FIND_BY_PROPERTY_ONE_MSG = "Repository performs search by a single property value, sorting by that property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ALL     = TEST_CASE_ID + "-findRelationshipsByProperty-all";
    private static final String A_FIND_BY_PROPERTY_ALL_MSG = "Repository performs search by two property values, sorting by the second property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_ANY     = TEST_CASE_ID + "-findRelationshipsByProperty-any";
    private static final String A_FIND_BY_PROPERTY_ANY_MSG = "Repository performs search by two property values, sorting by the second property, of first page of instances of type: ";

    private static final String A_FIND_BY_PROPERTY_NONE     = TEST_CASE_ID + "-findRelationshipsByProperty-none";
    private static final String A_FIND_BY_PROPERTY_NONE_MSG = "Repository performs search by two property value, sorting by the first property, of first page of instances of type: ";

    private final RelationshipDef     relationshipDef;
    private final String              testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param relationshipDef type of valid relationships
     */
    public TestRelationshipSearch(PerformanceWorkPad workPad,
                                  RelationshipDef    relationshipDef)
    {
        super(workPad, PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId());

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
        OMRSRepositoryHelper repositoryHelper = super.getRepositoryHelper();

        findRelationships(metadataCollection);
        findRelationshipsByPropertyValue(metadataCollection, repositoryHelper);
        findRelationshipsByProperty(metadataCollection, repositoryHelper);

        super.setSuccessMessage("Relationship search performance tests complete for: " + testTypeName);
    }

    /**
     * Attempt to find all relationships based solely on the typeDef GUID.
     *
     * @param metadataCollection through which to run findRelationships
     * @throws Exception on any error
     */
    private void findRelationships(OMRSMetadataCollection metadataCollection) throws Exception
    {

        final String methodName = "findRelationships";

        try {

            int fromElement = 0;
            long start = System.nanoTime();
            List<Relationship> results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                    relationshipDef.getGUID(),
                    null,
                    null,
                    fromElement,
                    null,
                    null,
                    null,
                    SequencingOrder.GUID,
                    performanceWorkPad.getMaxSearchResults());
            long elapsedTime = (System.nanoTime() - start) / 1000000;

            int page = 1;
            if (results != null && !results.isEmpty()) {
                assertCondition(true,
                        A_FIND_RELATIONSHIPS_ALL + "-p" + page,
                        A_FIND_RELATIONSHIPS_ALL_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null,
                        methodName,
                        elapsedTime);
            }

            // Page the results to test paging performance + tally a running count of the total number of
            // instances in the environment (do this here rather than via create tests so that such counts also work
            // for repositories that do not support write operations)
            while (results != null && !results.isEmpty()) {
                // Only count those relationships whose type is precisely the one we searched (to avoid double-counting
                // when we have searched a supertype, whose results will include various other subtypes as well)
                for (Relationship result : results) {
                    if (result.getType().getTypeDefName().equals(relationshipDef.getName())) {
                        performanceWorkPad.incrementRelationshipsFound(1);
                    }
                }
                fromElement = fromElement + results.size();
                start = System.nanoTime();
                results = metadataCollection.findRelationships(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        null,
                        null,
                        fromElement,
                        null,
                        null,
                        null,
                        SequencingOrder.GUID,
                        performanceWorkPad.getMaxSearchResults());
                elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    page += 1;
                    assertCondition(true,
                            A_FIND_RELATIONSHIPS_ALL + "-p" + page,
                            A_FIND_RELATIONSHIPS_ALL_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            }

        } catch (FunctionNotSupportedException exception) {
            super.addNotSupportedAssertion(A_FIND_RELATIONSHIPS_ALL,
                    A_FIND_RELATIONSHIPS_ALL_MSG + testTypeName,
                    PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                    null);
            return;
        } catch (Exception exc) {
            String operationDescription = "search relationships of type " + relationshipDef.getName();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("typeGUID", relationshipDef.getGUID());
            String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
            throw new Exception(msg, exc);
        }

    }

    /**
     * Attempt to find relationships based on various textual value matches.
     *
     * @param metadataCollection through which to run findRelationshipsByPropertyValue
     * @param repositoryHelper utilities for introspecting properties and generating regular expressions
     * @throws Exception on any error
     */
    private void findRelationshipsByPropertyValue(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper) throws Exception
    {

        final String methodName = "findRelationshipsByPropertyValue";

        // Start by finding the first string-based property that exists in this type definition, and whose name is at
        // least 3 characters long
        String stringPropertyName = null;
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef("performance-workbench", relationshipDef, methodName);
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
                List<Relationship> results = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        repositoryHelper.getExactMatchRegex(exact),
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.CREATION_DATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_EXACT,
                            A_FIND_BY_TEXT_EXACT_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_EXACT,
                        A_FIND_BY_TEXT_EXACT_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getExactMatchRegex(exact));
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        repositoryHelper.getStartsWithRegex(startsWith),
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.CREATION_DATE_RECENT,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_START,
                            A_FIND_BY_TEXT_START_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_START,
                        A_FIND_BY_TEXT_START_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getStartsWithRegex(startsWith));
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        repositoryHelper.getContainsRegex(contains),
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.LAST_UPDATE_OLDEST,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_CONTAINS,
                            A_FIND_BY_TEXT_CONTAINS_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_CONTAINS,
                        A_FIND_BY_TEXT_CONTAINS_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getContainsRegex(contains));
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        repositoryHelper.getEndsWithRegex(endsWith),
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.LAST_UPDATE_RECENT,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_END,
                            A_FIND_BY_TEXT_END_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_END,
                        A_FIND_BY_TEXT_END_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("searchCriteria", repositoryHelper.getEndsWithRegex(endsWith));
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByPropertyValue(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        regex,
                        0,
                        null,
                        null,
                        null,
                        SequencingOrder.GUID,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_TEXT_REGEX,
                            A_FIND_BY_TEXT_REGEX_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_TEXT_REGEX,
                        A_FIND_BY_TEXT_REGEX_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("searchCriteria", regex);
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }
        }

    }

    /**
     * Attempt to find relationships based on matching a property's value.
     *
     * @param metadataCollection through which to run findRelationshipsByProperty
     * @param repositoryHelper utilities for introspecting properties
     * @throws Exception on any error
     */
    private void findRelationshipsByProperty(OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper) throws Exception
    {

        final String methodName = "findRelationshipsByProperty";

        // Try to find a non-string-based property (if any) that exists in this type definition, since the
        // findRelationshipsByPropertyValue will already be doing fairly extensive testing of string-based properties
        PrimitivePropertyValue oneMatch = null;
        String oneName = null;
        PrimitivePropertyValue twoMatch = null;
        String twoName = null;
        PrimitivePropertyValue oneString = null;
        String oneStringName = null;
        PrimitivePropertyValue twoString = null;
        String twoStringName = null;
        List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef("performance-workbench", relationshipDef, methodName);
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
                List<Relationship> results = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        oneProperty,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        oneName,
                        SequencingOrder.PROPERTY_ASCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ONE,
                            A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ONE,
                        A_FIND_BY_PROPERTY_ONE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
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
                List<Relationship> results = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        twoProperties,
                        MatchCriteria.ALL,
                        0,
                        null,
                        null,
                        twoName,
                        SequencingOrder.PROPERTY_DESCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ALL,
                            A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ALL,
                        A_FIND_BY_PROPERTY_ALL_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ALL");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        twoProperties,
                        MatchCriteria.ANY,
                        0,
                        null,
                        null,
                        twoName,
                        SequencingOrder.PROPERTY_ASCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_ANY,
                            A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_ANY,
                        A_FIND_BY_PROPERTY_ANY_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "ANY");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

            try {
                long start = System.nanoTime();
                List<Relationship> results = metadataCollection.findRelationshipsByProperty(workPad.getLocalServerUserId(),
                        relationshipDef.getGUID(),
                        twoProperties,
                        MatchCriteria.NONE,
                        0,
                        null,
                        null,
                        oneName,
                        SequencingOrder.PROPERTY_DESCENDING,
                        performanceWorkPad.getMaxSearchResults());
                long elapsedTime = (System.nanoTime() - start) / 1000000;
                if (results != null && !results.isEmpty()) {
                    assertCondition(true,
                            A_FIND_BY_PROPERTY_NONE,
                            A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                            PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                            null,
                            methodName,
                            elapsedTime);
                }
            } catch (FunctionNotSupportedException exception) {
                super.addNotSupportedAssertion(A_FIND_BY_PROPERTY_NONE,
                        A_FIND_BY_PROPERTY_NONE_MSG + testTypeName,
                        PerformanceProfile.RELATIONSHIP_SEARCH.getProfileId(),
                        null);
                return;
            } catch (Exception exc) {
                String operationDescription = "search relationships of type " + relationshipDef.getName();
                Map<String, String> parameters = new HashMap<>();
                parameters.put("typeGUID", relationshipDef.getGUID());
                parameters.put("matchProperties", twoProperties.toString());
                parameters.put("matchCriteria", "NONE");
                String msg = this.buildExceptionMessage(testCaseId, methodName, operationDescription, parameters, exc.getClass().getSimpleName(), exc.getMessage());
                throw new Exception(msg, exc);
            }

        }

    }

}
