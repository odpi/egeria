/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

/**
 * Tests the text-based condition builder model.
 */
public class TextConditionBuilderTest {

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    @Test
    void testWildcardText() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            String propertyName = "qualifiedName";
            String regex = ".*\\Qname\\E.*";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    regex,
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

            List<IPersistentCollection> conditions = TextConditionBuilder.buildWildcardTextCondition(regex,
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value v], [(str v) sv], [(clojure.string/includes? sv "name")]]
            validateTextContains(conditions, propertyKeyword, helper.getUnqualifiedLiteralString(regex));

            typeNames = new HashSet<>();
            typeNames.add("GlossaryTerm");
            propertyName = "displayName";

            conditions = TextConditionBuilder.buildWildcardTextCondition(regex,
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false,
                    true);

            // Expected --> [(or (and [e :entityProperties/GlossaryTerm.displayName.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/GlossaryTerm.summary.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/GlossaryTerm.description.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/GlossaryTerm.usage.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/GlossaryTerm.abbreviation.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/GlossaryTerm.examples.value v] [(str v) sv] [(clojure.string/includes? sv "name")]) (and [e :entityProperties/Referenceable.qualifiedName.value v] [(str v) sv] [(clojure.string/includes? sv "name")]))]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected when searching for any string against a type with multiple string properties.");
            IPersistentCollection candidate = conditions.get(0);

            assertTrue(candidate instanceof IPersistentList, "Condition is expected to be wrapped in an inner list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.seq().first(), ConditionBuilder.OR_OPERATOR, "First element of list is expected to be the OR operator.");
            Object piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentList, "Nested condition is expected to be wrapped in an inner list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), ConditionBuilder.AND_OPERATOR, "First element of list is expected to be the AND operator.");

            List<IPersistentCollection> nestedConditions = new ArrayList<>();
            piece = clause.seq().next().first();
            assertTrue(piece instanceof IPersistentVector, "Second element of list is expected to be a condition.");
            nestedConditions.add((IPersistentVector) piece);
            piece = clause.seq().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Third element of list is expected to be a condition.");
            nestedConditions.add((IPersistentVector) piece);
            piece = clause.seq().next().next().next().first();
            assertTrue(piece instanceof IPersistentVector, "Fourth element of list is expected to be a condition.");
            nestedConditions.add((IPersistentVector) piece);

            propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the entities.");
            propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }
            validateTextContains(nestedConditions, propertyKeyword, helper.getUnqualifiedLiteralString(regex));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validateTextContains(List<IPersistentCollection> conditions, Keyword propertyKeyword, String propertyValue) {

        Symbol sv = Symbol.intern("sv");

        assertNotNull(conditions);
        assertEquals(conditions.size(), 3, "Three conditions are expected to do a contains string comparison.");

        IPersistentCollection candidate = conditions.get(0);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        IPersistentVector condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
        assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
        assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
        assertEquals(condition.nth(2), TextConditionBuilder.VALUE, "Third element of the triple is expected to be a symbol to capture the value of the property.");

        candidate = conditions.get(1);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
        Object piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a list.");
        IPersistentList clause = (IPersistentList) piece;
        assertEquals(clause.count(), 2, "Clause is expected to have two elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
        assertEquals(clause.seq().next().first(), TextConditionBuilder.VALUE, "Second element of the clause is expected to be the symbol used to capture the value of the property.");
        assertEquals(condition.nth(1), sv, "Second element of the tuple is expected to be a symbol to capture the string-converted value.");

        candidate = conditions.get(2);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
        piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "Clause is expected to be in a list.");
        clause = (IPersistentList) piece;
        assertEquals(clause.count(), 3, "Clause is expected to have three elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.CONTAINS, "First element of clause is expected to be the string contains operator.");
        assertEquals(clause.seq().next().first(), sv, "Second element of the clause is expected to be the symbol used to capture the string-converted value of the property.");
        assertEquals(clause.seq().next().next().first(), propertyValue, "Second element of the tuple is expected to be the string value to match against.");

    }

    @Test
    void testWildcardLucene() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            String base = "name";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            List<IPersistentCollection> conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    helper.getExactMatchRegex(base),
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    false);

            // Expected --> [[(wildcard-text-search-cs "name") [[e v a _]]]]
            validateLuceneWildcard(conditions, base);

            conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    helper.getContainsRegex(base),
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true);

            // Expected --> [[(wildcard-text-search-cs "*name*") [[e v a _]]]]
            validateLuceneWildcard(conditions, "*" + base + "*");

            conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    helper.getStartsWithRegex(base),
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true);

            // Expected --> [[(wildcard-text-search-cs "name*") [[e v a _]]]]
            validateLuceneWildcard(conditions, base + "*");

            conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    helper.getEndsWithRegex(base),
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true);

            // Expected --> [[(wildcard-text-search-cs "*name") [[e v a _]]]]
            validateLuceneWildcard(conditions, "*" + base);

            conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    "/" + base + "/",
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true);

            // Expected --> [[(wildcard-text-search-cs "/name/") [[e v a _]]]]
            validateLuceneWildcard(conditions, "/" + base + "/");

            conditions = TextConditionBuilder.buildWildcardLuceneCondition(
                    base,
                    connector,
                    typeNames,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    true);

            // Expected --> [[(wildcard-text-search-cs "/name/") [[e v a _]]]]
            validateLuceneWildcard(conditions, "/" + base + "/");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validateLuceneWildcard(List<IPersistentCollection> conditions, String regex) {

        assertNotNull(conditions);
        assertEquals(conditions.size(), 1, "One condition is expected to do a Lucene-optimized text search.");

        IPersistentCollection candidate = conditions.get(0);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        IPersistentVector condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");

        Object piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a list.");
        IPersistentList clause = (IPersistentList) piece;
        assertEquals(clause.count(), 2, "Clause is expected to have two elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.WILDCARD_TEXT_SEARCH_CS, "First element of clause is expected to be the Lucene wildcard search operator.");
        assertEquals(clause.seq().next().first(), regex, "Second element of the clause is expected to be the Lucene regex to match against.");

        piece = condition.nth(1);
        assertTrue(piece instanceof IPersistentVector, "Second element of tuple is expected to be a nested vector.");
        IPersistentVector vars = (IPersistentVector) piece;
        assertEquals(vars.length(), 1, "Vector is expected to have only a single element.");
        piece = vars.nth(0);
        assertTrue(piece instanceof IPersistentVector, "Variables for binding are expected to be nested within an inner vector.");
        vars = (IPersistentVector) piece;
        assertEquals(vars.length(), 4, "Four variables are expected to be bound.");
        assertEquals(vars.nth(0), XTDBQuery.DOC_ID, "First element is expected to be the symbol for the document ID.");
        assertEquals(vars.nth(1), TextConditionBuilder.VALUE, "Second element is expected to be the symbol for the value of the property.");
        assertEquals(vars.nth(2), TextConditionBuilder.ATTRIBUTE, "Third element is expected to be the symbol for the property name itself.");
        assertEquals(vars.nth(3), TextConditionBuilder.ELIDE, "Fourth element is expected to be an elide.");

    }

    @Test
    void testLuceneOptimization() {
        try {

            String propertyName = "qualifiedName";
            String base = "name";

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    base,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);

            List<IPersistentCollection> conditions = TextConditionBuilder.buildLuceneOptimizedConditions(
                    propertyName,
                    PropertyComparisonOperator.NEQ,
                    ipv,
                    connector,
                    true);

            // Expected --> [(not [(wildcard-text-search-cs "name") [[e v qualifiedName _]]] [(str qualifiedName) sv_qualifiedName] [(clojure.string/ends-with? sv_qualifiedName ".qualifiedName.value")])]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "Only a single outer-wrapped NOT clause expected for a not equals Lucene-optimized property text query.");
            IPersistentCollection outer = conditions.get(0);
            assertTrue(outer instanceof IPersistentList, "Single NOT-JOIN clause expected to be wrapped within a list.");
            IPersistentList clause = (IPersistentList) outer;
            assertEquals(clause.seq().first(), ConditionBuilder.NOT_JOIN_OPERATOR, "First element of clause expected to be the NOT-JOIN operator.");

            ISeq next = clause.seq().next();
            assertTrue(next.first() instanceof IPersistentVector, "Second element to be a vector.");

            List<IPersistentCollection> toValidate = new ArrayList<>();
            next = clause.seq().next().next();
            while (next != null) {
                Object candidate = next.first();
                assertTrue(candidate instanceof IPersistentCollection, "Nested conditions are expected to be collections themselves.");
                toValidate.add((IPersistentCollection) candidate);
                next = next.next();
            }
            validateLuceneOptimization(toValidate, propertyName, "name");

            conditions = TextConditionBuilder.buildLuceneOptimizedConditions(
                    propertyName,
                    PropertyComparisonOperator.LIKE,
                    ipv,
                    connector,
                    true);

            // Expected --> [[(wildcard-text-search-cs "/name/") [[e v qualifiedName _]]], [(str qualifiedName) sv_qualifiedName], [(clojure.string/ends-with? sv_qualifiedName ".qualifiedName.value")]]
            validateLuceneOptimization(conditions, propertyName, "/name/");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validateLuceneOptimization(List<IPersistentCollection> conditions, String propertyName, String regex) {

        Symbol sv = Symbol.intern("sv_" + propertyName);

        assertNotNull(conditions);
        assertEquals(conditions.size(), 3, "Three conditions are expected to do a Lucene-optimized single property text search.");

        IPersistentCollection candidate = conditions.get(0);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        IPersistentVector condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");

        Object piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a list.");
        IPersistentList clause = (IPersistentList) piece;
        assertEquals(clause.count(), 2, "Clause is expected to have two elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.WILDCARD_TEXT_SEARCH_CS, "First element of clause is expected to be the Lucene wildcard search operator.");
        assertEquals(clause.seq().next().first(), regex, "Second element of the clause is expected to be the Lucene regex to match against.");

        piece = condition.nth(1);
        assertTrue(piece instanceof IPersistentVector, "Second element of tuple is expected to be a nested vector.");
        IPersistentVector vars = (IPersistentVector) piece;
        assertEquals(vars.length(), 1, "Vector is expected to have only a single element.");
        piece = vars.nth(0);
        assertTrue(piece instanceof IPersistentVector, "Variables for binding are expected to be nested within an inner vector.");
        vars = (IPersistentVector) piece;
        assertEquals(vars.length(), 4, "Four variables are expected to be bound.");
        assertEquals(vars.nth(0), XTDBQuery.DOC_ID, "First element is expected to be the symbol for the document ID.");
        assertEquals(vars.nth(1), TextConditionBuilder.VALUE, "Second element is expected to be the symbol for the value of the property.");
        assertEquals(vars.nth(2), Symbol.intern(propertyName), "Third element is expected to be the symbol for the property name itself.");
        assertEquals(vars.nth(3), TextConditionBuilder.ELIDE, "Fourth element is expected to be an elide.");

        candidate = conditions.get(1);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");

        piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a list.");
        clause = (IPersistentList) piece;
        assertEquals(clause.count(), 2, "Clause is expected to have two elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
        assertEquals(clause.seq().next().first(), Symbol.intern(propertyName), "Second element of the clause is expected to be the symbol for the property name.");
        assertEquals(condition.nth(1), sv, "Second element of tuple is expected to be a symbol for the string-converted property name.");

        // [(clojure.string/ends-with? sv_qualifiedName ".qualifiedName.value")]
        candidate = conditions.get(2);
        assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
        condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 1, "Only a single clause is expected.");

        piece = condition.nth(0);
        assertTrue(piece instanceof IPersistentList, "Clause is expected to be contained within a list.");
        clause = (IPersistentList) piece;
        assertEquals(clause.count(), 3, "Clause is expected to have three elements.");
        assertEquals(clause.seq().first(), TextConditionBuilder.ENDS_WITH, "First element of clause is expected to be the ends-with operator.");
        assertEquals(clause.seq().next().first(), sv, "Second element of the clause is expected to be the symbol for the string-converted property name.");
        assertEquals(clause.seq().next().next().first(), "." + propertyName + ".value", "Third element of the clause is expected to be the value-qualified name of the property.");

    }

    @Test
    void testRegexConditions() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            String propertyName = "qualifiedName";
            String base = "name";
            Symbol variable = Symbol.intern("qn");
            Symbol sv = Symbol.intern("sv");

            OMRSRepositoryHelper helper = connector.getRepositoryHelper();

            InstanceProperties ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    helper.getExactMatchRegex(base),
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

            List<IPersistentCollection> conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value "name"]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected to do an exact match.");

            IPersistentCollection candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), base, "Third element of the triple is expected to be the value to match against.");

            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    helper.getStartsWithRegex(base),
                    this.getClass().getName());
            ipv = ip.getPropertyValue(propertyName);
            conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qn], [(str qn) sv], [(clojure.string/starts-with? sv "name")]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected to do a starts-with string comparison.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), variable, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            Object piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of condition is expected to be a list.");
            IPersistentList clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
            assertEquals(clause.seq().next().first(), variable, "Second element of clause is expected to be the symbol that captured the property's value.");
            assertEquals(condition.nth(1), sv, "Second element of condition is expected to be a variable to capture the string-converted value.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed in a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STARTS_WITH, "First element of clause is expected to be the string starts-with operator.");
            assertEquals(clause.seq().next().first(), sv, "Second element of clause is expected to be the symbol that captured the property's string-converted value.");
            assertEquals(clause.seq().next().next().first(), base, "Third element of clause is expected to be the value to match against.");

            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    helper.getEndsWithRegex(base),
                    this.getClass().getName());
            ipv = ip.getPropertyValue(propertyName);
            conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    false,
                    true);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qn], [(str qn) sv], [(clojure.string/ends-with? sv "name")]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected to do an ends-with string comparison.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), variable, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of condition is expected to be a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
            assertEquals(clause.seq().next().first(), variable, "Second element of clause is expected to be the symbol that captured the property's value.");
            assertEquals(condition.nth(1), sv, "Second element of condition is expected to be a variable to capture the string-converted value.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed in a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.ENDS_WITH, "First element of clause is expected to be the string ends-with operator.");
            assertEquals(clause.seq().next().first(), sv, "Second element of clause is expected to be the symbol that captured the property's string-converted value.");
            assertEquals(clause.seq().next().next().first(), base, "Third element of clause is expected to be the value to match against.");

            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    helper.getContainsRegex(base, true),
                    this.getClass().getName());
            ipv = ip.getPropertyValue(propertyName);
            conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    false,
                    false);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qn], [(str qn) sv], [(re-matches #"(?i).*\Qname\E.*" sv)]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected to do an ends-with string comparison.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), variable, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of condition is expected to be a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
            assertEquals(clause.seq().next().first(), variable, "Second element of clause is expected to be the symbol that captured the property's value.");
            assertEquals(condition.nth(1), sv, "Second element of condition is expected to be a variable to capture the string-converted value.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed in a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), ConditionBuilder.REGEX_OPERATOR, "First element of clause is expected to be the regex operator.");
            assertTrue(clause.seq().next().first() instanceof Pattern, "Second element of clause is expected to be a regular expression pattern to match against.");
            assertEquals(((Pattern)clause.seq().next().first()).pattern(), Pattern.compile("(?i).*\\Qname\\E.*").pattern(), "Second element of clause is expected to be the pattern against which to match.");
            assertEquals(clause.seq().next().next().first(), sv, "Third element of clause is expected to be the symbol that captured the property's string-converted value.");

            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    "[" + base + "]",
                    this.getClass().getName());
            ipv = ip.getPropertyValue(propertyName);
            conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    false,
                    false);

            // Expected --> [[e :entityProperties/Referenceable.qualifiedName.value qn], [(str qn) sv], [(re-matches #"[name]" sv)]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 3, "Three conditions are expected to do an ends-with string comparison.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property to match.");
            assertEquals(condition.nth(2), variable, "Third element of the triple is expected to be a symbol to capture the value of the property.");

            candidate = conditions.get(1);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of condition is expected to be a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), TextConditionBuilder.STR_OPERATOR, "First element of clause is expected to be the string operator.");
            assertEquals(clause.seq().next().first(), variable, "Second element of clause is expected to be the symbol that captured the property's value.");
            assertEquals(condition.nth(1), sv, "Second element of condition is expected to be a variable to capture the string-converted value.");

            candidate = conditions.get(2);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Condition is expected to be a single clause.");
            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "Clause is expected to be enclosed in a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.seq().first(), ConditionBuilder.REGEX_OPERATOR, "First element of clause is expected to be the regex operator.");
            assertTrue(clause.seq().next().first() instanceof Pattern, "Second element of clause is expected to be a regular expression pattern to match against.");
            assertEquals(((Pattern)clause.seq().next().first()).pattern(), Pattern.compile("[name]").pattern(), "Second element of clause is expected to be the pattern against which to match.");
            assertEquals(clause.seq().next().next().first(), sv, "Third element of clause is expected to be the symbol that captured the property's string-converted value.");

            ip = helper.addStringPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    helper.getContainsRegex(base, true),
                    this.getClass().getName());
            ipv = ip.getPropertyValue(propertyName);
            conditions = TextConditionBuilder.buildRegexConditions(
                    propertyKeyword,
                    ipv,
                    variable,
                    connector,
                    true,
                    true);

            // Expected --> [[(text-search-ci :entityProperties/Referenceable.qualifiedName.value "*name*") [[e _]]]]
            assertNotNull(conditions);
            assertEquals(conditions.size(), 1, "One condition is expected to do a Lucene-optimized text search.");

            candidate = conditions.get(0);
            assertTrue(candidate instanceof IPersistentVector, "Condition is expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Condition is expected to be a tuple.");

            piece = condition.nth(0);
            assertTrue(piece instanceof IPersistentList, "First element of tuple is expected to be a list.");
            clause = (IPersistentList) piece;
            assertEquals(clause.count(), 3, "Clause is expected to have three elements.");
            assertEquals(clause.seq().first(), TextConditionBuilder.TEXT_SEARCH_CI, "First element of clause is expected to be the Lucene term search operator (case-insensitive).");
            assertEquals(clause.seq().next().first(), propertyKeyword, "Second element of clause is expected to be the property keyword to match against.");
            assertEquals(clause.seq().next().next().first(), "*" + base + "*", "Third element of the clause is expected to be the Lucene regex to match against.");

            piece = condition.nth(1);
            assertTrue(piece instanceof IPersistentVector, "Second element of tuple is expected to be a nested vector.");
            IPersistentVector vars = (IPersistentVector) piece;
            assertEquals(vars.length(), 1, "Vector is expected to have only a single element.");
            piece = vars.nth(0);
            assertTrue(piece instanceof IPersistentVector, "Variables for binding are expected to be nested within an inner vector.");
            vars = (IPersistentVector) piece;
            assertEquals(vars.length(), 2, "Two variables are expected to be bound.");
            assertEquals(vars.nth(0), XTDBQuery.DOC_ID, "First element is expected to be the symbol for the document ID.");
            assertEquals(vars.nth(1), TextConditionBuilder.ELIDE, "Second element is expected to be an elide.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
