/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LineageSearchRequest describes the properties for a lineage search.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageSearchRequest
{
    /**
     * The starting node.
     */
    private LineageRequestNode       queriedNode;


    /**
     * List of related nodes to restrict the search.
     */
    private List<LineageRequestNode> relatedNodes;


    /**
     * Default constructor.
     */
    public LineageSearchRequest()
    {
    }


    /**
     * Return the starting node.
     *
     * @return request node name and type
     */
    public LineageRequestNode getQueriedNode()
    {
        return queriedNode;
    }


    /**
     * Set up the starting node.
     *
     * @param queriedNode request node name and type
     */
    public void setQueriedNode(LineageRequestNode queriedNode)
    {
        this.queriedNode = queriedNode;
    }


    /**
     * Return the list of related nodes.
     *
     * @return list of node names and types
     */
    public List<LineageRequestNode> getRelatedNodes()
    {
        return relatedNodes;
    }


    /**
     * Set up the list of related nodes.
     *
     * @param relatedNodes list of node names and types
     */
    public void setRelatedNodes(List<LineageRequestNode> relatedNodes)
    {
        this.relatedNodes = relatedNodes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageSearchRequest{" +
                       "queriedNode=" + queriedNode +
                       ", relatedNodes=" + relatedNodes +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof LineageSearchRequest that))
        {
            return false;
        }
        return Objects.equals(queriedNode, that.queriedNode) && Objects.equals(relatedNodes, that.relatedNodes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(queriedNode, relatedNodes);
    }
}
