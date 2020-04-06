/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

import java.util.List;

public abstract class AssetPropertyIteratorBase extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected AssetPagingIterator pagingIterator = null;


    /**
     * Typical Constructor creates an iterator with the supplied list of comments.
     *
     * @param parentAsset descriptor of parent asset
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    protected AssetPropertyIteratorBase(AssetDescriptor              parentAsset,
                                        int                          totalElementCount,
                                        int                          maxCacheSize)
    {
        super(parentAsset);

        pagingIterator = new AssetPagingIterator(parentAsset, this, totalElementCount, maxCacheSize);
    }


    /**
     * Copy/clone constructor sets up details of the parent asset from the template
     *
     * @param parentAsset descriptor of asset that his property relates to.
     * @param  template AssetPropertyBaseImpl to copy
     */
    protected AssetPropertyIteratorBase(AssetDescriptor     parentAsset, AssetPropertyIteratorBase template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            pagingIterator = new AssetPagingIterator(parentAsset, this, template.pagingIterator);
        }
    }


    /**
     * Return the number of elements in the list.
     *
     * @return elementCount
     */
    public int getElementCount()
    {
        if (pagingIterator == null)
        {
            return 0;
        }
        else
        {
            return pagingIterator.getElementCount();
        }
    }


    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected abstract AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template);


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected abstract List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                             int  maximumSize) throws PropertyServerException;
}
