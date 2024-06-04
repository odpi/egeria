/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.List;
import java.util.Objects;

/**
 * AssetSearchMatches is used to return an asset whose has anchored elements returned from a search.
 * The matching element are the OCF beans for the matching elements.
 */
public class AssetSearchMatches extends Asset
{
    List<MetadataElement> matchingElements = null;

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
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset summary
     */
    public AssetSearchMatches(AssetSearchMatches template)
    {
        super(template);

        if (template != null)
        {
            matchingElements = template.getMatchingElements();
        }
    }


    /**
     * Return the list of anchored elements that match the search criteria.
     *
     * @return list
     */
    public List<MetadataElement> getMatchingElements()
    {
        return matchingElements;
    }


    /**
     * Set up the list of anchored elements that match the search criteria.
     *
     * @param matchingElements list
     */
    public void setMatchingElements(List<MetadataElement> matchingElements)
    {
        this.matchingElements = matchingElements;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetSearchMatches{" +
                "matchingElements=" + matchingElements +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetSearchMatches that = (AssetSearchMatches) objectToCompare;
        return Objects.equals(matchingElements, that.matchingElements);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), matchingElements);
    }
}
