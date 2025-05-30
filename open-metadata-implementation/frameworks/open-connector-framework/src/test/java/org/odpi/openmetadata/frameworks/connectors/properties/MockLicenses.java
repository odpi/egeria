/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.License;

import java.util.ArrayList;
import java.util.List;


/**
 * MockLicenses implements the abstract methods for Licenses, so it can be tested.
 */
public class MockLicenses extends Licenses
{
    int totalElementCount = 0;

    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param totalElementCount - the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize - maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public MockLicenses(int             totalElementCount,
                        int             maxCacheSize)
    {
        super(maxCacheSize);

        this.totalElementCount = totalElementCount;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template - type-specific iterator to copy; null to create an empty iterator
     */
    public MockLicenses(MockLicenses template)
    {
        super(template);

        this.totalElementCount = template.totalElementCount;
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    protected Licenses cloneIterator()
    {
        return new MockLicenses(this);
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

        if (cacheStartPointer + maximumSize > totalElementCount)
        {
            numberOfEntries = totalElementCount - cacheStartPointer;
        }
        else
        {
            numberOfEntries = maximumSize;
        }

        if (numberOfEntries <= 0)
        {
            return null;
        }

        for (int i=0; i< numberOfEntries ; i++)
        {
            propertyList.add(new License());
        }

        return propertyList;
    }
}
