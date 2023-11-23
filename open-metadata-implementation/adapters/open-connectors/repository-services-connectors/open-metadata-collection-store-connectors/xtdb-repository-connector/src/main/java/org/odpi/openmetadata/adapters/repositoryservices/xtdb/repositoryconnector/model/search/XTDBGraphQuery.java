/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntitySummaryMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Captures the structure of a query against XTDB that spans the graph, and therefore could return
 * both relationships and entities together.
 */
public class XTDBGraphQuery extends XTDBQuery
{

    public static final Symbol RELATIONSHIP = Symbol.intern("r");
    private static final Keyword ENTITY_PROXIES = Keyword.intern(RelationshipMapping.ENTITY_PROXIES);

    /**
     * Default constructor for a new query.
     */
    public XTDBGraphQuery() {
        super();
    }

    /**
     * Add condition(s) to limit the resulting relationships by the provided lists.
     * @param rootEntityGUID by which to narrow the relationships
     * @param relationshipTypeGUIDs of relationship type definition GUIDs by which to limit the results
     * @param limitResultsByStatus of relationship statuses by which to limit the results
     */
    public void addRelationshipLimiters(String rootEntityGUID, List<String> relationshipTypeGUIDs, List<InstanceStatus> limitResultsByStatus) {
        addFindElement(RELATIONSHIP);
        // [r :entityProxies e] [r :entityProxies "e_..."]
        conditions.add(getRelatedToCondition());
        conditions.add(getRelatedToCondition(EntitySummaryMapping.getReference(rootEntityGUID)));
        if (relationshipTypeGUIDs != null && !relationshipTypeGUIDs.isEmpty()) {
            // [r :type.guids ...]
            conditions.addAll(getTypeCondition(RELATIONSHIP, TypeDefCategory.RELATIONSHIP_DEF, null, relationshipTypeGUIDs));
        }
        addStatusLimiters(limitResultsByStatus, RELATIONSHIP);
    }

    /**
     * Add condition(s) to limit the resulting entities by the provided criteria.
     * @param entityTypeGUIDs entity type definitions by which to limit
     * @param limitResultsByClassification entity classifications by which to limit
     * @param limitResultsByStatus of entity statuses by which to limit the results
     */
    public void addEntityLimiters(List<String> entityTypeGUIDs, List<String> limitResultsByClassification, List<InstanceStatus> limitResultsByStatus) {
        if (entityTypeGUIDs != null && !entityTypeGUIDs.isEmpty()) {
            // [e :type.guids ...]
            conditions.addAll(getTypeCondition(DOC_ID, TypeDefCategory.ENTITY_DEF, null, entityTypeGUIDs));
        }
        if (limitResultsByClassification != null && !limitResultsByClassification.isEmpty()) {
            // [e :classifications ...]   ;; single classification, or for multiple:
            // [e :classifications classification] [(hash-set "..." "..." ...) cf] [(contains? cf classification)]
            conditions.addAll(getClassificationConditions(limitResultsByClassification));
        }
        addStatusLimiters(limitResultsByStatus, DOC_ID);
    }

    /**
     * Add condition(s) to limit the resulting entities by the provided classifications.
     * @param limitByClassifications of classifications on which to limit (must be at least one)
     * @return {@code List<IPersistentCollection>} of the conditions
     */
    protected List<IPersistentCollection> getClassificationConditions(List<String> limitByClassifications) {

        List<IPersistentCollection> classificationConditions = new ArrayList<>();
        Keyword classificationsRef = Keyword.intern(EntitySummaryMapping.N_CLASSIFICATIONS);

        if (limitByClassifications.size() == 1) {
            // If we need only match a single classification, add it directly
            classificationConditions.add(PersistentVector.create(DOC_ID, classificationsRef, limitByClassifications.get(0)));
        } else {

            // Otherwise, create a set of conditions looking up against a hash-set
            Symbol setVar = Symbol.intern("cf");
            Symbol classificationVar = Symbol.intern("classification");
            // [e :classifications classification]
            classificationConditions.add(PersistentVector.create(DOC_ID, classificationsRef, classificationVar));

            List<Object> set = new ArrayList<>();
            set.add(ConditionBuilder.SET_OPERATOR);
            set.addAll(limitByClassifications);
            // [(hash-set "..." "..." ...) cf]
            classificationConditions.add(PersistentVector.create(PersistentList.create(set), setVar));

            List<Object> contains = new ArrayList<>();
            contains.add(Symbol.intern("contains?"));
            contains.add(setVar);
            contains.add(classificationVar);
            // [(contains? cf classification)]
            classificationConditions.add(PersistentVector.create(PersistentList.create(contains)));

        }

        return classificationConditions;

    }

    /**
     * Add a condition to match the value of a property to a variable.
     * @return PersistentVector for the condition
     */
    protected PersistentVector getRelatedToCondition() {
        return PersistentVector.create(RELATIONSHIP, ENTITY_PROXIES, DOC_ID);
    }

    /**
     * Add a condition to match the value of a property to a literal.
     * @param literal to match
     * @return PersistentVector for the condition
     */
    protected PersistentVector getRelatedToCondition(String literal) {
        return PersistentVector.create(RELATIONSHIP, ENTITY_PROXIES, literal);
    }

}
