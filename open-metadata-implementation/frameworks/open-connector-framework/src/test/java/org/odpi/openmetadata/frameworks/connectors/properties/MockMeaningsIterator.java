/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

import java.util.ArrayList;
import java.util.List;


/**
 * MockMeaningsIterator supports the iterator for the Meanings object.  It is used to test the logic of
 * the PagingIterator.
 */
public class MockMeaningsIterator extends Meanings
{
    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param parentAsset - descriptor of parent asset
     * @param totalElementCount - the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize - maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public MockMeaningsIterator(AssetDescriptor parentAsset,
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
    public MockMeaningsIterator(AssetDescriptor   parentAsset, Meanings    template)
    {
        super(parentAsset, template);
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset - descriptor of parent asset
     * @return new cloned object.
     */
    protected  Meanings  cloneIterator(AssetDescriptor  parentAsset)
    {
        return new MockMeaningsIterator(parentAsset, this);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer - where to start the cache.
     * @param maximumSize - maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException - there is a problem retrieving elements from the property (metadata) server.
     */
    protected List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                    int  maximumSize) throws PropertyServerException
    {
        int                            count = cacheStartPointer;
        int                            numberOfEntries = 0;
        List<AssetPropertyBase>        meanings = new ArrayList<>();

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
            Meaning meaning = new Meaning(null, null, Integer.toString(count++), null, null, Integer.toString(count++), null);
            meanings.add(meaning);
        }

        return meanings;
    }
}
