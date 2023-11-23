/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Tests the condition builder model.
 */
public class ConditionBuilderTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testSinglePropertyCondition() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "qualifiedName";
            String propertyValue = "a-qualified-name";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);
            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);
            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"]]
            validateSinglePropertyCondition(conditions, propertyKeyword, propertyValue);

            searchProperties.setMatchCriteria(MatchCriteria.ANY);
            conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);
            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"]]
            validateSinglePropertyCondition(conditions, propertyKeyword, propertyValue);

            searchProperties.setMatchCriteria(MatchCriteria.NONE);
            conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);
            // Expected --> [(not-join [e] [e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"])]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a single negated property.");
            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentList);
            IPersistentList condition = (IPersistentList) candidate;
            assertEquals(condition.count(), 3, "Three elements expected in the clause: 'not-join' followed by a selector and a condition.");
            assertEquals(condition.seq().first(), ConditionBuilder.NOT_JOIN_OPERATOR);
            Object embedded = condition.seq().next().first();
            assertTrue(embedded instanceof IPersistentCollection);
            embedded = condition.seq().next().next().first();
            assertTrue(embedded instanceof IPersistentCollection);
            validatePropertyCondition((IPersistentCollection) embedded, propertyKeyword, propertyValue);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validateSinglePropertyCondition(List<IPersistentCollection> conditions,
                                                 Keyword propertyKeyword,
                                                 Object propertyValue) {
        assertNotNull(conditions);
        assertEquals(conditions.size(), 1, "One condition is expected when searching for a single property.");
        validatePropertyCondition(conditions.get(0), propertyKeyword, propertyValue);
    }

    private void validatePropertyCondition(IPersistentCollection candidate,
                                           Keyword propertyKeyword,
                                           Object propertyValue) {
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        IPersistentVector condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
        assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
        assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
        assertEquals(condition.nth(2), propertyValue, "Third element of the triple is expected to be the value to match against.");
    }

    @Test
    void testMultiplePropertiesConditions() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName1 = "qualifiedName";
            String propertyValue1 = "a-qualified-name";
            String propertyName2 = "displayName";
            String propertyValue2 = "a-name";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName1,
                    propertyValue1,
                    this.getClass().getName());
            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    ip,
                    propertyName2,
                    propertyValue2,
                    this.getClass().getName());

            InstancePropertyValue ipv1 = ip.getPropertyValue(propertyName1);
            Set<Keyword> propertyKeywords1 = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName1,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv1);
            assertEquals(propertyKeywords1.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword1 = null;
            for (Keyword a : propertyKeywords1) {
                propertyKeyword1 = a;
            }

            InstancePropertyValue ipv2 = ip.getPropertyValue(propertyName2);
            Set<Keyword> propertyKeywords2 = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName2,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv2);
            assertEquals(propertyKeywords2.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword2 = null;
            for (Keyword a : propertyKeywords2) {
                propertyKeyword2 = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName1);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv1);
            PropertyCondition two = new PropertyCondition();
            two.setProperty(propertyName2);
            two.setOperator(PropertyComparisonOperator.EQ);
            two.setValue(ipv2);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            propertyConditions.add(two);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [(and [e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"] [e :entityProperties/GlossaryTerm.displayName.value "a-name"])]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a wrapped combination of properties.");
            IPersistentCollection candidate = conditions.get(0);

            assertTrue(candidate instanceof IPersistentList, "Condition is expected to be wrapped in an inner list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.seq().first(), ConditionBuilder.AND_OPERATOR, "First element of list is expected to be the AND operator.");

            Object piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentVector, "Second element of list is expected to be a condition.");
            IPersistentVector condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword1, "Second element of triple is expected to be the keyword for one of the properties to match.");
            assertEquals(condition.nth(2), propertyValue1, "Third element of the triple is expected to be the value to match that property against.");

            piece = clause.seq().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Third element of list is expected to be a condition.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword2, "Second element of triple is expected to be the keyword for another of the properties to match.");
            assertEquals(condition.nth(2), propertyValue2, "Third element of the triple is expected to be the value to match that property against.");

            searchProperties.setMatchCriteria(MatchCriteria.ANY);
            conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [(or-join [e] [e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"] [e :entityProperties/GlossaryTerm.displayName.value "a-name"])]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a wrapped combination of properties.");
            candidate = conditions.get(0);

            assertTrue(candidate instanceof IPersistentList, "Condition is expected to be wrapped in an inner list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.seq().first(), ConditionBuilder.OR_JOIN, "First element of list is expected to be the OR-JOIN operator.");

            piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentVector, "Second element of list is expected to be a vector of bindings.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 1, "Vector of bindings is expected to have only one element.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "Binding in the vector is expected to be the symbol for a document ID.");

            piece = clause.seq().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Third element of list is expected to be a condition.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword1, "Second element of triple is expected to be the keyword for one of the properties to match.");
            assertEquals(condition.nth(2), propertyValue1, "Third element of the triple is expected to be the value to match that property against.");

            piece = clause.seq().next().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Fourth element of list is expected to be a condition.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword2, "Second element of triple is expected to be the keyword for another of the properties to match.");
            assertEquals(condition.nth(2), propertyValue2, "Third element of the triple is expected to be the value to match that property against.");

            searchProperties.setMatchCriteria(MatchCriteria.NONE);
            conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [(not (or-join [e] [e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"] [e :entityProperties/GlossaryTerm.displayName.value "a-name"]))]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a wrapped combination of properties.");
            candidate = conditions.get(0);

            assertTrue(candidate instanceof IPersistentList, "Condition is expected to be wrapped in an inner list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.seq().first(), ConditionBuilder.NOT_OPERATOR, "First element of list is expected to be the NOT operator.");

            piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentList, "Second element of list is expected to be a nested list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), ConditionBuilder.OR_JOIN, "First element of list is expected to be the OR-JOIN operator.");

            piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentVector, "Second element of list is expected to be a vector of bindings.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 1, "Vector of bindings is expected to have only one element.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "Binding in the vector is expected to be the symbol for a document ID.");

            piece = clause.seq().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Third element of list is expected to be a condition.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword1, "Second element of triple is expected to be the keyword for one of the properties to match.");
            assertEquals(condition.nth(2), propertyValue1, "Third element of the triple is expected to be the value to match that property against.");

            piece = clause.seq().next().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Fourth element of list is expected to be a condition.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword2, "Second element of triple is expected to be the keyword for another of the properties to match.");
            assertEquals(condition.nth(2), propertyValue2, "Third element of the triple is expected to be the value to match that property against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNestedConditions() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName1 = "qualifiedName";
            String propertyValue1 = "a-qualified-name";
            String propertyName2 = "displayName";
            String propertyValue2 = "a-name";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip1 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName1,
                    propertyValue1,
                    this.getClass().getName());

            InstancePropertyValue ipv1 = ip1.getPropertyValue(propertyName1);
            Set<Keyword> propertyKeywords1 = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName1,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv1);
            assertEquals(propertyKeywords1.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword1 = null;
            for (Keyword a : propertyKeywords1) {
                propertyKeyword1 = a;
            }

            InstanceProperties ip2 = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName2,
                    propertyValue2,
                    this.getClass().getName());
            InstancePropertyValue ipv2 = ip2.getPropertyValue(propertyName2);
            Set<Keyword> propertyKeywords2 = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName2,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv2);
            assertEquals(propertyKeywords2.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword2 = null;
            for (Keyword a : propertyKeywords2) {
                propertyKeyword2 = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName1);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv1);

            PropertyCondition two = new PropertyCondition();
            two.setProperty(propertyName2);
            two.setOperator(PropertyComparisonOperator.EQ);
            two.setValue(ipv2);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            propertyConditions.add(two);
            SearchProperties nestedProperties = new SearchProperties();
            nestedProperties.setMatchCriteria(MatchCriteria.ALL);
            nestedProperties.setConditions(propertyConditions);

            PropertyCondition outer = new PropertyCondition();
            outer.setNestedConditions(nestedProperties);
            List<PropertyCondition> propertyConditions2 = new ArrayList<>();
            propertyConditions2.add(outer);

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ANY);
            searchProperties.setConditions(propertyConditions2);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value "a-qualified-name"], [e :entityProperties/GlossaryTerm.displayName.value "a-name"]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 2, "Two conditions are expected when searching for a nested set of multiple properties.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword1, "Second element of triple is expected to be the keyword for one of the the properties to match.");
            assertEquals(condition.nth(2), propertyValue1, "Third element of the triple is expected to be the value to match the property against.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword2, "Second element of triple is expected to be the keyword for another of the the properties to match.");
            assertEquals(condition.nth(2), propertyValue2, "Third element of the triple is expected to be the value to match the property against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testHeaderPropertyCondition() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "createdBy";
            String propertyValue = "someone";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :createdBy "someone"]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a single property.");
            IPersistentCollection candidate = conditions.get(0);

            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern("createdBy"), "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), propertyValue, "Third element of the triple is expected to be the value to match against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testRegexOperator() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "qualifiedName";
            String propertyValue = ".*\\Qqualified\\E.*";
            Symbol propertyBinding = Symbol.intern(propertyName);
            Symbol sv = Symbol.intern("sv");

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);
            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.LIKE);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qualifiedName], [(str qualifiedName) sv], [(clojure.string/includes? sv "qualified")]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected when searching for a contains regular expression.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), propertyBinding, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            Object piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a clause.");
            IPersistentList clause = (IPersistentList) piece;
            assertEquals(clause.count(), 2, "Clause is expected to have two elements.");
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
            assertEquals(clause.seq().next().first(), propertyBinding, "Second element of clause is expected to be the symbol capturing the property value.");
            assertEquals(condition.nth(1), sv, "Second element of tuple is expected to be a binding for the string-converted value.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed within a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.count(), 3, "Clause is expected to have three elements.");
            assertEquals(clause.seq().first(), TextConditionBuilder.CONTAINS, "First element of clause is expected to be the contains operator.");
            assertEquals(clause.seq().next().first(), sv, "Second element of clause is expected to be the symbol capturing the string-converted property value.");
            assertEquals(clause.seq().next().next().first(), helper.getUnqualifiedLiteralString(propertyValue), "Third element of clause is expected to be the value to compare against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testInOperator() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "qualifiedName";
            Symbol propertyBinding = Symbol.intern(propertyName);
            Symbol las = Symbol.intern("las");

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            List<String> values = new ArrayList<>();
            values.add("one");
            values.add("two");
            ArrayPropertyValue apv = new ArrayPropertyValue();
            apv.setArrayCount(values.size());
            for (int i = 0; i < values.size(); i++) {
                String value = values.get(i);
                InstanceProperties properties = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                        null,
                        propertyName,
                        value,
                        this.getClass().getName());
                InstancePropertyValue ipv = properties.getPropertyValue(propertyName);
                apv.setArrayValue(i, ipv);
            }

            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    apv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.IN);
            one.setValue(apv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qualifiedName], [(hash-set "one" "two") las], [(contains? las qualifiedName)]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected when searching using the IN operator.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), propertyBinding, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            Object piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a clause.");
            IPersistentList clause = (IPersistentList) piece;
            assertEquals(clause.count(), 3, "Clause is expected to have at least two elements.");
            assertEquals(clause.seq().first(), ConditionBuilder.SET_OPERATOR, "First element of clause is expected to be the set operator.");
            assertEquals(clause.seq().next().first(), values.get(0), "Second element of clause is expected to be one of the values to compare against.");
            assertEquals(clause.seq().next().next().first(), values.get(1), "Third element of clause is expected to be another of the values to compare against.");
            assertEquals(condition.nth(1), las, "Second element of tuple is expected to be a binding for the set.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed within a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.count(), 3, "Clause is expected to have three elements.");
            assertEquals(clause.seq().first(), ConditionBuilder.IN_OPERATOR, "First element of clause is expected to be the IN operator.");
            assertEquals(clause.seq().next().first(), las, "Second element of clause is expected to be the symbol defining the set.");
            assertEquals(clause.seq().next().next().first(), propertyBinding, "Third element of clause is expected to be the symbol used to capture the value of the property.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testOtherOperators() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "qualifiedName";
            String propertyValue = "a-qualified-name";
            Symbol propertyBinding = Symbol.intern(propertyName);

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);
            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the entities.");
            Keyword propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.NEQ);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qualifiedName], [(not= qualifiedName "a-qualified-name")]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 2, "Two conditions are expected when searching for a property with an operator other than equals.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), propertyBinding, "Third element of the triple is expected to be the symbol to capture the property's value.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition itself is expected to have a single nested clause.");
            Object piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause itself is expected to be a single list.");
            IPersistentList clause = (IPersistentList) piece;
            assertEquals(clause.count(), 3, "Clause is expected to be a triple.");
            assertEquals(clause.seq().first(), ConditionBuilder.NEQ_OPERATOR, "First element of triple is expected to be the not= operator.");
            assertEquals(clause.seq().next().first(), propertyBinding, "Second element of triple is expected to be the symbol used to capture the property's value.");
            assertEquals(clause.seq().next().next().first(), propertyValue, "Third element of the triple is expected to be the value against which to compare.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testMultiplePropertyVariations() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("RelatedTerm");
            typeNames.add("Synonym");

            String propertyName = "steward";
            String propertyValue = "username";
            Symbol propertyBinding = Symbol.intern("sv_" + propertyName);

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);
            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    RelationshipMapping.RELATIONSHIP_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 2, "Expected to have multiple matching properties in the relationships.");

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    RelationshipMapping.RELATIONSHIP_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    true,
                    true);

            // Expected --> [[(wildcard-text-search-cs "username") [[e v steward _]]], [(str steward) sv_steward], [(clojure.string/ends-with? sv_steward ".steward.value")]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected when searching for a property that appears in multiple typeDefs.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;

            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            Object piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Condition is expected to start with a clause.");
            IPersistentList clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.WILDCARD_TEXT_SEARCH_CS, "First element of clause is expected to be a case-sensitive wildcard search.");
            assertEquals(clause.seq().next().first(), propertyValue, "Second element of clause is expected to be the text value to find.");
            piece = condition.nth(1);
            assertTrue(piece instanceof IPersistentVector, "Condition is expected to end with a de-structured variable binding.");
            IPersistentVector vars = (IPersistentVector) piece;
            assertEquals(vars.length(), 1, "De-structured variables should be nested inside an outer vector.");
            piece = vars.nth(0);
            assertTrue(piece instanceof IPersistentVector, "De-structured variables are listed inside a vector.");
            vars = (IPersistentVector) piece;
            assertEquals(vars.nth(0), XTDBQuery.DOC_ID, "First element of de-structured variables is expected to be the document ID symbol.");
            assertEquals(vars.nth(1), TextConditionBuilder.VALUE, "Second element of de-structured variables is expected to be the value symbol.");
            assertEquals(vars.nth(2), Symbol.intern(propertyName), "Third element of de-structured variables is expected to be the property itself.");
            assertEquals(vars.nth(3), TextConditionBuilder.ELIDE, "Fourth element of de-structured variables is expected to an elide.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Condition is expected to start with a clause.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be a string operator.");
            assertEquals(clause.seq().next().first(), Symbol.intern(propertyName), "Second element of clause is expected to be the symbol that captures the property name.");
            assertEquals(condition.nth(1), propertyBinding, "Second element of clause is expected to be the symbol to bind to the string-converted value of the property name.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList);
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.ENDS_WITH, "First element of clause is expected to be a string ends-with operator.");
            assertEquals(clause.seq().next().first(), propertyBinding, "Second element of clause is expected to be the symbol used to bind to the string-converted value of the property name.");
            assertEquals(clause.seq().next().next().first(), "." + propertyName + ".value", "Third element of clause is expected to be the name of the comparison-ready value for the property being searched.");

            conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    RelationshipMapping.RELATIONSHIP_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [(or [e :relationshipProperties/RelatedTerm.steward.value "username"] [e :relationshipProperties/Synonym.steward.value "username"])]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a property that appears in multiple typeDefs.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentList, "Condition is expected to be enclosed in an OR clause.");
            clause = (IPersistentList) candidate;

            assertEquals(clause.count(), 3, "Clause is expected to have at least three elements.");
            assertEquals(clause.seq().first(), ConditionBuilder.OR_OPERATOR, "First element is expected to be an OR clause.");
            piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentVector, "Second element is expected to be a vector triple.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of condition is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.RELATIONSHIP_PROPERTIES_NS, "RelatedTerm.steward.value"), "Second element of condition is expected to be the keyword for one of the properties.");
            assertEquals(condition.nth(2), propertyValue, "Third element of condition is expected to be the value to match against.");
            piece = clause.seq().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Third element is expected to be a vector triple.");
            condition = (IPersistentVector) piece;
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of condition is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.RELATIONSHIP_PROPERTIES_NS, "Synonym.steward.value"), "Second element of condition is expected to be the keyword for the other of the properties.");
            assertEquals(condition.nth(2), propertyValue, "Third element of condition is expected to be the value to match against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNoResultsCondition() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");

            String propertyName = "imaginary-property";
            String propertyValue = "some-value";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);
            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 0, "Expected to no matching properties.");

            PropertyCondition one = new PropertyCondition();
            one.setProperty(propertyName);
            one.setOperator(PropertyComparisonOperator.EQ);
            one.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(one);
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<IPersistentCollection> conditions = ConditionBuilder.buildPropertyConditions(searchProperties,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    typeNames,
                    connector,
                    false,
                    true);

            // Expected --> [[e :type.category -1]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for a non-existent property.");
            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_CATEGORY), "Second element of triple is expected to be the keyword for the typeDef category.");
            assertEquals(condition.nth(2), -1, "Third element of the triple is expected to be an invalid typeDef category ordinal.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testNull() {
        try {

            assertNull(ConditionBuilder.buildPropertyConditions(null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    new HashSet<>(),
                    connector,
                    false,
                    true),
                    "No conditions are expected when the search properties are null.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
