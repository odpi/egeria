/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVertex;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageSearchResponse extends FFDCResponseBase
{
    private List<LineageVertex> vertices = new ArrayList<>();


    /**
     * Default constructor.
     */
    public LineageSearchResponse()
    {
        super();
    }


    /**
     * Set up bean constructor.
     *
     * @param result list of nodes
     */
    public LineageSearchResponse(List<LineageVertex> result)
    {
        super();

        this.vertices = result;
    }

    /**
     * Return the list of nodes.
     *
     * @return list of nodes
     */
    public List<LineageVertex> getVertices()
    {
        return vertices;
    }


    /**
     * Set up the list of nodes.
     *
     * @param vertices list of nodes
     */
    public void setVertices(List<LineageVertex> vertices)
    {
        this.vertices = vertices;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "LineageSearchResponse{" +
                "vertices=" + vertices +
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
        LineageSearchResponse that = (LineageSearchResponse) objectToCompare;
        return Objects.equals(vertices, that.vertices);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), vertices);
    }
}
