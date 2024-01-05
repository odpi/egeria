/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.*;

/**
 * Captures the structure of a query against XTDB.
 */
public class XTDBQuery
{

    // Variable names (for sorting)
    public static final Symbol DOC_ID = Symbol.intern("e");
    public static final Symbol CREATE_TIME = Symbol.intern("ct");
    public static final Symbol UPDATE_TIME = Symbol.intern("ut");
    public static final Symbol SORT_PROPERTY = Symbol.intern("sp");

    // Sort orders
    public static final Keyword SORT_ASCENDING = Keyword.intern("asc");
    public static final Keyword SORT_DESCENDING = Keyword.intern("desc");

    private IPersistentMap query;
    private final List<Symbol> findElements;
    protected final List<IPersistentCollection> conditions;
    protected final List<IPersistentCollection> rules;
    private final List<IPersistentVector> sequencing;

    /**
     * Default constructor for a new query.
     */
    public XTDBQuery() {
        query = PersistentArrayMap.EMPTY;
        findElements = new ArrayList<>();
        findElements.add(DOC_ID); // Always have the DocID itself as the first element, to ease parsing of results
        conditions = new ArrayList<>();
        rules = new ArrayList<>();
        sequencing = new ArrayList<>();
    }

    /**
     * Add a condition to match either endpoint of a relationship to the provided reference (primary key).
     * @param reference the primary key value of an entity, used to match either end of a relationship
     */
    public void addRelationshipEndpointConditions(String reference) {
        conditions.add(getReferenceCondition(Keyword.intern(RelationshipMapping.ENTITY_PROXIES), reference));
    }

    /**
     * Add a condition to match the value of a property to a reference (primary key).
     * @param property to match
     * @param reference the primary key value to which the property should refer
     * @return PersistentVector for the condition
     */
    protected PersistentVector getReferenceCondition(Keyword property, String reference) {
        return PersistentVector.create(DOC_ID, property, reference);
    }

    /**
     * Add a condition to limit the type of the results by their TypeDef GUID.
     * @param category by which to limit results
     * @param typeGuid by which to limit the results (if null, will be ignored)
     * @param subtypeLimits limit the results to only these subtypes (if provided: ignored if typeGuid is null)
     */
    public void addTypeCondition(TypeDefCategory category, String typeGuid, List<String> subtypeLimits) {
        conditions.addAll(getTypeCondition(DOC_ID, category, typeGuid, subtypeLimits));
        // And if we are searching for entities, enforce that we retrieve only full entities (not proxies)
        if (category.equals(TypeDefCategory.ENTITY_DEF)) {
            conditions.add(PersistentVector.create(DOC_ID, Keyword.intern(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER), false));
        }
    }

    /**
     * Add a condition to limit the type of the results by their TypeDef GUID.
     * @param variable to resolve against the type
     * @param category by which to limit results
     * @param typeGuid by which to limit the results
     * @param subtypeLimits limit the results to only these subtypes (if provided)
     * @return {@code List<IPersistentCollection>} of the conditions
     */
    protected List<IPersistentCollection> getTypeCondition(Symbol variable, TypeDefCategory category, String typeGuid, List<String> subtypeLimits) {
        List<IPersistentCollection> typeConditions = new ArrayList<>();
        if (subtypeLimits != null && !subtypeLimits.isEmpty()) {
            // If subtypes were specified, search only for those (explicitly)
            if (subtypeLimits.size() == 1) {
                // If there is only one, set a condition against that directly
                typeConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_GUIDS), subtypeLimits.get(0)));
            } else {

                // If there are multiple, build a hash-set against which to compare
                Symbol setVar = Symbol.intern("tf");
                Symbol typeVar = Symbol.intern("types");
                // [e :type.guids types]
                typeConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_GUIDS), typeVar));

                List<Object> set = new ArrayList<>();
                set.add(ConditionBuilder.SET_OPERATOR);
                set.addAll(subtypeLimits);
                // [(hash-set "..." "..." ...) tf]
                typeConditions.add(PersistentVector.create(PersistentList.create(set), setVar));

                List<Object> contains = new ArrayList<>();
                contains.add(Symbol.intern("contains?"));
                contains.add(setVar);
                contains.add(typeVar);
                // [(contains? tf types)]
                typeConditions.add(PersistentVector.create(PersistentList.create(contains)));

            }
        } else if (typeGuid != null) {
            // Otherwise, if there is a typeGuid, search for any matches against the typeGuid exactly or where it is a supertype
            typeConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_GUIDS), typeGuid));
        } else {
            // If a type GUID has not even been provided, then fallback to only limiting based on whether we want
            // instances of entities or relationships (but leave out if we have type GUIDs, as this is otherwise redundant)
            typeConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_CATEGORY), category.getOrdinal()));
        }
        return typeConditions;
    }

    /**
     * Add the provided list of conditions to those to be included in the query.
     * @param xtdbConditions list of conditions to add
     */
    public void addConditions(List<IPersistentCollection> xtdbConditions) {
        if (xtdbConditions != null) {
            conditions.addAll(xtdbConditions);
        }
    }

    /**
     * Retrieve the set of conditions appropriate to XTDB for the provided Egeria conditions.
     * @param searchProperties to translate
     * @param namespace by which to qualify properties
     * @param typeNames of all of the types we are including in the search
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     */
    public void addPropertyConditions(SearchProperties searchProperties,
                                      String namespace,
                                      Set<String> typeNames,
                                      XTDBOMRSRepositoryConnector xtdbConnector,
                                      boolean luceneEnabled,
                                      boolean luceneRegexes) {
        List<IPersistentCollection> xtdbConditions = ConditionBuilder.buildPropertyConditions(
                searchProperties,
                namespace,
                false,
                typeNames,
                xtdbConnector,
                luceneEnabled,
                luceneRegexes
        );
        addConditions(xtdbConditions);
    }

    /**
     * Retrieve the set of conditions appropriate to XTDB for the provided Egeria conditions.
     * @param searchClassifications to translate
     * @param typeNames of all of the types we are including in the search
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     */
    public void addClassificationConditions(SearchClassifications searchClassifications,
                                            Set<String> typeNames,
                                            XTDBOMRSRepositoryConnector xtdbConnector,
                                            boolean luceneEnabled,
                                            boolean luceneRegexes) {
        List<IPersistentCollection> xtdbConditions = getClassificationConditions(
                searchClassifications,
                typeNames,
                xtdbConnector,
                luceneEnabled,
                luceneRegexes
        );
        addConditions(xtdbConditions);
    }

    /**
     * Retrieve a set of translated XTDB conditions appropriate to the provided Egeria conditions.
     * @param searchClassifications to translate
     * @param typeNames of all of the types we are including in the search
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>}
     */
    protected List<IPersistentCollection> getClassificationConditions(SearchClassifications searchClassifications,
                                                                      Set<String> typeNames,
                                                                      XTDBOMRSRepositoryConnector xtdbConnector,
                                                                      boolean luceneEnabled,
                                                                      boolean luceneRegexes) {
        if (searchClassifications != null) {
            // Since classifications can only be applied to entities, we can draw the namespace directly
            String namespace = EntitySummaryMapping.N_CLASSIFICATIONS;
            List<ClassificationCondition> classificationConditions = searchClassifications.getConditions();
            MatchCriteria matchCriteria = searchClassifications.getMatchCriteria();
            if (classificationConditions != null && !classificationConditions.isEmpty()) {
                List<IPersistentCollection> allConditions = new ArrayList<>();
                for (ClassificationCondition condition : classificationConditions) {
                    String classificationName = condition.getName();
                    // TODO: if there are multiple classification names, is there a risk this is interpreted as an AND
                    //  that the entity needs to possess ALL of these classifications (irrespective of the MatchCriteria)?
                    allConditions.add(PersistentVector.create(DOC_ID, Keyword.intern(namespace), classificationName));
                    String qualifiedNamespace = ClassificationMapping.getNamespaceForClassification(namespace, classificationName);
                    List<IPersistentCollection> matchConditions = ConditionBuilder.buildPropertyConditions(
                            condition.getMatchProperties(),
                            qualifiedNamespace,
                            matchCriteria.equals(MatchCriteria.ANY),
                            typeNames,
                            xtdbConnector,
                            luceneEnabled,
                            luceneRegexes
                    );
                    if (matchConditions != null) {
                        allConditions.addAll(matchConditions);
                    }
                }
                return allConditions;
            }
        }
        return null;
    }

    /**
     * Add the provided statuses as limiters on which results should be retrieved from the query.
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param toLimit variable by which to limit by status (e.g. entity or relationship)
     */
    public void addStatusLimiters(List<InstanceStatus> limitResultsByStatus, Symbol toLimit) {
        if (limitResultsByStatus != null && !limitResultsByStatus.isEmpty()) {
            List<IPersistentCollection> statusConditions = getStatusLimiters(toLimit, limitResultsByStatus);
            if (statusConditions != null && !statusConditions.isEmpty()) {
                conditions.addAll(statusConditions);
            }
        } else {
            // If no status limit was specified, retrieve only non-DELETED objects
            Integer deleted = EnumPropertyValueMapping.getOrdinalForInstanceStatus(InstanceStatus.DELETED);
            Symbol variable = Symbol.intern(toLimit.toString() + "_" + InstanceAuditHeaderMapping.CURRENT_STATUS);
            Keyword propertyRef = Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS);
            conditions.add(PersistentVector.create(toLimit, propertyRef, variable));
            List<Object> predicateComparison = new ArrayList<>();
            predicateComparison.add(ConditionBuilder.NEQ_OPERATOR);
            predicateComparison.add(variable);
            predicateComparison.add(deleted);
            conditions.add(PersistentVector.create(PersistentList.create(predicateComparison)));
        }
    }

    /**
     * Retrieve the status condition(s) for the provided status limiters.
     * @param variable that should be limited
     * @param limitResultsByStatus list of statuses by which to limit results
     * @return {@code List<IPersistentCollection>} of the condition(s)
     */
    protected List<IPersistentCollection> getStatusLimiters(Symbol variable, List<InstanceStatus> limitResultsByStatus) {

        List<IPersistentCollection> statusConditions = new ArrayList<>();
        List<Integer> ordinals = new ArrayList<>();

        for (InstanceStatus limitByStatus : limitResultsByStatus) {
            Integer ordinal = EnumPropertyValueMapping.getOrdinalForInstanceStatus(limitByStatus);
            if (ordinal != null) {
                ordinals.add(ordinal);
            }
        }

        if (!ordinals.isEmpty()) {
            if (ordinals.size() == 1) {
                // If there is only a single status, set it as the sole condition
                statusConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS), ordinals.get(0)));
            } else {
                // Otherwise, create a set of conditions looking up against a hash-set
                Symbol setVar = Symbol.intern("sf");
                Symbol statusVar = Symbol.intern("status");
                // [e :currentStatus status]
                statusConditions.add(PersistentVector.create(variable, Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS), statusVar));

                List<Object> set = new ArrayList<>();
                set.add(ConditionBuilder.SET_OPERATOR);
                set.addAll(ordinals);
                // [(hash-set 1 2 ...) sf]
                statusConditions.add(PersistentVector.create(PersistentList.create(set), setVar));

                List<Object> contains = new ArrayList<>();
                contains.add(Symbol.intern("contains?"));
                contains.add(setVar);
                contains.add(statusVar);
                // [(contains? sf status)]
                statusConditions.add(PersistentVector.create(PersistentList.create(contains)));

            }
        }

        return statusConditions;

    }

    /**
     * Add the sequencing information onto the query.
     * @param sequencingOrder by which to sequence the results
     * @param sequencingProperty by which to sequence the results (required if sorting by property, otherwise ignored)
     * @param namespace by which to qualify the sorting property (required if sorting by property, otherwise ignored)
     * @param typeNames of all of the types we are including in the search (required if sorting by property, otherwise ignored)
     * @param xtdbConnector connectivity to the repository
     */
    public void addSequencing(SequencingOrder sequencingOrder,
                              String sequencingProperty,
                              String namespace,
                              Set<String> typeNames,
                              XTDBOMRSRepositoryConnector xtdbConnector) {
        final String methodName = "addSequencing";
        Set<Keyword> qualifiedSortProperties = null;
        if (sequencingOrder != null) {
            // Only proceed with sorting if some order has been defined, otherwise do not attempt to sort
            // the results (just use XTDB's consistent, undefined sort order for the results)
            if (sequencingProperty != null) {
                // Translate the provided sequencingProperty name into all of its possible appropriate property name
                // references (depends on the type limiting used for the search)
                qualifiedSortProperties = InstancePropertyValueMapping.getKeywordsForProperty(xtdbConnector, sequencingProperty, namespace, typeNames, null);
            }
            // Note: for sorting by anything other than document ID we need to ensure we also add the
            // element to the conditions and sequence (unless there already as part of another search criteria), hence the
            // 'addFindElement' logic.
            switch (sequencingOrder) {
                case LAST_UPDATE_OLDEST:
                    addFindElement(UPDATE_TIME);
                    conditions.add(PersistentVector.create(DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.UPDATE_TIME), UPDATE_TIME));
                    sequencing.add(PersistentVector.create(UPDATE_TIME, SORT_ASCENDING));
                    break;
                case LAST_UPDATE_RECENT:
                    addFindElement(UPDATE_TIME);
                    conditions.add(PersistentVector.create(DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.UPDATE_TIME), UPDATE_TIME));
                    sequencing.add(PersistentVector.create(UPDATE_TIME, SORT_DESCENDING));
                    break;
                case CREATION_DATE_OLDEST:
                    addFindElement(CREATE_TIME);
                    conditions.add(PersistentVector.create(DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.CREATE_TIME), CREATE_TIME));
                    sequencing.add(PersistentVector.create(CREATE_TIME, SORT_ASCENDING));
                    break;
                case CREATION_DATE_RECENT:
                    addFindElement(CREATE_TIME);
                    conditions.add(PersistentVector.create(DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.CREATE_TIME), CREATE_TIME));
                    sequencing.add(PersistentVector.create(CREATE_TIME, SORT_DESCENDING));
                    break;
                case PROPERTY_ASCENDING:
                    if (qualifiedSortProperties == null || qualifiedSortProperties.isEmpty()) {
                        xtdbConnector.logProblem(this.getClass().getName(),
                                                 methodName,
                                                 XTDBAuditCode.NO_SORT_PROPERTY,
                                                 null,
                                                 sequencingProperty,
                                                 typeNames == null ? "<null>" : typeNames.toString());
                    } else {
                        addPropertyBasedSorting(qualifiedSortProperties, SORT_ASCENDING);
                    }
                    break;
                case PROPERTY_DESCENDING:
                    if (qualifiedSortProperties == null || qualifiedSortProperties.isEmpty()) {
                        xtdbConnector.logProblem(this.getClass().getName(),
                                                 methodName,
                                                 XTDBAuditCode.NO_SORT_PROPERTY,
                                                 null,
                                                 sequencingProperty,
                                                 typeNames == null ? "<null>" : typeNames.toString());
                    } else {
                        addPropertyBasedSorting(qualifiedSortProperties, SORT_DESCENDING);
                    }
                    break;
                case ANY:
                case GUID:
                default:
                    sequencing.add(PersistentVector.create(DOC_ID, SORT_ASCENDING));
                    break;
            }
        }
    }

    /**
     * Add the necessary conditions for sorting based on a property: somewhat complex as we need to ensure that the
     * property being used in the sort is included in the search results themselves, and given the qualification of
     * property names this could mean several different properties we ultimately need to attempt to sort by, and hence
     * this separate method.
     * @param qualifiedSortProperties the set of properties by which we will sort
     * @param order indicating ascending or descending
     */
    protected void addPropertyBasedSorting(Set<Keyword> qualifiedSortProperties, Keyword order) {
        addFindElement(SORT_PROPERTY);
        if (qualifiedSortProperties.size() == 1) {
            // If there is only a single condition for sorting, just add it directly
            for (Keyword propertyRef : qualifiedSortProperties) {
                conditions.add(PersistentVector.create(DOC_ID, propertyRef, SORT_PROPERTY));
            }
        } else {
            // Otherwise, we need to combine the conditions together as an or-join (use the first result against any
            // of them for a given instance for sorting that instance)
            List<Object> orJoinConditions = new ArrayList<>();
            orJoinConditions.add(ConditionBuilder.OR_JOIN);
            orJoinConditions.add(PersistentVector.create(SORT_PROPERTY));
            for (Keyword propertyRef : qualifiedSortProperties) {
                orJoinConditions.add(PersistentVector.create(DOC_ID, propertyRef, SORT_PROPERTY));
            }
            conditions.add(PersistentList.create(orJoinConditions));
        }
        sequencing.add(PersistentVector.create(SORT_PROPERTY, order));
    }

    /**
     * Retrieve the query object, as ready-to-be-submitted to XTDB API's query method.
     * @return IPersistentMap containing the query
     */
    public IPersistentMap getQuery() {
        // Add the elements to be found:  :find [ e ... ]
        query = query.assoc(Keyword.intern("find"), PersistentVector.create(findElements));
        // Add the conditions to the query:  :where [[ ... condition ...], [ ... condition ... ], ... ]
        query = query.assoc(Keyword.intern("where"), PersistentVector.create(conditions));
        // Add the rules information to the query:  :rules [[ ... ]]
        if (rules != null && !rules.isEmpty()) {
            query = query.assoc(Keyword.intern("rules"), PersistentVector.create(rules));
        }
        // Add the sequencing information to the query:  :order-by [[ ... ]]
        if (sequencing != null && !sequencing.isEmpty()) {
            query = query.assoc(Keyword.intern("order-by"), PersistentVector.create(sequencing));
        }
        return query;
    }

    /**
     * Add the specified symbol to the list of those that are discovered by the search conditions (if not already in
     * the list)
     * @param element to add (if not already in the list)
     */
    protected void addFindElement(Symbol element) {
        if (!findElements.contains(element)) {
            findElements.add(element);
        }
    }

}
