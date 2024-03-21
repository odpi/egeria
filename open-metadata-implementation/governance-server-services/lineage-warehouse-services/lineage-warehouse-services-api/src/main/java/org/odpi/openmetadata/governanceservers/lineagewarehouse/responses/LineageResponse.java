/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVerticesAndEdges;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * Return a lineage graph
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageResponse extends FFDCResponseBase
{
    public LineageResponse()
    {
        super();
    }

    private LineageVerticesAndEdges lineageVerticesAndEdges;

    public LineageResponse(LineageVerticesAndEdges lineageVerticesAndEdges)
    {
        super();
        this.lineageVerticesAndEdges = lineageVerticesAndEdges;
    }


    /**
     * Return the graph
     *
     * @return graph structure
     */
    public LineageVerticesAndEdges getLineageVerticesAndEdges() {
        return lineageVerticesAndEdges;
    }


    /**
     * Set up the graph structure
     *
     * @param lineageVerticesAndEdges graph structure
     */
    public void setLineageVerticesAndEdges(LineageVerticesAndEdges lineageVerticesAndEdges)
    {
        this.lineageVerticesAndEdges = lineageVerticesAndEdges;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "LineageResponse{" +
                "lineageVerticesAndEdges=" + lineageVerticesAndEdges +
                "} " + super.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LineageResponse that = (LineageResponse) objectToCompare;
        return Objects.equals(lineageVerticesAndEdges, that.lineageVerticesAndEdges);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), lineageVerticesAndEdges);
    }
}
