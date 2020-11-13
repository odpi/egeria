/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


/**
 * TestAssetLicenses tests the invocation of a specific property iterator.
 */
public class TestAssetLicenses
{
    /**
     * Create a new property iterator
     *
     * @param totalElementCount number of elements available
     * @param maxCacheSize size of cache
     * @return property iterator
     */
    private AssetLicenses  getPropertyIterator(int    totalElementCount, int    maxCacheSize)
    {
        return new MockAssetLicenses(null, totalElementCount, maxCacheSize);
    }


    /**
     * Validate a combination of the paging iterate parameters works with this property iterator.
     *
     * @param totalElementCount number of elements available
     * @param maxCacheSize size of cache
     */
    private void  validatePropertyIterator(int    totalElementCount, int    maxCacheSize)
    {
        AssetLicenses iterator = getPropertyIterator(totalElementCount, maxCacheSize);

        int elementCount = 0;
        while (iterator.hasNext())
        {
            assertTrue(iterator.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == totalElementCount);
    }


    /**
     * Validate that a clone can be created
     */
    @Test public void  testClonedIterator()
    {
        AssetLicenses propertyIterator = getPropertyIterator(10, 100);

        propertyIterator.next();

        AssetLicenses propertyIteratorClone = new MockAssetLicenses(null, propertyIterator);

        int elementCount = 0;
        while (propertyIteratorClone.hasNext())
        {
            assertTrue(propertyIteratorClone.next() != null);
            elementCount ++;
        }

        assertTrue(elementCount == 10);
    }


    /**
     * Test that the iterator works well if there are no elements
     */
    @Test public void testEmptyIterator()
    {
        validatePropertyIterator(0, 100);
    }


    /**
     * Test that the iterator works when the number of elements is less than the cache size.
     */
    @Test public void testSmallIterator()
    {
        validatePropertyIterator(10,
                                 100);
    }


    /**
     * Test that the iterator works on a boundary between caches.
     */
    @Test public void testBoundaryIterator()
    {
        validatePropertyIterator(10,
                                 10);
    }


    /**
     * Test that the iterator works through multiple caches.
     */
    @Test public void testLargeIterator()
    {
        validatePropertyIterator(25,
                                 10);
    }


    /**
     * Test that the iterator works on a boundary between caches.
     */
    @Test public void testLargeBoundaryIterator()
    {
        validatePropertyIterator(30,
                                 10);
    }


    /**
     * Validate that element count is set.
     */
    @Test public void testElementCount()
    {
        AssetLicenses  propertyIterator = getPropertyIterator(30, 10);

        assertTrue(propertyIterator.getElementCount() == 30);

        AssetLicenses  clonedPropertyIterator = new MockAssetLicenses(null, propertyIterator);

        assertTrue(clonedPropertyIterator.getElementCount() == 30);

        clonedPropertyIterator = new MockAssetLicenses(null, null);

        assertTrue(clonedPropertyIterator.getElementCount() == 0);
    }


    /**
     * Validate that the remove request throws an exception.
     */
    @Test public void  testRemove()
    {
        try
        {
            getPropertyIterator(30, 10).remove();
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
     * Ensure toString is overridden in the subclass.
     */
    @Test public void testToString()
    {
        assertTrue(getPropertyIterator(10, 100).toString().contains("AssetLicenses"));
    }
}