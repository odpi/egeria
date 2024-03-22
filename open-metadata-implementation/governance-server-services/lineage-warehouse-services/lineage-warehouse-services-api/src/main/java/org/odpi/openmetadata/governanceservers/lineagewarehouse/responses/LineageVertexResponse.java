/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVertex;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Return a single node in the lineage graph
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageVertexResponse extends FFDCResponseBase
{
    private LineageVertex lineageVertex;

    public LineageVertexResponse()
    {
        super();
    }

    public LineageVertexResponse(LineageVertex lineageVertex)
    {
        super();

        this.lineageVertex = lineageVertex;
    }

    public LineageVertex getLineageVertex()
    {
        return lineageVertex;
    }

    public void setLineageVertex(LineageVertex lineageVertex)
    {
        this.lineageVertex = lineageVertex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "LineageVertexResponse{" +
                "lineageVertex=" + lineageVertex +
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
        LineageVertexResponse that = (LineageVertexResponse) objectToCompare;
        return Objects.equals(lineageVertex, that.lineageVertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), lineageVertex);
    }
}
