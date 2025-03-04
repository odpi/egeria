/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedMetadataElementSummaryList is for passing back a list of  RelatedMetadataElementSummary
 * or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataElementSummaryList
{
    private List<RelatedMetadataElementSummary> elementList = null;
    private String                              mermaidGraph = null;


    /**
     * Default constructor
     */
    public RelatedMetadataElementSummaryList()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataElementSummaryList(RelatedMetadataElementSummaryList template)
    {
        if (template != null)
        {
            elementList = template.getElementList();
            mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public List<RelatedMetadataElementSummary> getElementList()
    {
        return elementList;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param elementList result object
     */
    public void setElementList(List<RelatedMetadataElementSummary> elementList)
    {
        this.elementList = elementList;
    }


    /**
     * Return the mermaid string used to render a graph.
     *
     * @return string in Mermaid markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up mermaid string used to render a graph.
     *
     * @param mermaidGraph string in Mermaid markdown
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedMetadataElementSummaryList{" +
                "elementList=" + elementList +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        RelatedMetadataElementSummaryList that = (RelatedMetadataElementSummaryList) objectToCompare;
        return Objects.equals(elementList, that.elementList) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementList, mermaidGraph);
    }
}
