/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mocks.MockConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Tests the query model.
 */
public class XTDBQueryTest
{

    private static final XTDBOMRSRepositoryConnector connector = MockConnection.getMockConnector();

    private static final Keyword find  = Keyword.intern("find");
    private static final Keyword where = Keyword.intern("where");
    private static final Keyword order = Keyword.intern("order-by");

    @Test
    void testRelationshipQuery() {
        try {

            String guid = "123";
            String ref  = EntitySummaryMapping.getReference(guid);

            XTDBQuery cq = new XTDBQuery();
            cq.addRelationshipEndpointConditions(ref);
            IPersistentMap relation = cq.getQuery();
            assertNotNull(relation);
            assertTrue(relation.containsKey(find));
            assertTrue(relation.containsKey(where));

            Object candidate = relation.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), ref, "Third element of the triple is expected to be the reference to be matched.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testTypeConditionCategoryOnly() {
        try {

            XTDBQuery cq = new XTDBQuery();
            cq.addTypeCondition(TypeDefCategory.ENTITY_DEF, null, null);

            IPersistentMap catOnly = cq.getQuery();
            assertNotNull(catOnly);
            assertTrue(catOnly.containsKey(find));
            assertTrue(catOnly.containsKey(where));

            Object candidate = catOnly.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 2, "Two conditions are expected when searching for an entity by TypeDefCategory only.");
            // Expected --> [[e :type.category 6] [e :e/proxy false]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.TYPE_DEF_CATEGORY), "Second element of triple is expected to be the keyword :type.category.");
            assertEquals(condition.nth(2), 6, "Third element of the triple is expected to be the ordinal representing the entity TypeDefCategory (6).");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER), "Second element of triple is expected to be the keyword :e/proxy.");
            assertEquals(condition.nth(2), false, "Third element of the triple is expected to be false, indicating that we should only retrieve non-proxy entities.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testTypeConditionMain() {
        try {

            String typeGuid = "123";

            XTDBQuery cq = new XTDBQuery();
            cq.addTypeCondition(TypeDefCategory.ENTITY_DEF, typeGuid, null);

            IPersistentMap mainType = cq.getQuery();
            assertNotNull(mainType);
            assertTrue(mainType.containsKey(find));
            assertTrue(mainType.containsKey(where));

            Object candidate = mainType.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 2, "Two conditions are expected when searching for an entity by a main type GUID only.");
            // Expected --> [[e :type.guids "123"] [e :e/proxy false]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.TYPE_DEF_GUIDS), "Second element of triple is expected to be the keyword :type.guids.");
            assertEquals(condition.nth(2), typeGuid, "Third element of the triple is expected to be the GUID of the types to find.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER), "Second element of triple is expected to be the keyword :e/proxy.");
            assertEquals(condition.nth(2), false, "Third element of the triple is expected to be false, indicating that we should only retrieve non-proxy entities.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testTypeConditionSubtypes() {
        try {

            String typeGuid = "123";
            Symbol types = Symbol.intern("types");
            Symbol tf = Symbol.intern("tf");

            List<String> subtypeGuids = new ArrayList<>();
            subtypeGuids.add("abc");
            subtypeGuids.add("def");

            XTDBQuery cq = new XTDBQuery();
            cq.addTypeCondition(TypeDefCategory.ENTITY_DEF, typeGuid, subtypeGuids);

            IPersistentMap subtypes = cq.getQuery();
            assertNotNull(subtypes);
            assertTrue(subtypes.containsKey(find));
            assertTrue(subtypes.containsKey(where));

            Object candidate = subtypes.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 4, "Four conditions are expected when searching for an entity by multiple subtype GUIDs.");
            // Expected --> [[e :type.guids types] [(hash-set "abc" "def") tf] [(contains? tf types)] [e :e/proxy false]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.TYPE_DEF_GUIDS), "Second element of triple is expected to be the keyword :type.guids.");
            assertEquals(condition.nth(2), types, "Third element of the triple is expected to be a symbol representing the types.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Where condition is expected to be a tuple.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.SET_OPERATOR);
            compare.addAll(subtypeGuids);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to contain an instruction followed by one or more elements.");
            candidate = condition.nth(1);
            assertTrue(candidate instanceof Symbol);
            assertEquals(candidate, tf, "Clause is expected to set the value of a new symbol.");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            compare = new ArrayList<>();
            compare.add(ConditionBuilder.IN_OPERATOR);
            compare.add(tf);
            compare.add(types);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of types contains the value being searched.");

            candidate = conditions.nth(3);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER), "Second element of triple is expected to be the keyword :e/proxy.");
            assertEquals(condition.nth(2), false, "Third element of the triple is expected to be false, indicating that we should only retrieve non-proxy entities.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testClassificationConditions() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");
            String classificationName = "Confidentiality";
            Set<String> classificationTypeNames = new HashSet<>();
            classificationTypeNames.add(classificationName);
            String propertyName = "level";
            int propertyValue = 3;

            String classificationNamespace = ClassificationMapping.getNamespaceForClassification(EntitySummaryMapping.N_CLASSIFICATIONS, classificationName);
            String propertiesNamespace = ClassificationMapping.getNamespaceForProperties(classificationNamespace);
            OMRSRepositoryHelper helper = connector.getRepositoryHelper();
            InstanceProperties ip = helper.addIntPropertyToInstance(MockConnection.SOURCE_NAME,
                    null,
                    propertyName,
                    propertyValue,
                    this.getClass().getName());
            InstancePropertyValue ipv = ip.getPropertyValue(propertyName);

            Set<Keyword> propertyKeywords = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    propertiesNamespace,
                    classificationTypeNames,
                    ipv);
            assertEquals(propertyKeywords.size(), 1, "Expected to have only a single matching property in the classification.");
            Keyword propertyKeyword = null;
            for (Keyword a : propertyKeywords) {
                propertyKeyword = a;
            }

            PropertyCondition propertyCondition = new PropertyCondition();
            propertyCondition.setProperty(propertyName);
            propertyCondition.setOperator(PropertyComparisonOperator.EQ);
            propertyCondition.setValue(ipv);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(propertyCondition);
            SearchProperties matchProperties = new SearchProperties();
            matchProperties.setConditions(propertyConditions);

            List<ClassificationCondition> classificationConditions = new ArrayList<>();
            ClassificationCondition classificationCondition = new ClassificationCondition();
            classificationCondition.setName(classificationName);
            classificationCondition.setMatchProperties(matchProperties);
            classificationConditions.add(classificationCondition);

            SearchClassifications searchClassifications = new SearchClassifications();
            searchClassifications.setConditions(classificationConditions);

            XTDBQuery cq = new XTDBQuery();
            cq.addClassificationConditions(searchClassifications,
                    typeNames,
                    connector,
                    false,
                    true);

            IPersistentMap byClassification = cq.getQuery();
            assertNotNull(byClassification);
            assertTrue(byClassification.containsKey(find));
            assertTrue(byClassification.containsKey(where));

            Object candidate = byClassification.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 2, "Two conditions are expected when searching for a classification by name and property.");
            // Expected --> [[e :classifications "Confidentiality"] [e :classifications.Confidentiality.classificationProperties/Confidentiality.level.value 3]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(EntitySummaryMapping.N_CLASSIFICATIONS), "Second element of triple is expected to be the keyword for classifications.");
            assertEquals(condition.nth(2), classificationName, "Third element of the triple is expected to be the name of the classification to find.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), propertyKeyword, "Second element of triple is expected to be the keyword for the property being searched.");
            assertEquals(condition.nth(2), propertyValue, "Third element of the triple is expected to be the value to compare against.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testStatusLimitersDefault() {
        try {

            Symbol currentStatus = Symbol.intern("e_currentStatus");

            XTDBQuery cq = new XTDBQuery();
            cq.addStatusLimiters(null, XTDBQuery.DOC_ID);

            IPersistentMap limitByStatus = cq.getQuery();
            assertNotNull(limitByStatus);
            assertTrue(limitByStatus.containsKey(find));
            assertTrue(limitByStatus.containsKey(where));

            Object candidate = limitByStatus.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 2, "Two conditions are expected when limiting results by non-DELETE status.");
            // Expected --> [[e :currentStatus currentStatus] [(not= currentStatus 99)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the current status property.");
            assertEquals(condition.nth(2), currentStatus, "Third element of the triple is expected to be a symbol to capture the current status.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Nested clause is expected to be enclosed in a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain 3 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.NEQ_OPERATOR);
            compare.add(currentStatus);
            compare.add(InstanceStatus.DELETED.getOrdinal());
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to contain an instruction followed by 2 elements.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testStatusLimiters() {
        try {

            Symbol currentStatus = Symbol.intern("status");
            Symbol sf = Symbol.intern("sf");
            List<InstanceStatus> limitBy = new ArrayList<>();
            limitBy.add(InstanceStatus.ACTIVE);
            limitBy.add(InstanceStatus.DELETED);

            XTDBQuery cq = new XTDBQuery();
            cq.addStatusLimiters(limitBy, XTDBQuery.DOC_ID);

            IPersistentMap limitByStatus = cq.getQuery();
            assertNotNull(limitByStatus);
            assertTrue(limitByStatus.containsKey(find));
            assertTrue(limitByStatus.containsKey(where));

            Object candidate = limitByStatus.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 3, "Three conditions are expected when limiting results by an explicit list of statuses.");
            // Expected --> [[e "currentStatus" status] [(hash-set 15 99) sf] [(contains? sf status)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the current status property.");
            assertEquals(condition.nth(2), currentStatus, "Third element of the triple is expected to be a symbol to capture the current status.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Where condition is expected to be a tuple.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.SET_OPERATOR);
            for (InstanceStatus limit : limitBy) {
                compare.add(limit.getOrdinal());
            }
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to contain an instruction followed by one or more elements.");
            candidate = condition.nth(1);
            assertTrue(candidate instanceof Symbol);
            assertEquals(candidate, sf, "Clause is expected to set the value of a new symbol.");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain 3 elements.");
            compare = new ArrayList<>();
            compare.add(ConditionBuilder.IN_OPERATOR);
            compare.add(sf);
            compare.add(currentStatus);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that one of the statuses in the set matches the current status of a document.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testSequencingDefault() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            XTDBQuery cq = new XTDBQuery();
            cq.addSequencing(null,
                    null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            IPersistentMap sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertFalse(sequencing.containsKey(order));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testSequencingUpdate() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            XTDBQuery cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.LAST_UPDATE_OLDEST,
                    null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            IPersistentMap sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            Object candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            IPersistentVector criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by update date.");
            // Expected --> [[ut :asc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            IPersistentVector criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.UPDATE_TIME, "First element of tuple is expected to be the update time symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_ASCENDING, "Second element of tuple is expected to be the keyword for ascending sort.");

            validateSequencingConditions(sequencing,
                                         Keyword.intern(InstanceAuditHeaderMapping.UPDATE_TIME),
                                         XTDBQuery.UPDATE_TIME);

            cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.LAST_UPDATE_RECENT,
                    null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by update date.");
            // Expected --> [[ut :desc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.UPDATE_TIME, "First element of tuple is expected to be the update time symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_DESCENDING, "Second element of tuple is expected to be the keyword for descending sort.");

            validateSequencingConditions(sequencing,
                                         Keyword.intern(InstanceAuditHeaderMapping.UPDATE_TIME),
                                         XTDBQuery.UPDATE_TIME);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testSequencingCreate() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");

            XTDBQuery cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.CREATION_DATE_OLDEST,
                    null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            IPersistentMap sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            Object candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            IPersistentVector criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by creation date.");
            // Expected --> [[ct :asc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            IPersistentVector criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.CREATE_TIME, "First element of tuple is expected to be the creation time symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_ASCENDING, "Second element of tuple is expected to be the keyword for ascending sort.");

            validateSequencingConditions(sequencing,
                                         Keyword.intern(InstanceAuditHeaderMapping.CREATE_TIME),
                                         XTDBQuery.CREATE_TIME);

            cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.CREATION_DATE_RECENT,
                    null,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by creation date.");
            // Expected --> [[ct :desc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.CREATE_TIME, "First element of tuple is expected to be the creation time symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_DESCENDING, "Second element of tuple is expected to be the keyword for descending sort.");

            validateSequencingConditions(sequencing,
                                         Keyword.intern(InstanceAuditHeaderMapping.CREATE_TIME),
                                         XTDBQuery.CREATE_TIME);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testSequencingProperty() {
        try {

            Set<String> typeNames = new HashSet<>();
            typeNames.add("Referenceable");
            String propertyName = "qualifiedName";
            Set<Keyword> qualifiedPropertyNames = InstancePropertyValueMapping.getKeywordsForProperty(connector,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    null);
            assertEquals(qualifiedPropertyNames.size(), 1, "Expected to have only a single qualified property name for Referenceable's qualifiedName.");
            Keyword propertyKeyword = null;
            for (Keyword a : qualifiedPropertyNames) {
                propertyKeyword = a;
            }

            XTDBQuery cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.PROPERTY_ASCENDING,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            IPersistentMap sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            Object candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            IPersistentVector criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by property.");
            // Expected --> [[sp :asc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            IPersistentVector criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.SORT_PROPERTY, "First element of tuple is expected to be the property symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_ASCENDING, "Second element of tuple is expected to be the keyword for ascending sort.");

            validateSequencingConditions(sequencing,
                                         propertyKeyword,
                                         XTDBQuery.SORT_PROPERTY);

            cq = new XTDBQuery();
            cq.addSequencing(SequencingOrder.PROPERTY_DESCENDING,
                    propertyName,
                    EntityDetailMapping.ENTITY_PROPERTIES_NS,
                    typeNames,
                    connector);

            sequencing = cq.getQuery();
            assertNotNull(sequencing);
            assertTrue(sequencing.containsKey(find));
            assertTrue(sequencing.containsKey(where));
            assertTrue(sequencing.containsKey(order));

            candidate = sequencing.valAt(order);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are expected to be enclosed in an outer vector.");
            criteria = (IPersistentVector) candidate;
            assertEquals(criteria.length(), 1, "One criterion is expected when sequencing by property.");
            // Expected --> [[sp :desc]]

            candidate = criteria.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Sequencing criteria are are expected to be enclosed in an inner vector.");
            criterion = (IPersistentVector) candidate;
            assertEquals(criterion.length(), 2, "Sequencing criterion is expected to be a tuple.");
            assertEquals(criterion.nth(0), XTDBQuery.SORT_PROPERTY, "First element of tuple is expected to be the property symbol.");
            assertEquals(criterion.nth(1), XTDBQuery.SORT_DESCENDING, "Second element of tuple is expected to be the keyword for descending sort.");

            validateSequencingConditions(sequencing,
                                         propertyKeyword,
                                         XTDBQuery.SORT_PROPERTY);

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    private void validateSequencingConditions(IPersistentMap query,
                                              Keyword property,
                                              Symbol variable) {

        // Also ensure that we have query conditions to retrieve the update time
        Object candidate = query.valAt(find);
        assertTrue(candidate instanceof IPersistentVector);
        IPersistentVector criteria = (IPersistentVector) candidate;
        assertEquals(criteria.length(), 2, "Two properties are expected when sequencing by update date.");
        assertEquals(criteria.nth(0), XTDBQuery.DOC_ID, "First property is always expected to be the document ID symbol.");
        assertEquals(criteria.nth(1), variable, "Second property is expected to be a variable symbol representing the sequencing property.");

        candidate = query.valAt(where);
        assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
        IPersistentVector conditions = (IPersistentVector) candidate;
        assertEquals(conditions.length(), 1, "One conditions is expected when sequencing by a property.");
        candidate = conditions.nth(0);
        assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
        IPersistentVector condition = (IPersistentVector) candidate;
        assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
        assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the document ID symbol.");
        assertEquals(condition.nth(1), property, "Second element of triple is expected to be the keyword for the property used for sequencing.");
        assertEquals(condition.nth(2), variable, "Third element of the triple is expected to be the symbol used to capture the value of the property used for sequencing.");

    }

    @Test
    void testEmpty() {
        try {

            XTDBQuery      cq    = new XTDBQuery();
            IPersistentMap empty = cq.getQuery();
            assertNotNull(empty);
            assertTrue(empty.containsKey(find));
            assertTrue(empty.containsKey(where));

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
