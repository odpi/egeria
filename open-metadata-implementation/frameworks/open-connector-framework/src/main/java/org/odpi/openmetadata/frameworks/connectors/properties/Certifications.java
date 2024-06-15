/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Certification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.util.Iterator;

/**
 * Certifications supports an iterator over a list of certifications awarded to the asset.
 * Callers can use it to step through the list
 * just once.  If they want to parse the list again, they could use the copy/clone constructor to create
 * a new iterator.
 */
public abstract class Certifications extends PropertyIteratorBase implements Iterator<Certification>
{
    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public Certifications(int maxCacheSize)
    {
        super(maxCacheSize);
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    public Certifications(Certifications template)
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
        if (template instanceof Certification)
        {
            return new Certification((Certification) template);
        }

        return null;
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    protected  abstract Certifications cloneIterator();


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
     * @return Certification next element object that has been cloned.
     */
    @Override
    public Certification next()
    {
        return (Certification)super.pagingIterator.next();
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
        return "Certifications{" +
                "pagingIterator=" + pagingIterator +
                '}';
    }
}