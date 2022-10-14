/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;

import java.util.ArrayList;
import java.util.List;


/**
 * MockRelatedAssets implements the abstract methods for RelatedAssets so it can be tested.
 */
public class MockRelatedAssets extends RelatedAssets
{
    private static final long     serialVersionUID = 1L;

    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param totalElementCount - the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize - maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public MockRelatedAssets(int             totalElementCount,
                             int             maxCacheSize)
    {
        super(totalElementCount, maxCacheSize);
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template - type-specific iterator to copy; null to create an empty iterator
     */
    public MockRelatedAssets(RelatedAssets template)
    {
        super(template);
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    protected RelatedAssets cloneIterator()
    {
        return new MockRelatedAssets(this);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer - where to start the cache.
     * @param maximumSize - maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     */
    protected List<ElementBase> getCachedList(int  cacheStartPointer,
                                              int  maximumSize)
    {
        int                            numberOfEntries;
        List<ElementBase>        propertyList = new ArrayList<>();

        if (cacheStartPointer + maximumSize > super.pagingIterator.getElementCount())
        {
            numberOfEntries = super.pagingIterator.getElementCount() - cacheStartPointer;
        }
        else
        {
            numberOfEntries = maximumSize;
        }

        for (int i=0; i< numberOfEntries ; i++)
        {
            propertyList.add(new RelatedAsset());
        }

        return propertyList;
    }
}
