/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search;

import clojure.lang.*;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstancePropertyValueMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Methods for building up complex conditions that search text (strings).
 */
public class TextConditionBuilder {

    private static final Logger log = LoggerFactory.getLogger(TextConditionBuilder.class);

    // Variable names (for destructuring)
    public static final Symbol ELIDE = Symbol.intern("_");
    public static final Symbol VALUE = Symbol.intern("v");
    public static final Symbol ATTRIBUTE = Symbol.intern("a");

    // Predicates (for comparisons)
    protected static final Symbol WILDCARD_TEXT_SEARCH_CI = Symbol.intern("wildcard-text-search-ci");
    protected static final Symbol WILDCARD_TEXT_SEARCH_CS = Symbol.intern("wildcard-text-search-cs");
    protected static final Symbol TEXT_SEARCH_CI = Symbol.intern("text-search-ci");
    protected static final Symbol TEXT_SEARCH_CS = Symbol.intern("text-search-cs");

    // String predicates
    protected static final Symbol STARTS_WITH = Symbol.intern("clojure.string/starts-with?");
    protected static final Symbol CONTAINS = Symbol.intern("clojure.string/includes?");
    protected static final Symbol ENDS_WITH = Symbol.intern("clojure.string/ends-with?");
    protected static final Symbol STR_OPERATOR = Symbol.intern("str");

    private static final Pattern ESCAPE_SPACES = Pattern.compile("(\\s)");

    private TextConditionBuilder() {}

    /**
     * Add conditions to the search to find any text field that matches the supplied criteria (without a separate Lucene
     * index).
     * @param regexCriteria defining what should be matched
     * @param xtdbConnector connectivity to the repository
     * @param typesToInclude defining which type definitions should be included in the search (to limit the properties)
     * @param namespace by which to qualify the properties
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} of condition(s) for the text matching
     */
    public static List<IPersistentCollection> buildWildcardTextCondition(String regexCriteria,
                                                                         XTDBOMRSRepositoryConnector xtdbConnector,
                                                                         Set<String> typesToInclude,
                                                                         String namespace,
                                                                         boolean luceneEnabled,
                                                                         boolean luceneRegexes) {

        final String methodName = "addWildcardTextCondition";
        log.info("Falling back to a non-Lucene wildcard text condition (likely to be slow!): {}", regexCriteria);

        final OMRSRepositoryHelper repositoryHelper = xtdbConnector.getRepositoryHelper();
        final String repositoryName = xtdbConnector.getRepositoryName();

        // Note: this fallback search should be avoided, as it will only iterate through the potential property
        //  combinations that are valid for the specified typesToInclude. For example, if this is limited to
        //  OpenMetadataRoot (which has no properties) then no property conditions at all will be used for the
        //  search and therefore ALL instances of that type will be retrieved (not the intended behavior of the
        //  find...ByPropertyValue methods).

        PrimitivePropertyValue string = new PrimitivePropertyValue();
        string.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        string.setPrimitiveValue(regexCriteria);

        // Build up a Set of all the unique string properties across all the types that are to be included for
        // the search
        Set<Keyword> stringProperties = new HashSet<>();
        for (String typeDefName : typesToInclude) {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
            if (typeDef != null) {
                List<TypeDefAttribute> properties = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);
                for (TypeDefAttribute property : properties) {
                    Set<Keyword> propertyRefs = InstancePropertyValueMapping.getKeywordsForProperty(xtdbConnector,
                            property.getAttributeName(),
                            namespace,
                            typesToInclude,
                            string);
                    stringProperties.addAll(propertyRefs);
                }
            }
        }

        List<IPersistentCollection> conditions = new ArrayList<>();
        List<Object> or = new ArrayList<>();
        // Note that we will only wrap with OR if there is more than a single condition...
        if (stringProperties.size() > 1) {
            or.add(ConditionBuilder.OR_OPERATOR);
            // For each string attribute, add an "or" condition that matches against the provided regex
            for (Keyword propertyRef : stringProperties) {
                List<Object> and = new ArrayList<>();
                and.add(ConditionBuilder.AND_OPERATOR);
                Symbol variable = Symbol.intern("v");
                List<IPersistentCollection> propertyConditions = ConditionBuilder.buildConditionForPropertyRef(
                        propertyRef,
                        PropertyComparisonOperator.LIKE,
                        string,
                        variable,
                        xtdbConnector,
                        luceneEnabled,
                        luceneRegexes
                );
                and.addAll(propertyConditions);
                or.add(PersistentList.create(and));
            }
            conditions.add(PersistentList.create(or));
        } else {
            for (Keyword propertyRef : stringProperties) {
                Symbol variable = Symbol.intern("v");
                List<IPersistentCollection> propertyConditions = ConditionBuilder.buildConditionForPropertyRef(
                        propertyRef,
                        PropertyComparisonOperator.LIKE,
                        string,
                        variable,
                        xtdbConnector,
                        luceneEnabled,
                        luceneRegexes
                );
                conditions.addAll(propertyConditions);
            }
        }

        return conditions;

    }

    /**
     * Adds conditions to the search to find any text field that matches the supplied criteria (leveraging a separate
     * Lucene index).
     * @param regexCriteria defining what should be matched
     * @param xtdbConnector connectivity to the repository
     * @param typesToInclude defining which type definitions should be included in the search (to limit the properties)
     * @param namespace by which to qualify the properties
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} of condition(s) for the text matching
     */
    public static List<IPersistentCollection> buildWildcardLuceneCondition(String regexCriteria,
                                                                           XTDBOMRSRepositoryConnector xtdbConnector,
                                                                           Set<String> typesToInclude,
                                                                           String namespace,
                                                                           boolean luceneRegexes) {

        final OMRSRepositoryHelper repositoryHelper = xtdbConnector.getRepositoryHelper();

        List<IPersistentCollection> conditions = null;
        // Since a Lucene index has some limitations and will never support a full Java regex on its own, the idea here
        // will be to add the Lucene condition if we can, but if not possible we will fallback to a non-Lucene search
        if (regexCriteria != null && regexCriteria.length() > 0) {
            String searchString = getLuceneComparisonString(regexCriteria, repositoryHelper, luceneRegexes);
            if (searchString == null) {
                // If we cannot run a Lucene-optimised query, then we will fallback to a full OR-based text condition
                // comparison: which could be VERY slow, but as long as it does not exceed the query timeout threshold
                // should at least still return accurate results
                conditions = buildWildcardTextCondition(
                        regexCriteria,
                        xtdbConnector,
                        typesToInclude,
                        namespace,
                        true,
                        luceneRegexes
                );
            } else {
                // Otherwise, it is some Lucene-supported search clause so we can run it directly via Lucene
                // Add the lucene query: [(wildcard-text-search "text") [[e _]]]
                conditions = new ArrayList<>();
                conditions.add(getLuceneWildcardClause(searchString, repositoryHelper.isCaseInsensitiveRegex(regexCriteria)));
            }
        }
        return conditions;

    }

    /**
     * Retrieve the Lucene-optimized conditions for matching against the provided criteria, or null if not able to
     * optimize this match via Lucene.
     * @param simpleName of the property (unqualified by type details)
     * @param comparator used to compare the property's value
     * @param value of the property
     * @param xtdbConnector connectivity to the repository
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} giving the appropriate XTDB query condition(s)
     */
    static List<IPersistentCollection> buildLuceneOptimizedConditions(String simpleName,
                                                                      PropertyComparisonOperator comparator,
                                                                      InstancePropertyValue value,
                                                                      XTDBOMRSRepositoryConnector xtdbConnector,
                                                                      boolean luceneRegexes) {

        List<IPersistentCollection> allConditionsForProperty = new ArrayList<>();
        final String methodName = "buildLuceneOptimizedConditions";
        final OMRSRepositoryHelper repositoryHelper = xtdbConnector.getRepositoryHelper();

        String regexSearchString = value.valueAsString();
        String searchString = null;
        switch (comparator) {
            case EQ:
            case NEQ:
                searchString = escapeLucenePhrase(regexSearchString);
                break;
            case LIKE:
                searchString = getLuceneComparisonString(regexSearchString, repositoryHelper, luceneRegexes);
                break;
            default:
                xtdbConnector.logProblem(TextConditionBuilder.class.getName(),
                                         methodName,
                                         XTDBAuditCode.INVALID_STRING_COMPARISON,
                                         null,
                                         simpleName,
                                         comparator.name());
                break;
        }
        if (searchString != null) {
            List<IPersistentCollection> intermediateConditions = new ArrayList<>();
            Symbol attribute = Symbol.intern(simpleName);
            // [(wildcard-text-search "searchString") [[e v simpleName _]]]
            IPersistentVector luceneClause = getLuceneWildcardClause(searchString, repositoryHelper.isCaseInsensitiveRegex(regexSearchString), attribute);
            intermediateConditions.add(luceneClause);
            //  [(str simpleName) sv_simpleName]   - to ensure the attribute keyword is made into a string and non-null
            Symbol nonNullStringVar = Symbol.intern("sv_" + simpleName);
            List<Object> forceString = new ArrayList<>();
            forceString.add(STR_OPERATOR);
            forceString.add(attribute);
            IPersistentVector enforceNonNullStringValue = PersistentVector.create(PersistentList.create(forceString), nonNullStringVar);
            intermediateConditions.add(enforceNonNullStringValue);
            // [(ends-with? sv_simpleName ".simpleName.value")]
            List<Object> predicateComparison = new ArrayList<>();
            predicateComparison.add(ENDS_WITH);
            predicateComparison.add(nonNullStringVar);
            predicateComparison.add(PropertyKeywords.getEndsWithPropertyNameForMatching(simpleName));
            intermediateConditions.add(PersistentVector.create(PersistentList.create(predicateComparison)));
            if (comparator.equals(PropertyComparisonOperator.NEQ)) {
                // If the comparison was a not-equals, we need to invert the condition we searched
                // (not-join [e] ... )
                List<Object> not = new ArrayList<>();
                not.add(ConditionBuilder.NOT_JOIN_OPERATOR);
                not.add(PersistentVector.create(XTDBQuery.DOC_ID));
                not.addAll(intermediateConditions);
                intermediateConditions = new ArrayList<>();
                intermediateConditions.add(PersistentList.create(not));
            }
            allConditionsForProperty.addAll(intermediateConditions);
        }

        return allConditionsForProperty;

    }

    /**
     * Retrieve the optimal regular expression query conditions for the provided inputs.
     * @param propertyRef to compare
     * @param value against which to compare
     * @param variable to which to compare
     * @param xtdbConnector connectivity to the repository
     * @param luceneEnabled indicates whether Lucene search index is configured (true) or not (false)
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return {@code List<IPersistentCollection>} of the conditions
     */
    static List<IPersistentCollection> buildRegexConditions(Keyword propertyRef,
                                                            InstancePropertyValue value,
                                                            Symbol variable,
                                                            XTDBOMRSRepositoryConnector xtdbConnector,
                                                            boolean luceneEnabled,
                                                            boolean luceneRegexes) {

        final String methodName = "buildRegexConditions";
        List<IPersistentCollection> propertyConditions = new ArrayList<>();
        List<IPersistentCollection> clauseConditions = new ArrayList<>();

        Object compareTo = InstancePropertyValueMapping.getValueForComparison(xtdbConnector, value);

        final OMRSRepositoryHelper repositoryHelper = xtdbConnector.getRepositoryHelper();

        if (compareTo instanceof String) {

            // "easy" cases -- direct query is possible
            String regexSearchString = (String) compareTo;
            if (repositoryHelper.isExactMatchRegex(regexSearchString, false)) {
                // If we are looking for an exact match, we will short-circuit out of this clause-based
                // query and just do an equality condition -- should be faster
                String unqualifiedLiteralString = repositoryHelper.getUnqualifiedLiteralString(regexSearchString);
                propertyConditions.add(getEqualsConditions(propertyRef, unqualifiedLiteralString));
                return propertyConditions;
            } else if (luceneEnabled) {
                // If Lucene is enabled, use it's search clauses here rather than reverting straight to a full Java regex
                String searchString = TextConditionBuilder.getLuceneComparisonString(regexSearchString, repositoryHelper, luceneRegexes);
                if (searchString != null) {
                    // If we are able to do a Lucene query, short-circuit out of this with the direct Lucene query clause:
                    // [(text-search :property "text") [[e _]]]
                    propertyConditions.add(TextConditionBuilder.getLuceneTermClause(propertyRef, searchString, repositoryHelper.isCaseInsensitiveRegex(regexSearchString)));
                    return propertyConditions;
                }
                // If we cannot run a Lucene-optimised query (searchString is null), then we will fallback to a
                // full OR-based text condition comparison (immediately below): which could be VERY slow, but as
                // long as it does not exceed the query timeout threshold should at least still return accurate
                // results
            }

            // "fallback" cases -- must do a full regex comparison
            // Otherwise we will retrieve a clause-based comparison depending on the regex requested
            //  [e :property variable]            - always needed, to define how to map the property's value to a variable
            IPersistentVector propertyToVariable = PersistentVector.create(XTDBQuery.DOC_ID, propertyRef, variable);
            //  [(str variable) s_variable]       - needed for strings, to ensure the string is non-null (sets value to "" for nil)
            Symbol nonNullStringVar = Symbol.intern("sv");
            List<Object> forceString = new ArrayList<>();
            forceString.add(STR_OPERATOR);
            forceString.add(variable);
            IPersistentVector enforceNonNullStringValue = PersistentVector.create(PersistentList.create(forceString), nonNullStringVar);
            clauseConditions.add(enforceNonNullStringValue);
            //  [(re-matches #"regex" s_variable)] - for a regex-based (string) predicate
            List<Object> predicateComparison = getRegexCondition(regexSearchString, nonNullStringVar, repositoryHelper);
            clauseConditions.add(PersistentVector.create(PersistentList.create(predicateComparison)));
            // No need to wrap anything here, will be handled by calling method(s)
            propertyConditions.add(propertyToVariable);
            propertyConditions.addAll(clauseConditions);

        } else {
            xtdbConnector.logProblem(TextConditionBuilder.class.getName(),
                                     methodName,
                                     XTDBAuditCode.NO_REGEX,
                                     null,
                                     value == null ? "<null>" : value.toString());
        }

        return propertyConditions;

    }

    /**
     * Retrieve conditions to match the provided regular expression against the provided variable's value.
     * @param regexSearchString regular expression to match against
     * @param variable whose value should be compared against
     * @param repositoryHelper through which we can introspect regular expressions
     * @return {@code List<Object>} of the condition
     */
    private static List<Object> getRegexCondition(String regexSearchString,
                                                  Symbol variable,
                                                  OMRSRepositoryHelper repositoryHelper) {
        List<Object> predicateComparison = new ArrayList<>();
        // The equality case should already have been handled before coming into this method: we will now use
        // Clojure's built-in string comparisons for simple regexes (startsWith, contains, endsWith), and only
        // fall-back to a full regex comparison if the requested regex is for case-insensitive matches or some
        // more complicated expression
        if (repositoryHelper.isStartsWithRegex(regexSearchString, false)) {
            predicateComparison.add(STARTS_WITH);
            predicateComparison.add(variable);
            predicateComparison.add(repositoryHelper.getUnqualifiedLiteralString(regexSearchString));
        } else if (repositoryHelper.isContainsRegex(regexSearchString, false)) {
            predicateComparison.add(CONTAINS);
            predicateComparison.add(variable);
            predicateComparison.add(repositoryHelper.getUnqualifiedLiteralString(regexSearchString));
        } else if (repositoryHelper.isEndsWithRegex(regexSearchString, false)) {
            predicateComparison.add(ENDS_WITH);
            predicateComparison.add(variable);
            predicateComparison.add(repositoryHelper.getUnqualifiedLiteralString(regexSearchString));
        } else {
            // For all other regexes, we need a (predicate #"value" variable) pattern, so compile one
            Pattern regex = Pattern.compile(regexSearchString);
            predicateComparison.add(ConditionBuilder.REGEX_OPERATOR);
            predicateComparison.add(regex);
            predicateComparison.add(variable);
        }
        return predicateComparison;
    }

    /**
     * Translate the provided String expression into one that can be used against a Lucene index (or null if not possible).
     * @param regexCriteria expression to be translated
     * @param repositoryHelper through which we can check the regular expression in the criteria
     * @param luceneRegexes indicates whether unquoted regexes should be treated as Lucene compatible (true) or not (false)
     * @return String that is usable for Lucene, or null if no Lucene search is possible
     */
    private static String getLuceneComparisonString(String regexCriteria, OMRSRepositoryHelper repositoryHelper, boolean luceneRegexes) {
        if (repositoryHelper.isExactMatchRegex(regexCriteria)
                || repositoryHelper.isStartsWithRegex(regexCriteria)
                || repositoryHelper.isContainsRegex(regexCriteria)
                || repositoryHelper.isEndsWithRegex(regexCriteria)) {
            // For these basic regex conditions we can easily un-qualify to a plain string that we
            // can use to hit the Lucene index, so do so
            String searchString = escapeLucenePhrase(repositoryHelper.getUnqualifiedLiteralString(regexCriteria));
            if (repositoryHelper.isStartsWithRegex(regexCriteria)) {
                searchString = searchString + "*";
            } else if (repositoryHelper.isEndsWithRegex(regexCriteria)) {
                searchString = "*" + searchString;
            } else if (repositoryHelper.isContainsRegex(regexCriteria)) {
                searchString = "*" + searchString + "*";
            }
            return searchString;
        } else if (luceneRegexes) {
            // Otherwise, we must assume it is a more complex regex. If we are treating unquoted regexes as
            // Lucene-compatible, create a direct Lucene regex condition for it
            // Note: we must first ensure that the value is qualified as a regex for Lucene, by wrapping it in
            // forward slashes
            if (! (regexCriteria.startsWith("/") && regexCriteria.endsWith("/")) ) {
                regexCriteria = "/" + regexCriteria + "/";
            }
            return regexCriteria;
        }
        // In any other scenario, we are unable to handle the query via Lucene, so return a null
        return null;
    }

    /**
     * Retrieve a Lucene-oriented <code>text-search</code> clause of the form: <code>[(text-search-[ci|cs] :property "searchString") [[e _]]]</code>
     * @param propertyRef property whose value the text should be matched against
     * @param searchString to use for the text-search
     * @param isCaseInsensitive indicates whether the query should be case insensitive (true) or case sensitive (false)
     * @return IPersistentVector of the form <code>[(text-search-[ci|cs] :property "searchString") [[e _]]]</code>
     */
    private static IPersistentVector getLuceneTermClause(Keyword propertyRef, String searchString, boolean isCaseInsensitive) {
        List<Object> luceneCriteria = new ArrayList<>();
        if (isCaseInsensitive) {
            luceneCriteria.add(TEXT_SEARCH_CI);
        } else {
            luceneCriteria.add(TEXT_SEARCH_CS);
        }
        luceneCriteria.add(propertyRef);
        luceneCriteria.add(searchString);
        IPersistentVector deStructured = PersistentVector.create((IPersistentVector)PersistentVector.create(XTDBQuery.DOC_ID, ELIDE));
        List<IPersistentCollection> luceneQuery = new ArrayList<>();
        luceneQuery.add(PersistentList.create(luceneCriteria));
        luceneQuery.add(deStructured);
        return PersistentVector.create(luceneQuery);
    }

    /**
     * Retrieve a Lucene-oriented <code>wildcard-text-search</code> clause of the form: <code>[(wildcard-text-search-[ci|cs] "searchString") [[e v a _]]]</code>
     * @param searchString to use for the wildcard-text-search
     * @param isCaseInsensitive indicates whether the query should be case insensitive (true) or case sensitive (false)
     * @return IPersistentVector of the form <code>[(wildcard-text-search-[ci|cs] "searchString") [[e v a _]]]</code>
     */
    private static IPersistentVector getLuceneWildcardClause(String searchString, boolean isCaseInsensitive) {
        return getLuceneWildcardClause(searchString, isCaseInsensitive, ATTRIBUTE);
    }

    /**
     * Retrieve a Lucene-oriented <code>wildcard-text-search</code> clause of the form: <code>[(wildcard-text-search-[ci|cs] "searchString") [[e v attribute _]]]</code>
     * @param searchString to use for the wildcard-text-search
     * @param isCaseInsensitive indicates whether the query should be case insensitive (true) or case sensitive (false)
     * @param attribute the symbol to use for the matching attribute
     * @return IPersistentVector of the form <code>[(wildcard-text-search-[ci|cs] "searchString") [[e v attribute _]]]</code>
     */
    private static IPersistentVector getLuceneWildcardClause(String searchString, boolean isCaseInsensitive, Symbol attribute) {
        List<Object> luceneCriteria = new ArrayList<>();
        if (isCaseInsensitive) {
            luceneCriteria.add(WILDCARD_TEXT_SEARCH_CI);
        } else {
            luceneCriteria.add(WILDCARD_TEXT_SEARCH_CS);
        }
        luceneCriteria.add(searchString);
        IPersistentVector deStructured = PersistentVector.create((IPersistentVector)PersistentVector.create(XTDBQuery.DOC_ID, VALUE, attribute, ELIDE));
        List<IPersistentCollection> luceneQuery = new ArrayList<>();
        luceneQuery.add(PersistentList.create(luceneCriteria));
        luceneQuery.add(deStructured);
        return PersistentVector.create(luceneQuery);
    }

    /**
     * Escape the provided string so that it is interpreted as a complete literal phrase by Lucene.
     * @param phrase to escape
     * @return String that should be interpreted by Lucene as a complete phrase
     */
    private static String escapeLucenePhrase(String phrase) {
        if (phrase != null) {
            String escaped = QueryParserBase.escape(phrase);
            // In addition to escaping special characters, we need to also escape spaces to avoid
            // the query parser interpreting a phrase as multiple words that each need to be matched as
            // anchored terms (which will likely always fail given a KeywordAnalyzer) -- for the sake of completeness
            // we will do this strictly against Java whitespace (space, tabs, newlines, etc) to ensure we match
            // even in those scenarios where there are multiple spaces together (each one will be individually escaped)
            return ESCAPE_SPACES.matcher(escaped).replaceAll("\\\\$1");
        }
        return null;
    }

    /**
     * Retrieve conditions to match where the provided property's value equals the provided string value.
     * @param propertyRef whose value should be compared
     * @param value to compare against
     * @return IPersistentCollection giving the conditions
     */
    private static IPersistentCollection getEqualsConditions(Keyword propertyRef, String value) {
        return PersistentVector.create(XTDBQuery.DOC_ID, propertyRef, value);
    }

}
