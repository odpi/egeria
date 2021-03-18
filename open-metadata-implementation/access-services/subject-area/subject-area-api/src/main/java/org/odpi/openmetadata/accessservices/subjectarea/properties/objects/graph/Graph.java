/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A graph contain
 * A Node is a Glossary Artifact in the subject area omas that has a type {@code  NodeType}, name, qualified name and description.
 * A node may be in one or more projects.
 * <p>
 * Nodes can be connected with {@code Line }s to form graphs. As they may be visualised, so a node has an associated
 * icon.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Graph implements Serializable {
    private Map<String, Node> nodes = null;
    private Map<String, Line> lines = null;
    private String rootNodeGuid = null;
    private String nodeFilter = null;
    private String lineFilter = null;
    /**
     * Get the nodes that are in the neighbourhood of the root node
     *
     * @return the nodes
     */
    public Map<String, Node> getNodes() {
        return nodes;
    }

    /**
     * Set the nodes that are in the neighbourhood of the root node
     *
     * @param nodes supply the nodes associated with the root node
     */
    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Get the lines that are in the neighbourhood of the root node
     *
     * @return the lines
     */
    public Map<String, Line> getLines() {
        return lines;
    }

    /**
     * Set the lines that are in the neighbourhood of the root node
     *
     * @param lines supply the nodes associated with the root node
     */
    public void setLines(Map<String, Line> lines) {
        this.lines = lines;
    }

    /**
     *
     * @return the root node guid
     */
    public String getRootNodeGuid() {
        return rootNodeGuid;
    }

    /**
     * the root node's guid.
     *
     * @param rootNodeGuid the guid of the root node
     */
    public void setRootNodeGuid(String rootNodeGuid) {
        this.rootNodeGuid = rootNodeGuid;
    }

    /**
     * Get the node type filters as a comma separated string
     * @return string of node types
     */
    public String getNodeFilter() {
        return nodeFilter;
    }

    /**
     * Set the node type filters as a comma separated string
     * @param nodeFilter the node type filters
     */
    public void setNodeFilter(String nodeFilter) {
        this.nodeFilter = nodeFilter;
    }

    /**
     * Get the line type filters as a comma separated string
     * @return string of line types
     */
    public String getLineFilter() {
        return lineFilter;
    }

    /**
     * Set the line type filters as a comma separated string
     * @param lineFilter the node type filters
     */
    public void setLineFilter(String lineFilter) {
        this.lineFilter = lineFilter;
    }

    public String toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("NodeGUID=" + rootNodeGuid + "\n");
        sb.append(("NodeTypeFilter=" + nodeFilter == null || nodeFilter == "") ? "none" : nodeFilter + "\n");
        sb.append(("LineTypeFilter=" + lineFilter == null || lineFilter == "") ? "none" : lineFilter + "\n");
        if (nodes != null && !nodes.isEmpty()) {

            sb.append("Nodes= [\n");
            Set<String> guids = nodes.keySet();
            for (String guid : guids) {
                nodes.get(guid).toString(sb);
            }
            sb.append("]\n");
        }
        if (lines != null && !lines.isEmpty()) {
            sb.append("Lines= [\n");
            Set<String> guids = lines.keySet();
            for (String guid : guids) {
                lines.get(guid).toString(sb);
            }
            sb.append("]\n");
        }

        sb.append('}');

        return sb.toString();
    }


    @Override
    public String toString() {
        return toString(new StringBuilder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return Objects.equals(rootNodeGuid, graph.getRootNodeGuid()) && Objects.equals(nodes, graph.nodes) &&
                Objects.equals(lines, graph.lines) &&  Objects.equals(nodeFilter, graph.nodeFilter) &&
                Objects.equals(lineFilter, graph.lineFilter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootNodeGuid, nodes, lines, nodeFilter, lineFilter);
    }
}