/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules.Rule;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Rules;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This component matches the properties provided under lineage-display-config which provide rules
 * based on which the edge's direction will be inverted
 */
@Service
@ConfigurationProperties(prefix = "lineage-display-config")
public class LineageGraphDisplayService {

    private Rules rules;

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }


    /**
     * This method applies the rules defined in the properties file on the graph.
     *
     * @param graph           graph processed from the response from open lineage
     * @param queriedNodeGUID the GUID of the queried node
     */
    public void applyRules(Graph graph, String queriedNodeGUID) {
        if (rules == null) {
            return;
        }

        for (Rule rule : rules.getAllRules()) {
            rule.apply(graph, queriedNodeGUID);
        }
    }

    /**
     * sets the level field for the nodes, in order to be displayed on levels
     * Stars from a start list of nodes and sets a the level+1 for the nodes on the ends of "to" edges and
     * legel -1 for the nodes from the "from" end of it's edges
     * once an edge or node is processed is removed from the list.
     *
     * @param startNodes the starting nodes
     * @param listNodes  the list of nodes
     * @param listEdges  the list of edges
     */
    public void setNodesLevel(List<Node> startNodes, List<Node> listNodes, List<Edge> listEdges) {

        ArrayList<Node> newStartNodes = new ArrayList<>();

        ListIterator<Edge> edgeListIterator = listEdges.listIterator();

        while (edgeListIterator.hasNext()) {
            Edge e = edgeListIterator.next();
            for (Node node : startNodes) {
                if (node.getId().equals(e.getFrom())) {

                    listNodes.stream()
                            .filter(n -> n.getLevel() == 0 && n.getId().equals(e.getTo()))
                            .forEach(item -> {
                                item.setLevel(node.getLevel() + 1);
                                newStartNodes.add(item);
                                edgeListIterator.remove();
                            });

                } else if (node.getId().equals(e.getTo())) {

                    listNodes.stream()
                            .filter(n -> n.getLevel() == 0 && n.getId().equals(e.getFrom()))
                            .forEach(item -> {
                                item.setLevel(node.getLevel() - 1);
                                newStartNodes.add(item);
                                edgeListIterator.remove();
                            });

                }
                listNodes.removeAll(newStartNodes);
            }

        }

        if (newStartNodes.size() > 0 && listEdges.size() > 0) {
            setNodesLevel(newStartNodes, listNodes, listEdges);
        }
    }

    @Override
    public String toString() {
        return "LineageGraphDisplayRulesService{" +
                "rules=" + rules +
                '}';
    }
}
