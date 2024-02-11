/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageSearchRequest
{
    private Node queriedNode = null;
    private List<Node> relatedNodes = null;

    public LineageSearchRequest()
    {
    }


    public LineageSearchRequest(Node queriedNode, List<Node> relatedNodes)
    {
        this.queriedNode  = queriedNode;
        this.relatedNodes = relatedNodes;
    }


    public Node getQueriedNode()
    {
        return queriedNode;
    }

    public void setQueriedNode(Node queriedNode)
    {
        this.queriedNode = queriedNode;
    }

    public List<Node> getRelatedNodes()
    {
        return relatedNodes;
    }

    public void setRelatedNodes(List<Node> relatedNodes)
    {
        this.relatedNodes = relatedNodes;
    }


    @Override
    public String toString()
    {
        return "LineageSearchRequest{" +
                "queriedNode=" + queriedNode +
                ", relatedNodes=" + relatedNodes +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        LineageSearchRequest that = (LineageSearchRequest) objectToCompare;
        return Objects.equals(queriedNode, that.queriedNode) &&
                Objects.equals(relatedNodes, that.relatedNodes);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(queriedNode, relatedNodes);
    }
}
