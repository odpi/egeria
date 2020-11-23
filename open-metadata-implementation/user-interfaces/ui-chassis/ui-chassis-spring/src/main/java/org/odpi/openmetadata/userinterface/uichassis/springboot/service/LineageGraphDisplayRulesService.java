/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Rule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This component matches the properties provided under lineage-display-config which provide rules
 * based on which the edge's direction will be inverted
 */
@Service
@ConfigurationProperties(prefix = "lineage-display-config")
public class LineageGraphDisplayRulesService {

    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }


    /**
     * This method applies the rules defined in the properties file on the graph.
     *
     * @param graph graph processed from the response from open lineage
     */
    public void applyRules(Graph graph) {
        List<Edge> edges = graph.getEdges();
        List<Node> nodes = graph.getNodes();

        if (rules == null || rules.isEmpty()) {
            return;
        }

        for (Edge edge : edges) {
            for (Rule rule : rules) {
                if (rule.getEdgeType() != null && rule.getEdgeType().equals(edge.getLabel())) {
                    applyRule(nodes, edge, rule);
                }
            }
        }
    }

    private void applyRule(List<Node> nodes, Edge edge, Rule rule) {
        if (rule.getSourceNodeType() != null && rule.getDestinationNodeType() != null) {
            applyRuleWithBothNodeTypes(nodes, edge, rule);
        } else if (rule.getSourceNodeType() != null && rule.getDestinationNodeType() == null) {
            applyRuleWithOneNodeType(nodes, edge, edge.getFrom(), rule.getSourceNodeType());
        } else if (rule.getSourceNodeType() == null && rule.getDestinationNodeType() != null) {
            applyRuleWithOneNodeType(nodes, edge, edge.getTo(), rule.getDestinationNodeType());
        } else {
            invertEdgeDirection(edge);
        }
    }

    private void applyRuleWithBothNodeTypes(List<Node> nodes, Edge edge, Rule rule) {
        boolean findSource = false;
        boolean findDestination = false;
        for (Node node : nodes) {
            if (node.getId().equals(edge.getFrom()) && node.getGroup().equals(rule.getSourceNodeType())) {
                findSource = true;
            }

            if (node.getId().equals(edge.getTo()) && node.getGroup().equals(rule.getDestinationNodeType())) {
                findDestination = true;
            }
            if (findSource && findDestination) {
                invertEdgeDirection(edge);
            }
        }
    }

    private void applyRuleWithOneNodeType(List<Node> nodes, Edge edge, String nodeId, String ruleNodeType) {
        boolean findNode = false;
        for (Node node : nodes) {
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

    @Override
    public String toString() {
        return "LineageGraphDisplayRulesService{" +
                "rules=" + rules +
                '}';
    }
}
