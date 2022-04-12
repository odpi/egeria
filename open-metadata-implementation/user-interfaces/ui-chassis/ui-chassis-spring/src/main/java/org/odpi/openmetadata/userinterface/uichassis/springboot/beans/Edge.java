/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.beans;

import java.util.Objects;

public class Edge {

    private String id;
    private String from;
    private String to;
    private String label;
    private String type;

    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Edge(String id, String from, String to, String label) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(id, edge.id) && Objects.equals(from, edge.from) && Objects.equals(to, edge.to) && Objects.equals(label, edge.label) && Objects.equals(type, edge.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, label, type);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
