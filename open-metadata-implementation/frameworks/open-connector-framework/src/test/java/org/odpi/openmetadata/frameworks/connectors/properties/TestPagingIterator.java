/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class TestPagingIterator
{
    @Test public void testEmptyMeanings()
    {
        Meanings meanings = new MockMeaningsIterator(null, 0, 100);

        int elementCount = 0;
        while (meanings.hasNext())
        {
            assertTrue(meanings.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 0);
    }


    @Test public void testSmallMeanings()
    {
        Meanings meanings = new MockMeaningsIterator(null, 10, 100);

        int elementCount = 0;
        while (meanings.hasNext())
        {
            assertTrue(meanings.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }

    @Test public void testBoundaryMeanings()
    {
        Meanings meanings = new MockMeaningsIterator(null, 10, 10);

        int elementCount = 0;
        while (meanings.hasNext())
        {
            assertTrue(meanings.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }


    @Test public void testLargeMeanings()
    {
        Meanings meanings = new MockMeaningsIterator(null, 25, 10);

        int elementCount = 0;
        while (meanings.hasNext())
        {
            assertTrue(meanings.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 25);
    }


    @Test public void testLargeBoundaryMeanings()
    {
        Meanings meanings    = new MockMeaningsIterator(null, 30, 10);

        int elementCount = 0;
        while (meanings.hasNext())
        {
            assertTrue(meanings.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 30);
    }
}