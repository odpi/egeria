/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;

import java.io.Serial;
import java.util.List;


/**
 * Base class for the iterators supporting asset universe
 */
public abstract class PropertyIteratorBase extends PropertyBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    protected PagingIterator pagingIterator = null;


    /**
     * Typical Constructor creates an iterator with the supplied list of comments.
     *
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    protected PropertyIteratorBase(int maxCacheSize)
    {
        super();

        pagingIterator = new PagingIterator(this, maxCacheSize);
    }


    /**
     * Copy/clone constructor sets up details of the parent asset from the template
     *
     * @param  template PropertyIteratorBase to copy
     */
    protected PropertyIteratorBase(PropertyIteratorBase template)
    {
        super(template);

        if (template != null)
        {
            pagingIterator = new PagingIterator(this, template.pagingIterator);
        }
    }


    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param template object to clone
     * @return new cloned object.
     */
    protected abstract ElementBase cloneElement(ElementBase template);


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected abstract List<ElementBase> getCachedList(int  cacheStartPointer,
                                                       int  maximumSize) throws PropertyServerException;
}
