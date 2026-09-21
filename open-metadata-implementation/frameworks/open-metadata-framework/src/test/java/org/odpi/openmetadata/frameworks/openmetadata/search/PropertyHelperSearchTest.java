/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * PropertyHelperSearchTest covers the part of {@link PropertyHelper} that turns a caller's request into the
 * search conditions a repository is asked to satisfy.
 * <br><br>
 * This is where a mistake is least visible.  A search built with the wrong comparison operator is a valid
 * query that the repository runs happily and answers with an empty list, so the caller - and any test
 * counting what it found - sees "no matches" rather than a failure.  Nothing distinguishes that from there
 * genuinely being nothing to find.  The truth table below states which operator each combination of the
 * three search flags produces, so a change to one of them cannot quietly turn an exact match into a
 * prefix match or a case sensitive search into a case insensitive one.
 */
public class PropertyHelperSearchTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * The eight combinations of the three search flags, each with the operator they have to produce.
     * {@code startsWith} and {@code endsWith} describe which ends of the value are anchored, so both of
     * them together is an exact match and neither of them is a contains.
     *
     * @return startsWith, endsWith, ignoreCase, expected operator
     */
    @DataProvider(name = "searchFlagCombinations")
    public Object[][] searchFlagCombinations()
    {
        return new Object[][]
                {
                        { true,  true,  false, PropertyComparisonOperator.EQ },
                        { true,  true,  true,  PropertyComparisonOperator.CASE_INSENSITIVE_EQ },
                        { true,  false, false, PropertyComparisonOperator.STARTS_WITH },
                        { true,  false, true,  PropertyComparisonOperator.CASE_INSENSITIVE_STARTS_WITH },
                        { false, true,  false, PropertyComparisonOperator.ENDS_WITH },
                        { false, true,  true,  PropertyComparisonOperator.CASE_INSENSITIVE_ENDS_WITH },
                        { false, false, false, PropertyComparisonOperator.LIKE },
                        { false, false, true,  PropertyComparisonOperator.CASE_INSENSITIVE_LIKE },
                };
    }


    /**
     * Each combination of the search flags produces its own comparison operator.
     *
     * @param startsWith anchor the start of the value
     * @param endsWith anchor the end of the value
     * @param ignoreCase match without regard to case
     * @param expectedOperator the operator those flags stand for
     */
    @Test(dataProvider = "searchFlagCombinations")
    public void theSearchFlagsChooseTheComparisonOperator(boolean                    startsWith,
                                                          boolean                    endsWith,
                                                          boolean                    ignoreCase,
                                                          PropertyComparisonOperator expectedOperator)
    {
        SearchOptions searchOptions = new SearchOptions();

        searchOptions.setStartsWith(startsWith);
        searchOptions.setEndsWith(endsWith);
        searchOptions.setIgnoreCase(ignoreCase);

        SearchProperties searchProperties = propertyHelper.getSearchPropertiesByName(List.of("qualifiedName"),
                                                                                     "widget-1",
                                                                                     searchOptions);

        assertNotNull(searchProperties);
        assertEquals(searchProperties.getConditions().get(0).getOperator(), expectedOperator);
    }


    /**
     * With no search options supplied the defaults apply, and the defaults are the widest search there is -
     * a case insensitive contains.  A caller who supplies nothing gets too many results rather than none.
     */
    @Test
    public void noSearchOptionsMeansTheWidestSearch()
    {
        SearchProperties searchProperties = propertyHelper.getSearchPropertiesByName(List.of("qualifiedName"),
                                                                                     "widget",
                                                                                     null);

        assertNotNull(searchProperties);
        assertEquals(searchProperties.getConditions().get(0).getOperator(),
                     PropertyComparisonOperator.CASE_INSENSITIVE_LIKE);
    }


    /**
     * The exact match options tighten the three flags down to an exact, case sensitive match - which is the
     * combination that produces {@code EQ}.  The two methods are used together throughout the handlers, so
     * the pairing matters as much as either one on its own.
     */
    @Test
    public void theExactMatchOptionsProduceAnExactMatch()
    {
        SearchOptions searchOptions = propertyHelper.getExactMatchSearchOptions(new QueryOptions());

        assertTrue(searchOptions.getStartsWith());
        assertTrue(searchOptions.getEndsWith());
        assertFalse(searchOptions.getIgnoreCase());

        SearchProperties searchProperties = propertyHelper.getSearchPropertiesByName(List.of("qualifiedName"),
                                                                                     "widget-1",
                                                                                     searchOptions);

        assertNotNull(searchProperties);
        assertEquals(searchProperties.getConditions().get(0).getOperator(), PropertyComparisonOperator.EQ);
    }


    /**
     * The exact match options are a new object.  Handlers derive them part way through a call and go on to
     * use the caller's own options afterwards, so tightening them must not tighten the query the caller
     * asked for.
     */
    @Test
    public void theExactMatchOptionsDoNotChangeTheSuppliedOptions()
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setPageSize(25);
        queryOptions.setStartFrom(50);

        SearchOptions searchOptions = propertyHelper.getExactMatchSearchOptions(queryOptions);

        assertEquals(searchOptions.getPageSize(), 25, "the paging should be carried over");
        assertEquals(searchOptions.getStartFrom(), 50);
        assertNotSame(searchOptions, queryOptions, "the exact match options should be a new object");

        searchOptions.setPageSize(1);

        assertEquals(queryOptions.getPageSize(), 25, "the supplied options should be untouched");
    }


    /**
     * A name search covers every property name it is given, and any one of them matching is enough.  Getting
     * the match criteria wrong here would turn "a term called Address or with Address as its display name"
     * into "a term whose every name is Address", which finds nothing.
     */
    @Test
    public void everyPropertyNameGetsItsOwnConditionAndAnyOneMatchingSuffices()
    {
        List<String> propertyNames = List.of("qualifiedName", "displayName", "name");

        SearchProperties searchProperties = propertyHelper.getSearchPropertiesByName(propertyNames,
                                                                                     "Address",
                                                                                     propertyHelper.getExactMatchSearchOptions(null));

        assertNotNull(searchProperties);
        assertEquals(searchProperties.getMatchCriteria(), MatchCriteria.ANY);
        assertEquals(searchProperties.getConditions().size(), 3);

        List<String> conditionProperties = new ArrayList<>();

        for (PropertyCondition condition : searchProperties.getConditions())
        {
            conditionProperties.add(condition.getProperty());

            PrimitiveTypePropertyValue value = (PrimitiveTypePropertyValue) condition.getValue();

            assertEquals(value.getPrimitiveValue(), "Address");
            assertEquals(value.getPrimitiveTypeCategory(), PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        }

        assertEquals(conditionProperties, propertyNames);
    }


    /**
     * With no property names to search there is nothing to ask for, and null is returned rather than an
     * empty set of conditions - an empty {@code SearchProperties} would be a query that matches everything.
     */
    @Test
    public void noPropertyNamesMeansNoSearchProperties()
    {
        assertNull(propertyHelper.getSearchPropertiesByName(null, "Address", null));
        assertNull(propertyHelper.getSearchPropertiesByName(new ArrayList<>(), "Address", null));
    }


    /**
     * A search condition list is created on demand and added to in place, and a null value adds nothing -
     * the same contract the property builders follow, and for the same reason: a caller builds up conditions
     * for whatever the request happened to supply without testing each one.
     */
    @Test
    public void searchConditionsAreBuiltUpAndANullValueAddsNothing()
    {
        assertNull(propertyHelper.addStringProperty(null, "qualifiedName", null, PropertyComparisonOperator.EQ),
                   "a null value should not start a condition list");

        List<PropertyCondition> conditions = propertyHelper.addStringProperty(null,
                                                                              "qualifiedName",
                                                                              "widget-1",
                                                                              PropertyComparisonOperator.EQ);

        assertNotNull(conditions);
        assertEquals(conditions.size(), 1);

        List<PropertyCondition> moreConditions = propertyHelper.addIntProperty(conditions,
                                                                               "domainIdentifier",
                                                                               42,
                                                                               PropertyComparisonOperator.EQ);

        assertSame(moreConditions, conditions, "conditions should be added to the list, not to a copy of it");
        assertEquals(conditions.size(), 2);

        propertyHelper.addStringProperty(conditions, "displayName", null, PropertyComparisonOperator.EQ);

        assertEquals(conditions.size(), 2, "a null value should not add a condition");

        assertEquals(conditions.get(0).getProperty(), "qualifiedName");
        assertEquals(conditions.get(0).getOperator(), PropertyComparisonOperator.EQ);
        assertEquals(conditions.get(1).getProperty(), "domainIdentifier");
        assertEquals(((PrimitiveTypePropertyValue) conditions.get(1).getValue()).getPrimitiveTypeCategory(),
                     PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /**
     * Asking for a classification by name produces a condition for that classification and nothing else; no
     * classification name produces no classification search at all, which is what leaves a query unfiltered
     * rather than filtered on a classification nobody named.
     */
    @Test
    public void aClassificationSearchIsOnlyBuiltWhenAClassificationIsNamed()
    {
        assertNull(propertyHelper.getSearchClassifications((String) null));

        SearchClassifications searchClassifications = propertyHelper.getSearchClassifications("Confidentiality");

        assertNotNull(searchClassifications);
        assertEquals(searchClassifications.getMatchCriteria(), MatchCriteria.ALL);
        assertEquals(searchClassifications.getConditions().size(), 1);
        assertEquals(searchClassifications.getConditions().get(0).getName(), "Confidentiality");
        assertNull(searchClassifications.getConditions().get(0).getSearchProperties(),
                   "a search by classification name alone should not constrain the classification's properties");
    }


    /**
     * The anchor search looks for the named value in the named property of the Anchors classification, and
     * has to match it exactly - an anchor search that matched on a prefix would pull in every element
     * anchored to anything whose guid started with the same characters.
     */
    @Test
    public void theAnchorSearchMatchesTheAnchorExactly()
    {
        SearchClassifications searchClassifications =
                propertyHelper.getAnchorSearchClassifications("3f8a5b21-4c7e-4a19-9d2b-6e0c1f7a8d34", "anchorGUID");

        assertEquals(searchClassifications.getMatchCriteria(), MatchCriteria.ALL);
        assertEquals(searchClassifications.getConditions().size(), 1);

        ClassificationCondition classificationCondition = searchClassifications.getConditions().get(0);

        assertEquals(classificationCondition.getName(), OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        SearchProperties classificationProperties = classificationCondition.getSearchProperties();

        assertEquals(classificationProperties.getMatchCriteria(), MatchCriteria.ALL);
        assertEquals(classificationProperties.getConditions().size(), 1);

        PropertyCondition propertyCondition = classificationProperties.getConditions().get(0);

        assertEquals(propertyCondition.getProperty(), "anchorGUID");
        assertEquals(propertyCondition.getOperator(), PropertyComparisonOperator.EQ);
        assertEquals(((PrimitiveTypePropertyValue) propertyCondition.getValue()).getPrimitiveValue(),
                     "3f8a5b21-4c7e-4a19-9d2b-6e0c1f7a8d34");
    }
}
