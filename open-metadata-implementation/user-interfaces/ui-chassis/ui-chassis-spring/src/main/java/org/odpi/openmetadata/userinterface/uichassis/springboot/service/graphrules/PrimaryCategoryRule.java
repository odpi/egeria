/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.graphrules;

import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrimaryCategoryRule implements Rule {
    private static final String TERM_CATEGORIZATION = "TermCategorization";
    private static final String REFERENCING_CATEGORY = "ReferencingCategory";
    private static final String PRIMARY_CATEGORY = "PrimaryCategory";
    private String edgeType;
    private String sourceNodeType;
    private String destinationNodeType;
    private String classificationProperty;

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

    public String getClassificationProperty() {
        return classificationProperty;
    }

    public void setClassificationProperty(String classificationProperty) {
        this.classificationProperty = classificationProperty;
    }


    /**
     * In order to mark the primaryCategory accordingly the graph is iterated and based on the existence of a classification the nodes and edges are altered
     *
     * @param graph           the lineage graph
     * @param queriedNodeGUID the guid of the queried node used to identify it in the graph
     */
    @Override
    public void apply(Graph graph, String queriedNodeGUID) {

        List<Node> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();

        List<Edge> edgesToRemove = new ArrayList<>();
        List<Node> nodesToDrop = new ArrayList<>();

        if (classificationProperty == null || edgeType == null) {
            return;
        }

        List<Edge> edgesWithProperLabel = graph.getEdges().stream()
                .filter(e -> e.getLabel().equals(edgeType)).collect(Collectors.toList());

        for (Edge edge : edgesWithProperLabel) {
            String primaryCategoryQualifiedName = null;
            String glossaryTermGUID = null;
            for (Node node : nodes) {
                if (node.getGroup().equals(sourceNodeType) && node.getId().equals(edge.getFrom())) {
                    Node classificationNode = getSecondNode(nodes, edge);
                    if (hasCorrectClassificationProperty(classificationNode)) {
                        primaryCategoryQualifiedName = classificationNode.getProperties().get(classificationProperty);
                        glossaryTermGUID = node.getId();
                        nodesToDrop.add(classificationNode);
                    }
                }
            }

            if (primaryCategoryQualifiedName == null) {
                continue;
            }
            String primaryCategoryGUID = getPrimaryCategoryGUID(nodes, primaryCategoryQualifiedName);
            markRelationships(edges, glossaryTermGUID, primaryCategoryGUID);

            edgesToRemove.add(edge);
        }

        graph.getNodes().removeAll(nodesToDrop);
        graph.getEdges().removeAll(edgesToRemove);
    }


    private Node getSecondNode(List<Node> nodes, Edge edge) {
        for (Node secondNode : nodes) {
            if (secondNode.getId().equals(edge.getTo()) && destinationNodeType.equals(secondNode.getGroup())) {
                return secondNode;
            }
        }
        return null;
    }

    /**
     * Null checks and has the classification property
     *
     * @param node the node that is checked
     * @return true or false
     */
    private boolean hasCorrectClassificationProperty(Node node) {
        return node != null && node.getProperties() != null && node.getProperties().containsKey(classificationProperty);
    }

    /**
     * Get the get the GUID of the node based on it's qualifiedName
     *
     * @param nodes the nodes of the graphg
     * @param primaryCategoryQualifiedName qualified name of the category whos GUID is searched
     * @return the GUID of thr primary category
     */
    private String getPrimaryCategoryGUID(List<Node> nodes, String primaryCategoryQualifiedName) {
        for (Node node : nodes) {
            if (primaryCategoryQualifiedName.equals(node.getQualifiedName())) {
                return node.getId();
            }
        }
        return null;
    }

    /**
     * Marks the relationships between a GlossaryTerm and it's Categories either as REFERENCING_CATEGORY or PRIMARY_CATEGORY
     *
     * @param edges of the graph
     * @param glossaryTermGUID  guid of the GlossaryTerm
     * @param primaryCategoryGUID guid of the primary category
     */
    private void markRelationships(List<Edge> edges, String glossaryTermGUID, String primaryCategoryGUID) {
        edges.stream()
                .filter(edge -> isTermCategorizationAndCorrectNodes(glossaryTermGUID, edge))
                .forEach(edge -> markRelationship(primaryCategoryGUID, edge));
    }

    private void markRelationship(String primaryCategoryGUID, Edge e) {
        if (e.getTo().equals(primaryCategoryGUID) || e.getFrom().equals(primaryCategoryGUID)) {
            e.setType(PRIMARY_CATEGORY);
        } else {
            e.setType(REFERENCING_CATEGORY);
        }
    }

    private boolean isTermCategorizationAndCorrectNodes(String glossaryTermGUID, Edge e) {
        return e.getLabel().equals(TERM_CATEGORIZATION) &&
                (e.getTo().equals(glossaryTermGUID) || e.getFrom().equals(glossaryTermGUID));
    }

    @Override
    public String toString() {
        return "PrimaryCategoryRule{" +
                "edgeType='" + edgeType + '\'' +
                ", sourceNodeType='" + sourceNodeType + '\'' +
                ", destinationNodeType='" + destinationNodeType + '\'' +
                ", classificationProperty='" + classificationProperty + '\'' +
                '}';
    }
}
