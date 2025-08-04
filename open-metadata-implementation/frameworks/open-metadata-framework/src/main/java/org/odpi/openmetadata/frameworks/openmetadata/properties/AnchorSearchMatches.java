/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;


import java.util.List;
import java.util.Objects;

/**
 * Captures the anchor open metadata element and corresponding matches retrieved during an
 * Anchors-based search request such as searching within an anchor context - anchor, domain, scope
 */
public class AnchorSearchMatches extends OpenMetadataElement
{
    List<OpenMetadataElement> matchingElements = null;

    /**
     * Typical Constructor
     */
    public AnchorSearchMatches()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public AnchorSearchMatches(OpenMetadataElement template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public AnchorSearchMatches(AnchorSearchMatches template)
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
    public List<OpenMetadataElement> getMatchingElements()
    {
        return matchingElements;
    }


    /**
     * Set up the list of anchored elements that match the search criteria.
     *
     * @param matchingElements list
     */
    public void setMatchingElements(List<OpenMetadataElement> matchingElements)
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
        return "AnchorSearchMatches{" +
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
        AnchorSearchMatches that = (AnchorSearchMatches) objectToCompare;
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

