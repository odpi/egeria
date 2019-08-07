/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.testng.Assert.*;

public class OMRSRepositoryHelperTest
{
    @Test
    void testformatRelationshipResults() throws PropertyErrorException, PagingErrorException {
        List<Relationship>   fullResults =null;
        int fromElement =0;
        // test null
        List<Relationship>  relationships = createHelper().formatRelationshipResults(fullResults,
            fromElement,
            null,
            SequencingOrder.ANY,
            0);
        assertTrue(relationships ==null);


        // test 1 relationship no paging
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                0);
        assertTrue(relationships.size()==1);

        // test 1 relationship page size 1
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                1);
        assertTrue(relationships.size()==1);


        // test 1 relationship page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==1);

        // test 3 relationships page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==3);

        // test 4 relationships page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertEquals(relationships.size(),3);

        // test 4 relationships page size 3 offset 3
        fromElement =3;
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==1);

        // test 4 relationships page size 3 offset 2
        fromElement =2;
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==2);



    }

    @Test
    void testRegexHelpers() {

        // Start with a regex and a string that it will match
        String testRegex = ".*[A-Za-z].*";
        String testMatch = "somethingABCelse";

        // test 1 ensure that the regular expression is match-able
        assertTrue(Pattern.matches(testRegex, testMatch));

        // test 2 ensure that it is not recognized as an exact match (non-regex)
        assertFalse(createHelper().isExactMatchRegex(testRegex));

        // test 3 ensure that getting the exact match literal IS recognized as an exact match (not a regex)
        String testExactMatch = createHelper().getExactMatchRegex(testRegex);
        assertTrue(createHelper().isExactMatchRegex(testExactMatch));

        // test 4 ensure that the exact match literal does NOT match the test match any more
        assertFalse(Pattern.matches(testExactMatch, testMatch));

        // test 5 ensure that the exact match literal matches the original (regex) string
        assertTrue(Pattern.matches(testExactMatch, testRegex));

        // test 6 ensure that un-qualifying the exact match gives us our original string back
        String testBack = createHelper().getUnqualifiedLiteralString(testExactMatch);
        assertTrue(testBack.equals(testRegex));

        String testString = "a-b-c-d-e-f-g";

        // test 7 test that "contains" works
        String testContains = createHelper().getContainsRegex("c-d-e");
        assertTrue(Pattern.matches(testContains, testString));
        assertFalse(Pattern.matches(testContains, "d"));

        // test 8 test that "startswith" works
        String testStartsWith = createHelper().getStartsWithRegex("a-b-c");
        assertTrue(Pattern.matches(testStartsWith, testString));
        assertFalse(Pattern.matches(testStartsWith, "x" + testString));

        // test 9 test that "endswith" works
        String testEndsWith = createHelper().getEndsWithRegex("e-f-g");
        assertTrue(Pattern.matches(testEndsWith, testString));
        assertFalse(Pattern.matches(testEndsWith, testString + "x"));

    }

    private OMRSRepositoryHelper createHelper() {
        return new OMRSRepositoryContentHelper(null);
    }
}
