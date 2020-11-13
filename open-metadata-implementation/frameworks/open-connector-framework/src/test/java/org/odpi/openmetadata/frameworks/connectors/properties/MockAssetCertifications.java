/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.ArrayList;
import java.util.List;


/**
 * MockAssetCertifications implements the abstract methods for AssetCertifications so it can be tested.
 */
public class MockAssetCertifications extends AssetCertifications
{
    private static final long     serialVersionUID = 1L;

    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param parentAsset - descriptor of parent asset
     * @param totalElementCount - the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize - maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public MockAssetCertifications(AssetDescriptor parentAsset,
                                   int             totalElementCount,
                                   int             maxCacheSize)
    {
        super(parentAsset, totalElementCount, maxCacheSize);
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset - descriptor of parent asset
     * @param template - type-specific iterator to copy; null to create an empty iterator
     */
    public MockAssetCertifications(AssetDescriptor   parentAsset, AssetCertifications template)
    {
        super(parentAsset, template);
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset - descriptor of parent asset
     * @return new cloned object.
     */
    protected AssetCertifications cloneIterator(AssetDescriptor  parentAsset)
    {
        return new MockAssetCertifications(parentAsset, this);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer - where to start the cache.
     * @param maximumSize - maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     */
    protected List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                    int  maximumSize)
    {
        int                            numberOfEntries;
        List<AssetPropertyBase>        propertyList = new ArrayList<>();

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
            AssetCertification propertyObject = new AssetCertification(null);
            propertyList.add(propertyObject);
        }

        return propertyList;
    }
}
