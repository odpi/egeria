/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * PagingIterator supports an iterator over a list of objects that extend ElementBase.
 * Callers can use it to step through the list just once.  If they want to parse the list again,
 * they could use the copy/clone constructor to create a new iterator.
 * </p>
 * <p>
 * PagingIterator provides paging support to enable the list in the iterator to be much bigger than
 * can be stored in memory.  It maintains pointers for the full list, and a potentially smaller
 * cached list that is used to batch up elements being retrieved from the property (metadata) server.
 * </p>
 * <p>
 * Thus is combines the ability to process very large lists whilst minimising the chatter with the property server.
 * The class uses a number of pointers and counts.  Imagine a list of 4000 columns in a table.
 * We can not retrieve all 4000 columns on one call. So we retrieve subsets over a number of REST calls as the
 * caller walks through the full list.
 * </p>
 * <ul>
 *     <li>
 *         maxCacheSize is the maximum elements we can retrieve in one call
 *     </li>
 *     <li>
 *         cachedElementList.size() is how many elements we have in memory at this time.
 *     </li>
 * </ul>
 */
public class PagingIterator extends PropertyBase implements Iterator<ElementBase>
{
    /**
     * Maximum that can be returned on each retrieve
     */
    protected int               maxCacheSize         = 10;

    /**
     * StartFrom used on calls to the repository
     */
    protected int               cachedElementStart   = 0;

    /**
     * Elements retrieved from the repository
     */
    protected List<ElementBase> cachedElementList    = new ArrayList<>();

    /**
     * Pointer in the cached list to the next item to return.
     */
    protected int               cachedElementPointer = 0;

    /**
     * Implementation that retrieves values from the repository.
     */
    protected PropertyIteratorBase iterator = null;

    /**
     * Logging destination.
     */
    protected static final Logger log = LoggerFactory.getLogger(PagingIterator.class);



    /**
     * Typical Constructor creates an iterator with the supplied list of comments.
     *
     * @param iterator type-specific iterator that wraps this paging iterator.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public PagingIterator(PropertyIteratorBase iterator,
                          int                  maxCacheSize)
    {
        super();

        log.debug("New PagingIterator:");
        log.debug("==> maxCacheSize: " + maxCacheSize);

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
            throw new OCFRuntimeException(OCFErrorCode.NO_ITERATOR.getMessageDefinition(this.getClass().getSimpleName()),
                                          this.getClass().getName(),
                                          "next");
        }
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param iterator type-specific iterator that wraps this paging iterator.
     * @param templateIterator template to copy; null to create an empty iterator
     */
    public PagingIterator(PropertyIteratorBase iterator,
                          PagingIterator       templateIterator)
    {
        super(templateIterator);

        if (templateIterator != null)
        {
            this.cachedElementStart = 0;
            this.cachedElementPointer = 0;
            this.cachedElementList = new ArrayList<>();

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
                throw new OCFRuntimeException(OCFErrorCode.NO_ITERATOR.getMessageDefinition(this.getClass().getSimpleName()),
                                              this.getClass().getName(),
                                              "next");
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
        if (cachedElementList == null)
        {
            /*
             * Cached element list is only null when the caller has retrieved the last item
             */
            return false;
        }

        if (cachedElementPointer == cachedElementList.size())
        {

            try
            {
                cachedElementList = iterator.getCachedList(cachedElementStart, maxCacheSize);
                cachedElementStart = cachedElementStart + maxCacheSize;
                if (cachedElementList == null)
                {
                    return false;
                }

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

        return true;
    }


    /**
     * Return the next element in the list
     *
     * @return ElementBase next element.
     * @throws OCFRuntimeException if there are no more elements in the list or there are problems retrieving
     *                             elements from the property (metadata) server.
     */
    @Override
    public ElementBase next()
    {
        if (this.hasNext())
        {
            ElementBase retrievedElement = iterator.cloneElement(cachedElementList.get(cachedElementPointer));
            cachedElementPointer++;

            log.debug("Returning next element:");
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
            throw new OCFRuntimeException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition(this.getClass().getName()),
                                                                                             this.getClass().getName(),
                                                                                             "next");
        }
    }


    /**
     * Remove the current element in the iterator.  This call is not supported and results in
     * an exception
     */
    @Override
    public void remove()
    {
        throw new OCFRuntimeException(OCFErrorCode.UNABLE_TO_REMOVE.getMessageDefinition(iterator.getClass().getName()),
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
        return "PagingIterator{" +
                "maxCacheSize=" + maxCacheSize +
                ", cachedElementStart=" + cachedElementStart +
                ", cachedElementList=" + cachedElementList +
                ", cachedElementPointer=" + cachedElementPointer +
                '}';
    }
}