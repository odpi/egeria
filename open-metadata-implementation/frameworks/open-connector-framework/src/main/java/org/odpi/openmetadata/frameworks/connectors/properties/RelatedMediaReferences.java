/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedMediaReference;

import java.util.Iterator;

/**
 * RelatedMediaReferences supports an iterator over a list of related media references.  Callers can use it to step
 * through the list just once.  If they want to parse the list again, they could use the copy/clone constructor to
 * create a new iterator.
 */
public abstract class RelatedMediaReferences extends PropertyIteratorBase
                                                  implements Iterator<RelatedMediaReference>
{
    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public RelatedMediaReferences(int maxCacheSize)
    {
        super(maxCacheSize);
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    public RelatedMediaReferences(RelatedMediaReferences template)
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
        if (template instanceof RelatedMediaReference)
        {
            return new RelatedMediaReference((RelatedMediaReference)template);
        }

        return null;
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    protected  abstract RelatedMediaReferences cloneIterator();


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
     * @return RelatedMediaReference next element object that has been cloned.
     */
    @Override
    public RelatedMediaReference next()
    {
        return (RelatedMediaReference)super.pagingIterator.next();
    }


    /**
     * Remove the current element in the iterator. (Null implementation since this iterator works off of cached
     * elements from the property (metadata) server.)
     */
    @Override
    public void remove()
    {
        throw new OMFRuntimeException(OCFErrorCode.UNABLE_TO_REMOVE.getMessageDefinition(this.getClass().getName()),
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
        return "RelatedMediaReferences{" +
                "pagingIterator=" + pagingIterator +
                '}';
    }
}