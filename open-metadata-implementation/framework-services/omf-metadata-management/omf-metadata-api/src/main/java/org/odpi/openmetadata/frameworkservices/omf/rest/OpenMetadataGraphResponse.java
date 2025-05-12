/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementGraph;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The response structure used on the Governance Action Framework REST API calls
 * that returns an OpenMetadataElementGraph object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataGraphResponse extends FFDCResponseBase
{
    private OpenMetadataElementGraph elementGraph = null;

    /**
     * Default constructor
     */
    public OpenMetadataGraphResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataGraphResponse(OpenMetadataGraphResponse template)
    {
        super(template);

        if (template != null)
        {
            this.elementGraph = template.getElementGraph();
        }
    }


    /**
     * Return the graph object.
     *
     * @return graph object
     */
    public OpenMetadataElementGraph getElementGraph()
    {
        return elementGraph;
    }


    /**
     * Set up the graph object.
     *
     * @param elementGraph - graph object
     */
    public void setElementGraph(OpenMetadataElementGraph elementGraph)
    {
        this.elementGraph = elementGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OpenMetadataGraphResponse{" +
                "elementGraph=" + elementGraph +
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
        if (!(objectToCompare instanceof OpenMetadataGraphResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getElementGraph(), that.getElementGraph());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (elementGraph == null)
        {
            return super.hashCode();
        }
        else
        {
            return elementGraph.hashCode();
        }
    }
}
