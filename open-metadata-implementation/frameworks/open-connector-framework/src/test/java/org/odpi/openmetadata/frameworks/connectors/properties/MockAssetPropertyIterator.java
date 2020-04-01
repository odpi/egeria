/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MockAssetPropertyIterator extends AssetPropertyIteratorBase implements Iterator<MockAssetProperty>
{
    private static final long     serialVersionUID = 1L;

    /**
     * Typical Constructor creates an iterator with the supplied list of comments.
     *
     * @param parentAsset descriptor of parent asset
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    protected MockAssetPropertyIterator(AssetDescriptor              parentAsset,
                                        int                          totalElementCount,
                                        int                          maxCacheSize)
    {
        /*
         * Initialize superclass.
         */
        super(parentAsset, totalElementCount, maxCacheSize);

        pagingIterator = new AssetPagingIterator(parentAsset,
                                                 this,
                                                 totalElementCount,
                                                 maxCacheSize);
    }


    /**
     * Copy/clone constructor sets up details of the parent asset from the template
     *
     * @param parentAsset descriptor of asset that his property relates to.
     * @param  template AssetPropertyBaseImpl to copy
     */
    protected MockAssetPropertyIterator(AssetDescriptor            parentAsset,
                                        AssetPropertyIteratorBase template)
    {
        /*
         * Initialize superclass.
         */
        super(parentAsset, template);

        if (template != null)
        {
            pagingIterator = new AssetPagingIterator(parentAsset,
                                                     this,
                                                     template.pagingIterator);
        }
    }


    /**
     * The iterator can only be used once to step through the elements.  This method returns
     * a boolean to indicate if it has got to the end of the list yet.
     *
     * @return boolean indicating whether there are more elements.
     */
    @Override
    public boolean hasNext()
    {
        return super.pagingIterator.hasNext();
    }


    /**
     * Return the next element in the iteration.
     *
     * @return Meaning next element object that has been cloned.
     */
    @Override
    public MockAssetProperty next()
    {
        return (MockAssetProperty) super.pagingIterator.next();
    }


    /**
     * Remove the current element in the iterator. (Null implementation since this iterator works off of cached
     * elements from the property (metadata) server.)
     */
    @Override
    public void remove()
    {
        super.pagingIterator.remove();
    }


    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected AssetPropertyBase cloneElement(AssetDescriptor   parentAsset,
                                             AssetPropertyBase template)
    {
        return new MockAssetProperty(parentAsset, template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     */
    protected List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                    int  maximumSize)
    {
        int                            numberOfEntries;
        List<AssetPropertyBase>        propertyBaseArrayList = new ArrayList<>();

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
            MockAssetProperty  property = new MockAssetProperty(null);
            propertyBaseArrayList.add(property);
        }

        return propertyBaseArrayList;
    }
}
