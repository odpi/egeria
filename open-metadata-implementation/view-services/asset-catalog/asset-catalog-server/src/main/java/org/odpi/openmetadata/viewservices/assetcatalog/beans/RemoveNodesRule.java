/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.ArrayList;
import java.util.List;

public class RemoveNodesRule implements Rule
{
    private String queriedNodeType;
    private List<String> nodeTypesToRemove;

    public String getQueriedNodeType() {
        return queriedNodeType;
    }

    public void setQueriedNodeType(String queriedNodeType) {
        this.queriedNodeType = queriedNodeType;
    }

    public List<String> getNodeTypesToRemove() {
        return nodeTypesToRemove;
    }

    public void setNodeTypesToRemove(List<String> nodeTypesToRemove) {
        this.nodeTypesToRemove = nodeTypesToRemove;
    }


    /**
     * If the queried node type matches the rule field defined in properties, the graph will be altered by deleting all the nodes of types nodeTypesToRemove from the graph
     *
     * @param graph           the lineage graph
     * @param queriedNodeGUID the guid of the queried node used to identify it in the graph
     */
    @Override
    public void apply(Graph graph, String queriedNodeGUID) {
        if (queriedNodeType == null || nodeTypesToRemove == null) {
            return;
        }

        if (areSameQueriedNodeTypes(queriedNodeGUID, graph.getNodes())) {
            removeNodesFromGraph(graph.getEdges(), graph.getNodes());
        }

    }

    private boolean areSameQueriedNodeTypes(String guid, List<RuleNode> nodes) {
        return nodes.stream().anyMatch(node -> node.getId().equals(guid) && node.getGroup().equals(queriedNodeType));
    }

    /**
     * Removes the nodes from the graph if they match the rule
     *
     * @param edges the edges of the graph
     * @param nodes nodes of the graph
     */
    private void removeNodesFromGraph(List<Edge> edges, List<RuleNode> nodes) {
        List<Edge>     edgesToRemove = new ArrayList<>();
        List<RuleNode> nodesToRemove = new ArrayList<>();
        for (RuleNode node : nodes) {
            if (nodeTypesToRemove.contains(node.getGroup())) {
                nodesToRemove.add(node);
                String newStartNode = findNewStartNode(edges, edgesToRemove, node);
                List<String> newEndNodes = findNewEndNodes(edges, edgesToRemove, node.getId());
                createNewEdges(edges, newStartNode, newEndNodes);
            }
        }
        nodes.removeAll(nodesToRemove);
        edges.removeAll(edgesToRemove);
    }


    private String findNewStartNode(List<Edge> edges, List<Edge> edgesToRemove, RuleNode node) {
        String newFrom = "";
        for (Edge edge : edges) {
            if (edge.getTo().equals(node.getId())) {
                edgesToRemove.add(edge);
                newFrom = edge.getFrom();
            }
        }
        return newFrom;
    }

    private List<String> findNewEndNodes(List<Edge> edges, List<Edge> edgesToRemove, String nodeId) {
        List<String> newTo = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getFrom().equals(nodeId)) {
                edgesToRemove.add(edge);
                newTo.add(edge.getTo());
            }
        }
        return newTo;
    }

    private void createNewEdges(List<Edge> edges, String newStartNode, List<String> newEndNodes) {
        for (String newEndNode : newEndNodes) {
            if (!newStartNode.equals("")) {
                edges.add(new Edge(newStartNode, newEndNode));
            }
        }
    }
}
