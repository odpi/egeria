/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules;

import lombok.ToString;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ToString
public class IncompleteRule implements Rule {
    private static final String INCOMPLETE = "Incomplete";
    public static final String TYPE = "type";
    private String edgeType;
    private String destinationNodeType;

    public String getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }

    public String getDestinationNodeType() {
        return destinationNodeType;
    }

    public void setDestinationNodeType(String destinationNodeType) {
        this.destinationNodeType = destinationNodeType;
    }

    /**
     * In order to mark the incomplete assets accordingly, the graph is iterated and based on the existence of a
     * classification the nodes and edges are altered.
     *
     * @param graph           the lineage graph
     * @param queriedNodeGUID the guid of the queried node used to identify it in the graph
     */
    @Override
    public void apply(Graph graph, String queriedNodeGUID) {

        if (edgeType == null) {
            return;
        }

        List<Edge> edgesToRemove = new ArrayList<>();
        List<Node> nodesToRemove = new ArrayList<>();

        List<Node> graphNodes = graph.getNodes();
        List<Edge> edgesWithProperLabel = getEdgesWithProperLabel(graph);

        for (Edge edge : edgesWithProperLabel) {
            for (Node node : graphNodes) {
                if (node.getId().equals(edge.getFrom())) {
                    Optional<Node> classificationNode = getSecondNode(graphNodes, edge);
                    if(classificationNode.isPresent()) {
                        nodesToRemove.add(classificationNode.get());
                        node.getProperties().put(TYPE, INCOMPLETE);
                        edgesToRemove.add(edge);
                    }
                }
            }
        }
        graph.getNodes().removeAll(nodesToRemove);
        graph.getEdges().removeAll(edgesToRemove);
    }

    private List<Edge> getEdgesWithProperLabel(Graph graph) {
        return graph.getEdges().stream().filter(e -> e.getLabel().equals(edgeType)).collect(Collectors.toList());
    }

    private Optional<Node> getSecondNode(List<Node> nodes, Edge edge) {
         return nodes.stream().filter(node -> node.getId().equals(edge.getTo()) && destinationNodeType.equals(node.getGroup())).findAny();
    }
}
