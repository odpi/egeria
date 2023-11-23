/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Methods for building up complex conditions used by the queries.
 */
public class ConditionBuilder {

    // Predicates (for comparisons)
    public static final Symbol OR_OPERATOR = Symbol.intern("or");
    public static final Symbol AND_OPERATOR = Symbol.intern("and");
    public static final Symbol NOT_OPERATOR = Symbol.intern("not");
    public static final Symbol NOT_JOIN_OPERATOR = Symbol.intern("not-join");
    public static final Symbol OR_JOIN = Symbol.intern("or-join");
    protected static final Symbol EQ_OPERATOR = Symbol.intern("=");
    protected static final Symbol NEQ_OPERATOR = Symbol.intern("not=");
    protected static final Symbol GT_OPERATOR = Symbol.intern(">");
    protected static final Symbol GTE_OPERATOR = Symbol.intern(">=");
    protected static final Symbol LT_OPERATOR = Symbol.intern("<");
    protected static final Symbol LTE_OPERATOR = Symbol.intern("<=");
    protected static final Symbol IS_NULL_OPERATOR = Symbol.intern("nil?");
    protected static final Symbol NOT_NULL_OPERATOR = Symbol.intern("some?");
    protected static final Symbol REGEX_OPERATOR = Symbol.intern("re-matches");
    protected static final Symbol IN_OPERATOR = Symbol.intern("contains?");
    public static final Symbol SET_OPERATOR = Symbol.intern("hash-set");

    protected static final Map<PropertyComparisonOperator, Symbol> PCO_TO_SYMBOL = createPropertyComparisonOperatorToSymbolMap();
    private static Map<PropertyComparisonOperator, Symbol> createPropertyComparisonOperatorToSymbolMap() {
        EnumMap<PropertyComparisonOperator, Symbol> map = new EnumMap<>(PropertyComparisonOperator.class);
        map.put(PropertyComparisonOperator.EQ, EQ_OPERATOR);
        map.put(PropertyComparisonOperator.NEQ, NEQ_OPERATOR);
        map.put(PropertyComparisonOperator.GT, GT_OPERATOR);
        map.put(PropertyComparisonOperator.GTE, GTE_OPERATOR);
        map.put(PropertyComparisonOperator.LT, LT_OPERATOR);
        map.put(PropertyComparisonOperator.LTE, LTE_OPERATOR);
        map.put(PropertyComparisonOperator.IS_NULL, IS_NULL_OPERATOR);
        map.put(PropertyComparisonOperator.NOT_NULL, NOT_NULL_OPERATOR);
        map.put(PropertyComparisonOperator.LIKE, REGEX_OPERATOR);
        map.put(PropertyComparisonOperator.IN, IN_OPERATOR);
        return map;
    }

    private ConditionBuilder() {}

    /**
     * Retrieve a set of translated XTDB conditions appropriate to the provided Egeria conditions.
     * @param searchProperties to translate
     * @param namespace by which to qualify properties
     * @param orNested true iff searchProperties is a set of conditions nested inside an OR (match criteria = ANY)
     * @param typeNames of all of the types we are including in the search
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>}
     */
    public static List<IPersistentCollection> buildPropertyConditions(SearchProperties searchProperties,
                                                                      String namespace,
                                                                      boolean orNested,
                                                                      Set<String> typeNames,
                                                                      XTDBOMRSRepositoryConnector xtdbConnector,
                                                                      boolean luceneEnabled,
                                                                      boolean luceneRegexes) {
        final String methodName = "buildPropertyConditions";
        if (searchProperties != null) {
            List<PropertyCondition> propertyConditions = searchProperties.getConditions();
            MatchCriteria matchCriteria = searchProperties.getMatchCriteria();
            if (propertyConditions != null && !propertyConditions.isEmpty()) {
                List<List<IPersistentCollection>> allConditions = new ArrayList<>();
                for (PropertyCondition condition : propertyConditions) {
                    // Ensure every condition, whether nested or singular, is added to the 'allConditions' list
                    List<IPersistentCollection> xtdbConditions = getSinglePropertyCondition(
                            condition,
                            namespace,
                            typeNames,
                            xtdbConnector,
                            luceneEnabled,
                            luceneRegexes
                    );
                    if (xtdbConditions != null && !xtdbConditions.isEmpty()) {
                        allConditions.add(xtdbConditions);
                    }
                }
                // apply the matchCriteria against the full set of nested property conditions
                List<Object> predicatedConditions = new ArrayList<>();
                switch (matchCriteria) {
                    case ALL:
                        List<IPersistentCollection> unwrapped = allConditions.stream().flatMap(Collection::stream).collect(Collectors.toList());
                        if (orNested && allConditions.size() > 1) {
                            // we should only wrap with an 'AND' predicate if we're nested inside an 'OR' predicate and
                            // there is more than a single condition
                            predicatedConditions.add(AND_OPERATOR);
                            predicatedConditions.addAll(unwrapped);
                        } else {
                            // otherwise, we can return the conditions directly (nothing more to process on them)
                            // (though remember they are a nested list, so we should flatten that list before returning it)
                            return unwrapped;
                        }
                        break;
                    case ANY:
                        if (allConditions.size() == 1) {
                            // If only a single condition, return it directly (no wrapping necessary)
                            return allConditions.get(0);
                        } else {
                            // (or-join [e] (and [e :property var] [(predicate ... var)]) )
                            predicatedConditions.add(OR_JOIN);
                            predicatedConditions.add(PersistentVector.create(XTDBQuery.DOC_ID));
                            for (List<IPersistentCollection> nestedConditions : allConditions) {
                                if (nestedConditions.size() == 1) {
                                    predicatedConditions.addAll(nestedConditions);
                                } else {
                                    List<Object> and = new ArrayList<>();
                                    and.add(AND_OPERATOR);
                                    and.addAll(nestedConditions);
                                    predicatedConditions.add(PersistentList.create(and));
                                }
                            }
                        }
                        break;
                    case NONE:
                        if (allConditions.size() == 1) {
                            // (not-join [e] ... )
                            predicatedConditions.add(NOT_JOIN_OPERATOR);
                            predicatedConditions.add(PersistentVector.create(XTDBQuery.DOC_ID));
                            predicatedConditions.addAll(allConditions.get(0));
                        } else {
                            // (not (or-join [e] ... ) )
                            predicatedConditions.add(NOT_OPERATOR);
                            List<Object> or = new ArrayList<>();
                            or.add(OR_JOIN);
                            or.add(PersistentVector.create(XTDBQuery.DOC_ID));
                            for (List<IPersistentCollection> nestedConditions : allConditions) {
                                if (nestedConditions.size() == 1) {
                                    or.addAll(nestedConditions);
                                } else {
                                    List<Object> and = new ArrayList<>();
                                    and.add(AND_OPERATOR);
                                    and.addAll(nestedConditions);
                                    or.add(PersistentList.create(and));
                                }
                            }
                            predicatedConditions.add(PersistentList.create(or));
                        }
                        break;
                    default:
                        xtdbConnector.logProblem(ConditionBuilder.class.getName(),
                                                 methodName,
                                                 XTDBAuditCode.UNMAPPED_MATCH_CRITERIA,
                                                 null,
                                                 matchCriteria.name());
                        break;
                }
                if (!predicatedConditions.isEmpty()) {
                    List<IPersistentCollection> wrapped = new ArrayList<>();
                    wrapped.add(getXtdbCondition(predicatedConditions));
                    return wrapped;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the XTDB query condition(s) for the specified property and comparison operations.
     * @param propertyRef to compare
     * @param comparator comparison to carry out
     * @param value against which to compare
     * @param variable to which to compare
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} of the conditions
     */
    static List<IPersistentCollection> buildConditionForPropertyRef(Keyword propertyRef,
                                                                    PropertyComparisonOperator comparator,
                                                                    InstancePropertyValue value,
                                                                    Symbol variable,
                                                                    XTDBOMRSRepositoryConnector xtdbConnector,
                                                                    boolean luceneEnabled,
                                                                    boolean luceneRegexes) {

        final String methodName = "buildConditionForPropertyRef";
        List<IPersistentCollection> propertyConditions = new ArrayList<>();
        List<IPersistentCollection> clauseConditions = new ArrayList<>();

        if (comparator.equals(PropertyComparisonOperator.EQ)) {
            // For equality we can compare directly to the value and short-circuit any additional processing
            propertyConditions.add(getEqualsConditions(xtdbConnector, propertyRef, value));
            return propertyConditions;
        } else {
            Symbol predicate = getPredicateForOperator(xtdbConnector, comparator);
            if (REGEX_OPERATOR.equals(predicate)) {
                // This method already handles wrapping, if needed, so we can return its results directly
                return TextConditionBuilder.buildRegexConditions(propertyRef, value, variable, xtdbConnector, luceneEnabled, luceneRegexes);
            } else if (IN_OPERATOR.equals(predicate)) {
                // For the IN comparison, we need an extra condition to setup the set to compare against
                // [(hash-set 1 2 3) las]    - needed for lists, to ensure the list is a unique set of keys to check against
                Symbol listAsSet = Symbol.intern("las");
                List<Object> forceSet = new ArrayList<>();
                forceSet.add(SET_OPERATOR);
                Object toCompare = InstancePropertyValueMapping.getValueForComparison(xtdbConnector, value);
                if (toCompare instanceof List) {
                    // add all elements of the array to the list
                    forceSet.addAll((List<?>)toCompare);
                }
                List<Object> set = new ArrayList<>();
                set.add(PersistentList.create(forceSet));
                set.add(listAsSet);
                IPersistentVector enforceSet = PersistentVector.create(set);
                // [(contains? las variable)]
                clauseConditions.add(enforceSet);
                List<Object> predicateComparison = new ArrayList<>();
                predicateComparison.add(predicate);
                predicateComparison.add(listAsSet);
                predicateComparison.add(variable);
                clauseConditions.add(PersistentVector.create(PersistentList.create(predicateComparison)));
            } else if (predicate != null) {
                // For everything else, we need a (predicate variable value) pattern
                // Setup a predicate comparing that variable to the value (with appropriate comparison operator)
                // [(predicate variable "value")] - for a non-string predicate
                List<Object> predicateComparison = new ArrayList<>();
                predicateComparison.add(predicate);
                predicateComparison.add(variable);
                // The null predicates only expect a single argument, so do not bother attempting to add
                // this third argument if the predicate is null-related
                if (!predicate.equals(IS_NULL_OPERATOR) && !predicate.equals(NOT_NULL_OPERATOR)) {
                    predicateComparison.add(InstancePropertyValueMapping.getValueForComparison(xtdbConnector, value));
                }
                clauseConditions.add(PersistentVector.create(PersistentList.create(predicateComparison)));
            } else {
                xtdbConnector.logProblem(ConditionBuilder.class.getName(),
                                         methodName,
                                         XTDBAuditCode.UNKNOWN_COMPARISON_OPERATOR,
                                         null,
                                         comparator.name());
                return propertyConditions;
            }

            // If we have not short-circuited, we need to translate the property's value into a variable
            //  [e :property variable]            - always needed, to define how to map the property's value to a variable
            IPersistentVector propertyToVariable = PersistentVector.create(XTDBQuery.DOC_ID, propertyRef, variable);

            propertyConditions.add(propertyToVariable);
            propertyConditions.addAll(clauseConditions);
        }

        return propertyConditions;

    }

    /**
     * Translate the provided condition, considered on its own, into a XTDB query condition. Handles both single
     * property conditions and nested conditions (though the latter simply recurse back to getPropertyConditions)
     * @param singleCondition to translate (should not contain nested condition)
     * @param namespace by which to qualify the properties in the condition
     * @param typeNames of all of the types we are including in the search
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} giving the appropriate XTDB query condition(s)
     * @see #buildPropertyConditions(SearchProperties, String, boolean, Set, XTDBOMRSRepositoryConnector, boolean, boolean)
     */
    private static List<IPersistentCollection> getSinglePropertyCondition(PropertyCondition singleCondition,
                                                                          String namespace,
                                                                          Set<String> typeNames,
                                                                          XTDBOMRSRepositoryConnector xtdbConnector,
                                                                          boolean luceneEnabled,
                                                                          boolean luceneRegexes) {
        final String methodName = "getSinglePropertyCondition";
        SearchProperties nestedConditions = singleCondition.getNestedConditions();
        if (nestedConditions != null) {
            // If the conditions are nested, simply recurse back on getPropertyConditions
            MatchCriteria matchCriteria = nestedConditions.getMatchCriteria();
            return buildPropertyConditions(
                    nestedConditions,
                    namespace,
                    matchCriteria.equals(MatchCriteria.ANY),
                    typeNames,
                    xtdbConnector,
                    luceneEnabled,
                    luceneRegexes
            );
        } else {
            // Otherwise, parse through and process a single value condition
            String simpleName = singleCondition.getProperty();
            PropertyComparisonOperator comparator = singleCondition.getOperator();
            InstancePropertyValue value = singleCondition.getValue();
            if (InstanceAuditHeaderMapping.isKnownBaseProperty(simpleName)) {
                // InstanceAuditHeader properties should neither be namespace-d nor '.value' qualified, as they are not
                // InstanceValueProperties but simple native types -- so we can simply return their conditions directly
                Keyword propertyRef = getAuditHeaderPropertyRef(namespace, simpleName);
                return buildConditionForPropertyRef(
                        propertyRef,
                        comparator,
                        value,
                        Symbol.intern(simpleName),
                        xtdbConnector,
                        luceneEnabled,
                        luceneRegexes
                );
            } else {
                // Any others we should assume are InstanceProperties.
                // These are somewhat more complicated, as we must be aware that the actual names
                // of properties in InstanceProperties are qualified by the type in which they are defined: in other
                // words, a single property like 'description' may actually appear on ~100 different types, and thus
                // there could be ~100 valid variations of the property. We therefore need to intersect the type limiters
                // that have been provided with the possible variations of the property to come up with a definitive
                // list of variations specific to this query before we can proceed.
                List<IPersistentCollection> allPropertyConditions = null;
                Set<Keyword> qualifiedSearchProperties;
                if (namespace.startsWith(EntitySummaryMapping.N_CLASSIFICATIONS) && !ClassificationMapping.isKnownBaseProperty(simpleName)) {
                    // If they are classification-specific instance properties, they need further qualification
                    String classificationNamespace = namespace + "." + ClassificationMapping.CLASSIFICATION_PROPERTIES_NS;
                    // Given the namespace qualification places into classificationProperties, we should ONLY need to
                    // search the classification type(s) for this property -- not all types
                    Set<String> classificationTypes = new HashSet<>();
                    String classificationTypeName = ClassificationMapping.getClassificationNameFromNamespace(EntitySummaryMapping.N_CLASSIFICATIONS, namespace);
                    classificationTypes.add(classificationTypeName);
                    qualifiedSearchProperties = InstancePropertyValueMapping.getKeywordsForProperty(xtdbConnector,
                            simpleName,
                            classificationNamespace,
                            classificationTypes,
                            value);
                } else {
                    qualifiedSearchProperties = InstancePropertyValueMapping.getKeywordsForProperty(xtdbConnector,
                            simpleName,
                            namespace,
                            typeNames,
                            value);
                }
                if (qualifiedSearchProperties.isEmpty()) {
                    // If there are NO valid combinations of this property given the type limiters used for the search,
                    // then we should ensure that no results are returned whatsoever by the query.
                    xtdbConnector.logProblem(ConditionBuilder.class.getName(),
                                             methodName,
                                             XTDBAuditCode.INVALID_PROPERTY,
                                             null,
                                             simpleName,
                                             typeNames == null ? "<null>" : typeNames.toString());
                    allPropertyConditions = new ArrayList<>(getNoResultsCondition());
                } else if (qualifiedSearchProperties.size() == 1) {
                    // If there is only a SINGLE valid property variation, then we can do a direct term-based lookup
                    // for that property against the specified value, as this will be the most optimal query
                    // (Looping only because 'qualifiedSearchProperties' is a set, and no other way to simply retrieve
                    // its one element)
                    for (Keyword qualifiedPropertyRef : qualifiedSearchProperties) {
                        List<IPersistentCollection> conditionsForOneProperty = buildConditionForPropertyRef(
                                qualifiedPropertyRef,
                                comparator,
                                value,
                                Symbol.intern(simpleName),
                                xtdbConnector,
                                luceneEnabled,
                                luceneRegexes
                        );
                        allPropertyConditions = new ArrayList<>(conditionsForOneProperty);
                    }
                } else if (luceneEnabled) {
                    // Otherwise, in cases where there are MULTIPLE valid property variations and the value is a string,
                    // we may be able to optimize the search via Lucene: by allowing Lucene's wildcard search to tell
                    // us the matching variable names (not construct a costly OR-based across all potential combinations)
                    if (value != null
                            && value.getInstancePropertyCategory().equals(InstancePropertyCategory.PRIMITIVE)
                            && ((PrimitivePropertyValue) value).getPrimitiveDefCategory().equals(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)) {
                        allPropertyConditions = TextConditionBuilder.buildLuceneOptimizedConditions(
                                simpleName,
                                comparator,
                                value,
                                xtdbConnector,
                                luceneRegexes
                        );
                    }
                }
                // In the worst case scenario, there are MULTIPLE valid property variations, but they could not be
                // optimized via Lucene, so we must fallback to a costly OR-based comparison across all of the different
                // property variations (this is the most likely set of conditions anywhere in the query process to
                // cause an overall query timeout)
                if (allPropertyConditions == null) {
                    allPropertyConditions = new ArrayList<>();
                    // Since in this fallback scenario there could be a different list of conditions for each different
                    // permutation of the property, we will need to specially handle how all of these conditions are
                    // combined to ensure accurate results
                    List<List<IPersistentCollection>> conditionAggregator = getFallbackConditions(
                            simpleName,
                            comparator,
                            value,
                            qualifiedSearchProperties,
                            xtdbConnector,
                            luceneEnabled,
                            luceneRegexes
                    );
                    if (conditionAggregator.size() == 1) {
                        // If there is only a single condition, we can just add it directly:
                        // - ALL and ANY are the same with a single condition
                        // - NONE will already be wrapping the condition appropriately
                        allPropertyConditions.addAll(conditionAggregator.get(0));
                    } else {
                        // Otherwise, there are multiple conditions -- but keep in mind these are each one set of
                        // conditions applicable to a different variation of _the same_ property...
                        // We therefore need to OR this combined set of conditions, as only one of the property variations
                        // needs to match in order to meet the criteria. (Note: this is costly, but unavoidable so long
                        // as there are no type limiters specified by the caller)
                        List<Object> or = new ArrayList<>();
                        or.add(OR_OPERATOR);
                        for (List<IPersistentCollection> subList : conditionAggregator) {
                            if (subList.size() == 1) {
                                // If there is only a single clause, add it directly to the OR
                                or.addAll(subList);
                            } else {
                                // If there are multiple, however, wrap them with an AND
                                List<Object> andList = new ArrayList<>();
                                andList.add(AND_OPERATOR);
                                andList.addAll(subList);
                                or.add(PersistentList.create(andList));
                            }
                        }
                        allPropertyConditions.add(PersistentList.create(or));
                    }
                }
                return allPropertyConditions;
            }
        }
    }

    /**
     * Retrieve the fallback conditions for matching a property, where we are not able to optimize the search via
     * Lucene (as the property is either null or not a string).
     * @param simpleName of the property (unqualified by type details)
     * @param comparator used to compare the property's value
     * @param value of the property
     * @param qualifiedSearchProperties the set of unique type-qualified property names as keywords
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<List<IPersistentCollection>>} giving the appropriate XTDB query condition(s)
     */
    private static List<List<IPersistentCollection>> getFallbackConditions(String simpleName,
                                                                           PropertyComparisonOperator comparator,
                                                                           InstancePropertyValue value,
                                                                           Set<Keyword> qualifiedSearchProperties,
                                                                           XTDBOMRSRepositoryConnector xtdbConnector,
                                                                           boolean luceneEnabled,
                                                                           boolean luceneRegexes) {

        List<List<IPersistentCollection>> allConditionsForProperty = new ArrayList<>();

        // If the value could not be Lucene-optimised, we have no choice but to fall-back to constructing a
        // costly OR-based query against all possible variations of the property: each will need namespace
        // AND type AND '.value' qualification to be searchable
        // Since depending on the types by which we are limiting the search there could be different variations
        // of the same property name, we need to include criteria to find all of them -- so we must iterate
        // through and build up a set of conditions for each variation of the property
        Symbol symbolForVariable = Symbol.intern(simpleName);
        for (Keyword qualifiedPropertyRef : qualifiedSearchProperties) {
            List<IPersistentCollection> conditionsForOneProperty = buildConditionForPropertyRef(
                    qualifiedPropertyRef,
                    comparator,
                    value,
                    symbolForVariable,
                    xtdbConnector,
                    luceneEnabled,
                    luceneRegexes
            );
            allConditionsForProperty.add(conditionsForOneProperty);
        }

        return allConditionsForProperty;

    }

    /**
     * Retrieve conditions to match where the provided property's value equals the provided value.
     * @param xtdbConnector connectivity to the repository
     * @param propertyRef whose value should be compared
     * @param value to compare against
     * @return IPersistentCollection giving the conditions
     */
    private static IPersistentCollection getEqualsConditions(XTDBOMRSRepositoryConnector xtdbConnector, Keyword propertyRef, InstancePropertyValue value) {
        return PersistentVector.create(XTDBQuery.DOC_ID, propertyRef, InstancePropertyValueMapping.getValueForComparison(xtdbConnector, value));
    }

    /**
     * Retrieve the XTDB predicate for the provided comparison operation.
     * @param xtdbConnector connectivity to the repository
     * @param comparator to translate into a XTDB predicate
     * @return Symbol giving the appropriate XTDB predicate
     */
    private static Symbol getPredicateForOperator(XTDBOMRSRepositoryConnector xtdbConnector, PropertyComparisonOperator comparator) {
        final String methodName = "getPredicateForOperator";
        Symbol toUse = PCO_TO_SYMBOL.getOrDefault(comparator, null);
        if (toUse == null) {
            xtdbConnector.logProblem(ConditionBuilder.class.getName(),
                                     methodName,
                                     XTDBAuditCode.UNKNOWN_COMPARISON_OPERATOR,
                                     null,
                                     comparator.name());
        }
        return toUse;
    }

    /**
     * Translate the provided condition into the appropriate XTDB representation (List for predicated-conditions, Vector
     * for any other conditions)
     * @param condition to translate
     * @return IPersistentCollection of the appropriate XTDB representation
     */
    private static IPersistentCollection getXtdbCondition(List<Object> condition) {
        if (condition != null && !condition.isEmpty()) {
            Object first = condition.get(0);
            if (first instanceof Symbol) {
                // If the first element is a Symbol, it's an OR, OR-JOIN, AND, NOT or NOT-JOIN -- create a list
                return PersistentList.create(condition);
            } else {
                // Otherwise (ie. single condition) assume it's a Vector -- create a Vector accordingly
                return PersistentVector.create(condition);
            }
        }
        return null;
    }

    /**
     * Retrieve a condition that will ensure no results are returned by a query.
     * @return {@code List<IPersistentCollection>}
     */
    private static List<IPersistentCollection> getNoResultsCondition() {
        List<IPersistentCollection> conditions = new ArrayList<>();
        conditions.add(PersistentVector.create(XTDBQuery.DOC_ID, Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_CATEGORY), -1));
        return conditions;
    }

    /**
     * Retrieve the reference to use for an audit header property (these are generally unqualified (no namespace),
     * unless they are embedded within a classification).
     * @param namespace by which to qualify the property (for classifications)
     * @param propertyName for which to retrieve a reference
     * @return Keyword
     */
    private static Keyword getAuditHeaderPropertyRef(String namespace, String propertyName) {
        Keyword propertyRef;
        // InstanceAuditHeader properties should neither be namespace-d nor '.value' qualified, as they are not
        // InstanceValueProperties but simple native types
        if (namespace != null && namespace.startsWith(EntitySummaryMapping.N_CLASSIFICATIONS)) {
            // However, if they are instance headers embedded in a classification, they still need the base-level
            // classification namespace qualifier
            propertyRef = Keyword.intern(namespace, propertyName);
        } else {
            propertyRef = Keyword.intern(propertyName);
        }
        return propertyRef;
    }

}
