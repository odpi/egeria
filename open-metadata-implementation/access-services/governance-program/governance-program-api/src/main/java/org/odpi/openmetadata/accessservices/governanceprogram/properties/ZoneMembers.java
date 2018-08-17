/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import java.util.Iterator;


public class ZoneMembers implements Iterator<AssetSummary>
{

    /**
     * The iterator can only be used once to step through the elements.  This method returns
     * a boolean to indicate if it has got to the end of the list yet.
     *
     * @return boolean indicating whether there are more elements.
     */
    @Override
    public boolean hasNext()
    {
        return false;
    }


    /**
     * Return the next element in the iteration.
     *
     * @return Rating next element object that has been cloned.
     */
    @Override
    public AssetSummary next()
    {
        return null;
    }


    /**
     * Remove the current element in the iterator. (Null implementation since this iterator works off of cached
     * elements from the property (metadata) server.)
     */
    @Override
    public void remove()
    {
    }
}
