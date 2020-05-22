/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Graph;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GraphResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Graph object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GraphResponse extends SubjectAreaOMASAPIResponse
{
    private Graph graph = null;

    /**
     * Default constructor
     */
    public GraphResponse() {
        this.setResponseCategory(ResponseCategory.Graph);
    }
    public GraphResponse(Graph graph)
    {
        this();
        this.graph=graph;
    }


    /**
     * Return the Graph object.
     *
     * @return graph
     */
    public Graph getGraph()
    {
        return graph;
    }

    /**
     * Set up the Graph object.
     *
     * @param graph - graph object
     */
    public void setGraph(Graph graph)
    {
        this.graph = graph;
    }


    @Override
    public String toString()
    {
        return "GraphResponse{" +
                "graph=" + graph +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
