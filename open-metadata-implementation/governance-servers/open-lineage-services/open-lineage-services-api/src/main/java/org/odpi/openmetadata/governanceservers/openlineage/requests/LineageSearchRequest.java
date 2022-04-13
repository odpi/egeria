/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode

public class LineageSearchRequest implements Serializable {
    private Node queriedNode;
    private List<Node> relatedNodes;

    public Node getQueriedNode() {
        return queriedNode;
    }

    public void setQueriedNode(Node queriedNode) {
        this.queriedNode = queriedNode;
    }

    public List<Node> getRelatedNodes() {
        return relatedNodes;
    }

    public void setRelatedNodes(List<Node> relatedNodes) {
        this.relatedNodes = relatedNodes;
    }

    @Override
    public String toString() {
        return "LineageSearchRequest{" +
                "queriedNode=" + queriedNode +
                ", relatedNodes=" + relatedNodes +
                '}';
    }
}
