/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A graph contain
 * A Node is an entity in the subject area omas that has a type {@code  NodeType}, name, qualified name and description.
 * A node may be in one or more projects.
 * <p>
 * Nodes can be connected with {@code Line }s to form graphs. As they may be visualised, so a node has an associated
 * icon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Graph implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Graph.class);
    private static final String className = Graph.class.getName();

    private Set<Node> nodes = null;
    private Set<Line> lines = null;


    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    public String toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        if (nodes !=null && !nodes.isEmpty()) {
            sb.append("Nodes= [\n");
            for (Node node:nodes) {
               node.toString(sb);
            }
            sb.append("Nodes= ]\n");
        }
        if (lines !=null && !lines.isEmpty()) {
            sb.append("Lines= [\n");
            for (Line line:lines) {
               line.toString(sb);
            }
            sb.append("Lines= ]\n");
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
        return Objects.equals(nodes, graph.nodes) &&
                Objects.equals(lines, graph.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, lines);
    }
}