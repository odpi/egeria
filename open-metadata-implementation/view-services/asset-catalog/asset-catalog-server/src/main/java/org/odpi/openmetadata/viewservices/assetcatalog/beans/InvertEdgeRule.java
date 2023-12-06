/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.List;

public class InvertEdgeRule implements Rule
{
    private String edgeType;
    private String sourceNodeType;
    private String destinationNodeType;

    public String getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }

    public String getSourceNodeType() {
        return sourceNodeType;
    }

    public void setSourceNodeType(String sourceNodeType) {
        this.sourceNodeType = sourceNodeType;
    }

    public String getDestinationNodeType() {
        return destinationNodeType;
    }

    public void setDestinationNodeType(String destinationNodeType) {
        this.destinationNodeType = destinationNodeType;
    }


    /** Inverts the edge direction based on the configured parameters
     * @param graph           the lineage graph
     * @param queriedNodeGUID the guid of the queried node used to identify it in the graph
     */
    public void apply(Graph graph, String queriedNodeGUID) {
        for (Edge edge : graph.getEdges()) {
            List<RuleNode> nodes = graph.getNodes();
            if (edgeType != null && edgeType.equals(edge.getLabel())) {
                invertEdge(nodes, edge);
            }
        }
    }

    private void invertEdge(List<RuleNode> nodes, Edge edge) {
        if (this.getSourceNodeType() != null && this.getDestinationNodeType() != null) {
            applyRuleWithBothNodeTypes(nodes, edge);
        } else if (this.getSourceNodeType() != null && this.getDestinationNodeType() == null) {
            applyRuleWithOneNodeType(nodes, edge, edge.getFrom(), this.getSourceNodeType());
        } else if (this.getSourceNodeType() == null && this.getDestinationNodeType() != null) {
            applyRuleWithOneNodeType(nodes, edge, edge.getTo(), this.getDestinationNodeType());
        } else {
            invertEdgeDirection(edge);
        }
    }

    private void applyRuleWithBothNodeTypes(List<RuleNode> nodes, Edge edge) {
        for (RuleNode node : nodes) {
            if (node.getId().equals(edge.getFrom()) && node.getGroup().equals(this.getSourceNodeType())) {
                for (RuleNode secondNode : nodes) {
                    if (secondNode.getId().equals(edge.getTo()) && secondNode.getGroup().equals(this.getDestinationNodeType())) {
                        invertEdgeDirection(edge);
                        break;
                    }
                }
            }
        }
    }

    private void applyRuleWithOneNodeType(List<RuleNode> nodes, Edge edge, String nodeId, String ruleNodeType) {
        boolean findNode = false;
        for (RuleNode node : nodes) {
            if (node.getId().equals(nodeId) && node.getGroup().equals(ruleNodeType)) {
                findNode = true;
            }
            if (findNode) {
                invertEdgeDirection(edge);
            }
        }
    }

    private void invertEdgeDirection(Edge edge) {
        String originalFrom = edge.getFrom();
        String originalTo = edge.getTo();
        edge.setFrom(originalTo);
        edge.setTo(originalFrom);
    }
}
