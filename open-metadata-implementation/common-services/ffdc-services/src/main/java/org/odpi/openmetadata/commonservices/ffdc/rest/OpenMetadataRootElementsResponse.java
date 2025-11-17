/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OpenMetadataRootElementsResponse is the response structure used on the OMVS REST API calls that returns
 * a list of open metadata bean elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRootElementsResponse extends FFDCResponseBase
{
    private List<OpenMetadataRootElement> elements = null;
    private String                        mermaidGraph = null;


    /**
     * Default constructor
     */
    public OpenMetadataRootElementsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRootElementsResponse(OpenMetadataRootElementsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.elements = template.getElements();
            this.mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the element list result.
     *
     * @return unique identifier
     */
    public List<OpenMetadataRootElement> getElements()
    {
        return elements;
    }


    /**
     * Set up the element list result.
     *
     * @param elements - unique identifier
     */
    public void setElements(List<OpenMetadataRootElement> elements)
    {
        this.elements = elements;
    }


    /**
     * Return mermaid graph for the list.
     *
     * @return string
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up mermaid graph for the list.
     *
     * @param mermaidGraph string
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
        return "OpenMetadataRootElementsResponse{" +
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
        if (!(objectToCompare instanceof OpenMetadataRootElementsResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(elements, that.elements) &&
                Objects.equals(this.mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elements, mermaidGraph);
    }
}
