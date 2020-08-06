/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


/**
 * TestAssetPagingIterator focuses on testing the common methods associated with the iterators.
 */
public class TestAssetPagingIterator
{

    /**
     * Validate that a null iterator class creates an exception
     */
    @Test public void  testNullIterator()
    {
        try
        {
            new AssetPagingIterator(null,
                                    null,
                                    0,
                                    100);
            assertTrue(false);
        }
        catch (OCFRuntimeException err)
        {
            assertTrue(err.getMessage().contains("OCF-PROPERTIES-400-015 "));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that a null iterator class creates an exception during a clone
     */
    @Test public void  testNullClonedIterator()
    {
        AssetPagingIterator pagingIterator = new AssetPagingIterator(null,
                                                                     new MockAssetPropertyIterator(null,
                                                                                                   -10,
                                                                                                   -100),
                                                                     -10,
                                                                     -100);

        try
        {
            new AssetPagingIterator(null,
                                    null,
                                    pagingIterator);
            assertTrue(false);

        }
        catch (OCFRuntimeException err)
        {
            assertTrue(err.getMessage().contains("OCF-PROPERTIES-400-015 "));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that a clone can be created
     */
    @Test public void  testClonedIterator()
    {
        MockAssetPropertyIterator propertyIterator = new MockAssetPropertyIterator(null,
                                                                                   10,
                                                                                   100);

        propertyIterator.next();

        MockAssetPropertyIterator propertyIteratorClone = new MockAssetPropertyIterator(null,
                                                                                        propertyIterator);

        int elementCount = 0;
        while (propertyIteratorClone.hasNext())
        {
            assertTrue(propertyIteratorClone.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }

    /**
     * Validate that the paging iterator handles an error reading from the server.
     */
    @Test public void testDisconnectedServer()
    {
        MockDisconnectedAssetPropertyIterator iterator = new MockDisconnectedAssetPropertyIterator(null, 10, 100);

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (OCFRuntimeException err)
        {
            assertTrue(err.getMessage().contains("OCF-PROPERTIES-404-002 "));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Test that the iterator works well if there are no elements
     */
    @Test public void testEmptyIterator()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null, 0, 100);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 0);
    }


    /**
     * Test that the iterator works when the number of elements is less than the cache size.
     */
    @Test public void testSmallIterator()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null,
                                                                           10,
                                                                           100);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }


    /**
     * Test that the iterator works on a boundary between caches.
     */
    @Test public void testBoundaryIterator()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null,
                                                                           10,
                                                                           10);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }


    /**
     * Test that the iterator works through multiple caches.
     */
    @Test public void testLargeIterator()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null,
                                                                           25,
                                                                           10);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 25);
    }


    /**
     * Test that the iterator works on a boundary between caches.
     */
    @Test public void testLargeBoundaryIterator()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null,
                                                                           30,
                                                                           10);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 30);
    }


    /**
     * Validate that the paging iterator handles reading more elements than there is.
     */
    @Test public void testTooManyReads()
    {
        MockAssetPropertyIterator iterator = new MockAssetPropertyIterator(null, 10, 100);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (OCFRuntimeException err)
        {
            assertTrue(err.getMessage().contains("OCF-PROPERTIES-400-014 "));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that element count is set.
     */
    @Test public void testElementCount()
    {
        MockAssetPropertyIterator  propertyIterator = new MockAssetPropertyIterator(null, 30, 10);

        assertTrue(propertyIterator.getElementCount() == 30);

    }

    /**
     * Validate that the remove request throws an exception.
     */
    @Test public void  testRemove()
    {
        try
        {
            new MockAssetPropertyIterator(null, 30, 10).remove();
        }
        catch (OCFRuntimeException err)
        {
            assertTrue(err.getMessage().contains("OCF-PROPERTIES-400-018 "));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Ensure toString works if the method is not overridden in the subclass.
     */
    @Test public void testToString()
    {
        AssetPagingIterator pagingIterator = new AssetPagingIterator(null,
                                                                     new MockAssetPropertyIterator(null,
                                                                                                   0,
                                                                                                   100),
                                                                     0,
                                                                     100);

        assertTrue(pagingIterator.toString().contains("AssetPagingIterator"));
    }
}