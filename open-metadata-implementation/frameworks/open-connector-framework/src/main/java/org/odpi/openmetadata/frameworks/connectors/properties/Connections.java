/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.io.Serial;
import java.util.Iterator;

/**
 * Connections supports an iterator over a list of connections.  Callers can use it to step through the list
 * just once.  If they want to parse the list again, they could use the copy/clone constructor to create
 * a new iterator.
 */
public abstract class Connections extends PropertyIteratorBase implements Iterator<Connection>
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public Connections(int maxCacheSize)
    {
        super(maxCacheSize);
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    public Connections(Connections template)
    {
        super(template);
    }


    /**
     * Provides a concrete implementation of cloneElement for the specific iterator type.
     *
     * @param template object to clone
     * @return new cloned object.
     */
    protected ElementBase cloneElement(ElementBase template)
    {
        if (template instanceof Connection)
        {
            return new Connection((Connection)template);
        }

        return null;
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    protected  abstract Connections cloneIterator();


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
     * @return Connection next element object that has been cloned.
     */
    @Override
    public Connection next()
    {
        return (Connection)super.pagingIterator.next();
    }


    /**
     * Remove the current element in the iterator. (Null implementation since this iterator works off of cached
     * elements from the property (metadata) server.)
     */
    @Override
    public void remove()
    {
        throw new OCFRuntimeException(OCFErrorCode.UNABLE_TO_REMOVE.getMessageDefinition(this.getClass().getName()),
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
        return "Connections{" +
                "pagingIterator=" + pagingIterator +
                '}';
    }
}