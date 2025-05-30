/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CertificationElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CertificationElementsResponse is the response structure used on the OMAS REST API calls that return a
 * list of related certification element summaries as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationElementsResponse extends FFDCResponseBase
{
    private MetadataElementSummary     startingElement = null;
    private List<CertificationElement> elements        = null;
    private String                     mermaidGraph    = null;


    /**
     * Default constructor
     */
    public CertificationElementsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificationElementsResponse(CertificationElementsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.startingElement = template.getStartingElement();
            this.elements = template.getElements();
            this.mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return details of the starting element.
     *
     * @return element
     */
    public MetadataElementSummary getStartingElement()
    {
        return startingElement;
    }


    /**
     * Set up details of the starting element.
     *
     * @param startingElement element
     */
    public void setStartingElement(MetadataElementSummary startingElement)
    {
        this.startingElement = startingElement;
    }


    /**
     * Return the list of element identifiers.
     *
     * @return list of objects or null
     */
    public List<CertificationElement> getElements()
    {
        return elements;
    }


    /**
     * Set up the list of element identifiers.
     *
     * @param elements - list of objects or null
     */
    public void setElements(List<CertificationElement> elements)
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
        return "CertificationElementsResponse{" +
                "startingElement=" + startingElement +
                ", elements=" + elements +
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
        if (!(objectToCompare instanceof CertificationElementsResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(this.startingElement, that.startingElement) &&
                Objects.equals(this.elements, that.elements) &&
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
        return Objects.hash(super.hashCode(), startingElement, elements, mermaidGraph);
    }
}
