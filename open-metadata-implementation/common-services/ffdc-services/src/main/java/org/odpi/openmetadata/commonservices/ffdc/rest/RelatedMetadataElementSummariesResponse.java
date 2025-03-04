/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelatedMetadataElementSummariesResponse is the response structure used on the OMAS REST API calls that return a
 * list of element summaries as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataElementSummariesResponse extends FFDCResponseBase
{
    private List<RelatedMetadataElementSummary> elements = null;
    private String                              mermaidGraph = null;


    /**
     * Default constructor
     */
    public RelatedMetadataElementSummariesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataElementSummariesResponse(RelatedMetadataElementSummariesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.elements = template.getElements();
            this.mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the list of element identifiers.
     *
     * @return list of objects or null
     */
    public List<RelatedMetadataElementSummary> getElements()
    {
        return elements;
    }


    /**
     * Set up the list of element identifiers.
     *
     * @param elements - list of objects or null
     */
    public void setElements(List<RelatedMetadataElementSummary> elements)
    {
        this.elements = elements;
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
        return "RelatedMetadataElementSummariesResponse{" +
                "elements=" + elements +
                ", mermaidGraph='" + mermaidGraph + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof RelatedMetadataElementSummariesResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(this.elements, that.elements) &&
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
        return Objects.hash(elements, mermaidGraph, super.hashCode());
    }
}
