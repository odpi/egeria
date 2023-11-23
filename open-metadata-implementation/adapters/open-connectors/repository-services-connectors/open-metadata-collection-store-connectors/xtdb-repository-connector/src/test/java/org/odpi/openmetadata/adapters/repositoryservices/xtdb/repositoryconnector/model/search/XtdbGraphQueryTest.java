/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Tests the graph query model.
 */
public class XtdbGraphQueryTest {

    private static final Keyword find  = Keyword.intern("find");
    private static final Keyword where = Keyword.intern("where");

    @Test
    void testRelationshipLimitersNull() {
        try {

            String guid = "123";
            String ref  = EntitySummaryMapping.getReference(guid);

            XTDBGraphQuery cgq = new XTDBGraphQuery();
            cgq.addRelationshipLimiters(guid, null, null);
            IPersistentMap relation = cgq.getQuery();
            assertNotNull(relation);
            assertTrue(relation.containsKey(find));
            assertTrue(relation.containsKey(where));

            Object candidate = relation.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 4, "Four conditions are expected for limiting by relationships without status or type.");
            // Expected --> [[r :entityProxies e] [r :entityProxies "e_123"] [r :currentStatus r_currentStatus] [(not= r_currentStatus 99)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), XTDBQuery.DOC_ID, "Third element of the triple is expected to be the document ID symbol.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), ref, "Third element of the triple is expected to be the entity reference to match.");

            Symbol status = Symbol.intern("r_currentStatus");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the status property.");
            assertEquals(condition.nth(2), status, "Third element of the triple is expected to be the symbol to capture the status.");

            candidate = conditions.nth(3);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.NEQ_OPERATOR);
            compare.add(status);
            compare.add(99);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of status is not deleted (99).");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testRelationshipLimitersType() {
        try {

            String guid = "123";
            String ref  = EntitySummaryMapping.getReference(guid);

            List<String> types = new ArrayList<>();
            types.add("e6670973-645f-441a-bec7-6f5570345b92");

            XTDBGraphQuery cgq = new XTDBGraphQuery();
            cgq.addRelationshipLimiters(guid, types, null);
            IPersistentMap relation = cgq.getQuery();
            assertNotNull(relation);
            assertTrue(relation.containsKey(find));
            assertTrue(relation.containsKey(where));

            Object candidate = relation.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 5, "Five conditions are expected for limiting by relationships by type.");
            // Expected --> [[r :entityProxies e] [r :entityProxies "e_123"] [r :type.guids "e6670973-645f-441a-bec7-6f5570345b92"] [r :currentStatus r_currentStatus] [(not= r_currentStatus 99)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), XTDBQuery.DOC_ID, "Third element of the triple is expected to be the document ID symbol.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), ref, "Third element of the triple is expected to be the entity reference to match.");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.TYPE_DEF_GUIDS), "Second element of triple is expected to be the keyword for the typeDef GUIDs.");
            assertEquals(condition.nth(2), types.get(0), "Third element of the triple is expected to be the relationship typeDef GUID to match.");

            Symbol status = Symbol.intern("r_currentStatus");

            candidate = conditions.nth(3);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the status property.");
            assertEquals(condition.nth(2), status, "Third element of the triple is expected to be the symbol to capture the status.");

            candidate = conditions.nth(4);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.NEQ_OPERATOR);
            compare.add(status);
            compare.add(99);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of status is not deleted (99).");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testRelationshipLimitersStatus() {
        try {

            String guid = "123";
            String ref  = EntitySummaryMapping.getReference(guid);
            Symbol status = Symbol.intern("status");
            Symbol sf = Symbol.intern("sf");

            List<InstanceStatus> statuses = new ArrayList<>();
            statuses.add(InstanceStatus.ACTIVE);
            statuses.add(InstanceStatus.DELETED);

            XTDBGraphQuery cgq = new XTDBGraphQuery();
            cgq.addRelationshipLimiters(guid, null, statuses);
            IPersistentMap relation = cgq.getQuery();
            assertNotNull(relation);
            assertTrue(relation.containsKey(find));
            assertTrue(relation.containsKey(where));

            Object candidate = relation.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 5, "Five conditions are expected for limiting by relationships by one of multiple statuses.");
            // Expected --> [[r :entityProxies e] [r :entityProxies "e_123"] [r :currentStatus status] [(hash-set 15 99) sf] [(contains? sf status)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), XTDBQuery.DOC_ID, "Third element of the triple is expected to be the document ID symbol.");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(RelationshipMapping.ENTITY_PROXIES), "Second element of triple is expected to be the keyword :entityProxies.");
            assertEquals(condition.nth(2), ref, "Third element of the triple is expected to be the entity reference to match.");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.RELATIONSHIP, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the status property.");
            assertEquals(condition.nth(2), status, "Third element of the triple is expected to be the symbol to capture the status.");

            candidate = conditions.nth(3);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 2, "Where condition is expected to be a tuple.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.SET_OPERATOR);
            for (InstanceStatus a : statuses) {
                compare.add(a.getOrdinal());
            }
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to contain an instruction followed by one or more elements.");
            candidate = condition.nth(1);
            assertTrue(candidate instanceof Symbol);
            assertEquals(candidate, sf, "Clause is expected to set the value of a new symbol.");

            candidate = conditions.nth(4);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            compare = new ArrayList<>();
            compare.add(ConditionBuilder.IN_OPERATOR);
            compare.add(sf);
            compare.add(status);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of statuses contains the value being searched.");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEntityLimitersType() {
        try {

            List<String> types = new ArrayList<>();
            types.add("a32316b8-dc8c-48c5-b12b-71c1b2a080bf");

            XTDBGraphQuery cgq = new XTDBGraphQuery();
            cgq.addEntityLimiters(types, null, null);
            IPersistentMap entities = cgq.getQuery();
            assertNotNull(entities);
            assertTrue(entities.containsKey(find));
            assertTrue(entities.containsKey(where));

            Object candidate = entities.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 3, "Three conditions are expected for limiting entities by a single type.");
            // Expected --> [[e :type.guids "a32316b8-dc8c-48c5-b12b-71c1b2a080bf"] [e :currentStatus e_currentStatus] [(not= e_currentStatus 99)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.TYPE_DEF_GUIDS), "Second element of triple is expected to be the keyword for typeDef GUIDs.");
            assertEquals(condition.nth(2), types.get(0), "Third element of the triple is expected to be the value of the typeDef GUID by which to limit.");

            Symbol status = Symbol.intern("e_currentStatus");

            candidate = conditions.nth(1);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.DOC_ID, "First element of triple is expected to be the entity ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the status property.");
            assertEquals(condition.nth(2), status, "Third element of the triple is expected to be the symbol to capture the status.");

            candidate = conditions.nth(2);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            IPersistentList clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            List<Object> compare = new ArrayList<>();
            compare.add(ConditionBuilder.NEQ_OPERATOR);
            compare.add(status);
            compare.add(99);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of status is not deleted (99).");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    void testEntityLimitersClassification() {
        try {

            List<String> classificationNames = new ArrayList<>();
            classificationNames.add("Confidentiality");
            classificationNames.add("MobileAsset");
            Symbol classification = Symbol.intern("classification");
            Symbol cf = Symbol.intern("cf");

            XTDBGraphQuery cgq = new XTDBGraphQuery();
            cgq.addEntityLimiters(null, classificationNames, null);
            IPersistentMap entities = cgq.getQuery();
            assertNotNull(entities);
            assertTrue(entities.containsKey(find));
            assertTrue(entities.containsKey(where));

            Object candidate = entities.valAt(where);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are expected to be enclosed in an outer vector.");
            IPersistentVector conditions = (IPersistentVector) candidate;
            assertEquals(conditions.length(), 5, "Five conditions are expected for limiting entities by multiple classifications.");
            // Expected --> [[e :classifications classification] [(hash-set "Confidentiality" "MobileAsset") cf] [(contains? cf classification)] [e :currentStatus e_currentStatus] [(not= e_currentStatus 99)]]

            candidate = conditions.nth(0);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            IPersistentVector condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBQuery.DOC_ID, "First element of triple is expected to be the relationship ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(EntitySummaryMapping.N_CLASSIFICATIONS), "Second element of triple is expected to be the keyword for classifications.");
            assertEquals(condition.nth(2), classification, "Third element of the triple is expected to be a symbol to capture the classification.");

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
            compare.addAll(classificationNames);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to contain an instruction followed by one or more elements.");
            candidate = condition.nth(1);
            assertTrue(candidate instanceof Symbol);
            assertEquals(candidate, cf, "Clause is expected to set the value of a new symbol.");

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
            compare.add(cf);
            compare.add(classification);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of classifications contains the value being searched.");

            Symbol status = Symbol.intern("e_currentStatus");

            candidate = conditions.nth(3);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 3, "Where condition is expected to be a triple.");
            assertEquals(condition.nth(0), XTDBGraphQuery.DOC_ID, "First element of triple is expected to be the entity ID symbol.");
            assertEquals(condition.nth(1), Keyword.intern(InstanceHeaderMapping.CURRENT_STATUS), "Second element of triple is expected to be the keyword for the status property.");
            assertEquals(condition.nth(2), status, "Third element of the triple is expected to be the symbol to capture the status.");

            candidate = conditions.nth(4);
            assertTrue(candidate instanceof IPersistentVector, "Where conditions are are expected to be enclosed in an inner vector.");
            condition = (IPersistentVector) candidate;
            assertEquals(condition.length(), 1, "Where condition is expected to be a single list.");
            candidate = condition.nth(0);
            assertTrue(candidate instanceof IPersistentList, "Clause is expected to begin with a list.");
            clause = (IPersistentList) candidate;
            assertEquals(clause.count(), 3, "Clause list is expected to contain at least 2 elements.");
            compare = new ArrayList<>();
            compare.add(ConditionBuilder.NEQ_OPERATOR);
            compare.add(status);
            compare.add(99);
            assertEquals(clause, PersistentList.create(compare), "Clause is expected to check that the set of status is not deleted (99).");

        } catch (Exception e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

}
