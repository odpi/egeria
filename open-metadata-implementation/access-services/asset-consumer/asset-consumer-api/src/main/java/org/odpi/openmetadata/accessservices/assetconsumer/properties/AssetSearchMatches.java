/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.util.List;

/**
 * AssetSearchMatches is used to return an asset whose has anchored elements returned from a search.
 * The matching element are the OCF beans for the matching elements.
 */
public class AssetSearchMatches extends Asset
{
    List<ElementBase> matchingElements = null;

    /**
     * Default constructor
     */
    public AssetSearchMatches()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset summary
     */
    public AssetSearchMatches(Asset template)
    {
        super(template);

        if (template != null)
        {

        }
    }


    /**
     *
     * @return
     */
    public List<ElementBase> getMatchingElements()
    {
        return matchingElements;
    }

    public void setMatchingElements(List<ElementBase> matchingElements)
    {
        this.matchingElements = matchingElements;
    }


}
