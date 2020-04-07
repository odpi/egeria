/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PagingIterator supports an iterator over a list of objects that extend AssetPropertyBase.
 * Callers can use it to step through the list just once.  If they want to parse the list again,
 * they could use the copy/clone constructor to create a new iterator.
 *
 * PagingIterator provides paging support to enable the list in the iterator to be much bigger than
 * can be stored in memory.  It maintains pointers for the full list, and a potentially smaller
 * cached list that is used to batch up elements being retrieved from the property (metadata) server.
 * Thus is combines the ability to process very large lists whilst minimising the chatter with the property server.
 *
 * The class uses a number of pointers and counts.  Imagine a list of 4000 columns in a table.
 * We can not retrieve all 4000 columns on one call. So we retrieve subsets over a number of REST calls as the
 * caller walks through the full list.
 * <ul>
 *     <li>
 *         totalElement is 4000 - ie how many elements there are in total.
 *     </li>
 *     <li>
 *         maxCacheSize is the maximum elements we can retrieve in one call
 *     </li>
 *     <li>
 *         cachedElementList.size() is how many elements we have in memory at this time.
 *     </li>
 * </ul>
 *
 * cachedElementList.size() is set to maxCacheSize() for all retrieves except for processing the last cache of elements.
 * For example, if the totalElement is 25 and the maxCacheSize() is 10 then we would retrieve 3 caches -
 * the first two would have 10 elements in them and the third will have 5 elements.
 * In the first 2 retrieves, maxCacheSize and cachedElementList.size() are set to 10.
 * In the last one, maxCacheSize==10 and cachedElementList.size()==5.
 */
public class AssetPagingIterator extends AssetPropertyBase implements Iterator<AssetPropertyBase>
{
    private static final long     serialVersionUID = 1L;

    protected int                       maxCacheSize         = 1;

    protected int                       totalElementCount    = 0;
    protected int                       cachedElementStart   = 0;
    protected List<AssetPropertyBase>   cachedElementList    = new ArrayList<>();
    protected int                       cachedElementPointer = 0;

    protected AssetPropertyIteratorBase iterator = null;

    private static final Logger log = LoggerFactory.getLogger(AssetPagingIterator.class);



    /**
     * Typical Constructor creates an iterator with the supplied list of comments.
     *
     * @param parentAsset descriptor of parent asset.
     * @param iterator type-specific iterator that wraps this paging iterator.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public AssetPagingIterator(AssetDescriptor           parentAsset,
                               AssetPropertyIteratorBase iterator,
                               int                       totalElementCount,
                               int                       maxCacheSize)
    {
        super(parentAsset);

        log.debug("New PagingIterator:");
        log.debug("==> totalElementCount: " + totalElementCount);
        log.debug("==> maxCacheSize: " + maxCacheSize);

        if (totalElementCount > 0)
        {
            this.totalElementCount = totalElementCount;
        }

        if (maxCacheSize > 0)
        {
            this.maxCacheSize = maxCacheSize;
        }

        if (iterator != null)
        {
            this.iterator = iterator;
        }
        else
        {
            /*
             * Throw runtime exception to show the caller they are not using the list correctly.
             */
            throw new OCFRuntimeException(OCFErrorCode.NO_ITERATOR.getMessageDefinition(this.getClass().getSimpleName(),
                                                                                        super.getParentAssetName(),
                                                                                        super.getParentAssetTypeName()),
                                          this.getClass().getName(),
                                          "next");
        }
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param iterator type-specific iterator that wraps this paging iterator.
     * @param templateIterator template to copy; null to create an empty iterator
     */
    public AssetPagingIterator(AssetDescriptor           parentAsset,
                               AssetPropertyIteratorBase iterator,
                               AssetPagingIterator       templateIterator)
    {
        super(parentAsset, templateIterator);

        if (templateIterator != null)
        {
            this.cachedElementStart = 0;
            this.cachedElementPointer = 0;
            this.cachedElementList = new ArrayList<>();

            if (templateIterator.totalElementCount > 0)
            {
                this.totalElementCount = templateIterator.totalElementCount;
            }

            if (templateIterator.maxCacheSize > 0)
            {
                this.maxCacheSize = templateIterator.maxCacheSize;
            }

            if (iterator != null)
            {
                this.iterator = iterator;
            }
            else
            {
               /*
                * Throw runtime exception to show the caller they are not using the list correctly.
                */
                throw new OCFRuntimeException(OCFErrorCode.NO_ITERATOR.getMessageDefinition(this.getClass().getSimpleName(),
                                                                                            super.getParentAssetName(),
                                                                                            super.getParentAssetTypeName()),
                                              this.getClass().getName(),
                                              "next");
            }

            if (templateIterator.cachedElementStart <= templateIterator.maxCacheSize)
            {
                /*
                 * The template's cache starts at the beginning of the total list so ok to copy it.
                 */
                for (AssetPropertyBase templateElement : templateIterator.cachedElementList)
                {
                    this.cachedElementList.add(iterator.cloneElement(parentAsset, templateElement));
                }
            }
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
        return (cachedElementStart < totalElementCount);
    }


    /**
     * Return the next element in the list
     *
     * @return AssetPropertyBase next element.
     * @throws OCFRuntimeException if there are no more elements in the list or there are problems retrieving
     *                             elements from the property (metadata) server.
     */
    @Override
    public AssetPropertyBase next()
    {
        /*
         * Check more elements available
         */
        if (this.hasNext())
        {
            AssetPropertyBase retrievedElement;

            /*
             * If the pointer is at the end of the cache then retrieve more content from the property (metadata)
             * server.
             */
            if (cachedElementPointer == cachedElementList.size())
            {
                try
                {
                    cachedElementList = iterator.getCachedList(cachedElementStart, maxCacheSize);
                    cachedElementPointer = 0;
                }
                catch (PropertyServerException error)
                {
                    /*
                     * Problem retrieving next cache.  The exception includes a detailed error message,
                     */
                    throw new OCFRuntimeException(OCFErrorCode.PROPERTIES_NOT_AVAILABLE.getMessageDefinition(error.getReportedErrorMessage(),
                                                                                                             this.toString()),
                                                  this.getClass().getName(),
                                                  "next",
                                                  error);
                }
            }

            retrievedElement = iterator.cloneElement(getParentAsset(), cachedElementList.get(cachedElementPointer));
            cachedElementPointer++;
            cachedElementStart++;

            log.debug("Returning next element:");
            log.debug("==> totalElementCount: " + totalElementCount);
            log.debug("==> cachedElementPointer: " + cachedElementPointer);
            log.debug("==> cachedElementStart:" + cachedElementStart);
            log.debug("==> maxCacheSize:" + maxCacheSize);

            return retrievedElement;
        }
        else
        {
            /*
             * Throw runtime exception to show the caller they are not using the list correctly.
             */
            throw new OCFRuntimeException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition(this.getClass().getSimpleName(),
                                                                                             super.getParentAssetName(),
                                                                                             super.getParentAssetTypeName()),
                                          this.getClass().getName(),
                                          "next");
        }
    }


    /**
     * Return the number of elements in the list.
     *
     * @return elementCount
     */
    public int getElementCount()
    {
        return totalElementCount;
    }


    /**
     * Remove the current element in the iterator.  This call is not supported and results in
     * an exception
     */
    @Override
    public void remove()
    {
        throw new OCFRuntimeException(OCFErrorCode.UNABLE_TO_REMOVE.getMessageDefinition(this.getParentAssetTypeName(),
                                                                                         this.getParentAssetName(),
                                                                                         iterator.getClass().getName()),
                                      this.getClass().getName(),
                                      "remove");
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetPagingIterator{" +
                "maxCacheSize=" + maxCacheSize +
                ", totalElementCount=" + totalElementCount +
                ", cachedElementStart=" + cachedElementStart +
                ", cachedElementList=" + cachedElementList +
                ", cachedElementPointer=" + cachedElementPointer +
                '}';
    }
}