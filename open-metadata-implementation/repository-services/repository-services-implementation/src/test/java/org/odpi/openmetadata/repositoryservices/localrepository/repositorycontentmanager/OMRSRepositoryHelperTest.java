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

        // Start with a regex
        String testRegex = ".*[A-Za-z].*";

        // test 1 ensure that it is not recognized as a literal (non-regex)
        assertFalse(createHelper().isLiteral(testRegex));

        // test 2 ensure that getting the qualified literal IS recognized as a literal (not a regex)
        String testLiteral = createHelper().getQualifiedLiteralString(testRegex);
        assertTrue(createHelper().isLiteral(testLiteral));

        // test 3 ensure that unqualifying the literal gives us our original string back
        String testBack = createHelper().getUnqualifiedLiteralString(testLiteral);
        assertTrue(testBack.equals(testRegex));

    }

    private OMRSRepositoryHelper createHelper() {
        return new OMRSRepositoryContentHelper(null);
    }
}
